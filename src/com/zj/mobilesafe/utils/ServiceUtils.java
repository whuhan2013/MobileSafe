package com.zj.mobilesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceUtils {
	
	/**
	 * ����ĳ�������Ƿ񻹻���
	 * serviceName:�������ķ��������
	 * context:
	 */
	
	public static boolean isServiceRunning(Context context,String serviceName)
	{
		//У������Ƿ񻹻���,ActivityManager���Թ������Ҳ���Թ���activity
		ActivityManager am=(ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> infos= am.getRunningServices(100);
		for(RunningServiceInfo info:infos)
		{
			String name=info.service.getClassName();
			if(serviceName.equals(name))
			{
				return true;
			}
		}
		return false;
	}

}
