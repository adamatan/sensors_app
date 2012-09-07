package name.matan.sensation;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

/**
 * Registration form with link to agreement page.
 * @author adamatan
 *
 */
public class MainActivity extends Activity {

	private NumberPicker np;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		np = (NumberPicker) findViewById(R.id.np);
		np.setMaxValue(120);
		np.setMinValue(0);
		np.setValue(30);

		Button tosButton = (Button) findViewById(R.id.button1);
		tosButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, SensorManagementActivity.class);
				startActivity(intent);
			}
		});
	}
	
	private void isInputOK() {
		
		Toast.makeText(getApplicationContext(), "Is the phone number correct?", Toast.LENGTH_SHORT).show();
		
	}

	/**
	 * Shows an alert dialog before viewing the TOS
	 * Credit: http://www.androidhive.info/2011/09/how-to-show-alert-dialog-in-android/
	 */
	public void onClick(View v) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle(getString(R.string.tos_alert_title));
		alertDialog.setMessage(getString(R.string.tos_alert_text));
		alertDialog.setIcon(R.drawable.bananas_small);
		// Yes - open the TOS
		alertDialog.setPositiveButton(getString(R.string.tos_alert_button_yes), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which) {
				//
				Intent intent = new Intent(MainActivity.this, TosActivity.class);
				startActivity(intent);
			}
		});

		// No - Go back to MainActivity
		alertDialog.setNegativeButton(getString(R.string.tos_alert_button_no), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getApplicationContext(), getString(R.string.tos_alert_no_toast), 
						Toast.LENGTH_SHORT).show();
				dialog.cancel();
			}
		});
		alertDialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
