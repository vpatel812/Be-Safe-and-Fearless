package com.bSecure;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainSplashScreen extends Activity {
	ImageView im;
	Animation an;
	static int c = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_splash_screen);
		im = (ImageView) findViewById(R.id.img1);
		an = AnimationUtils.loadAnimation(this, R.anim.pull_in_from_left);
		an.reset();
		im.startAnimation(an);

		/****** Create Thread that will sleep for 1 second *************/
		Thread background = new Thread() {
			public void run() {

				try {
					// Thread will sleep for 1 second
					sleep(1000);

					// After 1 seconds redirect to another intent

					Intent i = new Intent(getBaseContext(), UserRegistration.class);
					startActivity(i);

					// Remove activity
					finish();

				} catch (Exception e) {

				}
			}
		};

		// start thread
		background.start();
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();

	}
}
