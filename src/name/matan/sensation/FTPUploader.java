package name.matan.sensation;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.net.InetAddress;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import android.content.Context;
import android.util.Log;

public class FTPUploader extends Thread {

	private String filename;
	private Context context;

	public FTPUploader(Context applicationContext, String filename) {
		this.filename=filename;
		this.context=applicationContext;
	}

	@Override
	public void run() {
		FTPClient ftpClient = new FTPClient();
		try {
			Log.i("FTPUploader", String.format("Uploading %s to ftp", 
					this.filename));
			ftpClient.connect(InetAddress.getByName("192.117.150.233"));
			ftpClient.login("anonymous", "");
			ftpClient.changeWorkingDirectory("/home/adamatan");
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			BufferedInputStream buffIn=null;
			FileInputStream file = context.openFileInput(filename); //new File(Environment.getExternalStorageDirectory(), filename);
			buffIn=new BufferedInputStream(file);
			ftpClient.enterLocalPassiveMode();
			ftpClient.storeFile(filename, buffIn);
			buffIn.close();
			ftpClient.logout();
			ftpClient.disconnect();
			Log.i("FTPUploader", "Completed without errors.");
		} catch (Exception e) {
			Log.e("FTPUploader", "Could not upload file to ftp:\n"+e.toString());
		}
	}

}
