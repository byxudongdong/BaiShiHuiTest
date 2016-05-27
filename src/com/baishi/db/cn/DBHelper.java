package com.baishi.db.cn;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	////建表发件
	public static final String USERTABASE_CREATE = "create table BaiShiFajian (_id integer primary key autoincrement, "
			+ "BillCode String unique, " +
			"NextSite String, " +
			"VehicleNo String, " +
			"ScanDate String, " +
			"ScanTime String, "+
			"X_Auth_User String, "+
			"send_up_flag int, "+
			"X_Auth_Site String)";
	//建表到件
	public static final String DATABASE_CREATE = "create table BaiShiDaoJian (_id integer primary key autoincrement, "
			+ "BillCode String unique, " +
			"PreSite String, " +
			"VehicleNo String, " +
			"ScanDate String, " +
			"ScanTime String, "+
			"X_Auth_User String, "+
			"send_up_flag int, "+
			"X_Auth_Site String)";
	
	//建表集包
	public static final String PROFESSRTABASE_CREATE = "create table BaiShiJiBao (_id integer primary key autoincrement, "
			+ "DestinationSite String ,"+
			"BagNumber String ," +
			"BillCode String unique, " +
			"ScanDate  String," +
			"ScanTime  String, "+
			"X_Auth_User String, "+
			"send_up_flag int, "+
			"X_Auth_Site String)";	
	//数据库名字
	public static final String DATABASE_NAME = "baishidatabase";
	//版本号
	private static final int DATABASE_VERSION = 1;
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
		db.execSQL(USERTABASE_CREATE);
		db.execSQL(PROFESSRTABASE_CREATE);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS user");
		onCreate(db);
	}
}
