package com.example.datamanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.example.cleverconnectivity.R;

public class ScreenReceiver extends BroadcastReceiver {

	private boolean screenOff;
	

	@Override
	public void onReceive(Context context, Intent intent) {


		
		if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {

			//initialize connectivity positions
			SaveConnectionPreferences connPrefs = new SaveConnectionPreferences(context);
			connPrefs.saveAllConnectionSettingInSharedPrefs();
			
			screenOff = true;

		} else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

			screenOff = false;

		}

		Intent i = new Intent(context, MainService.class);

		i.putExtra("screen_state", screenOff);

		context.startService(i);

	}

}
