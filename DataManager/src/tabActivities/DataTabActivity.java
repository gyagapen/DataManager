package tabActivities;

import com.example.datamanager.ChangeNetworkMode;
import com.example.datamanager.DataActivation;
import com.example.datamanager.LogsProvider;
import com.example.datamanager.SharedPrefsEditor;
import com.gyagapen.cleverconnectivity.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

public class DataTabActivity extends Activity {

	private CheckBox cbData = null;
	private CheckBox cbDataMgr = null;
	private CheckBox cbox2GSwitch = null;
	private TextView tv2GSwitch = null;

	private LogsProvider logsProvider = null;

	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;
	private DataActivation dataActivation;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_data);

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

		cbData = (CheckBox) findViewById(R.id.checkBoxData);
		cbDataMgr = (CheckBox) findViewById(R.id.checkBoxDataMgr);

		cbox2GSwitch = (CheckBox)findViewById(R.id.CheckBox2GSwitch);
		tv2GSwitch = (TextView)findViewById(R.id.TextView2GSwitch);
	}



	/**
	 * Init ui components from value extracted from shared preferences
	 */
	private void initializeUiComponentsData() {

		boolean dataIsActivated = sharedPrefsEditor.isDataActivated();
		cbData.setChecked(dataIsActivated);

		boolean dataMgrIsActivated = sharedPrefsEditor.isDataMgrActivated();
		cbDataMgr.setChecked(dataMgrIsActivated);

		//show or hide 2G switch fields
		ChangeNetworkMode changeNetworkMode = new ChangeNetworkMode(this);
		activate2GSwitch(changeNetworkMode.isCyanogenMod());


	}


	/**
	 * SHow or hide fields related to the 2g switch
	 * @param isActivate
	 */
	private void activate2GSwitch(boolean isActivate)
	{


		if(!isActivate)
		{
			//hide fields related to 2G switch
			tv2GSwitch.setText(R.string.switch_incompatible2G);
			tv2GSwitch.setEnabled(false);
			cbox2GSwitch.setEnabled(false);

		}
		else
		{
			//show fields related to 2G switch
			tv2GSwitch.setEnabled(true);
			cbox2GSwitch.setEnabled(true);

			//init checkbox
			cbox2GSwitch.setChecked(sharedPrefsEditor.is2GSwitchActivated());

		}
	}




	private void applySettings()
	{
		boolean dataIsActivated = cbData.isChecked();
		boolean dataMgrIsActivated = cbDataMgr.isChecked();
		boolean is2GSwitchActivated = cbox2GSwitch.isChecked();

		sharedPrefsEditor.setDataActivation(dataIsActivated);
		sharedPrefsEditor.setDataActivationManager(dataMgrIsActivated);
		sharedPrefsEditor.set2GSwitchActivation(is2GSwitchActivated);


		try{
			// if data is disabled; data connection is stopped
			if (!dataIsActivated) {

				// keep auto sync (in case of wifi connection)
				dataActivation.setMobileDataEnabled(false);
				// dataActivation.setAutoSync(true);
			} else {
				// activate data
				dataActivation.setMobileDataEnabled(true);
				// dataActivation.setAutoSync(true);
			}

		} catch (Exception e) {
			logsProvider.error(e);
		}



	}



	protected void onDestroy() {

		applySettings();

		super.onDestroy();
	}





}
