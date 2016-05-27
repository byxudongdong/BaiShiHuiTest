package com.example.baishihuitong;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import sendupSet.TimeQueryData;

import com.baishi.db.cn.DBHelper;
import com.delet.data.DeleScanDataActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MenuMainActivity extends Activity implements OnClickListener {
	public Button yewubutton, delectscanbutton, setingbutton,
			Shuttle_bus_back_button;
	public TimeQueryData timeQuery;
	public DBHelper dbHelper;
	private SQLiteDatabase database;
	public int all_unsend_count;
	public TextView mainmenu_send_number_count;
	private SharedPreferences mPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mainmenu_activity);
		find_view();
		dbHelper = new DBHelper(this);
		database = dbHelper.getWritableDatabase();
		timeQuery = new TimeQueryData(database);
		mPreferences = getSharedPreferences("mode", MODE_PRIVATE);
		mPreferences.edit().putInt("all_number_today_fajian", 0).commit();
		
		all_unsend_count = getAllunsendCount();

		mainmenu_send_number_count.setText(all_unsend_count + "票" + mPreferences.getInt("all_number_fajian", 0));
	}

	public int getAllunsendCount() {
		SharedPreferences mPreferences = getSharedPreferences("mode", MODE_PRIVATE);
		int unSend_count_msg_fajian = mPreferences.getInt(
				"unSend_count_msg_fajian", 0);
		int unSend_count_msg_daojian = mPreferences.getInt("unSend_count_msg_daojian",
				0);
		int unSend_count_msg_jibao = mPreferences.getInt("unSend_count_msg_jibao",
				0);
		return unSend_count_msg_fajian + unSend_count_msg_daojian
				+ unSend_count_msg_jibao;
	}

	public void find_view() {
		yewubutton = (Button) findViewById(R.id.yewubutton);
		delectscanbutton = (Button) findViewById(R.id.delectscanbutton);
		setingbutton = (Button) findViewById(R.id.setingbutton);
		Shuttle_bus_back_button = (Button) findViewById(R.id.Shuttle_bus_back_button);
		mainmenu_send_number_count = (TextView) findViewById(R.id.mainmenu_send_number_count);
		yewubutton.setOnClickListener(this);
		delectscanbutton.setOnClickListener(this);
		setingbutton.setOnClickListener(this);
		Shuttle_bus_back_button.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		all_unsend_count = getAllunsendCount();
		mainmenu_send_number_count.setText(all_unsend_count + "票");
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.yewubutton:
			Intent yIntent = new Intent(MenuMainActivity.this,
					BusinessActivity.class);
			startActivity(yIntent);
			break;
		case R.id.delectscanbutton:
			Intent delectscan = new Intent(MenuMainActivity.this,
					DeleScanDataActivity.class);
			startActivity(delectscan);
			break;
		case R.id.setingbutton:
			startActivity(new Intent(MenuMainActivity.this,
					SettingActivity.class));
			break;
		case R.id.Shuttle_bus_back_button:
			// finish();
			AlertDialog.Builder builder = new Builder(MenuMainActivity.this);
			builder.setMessage("确认返回登录界面？");
			builder.setTitle("询问");
			builder.setPositiveButton("确认",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							MenuMainActivity.this.finish();
						}
					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.create().show();
			break;
		}
	}

}
