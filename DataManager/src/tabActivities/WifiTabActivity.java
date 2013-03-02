package tabActivities;

import com.example.datamanager.DataActivation;
import com.example.datamanager.LogsProvider;
import com.example.datamanager.SharedPrefsEditor;
import com.gyagapen.cleverconnectivity.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;

public class WifiTabActivity extends Activity {

	private CheckBox cbWifi = null;
	private CheckBox cbWifiMgr = null;
	private CheckBox cbAutoWifiOff = null;
	private CheckBox cbAutoWifiOn = null;
	private CheckBox cbCheckNetConnWifi = null;

	private LogsProvider logsProvider = null;

	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;
	private DataActivation dataActivation;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_wifi);

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

		cbWifi = (CheckBox) findViewById(R.id.checkBoxWifi);
		cbWifiMgr = (CheckBox) findViewById(R.id.checkBoxWifiMgr);
		cbAutoWifiOff = (CheckBox) findViewById(R.id.checkBoxAutoWifiOff);
		cbAutoWifiOn = (CheckBox) findViewById(R.id.CheckBoxAutoWifiOn);
		cbCheckNetConnWifi = (CheckBox)findViewById(R.id.cboxCheckNetConnectionWIfi);

	}



	/**
	 * Init ui components from value extracted from shared preferences
	 */
	private void initializeUiComponentsData() {

		boolean wifiIsActivated = sharedPrefsEditor.isWifiActivated();
		cbWifi.setChecked(wifiIsActivated);

		boolean wifiMgrIsActivated = sharedPrefsEditor.isWifiManagerActivated();
		cbWifiMgr.setChecked(wifiMgrIsActivated);

		boolean autoWifiOffIsActivated = sharedPrefsEditor
				.isAutoWifiOffActivated();
		cbAutoWifiOff.setChecked(autoWifiOffIsActivated);

		boolean autoWifiOnIsActivated = sharedPrefsEditor
				.isAutoWifiOnActivated();
		cbAutoWifiOn.setChecked(autoWifiOnIsActivated);

		cbCheckNetConnWifi.setChecked(sharedPrefsEditor.getCheckNetConnectionWifi());
	}



	private void applySettings()
	{

		boolean wifiIsActivated = cbWifi.isChecked();
		boolean wifiMgrIsActivated = cbWifiMgr.isChecked();
		boolean autoWifiIsActivated = cbAutoWifiOff.isChecked();
		boolean autoWifiOnIsActivated = cbAutoWifiOn.isChecked();
		boolean isCheckNetConnWifi = cbCheckNetConnWifi.isChecked();

		sharedPrefsEditor.setWifiActivation(wifiIsActivated);
		sharedPrefsEditor.setWifiActivationManager(wifiMgrIsActivated);
		sharedPrefsEditor.setAutoWifiOffActivation(autoWifiIsActivated);
		sharedPrefsEditor.setAutoWifiOnActivation(autoWifiOnIsActivated);
		sharedPrefsEditor.setCheckNetConnectionWifi(isCheckNetConnWifi);

		// if wifi is disabled, wifi connection is stopped
		if (!wifiIsActivated) {
			dataActivation.setWifiConnectionEnabled(false, false, sharedPrefsEditor);
			// dataActivation.setAutoSync(true);
		} else {
			dataActivation.setWifiConnectionEnabled(true, false, sharedPrefsEditor);
			// dataActivation.setAutoSync(true);
		}


	}



	protected void onDestroy() {

		applySettings();

		super.onDestroy();
	}



}
