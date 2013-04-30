package com.vsu.training;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class TrainingStatActivity extends TabActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stat);
		TabHost th = getTabHost();
		// add All Exercises tab
		TabSpec ts = th.newTabSpec("all");
		ts.setIndicator("Все категории");
		ts.setContent(new Intent(this, AllExStatTabActivity.class));
		th.addTab(ts);
		// add Eye Exercises tab
		ts = th.newTabSpec("eye");
		ts.setIndicator("Eye");
		ts.setContent(new Intent(this, EyeExStatTabActivity.class));
		th.addTab(ts);
		ts = th.newTabSpec("hands");
		ts.setIndicator("Hands");
		ts.setContent(new Intent(this, EyeExStatTabActivity.class));
		th.addTab(ts);
		th.setCurrentTabByTag("all");
	}
}
