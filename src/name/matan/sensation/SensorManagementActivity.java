package name.matan.sensation;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ToggleButton;
import android.support.v4.app.NavUtils;

public class SensorManagementActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_management);        
        Button allOnButton = (Button) findViewById(R.id.allOnButton);
        allOnButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ToggleButton accelerometerToggle = (ToggleButton) findViewById(R.id.accelerometerToggleButton);
				
			}
		});
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
