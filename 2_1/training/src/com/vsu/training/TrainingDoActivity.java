package com.vsu.training;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TrainingDoActivity extends TrainingActivity{
	DBAdapter dba;
	Repeating rep;
	SharedPreferences TrSettings;
	TextView name;
	TextView text;
	ImageView pic;
	Button next;
	Button skip;
	Button skip_all;
	DBAdapter.ExFromDb efdb;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.donow);
		dba = new DBAdapter(this);
		dba.open();
		TrSettings = getSharedPreferences(TR_PREFERENCES, Context.MODE_PRIVATE);
		//efdb = new DBAdapter.ExFromDb();
		name = (TextView) findViewById(R.id.nameTextView);
		text = (TextView) findViewById(R.id.doTextView);
		pic = (ImageView) findViewById(R.id.doImageView);
		next = (Button) findViewById(R.id.doNextButton);
		skip = (Button) findViewById(R.id.doSkipButton);
		skip_all = (Button) findViewById(R.id.skipAllButton);
		Editor ed = TrSettings.edit();
		rep.stopAlarm();
		ed.putBoolean(TR_PREFERENCES_AL, false);
		ed.commit();
		
		/*if (efdb.getNextEx()) {
			name.setText(dba.nowEx.name);
			text.setText(dba.nowEx.text);
			pic.setImageResource(dba.nowEx.image_id);		
			next.setVisibility(Button.VISIBLE);
			skip.setVisibility(Button.VISIBLE);		
		}
		else {
			name.setText("Активных упражнений не найдено");
			text.setText("");
			next.setVisibility(Button.INVISIBLE);
			skip.setVisibility(Button.INVISIBLE);	
			//pic.setImageResource(dba.nowEx.sorry_image);
		}*/
	}
	void loadNextEx() {
		if (efdb.getNextEx()) {
			name.setText(dba.nowEx.name);
			text.setText(dba.nowEx.text);
			pic.setImageResource(dba.nowEx.image_id);
		}
		else {
			name.setText("Тренировка завершена!");
			text.setText("");
			next.setVisibility(Button.INVISIBLE);
			skip.setVisibility(Button.INVISIBLE);
			//pic.setImageResource(dba.nowEx.finish_image);
		}
	}
	public void doNext_Click() {
		efdb.insert_into_stat(true);
		loadNextEx();
	}
	public void doSkip_Click() {
		efdb.insert_into_stat(false);
		loadNextEx();	
	}
	public void skipAll() {
		while (efdb.getNextEx()) { efdb.insert_into_stat(false); }
		onDestroy();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		dba.close();
		rep.startAlarm();
		Editor ed = TrSettings.edit();
		ed.putBoolean(TR_PREFERENCES_AL, true);
		ed.commit();
		TrainingDoActivity.this.finish();
	}
}
