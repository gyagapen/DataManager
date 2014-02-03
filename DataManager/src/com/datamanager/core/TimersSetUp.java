package com.datamanager.core;

import java.util.Calendar;

import com.datamanager.tabActivities.AppLauncher;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class TimersSetUp {

	static final int TIMER_ON_ID = 21;
	static final int TIMER_OFF_ID = 22;
	static final int TIMER_SCREEN_DELAY_ID = 23;

	private int timeScreenDelay = 5;
	
	private Context context = null;

	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;
	private DataActivation dataActivation = null;
	private LogsProvider logsProvider = null;

	public TimersSetUp(Context aContext) {
		context = aContext;

		logsProvider = new LogsProvider(aContext, this.getClass());
		
		// shared prefs init
		prefs = context.getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				AppLauncher.SHARED_ACCESS_MODE);
		dataActivation = new DataActivation(context);
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);
	}

	/**
	 * Start timer On
	 */
	public void StartTimerOn(int minutes) {
		// start timer on

		

		// setting alarm
		Calendar onTime = Calendar.getInstance();
		onTime.add(Calendar.MINUTE, minutes);

		Intent alarmLauncher = new Intent(context,
				TimerOnReceiver.class);

		PendingIntent timeOnAlarm = null;

		AlarmManager alarms = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);

		timeOnAlarm = PendingIntent.getBroadcast(context,
				TIMER_ON_ID, alarmLauncher, PendingIntent.FLAG_UPDATE_CURRENT);

	    logsProvider.info("timer on : Timer On launched " + minutes);
		//Log.i("desc time on", onTime.toString());

		alarms.cancel(timeOnAlarm);

		alarms.set(AlarmManager.RTC,
				onTime.getTimeInMillis(),
				timeOnAlarm);
		
		sharedPrefsEditor.setTimeOffActivation(false);
	}
	
	public void StartTimerOn() {
		
		// value of timer on
		int timerOn = sharedPrefsEditor.getTimeOn();
		
		//first time on
		if(sharedPrefsEditor.isFirstTimeOnIsActivated())
		{
			if(sharedPrefsEditor.isFirstTimeOn())
			{
			    logsProvider.info("first time on detected");
				
				timerOn = sharedPrefsEditor.getFirstTimeOn();
				
				//set first time on to false
				sharedPrefsEditor.setIsFirstTimeOn(false);
				
			}
		}
		
		
	
		StartTimerOn(timerOn);
	}
	
	
	/**
	 * Start timer off
	 */
	public void StartTimerOff() {
		
		
		//value of timer on
		int timerOff = sharedPrefsEditor.getTimeOff();
		
		// setting alarm
		Calendar offTime = Calendar.getInstance();
		offTime.add(Calendar.MINUTE, timerOff);
		
		Intent alarmLauncher = new Intent(context, TimerOffReceiver.class);

		PendingIntent timeOffAlarm = null;

		AlarmManager alarms = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
		
		timeOffAlarm = PendingIntent.getBroadcast(context,
				TIMER_OFF_ID, alarmLauncher,
				PendingIntent.FLAG_UPDATE_CURRENT);
		
	    logsProvider.info("timer off : Timer Off launched "+timerOff);
		
		alarms.cancel(timeOffAlarm);

		alarms.set(AlarmManager.RTC,
				offTime.getTimeInMillis(), 
				timeOffAlarm);
		
		sharedPrefsEditor.setTimeOffActivation(true);

	}
	
	
	/**
	 * Start screen delay timer (s)
	 */
	public void StartScreenDelayTimer() {
		
		
		//value of timer on
		int screenDelay = sharedPrefsEditor.getScreenDelayTimer();
		
		// setting alarm
		Calendar offTime = Calendar.getInstance();
		offTime.add(Calendar.SECOND, screenDelay);
		
		Intent alarmLauncher = new Intent(context, TimeScreenDelayReceiver.class);

		PendingIntent timeScreenDelayAlarm = null;

		AlarmManager alarms = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
		
		timeScreenDelayAlarm = PendingIntent.getBroadcast(context,
				TIMER_SCREEN_DELAY_ID, alarmLauncher,
				PendingIntent.FLAG_UPDATE_CURRENT);
		
	    logsProvider.info("screen timer delay : Timer screen delay launched "+screenDelay);
		
		alarms.cancel(timeScreenDelayAlarm);
		
		sharedPrefsEditor.setScreenOnIsDelayed(true);

		alarms.set(AlarmManager.RTC,
				offTime.getTimeInMillis(), 
				timeScreenDelayAlarm);
		
		

	}
	
	
	
	/**
	 * Cancel TimerOn
	 */
	public void CancelTimerOn() {
		
		
		Intent intent = new Intent(context, TimerOnReceiver.class);
		PendingIntent timeOn = PendingIntent.getBroadcast(context,
				TIMER_ON_ID, intent, 0);
		AlarmManager alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(context.ALARM_SERVICE);

		alarmManager.cancel(timeOn);
	}

	/**
	 * Cancel TimerOff
	 */
	public void CancelTimeOff() {
		
		Intent intent = new Intent(context, TimerOffReceiver.class);
		PendingIntent timeOff = PendingIntent.getBroadcast(context,
				TIMER_OFF_ID, intent, 0);
		AlarmManager alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(context.ALARM_SERVICE);

		alarmManager.cancel(timeOff);
		
		sharedPrefsEditor.setTimeOffActivation(false);
	}
	
	
	public void CancelScreenDelay() {
		
		Intent intent = new Intent(context, TimeScreenDelayReceiver.class);
		PendingIntent timeScreenDelay = PendingIntent.getBroadcast(context,
				TIMER_SCREEN_DELAY_ID, intent, 0);
		AlarmManager alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(context.ALARM_SERVICE);

		alarmManager.cancel(timeScreenDelay);
		
		sharedPrefsEditor.setScreenOnIsDelayed(false);
	}



}
