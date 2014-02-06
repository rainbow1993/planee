package com.example.face;

import android.graphics.Canvas;
import android.view.MotionEvent;

public interface DisplayView {
	/*
	 * 绘图方法
	 */
	public void draw(Canvas canvas);
	/*
	 * 事件方法
	 */
	public void onTouchEvent(MotionEvent event);
	/*
	 * 逻辑方法
	 */
	public void logic();
}
