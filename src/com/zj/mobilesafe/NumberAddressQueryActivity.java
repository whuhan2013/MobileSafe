package com.zj.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zj.mobilesafe.db.dao.NumberAdressQueryUtils;

public class NumberAddressQueryActivity extends Activity {
	
	EditText et_phone;
	TextView result;
	private String tag ="NumberAddressQueryActivity";
	private Vibrator vibrator;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_query);
		
		et_phone=(EditText) findViewById(R.id.et_phone);
		result=(TextView) findViewById(R.id.result);
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		et_phone.addTextChangedListener(new TextWatcher() {
			/*
			 * 当文本发生变化时回调
			 * @see android.text.TextWatcher#onTextChanged(java.lang.CharSequence, int, int, int)
			 */
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(s!=null&&s.length()>3)
				{
					//查询数据库并且显示结果
					String address=NumberAdressQueryUtils.queryNumber(s.toString());
					//返回给Text
					result.setText(address);
				}
							
			}
			/**
			 * 当文本发生变化前回调
			 */
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			/**
			 * 当文本发生变化之后回调
			 */
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	/**
	 * 查询号码归属地
	 * @param view
	 */
	public void numberAddressQuery(View view)
	{
		String phone=et_phone.getText().toString().trim();
		if(TextUtils.isEmpty(phone))
		{
			Toast.makeText(this, "号码为空", 0).show();
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			 et_phone.startAnimation(shake);
			 
			 //当电话号码为空的时候，就去振动手机提醒用户
//			 vibrator.vibrate(2000);
			 long[] pattern = {200,200,300,300,1000,2000};
			 //-1不重复 0循环振动 1；
			 vibrator.vibrate(pattern, -1);
			return;
		}else
		{
			//去数据库查询号码归属地
			//写一个工具类去查询 数据库
			String location=NumberAdressQueryUtils.queryNumber(phone);
			Log.i(tag , "您要查询的电话号码为"+phone);
			result.setText("归属地为:"+location);
		}
	}

}
