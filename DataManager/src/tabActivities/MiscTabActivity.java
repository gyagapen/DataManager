package tabActivities;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.example.datamanager.DataActivation;
import com.example.datamanager.LogsProvider;
import com.example.datamanager.SharedPrefsEditor;
import com.gyagapen.cleverconnectivity.R;

public class MiscTabActivity extends Activity implements OnClickListener, OnCheckedChangeListener {
	
	
	private CheckBox cbLogsOff = null;
	private TextView tvViewLogs = null;
	private Button buttonReportBug = null;
	private Button buttonViewLogFile = null;
	private TextView versionTv = null;
	
	private LogsProvider logsProvider = null;

	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;
	private DataActivation dataActivation;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.misc_tab);
		
		logsProvider = new LogsProvider(getApplicationContext(), this.getClass());

		// shared prefs init
		prefs = getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				Activity.MODE_PRIVATE);

		dataActivation = new DataActivation(getBaseContext());
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);
		
		buttonReportBug = (Button) findViewById(R.id.buttonReportBug);
		buttonReportBug.setOnClickListener(this);

		buttonViewLogFile = (Button) findViewById(R.id.buttonLogFile);
		buttonViewLogFile.setOnClickListener(this);

		
		loadUiComponents();
		initializeUiComponentsData();

		
	}
	
	/**
	 * 
	 * Load of components of UI
	 */
	private void loadUiComponents() {
		
		versionTv = (TextView)findViewById(R.id.version);
		cbLogsOff = (CheckBox)findViewById(R.id.checkBoxDeactivateLogs);
		cbLogsOff.setOnCheckedChangeListener(this);
		tvViewLogs = (TextView)findViewById(R.id.textViewLogs);
	}
	
	
	
	/**
	 * Init ui components from value extracted from shared preferences
	 */
	private void initializeUiComponentsData() {

		versionTv.setText(getVersionName());
		boolean logsIsDisabled = !sharedPrefsEditor.isLogsEnabled();
		cbLogsOff.setChecked(logsIsDisabled);
		setLogsButtonStatus(!logsIsDisabled);
	}
	
	
	
	private void applySettings()
	{
		boolean isLogsOff = !cbLogsOff.isChecked();
		sharedPrefsEditor.setLogsEnabled(isLogsOff);
	}
	
	public void setLogsButtonStatus(boolean isDisabled)
	{
		tvViewLogs.setEnabled(isDisabled);
		buttonViewLogFile.setEnabled(isDisabled);

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

	
	protected void onDestroy() {
		
		applySettings();
		
		super.onDestroy();
	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		
		if(buttonView == cbLogsOff)
		{
			setLogsButtonStatus(!isChecked);
		}

		
	}

	public void onClick(View v) {
		if (v == buttonReportBug) {
			// send mail to dev
			Intent email = new Intent(Intent.ACTION_SEND);
			email.putExtra(Intent.EXTRA_EMAIL, new String[] { MainTabActivity.MAIL_RECIPIENT });

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
			

			email.putExtra(Intent.EXTRA_SUBJECT, MainTabActivity.MAIL_SUBJECT + " - "
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
	}

	
	/**
	 * Display captured logfile
	 */
	public void readLogFile() {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		Uri logFileUri = Uri.parse("file://"
				+ Environment.getExternalStorageDirectory() + "/"
				+ MainTabActivity.LOG_FILE_NAME);
		intent.setDataAndType(logFileUri, "text/plain");
		startActivity(intent);
	}
	
	

	
}
