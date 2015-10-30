package com.embeded.policemen;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class More extends Activity{
	TextView title, time, alltext,back,voice,pict,voic;
	ImageView pic;
	String title1,time1,id2,allmessage,picname,voicename;
	URL url = null;
	HttpURLConnection httpurlconnection = null;
	InputStream data,voicedata;
	Bitmap bitmap;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more);
		
		Intent intent = getIntent();
		id2=intent.getExtras().getString("id");
		time1=intent.getExtras().getString("time");
		allmessage =intent.getExtras().getString("infor");
		picname =intent.getExtras().getString("picname");
		voicename =intent.getExtras().getString("voicename");
		
		time = (TextView)findViewById(R.id.time123);
		alltext = (TextView)findViewById(R.id.tvmore);
		back = (TextView)findViewById(R.id.return001);
		voice = (TextView)findViewById(R.id.voice);
		pict = (TextView)findViewById(R.id.ima);
		voic = (TextView)findViewById(R.id.vic);
		pic = (ImageView)findViewById(R.id.pic);
		
		// download the pic
		if(picname.equals("null")){	
			pict.setText("pic:   ��ͼƬ");
		}else{
			Initallpic();
			pict.setText("pic:   "+picname);
		}
		if(voicename.equals("null")){
			voic.setText("voice: ������");
			//voice.setBackgroundResource(R.drawable.about);
			voice.setBackgroundResource(R.drawable.voice1);
		}else{
			voic.setText("voice: "+voicename);
			voice.setBackgroundResource(R.drawable.voice1);			
		}	
		time.setText(time1);
		alltext.setText(allmessage);
		
		Onclick();
	}

	void Onclick() {
		OnClickListener monclicklistener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (v.equals(back)) {
					More.this.finish();
				}else if(v.equals(voice)){
					if(!voicename.equals("null")){
						Initvoice();
					}
				}
			}
		};
		back.setOnClickListener(monclicklistener);
		voice.setOnClickListener(monclicklistener);
	}
	
	void Initallpic(){
		Thread downThread = new Thread(new PicThread());
		downThread.start();
	}
	
	void Initvoice(){
		Thread downvoiceThread = new Thread(new VoiceThread());
		downvoiceThread.start();
	}
	
	class PicThread implements Runnable {
		@Override
		public void run() {
 
			try {
				// URL�Ϸ���������һ��������֤�����Ƿ���ȷ
				data = downpic();
				Message msg = handler.obtainMessage();
				if (data!=null) {
					
						msg.what = 0;
						handler.sendMessage(msg);
					
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

	class VoiceThread implements Runnable {
		@Override
		public void run() {
 
			try {
				// URL�Ϸ���������һ��������֤�����Ƿ���ȷ
				voicedata = downvoice();
				Message msg = handler2.obtainMessage();
				if (voicedata!=null) {
					
						msg.what = 0;
						handler2.sendMessage(msg);
					
				} else {
					msg.what = 2;
					handler2.sendMessage(msg);
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
				
				bitmap = BitmapFactory.decodeStream(data);		
				
				
	    			File file = new File("/sdcard/myImagea/");
	    			file.mkdirs();
	    			  FileOutputStream fout;
	    			String picturePath = "/sdcard/myImagea/" + picname;

	    			try {
	    				fout = new FileOutputStream(picturePath);
	    				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);

	    			} catch (FileNotFoundException e) {
	    				e.printStackTrace();
	    			}
				
	    			connectHanlder.sendEmptyMessage(0);
	    			
				Toast.makeText(More.this, "��ȡͼƬ�ɹ�", Toast.LENGTH_SHORT)
						.show();
				
				break;
			case 1:

				Toast.makeText(More.this, "��ȡͼƬʧ��", Toast.LENGTH_SHORT)
						.show();
				break;
			case 2:

				Toast.makeText(More.this, "����������ʧ�ܣ�", Toast.LENGTH_SHORT)
						.show();
				break;
			}

		}
	};
	
	Handler handler2 = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
			//	MediaRecorder mMediaRecorder01 = new MediaRecorder();
				MediaPlayer mmediaplayer = new MediaPlayer();
				try {
					mmediaplayer.setDataSource("http://219.245.64.1:8080/service/Voice/"+voicename);
					mmediaplayer.prepare();
					mmediaplayer.start();
				} catch (IllegalArgumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalStateException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				  
				
				File file = new File("/sdcard/myVoice2/");
	    			file.mkdirs();
	    			// create the file
	    			String voicePath = "/sdcard/myVoice2/" + voicename;
	    			// ������д���ļ�
	    			try {
	    				new FileOutputStream(voicePath);

	    			} catch (FileNotFoundException e) {
	    				e.printStackTrace();
	    			}
				
				Toast.makeText(More.this, "��ȡ�����ɹ�", Toast.LENGTH_SHORT)
						.show();
				
				break;
			case 1:

				Toast.makeText(More.this, "��ȡʧ��", Toast.LENGTH_SHORT)
						.show();
				break;
			case 2:

				Toast.makeText(More.this, "����������ʧ�ܣ�", Toast.LENGTH_SHORT)
						.show();
				break;
			}

		}
	};
	
	
	private Handler connectHanlder = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
          
            // ����UI����ʾͼƬ  
            if (bitmap != null) {  
                pic.setImageBitmap(bitmap);// display image  
                System.out.println("!!!!!!!!!1");
                            
                
            }  
        }  
    };  
	
	private InputStream downpic() {
		// ʹ��apache HTTP�ͻ���ʵ��
		String urlStr1 = "http://219.245.64.1:8080/service/Image/";
		String urlStr=urlStr1+picname;
	
		try {
			data = getImageStream(urlStr);			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	
	}
	 
	 public InputStream getImageStream(String path) throws Exception{  
	        URL url = new URL(path);  
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
	        conn.setConnectTimeout(5 * 1000);  
	        conn.setRequestMethod("GET");  
	        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){  
	            return conn.getInputStream();  
	        }  
	        return null;  
	    }  
	 
	 private InputStream downvoice() {
		
			// ʹ��apache HTTP�ͻ���ʵ��
			String urlStr1 = "http://219.245.67.1:8080/service/Voice/";
			String urlStr=urlStr1+voicename;
		
			try {
				voicedata = getVoiceStream(urlStr);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return voicedata;
		
		}
		 
		 public InputStream getVoiceStream(String path) throws Exception{  
		        URL url = new URL(path);  
		        HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
		        conn.setConnectTimeout(5 * 1000);  
		        conn.setRequestMethod("GET");  
		        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){  
		            return conn.getInputStream();  
		        }  
		        return null;  
		    }  

}
