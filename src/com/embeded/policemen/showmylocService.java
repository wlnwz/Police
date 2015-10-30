package com.embeded.policemen;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

public class showmylocService extends Service {
	private String policename1,policephone,peoplename,location1,location2,weizhi;

	URL url = null;
	HttpURLConnection httpurlconnection = null;
	private String responseMsg = "",abc="1";
	private int aa,ID=1;
	private boolean czjc;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * 每次调用startService，将执行onStart
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void onStart(final Intent intent, int startId) {

		policename1 = intent.getStringExtra("policename");
		policephone = intent.getStringExtra("policephone");
		peoplename = intent.getStringExtra("peoplename");
		location1 = intent.getStringExtra("location1");
		location2 = intent.getStringExtra("jingdu1");
		weizhi = intent.getStringExtra("weizhi");

		if (czjc == true) {
			mHandler.post(runnable);
		} else {

			
			mHandler.removeCallbacks(runnable);
		}
		super.onStart(intent, startId);
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			System.out.println("handler");
			switch (msg.what) {
			case 0:

				Toast.makeText(showmylocService.this, "发送成功", 1).show();
				break;
			case 1:
				Toast.makeText(getApplicationContext(), "发送失败",
						Toast.LENGTH_SHORT).show();
				break;
			case 2:
				// registerDialog.cancel();
				Toast.makeText(getApplicationContext(), "服务器连接失败！",
						Toast.LENGTH_SHORT).show();
				break;
			}

		}
	};

	private boolean upServerplocation() {
		boolean loginValidate = false;
		// 使用apache HTTP客户端实现
		String urlStr = "http://219.245.64.1:8080/service/servlet/PUplocServlet";
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
		} catch (ProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		DataOutputStream dos;
		// 上传用户名
		try {
			dos = new DataOutputStream(httpurlconnection.getOutputStream());
			dos.writeInt(ID);
			dos.writeUTF(abc);
			dos.writeUTF(policename1);
			dos.writeUTF(peoplename);
			dos.writeUTF(policephone);
			dos.writeUTF(location1);
			dos.writeUTF(location2);
			dos.writeUTF(weizhi);
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
			if(abc.equals("1")){
			responseMsg = dis.readUTF();
			ID=dis.readInt();
			}else{
				responseMsg = dis.readUTF();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return loginValidate;
	}

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
          
			up();            
            abc="0";
            Toast.makeText(showmylocService.this, "刷新线程", 1).show();
			mHandler.postDelayed(runnable, 10000);
            System.out.println("@@@@@policephone="+policephone);
		}
	};

	private Handler mHandler = new Handler();

	void up() {
		Thread downThread = new Thread(new UppoliceThread());
		downThread.start();
		try {
			downThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	class UppoliceThread implements Runnable {
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

	public void finish() {
		// TODO Auto-generated method stub
		mHandler.removeCallbacks(runnable);
	}
	
//	Runnable runnable = new Runnable() {
//		
//		@Override
//		public void run() {
//            up();
//            aa="1";
//			mHandler.postDelayed(runnable, 10000);
//        
//		}
//	};
//
//	private Handler mHandler = new Handler();
//	void up() {
//		Thread upThread = new Thread(new UppoliceThread());
//		upThread.start();
//	}
//	class UppoliceThread implements Runnable {
//		@Override
//		public void run() {
//
//			try {
//				// URL合法，但是这一步并不验证密码是否正确
//				boolean upValidate1 = upServerplocation();
//				Message msg = handler.obtainMessage();
//				if (upValidate1) {
//					if (responseMsg1.equals("success")) {
//						msg.what = 0;
//						handler.sendMessage(msg);
//					} else {
//						msg.what = 1;
//						handler.sendMessage(msg);
//					}
//				} else {
//					msg.what = 2;
//					handler.sendMessage(msg);
//				}
//			} catch (Exception e) {
//				// 发生超时,返回值区别于null与正常信息
//				e.printStackTrace();
//			}
//		}
//	}
//	private boolean upServerplocation() {
//		boolean loginValidate1 = false;
//		// use HTTP
//		String urlStr = "http://219.245.67.1:8080/service/servlet/PUplocServlet";
//		HttpPost request = new HttpPost(urlStr);
//		// 如果传递参数多的话，可以丢传递的参数进行封装
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		// 添加用户名和密码
//		params.add(new BasicNameValuePair("policename", policename));
//		params.add(new BasicNameValuePair("policephone", policephone));
//		params.add(new BasicNameValuePair("peoplename", peoplename));
//		params.add(new BasicNameValuePair("policewd", String.valueOf(weidu1)));
//		params.add(new BasicNameValuePair("policejd",String.valueOf(jingdu1)));
//		params.add(new BasicNameValuePair("weizhi",String.valueOf(weizhi)));
//		params.add(new BasicNameValuePair("aa",String.valueOf(aa)));
//		System.out.println("params" + params);
//		try {
//			System.out.println("response??");
//			// 设置请求参数项
//			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
//			HttpClient client = getHttpClient();
//			// 执行请求返回相应
//			HttpResponse response = client.execute(request);
//			System.out.println("response" + response);
//			// 判断是否请求成功
//			if (response.getStatusLine().getStatusCode() == 200) {
//				loginValidate1 = true;
//				// 获得响应信息
//				responseMsg1 = EntityUtils.toString(response.getEntity());
//				System.out.println("responseMsg" + responseMsg);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return loginValidate1;
//	}
}
