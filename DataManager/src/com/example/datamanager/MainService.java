package com.example.datamanager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Timer;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.widget.Toast;

public class MainService extends Service {

	// Timers
	private Timer timerOn = null;
	private Timer timerOff = null;
	private TimerOnTask timerOnTask = null;
	private TimerOffTask timerOffTask = null;

	// data handler
	DataHandler dataHandler = null;



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

		BroadcastReceiver mReceiver = new ScreenReceiver();

		registerReceiver(mReceiver, filter);
		
		

		// Timers implementation
		timerOn = new Timer();
		timerOff = new Timer();
		
		// data handler
		dataHandler = new DataHandler(getBaseContext(), this);	

		

	}

	// when service starts
	public int onStartCommand(Intent intent, int flags, int startId) {

		boolean screenOff = false;
		
		try
		{
			 screenOff = intent.getBooleanExtra("screen_state", false);
		}
		catch(NullPointerException ex)
		{
			//first run
			screenOff = false;
		}
		
		
		Toast.makeText(getBaseContext(), "Screen on : "+screenOff, Toast.LENGTH_SHORT).show();

		// if screen is on
		if (screenOff) {
			
			//stop all timers if there are running
			CancelTimeOff();
			CancelTimerOn();
			
			// activate data

		} else { //screen is off

			
			try {
				launchTimer();
				StartTimerOn();
			} catch (InterruptedException e) {
				Toast.makeText(getBaseContext(), "Erreur", Toast.LENGTH_SHORT)
						.show();
				e.printStackTrace();
			}
		}

		return super.onStartCommand(intent, flags, startId);
	}

	public void launchTimer() throws InterruptedException {

		// fetching preferences
		/*SharedPreferences dataManagerSettings = getSharedPreferences(
				SharedPreferences.PREFERENCE_NAME, Context.MODE_PRIVATE);
		int timeOn = dataManagerSettings.getInt(MainActivity.STR_TIME_ON,
				MainActivity.TIME_ON);
		int timeOff = dataManagerSettings.getInt(MainActivity.STR_TIME_OFF,
				MainActivity.TIME_OFF);*/



	}
	
	/**
	 * Start timer On
	 */
	public void StartTimerOn()
	{
		// start timer on
		Toast.makeText(getBaseContext(), "Start timer on", Toast.LENGTH_SHORT)
				.show();
		timerOnTask = new TimerOnTask(dataHandler);
		timerOn.schedule(timerOnTask, 10000, 10000);
	}
	
	/**
	 * Start timer off
	 */
	public void StartTimerOff()
	{
		
		// start timer off
		Toast.makeText(getBaseContext(), "Start timer off", Toast.LENGTH_SHORT)
				.show();
		timerOffTask = new TimerOffTask(dataHandler);
		timerOff.schedule(timerOffTask, 10000, 10000);
		
	}
	
	
	/**
	 * Cancel TimerOn
	 */
	public void CancelTimerOn()
	{
		if(timerOnTask != null)
		{
			timerOnTask.cancel();
		}
	}
	
	/**
	 * Cancel TimerOff
	 */
	public  void CancelTimeOff()
	{
		if(timerOffTask != null)
		{
			timerOffTask.cancel();
		}
	}
	
	/**
	 * Enable or disable data
	 * @param enabled
	 * @throws Exception
	 */
	public void setMobileDataEnabled(boolean enabled) throws Exception {
		Context context = getBaseContext();
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
	
	

}
