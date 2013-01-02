package com.example.datamanager;

import java.util.Timer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class ScreenReceiver extends BroadcastReceiver {

	private boolean screenOff;

	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;

	private DataActivation dataActivation;
	
	// Timers for screen delay on
	private Timer timerScreenDelay= null;
	private TimerScreenDelayTask timerScreenDelayTask = null;
	private int timeScreenDelay = 5000;

	@Override
	public void onReceive(Context context, Intent intent) {

		// if service is activated // shared prefs init
		prefs = context.getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				Activity.MODE_PRIVATE);
		dataActivation = new DataActivation(context);
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);

		// if service is running
		if (sharedPrefsEditor.isServiceActivated()) {

			if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {

				// initialize connectivity positions
				SaveConnectionPreferences connPrefs = new SaveConnectionPreferences(
						context);
				connPrefs.saveAllConnectionSettingInSharedPrefs();

				screenOff = true;

			} else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

				screenOff = false;

			}
			
			if(!screenOff && !sharedPrefsEditor.isScreenOnDelayed() && sharedPrefsEditor.getScreenDelayTimer() >0)
			{
				Log.i("Screen delay", "screen is delayed for: "+timeScreenDelay+"ms");
				
				/*timerScreenDelay = new Timer();
				timerScreenDelayTask = new TimerScreenDelayTask(context);
				
				sharedPrefsEditor.setScreenOnIsDelayed(true);
				timerScreenDelay.schedule(timerScreenDelayTask, timeScreenDelay);*/
				TimersSetUp timerSetUp = new TimersSetUp(context);
				timerSetUp.StartScreenDelayTimer();
			}
			else
			{

				if(!sharedPrefsEditor.isScreenOnDelayed())
				{
					Intent i = new Intent(context, MainService.class);
	
					i.putExtra("screen_state", screenOff);
					
					context.startService(i);
				}
			}

			

		}

	}

}
