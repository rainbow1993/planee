package com.example.domain;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class Audio {
	//音效ID
	public static int effcet_ui_ananniu;
	public static int effcet_sfx_xiaobaozha;
	public static int effcet_sfx_zhongbaozha;
	
	//资源管理器
	private AssetManager assetManager;
	//音效播放器
	private SoundPool soundPool;
	//媒体播放器
	private MediaPlayer mediaPlayer;
	//背景音乐路径
	private String musicSrc;
	//背景音乐循环标识
	private boolean loop;
	
	public Audio(Context context){
		assetManager = context.getAssets();
		//音乐初始化
		mediaPlayer = new MediaPlayer();
		soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		init();
	}
	
	/*
	 * 初始化
	 */
	public void init(){
		effcet_ui_ananniu = loadSound("effcet_ui_ananniu.ogg");
		effcet_sfx_xiaobaozha = loadSound("effcet_sfx_xiaobaozha.ogg");
		effcet_sfx_zhongbaozha = loadSound("effcet_sfx_zhongbaozha.ogg");
	}
	/*
	 * 播放音效
	 */
	public void playSound(int id){
		soundPool.play(id, 1, 1, 0, 0, 1);
	}
	/*
	 * 载入音效
	 */
	private int loadSound(String src){
		try {
			return soundPool.load(assetManager.openFd(src), 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	/*
	 * 设置背景音乐
	 */
	public void setMusic(String musicSrc){
		this.musicSrc = musicSrc;
		//每次设置音乐时，都会重置mediaplayer
		mediaPlayer.reset();
		try {
			AssetFileDescriptor afd = assetManager.openFd(musicSrc);
			mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("音乐资源加载失败");
		}
	}
	/*
	 * 开始播放背景音乐
	 */
	public void start(){
		if(!mediaPlayer.isPlaying()){
			try {
				mediaPlayer.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mediaPlayer.start();
		}
	}
	/*
	 * 停止播放背景音乐
	 */
	public void stop(){
		if(mediaPlayer.isPlaying()){
			try {
				mediaPlayer.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mediaPlayer.stop();
		}
	}
	public void pause(){
		try {
			mediaPlayer.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mediaPlayer.pause();
	}
	/*
	 * 背景音乐循环模式
	 */
	public void setLoop(boolean loop){
		mediaPlayer.setLooping(loop);
	}
	/*
	 * 得到当前播放器实例
	 */
	public MediaPlayer getMediaPlayer(){
		return mediaPlayer;
	}
	/*
	 * 释放内存
	 **/
	public void release(){
		mediaPlayer.release();
//		soundPool.release();
	}
}
