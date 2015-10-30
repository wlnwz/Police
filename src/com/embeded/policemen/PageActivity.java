package com.embeded.policemen;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;

public class PageActivity extends Activity {
	private AnimationDrawable ad;
	private ImageView mimageview;
	private final int SPLASH_DISPLAY_LENGHT = 2000;
	Button mbutton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page);
		mimageview = (ImageView) findViewById(R.id.jc);
		mimageview.setImageResource(R.anim.donghua);
		ad = (AnimationDrawable) mimageview.getDrawable();
		ad.start();

		new Handler().postDelayed(new Runnable() {
			public void run() {

				Intent mainIntent = new Intent(PageActivity.this,
						Login.class);
				PageActivity.this.startActivity(mainIntent);
				PageActivity.this.finish();
			}

		}, SPLASH_DISPLAY_LENGHT);

	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		// ÍË³ö
		if ((Intent.FLAG_ACTIVITY_CLEAR_TOP & intent.getFlags()) != 0) {
			finish();
		}
	}

}
