package com.example.planee;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import com.example.domain.Assets;
import com.example.domain.Audio;

public class GameView extends SurfaceView implements Runnable,Callback{
	public static final int GAME_LOADING = 0;
	public static final int GAME_MENU = 1;
	public static final int GAME_GAMEING =2;
	public static final int GAME_GAMEOVER = 3;
	
	public static int GAME_HEIGHT;
	public static int GAME_WIDTH;
	//游戏当前状态
	private int gameState = GAME_LOADING;
	
	//声明资源管理器
	private Assets assets;
	private Audio audio;
	
	private Loading loading;
	private Playing playing;
	
	private SurfaceHolder holder;
	private Thread thread;
	
	//线程循环
	private boolean threadLoop = true;
	
	//缩放
	private Canvas bufCanvas;
	private Bitmap bufBitmap;
	public static float SCALE_X;
	public static float SCALE_Y;
	
	public GameView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		holder = getHolder();
		holder.addCallback(this);
		assets = new Assets(context);
		audio = new Audio(context);
		
		//缩放
		bufBitmap = Bitmap.createBitmap(480, 854, Config.ARGB_8888);
		bufCanvas = new Canvas(bufBitmap);
		bufCanvas.drawRGB(255, 0, 255);
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		GAME_HEIGHT = getHeight();
		GAME_WIDTH = getWidth();
		//得到缩放因子
		SCALE_X = GAME_WIDTH / 480f;
		SCALE_Y = GAME_HEIGHT / 854f;
		
		//实例化资源管理器
		loading = new Loading(this);
		playing = new Playing(this);
		
		startThread();
	}
	
	public void draw(){
		if(holder.getSurface().isValid()){
			Canvas canvas = holder.lockCanvas();
			//缩放画布
			canvas.scale(SCALE_X, SCALE_Y, 0, 0);
			
			switch (gameState) {
			case GAME_LOADING:
				loading.draw(bufCanvas);
				break;
			case GAME_MENU:
				break;
			case GAME_GAMEING:
				playing.draw(bufCanvas);
				break;
			}
			//真实绘图
			canvas.drawBitmap(bufBitmap, 0, 0, null);
			
			holder.unlockCanvasAndPost(canvas);
			
//			Canvas canvas = holder.lockCanvas();
//			
//			switch (gameState) {
//			case GAME_LOADING:
//				loading.draw(canvas);
//				break;
//			case GAME_MENU:
//				break;
//			case GAME_GAMEING:
//				playing.draw(canvas);
//				break;
//			}
//			//
//			canvas.drawBitmap(bufBitmap, 0, 0, null);
//			
//			holder.unlockCanvasAndPost(canvas);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (gameState) {
		case GAME_LOADING:
			loading.onTouchEvent(event);
			break;
		case GAME_MENU:
			break;
		case GAME_GAMEING:
			playing.onTouchEvent(event);
			break;
		}
		return true;
	}
	/*逻辑处理*/
	public void logic(){
		switch (gameState) {
		case GAME_LOADING:
			loading.logic();
			break;
		case GAME_MENU:
			break;
		case GAME_GAMEING:
			playing.logic();
			break;
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (threadLoop) {
			logic();
			draw();
			try {
				thread.sleep(16);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * 开始/重启线程
	 * */
	public void startThread(){
		threadLoop = true;
		thread = new Thread(this);
		thread.start();
	}
	/*
	 * 暂停、停止线程
	 * */
	public void stopThread(){
		threadLoop = false;
		audio.stop();
	}
	/*
	 * 设置当前游戏状态
	 */
	public void setGameState(int gameState){
		this.gameState = gameState;
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
	}
	/*
	 * 得到当前资源管理器实例
	 */
	public Assets getAssets(){
		return assets;
	}
	/*
	 * 得到当前音乐管理器实例
	 */
	public Audio getAudio(){
		return audio;
	}
	
	
}
