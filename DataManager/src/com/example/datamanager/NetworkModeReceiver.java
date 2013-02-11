package com.example.datamanager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class NetworkModeReceiver extends BroadcastReceiver {

	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;

	private LogsProvider logsProvider = null;
	private DataActivation dataActivation;
	
	//for 2g switch
	private ChangeNetworkMode changeNetworkMode = null;
	
	public void onReceive(Context context, Intent intent) {
		
		logsProvider = new LogsProvider(context);
		
		prefs = context.getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				Activity.MODE_PRIVATE);
		dataActivation = new DataActivation(context);
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);
		
		
		if(sharedPrefsEditor.is2GSwitchActivated())
		{
		
			sharedPrefsEditor.setNetworkModeIsSwitching(false);
			
			changeNetworkMode = new ChangeNetworkMode(context);
			
			logsProvider.info("network receiver received");
			
			if(sharedPrefsEditor.is2GActivated())
			{
				logsProvider.info("2G real activation");
				
				
				//check if we have to switch to 3G
				if(dataActivation.isScreenIsOn() && sharedPrefsEditor.is2GSwitchActivated())
				{
					changeNetworkMode.switchTo3GIfNecesary();
				}
				else
				{
					//check if data activation is needed
					if(sharedPrefsEditor.isDataActivated())
					{
						try {
							dataActivation.setMobileDataEnabled(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			else
			{
				logsProvider.info("3G real activation");

				//check if we have to switch to 2g
				if(!dataActivation.isScreenIsOn() && sharedPrefsEditor.is2GSwitchActivated())
				{
					changeNetworkMode.switchTo2GIfNecesary();
				}
				else
				{
					//check if data activation is needed
					if(sharedPrefsEditor.isDataActivated())
					{
						try {
							dataActivation.setMobileDataEnabled(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				
				
			}
		
		}
	}

}
