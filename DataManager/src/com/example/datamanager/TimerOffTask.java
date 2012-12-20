package com.example.datamanager;

import java.util.TimerTask;

import android.content.Context;
import android.widget.Toast;
import com.example.cleverconnectivity.R;

public class TimerOffTask extends TimerTask {
	
	
	private DataHandler dataHandler = null;
	
	public TimerOffTask(DataHandler aDataHandler) {
		
		super();
		dataHandler = aDataHandler;
	}

	/**
	 * Behavior when time out
	 */
	public void run() {
		
		//tell the datahandler to enable timer and to start timerOn
		dataHandler.sendEmptyMessage(2);
	}

}
