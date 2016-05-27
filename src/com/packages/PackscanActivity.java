package com.packages;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import sendupSet.JsonToString;
import sendupSet.TimeQueryData;
import sendupSet.WeakUPScreen;
import userMessage.GetTime;
import userMessage.ReadFileSN;
import userMessage.SiteCodeMessage;
import userMessage.XDeviceInfo;

import com.arrive.ArrivescanActivity;
import com.baishi.db.cn.DBHelper;
import com.baishi.db.cn.FaJianSQLite;
import com.baishi.db.cn.GetSQupFlag;
import com.baishi.db.cn.GetSaveDabaseSQ;
import com.baishi.db.cn.MyNative;
import com.baishi.db.cn.SaveShardMessage;
import com.baishihuitong.untils.HttpUrl;
import com.baishihuitong.untils.ListViewCompat;
import com.bluetooth.BluetoothLeService;
import com.example.baishihuitong.LoginActivity;
import com.example.baishihuitong.PlayBeepSound;
import com.example.baishihuitong.R;
import com.example.baishihuitong.SendscanActivity;
import com.google.gson.Gson;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PackscanActivity extends Activity {
	public EditText pretname;// 下一站
	public static String NEXT_SITECODE = "";
	public EditText workorder;// 任务单
	public EditText waybillnumber;// 运单号
	TextView verify,send_number_count,upload_jibao_flag,workday_count;// 验证
	TextView nolocal;// 未锁
	Button fanhui;// 返回
	Button delect;// 删除
	Button queding;// 确定
	public String numberString;
	private SharedPreferences mPreferences;
	private ListViewCompat listView;
	public MyNative myNative;
	public boolean is_sending_up = false;
	public int is_sending_up_mub_count = 0;
	public int unSend_count_msg = 0;
	public int all_number_today = 0;
	public int current_all_number = 0;
	public Toast toast;
	public String jibaotable_first_billcode = "";
//	public int all_number_today = 0;
	/**
	 * 广播接收器
	 */
	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
				String EXTRA_DATA = intent
						.getStringExtra(BluetoothLeService.EXTRA_DATA);
				if (EXTRA_DATA.length() > 0) {
					numberString = EXTRA_DATA;
					if(pretname.isFocused()){
						check_site_code(numberString);
						
					}else if(workorder.isFocused()){
						if(check_sitecode_flag && show_dialog_ing){
							if(myNative.OpCom_CheckBarcode(numberString, 6) == 1 ){
								//包号
								WeakUPScreen.onScan(PackscanActivity.this);
								play_beep_sound.playBeepSoundAndVibrate(0);
								if(workorder.getText().toString().length() == 12 && !numberString.equals(workorder.getText().toString())){
									show_dialog_ing = false;
									WORK_PRE = workorder.getText().toString();
									showDialog(PackscanActivity.this,"确认更改包号？");
								}else{
									dialog_work_click_button();
								}
							}else{
								play_beep_sound.playBeepSoundAndVibrate(1);
								showTextToast("包号不符合规则");
							}
						}
					}else if(myNative.OpCom_CheckBarcode(numberString, 3) == 1){
						//运单号
						if(!workorder.getText().toString().startsWith("43")){
								if(numberString.length() == 12 && 
										(numberString.startsWith("40") || numberString.startsWith("42")
												|| numberString.startsWith("43"))){
									play_beep_sound.playBeepSoundAndVibrate(1);
									showTextToast("条码不符合规则");
								}else if(check_sitecode_flag){
									number_check(numberString);
								}
						}else if(check_sitecode_flag){
							number_check(numberString);
						}
					}else{
						play_beep_sound.playBeepSoundAndVibrate(1);
						showTextToast("条码不符合规则");
					}
				}
			}
		}
	};
	
	public void check_site_code(String station_name){
		if(show_dialog_ing){
				check_sitecode_flag = false;
				check_sitecode_success = false;
				if(myNative.OpCom_CheckBarcode(pretname.getText().toString(), 2) != 1 && 
						NEXT_SITECODE.length() == 6 && 
						!station_name.equals(NEXT_SITECODE) &&
						pretname.getText().toString().length() > 0){
					NEXT_SITECODE = station_name;
					pretname.setText(station_name);
					show_dialog_ing = false;
					play_beep_sound.playBeepSoundAndVibrate(0);
					showDialog(PackscanActivity.this,"确认更改站点？");
				}else{
					NEXT_SITECODE = station_name;
					pretname.setText(station_name);
					dialog_next_click_button(station_name);
				}
		}
	}
	 private void showTextToast(String msg) {
		 TextView text = new TextView(this);
		 text.setTextSize(24);
		 text.setPadding(10, 5, 10, 5);
  		text.setTextColor(Color.rgb(255, 255, 255));
  		text.setBackgroundColor(Color.rgb(50, 50, 50));
     	text.setText(msg);
	        if (toast == null) {
	        	toast = new Toast(getApplicationContext());
	        } 
	        toast.setView(text);
	        toast.setGravity(Gravity.CENTER, 0, 40);
	        toast.show();
	        
	    }
	public void number_check(String number)
	{
		waybillnumber.setText(number);
		if(myNative.OpCom_CheckBarcode(workorder.getText().toString(), 6) == 1){
			waybillnumber.requestFocus();
			saveDabaseDaoJian();
			
		}else{
			showTextToast("包号单错误");
		}
	}
	public SaveShardMessage saveShard;
	public DBHelper dbHelper;
	private SQLiteDatabase database;
	public static int num_count_limit ,num_count_time;
	public LinearLayout dialog_progress_line;
	public PlayBeepSound play_beep_sound;
	private InputMethodManager mIM;
	public int Get_SQLite_count = 0;
	GetTime Time;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.packagescaning);
		WeakUPScreen.oncreate(PackscanActivity.this);
		mIM = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		init();
		play_beep_sound = new PlayBeepSound(PackscanActivity.this);
		myNative = new MyNative();
		// 本地数据库
		dbHelper = new DBHelper(this);
		database = dbHelper.getWritableDatabase();
		saveShard = new SaveShardMessage(database);
		timeQuery = new TimeQueryData(database);
		Time = new GetTime();
		String tim = Time.getCurrentTime("HH:mm:ss");
		old_tiem = getOldTime(tim);
		registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
		num_count_limit = SaveShardMessage
				.getchange_count_flag(PackscanActivity.this);
		num_count_time = SaveShardMessage.getchange_time_flag(PackscanActivity.this);
		mPreferences = getSharedPreferences("mode", MODE_PRIVATE);
		unSend_count_msg = mPreferences.getInt("unSend_count_msg_jibao", 0);
		all_number_today = mPreferences.getInt("all_number_today_jibao", 0);
		jibaotable_first_billcode = mPreferences.getString("jibaotable_first_billcode", "");
		
		getSQLite = new GetSaveDabaseSQ(database);
		current_all_number = fajian_first_billcode(jibaotable_first_billcode)[0];
		int count = (int) timeQuery.getCount("集包", Time.before4YesterDay(),0);
		if(count > 0){
			timeQuery.delete_batch("集包");
		}
		ArrayList<JiBaoSQLite> daojianlist =getDaojianlist(current_all_number, unSend_count_msg);
		 adapter = new PrivateListJiBaoAdapter(PackscanActivity.this, daojianlist);
		listView.setAdapter(adapter);
		sending_count_n = unSend_count_msg;
		adapter.setNowSending(sending_count_n);
		
		workday_count.setText("集包 已扫描 " + all_number_today);
		send_number_count.setText("未上传：" + (unSend_count_msg > 0 ? unSend_count_msg : 0));
		check_sleep_time();
		setViewOnClick();
		mListViewRefresh();
	}

	boolean LOAD_MORE_FLAGE = true, LOAD_MORE_END = true;
	int number_index;
	// 列表刷新
	public void mListViewRefresh() {
			listView.setPullRefreshEnable(true);
			listView.setPullLoadEnable(true);
			listView.setXListViewListener(new ListViewCompat.IXListViewListener() {
				public void onRefresh() {
					listView.setRefreshTime(Time.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
					LOAD_MORE_FLAGE = true;
					LOAD_MORE_END = true;
					mGetSaveDabaseSQ(false,(int) saveShard.getCount("集包"));
				}
				public void onLoadMore() {
					if (LOAD_MORE_FLAGE && current_all_number > 3) {
						number_index = current_all_number - 3;
						LOAD_MORE_FLAGE = false;
					} else if (LOAD_MORE_FLAGE && current_all_number < 3) {
						number_index = 0;
						LOAD_MORE_FLAGE = false;
					}
					if (LOAD_MORE_END) {
						if (number_index <= 0) {
							LOAD_MORE_END = false;
						} else {
							mGetSaveDabaseSQ(true,number_index);
						}
					}
					if (number_index < 0) {
						Toast.makeText(PackscanActivity.this, "已加载全部数据",
								Toast.LENGTH_SHORT).show();
					}
					number_index -= 3;
					listView.stopRefresh();
					listView.stopLoadMore();
				}
			});
		}
	// 退出时间
				private long currentBackPressedTime = 0;
				// 退出间隔
				private static final int BACK_PRESSED_INTERVAL = 2000;
	public void setViewOnClick(){
		pretname.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
				if(s.length() > 6 && myNative.OpCom_CheckBarcode(s.toString(), 2) == 1){
					play_beep_sound.playBeepSoundAndVibrate(1);
					showTextToast("只能输入6位数字");
				}else if (myNative.OpCom_CheckBarcode(pretname.getText().toString(),
						2) == 1) {
					NEXT_SITECODE = pretname.getText().toString();
					check_sitecode();
				}else{
					play_beep_sound.playBeepSoundAndVibrate(1);
					showTextToast("只能输入6位数字");
				}
			}
		});
		pretname.setOnKeyListener(new OnKeyListener() {
			
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_ENTER){
//					check_site_code(pretname.getText().toString());
					workorder.requestFocus();
				}
				return false;
			}
		});
		pretname.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (System.currentTimeMillis() - currentBackPressedTime > BACK_PRESSED_INTERVAL) {
					currentBackPressedTime = System.currentTimeMillis();
				} else {
					getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
					pretname.setSelection(pretname.getText().toString().length());
					mIM.showSoftInput(pretname,InputMethodManager.SHOW_FORCED);
				}
				
			}
		});
		pretname.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					pretname.setBackgroundColor(Color.rgb(00, 119, 153));
					workorder.setBackgroundColor(Color.rgb(255, 255, 255));
					waybillnumber.setBackgroundColor(Color.rgb(255, 255, 255));
				}
			}
		});
		workorder.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
				if(s.length() > 12){
					play_beep_sound.playBeepSoundAndVibrate(1);
					showTextToast("只能输入12位");
				}
			}
		});
		
		
		workorder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (System.currentTimeMillis() - currentBackPressedTime > BACK_PRESSED_INTERVAL) {
					currentBackPressedTime = System.currentTimeMillis();
				} else {
					getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
					workorder.setSelection(workorder.getText().toString().length());
					mIM.showSoftInput(workorder,InputMethodManager.SHOW_FORCED);
				}
			}
		});
		workorder.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_ENTER){
					if(workorder.isFocused() && workorder.getText().toString().length() > 0){
						waybillnumber.requestFocus();
					}
				}
				return false;
			}
		});
		workorder.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					workorder.setBackgroundColor(Color.rgb(00, 119, 153));
					pretname.setBackgroundColor(Color.rgb(255, 255, 255));
					waybillnumber.setBackgroundColor(Color.rgb(255, 255, 255));
				}
			}
		});
		waybillnumber.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			public void afterTextChanged(Editable s) {
				if(s.length() > 14){
					play_beep_sound.playBeepSoundAndVibrate(1);
					showTextToast("只能输入少于14位数字");
				}
			}
		});
		waybillnumber.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (System.currentTimeMillis() - currentBackPressedTime > BACK_PRESSED_INTERVAL) {
					currentBackPressedTime = System.currentTimeMillis();
				} else {
					getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
					waybillnumber.setSelection(waybillnumber.getText().toString().length());
					mIM.showSoftInput(waybillnumber,InputMethodManager.SHOW_FORCED);
				}
			}
		});
		waybillnumber.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					workorder.setBackgroundColor(Color.rgb(255, 255, 255));
					pretname.setBackgroundColor(Color.rgb(255, 255, 255));
					waybillnumber.setBackgroundColor(Color.rgb(00, 119, 153));
				}
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int index = (Integer) parent.getItemAtPosition(position);
				Message msg = new Message();
				msg.what = 0x003;
				msg.arg1 = index;
				msg.arg2 = position;
				handler.sendMessage(msg);
			}
		});
		
		fanhui.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (unSend_count_msg != 0){
					showDialog(PackscanActivity.this, "还有未上传数据，确认上传？");
				}else{
					showDialog(PackscanActivity.this, "确认退出？");
				}
			}
		});

		verify.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(myNative.OpCom_CheckBarcode(pretname.getText().toString(), 2) == 1){
					NEXT_SITECODE = pretname.getText().toString();
					check_sitecode();
				}else{
					play_beep_sound.playBeepSoundAndVibrate(1);
					showTextToast("站点为6位数字");
				}
			}
		});
		
		delect.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(index_dele > 0 && position > 0 ){
					if(position > 0 && position <= unSend_count_msg && jibaolist.size() > 0){	
						Packets_sending_up_start = (int) saveShard.getCount("集包");
						delet_dbHelp(index_dele);
						index_dele = -1;
						position = -1;
					}else {
						play_beep_sound.playBeepSoundAndVibrate(1);
						index_dele = -1;
						position = -1;
						showTextToast("已上传数据不能删除");
					}
				}else if(position == 0 && unSend_count_msg > 0 && jibaolist.size() > 0){
					delet_dbHelp(current_all_number);
					index_dele = -1;
					position = -1;
				} else if (index_dele < 0 && unSend_count_msg > 0  && jibaolist.size() > 0) {
					play_beep_sound.playBeepSoundAndVibrate(1);
					int index = jibaolist.get(0).getIndex();
					delet_dbHelp(index);
				}else if( unSend_count_msg == 0){
					play_beep_sound.playBeepSoundAndVibrate(1);
					index_dele = -1;
					position = -1;
					showTextToast("已上传数据不能删除");
				}
			}
		});
		queding.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(check_sitecode_flag&& check_sitecode_success){
					if(myNative.OpCom_CheckBarcode(pretname.getText().toString(), 2) != 1){
						if(myNative.OpCom_CheckBarcode(workorder.getText().toString(), 6) == 1){
							if(myNative.OpCom_CheckBarcode(waybillnumber.getText().toString(), 3) == 1){
								//运单号
								if(!workorder.getText().toString().startsWith("43")){
										if(workorder.getText().toString().length() == 12 && 
												(waybillnumber.getText().toString().startsWith("40") 
														|| waybillnumber.getText().toString().startsWith("42")
														|| waybillnumber.getText().toString().startsWith("43"))){
											play_beep_sound.playBeepSoundAndVibrate(1);
											showTextToast("条码不符合规则");
										}else{
											number_check(waybillnumber.getText().toString());
										}
								}else {
									number_check(waybillnumber.getText().toString());
								}
							}else{
								play_beep_sound.playBeepSoundAndVibrate(1);
								showTextToast("运单号错误");
							}
						}else{
							play_beep_sound.playBeepSoundAndVibrate(1);
							showTextToast("包号错误");
						}
					}else{
						play_beep_sound.playBeepSoundAndVibrate(1);
						showTextToast("站点未验证");
					}
					
				}else{
					play_beep_sound.playBeepSoundAndVibrate(1);
					showTextToast("站点未验证");
				}
			}
		});
		
		nolocal.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(nolocal.getText().equals("未锁")){
					nolocal.setText("已锁");
					pretname.setFocusable(false);
					workorder.setFocusable(false);
					KEYCODE_BACK_flag = true;
					pretname.setBackgroundColor(Color.parseColor("#808080"));
					workorder.setBackgroundColor(Color.parseColor("#808080"));
				}else{
					nolocal.setText("未锁");
					KEYCODE_BACK_flag = false;
					pretname.setFocusable(true);
					pretname.requestFocus();
					pretname.setFocusableInTouchMode(true);
					workorder.setFocusable(true);
					workorder.requestFocus();
					workorder.setFocusableInTouchMode(true);
					pretname.setBackgroundColor(Color.parseColor("#F7F7F7"));
					workorder.setBackgroundColor(Color.parseColor("#F7F7F7"));
				}
			}
		});
	}
	public void delet_dbHelp(int position){
		deleDatabaseSQ(position);
		unSend_count_msg--;
		all_number_today--;
		mPreferences.edit().putInt("unSend_count_msg_jibao", unSend_count_msg).commit();
		mPreferences.edit().putInt("all_number_today_jibao", all_number_today).commit();
		
		if(unSend_count_msg < 1)
			unSend_count_msg = 0;
		current_all_number = fajian_first_billcode(jibaotable_first_billcode)[0];
//		all_number_today = (int) timeQuery.getCount("集包", getCurrentTime("yyyy-MM-dd"));
		mGetSaveDabaseSQ(false,current_all_number);
	}
	
	public boolean KEYCODE_BACK_flag = false;
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){  
			if(!KEYCODE_BACK_flag)
				if (unSend_count_msg != 0){
					showDialog(PackscanActivity.this, "还有未上传数据，确认上传？");
				}else{
					showDialog(PackscanActivity.this, "确认退出？");
				}
			return  KEYCODE_BACK_flag? true : super.onKeyDown(keyCode, event);
		}else if(keyCode == 131){
			if(unSend_count_msg > 0 && !is_sending_up)
				send_FaJian_parames(1);
			return true;
		}  
		return  super.onKeyDown(keyCode, event);
	};
	public boolean check_sitecode_flag = false;
	public int now_unsending_count;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x001) {
				if(check_sitecode_success){
					check_sitecode_flag = true;
					workorder.requestFocus();
					dialog_progress_line.setVisibility(View.GONE);
					PRE_STATION = siteCodeMsg.getSitename();
					pretname.setText(siteCodeMsg.getSitename());
					m_thread.interrupt();
				}else{
					dialog_progress_line.setVisibility(View.GONE);
					m_thread.interrupt();
				}
			}else if(msg.what == 0x002){
//				unSend_count_msg = unSend_count_msg - sending_count_n;
//				adapter.setNowSending(unSend_count_msg);
//				mGetSaveDabaseSQ(false,(int) saveShard.getCount("集包"));
				upload_jibao_flag.setVisibility(View.GONE);
				workday_count.setText("集包 已扫描 " + (int) timeQuery.getCount("集包", Time.getCurrentTime("yyyy-MM-dd"),1));
				send_number_count.setText("未上传：" + (unSend_count_msg > 0 ? unSend_count_msg : 0));
				if (unSend_count_msg >= num_count_limit) {
					send_number_count.setTextColor(Color.rgb(255, 00, 00));
				} else {
					send_number_count.setTextColor(Color.rgb(238, 251, 5));
				}
				showTextToast("上传成功");
			}else if(msg.what == 0x003){
				index_dele = msg.arg1;
				position = msg.arg2;
				if(position == 0)
					adapter.setpostion(0);
				else
					adapter.setpostion(position - 1);
				adapter.notifyDataSetChanged();
			}else if(msg.what == 0x005){
				upload_jibao_flag.setVisibility(View.VISIBLE);
				is_sending_up = false;
				
			}
		};
	};

	public static int index_dele = -1;
	public static int position = -1;
	public String getXDeviceInfo(){
		XDeviceInfo xDevice = new XDeviceInfo();
		xDevice.setKey(SaveShardMessage.TOKEN + "-" + Time.getCurrentTime("yyyyMMddHHmm"));
		xDevice.setPhoneNumber(null);
		xDevice.setIMEI(null);
		xDevice.setIMSI(null);
		xDevice.setDeviceID(LoginActivity.DEVICE_ID);
		xDevice.setDeviceModel(null);
		xDevice.setSystemType(null);
		xDevice.setSystemVersion(null);
		String xDeviceInfo = new Gson().toJson(xDevice);
		Log.d("标记符号", xDeviceInfo);
		return xDeviceInfo;
	}
	
	public int sending_count_n = 0;
	public boolean Packets_sending_up = false;
	public int Packets_sending_up_start = 0;
	
	public static boolean checkNetworkConnection(Context context)  
	   {  
	       final ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);  
	  
	       final android.net.NetworkInfo wifi =connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);    
	  
	       if(wifi.isAvailable())  
	           return true;  
	       else  
	           return false;  
	   }
	
	/**
	 * 发件请求
	 */
	public void send_FaJian_parames(int send_flag){
		if(checkNetworkConnection(PackscanActivity.this) && unSend_count_msg > 0){
			new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(SendscanActivity.send_time_limit);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					handler.sendEmptyMessage(0x005);
				}
			}).start();
			String time = Time.getCurrentTime("HH:mm:ss");
			old_tiem = getOldTime(time);
			handler.sendEmptyMessage(0x005);
			Packets_sending_up_start = (int) saveShard.getCount("集包");
			now_unsending_count = unSend_count_msg;
			if(send_flag == 1){
				send_flag_parames(unSend_count_msg);
			}else if(send_flag == 0){
				send_flag_parames(unSend_count_msg);
			}
		}
	}
	
	public void send_flag_parames(int unSend_count_msg) {
		is_sending_up = true;
		int start = 0;
		String tim = Time.getCurrentTime("HH:mm:ss");
		long current_time = getOldTime(tim);
		
		send_flag_JiBao();
	}
	public ArrayList<JiBaoSQLite> fajianlist2;
	public void send_flag_JiBao(){
		JsonToString jsont = new JsonToString();
		Map<String, String> paramsmap = new HashMap<String, String>();
		fajianlist2 = GetSQupFlag.JiBaoSQ(database, 0);
		paramsmap.put("scanDataList", jsont.getJiBaoDataList(fajianlist2));
		paramsmap.put("xDeviceInfo", getXDeviceInfo());
		paramsmap.put("userName", "\"" + mPreferences.getString("X-Auth-User", "") + "\"");
		paramsmap.put("siteCode", "\"" + mPreferences.getString("X-Auth-Site", "") + "\"");
		paramsmap.put("clientTime", "\"" + Time.getCurrentTime("yyyy-MM-dd") + "T" + Time.getCurrentTime("HH:mm:ss")+ "+08:00" +"\"");
		Map<String, String> headParams = getHeadParams();
		
		//http://handset2.appl.800best.com/hyco/v1/HTScan/HycoSend
		//http://opendev.appl.800best.com/hscedev/v1/HTScan/HycoSend
		String url = LoginActivity.http_util + "/v1/HTScan/HycoBagging";
		new getSiteCode(paramsmap, headParams,url,1).execute();
	}

	public static long old_tiem = 0;
	public TimeQueryData timeQuery;
	/**
	 * 数据库保存
	 */
	public void saveDabaseDaoJian(){
		WeakUPScreen.onScan(PackscanActivity.this);
			String time =  Time.getCurrentTime("HH:mm:ss");
			String date = Time.getCurrentTime("yyyy-MM-dd");
			jibaotable_first_billcode = "@" + waybillnumber.getText().toString();
			adapter.setpostion(0);
			long flag = saveShard.saveShardJiBaoMessage(new JiBaoSQLite(NEXT_SITECODE,
						workorder.getText().toString(),
						jibaotable_first_billcode,date,time,
						mPreferences.getString("X-Auth-User", ""),
						mPreferences.getString("X-Auth-Site", ""),0));
			if(flag > 0){
				unSend_count_msg++;
				all_number_today ++;
				mPreferences.edit().putInt("unSend_count_msg_jibao", unSend_count_msg).commit();
				mPreferences.edit().putInt("all_number_today_jibao", all_number_today).commit();
				mPreferences.edit().putString("jibaotable_first_billcode", jibaotable_first_billcode).commit();
				waybillnumber.setText("");
				play_beep_sound.playBeepSoundAndVibrate(0);
				workday_count.setText("集包 已扫描 " + all_number_today);
				send_number_count.setText("未上传：" + (unSend_count_msg > 0 ? unSend_count_msg : 0));
				current_all_number = fajian_first_billcode(jibaotable_first_billcode)[0];
				mGetSaveDabaseSQ(false,current_all_number);
				
			}else{
				int[] mindex = fajian_first_billcode(jibaotable_first_billcode);
				current_all_number = mindex[0];
				mGetSaveDabaseSQ(false,current_all_number);
				play_beep_sound.playBeepSoundAndVibrate(1);
				if(mindex[1]== 0){
					position = 0;
					
					showTextToast("重复运单号是否删除");
				}else{
					position = -1;
					showTextToast("已上传数据，不能删除");
				}
			}
	}
	public int[] fajian_first_billcode(String fajiantable_first_billcode){
		return GetSQupFlag.getTableIndex(database, fajiantable_first_billcode, "BaiShiJiBao");
	}
	
	public Thread timer_thread;
	public boolean timer_thread_flag = true;
	public void check_sleep_time(){
		timer_thread = new Thread(new Runnable() {
			public void run() {
				while(true){
					try {
						Thread.sleep(1000*2);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					String tim = Time.getCurrentTime("HH:mm:ss");
					long current_time = getOldTime(tim);
					if (mPreferences.getString("data", "null").equals("null")) {
						mPreferences
								.edit()
								.putString("data",
										Time.getCurrentTime("yyyy-MM-dd"))
								.commit();
					}
					if (!Time.getCurrentTime("yyyy-MM-dd").equals(
							mPreferences.getString("data", "null"))) {
						all_number_today = 0;
						mPreferences
								.edit()
								.putString("data",
										Time.getCurrentTime("yyyy-MM-dd"))
								.commit();
					}
					if(is_sending_up){
						send_flag_img ++;
						if(send_flag_img > 10){
							send_flag_img = 0;
							handler.sendEmptyMessage(0x005);
						}
					}else{
						send_flag_img = 0;
					}
					if(!is_sending_up &&old_tiem > 0 
							&& (current_time - old_tiem) > num_count_time * 60 * 1000 
							&& unSend_count_msg > 0){
						
						send_FaJian_parames(1);
					}else{
						if (!is_sending_up && unSend_count_msg >= num_count_limit && !Packets_sending_up) {
							send_FaJian_parames(1);
							
						}
					}
				}
			}
		});
		timer_thread.start();
	}
	int send_flag_img = 0;
	public void close_timer(){
		if (timer_thread != null) {
			timer_thread_flag = false;
			timer_thread.interrupt();
			timer_thread = null;
		}
	}
	public long getOldTime(String time){
		SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
		long timeStart = 0;
		try {
			timeStart = sdf.parse(time).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timeStart;
	}
	/**
	 * 删除
	 * @param position
	 */
	public void deleDatabaseSQ(int position){
		saveShard.deleShardJiBao(position);
	}
	
	/**
	 * 获取数据库数据
	 */
	GetSaveDabaseSQ getSQLite ;
	PrivateListJiBaoAdapter adapter;
	ArrayList<JiBaoSQLite> jibaolist = new ArrayList<JiBaoSQLite>();
	public void mGetSaveDabaseSQ(boolean falg,int current_all_number){
		if(unSend_count_msg > 3){
			Get_SQLite_count = 3;
		}else{
			Get_SQLite_count = unSend_count_msg;
		}
		ArrayList<JiBaoSQLite> list = getDaojianlist(current_all_number, Get_SQLite_count);
		jibaolist.clear();
		jibaolist.addAll(list);
		adapter.setNowSending(unSend_count_msg);
		 adapter.setmMessageItems(jibaolist);
		adapter.notifyDataSetChanged();
		workday_count.setText("集包 已扫描 " + all_number_today);
		send_number_count.setText("未上传：" + (unSend_count_msg > 0 ? unSend_count_msg : 0));
		if(unSend_count_msg >= num_count_limit){
			send_number_count.setTextColor(Color.rgb(255, 00, 00));
		}else{
			send_number_count.setTextColor(Color.rgb(238, 251, 5));
		}
	}
	
	public ArrayList<JiBaoSQLite> getDaojianlist(int current_num,int count){
		ArrayList<JiBaoSQLite> daojianlist = getSQLite.getJiBaoData(database, current_num,count, false);
		ArrayList<JiBaoSQLite> jibao_print = new ArrayList<JiBaoSQLite>();
		for(int i = daojianlist.size() - 1; i >= 0; i --){
			jibao_print.add(daojianlist.get(i));
		}
		return jibao_print;
	}
	
	
	public Thread m_thread;
	public boolean check_sitecode_success = false;
	public void check_sitecode() {
		if(show_dialog_ing){
			check_sitecode_flag = false;
			check_sitecode_success = false;
			m_thread = new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					handler.sendEmptyMessage(0x001);
				}
			});
			m_thread.start();
			dialog_progress_line.setVisibility(View.VISIBLE);
			Map<String, String> paramsmap = new HashMap<String, String>();
			paramsmap.put("siteCode", "\"" + NEXT_SITECODE + "\"");
			Map<String, String> headParams = getHeadParams();
			// url = http://handset2.appl.800best.com/hyco/v1/HTSite/HycoSearchSite
			//http://opendev.appl.800best.com/hscedev/v1/HTSite/HycoSearchSite
			String url = LoginActivity.http_util + "/v1/HTSite/HycoSearchSite";
			new getSiteCode(paramsmap, headParams,url,0).execute();
		}
	}
	
	public Map<String, String>  getHeadParams(){
		Map<String, String> headParams = new HashMap<String, String>();
		headParams.put("Content-Type", "application/x-www-form-urlencoded");
		headParams.put("X-Auth-Token", SaveShardMessage.TOKEN);
		headParams
				.put("X-Auth-Site", mPreferences.getString("X-Auth-Site", ""));
		headParams
				.put("X-Auth-User", mPreferences.getString("X-Auth-User", ""));
		return headParams;
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

		protected String doInBackground(Void... params) {
			HttpUrl httpUrl = new HttpUrl();
			String result = httpUrl
					.post(http_url,
							paramsmap, headParams);
			return result;
		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			is_sending_up = false;
			send_up_destroy = false;
			if (null != result && result.length() > 0) {
				Log.d("KKKKKKKK", result);
				
				if(flag == 0){
					if( !result.equals("500")){
						setSiteCode(result);
						if(result.equals("403")){
							check_sitecode_success = false;
							dialog_progress_line.setVisibility(View.GONE);
							play_beep_sound.playBeepSoundAndVibrate(1);
//							showTextToast("Token失效，请重新登录");
						}else if(!"null".equals(siteCodeMsg.getSitecode()) && siteCodeMsg.getSitecode().length() > 4){
							check_sitecode_success = true;
							handler.sendEmptyMessage(0x001);
						}else {
							dialog_progress_line.setVisibility(View.GONE);
							check_sitecode_success = false;
							play_beep_sound.playBeepSoundAndVibrate(1);
							showTextToast("验证失败");
						}
					}else{
						dialog_progress_line.setVisibility(View.GONE);
						check_sitecode_success = false;
						play_beep_sound.playBeepSoundAndVibrate(1);
						showTextToast("验证失败");
					}
				}else if(flag == 1){
					checkSendScan_parames(result);
				}else if(flag == 2){
					check_login_again(result);
				}
			}
		}
		public void check_login_again(String result){
			if (null != result && result.length() > 0) {
				try {
					JSONObject mJsonObject = new JSONObject(result);
					String token = mJsonObject.getString("token");
					if(token.length() > 6){
						is_sending_up = false;
						showTextToast("Token获取成功！");
						SaveShardMessage.setLogin_token(token);
					}else {
//						showTextToast("Token获取失败，请退出并重新登录");
						is_sending_up = false;
						Login_again();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		}
		public void setSiteCode(String result){
			JSONObject mJsonObject;
			try {
				mJsonObject = new JSONObject(result);
				siteCodeMsg.setSitename(mJsonObject.getString("sitename"));
				siteCodeMsg.setDispatchrange(mJsonObject
						.getString("dispatchrange"));
				siteCodeMsg.setNotdispatchrange(mJsonObject
						.getString("notdispatchrange"));
				siteCodeMsg.setSitecode(mJsonObject.getString("sitecode"));
				siteCodeMsg.setPhone(mJsonObject.getString("phone"));
				siteCodeMsg
						.setPrincipal(mJsonObject.getString("principal"));
				siteCodeMsg.setCity(mJsonObject.getString("city"));
				siteCodeMsg.setProvince(mJsonObject.getString("province"));
				siteCodeMsg.setModifydate(mJsonObject
						.getString("modifydate"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		public void checkSendScan_parames(String result){
			JSONObject mJsonObject = null;
			try {
				if(!result.equals("403")){
					 mJsonObject = new JSONObject(result);
				}
				if(result.equals("403")){
					play_beep_sound.playBeepSoundAndVibrate(1);
//					showDialog(PackscanActivity.this,"Token失效，确定重新获取？");
					showTextToast("Token失效，正在重新获取？");
					Login_again();
					is_sending_up = false;
					
				}else
				if(0 <= Integer.parseInt(mJsonObject.getString("acceptcount"))){
					Iterator<JiBaoSQLite> it = fajianlist2.iterator();
					unSend_count_msg -= fajianlist2.size() ;
					if(unSend_count_msg >= fajianlist2.size()){
						unSend_count_msg -= fajianlist2.size();
					}else unSend_count_msg = 0;
					
					mPreferences.edit().putInt("unSend_count_msg_jibao", unSend_count_msg).commit();
					handler.sendEmptyMessage(0x002);
					while(it.hasNext()){
						JiBaoSQLite next = it.next();
						GetSQupFlag.updataFaJian(database, next.getBillCode());
						it.remove();
					}
					is_sending_up = false;
				}else{
					play_beep_sound.playBeepSoundAndVibrate(1);
					showTextToast("上传失败");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public void init() {
		pretname = (EditText) findViewById(R.id.packnextname);
		workorder = (EditText) findViewById(R.id.packworkorder);
		waybillnumber = (EditText) findViewById(R.id.packwaybillnumber);
		verify = (TextView) findViewById(R.id.packverify);
		nolocal = (TextView) findViewById(R.id.packnolock);
		send_number_count = (TextView) findViewById(R.id.pack_number_count);
		workday_count = (TextView) findViewById(R.id.workday_count);
		upload_jibao_flag = (TextView) findViewById(R.id.upload_jibao_flag);
		listView = (ListViewCompat) findViewById(R.id.packages_daolistview);
		fanhui = (Button) findViewById(R.id.packfanhui);
		delect = (Button) findViewById(R.id.packdelect);
		queding = (Button) findViewById(R.id.packqueding);
		pretname.setInputType(InputType.TYPE_NULL);
		workorder.setInputType(InputType.TYPE_NULL);
		waybillnumber.setInputType(InputType.TYPE_NULL);
		upload_jibao_flag.setVisibility(View.GONE);
		dialog_progress_line = (LinearLayout) findViewById(R.id.pack_dialog_progress_line);
		dialog_progress_line.setVisibility(View.GONE);
	}
	public void Login_again(){
		is_sending_up = true;
		Map<String, String> paramsmap = new HashMap<String, String>();
		paramsmap.put("version", "1");
		paramsmap.put("userName", "\"" + mPreferences.getString("X-Auth-User", "") + "\"");
		paramsmap.put("password", "\"" + LoginActivity.PASS_WORD+ "\"");
		paramsmap.put("siteCode", "\"" + mPreferences.getString("X-Auth-Site", "") + "\"");
		paramsmap.put("guid", "\""+LoginActivity.DEVICE_ID+"\"");
		paramsmap.put("location", "");
		paramsmap.put("mac", "");
		paramsmap.put("imei", "");
		paramsmap.put("imsi", "");
		Map<String, String> headParams = new HashMap<String, String>();
		headParams.put("Content-Type", "application/x-www-form-urlencoded");
		
		String url = LoginActivity.http_util + "/v1/User/HycoValidateUser";
		new getSiteCode(paramsmap, headParams, url, 2).execute();
	}
	public void dialog_next_click_button(String station_name){
		
			if(NEXT_SITECODE.length() == 6){
				play_beep_sound.playBeepSoundAndVibrate(0);
				check_sitecode();
			}else{
				play_beep_sound.playBeepSoundAndVibrate(1);
				showTextToast("站点错误");
			}
			
	}
	
	public void dialog_work_click_button(){
		if(pretname.getText().toString() != null)
		workorder.setText(numberString);
		waybillnumber.requestFocus();
	}
	public boolean show_dialog_ing = true;
	public String PRE_STATION = "";
	public String WORK_PRE = "";
	public void showDialog(Context context,final String msg) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage(msg);
		builder.setTitle("询问");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(msg.equals("还有未上传数据，确认上传？")){
					befor_destroy();
					send_up_destroy = true;
				}else if(msg.equals("确认更改站点？")){
					show_dialog_ing = true;
					dialog_next_click_button(NEXT_SITECODE);
				}else if(msg.equals("确认更改包号？")){
					show_dialog_ing = true;
					dialog_work_click_button();
				}else if (msg.equals("确认退出？")) {
					PackscanActivity.this.finish();
				}else if(msg.equals("Token失效，确定重新获取？")){
					Login_again();
				}
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(msg.equals("还有未上传数据，确认上传？")){
					PackscanActivity.this.finish();
				}else if(msg.equals("确认更改站点？")){
					pretname.setText(PRE_STATION);
					show_dialog_ing = true;
					check_sitecode_flag = true;
				}else if(msg.equals("确认更改任务单？")){
					workorder.setText(WORK_PRE);
					check_sitecode_flag = true;
					show_dialog_ing = true;
				}else {
					show_dialog_ing = true;
				}
			}
		});
		builder.create().show();
	}

	public boolean send_up_destroy = false;
	public void befor_destroy(){
		if(!is_sending_up){
			send_FaJian_parames(0);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		WeakUPScreen.onResume();
		check_sitecode_flag = true;
		registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mPreferences.edit().putInt("unSend_count_msg_jibao", unSend_count_msg).commit();
		close_timer();
		WeakUPScreen.onDestroy();
		NEXT_SITECODE = "";
		unregisterReceiver(mGattUpdateReceiver);
		
	}

	private static IntentFilter makeGattUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
		return intentFilter;
	}
}
