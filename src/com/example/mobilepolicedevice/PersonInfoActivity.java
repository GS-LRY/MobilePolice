package com.example.mobilepolicedevice;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;

import com.example.bean.NormalList_Result;
import com.example.db.DatabaseUtil;
import com.example.db.MyHelper;
import com.example.http.HttpOperation;
import com.example.json.JsonUtil;
import com.example.model.Escaped;
import com.example.model.Normal;
import com.example.service.impl.OneMoreFunctionImpl;
import com.hdos.usbdevice.publicSecurityIDCardLib;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Matrix;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PersonInfoActivity extends BaseActivity implements
		OnBottomDragListener {

	PersonInfoActivity mact;

	Button btnAreaSelec;// 获取搜索区域按钮
	Button btnPlaceSelec;// 获取搜索地点按钮
	Button btnInfo;// 获取读取信息按钮
	Button btnHandSearch;// 比对身份证号-在逃人员
	EditText editName;// 获取姓名
	EditText editSex;// 获取性别
	EditText editNation;// 获取民族
	EditText editBirth;// 获取生日
	EditText editId;// 获取身份证号
	EditText editAddr;// 获取地址
	EditText editOffice;// 获取发证机关
	EditText expDate;// 有效期限
	TextView imageId;// 获取头像
	
	private LinearLayout Llyt_Escaped;// 在逃人员显示区域
	private EditText editPersonTypeShow;// 人员分类
	private EditText editLADWShow;// 立案单位
	private EditText editNRBJZDRYKSJShow;// 纳入部级重点人员库时间
	private EditText editHJDXZShow;// 户籍所在地
	private EditText editXZDXZShow;// 现居住地址
	private EditText editZJLASJShow;// 最近立案时间

	private publicSecurityIDCardLib iDCardDevice;
	private Context context;

	private OneMoreFunctionImpl oneMoreFunctionImpl = new OneMoreFunctionImpl();
	private HttpOperation httpOperation = new HttpOperation();
	private JsonUtil jsonUtil = new JsonUtil();

	private DatabaseUtil mDBUtil;

	private byte[] name = new byte[32];
	private String mName;
	private byte[] sex = new byte[6];
	private String mSex;
	private byte[] birth = new byte[18];
	private String mBirth;
	private byte[] nation = new byte[12];
	private String mNation;
	private byte[] address = new byte[72];
	private String mAddress;
	private byte[] Department = new byte[32];
	private String mDepartment;
	private byte[] IDNo = new byte[38];
	private String mIDNo;
	private byte[] EffectDate = new byte[18];
	private String mExpDate;
	private byte[] ExpireDate = new byte[18];
	byte[] BmpFile = new byte[38556];
	byte[] FpMsg = new byte[1024];
	private boolean bFp = false;
	String pkName;
	SpannableString mImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personinformation_collect, this);

		mact = this;
		pkName = this.getPackageName();
		context = this;
		// iDCardDevice = new publicSecurityIDCardLib(this);
		// iDCardDevice = null;
		editName = (EditText) findViewById(R.id.editName);
		editSex = (EditText) findViewById(R.id.editSex);
		editNation = (EditText) findViewById(R.id.editNation);
		editBirth = (EditText) findViewById(R.id.editBirth);
		editId = (EditText) findViewById(R.id.editId);
		editAddr = (EditText) findViewById(R.id.editAddr);
		editOffice = (EditText) findViewById(R.id.editOffice);
		expDate = (EditText) findViewById(R.id.expDate);
		imageId = (TextView) findViewById(R.id.imageId);
		btnAreaSelec = (Button) findViewById(R.id.btnAreaSelec);
		btnPlaceSelec = (Button) findViewById(R.id.btnPlaceSelec);
		btnInfo = (Button) findViewById(R.id.btnInfo);
		btnHandSearch = (Button) findViewById(R.id.btn_handSearch);
		Llyt_Escaped = (LinearLayout)findViewById(R.id.Llyt_Escaped);
		editPersonTypeShow = (EditText)findViewById(R.id.editPersonTypeShow);
		editLADWShow = (EditText)findViewById(R.id.editLADWShow);
		editNRBJZDRYKSJShow = (EditText)findViewById(R.id.editNRBJZDRYKSJShow);
		editHJDXZShow = (EditText)findViewById(R.id.editHJDXZShow);
		editXZDXZShow = (EditText)findViewById(R.id.editXZDXZShow);
		editZJLASJShow = (EditText)findViewById(R.id.editZJLASJShow);

		btnInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int ret;
				if (iDCardDevice == null)
					iDCardDevice = new publicSecurityIDCardLib(context);
				ret = iDCardDevice.readBaseMsgToStr(pkName, BmpFile, FpMsg,
						name, sex, nation, birth, address, IDNo, Department,
						EffectDate, ExpireDate);

				int[] colors = iDCardDevice.convertByteToColor(BmpFile);
				Bitmap b = Bitmap.createBitmap(colors, 102, 126,
						Config.ARGB_8888);
				ImageSpan imgSpan = new ImageSpan(scaleBitmap(b,
						imageId.getWidth(), imageId.getHeight()));
				SpannableString spanString = new SpannableString("icon");
				spanString.setSpan(imgSpan, 0, 4,
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				mImage = spanString;
				if (ret == 0x90) {
					// 指纹显示
					bFp = false;
					String strFp = "";
					for (int i = 0; i < 1024; i++) {
						strFp = strFp + String.format("%02x ", FpMsg[i] & 0xFF);
						if (FpMsg[i] != 0x00)
							bFp = true;
					}
					try {
						mName = new String(name, "Unicode");
						mSex = new String(sex, "Unicode");
						mNation = new String(nation, "Unicode");
						mBirth = new String(birth, "Unicode");
						mIDNo = new String(IDNo, "Unicode");
						mAddress = new String(address, "Unicode");
						mDepartment = new String(Department, "Unicode");
						mExpDate = new String(EffectDate, "Unicode") + "至"
								+ new String(ExpireDate, "Unicode");
						new Thread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								Message message = new Message();
								message.what = 0;
								mHandler.sendMessage(message);
							}
						}).start();
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					// Toast.makeText(getApplicationContext(),
					// "模块状态错误:" + String.format("0x%02x", ret),
					// Toast.LENGTH_SHORT).show();
					return;
				}
			}
		});
		/* 每隔1秒发送读取身份证信息命令 */
		handler.postDelayed(runnable, 1000);

		btnAreaSelec.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}
		});

		btnPlaceSelec.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}
		});

		btnHandSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!oneMoreFunctionImpl.NetWorkStatus(getApplicationContext())) {
					Toast.makeText(getApplicationContext(), "网络未连接",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (editId.getText().toString() == ""
						|| editId.getText().toString().equals("")
						|| editId.getText() == null) {
					Toast.makeText(getApplicationContext(), "所要比对的身份证号码为空",
							Toast.LENGTH_SHORT).show();
					return;
				}
				new Thread(new Runnable() {
					@Override
					public void run() {
						new Thread(networkTask).start();
					}
				}).start();
			}
		});
	}

	/**
	 * 访问服务器
	 */
	Runnable networkTask = new Runnable() {
		@Override
		public void run() {
			// 在这里进行 http request.网络请求相关操作
			String result = httpOperation.getStringFromServer(
					"compareEscapedByPersonId.do", editId.getText().toString());
			Message msg = new Message();
			msg.what = 1;
			msg.obj = result;
			mHandler.sendMessage(msg);
		}
	};

	/**
	 * 子线程进行UI更新
	 */
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 0:
				editName.setText(mName);
				editSex.setText(mSex);
				editNation.setText(mNation);
				editBirth.setText(mBirth);
				editId.setText(mIDNo);
				editAddr.setText(mAddress);
				editOffice.setText(mDepartment);
				expDate.setText(mExpDate);
				imageId.setText(mImage);
				mDBUtil = new DatabaseUtil(PersonInfoActivity.this);

				String sql = "SELECT * FROM "
						+ MyHelper.TABLE_NAME_FingerPrintTemplate
						+ " WHERE mIDNo = '" + mIDNo + "'";
				int num = mDBUtil.queryBySQL_FingerPrintTemplat(sql);
				if (num == 0) {
					mDBUtil.Insert_FingerPrintTemplate(mIDNo);
				}
				num = mDBUtil.queryBySQL_FingerPrintTemplat(sql);

				List<Normal> norList = null;
				int nornum = 0;

				// 插入数据之前查询记录条数
				norList = mDBUtil.queryAll_Normal();
				nornum = norList.size();
				Toast.makeText(getApplicationContext(),
						"核查之前有" + nornum + "条数据", Toast.LENGTH_SHORT).show();

				// 将要存入本地数据库的信息进行准备
				Normal normal = new Normal();
				normal.setPersonid(mIDNo);
				normal.setPersonname(mName);
				normal.setAddresscode("123");
				normal.setAddressname("上海");
				normal.setAddressgps("sss");
				normal.setUserid(22);
				normal.setInfosubmit(0);
				if (bFp) {
					normal.setPersonfp(1);
				} else {
					normal.setPersonfp(0);
				}
				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Date nowTime = new Date();
				normal.setCommittime(formatter.format(nowTime));

				// 在插入数据之前进行信息比对，看是否在一定时间范围内已经存入本地数据库
				if (norList.size() > 0) {
					norList = mDBUtil.queryNormalByPersonid(normal
							.getPersonid());
					int differsMinutes = 0;// 相差分钟数
					for (int i = 0; i < norList.size(); i++) {
						Normal n = norList.get(i);
						try {
							Date oldCommittime = formatter.parse(n
									.getCommittime());
							long milliseconds = nowTime.getTime()
									- oldCommittime.getTime();
							differsMinutes = (int) (milliseconds / (60 * 1000));
							// 同一个人的信息在五分钟内被重复核查，信息不存入数据库
							if (differsMinutes <= 5) {
								break;
							}
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					// 将核录信息记录存入本地数据库
					// 相隔超过5分钟允许插入同一个人的信息
					if (differsMinutes > 5) {
						mDBUtil.Insert_Normal(normal);
					}
				} else {
					mDBUtil.Insert_Normal(normal);
				}

				// 插入数据之后查询记录条数
				norList = mDBUtil.queryAll_Normal();
				nornum = norList.size();
				Toast.makeText(getApplicationContext(),
						"核查之后有" + nornum + "条数据", Toast.LENGTH_SHORT).show();
				
				// 自动去比对在逃人员数据库
				new Thread(new Runnable() {
					@Override
					public void run() {
						new Thread(networkTask).start();
					}
				}).start();
				break;
			case 1:
				String result = msg.obj.toString();
				Escaped escaped = null;
				if (result.equals("timeout") || result == "timeout") {
					Toast.makeText(getApplicationContext(),
							"连接超时，请检查服务器是否正常运行", Toast.LENGTH_LONG).show();
				} else {
					if (result.equals("false") || result == "false") {
						// 隐藏在逃人员显示界面
						Llyt_Escaped.setVisibility(View.GONE);
//						Toast.makeText(getApplicationContext(), "不是在逃人员",
//								Toast.LENGTH_LONG).show();
					} else {
						// 如果所查身份为在逃人员，将信息存入本地数据库
						ArrayList<Escaped> escapedList = jsonUtil.toEscapedList(result);
//						Toast.makeText(getApplicationContext(), escapedList.size(), Toast.LENGTH_SHORT).show();
						if(escapedList.size()>0){
							escaped = escapedList.get(0);
							mDBUtil = new DatabaseUtil(PersonInfoActivity.this);
							mDBUtil.Insert_Escaped(escaped);
						}
						// 显示在逃人员信息
						Llyt_Escaped.setVisibility(View.VISIBLE);
						editName.setText(escaped.getXm());
						editSex.setText(escaped.getXb());
						
						editPersonTypeShow.setText(escaped.getZdryxl());
						editLADWShow.setText(escaped.getLadw());
						formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						editNRBJZDRYKSJShow.setText(formatter.format(escaped.getNrbjzdryksj()));
						editHJDXZShow.setText(escaped.getHjdxz());
						editXZDXZShow.setText(escaped.getXzdxz());
						editZJLASJShow.setText(formatter.format(escaped.getZjlasj()));
					}
				}
				break;
			default:
				break;
			}

		}
	};

	/**
	 * 根据给定的宽和高进行拉伸
	 * 
	 * @param origin
	 *            原图
	 * @param newWidth
	 *            新图的宽
	 * @param newHeight
	 *            新图的高
	 * @return
	 */
	private Bitmap scaleBitmap(Bitmap origin, int newWidth, int newHeight) {
		if (origin == null) {
			return null;
		}
		int height = origin.getHeight();
		int width = origin.getWidth();
		float scaleWidth = (float) (((float) newWidth * 2.5) / width);
		float scaleHeight = (float) (((float) newHeight * 2.5) / height);
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix,
				false);
		if (!origin.isRecycled()) {
			origin.recycle();
		}
		return newBM;
	}

	/**
	 * 定时器，每隔1秒发送读取身份证信息命令
	 */
	private Handler handler = new Handler();
	private Runnable runnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				handler.postDelayed(this, 5000);
				btnInfo.performClick();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	public Activity getActivity() {
		// TODO Auto-generated method stub
		return this;
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
	public void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDragBottom(boolean rightToLeft) {
		finish();
	}
}
