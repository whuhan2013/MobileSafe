package com.zj.mobilesafe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zj.mobilesafe.utils.StreamTools;

public class SplashActivity extends Activity {
	protected static final int ENTER_HOME = 0;
	protected static final int SHOW_UPDATE_DIALO = 1;
	protected static final int ERRORmesg = 2;
	private TextView tv_splash_version;
	private String tag="SplashActivity";
	private String description;
	private String apkurl;
	private SharedPreferences sp;
	
	private TextView tv_update_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tv_splash_version=(TextView) findViewById(R.id.tv_splash_version);
        tv_splash_version.setText("版本号:"+getVersionName());
        tv_update_info=(TextView) findViewById(R.id.tv_splash_version);
        
        sp=getSharedPreferences("config", MODE_PRIVATE);
        boolean update=sp.getBoolean("update", false);
        //installShortCut();
        
        //拷贝数据库
        copyDB("address.db");
        copyDB("antivirus.db");
        if(update)
        {
        	//检查更新
            checkUpdate();
        }else
        {
        	//自动升级已经关闭
        	handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					//进入主页面
					enterHome();
				}
			}, 2000);
        }
        
        
        //切换动画
//        AlphaAnimation aa=new AlphaAnimation(0.2f, 1.0f);
//        aa.setDuration(500);
//        findViewById(R.id.rl_root_splash).startAnimation(aa);
    }
    
    
    /**
	 * 创建快捷图标
	 */
	private void installShortCut() {
		boolean shortcut = sp.getBoolean("shortcut", false);
		if(shortcut)
			return;
		Editor editor = sp.edit();
		//发送广播的意图， 大吼一声告诉桌面，要创建快捷图标了
		Intent intent = new Intent();
		intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		//快捷方式  要包含3个重要的信息 1，名称 2.图标 3.干什么事情
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "手机小卫士");
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
				//桌面点击图标对应的意图。
		Intent shortcutIntent = new Intent();
		shortcutIntent.setAction("android.intent.action.MAIN");
		shortcutIntent.addCategory("android.intent.category.LAUNCHER");
		shortcutIntent.setClassName(getPackageName(), "com.zj.mobilesafe.SplashActivity");
