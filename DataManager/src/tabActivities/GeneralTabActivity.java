package tabActivities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;

import com.example.datamanager.DataActivation;
import com.example.datamanager.LogsProvider;
import com.example.datamanager.SharedPrefsEditor;
import com.gyagapen.cleverconnectivity.R;

public class GeneralTabActivity extends Activity {

	private CheckBox cbServiceIsDeactivated = null;
	private CheckBox cbServiceIsDeactivatedPlugged = null;
	private CheckBox cbKeyguardOff = null;

	private LogsProvider logsProvider = null;

	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;
	private DataActivation dataActivation;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_general);

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
		
		cbServiceIsDeactivated = (CheckBox) findViewById(R.id.checkBoxDeactivateAll);
		cbServiceIsDeactivatedPlugged = (CheckBox) findViewById(R.id.checkBoxDeactivatePlugged);
		cbKeyguardOff = (CheckBox)findViewById(R.id.checkBoxKeyguardOff);
		
	}
	
	
	
	/**
	 * Init ui components from value extracted from shared preferences
	 */
	private void initializeUiComponentsData() {

		boolean serviceIsDeactivated = sharedPrefsEditor.isServiceDeactivatedAll();
		cbServiceIsDeactivated.setChecked(serviceIsDeactivated);

		boolean serviceIsDeactivatedPlugged = sharedPrefsEditor.isDeactivatedWhilePlugged();
		cbServiceIsDeactivatedPlugged.setChecked(serviceIsDeactivatedPlugged); 
		
		cbKeyguardOff.setChecked(sharedPrefsEditor.isEnabledWhenKeyguardOff());

	}
	
	
	
	private void applySettings()
	{
		boolean isServiceDeactived = cbServiceIsDeactivated.isChecked();
		boolean isServiceDeactivatedPlugged = cbServiceIsDeactivatedPlugged.isChecked();
		boolean isKeyguardOff = cbKeyguardOff.isChecked();
		
		sharedPrefsEditor.setDeactivateAll(isServiceDeactived);
		sharedPrefsEditor.setDeactivateWhilePlugged(isServiceDeactivatedPlugged);
		sharedPrefsEditor.setEnabledWhenKeyguardOff(isKeyguardOff);
		
		//stop service if deactivate is checked or deactivate while plugged check and phone is plugged
		if(isServiceDeactived || (isServiceDeactivatedPlugged && dataActivation.isPhonePlugged()))
		{
			MainTabActivity.stopDataManagerService(this, sharedPrefsEditor);
		}
		else
		{
			MainTabActivity.StartDataManagerService(this, sharedPrefsEditor);
		}
	}
	
	
	
	protected void onDestroy() {
		
		applySettings();
		
		super.onDestroy();
	}


}
