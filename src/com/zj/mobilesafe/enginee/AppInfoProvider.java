package com.zj.mobilesafe.enginee;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.zj.mobilesafe.domain.AppInfo;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

/**
 * ҵ�񷽷����ṩ�ֻ��а�װӦ�ó�����Ϣ
 * @author Administrator
 *
 */
public class AppInfoProvider {
	
	public static List<AppInfo> getAppInfos(Context context){
		PackageManager pm = context.getPackageManager();
		//���еİ�װ��ϵͳ�ϵ�Ӧ�ó������Ϣ��
		List<PackageInfo> packInfos = pm.getInstalledPackages(0);
		List<AppInfo> appInfos = new ArrayList<AppInfo>();
		for(PackageInfo packInfo : packInfos){
			AppInfo appInfo = new AppInfo();
			//packInfo  �൱��һ��Ӧ�ó���apk�����嵥�ļ�
			String packname = packInfo.packageName;
			Drawable icon = packInfo.applicationInfo.loadIcon(pm);
			String name = packInfo.applicationInfo.loadLabel(pm).toString();
			int flags = packInfo.applicationInfo.flags;//Ӧ�ó�����Ϣ�ı�� �൱���û��ύ�Ĵ��
			int uid=packInfo.applicationInfo.uid; //����ϵͳ���������Ĺ̶���ţ�һ����װ���ֻ��ˣ���̶�����
//			File rcvfile = new File("/proc/uid_stat/"+uid+"/tcp_rcv");
//			File sndfILE = new File("/proc/uid_stat/"+uid+"/tcp_snd");
			appInfo.setUid(uid);
			if((flags&ApplicationInfo.FLAG_SYSTEM)==0){
				//�û�����
				appInfo.setUserApp(true);
			}else{
				//ϵͳ����
				appInfo.setUserApp(false);
			}
			if((flags&ApplicationInfo.FLAG_EXTERNAL_STORAGE)==0){
				//�ֻ����ڴ�
				appInfo.setInRom(true);
			}else{
				//�ֻ���洢�豸
				appInfo.setInRom(false);
			}
			appInfo.setPackname(packname);
			appInfo.setIcon(icon);
			appInfo.setName(name);
			appInfos.add(appInfo);
		}
		return appInfos;
	}

}