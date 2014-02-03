package com.datamanager.core;

import java.util.ArrayList;
import java.util.List;

import com.datamanager.tabActivities.AppLauncher;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class ApplicationsManager {
	
	private PackageManager pm = null;
	private Context context = null;
	private LogsProvider logsProvider = null;
	
	public ApplicationsManager(Context aContext) {
		
		context = aContext;
		pm = aContext.getPackageManager();
		logsProvider = new LogsProvider(aContext, this.getClass());
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
	    
	    StringToArrayHelper sArrayHelper = new StringToArrayHelper(sharedPrefsEditor.getApplicationsOnSet(), AppLauncher.SEPARATOR);
	    
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
		StringToArrayHelper sArrayHelper = new StringToArrayHelper(sharedPrefsEditor.getApplicationsOnSet(), AppLauncher.SEPARATOR);
		
		logsProvider.info("array content :"+sharedPrefsEditor.getApplicationsOnSet());
		
		int appCounter = 0;
		
		while( (appCounter <sArrayHelper.count()) && !(result) )
		{
			
			String appPackageName = sArrayHelper.getElement(appCounter);
			
			logsProvider.info("Checking if "+appPackageName+" is running "+appCounter);
			
			//check if app is running
			if(!appPackageName.trim().equals("") && isNamedProcessRunning(appPackageName))
			{
				logsProvider.info(appPackageName+" is running "+appCounter);
				result = true;
			}
			
			appCounter++;
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
			logsProvider.info("app "+packageName+" not found");
			
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
		       if (processName.equals(process.processName) && process.importance != RunningAppProcessInfo.IMPORTANCE_BACKGROUND)
		       {
		           return true;
		       }
		   }
		   return false;}
}
