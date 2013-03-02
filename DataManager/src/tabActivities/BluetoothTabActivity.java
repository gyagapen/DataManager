package tabActivities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;

import com.example.datamanager.DataActivation;
import com.example.datamanager.LogsProvider;
import com.example.datamanager.SharedPrefsEditor;
import com.gyagapen.cleverconnectivity.R;

public class BluetoothTabActivity extends Activity {
	
	private CheckBox cboxBluetoothOffSleepM = null;
	
	private LogsProvider logsProvider = null;

	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;
	private DataActivation dataActivation;

	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_bluetooth);
		
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
		
		cboxBluetoothOffSleepM = (CheckBox)findViewById(R.id.checkBoxBluetoothOffSleepM);
	}
	
	
	
	/**
	 * Init ui components from value extracted from shared preferences
	 */
	private void initializeUiComponentsData() {

		cboxBluetoothOffSleepM.setChecked(sharedPrefsEditor.getBluetoothDeactivateDuringSleep());
	}
	
	
	
	private void applySettings()
	{
		boolean isBluetoothOffSleepM = cboxBluetoothOffSleepM.isChecked();
		
		sharedPrefsEditor.setBluetoothDeactivationDuringSleep(isBluetoothOffSleepM);
	}
	
	
	
	protected void onDestroy() {
		
		applySettings();
		
		super.onDestroy();
	}


}
