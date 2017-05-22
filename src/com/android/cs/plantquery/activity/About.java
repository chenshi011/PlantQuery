package com.android.cs.plantquery.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.android.cs.plantquery.R;

public class About extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.about);










	}
}
