package com.example.datamanager;

import android.content.Context;
import android.content.Intent;
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
	
	public ChangeNetworkMode(Context aContext) {
		context = aContext;
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
		if(!is2GEnabled())
		{
			changeNetworkMode(NETWORK_MODE_GSM_ONLY);
			Log.i("CConnectivity", "switched to 2G");
		}
	}
	
	
	public void switchTo3G()
	{
		if(is2GEnabled())
		{
			changeNetworkMode(NETWORK_MODE_GSM_UMTS);
			Log.i("CConnectivity", "switched to 3G");
		}
	}

}
