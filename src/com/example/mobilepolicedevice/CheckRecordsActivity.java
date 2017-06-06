package com.example.mobilepolicedevice;

import android.app.Activity;
import android.os.Bundle;
import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;

public class CheckRecordsActivity extends BaseActivity implements OnBottomDragListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_records,this);
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
		
	}

}
