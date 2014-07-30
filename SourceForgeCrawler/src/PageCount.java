import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.StringTokenizer;


public class PageCount implements Constants{

	public static void initializePageCount(String language) {

		try{
			URL parentURL = new URL(PARENT_URL.replace("$", language));
			System.out.println("Processing Page-Count on Page : "+PARENT_URL.replace("$", language));
			BufferedReader inStream = new BufferedReader(new InputStreamReader(parentURL.openStream()));
			String inputLine;
			while ((inputLine = inStream.readLine()) != null) {
				// Process each line.
				if (inputLine.contains("Showing page 1 of ")) {
					StringTokenizer str = new StringTokenizer(inputLine.trim().replace(".", ""), " ");
					String countVal="0";
					while (str.hasMoreTokens()) {
						countVal = str.nextToken();
					}
					MainHandler.totalPagesCount = Integer.valueOf(countVal).intValue();
				}
			}
			inStream.close();

		}catch(Exception e){
			e.printStackTrace();
		}
	}
}