package com.zj.mobilesafe;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.zj.mobilesafe.utils.SmsUtils;
import com.zj.mobilesafe.utils.SmsUtils.BackUpCallBack;

public class AtoolsAcitivity extends Activity {
	private ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atools);
	}
	/**
	 * ����¼��������������ز�ѯ��ҳ��
	 * @param view
	 */
	public void numberQuery(View view)
	{
		
		//������������ҳ��
		
		Intent intent=new Intent(this,NumberAddressQueryActivity.class);
		startActivity(intent);
		
	}
	/**
	 * ���ŵı���
	 * @param view
	 */
	public void smsBackUp(View view)
	{
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("���ڱ��ݶ���");
		pd.show();
		new Thread(){
			public void run() {
				try {
					SmsUtils.backupSms(getApplicationContext(), new BackUpCallBack() {
						@Override
						public void onSmsBackup(int progress) {
							pd.setProgress(progress);
						}
						@Override
						public void beforeBackup(int max) {
							pd.setMax(max);
						}
					});
					/**
					 * �����߳��и���������
					 */
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(AtoolsAcitivity.this, "���ݳɹ�", 0).show();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(AtoolsAcitivity.this, "����ʧ��", 0).show();
						}
					});
				}finally{
					pd.dismiss();
				}
			};
		}.start();
	}
	/**
	 * ���ŵĻ�ԭ
	 * @param view
	 */
	public void smsRestore(View view)
	{
		SmsUtils.restoreSms(this,true);
		Toast.makeText(this, "��ԭ�ɹ�", 0).show();
	}

}
