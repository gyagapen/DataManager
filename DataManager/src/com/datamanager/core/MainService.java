package com.datamanager.core;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.datamanager.tabActivities.AppLauncher;
import com.gyagapen.cleverconnectivity.R;

public class MainService extends Service {



	TimersSetUp timerSetUp = null;

	//to check wether service is active or not
	static MainService serviceInstance = null;

	// time values
	private int timeCheckData = 5000;
	private final static int NOTIFICATION_ID=1;

	private LogsProvider logsProvider = null;


	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;

	//for 2g switch
	private ChangeNetworkMode changeNetworkMode = null;	

	// screen broadcast receiver
	private BroadcastReceiver mReceiver = null;
	
	//keyguard receiver
	private KeyguardReceiver keyguardReceiver = null;
	
	//battery level receiver
	private BatteryLevelReceiver batteryLevelReceiver = null;
	
	//battery receiver

	private DataActivation dataActivation = null;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public void onCreate() {

		super.onCreate();

		//instanciate instance
		serviceInstance = this;
		
		logsProvider = new LogsProvider(getApplicationContext(), this.getClass());
		
		logsProvider.info("MainService onCreate is called");

		// shared prefs init
		prefs = getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				AppLauncher.SHARED_ACCESS_MODE);
		dataActivation = new DataActivation(getBaseContext());
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);

		// REGISTER RECEIVER THAT HANDLES SCREEN ON AND SCREEN OFF LOGIC
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		mReceiver = new ScreenReceiver();
		this.registerReceiver(mReceiver, filter);
		logsProvider.info("ScreenReceiver registered");


		//register keyguard receiver if needed
		if(sharedPrefsEditor.isEnabledWhenKeyguardOff())
		{
			keyguardReceiver = new KeyguardReceiver();
			this.registerReceiver(keyguardReceiver, new IntentFilter(Intent.ACTION_USER_PRESENT));
		}

		//register battery monitoring if necesary
		if(sharedPrefsEditor.getLowProfileBatteryActivation())
		{
			registerBatteryMonitoring();
		}

		changeNetworkMode = new ChangeNetworkMode(getApplicationContext());

		timerSetUp = new TimersSetUp(this);

		// register service start in preferences
		sharedPrefsEditor.setServiceActivation(true);
		Toast.makeText(getBaseContext(), "DataManager service started",
				Toast.LENGTH_SHORT).show();


		if(sharedPrefsEditor.isNotificationEnabled())
		{
			Notification note = manageNotifications(sharedPrefsEditor, getApplicationContext(), logsProvider);
			if(note != null)
			{
				startForeground( 42, note );
			}
		}
		else
		{
			//start in foreground
			Notification note = new Notification( 0, null, System.currentTimeMillis() );
			note.flags |= Notification.FLAG_NO_CLEAR;
			startForeground( 42, note );
		}


		//start time on if screen is off
		if(!dataActivation.isScreenIsOn())
		{
			//set first time on
			sharedPrefsEditor.setIsFirstTimeOn(true);
			Intent i = new Intent(getBaseContext(), MainService.class);
			i.putExtra("screen_state", true);
			getBaseContext().startService(i);
		}

		//managing sleep hours
		AlarmMgr alarmMgr = new AlarmMgr(this, sharedPrefsEditor);
		alarmMgr.manageSleepingHours(sharedPrefsEditor.getSleepTimeOff(),
				sharedPrefsEditor.getSleepTimeOn());

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

			logsProvider.info("main service : screen is on");
			
			//update screen was previously off
			sharedPrefsEditor.setScrenWasOff(false);
			
			//screen is not delayed
			sharedPrefsEditor.setScreenOnIsDelayed(false);


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

					//bluetooth if necesary
					if(sharedPrefsEditor.isSleeping() && sharedPrefsEditor.getBluetoothActivation())
					{
						dataActivation.setBluetoothChipActivation(true);
					}


				}
				else
				{
					dataActivation.setConnectivityEnabled(sharedPrefsEditor);
				}




			} catch (Exception e) {
				logsProvider.error(e);
			}

		} else { // screen is off

			logsProvider.info("main service : screen is off");
			
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
					logsProvider.error(e);
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
		

		logsProvider.info("OnDestroy: unregistering receivers");
		
		//reinitialize shared prefs
		prefs = getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				AppLauncher.SHARED_ACCESS_MODE);
		dataActivation = new DataActivation(getBaseContext());
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);
		
		//unregister other receivers
		if(sharedPrefsEditor.isEnabledWhenKeyguardOff())
		{
			unregisterReceiver(keyguardReceiver);
			
		}

		//register battery monitoring if necesary
		if(sharedPrefsEditor.getLowProfileBatteryActivation())
		{
			unregisterReceiver(batteryLevelReceiver);
		}
		
		// unregister service to screen broadcast receiver
		unregisterReceiver(mReceiver);

		if(!sharedPrefsEditor.isServiceActivated())
		{

			timerSetUp.CancelTimeOff();
			timerSetUp.CancelTimerOn();

			Toast.makeText(getBaseContext(), "DataManager service stopped",
					Toast.LENGTH_SHORT).show();
		
			//desintanciate instance
			serviceInstance = null;
			
			//clear notification
			clearNotification();

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



	public static Notification showNotification(String message, String subText, Context context, LogsProvider logsProvider, SharedPrefsEditor sharedPrefsEditor)
	{
		Notification notification = null;
		
		//if service is running
		if(sharedPrefsEditor.isNotificationEnabled() && sharedPrefsEditor.isServiceActivated())
		{
			logsProvider.info("new notification: "+message+ " - "+subText);
			Intent intent = new Intent(context, AppLauncher.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 01, intent, Intent.FLAG_ACTIVITY_CLEAR_TASK);
			NotificationCompat.Builder  builder = new NotificationCompat.Builder(context);
			Bitmap bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher), 
					context.getResources().getDimensionPixelSize(android.R.dimen.notification_large_icon_width),
					context.getResources().getDimensionPixelSize(android.R.dimen.notification_large_icon_height), 
					true);
			builder.setContentTitle("CleverConnectivity");
			builder.setContentText(message);
			if (!subText.equals("") || subText != null)
			{
				builder.setSubText(subText);
			}
			builder.setNumber(101);
			builder.setOngoing(true);
			builder.setContentIntent(pendingIntent);
			builder.setTicker("CleverConnectivity");
			builder.setSmallIcon(R.drawable.ic_launcher);
			builder.setLargeIcon(bm);
			builder.setAutoCancel(true);
			builder.setPriority(0);
			notification = builder.build();
			/*NotificationManager notificationManger = 
					(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManger.notify(NOTIFICATION_ID, notification);*/
			
		}
		
		return notification;
	}

	public void registerBatteryMonitoring()
	{
		logsProvider.info("Battery monitoring started");

		//reset
		sharedPrefsEditor.setBatteryIsCurrentlyLow(false);

		batteryLevelReceiver = new BatteryLevelReceiver();
		this.registerReceiver(batteryLevelReceiver,
				new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

	}
	
	public void clearNotification()
	{
		logsProvider.info("notification is cleared");
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.cancel(NOTIFICATION_ID);
	}

	public static Notification manageNotifications(SharedPrefsEditor sharedPrefsEditor, Context context, LogsProvider logsProvider)
	{
		Notification notification = null;
		
		if(sharedPrefsEditor.isSleeping())
		{
			if(sharedPrefsEditor.isBatteryCurrentlyLow())
			{
				notification = showNotification(context.getString(R.string.notif_running),context.getString(R.string.notif_bat_low), context, logsProvider,sharedPrefsEditor);
			}
			else
			{
				notification = showNotification(context.getString(R.string.notif_running),context.getString(R.string.notif_sleep_on), context,logsProvider,sharedPrefsEditor);
			}
		}
		else
		{
			if(sharedPrefsEditor.isSleepHoursActivated())
			{
				notification = showNotification(context.getString(R.string.notif_running),context.getString(R.string.notif_sleep_on), context,logsProvider,sharedPrefsEditor);
			}
			else
			{
				notification = showNotification(context.getString(R.string.notif_running),"", context,logsProvider,sharedPrefsEditor);
			}
		}
		
		return notification;
	}
	

}
