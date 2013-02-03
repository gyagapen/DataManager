package com.example.datamanager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import com.gyagapen.cleverconnectivity.R;

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
				Activity.MODE_PRIVATE);
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);

		// if service is activated
		if (sharedPrefsEditor.isServiceActivated()) {

			// sleeping hours
			AlarmMgr alarmMgr = new AlarmMgr(arg0, sharedPrefsEditor);
			alarmMgr.manageSleepingHours(sharedPrefsEditor.getSleepTimeOff(),
					sharedPrefsEditor.getSleepTimeOn());
			
			//start data manager service
			Intent intent = new Intent(arg0, MainService.class);
			arg0.startService(intent);
			Log.i("Autostart", "started");

		}
	}
	

}
