package com.android.cs.plantquery;

import android.app.Activity;
import android.os.Bundle;

import com.android.cs.plantquery.common.PlantManager;
import com.android.cs.plantquery.db.DBHelper;

public class BusinessActivity extends Activity {
	protected DBHelper dbHelper;
	protected PlantManager plantManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		dbHelper = DBHelper.getInstance(this);
		plantManager = new PlantManager(this, dbHelper);
		super.onCreate(savedInstanceState);
	}

}
