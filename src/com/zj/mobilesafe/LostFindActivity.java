package com.zj.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LostFindActivity extends Activity {
	private SharedPreferences sp;
	private TextView tv_safenumber;
	private ImageView iv_protecting;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//判断是否做过设置向导，如果没有，则跳转到设置向导页面
		sp=getSharedPreferences("config",MODE_PRIVATE);
		boolean configed=sp.getBoolean("configed", false);
		System.out.println("到这了,config="+configed );
		if(configed)
		{
			//就在当前页面
		setContentView(R.layout.activity_lost_find);
		tv_safenumber=(TextView) findViewById(R.id.tv_safenumber);
		iv_protecting=(ImageView) findViewById(R.id.iv_protecting);
		
		//得到安全号码
		String safenumber=sp.getString("safenumber", "");
		tv_safenumber.setText(safenumber);
		
		//判断防盗保护状态
		boolean protecting=sp.getBoolean("protecting", false);
		if(protecting)
		{
			//已经开启
			iv_protecting.setImageResource(R.drawable.lock);
		}else
		{
			//没有开启
			iv_protecting.setImageResource(R.drawable.unlock);
		}
		}else
		{
			//跳转到设置向导页面
			Intent intent=new Intent(this,Setup1Activity.class);
			startActivity(intent);
			
			
			//关闭当前页面
			finish();
			
		}
		
		
		
	}
	/**
	 * 重新进入手机防盗向导页面
	 * @param view
	 */
	public void reEnterSetup(View view)
	{
		//跳转到设置向导页面
		Intent intent=new Intent(this,Setup1Activity.class);
		startActivity(intent);
		
		
		//关闭当前页面
		finish();
		//要求在finish方法或者activity后面执行.
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

}
