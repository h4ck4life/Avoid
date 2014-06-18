package com.conner.avoid.android;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.conner.avoid.Application;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(android.os.Build.VERSION.SDK_INT >= 11)
			dimMenuBar();
		
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useWakelock = true;
		config.useAccelerometer = false;
		config.useCompass = false;
		initialize(new Application(), config);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if(android.os.Build.VERSION.SDK_INT >= 11)
			dimMenuBar();
	}
	
	@SuppressLint("NewApi")
	protected void dimMenuBar() {
		View decorView = getApplicationWindow().getDecorView();
		int uiOptions = View.SYSTEM_UI_FLAG_LOW_PROFILE;
		decorView.setSystemUiVisibility(uiOptions);
	}
}
