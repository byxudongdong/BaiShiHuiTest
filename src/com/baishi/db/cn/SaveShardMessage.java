package com.baishi.db.cn;

import com.arrive.DaoJianSQLite;
import com.packages.JiBaoSQLite;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SaveShardMessage {
	public Context context;
	public SQLiteDatabase database;

	public SaveShardMessage(SQLiteDatabase database) {
		super();
		this.database = database;
	}
	public SaveShardMessage(Context context) {
		super();
		this.context = context;
	}
	
	/**
	 * 数据库
	 * @param accept_msg
	 */
	public long saveShardFaJianMessage(FaJianSQLite accept_msg) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("BillCode", accept_msg.getBillCode());
		contentvalues.put("NextSite", accept_msg.getNextSite());
		contentvalues.put("VehicleNo", accept_msg.getVehicleNo());
		contentvalues.put("ScanDate", accept_msg.getScanDate());
		contentvalues.put("ScanTime", accept_msg.getScanTime());
		contentvalues.put("X_Auth_User", accept_msg.getX_Auth_User().replace(".", "_"));
		contentvalues.put("X_Auth_Site", accept_msg.getX_Auth_Site());
		contentvalues.put("send_up_flag", accept_msg.getSend_up_flag());
		
		long insert = database.insert("BaiShiFajian", null, contentvalues);
		return insert;
	}
	
	public void deleShardFaJian(int position){
		database.delete("BaiShiFajian", "_id=?", new String[] { position
				+ "" });
	}
	
	public void deleShardDaoJian(int position){
		database.delete("BaiShiDaoJian", "_id=?", new String[] { position
				+ "" });
	}
	
	public void deleShardJiBao(int position){
		database.delete("BaiShiJiBao", "_id=?", new String[] { position
				+ "" });
	}
	
	public void deleTableFaJian(){
		database.execSQL("drop table if exists BaiShiFajian");
		database.execSQL(DBHelper.USERTABASE_CREATE);
	}
	
	public void deleTableDaoJian(){
		database.execSQL("drop table if exists BaiShiDaoJian");
		database.execSQL(DBHelper.DATABASE_CREATE);
	}
	
	public void deleTableJiBao(){
		database.execSQL("drop table if exists BaiShiJiBao");
		database.execSQL(DBHelper.PROFESSRTABASE_CREATE);
	}
	
	public long saveShardDaoJianMessage(DaoJianSQLite accept_msg) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("BillCode", accept_msg.getBillCode());
		contentvalues.put("PreSite", accept_msg.getPreSite());
		contentvalues.put("VehicleNo", accept_msg.getVehicleNo());
		contentvalues.put("ScanDate", accept_msg.getScanDate());
		contentvalues.put("ScanTime", accept_msg.getScanTime());
		contentvalues.put("X_Auth_User", accept_msg.getX_Auth_User().replace(".", "_"));
		contentvalues.put("X_Auth_Site", accept_msg.getX_Auth_Site());
		contentvalues.put("send_up_flag", accept_msg.getSend_up_flag());
		
		long insert = database.insert("BaiShiDaoJian", null, contentvalues);
		return insert;
		
	}
	
	public long saveShardJiBaoMessage(JiBaoSQLite accept_msg) {
		ContentValues contentvalues = new ContentValues();
		contentvalues.put("BillCode", accept_msg.getBillCode());
		contentvalues.put("DestinationSite", accept_msg.getDestinationSite());
		contentvalues.put("BagNumber", accept_msg.getBagNumber());
		contentvalues.put("ScanDate", accept_msg.getScanDate());
		contentvalues.put("ScanTime", accept_msg.getScanTime());
		contentvalues.put("X_Auth_User", accept_msg.getX_Auth_User().replace(".", "_"));
		contentvalues.put("X_Auth_Site", accept_msg.getX_Auth_Site());
		contentvalues.put("send_up_flag", accept_msg.getSend_up_flag());
		long insert = database.insert("BaiShiJiBao", null, contentvalues);
		return insert;
	}
	public static String TOKEN = "";
	public static void setLogin_token(String token){
		TOKEN = token;
	}
	/**
	 * 获取数据库条码数量
	 * @param actionBarName
	 * @return
	 */

	public long getCount(String actionBarName){
		String dataBase_name = "";
		if (actionBarName.equals("发件")) {
			dataBase_name = "BaiShiFajian";
		} else if (actionBarName.equals("到件")) {
			dataBase_name = "BaiShiDaoJian";
		} else if (actionBarName.equals("集包")) {
			dataBase_name = "BaiShiJiBao";
		}
		Cursor cursor =database.rawQuery("select count(*) from " + dataBase_name, null);
		cursor.moveToFirst();
		long reslut=cursor.getLong(0);
		return reslut;
	}
	
	/**
	 *数据上传方式
	 * 
	 */
	public void Sharedchange_sendup_flag(int change_sendup_flag) {
		SharedPreferences settings = context.getSharedPreferences(
				"change_sendup_flag", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("change_sendup_flag", change_sendup_flag);
		editor.commit();
	}
	/**
	 * 数据上传方式
	 * 手动0,自动1
	 */
	public static int change_sendup_flag = 1;
	public static int getchange_sendup_flag(Context activity){
		SharedPreferences acceptMessage = activity.getSharedPreferences("change_sendup_flag", Activity.MODE_PRIVATE);
		change_sendup_flag = acceptMessage.getInt("change_sendup_flag", 1);
		return change_sendup_flag;
	}

	/**
	 * 条数阀值
	 * 
	 * @param reversePolarity
	 */
	public void Sharedchange_count_flag(int change_count_flag) {
		SharedPreferences settings = context.getSharedPreferences(
				"change_count_flag", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("change_count_flag", change_count_flag);
		editor.commit();
	}
	
	/**
	 * 条数阀值
	 * 
	 */
	public static int change_count_flag = 100;
	public static int getchange_count_flag(Context activity){
		SharedPreferences acceptMessage = activity.getSharedPreferences("change_count_flag", Activity.MODE_PRIVATE);
		change_count_flag = acceptMessage.getInt("change_count_flag", 100);
		return change_count_flag;
	}

	/**
	 * 延迟时间
	 */
	public void Sharedchange_time_flag(int change_time_flag) {
		SharedPreferences settings = context.getSharedPreferences(
				"change_time_flag", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("change_time_flag", change_time_flag);
		editor.commit();
	}


	/**
	 * 延迟时间
	 */
	public static int change_time_flag = 7;
	public static int getchange_time_flag(Context activity){
		SharedPreferences acceptMessage = activity.getSharedPreferences("change_time_flag", Activity.MODE_PRIVATE);
		change_time_flag = acceptMessage.getInt("change_time_flag", 7);
		return change_time_flag;
	}
	
	/**
	 * 服务器设置
	 */
	public static String system_service_flag = "http://opendev.appl.800best.com/hscedev";
//	public static String system_service_flag = "http://handset2.appl.800best.com/hyco";
	
	public void Sharedsystem_service_flag(String value) {
		SharedPreferences settings = context.getSharedPreferences(
				"change_time_flag", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("system_service_flag", value);
		editor.commit();
	}
	public static String getsystem_service_flag(Context activity){
		SharedPreferences acceptMessage = activity.getSharedPreferences("change_time_flag", Activity.MODE_PRIVATE);
		system_service_flag = acceptMessage.getString("system_service_flag", system_service_flag);
		return system_service_flag;
	}
}
