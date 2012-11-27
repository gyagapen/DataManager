package com.example.datamanager;

import java.util.TimerTask;

import android.content.Context;
import android.widget.Toast;

public class TimerOnTask extends TimerTask {
	
	

	private DataHandler dataHandler = null;
	private int dataInterval = 5000; //ms
	
	public TimerOnTask(DataHandler dataHandler, int dataIntervalCheck) {
		
		super();
		this.dataHandler = dataHandler;
	}

	/**
	 * Behavior when time out
	 */
	public void run() {

		
		// launch thread that decides if we have to disable data
		Thread dataThread = new Thread(new DataRunnable(dataHandler, dataInterval));
		dataThread.start();

	}

}
