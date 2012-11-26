package com.example.threadtest;


public class ARunnableBar implements Runnable {

	
	AHandler aHandler = null;
	
	public ARunnableBar(AHandler handler) {
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
		
		
		for(int i=0;i<10;i++)
		{
			//send msg to handler to start showing progress dialog
			aHandler.sendEmptyMessage(2);
			
			//wait 500ms
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		//send msg to hide progress dialog
		aHandler.sendEmptyMessage(3);
	}

}
