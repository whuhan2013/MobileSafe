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
			 * ���ı������仯ʱ�ص�
			 * @see android.text.TextWatcher#onTextChanged(java.lang.CharSequence, int, int, int)
			 */
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(s!=null&&s.length()>3)
				{
					//��ѯ���ݿⲢ����ʾ���
					String address=NumberAdressQueryUtils.queryNumber(s.toString());
					//���ظ�Text
					result.setText(address);
				}
							
			}
			/**
			 * ���ı������仯ǰ�ص�
			 */
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			/**
			 * ���ı������仯֮��ص�
			 */
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	/**
	 * ��ѯ���������
	 * @param view
	 */
	public void numberAddressQuery(View view)
	{
		String phone=et_phone.getText().toString().trim();
		if(TextUtils.isEmpty(phone))
		{
			Toast.makeText(this, "����Ϊ��", 0).show();
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			 et_phone.startAnimation(shake);
			 
			 //���绰����Ϊ�յ�ʱ�򣬾�ȥ���ֻ������û�
//			 vibrator.vibrate(2000);
			 long[] pattern = {200,200,300,300,1000,2000};
			 //-1���ظ� 0ѭ���� 1��
			 vibrator.vibrate(pattern, -1);
			return;
		}else
		{
			//ȥ���ݿ��ѯ���������
			//дһ��������ȥ��ѯ ���ݿ�
			String location=NumberAdressQueryUtils.queryNumber(phone);
			Log.i(tag , "��Ҫ��ѯ�ĵ绰����Ϊ"+phone);
			result.setText("������Ϊ:"+location);
		}
	}

}
