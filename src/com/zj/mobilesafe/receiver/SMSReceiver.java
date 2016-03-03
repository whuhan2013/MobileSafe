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
	
		
		//д���ն��ŵĴ���
		Object []objs= (Object[]) intent.getExtras().get("pdus");
		sp=context.getSharedPreferences("config", context.MODE_PRIVATE);
		for(Object b:objs)
		{
			//�����ĳһ������
			SmsMessage sms= SmsMessage.createFromPdu((byte[]) b);
			//������
			String sender= sms.getOriginatingAddress(); 
			String safenumber=sp.getString("safenumber", "");
			if(sender.contains(safenumber))
			{
				//��������
				String body=sms.getMessageBody();
				if("#*location*#".equals(body))
				{
					//���ֻ�GPSλ����Ϣ
					Log.i(tag, "�õ��ֻ���GPS");
					Intent i=new Intent(context,GPSService.class);
					context.startService(i);
					SharedPreferences sp=context.getSharedPreferences("config", context.MODE_PRIVATE);
					String lastlocation=sp.getString("lastlocation", null);
					if(TextUtils.isEmpty(lastlocation))
					{
						//λ��û�еõ�
						SmsManager.getDefault().sendTextMessage(sender, null, "geting location....", null, null);
					}else
					{
						SmsManager.getDefault().sendTextMessage(sender, null, lastlocation, null, null);
					}
					//�ض϶��Ź㲥
					abortBroadcast();
					
				}else if("#*alarm*#".equals(body))
				{
					Log.i(tag, "���ű�������");
					MediaPlayer player=MediaPlayer.create(context, R.raw.gbxt);
					player.setLooping(false);
					player.setVolume(0.3f, 0.3f);
					player.start();
					
					abortBroadcast();
				}else if("#*wipedata*#".equals(body))
				{
					Log.i(tag, "Զ���������");
					abortBroadcast();
				}else if("#*lockscreen*#".equals(body))
				{
					Log.i(tag, "Զ������");
					abortBroadcast();
				}
			}
			
			
		}

	}

}
