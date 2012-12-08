package com.example.datamanager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
	
	   private static final String DEBUG_TAG = "AlarmReceiver";
	   
		// SharedPreferences
		private SharedPreferences prefs = null;
		private SharedPrefsEditor sharedPrefsEditor = null;

		private DataActivation dataActivation;
	   
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        
	    	Log.d(DEBUG_TAG, "Recurring alarm; activate or desactivate all connectivity");
	    	
			// shared prefs init
			prefs = context.getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
					Activity.MODE_PRIVATE);
			dataActivation = new DataActivation(context);
			sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);
	    	
	    	Bundle bundle = intent.getExtras();
	    	boolean activateConnectivity = bundle.getBoolean(MainActivity.STR_ACTIVATE_CONNECTIVITY);
	    	
	    	//if we have to desactivate connectivity, we save it in shared preferences
	    	if(!activateConnectivity)
	    	{
	    		//is sleeping
	    		sharedPrefsEditor.setIsSleeping(true);
	    	}
	    	else
	    	{
	    		//sleep off
	    		sharedPrefsEditor.setIsSleeping(false);
	    	}

	        
	    	
	        
	    }

}
