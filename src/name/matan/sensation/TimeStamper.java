package name.matan.sensation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeStamper {

	public static String format(Date date, String format) {
		DateFormat formatter = new SimpleDateFormat(format);
		return(formatter.format(date));
	}
	
	public static String formatNow(String format) {
		return format(new Date(), format);
	}
}
