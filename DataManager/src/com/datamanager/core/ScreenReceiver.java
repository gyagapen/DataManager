package com.datamanager.core;

import com.datamanager.tabActivities.AppLauncher;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class ScreenReceiver extends BroadcastReceiver {

	private boolean screenOff;

	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;
	private DataActivation dataActivation;

	private LogsProvider logsProvider = null;

	@Override
	public void onReceive(Context context, Intent intent) {

		logsProvider = new LogsProvider(context, this.getClass());

		// shared prefs init
		prefs = context.getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				AppLauncher.SHARED_ACCESS_MODE);
		dataActivation = new DataActivation(context);
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);

		logsProvider.info("Screen was off: "+sharedPrefsEditor.wasScreenOff() + " screenOnDelay: "+sharedPrefsEditor.isScreenOnDelayed());
		
		// if service is running
		if (sharedPrefsEditor.isServiceActivated()) {

			// screen off
			if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {

				if (!sharedPrefsEditor.isScreenOnDelayed()) {
					// if the screen wasn't previously off: avoid duplicate
					// event
					if (!sharedPrefsEditor.wasScreenOff()) {

						// save the last screen state
						sharedPrefsEditor.setScrenWasOff(true);
						logsProvider.info("set Screen was off to TRUE");
						
						// initialize connectivity positions
						SaveConnectionPreferences connPrefs = new SaveConnectionPreferences(
								context);
						connPrefs.saveAllConnectionSettingInSharedPrefs();

						screenOff = true;

						// set is firt time on to true
						sharedPrefsEditor.setIsFirstTimeOn(true);


						Intent i = new Intent(context, MainService.class);
						i.putExtra("screen_state", screenOff);
						context.startService(i);
					}
				}

			} else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

				logsProvider.info("screen is ON, isEnabledWhenKeyguardOff: "+sharedPrefsEditor.isEnabledWhenKeyguardOff()+", isKeyguardIsActive: "+dataActivation.isKeyguardIsActive());
				
				//  not enabling connectivity when keyguard is off or keyguard
				// is not active
				if (!(sharedPrefsEditor.isEnabledWhenKeyguardOff()
						&& dataActivation.isKeyguardIsActive())) {
					screenOff = false;

					logsProvider.info("SCREEN ON");
					
					if (!sharedPrefsEditor.isScreenOnDelayed()
							&& sharedPrefsEditor.getScreenDelayTimer() > 0) {
						logsProvider
								.info("Screen delay : screen is delayed for: "
										+ sharedPrefsEditor
												.getScreenDelayTimer() + "ms");

						TimersSetUp timerSetUp = new TimersSetUp(context);
						timerSetUp.StartScreenDelayTimer();
					} else {

						if (!sharedPrefsEditor.isScreenOnDelayed()) {
							// save the last screen state
							sharedPrefsEditor.setScrenWasOff(false);
							logsProvider.info("set Screen was off to FALSE");

							Intent i = new Intent(context, MainService.class);
							i.putExtra("screen_state", screenOff);
							context.startService(i);
						}
					}

				}
				/*
				 * else //keyguard connectivity {
				 * sharedPrefsEditor.setScreenOnIsDelayed(true); }
				 */

			}

			logsProvider.info("screen is " + !screenOff);

		}

	}
	

}
