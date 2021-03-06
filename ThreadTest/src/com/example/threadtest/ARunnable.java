package com.example.threadtest;


public class ARunnable implements Runnable {

	
	AHandler aHandler = null;
	
	public ARunnable(AHandler handler) {
		super();
		aHandler = handler;
		
	}
	
	//will be launched by a thread
	public void run() {
	
		traitementDesDonnes();
		
	}
	
	
	//dummy method for simulate processing
	private void traitementDesDonnes()
	{
		//send msg to handler to start showing progress dialog
		aHandler.sendEmptyMessage(0);
		
		for(int i=0;i<10;i++)
		{
			//wait 500ms
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		//send msg to hide progress dialog
		aHandler.sendEmptyMessage(1);
	}

}
