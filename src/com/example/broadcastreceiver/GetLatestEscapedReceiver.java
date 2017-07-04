package com.example.broadcastreceiver;

import com.example.service.GetLatestEscapedDataService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class GetLatestEscapedReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent j = new Intent(context,GetLatestEscapedDataService.class);
		context.startService(j);
	}

}
