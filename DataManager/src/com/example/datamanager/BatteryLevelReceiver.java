package com.example.datamanager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class BatteryLevelReceiver extends BroadcastReceiver {

	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;

	private LogsProvider logsProvider = null;
	private DataActivation dataActivation;

	public void onReceive(Context context, Intent intent) {
		logsProvider = new LogsProvider(context, this.getClass());

		prefs = context.getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				Activity.MODE_PRIVATE);
		dataActivation = new DataActivation(context);
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);

		int bLevel = intent.getIntExtra("level", 0);
		
		if(bLevel <= sharedPrefsEditor.getBatteryLevelToMonitor())
		{
			batteryLevelIsLow(sharedPrefsEditor, context, dataActivation, logsProvider, bLevel);
		}
		else
		{
			batteryLevelIsNotLow(sharedPrefsEditor, context, dataActivation, logsProvider, bLevel);
		}

	}
	
	public static void batteryLevelIsLow(SharedPrefsEditor sharedPrefsEditor, Context context, DataActivation dataActivation, LogsProvider logsProvider, int bLevel)
	{
		
		
		
		//check if low is not already activated
		if(!sharedPrefsEditor.isBatteryCurrentlyLow())
		{
			logsProvider.info("Low battery level: "+bLevel+"%");
			//MainService.showNotification("Running...","Low Battery", context, logsProvider);
			MainService.manageNotifications(sharedPrefsEditor, context, logsProvider);
			
			//activate low profile
			sharedPrefsEditor.setBatteryIsCurrentlyLow(true);
			
			
			//activate sleep hours 
			AlarmReceiver.setSleepHoursOn(sharedPrefsEditor, context, dataActivation, logsProvider);
		}
	}
	
	public static void batteryLevelIsNotLow(SharedPrefsEditor sharedPrefsEditor, Context context, DataActivation dataActivation, LogsProvider logsProvider, int bLevel)
	{
		
		
		
		//check if low is  already activated
		if(sharedPrefsEditor.isBatteryCurrentlyLow())
		{
			logsProvider.info("High battery level: "+bLevel+"%");
			//MainService.showNotification("Running...","", context,logsProvider);
			MainService.manageNotifications(sharedPrefsEditor, context, logsProvider);
			
			//activate low profile
			sharedPrefsEditor.setBatteryIsCurrentlyLow(false);
			
			//deactivate sleep hours if sleep hours time on is not activated
			if(!sharedPrefsEditor.isSleepTimeOnCurrentlyActivated())
			{
				AlarmReceiver.setSleepHoursOff(sharedPrefsEditor, context, dataActivation, logsProvider);
			}
		}
	}

}
