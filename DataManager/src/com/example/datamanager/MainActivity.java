package com.example.datamanager;

import java.io.File;
import java.io.IOException;

import tabActivities.SleepTimerPickerActivity;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.gyagapen.cleverconnectivity.R;

public class MainActivity extends Activity implements OnClickListener,
OnCheckedChangeListener {

	static final String STR_ACTIVATE_CONNECTIVITY = "activateConnectivity";

	static final int ID_ALARM_TIME_ON = 1879;
	static final int ID_ALARM_TIME_OFF = 1899;

	static final String SEPARATOR = "##";

	static final String LOG_FILE_NAME = "CleverConnectivity.log";

	static final boolean APPLICATION_IS_FREE = true;

	// mail data
	private final String MAIL_RECIPIENT = "gyagapen@gmail.com";
	private  String MAIL_SUBJECT = "CleverConnectivity Bug Report";
	private final String MAIL_SUBJECT_PAID = "CleverConnectivity Bug Report";

	private LogsProvider logsProvider = null;

	// ui components
	private CheckBox cbData = null;
	private CheckBox cbDataMgr = null;
	private CheckBox cbWifi = null;
	private CheckBox cbWifiMgr = null;
	private CheckBox cbAutoSync = null;
	private CheckBox cbAutoSyncMgr = null;
	private CheckBox cbAutoWifiOff = null;
	private CheckBox cbAutoWifiOn = null;
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
	private CheckBox cboxFirstTimeOn = null;
	private TextView tvFirstTimeOn = null;
	private TextView tvFirstTimeOnDesc = null;
	private EditText edFirstTimeOn = null;
	private CheckBox cbox2GSwitch = null;
	private TextView tv2GSwitch = null;
	private Button buttonDeactivationStc = null;
	private Button buttonMgConnPerApp = null;
	private CheckBox cbCheckNetConnWifi = null;
	private CheckBox cbKeyguardOff = null;
	private CheckBox cbLogsOff = null;
	private TextView tvViewLogs = null;
	private CheckBox cboxBluetoothOffSleepM = null;

	private int RETURN_CODE = 0;

	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;
	private DataActivation dataActivation;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		logsProvider = new LogsProvider(getApplicationContext(), this.getClass());


		// initialize connectivity positions
		SaveConnectionPreferences connPrefs = new SaveConnectionPreferences(
				getApplicationContext());
		connPrefs.saveAllConnectionSettingInSharedPrefs();

		// shared prefs init
		prefs = getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				Activity.MODE_PRIVATE);

		dataActivation = new DataActivation(getBaseContext());
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);

		//sharedPrefsEditor.setApplicationOnSet("");

		try {
			sharedPrefsEditor.initializePreferences();

			setContentView(R.layout.main_application);



			// instanciate buttons
			buttonSave = (Button) findViewById(R.id.buttonSave);
			buttonSave.setOnClickListener(this);

			buttonEditSleepHours = (Button) findViewById(R.id.button_edit_sleep_hours);
			buttonEditSleepHours.setOnClickListener(this);

			buttonReportBug = (Button) findViewById(R.id.buttonReportBug);
			buttonReportBug.setOnClickListener(this);

			buttonViewLogFile = (Button) findViewById(R.id.buttonLogFile);
			buttonViewLogFile.setOnClickListener(this);

			buttonDeactivationStc = (Button) findViewById(R.id.button_deactivation_shortcut);
			buttonDeactivationStc.setOnClickListener(this);


			buttonMgConnPerApp = (Button)findViewById(R.id.button_manage_app);
			buttonMgConnPerApp.setOnClickListener(this);


			// init ui
			loadUiComponents();
			initializeUiComponentsData();

			cbData.setOnCheckedChangeListener(this);
			cbWifi.setOnCheckedChangeListener(this);
			cbAutoSync.setOnCheckedChangeListener(this);



			//difference between paid and free app
			if(APPLICATION_IS_FREE)
			{
				//ads
				//populateAdMobs();
			}
			else
			{
				//change name
				MAIL_SUBJECT = MAIL_SUBJECT_PAID;
			}

		} catch (IOException e) {

			logsProvider.error(e);
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
		cbAutoWifiOn = (CheckBox) findViewById(R.id.CheckBoxAutoWifiOn);
		cbSleepHours = (CheckBox) findViewById(R.id.checkBoxSleepHours);

		cbServiceIsDeactivated = (CheckBox) findViewById(R.id.checkBoxDeactivateAll);
		cbServiceIsDeactivatedPlugged = (CheckBox) findViewById(R.id.checkBoxDeactivatePlugged);

		edSleepHours = (TextView) findViewById(R.id.tvSleepHours);

		edTimeOn = (EditText) findViewById(R.id.editTextTimeOn);
		edTimeOnCheck = (EditText) findViewById(R.id.EditTextTimeOnCheck);
		edTimeOff = (EditText) findViewById(R.id.editTextTimeOff);
		edInterval = (EditText) findViewById(R.id.editTextInterval);
		edScreenOnDelay = (EditText) findViewById(R.id.EditTextScreenDelay);

		//load fields related to firstTimeOn
		tvFirstTimeOn = (TextView) findViewById(R.id.textViewFirstTimeOn);
		tvFirstTimeOnDesc = (TextView) findViewById(R.id.textViewFirstTimeOnDesc);
		cboxFirstTimeOn = (CheckBox)findViewById(R.id.checkBoxFirstTimeOn);
		cboxFirstTimeOn.setOnCheckedChangeListener(this);
		edFirstTimeOn = (EditText)findViewById(R.id.EditTextFirstTimeOn);

		cbox2GSwitch = (CheckBox)findViewById(R.id.CheckBox2GSwitch);
		tv2GSwitch = (TextView)findViewById(R.id.TextView2GSwitch);

		cbCheckNetConnWifi = (CheckBox)findViewById(R.id.cboxCheckNetConnectionWIfi);
		cbKeyguardOff = (CheckBox)findViewById(R.id.checkBoxKeyguardOff);

		cbLogsOff = (CheckBox)findViewById(R.id.checkBoxDeactivateLogs);
		cbLogsOff.setOnCheckedChangeListener(this);
		tvViewLogs = (TextView)findViewById(R.id.textViewLogs);
		
		cboxBluetoothOffSleepM = (CheckBox)findViewById(R.id.checkBoxBluetoothOffSleepM);
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

		boolean autoWifiOnIsActivated = sharedPrefsEditor
				.isAutoWifiOnActivated();
		cbAutoWifiOn.setChecked(autoWifiOnIsActivated);

		boolean sleepHoursIsActivated = sharedPrefsEditor
				.isSleepHoursActivated();
		cbSleepHours.setChecked(sleepHoursIsActivated);

		boolean serviceIsDeactivated = sharedPrefsEditor.isServiceDeactivatedAll();
		cbServiceIsDeactivated.setChecked(serviceIsDeactivated);

		boolean serviceIsDeactivatedPlugged = sharedPrefsEditor.isDeactivatedWhilePlugged();
		cbServiceIsDeactivatedPlugged.setChecked(serviceIsDeactivatedPlugged); 

		//sleep hours
		String sleepTimeOn = sharedPrefsEditor.getSleepTimeOn();
		String sleepTimeOff = sharedPrefsEditor.getSleepTimeOff();

		edSleepHours.setText(sleepTimeOn + "-" + sleepTimeOff);

		TextView versionTv = (TextView)findViewById(R.id.version);
		versionTv.setText(getVersionName());


		//show or hide first time on fields
		cboxFirstTimeOn.setChecked(sharedPrefsEditor.isFirstTimeOnIsActivated());
		edFirstTimeOn.setText(String.valueOf(sharedPrefsEditor.getFirstTimeOn()));

		activateFirstTimeOn(sharedPrefsEditor.isFirstTimeOnIsActivated());

		//show or hide 2G switch fields
		ChangeNetworkMode changeNetworkMode = new ChangeNetworkMode(this);
		activate2GSwitch(changeNetworkMode.isCyanogenMod());

		cbCheckNetConnWifi.setChecked(sharedPrefsEditor.getCheckNetConnectionWifi());
		cbKeyguardOff.setChecked(sharedPrefsEditor.isEnabledWhenKeyguardOff());

		boolean logsIsDisabled = !sharedPrefsEditor.isLogsEnabled();
		cbLogsOff.setChecked(logsIsDisabled);
		setLogsButtonStatus(!logsIsDisabled);

		cboxBluetoothOffSleepM.setChecked(sharedPrefsEditor.getBluetoothDeactivateDuringSleep());

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

			String versionName = getVersionName();
			
			//attachment
			File file = new File(Environment.getExternalStorageDirectory()+File.separator+"CleverConnectivity.log");
			if (file.exists() && file.canRead()) {
				logsProvider.info("logs file is available: "+file.getName());
				Uri uri = Uri.parse("file://" + file.getAbsolutePath());
				email.putExtra(Intent.EXTRA_STREAM, uri);
			}
			else if(!file.exists())
			{
				logsProvider.info("logs file does not exist: "+file.getName());
			}
			else if(!file.canRead())
			{
				logsProvider.info("logs file cannot be read: "+file.getName());
			}
			

			email.putExtra(Intent.EXTRA_SUBJECT, MAIL_SUBJECT + " - "
					+ versionName);
			// email.putExtra(Intent.EXTRA_TEXT, "message");
			email.setType("message/rfc822");
			startActivity(Intent.createChooser(email,
					"Choose an Email client :"));

		} else if (v == buttonViewLogFile) {
			// generate log file
			//DumpLogToFile();

			// display log file
			readLogFile();
		}
		else if(v == cbox2GSwitch)
		{
			//show error message
			show2GSwitchErrorMessage();
		}
		else if(v == buttonDeactivationStc)
		{
			//create shortcut
			logsProvider.info("shortcut creation command");
			createDeactivationShortcut();
		}
		else if(v == buttonMgConnPerApp)
		{
			//open new activity
			Intent myIntent = new Intent(this, ApplicationListActivity.class);
			startActivity(myIntent);
		}

	}

	/**
	 * Stop data manager service
	 */
	public static void stopDataManagerService(Context context, SharedPrefsEditor sharedPrefsEditor) {
		// if service is started
		// if (sharedPrefsEditor.isServiceActivated()) {
		// register service stopped in preferences
		sharedPrefsEditor.setServiceActivation(false);
		context.stopService(new Intent(context, MainService.class));
		// }
	}

	public static void StartDataManagerService(Context context, SharedPrefsEditor sharedPrefsEditor) {
		// if service is not started
		// if (!sharedPrefsEditor.isServiceActivated()) {
		sharedPrefsEditor.setServiceActivation(true);
		Intent serviceIntent = new Intent(context, MainService.class);
		context.startService(serviceIntent);
		// }
	}

	/**
	 * whenever a checkbox is checked/unchecked
	 */

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		if(buttonView == cboxFirstTimeOn)
		{
			boolean isFirstTimeOnActivated = cboxFirstTimeOn.isChecked();
			activateFirstTimeOn(isFirstTimeOnActivated);
		}
		else if(buttonView == cbLogsOff)
		{
			setLogsButtonStatus(!isChecked);
		}

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

				AlarmMgr alarmMgr = new AlarmMgr(this, sharedPrefsEditor);
				alarmMgr.manageSleepingHours(sleepTimeOff, sleepTimeOn);

			}
		}
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

		int firstTimeOn = 0;
		try
		{
			firstTimeOn = Integer.parseInt(edFirstTimeOn.getText().toString());
		}
		catch(Exception e)
		{
			firstTimeOn = sharedPrefsEditor.getFirstTimeOn();
		}

		boolean dataIsActivated = cbData.isChecked();
		boolean dataMgrIsActivated = cbDataMgr.isChecked();

		boolean wifiIsActivated = cbWifi.isChecked();
		boolean wifiMgrIsActivated = cbWifiMgr.isChecked();

		boolean autoSyncIsActivated = cbAutoSync.isChecked();

		boolean autoWifiIsActivated = cbAutoWifiOff.isChecked();

		boolean autoWifiOnIsActivated = cbAutoWifiOn.isChecked();

		boolean sleepHoursIsActivated = cbSleepHours.isChecked();

		boolean isAutoSyncMgrIsActivated = cbAutoSyncMgr.isChecked();

		boolean isServiceDeactived = cbServiceIsDeactivated.isChecked();
		boolean isServiceDeactivatedPlugged = cbServiceIsDeactivatedPlugged.isChecked();

		boolean isFirstTimeOnIsActivated = cboxFirstTimeOn.isChecked();

		boolean is2GSwitchActivated = cbox2GSwitch.isChecked();

		boolean isCheckNetConnWifi = cbCheckNetConnWifi.isChecked();

		boolean isKeyguardOff = cbKeyguardOff.isChecked();

		boolean isLogsOff = !cbLogsOff.isChecked();
		
		boolean isBluetoothOffSleepM = cboxBluetoothOffSleepM.isChecked();

		// save all these preferences
		sharedPrefsEditor.setAllValues(timeOn, timeOff, intervalCheck,
				dataIsActivated, dataMgrIsActivated, wifiIsActivated,
				wifiMgrIsActivated, autoSyncIsActivated, autoWifiIsActivated,
				sleepHoursIsActivated, isAutoSyncMgrIsActivated, 
				isServiceDeactived,isServiceDeactivatedPlugged, timeOnCheck,timeScreenOnDelay,
				isFirstTimeOnIsActivated, firstTimeOn, autoWifiOnIsActivated, is2GSwitchActivated, isCheckNetConnWifi, isKeyguardOff, isLogsOff, isBluetoothOffSleepM);

		try {
			// if data is disabled; data connection is stopped
			if (!dataIsActivated) {

				// keep auto sync (in case of wifi connection)
				dataActivation.setMobileDataEnabled(false, sharedPrefsEditor);
				// dataActivation.setAutoSync(true);
			} else {
				// activate data
				dataActivation.setMobileDataEnabled(true, sharedPrefsEditor);
				// dataActivation.setAutoSync(true);
			}

			// if wifi is disabled, wifi connection is stopped
			if (!wifiIsActivated) {
				dataActivation.setWifiConnectionEnabled(false, false, sharedPrefsEditor);
				// dataActivation.setAutoSync(true);
			} else {
				dataActivation.setWifiConnectionEnabled(true, false, sharedPrefsEditor);
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
				stopDataManagerService(this, sharedPrefsEditor);
			}
			else
			{
				StartDataManagerService(this, sharedPrefsEditor);
			}


			// sleeping hours
			AlarmMgr alarmMgr = new AlarmMgr(this, sharedPrefsEditor);
			alarmMgr.manageSleepingHours(sharedPrefsEditor.getSleepTimeOff(),
					sharedPrefsEditor.getSleepTimeOn());

		} catch (Exception e) {
			logsProvider.error(e);
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
	/*public void DumpLogToFile() {
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
	}*/

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

	//get version number of the application
	public String getVersionName()
	{
		String versionName = "";
		try {
			versionName = this.getPackageManager().getPackageInfo(
					this.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			logsProvider.error(e);
		}

		return versionName;

	}


	/**
	 * SHow or hide fields related to first time off
	 * @param isActivate
	 */
	private void activateFirstTimeOn(boolean isActivate)
	{


		if(!isActivate)
		{
			//hide fields related to first time off
			tvFirstTimeOn.setEnabled(false);
			tvFirstTimeOnDesc.setEnabled(false);
			edFirstTimeOn.setEnabled(false);

		}
		else
		{
			//show fields related to first time off
			tvFirstTimeOn.setEnabled(true);
			tvFirstTimeOnDesc.setEnabled(true);
			edFirstTimeOn.setEnabled(true);


		}
	}

	/**
	 * SHow or hide fields related to the 2g switch
	 * @param isActivate
	 */
	private void activate2GSwitch(boolean isActivate)
	{


		if(!isActivate)
		{
			//hide fields related to 2G switch
			tv2GSwitch.setText(R.string.switch_incompatible2G);
			tv2GSwitch.setEnabled(false);
			cbox2GSwitch.setEnabled(false);

			//set an onclicklistener to show an error msg
			cbox2GSwitch.setOnClickListener(this);

		}
		else
		{
			//show fields related to 2G switch
			tv2GSwitch.setEnabled(true);
			cbox2GSwitch.setEnabled(true);

			//init checkbox
			cbox2GSwitch.setChecked(sharedPrefsEditor.is2GSwitchActivated());

		}
	}

	/*public void populateAdMobs()
	{
		// Starting RevMob session
	    revmob = RevMob.start(this, APPLICATION_ID);
	    RevMobBanner banner1 = revmob.createBanner(this);
	    RevMobBanner banner2 = revmob.createBanner(this);

	    // Lookup your LinearLayout (We are assuming it�s been given
	    // the attribute android:id="@+id/banner")
	    LinearLayout ad1 = (LinearLayout)findViewById(R.id.bannerAds1);
	    LinearLayout ad2 = (LinearLayout)findViewById(R.id.bannerAds2);

	    // Add the banner to it
	    ad1.removeAllViews();
	    ad1.addView(banner1);

	    ad2.removeAllViews();
	    ad2.addView(banner2);
	}*/

	public void show2GSwitchErrorMessage()
	{
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		// 2. Chain together various setter methods to set the dialog characteristics
		builder.setMessage(R.string.switch_incompatible2G)
		.setTitle("Error...");


		// 3. Get the AlertDialog from create()
		AlertDialog dialog = builder.create();

		dialog.show();
	}


	/**
	 * Create deactivation shortcut on homescreen
	 */
	public void createDeactivationShortcut()
	{
		Intent shortcutIntent = new Intent(getApplicationContext(),
				ShortcutActivateReceiver.class);


		Intent addIntent = new Intent();
		addIntent
		.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getResources().getString(R.string.shortcut_activation_name));
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
				Intent.ShortcutIconResource.fromContext(getApplicationContext(),
						R.drawable.ic_launcher));

		addIntent
		.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		getApplicationContext().sendBroadcast(addIntent);
	}


	public void setLogsButtonStatus(boolean isDisabled)
	{
		tvViewLogs.setEnabled(isDisabled);
		buttonViewLogFile.setEnabled(isDisabled);

	}


}
