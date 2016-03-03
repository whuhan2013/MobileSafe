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

		// 读取之前保存的SIM卡信息，再读取当前SIM信息，比较是否一致
		sp = context.getSharedPreferences("config", context.MODE_PRIVATE);
		boolean protecting = sp.getBoolean("protecting", false);
		if (protecting) {
			// 开启了
			tm = (TelephonyManager) context
					.getSystemService(context.TELEPHONY_SERVICE);

			String saveSim = sp.getString("sim", null);
			String realSim = tm.getSimSerialNumber();
			if (saveSim.equals(realSim)) {
				// 没有变化

			} else {
				// 已经变更了
				System.out.println("SIM卡已经变更了");
				Toast.makeText(context, "SIM卡已经变更", 1).show();
				String destinationAddress=sp.getString("safenumber", "");
				SmsManager.getDefault().sendTextMessage(destinationAddress, null, "SIM Card charged....", null, null);
			}

		}

	}

}
