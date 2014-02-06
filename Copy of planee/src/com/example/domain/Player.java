package com.example.domain;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.example.face.DisplayView;
import com.example.planee.GameView;
import com.example.util.MoreRect;

public class Player implements DisplayView{
	/*音效管理器*/
	private Audio audio;
	/*主角坐标*/
	private float x = 200f;
	private float y = 500f;
	/*触摸坐标*/
	private float touchX = 0;
	private float touchY = 0;
	/*存活状态*/
	private boolean live = true;
	/*生命 默认应为：100*/
	private int hp = 90;
	/*金币*/
	private int money;
	/*飞行总距离*/
	private int distance;
	/*游戏得分*/
	private int score;
	/*GameView引用*/
	private GameView gameView;
	/*主角图片*/
	private Bitmap playerBitmap;
	/*HP*/
	private int playerHpX = 5;
	private int playerHpY = 14;
	private int playerHpW;
	private int playerHp1_2W;
	private int playerHpX_1_2W;
	private Bitmap playerHp;
	private Bitmap playerHpLine;
	private Bitmap playerHpLineBack;
	/*无敌状态计时器*/
	private int count = 0;
	/*无敌状态时间*/
	private int time = 0;
	/*无敌状态标识*/
	private boolean invincible = false;
	/*爆炸*/
	private Pang pang;
	/*子弹*/
	private List<Bullet> bullets = new ArrayList<Bullet>();
	/*开火计数*/
	private int bullet_count = 0;
	/*测试用画笔*/
	private Paint paint = new Paint();
	/*
	 * 体积，碰撞检测用*/
	private MoreRect moreRect;
	
	public Player(GameView gameView){
		this.gameView = gameView;
		/*得到音效管理器*/
		audio = gameView.getAudio();
		/*加载游戏资源*/
		playerBitmap = gameView.getAssets().playerBitmap;
		playerHp = gameView.getAssets().playerHp;
		playerHpW = playerHp.getWidth();
		playerHp1_2W = playerHpW/2;
		playerHpX_1_2W = playerHpX + playerHp1_2W;
		//血条资源
		playerHpLine = gameView.getAssets().playerHpLine;
		playerHpLineBack = gameView.getAssets().playerHpLineBack;
		/*爆炸初始化*/
		pang = new Pang(gameView, x, y, 1f);
		//初始化rect
		/*
		 * (48,0)*(79,80)
		 * (0,15)*(126,52)
		 */
		moreRect = new MoreRect(
			(int)x, (int)y, 
			new Rect(48,0,79,80),
			new Rect(0,15,126,52)
		);
		/*测试用画笔初始化*/
		paint.setColor(Color.WHITE);
		paint.setTextSize(15);
	}
	
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		//绘制子弹
		for(int i=0; i<bullets.size(); i++){
			bullets.get(i).draw(canvas);
		}
		//主角
		if(live){
			if(invincible && count%5 == 0){
			}
			else{
				canvas.drawBitmap(playerBitmap, x, y, null);
			}
		}
		else{
			pang.draw(canvas);
		}
		//血条背景
		canvas.drawBitmap(playerHpLineBack, playerHpX_1_2W, playerHpY, null);
		//真实血条
		if(hp!=0){
			canvas.save();
			canvas.clipRect(
					playerHpX_1_2W, 
					playerHpY, 
					playerHpX_1_2W+playerHpLine.getWidth(), 
					playerHpY+playerHpLine.getWidth());
			canvas.drawBitmap(playerHpLine, playerHpX_1_2W - (100-hp), playerHpY, null);
			canvas.restore();
		}
		//HP文字
		canvas.drawBitmap(playerHp, playerHpX, playerHpY, null);
	}

	@Override
	public void onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(live)
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN :
			break;
		case MotionEvent.ACTION_MOVE :
			x = event.getX(0) - playerBitmap.getWidth()/2;
			y = event.getY(0) - playerBitmap.getHeight();
			//缩放
			x = x / GameView.SCALE_X;
			y = y / GameView.SCALE_Y;
			break;
		case MotionEvent.ACTION_UP :
			break;
		}
	}
	
	@Override
	public void logic() {
		// TODO Auto-generated method stub
		//生命值小于0时死亡
		if(hp <= 0){
			live = false;
			pang.logic();
		}
		//更新Rect
		moreRect.set((int)x, (int)y);
		/////// 无敌逻辑  ///////////
		if(invincible && count>time){
			invincible = false;
			count = 0;
			time = 0;
		}
		count++;
		//子弹逻辑
		for(int i=0; i<bullets.size(); i++){
			bullets.get(i).logic();
			if(!bullets.get(i).isLive()){
				bullets.remove(i);
			}
		}
		//开火
		this.fire();
	}
	
	public MoreRect getMoreRect() {
		return moreRect;
	}
	/*开火*/
	public void fire(){
		if(bullet_count%2 == 0 && live)
			bullets.add(new Bullet(gameView, (int)x+playerBitmap.getWidth()/2-33/2, (int)y, true));
		//可能溢出
		bullet_count++;
	}
	/*设置生命值*/
	public void setHp(int hp){
		this.hp = hp;
	}
	/*得到生命值*/
	public int getHp(){
		return hp;
	}
	/*减命*/
	public void minusHp(int hp){
		if(!invincible){
			this.hp = this.hp - hp;
			if(this.hp<=0){
				pang.setLocation(x, y);
				live = false;
			}
		}
	}
	/*加命*/
	public void plusHp(int hp){
		this.hp = this.hp + hp;
	}
	/*是否存活*/
	public boolean isLive() {
		return live;
	}
	/*设置存活状态*/
	public void setLive(boolean live) {
		this.live = live;
	}
	/*设置、启动无敌状态*/
	public void setInvincible(int time){
		this.time = time;
		invincible = true;
	}
	/*得到无敌状态标识*/
	public boolean isInvincible() {
		return invincible;
	}
	/*得到发射出去的子弹*/
	public List<Bullet> getBullets(){
		return bullets;
	}
}
