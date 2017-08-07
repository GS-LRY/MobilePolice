package com.example.mobilepolicedevice;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import zuo.biao.library.interfaces.OnBottomDragListener;
import zuo.biao.library.manager.SystemBarTintManager;

import com.example.bean.NormalList_Result;
import com.example.db.DatabaseUtil;
import com.example.db.MyHelper;
import com.example.http.HttpOperation;
import com.example.json.JsonUtil;
import com.example.model.Escaped;
import com.example.model.Normal;
import com.example.service.GetLatestEscapedDataService;
import com.example.service.UploadNormalRecordService;
import com.example.service.impl.OneMoreFunctionImpl;
import com.hdos.usbdevice.publicSecurityIDCardLib;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PersonInfoActivity extends BaseActivity implements
		OnBottomDragListener {

	PersonInfoActivity mact;

	// Button btnAreaSelec;// 获取搜索区域按钮
	// Button btnPlaceSelec;// 获取搜索地点按钮
	Spinner spnAreaSelec;// 获取搜索区域下拉列表
	private String strAreaSelec;
	Spinner spnPlaceSelec;// 获取搜索地点下拉列表
	private String strPlaceSelec;
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
	TextView txtuserstatus;// 用户状态

	private LinearLayout Llyt_Escaped;// 在逃人员显示区域
	private LinearLayout Llyt_NoEscaped;// 非在逃人员显示区域
	private EditText editPersonTypeShow;// 人员分类
	private EditText editLADWShow;// 立案单位
	private EditText editNRBJZDRYKSJShow;// 纳入部级重点人员库时间
	private EditText editHJDXZShow;// 户籍所在地
	private EditText editXZDXZShow;// 现居住地址
	private EditText editZJLASJShow;// 最近立案时间

	private int isEscaped = 0;// 是否是在逃或重点人员，默认是非在逃或重点人员

	private boolean isChecked = false;// 是否已经核查

	private publicSecurityIDCardLib iDCardDevice;
	private Context context;
	private DatabaseUtil mDButil = new DatabaseUtil(PersonInfoActivity.this);
	private OneMoreFunctionImpl oneMoreFunctionImpl = new OneMoreFunctionImpl();
	private HttpOperation httpOperation = new HttpOperation();
	private JsonUtil jsonUtil = new JsonUtil();

	private DatabaseUtil mDBUtil;

	private ArrayAdapter<String> AreaAdapter, PlaceAdapter;
	private String[] area = null;
	private String[] place = null;

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

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		setContentView(R.layout.personinformation_collect, this);

		/********************************** 畅享7体检版 **********************************/
		// Intent startIntent = new
		// Intent(this,UploadNormalRecordService.class);
		// startService(startIntent);
		// // 当本地没有在逃人员数据时，拉下来最新加入的100条数据
		// if (oneMoreFunctionImpl.NetWorkStatus(getApplicationContext())) {
		// String sql = "select * from "+MyHelper.TABLE_NAME_Escaped;
		// int num = mDButil.queryNumBySQL(sql);
		// // Toast.makeText(getApplicationContext(), "在逃人员数"+num,
		// Toast.LENGTH_SHORT);
		// if(num==0){
		// new Thread(netWorkTask).start();
		// }else{
		// Intent getLatestEscapedIntent = new
		// Intent(this,GetLatestEscapedDataService.class);
		// startService(getLatestEscapedIntent);
		// }
		// }
		String sql = "select * from " + MyHelper.TABLE_NAME_Escaped;
		int num = mDButil.queryNumBySQL(sql);
		Toast.makeText(getApplicationContext(), "在逃人员数" + num,
				Toast.LENGTH_SHORT);
		if (num == 0) {
			Message msg = new Message();
			msg.what = 4;
			mHandler.sendMessage(msg);
		}
		/********************************** 畅享7体检版 **********************************/
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
		// btnAreaSelec = (Button) findViewById(R.id.btnAreaSelec);
		// btnPlaceSelec = (Button) findViewById(R.id.btnPlaceSelec);
		spnAreaSelec = (Spinner) findViewById(R.id.spnAreaSelec);
		spnPlaceSelec = (Spinner) findViewById(R.id.spnPlaceSelec);
		btnInfo = (Button) findViewById(R.id.btnInfo);
		btnHandSearch = (Button) findViewById(R.id.btn_handSearch);
		Llyt_Escaped = (LinearLayout) findViewById(R.id.Llyt_Escaped);
		Llyt_NoEscaped = (LinearLayout) findViewById(R.id.Llyt_NoEscaped);
		editPersonTypeShow = (EditText) findViewById(R.id.editPersonTypeShow);
		editLADWShow = (EditText) findViewById(R.id.editLADWShow);
		editNRBJZDRYKSJShow = (EditText) findViewById(R.id.editNRBJZDRYKSJShow);
		editHJDXZShow = (EditText) findViewById(R.id.editHJDXZShow);
		editXZDXZShow = (EditText) findViewById(R.id.editXZDXZShow);
		editZJLASJShow = (EditText) findViewById(R.id.editZJLASJShow);
		txtuserstatus = (TextView) findViewById(R.id.text_userstatus);

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

		area = this.getResources().getStringArray(R.array.area);
		place = this.getResources().getStringArray(R.array.lujiazui);
		PlaceAdapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, place);
		PlaceAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnPlaceSelec.setAdapter(PlaceAdapter);

		AreaAdapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, area);
		AreaAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnAreaSelec.setAdapter(AreaAdapter);

		// 区域选择下拉列表选择事件
		spnAreaSelec.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Spinner spinner = (Spinner) parent;
				String areaname = (String) spinner.getItemAtPosition(position);
				strAreaSelec = areaname;
				PlaceAdapter = null;
				if (areaname == "陆家嘴街道" || areaname.equals("陆家嘴街道")) {
					place = getResources().getStringArray(R.array.lujiazui);
				} else if (areaname == "周家渡街道" || areaname.equals("周家渡街道")) {
					place = getResources().getStringArray(R.array.zhoujiadu);
				} else if (areaname == "塘桥街道" || areaname.equals("塘桥街道")) {
					place = getResources().getStringArray(R.array.tangqiao);
				} else if (areaname == "唐镇" || areaname.equals("唐镇")) {
					place = getResources().getStringArray(R.array.tangzhen);
				} else if (areaname == "三林镇" || areaname.equals("三林镇")) {
					place = getResources().getStringArray(R.array.sanlinzhen);
				} else if (areaname == "张江高科技园区" || areaname.equals("张江高科技园区")) {
					place = getResources().getStringArray(
							R.array.zhangjianggaokejiyuanqu);
				}
				PlaceAdapter = new ArrayAdapter<String>(context,
						android.R.layout.simple_spinner_item, place);
				PlaceAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spnPlaceSelec.setAdapter(PlaceAdapter);

				TextView tv = (TextView) view;
				tv.setTextSize(12.0f);
				tv.setGravity(Gravity.LEFT);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		// 地点选择下拉列表选择事件
		spnPlaceSelec.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Spinner spinner = (Spinner) parent;

				strPlaceSelec = (String) spinner.getItemAtPosition(position);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		btnHandSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 关闭虚拟键盘
				hintKbTwo();
				if (editId.getText().toString() == ""
						|| editId.getText().toString().equals("")
						|| editId.getText() == null) {
					Toast.makeText(getApplicationContext(), "所要比对的身份证号码为空",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (!oneMoreFunctionImpl.NetWorkStatus(getApplicationContext())) {
					Message msg = new Message();
					msg.what = 3;
					mHandler.sendMessage(msg);
				} else {
					// 点击查询身份证号时，把不必要的信息清空
					editNation.setText("");
					editBirth.setText("");
					editAddr.setText("");
					editOffice.setText("");
					expDate.setText("");
					imageId.setText("");

					new Thread(new Runnable() {
						@Override
						public void run() {
							new Thread(networkTask).start();
						}
					}).start();
				}
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
			// 连接超时，放弃访问服务器，转向访问本地在逃人员数据库
			if (result.equals("timeout") || result == "timeout") {
				msg.what = 3;
			} else {
				msg.what = 1;
			}
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
				editBirth.setText(getBirthDayFromSFZH(mBirth));
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
				// Toast.makeText(getApplicationContext(),
				// "核查之前有" + nornum + "条数据", Toast.LENGTH_SHORT).show();

				// 将要存入本地数据库的信息进行准备
				Normal normal = new Normal();
				normal.setPersonid(mIDNo);
				normal.setPersonname(mName);
				normal.setAddresscode("819");
				normal.setAddressname(strAreaSelec + "," + strPlaceSelec);
				normal.setAddressgps("31.1962772285,121.5947621898");
				normal.setUserid(30);
				normal.setInfosubmit(0);
				if (bFp) {
					normal.setPersonfp(1);
				} else {
					normal.setPersonfp(0);
				}
				normal.setComparfp(0);
				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Date nowTime = new Date();
				normal.setCommittime(formatter.format(nowTime));

				// 联网状态下，自动去服务器比对在逃人员数据库
				if (oneMoreFunctionImpl.NetWorkStatus(getApplicationContext())) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							new Thread(networkTask).start();
						}
					}).start();
				} else {
					// 离线状态下，虚拟点击比对身份证按钮，比对离线数据库
					btnHandSearch.performClick();
				}

				// Toast.makeText(getApplicationContext(),
				// isEscaped+"21", Toast.LENGTH_SHORT).show();
				// 已经核查过了
				if (isChecked) {
					normal.setIsescaped(isEscaped);// 设置是否是在逃或重点人员

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
						// Toast.makeText(getApplicationContext(),
						// "应该插入数据", Toast.LENGTH_SHORT).show();

						mDBUtil.Insert_Normal(normal);
					}

					// 插入数据之后查询记录条数
					// norList = mDBUtil.queryAll_Normal();
					// nornum = norList.size();
					isChecked = false;// 插入数据后，将核查状态置为初始状态
				}

				// Toast.makeText(getApplicationContext(),
				// "核查之后有" + nornum + "条数据", Toast.LENGTH_SHORT).show();
				break;
			case 1:
				String result = msg.obj.toString();
				Escaped escaped = null;
				if (result.equals("timeout") || result == "timeout") {
					// Toast.makeText(getApplicationContext(),
					// "连接超时，请检查服务器是否正常运行", Toast.LENGTH_LONG).show();
				} else {
					if (result.equals("false") || result == "false") {

						isEscaped = 0;// 不是在逃或重点人员
						editName.setText("");
						editSex.setText("");

						// 隐藏在逃人员显示界面
						Llyt_Escaped.setVisibility(View.GONE);
						// 显示非在逃人员界面
						Llyt_NoEscaped.setVisibility(View.VISIBLE);
						// Toast.makeText(getApplicationContext(), "不是在逃人员",
						// Toast.LENGTH_LONG).show();
					} else {
						// 如果所查身份为在逃人员，将信息存入本地数据库
						isEscaped = 1;// 是在逃或重点人员
						ArrayList<Escaped> escapedList = jsonUtil
								.toEscapedList(result);
						// Toast.makeText(getApplicationContext(),
						// escapedList.size(), Toast.LENGTH_SHORT).show();
						if (escapedList.size() > 0) {
							escaped = escapedList.get(0);
							mDBUtil = new DatabaseUtil(PersonInfoActivity.this);
							mDBUtil.Insert_Escaped(escaped);
						}
						// 显示在逃人员信息
						Llyt_Escaped.setVisibility(View.VISIBLE);
						// 隐藏非在逃人员信息
						Llyt_NoEscaped.setVisibility(View.GONE);
						editName.setText(escaped.getXm());
						editSex.setText(escaped.getXb());
						editBirth.setText(getBirthDay(escaped.getSfzh()));
						editPersonTypeShow.setText(escaped.getZdryxl());
						editLADWShow.setText(escaped.getLadw());
						formatter = new SimpleDateFormat("yyyy-MM-dd");
						editNRBJZDRYKSJShow.setText(formatter.format(escaped
								.getNrbjzdryksj()));
						editHJDXZShow.setText(escaped.getHjdxz());
						editXZDXZShow.setText(escaped.getXzdxz());
						editZJLASJShow.setText(formatter.format(escaped
								.getZjlasj()));
					}
				}
				isChecked = true;// 已经核查
				break;
			case 2:
				String userstatus = msg.obj.toString();
				txtuserstatus.setText(userstatus);
				break;
			case 3:
				// sql = "select * from "+MyHelper.TABLE_NAME_Escaped;
				// num = mDBUtil.queryNumBySQL(sql);
				// Toast.makeText(getApplicationContext(),
				// ""+num,Toast.LENGTH_SHORT).show();
				Escaped newescaped = null;
				mDBUtil = new DatabaseUtil(PersonInfoActivity.this);
				try {
					newescaped = mDBUtil.queryEscapedByPersonId(editId
							.getText().toString());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (newescaped == null) {
					// editName.setText("");
					// editSex.setText("");
					// editBirth.setText("");
					isEscaped = 0;// 不是在逃或重点人员
					// 隐藏在逃人员显示界面
					Llyt_Escaped.setVisibility(View.GONE);
					// 显示非在逃人员界面
					Llyt_NoEscaped.setVisibility(View.VISIBLE);
				} else {
					isEscaped = 1;// 是在逃或重点人员
					// 显示在逃人员信息
					Llyt_Escaped.setVisibility(View.VISIBLE);
					// 隐藏非在逃人员信息
					Llyt_NoEscaped.setVisibility(View.GONE);
					editName.setText(newescaped.getXm());
					editSex.setText(newescaped.getXb());
					editBirth.setText(getBirthDay(newescaped.getSfzh()));
					editPersonTypeShow.setText(newescaped.getZdryxl());
					editLADWShow.setText(newescaped.getLadw());
					formatter = new SimpleDateFormat("yyyy-MM-dd");
					editNRBJZDRYKSJShow.setText(formatter.format(newescaped
							.getNrbjzdryksj()));
					editHJDXZShow.setText(newescaped.getHjdxz());
					editXZDXZShow.setText(newescaped.getXzdxz());
					editZJLASJShow.setText(formatter.format(newescaped
							.getZjlasj()));
				}
				isChecked = true;// 已经核查
				break;
			case 4:
				try {
					InputStream in = getAssets().open("t_escaped.sql");
					mDButil.InitEscapedData(in);
					String sqls = "select * from "
							+ MyHelper.TABLE_NAME_Escaped;
					int nums = mDButil.queryNumBySQL(sqls);
					Toast.makeText(getApplicationContext(), "在逃人员数" + nums,
							Toast.LENGTH_SHORT);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			default:
				break;
			}

		}
	};

	// 此方法只是关闭软键盘
	private void hintKbTwo() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive() && getCurrentFocus() != null) {
			if (getCurrentFocus().getWindowToken() != null) {
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}

	/**
	 * 从在逃人员身份证号码中提取生日
	 */
	private String getBirthDay(String str) {
		int i = 6;
		String year = str.substring(i, i + 4);// 年
		String month = str.substring(i + 4, i + 6);// 月
		String day = str.substring(i + 6, i + 8);// 日
		return year + "年" + month + "月" + day + "日";
	}

	/**
	 * 从读取身份证号码中提取生日
	 */
	private String getBirthDayFromSFZH(String str) {
		int i = 0;
		String year = str.substring(i, i + 4);// 年
		String month = str.substring(i + 4, i + 6);// 月
		String day = str.substring(i + 6, i + 8);// 日
		return year + "年" + month + "月" + day + "日";
	}

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
		// Mate 9 newWidth*3 newHeight*3
		// 华为畅享7 plus newWidth*2 newHeight*2
		float scaleWidth = (float) (((float) newWidth * 3) / width);
		float scaleHeight = (float) (((float) newHeight * 3) / height);
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
				handler.postDelayed(this, 1000);
				Message msg = new Message();
				msg.what = 2;
				String result = "用户状态：在线";
				if (!oneMoreFunctionImpl.NetWorkStatus(getApplicationContext())) {
					result = "用户状态：离线";
				}
				msg.obj = result;
				mHandler.sendMessage(msg);

				btnInfo.performClick();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	/********************************** 畅享7体检版 **********************************/
	// Runnable netWorkTask = new Runnable() {
	// public void run() {
	// String jsondata =
	// httpOperation.getStringFromServer("getEscapedHundred.do","");
	// Message msg = new Message();
	// msg.what = 0;
	// msg.obj = jsondata;
	// handlers.sendMessage(msg);
	// }
	// };
	//
	// Handler handlers = new Handler(){
	// public void handleMessage(Message msg) {
	// switch (msg.what) {
	// case 0:
	// String jsondata = msg.obj.toString();
	// if(jsondata.length()>20){
	// JsonUtil jsonutil = new JsonUtil();
	// ArrayList<Escaped> escapedList = jsonutil.toEscapedList(jsondata);
	// // Escaped escaped = escapedList.get(0);
	// // SimpleDateFormat formatter = new
	// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// // Toast.makeText(getApplicationContext(),
	// "XM:"+escaped.getXm()+"XB:"+escaped.getXb()+"SFZH:"+escaped.getSfzh()+"ZDRYLBBJ:"+escaped.getZdrylbbj()+""
	// +
	// //
	// "ZDRYXL:"+escaped.getZdryxl()+"LADW:"+escaped.getLadw()+"NRBJZDRYKSJ:"+formatter.format(escaped.getNrbjzdryksj())+"HJDQH:"+escaped.getHjdqh()+"HJDXZ:"+escaped.getHjdxz()+"XZDQH:"+escaped.getXzdqh()+""
	// +
	// //
	// "XZDXZ:"+escaped.getXzdxz()+"ZJLASJ:"+formatter.format(escaped.getZjlasj()),
	// Toast.LENGTH_LONG).show();
	// for (int i = 0; i < escapedList.size(); i++) {
	// Escaped escaped = escapedList.get(i);
	// mDButil.Insert_Escaped(escaped);
	// }
	// String sql = "select * from "+MyHelper.TABLE_NAME_Escaped;
	// int num = mDButil.queryNumBySQL(sql);
	//
	// if(num>=100){
	// Toast.makeText(getApplicationContext(), "获取在逃人员信息成功",
	// Toast.LENGTH_SHORT).show();
	// }else{
	// Toast.makeText(getApplicationContext(), "插入在逃人员信息失败",
	// Toast.LENGTH_SHORT).show();
	// }
	// }else {
	// if(jsondata == "timeout" || jsondata.equals("timeout")){
	// Toast.makeText(getApplicationContext(),
	// "连接超时，请检查服务器是否正常运行", Toast.LENGTH_SHORT).show();
	// }else{
	// Toast.makeText(getApplicationContext(),
	// "获取在逃人员信息失败", Toast.LENGTH_SHORT).show();
	// }
	// }
	//
	// break;
	//
	// default:
	// break;
	// }
	// };
	// };
	/********************************** 畅享7体检版 **********************************/
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
