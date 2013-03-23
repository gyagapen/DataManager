package com.example.datamanager;

import java.io.IOException;

import android.content.SharedPreferences;

public class SharedPrefsEditor {

	private SharedPreferences dataManagerSettings = null;
	private SharedPreferences.Editor prefEditor = null;

	// time on & off for connectivity (in minutes)
	static final String STR_TIME_ON = "TimeOn";
	static final String STR_TIME_ON_CHECK = "TimeOnCheck";
	static final String STR_TIME_OFF = "TimeOff";
	static final String STR_PREFS_ARE_ESTABLISHED = "prefsAreEstablished";
	static final String STR_DATA_IS_ACTIVATED = "dataIsActivated";
	static final String STR_DATA_MANAGER_IS_ACTIVATED = "dataMgrIsActivated";
	static final String STR_WIFI_IS_ACTIVATED = "wifiIsActivated";
	static final String STR_WIFI_MANAGER_IS_ACTIVATED = "wifiMgrIsActivated";
	static final String STR_INTERVAL_CHECK = "IntervalCheckTime";
	static final String STR_SERVICE_RUNNING = "ServiceIsRunning";
	static final String STR_AUTO_SYNC = "AutoSync";
	static final String STR_SLEEP_IS_ACTIVATED="SleepHours";
	static final String STR_SLEEP_ON_HOUR="SleepHoursTimeOn";
	static final String STR_SLEEP_OFF_HOUR="SleepHoursTimeOff";
	static final String STR_AUTO_WIFI_OFF_IS_ACTIVATED="AutoWifiOff";
	static final String STR_AUTO_WIFI_ON_IS_ACTIVATED="AutoWifiOn";
	static final String STR_IS_SLEEPING="IS_SLEEPING";
	static final String STR_AUTO_SYNC_MGR_IS_ACTIVATED="autoSyncMgrIsActivated";
	static final String STR_TIME_OFF_IS_ACTIVATED="timeOffIsActivated";
	static final String STR_DEACTIVATE_PLUGGED="deactivateWhilePlugged";
	static final String STR_DEACTIVATE_ALL="deactivateAll";
	static final String STR_DATA_ACTIVATION_DELAYED="dataActivationDelayed";
	static final String STR_SCREEN_ON_ACTIVATION_DELAYED="screenOnActivationDelayed";
	static final String STR_SCREEN_DELAY_TIMER="screenDelayTimer";
	static final String STR_FIRST_TIME_ON_IS_ACTIVATED="firstTimeOnIsActivated";
	static final String STR_FIRST_TIME_ON_VALUE="firstTimeOnValue";
	static final String STR_IS_FIRST_TIME_ON="isFirstTimeOn";
	static final String STR_CHECKING_AUTO_WIFI="checkingAutoWifi";
	static final String STR_IS_2G_SWITCH_ACTIVATED="is2gSwitchActivated";
	static final String STR_ORIGINAL_PREFERRED_NETWORK_MODE="originalPreferredNeworkMode";
	static final String STR_NETWORK_MODE_IS_SWITCHING = "networkModeIsSwitching";
	static final String STR_IS_2G_ACTIVATED = "is2GActivated";
	static final String STR_APPLICATION_ON_SET = "applicationOnSet"; 
	static final String STR_IS_APPLICATION_ON_SET_IS_ACTIVATED= "isApplicationOnSetActivated";
	static final String STR_CHECK_NET_CONNECTION_WIFI = "checkNetConnectionWifi";
	static final String STR_NET_CONN_HAS_TO_BE_CHECKED = "netConnHasToBeChecked";
	static final String STR_ENABLE_WHEN_KEYGUARD_OFF = "disableWhenKeyguardOff";
	static final String STR_LOGS_ENABLED = "logsEnabled";
	static final String STR_BLUETOOTH_ACTIVATION = "bluetoothActivation";
	static final String STR_BLUETOOTH_DEACTIVATE_SLEEP_MODE = "bluetoothDeactivateSleepMode";
	static final String STR_SHOW_NOTIFICATION = "showNotification";

