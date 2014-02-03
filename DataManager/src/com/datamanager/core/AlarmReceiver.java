package com.datamanager.core;


import com.datamanager.tabActivities.AppLauncher;
import com.gyagapen.cleverconnectivity.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;

public class AlarmReceiver extends BroadcastReceiver {


	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;

	private DataActivation dataActivation;
	private LogsProvider logsProvider = null;

	@Override
	public void onReceive(Context context, Intent intent) {



		// shared prefs init
		prefs = context.getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				AppLauncher.SHARED_ACCESS_MODE);
		dataActivation = new DataActivation(context);
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);

		logsProvider = new LogsProvider(context, this.getClass());

		// if service is active
		if (sharedPrefsEditor.isServiceActivated()) {

			Bundle bundle = intent.getExtras();
			boolean activateConnectivity = bundle
					.getBoolean(AppLauncher.STR_ACTIVATE_CONNECTIVITY);

			logsProvider.info("Recurring alarm; activate  all connectivity : "
					+ String.valueOf(activateConnectivity));

			// if we have to desactivate connectivity, we save it in shared
			// preferences
			if (!activateConnectivity) {

				sharedPrefsEditor.setSleepTimeOnIsCurrentlyActivated(true);
				MainService.showNotification(context.getString(R.string.notif_running),context.getString(R.string.notif_sleep_on), context,logsProvider,sharedPrefsEditor);
				setSleepHoursOn(sharedPrefsEditor, context, dataActivation, logsProvider);

			} else {


				//if battery level is ok
				if(!sharedPrefsEditor.isBatteryCurrentlyLow())
				{					sharedPrefsEditor.setSleepTimeOnIsCurrentlyActivated(false);

				MainService.showNotification(context.getString(R.string.notif_running),context.getString(R.string.notif_sleep_off), context,logsProvider,sharedPrefsEditor);
				setSleepHoursOff(sharedPrefsEditor, context, dataActivation, logsProvider);
				}
			}

		}

	}

	public static void setSleepHoursOn(SharedPrefsEditor shPref, Context aContext, DataActivation dActivation, LogsProvider logsProvider)
	{
		// is sleeping
		shPref.setIsSleeping(true);

		// disable all connectivity if screen is off
		PowerManager pm = (PowerManager) aContext
				.getSystemService(Context.POWER_SERVICE);

		if (!pm.isScreenOn()) {
			try {
				dActivation
				.setConnectivityDisabled(shPref);

				//cancel timers
				TimersSetUp timersSetUp = new TimersSetUp(aContext);
				timersSetUp.CancelTimerOn();
				timersSetUp.CancelTimeOff();

			} catch (Exception e) {
				logsProvider.error(e);
			}
		}

	}


	public static void setSleepHoursOff(SharedPrefsEditor shPref, Context aContext, DataActivation dActivation, LogsProvider logsProvider)
	{
		// sleep off
		shPref.setIsSleeping(false);

		// start service
		Intent i = new Intent(aContext, MainService.class);

		//if screen is off and battery level is ok
		if(!dActivation.isScreenIsOn() )
		{

			//set first time on to false
			shPref.setIsFirstTimeOn(false);

			//restart the cycle
			try {

				dActivation.setConnectivityEnabled(shPref);
			} catch (Exception e) {
				logsProvider.error(e);
			}
			i.putExtra("screen_state", true);
		}

		aContext.startService(i);

	}

}
