package com.zj.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public abstract class BaseSetupActivity extends Activity {

	//手势识别器
		private GestureDetector detector; 
		protected SharedPreferences sp;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			sp=getSharedPreferences("config", MODE_PRIVATE);
			//实例化所向手势识别器
			detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){ 
				/**
				 * 当手指滑动的时候回调
				 */
				@Override 
				public boolean onFling(MotionEvent e1, MotionEvent e2,float velocityX, float velocityY) 
				{ 
					//屏蔽滑动很慢的情况
					if(Math.abs(velocityX)<200)
					{
						//划动的太慢了
						Toast.makeText(getApplicationContext(), "划动的太慢了", 0).show();

						return true;
					}
					
					//屏蔽斜着划的情况
					if(Math.abs((e2.getRawY()-e1.getRawY()))>100)
					{
						Toast.makeText(getApplicationContext(), "不能这样划动屏幕", 0).show();
						return true;
					}
					
					if((e2.getRawX()-e1.getRawX())>200)
					{
						//显示上一个页面,从左往右滑动
						System.out.println("显示上一个页面,从左往右滑动");
						showPre();
						return true;  
					}
					
					if((e1.getRawX()-e2.getRawX())>200)
					{
						//显示下一个页面，从右向左滑动
						System.out.println("显示下一个页面，从右向左滑动");
						showNext();
						return true;
					}
					return super.onFling(e1, e2, velocityX, velocityY);
				} 
				});
		}
		
		//使用手势识别器
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			// TODO Auto-generated method stub
			detector.onTouchEvent(event);
			return super.onTouchEvent(event);
		}
		
		public abstract void showNext();
		public abstract void showPre();
		/**
		 * 下一步的点击事件
		 * @param view
		 */
		public void next(View view)
		{
			showNext();
		}
		
		/**
		 * 上一步
		 */
		
		public void pre(View view)
		{
			showPre();
		}

}
