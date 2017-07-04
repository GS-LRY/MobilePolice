package com.example.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.broadcastreceiver.AlarmReceiver;
import com.example.broadcastreceiver.GetLatestEscapedReceiver;
import com.example.db.DatabaseUtil;
import com.example.db.MyHelper;
import com.example.http.HttpOperation;
import com.example.json.JsonUtil;
import com.example.model.Escaped;
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

/**
 * 获取最新在逃人员数据
 * @author LuRuyi
 * @date 2017-7-4
 *
 */
public class GetLatestEscapedDataService extends Service{

	private DatabaseUtil mDBUtil = new DatabaseUtil(GetLatestEscapedDataService.this);
	private OneMoreFunctionImpl oneMoreFunctionImpl = new OneMoreFunctionImpl();
	private HttpOperation httpOperation = new HttpOperation();
	JsonUtil jsonutil = new JsonUtil();
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
		if (oneMoreFunctionImpl.NetWorkStatus(getApplicationContext())){
			new Thread(new Runnable() {
				@Override
				public void run() {
					new Thread(networkTask).start();
					//Log.v("uploadinformation", "upload success");
				}
			}).start();
		}
		AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
		int anHour = 1 * 4 * 1000; // 这是5秒的毫秒数
		long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
		Intent i = new Intent(this, GetLatestEscapedReceiver.class);
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
			// TODO Auto-generated method stub
			Escaped escaped = null;
			try {
				escaped = mDBUtil.queryLocalLatestEscaped();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// 根据最新的日期获取该日期下的所有在逃人员信息
			ArrayList<Escaped> escapedlist = null;
			try {
				escapedlist = mDBUtil.queryEscapedByNrbjzdryksj(escaped.getNrbjzdryksj());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Map map = new HashMap<String, String>();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String nrbjzdryksj = formatter.format(escaped.getNrbjzdryksj());
			map.put("nrbjzdryksj", nrbjzdryksj);
			map.put("sfzh", escaped.getSfzh());
			if(escapedlist!=null&&escapedlist.size()>0){
				map.put("samepeople", "yes");
				String strescapedlist = jsonutil.EscapedListToJSON(escapedlist);
				map.put("escapedlist", strescapedlist);
			}else{
				map.put("samepeople", "no");
			}
			String jsondata = jsonutil.MaptoJSON(map);
			String result = null;
			result = httpOperation.getStringFromServer("getServerUpdateEscaped.do",jsondata);
			
			if(result!=null){
				if(result=="false"||result.equals("fasle")||result=="none"||result.equals("none")){
					return;
				}
				Message msg = new Message();
				msg.what = 0;
				msg.obj = result;
				handler.sendMessage(msg);
			}
		}
		
	};
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				String obj = (String)msg.obj;
				Escaped oldEscaped = null;
				ArrayList<Escaped> LatestEscapedList = jsonutil.toEscapedList(obj);
				for (int i = 0; i < LatestEscapedList.size(); i++) {
					Escaped LatestEscaped = LatestEscapedList.get(i);
					try {
						// 双保险，以防重复插入在逃人员信息
						oldEscaped = mDBUtil.queryEscapedByPersonId(LatestEscaped.getSfzh());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(oldEscaped==null){
						mDBUtil.Insert_Escaped(LatestEscaped);
					}
				}
				break;
		};
	};};

}
