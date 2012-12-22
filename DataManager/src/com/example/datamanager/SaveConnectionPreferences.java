package com.example.datamanager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import com.gyagapen.cleverconnectivity.R;

public class SaveConnectionPreferences {


	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;

	private DataActivation dataActivation;

	public SaveConnectionPreferences(Context context) {

		// shared prefs init
		prefs = context.getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				Activity.MODE_PRIVATE);
		dataActivation = new DataActivation(context);
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);
	}
	
	public void saveAllConnectionSettingInSharedPrefs()
	{
		boolean autoSyncIsActivated = dataActivation.isAutoSyncIsActivated();
		sharedPrefsEditor.setAutoSyncActivation(autoSyncIsActivated);
		
		boolean wifiConnectionIsActivated = dataActivation.isWifiChipActivated();
		sharedPrefsEditor.setWifiActivation(wifiConnectionIsActivated); 
		
		boolean dataConnectionIsActivated = dataActivation.isDataChipActivated();
		sharedPrefsEditor.setDataActivation(dataConnectionIsActivated);
		
		
	}

}
