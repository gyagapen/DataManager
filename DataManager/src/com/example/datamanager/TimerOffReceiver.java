package com.example.datamanager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


public class TimerOffReceiver extends BroadcastReceiver {

	
	// SharedPreferences
		private SharedPreferences prefs = null;
		private SharedPrefsEditor sharedPrefsEditor = null;

		private DataActivation dataActivation;
		
		private TimersSetUp timerSetUp = null;
		
		private LogsProvider logsProvider;
	
	//this is executed when timer off is expired
	public void onReceive(Context context, Intent intent) {

		logsProvider = new LogsProvider(context);
		
		// shared prefs init
		prefs = context.getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				Activity.MODE_PRIVATE);
		dataActivation = new DataActivation(context);
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);
		
	    logsProvider.info("Alarme time off : time off is expired");
		
		
		//enable connectivity
		try {
			
			//if auto-wifi on is activated, will check for wifi availability
			if(!sharedPrefsEditor.isWifiActivated() && sharedPrefsEditor.isAutoWifiOnActivated())
			{
				//cheking wether to enable wifi if known networks are avalaible
				dataActivation.checkWifiScanResults(sharedPrefsEditor);
				
				//enable 3g and sync meanwhile
				if(sharedPrefsEditor.isAutoSyncActivated())
				{
					dataActivation.setAutoSync(true, sharedPrefsEditor, false);
				}
				
				/*if(sharedPrefsEditor.isDataActivated())
				{
					dataActivation.setMobileDataEnabled(true);
				}*/
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
