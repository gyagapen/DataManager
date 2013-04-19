package tabActivities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.sax.TextElementListener;
import android.view.MotionEvent;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.datamanager.DataActivation;
import com.example.datamanager.LogsProvider;
import com.example.datamanager.SharedPrefsEditor;
import com.gyagapen.cleverconnectivity.R;

public class GeneralTabActivity extends Activity {

	private CheckBox cbServiceIsDeactivated = null;
	private CheckBox cbServiceIsDeactivatedPlugged = null;
	private CheckBox cbKeyguardOff = null;
	private CheckBox cbNotification = null;
	private CheckBox cbBatteryLevel = null;
	private EditText edBatteryLevel = null;

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
		cbNotification = (CheckBox)findViewById(R.id.checkBoxNotification);
		cbBatteryLevel = (CheckBox)findViewById(R.id.cbBatteryLevel);
		edBatteryLevel = (EditText)findViewById(R.id.edBatteryLevel);
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
		
		cbNotification.setChecked(sharedPrefsEditor.isNotificationEnabled());
		
		cbBatteryLevel.setChecked(sharedPrefsEditor.getLowProfileBatteryActivation());
		edBatteryLevel.setText(sharedPrefsEditor.getBatteryLevelToMonitor().toString());

	}
	
	
	
	private void applySettings()
	{
		edBatteryLevel.clearFocus();
		
		boolean isServiceDeactived = cbServiceIsDeactivated.isChecked();
		boolean isServiceDeactivatedPlugged = cbServiceIsDeactivatedPlugged.isChecked();
		boolean isKeyguardOff = cbKeyguardOff.isChecked();
		boolean isNotificationEnabled = cbNotification.isChecked();
		boolean isBatteryLevelEnabled = cbBatteryLevel.isChecked();
		
		int batteryLevelToMonitor = 0;
		
		try
		{
			batteryLevelToMonitor = Integer.parseInt(edBatteryLevel.getText().toString());
		}
		catch(Exception e)
		{
			
			//if error keep old value
			batteryLevelToMonitor = sharedPrefsEditor.getBatteryLevelToMonitor();
			
			logsProvider.error(e);
		}
		
		sharedPrefsEditor.setDeactivateAll(isServiceDeactived);
		sharedPrefsEditor.setDeactivateWhilePlugged(isServiceDeactivatedPlugged);
		sharedPrefsEditor.setEnabledWhenKeyguardOff(isKeyguardOff);
		sharedPrefsEditor.setNotificationActivation(isNotificationEnabled);
		sharedPrefsEditor.setLowBatteryProfileActiviation(isBatteryLevelEnabled);
		sharedPrefsEditor.setBatteryLevelToMonitor(batteryLevelToMonitor);
		
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

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.onTouchEvent(event);
	}

}
