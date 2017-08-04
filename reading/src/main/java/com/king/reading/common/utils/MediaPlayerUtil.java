package com.king.reading.common.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class MediaPlayerUtil {
	private final static String TAG = "MediaPlayerUtil";

	public static MediaPlayer INSTANCE;

	public static MediaPlayer getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new MediaPlayer();
		}
		return INSTANCE;
	}

	// 播放assets文件夹下的音频
	public static void play(final Context context, final String assetUrl) {
		AssetFileDescriptor descriptor = null;
		try {
			descriptor = context.getAssets().openFd(assetUrl);
			getInstance().reset();
			Log.e(TAG, "reset");
			getInstance().setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
			Log.e(TAG, "setDataSource");
			getInstance().prepareAsync();
			Log.e(TAG, "prepare");
			getInstance().setOnPreparedListener(new OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					// TODO Auto-generated method stub
					Log.d(TAG, "preparedfinished");
					mp.start();
				}
			});
			getInstance().setOnErrorListener(new OnErrorListener() {

				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					// TODO Auto-generated method stub
					Log.e(TAG, "onError");
					mp.stop();
					return false;
				}
			});
			getInstance().setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					mp.stop();
					mp.setOnCompletionListener(null);
				}
			});

		} catch (Exception e) {
			Log.e(TAG, "catch");
			e.printStackTrace();
		} finally {
			try {
				if (descriptor != null)
					descriptor.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 播放assets文件夹下的音频
	public static void play1(final Context context, final String assetUrl) {
		AssetFileDescriptor descriptor = null;
		try {
			MediaPlayer mediaPlayer = new MediaPlayer();// 不使用单例模式，不会和跟读中的音频播放有冲突
			descriptor = context.getAssets().openFd(assetUrl);
			mediaPlayer.reset();
			Log.e(TAG, "reset");
			mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
			Log.e(TAG, "setDataSource");
			mediaPlayer.prepareAsync();
			Log.e(TAG, "prepare");
			mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mp) {
					// TODO Auto-generated method stub
					Log.d(TAG, "preparedfinished");
					mp.start();
				}
			});
			mediaPlayer.setOnErrorListener(new OnErrorListener() {

				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					// TODO Auto-generated method stub
					Log.e(TAG, "onError");
					mp.stop();
					return false;
				}
			});
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					mp.stop();
					mp.setOnCompletionListener(null);
				}
			});

		} catch (Exception e) {
			Log.e(TAG, "catch");
			e.printStackTrace();
		} finally {
			try {
				if (descriptor != null)
					descriptor.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 播放SD卡文件夹下的音频
	public static void playFromSdCard(Context context, final String SdUrl) {
		try {
			String soundPath = SdUrl;
			Log.e(TAG, "soundPath:" + soundPath);
			File sound = new File(soundPath);
			if (sound.exists()) {
				getInstance().reset();
				Log.e(TAG, "reset");
				getInstance().setDataSource(soundPath);
				Log.e(TAG, "setDataSource");
				getInstance().prepare();
				// getInstance().prepareAsync();
				Log.e(TAG, "prepare");
				// getInstance().start();
				getInstance().setOnPreparedListener(new OnPreparedListener() {

					@Override
					public void onPrepared(MediaPlayer mp) {
						// TODO Auto-generated method stub
						Log.d(TAG, "preparedfinished");
						mp.start();
					}
				});
				getInstance().setOnErrorListener(new OnErrorListener() {

					@Override
					public boolean onError(MediaPlayer mp, int what, int extra) {
						// TODO Auto-generated method stub
						Log.e(TAG, "onError");
						mp.stop();
						return false;
					}
				});
			}

		} catch (Exception e) {
			Log.e(TAG, "catch");
			e.printStackTrace();
		}
	}

	// 播放网络音频
	public static void playFromIntenet(Context context, final String url) {
		try {
			Log.e(TAG, "url:" + url);
			getInstance().reset();
			Log.e(TAG, "reset");
			getInstance().setDataSource(url);
			Log.e(TAG, "setDataSource");
			getInstance().prepare();
			Log.e(TAG, "prepare");
			getInstance().setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mp) {
					// TODO Auto-generated method stub
					Log.d(TAG, "preparedfinished");
					mp.start();
				}
			});
			getInstance().setOnErrorListener(new OnErrorListener() {

				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					// TODO Auto-generated method stub
					Log.e(TAG, "onError");
					mp.stop();
					return false;
				}
			});

		} catch (Exception e) {
			Log.e(TAG, "catch");
			e.printStackTrace();
		}
	}

	public static void start() {
		if (getInstance() != null) {
			if (!getInstance().isPlaying())
				getInstance().start();
		}
	}

	public static void pause() {
		if (getInstance() != null) {
			if (getInstance().isPlaying())
				getInstance().pause();
		}
	}

	// 停止播放
	public static void stop() {
		if (getInstance() != null) {
			if (getInstance().isPlaying())
				getInstance().stop();
			getInstance().setOnCompletionListener(null);
		}
	}

	// // 获取跟读课文音频文件的总播放时长
	// public static int getMP3DurationFromPath(List<Question> list) {
	// Log.e(TAG, "获取跟读课文音频文件的总播放时长");
	// int duration = 0;
	// for (int i = 0; i < list.size(); i++) {
	// duration += getMP3Duration(list.get(i).getMp3Url());
	// }
	// return duration;
	// }

	// 获取指定音频文件时长
	public static int getMP3Duration(String path) {
		Log.e(TAG, "获取指定音频文件时长" + path);
		int duration = 0;
		
		File sound = new File(path);
		if (sound.exists()) {
			MediaPlayer mediaPlayer = new MediaPlayer();
			mediaPlayer.reset();
			try {
				mediaPlayer.setDataSource(path);
				mediaPlayer.prepare();
				duration = mediaPlayer.getDuration();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.stop();
				}
			}
		}
		return duration;
	}
}
