package com.example.datamanager;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SyncAdapterType;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.StrictMode;


public class DataActivation {

	static final String PING_HOST_1 = "http://www.google.com/";
	static final String PING_HOST_2 = "http://fr.yahoo.com/";

	private Context context = null;

	//when wifi and data are on, data activation will be delayed
	private int timeBeforeActivateData = 5000;
	private LogsProvider logsProvider = null;

	
	public DataActivation(Context aContext) {
		context = aContext;
		
		logsProvider = new LogsProvider(aContext, this.getClass());


	}

	/**
	 * Enable/disable 3G Connection
	 * 
	 * @param enabled
	 * @param enableAutoSync
	 * @throws Exception
	 */
	public void setMobileDataEnabled(boolean enabled) throws Exception {

		//only if necessary
		if((!isDataChipActivated() && enabled) || (isDataChipActivated() && !enabled))
		{

			logsProvider.info("Data is activate : " + enabled);

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
	}

	/**
	 * Enable/disable AutoSync
	 * 
	 * @param isEnabled
	 */
	public void setAutoSync(boolean isEnabled,
			SharedPrefsEditor sharedPrefsEditor, boolean forceSync) {

		//only if necessary
		if ( (isAutoSyncIsActivated() && !isEnabled) || (!isAutoSyncIsActivated() && isEnabled) )
		{

			if (sharedPrefsEditor.isAutoSyncActivated() && isEnabled) {

				logsProvider.info("Sync : Auto Sync ON");
				ContentResolver.setMasterSyncAutomatically(true);

			} else {
				logsProvider.info("Sync : Auto Sync OFF");
				ContentResolver.setMasterSyncAutomatically(false);
			}

		}
	}

	/**
	 * Enable/disable wifi
	 * 
	 * @param enabled
	 */
	public void setWifiConnectionEnabled(boolean enabled, boolean netHasToBeChecked, SharedPrefsEditor sharedPrefsEditor) {

		//if internet conn has to be checked
		sharedPrefsEditor.setNetConnHasToBeChecked(netHasToBeChecked);
		
		WifiManager wifiManager = (WifiManager) this.context
				.getSystemService(Context.WIFI_SERVICE);

		//only if necessary
		if( (isWifiChipActivated() && !enabled) || (!isWifiChipActivated() && enabled))
		{

			wifiManager.setWifiEnabled(enabled);
		}
		
		

	}

	/**
	 * Indicates wether the wifi is activated
	 */
	public boolean isWifiChipActivated() {

		/**
		 * ConnectivityManager connec = (ConnectivityManager) context
		 * .getSystemService(Context.CONNECTIVITY_SERVICE);
		 * 
		 * NetworkInfo mobile = connec
		 * .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		 * 
		 * return mobile.isConnectedOrConnecting();
		 **/

		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);

		return wifiManager.isWifiEnabled();
	}

	/**
	 * Indicates wether data connection is activated
	 */
	public boolean isDataChipActivated() {

		boolean mobileDataEnabled = false; // Assume disabled
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		try {
			Class cmClass = Class.forName(cm.getClass().getName());
			Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
			method.setAccessible(true); // Make the method callable
			// get the setting for "mobile data"
			mobileDataEnabled = (Boolean) method.invoke(cm);
		} catch (Exception e) {
			logsProvider.error(e);
		}

		return mobileDataEnabled;
	}

	/**
	 * Enable 3G or Wifi depending of sharedPrefs
	 * 
	 * @throws Exception
	 * 
	 */
	public void setConnectivityEnabled(SharedPrefsEditor sharedPrefsEditor)
			throws Exception {

		if (sharedPrefsEditor.isAutoSyncMgrIsActivated() && sharedPrefsEditor.isAutoSyncActivated()) {
			// activate sync
			setAutoSync(true, sharedPrefsEditor, true);
		}


		if (sharedPrefsEditor.isWifiManagerActivated() && sharedPrefsEditor.isWifiActivated()) {
			// activate wifi
			setWifiConnectionEnabled(true, true, sharedPrefsEditor);

			//if data is activated
			if(sharedPrefsEditor.isDataActivated())
			{

				//if  data is not activated
				if(isDataChipActivated() && !isWifiChipActivated())
				{

					//wait 5s to activate data
					sharedPrefsEditor.setDataActivationDelayed(true);
					try {
						logsProvider.info("WIFI : waiting for wifi activation");
						Thread.sleep(timeBeforeActivateData);
					} catch (InterruptedException e) {
						sharedPrefsEditor.setDataActivationDelayed(false);
					}
				}

				sharedPrefsEditor.setDataActivationDelayed(false);
			}
		}

		if (sharedPrefsEditor.isDataMgrActivated() && sharedPrefsEditor.isDataActivated()) {
			// activate data connection
			//else data will be activated by network mode receiver
			if(!sharedPrefsEditor.isNetworkModeSwitching())
			{
				setMobileDataEnabled(true);
			}
		}

	}




