package com.example.domain;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.example.face.DisplayView;
import com.example.planee.GameView;
import com.example.util.MoreRect;

public class Enemy implements DisplayView{
	//敌机类型
	public static enum Type{EASY, PLAIN, HARD, BOSS}
	/*敌机坐标*/
	private float x;
	private float y;
	/*敌机生命*/
	private int hp;
	/*敌机速度*/
	private int speed;
	/*敌机图片资源*/
	private Bitmap enemyBitmap;
	/*是否存活*/
	private boolean live = true;
	/*爆炸是否完成*/
	private boolean pangOk = false;
	/*敌机多矩形体积，碰撞检测用*/
	private MoreRect moreRect;
	/*爆炸*/
	private Pang pang;
	/*飞机样式*/
	private Type type;
	
	public Enemy(GameView gameView, int x, int y, Type type){
		this.x = x;
		this.y = y;
		this.type = type;
		/*爆炸*/
		pang = new Pang(gameView, x, y, 1f);
		/*
		 * Easy ： (32,0)*(65,33) 
		 *        (0,33)*(97,59) 
		 * Hard：(44,0)*(70,81) 
		 *       (0,34)*(113,71) 
		 * Plain： (33,0)*(70,74) 
		 *         (0,30)*(103,59)
		 */
		switch (type) {
		case EASY:
			speed = 5;
			hp = 40;
			enemyBitmap = gameView.getAssets().enemyEasy;
			moreRect = new MoreRect(
				x, y, 
				new Rect(32,0,65,33),
				new Rect(0,33,97,59)
			);
			break;
		case PLAIN:
			speed = 8;
			hp = 70;
			enemyBitmap = gameView.getAssets().enemyPlain;
			moreRect = new MoreRect(
				x, y, 
				new Rect(33,0,70,74),
				new Rect(0,30,103,59)
			);
			break;
		case HARD:
			speed = 11;
			hp = 100;
			enemyBitmap = gameView.getAssets().enemyHard;
			moreRect = new MoreRect(
				x, y, 
				new Rect(44,0,70,81),
				new Rect(0,34,113,71)
			);
			break;
		case BOSS:
			break;
		}
	}
	Paint paint = new Paint();
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		if(live)
			canvas.drawBitmap(enemyBitmap, x, y, null);
		else
			pang.draw(canvas);
	}

	@Override
	public void onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void logic() {
		// TODO Auto-generated method stub
		pang.setLocation(x, y);
		if(live){
			y = y + speed;
			moreRect.set( (int)x, (int)y );
		}
		else{
			pang.logic();
		}
		//超出屏幕死亡
		if(y > GameView.GAME_HEIGHT){
			pangOk = true;
			live = false;
		}
	}
	/*设置生命值*/
	public void setHp(int hp){
		this.hp = hp;
		if(this.hp <= 0)
			live = false;
	}
	/*得到生命值*/
	public int getHp(){
		return hp;
	}
	/*减命*/
	public void minusHp(int hp) {
		this.hp = this.hp - hp;
		if(this.hp <= 0)
			live = false;
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
	/*是否被一个子弹打中*/
	public boolean isHit(Bullet bullet){
		if(bullet.getMoreRect().isCollision(moreRect)){
			return true;
		}
		return false;
	}
	/*是否被一堆子弹打中*/
	public boolean isHit(List<Bullet> bullets){
		for(int i=0; i<bullets.size(); i++){
			if(isHit(bullets.get(i))){
				//当子弹死亡
				bullets.get(i).setLive(false);
				return true;
			}
		}
		return false;
	}
	
	public float getX(){
		return x;
	}
	public float getY(){
		return y;
	}
	public MoreRect getMoreRect(){
		return moreRect;
	}
	
	public boolean isPangOk(){
		return pang.isPangOk();
	}
}
