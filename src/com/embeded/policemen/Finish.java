package com.embeded.policemen;

import java.text.SimpleDateFormat;
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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Finish extends Activity{
	String  ID1,userID1, peoplephone, peoplename,date,time;
	private TextView starttime,finishtime,toutletime, save, name, phone,back,bad,good;
	private String responseMsg = "";
	private int record=1;
	private ImageView goodorbad;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finish);
	
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
		date = sDateFormat.format(new java.util.Date());


		
		Intent intent = getIntent();
		ID1 = intent.getExtras().getString("ID1");
		userID1 = intent.getExtras().getString("userid");
		peoplename = intent.getExtras().getString("peoplename");
		peoplephone = intent.getExtras().getString("telphone");
		time = intent.getExtras().getString("time");
		
		back = (TextView) findViewById(R.id.return001);
		name = (TextView) findViewById(R.id.peoplename);
		phone = (TextView) findViewById(R.id.peoplephone);
		save = (TextView) findViewById(R.id.sure);
		starttime = (TextView) findViewById(R.id.starttime);
		finishtime = (TextView) findViewById(R.id.finishtime);
		goodorbad = (ImageView) findViewById(R.id.finish123);
		bad = (TextView) findViewById(R.id.bad);
		good = (TextView) findViewById(R.id.good);
		
		name.setText("���ڣ�"+peoplename);
		phone.setText("�绰��"+peoplephone);
		starttime.setText("����ʱ�䣺"+time);
		finishtime.setText("�᰸ʱ�䣺"+date);

		InitClick();
	}
	void InitClick() {
		OnClickListener onclicklistener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (v.equals(save)) {

					Thread upThread = new Thread(new JAThread());
					upThread.start();

				}else if(v.equals(back)){
					Finish.this.finish();
				}else if(v.equals(bad)){
					goodorbad.setBackgroundResource(R.drawable.finish3);				
					record=0;
				}else if(v.equals(good)){
					goodorbad.setBackgroundResource(R.drawable.finish2);
					record=1;
				}				
			}			
		};
		save.setOnClickListener(onclicklistener);
		back.setOnClickListener(onclicklistener);
		bad.setOnClickListener(onclicklistener);
		good.setOnClickListener(onclicklistener);
	}
	
	class JAThread implements Runnable {
		@Override
		public void run() {

			try {
				// URL�Ϸ���������һ��������֤�����Ƿ���ȷ
				boolean upValidate = uppingfen();
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
				// ������ʱ,����ֵ������null��������Ϣ
				e.printStackTrace();
			}
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toast.makeText(Finish.this, "�ɹ��᰸", Toast.LENGTH_SHORT).show();
				
				Intent intent = new Intent(Finish.this, Myrecord.class);
				intent.putExtra("grade", "0");
				setResult(456, intent);
				Finish.this.finish();
				
				break;
			case 1:

				Toast.makeText(Finish.this, "�᰸ʧ��", Toast.LENGTH_SHORT).show();
				break;
			case 2:

				Toast.makeText(Finish.this, "����������ʧ�ܣ�", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};

	
	private boolean uppingfen() {
		boolean loginValidate = false;
		// ʹ�� HTTP�ͻ���ʵ��

		String urlStr = "http://219.245.64.1:8080/service/servlet/PJAServlet";
		HttpPost request = new HttpPost(urlStr);
		// ������ݲ�����Ļ������Զ����ݵĲ������з�װ
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("Finish","2"));
		params.add(new BasicNameValuePair("ID1", ID1));
		params.add(new BasicNameValuePair("userID1", userID1));
		params.add(new BasicNameValuePair("record", String.valueOf(record)));
		params.add(new BasicNameValuePair("overtime", date));
		System.out.println("params" + params);
		try {
			System.out.println("response??");
			// �������������
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpClient client = getHttpClient();
			// ִ�����󷵻���Ӧ
			HttpResponse response = client.execute(request);
			System.out.println("response" + response);
			// �ж��Ƿ�����ɹ�
			if (response.getStatusLine().getStatusCode() == 200) {
				loginValidate = true;
				// �����Ӧ��Ϣ
				responseMsg = EntityUtils.toString(response.getEntity());
				System.out.println("responseMsg" + responseMsg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loginValidate;
	}

	// ��ʼ��HttpClient�������ó�ʱ
	public HttpClient getHttpClient() {
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
		HttpConnectionParams.setSoTimeout(httpParams, 10000);
		HttpClient client = new DefaultHttpClient(httpParams);
		return client;
	}
}
