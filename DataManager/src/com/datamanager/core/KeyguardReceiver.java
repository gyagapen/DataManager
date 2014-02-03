package com.datamanager.core;

import com.datamanager.tabActivities.AppLauncher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class KeyguardReceiver extends BroadcastReceiver {



	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;
	private DataActivation dataActivation;
	
	private LogsProvider logsProvider = null;


	public void onReceive(Context context, Intent intent) {

		// shared prefs init
		prefs = context.getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				AppLauncher.SHARED_ACCESS_MODE);
		dataActivation = new DataActivation(context);
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);
		
		logsProvider = new LogsProvider(context, this.getClass());	

		// if service is running
		if (sharedPrefsEditor.isServiceActivated()) {

			if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
				
				sharedPrefsEditor.setScreenOnIsDelayed(false);

				logsProvider.info("keyguard is off");

				boolean screenOff = false;

				if(!sharedPrefsEditor.isScreenOnDelayed() && sharedPrefsEditor.getScreenDelayTimer() >0)
				{
					logsProvider.info("screen is delayed for: "+sharedPrefsEditor.getScreenDelayTimer()+"ms");


					TimersSetUp timerSetUp = new TimersSetUp(context);
					timerSetUp.StartScreenDelayTimer();
				}
				else
				{

					if(!sharedPrefsEditor.isScreenOnDelayed())
					{
						// save the last screen state
						sharedPrefsEditor.setScrenWasOff(false);
						
						Intent i = new Intent(context, MainService.class);
						i.putExtra("screen_state", screenOff);
						context.startService(i);
					}
				}



			}

		}

	}

}
