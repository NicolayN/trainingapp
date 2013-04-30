package com.vsu.training;

import java.io.InputStream;

import android.os.Bundle;
import android.widget.TextView;

public class TrainingHelpActivity extends TrainingActivity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		// loading from text file
		InputStream is = getResources().openRawResource(R.raw.training_help);
		TextView helpText = (TextView) findViewById(R.id.helpTextView);
		String strFile = is.toString();
		helpText.setText(strFile);
	}

}
