package tabActivities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;

import com.example.datamanager.DataActivation;
import com.example.datamanager.LogsProvider;
import com.example.datamanager.SharedPrefsEditor;
import com.gyagapen.cleverconnectivity.R;

public class SyncTabActivity extends Activity {

	private CheckBox cbAutoSync = null;
	private CheckBox cbAutoSyncMgr = null;

	private LogsProvider logsProvider = null;

	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;
	private DataActivation dataActivation;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_sync);

		logsProvider = new LogsProvider(getApplicationContext(), this.getClass());

		// shared prefs init
		prefs = getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				Activity.MODE_PRIVATE);

		dataActivation = new DataActivation(getBaseContext());
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);

		loadUiComponents();
		initializeUiComponentsData();


	}


	/**
	 * 
	 * Load of components of UI
	 */
	private void loadUiComponents() {

		cbAutoSync = (CheckBox) findViewById(R.id.checkBoxAutoSync);
		cbAutoSyncMgr = (CheckBox) findViewById(R.id.checkBoxAutoSyncMgr);
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



	private void applySettings()
	{
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



	protected void onDestroy() {

		applySettings();

		super.onDestroy();
	}



}
