package com.datamanager.core;

import com.datamanager.tabActivities.AppLauncher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.TrafficStats;

public class TimerCheckReceiver extends BroadcastReceiver {

	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;

	private LogsProvider logsProvider = null;

	private DataActivation dataActivation;

	private TimersSetUp timerSetUp;

	// nb of bytes sent and received at the beginning of the timer
	private long nbBytesSent1 = 0;
	private long nbBytesReceived1 = 0;

	// nb of bytes sent and received at the end of the timer
	private long nbBytesReceived2 = 0;
	private long nbBytesSent2 = 0;

	// limit, above that number of bytes, will consider that data is used
	private int bytesLimit = 7000;

	// executed when timer check is expired
	public void onReceive(Context context, Intent intent) {

		// init
		logsProvider = new LogsProvider(context, this.getClass());

		// shared prefs init
		prefs = context.getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				AppLauncher.SHARED_ACCESS_MODE);
		dataActivation = new DataActivation(context);
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);
		timerSetUp = new TimersSetUp(context);

		logsProvider.info("Timer data usage check is expired");

		// check if data threshold is passed
		nbBytesSent1 = intent.getLongExtra("nbBytesSent1", 0);
		nbBytesReceived1 = intent.getLongExtra("nbBytesReceived1", 0);

		nbBytesReceived2 = TrafficStats.getTotalRxBytes();
		nbBytesSent2 = TrafficStats.getTotalTxBytes();

		int bytesUsed = (int) (nbBytesReceived2 - nbBytesReceived1);

		logsProvider.info("Data usage received " + String.valueOf(bytesUsed));

		boolean dataIsReceived = (bytesUsed > bytesLimit);

		boolean dataIsUsed = true;

		// if no data received then check if data is sent
		if (!dataIsReceived) {
			bytesUsed = (int) (nbBytesSent2 - nbBytesSent1);
			dataIsUsed = (bytesUsed > bytesLimit);
			logsProvider.info("Data usage sent " + String.valueOf(bytesUsed));
		} else {
			// data connection is used
			dataIsUsed = true;
		}
		
		//start next timer if necesary
		startNexttimer(sharedPrefsEditor,context,dataIsUsed);
	}

	/**
	 * Decide the timer to start depending if data is used or not
	 * 
	 * @param sharedPrefsEditor
	 * @param context
	 */
	public void startNexttimer(SharedPrefsEditor sharedPrefsEditor,
			Context context, boolean dataIsUsed) {

		// interval to delay timer off when data is used
		int delayTimeOff = sharedPrefsEditor.getTimeOnCheck();

		timerSetUp = new TimersSetUp(context);

		// if data is used
		if (dataIsUsed) {
			// data is used
			logsProvider.info("Data usage : Data USED");

			// reset timerOn
			timerSetUp.CancelTimerOn();
			timerSetUp.StartTimerOn(delayTimeOff);
		} else {
			// data not used
			// cancel timer On
			timerSetUp.CancelTimerOn();

			// start timer Off
			timerSetUp.StartTimerOff();

			logsProvider.info("Data usage : Data NOT used");

			try {

				// auto wifi off
				if (sharedPrefsEditor.isAutoWifiOffActivated()
						&& sharedPrefsEditor.isWifiActivated()) {
					logsProvider.info("Auto Wifi Off : Launch check");
					dataActivation.checkWifiScanResults(sharedPrefsEditor);

					// auto sync off
					if (sharedPrefsEditor.isDataMgrActivated()) {
						dataActivation.setMobileDataEnabled(false,
								sharedPrefsEditor);
					}

					if (sharedPrefsEditor.isAutoSyncMgrIsActivated()) {
						dataActivation.setAutoSync(false, sharedPrefsEditor,
								false);
					}
				} else {
					// wifi and 3g off
					dataActivation.setConnectivityDisabled(sharedPrefsEditor);

				}

			} catch (Exception e) {
				logsProvider.error(e);
			}

		}

	}

}
