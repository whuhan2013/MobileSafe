package com.zj.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
	
	/**
	 * MD5���ܷ���
	 * @param password
	 * @return
	 */
	public static String md5Password(String password)
	{
		StringBuffer buffer = null;
		try {
			//�õ�һ����ϢժҪ��
					MessageDigest digest= MessageDigest.getInstance("md5");
					
					byte [] result= digest.digest(password.getBytes());
					 buffer=new StringBuffer();
					//��ÿһ��BYTE��һ��������0XFF
					for(byte b:result)
					{
						//������
						int number=b & 0xff;
						String str=Integer.toHexString(number);
						System.out.println(str);
						if(str.length()==1)
						{
							buffer.append("0");
						}
						
						buffer.append(str);
					}
					//�õ��ı�׼MD5���ܺ�Ľ��
					return buffer.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		
	
			}
	
	

	}


