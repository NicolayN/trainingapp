package com.vsu.training;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;

public class TrainingSplashActivity extends TrainingActivity {
	int animDestroy = 0;

	void callMainActivity() {
		startActivity(new Intent(TrainingSplashActivity.this,
				TrainingMenuActivity.class));
		TrainingSplashActivity.this.finish();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();	
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		Animation an = AnimationUtils.loadAnimation(this, R.anim.fade_in_anim);
		AnimationListener al = new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
				animDestroy++;
				if (animDestroy == 3) {
					callMainActivity();
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}
		};
		an.setAnimationListener(al);
		LayoutAnimationController controller = new LayoutAnimationController(an);
		LinearLayout ll = (LinearLayout) findViewById(R.id.splashLinearLayout);
		ll.setLayoutAnimation(controller);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		LinearLayout ll = (LinearLayout) findViewById(R.id.splashLinearLayout);
		ll.clearAnimation();
	}

}