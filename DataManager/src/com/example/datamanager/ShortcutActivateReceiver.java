package com.example.datamanager;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class ShortcutActivateReceiver extends Activity {
	
	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;

	private DataActivation dataActivation;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		// shared prefs init
		prefs = getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				Activity.MODE_PRIVATE);

		dataActivation = new DataActivation(getBaseContext());
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);
		
		if (!sharedPrefsEditor.isServiceActivated())
		{	
			sharedPrefsEditor.setServiceActivation(true);
			MainActivity.StartDataManagerService(this);
			
			Log.i("CConnectivity", "shortcut : enable");
		}
		else
		{
			
			sharedPrefsEditor.setServiceActivation(false);
			MainActivity.stopDataManagerService(this);
			
			Log.i("CConnectivity", "shortcut : disable");
		}
		
		finish();
		
		
		
	}

}
