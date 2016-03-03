package com.zj.mobilesafe.test;

import java.util.List;
import java.util.Random;

import com.zj.mobilesafe.db.BlackNumberDBOpenHelper;
import com.zj.mobilesafe.db.BlackNumberDao;
import com.zj.mobilesafe.domain.BlackNumberInfo;

import android.test.AndroidTestCase;

public class TestBlackNumebrDB extends AndroidTestCase {
	
	public void testCreateDB() throws Exception
	{
		BlackNumberDBOpenHelper helper=new BlackNumberDBOpenHelper(getContext());
		helper.getReadableDatabase();
	}
	
	
	
	public void testAdd() throws Exception
	{
		BlackNumberDao dao=new BlackNumberDao(getContext());
		long basenumber = 13500000000l;
		Random random=new Random();
		for(int i=0;i<100;i++)
		{
		  dao.add(String.valueOf(basenumber+i), String.valueOf(random.nextInt(3)+1));
		}
	}
	
	public void testFindAll() throws Exception
	{
		BlackNumberDao dao=new BlackNumberDao(getContext());
		List<BlackNumberInfo> infos=dao.findAll();
		for(BlackNumberInfo info :infos)
		{
			System.out.println(info.toString());
		}
	}
	
	public void testDelete() throws Exception
	{
		BlackNumberDao dao=new BlackNumberDao(getContext());

		dao.delete("110");
	}
	
	public void testUpdate() throws Exception
	{
		BlackNumberDao dao=new BlackNumberDao(getContext());

		dao.update("110", "2");
	}
	
	public void testFind() throws Exception
	{
		BlackNumberDao dao=new BlackNumberDao(getContext());

		boolean result=dao.find("110");
		assertEquals(true, result);
	}

}
