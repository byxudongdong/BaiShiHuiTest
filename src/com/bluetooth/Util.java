package com.bluetooth;

import java.util.ArrayList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Util
{
	private SQLiteDatabase database;
	public Util(SQLiteDatabase database) {
		// TODO Auto-generated constructor stub
		this.database = database;
	}

	/** 记录本地存放的用户数 */
	public static int userNum = 0;
	public static String USER_NAME = "";
	/** 获得本地用户信息链表 */
	public static ArrayList<String> getLocalUserInfoList(Activity activity)
	{
		ArrayList<String> result = new ArrayList<String>();

		SharedPreferences settings = activity.getSharedPreferences("setting", Activity.MODE_PRIVATE);
		userNum = settings.getInt("userNum", 0);
//		USER_NAME = settings.getString("user_name" , "");
		for(int i=0; i<userNum; i++)
		{
			String name = settings.getString("name_" + i, "");
			result.add(name);
		}

		return result;
	}
	
	/**
	 * 获取数据库条码数量
	 * @param actionBarName
	 * @return
	 */

	public long getCount(String actionBarName){
		String dataBase_name = "";
		if (actionBarName.equals("收件扫描")) {
			dataBase_name = "Shoujian";
		} else if (actionBarName.equals("发件扫描")) {
			dataBase_name = "Fajian";
		} else if (actionBarName.equals("到件扫描")) {
			dataBase_name = "Daojian";
		} else if (actionBarName.equals("派件扫描")) {
			dataBase_name = "Paijian";
		} else if (actionBarName.equals("签收扫描")) {
			dataBase_name = "Qianshou";
		}
		Cursor cursor =database.rawQuery("select count(*) from " + dataBase_name, null);
		cursor.moveToFirst();
		long reslut=cursor.getLong(0);
		return reslut;
	}
	//=============自动上传==============
		public static boolean uploading;//自动上传标记
		public static boolean BLE_OPEN_FLAGE = false;//蓝牙开启标记
		public static boolean DIALOG_VIEW_SHOE = false;//搜索服务提示帧布局显示标记
		public static BluetoothLeService mBluetoothLeService = null;
		public static boolean getUploadingMessage(Activity activity){
			SharedPreferences acceptMessage = activity.getSharedPreferences("AUTO_uploading", Activity.MODE_PRIVATE);
			uploading = acceptMessage.getBoolean("uploading", false);
			return uploading;
		}

		public static boolean motion_connect = true;//自动连接标记
		public static int thousand = 1000;//以毫秒表示时间单位秒
		public static int motion_time_divide = 3 * thousand;//自动连接间隔时间
		public static boolean getmotion_connect(Activity activity){
			SharedPreferences acceptMessage = activity.getSharedPreferences("motion_connect", Activity.MODE_PRIVATE);
			motion_connect = acceptMessage.getBoolean("motion_connect", true);
			return motion_connect;

		}

		public static int getmotion_time_divide(Activity activity){
			SharedPreferences acceptMessage = activity.getSharedPreferences("motion_time_divide", Activity.MODE_PRIVATE);
			motion_time_divide = acceptMessage.getInt("motion_time_divide", motion_time_divide);
			return motion_time_divide;

		}
		
		public static boolean motion_sleep_divide = true;
		public static boolean getmotion_sleep_divide(Activity activity){
			SharedPreferences acceptMessage = activity.getSharedPreferences("motion_sleep_divide", Activity.MODE_PRIVATE);
			motion_sleep_divide = acceptMessage.getBoolean("motion_sleep_divide", true);
			return motion_sleep_divide;

		}

		public static String power_show = "0";
}