	// Default values
	static final int TIME_ON = 2; // min
	static final int TIME_OFF = 12; // min
	static final int TIME_ON_CHECK = 1; // min
	static final int INTERVAL_CHECK = 2; // seconds
	static final int SCREEN_DELAY_TIMER = 0; // seconds
	static final int FIRST_TIME_ON_VALUE=3;//min
	static final boolean PREFS_ARE_ESTABLISHED = true;
	static final boolean DATA_IS_ACTIVATED = true;
	static final boolean DATA_MGR_IS_ACTIVATED = true;
	static final boolean WIFI_IS_ACTIVATED = true;
	static final boolean WIFI_MGR_IS_ACTIVATED = true;
	static final boolean SERVICE_IS_STARTED = false;
	static final boolean AUTO_SYNC_IS_ACTIVATED = false;
	static final boolean AUTO_SYNC_MGR_IS_ACTIVATED = true;
	static final boolean SLEEP_IS_ACTIVATED = false;
	static final boolean AUTO_WIFI_OFF_IS_ACTIVATED= false;
	static final boolean AUTO_WIFI_ON_IS_ACTIVATED= false;
	static final String SLEEP_ON="00:00"; //hh:mm
	static final String SLEEP_OFF="06:00"; //hh:mm
	public static final String PREFERENCE_NAME = "DataManagerPreferences";
	static final boolean IS_SLEEPING=false;
	static final boolean TIME_OFF_IS_ACTIVATED=false;
	static final boolean DEACTIVATE_PLUGGED=false;
	static final boolean DEACTIVATE_ALL=false;
	static final boolean DATA_ACTIVATION_DELAYED=false;
	static final boolean SCREEN_ON_ACTIVATION_DELAYED=false;
	static final boolean FIRST_TIME_ON_IS_ACTIVATED=false;
	static final boolean IS_FIRST_TIME_ON=false;
	static final boolean CHECKING_AUTO_WIFI=false;
	static final boolean IS_2G_SWITCH_ACTIVATED=false;
	static final int ORIGINAL_PREFERRED_NETWORK_MODE=3;
	static final boolean NETWORK_MODE_IS_SWITCHING = false;
	static final boolean IS_2G_ACTIVATED = false;
	static final String APPLICATION_ON_SET = "";
	static final boolean IS_APPLICATION_ON_SET_IS_ACTIVATED=true;
	static final boolean CHECK_NET_CONNECTION_WIFI=false;
	static final boolean NET_CONN_HAS_TO_BE_CHECKED = false;
	static final boolean ENABLE_WHEN_KEYGUARD_OFF = false;
	static final boolean LOGS_ENABLED = true;
	static final boolean BLUETOOTH_ACTIVATION = false;
	static final boolean BLUETOOTH_DEACTIVATE_SLEEP_MODE = false;
	static final boolean SHOW_NOTIFICATION = true;
	
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
			prefEditor.putInt(STR_TIME_ON_CHECK, TIME_ON_CHECK);
			prefEditor.putInt(STR_TIME_OFF, TIME_OFF);
			prefEditor.putInt(STR_INTERVAL_CHECK, INTERVAL_CHECK);
			prefEditor.putInt(STR_SCREEN_DELAY_TIMER, SCREEN_DELAY_TIMER);
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
			prefEditor.putBoolean(STR_AUTO_SYNC_MGR_IS_ACTIVATED, AUTO_SYNC_MGR_IS_ACTIVATED);
			prefEditor.putBoolean(STR_SLEEP_IS_ACTIVATED, SLEEP_IS_ACTIVATED);
			prefEditor.putString(STR_SLEEP_OFF_HOUR, SLEEP_OFF);
			prefEditor.putString(STR_SLEEP_ON_HOUR, SLEEP_ON);
			prefEditor.putBoolean(STR_AUTO_WIFI_OFF_IS_ACTIVATED, AUTO_WIFI_OFF_IS_ACTIVATED);
			prefEditor.putBoolean(STR_AUTO_WIFI_ON_IS_ACTIVATED, AUTO_WIFI_ON_IS_ACTIVATED);
			prefEditor.putBoolean(STR_IS_SLEEPING, IS_SLEEPING);
			prefEditor.putBoolean(STR_TIME_OFF_IS_ACTIVATED, TIME_OFF_IS_ACTIVATED);
			prefEditor.putBoolean(STR_DEACTIVATE_PLUGGED, DEACTIVATE_PLUGGED);
			prefEditor.putBoolean(STR_DEACTIVATE_ALL, DEACTIVATE_ALL);
			prefEditor.putBoolean(STR_DATA_ACTIVATION_DELAYED, DATA_ACTIVATION_DELAYED);
			prefEditor.putBoolean(STR_SCREEN_ON_ACTIVATION_DELAYED, SCREEN_ON_ACTIVATION_DELAYED);
			prefEditor.putBoolean(STR_FIRST_TIME_ON_IS_ACTIVATED, FIRST_TIME_ON_IS_ACTIVATED);
			prefEditor.putInt(STR_FIRST_TIME_ON_VALUE, FIRST_TIME_ON_VALUE);
			prefEditor.putBoolean(STR_CHECKING_AUTO_WIFI, CHECKING_AUTO_WIFI);
			prefEditor.putBoolean(STR_IS_2G_SWITCH_ACTIVATED, IS_2G_SWITCH_ACTIVATED);
			prefEditor.putInt(STR_ORIGINAL_PREFERRED_NETWORK_MODE, ORIGINAL_PREFERRED_NETWORK_MODE);
			prefEditor.putBoolean(STR_NETWORK_MODE_IS_SWITCHING, NETWORK_MODE_IS_SWITCHING);
			prefEditor.putBoolean(STR_IS_APPLICATION_ON_SET_IS_ACTIVATED, IS_APPLICATION_ON_SET_IS_ACTIVATED);
			prefEditor.putBoolean(STR_CHECK_NET_CONNECTION_WIFI, CHECK_NET_CONNECTION_WIFI);
			prefEditor.putBoolean(STR_NET_CONN_HAS_TO_BE_CHECKED, NET_CONN_HAS_TO_BE_CHECKED);
			prefEditor.putBoolean(STR_ENABLE_WHEN_KEYGUARD_OFF, ENABLE_WHEN_KEYGUARD_OFF);
			prefEditor.putBoolean(STR_LOGS_ENABLED, LOGS_ENABLED);
			prefEditor.putBoolean(STR_BLUETOOTH_ACTIVATION, dataConnectionState.isBluetoothChipEnabled());
			prefEditor.putBoolean(STR_BLUETOOTH_DEACTIVATE_SLEEP_MODE, BLUETOOTH_DEACTIVATE_SLEEP_MODE);
			prefEditor.putBoolean(STR_SHOW_NOTIFICATION, SHOW_NOTIFICATION);
			

