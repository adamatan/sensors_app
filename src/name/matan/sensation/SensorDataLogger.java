package name.matan.sensation;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import android.content.Context;

/**
 * Stores the data logged for a specific sensor.
 * @author adamatan
 * @param <T>
 */

public abstract class SensorDataLogger<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5071736338480679823L;
	private final int sensorId;
	private Map<Date, T> data 	 = new TreeMap<Date, T>();
	private Map<Date, T> oldData = new TreeMap<Date, T>();
	private volatile T lastValue;
	private volatile Date lastDate;
	
	public SensorDataLogger(int sensorId, Context context) {
		this.sensorId=sensorId;
	}
	
	public List<String> getLogLinesForFile() {
		List<String> result = new ArrayList<String>();
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HHmm-ss");
		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		for (Date date : data.keySet()) {
			String timestamp = formatter.format(date);
			result.add(String.format("%s %02d %s\n", 
					timestamp, sensorId, this.data.get(date)));
		}
		oldData.putAll(data);
		data.clear();
		return result;
	}
	
	public void logData() {
		Date now = new Date();
		this.lastDate=now;
		T value = this.getCurrentReading();
		this.lastValue=value;
		data.put(now, value);
	}
	
	/**
	 * Returns the current data from the sensor.
	 */
	public abstract T getCurrentReading();

	/**
	 * @return All values - new and uploaded.
	 */
	public ArrayList<T> getAllValues() {
		ArrayList<T> result = new ArrayList<T>();
		TreeMap<Date, T> resultMap = new TreeMap<Date, T>(oldData);
		resultMap.putAll(data);
		result.addAll(resultMap.values());
		return result;
	}
	
	public T getLastValue() {
		return this.lastValue;
	}
	
	public Date getLastSampleDate() {
		return this.lastDate;
	}
}
