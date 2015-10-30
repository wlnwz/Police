package com.embeded.policemen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.Toast;

import com.embeded.slidingmenu.view.LeftFragment;
import com.embeded.slidingmenu.view.SlidingMenu;
import com.embeded.slidingmenu.view.ViewPageFragment;

public class MaindemoActivity extends FragmentActivity{
	SlidingMenu mSlidingMenu;
	LeftFragment leftFragment;
	ViewPageFragment viewPageFragment;
	private String username,phone,id,state;
	public static Activity MaindemoActivity;
	int REQUEST_CODE = 100;
	boolean internetavaliable;
	private boolean czjc = true;

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
	}

	@Override
	protected void onCreate(Bundle v) {
		super.onCreate(v);
		setContentView(R.layout.activity_maindemo);
		MaindemoActivity = this;
        Intent intent  = getIntent();
        username =intent.getExtras().getString("NAME");
        id =intent.getExtras().getString("id");
        phone =intent.getExtras().getString("phone");
        state =intent.getExtras().getString("state");
         
		Intent intent3 = new Intent();
 		intent3.putExtra("username", username);
 		intent3.putExtra("id", id);
 		intent3.putExtra("phone", phone);
 		intent3.putExtra("state", state);
 		intent3.setAction("com.example.name123");
 		System.out.println(username+phone);
 		MaindemoActivity.this.sendBroadcast(intent3);
         
		init();

		internetavaliable = isNetworkAvailable(this);
		if (internetavaliable == true) {
		} else {
			Toast.makeText(MaindemoActivity.this, "您的网络连接不正常，请重新连接！",
					Toast.LENGTH_SHORT).show();
		}				
	}

	
	private void init() {
		mSlidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);
		mSlidingMenu.setLeftView(getLayoutInflater().inflate(
				R.layout.left_frame, null));
		mSlidingMenu.setCenterView(getLayoutInflater().inflate(
				R.layout.center_frame, null));

		FragmentTransaction fTransaction = this.getSupportFragmentManager()
				.beginTransaction();
		leftFragment = new LeftFragment();
		fTransaction.replace(R.id.left_frame, leftFragment);
       
		Bundle bundle=new Bundle();
        bundle.putString("username", username);
        bundle.putString("phone", phone);
        bundle.putString("id", id);
        bundle.putString("state", state);
		leftFragment.setArguments(bundle);
		
		viewPageFragment = new ViewPageFragment();
		fTransaction.replace(R.id.center_frame, viewPageFragment);
		viewPageFragment.setArguments(bundle);
		
		System.out.println("username!!!!!" + username);
		fTransaction.commit();
	}



	public void showLeft() {
		mSlidingMenu.showLeftView();
	}

	public void showRight() {
		mSlidingMenu.showRightView();
	}



	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
			return false;
		} else {
			NetworkInfo networkinfo = cm.getActiveNetworkInfo();
			if (networkinfo == null || !networkinfo.isAvailable()) {
				return false;
			}
			return true;
		}
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub		
		super.onResume();
		
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
	}

	@Override
	public void onStop() {
		super.onStop();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		
	}
	
}
