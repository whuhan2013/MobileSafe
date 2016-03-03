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
        tv_splash_version.setText("�汾��:"+getVersionName());
        tv_update_info=(TextView) findViewById(R.id.tv_splash_version);
        
        sp=getSharedPreferences("config", MODE_PRIVATE);
        boolean update=sp.getBoolean("update", false);
        //installShortCut();
        
        //�������ݿ�
        copyDB("address.db");
        copyDB("antivirus.db");
        if(update)
        {
        	//������
            checkUpdate();
        }else
        {
        	//�Զ������Ѿ��ر�
        	handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					//������ҳ��
					enterHome();
				}
			}, 2000);
        }
        
        
        //�л�����
//        AlphaAnimation aa=new AlphaAnimation(0.2f, 1.0f);
//        aa.setDuration(500);
//        findViewById(R.id.rl_root_splash).startAnimation(aa);
    }
    
    
    /**
	 * �������ͼ��
	 */
	private void installShortCut() {
		boolean shortcut = sp.getBoolean("shortcut", false);
		if(shortcut)
			return;
		Editor editor = sp.edit();
		//���͹㲥����ͼ�� ���һ���������棬Ҫ�������ͼ����
		Intent intent = new Intent();
		intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		//��ݷ�ʽ  Ҫ����3����Ҫ����Ϣ 1������ 2.ͼ�� 3.��ʲô����
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "�ֻ�С��ʿ");
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
				//������ͼ���Ӧ����ͼ��
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
     * �����ݿ⿽����ָ��Ŀ¼
     */
    private void copyDB(String filename) {
		// TODO Auto-generated method stub
    	
    	//ֻҪ������һ�ξͲ���Ҫ�ٿ�����
		try {
			File file=new File(getFilesDir(), filename);
			if(file.exists()&&file.length()>0)
			{
				
				//����Ҫ����
				Log.i(tag, "���ݿ��Ѿ�����");
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
				//��ʾ�����Ի���
				Log.i(tag, "��ʾ�����Ի���");
				showUpdateDialog();
				break;
				
			case ENTER_HOME:
				//������ҳ��
				enterHome();
				break;
				
			case ERRORmesg:
				//���� 
				enterHome();
				Toast.makeText(getApplicationContext(), "�����޷�����", 0).show();
				
				break;

			default:
				break;
			}
		}
    	
    	
    };
    
    /**
     * ����Ƿ����°汾
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
					//����
					HttpURLConnection conn= (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
				    conn.setConnectTimeout(4000);
				    int code = conn.getResponseCode();
					if(code == 200)
					{
						//�����ɹ�
						InputStream is= conn.getInputStream();
						//����ת�����ַ���
						String result=StreamTools.readFromStream(is);
						Log.i(tag, "�����ɹ�"+result);
						//json����
						JSONObject obj=new JSONObject(result);
						String version=(String) obj.get("version");
						 description=(String) obj.get("description");
						 apkurl=(String) obj.get("apkurl");
						System.out.println("�õ����Ϊ"+version);
						//У���°汾
						 if(getVersionName().equals(version))
						 {
							 //�汾һ��,������ҳ��
							 msg.what= ENTER_HOME;
							 
						 }else
						 {
							
							 //���°汾,�����Ի���
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
					 //���˶���ʱ��
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
     * ���������Ի���
     */
    protected void showUpdateDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder=new Builder(this);
		builder.setTitle("��ʾ����");
		//builder.setCancelable(false);  ǿ������
		builder.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				//������ҳ��
				enterHome();
				dialog.dismiss();
			}
		});
		builder.setMessage(description);
		builder.setPositiveButton("��������", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//����APK�������滻��װ
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					//SD���Ѿ�װ����
					FinalHttp finalHttp=new FinalHttp();
					finalHttp.download(apkurl, Environment.getExternalStorageDirectory().getAbsolutePath()+"/mobilesafe2.0.apk", 
							new AjaxCallBack<File>() 
							{

								@Override
								public void onFailure(Throwable t, int errorNo,
										String strMsg) {
									// TODO Auto-generated method stub
									t.printStackTrace();
									Toast.makeText(getApplicationContext(), "����ʧ����", 1).show();
									super.onFailure(t, errorNo, strMsg);
								}

								@Override
								public void onLoading(long count, long current) {
									// TODO Auto-generated method stub
									tv_update_info.setVisibility(View.VISIBLE);
									int progess=(int) (current*100/count);
									
									//��ǰ���ذٷֱ�
									tv_update_info.setText("���ؽ���:"+progess+"%");
									super.onLoading(count, current);
								}

								@Override
								public void onSuccess(File t) {
									// TODO Auto-generated method stub
									super.onSuccess(t);
									
									//��װ 
									installApk(t);
								}
								//��װAPK

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
					//SD��������
					Toast.makeText(getApplicationContext(), "û��SD��,�밲װ������", 0).show();
				}
				
			}
		});
		
		builder.setNegativeButton("�´���˵", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				
				//������ҳ��
			    enterHome();	
			}
		});
		
		builder.show();
		
	}

    
	protected void enterHome() {
		// TODO Auto-generated method stub
		Intent intent=new Intent(this,HomeActivity.class);
		startActivity(intent);
		//�رյ�ǰҳ��
		finish();
	}

	/**
     * �õ�Ӧ�ó���汾��
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
