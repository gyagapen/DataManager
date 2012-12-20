package com.example.datamanager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.example.cleverconnectivity.R;

public class DataHandler extends Handler {
	
	private Context aContext = null;

	//parent service
	private MainService parentService = null;

	

	
	public DataHandler(Context context, MainService aService) {
		super();
		aContext = context;
		parentService = aService;
	}
	
	//handle message from thread
	public void handleMessage(Message msg)
	{

		//get shared preferences editor
		SharedPrefsEditor sharedPrefsEditor = parentService.getSharedPrefsEditor();
		
		if(msg.what == 0)
		{
			//data is used
			Log.i("Data usage", "Data USED");
			
			//reset timerOn
			parentService.CancelTimerOn();
			parentService.StartTimerOn();
			
		}
		else if(msg.what == 1)
		{
			//data not used

			Log.i("Data usage", "Data NOT used");
			
			try {
				
				//auto wifi off
				if(sharedPrefsEditor.isAutoWifiOffActivated() && sharedPrefsEditor.isWifiActivated())
				{
					Log.i("Auto Wifi Off", "Launch check");
					parentService.getDataActivator().checkWifiScanResults();
					
					//3g and auto sync off
					parentService.getDataActivator().setMobileDataEnabled(false);
					parentService.getDataActivator().setAutoSync(false, sharedPrefsEditor);
				}
				else
				{
					//wifi and 3g off
					parentService.getDataActivator().setConnectivityDisabled();
					//disable autosync too
					parentService.getDataActivator().setAutoSync(false, sharedPrefsEditor);
				}
				
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//cancel timer On
			parentService.CancelTimerOn();
			
			//start timer Off
			parentService.StartTimerOff();
			
		}
		else if(msg.what == 2)
		{
			//enable data
			try {
				
				
				parentService.getDataActivator().setConnectivityEnabled(sharedPrefsEditor); 
				//activate autosync
				parentService.getDataActivator().setAutoSync(true, sharedPrefsEditor);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			//cancel timerOff
			parentService.CancelTimeOff();
			
			//reset timerOn
			parentService.CancelTimerOn();
			parentService.StartTimerOn();
		}

	}
	


}
