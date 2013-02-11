package com.example.datamanager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

public class ShortcutActivateReceiver extends Activity {
	
	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;

	private DataActivation dataActivation;
	private LogsProvider logsProvider = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		logsProvider = new LogsProvider(getApplicationContext());
		
		// shared prefs init
		prefs = getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				Activity.MODE_PRIVATE);

		dataActivation = new DataActivation(getBaseContext());
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);
		
		if (!sharedPrefsEditor.isServiceActivated())
		{	
			sharedPrefsEditor.setDeactivateAll(false);
			MainActivity.StartDataManagerService(this, sharedPrefsEditor);
			
		    logsProvider.info("shortcut : enable");
		}
		else
		{
			
			sharedPrefsEditor.setDeactivateAll(true);
			MainActivity.stopDataManagerService(this, sharedPrefsEditor);
			
		    logsProvider.info("shortcut : disable");
		}
		
		finish();
		
		
		
	}

}
