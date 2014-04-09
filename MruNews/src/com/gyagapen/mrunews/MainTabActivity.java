package com.gyagapen.mrunews;

import java.io.File;
import java.io.IOException;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.gyagapen.mrunews.common.LogsProvider;
import com.gyagapen.mrunews.common.MenuHelper;
import com.gyagapen.mrunews.common.SharedPrefsEditor;



public class MainTabActivity extends TabActivity{

	private LogsProvider logsProvider = null;
	
	private MenuHelper menuHelper = null;
	


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabbed_layout);
		
		logsProvider = new LogsProvider(getApplicationContext(), this.getClass());
		

		//init menu helper
		menuHelper = new MenuHelper(this);
		
		// create cache
		createCache();


		TabHost tabHost = getTabHost();

		// Tab for News
		TabSpec newsTab = tabHost.newTabSpec("News");
		// setting Title and Icon for the Tab
		newsTab.setIndicator("News");
		//genspec.setIndicator("", getResources().getDrawable(R.drawable.general));
		Intent genIntent = new Intent(this, MainNewsActivity.class);
		newsTab.setContent(genIntent);
		
		//Tab for semdex
		TabSpec semdexTab = tabHost.newTabSpec("Semdex");
		semdexTab.setIndicator("Semdex");
		Intent semdexIntent = new Intent(this, SemdexListActivity.class);
		semdexTab.setContent(semdexIntent);
		
		//Tab for semdex
		TabSpec demTab = tabHost.newTabSpec("Dem");
		demTab.setIndicator("Dem");
		Intent demIntent = new Intent(this, DemListActivity.class);
		demTab.setContent(demIntent);


		
		// Adding all TabSpec to TabHost
		tabHost.addTab(newsTab); 
		tabHost.addTab(semdexTab); 
		tabHost.addTab(demTab); 
		
	}
	
	//menu
	public boolean onCreateOptionsMenu(Menu menu) {
		
		menuHelper.populatedMenu(menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	//on menu on clicked
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		menuHelper.executeMenuAction(item);
		
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Create cache for http requests
	 */
	public void createCache() {

		try {
			File httpCacheDir = null;
			// try to install cache on external memory
			if (getExternalCacheDir() != null) {
				httpCacheDir = new File(getExternalCacheDir(), "http");
			} else // use internal cache
			{
				httpCacheDir = new File(getCacheDir(), "http");
			}
			long httpCacheSize = 10 * 1024 * 1024; // 10 MiB

			HttpResponseCache.install(httpCacheDir, httpCacheSize);
		} catch (IOException e) {
			Log.i("MruNews", "HTTP response cache installation failed:" + e);
		}
	}

	@Override
	protected void onStop() {

		HttpResponseCache cache = HttpResponseCache.getInstalled();
		if (cache != null) {
			try {
				if(cache != null)
				{
					cache.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			;
		}

		super.onStop();
	}
	
	
	
}
