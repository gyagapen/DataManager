package com.example.datamanager;

import java.util.Timer;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.widget.Toast;

public class MainService extends Service {

	// Timers
	private Timer timerOn = null;
	private Timer timerOff = null;
	private TimerOnTask timerOnTask = null;
	private TimerOffTask timerOffTask = null;

	//time values
	private int timeOnValue =0;
	private int timeOffValue =0;
	private int timeCheckData = 5000;
	
	// data handler
	DataHandler dataHandler = null;

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
		timerOn = new Timer();
		timerOff = new Timer();

		// data handler
		dataHandler = new DataHandler(getBaseContext(), this);

		// shared prefs init
		prefs = getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				Activity.MODE_PRIVATE);
		 dataActivation = new DataActivation(getBaseContext());
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);

		// register service start in preferences
		sharedPrefsEditor.setServiceActivation(true);

		Toast.makeText(getBaseContext(), "DataManager service started",
				Toast.LENGTH_SHORT).show();

	}

	// when service starts
	public int onStartCommand(Intent intent, int flags, int startId) {

		boolean screenOff = false;

		try {
			screenOff = intent.getBooleanExtra("screen_state", false);
		} catch (NullPointerException ex) {
			// first run
			screenOff = false;
		}

		/*Toast.makeText(getBaseContext(), "Screen on : " + screenOff,
				Toast.LENGTH_SHORT).show();*/

		// if screen is on
		if (!screenOff) {

			// stop all timers if there are running
			CancelTimeOff();
			CancelTimerOn();

			// activate data or wifi
			try {
				dataActivation.setConnectivityEnabled(sharedPrefsEditor);
				// activate autosync too
				dataActivation.setAutoSync(true);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else { // screen is off

			StartTimerOn();

		}

		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * Start timer On
	 */
	public void StartTimerOn() {
		// start timer on
		/*Toast.makeText(getBaseContext(), "Start timer on", Toast.LENGTH_SHORT)
				.show();*/
		
		
		timeOnValue = sharedPrefsEditor.getTimeOn()*60*1000; //from min to ms
		timeCheckData = sharedPrefsEditor.getIntervalCheck()*1000; //from s to ms
		
		timerOnTask = new TimerOnTask(dataHandler, timeCheckData);
		timerOn.schedule(timerOnTask, timeOnValue, timeOnValue);
	}

	/**
	 * Start timer off
	 */
	public void StartTimerOff() {

		// start timer off
		/*Toast.makeText(getBaseContext(), "Start timer off", Toast.LENGTH_SHORT)
				.show();*/
		
		timeOffValue = sharedPrefsEditor.getTimeOff()*60*1000; //in ms
		
		timerOffTask = new TimerOffTask(dataHandler);
		timerOff.schedule(timerOffTask, timeOffValue, timeOffValue);

	}

	/**
	 * Cancel TimerOn
	 */
	public void CancelTimerOn() {
		if (timerOnTask != null) {
			timerOnTask.cancel();
		}
	}

	/**
	 * Cancel TimerOff
	 */
	public void CancelTimeOff() {
		if (timerOffTask != null) {
			timerOffTask.cancel();
		}
	}



	@Override
	public void onDestroy() {
		// unregister service to screen broadcast receiver
		unregisterReceiver(mReceiver);

		// stops timers
		if (timerOnTask != null) {
			timerOnTask.cancel();
		}

		if (timerOffTask != null) {
			timerOffTask.cancel();
		}

		// register service stopped in preferences
		sharedPrefsEditor.setServiceActivation(false);

		Toast.makeText(getBaseContext(), "DataManager service stopped",
				Toast.LENGTH_SHORT).show();

		super.onDestroy();

	}
	
	public DataActivation getDataActivator()
	{
		return dataActivation;
	}
	
	public SharedPrefsEditor getSharedPrefsEditor()
	{
		return sharedPrefsEditor;
	}

}
