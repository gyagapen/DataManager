package com.datamanager.core;

import com.datamanager.tabActivities.AppLauncher;

import android.app.Activity;
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
		
		logsProvider = new LogsProvider(getApplicationContext(), this.getClass());
		
		// shared prefs init
		prefs = getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				AppLauncher.SHARED_ACCESS_MODE);

		dataActivation = new DataActivation(getBaseContext());
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);
		
		if (!sharedPrefsEditor.isServiceActivated())
		{	
			sharedPrefsEditor.setDeactivateAll(false);
			AppLauncher.StartDataManagerService(this, sharedPrefsEditor, logsProvider);
			
		    logsProvider.info("shortcut : enable");
		}
		else
		{
			
			sharedPrefsEditor.setDeactivateAll(true);
			AppLauncher.stopDataManagerService(this, sharedPrefsEditor, logsProvider);
			
		    logsProvider.info("shortcut : disable");
		}
		
		finish();
		
		
		
	}

}
