package com.hu321.autoshotdaemon;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootBroadcastReveiver extends BroadcastReceiver {
	private static final String TAG = "BootBroadcastReveiver";  
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {  
            Intent bootServiceIntent = new Intent(context, MainService.class);  
            context.startService(bootServiceIntent);  
            Log.d(TAG, "--------Boot start service-------------");  
        }  
	}

}
