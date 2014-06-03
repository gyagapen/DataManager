package com.datamanager.tabActivities;

import com.actionbarsherlock.app.SherlockFragment;
import com.datamanager.core.DataActivation;
import com.datamanager.core.LogsProvider;
import com.datamanager.core.SharedPrefsEditor;
import com.gyagapen.cleverconnectivity.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

public class TimersTabActivity extends SherlockFragment {

	private EditText edTimeOn = null;
	private EditText edTimeOnCheck = null;
	private EditText edTimeOff = null;
	private EditText edInterval = null;

	private LogsProvider logsProvider = null;

	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;
	private DataActivation dataActivation;

	private View rootView;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.timers_tab, container, false);

		logsProvider = new LogsProvider(rootView.getContext(), this.getClass());

		// shared prefs init
		prefs = rootView.getContext().getSharedPreferences(
				SharedPrefsEditor.PREFERENCE_NAME, AppLauncher.SHARED_ACCESS_MODE);

		dataActivation = new DataActivation(rootView.getContext());
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);

		loadUiComponents();
		initializeUiComponentsData();

		return rootView;

	}

	/**
	 * 
	 * Load of components of UI
	 */
	private void loadUiComponents() {

		edTimeOn = (EditText) rootView.findViewById(R.id.editTextTimeOn);
		edTimeOnCheck = (EditText) rootView
				.findViewById(R.id.EditTextTimeOnCheck);
		edTimeOff = (EditText) rootView.findViewById(R.id.editTextTimeOff);
		edInterval = (EditText) rootView.findViewById(R.id.editTextInterval);
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

		int checkTime = sharedPrefsEditor.getIntervalCheck();
		edInterval.setText(String.valueOf(checkTime));

	}

	private void applySettings() {
		// get settings values
		int timeOn = 0;
		try {
			timeOn = Integer.parseInt(edTimeOn.getText().toString());
		} catch (Exception e) {
			timeOn = sharedPrefsEditor.getTimeOn();
		}

		int timeOnCheck = 0;
		try {
			timeOnCheck = Integer.parseInt(edTimeOnCheck.getText().toString());
		} catch (Exception e) {
			timeOnCheck = sharedPrefsEditor.getTimeOnCheck();
		}

		int timeOff = 0;

		try {
			timeOff = Integer.parseInt(edTimeOff.getText().toString());
		} catch (Exception e) {
			timeOff = sharedPrefsEditor.getTimeOff();
		}

		int intervalCheck = 0;
		try {
			intervalCheck = Integer.parseInt(edInterval.getText().toString());
		} catch (Exception e) {
			intervalCheck = sharedPrefsEditor.getIntervalCheck();
		}

		sharedPrefsEditor.setTimeOn(timeOn);
		sharedPrefsEditor.setTimeOff(timeOff);
		sharedPrefsEditor.setTimeOnCheck(timeOnCheck);
		sharedPrefsEditor.setIntervalCheckTime(intervalCheck);

	}

	public void onDestroy() {
		applySettings();
		logsProvider.info("on destroy is called");
		super.onDestroy();
	}
	
	public void onPause() {
		applySettings();
		logsProvider.info("onPause is called");
		super.onPause();
	}

}
