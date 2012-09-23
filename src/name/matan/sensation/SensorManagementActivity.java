package name.matan.sensation;

import java.util.ArrayList;
import java.util.List;

import name.matan.sensation.location.LocationDataLogger;
import name.matan.sensation.location.LocationWrapper;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

public class SensorManagementActivity extends Activity {

	private final class SensorReadingsThread extends Thread {
		private volatile int delayMilliseconds;

		public SensorReadingsThread(int delayMilliseconds) {
			super();
			this.delayMilliseconds = delayMilliseconds;
		}

		public int getDelayMilliseconds() {
			return delayMilliseconds;
		}

		public void setDelayMilliseconds(int delayMilliseconds) {
			this.delayMilliseconds = delayMilliseconds;
			this.interrupt();
		}

		public void run() {
			while (true) {
				try {
					Thread.sleep(this.delayMilliseconds);
				} catch (InterruptedException e) {
					// Do nothing - it means that setDelayMilliseconds was called
				}
				mHandler.post(mUpdateResults);
				counter++;
			}
		}
	}


	private Button allOnButton;
	private Button allOffButton;

	private TextView cellLocationButton;

	private ToggleButton accelerometerToggle;
	private ToggleButton gpsToggle;
	private ToggleButton cellLocationToggle;
	private ToggleButton gyroToggle;
	private List<ToggleButton> allToggleButtons;

	private Button updateButton;

	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;

	private TimePicker uploadTimePicker;
	private int uploadFrequencyDelaySeconds;

	long lastUpdateTimeMillis;

	String udid;

	final Handler mHandler = new Handler();
	private int counter=1;
	private volatile SensorReadingsThread updaterThread;

	private SensorDataLogger<LocationWrapper> locationDataLogger;

	final Runnable mUpdateResults = new Runnable() {
		public void run() {
			locationDataLogger.logData();

			if (System.currentTimeMillis()>(lastUpdateTimeMillis+uploadFrequencyDelaySeconds*1000)) {
				Log.i(SensorManagementActivity.class.getSimpleName(), "Time to upload results!");
				String filename=udid+"_"+TimeStamper.formatNow("yyyy-MM-dd-HHmm-ss");
				FileWriter fw = new FileWriter(SensorManagementActivity.this, filename);
				StringBuilder sb = new StringBuilder();

				if (cellLocationToggle.isChecked()) {
					List<String> logFileEntries = locationDataLogger.getLogLinesForFile();
					for (String s : logFileEntries) {
						sb.append(s);
					}
				}

				fw.write(sb.toString());

				Runnable uploader = new FTPUploader(SensorManagementActivity.this, filename);
				new Thread(uploader).start();
				lastUpdateTimeMillis=System.currentTimeMillis();
			}
			else {
				long diff=(System.currentTimeMillis()-lastUpdateTimeMillis)/1000;
				Log.e(SensorManagementActivity.class.getSimpleName(), 
						String.format("%d passed, %d seconds till next upload", diff, uploadFrequencyDelaySeconds-diff));
			}
		}
	};


	protected void startUpdateThread() {
		updaterThread = new SensorReadingsThread(500);
		updaterThread.start();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensor_management);
		this.udid=getIntent().getStringExtra("UDID");
		startUpdateThread();
		setButtons();
		setPreferences();
		locationDataLogger = new LocationDataLogger(4, this);

		allOnButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				for (ToggleButton toggleButton : allToggleButtons) {
					toggleButton.setChecked(true);
				}
			}
		});

		allOffButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				for (ToggleButton toggleButton : allToggleButtons) {
					toggleButton.setChecked(false);
				}
			}
		});

	}

	private void setPreferences() {
		preferences = getPreferences(MODE_WORLD_READABLE);
		editor = preferences.edit();
		updateButtonsFromPreferences();
	}

	private void updateButtonsFromPreferences() {
		for (ToggleButton button:allToggleButtons) {
			String preferencesKey = (String) button.getTag();
			button.setChecked(preferences.getBoolean(preferencesKey, true));
		}
	}

	private void updatePreferencesFromButtons() {
		setUploadFrequencyFromUI();
		for (ToggleButton button:allToggleButtons) {
			String preferencesKey = (String) button.getTag();
			editor.putBoolean(preferencesKey, button.isChecked());
		}
		editor.commit();
	}

	private void setButtons() {
		allOnButton 	= (Button) findViewById(R.id.allOnButton);
		allOffButton 	= (Button) findViewById(R.id.allOffButton);

		cellLocationButton = (TextView) findViewById(R.id.cellLocationMoreButton);

		accelerometerToggle = (ToggleButton) findViewById(R.id.accelerometerToggleButton);
		gpsToggle 			= (ToggleButton) findViewById(R.id.GpsToggleButton);
		cellLocationToggle	= (ToggleButton) findViewById(R.id.CellLocatinToggleButton);
		gyroToggle 			= (ToggleButton) findViewById(R.id.GyroToggleButton);

		uploadTimePicker    = (TimePicker) findViewById(R.id.uploadFrequencyTimePicker);

		// Set All-On, All-off buttons
		allToggleButtons 	= new ArrayList<ToggleButton>();
		allToggleButtons.add(accelerometerToggle);
		allToggleButtons.add(gpsToggle);
		allToggleButtons.add(cellLocationToggle);
		allToggleButtons.add(gyroToggle);

		for (ToggleButton toggleButton:allToggleButtons) {
			toggleButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {}
			});
		}

		cellLocationButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SensorManagementActivity.this, CellularLocationLogActivity.class);
				intent.putExtra(getString(R.string.logger_data_object), locationDataLogger);
				startActivity(intent);
			}
		});


		Spinner spinner = (Spinner) findViewById(R.id.sensor_management_update_frequency_spinner);
		final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.sensor_management_update_frequency, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				int delay;
				switch (pos) {
				case 0:	
					delay=60000;
					break;
				case 1:	
					delay=20000;
					break;
				case 2:	
					delay=1000;
					break;
				default:
					delay=60000;
				}
				updaterThread.setDelayMilliseconds(delay);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		updateButton = (Button) findViewById(R.id.sensorManagementUpdateNowButton);
		updateButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				updatePreferencesFromButtons();
			}
		});

		uploadTimePicker.setIs24HourView(true);
		uploadTimePicker.setCurrentHour(0);
		uploadTimePicker.setCurrentMinute(10);
		setUploadFrequencyFromUI();

		lastUpdateTimeMillis=System.currentTimeMillis();
	}

	private void setUploadFrequencyFromUI() {
		this.uploadFrequencyDelaySeconds=uploadTimePicker.getCurrentMinute()*60+
				uploadTimePicker.getCurrentHour()*3600;
		if (this.uploadFrequencyDelaySeconds==0) {
			uploadTimePicker.setCurrentMinute(1);
			this.setUploadFrequencyFromUI();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_sensor_management, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
