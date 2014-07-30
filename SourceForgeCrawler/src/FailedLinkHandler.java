import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class FailedLinkHandler {

	private final ExecutorService executor = Executors.newFixedThreadPool(100);

	public void processFailedURLs() throws Exception{
		int i=1;
		while(MainHandler.retryURLs.size()>0){
			i = i>5?1:i;
			ProxyHolder.switchProxySettings(i++);

			List<Future<Integer>> list = new ArrayList<Future<Integer>>();
			for(String link : MainHandler.retryURLs){
				Callable<Integer> loopWorker = new PrimaryLinkCrawler(link,0);
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
		MainHandler.retryURLs.clear();
	}

	public void retryDownloadLinks(String language) throws Exception{

		while(MainHandler.downloadLinks.size()>0){

			List<Future<Integer>> list = new ArrayList<Future<Integer>>();
			for(String parentURL : MainHandler.downloadLinks){
				Callable<Integer> childCrawler = new DownloadLinkCrawler(parentURL,language);
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
}
