package name.matan.sensation;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

	private void saveTosToFile() {
		
		Date date = new Date();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd--HH:mm:ss z");
		String timestamp = formatter.format(date);
		String fileContents = String.format("User agreed to TOS, %s\nPhone UDID:%s\nGender:%d\nAge:%d\n", 
				timestamp, getPhoneUdid(), getGenderAsNumber(), getAgeNumberPicker().getValue());
		String filename = String.format("%s_%s_agreement.txt", getPhoneUdid(), timestamp);
		File file = new File(Environment.getExternalStorageDirectory(), filename);
		FileOutputStream fos;
		byte[] data = fileContents.getBytes();
		try {
		    fos = new FileOutputStream(file);
		    fos.write(data);
		    fos.flush();
		    fos.close();
			Toast.makeText(getApplicationContext(), "File saved: "+filename, Toast.LENGTH_LONG).show();
		} catch (FileNotFoundException e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}

	private void setAgreeToTosButton() {
		Button tosButton = (Button) findViewById(R.id.agreeToTosButton);
		tosButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isInputOK()) {
					saveTosToFile();
					Intent intent = new Intent(MainActivity.this, SensorManagementActivity.class);
					startActivity(intent);
				}
			}
		});

	}

	private void setNumberPicker() {
		np = (NumberPicker) findViewById(R.id.ageNumberPicker);
		np.setMaxValue(120);
		np.setMinValue(12);
		np.setValue(30);
	}

	private boolean isInputOK() {
		// Assert phone number
		String phoneNumberRegex = "[\\d-+]{6,12}";
		String phoneNumberInput = getPhoneUdid();
		boolean doChecks=true;
		
		if (doChecks) 
		if (! phoneNumberInput.matches(phoneNumberRegex)) {
			Toast.makeText(getApplicationContext(), "Phone number should contain 6 to 12 digits, '+'s and '-'s", 
					Toast.LENGTH_SHORT).show();
			return false;
		}

		// Assert Gender
		String selectedGenderText = getGenderRawText();
		String maleText = getString(R.string.gender_male);
		String femaleText = getString(R.string.gender_female);

		if (doChecks)
		if (! (selectedGenderText.equals(maleText) || selectedGenderText.equals(femaleText))) {
			Toast.makeText(getApplicationContext(), 
					String.format("Gender can be either %s or %s, not %s", maleText, femaleText, selectedGenderText), 
							Toast.LENGTH_SHORT).show();
			return false;
		}
		
		// Assert age
		NumberPicker ageNumberPicker = getAgeNumberPicker();
		int ageInput = ageNumberPicker.getValue();
		int minAge = ageNumberPicker.getMinValue();
		int maxAge = ageNumberPicker.getMaxValue();
		if (doChecks)
		if (! (ageInput>=minAge && ageInput<=maxAge)) {
			String errorMessage = String.format("Are you really %d years old? Age should be between %d and %d.", 
					ageInput, minAge, maxAge);
			Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
			return false;
		}
		
		
		return true;
	}

	private NumberPicker getAgeNumberPicker() {
		NumberPicker ageNumberPicker = (NumberPicker) findViewById(R.id.ageNumberPicker);
		return ageNumberPicker;
	}

	private int getGenderAsNumber() {
		return getGenderRawText()==getString(R.string.gender_male)?0:1;
	}
	
	private String getGenderRawText() {
		RadioGroup genderPicker = (RadioGroup) findViewById(R.id.genderRadioGroup);
		int buttonId = genderPicker.getCheckedRadioButtonId();
		RadioButton selectedGender = (RadioButton) findViewById(buttonId);
		String selectedGenderText = selectedGender.getText().toString();
		return selectedGenderText;
	}

	private String getPhoneUdid() {
		return ((TextView) findViewById(R.id.phoneNumberEditText)).getText().toString();
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
