package com.datamanager.core;

import com.datamanager.tabActivities.AppLauncher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.TrafficStats;

public class TimerOnReceiver extends BroadcastReceiver {

	// interval when 3G usage is cheked (ms)

	// limit, above that number of bytes, will consider that data is used
	private int bytesLimit = 7000;

	// delay timer on expiration if selected app is running - 5min
	private static int TIME_ON_APP_IS_RUNNING = 5;

	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;

	private LogsProvider logsProvider = null;

	// interval to delay timer off when data is used
	private int delayTimeOff = 1;

	private DataActivation dataActivation;

	private TimersSetUp timerSetUp;

	// this is executed when timer on is expired
	public void onReceive(Context context, Intent intent) {

		logsProvider = new LogsProvider(context, this.getClass());

		// shared prefs init
		prefs = context.getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				AppLauncher.SHARED_ACCESS_MODE);
		dataActivation = new DataActivation(context);
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);
		timerSetUp = new TimersSetUp(context);

		logsProvider.info("AlarmReceiverTimeOn : time on is expired");

		// getting dataInterval for check data usage timer (seconds)
		int dataInterval = sharedPrefsEditor.getIntervalCheck();

		ApplicationsManager appManager = new ApplicationsManager(context);

		if (sharedPrefsEditor.isApplicationConnMgrActivated()) {
			logsProvider
					.info("verifying if selected app is running in background");

			// if one of the selected app is running
			if (appManager.isSelectedAppIsRunning(sharedPrefsEditor)) {

				logsProvider
						.info("app is running in background, timer on resetted");

				// reset timerOn
				timerSetUp.CancelTimerOn();
				timerSetUp.StartTimerOn();
			} else {
				timerSetUp.StartTimerCheck(dataInterval);
			}
		} else {
			timerSetUp.StartTimerCheck(dataInterval);
		}

	}

	/*
	 * public void checkDataUsage(SharedPrefsEditor sharedPrefsEditor, Context
	 * context) { //getting dataInterval int dataInterval =
	 * sharedPrefsEditor.getIntervalCheck() * 1000;
	 * 
	 * //interval to delay timer off when data is used delayTimeOff =
	 * sharedPrefsEditor.getTimeOnCheck();
	 * 
	 * //check data usage boolean dataIsUsed = IsDataUsed(dataInterval);
	 * 
	 * timerSetUp = new TimersSetUp(context);
	 * 
	 * //if data is used if(dataIsUsed) { //data is used
	 * logsProvider.info("Data usage : Data USED");
	 * 
	 * //reset timerOn timerSetUp.CancelTimerOn();
	 * timerSetUp.StartTimerOn(delayTimeOff); } else { //data not used //cancel
	 * timer On timerSetUp.CancelTimerOn();
	 * 
	 * //start timer Off timerSetUp.StartTimerOff();
	 * 
	 * logsProvider.info("Data usage : Data NOT used");
	 * 
	 * try {
	 * 
	 * //auto wifi off if(sharedPrefsEditor.isAutoWifiOffActivated() &&
	 * sharedPrefsEditor.isWifiActivated()) {
	 * logsProvider.info("Auto Wifi Off : Launch check");
	 * dataActivation.checkWifiScanResults(sharedPrefsEditor);
	 * 
	 * //auto sync off if(sharedPrefsEditor.isDataMgrActivated()) {
	 * dataActivation.setMobileDataEnabled(false, sharedPrefsEditor); }
	 * 
	 * if(sharedPrefsEditor.isAutoSyncMgrIsActivated()) {
	 * dataActivation.setAutoSync(false, sharedPrefsEditor, false); } } else {
	 * //wifi and 3g off
	 * dataActivation.setConnectivityDisabled(sharedPrefsEditor);
	 * 
	 * }
	 * 
	 * 
	 * 
	 * } catch (Exception e) { logsProvider.error(e); }
	 * 
	 * 
	 * 
	 * } }
	 * 
	 * public boolean IsDataUsed(int dataInterval) { boolean dataIsUsed = false;
	 * 
	 * //workaround for bug if(dataInterval > 8000) { dataInterval = 8000; }
	 * 
	 * if (dataInterval > 0) { // get number of bytes received and sent long
	 * nbBytesSent1 = TrafficStats.getTotalTxBytes(); long nbBytesReceived1 =
	 * TrafficStats.getTotalRxBytes();
	 * 
	 * // get number of bytes received 5 second later try {
	 * Thread.sleep(dataInterval); } catch (InterruptedException e) {
	 * logsProvider.error(e); }
	 * 
	 * long nbBytesReceived2 = TrafficStats.getTotalRxBytes(); long nbBytesSent2
	 * = TrafficStats.getTotalTxBytes();
	 * 
	 * int bytesUsed = (int) (nbBytesReceived2 - nbBytesReceived1);
	 * 
	 * logsProvider.info("Data usage received "+String.valueOf(bytesUsed));
	 * 
	 * boolean dataIsReceived = (bytesUsed > bytesLimit);
	 * 
	 * // if no data received then check if data is sent if (!dataIsReceived) {
	 * bytesUsed = (int) (nbBytesSent2 - nbBytesSent1); dataIsUsed = (bytesUsed
	 * > bytesLimit);
	 * logsProvider.info("Data usage sent "+String.valueOf(bytesUsed)); } else {
	 * // data connection is used dataIsUsed = true; }
	 * 
	 * }
	 * 
	 * return dataIsUsed;
	 * 
	 * }
	 */

}
