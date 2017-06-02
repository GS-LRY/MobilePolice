package com.example.service.impl;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.service.OneMoreFunction;

public class OneMoreFunctionImpl implements OneMoreFunction {

	@Override
	public boolean NetWorkStatus(Context context) {
		ConnectivityManager conManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conManager != null) {
			NetworkInfo info = conManager.getActiveNetworkInfo();
			if (info != null && info.isConnected()) {
				if(info.getState() == NetworkInfo.State.CONNECTED){
					return true;
				}
			}
		}
		return false;
	}

}
