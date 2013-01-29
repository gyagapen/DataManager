package com.example.datamanager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class AppMgrOnCheckListener implements OnCheckedChangeListener {

	private String appPackageName = "";
	private Context context;

	// SharedPreferences
	private SharedPreferences prefs = null;
	private SharedPrefsEditor sharedPrefsEditor = null;

	private DataActivation dataActivation;

	public AppMgrOnCheckListener(String packageName, Context aContext) {

		appPackageName = packageName;
		context = aContext;

		// shared prefs init
		prefs = context.getSharedPreferences(SharedPrefsEditor.PREFERENCE_NAME,
				Activity.MODE_PRIVATE);
		dataActivation = new DataActivation(context);
		sharedPrefsEditor = new SharedPrefsEditor(prefs, dataActivation);
	}

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		//get mgr apps
		String appMgr = sharedPrefsEditor.getApplicationsOnSet();
		StringToArrayHelper arrayHelper = new StringToArrayHelper(appMgr, MainActivity.SEPARATOR);
		
		if(isChecked)
		{
			arrayHelper.addElementToList(appPackageName);
		}
		else
		{
			arrayHelper.removeElementFromList(appPackageName);
		}
		
		//save list in shared prefs
		sharedPrefsEditor.setApplicationOnSet(arrayHelper.convertListToString());

	}

}
