package com.datamanager.core;

import java.util.List;

import com.datamanager.tabActivities.AppLauncher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

public class WifiScanReceiver extends BroadcastReceiver {

	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;

	private DataActivation dataActivation;
	
	private LogsProvider logsProvider = null;

	@Override
	public void onReceive(Context context, Intent intent) {

		logsProvider = new LogsProvider(context, this.getClass());
		
		logsProvider.info("WIFI SCAN : scan results received");

		// shared prefs init
		prefs = context.getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				AppLauncher.SHARED_ACCESS_MODE);
		dataActivation = new DataActivation(context);
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);
		
		// if a known wifi has been found in scan results
		boolean knownWifiFound = false;

		// if service is running
		if (sharedPrefsEditor.isServiceActivated()) {

			// only if app is doing a check for auto wifi off or auto wifi on
			if (sharedPrefsEditor.isCheckingAutoWifiOn()) {
				// get scan results
				WifiManager wifiManager = (WifiManager) context
						.getSystemService(Context.WIFI_SERVICE);
				List<ScanResult> scanResults = wifiManager.getScanResults();

				if(scanResults != null)
				{
					// list of known wifi network
					List<WifiConfiguration> wifiKnownNetwirkList = wifiManager
							.getConfiguredNetworks();

					// counters for lists
					int scanCounter = 0;
					int knownWifiCounter = 0;

					

					// browse the list
					while (!knownWifiFound && scanCounter < scanResults.size()) {
						ScanResult aResult = scanResults.get(scanCounter);

						String scanSsid = aResult.SSID;

						// comparing with known networks
						knownWifiCounter = 0;
						while (!knownWifiFound
								&& knownWifiCounter < wifiKnownNetwirkList.size()) {
							// getting configuration
							WifiConfiguration wifiConfiguration = wifiKnownNetwirkList
									.get(knownWifiCounter);

							// Log.i("WIFI SCAN", scanSsid+" vs "+
							// wifiConfiguration.SSID);

							if (scanSsid.equals(wifiConfiguration.SSID.replace(
									"\"", ""))) {
								// known network found
								knownWifiFound = true;

								logsProvider.info("WIFI SCAN : know network found : "
										+ aResult.SSID);
							}

							knownWifiCounter++;

						}

						scanCounter++;
					}

					if (!knownWifiFound) {
						logsProvider.info("WIFI SCAN : Auto wifi off");

						// disable wifi
						sharedPrefsEditor.setWifiActivation(false);

					}

					// if time off and wifi manager is enabled
					if (sharedPrefsEditor.isWifiManagerActivated()
							&& sharedPrefsEditor.isTimeOffIsActivated()) {
						// shut down wifi
						dataActivation.setWifiConnectionEnabled(false, true, sharedPrefsEditor);
					}

					// if not time off and known networks are available
					if (!sharedPrefsEditor.isTimeOffIsActivated()) {

						if (knownWifiFound && !sharedPrefsEditor.isWifiActivated()) {
							// enable wifi
							sharedPrefsEditor.setWifiActivation(true);
							dataActivation.setWifiConnectionEnabled(true, true, sharedPrefsEditor);
						} 

						if(!knownWifiFound)
						{
							// disable wifi
							sharedPrefsEditor.setWifiActivation(false);
							dataActivation.setWifiConnectionEnabled(false, false, sharedPrefsEditor);
						}

					}


				}




				if(!sharedPrefsEditor.isTimeOffIsActivated()) //time on
				{
					//activate data
					if(sharedPrefsEditor.isDataActivated())
					{
						//else data will be activated by network mode receiver
						if(!sharedPrefsEditor.isNetworkModeSwitching())
						{
							try {
								//wait 3seconds if wifi is connecting
								if(knownWifiFound)
								{
									Thread.sleep(3000);
								}
								dataActivation.setMobileDataEnabled(true, sharedPrefsEditor);
							} catch (Exception e) {
								logsProvider.error(e);
							}
						}
					}
				}

				// set checking to false
				sharedPrefsEditor.setIsCheckingAutoWifi(false);

				//checking if screen has not be turned off meanwhile
				if(!dataActivation.isScreenIsOn())
				{
					//can cause pb if it's in sleep mode
					if(sharedPrefsEditor.isSleeping())
					{
						//desactivate all connectivity
						try {
							dataActivation.setConnectivityDisabled(sharedPrefsEditor);
						} catch (Exception e) {
							logsProvider.error(e);
						}
					}
				}

			}



		}

	}




}
