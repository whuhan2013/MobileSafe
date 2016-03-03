package com.zj.mobilesafe.receiver;

import com.zj.mobilesafe.R;
import com.zj.mobilesafe.service.GPSService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.provider.MediaStore.Audio.Media;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

public class SMSReceiver extends BroadcastReceiver {

	private String tag="SMSReceiver";
	private SharedPreferences sp;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
	
		
		//写接收短信的代码
		Object []objs= (Object[]) intent.getExtras().get("pdus");
		sp=context.getSharedPreferences("config", context.MODE_PRIVATE);
		for(Object b:objs)
		{
			//具体的某一条短信
			SmsMessage sms= SmsMessage.createFromPdu((byte[]) b);
			//发送者
			String sender= sms.getOriginatingAddress(); 
			String safenumber=sp.getString("safenumber", "");
			if(sender.contains(safenumber))
			{
				//短信内容
				String body=sms.getMessageBody();
				if("#*location*#".equals(body))
				{
					//得手机GPS位置信息
					Log.i(tag, "得到手机的GPS");
					Intent i=new Intent(context,GPSService.class);
					context.startService(i);
					SharedPreferences sp=context.getSharedPreferences("config", context.MODE_PRIVATE);
					String lastlocation=sp.getString("lastlocation", null);
					if(TextUtils.isEmpty(lastlocation))
					{
						//位置没有得到
						SmsManager.getDefault().sendTextMessage(sender, null, "geting location....", null, null);
					}else
					{
						SmsManager.getDefault().sendTextMessage(sender, null, lastlocation, null, null);
					}
					//截断短信广播
					abortBroadcast();
					
				}else if("#*alarm*#".equals(body))
				{
					Log.i(tag, "播放报警音乐");
					MediaPlayer player=MediaPlayer.create(context, R.raw.gbxt);
					player.setLooping(false);
					player.setVolume(0.3f, 0.3f);
					player.start();
					
					abortBroadcast();
				}else if("#*wipedata*#".equals(body))
				{
					Log.i(tag, "远程清除数据");
					abortBroadcast();
				}else if("#*lockscreen*#".equals(body))
				{
					Log.i(tag, "远程锁屏");
					abortBroadcast();
				}
			}
			
			
		}

	}

}
