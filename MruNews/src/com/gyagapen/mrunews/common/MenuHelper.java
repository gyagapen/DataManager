package com.gyagapen.mrunews.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;


import com.gyagapen.mrunews.R;

/**
 * Help to build menu on activities
 * 
 * @author Gui
 * 
 */
public class MenuHelper {

	private Context context;
	private LogsProvider logsProvider = null;

	public MenuHelper(Context context) {
		this.context = context;
		logsProvider = new LogsProvider(context, this.getClass());
		
	}

	public Menu populatedMenu(Menu menu) {
		menu.add(0, R.string.menuContactDev, 1, R.string.menuContactDev);

		return menu;
	}

	public void executeMenuAction(MenuItem menuItem) {

		if (menuItem.getItemId() == R.string.menuContactDev) {
			sendMailToDev();
		}

	}

	private void sendMailToDev() {
		// send mail to dev
		Intent email = new Intent(Intent.ACTION_SEND);
		email.putExtra(Intent.EXTRA_EMAIL,
				new String[] { StaticValues.MAIL_RECIPIENT });

		String versionName = getVersionName();

		// attachment
		/*
		 * File file = new File(Environment.getExternalStorageDirectory() +
		 * File.separator + "CleverConnectivity.log"); if (file.exists() &&
		 * file.canRead()) { logsProvider.info("logs file is available: " +
		 * file.getName()); Uri uri = Uri.parse("file://" +
		 * file.getAbsolutePath()); email.putExtra(Intent.EXTRA_STREAM, uri); }
		 * else if (!file.exists()) {
		 * logsProvider.info("logs file does not exist: " + file.getName()); }
		 * else if (!file.canRead()) {
		 * logsProvider.info("logs file cannot be read: " + file.getName()); }
		 */

		email.putExtra(Intent.EXTRA_SUBJECT, StaticValues.MAIL_SUBJECT + " - "
				+ versionName);
		// email.putExtra(Intent.EXTRA_TEXT, "message");
		email.setType("message/rfc822");
		context.startActivity(Intent.createChooser(email,
				"Choose an Email client :"));
	}

	// get version number of the application
	public String getVersionName() {
		String versionName = "";
		try {
			versionName = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			logsProvider.error(e);
		}

		return versionName;

	}
	
	//generate ads banner
	/*public void generateAdsBanner(AdView adView, LinearLayout layout, Activity activity)
	{
		// Create the adView
	    adView = new AdView(activity, AdSize.BANNER, StaticValues.MY_AD_UNIT_ID);

	    
	    // Add the adView to it
	    layout.addView(adView);

	    // Initiate a generic request to load it with an ad
	    adView.loadAd(new AdRequest());
	}*/

}
