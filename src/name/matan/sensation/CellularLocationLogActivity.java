package name.matan.sensation;

import java.util.List;

import name.matan.sensation.location.LocationDataLogger;
import name.matan.sensation.location.LocationWrapper;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class CellularLocationLogActivity extends MapActivity {

	private MapView mapView;
	private MapController mapController;
	private LocationMapOverlays itemizedoverlay;
	private List<Overlay> mapOverlays;
	private Drawable drawable;
	
	private TextView lastSampleTime;
	private TextView lastSampleValue;
	private TextView numberOfReadings;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cellular_location_log);
		setTitle("Cellular Location log");
		setElementsById();
		getCellLocation();
	}

	/**
	 * Sets UI elements in private members.
	 */
	private void setElementsById() {
		this.mapView = (MapView) findViewById(R.id.mapview);
		this.mapController = mapView.getController();
		mapView.setBuiltInZoomControls(true);
		mapView.setSatellite(true);
		lastSampleTime 		= (TextView) findViewById(R.id.cellLocationSampleTime);
		lastSampleValue 	= (TextView) findViewById(R.id.cellLocationSampleReading);
		numberOfReadings	= (TextView) findViewById(R.id.cellLocationNumberOfReadingsValue);
		
		mapOverlays = mapView.getOverlays();
		drawable = this.getResources().getDrawable(R.drawable.pin_app);
		itemizedoverlay = new LocationMapOverlays(drawable, this);
	}

	private void getCellLocation() {
		LocationDataLogger locationDataLogger = (LocationDataLogger) getIntent().getSerializableExtra(getString(R.string.logger_data_object));
		List<LocationWrapper> points = locationDataLogger.getAllValues();
		int size	= points.size();
		points 		= size>10 ? points.subList(size-0, size) : points;
		
		
		GeoPoint point;
		for (LocationWrapper lw : points) {
			double latitude 	= lw.getLat();
			double longitude 	= lw.getLon();
			int latitudeE6 	= (int) (1e6 * latitude);
			int longitudeE6 = (int) (1e6 * longitude);
			Log.i(this.getClass().getSimpleName(), String.format("Adding point %s", lw.toString()));
			point = new GeoPoint(latitudeE6, longitudeE6);
			mapController.setCenter(point);
			mapController.setZoom(15);
			OverlayItem overlayitem = new OverlayItem(point, "Sample location", lw.toString());
			itemizedoverlay.addOverlay(overlayitem);
			mapOverlays.add(itemizedoverlay);
		}
		lastSampleValue.setText(locationDataLogger.getLastValue().toString());
		lastSampleTime.setText(locationDataLogger.getLastSampleDate().toGMTString());
		numberOfReadings.setText(String.format("%d", size));
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}
