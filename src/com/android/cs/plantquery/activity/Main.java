package com.android.cs.plantquery.activity;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cs.plantquery.Constant;
import com.android.cs.plantquery.R;
import com.android.cs.plantquery.adapter.PlantListAdapter;
import com.android.cs.plantquery.adapter.SettingListAdapter;
import com.android.cs.plantquery.bean.InputResult;
import com.android.cs.plantquery.bean.MainFooterWidget;
import com.android.cs.plantquery.bean.Plant;
import com.android.cs.plantquery.bean.Setting;
import com.android.cs.plantquery.bean.XmlPaser;
import com.android.cs.plantquery.common.FileUtils;
import com.android.cs.plantquery.common.PlantManager;
import com.android.cs.plantquery.common.Reflection;
import com.android.cs.plantquery.common.SettingUtils;
import com.android.cs.plantquery.common.UIHelper;
import com.android.cs.plantquery.db.DBHelper;
import com.android.cs.plantquery.widget.MainFooterBar;
import com.android.cs.plantquery.widget.PagingBar;
import com.android.cs.plantquery.widget.PullToRefreshListView;
import com.android.cs.plantquery.widget.ScrollLayout;
import com.android.cs.plantquery.widget.SearchCondition;

/**
 * 主界面
 * 
 * @author CHENSHI
 * 
 */
public class Main extends Activity implements OnClickListener {

	MainFooterBar m_MainFooterBar;
	ScrollLayout main_scrolllayout;

	PullToRefreshListView listView_search;
	ListView listView_setting;

	EditText editText_excel_path, editText_pic_path;
	ProgressBar progressbar_input, progressbar_wait;

	PlantListAdapter m_plantListAdapter;
	SettingListAdapter m_settingAdapter;

	SearchCondition searchCondition;

	/** 启动Activity获得返回值（编辑植物） */
	private final static int EDIT_PLANT = 0x1;

	/** 清除数据对话框 */
	private final static int DIALOG_CLEAR = 0x11;

	/** 等待对话框 */
	private final static int DIALOG_WAIT = 0x12;

	TextView main_head_title;
	ProgressBar main_head_progress;
	TextView main_head_progress_text;
	AlertDialog.Builder edit_builder, del_builder;

	int currentPage = 1;
	int pageSize = 10;

	PagingBar pagingBar;
	View frame_input_startinput;
	MyReceiver m_receiver;
	DBHelper dbHelper;
	PlantManager plantManager;
	String m_keyWord;

	/** 底部容器 */
	List<MainFooterWidget> m_mainFooterWidgets;

