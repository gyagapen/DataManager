package com.example.datamanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ServiceDestroyReceiver extends BroadcastReceiver {
	
	private final static String LOGTAG = "CConnectivity";
	
	public void onReceive(Context context, Intent intent) {
	    Log.i(LOGTAG, "ServeiceDestroy onReceive...");
	    Log.i(LOGTAG, "action:" + intent.getAction());
	    Log.i(LOGTAG, "ServeiceDestroy auto start service...");
	    
	    DataActivation dataActivation = new DataActivation(context);

	  //start data manager service
		Intent serviceIntent = new Intent(context, MainService.class);
		serviceIntent.putExtra("screen_state", !dataActivation.isScreenIsOn());
		context.startService(serviceIntent);
		
	}

}
