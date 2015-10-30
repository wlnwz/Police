package com.embeded.policemen;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class PageActivity extends Activity {
	private AnimationDrawable animationDrawable;
	private ImageView pageImageview;
	private final int SPLASH_DISPLAY_LENGHT = 2000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page);
		pageImageview = (ImageView) findViewById(R.id.jc);
		pageImageview.setImageResource(R.anim.page_animation);
		animationDrawable = (AnimationDrawable) pageImageview.getDrawable();
		animationDrawable.start();

		new Handler().postDelayed(new Runnable() {
			public void run() {

				Intent pageIntent = new Intent(PageActivity.this,
						LoginActivity.class);
				PageActivity.this.startActivity(pageIntent);
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