	PowerManager.WakeLock wakeLock;
	TextView txtView_progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initViews();
	}

	private void initViews() {
		txtView_progress = (TextView) findViewById(R.id.txt_progress);
		main_head_title = (TextView) findViewById(R.id.main_head_title);
		main_head_progress = (ProgressBar) findViewById(R.id.main_head_progress);
		main_head_progress_text = (TextView) findViewById(R.id.main_head_progress_text);
		pagingBar = (PagingBar) findViewById(R.id.pagingBar);
		pagingBar.setOnPageChangeListener(pageChangeListener);
		progressbar_input = (ProgressBar) findViewById(R.id.progressbar_input);
		progressbar_wait = (ProgressBar) findViewById(R.id.progressbar_wait);
		main_scrolllayout = (ScrollLayout) findViewById(R.id.main_scrolllayout);
		main_scrolllayout.SetOnViewChangeListener(scroll_onViewChangeListener);
		listView_search = (PullToRefreshListView) findViewById(R.id.listview);
		listView_search.setOnItemClickListener(search_listOnItemClickListener);
		listView_search
				.setOnItemLongClickListener(search_listOnItemlongClickListener);

		String excelPath = SettingUtils.getString(this, Constant.EXCEL_PATH,
				Constant.EXCEL_PATH_DEFAULT);
		String picPath = SettingUtils.getString(this, Constant.PIC_PATH,
				Constant.PIC_PATH_DEFAULT);

		editText_excel_path = (EditText) findViewById(R.id.txt_excel_path);
		editText_pic_path = (EditText) findViewById(R.id.txt_pic_path);

		editText_excel_path.setText(excelPath);
		editText_pic_path.setText(picPath);
		frame_input_startinput = findViewById(R.id.frame_input_startinput);
		frame_input_startinput.setOnClickListener(this);

		searchCondition = (SearchCondition) findViewById(R.id.searchCondition);
		searchCondition.setOnqueryListen(onQueryListen);

		// 设置
		listView_setting = (ListView) findViewById(R.id.listview_setting);
		List<Setting> sets = XmlPaser.getSettingListFromXml(this);
		m_settingAdapter = new SettingListAdapter(this, sets);
		m_settingAdapter
				.setOnCheckedChangeListener(setting_listOnCheckedChangeListener);
		listView_setting.setAdapter(m_settingAdapter);
		listView_setting
				.setOnItemClickListener(setting_listOnItemClickListener);
		initMainFooterBar();

		boolean isFirstRunApp = SettingUtils.getBoolean(this,
				Constant.FISRT_RUN, true);
		if (isFirstRunApp) {
			showDialog(DIALOG_WAIT);
			new Thread() {
				@Override
				public void run() {
					if (FileUtils.copyFileFromAssets(Main.this,
							Constant.DB_PATH, Constant.ASSETS_DB)
							&& FileUtils.copyFolderFromAssets(Main.this,
									Constant.ASSETS_PATH,
									Constant.INPUT_TARGER_PATH)) {

						Message message = new Message();
						message.what = 1;
						fisrt_input_Handler.sendMessage(message);
					}
				}
			}.start();
		} else {
			dbHelper = DBHelper.getInstance(this);
			plantManager = new PlantManager(this, dbHelper);
			backgroudTask bTask = new backgroudTask();
			bTask.execute("");
		}

	}

	// 初始化底部停靠栏
	private void initMainFooterBar() {

		m_mainFooterWidgets = XmlPaser.getBaseWidgetsListFromXml(this);

		m_MainFooterBar = (MainFooterBar) findViewById(R.id.main_footerBar);
		m_MainFooterBar.setData(m_mainFooterWidgets);
		m_MainFooterBar.postInit();
		m_MainFooterBar
				.setOnItemClickLinstener(new MainFooterBar.OnItemClickLinstener() {
					@Override
					public void OnClick(Button btn, MainFooterWidget business) {
						int index = business.getIndex();
						main_head_title.setText(business.getName());
						main_scrolllayout.snapToScreen(index);
					}
				});
		if (m_MainFooterBar.getCount() > 0) {
			m_MainFooterBar.setSelectItem(0);
			main_head_title.setText(m_mainFooterWidgets.get(0).getName());
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.frame_input_startinput:
			SettingUtils.putString(Main.this, Constant.EXCEL_PATH,
					editText_excel_path.getText().toString());
			SettingUtils.putString(Main.this, Constant.PIC_PATH,
					editText_pic_path.getText().toString());
			UIHelper.hideSoftInput(this, v.getWindowToken());
			// ExcelUtils excelUtils = new ExcelUtils(Main.this,
			// progressbar_input, progressbar_wait, v,
			// inputcomplete_Handler);
			// excelUtils.execute();
			progressbar_wait.setVisibility(View.VISIBLE);
			frame_input_startinput.setVisibility(View.GONE);
			m_receiver = new MyReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction(Constant.ACTION_NAME);
			this.registerReceiver(m_receiver, filter);
			acquireWakeLock();
			startService(new Intent(this, InputService.class));
			break;

		default:
			break;
		}
	}

	public class MyReceiver extends BroadcastReceiver {
		// 自定义一个广播接收器
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			System.out.println("OnReceiver");
			Bundle bundle = intent.getExtras();
			
			if (bundle.containsKey("result")) {
				txtView_progress.setVisibility(View.VISIBLE);
				InputResult result = (InputResult) bundle.getSerializable("result");
				if (result.getStatus() == InputResult.status_err) {
					Toast.makeText(context, result.getMsg(), Toast.LENGTH_SHORT).show();
					progressbar_wait.setVisibility(View.GONE);
					frame_input_startinput.setVisibility(View.VISIBLE);
				}else {
					int progress = result.getProgress();
					txtView_progress.setText(String.format(getString(R.string.input_progress),result.getIndex(), result.getTotal()));
					progressbar_input.setProgress(progress);
					if (progress > 99) {
						Toast.makeText(context, getString(R.string.input_complete),
								Toast.LENGTH_SHORT).show();
						progressbar_wait.setVisibility(View.GONE);
						frame_input_startinput.setVisibility(View.VISIBLE);
						Message message = new Message();
						message.what = 1;
						inputcomplete_Handler.sendMessage(message);
					}
				}
			}
		}
	}

	private final ScrollLayout.OnViewChangeListener scroll_onViewChangeListener = new ScrollLayout.OnViewChangeListener() {

		@Override
		public void OnViewChange(int view) {
			m_MainFooterBar.setSelectItem(view);
			main_head_title.setText(m_mainFooterWidgets.get(view).getName());
		}
	};

	private final PagingBar.OnPageChangeListener pageChangeListener = new PagingBar.OnPageChangeListener() {

		@Override
		public void onchange(int page) {
			backgroudTask bTask = new backgroudTask();
			currentPage = page;
			bTask.execute("");
		}
	};

	/** 搜索列表ItemClick */
	private final AdapterView.OnItemClickListener search_listOnItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
			// TODO Auto-generated method stub
			try {
				Plant plant = (Plant) m_plantListAdapter.getItem(pos - 1); // 因为包含下列刷新的头部
				Intent intent = new Intent(Main.this, PlantDetail.class);
				intent.putExtra("data", plant);
				startActivity(intent);
			} catch (Exception e) {
			}

		}
	};

	/** 搜索列表OnItemLongClickListener(常按编辑) */
	private final AdapterView.OnItemLongClickListener search_listOnItemlongClickListener = new AdapterView.OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View v, int pos,
				long id) {
			try {
				final Plant plant = (Plant) m_plantListAdapter.getItem(pos - 1);
				edit_builder = new AlertDialog.Builder(Main.this);
				if (plant.getName() != null)
					edit_builder.setTitle(plant.getName());
				// 添加按钮，android.content.DialogInterface.OnClickListener.OnClickListener
				edit_builder.setItems(R.array.list_item_long_click_alert,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:
									Intent intent = new Intent(Main.this,
											PlantAdd.class);
									intent.putExtra("data", plant);
									startActivityForResult(intent, EDIT_PLANT);
									break;
								case 1:
									del_builder = new AlertDialog.Builder(
											Main.this);
									del_builder
											.setTitle(getString(R.string.list_item_alert_del_title));
									del_builder.setMessage(String
											.format(getString(R.string.list_item_alert_del_msg),
													plant.getName() != null ? plant
															.getName() : ""));
									del_builder
											.setPositiveButton(
													"确定",
													new DialogInterface.OnClickListener() {
														public void onClick(
																DialogInterface arg0,
																int arg1) {
															plantManager
																	.delPlant(plant);
															backgroudTask bTask = new backgroudTask();
															bTask.execute("");
														}
													});
									del_builder
											.setNegativeButton(
													"取消",
													new DialogInterface.OnClickListener() {
														public void onClick(
																DialogInterface arg0,
																int arg1) {
														}
													});
									del_builder.create().show();
									break;

								default:
									break;
								}
							}
						});
				edit_builder.create().show();
				return true;
			} catch (Exception e) {
			}
			return false;
		}
	};

	/** 设置列表Switch改变事件 */
	private final SettingListAdapter.OnCheckedChangeListener setting_listOnCheckedChangeListener = new SettingListAdapter.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				Setting setting, boolean isChecked) {
			String action = setting.getAction();
			Reflection.callAction(Main.this, action, isChecked);
		}
	};

	/** 设置列表ItemClick */
	private final AdapterView.OnItemClickListener setting_listOnItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
			// TODO Auto-generated method stub
			Setting setting = (Setting) m_settingAdapter.getItem(pos);

			int mode = setting.getMode();
			String action = setting.getAction();
			if (action == null)
				return;
			switch (mode) {
			case Setting.MODE_ACTION:
				Reflection.callAction(Main.this, action);
				break;
			case Setting.MODE_ACTIVITY:
				try {
					Class<?> clazz = Class.forName(action);
					Intent intent = new Intent(Main.this, clazz);
					startActivity(intent);
				} catch (Exception e) {
					e.getMessage();
				}
				break;
			case Setting.MODE_ACTIVITY_FORRESULT:
				try {
					Class<?> clazz = Class.forName(action);
					Intent intent = new Intent(Main.this, clazz);
					startActivityForResult(intent, EDIT_PLANT);
				} catch (Exception e) {
					e.getMessage();
				}
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == EDIT_PLANT) {
				try {
					Plant plant = (Plant) data.getSerializableExtra("data");
					if (!plantManager.isPlantExist(plant.getId()))
						plantManager.addPlant(plant);
					else {
						plantManager.updatePlant(plant);
					}
					backgroudTask bTask = new backgroudTask();
					bTask.execute("");
				} catch (Exception e) {
				}
			}
		} else if (resultCode == RESULT_CANCELED) {
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_WAIT:
			ProgressDialog dialog = new ProgressDialog(this);
			String msg = getString(R.string.main_head_inputing);
			dialog.setMessage(msg);
			return dialog;
		case DIALOG_CLEAR:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("提示");
			builder.setMessage("确认要清除当前数据");
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
							dbHelper.deleteTable(PlantManager.TABLE_NAME);
							currentPage = 1;
							SettingUtils.putBoolean(Main.this,
									Constant.FISRT_RUN, false);
							backgroudTask bTask = new backgroudTask();
							bTask.execute("");
						}
					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
							dismissDialog(DIALOG_CLEAR);
						}
					});

			return builder.create();
		}

		return super.onCreateDialog(id);
	}

	// ----------------------
	// Action
	// --------------
	public void setScroll(boolean isChecked) {
		SettingUtils.putBoolean(this, Constant.IS_SCROLL, isChecked);
		main_scrolllayout.setIsScroll(isChecked);
	}

	public void clearData() {
		showDialog(DIALOG_CLEAR);
	}

	// ----------------------
	/**
	 * 执行检索
	 */
	private final SearchCondition.OnQueryListen onQueryListen = new SearchCondition.OnQueryListen() {

		@Override
		public void onClick(CharSequence keyWord) {
			m_keyWord = keyWord.toString();
			currentPage = 1;
			backgroudTask bTask = new backgroudTask();
			bTask.execute("");
		}
	};

	/***
	 * 首次从Assets导入数据完成
	 */
	@SuppressLint("HandlerLeak")
	private final Handler fisrt_input_Handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			dismissDialog(DIALOG_WAIT);
			switch (msg.what) {
			case 1:
				SettingUtils.putBoolean(Main.this, Constant.FISRT_RUN, false);
				dbHelper = DBHelper.getInstance(Main.this);
				plantManager = new PlantManager(Main.this, dbHelper);
				backgroudTask btTask = new backgroudTask();
				btTask.execute("");
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	/***
	 * 导入数据完成
	 */
	@SuppressLint("HandlerLeak")
	private final Handler inputcomplete_Handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				releaseWakeLock();
				stopService(new Intent(Main.this, InputService.class));
				Main.this.unregisterReceiver(m_receiver);
				currentPage = 1;
				backgroudTask btTask = new backgroudTask();
				btTask.execute("");
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	/***
	 * 更新ListView进程
	 * 
	 * @author CHENSHI
	 * 
	 */
	class backgroudTask extends AsyncTask<String, Integer, SparseArray<Object>> {

		@Override
		protected void onPreExecute() {
			main_head_progress.setVisibility(View.VISIBLE);
			main_head_progress_text.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		@Override
		protected SparseArray<Object> doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			SparseArray<Object> results = plantManager.doQuery(m_keyWord,
					currentPage, pageSize);

			return results;
		}

		@SuppressWarnings({ "unchecked" })
		@Override
		protected void onPostExecute(SparseArray<Object> results) {
			main_head_progress.setVisibility(View.GONE);
			main_head_progress_text.setVisibility(View.GONE);
			if (results != null
					&& results.indexOfKey(Constant.RESULT_COUNT) > -1
					&& results.indexOfKey(Constant.RESULT_LIST) > -1) {
				try {
					pagingBar.setCurrentPage(currentPage);
					List<Plant> plants = (List<Plant>) results
							.get(Constant.RESULT_LIST);
					pagingBar.setTotalcount((Integer) results
							.get(Constant.RESULT_COUNT));
					if (m_plantListAdapter == null) {
						m_plantListAdapter = new PlantListAdapter(Main.this,
								plants);
						listView_search.setAdapter(m_plantListAdapter);
					} else {
						m_plantListAdapter.setList(plants);
						m_plantListAdapter.notifyDataSetChanged();
					}
				} catch (Exception e) {
				}
			}
			super.onPostExecute(results);
		}

	}

	private long firstTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			long secondTime = System.currentTimeMillis();
			if (secondTime - firstTime > 2500) {
				Toast.makeText(Main.this, "再按一次退出程序...", Toast.LENGTH_SHORT)
						.show();
				firstTime = secondTime;// 更新firstTime
				return false;
			} else {
				return super.onKeyDown(keyCode, event);
			}
		} else
			return super.onKeyDown(keyCode, event);
	}

	@SuppressLint("Wakelock")
	private void acquireWakeLock() {
		if (wakeLock != null) {
			wakeLock.release();
		}
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
				getPackageName());

		if (null != wakeLock) {

			wakeLock.acquire();
			wakeLock.setReferenceCounted(false);
		}
	}

	private void releaseWakeLock() {
		if (null != wakeLock) {
			wakeLock.release();
			wakeLock = null;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		releaseWakeLock();
		stopService(new Intent(Main.this, InputService.class));
		if (m_receiver != null)
			Main.this.unregisterReceiver(m_receiver);
	}

}
