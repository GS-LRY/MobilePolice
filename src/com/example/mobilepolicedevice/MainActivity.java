package com.example.mobilepolicedevice;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;

import com.example.db.DatabaseUtil;
import com.example.db.MyHelper;
import com.example.http.HttpOperation;
import com.example.json.JsonUtil;
import com.example.model.Escaped;
import com.example.service.impl.OneMoreFunctionImpl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends BaseActivity implements OnBottomDragListener{
	private ImageView ImgView_IdCardVerification;//身份核验
	private ImageView ImgView_FingerPrintVerification;//指纹核验
	private ImageView ImgView_FaceVerification;//人脸核验
	private ImageView ImgView_FingerPrintMatch;//指纹匹配
	private ImageView ImgView_CarVerification;//车辆核验
	private ImageView ImgView_DataVerification;//核验数据
	
	private DatabaseUtil mDButil = new DatabaseUtil(MainActivity.this);
	private OneMoreFunctionImpl oneMoreFunctionImpl = new OneMoreFunctionImpl();
	private HttpOperation httpOperation = new HttpOperation();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_ui,this);
		
		if (!oneMoreFunctionImpl.NetWorkStatus(getApplicationContext())) {
			Toast.makeText(getApplicationContext(), "网络未连接",
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		// 从服务器拉取数据
//		String sql = "select * from "+MyHelper.TABLE_NAME_Escaped;
//		int num = mDButil.queryNumBySQL(sql);
//		if(num==0){
//			new Thread(netWorkTask).start();
//		}
		
		ImgView_IdCardVerification = (ImageView)findViewById(R.id.ImgView_IdCardVerification);
		ImgView_FingerPrintVerification = (ImageView)findViewById(R.id.ImgView_FingerPrintVerification);
		ImgView_FaceVerification = (ImageView)findViewById(R.id.ImgView_FaceVerification);
		ImgView_FingerPrintMatch = (ImageView)findViewById(R.id.ImgView_FingerPrintMatch);
		ImgView_CarVerification = (ImageView)findViewById(R.id.ImgView_CarVerification);
		ImgView_DataVerification = (ImageView)findViewById(R.id.ImgView_DataVerification);
		ImgView_IdCardVerification.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,PersonInfoActivity.class);
				startActivity(intent);
			}
		});
		
		ImgView_FingerPrintVerification.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,FingerPrintActivity.class);
				startActivity(intent);
			}
		});
		
		ImgView_FingerPrintMatch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,FingerPrintMatch.class);
				startActivity(intent);
			}
		});
		
		ImgView_DataVerification.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,InformationRecordActivity.class);
				startActivity(intent);
			}
		});
		
		ImgView_CarVerification.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,CarActivity.class);
				startActivity(intent);
			}
		});
	}
	
	Runnable netWorkTask = new Runnable() {
		public void run() {
			String jsondata = httpOperation.getStringFromServer("getEscapedHundred.do","");
			Message msg = new Message();
			msg.what = 0;
			msg.obj = jsondata;
			handler.sendMessage(msg);
		}
	};
	
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				String jsondata = msg.obj.toString();
				if(jsondata.length()>20){
					JsonUtil jsonutil = new JsonUtil();
					ArrayList<Escaped> escapedList = jsonutil.toEscapedList(jsondata);
//					Escaped escaped = escapedList.get(0);
//					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//					Toast.makeText(getApplicationContext(), "XM:"+escaped.getXm()+"XB:"+escaped.getXb()+"SFZH:"+escaped.getSfzh()+"ZDRYLBBJ:"+escaped.getZdrylbbj()+"" +
//							"ZDRYXL:"+escaped.getZdryxl()+"LADW:"+escaped.getLadw()+"NRBJZDRYKSJ:"+formatter.format(escaped.getNrbjzdryksj())+"HJDQH:"+escaped.getHjdqh()+"HJDXZ:"+escaped.getHjdxz()+"XZDQH:"+escaped.getXzdqh()+"" +
//									"XZDXZ:"+escaped.getXzdxz()+"ZJLASJ:"+formatter.format(escaped.getZjlasj()), Toast.LENGTH_LONG).show();
					for (int i = 0; i < escapedList.size(); i++) {
						Escaped escaped = escapedList.get(i);
						mDButil.Insert_Escaped(escaped);
					}
					String sql = "select * from "+MyHelper.TABLE_NAME_Escaped;
					int num = mDButil.queryNumBySQL(sql);
					
					if(num==100){
						Toast.makeText(getApplicationContext(), "获取在逃人员信息成功",
								Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(getApplicationContext(), "插入在逃人员信息失败",
								Toast.LENGTH_SHORT).show();
					}
				}else {
					if(jsondata == "timeout" || jsondata.equals("timeout")){
						Toast.makeText(getApplicationContext(),
								"连接超时，请检查服务器是否正常运行", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(getApplicationContext(),
								"获取在逃人员信息失败", Toast.LENGTH_SHORT).show();
					}
				}
				
				break;

			default:
				break;
			}
		};
	};

	@Override
	public Activity getActivity() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDragBottom(boolean rightToLeft) {
		// TODO Auto-generated method stub
		finish();
	}

}
