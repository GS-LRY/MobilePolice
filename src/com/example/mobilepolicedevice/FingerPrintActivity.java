package com.example.mobilepolicedevice;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import zuo.biao.library.interfaces.OnBottomDragListener;

import com.IDWORLD.LAPI;
import com.example.bean.FingerPrintTemplate;
import com.example.db.DatabaseUtil;
import com.example.db.MyHelper;

import android.app.Activity;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 指纹采集功能
 * 
 * @author 陆如一
 * @time 2017-4-15
 * 
 */
public class FingerPrintActivity extends BaseActivity implements OnBottomDragListener {
	private Button btnOpen;
	private Button btnClose;
	private Button btnGetImage;
	private Button btnDeleteFingerPrint;
	private TextView txt_WhichFinger;
	private ImageView FingerPrintImageViewer;

	private ImageButton imgbtn_Thumb_Right;
	private ImageButton imgbtn_ForFinger_Right;
	private ImageButton imgbtn_MiddleFinger_Right;
	private ImageButton imgbtn_RingFinger_Right;
	private ImageButton imgbtn_LittleFinger_Right;
	private ImageButton imgbtn_Thumb_Left;
	private ImageButton imgbtn_ForFinger_Left;
	private ImageButton imgbtn_MiddleFinger_Left;
	private ImageButton imgbtn_RingFinger_Left;
	private ImageButton imgbtn_LittleFinger_Left;

	private final Integer[] ImgBtnId = new Integer[] { R.id.ImgBtn_Thumb_Right,
			R.id.ImgBtn_ForeFinger_Right, R.id.ImgBtn_MiddleFinger_Right,
			R.id.ImgBtn_RingFinger_Right, R.id.ImgBtn_LittleFinger_Right,
			R.id.ImgBtn_Thumb_Left, R.id.ImgBtn__ForeFinger_Left,
			R.id.ImgBtn_MiddleFinger_Left, R.id.ImgBtn_RingFinger_Left,
			R.id.ImgBtn_LittleFinger_Left };
	private final ImageButton[] ImgBtns = new ImageButton[] {
			imgbtn_Thumb_Right, imgbtn_ForFinger_Right,
			imgbtn_MiddleFinger_Right, imgbtn_RingFinger_Right,
			imgbtn_LittleFinger_Right, imgbtn_Thumb_Left,
			imgbtn_ForFinger_Left, imgbtn_MiddleFinger_Left,
			imgbtn_RingFinger_Left, imgbtn_LittleFinger_Left };
	private final String[] WhichFingerTemplate = new String[] {
			"Right_Thumb_Template", "Right_ForeFinger_Template",
			"Right_MiddleFinger_Template", "Right_RingFinger_Template",
			"Right_LittleFinger_Template", "Left_Thumb_Template",
			"Left_ForeFinger_Template", "Left_MiddleFinger_Template",
			"Left_RingFinger_Template", "Left_LittleFinger_Template" };
	private final String[] WhichFingerText = new String[] { "右手大拇指", "右手食指",
			"右手中指", "右手无名指", "右手小指", "左手大拇指", "左手食指", "左手中指", "左手无名指", "左手小指" };
	private final Integer[] ImgBtnBackground = new Integer[] {
			R.drawable.thumb_finish, R.drawable.forefinger_finish,
			R.drawable.middlefinger_finish, R.drawable.ringfinger_finish,
			R.drawable.littlefinger_finish, R.drawable.thumb_finish,
			R.drawable.forefinger_finish, R.drawable.middlefinger_finish,
			R.drawable.ringfinger_finish, R.drawable.littlefinger_finish };
	private final Integer[] ImgBtnBackgroundOrigin = new Integer[] {
			R.drawable.thumb, R.drawable.forefinger, R.drawable.middlefinger,
			R.drawable.ringfinger, R.drawable.littlefinger, R.drawable.thumb,
			R.drawable.forefinger, R.drawable.middlefinger,
			R.drawable.ringfinger, R.drawable.littlefinger };

	private LAPI m_cLAPI;
	private int m_hDevice = 0;
	private byte[] m_image = new byte[LAPI.WIDTH * LAPI.HEIGHT];
	private byte[] m_image_s = new byte[LAPI.WIDTH * LAPI.HEIGHT];
	private byte[] m_image_review = new byte[LAPI.WIDTH * LAPI.HEIGHT];
	private byte[] m_itemplate = new byte[LAPI.FPINFO_STD_MAX_SIZE];
	private byte[] m_itemplate_s = new byte[LAPI.FPINFO_STD_MAX_SIZE];

	private boolean isOpenDevice = false;// 打开设备状态

	private int whichfingernumber = 0;// 删除指纹时指代哪一根手指

