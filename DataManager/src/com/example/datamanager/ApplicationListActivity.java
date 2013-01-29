package com.example.datamanager;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;

import com.google.ads.ah;
import com.gyagapen.cleverconnectivity.R;

public class ApplicationListActivity extends Activity implements OnCheckedChangeListener, Runnable {


	private ListView listViewApps = null;
	private CheckBox cboxAppMgrConnSwitch = null;

	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;

	//waiting dialog
	private ProgressDialog progressDialog;

	private DataActivation dataActivation;
	
	private Handler mHandler = null;
	private Activity activity = null;
	private ArrayList<ApplicationDetail> apps = null;



	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.application_list);

		// shared prefs init
		prefs = this.getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				Activity.MODE_PRIVATE);
		dataActivation = new DataActivation(this);
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);



		//master switch init
		cboxAppMgrConnSwitch = (CheckBox)findViewById(R.id.cBoxAppConnMgrMasterSwitch);
		cboxAppMgrConnSwitch.setChecked(sharedPrefsEditor.isApplicationConnMgrActivated());



		//populate list view
		listViewApps = (ListView)findViewById(R.id.listViewApplications);
		listViewApps.setFastScrollEnabled(true);
		listViewApps.setTextFilterEnabled(true);


		

		// display waiting dialog
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Please wait...");

		progressDialog.show();
		
		activity = this;
		
		 mHandler = new Handler() {
		    public void handleMessage(Message msg) {
		    	
		    	if(msg.what == 0)
		    	{
		    		
		    		ListAppAdapter listAppAdapter = new ListAppAdapter(apps, activity , sharedPrefsEditor);

		    		listViewApps.setAdapter(listAppAdapter);
		    	}
		    }
		};

		// on cree une nouveau thread qui va faire la demande id Mobitrans
		// pendant ce temps une boite de dialogue demandant de patienter
		// s'affichera
		Thread t1 = new Thread(this);

		t1.start();


	}



	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		//master switch
		if(buttonView == cboxAppMgrConnSwitch)
		{
			sharedPrefsEditor.setApplicationConnMgrActivatation(isChecked);

		}

	}



	public void run() {

		
		//populate list
		ApplicationsManager appsMgr = new ApplicationsManager(getApplicationContext());
		 apps = appsMgr.getInstalledApps(false, sharedPrefsEditor);
		//sort list
		Collections.sort(apps);


		mHandler.sendEmptyMessage(0);
		

		progressDialog.dismiss();

	}

}
