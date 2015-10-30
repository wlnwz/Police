package com.util;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;


public class location {
	 
	public static LocationClient mLocClient;
	private static boolean isFirstLoc = true;
	private static MyLocationListenner myListener = new MyLocationListenner();
	static String city;
	String city123;
	public static double weidu;
	public static double jingdu;
	
	public  static void InIt() {
		LocationClientOption option = new LocationClientOption();
		
		
		option.setAddrType("all");
		option.setOpenGps(true);
		option.setCoorType("bd09ll"); 
		option.setScanSpan(2000);
		option.setPriority(LocationClientOption.GpsFirst);
		mLocClient.setLocOption(option);
		mLocClient.registerLocationListener(myListener);
		
		mLocClient.start();
		
	}
	 
	
	
	public static class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null )
				return;
			if (isFirstLoc) {
				isFirstLoc = false;		
				weidu = location.getLatitude();
				jingdu = location.getLongitude();
				 city = location.getCity();
			
			}
		//	mLocClient.stop();
		}			
		
		public void onReceivePoi(BDLocation poiLocation) {
		}

	}
	public String  get(){
		return city;
		
	}

}
