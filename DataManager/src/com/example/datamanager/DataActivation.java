package com.example.datamanager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.accounts.Account;
import android.accounts.AccountManager;
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
import android.util.Log;

public class DataActivation {

	private Context context = null;
	
	//when wifi and data are on, data activation will be delayed
	private int timeBeforeActivateData = 5000;
	

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

		//only if necessary
		if((!isDataChipActivated() && enabled) || (isDataChipActivated() && !enabled))
		{
		
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

				Log.i("Sync", "Auto Sync ON");
				ContentResolver.setMasterSyncAutomatically(true);

			} else {
				Log.i("Sync", "Auto Sync OFF");
				ContentResolver.setMasterSyncAutomatically(false);
			}
			
		}
	}

	/**
	 * Enable/disable wifi
	 * 
	 * @param enabled
	 */
	public void setWifiConnectionEnabled(boolean enabled) {

		//only if necessary
		if( (isWifiChipActivated() && !enabled) || (!isWifiChipActivated() && enabled))
		{
			WifiManager wifiManager = (WifiManager) this.context
					.getSystemService(Context.WIFI_SERVICE);
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
			// Some problem accessible private API
			// TODO do whatever error handling you want here
		}

		// Log.i("Data TOGGLE", String.valueOf(mobileDataEnabled));

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
			setWifiConnectionEnabled(true);
			
			//if data is activated
			if(sharedPrefsEditor.isDataActivated())
			{
				
				//if wifi and data are not activated
				if(!isWifiChipActivated() && !isDataChipActivated())
				{
				
					//wait 5s to activate data
					sharedPrefsEditor.setDataActivationDelayed(true);
					try {
						Log.i("WIFI", "waiting for wifi activation");
						Thread.sleep(timeBeforeActivateData);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						sharedPrefsEditor.setDataActivationDelayed(false);
						e.printStackTrace();
					}
				}
				
				sharedPrefsEditor.setDataActivationDelayed(false);
			}
		}

		if (sharedPrefsEditor.isDataMgrActivated() && sharedPrefsEditor.isDataActivated()) {
			// activate data connection
			setMobileDataEnabled(true);
		}

	}
	
	


	// disable all connectivity
	public void setConnectivityDisabled(SharedPrefsEditor sharedPrefsEditor) throws Exception {
		
		if (sharedPrefsEditor.isWifiManagerActivated()) {
			// activate wifi
			setWifiConnectionEnabled(false);
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
		Log.i("CHECK WIFI", "check wifi scan results");
		
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
		// results will be handle WifiScanreceiver
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

		Log.i("SYNC", "sync is launched");

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

						Log.i("SYNC", "syncing " + yourAccount.name
								+ type.authority);
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
        
        Log.i("phone plugged", "phone is plugged : "+isPlugged);
        
        return isPlugged;
	}
}
