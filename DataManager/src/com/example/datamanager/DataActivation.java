package com.example.datamanager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.content.ContentResolver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

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
	public void setMobileDataEnabled(boolean enabled)
			throws Exception {

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
	public void setAutoSync(boolean isEnabled) {
		ContentResolver.setMasterSyncAutomatically(isEnabled);
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
		ConnectivityManager connec = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo mobile = connec
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		return mobile.isConnectedOrConnecting();
	}

	/**
	 * Indicates wether data connection is activated
	 */
	public boolean isDataChipActivated() {
		ConnectivityManager connec = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo mobile = connec
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		return mobile.isConnectedOrConnecting();
	}
	
	
	/**
	 * Enable 3G or Wifi depending of sharedPrefs
	 * @throws Exception 
	 * 
	 */
	public void setConnectivityEnabled(SharedPrefsEditor sharedPrefsEditor) throws Exception
	{
		if(sharedPrefsEditor.isWifiManagerActivated() && sharedPrefsEditor.isWifiActivated()) //activate wifi
		{
			setWifiConnectionEnabled(true);
			
			if(sharedPrefsEditor.isDataActivated() && sharedPrefsEditor.isDataMgrActivated())
			{
				//activate data
				setMobileDataEnabled(true);
			}
		}
		else
		{
			//enable data
			setMobileDataEnabled(true);
		}

	}
	
	
	//disable all connectivity
	public void setConnectivityDisabled() throws Exception
	{
		setMobileDataEnabled(false);
		setWifiConnectionEnabled(false);
	}
}
