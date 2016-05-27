package com.bluetooth;

/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import com.example.baishihuitong.R;


/**
 * Activity for scanning and displaying available Bluetooth LE devices.
 */
public class DeviceScanActivity extends Activity {
	private LeDeviceListAdapter mLeDeviceListAdapter; // 服务列表适配器
	private BluetoothAdapter mBluetoothAdapter; // 蓝牙适配器
	private Handler mHandler; // 扫描延时
	private ListView bluetooth_list_scan;
	private static final int REQUEST_ENABLE_BT = 1; // 请求码
	// Stops scanning after 10 seconds.
	private static final long SCAN_PERIOD = 10000; // 延时时间
	private String  curren_select_address = "";

	private LinearLayout dialog_progress_line;
	private BluetoothLeService mBluetoothLeService;
	/**
	 * Service和Activity的连接
	 */
	private final ServiceConnection mServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName componentName,
									   IBinder service) {
			mBluetoothLeService = ((BluetoothLeService.LocalBinder) service)
					.getService();
			mBluetoothLeService.mSetContext(DeviceScanActivity.this);
			if (!mBluetoothLeService.initialize()) {
				finish();
			}
		}
		public void onServiceDisconnected(ComponentName componentName) {
		}
	};

	/**
	 * 广播接收器
	 */
	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();

			if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
				Log.i("AAAA", "AA执行！");
				invalidateOptionsMenu();
				view_visible = true;
			} else if (BluetoothLeService.ACTION_GATT_DISCONNECTED
					.equals(action)) {
				Log.i("AAAA", "bb执行！");
				invalidateOptionsMenu();
				Toast.makeText(DeviceScanActivity.this, R.string.connect_close,
						Toast.LENGTH_SHORT).show();
				dialog_progress_line.setVisibility(View.GONE);
			} else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED
					.equals(action)) {
				if (Util.DIALOG_VIEW_SHOE) {
					dialog_progress_line.setVisibility(View.GONE);
					Util.DIALOG_VIEW_SHOE = false;
				}
			}
		}
	};

//	public PlayBeepSound play_beep_sound;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHandler = new Handler();
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.setContentView(R.layout.bluetooth_scan_activity);
		//设置Actionbar
		SetActionBars actionbar = new SetActionBars(DeviceScanActivity.this, "配置指环");
		actionbar.setActionBars();

