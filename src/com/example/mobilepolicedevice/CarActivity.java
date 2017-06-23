package com.example.mobilepolicedevice;

import com.example.service.TestService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import zuo.biao.library.interfaces.OnBottomDragListener;

public class CarActivity extends BaseActivity implements OnBottomDragListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.carlayout, this);
		
//		Intent startIntent = new Intent(this,MyService.class);
//		startService(startIntent);
	}
	
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
