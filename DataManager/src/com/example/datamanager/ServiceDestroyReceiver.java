package com.example.datamanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ServiceDestroyReceiver extends BroadcastReceiver {
	
	private LogsProvider logsProvider = null;
	
	public void onReceive(Context context, Intent intent) {
		
		logsProvider = new LogsProvider(context, this.getClass());
		
		logsProvider.info("ServeiceDestroy onReceive...");
	    logsProvider.info("action:" + intent.getAction());
	    logsProvider.info("ServeiceDestroy auto start service...");
	    
	    DataActivation dataActivation = new DataActivation(context);

	  //start data manager service
		Intent serviceIntent = new Intent(context, MainService.class);
		serviceIntent.putExtra("screen_state", !dataActivation.isScreenIsOn());
		context.startService(serviceIntent);
		
	}

}
