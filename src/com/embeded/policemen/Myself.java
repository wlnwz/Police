package com.embeded.policemen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Myself extends Activity{
	TextView name, telp,back;
	Button cancel;
	String username, telphone, ID;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myself);

		Intent intent = getIntent();
		username = intent.getExtras().getString("username");
		telphone = intent.getExtras().getString("telphone");

		Init();
		Initclick();
	}

	void Init() {
		name = (TextView) findViewById(R.id.userinfor);
		telp = (TextView) findViewById(R.id.telinfor);
		cancel = (Button) findViewById(R.id.cancelbutton);
		back = (TextView) findViewById(R.id.back);
		name.setText(username);
		telp.setText("tel£º"+telphone);
	}

	void Initclick() {
		OnClickListener mOnclickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (v.equals(cancel)) {
					username = "NAME";
					telphone = "";
//					Intent intent = new Intent(Myself.this,
//							MaindemoActivity.class);
//					intent.putExtra("NAME", username);
//					intent.putExtra("phone", telphone);
//					setResult(100, intent);
//					Myself.this.finish();
					
					Intent intent = new Intent(Myself.this,
							LoginActivity.class);
					
					LoginActivity.sp.edit().putBoolean("AUTO_ISCHECK", false).commit();			
					// Login.sp.edit().clear().commit();  Çå¿Õsharepreference
					// Çå¿ÕÕ»
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					Myself.this.finish();
					
				}else if(v.equals(back)){
					Myself.this.finish();
				}
			}

		};
		cancel.setOnClickListener(mOnclickListener);
		back.setOnClickListener(mOnclickListener);
	}
}
