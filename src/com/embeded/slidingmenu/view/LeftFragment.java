/*
 * Copyright (C) 2014 yeran
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.embeded.slidingmenu.view;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.embeded.policemen.Myprepare;
import com.embeded.policemen.Myrecord;
import com.embeded.policemen.Myself;
import com.embeded.policemen.Note;
import com.embeded.policemen.R;
import com.embeded.policemen.R.color;

public class LeftFragment extends Fragment {

	private TextView myrecord,myprepare,busyornot;
	private TextView myself,note;
	
	private TextView set;
	private TextView name;
	private Button Active_Job;
	int REQUEST_CODE = 100;
	String name1 = "NAME", phone, ID, state;
	private ImageView red;
	private String[] policename, ptelphone, myweizhi, sure, grade, time;
	private int length = 100;
	private boolean a = true;
	private String BROADCAST_ACTION="com.example.start";
	private String responseMsg = "",state123="0";

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		myweizhi = new String[length];
		ptelphone = new String[length];
		policename = new String[length];
		time = new String[length];
		sure = new String[length];
		grade = new String[length];

		Bundle bundle = getArguments();
		name1 = bundle.getString("username");
		phone = bundle.getString("phone");
		ID = bundle.getString("id");
		state = bundle.getString("state");

		IntentFilter filter = new IntentFilter();
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

	// 第一个参数是这个Fragment将要显示的界面布局,
	// 第二个参数是这个Fragment所属的Activity,第三个参数是决定此fragment是否附属于Activity
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.left, null);

		red = (ImageView) view.findViewById(R.id.red);
		busyornot = (TextView) view.findViewById(R.id.busyornot);
		name = (TextView) view.findViewById(R.id.name_textview);
		myself = (TextView) view.findViewById(R.id.my_question_textview);
		myrecord = (TextView) view.findViewById(R.id.my_answer_textview);
		myprepare = (TextView) view.findViewById(R.id.my_prepare_textview);
		set = (TextView) view.findViewById(R.id.my_set_textview);
		note = (TextView) view.findViewById(R.id.my_collection_textview);
		
		Active_Job = (Button) view.findViewById(R.id.work);

		name.setText(name1);

		if (state.equals("1")) {
			busyornot.setText("停休");
			// busyornot.setBackgroundColor(R.color.red);
		}

		myself.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), Myself.class);
				intent.putExtra("username", name1);
				intent.putExtra("telphone", phone);
				startActivityForResult(intent, 100);
			}

		});

		Active_Job.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (a == true) {
					Active_Job.setText("停   休");
					busyornot.setText("停休");
					busyornot.setBackgroundResource(color.red);
					state123="1";	
					a = false;
				} else {
					Active_Job.setText("上   班");
					busyornot.setText("执勤");
					busyornot.setBackgroundResource(color.blue);
					state123="0";
					a = true;
				}
				
				Intent intent = new Intent();
				intent.putExtra("start", a);				
				intent.setAction(BROADCAST_ACTION);
				LeftFragment.this.getActivity().sendBroadcast(intent);
				
				Thread upThread = new Thread(new upThread());
				upThread.start();
			}

		});
		
		myprepare.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (name.getText().toString().equals("NAME")) {
					Toast.makeText(getActivity(), "请先登录您的账号！",
							Toast.LENGTH_LONG).show();

				} else {

					Intent intent = new Intent(getActivity(), Myprepare.class);
					intent.putExtra("policename", name1);
					startActivity(intent);
				}
			}

		});

		myrecord.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (name.getText().toString().equals("NAME")) {
					Toast.makeText(getActivity(), "请先登录您的账号！",
							Toast.LENGTH_LONG).show();

				} else {

					Intent intent = new Intent(getActivity(), Myrecord.class);
					intent.putExtra("policename", name1);
					startActivity(intent);
				}
			}

		});
		
		note.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), Note.class);				
				startActivity(intent);
			}

		});

		return view;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		if (resultCode == REQUEST_CODE) {

			Bundle extras = data.getExtras();

			name1 = extras.getString("NAME");
			phone = extras.getString("phone");
			name.setText(name1);

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	protected Context getApplicationContext() {
		// TODO Auto-generated method stub

		return null;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}
	class upThread implements Runnable {
		@Override
		public void run() {

			try {
				// URL合法，但是这一步并不验证密码是否正确
				boolean upValidate = up();
				Message msg = handler.obtainMessage();
				if (upValidate) {
					if (responseMsg.equals("success")) {
						msg.what = 0;
						handler.sendMessage(msg);
					} else {
						msg.what = 1;
						handler.sendMessage(msg);
					}
				} else {
					msg.what = 2;
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				// 发生超时,返回值区别于null与正常信息
				e.printStackTrace();
			}
		}
	} 
	Handler handler = new Handler();
	
	private boolean up() {
		boolean loginValidate = false;
		// 使用 HTTP客户端实现

		String urlStr = "http://219.245.64.1:8080/service/servlet/PworkServlet";
		HttpPost request = new HttpPost(urlStr);
		// 如果传递参数多的话，可以丢传递的参数进行封装
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("ID",ID));
		params.add(new BasicNameValuePair("pstate",state123));
	
		try {
			System.out.println("response??");
			// 设置请求参数项
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpClient client = getHttpClient();
			// 执行请求返回相应
			HttpResponse response = client.execute(request);
			System.out.println("response" + response);
			// 判断是否请求成功
			if (response.getStatusLine().getStatusCode() == 200) {
				loginValidate = true;
				// 获得响应信息
				responseMsg = EntityUtils.toString(response.getEntity());
				System.out.println("responseMsg" + responseMsg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loginValidate;
	}

	// 初始化HttpClient，并设置超时
	public HttpClient getHttpClient() {
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
		HttpConnectionParams.setSoTimeout(httpParams, 10000);
		HttpClient client = new DefaultHttpClient(httpParams);
		return client;
	}

}
