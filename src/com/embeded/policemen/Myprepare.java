package com.embeded.policemen;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
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

import com.embeded.policemen.Login.LoginThread;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Myprepare extends Activity{
	MyListAdapter myAdapter = null;
	private ListView listview;
	private TextView back;
	//private String[] peoplename,telphone,myweizhi,sure,infor,time,ID1,grade;
	private String[] ID1,userID1,peoplename,
					 wd,jd,myweizhi,time,infor,peopletelphone,
					 picname,voicename;
	private String policename, policephone,ID, state;
	private int length=100,posi;
	 private String responseMsg = "";
	 URL url = null;
		HttpURLConnection httpurlconnection = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myrecord);
	
		Intent intent = getIntent();
		policename=intent.getExtras().getString("policename");
		
		ID1=new String[length];
		userID1=new String[length];
		peoplename = new String[length];

		wd=new String[length];
		jd=new String[length];
		myweizhi=new String[length];
		infor=new String[length];
		time=new String[length];
		peopletelphone = new String[length];
		
		picname=new String[length];
		voicename = new String[length];
	        

		Thread finishThread = new Thread(new PrepareThread());
		finishThread.start();
		try {
			finishThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Init();
		Onclick();
	
	}
	
	void Init(){
		
		back=(TextView) findViewById(R.id.return001);
		listview = (ListView) findViewById(R.id.Listview);
		myAdapter = new MyListAdapter(Myprepare.this, R.layout.recordlist);
		listview.setAdapter(myAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterview, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				//if(sure[position].equals("1")){		
					
					posi = position;
					Intent intent = new Intent(Myprepare.this,Dealingwith.class);
					
					intent.putExtra("ID1", ID1[position]);
					intent.putExtra("userID1", userID1[position]);
					intent.putExtra("peoplename", peoplename[position]);
					
					intent.putExtra("wd", wd[position]);
					intent.putExtra("jd", jd[position]);
					intent.putExtra("weizhi", myweizhi[position]);
					intent.putExtra("infor", infor[position]);
					intent.putExtra("time", time[position]);
					intent.putExtra("phone", peopletelphone[position]);
					
					intent.putExtra("picname", picname[position]);
					intent.putExtra("voicename", voicename[position]);
					
					
					intent.putExtra("policename", policename);
					intent.putExtra("policephone", policephone);
					startActivity(intent);
					
		
			}
		});
		
	}
	
	void Onclick(){
		OnClickListener monclicklistener = new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(v.equals(back)){
					Myprepare.this.finish();
				}
				
			}
			
		};
		back.setOnClickListener(monclicklistener);
	}
	
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//
//		if (resultCode == 456) {
//
//			Bundle extras = data.getExtras();
//            String grade1;
//			grade1 = extras.getString("grade");
//		    grade[posi]=grade1;
//		  
//
//		}
//		super.onActivityResult(requestCode, resultCode, data);
//	}
//	
	class PrepareThread implements Runnable {
		@Override
		public void run() {

			try {
				// URL合法，但是这一步并不验证密码是否正确
				boolean upValidate = prepare123();
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
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:

				Toast.makeText(getApplicationContext(), "查询成功！",
						Toast.LENGTH_SHORT).show();

				break;
			case 1:
				Toast.makeText(getApplicationContext(), "查询失败",
						Toast.LENGTH_SHORT).show();
				break;
			case 2:
				// loadingDialog.cancel();
				Toast.makeText(getApplicationContext(), "服务器连接失败",
						Toast.LENGTH_SHORT).show();
				
				break;
			}

		}
	};
	
	private boolean prepare123() {
			boolean loginValidate = false;
			// 使用apache HTTP客户端实现
		//	String urlStr = "http://219.245.67.1:8080/service/servlet/PrecordServlet";
			String urlStr = "http://219.245.64.1:8080/service/servlet/DPrecordServlet";
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
				dos.writeUTF(policename);
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

				System.out.println("length"+length);
				
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
					peopletelphone[i]=dis.readUTF();
					
					picname[i]=dis.readUTF();
					voicename[i]=dis.readUTF();
//					grade[i] = dis.readUTF();
//					sure[i] = dis.readUTF();
					
					
					System.out.println("ID"+ID1[i]+peopletelphone[i]+peoplename[i]+voicename[i]);			
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
			TextView text=null,shijian=null,didian=null,qd = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						mTextViewResourceID, null);

				text = (TextView) convertView.findViewById(R.id.people2);
				shijian = (TextView) convertView.findViewById(R.id.time);
				didian = (TextView) convertView.findViewById(R.id.weizhi);
				qd = (TextView) convertView.findViewById(R.id.zhuangtai);
				
				text.setText("民众-"+peoplename[position]);
				didian.setText(myweizhi[position]);
				shijian.setText(time[position]);				
			    //qd.setText(grade[position]);
		
				
	
			}
			return convertView;
		}
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	    myAdapter.notifyDataSetChanged();
	    listview.setAdapter(myAdapter);
	}
}
