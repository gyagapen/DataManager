package com.example.datamanager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

public class LogsProvider {

	static final String LOG_APP_NAME = "CConnectivity";

	public static final String LOG_DIR = "";
	public static final String LOG_MODE_EXCEPTION = "exception";
	public static final String LOG_MODE_INFO = "info";

	public static final int fileMaxSize = 80000; //bytes

	private File sdCard;
	private File dir;
	private FileWriter fWriter;

	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;

	private DataActivation dataActivation = null;

	private String logFileAbsPath = "";
	File logFile = null;



	public LogsProvider(Context context) {

		// shared prefs init
		prefs = context.getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				Activity.MODE_PRIVATE);
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);

		sdCard = Environment.getExternalStorageDirectory();

		dir = new File(sdCard.getAbsolutePath(), LOG_DIR);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		logFileAbsPath = dir + File.separator + "CleverConnectivity.log";

		logFile = new File(logFileAbsPath);

		fWriter = null;
		try {
			if (fileMaxSize < logFile.length()) {
				// overwrites
				fWriter = new FileWriter(logFile, false);
			} else {
				fWriter = new FileWriter(logFile, true);
			}
		} catch (Exception e) {

		}
	}



	public void info(String text) {

		if(sharedPrefsEditor.isLogsEnabled())
		{
			Log.i(LOG_APP_NAME, text);
			MyLog(LOG_MODE_INFO, LOG_APP_NAME, text);
		}
	}

	public void MyLog(String logMode, String msgKey, Object msgValue) {




		BufferedWriter bufferedWritter = null;
		try {
			if(fWriter == null)
			{
				fWriter = new FileWriter(logFile, true);
			}
			else
			{
				bufferedWritter = new BufferedWriter(fWriter);
				String logValue = null;
				if (logMode.equalsIgnoreCase(LOG_MODE_EXCEPTION)) {
					logValue = logStackTrace((Throwable) msgValue);
				} else {
					logValue = msgValue.toString();
				}
				logValue = currentDateTimeValue() + ": " + logMode + " :" + msgKey
						+ ":  " + logValue + "\n";
				bufferedWritter.write(logValue);
				bufferedWritter.newLine();
				bufferedWritter.flush();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	public static String logStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		t.printStackTrace(pw);
		pw.flush();
		sw.flush();
		return sw.toString();
	}

	public static String currentDateTimeValue() {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd-HH:mm:ss");
		formatter.setLenient(false);

		Date curDate = new Date();
		String curTime = formatter.format(curDate);

		return curTime;
	}

}
