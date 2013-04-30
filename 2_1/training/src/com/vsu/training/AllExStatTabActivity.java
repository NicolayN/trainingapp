package com.vsu.training;

import android.os.Bundle;
import android.widget.TableLayout;

public class AllExStatTabActivity extends TrainingActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stat_tab);
		DBAdapter dba = new DBAdapter(this);
		dba.open();
		dba.getAllExStat((TableLayout) findViewById(R.id.TableLayoutEx));
		dba.close();
	}
}