			prefEditor.commit();
		}
	}

	public void resetPreferences() throws IOException {

		prefEditor.putInt(STR_TIME_ON, TIME_ON);
		prefEditor.putInt(STR_TIME_ON_CHECK, TIME_ON_CHECK);
		prefEditor.putInt(STR_TIME_OFF, TIME_OFF);
		prefEditor.putInt(STR_INTERVAL_CHECK, INTERVAL_CHECK);
		prefEditor.putInt(STR_SCREEN_DELAY_TIMER, SCREEN_DELAY_TIMER);
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
		prefEditor.putBoolean(STR_AUTO_SYNC_MGR_IS_ACTIVATED, AUTO_SYNC_MGR_IS_ACTIVATED);
		prefEditor.putBoolean(STR_SLEEP_IS_ACTIVATED, SLEEP_IS_ACTIVATED);
		prefEditor.putString(STR_SLEEP_OFF_HOUR, SLEEP_OFF);
		prefEditor.putString(STR_SLEEP_ON_HOUR, SLEEP_ON);
		prefEditor.putBoolean(STR_AUTO_WIFI_OFF_IS_ACTIVATED, AUTO_WIFI_OFF_IS_ACTIVATED);
		prefEditor.putBoolean(STR_AUTO_WIFI_ON_IS_ACTIVATED, AUTO_WIFI_ON_IS_ACTIVATED);
		prefEditor.putBoolean(STR_IS_SLEEPING, IS_SLEEPING);
		prefEditor.putBoolean(STR_TIME_OFF_IS_ACTIVATED, TIME_OFF_IS_ACTIVATED);
		prefEditor.putBoolean(STR_DEACTIVATE_PLUGGED, DEACTIVATE_PLUGGED);
		prefEditor.putBoolean(STR_DEACTIVATE_ALL, DEACTIVATE_ALL);
		prefEditor.putBoolean(STR_DATA_ACTIVATION_DELAYED, DATA_ACTIVATION_DELAYED);
		prefEditor.putBoolean(STR_SCREEN_ON_ACTIVATION_DELAYED, SCREEN_ON_ACTIVATION_DELAYED);
		prefEditor.putBoolean(STR_FIRST_TIME_ON_IS_ACTIVATED, FIRST_TIME_ON_IS_ACTIVATED);
		prefEditor.putInt(STR_FIRST_TIME_ON_VALUE, FIRST_TIME_ON_VALUE);
		prefEditor.putBoolean(STR_CHECKING_AUTO_WIFI, CHECKING_AUTO_WIFI);
		prefEditor.putBoolean(STR_IS_2G_SWITCH_ACTIVATED, IS_2G_SWITCH_ACTIVATED);
		prefEditor.putInt(STR_ORIGINAL_PREFERRED_NETWORK_MODE, ORIGINAL_PREFERRED_NETWORK_MODE);
		prefEditor.putBoolean(STR_NETWORK_MODE_IS_SWITCHING, NETWORK_MODE_IS_SWITCHING);
		prefEditor.putBoolean(STR_IS_APPLICATION_ON_SET_IS_ACTIVATED, IS_APPLICATION_ON_SET_IS_ACTIVATED);
		prefEditor.putBoolean(STR_CHECK_NET_CONNECTION_WIFI, CHECK_NET_CONNECTION_WIFI);
		prefEditor.putBoolean(STR_NET_CONN_HAS_TO_BE_CHECKED, NET_CONN_HAS_TO_BE_CHECKED);
		prefEditor.putBoolean(STR_ENABLE_WHEN_KEYGUARD_OFF, ENABLE_WHEN_KEYGUARD_OFF);
		prefEditor.putBoolean(STR_LOGS_ENABLED, LOGS_ENABLED);
		prefEditor.putBoolean(STR_BLUETOOTH_ACTIVATION, dataConnectionState.isBluetoothChipEnabled());
		prefEditor.putBoolean(STR_BLUETOOTH_DEACTIVATE_SLEEP_MODE, BLUETOOTH_DEACTIVATE_SLEEP_MODE);
		prefEditor.putBoolean(STR_SHOW_NOTIFICATION, SHOW_NOTIFICATION);
		prefEditor.commit();
	}

	public int getTimeOn() {
		return dataManagerSettings.getInt(STR_TIME_ON, TIME_ON);
	}
	
	public int getTimeOnCheck() {
		return dataManagerSettings.getInt(STR_TIME_ON_CHECK, TIME_ON_CHECK);
	}
	
	public boolean isLogsEnabled() {
		return dataManagerSettings.getBoolean(STR_LOGS_ENABLED, LOGS_ENABLED);
	}

	public int getTimeOff() {
		return dataManagerSettings.getInt(STR_TIME_OFF, TIME_OFF);
	}
	
	public int getScreenDelayTimer() {
		return dataManagerSettings.getInt(STR_SCREEN_DELAY_TIMER, SCREEN_DELAY_TIMER);
	}
	
	public boolean isEnabledWhenKeyguardOff()
	{
		return dataManagerSettings.getBoolean(STR_ENABLE_WHEN_KEYGUARD_OFF, ENABLE_WHEN_KEYGUARD_OFF);
	}
	
	public boolean getBluetoothActivation()
	{
		return dataManagerSettings.getBoolean(STR_BLUETOOTH_ACTIVATION, dataConnectionState.isBluetoothChipEnabled());
	}
	
	public boolean getBluetoothDeactivateDuringSleep()
	{
		return dataManagerSettings.getBoolean(STR_BLUETOOTH_DEACTIVATE_SLEEP_MODE, BLUETOOTH_DEACTIVATE_SLEEP_MODE);
	}
	
	public int getFirstTimeOn(){
		return dataManagerSettings.getInt(STR_FIRST_TIME_ON_VALUE, FIRST_TIME_ON_VALUE);
	}
	
	public int getOriginalPreferredMode(){
		return dataManagerSettings.getInt(STR_ORIGINAL_PREFERRED_NETWORK_MODE, ORIGINAL_PREFERRED_NETWORK_MODE);
	}
	
	public boolean isSleeping()
	{
		return dataManagerSettings.getBoolean(STR_IS_SLEEPING, IS_SLEEPING);
	}
	
	public boolean getCheckNetConnectionWifi()
	{
		return dataManagerSettings.getBoolean(STR_CHECK_NET_CONNECTION_WIFI, CHECK_NET_CONNECTION_WIFI);
	}
	
	public boolean is2GSwitchActivated()
	{
		return dataManagerSettings.getBoolean(STR_IS_2G_SWITCH_ACTIVATED, IS_2G_SWITCH_ACTIVATED);
	}
	
	public boolean is2GActivated()
	{
		return dataManagerSettings.getBoolean(STR_IS_2G_ACTIVATED, IS_2G_ACTIVATED);
	}
	
	public boolean isApplicationConnMgrActivated()
	{
		return dataManagerSettings.getBoolean(STR_IS_APPLICATION_ON_SET_IS_ACTIVATED, IS_APPLICATION_ON_SET_IS_ACTIVATED);
	}
	
	public boolean isFirstTimeOn()
	{
		return dataManagerSettings.getBoolean(STR_IS_FIRST_TIME_ON, IS_FIRST_TIME_ON);
	}
	
	public boolean isCheckingAutoWifiOn()
	{
		return dataManagerSettings.getBoolean(STR_CHECKING_AUTO_WIFI, CHECKING_AUTO_WIFI);
	}
	
	public boolean isFirstTimeOnIsActivated()
	{
		return dataManagerSettings.getBoolean(STR_FIRST_TIME_ON_IS_ACTIVATED, FIRST_TIME_ON_IS_ACTIVATED);
	}
	
	public boolean isNetworkModeSwitching()
	{
		return dataManagerSettings.getBoolean(STR_NETWORK_MODE_IS_SWITCHING, NETWORK_MODE_IS_SWITCHING);
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
	
	
	public boolean isTimeOffIsActivated() {
		return dataManagerSettings.getBoolean(STR_TIME_OFF_IS_ACTIVATED,
				TIME_OFF_IS_ACTIVATED);
	}
	
	
	public String getApplicationsOnSet()
	{
		return dataManagerSettings.getString(STR_APPLICATION_ON_SET,
				"");
	}
	

	public boolean isWifiActivated() {
		return dataManagerSettings.getBoolean(STR_WIFI_IS_ACTIVATED,
				dataConnectionState.isWifiChipActivated());
	}
	
	public boolean isDeactivatedWhilePlugged() {
		return dataManagerSettings.getBoolean(STR_DEACTIVATE_PLUGGED,
				DEACTIVATE_PLUGGED);
	}
	
	public boolean isAutoWifiOffActivated() {
		return dataManagerSettings.getBoolean(STR_AUTO_WIFI_OFF_IS_ACTIVATED,
				AUTO_WIFI_OFF_IS_ACTIVATED);
	}
	
	public boolean isAutoWifiOnActivated() {
		return dataManagerSettings.getBoolean(STR_AUTO_WIFI_ON_IS_ACTIVATED,
				AUTO_WIFI_ON_IS_ACTIVATED);
	}
	
	public boolean getNetHasToBeChecked() {
		return dataManagerSettings.getBoolean(STR_NET_CONN_HAS_TO_BE_CHECKED,
				NET_CONN_HAS_TO_BE_CHECKED);
	}
	
	public boolean isAutoSyncMgrIsActivated()
	{
		return dataManagerSettings.getBoolean(STR_AUTO_SYNC_MGR_IS_ACTIVATED, AUTO_SYNC_MGR_IS_ACTIVATED);
	}
	
	public boolean isSleepHoursActivated() {
		return dataManagerSettings.getBoolean(STR_SLEEP_IS_ACTIVATED,
				SLEEP_IS_ACTIVATED);
	}


	public boolean isWifiManagerActivated() {
		return dataManagerSettings.getBoolean(STR_WIFI_MANAGER_IS_ACTIVATED,
				WIFI_MGR_IS_ACTIVATED);
	}

	public boolean isServiceActivated() {
		return dataManagerSettings.getBoolean(STR_SERVICE_RUNNING,
				SERVICE_IS_STARTED);
	}
	
	public boolean isServiceDeactivatedAll() {
		return dataManagerSettings.getBoolean(STR_DEACTIVATE_ALL,
				DEACTIVATE_ALL);
	}
	
	public boolean isScreenOnDelayed() {
		return dataManagerSettings.getBoolean(STR_SCREEN_ON_ACTIVATION_DELAYED,
				SCREEN_ON_ACTIVATION_DELAYED);
	}
	
	public boolean isNotificationEnabled()
	{
		return dataManagerSettings.getBoolean(STR_SHOW_NOTIFICATION,
				SHOW_NOTIFICATION);
	}


	public boolean isAutoSyncActivated() {
		return dataManagerSettings.getBoolean(STR_AUTO_SYNC,
				dataConnectionState.isAutoSyncIsActivated());
	}
	
	public boolean isDataActivationDelayed() {
		return dataManagerSettings.getBoolean(STR_DATA_ACTIVATION_DELAYED, DATA_ACTIVATION_DELAYED);
	}
	
	public String getSleepTimeOn()
	{
		return dataManagerSettings.getString(STR_SLEEP_ON_HOUR, SLEEP_ON);
	}
	
	public String getSleepTimeOff()
	{
		return dataManagerSettings.getString(STR_SLEEP_OFF_HOUR, SLEEP_OFF);
	}
	
	public void setSleepTimeOn(String sleepTimeOn)
	{
		prefEditor.putString(STR_SLEEP_ON_HOUR, sleepTimeOn);
		prefEditor.commit();
	}
	

	
	public void setSleepTimeOff(String sleepTimeOff)
	{
		prefEditor.putString(STR_SLEEP_OFF_HOUR, sleepTimeOff);
		prefEditor.commit();
	}
	
	public void setIsCheckingAutoWifi(boolean isChecking)
	{
		prefEditor.putBoolean(STR_CHECKING_AUTO_WIFI, isChecking);
		prefEditor.commit();
	}
	
	public void setCheckNetConnectionWifi(boolean isChecking)
	{
		prefEditor.putBoolean(STR_CHECK_NET_CONNECTION_WIFI, isChecking);
		prefEditor.commit();
	}
	
	public void setBluetoothActivation(boolean isEnabled)
	{
		prefEditor.putBoolean(STR_BLUETOOTH_ACTIVATION, isEnabled);
		prefEditor.commit();
	}
	
	public void setBluetoothDeactivationDuringSleep(boolean isDisabled)
	{
		prefEditor.putBoolean(STR_BLUETOOTH_DEACTIVATE_SLEEP_MODE, isDisabled);
		prefEditor.commit();
	}
	
	public void set2GSwitchActivation(boolean isSwitchEnabled)
	{
		prefEditor.putBoolean(STR_IS_2G_SWITCH_ACTIVATED, isSwitchEnabled);
		prefEditor.commit();
	}
	
	public void setApplicationConnMgrActivatation(boolean isActivated)
	{
		prefEditor.putBoolean(STR_IS_APPLICATION_ON_SET_IS_ACTIVATED, isActivated);
		prefEditor.commit();
	}
	
	public void set2GActivation(boolean isActivated)
	{
		prefEditor.putBoolean(STR_IS_2G_ACTIVATED, isActivated);
		prefEditor.commit();
	}
	
	public void setNotificationActivation(boolean isActivated)
	{
		prefEditor.putBoolean(STR_SHOW_NOTIFICATION, isActivated);
		prefEditor.commit();
	}

	public void setTimeOn(int timeOn) {
		prefEditor.putInt(STR_TIME_ON, timeOn);
		prefEditor.commit();
	}
	
	public void setOriginalPreferredNetwork(int originalPreferredNetwork) {
		prefEditor.putInt(STR_ORIGINAL_PREFERRED_NETWORK_MODE, originalPreferredNetwork);
		prefEditor.commit();
	}
	
	public void setTimeOnCheck(int timeOn) {
		prefEditor.putInt(STR_TIME_ON_CHECK, timeOn);
		prefEditor.commit();
	}
	
	public void setFirstTimeOn(int firstTimeOn) {
		prefEditor.putInt(STR_FIRST_TIME_ON_VALUE, firstTimeOn);
		prefEditor.commit();
	}
	
	public void setScreenDelayTimer(int delayTimer) {
		prefEditor.putInt(STR_SCREEN_DELAY_TIMER, delayTimer);
		prefEditor.commit();
	}

	public void setTimeOff(int timeOff) {
		prefEditor.putInt(STR_TIME_OFF, timeOff);
		prefEditor.commit();
	}
	
	public void setIsFirstTimeOn(boolean isFirstTimeon) {
		prefEditor.putBoolean(STR_IS_FIRST_TIME_ON, isFirstTimeon);
		prefEditor.commit();
	}

	public void setIntervalCheckTime(int intervalCheckTime) {
		prefEditor.putInt(STR_INTERVAL_CHECK, intervalCheckTime);
		prefEditor.commit();
	}
	
	public void setLogsEnabled(boolean enabled) {
		prefEditor.putBoolean(STR_LOGS_ENABLED, enabled);
		prefEditor.commit();
	}

	public void setDataActivation(boolean isEnabled) {
		prefEditor.putBoolean(STR_DATA_IS_ACTIVATED, isEnabled);
		prefEditor.commit();
	}
	
	public void setFirstTimeOnIsActivated(boolean isEnabled) {
		prefEditor.putBoolean(STR_FIRST_TIME_ON_IS_ACTIVATED, isEnabled);
		prefEditor.commit();
	}
	
	public void setDeactivateWhilePlugged(boolean isEnabled) {
		prefEditor.putBoolean(STR_DEACTIVATE_PLUGGED, isEnabled);
		prefEditor.commit();
	}
	
	public void setDeactivateAll(boolean isEnabled) {
		prefEditor.putBoolean(STR_DEACTIVATE_ALL, isEnabled);
		prefEditor.commit();
	}

	public void setDataActivationManager(boolean isEnabled) {
		prefEditor.putBoolean(STR_DATA_MANAGER_IS_ACTIVATED, isEnabled);
		prefEditor.commit();
	}
	
	public void setEnabledWhenKeyguardOff(boolean isEnabled)
	{
		prefEditor.putBoolean(STR_ENABLE_WHEN_KEYGUARD_OFF, isEnabled);
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
	
	public void setAutoSyncMgrActivation(boolean isEnabled) {
		prefEditor.putBoolean(STR_AUTO_SYNC_MGR_IS_ACTIVATED, isEnabled);
		prefEditor.commit();
	}

	public void setServiceActivation(boolean isEnabled) {
		prefEditor.putBoolean(STR_SERVICE_RUNNING, isEnabled);
		prefEditor.commit();
	}
	
	public void setScreenOnIsDelayed(boolean isDelayed) {
		prefEditor.putBoolean(STR_SCREEN_ON_ACTIVATION_DELAYED, isDelayed);
		prefEditor.commit();
	}
	
	
	public void setDataActivationDelayed(boolean isEnabled) {
		prefEditor.putBoolean(STR_DATA_ACTIVATION_DELAYED, isEnabled);
		prefEditor.commit();
	}
	
	
	public void setNetworkModeIsSwitching(boolean isSwitching) {
		prefEditor.putBoolean(STR_NETWORK_MODE_IS_SWITCHING, isSwitching);
		prefEditor.commit();
	}
	
	
	public void setTimeOffActivation(boolean isEnabled) {
		prefEditor.putBoolean(STR_TIME_OFF_IS_ACTIVATED, isEnabled);
		prefEditor.commit();
	}
	
	public void setSleepHoursActivation(boolean isEnabled)
	{
		prefEditor.putBoolean(STR_SLEEP_IS_ACTIVATED, isEnabled);
		prefEditor.commit();
	}
	
	public void setIsSleeping(boolean isSleeping)
	{
		prefEditor.putBoolean(STR_IS_SLEEPING, isSleeping);
		prefEditor.commit();
	}
	
	public void setAutoWifiOffActivation(boolean isEnabled)
	{
		prefEditor.putBoolean(STR_AUTO_WIFI_OFF_IS_ACTIVATED, isEnabled);
		prefEditor.commit();
	}
	
	public void setApplicationOnSet(String apps)
	{
		prefEditor.putString(STR_APPLICATION_ON_SET, apps);
		prefEditor.commit();
		
	}
	
	public void setNetConnHasToBeChecked(boolean hasToBeChecked)
	{
		prefEditor.putBoolean(STR_NET_CONN_HAS_TO_BE_CHECKED, hasToBeChecked);
		prefEditor.commit();
		
	}
	
	
	public void setAutoWifiOnActivation(boolean isEnabled)
	{
		prefEditor.putBoolean(STR_AUTO_WIFI_ON_IS_ACTIVATED, isEnabled);
		prefEditor.commit();
	}

	public void setAllValues(int timeOn, int timeOff, int checkTime,
			boolean dataIsEnabled, boolean dataMgrIsEnabled,
			boolean wifiIsEnabled, boolean wifiMgrIsEnabled, boolean autoSyncIsActivated,
			boolean autoWifiOffIsActivated, boolean sleepHoursIsActivated, boolean isAutoSyncMgrIsActivated, 
			boolean serviceIsDeactivated, boolean serviceIsDeactivatedWhilePlugged, int timeOnCheck, int screenDelayTimer,
			boolean isFirsTimeOnIsActivated, int firstTimeOnValue,
			boolean isAutoWifiOnIsActivated, boolean is2GSwitchActivate, boolean checkNetConnWifi,
			boolean enableServiceWhenKeyguardOff, boolean logsAreEnabled, boolean bluetoothOffSleep) {
		
		prefEditor.putInt(STR_TIME_ON, timeOn);
		prefEditor.putInt(STR_TIME_ON_CHECK, timeOnCheck);
		prefEditor.putInt(STR_TIME_OFF, timeOff);
		prefEditor.putInt(STR_SCREEN_DELAY_TIMER, screenDelayTimer);
		prefEditor.putInt(STR_INTERVAL_CHECK, checkTime);
		prefEditor.putBoolean(STR_DATA_IS_ACTIVATED, dataIsEnabled);
		prefEditor.putBoolean(STR_DATA_MANAGER_IS_ACTIVATED, dataMgrIsEnabled);
		prefEditor.putBoolean(STR_WIFI_IS_ACTIVATED, wifiIsEnabled);
		prefEditor.putBoolean(STR_WIFI_MANAGER_IS_ACTIVATED, wifiMgrIsEnabled);
		prefEditor.putBoolean(STR_AUTO_SYNC, autoSyncIsActivated);
		prefEditor.putBoolean(STR_SLEEP_IS_ACTIVATED, sleepHoursIsActivated);
		prefEditor.putBoolean(STR_AUTO_WIFI_OFF_IS_ACTIVATED, autoWifiOffIsActivated);
		prefEditor.putBoolean(STR_AUTO_SYNC_MGR_IS_ACTIVATED, isAutoSyncMgrIsActivated);
		prefEditor.putBoolean(STR_DEACTIVATE_ALL, serviceIsDeactivated);
		prefEditor.putBoolean(STR_DEACTIVATE_PLUGGED, serviceIsDeactivatedWhilePlugged);
		prefEditor.putBoolean(STR_SCREEN_ON_ACTIVATION_DELAYED, false);
		prefEditor.putBoolean(STR_FIRST_TIME_ON_IS_ACTIVATED, isFirsTimeOnIsActivated);
		prefEditor.putInt(STR_FIRST_TIME_ON_VALUE, firstTimeOnValue);
		prefEditor.putBoolean(STR_AUTO_WIFI_ON_IS_ACTIVATED, isAutoWifiOnIsActivated);
		prefEditor.putBoolean(STR_IS_2G_SWITCH_ACTIVATED, is2GSwitchActivate);
		prefEditor.putBoolean(STR_NETWORK_MODE_IS_SWITCHING, false);
		prefEditor.putBoolean(STR_CHECK_NET_CONNECTION_WIFI, checkNetConnWifi);
		prefEditor.putBoolean(STR_NET_CONN_HAS_TO_BE_CHECKED, false);
		prefEditor.putBoolean(STR_ENABLE_WHEN_KEYGUARD_OFF, enableServiceWhenKeyguardOff);
		prefEditor.putBoolean(STR_LOGS_ENABLED, logsAreEnabled);
		prefEditor.putBoolean(STR_BLUETOOTH_DEACTIVATE_SLEEP_MODE, bluetoothOffSleep);
		
		prefEditor.commit();
	}
	
	
	

}
