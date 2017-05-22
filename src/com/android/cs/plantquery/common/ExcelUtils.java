package com.android.cs.plantquery.common;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.cs.plantquery.Constant;
import com.android.cs.plantquery.bean.ExcelBean;
import com.android.cs.plantquery.bean.Plant;
import com.android.cs.plantquery.bean.XmlPaser;
import com.android.cs.plantquery.db.DBHelper;

public class ExcelUtils {
	Context context;
	PlantManager plantManager;
	ProgressBar progressbar_input;
	ProgressBar progressbar_wait;
	Handler inputcomplete_Handler;
	View startView; // 开始按钮
	/**
	 * 更新进度条
	 */
	final static int UPDATE_PROGRESS = 0x0;
	/**
	 * 更新进度条错误
	 */
	final static int UPDATE_ERROR = 0x1;

	/**
	 * Excel导入工具
	 * 
	 * @param context
	 * @param progressbar_input
	 *            进度条
	 * @param progressbar_wait
	 *            loading
	 * @param startView
	 *            开始按钮
	 * @param inputcomplete_Handler 导入完成齁的事件
	 */
	public ExcelUtils(Context context, ProgressBar progressbar_input,
			ProgressBar progressbar_wait, View startView,
			Handler inputcomplete_Handler) {
		this.context = context;
		this.progressbar_input = progressbar_input;
		this.progressbar_wait = progressbar_wait;
		this.startView = startView;
		this.inputcomplete_Handler = inputcomplete_Handler;
		DBHelper dbHelper = DBHelper.getInstance(context);
		plantManager = new PlantManager(context, dbHelper);
	}

	public void execute() {
		progressbar_wait.setVisibility(View.VISIBLE);
		startView.setVisibility(View.GONE);
		final ExcelBean excelBean = XmlPaser.getExcelBeanFromXml(context);
		excelBean.getAlias_column();
		
		final String excelPath = SettingUtils.getString(context,Constant.EXCEL_PATH,
				Constant.EXCEL_PATH_DEFAULT);
		final String picPath = SettingUtils.getString(context, Constant.PIC_PATH,
				Constant.PIC_PATH_DEFAULT);
		new Thread() {

			@Override
			public void run() {
				readExcel(excelPath, picPath, excelBean);
			}

		}.start();

	}

	private final Handler inputHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_PROGRESS:
				int index = (Integer) msg.obj;
				progressbar_input.setProgress(index);
				if (index > 99) {
					Toast.makeText(context, "导入完成！", Toast.LENGTH_SHORT).show();
					progressbar_wait.setVisibility(View.GONE);
					startView.setVisibility(View.VISIBLE);

					Message message = new Message();
					message.what = 1;
					inputcomplete_Handler.sendMessage(message);
				}
				break;
			case UPDATE_ERROR:
				Toast.makeText(context, "导入Exce过程中出错，请检查Excel",
						Toast.LENGTH_SHORT).show();
				progressbar_wait.setVisibility(View.GONE);
				startView.setVisibility(View.VISIBLE);
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
			File excelDir = new File(excelPath);
			if (!excelDir.exists()) {
				Toast.makeText(context, "请确保Excel路径正确", Toast.LENGTH_LONG)
						.show();
				return;
			}
			File picDir = new File(picPath);
			if (!picDir.exists()) {
				Toast.makeText(context, "请确保图片存放文件夹路径正确", Toast.LENGTH_LONG)
						.show();
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
			for (int i = beginRow; i < rows; i++) {

				int index = (int) ((i + 1) * 100) / rows;
				Message message = new Message();
				message.what = UPDATE_PROGRESS;
				message.obj = index;
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
				} else {
					plantManager.updatePlant(plant);
				}
			}
			book.close();

		} catch (Exception e) {
			Toast.makeText(context, "Excel格式有误！", Toast.LENGTH_SHORT).show();
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
