/**
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
 **/
package com.embeded.slidingmenu.view;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.embeded.policemen.DetailActivity;
import com.embeded.policemen.MaindemoActivity;
import com.embeded.policemen.R;
import com.util.jwdtransfertolength;
import com.util.location;

public class ViewPageFragment extends Fragment {

	TextView peoplname;
	TextView juli = null, shijian = null, didian = null;
	private TextView showLeft,text1,text2,text3;
	View mView;

	MyListAdapter myAdapter = null;
	private ViewPager mPager;
	private ArrayList<Fragment> pagerItemList = new ArrayList<Fragment>();

	private ListView listview;
	static List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
	static SimpleAdapter adapter;
	public static Context context;
	private String policename, policephone, ID, state;
	double location1, location2;
	private MyBroadcastRecever myBroadcastRecever;

	URL url = null;
	HttpURLConnection httpurlconnection = null;
	private String responseMsg = "";
	private int length = 100;
	private String[] peoplename, peopletelphone, myweizhi, time, infor, ID1,
			userID1, wd, jd, picname, voice;
	private String[] peoplename2, peopletelphone2, myweizhi2, time2, infor2,
			ID12, userID12, wd2, jd2, picname2, voice2;
	double[] length123, length2;
	jwdtransfertolength transfer;
	double weidu1, jingdu1;
	private boolean start = false;
	LayoutInflater inflater ;

	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		IntentFilter filter = new IntentFilter();
		myBroadcastRecever = new MyBroadcastRecever();
		// 设置接收广播的类型，这里要和设置的类型匹配，还可以在AndroidManifest.xml文件中注册
		filter.addAction("com.example.start");
		getActivity().registerReceiver(myBroadcastRecever, filter);

		transfer = new jwdtransfertolength();
		location.mLocClient = new LocationClient(getActivity());
		location.InIt();
		// location.mLocClient.start();

		Bundle bundle = getArguments();
		policename = bundle.getString("username");
		policephone = bundle.getString("phone");
		ID = bundle.getString("id");
		state = bundle.getString("state");

		peoplename = new String[length];
		peopletelphone = new String[length];
		time = new String[length];
		myweizhi = new String[length];
		infor = new String[length];
		ID1 = new String[length];
		userID1 = new String[length];
		wd = new String[length];
		jd = new String[length];
		picname = new String[length];
		voice = new String[length];

		peoplename2 = new String[length];
		peopletelphone2 = new String[length];
		time2 = new String[length];
		myweizhi2 = new String[length];
		infor2 = new String[length];
		ID12 = new String[length];
		userID12 = new String[length];
		wd2 = new String[length];
		jd2 = new String[length];
		picname2 = new String[length];
		voice2 = new String[length];

