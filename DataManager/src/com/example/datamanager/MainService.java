package com.example.datamanager;

import android.app.Activity;
import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.widget.Toast;

public class MainService extends Service {



	TimersSetUp timerSetUp = null;


	// time values
	private int timeCheckData = 5000;
	
	private LogsProvider logsProvider = null;


	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;

	//for 2g switch
	private ChangeNetworkMode changeNetworkMode = null;	

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
		
		logsProvider = new LogsProvider(getApplicationContext());
		
		// shared prefs init
		prefs = getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				Activity.MODE_PRIVATE);
		dataActivation = new DataActivation(getBaseContext());
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);

		// REGISTER RECEIVER THAT HANDLES SCREEN ON AND SCREEN OFF LOGIC
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		mReceiver = new ScreenReceiver();
		registerReceiver(mReceiver, filter);
		
		
		//register keyguard receiver if needed
		if(sharedPrefsEditor.isEnabledWhenKeyguardOff())
		{
			KeyguardReceiver keyguardReceiver = new KeyguardReceiver();
			registerReceiver(keyguardReceiver, new IntentFilter(Intent.ACTION_USER_PRESENT));
		}

		changeNetworkMode = new ChangeNetworkMode(getApplicationContext());

		timerSetUp = new TimersSetUp(this);

		// register service start in preferences
		sharedPrefsEditor.setServiceActivation(true);
		Toast.makeText(getBaseContext(), "DataManager service started",
				Toast.LENGTH_SHORT).show();

		//start in foreground
		Notification note = new Notification( 0, null, System.currentTimeMillis() );
		note.flags |= Notification.FLAG_NO_CLEAR;
		startForeground( 42, note );

		//start time on if screen is off
		if(!dataActivation.isScreenIsOn())
		{

			
			//set first time on
			sharedPrefsEditor.setIsFirstTimeOn(true);

			Intent i = new Intent(getBaseContext(), MainService.class);

			i.putExtra("screen_state", true);

			getBaseContext().startService(i);
		}

	}

	// when service starts
	public int onStartCommand(Intent intent, int flags, int startId) {

		logsProvider.info("MainService : On command received");

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
			timerSetUp.CancelTimeOff();
			timerSetUp.CancelTimerOn();

			//switch to 3G if necesary
			changeNetworkMode.switchTo3GIfNecesary();

			// activate data or wifi
			try 
			{
				//if auto wifi on or auto wifi off is enabled
				if( (sharedPrefsEditor.isAutoWifiOnActivated() && !sharedPrefsEditor.isWifiActivated()) || (sharedPrefsEditor.isAutoWifiOffActivated() && sharedPrefsEditor.isWifiActivated()))
				{
					logsProvider.info("auto wifi on check");

					//cheking wether to enable wifi if known networks are avalaible
					dataActivation.checkWifiScanResults(sharedPrefsEditor);

					//enable 3g and sync meanwhile
					if(sharedPrefsEditor.isAutoSyncActivated())
					{
						dataActivation.setAutoSync(true, sharedPrefsEditor, false);
					}


				}
				else
				{
					dataActivation.setConnectivityEnabled(sharedPrefsEditor);
				}




			} catch (Exception e) {
				e.printStackTrace();
			}

		} else { // screen is off


			sharedPrefsEditor.setScreenOnIsDelayed(false);

			//get sleep state
			boolean isSleeping = sharedPrefsEditor.isSleeping();

			logsProvider.info("sleep: "+isSleeping);

			//switch to 2G if necesary
			changeNetworkMode.switchTo2GIfNecesary();

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
				timerSetUp.StartTimerOn();
			}





		}

		super.onStartCommand(intent, flags, startId);

		return START_STICKY;
	}




	@Override
	public void onDestroy() {

		if(!sharedPrefsEditor.isServiceActivated())
		{

			// unregister service to screen broadcast receiver
			unregisterReceiver(mReceiver);

			timerSetUp.CancelTimeOff();
			timerSetUp.CancelTimerOn();


			Toast.makeText(getBaseContext(), "DataManager service stopped",
					Toast.LENGTH_SHORT).show();


			super.onDestroy();
		}
		else
		{
			Intent in = new Intent();
			in.setAction("YouWillNeverKillMe");
			sendBroadcast(in);
			logsProvider.info("onDestroy()...");
		}

	}

	public DataActivation getDataActivator() {
		return dataActivation;
	}

	public SharedPrefsEditor getSharedPrefsEditor() {
		return sharedPrefsEditor;
	}

}
