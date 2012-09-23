package name.matan.sensation.location;

public class LocationWrapper {

	private double lat;
	private double lon;
	
	public LocationWrapper(double lat, double lon) {
		super();
		this.lat = lat;
		this.lon = lon;
	}
	
	@Override
	public String toString() {
		return String.format("Lat=%8.5f, Lon=%8.5f", this.lat, this.lon);
	}
}
