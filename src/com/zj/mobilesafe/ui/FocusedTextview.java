package com.zj.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;
/**
 * 自定义一个，一出生便有焦点
 * @author Administrator
 *
 */
public class FocusedTextview extends TextView {

	/**
	 * 自定义TextView
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
	 * 当前并没有焦点
	 * 
	 */
	@Override
	@ExportedProperty(category = "focus")
	public boolean isFocused() {
		// TODO Auto-generated method stub
		return true;
	}
	
	

}
