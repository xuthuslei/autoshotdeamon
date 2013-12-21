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
				// 检查Service状态

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
					// 这些代码是启动另外的一个应用程序的主Activity，当然也可以启动任意一个Activity
					ComponentName componetName = new ComponentName(
					// 这个是另外一个应用程序的包名
							"com.hu321.autoshot",
							// 这个参数是要启动的Activity
							"com.hu321.autoshot.MainActivity");

					try {
						Intent ootStartIntent = new Intent();
						ootStartIntent.setComponent(componetName);
						ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(ootStartIntent);
					} catch (Exception e) {
						// Toast.makeText(getApplicationContext(),
						// "可以在这里提示用户没有找到应用程序，或者是做其他的操作！", 0).show();
						Log.d(TAG, "start com.hu321.autoshot.MainActivity failed " + e.getMessage());
					}
				}
			}
		}
	};

}
