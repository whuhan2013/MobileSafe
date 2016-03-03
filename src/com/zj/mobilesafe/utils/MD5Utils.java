package com.zj.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
	
	/**
	 * MD5加密方法
	 * @param password
	 * @return
	 */
	public static String md5Password(String password)
	{
		StringBuffer buffer = null;
		try {
			//得到一个信息摘要器
					MessageDigest digest= MessageDigest.getInstance("md5");
					
					byte [] result= digest.digest(password.getBytes());
					 buffer=new StringBuffer();
					//把每一个BYTE做一个与运算0XFF
					for(byte b:result)
					{
						//与运算
						int number=b & 0xff;
						String str=Integer.toHexString(number);
						System.out.println(str);
						if(str.length()==1)
						{
							buffer.append("0");
						}
						
						buffer.append(str);
					}
					//得到的标准MD5加密后的结果
					return buffer.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		
	
			}
	
	

	}


