package com.datamanager.tabActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.datamanager.core.DataActivation;
import com.datamanager.core.LogsProvider;
import com.datamanager.core.SharedPrefsEditor;
import com.datamanager.core.ShortcutActivateReceiver;
import com.gyagapen.cleverconnectivity.R;

public class AdvancedTabActivity extends SherlockFragment implements
		OnClickListener, OnCheckedChangeListener {

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

	private View rootView;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.advanced_tab, container, false);

		logsProvider = new LogsProvider(rootView.getContext(), this.getClass());

		// shared prefs init
		prefs = rootView.getContext().getSharedPreferences(
				SharedPrefsEditor.PREFERENCE_NAME, AppLauncher.SHARED_ACCESS_MODE);

		dataActivation = new DataActivation(rootView.getContext());
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);

		buttonDeactivationStc = (Button) rootView
				.findViewById(R.id.button_deactivation_shortcut);
		buttonDeactivationStc.setOnClickListener(this);

		buttonMgConnPerApp = (Button) rootView
				.findViewById(R.id.button_manage_app);
		buttonMgConnPerApp.setOnClickListener(this);

		loadUiComponents();
		initializeUiComponentsData();

		return rootView;

	}

	public void onClick(View v) {

		if (v == buttonDeactivationStc) {
			// create shortcut
			logsProvider.info("shortcut creation command");
			createDeactivationShortcut();
		} else if (v == buttonMgConnPerApp) {
			
			//set manageAppIsOpen to avoid restart on main service
			sharedPrefsEditor.setManageAppIsOpened(true);
			
			// open new activity
			Intent myIntent = new Intent(rootView.getContext(),
					ApplicationListActivity.class);
			startActivity(myIntent);
		}

	}

	/**
	 * 
	 * Load of components of UI
	 */
	private void loadUiComponents() {

		edScreenOnDelay = (EditText) rootView
				.findViewById(R.id.EditTextScreenDelay);

		// load fields related to firstTimeOn
		tvFirstTimeOn = (TextView) rootView
				.findViewById(R.id.textViewFirstTimeOn);
		tvFirstTimeOnDesc = (TextView) rootView
				.findViewById(R.id.textViewFirstTimeOnDesc);
		cboxFirstTimeOn = (CheckBox) rootView
				.findViewById(R.id.checkBoxFirstTimeOn);
		cboxFirstTimeOn.setOnCheckedChangeListener(this);
		edFirstTimeOn = (EditText) rootView
				.findViewById(R.id.EditTextFirstTimeOn);

	}

	/**
	 * Init ui components from value extracted from shared preferences
	 */
	private void initializeUiComponentsData() {

		int timeScreenOnDelay = sharedPrefsEditor.getScreenDelayTimer();
		edScreenOnDelay.setText(String.valueOf(timeScreenOnDelay));

		// show or hide first time on fields
		cboxFirstTimeOn
				.setChecked(sharedPrefsEditor.isFirstTimeOnIsActivated());
		edFirstTimeOn
				.setText(String.valueOf(sharedPrefsEditor.getFirstTimeOn()));

		activateFirstTimeOn(sharedPrefsEditor.isFirstTimeOnIsActivated());

	}

	private void applySettings() {
		int timeScreenOnDelay = 0;
		try {
			timeScreenOnDelay = Integer.parseInt(edScreenOnDelay.getText()
					.toString());
		} catch (Exception e) {
			timeScreenOnDelay = sharedPrefsEditor.getScreenDelayTimer();
		}

		int firstTimeOn = 0;
		try {
			firstTimeOn = Integer.parseInt(edFirstTimeOn.getText().toString());
		} catch (Exception e) {
			firstTimeOn = sharedPrefsEditor.getFirstTimeOn();
		}

		boolean isFirstTimeOnIsActivated = cboxFirstTimeOn.isChecked();

		sharedPrefsEditor.setFirstTimeOnIsActivated(isFirstTimeOnIsActivated);
		sharedPrefsEditor.setScreenDelayTimer(timeScreenOnDelay);
		sharedPrefsEditor.setFirstTimeOn(firstTimeOn);
	}

	public void onDestroy() {
		applySettings();
		super.onDestroy();
	}

	/**
	 * Create deactivation shortcut on homescreen
	 */
	public void createDeactivationShortcut() {
		Intent shortcutIntent = new Intent(rootView.getContext(),
				ShortcutActivateReceiver.class);

		Intent addIntent = new Intent();
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getResources()
				.getString(R.string.shortcut_activation_name));
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
				Intent.ShortcutIconResource.fromContext(
						rootView.getContext(), R.drawable.ic_launcher));

		addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		rootView.getContext().sendBroadcast(addIntent);
	}

	/**
	 * whenever a checkbox is checked/unchecked
	 */

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		if (buttonView == cboxFirstTimeOn) {
			boolean isFirstTimeOnActivated = cboxFirstTimeOn.isChecked();
			activateFirstTimeOn(isFirstTimeOnActivated);
		}

	}

	/**
	 * SHow or hide fields related to first time off
	 * 
	 * @param isActivate
	 */
	private void activateFirstTimeOn(boolean isActivate) {

		if (!isActivate) {
			// hide fields related to first time off
			tvFirstTimeOn.setEnabled(false);
			tvFirstTimeOnDesc.setEnabled(false);
			edFirstTimeOn.setEnabled(false);

		} else {
			// show fields related to first time off
			tvFirstTimeOn.setEnabled(true);
			tvFirstTimeOnDesc.setEnabled(true);
			edFirstTimeOn.setEnabled(true);

		}
	}
	
	public void onPause() {
		applySettings();
		super.onPause();
	}

}
