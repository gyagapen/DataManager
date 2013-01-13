package com.example.datamanager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;


public class TimerOffReceiver extends BroadcastReceiver {

	
	// SharedPreferences
		private SharedPreferences prefs = null;
		private SharedPrefsEditor sharedPrefsEditor = null;

		private DataActivation dataActivation;
		
		private TimersSetUp timerSetUp = null;
	
	//this is executed when timer off is expired
	public void onReceive(Context context, Intent intent) {
		
		// shared prefs init
		prefs = context.getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				Activity.MODE_PRIVATE);
		dataActivation = new DataActivation(context);
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);
		
		Log.i("Alarme time off", "time off is expired");
		
		
		//enable connectivity
		try {
			
			//if auto-wifi on is activated, will check for wifi availability
			if(!sharedPrefsEditor.isWifiActivated() && sharedPrefsEditor.isAutoWifiOnActivated())
			{
				//cheking wether to enable wifi if known networks are avalaible
				dataActivation.checkWifiScanResults(sharedPrefsEditor);
				
				//enable 3g and sync meanwhile
				dataActivation.setAutoSync(true, sharedPrefsEditor, false);
				dataActivation.setMobileDataEnabled(true);
			}
			else
			{
				dataActivation.setConnectivityEnabled(sharedPrefsEditor);
			}

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		timerSetUp = new TimersSetUp(context);
		
		//cancel timerOff
		timerSetUp.CancelTimeOff();
		
		//reset timerOn
		timerSetUp.CancelTimerOn();
		timerSetUp.StartTimerOn();

	}
	
	

}
