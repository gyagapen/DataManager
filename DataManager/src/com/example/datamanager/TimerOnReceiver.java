package com.example.datamanager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.TrafficStats;
import android.util.Log;


public class TimerOnReceiver extends BroadcastReceiver {
	
	
	// interval when 3G usage is cheked (ms)

	// limit, above that number of bytes, will consider that data is used
	private int bytesLimit = 7000;
	
	//delay timer on expiration if selected app is running  - 5min
	private static int TIME_ON_APP_IS_RUNNING = 5;
	
	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;
	
	
	//interval to delay timer off when data is used
	private int delayTimeOff = 1;

	private DataActivation dataActivation;
	
	private TimersSetUp timerSetUp;

	//this is executed when timer on is expired
	public void onReceive(Context context, Intent intent) {
		
		// shared prefs init
		prefs = context.getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				Activity.MODE_PRIVATE);
		dataActivation = new DataActivation(context);
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);
		timerSetUp = new TimersSetUp(context);
		
		Log.i("AlarmReceiverTimeOn", "time on is expired");
		
		ApplicationsManager appManager = new ApplicationsManager(context);
		
		if(sharedPrefsEditor.isApplicationConnMgrActivated())
		{
			Log.i("CConnectivity", "verifying if selected app is running in background");
			
			//if one of the selected app is running
			if(appManager.isSelectedAppIsRunning(sharedPrefsEditor))
			{

				Log.i("CConnectivity", "app is running in background, next check in 5m");
				
				//reset timerOn for 5min
				timerSetUp.CancelTimerOn();
				timerSetUp.StartTimerOn(TIME_ON_APP_IS_RUNNING);
			}
			else
			{
				checkDataUsage(sharedPrefsEditor, context);
			}
		}
		else
		{
			checkDataUsage(sharedPrefsEditor, context);
		}
		
		

		
		
		
		
	}
	
	
	public void checkDataUsage(SharedPrefsEditor sharedPrefsEditor, Context context)
	{
		//getting dataInterval
		int dataInterval = sharedPrefsEditor.getIntervalCheck() * 1000;
		
		//interval to delay timer off when data is used
		delayTimeOff = sharedPrefsEditor.getTimeOnCheck();
		
		//check data usage
		boolean dataIsUsed = IsDataUsed(dataInterval);
		
		timerSetUp = new TimersSetUp(context);
		
		//if data is used
		if(dataIsUsed)
		{
			//data is used
			Log.i("Data usage", "Data USED");
			
			//reset timerOn
			timerSetUp.CancelTimerOn();
			timerSetUp.StartTimerOn(delayTimeOff);
		}
		else
		{
			//data not used

			Log.i("Data usage", "Data NOT used");
			
			try {
				
				//auto wifi off
				if(sharedPrefsEditor.isAutoWifiOffActivated() && sharedPrefsEditor.isWifiActivated())
				{
					Log.i("Auto Wifi Off", "Launch check");
					dataActivation.checkWifiScanResults(sharedPrefsEditor);
					
					//3g and auto sync off
					if(sharedPrefsEditor.isDataMgrActivated())
					{
						dataActivation.setMobileDataEnabled(false);
					}
					
					if(sharedPrefsEditor.isAutoSyncMgrIsActivated())
					{
						dataActivation.setAutoSync(false, sharedPrefsEditor, false);
					}
				}
				else
				{
					//wifi and 3g off
					dataActivation.setConnectivityDisabled(sharedPrefsEditor);
					
				}
				
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//cancel timer On
			timerSetUp.CancelTimerOn();
			
			//start timer Off
			timerSetUp.StartTimerOff();

		}
	}
	
	public boolean IsDataUsed(int dataInterval)
	{
		boolean dataIsUsed = false;
		
		//workaround for bug
		if(dataInterval > 8000)
		{
			dataInterval = 8000;
		}
		
		if (dataInterval > 0) {
			// get number of bytes received and sent
			long nbBytesSent1 = TrafficStats.getTotalTxBytes();
			long nbBytesReceived1 = TrafficStats.getTotalRxBytes();

			// get number of bytes received 5 second later
			try {
				Thread.sleep(dataInterval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			long nbBytesReceived2 = TrafficStats.getTotalRxBytes();
			long nbBytesSent2 = TrafficStats.getTotalTxBytes();

			int bytesUsed = (int) (nbBytesReceived2 - nbBytesReceived1);
			
			Log.i("Data usage received", String.valueOf(bytesUsed));
			
			boolean dataIsReceived = (bytesUsed > bytesLimit);

			// if no data received then check if data is sent
			if (!dataIsReceived) {
				bytesUsed = (int) (nbBytesSent2 - nbBytesSent1);
				dataIsUsed = (bytesUsed > bytesLimit);
				Log.i("Data usage sent", String.valueOf(bytesUsed));
			} else {
				// data connection is used
				dataIsUsed = true;
			}

		}

		return dataIsUsed;

	}
	

}