//		// 初始化声音和震动
//		play_beep_sound = new PlayBeepSound(DeviceScanActivity.this);
//		play_beep_sound.init_beep_sound();
		
		dialog_progress_line = (LinearLayout) findViewById(R.id.dialog_progress_line);
		dialog_progress_line.setVisibility(View.GONE);
		Intent gattServiceIntent = new Intent(DeviceScanActivity.this,
				BluetoothLeService.class);
		startService(gattServiceIntent);
		bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

		/**
		 * 检查装置是否支持BLE协议
		 */
		if (!getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT)
					.show();
			finish();
		}

		// Initializes a Bluetooth adapter. For API level 18 and above, get a
		// reference to一个
		// BluetoothAdapter through BluetoothManager.
		/**
		 * 初始化Bluetooth adapter，通过BluetoothManager获得引用
		 */
		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();

		// Checks if Bluetooth is supported on the device.

		/**
		 * 检查装置是否支持蓝牙
		 */
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, R.string.error_bluetooth_not_supported,
					Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
	}

	/**
	 * 当菜单有命令被选择时调用，同时更新服务列表适配器
	 */
	/*
	 * @Override public boolean onOptionsItemSelected(MenuItem item) { switch
	 * (item.getItemId()) { case R.id.menu_scan: mLeDeviceListAdapter.clear();
	 * scanLeDevice(true); break; case R.id.menu_stop: scanLeDevice(false);
	 * break; } return true; }
	 */

	/**
	 * 当activity恢复时，确保蓝牙已启用
	 */
	protected void onResume() {
		super.onResume();
		if(mBluetoothAdapter.isEnabled()){
			xxxx();
		}
		registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
	}

	private static IntentFilter makeGattUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
		intentFilter
				.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
		return intentFilter;
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x00) {
				dialog_progress_line.setVisibility(View.GONE);
				if(!BluetoothLeService.isFindService){
					mBluetoothLeService.disconnect();
					mBluetoothLeService.close();
				}	
			}
		};
	};

	public void saveShardSignMessage(String name, String address) {
		SharedPreferences settings = DeviceScanActivity.this
				.getSharedPreferences("curren_select_address",
						Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("curren_select_name", name);
		editor.putString("curren_select_address", address);
		editor.commit();
	}

	private boolean view_visible = true;
	public static final long TIME_SCAN = 20000;// 扫描服务最长时间

	public void xxxx() {

		// Ensures Bluetooth is enabled on the device. If Bluetooth is not
		// currently enabled,
		// fire an intent to display a dialog asking the user to grant
		// permission to enable it.
		if (!mBluetoothAdapter.isEnabled()) {
			if (!mBluetoothAdapter.isEnabled()) {
				Intent enableBtIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}
		}
		// Initializes list view adapter.

		/**
		 * 初始化服务列表
		 */
		mLeDeviceListAdapter = new LeDeviceListAdapter();

		bluetooth_list_scan = (ListView) this
				.findViewById(R.id.bluetooth_list_scan);
		bluetooth_list_scan.setAdapter(mLeDeviceListAdapter);
		bluetooth_list_scan
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> arg0, View arg1,
											int arg2, long arg3) {
						if (!Util.BLE_OPEN_FLAGE && view_visible) {
							final BluetoothDevice device = mLeDeviceListAdapter
									.getDevice(arg2);

							// scan_connection_device.setText(device.getName());
							curren_select_address = device.getAddress();
							if (mBluetoothLeService != null) {
								mBluetoothLeService
										.connect(curren_select_address);
							}
							saveShardSignMessage(device.getName(),
									device.getAddress());
							Util.mBluetoothLeService = mBluetoothLeService;
							
//							mBluetoothLeService.setMyBeepSound(play_beep_sound);
							if (view_visible) {
								dialog_progress_line
										.setVisibility(View.VISIBLE);
								view_visible = false;
								new Thread(new Runnable() {
									public void run() {
										try {
											Thread.sleep(TIME_SCAN);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										handler.sendEmptyMessage(0x00);
									}
								}).start();
							}
						}else {
							Toast.makeText(DeviceScanActivity.this, R.string.menu_disconnect_new,
									Toast.LENGTH_SHORT).show();
						}
					}
				});
		scanLeDevice(true);
	}

	/**
	 * 创建菜单
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.gatt_services, menu);
		if (Util.BLE_OPEN_FLAGE) {
			menu.findItem(R.id.menu_connect).setVisible(false);
//			menu.findItem(R.id.power_flag).setVisible(false);
			menu.findItem(R.id.menu_disconnect).setVisible(true);
		} else {
			menu.findItem(R.id.menu_connect).setVisible(true);
//			menu.findItem(R.id.power_flag).setVisible(false);
			menu.findItem(R.id.menu_disconnect).setVisible(false);
		}
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				break;
			case R.id.menu_disconnect:
//			Util.mBluetoothLeService.disconnect();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 重写onActivityResult()来处理返回的数据
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_ENABLE_BT
				&& resultCode == Activity.RESULT_CANCELED) {
			finish();
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/* *//**
	 * 当Activity暂停时，停止扫描并清空设备服务列表适配器
	 */
	@Override
	protected void onPause() {
		super.onPause();
		scanLeDevice(false);
	}

	/**
	 * 搜索BLE设备 1、当找到对应的设备后，立即停止扫描；
	 * 2、不要循环搜索设备，为每次搜索设置适合的时间限制。避免设备不在可用范围的时候持续不停扫描，消耗电量。
	 *
	 * @param enable
	 */
	private void scanLeDevice(final boolean enable) {
		if (enable) {
			/**
			 * 找到对应的设备后，立即停止扫描
			 */
			mHandler.postDelayed(new Runnable() {
				public void run() {
					mBluetoothAdapter.stopLeScan(mLeScanCallback);
					invalidateOptionsMenu();
				}
			}, SCAN_PERIOD);

			/**
			 * 启动
			 */
			mBluetoothAdapter.startLeScan(mLeScanCallback);
		} else {
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
		}
		invalidateOptionsMenu();
	}

	// Adapter for holding devices found through scanning.
	/**
	 * 设备服务列表适配器
	 *
	 * @author Administrator
	 */
	private class LeDeviceListAdapter extends BaseAdapter {
		private ArrayList<BluetoothDevice> mLeDevices;
		private LayoutInflater mInflator;

		public LeDeviceListAdapter() {
			super();
			mLeDevices = new ArrayList<BluetoothDevice>();
			mInflator = DeviceScanActivity.this.getLayoutInflater();
		}

		public void addDevice(BluetoothDevice device) {
			if (!mLeDevices.contains(device)) {
				mLeDevices.add(device);
			}
		}

		public BluetoothDevice getDevice(int position) {
			return mLeDevices.get(position);
		}

		public void clear() {
			mLeDevices.clear();
		}

		public int getCount() {
			return mLeDevices.size();

		}

		public Object getItem(int i) {
			return mLeDevices.get(i);
		}

		public long getItemId(int i) {
			return i;
		}

		public View getView(int i, View view, ViewGroup viewGroup) {
			ViewHolder viewHolder;

			if (view == null) {
				view = mInflator.inflate(R.layout.listitem_device, null);
				viewHolder = new ViewHolder();
				viewHolder.deviceAddress = (TextView) view
						.findViewById(R.id.device_address);
				viewHolder.deviceName = (TextView) view
						.findViewById(R.id.device_name);
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}

			BluetoothDevice device = mLeDevices.get(i);
			final String deviceName = device.getName();
			if (deviceName != null && deviceName.length() > 0)
				viewHolder.deviceName.setText(deviceName);
			else
				viewHolder.deviceName.setText(R.string.unknown_device);
			viewHolder.deviceAddress.setText(device.getAddress());

			return view;
		}
	}

	/**
	 * 实现 BluetoothAdapter.LeScanCallback 接口
	 */
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

		@Override
		public void onLeScan(final BluetoothDevice device, int rssi,
							 byte[] scanRecord) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mLeDeviceListAdapter.addDevice(device);
					mLeDeviceListAdapter.notifyDataSetChanged();
				}
			});
		}
	};

	static class ViewHolder {
		TextView deviceName;
		TextView deviceAddress;
	}

	protected void onDestroy() {
		super.onDestroy();
		unbindService(mServiceConnection);
		unregisterReceiver(mGattUpdateReceiver);
	}

}