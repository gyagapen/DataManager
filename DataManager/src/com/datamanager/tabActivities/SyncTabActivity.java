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

public class SyncTabActivity extends SherlockFragment {

	private CheckBox cbAutoSync = null;
	private CheckBox cbAutoSyncMgr = null;

	private LogsProvider logsProvider = null;

	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;
	private DataActivation dataActivation;

	private View rootView;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.tab_sync, container, false);

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

		cbAutoSync = (CheckBox) rootView.findViewById(R.id.checkBoxAutoSync);
		cbAutoSyncMgr = (CheckBox) rootView
				.findViewById(R.id.checkBoxAutoSyncMgr);
	}

	/**
	 * Init ui components from value extracted from shared preferences
	 */
	private void initializeUiComponentsData() {

		boolean autoSyncIsActivated = sharedPrefsEditor.isAutoSyncActivated();
		cbAutoSync.setChecked(autoSyncIsActivated);

		boolean autoSyncMgrIsActivated = sharedPrefsEditor
				.isAutoSyncMgrIsActivated();
		cbAutoSyncMgr.setChecked(autoSyncMgrIsActivated);

	}

	private void applySettings() {
		boolean autoSyncIsActivated = cbAutoSync.isChecked();
		boolean isAutoSyncMgrIsActivated = cbAutoSyncMgr.isChecked();

		sharedPrefsEditor.setAutoSyncActivation(autoSyncIsActivated);
		sharedPrefsEditor.setAutoSyncMgrActivation(isAutoSyncMgrIsActivated);

		// enable/disable autosync
		if (autoSyncIsActivated) {
			dataActivation.setAutoSync(true, sharedPrefsEditor, false);
		} else {
			dataActivation.setAutoSync(false, sharedPrefsEditor, false);

		}

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