//		shortcutIntent.setAction("com.itheima.xxxx");
//		shortcutIntent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		sendBroadcast(intent);
		editor.putBoolean("shortcut", true);
		editor.commit();
	}
    
    /**
     * 将数据库拷贝到指定目录
     */
    private void copyDB(String filename) {
		// TODO Auto-generated method stub
    	
    	//只要拷贝了一次就不需要再拷贝了
		try {
			File file=new File(getFilesDir(), filename);
			if(file.exists()&&file.length()>0)
			{
				
				//不需要拷贝
				Log.i(tag, "数据库已经存在");
			}else
			{
			InputStream is= getAssets().open(filename);
			
			FileOutputStream fos=new FileOutputStream(file);
			byte []buffer=new byte[1024];
			int len=0;
			while((len=is.read(buffer))!=-1)
			{
				fos.write(buffer, 0, len);
			}
			
			is.close();
			fos.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private Handler handler=new Handler()
    {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case SHOW_UPDATE_DIALO:
				//显示升级对话框
				Log.i(tag, "显示升级对话框");
				showUpdateDialog();
				break;
				
			case ENTER_HOME:
				//进入主页面
				enterHome();
				break;
				
			case ERRORmesg:
				//错误 
				enterHome();
				Toast.makeText(getApplicationContext(), "出错，无法升级", 0).show();
				
				break;

			default:
				break;
			}
		}
    	
    	
    };
    
    /**
     * 检查是否有新版本
     */
    private void checkUpdate()
    {
    	
    	new Thread()
    	{
    		

			//http://192.168.56.1:8080/updateinfo.html
    		public void run()
    		{
    			Message msg=Message.obtain();
    			long starttime=System.currentTimeMillis();
    			try {
    				
					URL url=new URL(getString(R.string.serverurl));
					//联网
					HttpURLConnection conn= (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
				    conn.setConnectTimeout(4000);
				    int code = conn.getResponseCode();
					if(code == 200)
					{
						//联网成功
						InputStream is= conn.getInputStream();
						//把流转换成字符串
						String result=StreamTools.readFromStream(is);
						Log.i(tag, "联网成功"+result);
						//json解析
						JSONObject obj=new JSONObject(result);
						String version=(String) obj.get("version");
						 description=(String) obj.get("description");
						 apkurl=(String) obj.get("apkurl");
						System.out.println("得到结果为"+version);
						//校验新版本
						 if(getVersionName().equals(version))
						 {
							 //版本一致,进入主页面
							 msg.what= ENTER_HOME;
							 
						 }else
						 {
							
							 //有新版本,弹出对话框
							 msg.what=SHOW_UPDATE_DIALO;
						 }

					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					msg.what=ERRORmesg;
					e.printStackTrace();
				}finally
				{
					 long endtime=System.currentTimeMillis();
					 //花了多少时间
					 long dTime=endtime-starttime;
					 if(dTime<2000)
					 {
						 try {
							Thread.sleep(2000-dTime);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					 }
					 
					handler.sendMessage(msg);
				}
    		}
    	}.start();
    }
    /**
     * 弹出升级对话框
     */
    protected void showUpdateDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder=new Builder(this);
		builder.setTitle("提示升级");
		//builder.setCancelable(false);  强制升级
		builder.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				//进入主页面
				enterHome();
				dialog.dismiss();
			}
		});
		builder.setMessage(description);
		builder.setPositiveButton("立刻升级", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//下载APK，并且替换安装
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					//SD卡已经装好了
					FinalHttp finalHttp=new FinalHttp();
					finalHttp.download(apkurl, Environment.getExternalStorageDirectory().getAbsolutePath()+"/mobilesafe2.0.apk", 
							new AjaxCallBack<File>() 
							{

								@Override
								public void onFailure(Throwable t, int errorNo,
										String strMsg) {
									// TODO Auto-generated method stub
									t.printStackTrace();
									Toast.makeText(getApplicationContext(), "下载失败了", 1).show();
									super.onFailure(t, errorNo, strMsg);
								}

								@Override
								public void onLoading(long count, long current) {
									// TODO Auto-generated method stub
									tv_update_info.setVisibility(View.VISIBLE);
									int progess=(int) (current*100/count);
									
									//当前下载百分比
									tv_update_info.setText("下载进度:"+progess+"%");
									super.onLoading(count, current);
								}

								@Override
								public void onSuccess(File t) {
									// TODO Auto-generated method stub
									super.onSuccess(t);
									
									//安装 
									installApk(t);
								}
								//安装APK

								private void installApk(File t) {
									// TODO Auto-generated method stub
									Intent intent=new Intent();
									intent.setAction("android.intent.action.VIEW");
									intent.addCategory("android.intent.category.DEFAULT");
									intent.setDataAndType(Uri.fromFile(t), "application/vnd.android.package-archive");
									
									startActivity(intent);
								}
						
						
					});
				}else
				{
					//SD卡不存在
					Toast.makeText(getApplicationContext(), "没有SD卡,请安装后再试", 0).show();
				}
				
			}
		});
		
		builder.setNegativeButton("下次再说", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				
				//进入主页面
			    enterHome();	
			}
		});
		
		builder.show();
		
	}

    
	protected void enterHome() {
		// TODO Auto-generated method stub
		Intent intent=new Intent(this,HomeActivity.class);
		startActivity(intent);
		//关闭当前页面
		finish();
	}

	/**
     * 得到应用程序版本号
     */
    
    private String getVersionName()
    {
    	PackageManager pm= getPackageManager();
    	try {
		PackageInfo info=pm.getPackageInfo(getPackageName(), 0);
		return info.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
    	
    }
}
