package com.example.domain;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.example.face.DisplayView;
import com.example.planee.GameView;

public class Pang implements DisplayView{
	//坐标
	private float x;
	private float y;
	//资源
	private Bitmap[] bz = new Bitmap[5];
	//资源大小比例
	private float scale;
	//当前帧
	private int flash = 0;
	//音效管理器
	private Audio audio;
	//爆炸是否完成
	private boolean pangOk = false;
	
	public Pang(GameView gameView , float x, float y, float scale){
		//飞机爆炸初始化
		this.x = x;
		this.y = y;
		this.scale = scale;
		bz[0] = gameView.getAssets().bz_1;
		bz[1] = gameView.getAssets().bz_2;
		bz[2] = gameView.getAssets().bz_3;
		bz[3] = gameView.getAssets().bz_4;
		bz[4] = gameView.getAssets().bz_5;
		audio = gameView.getAudio();
	}
	
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		if(flash < 5 && y > -81 && y < GameView.GAME_HEIGHT){
			canvas.save();
			canvas.scale(scale, scale, x, y);
			canvas.drawBitmap(bz[flash], x, y, null);
			canvas.restore();
		}
	}

	@Override
	public void onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void logic() {
		// TODO Auto-generated method stub
		if(flash < 5){
			if(flash == 0 && y > -81 && y < GameView.GAME_HEIGHT){
				audio.playSound(Audio.effcet_sfx_zhongbaozha);
			}
			flash++;
		}
		//如果帧为5则完成爆炸
		if(flash == 5)
			pangOk = true;
	}
	
	public boolean isPangOk(){
		return pangOk;
	}
	
	public void setLocation(float x, float y){
		this.x = x;
		this.y = y;
	}
}
