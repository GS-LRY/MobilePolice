package com.example.adapter;

import com.example.model.Normal;
import com.example.view.NormalView;

import zuo.biao.library.base.BaseView;
import zuo.biao.library.base.BaseViewAdapter;
import android.app.Activity;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class NormalAdapter extends BaseViewAdapter<Normal, NormalView> {

	public NormalAdapter(Activity context) {
		super(context);
	}

	@Override
	public NormalView createView(int position, ViewGroup parent) {
		return new NormalView(context, resources);
	}
	
	@Override
	public long getItemId(int position) {
		return getItem(position).getId();
	}
}
