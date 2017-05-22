package com.android.cs.plantquery.activity;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.android.cs.plantquery.Constant;
import com.android.cs.plantquery.R;
import com.android.cs.plantquery.bean.ExcelBean;
import com.android.cs.plantquery.bean.InputResult;
import com.android.cs.plantquery.bean.Plant;
import com.android.cs.plantquery.bean.XmlPaser;
import com.android.cs.plantquery.common.PlantManager;
import com.android.cs.plantquery.common.SettingUtils;
import com.android.cs.plantquery.db.DBHelper;

/***
 * 导入数据服务（使用广播发送更新UI）
 * 
 * @author CHENSHI
 * 
 */
public class InputService extends Service {
	PlantManager plantManager;
	/**
	 * 更新进度条
	 */
	final static int UPDATE_PROGRESS = 0x0;
	/**
	 * 更新进度条错误
	 */
	final static int UPDATE_ERROR = 0x1;

	InputResult m_inputResult;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		DBHelper dbHelper = DBHelper.getInstance(this);
		plantManager = new PlantManager(this, dbHelper);
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);

		final ExcelBean excelBean = XmlPaser
				.getExcelBeanFromXml(InputService.this);
		excelBean.getAlias_column();

		final String excelPath = SettingUtils.getString(this,
				Constant.EXCEL_PATH, Constant.EXCEL_PATH_DEFAULT);
		final String picPath = SettingUtils.getString(this, Constant.PIC_PATH,
				Constant.PIC_PATH_DEFAULT);
		new Thread() {

			@Override
			public void run() {
				readExcel(excelPath, picPath, excelBean);
			}

		}.start();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	@SuppressLint("HandlerLeak")
	private final Handler inputHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_PROGRESS:
			case UPDATE_ERROR:
				Intent intent = new Intent();
				intent.putExtra("result", m_inputResult);
				intent.setAction(Constant.ACTION_NAME);// action与接收器相同
				sendBroadcast(intent);
				break;

			default:
				break;
			}

			super.handleMessage(msg);
		}
	};

	/***
	 * 读取Excel
	 * 
	 * @param excelPath
	 * @param excelBean
	 */
	public void readExcel(String excelPath, String picPath, ExcelBean excelBean) {
		try {
			int add = 0;
			int update = 0;
			int total = 0;
			m_inputResult = new InputResult();
			m_inputResult.setStatus(InputResult.status_ok);
			File excelDir = new File(excelPath);
			if (!excelDir.exists()) {
				Message message = new Message();
				message.what = UPDATE_ERROR;
				message.obj = getString(R.string.input_excel_style_err);
				inputHandler.sendMessage(message);
				return;
			}
			File picDir = new File(picPath);
			if (!picDir.exists()) {
				Message message = new Message();
				message.what = UPDATE_ERROR;
				message.obj = getString(R.string.input_pic_path_err);
				inputHandler.sendMessage(message);
				return;
			}
			Workbook book = Workbook.getWorkbook(new File(excelPath));
			Sheet sheet = book.getSheet(excelBean.getSheetName());

			int rows = sheet.getRows(); // 行

			// int Cols = sheet.getColumns(); //列
			int Id_column = excelBean.getId_column();
			int Name_column = excelBean.getName_column();
			int Alias_column = excelBean.getAlias_column();
			int LatinName_column = excelBean.getLatinName_column();
			int Belongs_column = excelBean.getBelongs_column();
			int Branch_column = excelBean.getBranch_column();
			int City_column = excelBean.getCity_column();
			int Details_column = excelBean.getDetails_column();

			int beginRow = excelBean.getBeginRow();
			total = rows - beginRow;
			m_inputResult.setTotal(total);
			for (int i = beginRow; i < rows; i++) {
				m_inputResult.setIndex(i + 1 - beginRow);
				int index = (int) ((i + 1) * 100) / rows;
				m_inputResult.setProgress(index);
				Message message = new Message();
				message.what = UPDATE_PROGRESS;
				message.obj = m_inputResult;
				inputHandler.sendMessage(message);

				Cell[] cels = sheet.getRow(i);
				int size = cels.length;
				int id = -1;
				String name = null, alias = null, latinName = null, belongs = null, branch = null, city = null, details = null;
				if (Id_column < size)
					id = Integer.valueOf(cels[Id_column].getContents());
				if (Name_column < size)
					name = cels[Name_column].getContents();
				if (Alias_column < size)
					alias = cels[Alias_column].getContents();
				if (LatinName_column < size)
					latinName = cels[LatinName_column].getContents();
				if (Belongs_column < size)
					belongs = cels[Belongs_column].getContents();
				if (Branch_column < size)
					branch = cels[Branch_column].getContents();
				if (City_column < size)
					city = cels[City_column].getContents();
				if (Details_column < size)
					details = cels[Details_column].getContents();

				List<File> pic_files = getMatchFile(picDir, String.valueOf(id));
				String pics = null;
				if (pic_files != null) {
					int count = pic_files.size();
					if (count > 0) {
						pics = "";
						for (int j = 0; j < count; j++) {
							if (j < count - 1) {
								pics += pic_files.get(j).getPath() + ";";
							} else {
								pics += pic_files.get(j).getPath();
							}
						}
					}
				}
				Plant plant = new Plant(id, name, alias, latinName, belongs,
						branch, city, details, pics);
				if (!plantManager.isPlantExist(id)) {
					plantManager.addPlant(plant);
					add++;
					m_inputResult.setAdd(add);
				} else {
					plantManager.updatePlant(plant);
					update++;
					m_inputResult.setUpdate(update);
				}
			}
			book.close();

		} catch (Exception e) {
			Message message = new Message();
			message.what = UPDATE_ERROR;
			m_inputResult.setStatus(InputResult.status_err);
			m_inputResult.setMsg(getString(R.string.input_error));
			message.obj = m_inputResult;
			inputHandler.sendMessage(message);
		}
	}

	/**
	 * 寻找匹配的图片文件
	 * 
	 * @param dir
	 * @param matcher
	 * @return
	 */
	private List<File> getMatchFile(File dir, String matches) {
		List<File> resultFiles = null;
		final String matcher = matches;
		FileFilter filter = new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				String filename = pathname.getName().toLowerCase();
				int index = filename.indexOf("-");
				if (index > -1) {
					if (filename.startsWith(matcher + "-")) {
						return true;
					} else {
						return false;
					}
				} else {
					if (filename.matches(matcher + ".jpg")
							|| filename.matches(matcher + ".png")
							|| filename.matches(matcher + ".bmp")
							|| filename.matches(matcher + ".dib")
							|| filename.matches(matcher + ".gif")
							|| filename.matches(matcher + ".jfif")
							|| filename.matches(matcher + ".jpe")
							|| filename.matches(matcher + ".jpeg")
							|| filename.matches(matcher + ".tif")
							|| filename.matches(matcher + ".tiff")
							|| filename.matches(matcher + ".ico"))
						return true;
					else
						return false;
				}
			}
		};
		try {
			resultFiles = new ArrayList<File>();
			if (dir.isDirectory()) {
				File[] fs = dir.listFiles(filter);
				int len = fs.length;
				for (int i = 0; i < len; i++) {
					if (fs[i].isDirectory()) {
						List<File> files = getMatchFile(fs[i], matcher);
						resultFiles.addAll(files);
					} else if (fs[i].isFile()) {
						resultFiles.add(fs[i]);
					}
				}
			} else if (dir.isFile()) {
				File[] fs = dir.listFiles(filter);
				for (int i = 0; i < fs.length; i++) {
					resultFiles.add(fs[i]);
				}
			}
		} catch (Exception e) {
		}
		return resultFiles;
	}

}
