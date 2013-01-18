package com.example.datamanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.util.Log;

/**
 * Class that enable switching between 2g and 3g
 * @author Gui
 *
 */
public class ChangeNetworkMode {

	final static int NETWORK_MODE_WCDMA_PREF = 0; /* GSM/WCDMA (WCDMA preferred) */
	final static int NETWORK_MODE_GSM_ONLY = 1; /* GSM only */
	final static int NETWORK_MODE_WCDMA_ONLY = 2; /* WCDMA only */
	final static int NETWORK_MODE_GSM_UMTS = 3; /* GSM/WCDMA (auto mode, according to PRL) */
	final static int NETWORK_MODE_CDMA = 4; /* CDMA and EvDo (auto mode, according to PRL) */
	final static int NETWORK_MODE_CDMA_NO_EVDO = 5; /* CDMA only */
	final static int NETWORK_MODE_EVDO_NO_CDMA = 6; /* EvDo only */
	final static int NETWORK_MODE_GLOBAL = 7; /* GSM/WCDMA, CDMA, and EvDo (auto mode, according to PRL) */

	private Context context;

	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;
	private DataActivation dataActivation;

	public ChangeNetworkMode(Context aContext) {
		context = aContext;
		
		prefs = context.getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				Activity.MODE_PRIVATE);
		dataActivation = new DataActivation(aContext);
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);
	}

	private void changeNetworkMode(int networkMode) {
		Intent modifyIntent = new Intent("com.android.internal.telephony.MODIFY_NETWORK_MODE");
		modifyIntent.putExtra("networkMode", networkMode);
		context.sendBroadcast(modifyIntent);
	}

	public boolean isCyanogenMod() {
		PackageManager pm = context.getPackageManager();
		return pm.hasSystemFeature("com.cyanogenmod.android");
	}

	public boolean is2GEnabled() {
		int enabled = Settings.Secure.getInt(context.getContentResolver(),
				"preferred_network_mode", -1);

		return enabled == 1;
	}


	public int getPreferredNetworkMode()
	{
		int result = Settings.Secure.getInt(context.getContentResolver(),
				"preferred_network_mode", -1);

		return result;
	}

	public void switchTo2G()
	{
		sharedPrefsEditor.set2GActivation(true);
		
		if(!is2GEnabled())
		{
			sharedPrefsEditor.setNetworkModeIsSwitching(true);
			changeNetworkMode(NETWORK_MODE_GSM_ONLY);
			Log.i("CConnectivity", "switched to 2G");
		}
	}


	public void switchTo3G()
	{
		sharedPrefsEditor.set2GActivation(false);
		
		if(is2GEnabled())
		{
			sharedPrefsEditor.setNetworkModeIsSwitching(true);
			changeNetworkMode(NETWORK_MODE_GSM_UMTS);
			Log.i("CConnectivity", "switched to 3G");
		}
	}
	
	
	/**
	 * switch to 2G if necessary
	 * @param sharedPrefsEditor
	 */
	public void switchTo2GIfNecesary()
	{
		
		
		//if network is not already switching
		if(!sharedPrefsEditor.isNetworkModeSwitching())
		{
			if(sharedPrefsEditor.is2GSwitchActivated())
			{
				
				//save original preferred network before change it
				sharedPrefsEditor.setOriginalPreferredNetwork(getPreferredNetworkMode());
				
				switchTo2G();
				
			}
		}
	}
	
	/**
	 * switch to 3G if necessary
	 * @param sharedPrefsEditor
	 */
	public void switchTo3GIfNecesary()
	{
		//if network is not already switching
		if(!sharedPrefsEditor.isNetworkModeSwitching())
		{
			if(sharedPrefsEditor.is2GSwitchActivated())
			{
				Log.i("CConnectivity", "check if 3g will be enabled, original: "+sharedPrefsEditor.getOriginalPreferredMode() );
				
				//if original network mode was not 2G
				if(sharedPrefsEditor.getOriginalPreferredMode() != NETWORK_MODE_GSM_ONLY)
				{
					
					Log.i("CConnectivity", "3g should be swtich on");
					switchTo3G();
				}
			}
		}
	}

}
