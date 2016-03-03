package com.zj.mobilesafe.ui;

import com.zj.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * �Զ������Ͽؼ�
 * @author Administrator
 *
 */
public class SettingClickView extends RelativeLayout {
	
	
	private TextView tv_desc;
	private TextView tv_title;
	private  String desc_on;
	private String desc_off;
	/**
	 * ��ʼ�������ļ�
	 * @param context
	 */
	private void iniView(Context context) {
		// TODO Auto-generated method stub
		View.inflate(context, R.layout.setting_click_view, SettingClickView.this);
		
		tv_desc=(TextView) this.findViewById(R.id.tv_desc);
		tv_title=(TextView) this.findViewById(R.id.tv_title);
	}

	public SettingClickView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		
		iniView(context);
	}

	/**
	 * �������������Ĺ��췽��,�����ļ�ʹ�õ�ʱ����� 
	 * @param context
	 * @param attrs
	 */

	public SettingClickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		iniView(context);
		String title = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.zj.mobilesafe", "mytitle");
		desc_on = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.zj.mobilesafe", "desc_on");
		desc_off = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.zj.mobilesafe", "desc_off");
		
		tv_title.setText(title);
		setDesc(desc_off);
		
	}

	public SettingClickView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
		iniView(context);
	}
	
	
	
	/**
	 * ��Ͽؼ� �����ݷ����ı�
	 * 
	 */
	
	public void setDesc(String text)
	{
		tv_desc.setText(text);
	}
	
	/**
	 * ������Ͽؼ��ı���
	 */
	public void setTitle(String text)
	{
		tv_title.setText(text);
	}
	
	

}
