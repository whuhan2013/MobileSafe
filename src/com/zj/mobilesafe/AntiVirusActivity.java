package com.zj.mobilesafe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.zip.ZipFile;

import com.zj.mobilesafe.db.dao.AntivirsuDao;



import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AntiVirusActivity extends Activity {
	protected static final int SCANING = 0;
	protected static final int FINISH = 2;
	private ImageView iv_scan;
	private ProgressBar progressBar1;
	private PackageManager pm;
	private TextView tv_scan_status;
	private LinearLayout ll_container;
	
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SCANING:
				ScanInfo scanInfo = (ScanInfo) msg.obj;
				tv_scan_status.setText("����ɨ�裺"+scanInfo.name);
				TextView tv  =new TextView(getApplicationContext());
				if(scanInfo.isvirus){
					tv.setTextColor(Color.RED);
					tv.setText("���ֲ�����"+scanInfo.name);
				}else{
					tv.setTextColor(Color.BLACK);
					tv.setText("ɨ�谲ȫ��"+scanInfo.name);
				}
				ll_container.addView(tv, 0);
				break;
			case FINISH:
				tv_scan_status.setText("ɨ�����");
				iv_scan.clearAnimation();
				break;
			}
		};
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anti_virus);
		tv_scan_status = (TextView) findViewById(R.id.tv_scan_status);
		ll_container = (LinearLayout) findViewById(R.id.ll_container);
		iv_scan = (ImageView) findViewById(R.id.iv_scan);
		RotateAnimation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		ra.setDuration(1000);
		ra.setRepeatCount(Animation.INFINITE);
		iv_scan.startAnimation(ra);
		progressBar1  = (ProgressBar) findViewById(R.id.progressBar1);
		scanVirus();
	}
	/**
	 * ɨ�財��
	 */
	private void scanVirus() {
		pm = getPackageManager();
		tv_scan_status.setText("���ڳ�ʼ��8��ɱ�����档����");
		new Thread(){
			public void run() {
				List<PackageInfo>  infos = pm.getInstalledPackages(0);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				progressBar1.setMax(infos.size());
				int progress = 0;
				for(PackageInfo info:infos){
					//apk�ļ���������·��
					String sourcedir = info.applicationInfo.sourceDir;//apk apkͼƬ
					//zip��
					String md5 = getFileMd5(sourcedir);
					ScanInfo scaninfo = new ScanInfo();
					scaninfo.name = info.applicationInfo.loadLabel(pm).toString();
					scaninfo.packname = info.packageName;
					System.out.println(scaninfo.packname+":"+md5);
					//��ѯmd5��Ϣ���Ƿ��ڲ������ݿ�������ڡ�
					if(AntivirsuDao.isVirus(md5)){
						//���ֲ���
						scaninfo.isvirus = true;
					}else{
						//ɨ�谲ȫ
						scaninfo.isvirus = false;
					}
					Message msg = Message.obtain();
					msg.obj = scaninfo;
					msg.what = SCANING;
					handler.sendMessage(msg);
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					progress++;
					progressBar1.setProgress(progress);
				}
				Message msg = Message.obtain();
				msg.what = FINISH;
				handler.sendMessage(msg);
			};
		}.start();
	}
	/**
	 * ɨ����Ϣ���ڲ���
	 */
	class ScanInfo{
		String packname;
		String name;
		boolean isvirus;
	}
	
	/**
	 * ��ȡ�ļ���md5ֵ
	 * @param path �ļ���ȫ·������
	 * @return
	 */
	private String getFileMd5(String path){
		try {
			// ��ȡһ���ļ���������Ϣ��ǩ����Ϣ��
			File file = new File(path);
			// md5
			MessageDigest digest = MessageDigest.getInstance("md5");
			FileInputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = fis.read(buffer)) != -1) {
				digest.update(buffer, 0, len);
			}
			byte[] result = digest.digest();
			StringBuffer sb  = new StringBuffer();
			for (byte b : result) {
				// ������
				int number = b & 0xff;// ����
				String str = Integer.toHexString(number);
				// System.out.println(str);
				if (str.length() == 1) {
					sb.append("0");
				}
				sb.append(str);
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
}