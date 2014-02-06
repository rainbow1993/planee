package com.example.domain;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.example.face.DisplayView;
import com.example.planee.GameView;
import com.example.util.MoreRect;

public class Bullet implements DisplayView{
	//子弹坐标
	private int x;
	private int y;
	//子弹存活状态
	private boolean live = true;
	//子弹资源
	private Bitmap bullet;
	//当前子弹帧
	private int flash = 0;
	//默认子弹速度
	private int speedX = 0;
	private int speedY = 52;
	//子弹的好坏
	private boolean good;
	//子弹的体积
	private MoreRect moreRect;
	//子弹是否完成爆炸
	private boolean okPang = false;
	//子弹爆炸实例
	private Pang pang;
	
	public Bullet(GameView gameView, int x, int y, boolean good){
		this.x = x;
		this.y = y;
		this.good = good;
		bullet = gameView.getAssets().bullet;
		//初始化子弹体积
		moreRect = new MoreRect(x, y, new Rect(9, 1, 23, 45));
		//实例化爆炸
		pang = new Pang(gameView, x, y, 0.3f);
	}
	public Bullet(GameView gameView, int x, int y, boolean good, int speedX, int speedY){
		this(gameView, x, y, good);
		this.speedX = speedX;
		this.speedY = speedY;
	}
	
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		if(isLive())
			canvas.drawBitmap(bullet, x, y, null);
		else if(okPang == false){
			pang.draw(canvas);
		}
	}

	@Override
	public void onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void logic() {
		// TODO Auto-generated method stub
//		如果子弹超出屏幕
		if(y < -bullet.getHeight() || y > GameView.GAME_HEIGHT){
			live = false;
			okPang = true;
			return;
		}
		if(live){
			x = x - speedX;
			y = y - speedY;
			//更新体积待碰撞位置
			moreRect.set(x, y);
			//设置爆炸位置
			pang.setLocation(x, y);
		}
		else if(!live && !okPang){
			pang.logic();
			if(pang.isPangOk())
				okPang = true;
		}
	}
	
	/*得到体积*/
	public MoreRect getMoreRect(){
		return moreRect;
	}
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	public boolean isOkPang() {
		return okPang;
	}
}
