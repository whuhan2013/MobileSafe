package com.zj.mobilesafe;

import java.util.List;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.Bundle;

public class TrafficManagerActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//1.��ȡһ������������
		PackageManager pm = getPackageManager();
		//2.�����ֻ�����ϵͳ ��ȡ���е�Ӧ�ó����uid
		List<ApplicationInfo> appliactaionInfos = pm.getInstalledApplications(0);
		for(ApplicationInfo applicationInfo : appliactaionInfos){
			int uid = applicationInfo.uid;
			//proc/uid_stat/10086
			long tx = TrafficStats.getUidTxBytes(uid);//���͵� �ϴ�������byte
			long rx = TrafficStats.getUidRxBytes(uid);//���ص����� byte
			//��������ֵ -1 ��������Ӧ�ó���û�в������� ���߲���ϵͳ��֧������ͳ��
		}
		TrafficStats.getMobileTxBytes();//��ȡ�ֻ�3g/2g�����ϴ���������
		TrafficStats.getMobileRxBytes();//�ֻ�2g/3g���ص�������
		
		TrafficStats.getTotalTxBytes();//�ֻ�ȫ������ӿ� ����wifi��3g��2g�ϴ���������
		TrafficStats.getTotalRxBytes();//�ֻ�ȫ������ӿ� ����wifi��3g��2g���ص�������
		setContentView(R.layout.activity_traffic_manager);
	}
}