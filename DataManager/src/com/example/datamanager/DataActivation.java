package com.example.datamanager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.content.ContentResolver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.util.Log;

public class DataActivation {

	Context context = null;

	public DataActivation(Context aContext) {
		context = aContext;

	}

	/**
	 * Enable/disable 3G Connection
	 * 
	 * @param enabled
	 * @param enableAutoSync
	 * @throws Exception
	 */
	public void setMobileDataEnabled(boolean enabled) throws Exception {

		Log.i("Data activation", "Data is activate : " + enabled);

		final ConnectivityManager conman = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final Class conmanClass = Class.forName(conman.getClass().getName());
		final Field iConnectivityManagerField = conmanClass
				.getDeclaredField("mService");
		iConnectivityManagerField.setAccessible(true);
		final Object iConnectivityManager = iConnectivityManagerField
				.get(conman);
		final Class iConnectivityManagerClass = Class
				.forName(iConnectivityManager.getClass().getName());
		final Method setMobileDataEnabledMethod = iConnectivityManagerClass
				.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
		setMobileDataEnabledMethod.setAccessible(true);

		setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
	}

	/**
	 * Enable/disable AutoSync
	 * 
	 * @param isEnabled
	 */
	public void setAutoSync(boolean isEnabled,
			SharedPrefsEditor sharedPrefsEditor) {

		if (sharedPrefsEditor.isAutoSyncActivated() && isEnabled) {
			ContentResolver.setMasterSyncAutomatically(true);
		} else {
			ContentResolver.setMasterSyncAutomatically(false);
		}
	}

	/**
	 * Enable/disable wifi
	 * 
	 * @param enabled
	 */
	public void setWifiConnectionEnabled(boolean enabled) {
		
		WifiManager wifiManager = (WifiManager) this.context
				.getSystemService(Context.WIFI_SERVICE);
		wifiManager.setWifiEnabled(enabled);
	}

	/**
	 * Indicates wether the wifi is activated
	 */
	public boolean isWifiChipActivated() {
		
		/**ConnectivityManager connec = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo mobile = connec
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		return mobile.isConnectedOrConnecting();**/
		
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		
		return wifiManager.isWifiEnabled();
	}

	/**
	 * Indicates wether data connection is activated
	 */
	public boolean isDataChipActivated() {
		
		
		ConnectivityManager connec = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		
		NetworkInfo mobile = connec
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		
		boolean mobileIsConnected =  mobile.isConnectedOrConnecting();
		
		//workaround for problem as 3g is reported as disconneted if wifi is disable
		if(isWifiChipActivated())
		{
			//disconnect wifi
			//setWifiConnectionEnabled(false);
			
			//check 3g connection
			mobileIsConnected =  true;
			
			//reconnect wifi
			//setWifiConnectionEnabled(true);
		}
		
		return mobileIsConnected;
		
	}

	/**
	 * Enable 3G or Wifi depending of sharedPrefs
	 * 
	 * @throws Exception
	 * 
	 */
	public void setConnectivityEnabled(SharedPrefsEditor sharedPrefsEditor)
			throws Exception {
		/**if (sharedPrefsEditor.isWifiManagerActivated()
				&& sharedPrefsEditor.isWifiActivated()) // activate wifi
		{
			setWifiConnectionEnabled(true);

			if (sharedPrefsEditor.isDataActivated()
					&& sharedPrefsEditor.isDataMgrActivated()) {
				// activate data
				setMobileDataEnabled(true);
			}
		} 
		
		
		else {
			// enable data
			setMobileDataEnabled(true);
		}**/
		
		if(sharedPrefsEditor.isWifiActivated())
		{
			//activate wifi
			setWifiConnectionEnabled(true);
		}
		
		if(sharedPrefsEditor.isDataActivated())
		{
			//activate data connection
			setMobileDataEnabled(true);
		}

	}

	// disable all connectivity
	public void setConnectivityDisabled() throws Exception {
		setMobileDataEnabled(false);
		setWifiConnectionEnabled(false);
	}

	/**
	 * Is Auto-Sync activated
	 * 
	 * @return
	 */
	public boolean isAutoSyncIsActivated() {
		return ContentResolver.getMasterSyncAutomatically();
	}

	public boolean isScreenIsOn() {
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		boolean isScreenOn = pm.isScreenOn();
		return isScreenOn;
	}
}
