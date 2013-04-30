package com.vsu.training;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.Toast;

public class SettSchemaActivity extends TrainingActivity {
	DBAdapter dba = new DBAdapter(this);
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sett_tab);		
		dba.open();
		dba.getExForSettAct((TableLayout) findViewById(R.id.SettTableLayout));
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dba.close();
	}
}
