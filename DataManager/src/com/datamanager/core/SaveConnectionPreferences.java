package com.datamanager.core;

import com.datamanager.tabActivities.AppLauncher;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveConnectionPreferences {

	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;

	private DataActivation dataActivation;
	
	private LogsProvider logsProvider;

	public SaveConnectionPreferences(Context context) {

		logsProvider = new LogsProvider(context, SaveConnectionPreferences.class);
		
		// shared prefs init
		prefs = context.getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				AppLauncher.SHARED_ACCESS_MODE);
		dataActivation = new DataActivation(context);
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);
	}

	public void saveAllConnectionSettingInSharedPrefs() {

		logsProvider.info("saveConnectionPrefs: time off is " +sharedPrefsEditor.isTimeOffIsActivated());
		
		// if screen on connections not delayed or timer off is activated
		if (!sharedPrefsEditor.isScreenOnDelayed()
				&& !sharedPrefsEditor.isCheckingAutoWifiOn() && !sharedPrefsEditor.isTimeOffIsActivated()) {

			boolean autoSyncIsActivated = dataActivation
					.isAutoSyncIsActivated();
			sharedPrefsEditor.setAutoSyncActivation(autoSyncIsActivated);

			boolean wifiConnectionIsActivated = dataActivation
					.isWifiChipActivated();
			sharedPrefsEditor.setWifiActivation(wifiConnectionIsActivated);

			// if data activation is not delayed or data is not switching or
			// option isDataOffWhenWifi not checked
			if (!sharedPrefsEditor.isDataActivationDelayed()
					&& !sharedPrefsEditor.isNetworkModeSwitching()
					&& !(sharedPrefsEditor.isDataOffWhenWifi() && dataActivation
							.isWifiChipActivated())) {

				boolean dataConnectionIsActivated = dataActivation
						.isDataChipActivated();
				sharedPrefsEditor.setDataActivation(dataConnectionIsActivated);
				
				logsProvider.info("saving data connection state: "+dataConnectionIsActivated);
			}

			boolean bluetoothIsActivated = dataActivation
					.isBluetoothChipEnabled();
			sharedPrefsEditor.setBluetoothActivation(bluetoothIsActivated);

		}

	}

}
