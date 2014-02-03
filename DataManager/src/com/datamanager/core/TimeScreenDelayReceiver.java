package com.datamanager.core;

import com.datamanager.tabActivities.AppLauncher;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * When screen delay is past
 * 
 * @author Gui
 * 
 */
public class TimeScreenDelayReceiver extends BroadcastReceiver {

	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;

	private DataActivation dataActivation = null;

	private LogsProvider logsProvider = null;

	public void onReceive(Context context, Intent intent) {

		logsProvider = new LogsProvider(context, this.getClass());

		// shared prefs init
		prefs = context.getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				AppLauncher.SHARED_ACCESS_MODE);
		dataActivation = new DataActivation(context);
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);

		logsProvider.info("Screen on task : screen is "
				+ dataActivation.isScreenIsOn());
		sharedPrefsEditor.setScreenOnIsDelayed(false);

		// if screen is still on
		if (dataActivation.isScreenIsOn()) {

			// not enabling connectivity when keyguard is off or keyguard
			// is not active
			if (!(sharedPrefsEditor.isEnabledWhenKeyguardOff() && dataActivation
					.isKeyguardIsActive())) {

				// save the last screen state
				sharedPrefsEditor.setScrenWasOff(false);

				logsProvider.info("Screen on task : screen is on");
				Intent i = new Intent(context, MainService.class);
				i.putExtra("screen_state", false);
				context.startService(i);

			}

		}

	}

}
