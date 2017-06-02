package com.example.mobilepolicedevice;

import java.util.ArrayList;
import java.util.List;

import zuo.biao.library.base.BaseActivity;
import zuo.biao.library.interfaces.OnBottomDragListener;

import com.example.bean.NormalList_Result;
import com.example.db.DatabaseUtil;
import com.example.http.HttpOperation;
import com.example.json.JsonUtil;
import com.example.model.Normal;
import com.example.service.impl.OneMoreFunctionImpl;
import com.example.view.CustomListView;
import com.example.view.CustomListView.OnLoadMoreListener;
import com.example.view.CustomListView.OnRefreshListener;
import com.example.view.ItemForNormalView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author 陆如一
 * @time 2017-5-25
 */
public class InformationRecordActivity extends BaseActivity implements OnBottomDragListener {

	private Button btnUploadNormal;
	private DatabaseUtil mDBUtil;
	private OneMoreFunctionImpl oneMoreFunctionImpl = new OneMoreFunctionImpl();
	private HttpOperation httpOperation = new HttpOperation();

	/* 保存需要显示到列表的核查记录信息 */
	private ArrayList<ItemForNormalView> itemLists = new ArrayList<ItemForNormalView>();

	/* 自定义Adapte和自定义Listview */
	private CustomListAdapter mAdapter;
	private CustomListView mListView;

	/* 记录当前读取信息列表中的位置 */
	private int mCount = 10;
	/* 记录当前列表中item数 */
	private int NormalListCount = 0;

	/* 数据载入进度提示框 */
	ProgressDialog progress;

	private static final int LOAD_DATA_FINISH = 1;

