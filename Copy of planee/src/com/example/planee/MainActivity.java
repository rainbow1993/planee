package com.example.planee;

import net.youmi.android.AdManager;
import net.youmi.android.diy.banner.DiyAdSize;
import net.youmi.android.diy.banner.DiyBanner;
import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class MainActivity extends Activity {
	GameView gameView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 初始化应用的发布ID和密钥，以及设置测试模式
        AdManager.getInstance(this).init("dd0a51a288f2f519","2e2cbf77f1160ec0", false); 
        
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        gameView = new GameView(this);
		setContentView(gameView);
		
		//实例化LayoutParams(重要)
	    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
	        FrameLayout.LayoutParams.FILL_PARENT,
	        FrameLayout.LayoutParams.WRAP_CONTENT);     
	    //设置迷你Banner的悬浮位置
	    layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT; // 这里示例为右下角  
	    //实例化迷你Banner
	    DiyBanner banner = new DiyBanner(this, DiyAdSize.SIZE_MATCH_SCREENx32);//传入高度为32dp的DiyAdSize来定义迷你Banner
	    //调用Activity的addContentView函数
	    this.addContentView(banner, layoutParams);
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
//		gameView.startThread();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		gameView.stopThread();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
