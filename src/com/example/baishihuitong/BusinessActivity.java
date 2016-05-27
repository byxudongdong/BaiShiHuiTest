package com.example.baishihuitong;

import com.arrive.ArrivescanActivity;
import com.packages.PackscanActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class BusinessActivity extends Activity {

	public TextView business_send_number_count;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.business);
	    business_send_number_count = (TextView) findViewById(R.id.business_send_number_count);
	    business_send_number_count.setText(getAllunsendCount()  + "票");
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
	@Override
	protected void onResume() {
		 business_send_number_count.setText(getAllunsendCount() + "票");
		super.onResume();
	}
	public void onclick(View v){
	     switch (v.getId()) {
		case R.id.yetongbutton:
			startActivity(new Intent(BusinessActivity.this,BusinessselectActivity.class));
			
			break;
		case R.id.fascanbutton:
			startActivity(new Intent(BusinessActivity.this,SendscanActivity.class));
			break;
		case R.id.daoscanbutton:
			startActivity(new Intent(BusinessActivity.this,ArrivescanActivity.class));
			break;
		case R.id.jiscanbutton:
			Intent pIntent=new Intent(BusinessActivity.this,PackscanActivity.class);
			startActivity(pIntent);
			break;
		case R.id.businessback_button:
			finish();
			break;

		default:
			break;
		}	
	}
	
    
}
