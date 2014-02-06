package com.example.planee;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.example.domain.Assets;
import com.example.domain.Audio;
import com.example.face.DisplayView;

public class Loading implements DisplayView{
	//背景图
	private Bitmap backgound;
	//开始按钮图、坐标、是否按下
	private Bitmap start_button;
	private float start_button_x, start_button_y;
	private boolean start_button_touch = false;
	//飞机面板图、坐标
	private Bitmap face;
	private float face_x, face_y;
	//游戏LOGO图、坐标
	private Bitmap logo;
	private float logo_x, logo_y;
	//缩放因子
	private float scanX = 1f;
	private float scanY = 1f;
	//GameView引用
	private GameView gameView;
	//得到GameView中的资源实例
	private Assets assets;
	//得到GameView中的音乐播放器实例
	private Audio audio;
	
	/*
	 * 构造方法
	 * */
	public Loading(GameView gameView){
		this.gameView = gameView;
		
		assets = gameView.getAssets();
		audio = gameView.getAudio();
		
		backgound = assets.backgound;
		start_button = assets.start_button;
		face = assets.face;
		logo = assets.logo;
		
		face_x = 0;
		face_y = scanY * (854- face.getHeight());
		//GameView.GAME_WIDTH GameView.GAME_HEIGHT
		start_button_x = Math.abs(480 - start_button.getWidth()) / 2;
		start_button_y = scanY * (854 - 145);
		//GameView.GAME_WIDTH GameView.GAME_HEIGHT
		logo_x = Math.abs(480 - logo.getWidth()) / 2;
		logo_y = scanX * 75;
		//开始播放游戏主旋律背景音
		audio.setMusic("bgm_zhuxuanlv.ogg");
		audio.setLoop(true);
		audio.start();
	}
	
	/*
	 * 绘制方法
	 * */
	public void draw(Canvas canvas){
		canvas.drawBitmap(backgound, 0, 0, null);
		canvas.drawBitmap(face, face_x, face_y, null);
		canvas.drawBitmap(logo, logo_x, logo_y, null);
		//判断按钮是否按下,如果按下,缩小按钮
		if(start_button_touch){
			canvas.save();
			canvas.scale(0.85f,0.85f, start_button_x + start_button.getWidth()/2, start_button_y + start_button.getHeight()/2);
			canvas.drawBitmap(start_button, start_button_x, start_button_y, null);
			canvas.restore();
		}else
			canvas.drawBitmap(start_button, start_button_x, start_button_y, null);
	}
	
	/*
	 * 事件监听
	 * */
	public void onTouchEvent(MotionEvent event){
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			//如果在Button区域内按下,让button按下标识更改为true
			if( (start_button_x < event.getX()/GameView.SCALE_X && event.getX()/GameView.SCALE_X < (start_button_x+start_button.getWidth()))
					&& (start_button_y < event.getY()/GameView.SCALE_Y && event.getY()/GameView.SCALE_Y < (start_button_y+start_button.getHeight())) ){
				start_button_touch = true;
				audio.playSound(Audio.effcet_ui_ananniu);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			//每次抬起都会将start_button_touch更改为false
			start_button_touch = false;
			//如果在button区域内抬起,视为点击了button,进入游戏菜单
			if( (start_button_x < event.getX()/GameView.SCALE_X && event.getX()/GameView.SCALE_X < (start_button_x+start_button.getWidth()))
					&& (start_button_y < event.getY()/GameView.SCALE_Y && event.getY()/GameView.SCALE_Y < (start_button_y+start_button.getHeight())) ){
				//这里写开始
				gameView.setGameState(GameView.GAME_GAMEING);
				//更换背景音
				audio.setMusic("bgm_zhandou2.ogg");
				audio.setLoop(true);
				audio.start();
			}
			break;
		}
		
	}

	@Override
	public void logic() {
		// TODO Auto-generated method stub
		
	}
	
}
