package com.datamanager.core;

import com.datamanager.tabActivities.AppLauncher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class ServiceDestroyReceiver extends BroadcastReceiver {

	private LogsProvider logsProvider = null;

	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;
	private DataActivation dataActivation;

	public void onReceive(Context context, Intent intent) {

		logsProvider = new LogsProvider(context, this.getClass());



		// shared prefs init
		prefs = context.getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				AppLauncher.SHARED_ACCESS_MODE);

		dataActivation = new DataActivation(context);
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);
		
		logsProvider.info("ServeiceDestroy onReceive...");
		logsProvider.info("action:" + intent.getAction());

		// start data manager service if activated
		if (sharedPrefsEditor.isServiceActivated()) {
			

			logsProvider.info("ServeiceDestroy auto start service...");
			
			Intent serviceIntent = new Intent(context, MainService.class);
			serviceIntent.putExtra("screen_state",
					!dataActivation.isScreenIsOn());
			context.startService(serviceIntent);
		}
		else
		{
			logsProvider.info("ServeiceDestroy kill service as deactivated...");
			//kill service
			AppLauncher.stopDataManagerService(context, sharedPrefsEditor, logsProvider);
		}

	}

}
