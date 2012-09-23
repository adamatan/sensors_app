package name.matan.sensation;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

/**
 * Based on https://developers.google.com/maps/documentation/android/hello-mapview
 * @author adamatan
 */
public class LocationMapOverlays extends ItemizedOverlay {

	private ArrayList<OverlayItem> overlaysList = new ArrayList<OverlayItem>();
	private Context applicatonContext;

	
	
	
	
	
	
	
	
	public LocationMapOverlays(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		this.applicatonContext=context;
	}
	
	public void addOverlay(OverlayItem overlay) {
		overlaysList.add(overlay);
	    populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return this.overlaysList.get(i);
	}

	@Override
	public int size() {
		return this.overlaysList.size();
	}

	@Override
	protected boolean onTap(int index) {
	  OverlayItem item = overlaysList.get(index);
	  AlertDialog.Builder dialog = new AlertDialog.Builder(this.applicatonContext);
	  dialog.setTitle(item.getTitle());
	  dialog.setMessage(item.getSnippet());
	  dialog.show();
	  return true;
	}
	
	
}
