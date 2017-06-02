package com.example.mobilepolicedevice;

import java.util.ArrayList;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;

import com.IDWORLD.LAPI;
import com.example.db.DatabaseUtil;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class FingerPrintMatch extends BaseActivity implements OnSeekBarChangeListener,OnBottomDragListener{

	private ImageView FingerPrintImageViewer_Match;
	private TextView txt_Finger_Match, txt_Finger_Match_Result,
			txt_Finger_Match_SUM,txt_skBarfingermatch;
	private Button btnMatch, btnOpenMatch, btnGetImageMatch;
	private SeekBar skBarfingermatch;

	private int numss = 0;
	
	private int threshold = 60;

	private LAPI m_cLAPI;
	private int m_hDevice = 0;
	private byte[] m_image = new byte[LAPI.WIDTH * LAPI.HEIGHT];
	private byte[] m_image_s = new byte[LAPI.WIDTH * LAPI.HEIGHT];
	private byte[] m_itemplate = new byte[LAPI.FPINFO_STD_MAX_SIZE];
	private byte[] m_itemplate_s = new byte[LAPI.FPINFO_STD_MAX_SIZE];
	private ArrayList<byte[]> m_images = new ArrayList<byte[]>();

	private boolean isOpenDevice = false;// 打开设备状态

	public static final int MSG_SHOW_TEXT = 101;
	public static final int MSG_SHOW_IMAGE = 102;

	private DatabaseUtil mDBUtil;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fingerprint_match,this);

		btnOpenMatch = (Button) findViewById(R.id.btn_open_match);
		btnGetImageMatch = (Button) findViewById(R.id.btn_getimage_match);
		btnMatch = (Button) findViewById(R.id.btn_match);
		txt_Finger_Match = (TextView) findViewById(R.id.txt_Finger_Match);
		txt_Finger_Match_Result = (TextView) findViewById(R.id.txt_Finger_Match_Result);
		FingerPrintImageViewer_Match = (ImageView) findViewById(R.id.FingerPrintImageViewer_Match);
		txt_Finger_Match_SUM = (TextView) findViewById(R.id.txt_Finger_Match_SUM);
		skBarfingermatch = (SeekBar)findViewById(R.id.skBarfingermatch);
		txt_skBarfingermatch = (TextView)findViewById(R.id.txt_skBarfingermatch);
		
		skBarfingermatch.setOnSeekBarChangeListener(this);
		m_cLAPI = new LAPI(this);

		btnOpenMatch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Runnable r = new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						OPEN_DEVICE();
					}
				};
				Thread s = new Thread(r);
				s.start();
			}
		});

		isOpenDevice = btnOpenMatch.performClick();

		if (isOpenDevice) {
			// 每隔1秒发送采集指纹命令
			handler.postDelayed(runnable, 1000);
		}

		btnGetImageMatch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				GET_IMAGE();
			}
		});
	}
	
	

	/**
	 * 打开指纹识别设备
	 */
	protected Boolean OPEN_DEVICE() {
		String msg;
		boolean isOpen = false;
		m_hDevice = m_cLAPI.OpenDeviceEx();
		if (m_hDevice == 0) {
			msg = "不能打开设备";
		} else {
			msg = "打开设备成功";
			isOpen = true;
		}
		m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT, 0, 0, msg));
		return isOpen;
	}

	/**
	 * 子线程处理一些操作
	 */
	private final Handler m_fEvent = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SHOW_IMAGE:
				ShowFingerBitmap((byte[]) msg.obj, msg.arg1, msg.arg2);
				// int num = m_images.size();
				// Toast.makeText(getApplicationContext(), "一共有" + num + "个",
				// Toast.LENGTH_SHORT).show();
				//
				// m_image_s = m_images.get(0);
				if (numss >= 1) {
					m_cLAPI.CreateTemplate(m_hDevice, m_image_s, m_itemplate);
					m_cLAPI.CreateTemplate(m_hDevice, m_image, m_itemplate_s);
					boolean issame = true;
					for (int i = 0; i < m_image.length && i < m_image_s.length; i++) {
						if (m_image[i] != m_image_s[i]) {
							issame = false;
							break;
						}
					}
					int pp = m_cLAPI.CompareTemplates(m_hDevice, m_itemplate,
							m_itemplate_s);
					txt_Finger_Match_SUM.setText("指纹特征匹配个数："+pp);
					Toast.makeText(getApplicationContext(),
							"匹配值：" + pp + "个" + issame, Toast.LENGTH_SHORT)
							.show();
					if (pp > threshold) {
						txt_Finger_Match_Result.setText("匹配成功");
					} else {
						txt_Finger_Match_Result.setText("匹配失败");
					}
				}
				numss++;
				break;

			case MSG_SHOW_TEXT:
				txt_Finger_Match.setText((String) msg.obj);
				break;
			}
		};
	};

	/**
	 * 获取指纹图像
	 */
	protected void GET_IMAGE() {
		int ret;
		String msg;
		if(numss >=200){
			numss = 0;
		}
		if (numss % 2 == 0) {
			ret = m_cLAPI.GetImage(m_hDevice, m_image);
		} else {
			ret = m_cLAPI.GetImage(m_hDevice, m_image_s);
		}

		if (ret != LAPI.TRUE) {
			msg = "不能获取图像";
		} else {
			msg = "获取图像成功";
		}
		if (numss % 2 == 0) {
			int isFingerPress = m_cLAPI.IsPressFinger(m_hDevice, m_image);
			if (isFingerPress != 0) {
				// m_images.add(m_image);
				m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT, 0,
						0, msg));
				m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_IMAGE,
						LAPI.WIDTH, LAPI.HEIGHT, m_image));
			}
		} else {
			int isFingerPress = m_cLAPI.IsPressFinger(m_hDevice, m_image_s);
			if (isFingerPress != 0) {
				// m_images.add(m_image);
				m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT, 0,
						0, msg));
				m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_IMAGE,
						LAPI.WIDTH, LAPI.HEIGHT, m_image_s));
			}
		}

		// int isFingerPress = m_cLAPI.IsPressFinger(m_hDevice, m_image);
		// if (isFingerPress != 0) {
		// //m_images.add(m_image);
		// m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT, 0, 0,
		// msg));
		// m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_IMAGE,
		// LAPI.WIDTH,
		// LAPI.HEIGHT, m_image));
		// }

	}

	/**
	 * 显示采集到的指纹图像
	 */
	private void ShowFingerBitmap(byte[] image, int width, int height) {
		if (width == 0) {
			return;
		}
		if (height == 0) {
			return;
		}

		int[] RGBbits = new int[width * height];
		FingerPrintImageViewer_Match.invalidate();
		for (int i = 0; i < width * height; i++) {
			int v;
			if (image != null) {
				v = image[i] & 0xff;
			} else {
				v = 0;
			}
			RGBbits[i] = Color.rgb(v, v, v);
		}
		Bitmap bmp = Bitmap
				.createBitmap(RGBbits, width, height, Config.RGB_565);
		FingerPrintImageViewer_Match.setImageBitmap(bmp);
	}

	/**
	 * 定时器，每隔2秒发送采集指纹信息命令
	 */
	private Handler handler = new Handler();
	private Runnable runnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				handler.postDelayed(this, 3000);
				btnGetImageMatch.performClick();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	/**
	 * 匹配指纹特征
	 * 
	 * @param template_1
	 * @param template_2
	 * @return
	 */
	protected int CompareFingerPrintTemplate(byte[] template_1,
			byte[] template_2) {
		int score = 0;
		score = m_cLAPI.CompareTemplates(m_hDevice, template_1, template_2);
		return score;
	}

	
	@Override
	public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
		// TODO Auto-generated method stub
		threshold = progress;
		txt_skBarfingermatch.setText("当前通过需要匹配成功的个数："+progress);
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		
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
