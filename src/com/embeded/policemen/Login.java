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

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.util.Encrypt;
import com.util.SysApplication;

public class Login extends Activity implements OnClickListener {

	private TextView btnLoginCancel;
	private EditText etLoginName, etPassword;
	private Button btnLogin;
	URL url = null;
	HttpURLConnection httpurlconnection = null;
	private String responseMsg = "", phone = "0", ID, state;
	private String loginNameValue, passwordValue;

	private static final int REQUEST_TIMEOUT = 5 * 1000;// 设置请求超时10秒钟
	private static final int SO_TIMEOUT = 10 * 1000; // 设置等待数据超时时间10秒钟
	private static final int LOGIN_OK = 1;
	public static SharedPreferences sp;
	int REQUEST_CODE = 0;
	public static String name22;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		SysApplication.getInstance().addActivity(this);// 记录打开的Activity
		InitView();
	}

	private void InitView() {
		// TODO Auto-generated method stub

		etLoginName = (EditText) findViewById(R.id.loginname);
		etPassword = (EditText) findViewById(R.id.password);

		btnLoginCancel = (TextView) findViewById(R.id.login_cancel);
		btnLogin = (Button) findViewById(R.id.btnLogin);

		btnLoginCancel.setOnClickListener(this);
		btnLogin.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.login_cancel:
			this.finish();
			break;

		case R.id.btnLogin:
			Loginbtn();
			break;

		default:
			break;
		}
	}

	public void Loginbtn() {
		loginNameValue = etLoginName.getText().toString();
		passwordValue = etPassword.getText().toString();
		if (loginNameValue.equals("") || loginNameValue.length() > 20
				|| loginNameValue.length() < 1) {
			Toast.makeText(Login.this, "用户名格式错误", Toast.LENGTH_SHORT).show();
		} else if (passwordValue.equals("") || passwordValue.length() > 16
				|| passwordValue.length() < 6) {
			Toast.makeText(Login.this, "请输入正确密码", Toast.LENGTH_SHORT).show();
		} else {
			
			Thread loginThread = new Thread(new LoginThread());
			loginThread.start();
		}

	}

	// LoginThread线程类
	class LoginThread implements Runnable {

		@Override
		public void run() {
			String passwordValue0;
			passwordValue0 = Encrypt.md5(passwordValue);

			// URL合法，但是这一步并不验证密码是否正确
			boolean loginValidate = loginServer(loginNameValue, passwordValue0);
			Message msg = handler.obtainMessage();
			if (loginValidate) {
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
		}

	}

	// Handler
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0: {
				Toast.makeText(getApplicationContext(), "登录成功！",
						Toast.LENGTH_SHORT).show();
				String bundle = new String();
				bundle = etLoginName.getText().toString();
				System.out.println(bundle + "11111111");

				Intent intent = new Intent(Login.this, MaindemoActivity.class);
				intent.putExtra("NAME", loginNameValue);
				intent.putExtra("phone", phone);
				intent.putExtra("id", ID);
				intent.putExtra("state", state);
				System.out.println(loginNameValue + phone + ID + state);
				startActivity(intent);
				// auto_check = false;
				Login.this.finish();

				break;
			}
			case 1: {// loadingDialog.cancel();
				Toast.makeText(getApplicationContext(), "输入的密码错误",
						Toast.LENGTH_SHORT).show();
				break;
			}
			case 2: {
				// loadingDialog.cancel();
				Toast.makeText(getApplicationContext(), "服务器连接失败",
						Toast.LENGTH_SHORT).show();
				break;
			}
			}
		}
	};

	private boolean loginServer(String username, String password) {
		boolean loginValidate = false;
		// 使用apache HTTP客户端实现
		String urlStr = "http://219.245.64.1:8080/service/servlet/PLoginServlet";
		// String urlStr =
		// "http://172.20.7.2:8080/service/servlet/PLoginServlet";
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

		try {
			httpurlconnection.setRequestMethod("POST");
			httpurlconnection.setConnectTimeout(5000);
			httpurlconnection.setReadTimeout(10000);

		} catch (ProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		httpurlconnection.setDoOutput(true);
		DataOutputStream dos;
		// 添加用户名和密码
		try {
			dos = new DataOutputStream(httpurlconnection.getOutputStream());
			dos.writeUTF(String.valueOf(username));
			dos.writeUTF(String.valueOf(password));
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
			responseMsg = dis.readUTF();
			phone = dis.readUTF();
			ID = dis.readUTF();
			state = dis.readUTF();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loginValidate;
	}

	// // 初始化HttpClient，并设置超时
	public HttpClient getHttpClient() {
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
		HttpClient client = new DefaultHttpClient(httpParams);
		return client;
	}

}
