package name.matan.sensation;

import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.util.Log;

public class FileWriter {

	private String filename;
	private Context context;

	/**
	 * Initialize a FileWriter. No file open action is taken until write() is invoked.
	 * @param applicationContext The right context (i.e. application internal storage space) where the file
	 * will be written. The usual location is /data/data/APP_NAME/files, e.g. 
	 * /data/data/name.matan.sensation/files (root permission required to access this directory).
	 * @param filename 
	 */
	public FileWriter(Context applicationContext, String filename) {
		this.filename=filename;
		this.context=applicationContext;
		Log.i("FilwWriter", String.format("Initialized with \"%s\"", filename));
	}
	
	/**
	 * Writes data to file, flushes and closes.
	 * @param contents Content to write to file.
	 * @return true if writing was successful, false otherwise.
	 */
	public boolean write(String contents){		
		byte[] data = contents.getBytes();
		try {
			FileOutputStream fos = context.openFileOutput(this.filename, Context.MODE_PRIVATE);
			fos.write(data);
			fos.flush();
			fos.close();
			return true;
		} catch (IOException e) {
			return false;
		}

	}

}
