package com.zj.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BlackNumberDBOpenHelper extends SQLiteOpenHelper {

	/**
	 * 数据库创建的方法
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public BlackNumberDBOpenHelper(Context context) {
		super(context, "blacknumber.db", null, 1);
		// TODO Auto-generated constructor stub
		
	}

	/**
	 * 初始化数据库表结构 
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table blacknumber (_id integer primary key autoincrement,number varchar(20),mode varchar(20))");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
