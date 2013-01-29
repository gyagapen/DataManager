package com.example.datamanager;

import android.graphics.drawable.Drawable;
import android.util.Log;

public class ApplicationDetail implements Comparable<ApplicationDetail> {

	
	private String appname = "";
    private String pname = "";
    private String versionName = "";
    private int versionCode = 0;
    private Drawable icon;
    
    
    public void prettyPrint() {
        Log.i("CConnectivity", appname + "\t" + pname + "\t" + versionName + "\t" + versionCode);
    }


	public String getAppname() {
		return appname;
	}


	public void setAppname(String appname) {
		this.appname = appname;
	}


	public String getPname() {
		return pname;
	}


	public void setPname(String pname) {
		this.pname = pname;
	}


	public String getVersionName() {
		return versionName;
	}


	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}


	public int getVersionCode() {
		return versionCode;
	}


	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}


	public Drawable getIcon() {
		return icon;
	}


	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	
	public int compareTo(ApplicationDetail another) {
		
		ApplicationDetail application1 = this;
		ApplicationDetail application2 = another; 
		
		int returnValue = 0;
		
		if(application1.getAppname().compareTo(application2.getAppname())<0)
		{
			//app1 < app2
			returnValue = -1;
		}
		else
		{
			returnValue = 1;
		}
		
		//Log.i("CConnectivity", application1.getAppname()+" vs "+application2.getAppname()+" : "+returnValue);
		
		return returnValue;
	}
}
