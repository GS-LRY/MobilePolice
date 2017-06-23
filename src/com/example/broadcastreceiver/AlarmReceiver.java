package com.example.broadcastreceiver;

import com.example.service.UploadNormalRecordService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent i = new Intent(context,UploadNormalRecordService.class);
		context.startService(i);
	}
	

}
