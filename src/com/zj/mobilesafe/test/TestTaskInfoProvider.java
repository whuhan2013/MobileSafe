package com.zj.mobilesafe.test;

import java.util.List;

import android.test.AndroidTestCase;

import com.zj.mobilesafe.domain.TaskInfo;
import com.zj.mobilesafe.enginee.TaskInfoProvider;

public class TestTaskInfoProvider extends AndroidTestCase {
	public void testGetTaskInfos() throws Exception{
		List<TaskInfo> infos = TaskInfoProvider.getTaskInfos(getContext());
		for(TaskInfo info:infos){
			System.out.println(info.toString());
		}
	}
}
