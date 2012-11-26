package com.example.threadtest;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class AHandler extends Handler{

	//waiting dialog
	private ProgressDialog progressDialog = null;
	
	//progress bar
	private ProgressDialog progressBar = null;
	
	//local counter for progress bar
	private int progressBarCounter = 0;
	
	public AHandler(Context context) {
		
		//set progressDialog msg
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        
        //set progress bqr information
        progressBar = new ProgressDialog(context);
        progressBar.setTitle("Please wait...");
        progressBar.setMessage("Loading");
        progressBar.setMax(10);
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
       
	}
	

	public void handleMessage(Message msg)
	{
		if(msg.what == 0)
		{
			//show progress dialog
			progressDialog.show();
		}
		else if(msg.what == 1)
		{
			//hide progress dialog
			progressDialog.dismiss();
		}
		else if(msg.what == 2)
		{
			//progress bar advance
			if(progressBarCounter ==0)
			{
				progressBar.show();
			}
			
			progressBarCounter++;
			progressBar.setProgress(progressBarCounter);
			
		}
		else if(msg.what == 3)
		{
			//hide progress bar and reset counter
			progressBar.dismiss();
			progressBarCounter=0;
		}
	}
}
