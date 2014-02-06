package com.example.planee;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import com.example.domain.Assets;
import com.example.domain.Audio;
import com.example.domain.Backgound;
import com.example.domain.Enemy;
import com.example.domain.Player;
import com.example.face.DisplayView;

public class Playing implements DisplayView{
	private GameView gameView;
	private Assets assets;
	/*游戏背景*/
	private Backgound backgound;
	/*当前游戏背景资源图*/
	private Bitmap gameBackgound;
	/*主角*/
	private Player player;
	/*敌机*/
	private List<Enemy> enemys = new ArrayList<Enemy>();
	/*计时器*/
	private int count = 0;
	
	public Playing(GameView gameView){
		this.gameView = gameView;
		assets = gameView.getAssets();
		gameBackgound = assets.gameBackgound;
		backgound = new Backgound(gameBackgound);
		player = new Player(gameView);
		//添加敌机
		enemys.add(new Enemy(gameView, 100, -280, Enemy.Type.EASY));
		enemys.add(new Enemy(gameView, 200, -280, Enemy.Type.PLAIN));
		enemys.add(new Enemy(gameView, 300, -280, Enemy.Type.HARD));
	}
	
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		backgound.draw(canvas);
		//画出敌机
		for(int i=0; i<enemys.size(); i++){
			enemys.get(i).draw(canvas);
		}
		//画出猪脚
		player.draw(canvas);
	}

	@Override
	public void onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		backgound.onTouchEvent(event);
		player.onTouchEvent(event);
	}
	@Override
	public void logic() {
		// TODO Auto-generated method stub
		//如果猪脚死亡，那么结束游戏
		if (!player.isLive()) {
			count++;
			if (count > 80){
				//结束
				gameView.setGameState(GameView.GAME_GAMEOVER);
			}
		}
		
		backgound.logic();
		player.logic();
		
		for(int i=0; i<enemys.size(); i++){
			//敌机逻辑
			enemys.get(i).logic();
			//猪脚与敌机碰撞			
			if(player.isLive() && !player.isInvincible() && enemys.get(i).getMoreRect().isCollision(player.getMoreRect())){
				Log.e("GameCollision", "--->true");
				player.minusHp(100);
				player.setInvincible(60);
				enemys.get(i).minusHp(100);
			}
			//敌机与子弹碰撞
			if(enemys.get(i).isHit(player.getBullets())){
				enemys.get(i).minusHp(10);
			}
			//敌机死亡删除敌机
			if(!enemys.get(i).isLive() && enemys.get(i).isPangOk()){
				enemys.remove(i);
			}
		}
		//创造新的敌机
		if(enemys.size() <= 1){
			enemys.add(new Enemy(gameView, 100, -280, Enemy.Type.EASY));
			enemys.add(new Enemy(gameView, 200, -280, Enemy.Type.PLAIN));
			enemys.add(new Enemy(gameView, 300, -280, Enemy.Type.HARD));
		}
	}
	
}
