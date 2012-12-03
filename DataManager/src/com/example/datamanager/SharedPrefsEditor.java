package com.example.datamanager;

import java.io.IOException;

import android.app.Activity;
import android.content.SharedPreferences;

public class SharedPrefsEditor {

	private SharedPreferences dataManagerSettings = null;
	private SharedPreferences.Editor prefEditor = null;;

	// time on & off for connectivity (in minutes)
	static final String STR_TIME_ON = "TimeOn";
	static final String STR_TIME_OFF = "TimeOff";
	static final String STR_PREFS_ARE_ESTABLISHED = "prefsAreEstablished";
	static final String STR_DATA_IS_ACTIVATED = "dataIsActivated";
	static final String STR_DATA_MANAGER_IS_ACTIVATED = "dataMgrIsActivated";
	static final String STR_WIFI_IS_ACTIVATED = "wifiIsActivated";
	static final String STR_WIFI_MANAGER_IS_ACTIVATED = "wifiMgrIsActivated";
	static final String STR_INTERVAL_CHECK = "IntervalCheckTime";
	static final String STR_SERVICE_RUNNING = "ServiceIsRunning";
	static final String STR_AUTO_SYNC = "AutoSync";

	// Default values
	static final int TIME_ON = 3; // min
	static final int TIME_OFF = 15; // min
	static final int INTERVAL_CHECK = 5; // seconds
	static final boolean PREFS_ARE_ESTABLISHED = true;
	static final boolean DATA_IS_ACTIVATED = true;
	static final boolean DATA_MGR_IS_ACTIVATED = true;
	static final boolean WIFI_IS_ACTIVATED = true;
	static final boolean WIFI_MGR_IS_ACTIVATED = true;
	static final boolean SERVICE_IS_STARTED = false;
	static final boolean AUTO_SYNC_IS_ACTIVATED = false;
	static final String PREFERENCE_NAME = "DataManagerPreferences";

	// gives access to connection states
	DataActivation dataConnectionState;

	public SharedPrefsEditor(SharedPreferences someSharedPreferences,
			DataActivation dataActivation) {

		// shared preferences
		dataManagerSettings = someSharedPreferences;
		prefEditor = dataManagerSettings.edit();
		dataConnectionState = dataActivation;

	}

	public void initializePreferences() throws IOException {

		// first run
		if (!dataManagerSettings.contains(STR_DATA_MANAGER_IS_ACTIVATED)) {

			prefEditor.putInt(STR_TIME_ON, TIME_ON);
			prefEditor.putInt(STR_TIME_OFF, TIME_OFF);
			prefEditor.putInt(STR_INTERVAL_CHECK, INTERVAL_CHECK);
			prefEditor.putBoolean(STR_PREFS_ARE_ESTABLISHED,
					PREFS_ARE_ESTABLISHED);
			// original state of 3G activation
			prefEditor.putBoolean(STR_DATA_IS_ACTIVATED,
					dataConnectionState.isDataChipActivated());
			prefEditor.putBoolean(STR_DATA_MANAGER_IS_ACTIVATED,
					DATA_MGR_IS_ACTIVATED);
			prefEditor.putBoolean(STR_WIFI_IS_ACTIVATED, WIFI_IS_ACTIVATED);
			// original state of wifi activation
			prefEditor.putBoolean(STR_WIFI_MANAGER_IS_ACTIVATED,
					dataConnectionState.isWifiChipActivated());
			prefEditor.putBoolean(STR_SERVICE_RUNNING, SERVICE_IS_STARTED);
			// original state of auto-sync activation
			prefEditor.putBoolean(STR_AUTO_SYNC,
					dataConnectionState.isAutoSyncIsActivated());

			prefEditor.commit();
		}
	}

	public void resetPreferences() throws IOException {

		prefEditor.putInt(STR_TIME_ON, TIME_ON);
		prefEditor.putInt(STR_TIME_OFF, TIME_OFF);
		prefEditor.putInt(STR_INTERVAL_CHECK, INTERVAL_CHECK);
		prefEditor.putBoolean(STR_PREFS_ARE_ESTABLISHED, PREFS_ARE_ESTABLISHED);
		// original state of 3G activation
		prefEditor.putBoolean(STR_DATA_IS_ACTIVATED,
				dataConnectionState.isDataChipActivated());
		prefEditor.putBoolean(STR_DATA_MANAGER_IS_ACTIVATED,
				DATA_MGR_IS_ACTIVATED);
		prefEditor.putBoolean(STR_WIFI_IS_ACTIVATED, WIFI_IS_ACTIVATED);
		// original state of wifi activation
		prefEditor.putBoolean(STR_WIFI_MANAGER_IS_ACTIVATED,
				dataConnectionState.isWifiChipActivated());
		prefEditor.putBoolean(STR_SERVICE_RUNNING, SERVICE_IS_STARTED);
		// original state of auto-sync activation
		prefEditor.putBoolean(STR_AUTO_SYNC,
				dataConnectionState.isAutoSyncIsActivated());
		prefEditor.commit();
	}

