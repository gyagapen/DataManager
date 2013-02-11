package com.example.datamanager;

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

		logsProvider = new LogsProvider(context);
		
		// shared prefs init
		prefs = context.getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				Activity.MODE_PRIVATE);
		dataActivation = new DataActivation(context);
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);


		// if service is running
		if (sharedPrefsEditor.isServiceActivated()) {

			//screen off
			if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {

				// initialize connectivity positions
				SaveConnectionPreferences connPrefs = new SaveConnectionPreferences(
						context);
				connPrefs.saveAllConnectionSettingInSharedPrefs();

				screenOff = true;

				//set is firt time on to true
				sharedPrefsEditor.setIsFirstTimeOn(true);

				if(!sharedPrefsEditor.isScreenOnDelayed())
				{
					Intent i = new Intent(context, MainService.class);

					i.putExtra("screen_state", screenOff);

					context.startService(i);
				}



			} else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

				//if not enabling connectivity when keyguard is off
				if(!sharedPrefsEditor.isEnabledWhenKeyguardOff())
				{
					screenOff = false;

					if(!sharedPrefsEditor.isScreenOnDelayed() && sharedPrefsEditor.getScreenDelayTimer() >0)
					{
						logsProvider.info("Screen delay : screen is delayed for: "+sharedPrefsEditor.getScreenDelayTimer()+"ms");


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
				else //keyguard connectivity
				{
					sharedPrefsEditor.setScreenOnIsDelayed(true);
				}

			}

			logsProvider.info("screen is "+!screenOff);




		}

	}

}
