package com.datamanager.tabActivities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.actionbarsherlock.app.SherlockFragment;
import com.datamanager.core.DataActivation;
import com.datamanager.core.LogsProvider;
import com.datamanager.core.SharedPrefsEditor;
import com.gyagapen.cleverconnectivity.R;

public class BluetoothTabActivity extends SherlockFragment {
	
	private CheckBox cboxBluetoothOffSleepM = null;
	
	private LogsProvider logsProvider = null;

	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;
	private DataActivation dataActivation;

	
	
	private View rootView;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.tab_bluetooth, container, false);
		
		logsProvider = new LogsProvider(rootView.getContext(), this.getClass());

		// shared prefs init
		prefs = rootView.getContext().getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				AppLauncher.SHARED_ACCESS_MODE);

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
		
		cboxBluetoothOffSleepM = (CheckBox)rootView.findViewById(R.id.checkBoxBluetoothOffSleepM);
	}
	
	
	
	/**
	 * Init ui components from value extracted from shared preferences
	 */
	private void initializeUiComponentsData() {

		cboxBluetoothOffSleepM.setChecked(sharedPrefsEditor.getBluetoothDeactivateDuringSleep());
	}
	
	
	
	private void applySettings()
	{
		boolean isBluetoothOffSleepM = cboxBluetoothOffSleepM.isChecked();
		
		sharedPrefsEditor.setBluetoothDeactivationDuringSleep(isBluetoothOffSleepM);
	}
	
	
	
	public void onDestroy() {
		applySettings();
		super.onDestroy();
	}
	
	public void onPause() {
		applySettings();
		super.onPause();
	}


}
