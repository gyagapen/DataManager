package com.example.datamanager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.content.ContentResolver;
import android.content.Context;
import android.net.ConnectivityManager;

public class DataActivation {

	Context context = null;

	public DataActivation(Context aContext) {
		context = aContext;
		
		
	}
		
	public void setMobileDataEnabled(boolean enabled, boolean enableAutoSync) throws Exception {
		setAutoSync(enableAutoSync);
	    final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    final Class conmanClass = Class.forName(conman.getClass().getName());
	    final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
	    iConnectivityManagerField.setAccessible(true);
	    final Object iConnectivityManager = iConnectivityManagerField.get(conman);
	    final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
	    final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
	    setMobileDataEnabledMethod.setAccessible(true);

	    setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
	}
	
	
	public void setAutoSync(boolean isEnabled)
	{
		ContentResolver.setMasterSyncAutomatically(isEnabled);
	}

}
