package name.matan.sensation;

import java.util.regex.Pattern;

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
import android.widget.TextView;
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
		setNumberPicker();
		setAgreeToTosButton();
	}

	private void setAgreeToTosButton() {
		Button tosButton = (Button) findViewById(R.id.agreeToTosButton);
		tosButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isInputOK()) {
					Intent intent = new Intent(MainActivity.this, SensorManagementActivity.class);
					startActivity(intent);
				}
			}
		});

	}

	private void setNumberPicker() {
		np = (NumberPicker) findViewById(R.id.np);
		np.setMaxValue(120);
		np.setMinValue(0);
		np.setValue(30);
	}

	private boolean isInputOK() {
		String phoneNumberRegex = "[\\d-+]{6,12}";
		String phoneNumberInput = ((TextView) findViewById(R.id.phoneNumberEditText)).getText().toString();
		if (! phoneNumberInput.matches(phoneNumberRegex)) {
			Toast.makeText(getApplicationContext(), "Phone number should contain 6 to 12 digits, '+'s and '-'s", 
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
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
