package tabActivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.example.datamanager.DataActivation;
import com.example.datamanager.LogsProvider;
import com.example.datamanager.MainService;
import com.example.datamanager.MenuListAdapter;
import com.example.datamanager.SaveConnectionPreferences;
import com.example.datamanager.SharedPrefsEditor;
import com.gyagapen.cleverconnectivity.R;

/**
 * This class is in charge of launching main ui
 * 
 * @author Gui
 * 
 */
public class AppLauncher extends SherlockFragmentActivity {

	// initialization variable for cconnectivity
	private LogsProvider logsProvider = null;

	public static final String STR_ACTIVATE_CONNECTIVITY = "activateConnectivity";

	public static final int ID_ALARM_TIME_ON = 1879;
	public static final int ID_ALARM_TIME_OFF = 1899;

	public static final String SEPARATOR = "##";

	public static final String LOG_FILE_NAME = "CleverConnectivity.log";

	public static final boolean APPLICATION_IS_FREE = true;

	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;
	private DataActivation dataActivation;

	// mail data
	public static final String MAIL_RECIPIENT = "gyagapen@gmail.com";
	public static String MAIL_SUBJECT = "CleverConnectivity Bug Report";
	public static final String MAIL_SUBJECT_PAID = "CleverConnectivity Paid Bug Report";

	// Declare Variables for side menu
	DrawerLayout mDrawerLayout;
	ListView mDrawerList;
	ActionBarDrawerToggle mDrawerToggle;
	MenuListAdapter mMenuAdapter;
	String[] title;
	String[] subtitle;
	int[] icon;
	Fragment fragmentGeneral = new GeneralTabActivity();
	Fragment fragmentData = new DataTabActivity();
	Fragment fragmentWifi = new WifiTabActivity();
	Fragment fragmentSync = new SyncTabActivity();
	Fragment fragmentBluetooth = new BluetoothTabActivity();
	Fragment fragmentSleep = new SleepTimerPickerActivity();
	Fragment fragmentTimers = new TimersTabActivity();
	Fragment fragmentAdvanced = new AdvancedTabActivity();
	Fragment fragmentMisc = new MiscTabActivity();
	// Fragment fragmentSleep = new SleepTimerPickerActivity();
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from drawer_main.xml
		setContentView(R.layout.drawer_main);

		logsProvider = new LogsProvider(getApplicationContext(),
				this.getClass());

		// initialize connectivity positions
		SaveConnectionPreferences connPrefs = new SaveConnectionPreferences(
				getApplicationContext());
		connPrefs.saveAllConnectionSettingInSharedPrefs();

		// building side bar menu
		setUpSideBar(savedInstanceState);

	}

	private void setUpSideBar(Bundle savedInstanceState) {
		// Get the Title
		mTitle = mDrawerTitle = getTitle();

		// Generate title
		title = new String[] { "General", "Data", "Wifi", "Sync", "Bluetooth",
				"Sleep", "Timers", "Advanced", "Misc" };

		// Generate subtitle
		subtitle = new String[] { "", "", "", "", "", "", "", "", "", "" };

		// Generate icon
		icon = new int[] { R.drawable.general, R.drawable.data,
				R.drawable.wifi, R.drawable.sync, R.drawable.bluetooth,
				R.drawable.sleep, R.drawable.timers, R.drawable.advanced,
				R.drawable.misc };

		// Locate DrawerLayout in drawer_main.xml
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		// Locate ListView in drawer_main.xml
		mDrawerList = (ListView) findViewById(R.id.listview_drawer);

		// Set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		// Pass string arrays to MenuListAdapter
		mMenuAdapter = new MenuListAdapter(AppLauncher.this, title, subtitle,
				icon);

		// Set the MenuListAdapter to the ListView
		mDrawerList.setAdapter(mMenuAdapter);

		// Capture listview menu item click
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// Enable ActionBar app icon to behave as action to toggle nav drawer
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			public void onDrawerClosed(View view) {
				// TODO Auto-generated method stub
				super.onDrawerClosed(view);
			}

			public void onDrawerOpened(View drawerView) {
				// TODO Auto-generated method stub
				// Set the title on the action when drawer open
				getSupportActionBar().setTitle(mDrawerTitle);
				super.onDrawerOpened(drawerView);
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(0);
		}

	}

	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {

			if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
				mDrawerLayout.closeDrawer(mDrawerList);
			} else {
				mDrawerLayout.openDrawer(mDrawerList);
			}
		}

		return super.onOptionsItemSelected(item);
	}

	// ListView click listener in the navigation drawer
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		// Locate Position
		switch (position) {
		case 0:
			ft.replace(R.id.content_frame, fragmentGeneral);
			break;
		case 1:
			ft.replace(R.id.content_frame, fragmentData);
			break;
		case 2:
			ft.replace(R.id.content_frame, fragmentWifi);
			break;
		case 3:
			ft.replace(R.id.content_frame, fragmentSync);
			break;
		case 4:
			ft.replace(R.id.content_frame, fragmentBluetooth);
			break;
		case 5:
			ft.replace(R.id.content_frame, fragmentSleep);
			break;
		case 6:
			ft.replace(R.id.content_frame, fragmentTimers);
			break;
		case 7:
			ft.replace(R.id.content_frame, fragmentAdvanced);
			break;
		case 8:
			ft.replace(R.id.content_frame, fragmentMisc);
			break;

		}

		ft.commit();
		mDrawerList.setItemChecked(position, true);

		// Get the title followed by the position
		setTitle(title[position]);
		// Close drawer
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}

	/**
	 * Whenever application is closed
	 */

	protected void onDestroy() {

		super.onDestroy();

		System.exit(0);

	}

	private void applySettings() {

		// shared prefs init
		prefs = getSharedPreferences(
				SharedPrefsEditor.PREFERENCE_NAME, Activity.MODE_PRIVATE);

		dataActivation = new DataActivation(this);
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);

		boolean isServiceDeactived = sharedPrefsEditor.isServiceDeactivatedAll();
		boolean isServiceDeactivatedPlugged = sharedPrefsEditor.isDeactivatedWhilePlugged();
		

		// save the last screen state
		sharedPrefsEditor.setScrenWasOff(false);

		// stop service if deactivate is checked or deactivate while plugged
		// check and phone is plugged
		if (isServiceDeactived
				|| (isServiceDeactivatedPlugged && dataActivation
						.isPhonePlugged())) {
			stopDataManagerService(this,
					sharedPrefsEditor);
		} else {
			StartDataManagerService(this,
					sharedPrefsEditor);
		}
		
	}

	/**
	 * Stop data manager service
	 */
	private void stopDataManagerService(Context context,
			SharedPrefsEditor sharedPrefsEditor) {
		// if service is started
		// if (sharedPrefsEditor.isServiceActivated()) {
		// register service stopped in preferences
		sharedPrefsEditor.setServiceActivation(false);
		context.stopService(new Intent(context, MainService.class));
		// }
	}

	private void StartDataManagerService(Context context,
			SharedPrefsEditor sharedPrefsEditor) {
		// if service is not started
		// if (!sharedPrefsEditor.isServiceActivated()) {
		sharedPrefsEditor.setServiceActivation(true);
		Intent serviceIntent = new Intent(context, MainService.class);
		context.startService(serviceIntent);
		// }
	}
	
	protected void onStop() {
		logsProvider.info("main onStop is called");
		applySettings();
		super.onStop();
	}

}
