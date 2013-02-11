package com.example.datamanager;

import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class BatteryPluggedReceiver extends BroadcastReceiver {
	
	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;

	private DataActivation dataActivation;
	
	LogsProvider logsProvider = null;

	public void onReceive(Context context, Intent intent) {

		// shared prefs init
		prefs = context.getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				Activity.MODE_PRIVATE);
		dataActivation = new DataActivation(context);
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);
		
		logsProvider = new LogsProvider(context);


		if(!sharedPrefsEditor.isServiceDeactivatedAll() && sharedPrefsEditor.isDeactivatedWhilePlugged())
		{

			
			String action = intent.getAction();

		    if(action.equals(Intent.ACTION_POWER_CONNECTED)) {
		    	logsProvider.info("Phone is plugged");
				
				//deactivate service
		    	try {
					dataActivation.setConnectivityEnabled(sharedPrefsEditor);
				} catch (Exception e) {
					e.printStackTrace();
				}
				MainActivity.stopDataManagerService(context, sharedPrefsEditor);
	
		    }
		    else if(action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
		    	logsProvider.info("Phone is UNplugged");
				
				MainActivity.StartDataManagerService(context, sharedPrefsEditor);
				
		    }
		}
	}
}
