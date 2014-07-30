import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.io.FileUtils;

/**
 * HIGH LEVEL ALGORITHM/DESIGN
 * 
 * STEP 1: GATHER ALL THE *PAGE* LEVEL LINKS IN ORDER TO ITERATE OVER THE PAGES.
 * STEP 2: GATHER ALL THE *DOWNLOAD* LEVEL LINKS WITHIN THE PAGES TO ITERATE OVER TO IN ORDER TO CREATE LIST OF DOWNLOAD LINKS.
 * STEP 3: ONCE ALL DOWNLOAD
 * LINKS ARE COLLECTED FROM STEP 2, START DOWNLOADING THE DATA USING THE
 * DOWNLOADING LINKS.
 * 
 */

public class MainHandler implements Constants {

	// Primary links related data structures
	public static HashSet<String> listOfParentUrls = new HashSet<String>();
	public static HashSet<String> retryURLs = new HashSet<String>();

	// Download Links related data structures
	public static HashSet<String> retryDownloadLinks = new HashSet<String>();
	public static HashSet<String> downloadLinks = new HashSet<String>();
	public static HashSet<String> removalList = new HashSet<String>();
	public static HashSet<String> alreadyDownloaded = new HashSet<String>();
	private final ExecutorService executor = Executors.newFixedThreadPool(50);
	private String[] secondaryList;
	private static String languageExecutor = "";
	public static int totalPagesCount = 0;
	public static final FileWriter writer = new FileWriter();
	public static ArrayList<String> downloadQueue = new ArrayList<String>();
	public static int programCounter = 0;
	//
	private static final FailedLinkHandler failsHandler = new FailedLinkHandler();

	public MainHandler() throws Exception {
		try {

			//comment out next line for actual download
			ProxyHolder.setProxySettings();
			writer.init(languageExecutor);

			File downloadLocation = new File(FileWriter.downloadLocation+"/STAGING/");
			// if file doesn't exists, then create it
			if (!downloadLocation.exists()) {
				downloadLocation.mkdirs();
			}

			downloadLocation = new File(FileWriter.downloadLocation+languageExecutor);
			// if file doesn't exists, then create it
			if (!downloadLocation.exists()) {
				downloadLocation.mkdirs();
			}

		} catch (Exception e) {
			writer.writeOutputToFile("Unable to initiate Page Level Count for Language: "+languageExecutor);
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {

		languageExecutor = args[0].toString().trim();
		MainHandler handler = new MainHandler();

		// If primary Links file Exists Skip and move to Downloads Links Extraction Logic
		if(!writer.checkIfPrimaryLinksExist(languageExecutor)){
			handler.handlePrimaryLinks();
		}

		//If download Links file Exists Skip and move to Downloads  Logic
		/*
		writer.readExistingDownloadedFiles(new File(FileWriter.file.getAbsolutePath()+"/"+languageExecutor));
		handler.handleDownloadLinks();
		writer.writeOutputToFile("Retrying Failed Download Links.");
		failsHandler.retryDownloadLinks(languageExecutor);
		// Write primary links to File so as to not parse them in rerun.
		FileWriter.writePrimaryLinksToFile(new File(FileWriter.file.getAbsolutePath()+"/"+languageExecutor+"_DOWNLOADS.txt"), downloadLinks);
		 */

		// close executor after Threads have completed.
		handler.executor.shutdown();
	}

	private void handleDownloadLinks() throws Exception{
		secondaryList = new String [listOfParentUrls.size()];
		secondaryList = listOfParentUrls.toArray(secondaryList);
		writer.writeOutputToFile("Projects Download Links to be Extracted : "+secondaryList.length);

		// handle parent level links IN BATCH OF 500.
		int start = 1, end = (listOfParentUrls.size()-250>=0)?250:listOfParentUrls.size(), counter = listOfParentUrls.size();
		while(counter>0){
			writer.writeOutputToFile("RUNNING DOWNLOAD LINK SCAN FOR PAGES :"+start+" TO "+end);
			childLevelCrawler(start,end);
			listOfParentUrls.removeAll(removalList);
			removalList.clear();
			counter = counter-250;
			start = end+1;
			end = end + (((listOfParentUrls.size()-end)>250)?250:listOfParentUrls.size()-end);
			moveFileToDownloadFolder();
			FileWriter.writePrimaryLinksToFile(new File(FileWriter.file.getAbsolutePath()+"/"+languageExecutor+"_PRIMARY_LINKS.txt"), listOfParentUrls);
			writer.writeOutputToFile("Count Left to be Processsed: "+listOfParentUrls.size());
			System.exit(0);
		}
	}

	public void moveFileToDownloadFolder(){

		File source = new File(FileWriter.downloadLocation+"/STAGING/");
		File desc = new File(FileWriter.downloadLocation+languageExecutor+"/");
		try {
			FileUtils.copyDirectory(source, desc);
			FileUtils.deleteQuietly(source);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	private void handlePrimaryLinks() throws Exception{

		PageCount.initializePageCount(languageExecutor);
		writer.writeOutputToFile(languageExecutor.toUpperCase()+" TOTAL PAGES  COUNT : "+ MainHandler.totalPagesCount);

		// handle parent level links IN BATCH OF 500.
		int start = 1, end = (totalPagesCount-500>=0)?500:totalPagesCount, counter = totalPagesCount;
		int i=1;
		while(counter>0){
			writer.writeOutputToFile("RUNNING URL SCAN FOR PAGES :"+start+" TO "+end);
			parentLevelCrawler(start,end);
			counter = counter-500;
			start = end+1;
			end = end + (((totalPagesCount-end)>500)?500:totalPagesCount-end);
			writer.writeOutputToFile("Parent Level Failed Page Count : "+retryURLs.size());
			i = i>5?1:i;
			ProxyHolder.switchProxySettings(i++);
		}

		failsHandler.processFailedURLs();

		writer.writeOutputToFile("Parent Level Crawler found Total Projects : "+listOfParentUrls.size());

		// Write primary links to File so as to not parse them in rerun.
		FileWriter.writePrimaryLinksToFile(new File(FileWriter.file.getAbsolutePath()+"/"+languageExecutor+"_PRIMARY_LINKS.txt"), listOfParentUrls);
	}

	/**
	 * 
	 * @param pageStart
	 * @param pageEnd
	 */
	private void parentLevelCrawler(int pageStart, int pageEnd){

		List<Future<Integer>> list = new ArrayList<Future<Integer>>();

		for(int pageNumber=pageStart;pageNumber<=pageEnd;pageNumber++){
			String pageURL = PARTIAL_SEED_URL+PAGE_LEVEL_LINK.replace("$", languageExecutor)+pageNumber;
			Callable<Integer> loopWorker = new PrimaryLinkCrawler(pageURL,pageNumber);
			Future<Integer> submit =  executor.submit(loopWorker);
			list.add(submit);
		}

		for (Future<Integer> future : list) {
			try {
				future.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

	private void childLevelCrawler(int pageStart, int pageEnd) throws Exception{
		List<Future<Integer>> list = new ArrayList<Future<Integer>>();
		for(int i=pageStart;i<=pageEnd;i++){
			removalList.add(secondaryList[i-1]);
			Callable<Integer> childCrawler = new DownloadLinkCrawler(secondaryList[i-1],languageExecutor);
			Future<Integer> submit =  executor.submit(childCrawler);
			list.add(submit);
		}

		for (Future<Integer> future : list) {
			try {
				future.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}
}
