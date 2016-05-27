package sendupSet;

import java.util.ArrayList;

import com.arrive.DaoJianSQLite;
import com.baishi.db.cn.FaJianSQLite;
import com.packages.JiBaoSQLite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TimeQueryData {
	private String WHERE;
	private SQLiteDatabase database;

	private static final int FAJIAN = 0;
	private static final int DAOJIAN = 1;
	private static final int JIBAO = 2;

	public ArrayList<FaJianSQLite> faJianSQLite = new ArrayList<FaJianSQLite>();
	public ArrayList<DaoJianSQLite> daoJianSQLite = new ArrayList<DaoJianSQLite>();
	public ArrayList<JiBaoSQLite> jiBaoSQLite = new ArrayList<JiBaoSQLite>();
	public ArrayList<Integer> delete_batch = new ArrayList<Integer>();
	
	public TimeQueryData(SQLiteDatabase database) {
		super();
		this.database = database;
	}

	public int getCount(String actionBarName,String time,int flag){
		String dataBase_name = "";
		if (actionBarName.equals("发件")) {
			dataBase_name = "BaiShiFajian";
		} else if (actionBarName.equals("到件")) {
			dataBase_name = "BaiShiDaoJian";
		} else if (actionBarName.equals("集包")) {
			dataBase_name = "BaiShiJiBao";
		}
		Cursor cursor =database.rawQuery(
				"select * from "+ dataBase_name +" WHERE ScanDate=? order by _id",
				new String[] { time});
		int reslut = 0;
		delete_batch.clear();
		while (cursor.moveToNext()) {
//			cursor.moveToLast();
//			reslut=cursor.getLong(0);
			if(flag == 0){
				int uid2 = cursor.getInt(cursor.getColumnIndex("_id"));
				delete_batch.add(uid2);
			}
			
			reslut ++; 
		}
//		cursor.moveToFirst();
//		long reslut=cursor.getLong(0);
		return reslut;
	}
	
	public void delete_batch(String actionBarName){
		if (actionBarName.equals("发件")) {
			for(int index:delete_batch){
				deleShardFaJian(index);
			}
			
		} else if (actionBarName.equals("到件")) {
			for(int index:delete_batch){
				deleShardDaoJian(index);
			}
		} else if (actionBarName.equals("集包")) {
			for(int index:delete_batch){
				deleShardJiBao(index);
			}
		}
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
	public ArrayList<FaJianSQLite> getFaJianSQLite(String time, int start,
			int end) {
		getCursor(time, FAJIAN, start, end);
		return faJianSQLite;
	}

	public ArrayList<DaoJianSQLite> getDaoJianSQLite(String time, int start,
			int end) {
		getCursor(time, DAOJIAN, start, end);
		return daoJianSQLite;
	}

	public ArrayList<JiBaoSQLite> getJiBaoSQLite(String time, int start, int end) {
		getCursor(time, JIBAO, start, end);
		return jiBaoSQLite;
	}

	public void getCursor(String time, int flage, int start, int end) {
		if (flage == FAJIAN) {
			Cursor cursor = database
					.rawQuery(
							"select * from BaiShiFajian WHERE ScanDate=? order by _id asc limit ?,?",
							new String[] { time, String.valueOf(start),
									String.valueOf(end) });
			CursormoveToNext(cursor, FAJIAN);
		} else if (flage == DAOJIAN) {
			Cursor cursor = database
					.rawQuery(
							"select * from BaiShiDaoJian WHERE ScanDate=? order by _id asc limit ?,?",
							new String[] { time, String.valueOf(start),
									String.valueOf(end) });
			CursormoveToNext(cursor, DAOJIAN);
		} else if (flage == JIBAO) {
			Cursor cursor = database
					.rawQuery(
							"select * from BaiShiJiBao WHERE ScanDate=? order by _id asc limit ?,?",
							new String[] { time, String.valueOf(start),
									String.valueOf(end) });
			CursormoveToNext(cursor, JIBAO);
		}
	}

	public void getUnSend_cusor(String send_up_flag, int flage){
		if (flage == FAJIAN) {
			Cursor cursor = database
					.rawQuery(
							"select * from BaiShiFajian WHERE send_up_flag=? order by _id asc limit ?,?",
							new String[] { send_up_flag});
			CursormoveToNext(cursor, FAJIAN);
		} else if (flage == DAOJIAN) {
			Cursor cursor = database
					.rawQuery(
							"select * from BaiShiDaoJian WHERE send_up_flag=? order by _id asc limit ?,?",
							new String[] { send_up_flag });
			CursormoveToNext(cursor, DAOJIAN);
		} else if (flage == JIBAO) {
			Cursor cursor = database
					.rawQuery(
							"select * from BaiShiJiBao WHERE send_up_flag=? order by _id asc limit ?,?",
							new String[] { send_up_flag});
			CursormoveToNext(cursor, JIBAO);
		}
	}
	
	
	public void CursormoveToNext(Cursor cursor, int flag) {
		if (flag == FAJIAN) {
			CursorFaJianmoveToNext(cursor);
		} else if (flag == DAOJIAN) {
			CursorDaoJianmoveToNext(cursor);
		} else if (flag == JIBAO) {
			CursorJiBaomoveToNext(cursor);
		}
	}

	public void CursorFaJianmoveToNext(Cursor cursor) {
		faJianSQLite.clear();
		while (cursor.moveToNext()) {
			fajian_cusor(cursor);
		}
		cursor.close();
	}

	public void fajian_cusor(Cursor cursor) {
		int uid2 = cursor.getInt(cursor.getColumnIndex("_id"));
		String billCode = cursor.getString(cursor.getColumnIndex("BillCode"));
		String nextSite = cursor.getString(cursor.getColumnIndex("NextSite"));
		String scanTime = cursor.getString(cursor.getColumnIndex("ScanTime"));
		String scanDate = cursor.getString(cursor.getColumnIndex("ScanDate"));
		String vehicleNo = cursor.getString(cursor.getColumnIndex("VehicleNo"));
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

	public void CursorDaoJianmoveToNext(Cursor cursor) {
		daoJianSQLite.clear();
		while (cursor.moveToNext()) {
			int uid2 = cursor.getInt(cursor.getColumnIndex("_id"));
			String billCode = cursor.getString(cursor
					.getColumnIndex("BillCode"));
			String preSite = cursor.getString(cursor.getColumnIndex("PreSite"));
			String scanTime = cursor.getString(cursor
					.getColumnIndex("ScanTime"));
			String scanDate = cursor.getString(cursor
					.getColumnIndex("ScanDate"));
			String vehicleNo = cursor.getString(cursor
					.getColumnIndex("VehicleNo"));
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
			user.setX_Auth_User(x_Auth_User.replace("_", "."));
			user.setX_Auth_Site(x_Auth_Site);
			daoJianSQLite.add(user);
		}
		cursor.close();
	}

	public void CursorJiBaomoveToNext(Cursor cursor) {
		jiBaoSQLite.clear();
		while (cursor.moveToNext()) {
			int uid2 = cursor.getInt(cursor.getColumnIndex("_id"));
			String billCode = cursor.getString(cursor
					.getColumnIndex("BillCode"));
			String preSite = cursor.getString(cursor
					.getColumnIndex("DestinationSite"));
			String bagNumber = cursor.getString(cursor
					.getColumnIndex("BagNumber"));
			String scanDate = cursor.getString(cursor
					.getColumnIndex("ScanDate"));
			String scanTime = cursor.getString(cursor
					.getColumnIndex("ScanTime"));
			String x_Auth_User = cursor.getString(cursor
					.getColumnIndex("X_Auth_User"));
			String x_Auth_Site = cursor.getString(cursor
					.getColumnIndex("X_Auth_Site"));
			JiBaoSQLite user = new JiBaoSQLite();

			user.setBillCode(billCode.replace("@", ""));
			user.setDestinationSite(preSite);
			user.setScanTime(scanTime);
			user.setScanDate(scanDate);
			user.setBagNumber(bagNumber);
			user.setIndex(uid2);
			user.setX_Auth_User(x_Auth_User.replace("_", "."));
			user.setX_Auth_Site(x_Auth_Site);
			jiBaoSQLite.add(user);
		}
		cursor.close();
	}

	public boolean setFajianCursor(String number_value) {
		faJianSQLite.clear();
		Cursor cursor = database.rawQuery(
				"select * from BaiShiFajian WHERE BillCode=? order by _id",
				new String[] { number_value });
		CursormoveToNext(cursor, FAJIAN);
		if (faJianSQLite.size() > 0) {
			return false;
		}
		return true;
	}

	public boolean setDaojianCursor(String number_value) {
		daoJianSQLite.clear();
		Cursor cursor = database.rawQuery(
				"select * from BaiShiDaojian WHERE BillCode=? order by _id",
				new String[] { number_value });
		CursormoveToNext(cursor, DAOJIAN);
		if (daoJianSQLite.size() > 0) {
			return false;
		}
		return true;
	}

	public boolean setJiBaoCursor(String number_value) {
		jiBaoSQLite.clear();
		Cursor cursor = database.rawQuery(
				"select * from BaiShiJiBao WHERE BillCode=? order by _id",
				new String[] { number_value });
		CursormoveToNext(cursor, JIBAO);
		if (jiBaoSQLite.size() > 0) {
			return false;
		}
		
		return true;
	}
}
