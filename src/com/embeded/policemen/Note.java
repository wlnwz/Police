package com.embeded.policemen;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Note extends Activity {
	MyListAdapter myAdapter = null;
	private ListView listview;
	boolean a = true;
	private String[] tim1, titl, maintex,id2;
	private int Notelength = 0;
	private TextView back,time1, time2, title, maintext, showmain;
	private String responseMsg = "";
	URL url = null;
	HttpURLConnection httpurlconnection = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tongzhi);		

		tim1 = new String[Notelength];
		titl = new String[Notelength];
		maintex = new String[Notelength];

		
		// get the note(time,title,precontent)
		load();
		
		System.out.println("maintex" +tim1[0]+titl[0]+ maintex[0]);

		back = (TextView) findViewById(R.id.return001);
		listview = (ListView) findViewById(R.id.list1);
		myAdapter = new MyListAdapter(Note.this, R.layout.list_item);
		listview.setAdapter(myAdapter);

		Onclick();
	}

	void Onclick() {
		OnClickListener monclicklistener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (v.equals(back)) {
					Note.this.finish();
				}
			}
		};
		back.setOnClickListener(monclicklistener);
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
			return Notelength;
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
			
			time1 = (TextView) convertView.findViewById(R.id.show123);
			time2 = (TextView) convertView.findViewById(R.id.array_text2);
			title = (TextView) convertView.findViewById(R.id.title_text);
			maintext = (TextView) convertView.findViewById(R.id.main_text); 
		
		     String time123=tim1[position].substring(5,11);

			time1.setText(time123);
			time2.setText(tim1[position]);
			title.setText(titl[position]);
			maintext.setText(maintex[position]);

			//goto the Alltext to show the content
			showmain = (TextView) convertView.findViewById(R.id.showmain);
			showmain.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(Note.this,Alltext.class);
					intent.putExtra("id2", id2[position]);
					intent.putExtra("title", titl[position]);
					intent.putExtra("time", tim1[position]);
					intent.putExtra("alltext", maintex[position]);
					startActivity(intent);

				}
			});
			
			return convertView;
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
				break;
			case 1:

				Toast.makeText(Note.this, "读取失败", Toast.LENGTH_SHORT)
						.show();
				break;
			case 2:

				Toast.makeText(Note.this, "服务器连接失败！", Toast.LENGTH_SHORT)
						.show();
				break;
			}

		}
	};

	private boolean upServerplocation() {
		boolean loginValidate = false;
		// 使用apache HTTP客户端实现
		String urlStr = "http://219.245.64.1:8080/service/servlet/SureServlet";
		//	String urlStr = "http://219.245.65.5:8080/service/servlet/SureServlet";

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
			dos.writeUTF("news");
			dos.writeUTF(String.valueOf("policenormal"));
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
		
			Notelength = dis.readInt();
			id2 = new String[Notelength];
			titl = new String[Notelength];
			tim1 = new String[Notelength];
			maintex = new String[Notelength];
			
			for (int i = 0; i < Notelength; i++) {
				id2[i] = dis.readUTF();
				tim1[i] = dis.readUTF();
				titl[i] = dis.readUTF();
				maintex[i] = dis.readUTF();				
			}
			System.out.println("length1@@@@@@@@2" + Notelength );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loginValidate;
	}

}
