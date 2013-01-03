package com.example.datamanager;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gyagapen.cleverconnectivity.R;

public class MainActivity extends Activity implements OnClickListener,
		OnCheckedChangeListener {

	static final String STR_ACTIVATE_CONNECTIVITY = "activateConnectivity";

	static final int ID_ALARM_TIME_ON = 1879;
	static final int ID_ALARM_TIME_OFF = 1899;

	static final String LOG_FILE_NAME = "CleverConnectivity.log";

	static final boolean APPLICATION_IS_FREE = true;

	// mail data
	private final String MAIL_RECIPIENT = "gyagapen@gmail.com";
	private final String MAIL_SUBJECT = "CleverConnectivity Paid Bug Report";

	// ui components
	private CheckBox cbData = null;
	private CheckBox cbDataMgr = null;
	private CheckBox cbWifi = null;
	private CheckBox cbWifiMgr = null;
	private CheckBox cbAutoSync = null;
	private CheckBox cbAutoSyncMgr = null;
	private CheckBox cbAutoWifiOff = null;
	private CheckBox cbSleepHours = null;
	private CheckBox cbServiceIsDeactivated = null;
	private CheckBox cbServiceIsDeactivatedPlugged = null;
	private TextView edSleepHours = null;
	private EditText edTimeOn = null;
	private EditText edTimeOnCheck = null;
	private EditText edTimeOff = null;
	private EditText edInterval = null;
	private EditText edScreenOnDelay = null;
	private Button buttonSave = null;
	private Button buttonEditSleepHours = null;
	private Button buttonReportBug = null;
	private Button buttonViewLogFile = null;
	
	// private AdView mainStartAdView = null;
	// private AdView mainEndAdView = null;

	private int RETURN_CODE = 0;

	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;

	private DataActivation dataActivation;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		// initialize connectivity positions
		SaveConnectionPreferences connPrefs = new SaveConnectionPreferences(
				getApplicationContext());
		connPrefs.saveAllConnectionSettingInSharedPrefs();

		// shared prefs init
		prefs = getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				Activity.MODE_PRIVATE);

		dataActivation = new DataActivation(getBaseContext());
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);

		try {
			sharedPrefsEditor.initializePreferences();

			setContentView(R.layout.main_application);

			// init ui
			loadUiComponents();
			initializeUiComponentsData();

			// instanciate buttons
			buttonSave = (Button) findViewById(R.id.buttonSave);
			buttonSave.setOnClickListener(this);

			buttonEditSleepHours = (Button) findViewById(R.id.button_edit_sleep_hours);
			buttonEditSleepHours.setOnClickListener(this);

			buttonReportBug = (Button) findViewById(R.id.buttonReportBug);
			buttonReportBug.setOnClickListener(this);

			buttonViewLogFile = (Button) findViewById(R.id.buttonLogFile);
			buttonViewLogFile.setOnClickListener(this);

			cbData.setOnCheckedChangeListener(this);
			cbWifi.setOnCheckedChangeListener(this);
			cbAutoSync.setOnCheckedChangeListener(this);

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/**
	 * 
	 * Load of components of UI
	 */
	private void loadUiComponents() {
		cbData = (CheckBox) findViewById(R.id.checkBoxData);
		cbDataMgr = (CheckBox) findViewById(R.id.checkBoxDataMgr);

		cbWifi = (CheckBox) findViewById(R.id.checkBoxWifi);
		cbWifiMgr = (CheckBox) findViewById(R.id.checkBoxWifiMgr);

		cbAutoSync = (CheckBox) findViewById(R.id.checkBoxAutoSync);
		cbAutoSyncMgr = (CheckBox) findViewById(R.id.checkBoxAutoSyncMgr);
		cbAutoWifiOff = (CheckBox) findViewById(R.id.checkBoxAutoWifiOff);
		cbSleepHours = (CheckBox) findViewById(R.id.checkBoxSleepHours);
		
		cbServiceIsDeactivated = (CheckBox) findViewById(R.id.checkBoxDeactivateAll);
		cbServiceIsDeactivatedPlugged = (CheckBox) findViewById(R.id.checkBoxDeactivatePlugged);

		edSleepHours = (TextView) findViewById(R.id.tvSleepHours);

		edTimeOn = (EditText) findViewById(R.id.editTextTimeOn);
		edTimeOnCheck = (EditText) findViewById(R.id.EditTextTimeOnCheck);
		edTimeOff = (EditText) findViewById(R.id.editTextTimeOff);
		edInterval = (EditText) findViewById(R.id.editTextInterval);
		edScreenOnDelay = (EditText) findViewById(R.id.EditTextScreenDelay);

		/*
		 * mainStartAdView = (AdView)findViewById(R.id.adViewMainStart);
		 * mainEndAdView = (AdView)findViewById(R.id.adViewMainEnd);
		 * 
		 * if(!APPLICATION_IS_FREE) { //if application is a paid app, then no
		 * ads mainStartAdView.destroy(); mainEndAdView.destroy();
		 * 
		 * } else { //load ads mainEndAdView.loadAd(new AdRequest());
		 * mainStartAdView.loadAd(new AdRequest()); }
		 */

	}

	/**
	 * Init ui components from value extracted from shared preferences
	 */
	private void initializeUiComponentsData() {
		int timeOn = sharedPrefsEditor.getTimeOn();
		edTimeOn.setText(String.valueOf(timeOn));
		
		int timeOnCheck = sharedPrefsEditor.getTimeOnCheck();
		edTimeOnCheck.setText(String.valueOf(timeOnCheck));

		int timeOff = sharedPrefsEditor.getTimeOff();
		edTimeOff.setText(String.valueOf(timeOff));
		
		int timeScreenOnDelay = sharedPrefsEditor.getScreenDelayTimer();
		edScreenOnDelay.setText(String.valueOf(timeScreenOnDelay));

		int checkTime = sharedPrefsEditor.getIntervalCheck();
		edInterval.setText(String.valueOf(checkTime));

		boolean dataIsActivated = sharedPrefsEditor.isDataActivated();
		cbData.setChecked(dataIsActivated);

		boolean dataMgrIsActivated = sharedPrefsEditor.isDataMgrActivated();
		cbDataMgr.setChecked(dataMgrIsActivated);

		boolean wifiIsActivated = sharedPrefsEditor.isWifiActivated();
		cbWifi.setChecked(wifiIsActivated);

		boolean wifiMgrIsActivated = sharedPrefsEditor.isWifiManagerActivated();
		cbWifiMgr.setChecked(wifiMgrIsActivated);

		boolean autoSyncIsActivated = sharedPrefsEditor.isAutoSyncActivated();
		cbAutoSync.setChecked(autoSyncIsActivated);

		boolean autoSyncMgrIsActivated = sharedPrefsEditor
				.isAutoSyncMgrIsActivated();
		cbAutoSyncMgr.setChecked(autoSyncMgrIsActivated);

		boolean autoWifiOffIsActivated = sharedPrefsEditor
				.isAutoWifiOffActivated();
		cbAutoWifiOff.setChecked(autoWifiOffIsActivated);

		boolean sleepHoursIsActivated = sharedPrefsEditor
				.isSleepHoursActivated();
		cbSleepHours.setChecked(sleepHoursIsActivated);
		
		boolean serviceIsDeactivated = sharedPrefsEditor.isServiceDeactivatedAll();
		cbServiceIsDeactivated.setChecked(serviceIsDeactivated);
		
		boolean serviceIsDeactivatedPlugged = sharedPrefsEditor.isDeactivatedWhilePlugged();
		cbServiceIsDeactivatedPlugged.setChecked(serviceIsDeactivatedPlugged); 

		String sleepTimeOn = sharedPrefsEditor.getSleepTimeOn();
		String sleepTimeOff = sharedPrefsEditor.getSleepTimeOff();

		edSleepHours.setText(sleepTimeOn + "-" + sleepTimeOff);

	}

	@TargetApi(11)
	public void onClick(View v) {

		// if saved button is click
		if (v == buttonSave) {

			validateSettings();

		} else if (v == buttonEditSleepHours) // if edit sleep hours button
		{
			// open new activity
			Intent sleepTimePickerIntent = new Intent(this,
					SleepTimerPickerActivity.class);
			startActivityForResult(sleepTimePickerIntent, RETURN_CODE);
		} else if (v == buttonReportBug) {
			// send mail to dev
			Intent email = new Intent(Intent.ACTION_SEND);
			email.putExtra(Intent.EXTRA_EMAIL, new String[] { MAIL_RECIPIENT });

			String versionName = "";
			try {
				versionName = this.getPackageManager().getPackageInfo(
						this.getPackageName(), 0).versionName;
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}

			email.putExtra(Intent.EXTRA_SUBJECT, MAIL_SUBJECT + " - "
					+ versionName);
			// email.putExtra(Intent.EXTRA_TEXT, "message");
			email.setType("message/rfc822");
			startActivity(Intent.createChooser(email,
					"Choose an Email client :"));

		} else if (v == buttonViewLogFile) {
			// generate log file
			DumpLogToFile();

			// display log file
			readLogFile();
		}

	}

	/**
	 * Stop data manager service
	 */
	public static void stopDataManagerService(Context context) {
		// if service is started
		// if (sharedPrefsEditor.isServiceActivated()) {
		context.stopService(new Intent(context, MainService.class));
		// }
	}

	public static void StartDataManagerService(Context context) {
		// if service is not started
		// if (!sharedPrefsEditor.isServiceActivated()) {
		Intent serviceIntent = new Intent(context, MainService.class);
		context.startService(serviceIntent);
		// }
	}

	/**
	 * whenever a checkbox is checked/unchecked
	 */

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

	}

	// manage result from other activity
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// verify return code

		if (requestCode == RETURN_CODE) {

			// refresh sleep hours time off/on
			if (resultCode == RESULT_OK) {
				String sleepTimeOn = sharedPrefsEditor.getSleepTimeOn();
				String sleepTimeOff = sharedPrefsEditor.getSleepTimeOff();

				edSleepHours.setText(sleepTimeOn + "-" + sleepTimeOff);

				manageSleepingHours(sleepTimeOff, sleepTimeOn);

			}
		}
	}

	public void manageSleepingHours(String sleepTimeOff, String sleepTimeOn) {

		// decides wether to activate/desactivate sleeping hours now
		if (cbSleepHours.isChecked()) {

			// schedule alarms
			setUpAlarm(sleepTimeOn, getBaseContext(), false);
			setUpAlarm(sleepTimeOff, getBaseContext(), true);

			if (time1IsAftertimer2(sleepTimeOff, sleepTimeOn)) {
				// if time sleep on has passed and not time sleep off then
				// activate sleeping
				if (timeIsPassed(sleepTimeOn) && !timeIsPassed(sleepTimeOff)) {
					Log.i("TimePassed", sleepTimeOn + " passed and "
							+ sleepTimeOff + " not passed");
					sharedPrefsEditor.setIsSleeping(true);
				} else {
					sharedPrefsEditor.setIsSleeping(false);
				}
			} else {
				Log.i("Timer", sleepTimeOff + " is before " + sleepTimeOn);
				if (timeIsPassed(sleepTimeOn)) {
					Log.i("Timer", sleepTimeOn + " is passed");
					sharedPrefsEditor.setIsSleeping(true);
				} else if (!timeIsPassed(sleepTimeOff)) {
					Log.i("Timer", sleepTimeOff + " is NOT passed");
					sharedPrefsEditor.setIsSleeping(true);
				} else {
					Log.i("Timer", sleepTimeOn + " is NOT passed");
					sharedPrefsEditor.setIsSleeping(false);
				}
			}
		} else {
			// cancel alarms
			Intent intent = new Intent(this, AlarmReceiver.class);
			PendingIntent timeOn = PendingIntent.getBroadcast(this,
					ID_ALARM_TIME_ON, intent, 0);
			PendingIntent timeOff = PendingIntent.getBroadcast(this,
					ID_ALARM_TIME_OFF, intent, 0);
			AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

			alarmManager.cancel(timeOn);
			alarmManager.cancel(timeOff);

			// set sleeping to false
			sharedPrefsEditor.setIsSleeping(false);
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

		Intent alarmLauncher = new Intent(context, AlarmReceiver.class);
		alarmLauncher.putExtra(STR_ACTIVATE_CONNECTIVITY, activateConnectivity);

		PendingIntent recurringAlarm = null;

		AlarmManager alarms = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		if (activateConnectivity) {
			recurringAlarm = PendingIntent.getBroadcast(context,
					ID_ALARM_TIME_OFF, alarmLauncher,
					PendingIntent.FLAG_UPDATE_CURRENT);
			Log.i("Alarm set up off", time);

		} else {

			recurringAlarm = PendingIntent.getBroadcast(context,
					ID_ALARM_TIME_ON, alarmLauncher,
					PendingIntent.FLAG_UPDATE_CURRENT);

			Log.i("Alarm set up on", time);
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

	/*
	 * Save all settings in application
	 */
	public void validateSettings() {
		// load ui compenents
		loadUiComponents();

		// get settings values
		int timeOn = 0;
		try
		{
			timeOn = Integer.parseInt(edTimeOn.getText().toString());
		}
		catch(Exception e)
		{
			timeOn = sharedPrefsEditor.getTimeOn();
		}
		
		int timeOnCheck=0;
		try
		{
			timeOnCheck = Integer.parseInt(edTimeOnCheck.getText().toString());
		}
		catch(Exception e)
		{
			timeOnCheck = sharedPrefsEditor.getTimeOnCheck();
		}
		
		int timeOff = 0;
		
		try
		{
			timeOff = Integer.parseInt(edTimeOff.getText().toString());
		}
		catch (Exception e)
		{
			timeOff = sharedPrefsEditor.getTimeOff();
		}
		
		int intervalCheck = 0;
		try
		{
			intervalCheck = Integer.parseInt(edInterval.getText().toString());
		}
		catch(Exception e)
		{
			intervalCheck = sharedPrefsEditor.getIntervalCheck();
		}
		
		
		int timeScreenOnDelay = 0;
		try
		{
			timeScreenOnDelay = Integer.parseInt(edScreenOnDelay.getText().toString());
		}
		catch(Exception e)
		{
			timeScreenOnDelay = sharedPrefsEditor.getScreenDelayTimer();
		}

		boolean dataIsActivated = cbData.isChecked();
		boolean dataMgrIsActivated = cbDataMgr.isChecked();

		boolean wifiIsActivated = cbWifi.isChecked();
		boolean wifiMgrIsActivated = cbWifiMgr.isChecked();

		boolean autoSyncIsActivated = cbAutoSync.isChecked();

		boolean autoWifiIsActivated = cbAutoWifiOff.isChecked();

		boolean sleepHoursIsActivated = cbSleepHours.isChecked();

		boolean isAutoSyncMgrIsActivated = cbAutoSyncMgr.isChecked();
		
		boolean isServiceDeactived = cbServiceIsDeactivated.isChecked();
		boolean isServiceDeactivatedPlugged = cbServiceIsDeactivatedPlugged.isChecked();

		// save all these preferences
		sharedPrefsEditor.setAllValues(timeOn, timeOff, intervalCheck,
				dataIsActivated, dataMgrIsActivated, wifiIsActivated,
				wifiMgrIsActivated, autoSyncIsActivated, autoWifiIsActivated,
				sleepHoursIsActivated, isAutoSyncMgrIsActivated, isServiceDeactived,isServiceDeactivatedPlugged, timeOnCheck,timeScreenOnDelay);

		try {
			// if data is disabled; data connection is stopped
			if (!dataIsActivated) {

				// keep auto sync (in case of wifi connection)
				dataActivation.setMobileDataEnabled(false);
				// dataActivation.setAutoSync(true);
			} else {
				// activate data
				dataActivation.setMobileDataEnabled(true);
				// dataActivation.setAutoSync(true);
			}

			// if wifi is disabled, wifi connection is stopped
			if (!wifiIsActivated) {
				dataActivation.setWifiConnectionEnabled(false);
				// dataActivation.setAutoSync(true);
			} else {
				dataActivation.setWifiConnectionEnabled(true);
				// dataActivation.setAutoSync(true);
			}

			// enable/disable autosync
			if (autoSyncIsActivated) {
				dataActivation.setAutoSync(true, sharedPrefsEditor, false);
			} else {
				dataActivation.setAutoSync(false, sharedPrefsEditor, false);

			}



			// if data and data manager enable or if wifi and wifi manager
			// enable, service is started
			/*if ((dataIsActivated && dataMgrIsActivated)
					|| (wifiIsActivated && wifiMgrIsActivated)) {

				StartDataManagerService();
			}

			// if data manager and wifi manager are disabled, service is
			// stopped
			if ((!dataMgrIsActivated && !wifiMgrIsActivated)
					|| (!dataIsActivated && !wifiIsActivated)) {
				stopDataManagerService();
			}*/
			
			//stop service if deactivate is checked or deactivate while plugged check and phone is plugged
			if(isServiceDeactived || (isServiceDeactivatedPlugged && dataActivation.isPhonePlugged()))
			{
				stopDataManagerService(this);
			}
			else
			{
				StartDataManagerService(this);
			}
			
			
			// sleeping hours
			manageSleepingHours(sharedPrefsEditor.getSleepTimeOff(),
					sharedPrefsEditor.getSleepTimeOn());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Whenever application is closed
	 */

	protected void onDestroy() {

		// save all settings
		validateSettings();
		
		

		super.onDestroy();
		
		System.exit(0);
	}

	// whenever application is no more in the foreground

	protected void onPause() {
		// save all settings
		validateSettings();

		super.onPause();
	}

	/**
	 * whenever application is bring to foreground
	 */
	/*
	 * protected void onResume() {
	 * 
	 * // initialize connectivity positions SaveConnectionPreferences connPrefs
	 * = new SaveConnectionPreferences( getApplicationContext());
	 * connPrefs.saveAllConnectionSettingInSharedPrefs();
	 * 
	 * // refresh ui initializeUiComponentsData();
	 * 
	 * super.onResume(); }
	 */

	/**
	 * Dump logcat of this application to a file
	 */
	public void DumpLogToFile() {
		try {
			File filename = new File(Environment.getExternalStorageDirectory()
					+ "/" + LOG_FILE_NAME);
			boolean deleted = filename.delete();
			filename.createNewFile();
			//
			String cmd = "logcat -d -v time -f " + filename.getAbsolutePath()
					+ " ";
			// String cmd =
			// "logcat -d > "+Environment.getExternalStorageDirectory()+"/"+LOG_FILE_NAME;
			Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Display captured logfile
	 */
	public void readLogFile() {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		Uri logFileUri = Uri.parse("file://"
				+ Environment.getExternalStorageDirectory() + "/"
				+ LOG_FILE_NAME);
		intent.setDataAndType(logFileUri, "text/plain");
		startActivity(intent);
	}

}
