import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;


public class FileDownloader {

	public static void  initiateDownload(String downloadUrl, String destinationDir)  throws Exception{

		URL Url = new URL(downloadUrl);
		String fileName=downloadUrl.substring(downloadUrl.lastIndexOf('/') + 1);

		File downloadLocation = new File(destinationDir);
		// if file doesn't exists, then create it
		if (!downloadLocation.exists()) {
			downloadLocation.mkdirs();
		}

		ReadableByteChannel rbc = Channels.newChannel(Url.openStream());
		FileOutputStream fos = new FileOutputStream(destinationDir+"/"+fileName);
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		fos.close();
		rbc.close();
	}
}