	public static final int MSG_SHOW_TEXT = 101;
	public static final int MSG_SHOW_IMAGE_SAVE = 102;
	public static final int MSG_VIEW_TEMPLATE = 103;
	public static final int MSG_SHOW_IMGBTN = 104;
	public static final int MSG_SHOW_IMAGE = 105;
	public static final int MSG_CHANGE_BTNDELETETEXT = 106;
	public static final int MSG_SHOW_IMGBTN_ORIGINAL = 107;
	public static final int MSG_ID_ENABLED = 403;

	private DatabaseUtil mDBUtil;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		CLOSE_DEVICE();
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fingerprint_collect,this);

		btnOpen = (Button) findViewById(R.id.btnOpenDevice);
		btnClose = (Button) findViewById(R.id.btnCloseDevice);
		btnGetImage = (Button) findViewById(R.id.btnGetImage);
		btnDeleteFingerPrint = (Button) findViewById(R.id.btnDeleteFingerPrint);
		txt_WhichFinger = (TextView) findViewById(R.id.txt_WhichFinger);
		FingerPrintImageViewer = (ImageView) findViewById(R.id.FingerPrintImageViewer);

		imgbtn_Thumb_Right = (ImageButton) findViewById(R.id.ImgBtn_Thumb_Right);
		for (int i = 0; i < ImgBtnId.length; i++) {
			ImgBtns[i] = (ImageButton) findViewById(ImgBtnId[i]);
			ImgBtns[i].setOnClickListener(new ButtonListener());
		}
		// EnableAllButtons (true,false);
		m_cLAPI = new LAPI(this);

		btnOpen.setOnClickListener(new OnClickListener() {

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
		isOpenDevice = btnOpen.performClick();

		if (isOpenDevice) {
			/* 每隔1秒发送采集指纹命令 */
			handler.postDelayed(runnable, 1000);
		}

		btnClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Thread s = new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						CLOSE_DEVICE();
					}
				});
				s.start();
			}
		});

		btnGetImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				GET_IMAGE();
			}
		});

		btnDeleteFingerPrint.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				DeleteFingerPrintTemplate();
			}
		});

	}
	
	/**
	 * 一些初始化
	 */
	private void InitView(){
		
	}

	private void DeleteFingerPrintTemplate() {
		mDBUtil = new DatabaseUtil(FingerPrintActivity.this);
		String FingerImageToString = "";
		String mIDNo = "320483199304145718";
		String whichfinger = WhichFingerTemplate[whichfingernumber];
		mDBUtil.Update_FingerPrintTemplate(FingerImageToString, whichfinger,
				mIDNo);
		btnDeleteFingerPrint.setVisibility(View.GONE);
		// Toast.makeText(getApplicationContext(),whichfingernumber+"" ,
		// Toast.LENGTH_SHORT).show();
		m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_IMGBTN_ORIGINAL,
				whichfingernumber + ""));
		m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT, 0, 0,
				NextShowText()));
	}

	/**
	 * 打开指纹识别设备
	 */
	protected Boolean OPEN_DEVICE() {
		String msg;
		boolean isOpen = false;
		m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_ID_ENABLED,
				R.id.btnOpenDevice, 0));
		m_hDevice = m_cLAPI.OpenDeviceEx();
		if (m_hDevice == 0) {
			msg = "不能打开设备";
			// m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_ID_ENABLED,
			// R.id.btnOpenDevice, 1));
		} else {
			msg = "打开设备成功！";
			isOpen = true;
			// EnableAllButtons(false, true);
		}
		// m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT, 0, 0,
		// msg));
		return isOpen;
	}

	/**
	 * 关闭设备
	 */
	protected void CLOSE_DEVICE() {
		String msg;
		if (m_hDevice == 0)
			return;
		m_cLAPI.CloseDeviceEx(m_hDevice);
		m_hDevice = 0;
		msg = "关闭设备成功";
		// EnableAllButtons(true, false);
		m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT, 0, 0, msg));
	}

	/**
	 * 获取指纹图像
	 */
	protected void GET_IMAGE() {
		int ret;
		String msg = null;
		ret = m_cLAPI.GetImage(m_hDevice, m_image);
		if (ret != LAPI.TRUE) {
			msg = "不能获取图像";
		}

		// 判断是否是获取到指纹图像，获取到指纹图像的前提下进行图像的保存
		int isFingerPress = m_cLAPI.IsPressFinger(m_hDevice, m_image);

		if (isFingerPress != 0) {

			btnDeleteFingerPrint.setVisibility(View.GONE);

			m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT, 0, 0,
					msg));
			m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_IMAGE_SAVE,
					LAPI.WIDTH, LAPI.HEIGHT, m_image));
		}

	}

	/**
	 * 响应点击指纹图标事件
	 * 
	 * @author 陆如一
	 * 
	 */
	private class ButtonListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {
			case R.id.ImgBtn_Thumb_Right:
				ReviewShowFingerPrintImage(0);
				break;
			case R.id.ImgBtn_ForeFinger_Right:
				ReviewShowFingerPrintImage(1);
				break;
			case R.id.ImgBtn_MiddleFinger_Right:
				ReviewShowFingerPrintImage(2);
				break;
			case R.id.ImgBtn_RingFinger_Right:
				ReviewShowFingerPrintImage(3);
				break;
			case R.id.ImgBtn_LittleFinger_Right:
				ReviewShowFingerPrintImage(4);
				break;
			case R.id.ImgBtn_Thumb_Left:
				ReviewShowFingerPrintImage(5);
				break;
			case R.id.ImgBtn__ForeFinger_Left:
				ReviewShowFingerPrintImage(6);
				break;
			case R.id.ImgBtn_MiddleFinger_Left:
				ReviewShowFingerPrintImage(7);
				break;
			case R.id.ImgBtn_RingFinger_Left:
				ReviewShowFingerPrintImage(8);
				break;
			case R.id.ImgBtn_LittleFinger_Left:
				ReviewShowFingerPrintImage(9);
				break;
			default:
				break;
			}
		}

	}

	/**
	 * 点击指纹图标，显示相对应的已经采集的指纹图像，并显示删除按钮
	 */
	private void ReviewShowFingerPrintImage(int which) {

		whichfingernumber = which;

		String mIDNo = "320483199304145718";
		String fingerprint = "";
		String whichFingerText = "";
		mDBUtil = new DatabaseUtil(FingerPrintActivity.this);
		FingerPrintTemplate fingerPrintTemplate = mDBUtil
				.queryByIDNo_FingerPrintTemplate(mIDNo);

		if (which == 0 && fingerPrintTemplate.getRight_Thumb_Template() != null
				&& fingerPrintTemplate.getRight_Thumb_Template() != "") {
			fingerprint = fingerPrintTemplate.getRight_Thumb_Template();
			whichFingerText = WhichFingerText[0];
		} else if (which == 1
				&& fingerPrintTemplate.getRight_ForeFinger_Template() != null
				&& fingerPrintTemplate.getRight_ForeFinger_Template() != "") {
			fingerprint = fingerPrintTemplate.getRight_ForeFinger_Template();
			whichFingerText = WhichFingerText[1];
		} else if (which == 2
				&& fingerPrintTemplate.getRight_MiddleFinger_Template() != null
				&& fingerPrintTemplate.getRight_MiddleFinger_Template() != "") {
			fingerprint = fingerPrintTemplate.getRight_MiddleFinger_Template();
			whichFingerText = WhichFingerText[2];
		} else if (which == 3
				&& fingerPrintTemplate.getRight_RingFinger_Template() != null
				&& fingerPrintTemplate.getRight_RingFinger_Template() != "") {
			fingerprint = fingerPrintTemplate.getRight_RingFinger_Template();
			whichFingerText = WhichFingerText[3];
		} else if (which == 4
				&& fingerPrintTemplate.getRight_LittleFinger_Template() != null
				&& fingerPrintTemplate.getRight_LittleFinger_Template() != "") {
			fingerprint = fingerPrintTemplate.getRight_LittleFinger_Template();
			whichFingerText = WhichFingerText[4];
		} else if (which == 5
				&& fingerPrintTemplate.getLeft_Thumb_Template() != null
				&& fingerPrintTemplate.getLeft_Thumb_Template() != "") {
			fingerprint = fingerPrintTemplate.getLeft_Thumb_Template();
			whichFingerText = WhichFingerText[5];
		} else if (which == 6
				&& fingerPrintTemplate.getLeft_ForeFinger_Template() != null
				&& fingerPrintTemplate.getLeft_ForeFinger_Template() != "") {
			fingerprint = fingerPrintTemplate.getLeft_ForeFinger_Template();
			whichFingerText = WhichFingerText[6];
		} else if (which == 7
				&& fingerPrintTemplate.getLeft_MiddleFinger_Template() != null
				&& fingerPrintTemplate.getLeft_MiddleFinger_Template() != "") {
			fingerprint = fingerPrintTemplate.getLeft_MiddleFinger_Template();
			whichFingerText = WhichFingerText[7];
		} else if (which == 8
				&& fingerPrintTemplate.getLeft_RingFinger_Template() != null
				&& fingerPrintTemplate.getLeft_RingFinger_Template() != "") {
			fingerprint = fingerPrintTemplate.getLeft_RingFinger_Template();
			whichFingerText = WhichFingerText[8];
		} else if (which == 9
				&& fingerPrintTemplate.getLeft_LittleFinger_Template() != null
				&& fingerPrintTemplate.getLeft_LittleFinger_Template() != "") {
			fingerprint = fingerPrintTemplate.getLeft_LittleFinger_Template();
			whichFingerText = WhichFingerText[9];
		}

		try {

			if (fingerprint.equals(null) || fingerprint.equals("")
					|| fingerprint == null || fingerprint == null) {
//				Toast.makeText(getApplicationContext(), fingerprint + "空",
//						Toast.LENGTH_SHORT).show();
				btnDeleteFingerPrint.setVisibility(View.GONE);
			} else {
				m_image_review = fingerprint.getBytes("ISO-8859-1");
				// Toast.makeText(getApplicationContext(), fingerprint+"不空",
				// Toast.LENGTH_SHORT).show();
				m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_IMAGE,
						LAPI.WIDTH, LAPI.HEIGHT, m_image_review));
				m_fEvent.sendMessage(m_fEvent.obtainMessage(
						MSG_CHANGE_BTNDELETETEXT, whichFingerText));
				btnDeleteFingerPrint.setVisibility(View.VISIBLE);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 子线程处理一些操作
	 */
	private final Handler m_fEvent = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SHOW_IMAGE_SAVE:
				ShowFingerBitmap((byte[]) msg.obj, msg.arg1, msg.arg2);
				/**
				 * 在存储指纹采集图像之前，生成指纹特征与数据库里面该居民已录入的指纹特征进行比对，
				 * 如果某一根手指的指纹特征已经存入数据库，则不再保存图像以及指纹特征信息
				 */
				String mIDNo = "320483199304145718";
				saveFingerPrintImage(mIDNo);

				break;
			case MSG_SHOW_IMAGE:
				ShowFingerBitmap((byte[]) msg.obj, msg.arg1, msg.arg2);
				break;
			case MSG_SHOW_TEXT:
				txt_WhichFinger.setText((String) msg.obj);
				break;
			case MSG_VIEW_TEMPLATE:
				Toast.makeText(getApplicationContext(), (String) msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			case MSG_CHANGE_BTNDELETETEXT:
				btnDeleteFingerPrint.setText("删除" + (String) msg.obj + "指纹");
				break;
			case MSG_SHOW_IMGBTN:
				ImgBtns[Integer.parseInt((String) msg.obj)]
						.setBackground(getResources().getDrawable(
								ImgBtnBackground[Integer
										.parseInt((String) msg.obj)]));
				break;
			case MSG_SHOW_IMGBTN_ORIGINAL:
				ImgBtns[Integer.parseInt((String) msg.obj)]
						.setBackground(getResources().getDrawable(
								ImgBtnBackgroundOrigin[Integer
										.parseInt((String) msg.obj)]));
				break;
			}
		};
	};

	/*
	 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	 * +++++++++++++++++++++++++
	 */
	/**
	 * 测试用
	 */
	private void TestDB() {
		mDBUtil = new DatabaseUtil(FingerPrintActivity.this);
		String whichfinger = WhichFingerTemplate[0];
		String mIDNo = "320483199304145718";
		m_cLAPI.CreateTemplate(m_hDevice, m_image, m_itemplate);// 当前采集图像生成指纹特征
		String FingerImageToString = "";
		try {
			FingerImageToString = new String(m_image, "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// mDBUtil.Update_FingerPrintTemplate(FingerImageToString, whichfinger,
		// mIDNo);
		mDBUtil.Update_FingerPrintTemplate("123", whichfinger, mIDNo);
		FingerPrintTemplate fingerPrintTemplate = mDBUtil
				.queryByIDNo_FingerPrintTemplate(mIDNo);
		Toast.makeText(getApplicationContext(),
				fingerPrintTemplate.getRight_Thumb_Template(),
				Toast.LENGTH_SHORT).show();
		// FingerPrintTemplate fingerPrintTemplate = mDBUtil
		// .queryByIDNo_FingerPrintTemplate(mIDNo);
		// try {
		// m_image_s =
		// fingerPrintTemplate.getRight_Thumb_Template().getBytes("ISO-8859-1");
		// } catch (UnsupportedEncodingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// m_cLAPI.CreateTemplate(m_hDevice, m_image_s, m_itemplate_s);
		// int nums = m_cLAPI.CompareTemplates(m_hDevice, m_itemplate,
		// m_itemplate_s);
		// Toast.makeText(getApplicationContext(), "匹配情况" + nums + "个",
		// Toast.LENGTH_LONG).show();

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
		FingerPrintImageViewer.invalidate();
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
		FingerPrintImageViewer.setImageBitmap(bmp);
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
				handler.postDelayed(this, 4000);
				btnGetImage.performClick();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	/**
	 * 保存采集到的指纹数据
	 * 
	 */
	private void saveFingerPrintImage(String mIDNo) {

		mDBUtil = new DatabaseUtil(FingerPrintActivity.this);
		String whichfinger = WhichFingerTemplate[0];// 更新哪一个指纹，默认是第一个右手大拇指
		String which = "0";// 哪一个指纹代指数字，默认是第一个右手大拇指
		String msg = "";// 消息
		int MatchNumbers = 0;// 匹配特征值个数
		FingerPrintTemplate fingerPrintTemplate = mDBUtil
				.queryByIDNo_FingerPrintTemplate(mIDNo);
		/**
		 * 如果当前手指的指纹为空，那么就是要插入当前的指纹字段中，如果当前手指字段的指纹不为空，那么就要和当前采集的指纹图像进行比对，
		 * 如果数据库里面已经存在，那么不需要插入数据库，反之插入数据库
		 */
		if (fingerPrintTemplate.getRight_Thumb_Template() == null
				|| fingerPrintTemplate.getRight_Thumb_Template() == ""
				|| fingerPrintTemplate.getRight_Thumb_Template().equals(null)
				|| fingerPrintTemplate.getRight_Thumb_Template().equals("")) {
			// 右手大拇指数据为空，将右手大拇指图像插入数据库
			whichfinger = WhichFingerTemplate[0];
			which = "0";
			FingerPrintImageToDB(whichfinger, mIDNo, which);

			//msg = "请按下右手食指";
			msg = NextShowText();
			m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT, 0, 0,
					msg));

		} else {
			// 右手大拇指数据不为空，将右手大拇指图像与当前采集的指纹图像进行比对
			MatchNumbers = FingerPrintTemplateMatch(fingerPrintTemplate
					.getRight_Thumb_Template());
			// 当前采集的指纹图像与数据库里已经存在的右手大拇指图像重合，提示换手指
			if (MatchNumbers >= 60) {
				msg = "右手大拇指已经采集，请换一个手指";
				m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT, 0,
						0, msg));
				return;
			}
			// 当前采集的指纹图像与数据库里已经存在的右手大拇指图像不重合，继续往下比对
			if (fingerPrintTemplate.getRight_ForeFinger_Template() == null
					|| fingerPrintTemplate.getRight_ForeFinger_Template() == ""
					|| fingerPrintTemplate.getRight_ForeFinger_Template()
							.equals(null)
					|| fingerPrintTemplate.getRight_ForeFinger_Template()
							.equals("")) {
				whichfinger = WhichFingerTemplate[1];
				which = "1";
				FingerPrintImageToDB(whichfinger, mIDNo, which);

				//msg = "请按下右手中指";
				msg = NextShowText();
				m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT, 0,
						0, msg));

			} else {
				MatchNumbers = FingerPrintTemplateMatch(fingerPrintTemplate
						.getRight_ForeFinger_Template());
				if (MatchNumbers >= 60) {
					msg = "右手食指已经采集，请换一个手指";
					m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT,
							0, 0, msg));
					return;
				}
				if (fingerPrintTemplate.getRight_MiddleFinger_Template() == null
						|| fingerPrintTemplate.getRight_MiddleFinger_Template() == ""
						|| fingerPrintTemplate.getRight_MiddleFinger_Template()
								.equals(null)
						|| fingerPrintTemplate.getRight_MiddleFinger_Template()
								.equals("")) {
					whichfinger = WhichFingerTemplate[2];
					which = "2";
					FingerPrintImageToDB(whichfinger, mIDNo, which);

					//msg = "请按下右手无名指";
					msg = NextShowText();
					m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT,
							0, 0, msg));

				} else {
					MatchNumbers = FingerPrintTemplateMatch(fingerPrintTemplate
							.getRight_MiddleFinger_Template());
					if (MatchNumbers >= 60) {
						msg = "右手中指已经采集，请换一个手指";
						m_fEvent.sendMessage(m_fEvent.obtainMessage(
								MSG_SHOW_TEXT, 0, 0, msg));
						return;
					}
					if (fingerPrintTemplate.getRight_RingFinger_Template() == null
							|| fingerPrintTemplate
									.getRight_RingFinger_Template() == ""
							|| fingerPrintTemplate
									.getRight_RingFinger_Template()
									.equals(null)
							|| fingerPrintTemplate
									.getRight_RingFinger_Template().equals("")) {

						whichfinger = WhichFingerTemplate[3];
						which = "3";
						FingerPrintImageToDB(whichfinger, mIDNo, which);

						//msg = "请按下右手小指";
						msg = NextShowText();
						m_fEvent.sendMessage(m_fEvent.obtainMessage(
								MSG_SHOW_TEXT, 0, 0, msg));

					} else {
						MatchNumbers = FingerPrintTemplateMatch(fingerPrintTemplate
								.getRight_RingFinger_Template());
						if (MatchNumbers >= 60) {
							msg = "右手无名指已经采集，请换一个手指";
							m_fEvent.sendMessage(m_fEvent.obtainMessage(
									MSG_SHOW_TEXT, 0, 0, msg));
							return;
						}
						if (fingerPrintTemplate
								.getRight_LittleFinger_Template() == null
								|| fingerPrintTemplate
										.getRight_LittleFinger_Template() == ""
								|| fingerPrintTemplate
										.getRight_LittleFinger_Template()
										.equals(null)
								|| fingerPrintTemplate
										.getRight_LittleFinger_Template()
										.equals("")) {
							whichfinger = WhichFingerTemplate[4];
							which = "4";
							FingerPrintImageToDB(whichfinger, mIDNo, which);

							//msg = "请按下左手大拇指";
							msg = NextShowText();
							m_fEvent.sendMessage(m_fEvent.obtainMessage(
									MSG_SHOW_TEXT, 0, 0, msg));

						} else {
							MatchNumbers = FingerPrintTemplateMatch(fingerPrintTemplate
									.getRight_LittleFinger_Template());
							if (MatchNumbers >= 60) {
								msg = "右手小拇指已经采集，请换一个手指";
								m_fEvent.sendMessage(m_fEvent.obtainMessage(
										MSG_SHOW_TEXT, 0, 0, msg));
								return;
							}
							if (fingerPrintTemplate.getLeft_Thumb_Template() == null
									|| fingerPrintTemplate
											.getLeft_Thumb_Template() == ""
									|| fingerPrintTemplate
											.getLeft_Thumb_Template().equals(
													null)
									|| fingerPrintTemplate
											.getLeft_Thumb_Template()
											.equals("")) {
								whichfinger = WhichFingerTemplate[5];
								which = "5";
								FingerPrintImageToDB(whichfinger, mIDNo, which);

								//msg = "请按下左手食指";
								msg = NextShowText();
								m_fEvent.sendMessage(m_fEvent.obtainMessage(
										MSG_SHOW_TEXT, 0, 0, msg));

							} else {
								MatchNumbers = FingerPrintTemplateMatch(fingerPrintTemplate
										.getLeft_Thumb_Template());
								if (MatchNumbers >= 60) {
									msg = "左手大拇指已经采集，请换一个手指";
									m_fEvent.sendMessage(m_fEvent
											.obtainMessage(MSG_SHOW_TEXT, 0, 0,
													msg));
									return;
								}
								if (fingerPrintTemplate
										.getLeft_ForeFinger_Template() == null
										|| fingerPrintTemplate
												.getLeft_ForeFinger_Template() == ""
										|| fingerPrintTemplate
												.getLeft_ForeFinger_Template()
												.equals(null)
										|| fingerPrintTemplate
												.getLeft_ForeFinger_Template()
												.equals("")) {
									whichfinger = WhichFingerTemplate[6];
									which = "6";
									FingerPrintImageToDB(whichfinger, mIDNo,
											which);

									//msg = "请按下左手中指";
									msg = NextShowText();
									m_fEvent.sendMessage(m_fEvent
											.obtainMessage(MSG_SHOW_TEXT, 0, 0,
													msg));

								} else {
									MatchNumbers = FingerPrintTemplateMatch(fingerPrintTemplate
											.getLeft_ForeFinger_Template());
									if (MatchNumbers >= 60) {
										msg = "左手食指已经采集，请换一个手指";
										m_fEvent.sendMessage(m_fEvent
												.obtainMessage(MSG_SHOW_TEXT,
														0, 0, msg));
										return;
									}
									if (fingerPrintTemplate
											.getLeft_MiddleFinger_Template() == null
											|| fingerPrintTemplate
													.getLeft_MiddleFinger_Template() == ""
											|| fingerPrintTemplate
													.getLeft_MiddleFinger_Template()
													.equals(null)
											|| fingerPrintTemplate
													.getLeft_MiddleFinger_Template()
													.equals("")) {
										whichfinger = WhichFingerTemplate[7];
										which = "7";
										FingerPrintImageToDB(whichfinger,
												mIDNo, which);

										//msg = "请按下左手无名指";
										msg = NextShowText();
										m_fEvent.sendMessage(m_fEvent
												.obtainMessage(MSG_SHOW_TEXT,
														0, 0, msg));

									} else {
										MatchNumbers = FingerPrintTemplateMatch(fingerPrintTemplate
												.getLeft_MiddleFinger_Template());
										if (MatchNumbers >= 60) {
											msg = "左手中指已经采集，请换一个手指";
											m_fEvent.sendMessage(m_fEvent
													.obtainMessage(
															MSG_SHOW_TEXT, 0,
															0, msg));
											return;
										}
										if (fingerPrintTemplate
												.getLeft_RingFinger_Template() == null
												|| fingerPrintTemplate
														.getLeft_RingFinger_Template() == ""
												|| fingerPrintTemplate
														.getLeft_RingFinger_Template()
														.equals(null)
												|| fingerPrintTemplate
														.getLeft_RingFinger_Template()
														.equals("")) {
											whichfinger = WhichFingerTemplate[8];
											which = "8";
											FingerPrintImageToDB(whichfinger,
													mIDNo, which);

											//msg = "请按下左手小指";
											msg = NextShowText();
											m_fEvent.sendMessage(m_fEvent
													.obtainMessage(
															MSG_SHOW_TEXT, 0,
															0, msg));

										} else {
											MatchNumbers = FingerPrintTemplateMatch(fingerPrintTemplate
													.getLeft_RingFinger_Template());
											if (MatchNumbers >= 60) {
												msg = "左手无名指已经采集，请换一个手指";
												m_fEvent.sendMessage(m_fEvent
														.obtainMessage(
																MSG_SHOW_TEXT,
																0, 0, msg));
												return;
											}
											if (fingerPrintTemplate
													.getLeft_LittleFinger_Template() == null
													|| fingerPrintTemplate
															.getLeft_LittleFinger_Template() == ""
													|| fingerPrintTemplate
															.getLeft_LittleFinger_Template()
															.equals(null)
													|| fingerPrintTemplate
															.getLeft_LittleFinger_Template()
															.equals("")) {
												whichfinger = WhichFingerTemplate[9];
												which = "9";
												FingerPrintImageToDB(
														whichfinger, mIDNo,
														which);
												return;
											} else {
												MatchNumbers = FingerPrintTemplateMatch(fingerPrintTemplate
														.getLeft_LittleFinger_Template());
												if (MatchNumbers >= 60) {
													//msg = "10个手指的指纹都已采集";
													msg = NextShowText();
													m_fEvent.sendMessage(m_fEvent
															.obtainMessage(
																	MSG_SHOW_TEXT,
																	0, 0, msg));
													return;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 按右手大拇指-右手食指-。。。-左手小指顺序依次判断哪一个为空，则下一条显示相对应的提示文字
	 * @return
	 */
	private String NextShowText() {
		String mIDNo = "320483199304145718";
		String msg = "请按下右手大拇指";
		mDBUtil = new DatabaseUtil(FingerPrintActivity.this);
		FingerPrintTemplate fingerPrintTemplate = mDBUtil
				.queryByIDNo_FingerPrintTemplate(mIDNo);
		if (fingerPrintTemplate.getRight_Thumb_Template() == null
				|| fingerPrintTemplate.getRight_Thumb_Template() == ""
				|| fingerPrintTemplate.getRight_Thumb_Template().equals(null)
				|| fingerPrintTemplate.getRight_Thumb_Template().equals("")) {
			msg = "请按下右手大拇指";
		} else if (fingerPrintTemplate.getRight_ForeFinger_Template() == null
				|| fingerPrintTemplate.getRight_ForeFinger_Template() == ""
				|| fingerPrintTemplate.getRight_ForeFinger_Template().equals(
						null)
				|| fingerPrintTemplate.getRight_ForeFinger_Template()
						.equals("")) {
			msg = "请按下右手食指";
		} else if (fingerPrintTemplate.getRight_MiddleFinger_Template() == null
				|| fingerPrintTemplate.getRight_MiddleFinger_Template() == ""
				|| fingerPrintTemplate.getRight_MiddleFinger_Template().equals(
						null)
				|| fingerPrintTemplate.getRight_MiddleFinger_Template().equals(
						"")) {
			msg = "请按下右手中指";
		} else if (fingerPrintTemplate.getRight_RingFinger_Template() == null
				|| fingerPrintTemplate.getRight_RingFinger_Template() == ""
				|| fingerPrintTemplate.getRight_RingFinger_Template().equals(
						null)
				|| fingerPrintTemplate.getRight_RingFinger_Template()
						.equals("")) {
			msg = "请按下右手无名指";
		} else if (fingerPrintTemplate.getRight_LittleFinger_Template() == null
				|| fingerPrintTemplate.getRight_LittleFinger_Template() == ""
				|| fingerPrintTemplate.getRight_LittleFinger_Template().equals(
						null)
				|| fingerPrintTemplate.getRight_LittleFinger_Template().equals(
						"")) {
			msg = "请按下右手小指";
		} else if (fingerPrintTemplate.getLeft_Thumb_Template() == null
				|| fingerPrintTemplate.getLeft_Thumb_Template() == ""
				|| fingerPrintTemplate.getLeft_Thumb_Template().equals(null)
				|| fingerPrintTemplate.getLeft_Thumb_Template().equals("")) {
			msg = "请按下左手大拇指";
		} else if (fingerPrintTemplate.getLeft_ForeFinger_Template() == null
				|| fingerPrintTemplate.getLeft_ForeFinger_Template() == ""
				|| fingerPrintTemplate.getLeft_ForeFinger_Template().equals(
						null)
				|| fingerPrintTemplate.getLeft_ForeFinger_Template().equals("")) {
			msg = "请按下左手食指";
		} else if (fingerPrintTemplate.getLeft_MiddleFinger_Template() == null
				|| fingerPrintTemplate.getLeft_MiddleFinger_Template() == ""
				|| fingerPrintTemplate.getLeft_MiddleFinger_Template().equals(
						null)
				|| fingerPrintTemplate.getLeft_MiddleFinger_Template().equals(
						"")) {
			msg = "请按下左手中指";
		} else if (fingerPrintTemplate.getLeft_RingFinger_Template() == null
				|| fingerPrintTemplate.getLeft_RingFinger_Template() == ""
				|| fingerPrintTemplate.getLeft_RingFinger_Template().equals(
						null)
				|| fingerPrintTemplate.getLeft_RingFinger_Template().equals("")) {
			msg = "请按下左手无名指";
		} else if (fingerPrintTemplate.getLeft_LittleFinger_Template() == null
				|| fingerPrintTemplate.getLeft_LittleFinger_Template() == ""
				|| fingerPrintTemplate.getLeft_LittleFinger_Template().equals(
						null)
				|| fingerPrintTemplate.getLeft_LittleFinger_Template().equals(
						"")) {
			msg = "请按下左手小指";
		}else{
			msg = "10个手指的指纹都已采集";
		}

		return msg;
	}

	/**
	 * 比较当前要存入数据库内的指纹图像是否和数据库内已经存在的图像发生冲突
	 * 
	 * @param fingerprinttemplate
	 * @return
	 */
	private int FingerPrintTemplateMatch(String fingerprinttemplate) {
		try {
			m_image_s = fingerprinttemplate.getBytes("ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		m_cLAPI.CreateTemplate(m_hDevice, m_image, m_itemplate);
		m_cLAPI.CreateTemplate(m_hDevice, m_image_s, m_itemplate_s);
		return m_cLAPI.CompareTemplates(m_hDevice, m_itemplate, m_itemplate_s);
	}

	/**
	 * 将指纹图像存入数据库
	 */
	private void FingerPrintImageToDB(String whichfinger, String mIDNo,
			String which) {
		try {
			String FingerImageToString = "";
			FingerImageToString = new String(m_image, "ISO-8859-1");
			// Toast.makeText(
			// getApplicationContext(),
			// whichfinger + "+FingerImageToString长度"
			// + FingerImageToString.length(), Toast.LENGTH_SHORT)
			// .show();
			mDBUtil = new DatabaseUtil(FingerPrintActivity.this);
			mDBUtil.Update_FingerPrintTemplate(FingerImageToString,
					whichfinger, mIDNo);
			String sql = "select * from "
					+ MyHelper.TABLE_NAME_FingerPrintTemplate
					+ " where mIDNo = '320483199304145718' and Right_Thumb_Template is not null";
			int num = mDBUtil.queryBySQL_FingerPrintTemplat(sql);
			// if (num > 0) {
			// Toast.makeText(getApplicationContext(), whichfinger + "已经存入",
			// Toast.LENGTH_SHORT).show();
			// }

			m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_IMGBTN, which));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 创建指纹特征
	 */
	protected void CreateFingerPrintTemp() {
		int ret;
		String msg;
		ret = m_cLAPI.IsPressFinger(m_hDevice, m_image);
		if (ret == 0) {
			msg = "没有采集到指纹图像";
			m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT, 0, 0,
					msg));
			return;
		}

		ret = m_cLAPI.CreateTemplate(m_hDevice, m_image, m_itemplate);

		if (ret == 0) {
			msg = "创建指纹特征失败";
		} else {
			msg = "创建指纹特征成功";
		}
		m_fEvent.sendMessage(m_fEvent.obtainMessage(MSG_SHOW_TEXT, 0, 0, msg));
	}

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
