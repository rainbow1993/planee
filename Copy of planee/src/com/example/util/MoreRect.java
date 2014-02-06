package com.example.util;

import android.graphics.Rect;
/*
 * 多矩形组合类
 */
public class MoreRect {
	//相对Rect
	private Rect rect[];
	//绝对Rect
	private Rect absRect[];
	private int x;
	private int y;
	
	public MoreRect(int x, int y, Rect...rect){
		//声明rect数组
		this.rect = new Rect[rect.length];
		absRect = new Rect[rect.length];
		//为rect初始化
		int left;
		int top;
		int right;
		int bottom;
		for(int i=0; i<rect.length; i++){
			left = x + rect[i].left;
			top = y + rect[i].top;
			right = x + rect[i].right;
			bottom = y + rect[i].bottom;
			
			this.rect[i] = rect[i];
			absRect[i] = new Rect(left, top, right, bottom);
		}
	}
	
	/*
	 * 跟一个矩形发生碰撞
	 */
	public boolean isCollision(Rect rect){
		for(int i=0; i<this.rect.length; i++){
			if(rect.intersect(absRect[i]))
				return true;
		}
		return false;
	}
	
	/*
	 * 跟多个矩形发生碰撞
	 */
	public boolean isCollision(MoreRect moreRect){
		for(int i=0; i<absRect.length; i++){
			if(moreRect.isCollision(absRect[i]))
				return true;
		}
		return false;
	}
	
	/*
	 * 更新Rect位置
	 */
	public void set(int x, int y){
		int left;
		int top;
		int right;
		int bottom;
		for(int i=0; i<rect.length; i++){
			left = x + rect[i].left;
			top = y + rect[i].top;
			right = x + rect[i].right;
			bottom = y + rect[i].bottom;
			absRect[i].set(left, top, right, bottom);
		}
	}
	/*
	 * 得到矩形数量
	 */
	public int length(){
		return rect.length;
	}
	/*
	 * 得到矩形
	 */
	public Rect getAbsRect(int i){
		return absRect[i];
	}
	public Rect getRect(int i){
		return rect[i];
	}
}
