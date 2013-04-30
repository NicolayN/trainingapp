package com.vsu.training;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TrainingSettingsActivity extends TrainingActivity {
	SharedPreferences TrSettings;
	String[] modes = { "By interval", "By screen activity" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sett);
		TrSettings = getSharedPreferences(TR_PREFERENCES, Context.MODE_PRIVATE);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, modes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner spin = (Spinner) findViewById(R.id.settModeSpinner);
        spin.setAdapter(adapter);
        Editor ed = TrSettings.edit();
        if (!TrSettings.contains(TR_PREFERENCES_MODE)) {
        	ed.putBoolean(TR_PREFERENCES_MODE, true);
        	ed.commit();
        }
        if (TrSettings.getBoolean(TR_PREFERENCES_MODE, true)) { spin.setSelection(0); }
        else spin.setSelection(1);
        spin.setOnItemSelectedListener(new OnItemSelectedListener() { // ch
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {           	
            }
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				Editor ed = TrSettings.edit();
				if (position == 0) {
					ed.putBoolean(TR_PREFERENCES_MODE, true);
				}
				else { 
					ed.putBoolean(TR_PREFERENCES_MODE, false); 
				}
				ed.commit();
			}
          });
        // layouts with schema and profile
        Button intSetButt = (Button) findViewById(R.id.settSetIntButton);
        EditText in = (EditText) findViewById(R.id.settIntEditText);
        in.setText(String.valueOf(TrSettings.getInt(TR_PREFERENCES_INT, 10)));
        OnClickListener onInt = new OnClickListener() {
            @Override
            public void onClick(View v) {
            	EditText in = (EditText) findViewById(R.id.settIntEditText);
            	try {
            		int i = Integer.parseInt(in.getText().toString());
            		// checking that > 0?
            		if (i > 0) {
            			Editor ed = TrSettings.edit();
            			ed.putInt(TR_PREFERENCES_INT, i);		
            			ed.commit();
            			Toast.makeText(TrainingSettingsActivity.this, "Интервал установлен", Toast.LENGTH_LONG).show();  
            		}
            		else {
            			Toast.makeText(TrainingSettingsActivity.this, "Некорректное значение интервала", Toast.LENGTH_LONG).show();
            		}
            	}
            	catch (Exception e) {
            		Toast.makeText(TrainingSettingsActivity.this, "Некорректное значение интервала", Toast.LENGTH_LONG).show();
            	}
            }
        };
        intSetButt.setOnClickListener(onInt);
        Button schSetButt = (Button) findViewById(R.id.settChSchemaButton);
        OnClickListener onSch = new OnClickListener() {
            @Override
            public void onClick(View v) {
            	startActivity(new Intent(TrainingSettingsActivity.this, 
        				SettSchemaActivity.class));
            }
        };  
        schSetButt.setOnClickListener(onSch);
	}}
