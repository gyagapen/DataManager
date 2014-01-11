package com.example.datamanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;



public class VolumeButtonReceiver extends BroadcastReceiver {

	LogsProvider logsProvider = null;
	
	public void onReceive(Context context, Intent intent) {
		
		int volumePrev = (Integer)intent.getExtras().get("android.media.EXTRA_PREV_VOLUME_STREAM_VALUE");
		int volumeCurr = (Integer)intent.getExtras().get("android.media.EXTRA_VOLUME_STREAM_VALUE");
		
		AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		//audio.setStreamVolume(AudioManager.STREAM_MUSIC, volumePrev, AudioManager.FLAG_SHOW_UI);
		
		logsProvider = new LogsProvider(context, VolumeButtonReceiver.class);
		
		logsProvider.info("volume button pressed");
		
		
		
		logsProvider.info("prev volume"+ volumePrev);
		logsProvider.info("curr volume"+ volumeCurr);
		
	}

}
