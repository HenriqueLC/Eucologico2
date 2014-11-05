package com.b310.grupo10.eucologico2;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

public class Filtros extends Activity {
	Button bt;
	Spinner spn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filtros);
		
		bt = (Button)findViewById(R.id.button1);
		spn = (Spinner)findViewById(R.id.spinner1);
		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Filtros.this);
		final SharedPreferences.Editor spe = sp.edit();
		
		spn.setSelection(sp.getInt("FILTRO", 0));
		
		bt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				spe.putInt("FILTRO", spn.getSelectedItemPosition());
				spe.commit();
				finish();
				MapsActivity.instance.Update();
			}
		});
	}

}
