package com.example.baishihuitong;

import com.query.DaoJianQueryByTime;
import com.query.FajianQueryByTime;
import com.query.JiBaoQueryByTime;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BusinessselectActivity extends Activity {

	public Button yetongselectbutton;
	public TextView businessselect_send_number_count;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.businessselect);
	    yetongselectbutton = (Button) findViewById(R.id.yetongselectbutton);
	    businessselect_send_number_count = (TextView) findViewById(R.id.businessselect_send_number_count);
	    businessselect_send_number_count.setText(getAllunsendCount() + "票");
	    yetongselectbutton.setVisibility(View.GONE);
	}
	@Override
	protected void onResume() {
		 businessselect_send_number_count.setText(getAllunsendCount() + "票");
		super.onResume();
	}
	public int getAllunsendCount() {
		SharedPreferences mPreferences = getSharedPreferences("mode", MODE_PRIVATE);
		int unSend_count_msg_fajian = mPreferences.getInt(
				"unSend_count_msg_fajian", 0);
		int unSend_count_msg_daojian = mPreferences.getInt("unSend_count_msg_dao",
				0);
		int unSend_count_msg_jibao = mPreferences.getInt("unSend_count_msg_jibao",
				0);
		return unSend_count_msg_fajian + unSend_count_msg_daojian
				+ unSend_count_msg_jibao;
	}
	public void onclick(View v){
		switch (v.getId()) {
		case R.id.yetongselectbutton:
			startActivity(new Intent(BusinessselectActivity.this,TimeselectActivity.class));
			break;
		case R.id.faselectbutton:
			startActivity(new Intent(BusinessselectActivity.this,FajianQueryByTime.class));
			break;
		case R.id.daoselectbutton:
			startActivity(new Intent(BusinessselectActivity.this,DaoJianQueryByTime.class));
			break;
		case R.id.jiselectbutton:
			Intent mIntent=new Intent(BusinessselectActivity.this,JiBaoQueryByTime.class);
			startActivity(mIntent);
			break;
		case R.id.selectback_button:
			finish();
			break; 

		default:
			break;
		}
	}
	
       
}
