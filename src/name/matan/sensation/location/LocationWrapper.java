package name.matan.sensation.location;

import java.io.Serializable;

public class LocationWrapper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1121037904055934073L;
	private double lat;
	private double lon;

	public LocationWrapper(double lat, double lon) {
		super();
		this.lat = lat;
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}
	
	@Override
	public String toString() {
		return String.format("Lat=%8.5f, Lon=%8.5f", this.lat, this.lon);
	}
}
