package com.zj.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;
/**
 * �Զ���һ����һ�������н���
 * @author Administrator
 *
 */
public class FocusedTextview extends TextView {

	/**
	 * �Զ���TextView
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public FocusedTextview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public FocusedTextview(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public FocusedTextview(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * ��ǰ��û�н���
	 * 
	 */
	@Override
	@ExportedProperty(category = "focus")
	public boolean isFocused() {
		// TODO Auto-generated method stub
		return true;
	}
	
	

}
