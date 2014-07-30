import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;

import sun.net.www.protocol.http.HttpURLConnection;


public class DownloadLinkCrawler implements Constants, Callable<Integer>{

	private final String url;
	String language;
	DownloadLinkCrawler(String countUntil,String language) {
		this.url = countUntil;
		this.language = language;
	}

	@Override
	public Integer call() throws Exception {
		try{
			MainHandler.writer.writeOutputToFile("COUNTER FOR PROGRAM :"+(++MainHandler.programCounter));
			URL parentURL = new URL(url);
			BufferedReader inStream = new BufferedReader(new InputStreamReader(parentURL.openStream()));
			String inputLine;
			while ((inputLine = inStream.readLine()) != null) {
				if (inputLine.contains("<a href=") && inputLine.contains(Constants.DOWNLOAD_PRIMARY_LINK_PATTERN)) {
					StringTokenizer tokeize = new StringTokenizer(inputLine.trim(),"<");
					while(tokeize.hasMoreTokens()){
						String tokenizedData = tokeize.nextToken();
						if(tokenizedData.contains("href")){
							StringTokenizer innerTokenizer = new StringTokenizer(tokenizedData,"\"");
							while(innerTokenizer.hasMoreTokens()){
								String innerTokenizedData = innerTokenizer.nextToken();
								if(innerTokenizedData.contains("/projects") && innerTokenizedData.contains("download")){
									extractDownloadURL(HIGH_LEVEL_LINK+innerTokenizedData);
									MainHandler.listOfParentUrls.remove(url);
									return SUCCESS;
								}
							}
						}
					}
				}
			}
			inStream.close();
		}catch(Exception e){
			MainHandler.retryDownloadLinks.add(url);
			return FAILURE;
		}

		return SUCCESS;
	}

	public void extractDownloadURL(String url) throws Exception {
		try{
			URL childUrl = new URL(url);
			HttpURLConnection urlConn = (HttpURLConnection) childUrl.openConnection();
			urlConn.connect();
			urlConn.getInputStream();
			String downloadLink = urlConn.toString().replace("sun.net.www.protocol.http.HttpURLConnection:", "");



			URL Url = new URL(downloadLink);
			String fileName=downloadLink.substring(downloadLink.lastIndexOf('/') + 1);

			if(!MainHandler.alreadyDownloaded.contains(fileName)){
				MainHandler.downloadQueue.add(fileName);

				ReadableByteChannel rbc = Channels.newChannel(Url.openStream());
				FileOutputStream fos = new FileOutputStream(FileWriter.downloadLocation+"/STAGING/"+fileName);
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				fos.close();
				rbc.close();

				MainHandler.writer.writeOutputToFile("Successfully downloaded File: "+fileName);
				MainHandler.downloadQueue.remove(fileName);
				MainHandler.downloadLinks.add(downloadLink);

				MainHandler.writer.writeOutputToFile(MainHandler.downloadQueue.size()+" AND QUEUE :"+MainHandler.downloadQueue);
			}else{
				MainHandler.writer.writeOutputToFile("Already Downloaded File. Skipping File: "+fileName);
			}

		}catch(Exception e){
			MainHandler.writer.writeOutputToFile("Skipping Link. Broken Link found : "+url);
		}
	}
}