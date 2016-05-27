package com.baishi.db.cn;

import java.util.ArrayList;

import com.arrive.DaoJianSQLite;
import com.packages.JiBaoSQLite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class GetSaveDabaseSQ {
	public SQLiteDatabase database;

	public GetSaveDabaseSQ(SQLiteDatabase database) {
		super();
		this.database = database;
	}
	public  ArrayList<FaJianSQLite> getFajianData(SQLiteDatabase database,int current_num, int get_count,boolean pullUP2lod){
		ArrayList<FaJianSQLite> faJianSQLite=new ArrayList<FaJianSQLite>();
		int index_start = current_num;
		int count = get_count;
		faJianSQLite.clear();
		if(pullUP2lod){
			if(current_num >= 0 && current_num < get_count){
				index_start = 0;
				count = current_num;
			}
			else{
				index_start = current_num - get_count;
			}
		}else{
			if(current_num > get_count){
				index_start = current_num - get_count;
			}else{
				index_start = 0;
				count = current_num;
			}
		}
		
		Cursor cursor =database.rawQuery("select * from BaiShiFajian WHERE _id>? and send_up_flag = 0", new String[]{String.valueOf(index_start)});
		while(cursor.moveToNext()){
			int uid2=cursor.getInt(cursor.getColumnIndex("_id"));
			String billCode=cursor.getString(cursor.getColumnIndex("BillCode"));
			String nextSite=cursor.getString(cursor.getColumnIndex("NextSite"));
			String scanTime=cursor.getString(cursor.getColumnIndex("ScanTime"));
			String scanDate = cursor.getString(cursor.getColumnIndex("ScanDate"));
			String vehicleNo=cursor.getString(cursor.getColumnIndex("VehicleNo"));
			String x_Auth_User = cursor.getString(cursor
					.getColumnIndex("X_Auth_User"));
			String x_Auth_Site = cursor.getString(cursor
					.getColumnIndex("X_Auth_Site"));

			FaJianSQLite user = new FaJianSQLite();

			user.setBillCode(billCode.replace("@", ""));
			user.setNextSite(nextSite);
			user.setScanTime(scanTime);
			user.setScanDate(scanDate);
			user.setVehicleNo(vehicleNo);
			user.setIndex(uid2);
			user.setX_Auth_User(x_Auth_User.replace("_", "."));
			user.setX_Auth_Site(x_Auth_Site);
			faJianSQLite.add(user);
		}
		cursor.close();
		return faJianSQLite;
	}
	
	public  ArrayList<DaoJianSQLite> getDaojianData(SQLiteDatabase database,int current_num, int get_count,boolean pullUP2lod){
		ArrayList<DaoJianSQLite> daoJianSQLite=new ArrayList<DaoJianSQLite>();
		int index_start = current_num;
		int count = get_count;
		daoJianSQLite.clear();
		if(pullUP2lod){
			if(current_num >= 0 && current_num < get_count){
				index_start = 0;
				count = current_num;
			}
			else{
				index_start = current_num - get_count;
			}
		}else{
			if(current_num > get_count){
				index_start = current_num - get_count;
			}else{
				index_start = 0;
				count = current_num;
			}
		}
		Cursor cursor =database.rawQuery("select * from BaiShiDaojian WHERE _id>?", new String[]{String.valueOf(index_start)});
		while(cursor.moveToNext()){
			int uid2=cursor.getInt(cursor.getColumnIndex("_id"));
			String billCode=cursor.getString(cursor.getColumnIndex("BillCode"));
			String preSite=cursor.getString(cursor.getColumnIndex("PreSite"));
			String scanTime=cursor.getString(cursor.getColumnIndex("ScanTime"));
			String scanDate = cursor.getString(cursor.getColumnIndex("ScanDate"));
			String vehicleNo=cursor.getString(cursor.getColumnIndex("VehicleNo"));
			String x_Auth_User = cursor.getString(cursor
					.getColumnIndex("X_Auth_User"));
			String x_Auth_Site = cursor.getString(cursor
					.getColumnIndex("X_Auth_Site"));

			DaoJianSQLite user = new DaoJianSQLite();
			user.setBillCode(billCode.replace("@", ""));
			user.setPreSite(preSite);
			user.setScanTime(scanTime);
			user.setScanDate(scanDate);
			user.setVehicleNo(vehicleNo);
			user.setIndex(uid2);
			user.setX_Auth_Site(x_Auth_Site);
			user.setX_Auth_User(x_Auth_User.replace("_", "."));
			daoJianSQLite.add(user);
		}
		cursor.close();
		return daoJianSQLite;
	}
	
	public  ArrayList<JiBaoSQLite> getJiBaoData(SQLiteDatabase database,int current_num, int get_count,boolean pullUP2lod){
		ArrayList<JiBaoSQLite> jibaoSQLite=new ArrayList<JiBaoSQLite>();
		int index_start = current_num;
		int count = get_count;
		jibaoSQLite.clear();
		if(pullUP2lod){
			if(current_num >= 0 && current_num < get_count){
				index_start = 0;
				count = current_num;
			}
			else{
				index_start = current_num - get_count;
			}
		}else{
			if(current_num > get_count){
				index_start = current_num - get_count;
			}else{
				index_start = 0;
				count = current_num;
			}
		}
		Cursor cursor =database.rawQuery("select * from BaiShiJiBao WHERE _id>?", new String[]{String.valueOf(index_start)});
		while(cursor.moveToNext()){
			int uid2=cursor.getInt(cursor.getColumnIndex("_id"));
			String billCode=cursor.getString(cursor.getColumnIndex("BillCode"));
			String preSite=cursor.getString(cursor.getColumnIndex("DestinationSite"));
			String scanTime=cursor.getString(cursor.getColumnIndex("ScanTime"));
			String scanDate = cursor.getString(cursor.getColumnIndex("ScanDate"));
			String vehicleNo=cursor.getString(cursor.getColumnIndex("BagNumber"));
			String x_Auth_User = cursor.getString(cursor
					.getColumnIndex("X_Auth_User"));
			String x_Auth_Site = cursor.getString(cursor
					.getColumnIndex("X_Auth_Site"));
			JiBaoSQLite user = new JiBaoSQLite();
			user.setBillCode(billCode.replace("@", ""));
			user.setDestinationSite(preSite);
			user.setScanTime(scanTime);
			user.setScanDate(scanDate);
			user.setBagNumber(vehicleNo);
			user.setIndex(uid2);
			user.setX_Auth_Site(x_Auth_Site);
			user.setX_Auth_User(x_Auth_User.replace("_", "."));
			jibaoSQLite.add(user);
		}
		cursor.close();
		return jibaoSQLite;
	}

}
