package com.vsu.training;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

public class Repeating {
	// alarm 
	static Context mContext;
	static AlarmManager alarms;
	static SharedPreferences TrSettings;
	static Intent doInt;
	static PendingIntent penIntIntent;
	ScreenReceiver sr;
	public static class ScreenReceiver extends BroadcastReceiver {
		@Override
	    public void onReceive(Context context, Intent intent) {
			if ((intent.getAction().equals(Intent.ACTION_SCREEN_OFF)||intent.getAction().equals(Intent.ACTION_SHUTDOWN))&&(!TrSettings.getBoolean("mode", true))) { stopAlarm(); }
			if ((intent.getAction().equals(Intent.ACTION_SCREEN_ON))&&(!TrSettings.getBoolean("mode", true))) { startAlarm(); }
		}
	}
	public Repeating(Context mcnt, SharedPreferences sp) {
		mContext = mcnt;
	    TrSettings = sp;
	    alarms = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
	    doInt = new Intent(mContext, TrainingDoActivity.class);
	    penIntIntent = PendingIntent.getBroadcast(mContext, 0, doInt, 0);
	    IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
	    filter.addAction(Intent.ACTION_SCREEN_OFF);
	    filter.addAction(Intent.ACTION_SHUTDOWN);
	    sr = new ScreenReceiver();
	    mContext.registerReceiver(sr, filter);
	}		
	public static void startAlarm() {
		int alarmType = AlarmManager.RTC_WAKEUP;
		long timeToWait = TrSettings.getInt("interval", 1) * 60000;
		alarms.set(alarmType, timeToWait, penIntIntent);
	}
	public static void stopAlarm() {
		alarms.cancel(penIntIntent);	
	}	
}
