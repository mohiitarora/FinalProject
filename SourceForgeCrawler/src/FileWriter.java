import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Date;
import java.util.HashSet;


public class FileWriter {

	public static final String downloadLocation = "/Users/mohitarora/Downloads/DATA/";///home/appminer/DATA/  /Users/mohitarora/Downloads/DATA/
	public static final File file = new File(downloadLocation);
	public BufferedOutputStream fileWriter;

	public void init(String languageExecutor) throws Exception{
		// if file doesn't exists, then create it
		if (!file.exists()) {
			file.mkdirs();
		}
		fileWriter = new BufferedOutputStream(new FileOutputStream(new File(file.getAbsolutePath()+"/"+languageExecutor+"_OUTPUT.txt")));
	}

	public static void writePrimaryLinksToFile(File file, HashSet<String> listOfParentUrls) throws Exception{

		BufferedOutputStream parentFileOut = new BufferedOutputStream(new FileOutputStream(file));

		for(String data : listOfParentUrls){
			parentFileOut.write((data+"\n").getBytes());
		}
		parentFileOut.write("<<EOD>>".getBytes());
		parentFileOut.close();

	}

	public static void writeInterimLinksToFile(File file, HashSet<String> listOfParentUrls) throws Exception{

		BufferedOutputStream parentFileOut = new BufferedOutputStream(new FileOutputStream(file,true));

		for(String data : listOfParentUrls){
			parentFileOut.write((data+"\n").getBytes());
		}
		parentFileOut.close();

	}

	public static void writeDownloadsToFile(File file, HashSet<String> urlList) throws Exception{

		BufferedOutputStream parentFileOut = new BufferedOutputStream(new FileOutputStream(file));

		for(String data : urlList){
			parentFileOut.write((data+"\n").getBytes());
		}
		parentFileOut.close();

	}

	public void writeOutputToFile(String output) throws Exception{
		System.out.println((new Date())+" : LOG: "+output);
		fileWriter.write(output.getBytes());
	}

	/**
	 * Check if the Primary Links File Exists. If so, Load the download links to HashSet
	 * @param languageExecutor
	 * @return
	 * @throws Exception
	 */

	boolean checkIfPrimaryLinksExist(String languageExecutor) throws Exception{
		boolean exists = Boolean.FALSE;
		File primaryLinks = new File(FileWriter.file.getAbsolutePath()+"/"+languageExecutor+"_PRIMARY_LINKS.txt");
		if(primaryLinks.exists()){
			writeOutputToFile("Primary Links File Found. Loading Links.");
			exists = true;
			BufferedReader br = new BufferedReader(new FileReader(primaryLinks));
			String line;
			while ((line = br.readLine()) != null) {
				MainHandler.listOfParentUrls.add(line);
			}
			MainHandler.listOfParentUrls.remove("<<EOD>>");
			br.close();
			writeOutputToFile("Primary Links loaded. Count: "+MainHandler.listOfParentUrls.size());
		}
		return exists;
	}

	void readExistingDownloadedFiles(final File folder) {

		for (final File fileEntry : folder.listFiles()) {
			MainHandler.alreadyDownloaded.add(fileEntry.getName());
		}
	}

}
