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
	 * 点击事件，进入号码归属地查询的页面
	 * @param view
	 */
	public void numberQuery(View view)
	{
		
		//进入号码归属地页面
		
		Intent intent=new Intent(this,NumberAddressQueryActivity.class);
		startActivity(intent);
		
	}
	/**
	 * 短信的备份
	 * @param view
	 */
	public void smsBackUp(View view)
	{
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("正在备份短信");
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
					 * 在子线程中更改主界面
					 */
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(AtoolsAcitivity.this, "备份成功", 0).show();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(AtoolsAcitivity.this, "备份失败", 0).show();
						}
					});
				}finally{
					pd.dismiss();
				}
			};
		}.start();
	}
	/**
	 * 短信的还原
	 * @param view
	 */
	public void smsRestore(View view)
	{
		SmsUtils.restoreSms(this,true);
		Toast.makeText(this, "还原成功", 0).show();
	}

}
