package tabActivities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.example.datamanager.ApplicationListActivity;
import com.example.datamanager.DataActivation;
import com.example.datamanager.LogsProvider;
import com.example.datamanager.SharedPrefsEditor;
import com.example.datamanager.ShortcutActivateReceiver;
import com.gyagapen.cleverconnectivity.R;

public class AdvancedTabActivity extends Activity implements OnClickListener, OnCheckedChangeListener {

	private EditText edScreenOnDelay = null;
	private CheckBox cboxFirstTimeOn = null;
	private TextView tvFirstTimeOn = null;
	private TextView tvFirstTimeOnDesc = null;
	private EditText edFirstTimeOn = null;
	private Button buttonDeactivationStc = null;
	private Button buttonMgConnPerApp = null;

	private LogsProvider logsProvider = null;

	
	
	
	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;
	private DataActivation dataActivation;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.advanced_tab);

		logsProvider = new LogsProvider(getApplicationContext(), this.getClass());
		
		// shared prefs init
		prefs = getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				Activity.MODE_PRIVATE);

		dataActivation = new DataActivation(getBaseContext());
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);

		buttonDeactivationStc = (Button) findViewById(R.id.button_deactivation_shortcut);
		buttonDeactivationStc.setOnClickListener(this);

		buttonMgConnPerApp = (Button)findViewById(R.id.button_manage_app);
		buttonMgConnPerApp.setOnClickListener(this);

		loadUiComponents();
		initializeUiComponentsData();

	}

	public void onClick(View v) {


		if(v == buttonDeactivationStc)
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
	 * 
	 * Load of components of UI
	 */
	private void loadUiComponents() {

		edScreenOnDelay = (EditText) findViewById(R.id.EditTextScreenDelay);

		//load fields related to firstTimeOn
		tvFirstTimeOn = (TextView) findViewById(R.id.textViewFirstTimeOn);
		tvFirstTimeOnDesc = (TextView) findViewById(R.id.textViewFirstTimeOnDesc);
		cboxFirstTimeOn = (CheckBox)findViewById(R.id.checkBoxFirstTimeOn);
		cboxFirstTimeOn.setOnCheckedChangeListener(this);
		edFirstTimeOn = (EditText)findViewById(R.id.EditTextFirstTimeOn);

	}



	/**
	 * Init ui components from value extracted from shared preferences
	 */
	private void initializeUiComponentsData() {

		int timeScreenOnDelay = sharedPrefsEditor.getScreenDelayTimer();
		edScreenOnDelay.setText(String.valueOf(timeScreenOnDelay));


		//show or hide first time on fields
		cboxFirstTimeOn.setChecked(sharedPrefsEditor.isFirstTimeOnIsActivated());
		edFirstTimeOn.setText(String.valueOf(sharedPrefsEditor.getFirstTimeOn()));

		activateFirstTimeOn(sharedPrefsEditor.isFirstTimeOnIsActivated());
		
		

	}



	private void applySettings()
	{
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
		
		boolean isFirstTimeOnIsActivated = cboxFirstTimeOn.isChecked();
		
		sharedPrefsEditor.setFirstTimeOnIsActivated(isFirstTimeOnIsActivated);
		sharedPrefsEditor.setScreenDelayTimer(timeScreenOnDelay);
		sharedPrefsEditor.setFirstTimeOn(firstTimeOn);
	}



	protected void onDestroy() {

		applySettings();

		super.onDestroy();
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

	/**
	 * whenever a checkbox is checked/unchecked
	 */

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		if(buttonView == cboxFirstTimeOn)
		{
			boolean isFirstTimeOnActivated = cboxFirstTimeOn.isChecked();
			activateFirstTimeOn(isFirstTimeOnActivated);
		}


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





}
