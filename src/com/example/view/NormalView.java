package com.example.view;

import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobilepolicedevice.R;
import com.example.model.Normal;

import zuo.biao.library.base.BaseView;

public class NormalView extends BaseView<Normal> implements OnClickListener {
	private static final String TAG = "NormalView";

	public NormalView(Activity context, Resources resources) {
		super(context, resources);
	}

	private ImageView imgUpdateIcon;
	private ImageView imgFingerPrint;
	private TextView tvPersonName;
	private TextView tvPersonId;
	private TextView tvUserName;
	private TextView tvAddressName;
	private TextView tvCommitTime;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public View createView(LayoutInflater inflater) {
		convertView = inflater.inflate(R.layout.list_item_normal, null);

		imgUpdateIcon = findViewById(R.id.imgUpdateIcon);
		imgFingerPrint = findViewById(R.id.imgFingerPrint);
		tvPersonName = findViewById(R.id.tvPersonName);
		tvPersonId = findViewById(R.id.tvPersonId);
		tvUserName = findViewById(R.id.tvUserName);
		tvAddressName = findViewById(R.id.tvAddressName);
		tvCommitTime = findViewById(R.id.tvCommitTime);
		return convertView;
	}

	@Override
	public void bindView(Normal data) {
		if (data == null) {
			Log.e(TAG, "bindView data == null >> data=new Normal();");
			data = new Normal();
		}

		this.data = data;

		imgUpdateIcon
				.setImageResource(data.getInfosubmit() == 0 ? R.drawable.noupload
						: R.drawable.upload);
		imgFingerPrint.setImageResource(data.getPersonfp()==0?R.drawable.nofp:R.drawable.fp);
		tvPersonName.setText("姓名："+data.getPersonname());
		tvPersonId.setText("身份证号："+data.getPersonid());
		tvUserName.setText("核查警员："+data.getUserid());
		tvAddressName.setText("核查地点："+data.getAddressname());
		tvCommitTime.setText("核查时间："+data.getCommittime());

	}

}
