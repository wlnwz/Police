package com.util;

import java.util.LinkedList;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class SysApplication extends Application{
	private List<Activity> mList = new LinkedList<Activity>(); 
	private static SysApplication instance;
	
	private SysApplication()
	{
	}
	public synchronized static SysApplication getInstance()
	{
		if(null == instance)
		{
			instance = new SysApplication();
		}
		return instance;
	}
	//add Activity
	public void addActivity(Activity activity)
	{
		mList.add(activity);
	}
	
	public void exit()
	{
		try{
			for (Activity activity : mList)
			{
				if(activity != null)
					activity.finish();
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			System.exit(0);
		}
	}
	public void onLowMemory(){
		super.onLowMemory();
		System.gc();
	}
}
//2、然后在每个Acitivity的oncreate方法里面过SysApplication.getInstance().addActivity(this); 添加当前Acitivity到ancivitylist里面去（注意：每个Acitivity也就是这个界面一步一步退出时，经过的页面）

//public class after_service extends Activity{
//	@Override
//	protected void onCreate(Bundle savedInstanceState){
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.after_service);
//		
//		SysApplication.getInstance().addActivity(this);
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		menu.add(0,1,1,"退出");
//		return super.onCreateOptionsMenu(menu);
//	}
//	
//	@Override
//		public boolean onOptionsItemSelected(MenuItem item){
//		
//		if(item.getItemId() == 1){
//			AlertDialog isExit = new AlertDialog.Builder(this).create();
//			isExit.setTitle("系统提示");
//			isExit.setMessage("确定要退出吗");
//			isExit.setButton("确定", listener);
//			isExit.setButton2("取消",listener);
//			isExit.show();
//		}
//		return false;
//	}
////3、最后在想退出的时候调用SysApplication.getInstance().exit();可直接关闭所有的Acitivity并退出应用程序。
//	
////监听对话框里边的Button事件
//	DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
//	{
//		public void onClick(DialogInterface dialog, int which)
//		{
//			switch(which)
//			{
//				case AlertDialog.BUTTON_POSITIVE://确认按钮，退出程序
//					//finish();
//					//System.exit(0);
//					SysApplication.getInstance().exit();
//				case AlertDialog.BUTTON_NEGATIVE://取消按钮，取消对话框
//					break;
//					default:
//						break;
//			}
//		}
//	};
//}
