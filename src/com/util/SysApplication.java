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
//2��Ȼ����ÿ��Acitivity��oncreate���������SysApplication.getInstance().addActivity(this); ��ӵ�ǰAcitivity��ancivitylist����ȥ��ע�⣺ÿ��AcitivityҲ�����������һ��һ���˳�ʱ��������ҳ�棩

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
//		menu.add(0,1,1,"�˳�");
//		return super.onCreateOptionsMenu(menu);
//	}
//	
//	@Override
//		public boolean onOptionsItemSelected(MenuItem item){
//		
//		if(item.getItemId() == 1){
//			AlertDialog isExit = new AlertDialog.Builder(this).create();
//			isExit.setTitle("ϵͳ��ʾ");
//			isExit.setMessage("ȷ��Ҫ�˳���");
//			isExit.setButton("ȷ��", listener);
//			isExit.setButton2("ȡ��",listener);
//			isExit.show();
//		}
//		return false;
//	}
////3����������˳���ʱ�����SysApplication.getInstance().exit();��ֱ�ӹر����е�Acitivity���˳�Ӧ�ó���
//	
////�����Ի�����ߵ�Button�¼�
//	DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
//	{
//		public void onClick(DialogInterface dialog, int which)
//		{
//			switch(which)
//			{
//				case AlertDialog.BUTTON_POSITIVE://ȷ�ϰ�ť���˳�����
//					//finish();
//					//System.exit(0);
//					SysApplication.getInstance().exit();
//				case AlertDialog.BUTTON_NEGATIVE://ȡ����ť��ȡ���Ի���
//					break;
//					default:
//						break;
//			}
//		}
//	};
//}
