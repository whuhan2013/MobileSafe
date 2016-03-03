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

	//�õ�λ�÷���
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
		//ע�����λ�÷���
		//��λ���ṩ����������
		Criteria criteria=new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		
		String provider=lm.getBestProvider(criteria, true);
		//��ǰ��õ�λ���ṩ��
		lm.requestLocationUpdates(provider, 0, 0, listener);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//ȡ����λ�÷���
				lm.removeUpdates(listener);
				listener=null;
	}
	
	class MyLocationListener implements LocationListener
	{
		/**
		 * ��λ�øı��ʱ��ص��÷���
		 */

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			String longitude="longitude:"+location.getLongitude();
			String latitude="latitude:"+location.getLatitude();
			String accuracy="accuracy:"+location.getAccuracy();
			
			//���Ͷ��Ÿ���ȫ����
			//ת����������
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
		 * ��״̬�����ı��ʱ��GPS����ر�
		 */
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		/**
		 * ĳһ��λ���ṩ�߿�ʹ������ص�
		 */
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		

		/**
		 * ��һ��λ���ṩ�߲���ʹ����
		 */
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
