package name.matan.sensation;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class CellularLocationLogActivity extends Activity {

	private TextView lat;
	private TextView lon;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cellular_location_log);
        setTitle("Cellular Location log");
        this.lat = (TextView) findViewById(R.id.cellLocationLatText);
        this.lon = (TextView) findViewById(R.id.cellLocationLonText);
        getCellLocation();
    }

	private void getCellLocation() {
		    LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		    LocationListener locationListener = new LocationListener() {
		        public void onLocationChanged(Location location) {
		        	if (location!=null) {
		        		lat.setText(String.format("%7.5f", location.getLatitude()));
		        		lon.setText(String.format("%7.5f", location.getLongitude()));
		        	}
		        }

		        public void onStatusChanged(String provider, int status, Bundle extras) {}
		        public void onProviderEnabled(String provider) {}
		        public void onProviderDisabled(String provider) {}
		      };
		    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_cellular_location_log, menu);
        return true;
    }
}