		length123 = new double[length];

	
	}

	private Handler mHandler = new Handler();
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			load();
			mHandler.postDelayed(runnable, 10000);

		}
	}; 

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	
			mView = inflater.inflate(R.layout.main_pager, null);
			showLeft = (TextView) mView.findViewById(R.id.showLeft);
			text1 = (TextView) mView.findViewById(R.id.text1);
			text2 = (TextView) mView.findViewById(R.id.text2);
			text3 = (TextView) mView.findViewById(R.id.text3);			
			listview = (ListView) mView.findViewById(R.id.Listview);
			myAdapter = new MyListAdapter(getActivity(), R.layout.recordlist1);
			listview.setAdapter(myAdapter);
			listview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> adapterview, View view,
						int position, long id) {
					// TODO Auto-generated method stub

					Intent intent = new Intent(getActivity(), DetailActivity.class);
					intent.putExtra("ID1", ID12[position]);
					intent.putExtra("userID1", userID12[position]);
					intent.putExtra("peoplename", peoplename2[position]);
					intent.putExtra("time", time2[position]);
					intent.putExtra("wd", wd2[position]);
					intent.putExtra("jd", jd2[position]);
					intent.putExtra("infor", infor2[position]);
					intent.putExtra("phone", peopletelphone2[position]);
					intent.putExtra("weizhi", myweizhi2[position]);
					intent.putExtra("picname", picname2[position]);
					intent.putExtra("voicename", voice2[position]);

					intent.putExtra("policename", policename);
					intent.putExtra("policephone", policephone);

					startActivityForResult(intent, 456);
				}
			});			
			
		return mView;
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);

		showLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((MaindemoActivity) getActivity()).showLeft();
			}
		});

	}

	public boolean isFirst() {
		if (mPager.getCurrentItem() == 0)
			return true;
		else
			return false;
	}

	public boolean isEnd() {
		if (mPager.getCurrentItem() == pagerItemList.size() - 1)
			return true;
		else
			return false;
	}

	public class MyAdapter extends FragmentPagerAdapter {
		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return pagerItemList.size();
		}

		@Override
		public Fragment getItem(int position) {

			Fragment fragment = null;
			if (position < pagerItemList.size())
				fragment = pagerItemList.get(position);
			else
				fragment = pagerItemList.get(0);

			return fragment;

		}
	}

	public interface MyPageChangeListener {
		public void onPageSelected(int position);
	}
 
	// 进入搜索报警相应线程，查询数据库，如果已经被响应，更新
	private void Initsearch() {
		LayoutInflater inflater = this.getActivity().getLayoutInflater();;
		if (start == false) {
			mHandler.post(runnable);	
			
				text1.setVisibility(View.GONE);
				text2.setVisibility(View.GONE);
				text3.setVisibility(View.GONE);
				listview.setVisibility(View.VISIBLE);				
		} else {	
			mHandler.removeCallbacks(runnable);
			listview.setVisibility(View.GONE);
			text1.setVisibility(View.VISIBLE);
			text2.setVisibility(View.VISIBLE);
			text3.setVisibility(View.VISIBLE);				
		}	
	
	}

	void load() {
		Thread downThread = new Thread(new SureThread());
		downThread.start();
		try {
			downThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class SureThread implements Runnable {
		@Override
		public void run() {

			try {
				// URL合法，但是这一步并不验证密码是否正确
				boolean upValidate = upServerplocation();
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

	Handler handler = new Handler() {
		 public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:

				myAdapter.notifyDataSetChanged();
				listview.setAdapter(myAdapter);

				break;
			case 1:

				Toast.makeText(getActivity(), "读取失败", Toast.LENGTH_SHORT)
						.show();
				break;
			case 2:

				Toast.makeText(getActivity(), "服务器连接失败！", Toast.LENGTH_SHORT)
						.show();
				break;
			}

		}
	};

	private boolean upServerplocation() {
		boolean loginValidate = false;
		// 使用apache HTTP客户端实现
//		String urlStr = "http://219.245.67.1:8080/service/servlet/PSureServlet";
		String urlStr = "http://219.245.64.1:8080/service/servlet/PSureServlet";
		try {
			url = new URL(urlStr);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			httpurlconnection = (HttpURLConnection) url.openConnection();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		httpurlconnection.setDoOutput(true);
		try {
			httpurlconnection.setRequestMethod("POST");
			httpurlconnection.setConnectTimeout(5000);
			httpurlconnection.setReadTimeout(10000);
		} catch (ProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		DataOutputStream dos;
		// 上传用户名
		try {
			dos = new DataOutputStream(httpurlconnection.getOutputStream());
			dos.writeUTF("request");
			dos.flush();
			dos.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			loginValidate = true;
			DataInputStream dis = new DataInputStream(
					httpurlconnection.getInputStream());

			// 读取连接状态以及数据总数
			responseMsg = dis.readUTF();
			length = dis.readInt();

			length123 = new double[length];
			weidu1 = location.weidu;
			jingdu1 = location.jingdu;

			// 循环读取警察位置表中的所有信息，包括警察编号、电话、位置信息等
			for (int i = 0; i < length; i++) {
				ID1[i] = dis.readUTF();
				userID1[i] = dis.readUTF();
				peoplename[i] = dis.readUTF();
				wd[i] = dis.readUTF();
				jd[i] = dis.readUTF();
				myweizhi[i] = dis.readUTF();
				time[i] = dis.readUTF();
				infor[i] = dis.readUTF();
				peopletelphone[i] = dis.readUTF();
				picname[i] = dis.readUTF();
				voice[i] = dis.readUTF();

				length123[i] = (transfer.getDistance(Double.parseDouble(jd[i]),
						Double.parseDouble(wd[i]), jingdu1, weidu1));

				System.out.println("ID" + ID1[i] + peopletelphone[i]
						+ peoplename[i] + voice[i]);
				System.out
						.println("weidu1/jingdu1[@@@@]" + length123[i] + "\n");
			}

			// 进行按距离重新更新数组，按距离远近显示列表
			List<Double> list = new ArrayList<Double>();
			for (double i : length123) {
				list.add(i);
			}
			// 对数组排序
			java.util.Arrays.sort(length123);

			System.out.println("排序以及它们的下标分别为：");
			for (int i = 0; i <= length123.length - 1; i++) {
				int index = list.indexOf(length123[i]);
				ID12[i] = ID1[index];
				userID12[i] = userID1[index];
				peoplename2[i] = peoplename[index];
				wd2[i] = wd[index];
				jd2[i] = jd[index];
				myweizhi2[i] = myweizhi[index];
				time2[i] = time[index];
				infor2[i] = infor[index];
				peopletelphone2[i] = peopletelphone[index];
				picname2[i] = picname[index];
				voice2[i] = voice[index];

				System.out.print(length123[i] + " 下标:");
				System.out.print(list.indexOf(length123[i]) + "\r\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return loginValidate;
	}

	public class MyListAdapter extends ArrayAdapter<Object> {
		int mTextViewResourceID = 0;
		private Context mContext;

		public MyListAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
			mTextViewResourceID = textViewResourceId;
			mContext = context;
		}

		public int getCount() {
			return length;
		}

		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {

			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						mTextViewResourceID, null);

			}
			peoplname = (TextView) convertView.findViewById(R.id.people2);
			shijian = (TextView) convertView.findViewById(R.id.time);
			didian = (TextView) convertView.findViewById(R.id.weizhi);
			juli = (TextView) convertView.findViewById(R.id.juli);
			peoplname.setText("民众:" + peoplename2[position]);
			didian.setText((myweizhi2[position].equals(" ")) ? "未知":myweizhi2[position]);
			juli.setText("---距离 :" + ((didian.getText().equals("未知"))?"未知":String.valueOf(length123[position]) + "/m"));

			shijian.setText(time2[position]);
			return convertView;
		}
	}

	class MyBroadcastRecever extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			if (intent.getAction().equals("com.example.start")) {
				start = intent.getBooleanExtra("start", false);	
				
				Initsearch();
			}
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
		getActivity().unregisterReceiver(myBroadcastRecever);
	}
}
