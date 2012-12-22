package com.example.datamanager;

import android.net.TrafficStats;
import com.gyagapen.cleverconnectivity.R;


/**
 * Detects wether 3g is used 
 * @author Gui
 *
 */
public class DataRunnable implements Runnable {
	
	//interval when 3G usage is cheked (ms)
	private  int dataInterval = 5000;
	
	//limit, above that number of bytes, will consider that data is used
	private int bytesLimit = 5000;

	//data handler is the interface to communicate to activities
	private DataHandler dataHandler = null;;

	public DataRunnable(DataHandler dataHandler, int dataIntervalCheck) {
		this.dataHandler = dataHandler;
		dataInterval = dataIntervalCheck;
		
	}

	

	public void run() {


			boolean dataIsUsed = false;

			// get number of bytes received and sent
			long nbBytesSent1 = TrafficStats.getTotalTxBytes();
			long nbBytesReceived1 = TrafficStats.getTotalRxBytes();

			// get number of bytes received 5 second later
			try {
				Thread.sleep(dataInterval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			long nbBytesReceived2 = TrafficStats.getTotalRxBytes();
			long nbBytesSent2 = TrafficStats.getTotalTxBytes();

			int bytesUsed = (int) (nbBytesReceived2 - nbBytesReceived1);
			boolean dataIsReceived = (bytesUsed > bytesLimit);
			

			// if no data received then check if data is sent
			if (!dataIsReceived) {
				bytesUsed = (int) (nbBytesSent2 - nbBytesSent1);
				dataIsUsed = (bytesUsed > bytesLimit);;
			} else {
				// data connection is used
				dataIsUsed = true;
			}

			if (dataIsUsed) {
				dataHandler.sendEmptyMessage(0);
			} else {
				dataHandler.sendEmptyMessage(1);
			}


	}

}
