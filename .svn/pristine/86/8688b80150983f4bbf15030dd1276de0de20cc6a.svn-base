package com.bluetooth;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySqlite extends SQLiteOpenHelper {
	public static String SHOU_JIAN = "CREATE TABLE Shoujian("
			+ "_id integer primary key autoincrement,"
			+ "acceptMessageTime String," 
			+ "acceptMessage String,"
			+ "acceptMessageMan String,"
			+ "acceptMessageBoolean String)";
	public static String FA_JIAN = "CREATE TABLE Fajian("
			+"_id integer primary key autoincrement,"
			+"sendMessageTime String," 
			+"sendMessageNumber String,"
			+ "sendMessageNext String,"
			+ "sendMessageBoolean String)";
	public static String DAO_JIAN = "CREATE TABLE Daojian("
			+"_id integer primary key autoincrement,"
			+ "arriveMessageTime String," 
			+ "arriveMessageNumber String,"
			+ "arriveMessagePre String,"
			+ "arriveMessageBoolean String)";
	public static String PAI_JIAN = "CREATE TABLE Paijian("
			+"_id integer primary key autoincrement,"
			+"pieceMessageTime String," +
			"pieceMessageNumber String,"
			+ "pieceMessageMan String,"
			+ "pieceMessageBoolean String)";
	public static String QIAN_SHOU = "CREATE TABLE Qianshou("
			+"_id integer primary key autoincrement,"
			+"signMessageTime String," +
			"signMessageNumber String," +
			"signMessageImgPath String,"
			+ "signMessageMan String,"
			+ "signMessageBoolean String)";
	
	
	public MySqlite(Context context) {
		super(context, "haozhuang.db", null, 1);
	}
		@Override
		public void onCreate(SQLiteDatabase haozhuang) {
			haozhuang.execSQL(SHOU_JIAN);
			haozhuang.execSQL(FA_JIAN);
			haozhuang.execSQL(DAO_JIAN);
			haozhuang.execSQL(PAI_JIAN);
			haozhuang.execSQL(QIAN_SHOU);		
		}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public boolean deleteDatabase(Context context) {
		return context.deleteDatabase("haozhuang.db");
	}
	
}
