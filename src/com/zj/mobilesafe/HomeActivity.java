package com.zj.mobilesafe;

import com.zj.mobilesafe.utils.MD5Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {
	GridView list_home;
	private MyAdapter adapter;
	private SharedPreferences sp;
	protected String tag="HomeActivity";
	private static String []names={
		"手机防盗","通讯卫士","软件管理",
		"进程管理","流量统计","手机杀毒",
		"缓存清理","高级工具","设置中心"
	};
	
	private AlertDialog dialog;
	
	private static int [] ids={
		R.drawable.safe,R.drawable.callmsgsafe,R.drawable.app,
		R.drawable.taskmanager,R.drawable.netmanager,R.drawable.trojan,
		R.drawable.sysoptimize,R.drawable.atools,R.drawable.settings
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		sp=getSharedPreferences("config", MODE_PRIVATE);
		
		list_home=(GridView) findViewById(R.id.list_home);
		adapter=new MyAdapter();
		list_home.setAdapter(adapter);
		
		list_home.setOnItemClickListener(new OnItemClickListener() {

			Intent intent;
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				switch (position) {
				
				case 0:   //进入手机防盗中心
					showLostFindDialog();
					break;
				case 1:
					intent=new Intent(HomeActivity.this,CallSmsSafeActivity.class);
					startActivity(intent);
					break;
					
				case 2:  //软件管理器
					intent=new Intent(HomeActivity.this,AppManagerActivity.class);
					startActivity(intent);
					break;
					
				case 3:  //进程管理器
					intent=new Intent(HomeActivity.this,TaskManagerActivity.class);
					startActivity(intent);
					break;
					
				case 4:  //进程管理器
					intent=new Intent(HomeActivity.this,TrafficManagerActivity.class);
					startActivity(intent);
					break;
					
				case 5:  //进程管理器
					intent=new Intent(HomeActivity.this,AntiVirusActivity.class);
					startActivity(intent);
					break;
					
				case 6:  //进程管理器
					intent=new Intent(HomeActivity.this,CleanCacheActivity.class);
					startActivity(intent);
					break;
					
				case 7:   //进入高级工具
					 intent=new Intent(HomeActivity.this,AtoolsAcitivity.class);
					 startActivity(intent);
					break;
				case 8:  //进入设置中心
					 intent=new Intent(HomeActivity.this,SettingActivity.class);
					startActivity(intent);
					break;

				default:
					break;
				}
				
			}
			
			
		});
	}
	
	protected void showLostFindDialog() {
		// TODO Auto-generated method stub
		//判断是否设置过密码
		if(isSetupPwd())
		{
			//已经设置过密码了，弹出的是输入对话框
			showEnterDialog();
		}else
		{
			//没有设置密码，弹出的设置密码的对话框
			showSetupPwdDialog();
		}
	}
	
	private EditText et_setup_pwd;
	private EditText et_setup_confirm;
	
	private Button ok;
	private Button cancel;
	
	/**
	 * 设置密码对话框
	 */
	private void showSetupPwdDialog() {
		// TODO Auto-generated method stub
		
		AlertDialog.Builder builder= new Builder(HomeActivity.this);
		//自定义一个布局文件
		View view=View.inflate(HomeActivity.this, R.layout.dialog_setup_password, null);
		
		et_setup_pwd=(EditText) view.findViewById(R.id.et_setup_pwd);
		et_setup_confirm=(EditText) view.findViewById(R.id.et_setup_confirm);
		
		ok=(Button) view.findViewById(R.id.ok);

		cancel=(Button) view.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//取消对话框
				dialog.dismiss(); 
			}
		});
        ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//取出密码
				String password=et_setup_pwd.getText().toString().trim();
				String password_confirm=et_setup_confirm.getText().toString().trim();
				
				if(TextUtils.isEmpty(password)||TextUtils.isEmpty(password_confirm))
				{
					Toast.makeText(HomeActivity.this, "密码为空，请输入", 0).show();
					return;
				}
				
				//判断是否一致，才去保存
				if(password.equals(password_confirm))
				{
					//一致的话，则进入，不一致，则
					
					Editor editor= sp.edit();
					editor.putString("password", MD5Utils.md5Password(password));  //保存成加密后的
					editor.commit();
					dialog.dismiss();
					
					Log.i(tag, "进入手机防盗页面");
					Intent intent=new Intent(HomeActivity.this,LostFindActivity.class);
					startActivity(intent);
				}else
				{
					Toast.makeText(HomeActivity.this, "密码不一致，请重新输入", 0).show();
					return;

				}
			}
		});
		builder.setView(view);
		dialog=builder.show();
	}

	/**
	 * 输入密码对话框
	 */
	private void showEnterDialog() {
		// TODO Auto-generated method stub
		
		AlertDialog.Builder builder= new Builder(HomeActivity.this);
		//自定义一个布局文件
		View view=View.inflate(HomeActivity.this, R.layout.dialog_enter_password, null);
		
		et_setup_pwd=(EditText) view.findViewById(R.id.et_setup_pwd);
		
		
		ok=(Button) view.findViewById(R.id.ok);

		cancel=(Button) view.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//取消对话框
				dialog.dismiss(); 
			}
		});
        ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//取出密码
				
				String password=et_setup_pwd.getText().toString().trim();
				String savePassWord = sp.getString("password", "");
				if(TextUtils.isEmpty(password))
				{
					Toast.makeText(HomeActivity.this, "密码为空", 0).show();
					
					return;
				}
				
				if(MD5Utils.md5Password(password).equals(savePassWord))
				{
					//密码一致
					//进入主页
					dialog.dismiss();
					Log.i(tag, "进入手机防盗页面");
					Intent intent=new Intent(HomeActivity.this,LostFindActivity.class);
					startActivity(intent);
				
					
				}else
				{
					Toast.makeText(HomeActivity.this, "密码错误，请重新输入", 1).show();
                     et_setup_pwd.setText("");
					return;
				}
			}
		});
		//builder.setView(view);
		dialog=builder.create();
        
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
	}

	/**
	 * 判断是否设置过密码
	 * @return
	 */
	private boolean isSetupPwd()
	{
		String passWord=sp.getString("password", null);
		if(TextUtils.isEmpty(passWord))
		{
		return false;
		}else
		{
			return true;
		}
	}

	class MyAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return names.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view=View.inflate(HomeActivity.this, R.layout.list_item_home, null);
			ImageView iv_item= (ImageView) view.findViewById(R.id.iv_item);
			TextView tv_item = (TextView) view.findViewById(R.id.tv_item);
			iv_item.setImageResource(ids[position]);
			tv_item.setText(names[position]);
			return view;
		}
		
	}

}
