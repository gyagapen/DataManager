package com.example.datamanager;

import java.util.Calendar;
import java.util.TimeZone;

import com.gyagapen.cleverconnectivity.R;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmMgr {

	
	private Context context = null;
	private SharedPrefsEditor sharedPrefsEditor = null;
	private LogsProvider logsProvider = null;
	
	public AlarmMgr(Context aContext, SharedPrefsEditor asEditor) {
		context = aContext;
		sharedPrefsEditor = asEditor;
		logsProvider = new LogsProvider(context, this.getClass());
	}
	
	
	public void manageSleepingHours(String sleepTimeOff, String sleepTimeOn) {

		// decides wether to activate/desactivate sleeping hours now
		if (sharedPrefsEditor.isSleepHoursActivated()) {

			// schedule alarms
			setUpAlarm(sleepTimeOn, context, false);
			setUpAlarm(sleepTimeOff, context, true);

			if (time1IsAftertimer2(sleepTimeOff, sleepTimeOn)) {
				// if time sleep on has passed and not time sleep off then
				// activate sleeping
				if (timeIsPassed(sleepTimeOn) && !timeIsPassed(sleepTimeOff)) {
					logsProvider.info(sleepTimeOn + " passed and "
							+ sleepTimeOff + " not passed");
					MainService.showNotification(context.getString(R.string.notif_running),context.getString(R.string.notif_sleep_on), context,logsProvider);
					sharedPrefsEditor.setIsSleeping(true);
				} else {
					sharedPrefsEditor.setIsSleeping(false);
					MainService.showNotification(context.getString(R.string.notif_running),context.getString(R.string.notif_sleep_off), context,logsProvider);
				}
			} else {
				logsProvider.info(sleepTimeOff + " is before " + sleepTimeOn);
				if (timeIsPassed(sleepTimeOn)) {
					logsProvider.info(sleepTimeOn + " is passed");
					sharedPrefsEditor.setIsSleeping(true);
					MainService.showNotification(context.getString(R.string.notif_running),context.getString(R.string.notif_sleep_on), context,logsProvider);
				} else if (!timeIsPassed(sleepTimeOff)) {
					logsProvider.info(sleepTimeOff + " is NOT passed");
					sharedPrefsEditor.setIsSleeping(true);
					MainService.showNotification(context.getString(R.string.notif_running),context.getString(R.string.notif_sleep_on), context,logsProvider);
				} else {
					logsProvider.info(sleepTimeOn + " is NOT passed");
					sharedPrefsEditor.setIsSleeping(false);
					MainService.showNotification(context.getString(R.string.notif_running),context.getString(R.string.notif_sleep_off), context,logsProvider);
				}
			}
		} else {
			// cancel alarms
			Intent intent = new Intent(context, AlarmReceiver.class);
			PendingIntent timeOn = PendingIntent.getBroadcast(context,
					MainActivity.ID_ALARM_TIME_ON, intent, 0);
			PendingIntent timeOff = PendingIntent.getBroadcast(context,
					MainActivity.ID_ALARM_TIME_OFF, intent, 0);
			AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

			alarmManager.cancel(timeOn);
			alarmManager.cancel(timeOff);

			// set sleeping to false
			sharedPrefsEditor.setIsSleeping(false);
			MainService.showNotification(context.getString(R.string.notif_running),context.getString(R.string.notif_sleep_off), context,logsProvider);
		}
		

	}

	public void setUpAlarm(String time, Context context,
			boolean activateConnectivity) {

		// get hour and minute from time
		String[] splittedTime = time.split(":");
		int hour = Integer.valueOf(splittedTime[0]);
		int minute = Integer.valueOf(splittedTime[1]);

		// setting alarm
		Calendar updateTime = Calendar.getInstance();
		updateTime.set(Calendar.HOUR_OF_DAY, hour);
		updateTime.set(Calendar.MINUTE, minute);
		
		//avoid alarm to be fired up if time has already passed
		if(timeIsPassed(time))
		{
			//add one day
			updateTime.add(Calendar.DATE, 1);
		}

		Intent alarmLauncher = new Intent(context, AlarmReceiver.class);
		alarmLauncher.putExtra(MainActivity.STR_ACTIVATE_CONNECTIVITY, activateConnectivity);

		PendingIntent recurringAlarm = null;

		AlarmManager alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

		if (activateConnectivity) {
			recurringAlarm = PendingIntent.getBroadcast(context,
					MainActivity.ID_ALARM_TIME_OFF, alarmLauncher,
					PendingIntent.FLAG_CANCEL_CURRENT);
			logsProvider.info("Alarm set up off "+time);

		} else {

			recurringAlarm = PendingIntent.getBroadcast(context,
					MainActivity.ID_ALARM_TIME_ON, alarmLauncher,
					PendingIntent.FLAG_CANCEL_CURRENT);

			logsProvider.info("Alarm set up on "+time);
		}

		alarms.cancel(recurringAlarm);

		alarms.setRepeating(AlarmManager.RTC_WAKEUP,
				updateTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
				recurringAlarm);
	}

	public boolean timeIsPassed(String time) {
		// get hour and minute from time
		String[] splittedTime = time.split(":");
		int hour = Integer.valueOf(splittedTime[0]);
		int minute = Integer.valueOf(splittedTime[1]);

		// get current time
		Calendar currentTime = Calendar.getInstance();
		// currentTime.setTimeZone(TimeZone.getTimeZone("GMT"));

		// Log.i("current hour", currentTime.toString());

		// setting alarm time
		Calendar instanceTime = Calendar.getInstance();
		// instanceTime.setTimeZone(TimeZone.getTimeZone("GMT"));
		instanceTime.set(Calendar.HOUR_OF_DAY, hour);
		instanceTime.set(Calendar.MINUTE, minute);

		// Log.i("alarm hour", instanceTime.toString());

		return currentTime.after(instanceTime);
	}

	public boolean time1IsAftertimer2(String time1, String time2) {
		// building time1

		String[] splittedTime1 = time1.split(":");
		int hour1 = Integer.valueOf(splittedTime1[0]);
		int minute1 = Integer.valueOf(splittedTime1[1]);

		Calendar calTime1 = Calendar.getInstance();
		calTime1.setTimeZone(TimeZone.getTimeZone("GMT"));
		calTime1.set(Calendar.HOUR_OF_DAY, hour1);
		calTime1.set(Calendar.MINUTE, minute1);

		// building time2
		String[] splittedTime2 = time2.split(":");
		int hour2 = Integer.valueOf(splittedTime2[0]);
		int minute2 = Integer.valueOf(splittedTime2[1]);

		Calendar calTime2 = Calendar.getInstance();
		calTime2.setTimeZone(TimeZone.getTimeZone("GMT"));
		calTime2.set(Calendar.HOUR_OF_DAY, hour2);
		calTime2.set(Calendar.MINUTE, minute2);

		return calTime1.after(calTime2);

	}
}
