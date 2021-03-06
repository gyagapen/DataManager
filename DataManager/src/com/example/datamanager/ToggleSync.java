package com.example.datamanager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class ToggleSync extends BroadcastReceiver {

	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;

	private DataActivation dataActivation;

	@Override
	public void onReceive(Context context, Intent intent) {

		// shared prefs init
		prefs = context.getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				Activity.MODE_PRIVATE);
		dataActivation = new DataActivation(context);
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);

		// if screen is On
		if (dataActivation.isScreenIsOn()) {
			
			Log.i("SYNC Receiver", "Sync state changed");
			
			// get auto sync state
			boolean autoSyncIsActivated = dataActivation
					.isAutoSyncIsActivated();

			// setting saved in shared prefs
			sharedPrefsEditor.setAutoSyncActivation(autoSyncIsActivated);
		}

	}

}
