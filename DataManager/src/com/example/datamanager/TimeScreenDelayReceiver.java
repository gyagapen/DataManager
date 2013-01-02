package com.example.datamanager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;


/**
 * When screen delay is past
 * @author Gui
 *
 */
public class TimeScreenDelayReceiver extends BroadcastReceiver{

	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;

	private DataActivation dataActivation = null;
	
	public void onReceive(Context context, Intent intent) {
		
		// shared prefs init
		prefs = context.getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				Activity.MODE_PRIVATE);
		dataActivation = new DataActivation(context);
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);
		
		Log.i("Screen on task", "screen is "+dataActivation.isScreenIsOn());
		
		//if screen is still on
		if(dataActivation.isScreenIsOn())
		{
		
			Log.i("Screen on task", "screen is on");
			
			Intent i = new Intent(context, MainService.class);
	
			i.putExtra("screen_state", false);
			
			context.startService(i);
			
			
		}
		
		sharedPrefsEditor.setScreenOnIsDelayed(false);
		
	}

	
}
