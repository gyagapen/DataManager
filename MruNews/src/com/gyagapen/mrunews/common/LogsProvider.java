package com.gyagapen.mrunews.common;


import java.io.File;

import org.apache.log4j.Logger;

import android.content.Context;
import android.os.Environment;
import de.mindpipe.android.logging.log4j.LogConfigurator;

public class LogsProvider {

	static final String LOG_APP_NAME = "MauritiusNews.log";

	public static final int fileMaxSize = 80000; //bytes

	 private boolean logIsEnabled = true;
	 private  Logger log = null;


	public LogsProvider(Context context, Class callerClass) {

		
		configure();
		log = Logger.getLogger(callerClass);

		
	}



	public void info(String text) {

		if(logIsEnabled)
		{
			log.info(text);
		}
	}
	
	
	public void error(Exception ex) {

		if(logIsEnabled)
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
