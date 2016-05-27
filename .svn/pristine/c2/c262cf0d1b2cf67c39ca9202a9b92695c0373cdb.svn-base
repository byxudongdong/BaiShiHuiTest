package com.bluetooth;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;


import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import com.example.baishihuitong.R;


public class BluetoothLeService extends Service {
	private final static String TAG = BluetoothLeService.class.getSimpleName();
	private BluetoothManager mBluetoothManager;
	private BluetoothAdapter mBluetoothAdapter;
	private String mBluetoothDeviceAddress, curren_select_address;
	private BluetoothGatt mBluetoothGatt;
	public final static String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
	public final static String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
	public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
//	public final static String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
//	public final static String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";
	public final static String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";
	public final static String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
	//"com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
	public final static String ACTION_POWER_DATA = "com.example.bluetooth.le.ACTION_POWER_DATA";
	public final static int POWER_SPACING_TIME = 5*1000;
	public final static UUID UUID_HEART_RATE_MEASUREMENT = UUID.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT);
	Timer timer;
	public static boolean isFindService = false;
	public Context context;
	
	public void mSetContext(Context context) {
		this.context = context;
	}

	public final static String DEVICE_SERVICE_POWER = "4002530D622A";// 电量获取
	public final static String WRITE_DEVICE_UUID = "0000fff1-0000-1000-8000-00805f9b34fb";
	public final static String Notification_UUID = "0000fff4-0000-1000-8000-00805f9b34fb";
	public BluetoothGattCharacteristic mcharacteristic,
			notifacationCharacteristic;
	private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
		/**
		 * 链接状态改变时回调该方法
		 */
		public void onConnectionStateChange(BluetoothGatt gatt, int status,
				int newState) {
			String intentAction;
			if (newState == BluetoothProfile.STATE_CONNECTED) {
				intentAction = ACTION_GATT_CONNECTED;
				Util.BLE_OPEN_FLAGE = true;
				broadcastUpdate(intentAction);
				// Attempts to discover services after successful connection.
				Log.i(TAG, "Attempting to start service discovery:"
						+ mBluetoothGatt.discoverServices());
				i = 0;
				if (timer != null) {
					timer.cancel();
					timer = null;
					Log.d(">>>>>>timer", "timer关闭");
				}
			} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {

				intentAction = ACTION_GATT_DISCONNECTED;
				SharedPreferences acceptMessage = getSharedPreferences(
						"curren_select_address", Activity.MODE_PRIVATE);
				curren_select_address = acceptMessage.getString(
						"curren_select_address", "");
				Log.i(TAG, "Disconnected from GATT server.");

				// 连接断开后重新发送连接请求
//				if (Util.BLE_OPEN_FLAGE && Util.motion_connect) {
//					timer = new Timer();
//					Log.i("ttttttt1111111111", "new Timer();");
//					timer.schedule(new TimerTask() {
//						public void run() {
//							close();
//							connect(curren_select_address);
//						}
//					}, Util.motion_time_divide, Util.motion_time_divide);
//				} else {
//					close();
//					if (timer != null) {
//						timer.cancel();
//						timer = null;
//					}
//				}
				Util.BLE_OPEN_FLAGE = false;
				broadcastUpdate(intentAction);
				stopPowerThread();
			}
		}

		/**
		 * 发现service时回调该方法
		 */
		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
				isFindService = true;
				discovered_service();
				broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
				if (timer != null) {
					timer.cancel();
				}
			} else {
				Log.w(TAG, "onServicesDiscovered received: " + status);
			}
		}

		public void discovered_service() {
			List<BluetoothGattService> mGattService = Util.mBluetoothLeService
					.getSupportedGattServices();
			for (int j = 0; j < mGattService.size(); j++) {
				List<BluetoothGattCharacteristic> mGattCharacteristics = mGattService
						.get(j).getCharacteristics();
				for (int i = 0; i < mGattCharacteristics.size(); i++) {
					String ss = mGattCharacteristics.get(i).getUuid()
							.toString();
					if (WRITE_DEVICE_UUID.equals(ss)) {
						mcharacteristic = mGattCharacteristics.get(i);
						isFindService = true;
						Util.DIALOG_VIEW_SHOE = true;
						Log.d("WRITE_DEVICE_UUID<<<<<<<<<<", "发现service");
//						sendPowerCharacteristicWrite();
					}

					if (Notification_UUID.equals(ss)) {
						notifacationCharacteristic = mGattCharacteristics
								.get(i);
						final int charaProp = notifacationCharacteristic
								.getProperties();
						if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
							Util.mBluetoothLeService
									.setCharacteristicNotification(
											notifacationCharacteristic, true);
							Util.DIALOG_VIEW_SHOE = true;
							Log.d("Notification_UUID<<<<<<", "发现service");
						}
					}
				}
			}
		}

		/**
		 * 
		 * 当设备通过Notification发送数据过来时，回调该方法
		 */
		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
		}

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			// broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
		}

		/**
		 * 注： 1、某些函数调用之间存在先后关系。例如首先需要connect上才能discoverServices。
		 * 2、一些函数调用是异步的，需要得到的值不会立即返回， 而会在BluetoothGattCallback的回调函数中返回。
		 * 例如discoverServices与onServicesDiscovered回调，——搜索连接设备所支持的service
		 * readCharacteristic与onCharacteristicRead回调，——读取指定的characteristic。
		 * setCharacteristicNotification与onCharacteristicChanged回调等
		 * 。——设置当指定characteristic值变化时，发出通知。
		 */
		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
				broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
			}
		}
	};

	private Thread powerThread ;
	public void sendPowerCharacteristicWrite(){
		powerThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(Util.BLE_OPEN_FLAGE){
					writeCharacteristic();
					try {
						Thread.sleep(POWER_SPACING_TIME);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		powerThread.start();
	}
	public void stopPowerThread(){
		if(null != powerThread){
			powerThread.interrupt();
			powerThread = null;
		}
		
	}
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		super.onCreate();
		Notification notification = new Notification(R.drawable.ic_launcher,
				"启动服务发出通知", System.currentTimeMillis());
		// 设置内容和点击事件
		Intent intent1 = new Intent(this, DeviceScanActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent1, 0);
		notification.setLatestEventInfo(this, "优先级通知", "提高优先级", contentIntent);
		notification.flags = Notification.FLAG_AUTO_CANCEL; // 设置为点击后自动取消
		startForeground(1235, notification);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (timer != null) {
			timer.cancel();
		}
		disconnect();
		close();
		stopForeground(true);
	}

	/**
	 * 更新广播
	 */
	private void broadcastUpdate(final String action) {
		final Intent intent = new Intent(action);
		sendBroadcast(intent);
	}

	final StringBuilder stringBuilder = new StringBuilder();
	int i = 0;
	public void broadcastUpdate(final String action,
			final BluetoothGattCharacteristic characteristic) {
		Log.d("@@@@@@@@@@@@@@@@@@@@@@",
				stringBuilder.toString());
		
		final Intent intent = new Intent(action);
		final Intent power_intent = new Intent(ACTION_POWER_DATA);
		final byte[] data = characteristic.getValue();
		if (data != null && data.length > 0) {
			i ++;
			for (byte byteChar : data)
				stringBuilder.append(String.format("%02X ", byteChar));// 表示以十六进制形式输出,02表示不足两位，前面补0输出；出过两位，不影响
		
			if (stringBuilder.toString().startsWith("40 03 52 0D")) {	
				power_intent.putExtra(EXTRA_DATA, "");
				Log.d("sendBroadcast power_intent", stringBuilder.toString());
				String current = getPowerNumberString(stringBuilder.toString());
				power_intent.putExtra(EXTRA_DATA,current);
				sendBroadcast(power_intent);
				stringBuilder.delete(0, stringBuilder.length());
				return;
			} else {
				if (stringBuilder.toString().contains("53 01 ")
						&& stringBuilder.toString().startsWith("40")
						&& stringBuilder.toString().endsWith("2A ")) {
				
					Log.d("@@@@@@@@11", stringBuilder.toString() + "/////" + i);
					if (checkCurrentNumber(stringBuilder.toString())) {
						i = 0;
						intent.putExtra(
								EXTRA_DATA,
								getCurrentNumberString(stringBuilder.toString()));
						stringBuilder.delete(0, stringBuilder.length());
						
						sendBroadcast(intent);
					} 
					if(i > 2){
						i = 0;
						stringBuilder.delete(0, stringBuilder.length());
					}
					return;
				}
			}
		}
	}

	/**
	 * 校验条码信息
	 * 
	 * @param string
	 * @return
	 */
	public boolean checkCurrentNumber(String string) {
		String[] strs = string.split(" ");
		int[] numbers = new int[strs.length];
		for (int i = 0; i < strs.length; i++) {
			int integer = Integer.parseInt(strs[i], 16);
			numbers[i] = integer;
		}
		int resout = 0;
		// resout ——》数据段 ； numbers[strs.length - 2]——》校验位 ； numbers[1] ——》帧长度；
		for (int i = 1; i < strs.length - 2; i++) {
			resout = resout + numbers[i];
		}
		if (strs.length - 4 == numbers[1]
				&& (resout - numbers[strs.length - 2]) % 16 == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 获得校验后的条码
	 * 
	 * @param string
	 * @return
	 */
	public String getCurrentNumberString(String string) {
		String Current = string.replaceAll(" ", "");
		char[] chars = Current.toCharArray();
		String number = "";
		for (int i = 8; i < chars.length - 6; i++) {
			number += chars[i];
		}
		return toStringHex1(number);
	}

	public String getPowerNumberString(String string) {
		String Current = string.replaceAll(" ", "");
		char[] chars = Current.toCharArray();
		String number = "";
		for (int i = 8; i < chars.length - 4; i++) {
			number += chars[i];
		}
		return number;
	}

	public static String toStringHex1(String s) {
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(
						s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			s = new String(baKeyword, "ASCII");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}

	// 写数据控制戒指电量
	public void writeCharacteristic(){
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
			final int charaProp = mcharacteristic.getProperties();
			if ((charaProp | BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
				byte[] WriteBytes = null;
				WriteBytes = hex2byte(DEVICE_SERVICE_POWER.getBytes());
				mcharacteristic.setValue(WriteBytes);
				Log.d("log_write", DEVICE_SERVICE_POWER);
				mBluetoothGatt.writeCharacteristic(mcharacteristic);
			}
	}

	// 获得写数据的16进制字节
	public byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0) {
			throw new IllegalArgumentException("长度不是偶数");
		}
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			// 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		b = null;
		return b2;
	}

	public class LocalBinder extends Binder{
		BluetoothLeService getService() {
			return BluetoothLeService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	private final IBinder mBinder = new LocalBinder();

	public boolean initialize() {
		// For API level 18 and above, get a reference to BluetoothAdapter
		// through
		// BluetoothManager.
		if (mBluetoothManager == null) {
			mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
			if (mBluetoothManager == null) {
				Log.e(TAG, "Unable to initialize BluetoothManager.");
				return false;
			}
		}
		mBluetoothAdapter = mBluetoothManager.getAdapter();
		if (mBluetoothAdapter == null) {
			Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
			return false;
		}
		return true;
	}

	public boolean connect(final String address) {
		if (mBluetoothAdapter == null || address == null) {
			Log.w(TAG,
					"BluetoothAdapter not initialized or unspecified address.");
			return false;
		}
		if (mBluetoothDeviceAddress != null
				&& address.equals(mBluetoothDeviceAddress)
				&& mBluetoothGatt != null) {
			Log.d(TAG,
					"Trying to use an existing mBluetoothGatt for connection.");
			if (mBluetoothGatt.connect()) {
				return true;
			} else {
				return false;
			}
		}
		/**
		 * 先获取远程设备的引用，然后通过该引用发起链接
		 */
		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);// 远程设备
		if (device == null) {
			Log.w(TAG, "Device not found.  Unable to connect.");
			return false;
		}
		mBluetoothGatt = device.connectGatt(this, false, mGattCallback);// 传入Gatt回调方法
		Log.d(TAG, "Trying to create a new connection.");
		mBluetoothDeviceAddress = address;
		return true;
	}

	public void disconnect() {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.disconnect();
	}

	public void close() {
		if (mBluetoothGatt == null) {
			return;
		}
		mBluetoothGatt.close();
		mBluetoothGatt = null;
	}

	public void setCharacteristicNotification(
			BluetoothGattCharacteristic characteristic, boolean enabled) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		/**
		 * 设置当指定characteristic值变化时，发出通知。
		 */
		mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

	}

	public List<BluetoothGattService> getSupportedGattServices() {
		if (mBluetoothGatt == null)
			return null;

		return mBluetoothGatt.getServices();
	}
}
