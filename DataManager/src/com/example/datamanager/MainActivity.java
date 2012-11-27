package com.example.datamanager;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener {
	
	
	//ui components
	CheckBox cbData = null;
	CheckBox cbDataMgr = null;
	EditText edTimeOn = null;
	EditText edTimeOff = null;
	EditText edInterval = null;
	Button buttonSave = null;


	//SharedPreferences
	SharedPreferences prefs = null;
	SharedPrefsEditor sharedPrefsEditor = null;
	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //shared prefs init
        prefs = getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME, Activity.MODE_PRIVATE);
        sharedPrefsEditor = new SharedPrefsEditor(prefs);
        
        try {
			sharedPrefsEditor.initializePreferences();
		
	        setContentView(R.layout.activity_main);
	        
	        //init ui
	        loadUiComponents();
	        initializeUiComponentsData();
	        
	        //instanciate save button
	        buttonSave = (Button)findViewById(R.id.buttonSave);
	        buttonSave.setOnClickListener(this);
	        
	        //lancement du service
	        Intent serviceIntent = new Intent(this, MainService.class);
	        startService(serviceIntent);
			
		} catch (IOException e) {

			e.printStackTrace();
		}
                

        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    
	
	
	/**
	 * 
	 * Load of components of UI
	 */
	private void loadUiComponents()
	{
		cbData = (CheckBox)findViewById(R.id.checkBoxData);
		cbDataMgr = (CheckBox)findViewById(R.id.checkBoxDataMgr);
		
		edTimeOn = (EditText)findViewById(R.id.editTextTimeOn);
		edTimeOff = (EditText)findViewById(R.id.editTextTimeOff);
		edInterval = (EditText)findViewById(R.id.editTextInterval);
		
	}
	
	/**
	 * Init ui components from value extracted from shared preferences
	 */
	private void initializeUiComponentsData()
	{
		int timeOn = sharedPrefsEditor.getTimeOn();
		edTimeOn.setText(String.valueOf(timeOn));
		
		int timeOff = sharedPrefsEditor.getTimeOff();
		edTimeOff.setText(String.valueOf(timeOff));
		
		int checkTime = sharedPrefsEditor.getIntervalCheck();
		edInterval.setText(String.valueOf(checkTime));
		
		boolean dataIsActivated = sharedPrefsEditor.isDataActivated();
		cbData.setChecked(dataIsActivated);
		
		boolean dataMgrIsActivated = sharedPrefsEditor.isDataMgrActivated();
		cbDataMgr.setChecked(dataMgrIsActivated);
		
	}

	public void onClick(View v) {
		
		
		//if saved button is click
		if(v == buttonSave)
		{
			//load ui compenents
			loadUiComponents();
			
			//get settings values
			int timeOn = Integer.parseInt(edTimeOn.getText().toString());
			int timeOff = Integer.parseInt(edTimeOff.getText().toString());
			int intervalCheck = Integer.parseInt(edInterval.getText().toString());
			
			boolean dataIsActivated = cbData.isChecked();
			boolean dataMgrIsActivated = cbDataMgr.isChecked();
			
			//save all these preferences
			sharedPrefsEditor.setAllValues(timeOn, timeOff, intervalCheck, dataIsActivated, dataMgrIsActivated);
			
		}

		
	}

	
		
	

}