	private static final int REFRESF_DATA_FINISH = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information_record,this);
		mDBUtil = new DatabaseUtil(InformationRecordActivity.this);
		btnUploadNormal = (Button) findViewById(R.id.btnUploadNormal);

		BuildAppData();

		InitView();

		btnUploadNormal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!oneMoreFunctionImpl.NetWorkStatus(getApplicationContext())) {
					Toast.makeText(getApplicationContext(), "网络未连接",
							Toast.LENGTH_SHORT).show();
					return;
				}
				new Thread(new Runnable() {
					@Override
					public void run() {
						new Thread(networkTask).start();
						LoadData(REFRESF_DATA_FINISH);/*上传数据后刷新列表*/
					}
				}).start();
			}
		});
	}

	/**
	 * 处理弹出消息
	 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				// Toast.makeText(getApplicationContext(), msg.obj.toString(),
				// Toast.LENGTH_LONG).show();
				NormalList_Result obj = (NormalList_Result) msg.obj;
				ArrayList<Normal> norList = obj.normalList;
				String result = obj.result.toString();
				if (result == "true" || result.equals("true")) {
					Toast.makeText(getApplicationContext(), "提交成功",
							Toast.LENGTH_LONG).show();
					for (int i = 0; i < norList.size(); i++) {
						Normal normal = norList.get(i);
						mDBUtil.Update_Normal(normal.getId());
					}
				} else {
					if (result.equals("timeout") || result == "timeout") {
						Toast.makeText(getApplicationContext(),
								"连接超时，请检查服务器是否正常运行", Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(getApplicationContext(), result,
								Toast.LENGTH_LONG).show();
					}
				}
				break;
			case REFRESF_DATA_FINISH:/* 刷新 */
				if (mAdapter != null) {
					mAdapter.mList = (ArrayList<ItemForNormalView>) msg.obj;
					mAdapter.notifyDataSetChanged();
				}
				mListView.onRefreshComplete();/* 下拉刷新完成 */
				progress.dismiss();
				break;
			case LOAD_DATA_FINISH:/* 加载更多 */
				if (mAdapter != null) {
					mAdapter.mList = (ArrayList<ItemForNormalView>) msg.obj;
					mAdapter.notifyDataSetChanged();
				}
				mListView.onLoadMoreComplete();/* 加载更多完成 */
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 访问服务器
	 */
	Runnable networkTask = new Runnable() {
		@Override
		public void run() {
			// 在这里进行 http request.网络请求相关操作
			ArrayList<Normal> norList = mDBUtil.queryAll_Normal_NotSubmit();
			JsonUtil jsonutil = new JsonUtil();
			String jsondata = jsonutil.toJSONString(norList);
			String result = httpOperation.getStringFromServer("addNormal.do",
					jsondata);
			NormalList_Result normalList_result = new NormalList_Result();
			normalList_result.normalList = norList;
			normalList_result.result = result;
			Message msg = new Message();
			msg.what = 0;
			msg.obj = normalList_result;
			handler.sendMessage(msg);
		}
	};

	/**
	 * 初始化
	 */
	private void InitView() {
		/* 初始化绑定数据 */
		mAdapter = new CustomListAdapter(this, itemLists);
		mListView = (CustomListView) findViewById(R.id.InfoRecordListView);
		mListView.setAdapter(mAdapter);

		/* 列表下拉刷新监听 */
		mListView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				LoadData(REFRESF_DATA_FINISH);
			}
		});

		/* 列表加载更多监听 */
		mListView.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				LoadData(LOAD_DATA_FINISH);
			}
		});

		/* 列表中每一个item点击事件的监听 */
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

			}
		});

		/* 更新数据进度框 */
		progress = new ProgressDialog(InformationRecordActivity.this);
		progress.setTitle("提示");
		progress.setMessage("列表刷新中，马上就好");
	}

	/**
	 * 载入Listview所需要的数据
	 */
	private void LoadData(int type) {
		ArrayList<Normal> norLists = mDBUtil.queryAll_Normal();
		NormalListCount = norLists.size();
		ArrayList<ItemForNormalView> ifnvList = null;
		Normal normal = new Normal();
		ItemForNormalView ifnv = new ItemForNormalView();
		switch (type) {
		case REFRESF_DATA_FINISH:
			mCount = 10;
			if (mCount < NormalListCount) {
				ifnvList = new ArrayList<ItemForNormalView>();
				for (int i = 0; i < mCount; i++) {
					normal = norLists.get(i);
					ifnv = setItemForNormalView(normal);
					ifnvList.add(ifnv);
				}
			} else {
				ifnvList = new ArrayList<ItemForNormalView>();
				for (int i = 0; i < NormalListCount; i++) {
					normal = norLists.get(i);
					ifnv = setItemForNormalView(normal);
					ifnvList.add(ifnv);
				}
			}
			break;

		default:
			break;
		}

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		/* 发送消息给主线程，通知其更改UI */
		if (type == REFRESF_DATA_FINISH) {
			Message msg = handler.obtainMessage(REFRESF_DATA_FINISH, ifnvList);
			handler.sendMessage(msg);
		} else if (type == LOAD_DATA_FINISH) {
			Message msg = handler.obtainMessage(LOAD_DATA_FINISH, ifnvList);
			handler.sendMessage(msg);
		}
	}

	/**
	 * 自定义一个数据适配器，将自定义的listview所需要的数据传递到listview里面
	 * 
	 */
	private class CustomListAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		public ArrayList<ItemForNormalView> mList;

		public CustomListAdapter(Context pContext,
				ArrayList<ItemForNormalView> pList) {
			mInflater = LayoutInflater.from(pContext);
			if (pList != null) {
				mList = pList;
			} else {
				mList = new ArrayList<ItemForNormalView>();
			}
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			System.out.println("点击了第" + position + "个");
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (getCount() == 0) {
				return null;
			}
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = mInflater
						.inflate(R.layout.list_item_normal, null);
				holder = new ViewHolder();
				holder.imagUploadIcon = (ImageView) convertView
						.findViewById(R.id.imgUpdateIcon);
				holder.tvPersonName = (TextView) convertView
						.findViewById(R.id.tvPersonName);
				holder.tvPersonId = (TextView) convertView
						.findViewById(R.id.tvPersonId);
				holder.tvAddressName = (TextView) convertView
						.findViewById(R.id.tvAddressName);
				holder.tvCommitTime = (TextView) convertView
						.findViewById(R.id.tvCommitTime);
				holder.tvUserName = (TextView) convertView
						.findViewById(R.id.tvUserName);
				holder.imagFpIcon = (ImageView) convertView
						.findViewById(R.id.imgFingerPrint);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			ItemForNormalView ifnv = mList.get(position);
			holder.imagUploadIcon.setImageBitmap(ifnv.getUploadIcon());
			holder.tvPersonName.setText(ifnv.getPersonName());
			holder.tvPersonId.setText(ifnv.getPersonId());
			holder.tvCommitTime.setText(ifnv.getCommitTime());
			holder.tvAddressName.setText(ifnv.getAddressName());
			holder.tvUserName.setText(ifnv.getUserName());
			return convertView;
		}

	}

	private static class ViewHolder {
		private ImageView imagUploadIcon;
		private TextView tvPersonId;
		private TextView tvPersonName;
		private TextView tvAddressName;
		private TextView tvCommitTime;
		private TextView tvUserName;
		private ImageView imagFpIcon;
	}

	/**
	 * 初始化绑定Listview数据
	 */
	private void BuildAppData() {
		ArrayList<Normal> norLists = mDBUtil.queryAll_Normal();
		NormalListCount = norLists.size();
		Normal normal = new Normal();
		ItemForNormalView ifnv = new ItemForNormalView();
		if (NormalListCount > 10) {
			for (int i = 0; i < 10; i++) {
				normal = norLists.get(i);
				/* 判断核录信息是否已经上传 */
				ifnv = setItemForNormalView(normal);
				itemLists.add(ifnv);
			}
		} else {
			for (int i = 0; i < NormalListCount; i++) {
				normal = norLists.get(i);
				ifnv = setItemForNormalView(normal);
				itemLists.add(ifnv);
			}
		}
	}

	private ItemForNormalView setItemForNormalView(Normal normal) {
		ItemForNormalView ifnv = new ItemForNormalView();
		if (normal.getInfosubmit() == 0) {
			ifnv.setUploadIcon(BitmapFactory.decodeResource(
					getResources(), R.drawable.noupload));
		} else {
			ifnv.setUploadIcon(BitmapFactory.decodeResource(
					getResources(), R.drawable.upload));
		}

		ifnv.setPersonName("姓名：" + normal.getPersonname());
		ifnv.setPersonId("身份证号：" + normal.getPersonid());
		ifnv.setUserName("核查警员：" + normal.getUserid());
		ifnv.setAddressName("核查地点：" + normal.getAddressname());
		ifnv.setCommitTime("核查时间：" + normal.getCommittime());

		/* 判断指纹是否采集 */
		if (normal.getPersonfp() == 0) {
			ifnv.setFingerprintIcon(BitmapFactory.decodeResource(
					getResources(), R.drawable.nofp));
		} else {
			ifnv.setFingerprintIcon(BitmapFactory.decodeResource(
					getResources(), R.drawable.fp));
		}
		return ifnv;
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
		finish();
	}

}
