package name.matan.sensation;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class CellularLocationLogActivity extends MapActivity {

	private TextView latTextView;
	private TextView lonTextView;
	private MapView mapView;
	private MapController mapController;
	private LocationMapOverlays itemizedoverlay;
	private List<Overlay> mapOverlays;
	private Drawable drawable;
	private TextView numberOfSamplesText;
	private int numberOfSamepls;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cellular_location_log);
        setTitle("Cellular Location log");
        setElementsById();
        
        mapOverlays = mapView.getOverlays();
        drawable = this.getResources().getDrawable(R.drawable.pin_app);
        itemizedoverlay = new LocationMapOverlays(drawable, this);
        
        getCellLocation();
    }

    /**
     * Sets UI elements in private members.
     */
	private void setElementsById() {
		this.latTextView = (TextView) findViewById(R.id.cellLocationLatText);
        this.lonTextView = (TextView) findViewById(R.id.cellLocationLonText);
        this.mapView = (MapView) findViewById(R.id.mapview);
        this.mapController = mapView.getController();
        mapView.setBuiltInZoomControls(true);
        mapView.setSatellite(true);
        this.numberOfSamepls = 0;  
        this.numberOfSamplesText = (TextView) findViewById(R.id.cellLocationNumberOfSamples);
	}

	private void getCellLocation() {
		    LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		    LocationListener locationListener = new LocationListener() {
		        public void onLocationChanged(Location location) {
		        	if (location!=null) {
		        		double latitude = location.getLatitude();
		        		double longitude = location.getLongitude();
		        		int latitudeE6 	= (int) (1e6 * latitude);
		        		int longitudeE6 = (int) (1e6 * longitude);
		        		GeoPoint point = new GeoPoint(latitudeE6, longitudeE6);
		        		
						latTextView.setText(String.format("%7.5f", latitude));
						lonTextView.setText(String.format("%7.5f", longitude));
						mapController.setCenter(point);
						mapController.setZoom(15);
						
						OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");
						itemizedoverlay.addOverlay(overlayitem);
						mapOverlays.add(itemizedoverlay);
						
						numberOfSamepls+=1;
						numberOfSamplesText.setText(Integer.toString(numberOfSamepls));
		        	}
		        }

		        public void onStatusChanged(String provider, int status, Bundle extras) {}
		        public void onProviderEnabled(String provider) {}
		        public void onProviderDisabled(String provider) {}
		      };
		    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putInt("numberOfSamples", this.numberOfSamepls);
		savedInstanceState.putString("NumberOfSamplesText", this.numberOfSamplesText.getText().toString());
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	  super.onRestoreInstanceState(savedInstanceState);
	  this.numberOfSamepls=savedInstanceState.getInt("numberOfSamples");
	  this.numberOfSamplesText.setText(savedInstanceState.getString("NumberOfSamplesText"));
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_cellular_location_log, menu);
        return true;
    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}
