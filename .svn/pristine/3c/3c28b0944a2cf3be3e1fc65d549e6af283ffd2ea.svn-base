package com.query;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import sendupSet.TimeQueryData;
import userMessage.GetTime;
import userMessage.PopupWindowClas;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class FajianQueryBytoday extends Activity{
	public ListViewCompat lisview;
	public TextView fajian_number_all;
	public fajian_query_bytoday_adapter adapter;
	public TimeQueryData timeQuery;
	public DBHelper dbHelper;
	private SQLiteDatabase database;
	ArrayList<FaJianSQLite> faJianSQLite_today = new ArrayList<FaJianSQLite>();
	String key_value;
	GetTime time;
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
		time = new GetTime();
		refresh(0, 10,false);
		adapter = new fajian_query_bytoday_adapter(FajianQueryBytoday.this, faJianSQLite_today);
		lisview.setAdapter(adapter);
		if(!(faJianSQLite_today.size() > 0)){
			Toast.makeText(FajianQueryBytoday.this, "没有上传数据", Toast.LENGTH_SHORT).show();
		}
		mListViewRefresh();
		lisview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				int postion = 0;
				if(arg2 > 0){
					postion = arg2 - 1;
				}
				FaJianSQLite fajian_item = faJianSQLite_today.get(postion);
				Message msg = new Message();
				msg.what = 0x001;
				msg.arg1 = postion;
				handler.sendMessage(msg);
				new PopupWindowClas(FajianQueryBytoday.this).showPopupWindow(arg1,fajian_item,"发件");
			}
		});
	}
	
	public void refresh(int start,int end,boolean flag){
		ArrayList<FaJianSQLite> faJianSQLite = new ArrayList<FaJianSQLite>();
		int count = 0 ;
		if(!flag)
			faJianSQLite_today.clear();
		SharedPreferences mPreferences = getSharedPreferences("mode", MODE_PRIVATE);
		int unSend_count_msg = 0;
		if(key_value.equals("today_intent")){
			unSend_count_msg = mPreferences.getInt("unSend_count_msg_fajian", 0);
			count = (int) timeQuery.getCount("发件", time.getCurrentTime("yyyy-MM-dd"),1);
			faJianSQLite = timeQuery.getFaJianSQLite(time.getCurrentTime("yyyy-MM-dd"),start,end);
		}else if(key_value.equals("yesterday_intent")){
			unSend_count_msg = 0;
			count = (int) timeQuery.getCount("发件", time.yesTerDay(),1);
			faJianSQLite = timeQuery.getFaJianSQLite(time.yesTerDay(),start,end);
		}else  if(key_value.equals("befor_yesterday_intent")){
			unSend_count_msg = 0;
			count = (int) timeQuery.getCount("发件", time.beforeYesterDay(),1);
			faJianSQLite = timeQuery.getFaJianSQLite(time.beforeYesterDay(),start,end);
		}
		
		faJianSQLite_today.addAll(faJianSQLite);
		fajian_number_all.setText("数据总数：" + count + "   未上传数据：" + unSend_count_msg);
	}
	
	public int number_index = 0;
	public void mListViewRefresh() {
		lisview.setPullRefreshEnable(true);
		lisview.setPullLoadEnable(true);
		lisview.setXListViewListener(new ListViewCompat.IXListViewListener() {
			public void onRefresh() {
				lisview.setRefreshTime(time.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
				
				refresh(0, 10,false);
				adapter.setList_data(faJianSQLite_today);
				adapter.notifyDataSetChanged();
			}

			public void onLoadMore() {
				number_index += 10;
				refresh(number_index, 10, true);
				adapter.setList_data(faJianSQLite_today);
				adapter.notifyDataSetChanged();
				lisview.stopRefresh();
				lisview.stopLoadMore();
			}
		});
	}
	
}
