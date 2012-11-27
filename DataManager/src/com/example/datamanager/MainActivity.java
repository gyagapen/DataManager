package com.example.datamanager;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	// ui components
	CheckBox cbData = null;
	CheckBox cbDataMgr = null;
	EditText edTimeOn = null;
	EditText edTimeOff = null;
	EditText edInterval = null;
	Button buttonSave = null;

	// SharedPreferences
	SharedPreferences prefs = null;
	SharedPrefsEditor sharedPrefsEditor = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// shared prefs init
		prefs = getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				Activity.MODE_PRIVATE);
		sharedPrefsEditor = new SharedPrefsEditor(prefs);
		
		/*try {
			sharedPrefsEditor.resetPreferences();
		} catch (IOException e1) {
			e1.printStackTrace();
		}*/

		try {
			sharedPrefsEditor.initializePreferences();

			setContentView(R.layout.activity_main);

			// init ui
			loadUiComponents();
			initializeUiComponentsData();

			// instanciate save button
			buttonSave = (Button) findViewById(R.id.buttonSave);
			buttonSave.setOnClickListener(this);

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/**
	 * 
	 * Load of components of UI
	 */
	private void loadUiComponents() {
		cbData = (CheckBox) findViewById(R.id.checkBoxData);
		cbDataMgr = (CheckBox) findViewById(R.id.checkBoxDataMgr);

		edTimeOn = (EditText) findViewById(R.id.editTextTimeOn);
		edTimeOff = (EditText) findViewById(R.id.editTextTimeOff);
		edInterval = (EditText) findViewById(R.id.editTextInterval);

	}

	/**
	 * Init ui components from value extracted from shared preferences
	 */
	private void initializeUiComponentsData() {
		int timeOn = sharedPrefsEditor.getTimeOn();
		edTimeOn.setText(String.valueOf(timeOn));

		int timeOff = sharedPrefsEditor.getTimeOff();
		edTimeOff.setText(String.valueOf(timeOff));

		int checkTime = sharedPrefsEditor.getIntervalCheck();
		edInterval.setText(String.valueOf(checkTime));

		boolean dataIsActivated = sharedPrefsEditor.isDataActivated();
		cbData.setChecked(dataIsActivated);

		boolean dataMgrIsActivated = sharedPrefsEditor.isDataMgrActivated();
		cbDataMgr.setChecked(dataMgrIsActivated);

	}

	public void onClick(View v) {

		// if saved button is click
		if (v == buttonSave) {
			// load ui compenents
			loadUiComponents();

			// get settings values
			int timeOn = Integer.parseInt(edTimeOn.getText().toString());
			int timeOff = Integer.parseInt(edTimeOff.getText().toString());
			int intervalCheck = Integer.parseInt(edInterval.getText()
					.toString());

			boolean dataIsActivated = cbData.isChecked();
			boolean dataMgrIsActivated = cbDataMgr.isChecked();

			// save all these preferences
			sharedPrefsEditor.setAllValues(timeOn, timeOff, intervalCheck,
					dataIsActivated, dataMgrIsActivated);

			try {
				// if data is disabled; data connection is stopped
				if (!dataIsActivated) {

					//keep auto sync (in case of wifi connection)
					setMobileDataEnabled(false, false);

				} else {
					// activate data
					setMobileDataEnabled(true, true);
					
					// if data manager is disabled, service is stopped
					if (!dataMgrIsActivated) {
						stopDataManagerService();
					} else {
						// start service
						//
						//Toast.makeText(this, "service is started : "+sharedPrefsEditor.isDataMgrActivated(), Toast.LENGTH_SHORT).show();
						StartDataManagerService();
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * Stop data manager service
	 */
	public void stopDataManagerService() {
		// if service is started
		//if (sharedPrefsEditor.isServiceActivated()) {
			stopService(new Intent(this, MainService.class));
		//}
	}

	/**
	 * Enable or disable data
	 * 
	 * @param enabled
	 * @throws Exception
	 */
	public void setMobileDataEnabled(boolean enabled, boolean enableAutoSync) throws Exception {

		DataActivation dataActivation = new DataActivation(getBaseContext());

		dataActivation.setMobileDataEnabled(enabled, enableAutoSync);
	}

	public void StartDataManagerService() {
		// if service is not started
		//if (!sharedPrefsEditor.isServiceActivated()) {
			Intent serviceIntent = new Intent(this, MainService.class);
			startService(serviceIntent);
		//}
	}

}
