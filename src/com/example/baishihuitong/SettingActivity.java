package com.example.baishihuitong;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.arrive.ArrivescanActivity;
import com.baishi.db.cn.DBHelper;
import com.baishi.db.cn.FaJianSQLite;
import com.baishi.db.cn.GetSaveDabaseSQ;
import com.baishi.db.cn.SaveShardMessage;
import com.baishihuitong.untils.HttpUrl;
import com.bluetooth.BluetoothSearchActivity;
import com.example.baishihuitong.SendscanActivity.getSiteCode;
import com.google.gson.Gson;

import sendupSet.JsonToString;
import sendupSet.SendupSet;
import sendupSet.SystemServiceSendSet;
import userMessage.SiteCodeMessage;
import userMessage.XDeviceInfo;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class SettingActivity extends Activity {
	private SharedPreferences mPreferences;
	public int unSend_count_msg = 0;
	
	public SaveShardMessage saveShard;
	public DBHelper dbHelper;
	private SQLiteDatabase database;
	GetSaveDabaseSQ getSQLite ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.setting);
	    dbHelper = new DBHelper(this);
	   
		database = dbHelper.getWritableDatabase();
		getSQLite = new GetSaveDabaseSQ(database);
		 saveShard = new SaveShardMessage(database);
	    mPreferences = getSharedPreferences("mode", MODE_PRIVATE);
		unSend_count_msg = mPreferences.getInt("unSend_count_msg", 0);
	}
	public void showDialog(Context context) {
		AlertDialog.Builder builder = new Builder(context);
		final EditText edit = new EditText(SettingActivity.this);
		builder.setView(edit);
		builder.setTitle("服务器修改口令");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(edit.getText().toString().equals("800best"))
					startActivity(new Intent(SettingActivity.this,
							SystemServiceSendSet.class));
				else{
					Toast.makeText(getApplicationContext(), "口令错误！",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
			}
		});
		builder.create().show();
	}
	
	public void onclick(View v){
		switch (v.getId()) {
		
		case R.id.shangchuangbutton :
//			showDialog(SettingActivity.this);
			break;
		case R.id.shangsetingbutton :
				startActivity(new Intent(SettingActivity.this,SendupSet.class));
			break;
		
		case R.id.lypairbutton :
			startActivity(new Intent(SettingActivity.this, BluetoothSearchActivity.class));
			
			break;
		
		case R.id.timetongbutton :
			send_FaJian_parames();
			break;
		
		case R.id.settingback_button :
			finish();
			break;
			

		default:
			break;
		}
	}
	
	/**
	 * 发件请求
	 */
	public void send_FaJian_parames(){
//		if(saveShard.getchange_sendup_flag(SettingActivity.this) == 1){
//			JsonToString jsont = new JsonToString();
			Map<String, String> paramsmap = new HashMap<String, String>();
			paramsmap.put("packageName", "com.best.android.bexrunner");
			paramsmap.put("revision", "420");
			paramsmap.put("sdkint", "19");
			paramsmap.put("clientApkMd5", "123456");
			paramsmap.put("deviceId", LoginActivity.DEVICE_ID);
			paramsmap.put("siteCode", mPreferences.getString("X-Auth-Site", ""));
			Map<String, String> headParams = getHeadParams();
			String url = "http://opendev.appl.800best.com/hsce/v1/ApkCheckUpdateService/ApkCheckUpdate";
			new getSiteCode(paramsmap, headParams,url,1).execute();
//		}
	}
	
	public Map<String, String>  getHeadParams(){
		Map<String, String> headParams = new HashMap<String, String>();
		headParams.put("Content-Type", "application/x-www-form-urlencoded");
//		headParams.put("X-Auth-Token", SaveShardMessage.TOKEN);
//		headParams
//				.put("X-Auth-Site", mPreferences.getString("X-Auth-Site", ""));
//		headParams
//				.put("X-Auth-User", mPreferences.getString("X-Auth-User", ""));
		return headParams;
	}
	/**
	 * 获得当前时间
	 */
	public String getCurrentTime(String current_time) {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(current_time);
		return formatter.format(date);
	}
	public String getXDeviceInfo(){
		XDeviceInfo xDevice = new XDeviceInfo();
		xDevice.setKey(SaveShardMessage.TOKEN + "-" + getCurrentTime("yyyyMMddHHmm"));
		xDevice.setPhoneNumber(null);
		xDevice.setIMEI(null);
		xDevice.setIMSI(null);
		xDevice.setDeviceID("108770");
		xDevice.setDeviceModel(null);
		xDevice.setSystemType(null);
		xDevice.setSystemVersion(null);
		String xDeviceInfo = new Gson().toJson(xDevice);
		Log.d("标记符号", "打印信息");
		return xDeviceInfo;
	}
	public ArrayList<FaJianSQLite> getFajianlist(int current_num,int count){
		ArrayList<FaJianSQLite> fajianlist = getSQLite.getFajianData(database, current_num,count, false);
		ArrayList<FaJianSQLite> fajian_print = new ArrayList<FaJianSQLite>();
		for(int i = fajianlist.size() - 1; i >= 0; i --){
			fajian_print.add(fajianlist.get(i));
		}
		return fajian_print;
	}
	SiteCodeMessage siteCodeMsg = new SiteCodeMessage();
	class getSiteCode extends AsyncTask<Void, Void, String> {
		public Map<String, String> paramsmap;
		public Map<String, String> headParams;
		public String http_url;
		public int flag;

		public getSiteCode(Map<String, String> paramsmap,
				Map<String, String> headParams,String http_url,int flag) {
			super();
			this.paramsmap = paramsmap;
			this.headParams = headParams;
			this.http_url = http_url;
			this.flag = flag;
		}

		@Override
		protected String doInBackground(Void... params) {
			HttpUrl httpUrl = new HttpUrl();
			String result = httpUrl
					.post(http_url,
							paramsmap, headParams);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (null != result && result.length() > 0) {
				Log.d("KKKKKKKK", result);
				if(flag == 1)
					checkSendScan_parames(result);
			}
		}
		
		
		public void checkSendScan_parames(String result){
			Log.d("应用升级++++++", result);
//			JSONObject mJsonObject;
//			try {
//				mJsonObject = new JSONObject(result);
//				if(mJsonObject.getString("acceptcount").length() > 0)
//					handler.sendEmptyMessage(0x002);
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
		}
	}
	
	
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			
			if (msg.what == 0x002) {
				unSend_count_msg = 0;
				mPreferences.edit().putInt("unSend_count_msg", unSend_count_msg).commit();
			}
		};
	};
	 boolean flag_dialog = false;
	public boolean ShowDialog(String message) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SettingActivity.this);
		builder.setCancelable(false);
		builder.setMessage("确认" + message + "?");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				send_FaJian_parames();
				flag_dialog = true;
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", null);
		builder.show();
		return flag_dialog;
	}
}
