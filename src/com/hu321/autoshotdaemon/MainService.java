package com.hu321.autoshotdaemon;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class MainService extends Service {
	private static final String TAG = "MainService";

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		Log.d(TAG, "---------onCreate--------");
		registerReceiver(batteryChangedReceiver, new IntentFilter(
				Intent.ACTION_TIME_TICK));
	}

	private BroadcastReceiver batteryChangedReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			boolean isServiceRunning = false;
			boolean isActivityRunning = false;
			if (Intent.ACTION_TIME_TICK.equals(intent.getAction())) {
				// ���Service״̬

				ActivityManager manager = (ActivityManager) context
						.getSystemService(Context.ACTIVITY_SERVICE);
				for (RunningServiceInfo service : manager
						.getRunningServices(Integer.MAX_VALUE)) {
					if ("com.hu321.autoshotdaemon.MainService"
							.equals(service.service.getClassName()))

					{
						isServiceRunning = true;
						Log.d(TAG, "com.hu321.autoshotdaemon.MainService is runing");
					}
					
				}
				for (RunningAppProcessInfo app : manager
						.getRunningAppProcesses()) {		
					Log.d(TAG, "app name :" + app.processName);
					if ("com.hu321.autoshot"
							.equals(app.processName))

					{
						isActivityRunning = true;
						Log.d(TAG, "com.hu321.autoshot is runing");
					}

				}
	
				if (!isServiceRunning) {
					Log.d(TAG, "start com.hu321.autoshotdaemon.MainService");
					Intent i = new Intent(context, MainService.class);
					context.startService(i);
				}

				if (!isActivityRunning) {
					Log.d(TAG, "start com.hu321.autoshot.MainActivity");
					// ��Щ���������������һ��Ӧ�ó������Activity����ȻҲ������������һ��Activity
					ComponentName componetName = new ComponentName(
					// ���������һ��Ӧ�ó���İ���
							"com.hu321.autoshot",
							// ���������Ҫ������Activity
							"com.hu321.autoshot.MainActivity");

					try {
						Intent ootStartIntent = new Intent();
						ootStartIntent.setComponent(componetName);
						ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(ootStartIntent);
					} catch (Exception e) {
						// Toast.makeText(getApplicationContext(),
						// "������������ʾ�û�û���ҵ�Ӧ�ó��򣬻������������Ĳ�����", 0).show();
						Log.d(TAG, "start com.hu321.autoshot.MainActivity failed " + e.getMessage());
					}
				}
			}
		}
	};

}
