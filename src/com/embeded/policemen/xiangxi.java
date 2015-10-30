package com.embeded.policemen;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.util.MyOrientationListener;
import com.util.MyOrientationListener.OnOrientationListener;
import com.util.jwdtransfertolength;

public class xiangxi extends Activity implements OnGetRoutePlanResultListener {
	SupportMapFragment map;
	LocationClient mLocClient;
	MapStatusUpdate u;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private double weidu1, jingdu1;
	boolean isFirstLoc = true;// �Ƿ��״ζ�λ
	private LatLng ll, ll2;
	private OverlayOptions oo;
	private BitmapDescriptor bdA;
	private Marker mMarker;
	private LocationMode mCurrentMode;
	private String weizhi, weidu, jingdu, id, userid, peoplephone, peoplename,
			infor, time123, length, policename, policephone, responseMsg = "",
			responseMsg1 = "", picname, voicename, abc = "1", date;

	jwdtransfertolength transfer;
	private InfoWindow mInfoWindow;
	MyOrientationListener myOrientationListener;
	private float mCurrentAccracy;
	public MyLocationListenner myListener = new MyLocationListenner();
	BitmapDescriptor mCurrentMarker;
	Button requestLocButton, see;
	TextView weiz, call, receive, more, back;
	private int mXDirection;
	Button button;
	URL url = null, url1 = null;
	HttpURLConnection httpurlconnection = null, httpurlconnection1 = null;
	private boolean czjc = true;
	private int ID = 1;
	RouteLine route = null;
	RoutePlanSearch mSearch = null; // ����ģ�飬Ҳ��ȥ����ͼģ�����ʹ��
	int nodeIndex = -1, haverote = 0;// �ڵ�����,������ڵ�ʱʹ��
	OverlayManager routeOverlay = null;
	boolean useDefaultIcon = false;
	DrivingRouteOverlay overlay;
	WalkingRouteOverlay overlay2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(this.getApplication());
		setContentView(R.layout.view_pager);
		Intent intent = getIntent();
		weizhi = intent.getExtras().getString("weizhi");
		weidu = intent.getExtras().getString("wd");
		jingdu = intent.getExtras().getString("jd");
		infor = intent.getExtras().getString("infor");
		time123 = intent.getExtras().getString("time");
		id = intent.getExtras().getString("ID1");
		userid = intent.getExtras().getString("userID1");
		peoplephone = intent.getExtras().getString("phone");
		peoplename = intent.getExtras().getString("peoplename");
		picname = intent.getExtras().getString("picname");
		voicename = intent.getExtras().getString("voicename");

		policephone = intent.getExtras().getString("policephone");
		policename = intent.getExtras().getString("policename");

		mSearch = RoutePlanSearch.newInstance();
		mSearch.setOnGetRoutePlanResultListener(this);

		initOritationListener();

