package com.datamanager.core;

import com.datamanager.tabActivities.AppLauncher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

public class WifiConnectionReceiver extends BroadcastReceiver {

	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;
	private DataActivation dataActivation = null;
	private LogsProvider logsProvider = null;

	public void onReceive(Context context, Intent intent) {

		logsProvider = new LogsProvider(context, this.getClass());

		// shared prefs init
		prefs = context.getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				AppLauncher.SHARED_ACCESS_MODE);

		dataActivation = new DataActivation(context);
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);

		if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
			NetworkInfo networkInfo = intent
					.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);

			try {

				if (networkInfo.isConnected()) {

					// disable data if option isDataOffWhenWifi checked
					if (sharedPrefsEditor.isDataOffWhenWifi()) {
						dataActivation.setMobileDataEnabled(false,
								sharedPrefsEditor);
					}

					// Wifi is connected
					logsProvider.info("Wifi is connected, getcheckconnwifi: "
							+ sharedPrefsEditor.getCheckNetConnectionWifi()
							+ " netHasToBeChecked: "
							+ sharedPrefsEditor.getNetHasToBeChecked());

					// verify internet connection
					// checking if internet connection is availaible
					if (sharedPrefsEditor.getCheckNetConnectionWifi()
							&& sharedPrefsEditor.getNetHasToBeChecked()) {
						if (!dataActivation.isInternetConnectionAvailable()) {
							// shut down wifi
							dataActivation.setWifiConnectionEnabled(false,
									false, sharedPrefsEditor);
						}

						sharedPrefsEditor.setNetConnHasToBeChecked(false);
					}

				} else if (!networkInfo.isConnected()) {

					// Wifi is disconnected
					logsProvider.info("Wifi is disconnected: ");

					// if option isDataOffWhenWifi checked and data is enabled
					if (sharedPrefsEditor.isDataOffWhenWifi()
							&& sharedPrefsEditor.isDataActivated()) {
						// if screen is on then enable data
						if (dataActivation.isScreenIsOn()) {
							dataActivation.setMobileDataEnabled(true,
									sharedPrefsEditor);
						} else {

							// if timeOn then activate data
							if (!sharedPrefsEditor.isTimeOffIsActivated()) {

								// if not sleeping
								if (!sharedPrefsEditor.isSleeping()) {
									dataActivation.setMobileDataEnabled(true,
											sharedPrefsEditor);
								}
							} else // if time off, then deactivate data
							{
								dataActivation.setMobileDataEnabled(false,
										sharedPrefsEditor);
							}
						}
					}

				}

			} catch (Exception e) {
				logsProvider.error(e);
				e.printStackTrace();
			}

		}

	}
}
