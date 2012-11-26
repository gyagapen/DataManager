package com.example.datamanager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

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
		
		
		if(msg.what == 0)
		{
			//data is used
			Toast.makeText(aContext, "Data used", Toast.LENGTH_SHORT).show();
			
			//reset timerOn
			parentService.CancelTimerOn();
			parentService.StartTimerOn();
			
		}
		else if(msg.what == 1)
		{
			//data not used
			Toast.makeText(aContext, "DATA NOT USED", Toast.LENGTH_SHORT).show();
			
			//disable data
			Toast.makeText(aContext, "DISABLE DATA ", Toast.LENGTH_SHORT).show();
			try {
				parentService.setMobileDataEnabled(false);
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
				parentService.setMobileDataEnabled(true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//disable data
			Toast.makeText(aContext, "DATA ENABLED", Toast.LENGTH_SHORT).show();
			
			//cancel timerOff
			parentService.CancelTimeOff();
			
			//reset timerOn
			parentService.CancelTimerOn();
			parentService.StartTimerOn();
		}

	}
	


}
