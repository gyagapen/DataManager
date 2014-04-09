package com.gyagapen.mrunews.common;

import java.io.IOException;

import android.content.SharedPreferences;

public class SharedPrefsEditor {

	private SharedPreferences newsPrefs = null;
	private SharedPreferences.Editor prefEditor = null;

	//pref name
	static final String STR_IMG_LINKS = "ImgLinks";
	
	
	
	
	public SharedPrefsEditor(SharedPreferences someSharedPreferences) {

		// shared preferences
		newsPrefs = someSharedPreferences;

	}

	public void initializePreferences(){
		
		
	}
	
	

}
