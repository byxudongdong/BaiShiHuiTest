package sendupSet;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;

public class WeakUPScreen {
	static PowerManager.WakeLock wakeLock;

	// public static void wakeUpAndUnlock(Context context){
	//
	// //获取电源管理器对象
	// PowerManager pm=(PowerManager)
	// context.getSystemService(Context.POWER_SERVICE);
	// if(!pm.isScreenOn()){
	// KeyguardManager km= (KeyguardManager)
	// context.getSystemService(Context.KEYGUARD_SERVICE);
	// KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
	// //解锁
	// kl.disableKeyguard();
	// //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
	// wakeLock = pm.newWakeLock( PowerManager.SCREEN_DIM_WAKE_LOCK,"bright");
	// //点亮屏幕
	// wakeLock.acquire(300000);
	// //释放
	// wakeLock.release();
	// }
	// onScan();
	// }
	public static void oncreate(Context context) {
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,
				"MyWakeLock");
	}

	public static void onScan(Context context) {
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		if (!pm.isScreenOn()) {
			KeyguardManager km = (KeyguardManager) context
					.getSystemService(Context.KEYGUARD_SERVICE);
			KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
			// 解锁
			kl.disableKeyguard();
			// 获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
			wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,
					"bright");
			// 点亮屏幕
			wakeLock.acquire();
			// 释放
			wakeLock.release();
		} else
			wakeLock.acquire(300000);
	}

	public static void onResume() {
		if (!wakeLock.isHeld())
			wakeLock.acquire(300000);
	}

	public static void onDestroy() {
		if (wakeLock.isHeld())
			wakeLock.release();
	}
}
