package com.bluetooth;

import com.bluetooth.SlideSwitchView.OnSwitchChangedListener;
import com.example.baishihuitong.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


public class BluetoothSearchActivity extends Activity{

	public Button search_blue_service;
	public TextView scan_connection_device,peidui_text ,scan_connection_name;
	public SlideSwitchView switch2;
	public String curren_select_address ,curren_select_name;
	public boolean unbind_service,flag_open;
	private ScrollView search_scrollView;
	private float mLastX = -1; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.bluetooth_search_activity);
		SetActionBars actionbar = new SetActionBars(BluetoothSearchActivity.this, "搜索设备");
		actionbar.setActionBars();
		/**
		 * 初始化Bluetooth adapter，通过BluetoothManager获得引用
		 */
		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
//		getActionBar().hide();
		search_blue_service = (Button) findViewById(R.id.search_blue_service);
		search_blue_service.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(mBluetoothAdapter.isEnabled()){
					Intent intent = new Intent(BluetoothSearchActivity.this,
							DeviceScanActivity.class);
					startActivity(intent);
				}else{
					Toast.makeText(BluetoothSearchActivity.this, R.string.bluetooth_open,
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		SharedPreferences acceptMessage = BluetoothSearchActivity.this.getSharedPreferences("curren_select_address", Activity.MODE_PRIVATE);
  		curren_select_address = acceptMessage.getString("curren_select_address", "");
  		curren_select_name = acceptMessage.getString("curren_select_name", "");
  		
  		scan_connection_device = (TextView) findViewById(R.id.scan_connection_device);
  		scan_connection_name = (TextView) findViewById(R.id.scan_connection_name);
  		
  		scan_connection_name.setText( curren_select_name );
  		scan_connection_device.setText(curren_select_address );
		switch2 = (SlideSwitchView) findViewById(R.id.lanya_shezhi);
		switch2.setOnChangeListener(new OnSwitchChangedListener() {
			
			public void onSwitchChange(SlideSwitchView switchView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
//					mBluetoothAdapter.enable();
					unbind_service = false;
					flag_open = true; 
				} else {
					Util.BLE_OPEN_FLAGE = false;
//					mBluetoothAdapter.disable();
					if (Util.mBluetoothLeService!=null) {
						unbind_service = true;
						Util.mBluetoothLeService.disconnect();
						Util.mBluetoothLeService.stopSelf();
					}
					flag_open = false;
				}
			}
		});

		search_scrollView = (ScrollView) findViewById(R.id.search_scrollView);
		search_scrollView.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mLastX = event.getRawX();
					break;
				case MotionEvent.ACTION_MOVE:
					final float deltaX = event.getRawX() - mLastX;
					if(deltaX > 200 && mLastX > 0 && mLastX < 100){
						finish();
					}
					break;
				}
				return false;
			}
		});

		
	}
	private BluetoothAdapter mBluetoothAdapter; // 蓝牙适配器
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (Util.BLE_OPEN_FLAGE) {
			switch2.setChecked(true);
		}else{
			switch2.setChecked(false);
		}
		SharedPreferences acceptMessage = BluetoothSearchActivity.this.getSharedPreferences("curren_select_address", Activity.MODE_PRIVATE);
  		curren_select_address = acceptMessage.getString("curren_select_address", "");
  		curren_select_name = acceptMessage.getString("curren_select_name", "");
  		
  		scan_connection_name.setText( curren_select_name );
  		scan_connection_device.setText(curren_select_address );
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
