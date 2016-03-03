package com.zj.mobilesafe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SelectContactActivity extends Activity {
	
	ListView list_seect_contact;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_select_contact);
		list_seect_contact=(ListView) findViewById(R.id.list_seect_contact);
		final List<Map<String, String>> data=getContactInfo(); 
		list_seect_contact.setAdapter(new SimpleAdapter(this, data, R.layout.contact_item_view, new String[]{"name","phone"}, new int[]{R.id.tv_name,R.id.tv_phone}));
	
		list_seect_contact.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String phone=data.get(position).get("phone"); 
				Intent data=new Intent(); 
				data.putExtra("phone", phone);
				setResult(0, data);
				//关闭当前页面
				finish();
			}
		});
	}
	
	
	/**
	 * 读取手机中的联系人
	 * @return
	 */
	private List<Map<String, String>> getContactInfo() {
		// TODO Auto-generated method stub
		//得到一个内容解析器
		List<Map<String, String>> list=new ArrayList<Map<String,String>>();
		ContentResolver resolver= getContentResolver();
		//raw_contact表
		
		Uri uri=Uri.parse("content://com.android.contacts/raw_contacts");
		Uri uriData=Uri.parse("content://com.android.contacts/data");
		
		Cursor cursor= resolver.query(uri, new String[]{"contact_id"}, null, null, null);
		
		while(cursor.moveToNext())
		{
			
			String contact_id=cursor.getString(0);
			if(contact_id!=null)
			{
				Map<String, String> map=new HashMap<String, String>();
			Cursor Datacursor=resolver.query(uriData, new String[]{"data1","mimetype"}, "contact_id=?", new String[]{contact_id}, null);
			
			while(Datacursor.moveToNext())
			{
				String data1=Datacursor.getString(0);
				String mimetype=Datacursor.getString(1);
				System.out.println("data1=="+data1+"mimetype"+mimetype);
				if("vnd.android.cursor.item/name".equals(mimetype))
				{
					map.put("name", data1);
				}else if("vnd.android.cursor.item/phone_v2".equals(mimetype))
				{
					map.put("phone", data1);
				}
				
			}
			list.add(map);
			
			Datacursor.close();
			}
		}
		
		cursor.close();
		return list;
	}

}