	// disable all connectivity
	public void setConnectivityDisabled(SharedPrefsEditor sharedPrefsEditor) throws Exception {

		if (sharedPrefsEditor.isWifiManagerActivated()) {
			// activate wifi
			setWifiConnectionEnabled(false, false, sharedPrefsEditor);
		}

		if (sharedPrefsEditor.isDataMgrActivated()) {
			// activate data connection
			setMobileDataEnabled(false);
		}

		if (sharedPrefsEditor.isAutoSyncMgrIsActivated()) {
			// activate sync
			setAutoSync(false, sharedPrefsEditor, false);
		}
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

	/**
	 * Check wifi scan results to know if a known is available, else wifi will
	 * be disconnected
	 * 
	 * @return
	 */
	public void checkWifiScanResults(SharedPrefsEditor sharedPrefsEditor) {
		logsProvider.info("CHECK WIFI : check wifi scan results");

		//turn wifi on if off
		if(!isWifiChipActivated())
		{
			WifiManager wifiManager = (WifiManager) this.context
					.getSystemService(Context.WIFI_SERVICE);
			wifiManager.setWifiEnabled(true);
		}

		//set checking to false
		sharedPrefsEditor.setIsCheckingAutoWifi(true);

		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		//results will be handle WifiScanreceiver
		wifi.startScan();



	}

	public void forceSync() {

		// acquire partial wakelock
		PowerManager mgr = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		WakeLock wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
				"SyncLock");
		wakeLock.acquire();

		AccountManager am = AccountManager.get(context);
		Account[] accounts = am.getAccounts();

		SyncAdapterType[] types = ContentResolver.getSyncAdapterTypes();

		logsProvider.info("SYNC : sync is launched");

		Bundle bundle = new Bundle();
		bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
		bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
		bundle.putBoolean(ContentResolver.SYNC_EXTRAS_FORCE, true);

		for (int i = 0; i < accounts.length; i++) {
			Account yourAccount = accounts[i];
			for (SyncAdapterType type : types) {
				if (yourAccount.type.equals(type.accountType)) {
					boolean isSyncable = ContentResolver.getIsSyncable(
							yourAccount, type.authority) > 0;
							if (isSyncable) {
								ContentResolver.requestSync(yourAccount,
										type.authority, bundle);


							}
				}
			}
		}

		// release wake lock
		wakeLock.release();

		// retrieve all active syncs
		/*
		 * List<SyncInfo> activeSyncs = ContentResolver.g;
		 * 
		 * AccountManager am = AccountManager.get(context); Account[] accounts =
		 * am.getAccounts();
		 * 
		 * Log.i("SYNC", "sync is launched");
		 * 
		 * //for each active syncs for(int i=0;i<accounts.length;i++) { Account
		 * activeAccount = accounts[i];
		 * 
		 * Bundle bundle = new Bundle();
		 * bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
		 * bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
		 * 
		 * //request sync for this accout
		 * ContentResolver.requestSync(activeAccount, accounts[i]., bundle);
		 * 
		 * Log.i("SYNC", "syncing "+activeSyncs.get(i).account.name); }
		 */

		/*
		 * Bundle bundle = new Bundle();
		 * bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
		 * bundle.putBoolean(ContentResolver.SYNC_EXTRAS_FORCE, true);
		 * bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
		 * ContentResolver.requestSync(null, ContentResolver.g.getAuthority(),
		 * bundle);
		 */
	}

	/**
	 * Return true is the phone is in charge
	 * @return
	 */
	public boolean isPhonePlugged()
	{
		Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
		boolean isPlugged = (plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB);

		logsProvider.info("phone plugge  : phone is plugged : "+isPlugged);

		return isPlugged;
	}


	public boolean isInternetConnectionAvailable()
	{
		boolean isInternetAvailaible = false;

		
		//avoid androidblockguard policy error
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = 
					new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		URL url;
		try {
			url = new URL(PING_HOST_1);
			HttpURLConnection conn= (HttpURLConnection) url.openConnection();
			int responseCode = conn.getResponseCode();
			
			isInternetAvailaible = (responseCode == 200);
			
		} catch (MalformedURLException e) {
			logsProvider.info("net check err : "+e.getMessage());
			isInternetAvailaible = false;
		} catch (IOException e) {
			logsProvider.info("net check err : "+e.getMessage());
			isInternetAvailaible = false;
		}
		
		
	
		

		logsProvider.info("Internet connection check: "+isInternetAvailaible);

		return isInternetAvailaible;
	}
	
	public boolean isKeyguardIsActive()
	{
		KeyguardManager keyguardManager = (KeyguardManager)context.getSystemService(Activity.KEYGUARD_SERVICE);
		
		boolean keyGActive =  keyguardManager.inKeyguardRestrictedInputMode();
		logsProvider.info("keyguard state: "+keyGActive);
		
		return keyGActive;
	}


}
