package com.query;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import sendupSet.TimeQueryData;
import userMessage.PopupWindowClas;

import com.arrive.DaoJianSQLite;
import com.baishi.db.cn.DBHelper;
import com.baishi.db.cn.FaJianSQLite;
import com.baishihuitong.untils.ListViewCompat;
import com.example.baishihuitong.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DaoJianQueryBytoday extends Activity{
	public ListViewCompat lisview;
	public TextView fajian_number_all;
	public daojian_query_bytoday_adapter adapter;
	public TimeQueryData timeQuery;
	public DBHelper dbHelper;
	private SQLiteDatabase database;
	ArrayList<DaoJianSQLite> daoJianSQLite_today = new ArrayList<DaoJianSQLite>();
	String key_value;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x001) {
				int position = msg.arg1;
				adapter.setpostion(position);
				adapter.notifyDataSetChanged();
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,    
	                WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fajian_query_bytoday);
		final Intent intent = getIntent();
		key_value = intent.getStringExtra("key_intent");
		
		fajian_number_all = (TextView) findViewById(R.id.fajian_number_all);
		
		lisview = (ListViewCompat) findViewById(R.id.fajian_query_listview);
		dbHelper = new DBHelper(this);
		database = dbHelper.getWritableDatabase();
		timeQuery = new TimeQueryData(database);
		
		refresh(0, 10,false);
		adapter = new daojian_query_bytoday_adapter(DaoJianQueryBytoday.this, daoJianSQLite_today);
		lisview.setAdapter(adapter);
		if(!(daoJianSQLite_today.size() > 0)){
			
			Toast.makeText(DaoJianQueryBytoday.this, "没有上传数据", Toast.LENGTH_SHORT).show();
		}
		mListViewRefresh();
		lisview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				int postion = 0;
				if(arg2 > 0){
					postion = arg2 - 1;
				}
				DaoJianSQLite daojian_item = daoJianSQLite_today.get(postion);
				FaJianSQLite fajian_item = new FaJianSQLite(daojian_item.getBillCode(), daojian_item.PreSite, 
						daojian_item.VehicleNo, daojian_item.getScanTime(), daojian_item.getScanDate(), 
						daojian_item.getX_Auth_User(), daojian_item.getX_Auth_Site(),1);
				Message msg = new Message();
				msg.what = 0x001;
				msg.arg1 = postion;
				handler.sendMessage(msg);
				new PopupWindowClas(DaoJianQueryBytoday.this).showPopupWindow(arg1,fajian_item,"到件");
				
			}
		});
	}
	
	public void refresh(int start,int end,boolean flag){
		ArrayList<DaoJianSQLite> daoJianSQLite = new ArrayList<DaoJianSQLite>();
		int count = 0 ;
		if(!flag)
			daoJianSQLite_today.clear();
		SharedPreferences mPreferences = getSharedPreferences("mode", MODE_PRIVATE);
		int unSend_count_msg = 0;
		if(key_value.equals("today_intent")){
			unSend_count_msg = mPreferences.getInt("unSend_count_msg_dao", 0);
			count = (int) timeQuery.getCount("到件", getCurrentTime("yyyy-MM-dd"),1);
			daoJianSQLite = timeQuery.getDaoJianSQLite(getCurrentTime("yyyy-MM-dd"),start,end);
		}else if(key_value.equals("yesterday_intent")){
			unSend_count_msg =  0;
			count = (int) timeQuery.getCount("到件", yesTerDay(),1);
			daoJianSQLite = timeQuery.getDaoJianSQLite(yesTerDay(),start,end);
		}else  if(key_value.equals("befor_yesterday_intent")){
			unSend_count_msg = 0;
			count = (int) timeQuery.getCount("到件", beforeYesterDay(),1);
			daoJianSQLite = timeQuery.getDaoJianSQLite(beforeYesterDay(),start,end);
		}
		
		daoJianSQLite_today.addAll(daoJianSQLite);
		fajian_number_all.setText("数据总数：" + count + "   未上传数据：" + unSend_count_msg);
	}
	public int number_index = 0;
	public void mListViewRefresh() {
		lisview.setPullRefreshEnable(true);
		lisview.setPullLoadEnable(true);
		lisview.setXListViewListener(new ListViewCompat.IXListViewListener() {
			public void onRefresh() {
				lisview.setRefreshTime(getCurrentTime("yyyy-MM-dd HH:mm:ss"));
				refresh(0, 10,false);
				adapter.setList_data(daoJianSQLite_today);
				adapter.notifyDataSetChanged();
			}

			public void onLoadMore() {
				number_index += 10;
				refresh(number_index, 10, true);
				adapter.setList_data(daoJianSQLite_today);
				adapter.notifyDataSetChanged();
				lisview.stopRefresh();
				lisview.stopLoadMore();
			}
		});
	}
	
	// 获取昨天日期
		public String yesTerDay() {
			Format f = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			System.out.println("昨天:" + f.format(c.getTime()));
			c.add(Calendar.DAY_OF_MONTH, -1);
			String time = f.format(c.getTime());
			return time;
		}

		// 获取前天日期
		public String beforeYesterDay() {
			Format f = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			System.out.println("前天:" + f.format(c.getTime()));
			c.add(Calendar.DAY_OF_MONTH, -2);
			String time = f.format(c.getTime());
			return time;
		}
	
	/**
	 * 获得当前时间
	 */
	public String getCurrentTime(String current_time) {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(current_time);
		return formatter.format(date);
	}
}
