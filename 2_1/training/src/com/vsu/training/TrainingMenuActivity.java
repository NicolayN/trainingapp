package com.vsu.training;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TrainingMenuActivity extends TrainingActivity {
	SharedPreferences TrSettings;
	int PrevInterval;
	Repeating rep;
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Editor ed = TrSettings.edit();
		if (TrSettings.getInt(TR_PREFERENCES_INT, 10) != PrevInterval) {
		    rep.stopAlarm();
			ed.putBoolean(TR_PREFERENCES_AL, true);
			rep.startAlarm();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		TrSettings = getSharedPreferences(TR_PREFERENCES, Context.MODE_PRIVATE);
		PrevInterval = TrSettings.getInt(TR_PREFERENCES_INT, 500);
		rep = new Repeating(this, TrSettings);
		// start alarm if it is not started previously
		Editor ed = TrSettings.edit();
		if (!TrSettings.contains(TR_PREFERENCES_MODE)) {
			ed.putBoolean(TR_PREFERENCES_MODE, true);
			ed.commit();// standart alarm
		}
		if (!TrSettings.contains(TR_PREFERENCES_INT)) {
	       	ed.putInt(TR_PREFERENCES_INT, 1);
	       	ed.commit();
	    }
		ed.putBoolean(TR_PREFERENCES_AL, true);
		if (!TrSettings.getBoolean(TR_PREFERENCES_AL, false)) {
			ed.putBoolean(TR_PREFERENCES_AL, true);
			rep.startAlarm(); // start alarm
		}			
	}
	public void onDoNowClick(View arg0) {
		// TODO Auto-generated method stub
		startActivity(new Intent(TrainingMenuActivity.this, 
				TrainingDoActivity.class));
	}
	public void onSettClick(View arg0) {
		// TODO Auto-generated method stub
		startActivity(new Intent(TrainingMenuActivity.this, 
				TrainingSettingsActivity.class));
	}
	public void onStatClick(View arg0) {
		// TODO Auto-generated method stub
		startActivity(new Intent(TrainingMenuActivity.this, 
				TrainingStatActivity.class));
	}
	public void onHelpClick(View arg0) {
		// TODO Auto-generated method stub
		startActivity(new Intent(TrainingMenuActivity.this, 
				TrainingHelpActivity.class));
	}
	public void onStopClick(View arg0) {
		// TODO Auto-generated method stub
		// stop service, write to log (?)
		// shut down the activity
		TrainingMenuActivity.this.finish();
	}
}
