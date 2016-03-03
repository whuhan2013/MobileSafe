package com.zj.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NumberAdressQueryUtils {
	
	private static  String path="data/data/com.zj.mobilesafe/files/address.db";
	
	/**
	 * 根据传入的号码返回一个归属地
	 * @param number
	 * @return
	 */
	public static String queryNumber(String number)
	{
		String address =number;
		//使用正则表达式匹配
		SQLiteDatabase database= SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		if(number.matches("^1[34568]\\d{9}$"))
		{
			//手机号码
		
			Cursor cursor= database.rawQuery("select location from data2 where id =(select outkey from data1 where id=?)", new String[]{number.substring(0, 7)});
			while(cursor.moveToNext())
			{
				String location=cursor.getString(0);
				address=location;
			}
			cursor.close();
		}else
		{
			//其他号码
			switch (number.length()) {
			case 3:
				address="火警号码";
				break;
			case 4:
				address="模拟器";
				break;
			case 5:
				address="客服电话";
				break;
			case 7:
				address="本地号码";
				break;
			case 8:
				address="本地号码";
				break;

			default:
				
				//处理长途电话
				if(number.length()>10&&number.startsWith("0"))
				{
					Cursor cursor= database.rawQuery("select location from data2 where area = ?", new String[]{number.substring(1, 3)});
				    while(cursor.moveToNext())
				    {
				    	String location=cursor.getString(0);
				    	address= location.substring(0, location.length()-2);
				    	
				    }
				    
				    cursor.close();
				    
				    cursor=database.rawQuery("select location from data2 where area = ?", new String[]{number.substring(1, 4)});
				    while(cursor.moveToNext())
				    {
				    	String location=cursor.getString(0);
				    	address= location.substring(0, location.length()-2);
				    	
				    }
				    
				    cursor.close();
				}
				break;
			}
		}
		
		
		
		return address;
	}

}
