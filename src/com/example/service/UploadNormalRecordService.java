package com.example.service;

import java.util.ArrayList;

import com.example.bean.NormalList_Result;
import com.example.broadcastreceiver.AlarmReceiver;
import com.example.db.DatabaseUtil;
import com.example.http.HttpOperation;
import com.example.json.JsonUtil;
import com.example.mobilepolicedevice.InformationRecordActivity;
import com.example.model.Normal;
import com.example.service.impl.OneMoreFunctionImpl;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

public class UploadNormalRecordService extends Service {

	private DatabaseUtil mDBUtil;
	private OneMoreFunctionImpl oneMoreFunctionImpl = new OneMoreFunctionImpl();
	private HttpOperation httpOperation = new HttpOperation();

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mDBUtil = new DatabaseUtil(UploadNormalRecordService.this);
		// if (!oneMoreFunctionImpl.NetWorkStatus(getApplicationContext())) {
		// Toast.makeText(getApplicationContext(), "网络未连接",
		// Toast.LENGTH_SHORT).show();
		// return;
		// }
		new Thread(new Runnable() {
			@Override
			public void run() {
				new Thread(networkTask).start();
				Log.v("uploadinformation", "upload success");
			}
		}).start();
		AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
		int anHour = 1 * 10 * 1000; // 这是一小时的毫秒数
		long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
		Intent i = new Intent(this, AlarmReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/**
	 * 访问服务器
	 */
	Runnable networkTask = new Runnable() {
		@Override
		public void run() {
			// 在这里进行 http request.网络请求相关操作
			ArrayList<Normal> norList = mDBUtil.queryAll_Normal_NotSubmit();
			JsonUtil jsonutil = new JsonUtil();
			String jsondata = jsonutil.toJSONString(norList);
			String result = httpOperation.getStringFromServer("addNormal.do",
					jsondata);
			NormalList_Result normalList_result = new NormalList_Result();
			normalList_result.normalList = norList;
			normalList_result.result = result;
			Message msg = new Message();
			msg.what = 0;
			msg.obj = normalList_result;
			handler.sendMessage(msg);
		}
	};

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				// Toast.makeText(getApplicationContext(), msg.obj.toString(),
				// Toast.LENGTH_LONG).show();
				NormalList_Result obj = (NormalList_Result) msg.obj;
				ArrayList<Normal> norList = obj.normalList;
				String result = obj.result.toString();
				if (result == "true" || result.equals("true")) {
//					Toast.makeText(getApplicationContext(), "提交成功",
//							Toast.LENGTH_LONG).show();
					for (int i = 0; i < norList.size(); i++) {
						Normal normal = norList.get(i);
						mDBUtil.Update_Normal(normal.getId());
					}
				}
				break;
			}
		}
	};

}
