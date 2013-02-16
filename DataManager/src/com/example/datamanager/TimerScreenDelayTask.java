package com.example.datamanager;

import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class TimerScreenDelayTask extends TimerTask {

	private Context context = null;
	private TimersSetUp timerSetUp = null;

	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;

	private DataActivation dataActivation = null;
	
	private LogsProvider logsProvider = null;

	public TimerScreenDelayTask(Context aContext) {

		logsProvider = new LogsProvider(aContext, this.getClass());
		
		context = aContext;
		timerSetUp = new TimersSetUp(context);

		// shared prefs init
		prefs = context.getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				Activity.MODE_PRIVATE);
		dataActivation = new DataActivation(context);
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);
		
	}

	public void run() {

	    logsProvider.info("Screen on task : screen delay task has been launched");
		
		// stop all timers if there are running

		/*timerSetUp.CancelTimeOff();
		timerSetUp.CancelTimerOn();

		// activate data or wifi
		try {
			dataActivation.setConnectivityEnabled(sharedPrefsEditor);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		sharedPrefsEditor.setScreenOnIsDelayed(false);*/
		
	    logsProvider.info("Screen on task : screen is "+dataActivation.isScreenIsOn());
		
		//if screen is still on
		if(dataActivation.isScreenIsOn())
		{
		
		    logsProvider.info("Screen on task : screen is on");
			
			Intent i = new Intent(context, MainService.class);
	
			i.putExtra("screen_state", false);
			
			context.startService(i);
			
			sharedPrefsEditor.setScreenOnIsDelayed(false);
		}
		

	}

}
