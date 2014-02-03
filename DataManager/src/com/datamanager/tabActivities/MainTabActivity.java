package com.datamanager.tabActivities;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.datamanager.core.LogsProvider;
import com.datamanager.core.MainService;
import com.datamanager.core.SaveConnectionPreferences;
import com.datamanager.core.SharedPrefsEditor;
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
		genspec.setIndicator("", getResources().getDrawable(R.drawable.general));
		Intent genIntent = new Intent(this, GeneralTabActivity.class);
		genspec.setContent(genIntent);

		// Tab for Data
		TabSpec dataspec = tabHost.newTabSpec("Data");
		dataspec.setIndicator("", getResources().getDrawable(R.drawable.data));
		Intent dataIntent = new Intent(this, DataTabActivity.class);
		dataspec.setContent(dataIntent);

		// Tab for Wifi
		TabSpec wifispec = tabHost.newTabSpec("Wifi");
		wifispec.setIndicator("", getResources().getDrawable(R.drawable.wifi));
		Intent wifiIntent = new Intent(this, WifiTabActivity.class);
		wifispec.setContent(wifiIntent);

		// Tab for Sync
		TabSpec syncspec = tabHost.newTabSpec("Sync");
		syncspec.setIndicator("", getResources().getDrawable(R.drawable.sync));
		Intent syncIntent = new Intent(this, SyncTabActivity.class);
		syncspec.setContent(syncIntent);

		TabSpec bluetootspec = tabHost.newTabSpec("Bluetooth");
		bluetootspec.setIndicator("", getResources().getDrawable(R.drawable.bluetooth));
		Intent bluetoothIntent = new Intent(this, BluetoothTabActivity.class);
		bluetootspec.setContent(bluetoothIntent);

		// Tab for sleep hours
		TabSpec sleepspec = tabHost.newTabSpec("Sleep");
		sleepspec.setIndicator("", getResources().getDrawable(R.drawable.sleep));
		Intent sleepIntent = new Intent(this, SleepTimerPickerActivity.class);
		sleepspec.setContent(sleepIntent);

		// Tab for timers 
		TabSpec timerspec = tabHost.newTabSpec("Timers");
		timerspec.setIndicator("", getResources().getDrawable(R.drawable.timers));
		Intent timerIntent = new Intent(this, TimersTabActivity.class);
		timerspec.setContent(timerIntent);


		// Tab for advanced 
		TabSpec advancedspec = tabHost.newTabSpec("Advanced");
		advancedspec.setIndicator("", getResources().getDrawable(R.drawable.advanced));
		Intent advancedIntent = new Intent(this, AdvancedTabActivity.class);
		advancedspec.setContent(advancedIntent);

		// Tab for Misc 
		TabSpec miscspec = tabHost.newTabSpec("Misc");
		miscspec.setIndicator("", getResources().getDrawable(R.drawable.misc));
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
	 * Whenever application is closed
	 */

	protected void onDestroy() {

		super.onDestroy();

		System.exit(0);


	}

}
