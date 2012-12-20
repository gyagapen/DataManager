package com.example.datamanager;

import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;
import com.example.cleverconnectivity.R;

public class WifiScanReceiver extends BroadcastReceiver {

	
	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;

	private DataActivation dataActivation;
   
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		Log.i("WIFI SCAN", "scan results received");
		
		// shared prefs init
		prefs = context.getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				Activity.MODE_PRIVATE);
		dataActivation = new DataActivation(context);
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);
		
		//only when screen off and datamanager is running
		if(!dataActivation.isScreenIsOn() && sharedPrefsEditor.isDataMgrActivated() && sharedPrefsEditor.isAutoWifiOffActivated())
		{
			//get scan results
			WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			List<ScanResult> scanResults = wifiManager.getScanResults();
			
			//list of known wifi network
			List<WifiConfiguration> wifiKnownNetwirkList = wifiManager.getConfiguredNetworks();
			
			//counters for lists
			int scanCounter = 0;
			int knownWifiCounter = 0;
			
			//if a known wifi has been found in scan results
			boolean knownWifiFound = false;
			
			//browse the list
			while(!knownWifiFound && scanCounter < scanResults.size())
			{
				ScanResult aResult = scanResults.get(scanCounter);
				
				String scanSsid = aResult.SSID;
				
				
				
				//comparing with known networks
				knownWifiCounter =0;
				while(!knownWifiFound && knownWifiCounter < wifiKnownNetwirkList.size())
				{
					//getting configuration
					WifiConfiguration wifiConfiguration = wifiKnownNetwirkList.get(knownWifiCounter);
					
				
					Log.i("WIFI SCAN", scanSsid+" vs "+ wifiConfiguration.SSID);
					
					if(scanSsid.equals(wifiConfiguration.SSID.replace("\"", "")))
					{
						//known network found
						knownWifiFound = true;
						
						Log.i("WIFI SCAN","know network found : "+aResult.SSID);
					}
					
					knownWifiCounter++;
					
				}
				
				
				scanCounter++;
			}
			
			if(!knownWifiFound)
			{
				Log.i("WIFI SCAN","Auto wifi off");
				
				//disable wifi
				sharedPrefsEditor.setWifiActivation(false);
				
			}
			
			//shut down wifi
			dataActivation.setWifiConnectionEnabled(false);
		}

		
	}

}
