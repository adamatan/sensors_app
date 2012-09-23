package name.matan.sensation.location;

import java.io.Serializable;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import name.matan.sensation.SensorDataLogger;

public class LocationDataLogger extends SensorDataLogger<LocationWrapper> implements
Serializable {

	private static final long serialVersionUID = 4273157181210391709L;
	private double currentLat=0;
	private double currentLon=0;

	public LocationDataLogger(int sensorId, Context context) {
		super(sensorId, context);
		Log.e("LDL", "Called");
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				Log.e("LDL", "Updated");
				if (location!=null) {
					currentLat = location.getLatitude();
					currentLon = location.getLongitude();
				}
			}
			public void onStatusChanged(String provider, int status, Bundle extras) {}
			public void onProviderEnabled(String provider) {}
			public void onProviderDisabled(String provider) {}
		};
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
	}

	@Override
	public LocationWrapper getCurrentReading() {
		LocationWrapper lw = new LocationWrapper(this.currentLat, this.currentLon);
		Log.e("LDL", lw.toString());
		return lw;
	}

}
