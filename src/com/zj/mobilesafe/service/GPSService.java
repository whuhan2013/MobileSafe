package com.zj.mobilesafe.service;

import java.io.InputStream;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.TextView;

public class GPSService extends Service {

	//用到位置服务
	private LocationManager lm;
	private MyLocationListener listener;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		lm=(LocationManager) getSystemService(LOCATION_SERVICE);
		listener=new MyLocationListener();
		//注册监听位置服务
		//给位置提供者设置条件
		Criteria criteria=new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		
		String provider=lm.getBestProvider(criteria, true);
		//当前最好的位置提供者
		lm.requestLocationUpdates(provider, 0, 0, listener);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//取消监位置服务
				lm.removeUpdates(listener);
				listener=null;
	}
	
	class MyLocationListener implements LocationListener
	{
		/**
		 * 当位置改变的时候回调该方法
		 */

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			String longitude="longitude:"+location.getLongitude();
			String latitude="latitude:"+location.getLatitude();
			String accuracy="accuracy:"+location.getAccuracy();
			
			//发送短信给安全号码
			//转换火星坐标
//			try
//			{
//			InputStream is= getAssets().open("axisoffset.dat");
//			ModifyOffset offset=ModifyOffset.getInstance(is);
//			PointDouble newPoint= offset.s2c(new PointDouble(location.getLongitude(), location.getLatitude()));
//			
//			longitude="j:"+offset.X+"\n";
//			latitude="w:"+offset.Y+"\n";
//			
//			
//			}catch(Exception e)
//			{
//				e.printStackTrace();
//			}
			
			SharedPreferences sp=getSharedPreferences("config", MODE_PRIVATE);
			Editor editor= sp.edit();
			editor.putString("lastlocation", longitude+latitude+accuracy);
			editor.commit();
		}

		/**
		 * 当状态发生改变的时候GPS打开与关闭
		 */
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		/**
		 * 某一个位置提供者可使用了则回调
		 */
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		

		/**
		 * 当一个位置提供者不可使用了
		 */
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
