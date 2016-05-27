package com.example.baishihuitong;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import sendupSet.SendupSet;
import sendupSet.SystemServiceSendSet;
import userMessage.ReadFileSN;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baishi.db.cn.SaveShardMessage;
import com.baishihuitong.untils.HttpUrl;
import com.baishihuitong.untils.Login;
import com.baishihuitong.untils.MD5transformation;

public class LoginActivity extends Activity {
	private Button login, xinxi;
	public static String result = "";
	private EditText name;
	private EditText password;
	private EditText companycode;
	private TextView userText;
	private TextView nametext;
	private SharedPreferences mPreferences;
	public SaveShardMessage saveShard;
	public LinearLayout login_dialog_progress_line;
	public boolean logining_flag = false, login_success = false;

	public final String PASSWORD = "800best";
	public Thread m_thread;
	public static String http_util = "";
	private InputMethodManager mIM;
	public Toast toast;
	 public static String DEVICE_ID = "";
	 public static String PASS_WORD = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 取消状态栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main);
		DEVICE_ID =  ReadFileSN.ReadFile();
		init();
		mIM = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		login.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				longin_asyncTask();
//				startActivity(new Intent(LoginActivity.this,
//						MenuMainActivity.class));
			}
		});

		xinxi.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(LoginActivity.this,"服务器修改口令");
			}
		});
		companycode.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				companycode.setSelection(companycode.getText().toString()
						.length());
			}
		});
		companycode.setOnKeyListener(new OnKeyListener() {
			
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_ENTER)
					longin_asyncTask();
				return false;
			}
		});

		companycode.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if(s.length() > 6){
					showTextToast("网点编码为6位数字");
				}
			}
		});
		password.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				password.setSelection(password.getText().toString().length());
			}
		});

		name.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				name.setSelection(name.getText().toString().length());
			}
		});

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			showDialog(LoginActivity.this,"进入管理员界面");
		}
		return super.onKeyDown(keyCode, event);
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
	        toast.setGravity(Gravity.CENTER, 0, 0);
	        toast.show();
	        
	    }
	public void longin_asyncTask(){
		if (!logining_flag) {
			if (name.getText().toString().length() > 0
					&& password.getText().toString().length() > 0) {
				if (companycode.getText().toString().length() == 6) {
					login_dialog_progress_line
							.setVisibility(View.VISIBLE);
					logining_flag = true;
					PASS_WORD =MD5transformation.MD5(password.getText().toString());
					new getListTask( name.getText().toString(),PASS_WORD,companycode.getText().toString()).execute();
					m_thread = new Thread(new Runnable() {
						public void run(){
							try {
								Thread.sleep(8000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							handler.sendEmptyMessage(0x001);
						}
					});
					m_thread.start();
				} else {
					showTextToast("网点编码为6位数字");
				}
			} else {
				showTextToast("用户名密码不能为空！");
			}
		}
	}
	
	public void showDialog(Context context,final String title) {
		AlertDialog.Builder builder = new Builder(context);
		final EditText edit = new EditText(LoginActivity.this);
		builder.setView(edit);
		builder.setTitle(title);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(edit.getText().toString().equals(PASSWORD) && title.equals("服务器修改口令"))
					startActivity(new Intent(LoginActivity.this,
							SystemServiceSendSet.class));
				else if(edit.getText().toString().equals(PASSWORD) && title.equals("进入管理员界面")){
					LoginActivity.this.finish();
				}else{
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
	

	public void miss_focuse() {
		name.setEnabled(false);
	}

	public void init() {
		login = (Button) findViewById(R.id.loginbtn);
		xinxi = (Button) findViewById(R.id.xinxi);
		name = (EditText) findViewById(R.id.name);
		password = (EditText) findViewById(R.id.password);
		companycode = (EditText) findViewById(R.id.wangname);
		userText = (TextView) findViewById(R.id.userText);
		nametext = (TextView) findViewById(R.id.nametext);
		login_dialog_progress_line = (LinearLayout) findViewById(R.id.login_dialog_progress_line);
		login_dialog_progress_line.setVisibility(View.GONE);
		mPreferences = getSharedPreferences("mode", MODE_PRIVATE);
		userText.setText(mPreferences.getString("username", "未定义用户名"));
		nametext.setText(mPreferences.getString("sitename", "未定义站点"));
		name.setText(mPreferences.getString("X-Auth-User", ""));
//		name.setInputType(InputType.TYPE_NULL);
//		password.setInputType(InputType.TYPE_NULL);
//		companycode.setInputType(InputType.TYPE_NULL);
		companycode.setText(mPreferences.getString("X-Auth-Site", ""));
		saveShard = new SaveShardMessage(LoginActivity.this);
		http_util = saveShard.getsystem_service_flag(LoginActivity.this);
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x001) {
				if (!login_success) {
					logining_flag = false;
					login_dialog_progress_line.setVisibility(View.GONE);
					Toast.makeText(LoginActivity.this, "登录验证失败！",
							Toast.LENGTH_SHORT).show();
					m_thread.interrupt();
				} else {
					m_thread.interrupt();
					login_success = false;
				}
			}else if(msg.what == 0x002){
				startActivity(new Intent(LoginActivity.this,
						MenuMainActivity.class));
			}
		};
	};

	/**
	 * 异步任务 {"issuccess":true,"validationflag":-1,"validationmessage":"验证通过",
	 * "needtwovalidation":false,"hsuserflag":0,"hsusermessage":"",
	 * "hsuserguid":"eca85b7c-0a20-42b4-a5db-8baad480197f",
	 * "usercode":"999997.003","username":"小张","sitecode":"999997",
	 * "sitename":"站点B","sitetype":1,"sitetypename":"一级站点",
	 * "clientsitetypecode":1,"token":"da55fbae-ee2e-4374-aadb-9aee5c7d40b0",
	 * "innerexception"
	 * :null,"settlementsitecode":"999997","isweakpassword":false}
	 * 
	 * @author Administrator
	 * 
	 */
	class getListTask extends AsyncTask<Void, Void, String> {
		public String _userName;
		public String _password;
		public String _siteCode;
		
		public getListTask(String _userName, String _password, String _siteCode) {
			super();
			this._userName = _userName;
			this._password = _password;
			this._siteCode = _siteCode;
		}
		protected String doInBackground(Void... params) {
			HttpUrl httpUrl = new HttpUrl();
			Map<String, String> paramsmap = new HashMap<String, String>();
			paramsmap.put("version", "1");
			paramsmap.put("userName", "\"" + _userName + "\"");
			paramsmap.put("password", "\"" + _password+ "\"");
			paramsmap.put("siteCode", "\"" + _siteCode + "\"");
			paramsmap.put("guid", "\""+DEVICE_ID+"\"");
			paramsmap.put("location", "");
			paramsmap.put("mac", "");
			paramsmap.put("imei", "");
			paramsmap.put("imsi", "");
			String params_length = httpUrl.mapToString(paramsmap);
			Map<String, String> headParams = new HashMap<String, String>();
			headParams.put("Content-Type", "application/x-www-form-urlencoded");
			// url =
			// http://handset2.appl.800best.com/hyco/v1/User/HycoValidateUser
			// http://opendev.appl.800best.com/hscedev/v1/User/HycoValidateUser
			String result = httpUrl.post(http_util
					+ "/v1/User/HycoValidateUser", paramsmap,
					headParams);
			Log.d("登录", http_util);
			return result;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (null != result && result.length() > 0) {
				JSONObject mJsonObject;
				Login mLogin = new Login();
				try {
					mJsonObject = new JSONObject(result);
					String validationmessage = mJsonObject
							.getString("validationmessage");
					String token = mJsonObject.getString("token");
					String sitecode = mJsonObject.getString("sitecode");
					String sitename = mJsonObject.getString("sitename");
					String usercode = mJsonObject.getString("usercode");
					String sitetypename = mJsonObject.getString("sitetypename");
					String username = mJsonObject.getString("username");
					mLogin.setValidationmessage(validationmessage);
					mLogin.setToken(token);
					mLogin.setSitecode(sitecode);
					mLogin.setSitename(sitename);
					mLogin.setSitetypename(sitetypename);
					mLogin.setUsercode(usercode);
					mPreferences.edit().putString("username", username)
							.commit();
					mPreferences.edit().putString("sitename", sitename)
							.commit();
					mPreferences.edit().putString("token", token).commit();

				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (null != mLogin.getValidationmessage()) {
					login_success = true;
					logining_flag = false;
					if (mLogin.getValidationmessage().equals("验证通过")) {
						m_thread.interrupt();
						login_dialog_progress_line.setVisibility(View.GONE);
						mPreferences
								.edit()
								.putString("X-Auth-Site",
										companycode.getText().toString())
								.commit();
						mPreferences
								.edit()
								.putString("X-Auth-User",
										name.getText().toString()).commit();
						SaveShardMessage.setLogin_token(mLogin.getToken());
						Toast.makeText(getApplicationContext(), "登录成功！",
								Toast.LENGTH_SHORT).show();
						userText.setText(mPreferences.getString("username",
								"未定义用户名"));
						nametext.setText(mPreferences.getString("sitename",
								"未定义站点"));
						handler.sendEmptyMessage(0x002);
					} else if (mLogin.getValidationmessage().equals("用户名不存在")) {
					
						login_dialog_progress_line.setVisibility(View.GONE);
						Toast.makeText(getApplicationContext(), "用户名不存在！",
								Toast.LENGTH_SHORT).show();
					}else if (mLogin.getValidationmessage().equals("用户名密码不匹配")) {
						login_dialog_progress_line.setVisibility(View.GONE);
						Toast.makeText(getApplicationContext(), "用户名密码不匹配",
								Toast.LENGTH_SHORT).show();
					} else {
						login_dialog_progress_line.setVisibility(View.GONE);
						Toast.makeText(getApplicationContext(), "登录失败！",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		password.setText("");
		super.onResume();
	}
}
