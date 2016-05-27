package com.example.baishihuitong;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.content.Context;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

public class PlayBeepSound {

	private Context mContext;

	public PlayBeepSound(Context mContext) {
		super();
		this.mContext = mContext;
		init_beep_sound();
	}
	
	public void init_beep_sound() {
		initBeepSound();
	}

	private SoundPool mSoundPool;
	private int playingId = 0;
	private static Map<Integer,Integer> soundMap = new HashMap<Integer,Integer>();

	/**
	 * 初始化声音
	 */
	private void initBeepSound() {
		mSoundPool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 5);
		Log.i("ppppp","initBeepSound soundMap = "+soundMap);
		soundMap.put(0 , mSoundPool.load(mContext, R.raw.test_4k_8820_200ms , 1));
		soundMap.put(1 , mSoundPool.load(mContext, R.raw.test_4k_8820_2, 1));
		
	}

	public void playButtonSound(int code){
		if(mSoundPool == null) return;
		if(playingId !=0 )mSoundPool.stop(playingId);
		playingId = 0;
		if(soundMap.get(code) != null){
			playingId = mSoundPool.play(soundMap.get(code), 1, 1, 0, 0, 1);
		}
	}

	/**
	 * 播放声音和震动
	 */
	public void playBeepSoundAndVibrate(int index) {
		if(mSoundPool != null && soundMap.get(index) != null){
			mSoundPool.play(soundMap.get(index), 1, 1, 0, 0, 1);
		}
	}

}

