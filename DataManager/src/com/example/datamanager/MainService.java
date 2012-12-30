package com.example.datamanager;

import java.util.Calendar;
import java.util.Timer;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MainService extends Service {

	// Timers
	/*private Timer timerOn = null;
	private Timer timerOff = null;
	private TimerOnTask timerOnTask = null;
	private TimerOffTask timerOffTask = null;*/
	
	TimersSetUp timerSetUp = null;
	

	// time values
	private int timeOnValue = 0;
	private int timeOffValue = 0;
	private int timeCheckData = 5000;


	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;

	// screen broadcast receiver
	private BroadcastReceiver mReceiver = null;

	private DataActivation dataActivation = null;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public void onCreate() {

		super.onCreate();

		// REGISTER RECEIVER THAT HANDLES SCREEN ON AND SCREEN OFF LOGIC

		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);

		filter.addAction(Intent.ACTION_SCREEN_OFF);

		mReceiver = new ScreenReceiver();

		registerReceiver(mReceiver, filter);

		// Timers implementation
		/*timerOn = new Timer();
		timerOff = new Timer();*/
		
		timerSetUp = new TimersSetUp(this);

		// shared prefs init
		prefs = getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				Activity.MODE_PRIVATE);
		dataActivation = new DataActivation(getBaseContext());
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);

		// register service start in preferences
		sharedPrefsEditor.setServiceActivation(true);

		Toast.makeText(getBaseContext(), "DataManager service started",
				Toast.LENGTH_SHORT).show();

		//start in foreground
		Notification note = new Notification( 0, null, System.currentTimeMillis() );
	    note.flags |= Notification.FLAG_NO_CLEAR;
	    startForeground( 42, note );
	}

	// when service starts
	public int onStartCommand(Intent intent, int flags, int startId) {

		Log.i("MainService", "On command received");
		
		boolean screenOff = false;

		try {
			screenOff = intent.getBooleanExtra("screen_state", false);
		} catch (NullPointerException ex) {
			// first run
			screenOff = false;
		}
		
		

		// if screen is on
		if (!screenOff) {

			
			// stop all timers if there are running
			/*CancelTimeOff();
			CancelTimerOn();*/
			timerSetUp.CancelTimeOff();
			timerSetUp.CancelTimerOn();

			// activate data or wifi
			try {
				dataActivation.setConnectivityEnabled(sharedPrefsEditor);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else { // screen is off

			//get sleep state
			boolean isSleeping = sharedPrefsEditor.isSleeping();
			
			if(isSleeping)
			{
				//desactivate all connectivity
				try {
					dataActivation.setConnectivityDisabled(sharedPrefsEditor);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				//start timer
				//StartTimerOn();
				timerSetUp.StartTimerOn();
			}
			

		}

		return super.onStartCommand(intent, flags, startId);
	}

	
	
	
	@Override
	public void onDestroy() {
		// unregister service to screen broadcast receiver
		unregisterReceiver(mReceiver);

		// stops timers
		/*if (timerOnTask != null) {
			timerOnTask.cancel();
		}

		if (timerOffTask != null) {
			timerOffTask.cancel();
		}*/
		
		timerSetUp.CancelTimeOff();
		timerSetUp.CancelTimerOn();

		// register service stopped in preferences
		sharedPrefsEditor.setServiceActivation(false);

		Toast.makeText(getBaseContext(), "DataManager service stopped",
				Toast.LENGTH_SHORT).show();
		

		super.onDestroy();

	}

	public DataActivation getDataActivator() {
		return dataActivation;
	}

	public SharedPrefsEditor getSharedPrefsEditor() {
		return sharedPrefsEditor;
	}

}
