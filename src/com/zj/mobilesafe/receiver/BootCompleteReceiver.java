package com.zj.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class BootCompleteReceiver extends BroadcastReceiver {

	private SharedPreferences sp;
	private TelephonyManager tm;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		// ��ȡ֮ǰ�����SIM����Ϣ���ٶ�ȡ��ǰSIM��Ϣ���Ƚ��Ƿ�һ��
		sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		boolean protecting = sp.getBoolean("protecting", false);
		if (protecting) {
			// ������
			tm = (TelephonyManager) context
					.getSystemService(context.TELEPHONY_SERVICE);

			String saveSim = sp.getString("sim", null);
			String realSim = tm.getSimSerialNumber();
			if (saveSim.equals(realSim)) {
				// û�б仯

			} else {
				// �Ѿ������
				System.out.println("SIM���Ѿ������");
				Toast.makeText(context, "SIM���Ѿ����", 1).show();
				String destinationAddress=sp.getString("safenumber", "");
				SmsManager.getDefault().sendTextMessage(destinationAddress, null, "SIM Card charged....", null, null);
			}

		}

	}

}
