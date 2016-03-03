package com.zj.mobilesafe;

import com.zj.mobilesafe.service.AddressService;
import com.zj.mobilesafe.service.CallSmsSafeService;
import com.zj.mobilesafe.service.WatchDogService;
import com.zj.mobilesafe.ui.SettingClickView;
import com.zj.mobilesafe.ui.SettingItemView;
import com.zj.mobilesafe.utils.ServiceUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SettingActivity extends Activity {
	
	private SettingItemView siv_update;
	private SharedPreferences sp;
	private SettingItemView siv_show_address;
	private Intent showAddress;
	//���������Ź�����
		private SettingItemView siv_watchdog;
		private Intent watchDogIntent;
		
	
	//���������ص�����
	private SettingItemView siv_callsms_safe;
	private Intent callSmsSafeIntent;
	//���ù�������ʾ�򱳾�
	private SettingClickView scv_changebg;
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		
		
		super.onResume();
		
		boolean isServiceRunning= ServiceUtils.isServiceRunning(SettingActivity.this, "com.zj.mobilesafe.service.AddressService");
		if(isServiceRunning)
		{
			//��������ķ����Ǵ��ڿ���״̬
			siv_show_address.setChecked(true);
		}else
		{
			siv_show_address.setChecked(false);
		}
		
		

		boolean iscallSmsServiceRunning = ServiceUtils.isServiceRunning(
				SettingActivity.this,
				"com.zj.mobilesafe.service.CallSmsSafeService");
		siv_callsms_safe.setChecked(iscallSmsServiceRunning);
		
		boolean iswatchdogServiceRunning = ServiceUtils.isServiceRunning(
				SettingActivity.this,
				"com.zj.mobilesafe.service.WatchDogService");
		siv_watchdog.setChecked(iswatchdogServiceRunning);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		sp=getSharedPreferences("config", MODE_PRIVATE);
		siv_show_address=(SettingItemView) findViewById(R.id.siv_show_address);
		siv_update=(SettingItemView) findViewById(R.id.siv_update);
		boolean update=sp.getBoolean("update", false);
		if(update)
		{
			//�Զ������Ѿ�����
			siv_update.setChecked(true);
			//siv_update.setDesc("�Զ������Ѿ�����");
		}else
		{
			siv_update.setChecked(false);
			//siv_update.setDesc("�Զ������Ѿ��ر�");
		}
		
		siv_update.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Editor editor=sp.edit();
				//�ж��Ƿ�ѡ��
				if(siv_update.isChecked())
				{
					
					siv_update.setChecked(false);
					//siv_update.setDesc("�Զ������Ѿ��ر�");
					editor.putBoolean("update", false);
				}else
				{
					siv_update.setChecked(true);
					//siv_update.setDesc("�Զ������Ѿ�����");
					editor.putBoolean("update", true);
				}
				
				editor.commit();
					
			}
		});
		
		
		//����������
		siv_watchdog = (SettingItemView) findViewById(R.id.siv_watchdog);
		watchDogIntent = new Intent(this, WatchDogService.class);
		siv_watchdog.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (siv_watchdog.isChecked()) {
							// ��Ϊ��ѡ��״̬
							siv_watchdog.setChecked(false);
							stopService(watchDogIntent);
						} else {
							// ѡ��״̬
							siv_watchdog.setChecked(true);
							startService(watchDogIntent);
						}

					}
				});

		
		
		//������ʾ��������ؿؼ�
		showAddress=new Intent(this,AddressService.class);
		boolean isServiceRunning= ServiceUtils.isServiceRunning(SettingActivity.this, "com.zj.mobilesafe.service.AddressService");
		if(isServiceRunning)
		{
			//��������ķ����Ǵ��ڿ���״̬
			siv_show_address.setChecked(true);
		}else
		{
			siv_show_address.setChecked(false);
		}
		
		siv_show_address.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(siv_show_address.isChecked())
				{
					//��Ϊ��ѡ��״̬
					siv_show_address.setChecked(false);
					stopService(showAddress);
				}else
				{
					//��Ϊѡ��״̬
					siv_show_address.setChecked(true);
					//��������
					startService(showAddress);
				}
					
			}
		});
		
		//��������������
				siv_callsms_safe = (SettingItemView) findViewById(R.id.siv_callsms_safe);
				callSmsSafeIntent = new Intent(this, CallSmsSafeService.class);
				siv_callsms_safe.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								if (siv_callsms_safe.isChecked()) {
									// ��Ϊ��ѡ��״̬
									siv_callsms_safe.setChecked(false);
									stopService(callSmsSafeIntent);
								} else {
									// ѡ��״̬
									siv_callsms_safe.setChecked(true);
									startService(callSmsSafeIntent);
								}

							}
						});
		
		//���ù����ر���
		final String []items={"��͸��","������","��ʿ��","������","ƻ����"};
		scv_changebg=(SettingClickView) findViewById(R.id.scv_changebg);
		scv_changebg.setTitle("��������ʾ����");
		int which=sp.getInt("which", 0);
		scv_changebg.setDesc(items[which]);
		//scv_changebg.setDesc("");
		scv_changebg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			    int dd=sp.getInt("which", 0);
				//����һ���Ի���
				AlertDialog.Builder builder=new Builder(SettingActivity.this);
				builder.setTitle("��������ʾ����");
				
				builder.setSingleChoiceItems(items , dd, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
						//����ѡ�����
						Editor editor= sp.edit();
						editor.putInt("which", which);
						editor.commit();
						scv_changebg.setDesc(items[which]);
						//ȡ���Ի���
						dialog.dismiss();
					}
				});
				builder.setNegativeButton("cancel", null);
				builder.show();
			}
		});
	}
	
	

}
