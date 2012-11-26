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

public class MainActivity extends Activity{
	
	//ui components
	CheckBox cbData = null;
	CheckBox cbDataMgr = null;
	EditText edTimeOn = null;
	EditText edTimeOff = null;
	EditText edInterval = null;
	Button buttonSave = null;

	//time on & off for connectivity (in minutes)
	static final String STR_TIME_ON="TimeOn";
	static final String STR_TIME_OFF="TimeOff";
	private final String STR_IS_ESTABLISHED="dataIsEstablished";
	static final int TIME_ON = 3;
	static final int TIME_OFF = 15;
	static final String PREFERENCE_NAME = "DataManagerPreferences";
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        //lancement du service
        Intent serviceIntent = new Intent(this, MainService.class);
        startService(serviceIntent);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    
	public void initializePreferences() throws IOException
	{
		SharedPreferences dataManagerSettings = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);  
		
		if(!dataManagerSettings.contains("dataIsEstablished"))
		{
			SharedPreferences.Editor prefEditor = dataManagerSettings.edit();
		
			prefEditor.putInt(STR_TIME_ON, TIME_ON );
			prefEditor.putInt(STR_TIME_OFF, TIME_OFF );
			prefEditor.putBoolean(STR_IS_ESTABLISHED, true);  
			prefEditor.commit();
		}
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
		
		buttonSave = (Button)findViewById(R.id.buttonSave);
		
		//set listener for all compenents
	}
	
	

}
