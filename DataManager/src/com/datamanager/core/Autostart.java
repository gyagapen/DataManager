package com.datamanager.core;

import com.datamanager.tabActivities.AppLauncher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Called when android starts
 * 
 * @author Gui
 * 
 */
public class Autostart extends BroadcastReceiver {

	// SharedPreferences
	SharedPreferences prefs = null;
	SharedPrefsEditor sharedPrefsEditor = null;

	public void onReceive(Context arg0, Intent arg1) {
		
		//initialize connectivity positions
		SaveConnectionPreferences connPrefs = new SaveConnectionPreferences(arg0);
		connPrefs.saveAllConnectionSettingInSharedPrefs();
		
		// shared prefs init
		DataActivation dataActivation = new DataActivation(arg0);
		prefs = arg0.getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				AppLauncher.SHARED_ACCESS_MODE);
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);
		

		// if service is activated
		if (sharedPrefsEditor.isServiceActivated()) {

			// save the last screen state
			sharedPrefsEditor.setScrenWasOff(false);
			
			//start data manager service
			Intent intent = new Intent(arg0, MainService.class);
			arg0.startService(intent);

		}
	}

}
