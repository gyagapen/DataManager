package tabActivities;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.example.datamanager.LogsProvider;
import com.example.datamanager.MainService;
import com.example.datamanager.SaveConnectionPreferences;
import com.example.datamanager.SharedPrefsEditor;
import com.example.datamanager.SleepTimerPickerActivity;
import com.gyagapen.cleverconnectivity.R;



public class MainTabActivity extends TabActivity{

	private LogsProvider logsProvider = null;
	
	public static final String STR_ACTIVATE_CONNECTIVITY = "activateConnectivity";

	public static final int ID_ALARM_TIME_ON = 1879;
	public static final int ID_ALARM_TIME_OFF = 1899;

	public static final String SEPARATOR = "##";

	public static final String LOG_FILE_NAME = "CleverConnectivity.log";

	public static final boolean APPLICATION_IS_FREE = true;
	
	

	// mail data
	public static final String MAIL_RECIPIENT = "gyagapen@gmail.com";
	public static String MAIL_SUBJECT = "CleverConnectivity Bug Report";
	public static final String MAIL_SUBJECT_PAID = "CleverConnectivity Paid Bug Report";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabbed_layout);
		
		logsProvider = new LogsProvider(getApplicationContext(), this.getClass());


		// initialize connectivity positions
		SaveConnectionPreferences connPrefs = new SaveConnectionPreferences(
				getApplicationContext());
		connPrefs.saveAllConnectionSettingInSharedPrefs();

		TabHost tabHost = getTabHost();

		// Tab for General
		TabSpec genspec = tabHost.newTabSpec("General");
		// setting Title and Icon for the Tab
		genspec.setIndicator("General");
		Intent genIntent = new Intent(this, GeneralTabActivity.class);
		genspec.setContent(genIntent);

		// Tab for Data
		TabSpec dataspec = tabHost.newTabSpec("Data");
		dataspec.setIndicator("Data");
		Intent dataIntent = new Intent(this, DataTabActivity.class);
		dataspec.setContent(dataIntent);

		// Tab for Wifi
		TabSpec wifispec = tabHost.newTabSpec("Wifi");
		wifispec.setIndicator("Wifi");
		Intent wifiIntent = new Intent(this, WifiTabActivity.class);
		wifispec.setContent(wifiIntent);

		// Tab for Sync
		TabSpec syncspec = tabHost.newTabSpec("Sync");
		syncspec.setIndicator("Sync");
		Intent syncIntent = new Intent(this, SyncTabActivity.class);
		syncspec.setContent(syncIntent);

		TabSpec bluetootspec = tabHost.newTabSpec("Bluetooth");
		bluetootspec.setIndicator("Bluetooth");
		Intent bluetoothIntent = new Intent(this, BluetoothTabActivity.class);
		bluetootspec.setContent(bluetoothIntent);

		// Tab for sleep hours
		TabSpec sleepspec = tabHost.newTabSpec("Sleep");
		sleepspec.setIndicator("Sleep");
		Intent sleepIntent = new Intent(this, SleepTimerPickerActivity.class);
		sleepspec.setContent(sleepIntent);

		// Tab for timers 
		TabSpec timerspec = tabHost.newTabSpec("Timers");
		timerspec.setIndicator("Timers");
		Intent timerIntent = new Intent(this, TimersTabActivity.class);
		timerspec.setContent(timerIntent);


		// Tab for advanced 
		TabSpec advancedspec = tabHost.newTabSpec("Advanced");
		advancedspec.setIndicator("Advanced");
		Intent advancedIntent = new Intent(this, AdvancedTabActivity.class);
		advancedspec.setContent(advancedIntent);

		// Tab for Misc 
		TabSpec miscspec = tabHost.newTabSpec("Misc");
		miscspec.setIndicator("Misc");
		Intent miscIntent = new Intent(this, MiscTabActivity.class);
		miscspec.setContent(miscIntent);

		// Adding all TabSpec to TabHost
		tabHost.addTab(genspec); 
		tabHost.addTab(dataspec);
		tabHost.addTab(wifispec);
		tabHost.addTab(syncspec);
		tabHost.addTab(bluetootspec);
		tabHost.addTab(sleepspec);
		tabHost.addTab(timerspec);
		tabHost.addTab(advancedspec);
		tabHost.addTab(miscspec);
	}
	
	
	/**
	 * Stop data manager service
	 */
	public static void stopDataManagerService(Context context, SharedPrefsEditor sharedPrefsEditor) {
		// if service is started
		// if (sharedPrefsEditor.isServiceActivated()) {
		// register service stopped in preferences
		sharedPrefsEditor.setServiceActivation(false);
		context.stopService(new Intent(context, MainService.class));
		// }
	}

	public static void StartDataManagerService(Context context, SharedPrefsEditor sharedPrefsEditor) {
		// if service is not started
		// if (!sharedPrefsEditor.isServiceActivated()) {
		sharedPrefsEditor.setServiceActivation(true);
		Intent serviceIntent = new Intent(context, MainService.class);
		context.startService(serviceIntent);
		// }
	}
	
	
	/**
	 * Whenever application is closed
	 */

	protected void onDestroy() {

		super.onDestroy();

		System.exit(0);


	}

}
