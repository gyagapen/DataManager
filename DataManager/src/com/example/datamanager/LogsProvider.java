package com.example.datamanager;


import java.io.File;

import org.apache.log4j.Logger;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import de.mindpipe.android.logging.log4j.LogConfigurator;

public class LogsProvider {

	static final String LOG_APP_NAME = "CleverConnectivity.log";

	public static final int fileMaxSize = 80000; //bytes


	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;

	private DataActivation dataActivation = null;

	 private  Logger log = null;


	public LogsProvider(Context context, Class callerClass) {

		// shared prefs init
		prefs = context.getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				Activity.MODE_PRIVATE);
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);
		
		configure();
		log = Logger.getLogger(callerClass);

		
	}



	public void info(String text) {

		if(sharedPrefsEditor.isLogsEnabled())
		{
			log.info(text);
		}
	}
	
	
	public void error(Exception ex) {

		if(sharedPrefsEditor.isLogsEnabled())
		{
			log.error(ex);
		}
	}
		
	
    public static void configure() {
        final LogConfigurator logConfigurator = new LogConfigurator();
                
        logConfigurator.setFileName(Environment.getExternalStorageDirectory() + File.separator + LOG_APP_NAME);
        logConfigurator.setRootLevel(org.apache.log4j.Level.DEBUG);
        logConfigurator.setMaxFileSize(fileMaxSize);
        logConfigurator.setMaxBackupSize(0);
        // Set log level of a specific logger
        logConfigurator.setLevel("org.apache", org.apache.log4j.Level.DEBUG);
        try
        {
        	logConfigurator.configure();
        }
        catch (Exception e)
        {
        	//can't log
        }
    }

}
