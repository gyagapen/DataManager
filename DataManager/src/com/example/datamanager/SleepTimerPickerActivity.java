package com.example.datamanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TimePicker;

public class SleepTimerPickerActivity extends Activity implements
		OnClickListener {

	// time pickers
	private TimePicker timePickerSleepOn = null;
	private TimePicker timePickerSleepOff = null;

	// save button
	private Button buttonSave = null;

	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;

	private DataActivation dataActivation;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sleeptimepicker);

		// shared prefs init
		prefs = getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				Activity.MODE_PRIVATE);

		dataActivation = new DataActivation(getBaseContext());
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);

		// initialize ui components
		initUiComponents();
		
		//load data componenets 
		loadTimePickerData();
		
		//save button implementation
		buttonSave.setOnClickListener(this);

	}

	private void initUiComponents() {
		timePickerSleepOff = (TimePicker) findViewById(R.id.timePickerSleepOff);
		timePickerSleepOn = (TimePicker) findViewById(R.id.timerPickerSleepOn);
		buttonSave = (Button)findViewById(R.id.buttonSaveSleepHours);
	}

	private void loadTimePickerData() {
		// sleep off
		String[] timeSleepOff = sharedPrefsEditor.getSleepTimeOff().split(":");
		int hourSleepOff = Integer.valueOf(timeSleepOff[0]);
		int minuteSleepOff = Integer.valueOf(timeSleepOff[1]);

		timePickerSleepOff.setCurrentHour(hourSleepOff);
		timePickerSleepOff.setCurrentMinute(minuteSleepOff);

		// sleep on
		String[] timeSleepOn = sharedPrefsEditor.getSleepTimeOn().split(":");
		int hourSleepOn = Integer.valueOf(timeSleepOn[0]);
		int minuteSleepOn = Integer.valueOf(timeSleepOn[1]);

		timePickerSleepOn.setCurrentHour(hourSleepOn);
		timePickerSleepOn.setCurrentMinute(minuteSleepOn);
	}

	public void onClick(View v) {
		
		//if save button is clicked
		if(v == buttonSave)
		{
			//getting value from sleep time on
			String hourSleepOn = timePickerSleepOn.getCurrentHour().toString();
			String minuteSleepOn = timePickerSleepOn.getCurrentMinute().toString();
			String timeOn = hourSleepOn+":"+minuteSleepOn;
			
			//getting value from sleep time off
			String hourSleepOff = timePickerSleepOff.getCurrentHour().toString();
			String minuteSleepOff = timePickerSleepOff.getCurrentMinute().toString();
			String timeOff = hourSleepOff+":"+minuteSleepOff;
			
			//saving in preferences
			sharedPrefsEditor.setSleepTimeOn(timeOn);
			sharedPrefsEditor.setSleepTimeOff(timeOff);
			
			//create the intent
			Intent intent = new Intent();
			
			//return result with intent
			setResult(RESULT_OK, intent);
			
			//terminate activity
			finish();
			
		}

	}

}
