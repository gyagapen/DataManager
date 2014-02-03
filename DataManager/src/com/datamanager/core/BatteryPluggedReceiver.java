package com.datamanager.core;

import com.datamanager.tabActivities.AppLauncher;

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
				AppLauncher.SHARED_ACCESS_MODE);
		dataActivation = new DataActivation(context);
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);
		
		logsProvider = new LogsProvider(context, this.getClass());


		if(!sharedPrefsEditor.isServiceDeactivatedAll() && sharedPrefsEditor.isDeactivatedWhilePlugged())
		{

			
			String action = intent.getAction();

		    if(action.equals(Intent.ACTION_POWER_CONNECTED)) {
		    	logsProvider.info("Phone is plugged");
				
				//deactivate service
		    	try {
					dataActivation.setConnectivityEnabled(sharedPrefsEditor);
				} catch (Exception e) {
					logsProvider.error(e);
				}
				AppLauncher.stopDataManagerService(context, sharedPrefsEditor, logsProvider);
	
		    }
		    else if(action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
		    	logsProvider.info("Phone is UNplugged");
				
				AppLauncher.StartDataManagerService(context, sharedPrefsEditor, logsProvider);
				
		    }
		}
	}
}