	public int getTimeOn() {
		return dataManagerSettings.getInt(STR_TIME_ON, TIME_ON);
	}

	public int getTimeOff() {
		return dataManagerSettings.getInt(STR_TIME_OFF, TIME_OFF);
	}

	public int getIntervalCheck() {
		return dataManagerSettings.getInt(STR_INTERVAL_CHECK, INTERVAL_CHECK);
	}

	public boolean isDataActivated() {
		return dataManagerSettings.getBoolean(STR_DATA_IS_ACTIVATED,
				dataConnectionState.isDataChipActivated());
	}

	public boolean isDataMgrActivated() {
		return dataManagerSettings.getBoolean(STR_DATA_MANAGER_IS_ACTIVATED,
				DATA_MGR_IS_ACTIVATED);
	}

	public boolean isWifiActivated() {
		return dataManagerSettings.getBoolean(STR_WIFI_IS_ACTIVATED,
				dataConnectionState.isWifiChipActivated());
	}

	public boolean isWifiManagerActivated() {
		return dataManagerSettings.getBoolean(STR_WIFI_MANAGER_IS_ACTIVATED,
				WIFI_MGR_IS_ACTIVATED);
	}

	public boolean isServiceActivated() {
		return dataManagerSettings.getBoolean(STR_SERVICE_RUNNING,
				SERVICE_IS_STARTED);
	}

	public boolean isAutoSyncActivated() {
		return dataManagerSettings.getBoolean(STR_AUTO_SYNC,
				dataConnectionState.isAutoSyncIsActivated());
	}

	public void setTimeOn(int timeOn) {
		prefEditor.putInt(STR_TIME_ON, timeOn);
		prefEditor.commit();
	}

	public void setTimeOff(int timeOff) {
		prefEditor.putInt(STR_TIME_OFF, timeOff);
		prefEditor.commit();
	}

	public void setIntervalCheckTime(int intervalCheckTime) {
		prefEditor.putInt(STR_INTERVAL_CHECK, intervalCheckTime);
		prefEditor.commit();
	}

	public void setDataActivation(boolean isEnabled) {
		prefEditor.putBoolean(STR_DATA_IS_ACTIVATED, isEnabled);
		prefEditor.commit();
	}

	public void setDataActivationManager(boolean isEnabled) {
		prefEditor.putBoolean(STR_DATA_MANAGER_IS_ACTIVATED, isEnabled);
		prefEditor.commit();
	}

	public void setWifiActivation(boolean isEnabled) {
		prefEditor.putBoolean(STR_WIFI_IS_ACTIVATED, isEnabled);
		prefEditor.commit();
	}

	public void setWifiActivationManager(boolean isEnabled) {
		prefEditor.putBoolean(STR_WIFI_MANAGER_IS_ACTIVATED, isEnabled);
		prefEditor.commit();
	}

	public void setAutoSyncActivation(boolean isEnabled) {
		prefEditor.putBoolean(STR_AUTO_SYNC, isEnabled);
		prefEditor.commit();
	}

	public void setServiceActivation(boolean isEnabled) {
		prefEditor.putBoolean(STR_SERVICE_RUNNING, isEnabled);
		prefEditor.commit();
	}

	public void setAllValues(int timeOn, int timeOff, int checkTime,
			boolean dataIsEnabled, boolean dataMgrIsEnabled,
			boolean wifiIsEnabled, boolean wifiMgrIsEnabled, boolean autoSyncIsActivated) {
		prefEditor.putInt(STR_TIME_ON, timeOn);
		prefEditor.putInt(STR_TIME_OFF, timeOff);
		prefEditor.putInt(STR_INTERVAL_CHECK, checkTime);
		prefEditor.putBoolean(STR_DATA_IS_ACTIVATED, dataIsEnabled);
		prefEditor.putBoolean(STR_DATA_MANAGER_IS_ACTIVATED, dataMgrIsEnabled);
		prefEditor.putBoolean(STR_WIFI_IS_ACTIVATED, wifiIsEnabled);
		prefEditor.putBoolean(STR_WIFI_MANAGER_IS_ACTIVATED, wifiMgrIsEnabled);
		prefEditor.putBoolean(STR_AUTO_SYNC, autoSyncIsActivated);
		prefEditor.commit();
	}

}
