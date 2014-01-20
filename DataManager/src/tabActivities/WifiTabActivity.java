package tabActivities;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.datamanager.DataActivation;
import com.example.datamanager.LogsProvider;
import com.example.datamanager.SharedPrefsEditor;
import com.gyagapen.cleverconnectivity.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

public class WifiTabActivity extends SherlockFragment {

	private CheckBox cbWifi = null;
	private CheckBox cbWifiMgr = null;
	private CheckBox cbAutoWifiOff = null;
	private CheckBox cbAutoWifiOn = null;
	private CheckBox cbCheckNetConnWifi = null;

	private LogsProvider logsProvider = null;

	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;
	private DataActivation dataActivation;

	private View rootView;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.tab_wifi, container, false);

		logsProvider = new LogsProvider(rootView.getContext(), this.getClass());

		// shared prefs init
		prefs = rootView.getContext().getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				Activity.MODE_PRIVATE);

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

		cbWifi = (CheckBox) rootView.findViewById(R.id.checkBoxWifi);
		cbWifiMgr = (CheckBox) rootView.findViewById(R.id.checkBoxWifiMgr);
		cbAutoWifiOff = (CheckBox) rootView.findViewById(R.id.checkBoxAutoWifiOff);
		cbAutoWifiOn = (CheckBox) rootView.findViewById(R.id.CheckBoxAutoWifiOn);
		cbCheckNetConnWifi = (CheckBox)rootView.findViewById(R.id.cboxCheckNetConnectionWIfi);

	}



	/**
	 * Init ui components from value extracted from shared preferences
	 */
	private void initializeUiComponentsData() {

		boolean wifiIsActivated = sharedPrefsEditor.isWifiActivated();
		cbWifi.setChecked(wifiIsActivated);

		boolean wifiMgrIsActivated = sharedPrefsEditor.isWifiManagerActivated();
		cbWifiMgr.setChecked(wifiMgrIsActivated);

		boolean autoWifiOffIsActivated = sharedPrefsEditor
				.isAutoWifiOffActivated();
		cbAutoWifiOff.setChecked(autoWifiOffIsActivated);

		boolean autoWifiOnIsActivated = sharedPrefsEditor
				.isAutoWifiOnActivated();
		cbAutoWifiOn.setChecked(autoWifiOnIsActivated);

		cbCheckNetConnWifi.setChecked(sharedPrefsEditor.getCheckNetConnectionWifi());
	}



	private void applySettings()
	{

		boolean wifiIsActivated = cbWifi.isChecked();
		boolean wifiMgrIsActivated = cbWifiMgr.isChecked();
		boolean autoWifiIsActivated = cbAutoWifiOff.isChecked();
		boolean autoWifiOnIsActivated = cbAutoWifiOn.isChecked();
		boolean isCheckNetConnWifi = cbCheckNetConnWifi.isChecked();

		sharedPrefsEditor.setWifiActivation(wifiIsActivated);
		sharedPrefsEditor.setWifiActivationManager(wifiMgrIsActivated);
		sharedPrefsEditor.setAutoWifiOffActivation(autoWifiIsActivated);
		sharedPrefsEditor.setAutoWifiOnActivation(autoWifiOnIsActivated);
		sharedPrefsEditor.setCheckNetConnectionWifi(isCheckNetConnWifi);

		// if wifi is disabled, wifi connection is stopped
		if (!wifiIsActivated) {
			dataActivation.setWifiConnectionEnabled(false, false, sharedPrefsEditor);
		} else {
			dataActivation.setWifiConnectionEnabled(true, false, sharedPrefsEditor);
			// disable data if option isDataOffWhenWifi checked
			if (sharedPrefsEditor.isDataOffWhenWifi()) {
				try {
					dataActivation.setMobileDataEnabled(false,
							sharedPrefsEditor);
				} catch (Exception e) {
					logsProvider.error(e);
					e.printStackTrace();
				}
			}

		}


	}


	public void onDestroy() {
		applySettings();
		super.onDestroy();
	}



}
