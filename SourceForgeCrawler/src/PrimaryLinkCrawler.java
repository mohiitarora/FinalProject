import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;

public class PrimaryLinkCrawler implements Constants, Callable<Integer>{

	private final String url;
	private final int pageNumber;

	PrimaryLinkCrawler(String countUntil, int pageNumber) {
		this.url = countUntil;
		this.pageNumber = pageNumber;
	}

	@Override
	public Integer call() throws InterruptedException, IOException {

		try{
			URL parentURL = new URL(url);
			BufferedReader inStream = new BufferedReader(new InputStreamReader(parentURL.openStream()));
			String inputLine;
			while ((inputLine = inStream.readLine()) != null) {
				// Process each line.
				if (inputLine.contains("<a href=") && inputLine.contains(Constants.END_SEARCH_PARAMETER)) {
					StringTokenizer tokeize = new StringTokenizer(inputLine.trim(),"<");
					while(tokeize.hasMoreTokens()){
						String tokenizedData = tokeize.nextToken();
						if(tokenizedData.contains("href")){
							StringTokenizer innerTokenizer = new StringTokenizer(tokenizedData,"\"");
							while(innerTokenizer.hasMoreTokens()){
								String innerTokenizedData = innerTokenizer.nextToken();
								if(innerTokenizedData.contains("=directory")){
									MainHandler.listOfParentUrls.add(HIGH_LEVEL_LINK+innerTokenizedData);
								}
							}
						}
					}
				}
			}
			inStream.close();
		}catch(Exception e){
			e.printStackTrace();
			MainHandler.retryURLs.add(url.trim());
			return FAILURE;
		}

		if(MainHandler.retryURLs.contains(url.trim())){
			MainHandler.retryURLs.remove(url);
		}
		return SUCCESS;
	}
}