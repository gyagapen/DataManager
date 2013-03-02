package tabActivities;

import com.example.datamanager.DataActivation;
import com.example.datamanager.LogsProvider;
import com.example.datamanager.SharedPrefsEditor;
import com.gyagapen.cleverconnectivity.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;

public class TimersTabActivity extends Activity {

	private EditText edTimeOn = null;
	private EditText edTimeOnCheck = null;
	private EditText edTimeOff = null;
	private EditText edInterval = null;

	private LogsProvider logsProvider = null;

	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;
	private DataActivation dataActivation;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timers_tab);

		logsProvider = new LogsProvider(getApplicationContext(), this.getClass());

		// shared prefs init
		prefs = getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				Activity.MODE_PRIVATE);

		dataActivation = new DataActivation(getBaseContext());
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);

		loadUiComponents();
		initializeUiComponentsData();

	}

	/**
	 * 
	 * Load of components of UI
	 */
	private void loadUiComponents() {

		edTimeOn = (EditText) findViewById(R.id.editTextTimeOn);
		edTimeOnCheck = (EditText) findViewById(R.id.EditTextTimeOnCheck);
		edTimeOff = (EditText) findViewById(R.id.editTextTimeOff);
		edInterval = (EditText) findViewById(R.id.editTextInterval);
	}



	/**
	 * Init ui components from value extracted from shared preferences
	 */
	private void initializeUiComponentsData() {

		int timeOn = sharedPrefsEditor.getTimeOn();
		edTimeOn.setText(String.valueOf(timeOn));

		int timeOnCheck = sharedPrefsEditor.getTimeOnCheck();
		edTimeOnCheck.setText(String.valueOf(timeOnCheck));

		int timeOff = sharedPrefsEditor.getTimeOff();
		edTimeOff.setText(String.valueOf(timeOff));

		int checkTime = sharedPrefsEditor.getIntervalCheck();
		edInterval.setText(String.valueOf(checkTime));

	}



	private void applySettings()
	{
		// get settings values
		int timeOn = 0;
		try
		{
			timeOn = Integer.parseInt(edTimeOn.getText().toString());
		}
		catch(Exception e)
		{
			timeOn = sharedPrefsEditor.getTimeOn();
		}

		int timeOnCheck=0;
		try
		{
			timeOnCheck = Integer.parseInt(edTimeOnCheck.getText().toString());
		}
		catch(Exception e)
		{
			timeOnCheck = sharedPrefsEditor.getTimeOnCheck();
		}

		int timeOff = 0;

		try
		{
			timeOff = Integer.parseInt(edTimeOff.getText().toString());
		}
		catch (Exception e)
		{
			timeOff = sharedPrefsEditor.getTimeOff();
		}

		int intervalCheck = 0;
		try
		{
			intervalCheck = Integer.parseInt(edInterval.getText().toString());
		}
		catch(Exception e)
		{
			intervalCheck = sharedPrefsEditor.getIntervalCheck();
		}

		sharedPrefsEditor.setTimeOn(timeOn);
		sharedPrefsEditor.setTimeOff(timeOff);
		sharedPrefsEditor.setTimeOnCheck(timeOnCheck);
		sharedPrefsEditor.setIntervalCheckTime(intervalCheck);

	}



	protected void onDestroy() {

		applySettings();

		super.onDestroy();
	}


}