		InitMap();
		Initmarketclick();
	}

	void InitMap() {

		call = (TextView) findViewById(R.id.call);
		weiz = (TextView) findViewById(R.id.location1);
		more = (TextView) findViewById(R.id.tv_more);
		receive = (TextView) findViewById(R.id.receive);
		back = (TextView) findViewById(R.id.return001);
		see = (Button) findViewById(R.id.see);

		weiz.setText(weizhi);

		requestLocButton = (Button) findViewById(R.id.button1);
		requestLocButton.getBackground().setAlpha(150);

		mCurrentMode = LocationMode.FOLLOWING;
		// ��ͼ��ʼ��
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMyLocationEnabled(true);
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		// ���ö�λģʽ
		option.setAddrType("all");
		option.setOpenGps(true);// ��gps
		option.setCoorType("bd09ll"); // ������������
		option.setScanSpan(2000);
		option.setPriority(LocationClientOption.GpsFirst);
		mLocClient.setLocOption(option);
		mLocClient.start();
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				mCurrentMode, true, mCurrentMarker));

		route = null;
		Initclick();
	}

	private void initOritationListener() {
		myOrientationListener = new MyOrientationListener(
				this.getApplicationContext());
		myOrientationListener
				.setOnOrientationListener(new OnOrientationListener() {
					@Override
					public void onOrientationChanged(float x) {
						mXDirection = (int) x;

						// ���춨λ����
						MyLocationData locData = new MyLocationData.Builder()
								.accuracy(mCurrentAccracy)
								// �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
								.direction(mXDirection).latitude(weidu1)
								.longitude(jingdu1).build();
						// ���ö�λ����
						mBaiduMap.setMyLocationData(locData);
					}
				});
	}

	void Initclick() {

		OnClickListener btnClickListener = new OnClickListener() {
			public void onClick(View v) {
				if (v.equals(requestLocButton)) {

					switch (mCurrentMode) {
					case NORMAL:

						mCurrentMode = LocationMode.FOLLOWING;
						mBaiduMap
								.setMyLocationConfigeration(new MyLocationConfiguration(
										mCurrentMode, true, mCurrentMarker));
						u = MapStatusUpdateFactory.zoomTo(15);
						mBaiduMap.animateMapStatus(u);
						requestLocButton.setText("��");
						requestLocButton
								.setBackgroundResource(R.drawable.white);
						requestLocButton.getBackground().setAlpha(200);
						mBaiduMap.clear();
						break;
					case COMPASS:
						mCurrentMode = LocationMode.NORMAL;
						mBaiduMap
								.setMyLocationConfigeration(new MyLocationConfiguration(
										mCurrentMode, true, mCurrentMarker));
						requestLocButton.setText("");
						requestLocButton
								.setBackgroundResource(R.drawable.fuyuan);
						requestLocButton.getBackground().setAlpha(200);
						break;
					case FOLLOWING:
						mCurrentMode = LocationMode.COMPASS;
						mBaiduMap
								.setMyLocationConfigeration(new MyLocationConfiguration(
										mCurrentMode, true, mCurrentMarker));
						requestLocButton.setText("��");
						requestLocButton.setBackgroundColor(Color.WHITE);
						requestLocButton.getBackground().setAlpha(200);
						break;
					}
				} else if (v.equals(call)) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							xiangxi.this);
					builder.setIcon(R.drawable.hx);
					builder.setTitle("-Call the person-");
					builder.setMessage("You will call the person who made the alarm! ");
					// PositiveButton
					builder.setPositiveButton("call",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent intent = new Intent();
									intent.setAction(Intent.ACTION_CALL);
									intent.setData(Uri.parse("tel:"
											+ peoplephone));
									startActivity(intent);
								}
							});
					// NegativeButton
					builder.setNegativeButton("cancel",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							});
					builder.show();

				} else if (v.equals(more)) {
					Intent intent = new Intent(xiangxi.this, More.class);
					intent.putExtra("id", id);
					intent.putExtra("userid", userid);
					intent.putExtra("infor", infor);
					intent.putExtra("time", time123);
					intent.putExtra("picname", picname);
					intent.putExtra("voicename", voicename);
					startActivity(intent);
				} else if (v.equals(receive)) {

					update();

				} else if (v.equals(back)) {
					xiangxi.this.finish();
				} else if (v.equals(see)) {

					if (czjc == true) {
						mHandler.post(runnable);
						see.setTextColor(getResources().getColor(R.color.red));
						see.setText("�ɼ�");
						see.setTextSize(14);
						czjc = false;
					} else {
						mHandler.removeCallbacks(runnable);
						abc = "3";
						up();
						see.setTextColor(getResources().getColor(R.color.black));
						see.setText("���ɼ�");
						see.setTextSize(10);

						czjc = true;
						abc = "1";
					}

				}
			}
		};
		requestLocButton.setOnClickListener(btnClickListener);
		more.setOnClickListener(btnClickListener);
		receive.setOnClickListener(btnClickListener);
		call.setOnClickListener(btnClickListener);
		back.setOnClickListener(btnClickListener);
		see.setOnClickListener(btnClickListener);
	}

	/**
	 * ��λSDK��������
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view ���ٺ��ڴ����½��յ�λ��
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360
					.direction(mXDirection).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);

			if (isFirstLoc) {
				isFirstLoc = false;
				mCurrentAccracy = location.getRadius();
				weidu1 = location.getLatitude();
				jingdu1 = location.getLongitude();
				ll = new LatLng(weidu1, jingdu1);
				weizhi = location.getAddrStr();
				u = MapStatusUpdateFactory.newLatLng(ll);
				u = MapStatusUpdateFactory.zoomTo(15);

				mBaiduMap.setMapStatus(u);
				mBaiduMap.animateMapStatus(u);
				System.out.println("location==" + ll);

				initOverlay();

			}

		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	void initOverlay() {

		ll2 = new LatLng(Double.parseDouble(weidu), Double.parseDouble(jingdu));
		transfer = new jwdtransfertolength();
		length = String.valueOf(transfer.getDistance(
				Double.parseDouble(jingdu), Double.parseDouble(weidu), jingdu1,
				weidu1));

		bdA = BitmapDescriptorFactory.fromResource(R.drawable.ddx);
		oo = new MarkerOptions().position(ll2).icon(bdA).zIndex(9)
				.draggable(true);
		mMarker = (Marker) (mBaiduMap.addOverlay(oo));

		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll2);
		mBaiduMap.setMapStatus(u);
		Toast.makeText(getApplicationContext(), "�����ǩ", Toast.LENGTH_SHORT)
				.show();

	}

	public void Initmarketclick() {
		OnMarkerClickListener onClickListener = new OnMarkerClickListener() {
			public boolean onMarkerClick(final Marker marker) {
				button = new Button(xiangxi.this.getApplicationContext());
				button.setBackgroundResource(R.drawable.popup);
				final LatLng ll = marker.getPosition();
				Point p = mBaiduMap.getProjection().toScreenLocation(ll);
				p.y -= 47;
				LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);
				OnInfoWindowClickListener listener = null;

				button.setText("����:" + peoplename + "-��ϸ");
				{
					listener = new OnInfoWindowClickListener() {
						public void onInfoWindowClick() {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									xiangxi.this);
							builder.setIcon(R.drawable.hx);
							builder.setTitle("�������ڣ�" + peoplename + "��" + "\n"
									+ " Ŀǰ��������" + length + "m");
							builder.setMessage("�ҵĵ绰:" + peoplephone);
							builder.setPositiveButton("�鿴������Ϣ",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											Toast.makeText(
													getApplicationContext(),
													"��δ����", Toast.LENGTH_SHORT)
													.show();
											// @@@@@@@@@@@

										}
									});
							builder.setNegativeButton("ȡ��",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {

										}
									});
							builder.show();
							mBaiduMap.hideInfoWindow();
						}
					};
				}
				mInfoWindow = new InfoWindow(button, llInfo, listener);
				mBaiduMap.showInfoWindow(mInfoWindow);
				return true;

			}
		};
		mBaiduMap.setOnMarkerClickListener(onClickListener);

	}

	void Initrute1() {
		PlanNode stNode = PlanNode.withLocation(ll);
		PlanNode enNode = PlanNode.withLocation(ll2);
		mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode).to(
				enNode));
	}

	void Initrute2() {
		PlanNode stNode = PlanNode.withLocation(ll);
		PlanNode enNode = PlanNode.withLocation(ll2);
		mSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode).to(
				enNode));
	}

	// /**
	// * �ڵ����ʾ��
	// *
	// * @param v
	// */
	// public void nodeClick(View v) {
	// if (route == null ||
	// route.getAllStep() == null) {
	// return;
	// }
	// if (nodeIndex == -1 && v.getId() == R.id.pre) {
	// return;
	// }
	// //���ýڵ�����
	// if (v.getId() == R.id.next) {
	// if (nodeIndex < route.getAllStep().size() - 1) {
	// nodeIndex++;
	// } else {
	// return;
	// }
	// } else if (v.getId() == R.id.pre) {
	// if (nodeIndex > 0) {
	// nodeIndex--;
	// } else {
	// return;
	// }
	// }
	// //��ȡ�ڽ����Ϣ
	// LatLng nodeLocation = null;
	// String nodeTitle = null;
	// Object step = route.getAllStep().get(nodeIndex);
	// if (step instanceof DrivingRouteLine.DrivingStep) {
	// nodeLocation = ((DrivingRouteLine.DrivingStep)
	// step).getEntrace().getLocation();
	// nodeTitle = ((DrivingRouteLine.DrivingStep) step).getInstructions();
	// } else if (step instanceof WalkingRouteLine.WalkingStep) {
	// nodeLocation = ((WalkingRouteLine.WalkingStep)
	// step).getEntrace().getLocation();
	// nodeTitle = ((WalkingRouteLine.WalkingStep) step).getInstructions();
	// } else if (step instanceof TransitRouteLine.TransitStep) {
	// nodeLocation = ((TransitRouteLine.TransitStep)
	// step).getEntrace().getLocation();
	// nodeTitle = ((TransitRouteLine.TransitStep) step).getInstructions();
	// }
	//
	// if (nodeLocation == null || nodeTitle == null) {
	// return;
	// }
	// //�ƶ��ڵ�������
	// mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(nodeLocation));
	// // show popup
	// popupText = new TextView(xiangxi.this);
	// popupText.setBackgroundResource(R.drawable.popup);
	// popupText.setTextColor(0xFF000000);
	// popupText.setText(nodeTitle);
	// mBaiduMap.showInfoWindow(new InfoWindow(popupText, nodeLocation, null));
	//
	// }

	void update() {
		Thread upThread = new Thread(new Update());
		upThread.start();
	}

	class Update implements Runnable {
		@Override
		public void run() {

			try {
				// URL�Ϸ���������һ��������֤�����Ƿ���ȷ
				boolean upValidate = policeupdate();
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

				Toast.makeText(getApplicationContext(), "���ܳɹ���",
						Toast.LENGTH_SHORT).show();

				break;
			case 1:
				Toast.makeText(getApplicationContext(), "����ʧ��",
						Toast.LENGTH_SHORT).show();
				break;
			case 2:

				Toast.makeText(getApplicationContext(), "����������ʧ��",
						Toast.LENGTH_SHORT).show();

				break;
			}

		}
	};

	private boolean policeupdate() {
		boolean loginValidate = false;
		// use HTTP
		String urlStr = "http://219.245.67.1:8080/service/servlet/PUpdateServlet";
		HttpPost request = new HttpPost(urlStr);
		// ������ݲ�����Ļ������Զ����ݵĲ������з�װ
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// ����û���������
		params.add(new BasicNameValuePair("policename", policename));
		params.add(new BasicNameValuePair("policephone", policephone));
		params.add(new BasicNameValuePair("ID1", id));
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

	private Handler mHandler = new Handler();
	Runnable runnable = new Runnable() {
		@Override
		public void run() {

			SimpleDateFormat sDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd-HHmmss");
			date = sDateFormat.format(new java.util.Date());

			up();
			abc = "0";
			Toast.makeText(xiangxi.this, "ˢ���߳�", Toast.LENGTH_SHORT).show();
			mHandler.postDelayed(runnable, 10000);

		}
	};

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
				// URL�Ϸ���������һ��������֤�����Ƿ���ȷ
				boolean upValidate1 = upServerplocation();
				Message msg = handler.obtainMessage();
				if (upValidate1) {
					if (responseMsg1.equals("success")) {
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

	private boolean upServerplocation() {
		boolean loginValidate = false;
		// ʹ��apache HTTP�ͻ���ʵ��
		String urlStr = "http://219.245.67.1:8080/service/servlet/PUplocServlet";
		try {
			url1 = new URL(urlStr);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			httpurlconnection1 = (HttpURLConnection) url1.openConnection();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		httpurlconnection1.setDoOutput(true);
		try {
			httpurlconnection1.setRequestMethod("POST");
		} catch (ProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		DataOutputStream dos;
		// �ϴ��û���
		try {
			dos = new DataOutputStream(httpurlconnection1.getOutputStream());
			dos.writeInt(ID);
			dos.writeUTF(abc);
			dos.writeUTF(policename);
			dos.writeUTF(peoplename);
			dos.writeUTF(policephone);
			dos.writeUTF(String.valueOf(weidu1));
			dos.writeUTF(String.valueOf(jingdu1));
			dos.writeUTF(weizhi);
			dos.writeUTF(date);
			dos.flush();
			dos.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			loginValidate = true;
			DataInputStream dis = new DataInputStream(
					httpurlconnection1.getInputStream());

			// ��ȡ����״̬�Լ���������
			if (abc.equals("1")) {
				responseMsg1 = dis.readUTF();
				ID = dis.readInt();
				System.out.println("ID!!!!!!!!!!" + ID);
			} else if (abc.equals("0")) {
				responseMsg1 = dis.readUTF();
				System.out.println("ID@@@@@@@@" + ID);
			} else if (abc.equals("3")) {
				responseMsg1 = dis.readUTF();
				System.out.println("ID######" + ID);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return loginValidate;
	}

	public void onStart() {
		super.onStart();
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMyLocationEnabled(true);
		mLocClient = new LocationClient(xiangxi.this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		// ���ö�λģʽ
		option.setAddrType("all");
		option.setOpenGps(true);// ��gps
		option.setCoorType("bd09ll"); // ������������
		option.setScanSpan(2000);
		option.setPriority(LocationClientOption.GpsFirst);
		mLocClient.setLocOption(option);
		mLocClient.start();
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				mCurrentMode, true, mCurrentMarker));
		myOrientationListener.start();
		if (!mLocClient.isStarted()) {
			mLocClient.start();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	@Override
	public void onStop() {
		mBaiduMap.setMyLocationEnabled(false);
		mLocClient.stop();
		mHandler.removeCallbacks(runnable);
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mLocClient.stop();

		mBaiduMap.setMyLocationEnabled(false);
		mBaiduMap.clear();
		mMapView.onDestroy();
		mMapView = null;

	}

	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(xiangxi.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// ���յ��;�����ַ����壬ͨ�����½ӿڻ�ȡ�����ѯ��Ϣ
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			nodeIndex = -1;
			route = result.getRouteLines().get(0);
			overlay = new MyDrivingRouteOverlay(mBaiduMap);
			routeOverlay = overlay;
			mBaiduMap.setOnMarkerClickListener(overlay);
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
			Initmarketclick();
		}
	}

	@Override
	public void onGetTransitRouteResult(TransitRouteResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(xiangxi.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// ���յ��;�����ַ����壬ͨ�����½ӿڻ�ȡ�����ѯ��Ϣ
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			nodeIndex = -1;
			route = result.getRouteLines().get(0);
			overlay2 = new MyWalkingRouteOverlay(mBaiduMap);
			mBaiduMap.setOnMarkerClickListener(overlay2);
			routeOverlay = overlay2;
			overlay2.setData(result.getRouteLines().get(0));
			overlay2.addToMap();
			overlay2.zoomToSpan();
			Initmarketclick();
		}

	}

	// ����RouteOverly
	private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

		public MyDrivingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
			}
			return null;
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);

			}
			return null;
		}

	}

	private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

		public MyWalkingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
			}
			return null;
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
			}
			return null;
		}
	}

	// �����Զ���Menu
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 1, "·���滮-�ݳ�");
		menu.add(0, 2, 1, "·���滮-����");
		menu.add(0, 3, 1, "ȡ��·���滮");
		// ���ò˵��ļ����¼�
		menu.getItem(0).setOnMenuItemClickListener(
				new OnMenuItemClickListener() {
					public boolean onMenuItemClick(MenuItem item) {
						if (haverote == 0) {
							clearroute();
							Initrute1();
							mCurrentMode = LocationMode.NORMAL;
							mBaiduMap
									.setMyLocationConfigeration(new MyLocationConfiguration(
											mCurrentMode, true, mCurrentMarker));
							requestLocButton.setText("");
							requestLocButton
									.setBackgroundResource(R.drawable.fuyuan);
							requestLocButton.getBackground().setAlpha(200);
							haverote = 1;
						} else {
						}
						return false;
					}
				});
		menu.getItem(1).setOnMenuItemClickListener(
				new OnMenuItemClickListener() {
					public boolean onMenuItemClick(MenuItem item) {
						if (haverote == 0) {
							clearroute();
							Initrute2();
							mCurrentMode = LocationMode.NORMAL;
							mBaiduMap
									.setMyLocationConfigeration(new MyLocationConfiguration(
											mCurrentMode, true, mCurrentMarker));
							requestLocButton.setText("");
							requestLocButton
									.setBackgroundResource(R.drawable.fuyuan);
							requestLocButton.getBackground().setAlpha(200);
							haverote = 1;
						}else{
							
						}
						return false;
					}
				});
		// �˳�·���滮�ļ����¼�
		menu.getItem(2).setOnMenuItemClickListener(
				new OnMenuItemClickListener() {
					public boolean onMenuItemClick(MenuItem item) {
						clearroute();
						haverote = 0;
						return true;
					}
				});

		return true;
	}

	void clearroute() {
		if (overlay != null) {
			overlay.removeFromMap();
		}
		if (overlay2 != null) {
			overlay2.removeFromMap();
		}
	}
}
