package com.example.datamanager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class ApplicationsManager {
	
	private PackageManager pm = null;
	private Context context = null;
	
	public ApplicationsManager(Context aContext) {
		
		context = aContext;
		pm = aContext.getPackageManager();
	}
	

	private ArrayList<ApplicationDetail> getPackages(SharedPrefsEditor sharedPrefsEditor) {
	    ArrayList<ApplicationDetail> apps = getInstalledApps(false, sharedPrefsEditor); /* false = no system packages */
	    final int max = apps.size();
	    for (int i=0; i<max; i++) {
	        apps.get(i).prettyPrint();
	    }
	    return apps;
	}

	public ArrayList<ApplicationDetail> getInstalledApps(boolean getSysPackages, SharedPrefsEditor sharedPrefsEditor) {
	    ArrayList<ApplicationDetail> res = new ArrayList<ApplicationDetail>();
	    
	    StringToArrayHelper sArrayHelper = new StringToArrayHelper(sharedPrefsEditor.getApplicationsOnSet(), MainActivity.SEPARATOR);
	    
	    List<PackageInfo> packs = pm.getInstalledPackages(0);
	    for(int i=0;i<packs.size();i++) {
	        PackageInfo p = packs.get(i);
	        if ((!getSysPackages) && (p.versionName == null)) {
	            continue ;
	        }
	        ApplicationDetail newInfo = new ApplicationDetail();
	        
	        //if application is checked
	        if(sArrayHelper.containsEleement(p.packageName))
	        {
	        	//add - in front of name to bring it up 
	        	newInfo.setAppname("-"+p.applicationInfo.loadLabel(pm).toString());
	        }
	        else
	        { 
	        	newInfo.setAppname(p.applicationInfo.loadLabel(pm).toString());
	        }
	        
	        newInfo.setPname(p.packageName);
	        newInfo.setVersionName(p.versionName);
	        newInfo.setVersionCode(p.versionCode);
	        newInfo.setIcon(p.applicationInfo.loadIcon(pm));
	        res.add(newInfo);
	        

	    }
	    return res; 
	}
	
	/**
	 * Check if one of the app selected in the manage connectivity per app activity is running
	 * @param sharedPrefsEditor
	 * @return
	 */
	public boolean isSelectedAppIsRunning(SharedPrefsEditor sharedPrefsEditor)
	{
		boolean result = false;
		
		//get list
		StringToArrayHelper sArrayHelper = new StringToArrayHelper(sharedPrefsEditor.getApplicationsOnSet(), MainActivity.SEPARATOR);
		
		Log.i("CConnectivity", "array content :"+sharedPrefsEditor.getApplicationsOnSet());
		
		for(int i=0; i<sArrayHelper.count(); i++)
		{
			String appPackageName = sArrayHelper.getElement(i);
			
			Log.i("CConnectivity", "Checking if "+appPackageName+" is running "+i);
			
			//check if app is running
			if(!appPackageName.trim().equals("") && isNamedProcessRunning(appPackageName))
			{
				Log.i("CConnectivity", appPackageName+" is running "+i);
				result = true;
			}
			

		}
		
		return result;
	}
	
	
	public ApplicationDetail getAppInfoFromPackageName(String packageName)
	{
	
		ApplicationDetail appDetail = new ApplicationDetail();
		
		try {
			ApplicationInfo appInfo = pm.getApplicationInfo(packageName, 0);
			
			appDetail.setAppname(appInfo.loadLabel(pm).toString());
			appDetail.setPname(appInfo.packageName);
			appDetail.setIcon(appInfo.loadIcon(pm));
			
		} catch (NameNotFoundException e) {
			
			//if not found
			Log.i("CConnectivity", "app "+packageName+" not found");
			
			appDetail = null;
		}
		
		return appDetail;
	}
	
	//is application is running 
	   boolean isNamedProcessRunning(String processName){
		   if (processName == null) return false;

		   ActivityManager manager = (ActivityManager)  context.getSystemService(Activity.ACTIVITY_SERVICE);
		   List<RunningAppProcessInfo> processes = manager.getRunningAppProcesses();

		   for (RunningAppProcessInfo process : processes)
		   {
			
			   //process is not in cache
		       if (processName.equals(process.processName) && process.importance == process.IMPORTANCE_PERCEPTIBLE)
		       {
		           return true;
		       }
		   }
		   return false;}
}
