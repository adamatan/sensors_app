package name.matan.sensation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

/**
 * Stores the data logged for a specific sensor.
 * @author adamatan
 * @param <T>
 */

public abstract class SensorDataLogger<T> {

	private final int sensorId;
	private Map<Date, T> data 	 = new TreeMap<Date, T>();
	private Map<Date, T> oldData = new TreeMap<Date, T>();
	
	public SensorDataLogger(int sensorId) {
		this.sensorId=sensorId;
	}
	
	public TreeMap<Date, String> getLogLinesForFile() {
		TreeMap<Date, String> result = new TreeMap<Date, String>();
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HHmm-ss");
		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		for (Date date : data.keySet()) {
			String timestamp = formatter.format(date);
			result.put(date, String.format("%s %d %s", 
					timestamp, sensorId, this.data.get(date)));
		}
		oldData.putAll(data);
		data.clear();
		return result;
	}
	
	public void putData(String dataString, T rawData) {
		Date now = new Date();
		data.put(now, rawData);
	}

	public List<T> getAllValues() {
		List<T> result = new ArrayList<T>();
		TreeMap<Date, T> resultMap = new TreeMap<Date, T>(oldData);
		resultMap.putAll(data);
		result.addAll(resultMap.values());
		return result;
	}
}
