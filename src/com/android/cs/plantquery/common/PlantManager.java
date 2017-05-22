package com.android.cs.plantquery.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.android.cs.plantquery.Constant;
import com.android.cs.plantquery.bean.Plant;
import com.android.cs.plantquery.db.DBColumns;
import com.android.cs.plantquery.db.DBHelper;

/**
 * 
 * @author CHENSHI 植物管理（DB类）
 * 
 */
public class PlantManager {

	/** 关键字 */
	public final static String KEY_WORD = "keys";
	/** 关键字（筛选区域） */
	public final static String KEY_WORD_AREA = "area";
	/** 关键字（城市） */
	public final static String KEY_WORD_CITY = "city";
	/** 关键字（种类） */
	public final static String KEY_WORD_SORT = "sort";

	Context context;
	DBHelper dbHelper;
	public final static String TABLE_NAME = "plant_list";
	
	

	/**
	 * 任务管理类
	 * 
	 * @param context
	 * @param dbHelper
	 */
	public PlantManager(Context context, DBHelper dbHelper) {
		this.context = context;
		this.dbHelper = dbHelper;
		openOrCreateTable();
	}

	public static final class Plant_T implements BaseColumns {
		public static final String ID = "id";
		public static final String NAME = "name";
		public static final String ALIAS = "alias";
		public static final String LATINNAME = "latinName";
		/** 所属 */
		public static final String BELONGS = "belongs";
		/** 科目 */
		public static final String BRANCH = "branch";
		public static final String CITY = "city";
		public static final String DETAILS = "details";
		public static final String PICS = "pics";
	}

	private void openOrCreateTable() {
		if (!dbHelper.isTableExist(TABLE_NAME)) {
			DBColumns columns = new DBColumns();
			columns.addToColumns(Plant_T.ID, DBColumns.NUMERIC_TYPE);
			columns.addToColumns(Plant_T.NAME, DBColumns.TEXT_TYPE);
			columns.addToColumns(Plant_T.ALIAS, DBColumns.TEXT_TYPE);
			columns.addToColumns(Plant_T.LATINNAME, DBColumns.TEXT_TYPE);
			columns.addToColumns(Plant_T.BELONGS, DBColumns.TEXT_TYPE);
			columns.addToColumns(Plant_T.BRANCH, DBColumns.TEXT_TYPE);
			columns.addToColumns(Plant_T.CITY, DBColumns.TEXT_TYPE);
			columns.addToColumns(Plant_T.DETAILS, DBColumns.TEXT_TYPE);
			columns.addToColumns(Plant_T.PICS, DBColumns.TEXT_TYPE);
			dbHelper.createSimpleTable(TABLE_NAME, columns);
		}
	}

	/**
	 * 判断该条记录是否存在
	 * 
	 * @param time
	 * @return
	 */
	public boolean isPlantExist(int id) {
		boolean bs = false;
		try {
			String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " where "
					+ Plant_T.ID + "='" + id + "'";
			Cursor cursor = dbHelper.getCursor(sql);
			if (cursor.moveToNext()) {
				int c = cursor.getInt(0);
				if (c > 0)
					bs = true;
			}
			cursor.close();
		} catch (Exception exception) {
			Log.e("", exception.getLocalizedMessage());
		}
		return bs;
	}

	// /***
	// * 获取全部列表
	// *
	// * @return
	// */
	// public List<Plant> getPlantList() {
	// return doQuery(null, null);
	// }

	
	/**
	 * 获取记录条数
	 * @param whereClause
	 * @return
	 */
	public int getTableCount(String whereClause) {
		int result = 0;
		try {
			String sql = "SELECT COUNT(*) FROM " + TABLE_NAME;
			if(whereClause!=null)
				sql += whereClause;
			Cursor cursor = dbHelper.getCursor(sql);
			if (cursor.moveToNext()) {
				result = cursor.getInt(0);
			}
			cursor.close();
		} catch (Exception exception) {
			Log.e("", exception.getLocalizedMessage());
		}
		return result;
	}
	
	/**
	 * 查询植物
	 * 
	 * @param whereClause
	 *            条件
	 * @param currentPage
	 *            当前页
	 * @param pageSize
	 *            每页显示的记录
	 * @return Map<Integer, List<Plant>>
	 */
	public SparseArray<Object> doQuery(String whereClause, int currentPage, int pageSize) {
		SparseArray<Object> result = new SparseArray<Object>();
		List<Plant> plants = new ArrayList<Plant>();
		int form = (currentPage - 1) * pageSize;
		//int to = currentPage * pageSize;
		int to =  pageSize;
		String sql_extra = getWhereClause(whereClause);

		String sql = "select " + Plant_T.ID + "," + Plant_T.NAME + ","
				+ Plant_T.ALIAS + "," + Plant_T.LATINNAME + ","
				+ Plant_T.BELONGS + "," + Plant_T.BRANCH + "," + Plant_T.CITY
				+ "," + Plant_T.DETAILS + "," + Plant_T.PICS + " from "
				+ TABLE_NAME;

		sql += sql_extra;
		
		int count = getTableCount(sql_extra);
		result.put(Constant.RESULT_COUNT, count);
		
		sql += " order by " + Plant_T.ID + " asc";
		
		
		sql += " limit " + form + "," + to;
		try {
			Cursor cursor = dbHelper.getCursor(sql);
			while (cursor.moveToNext()) {

				int mid = cursor.getInt(0);
				String name = cursor.getString(1);
				String alias = cursor.getString(2);
				String latinName = cursor.getString(3);
				String belongs = cursor.getString(4);
				String branch = cursor.getString(5);
				String city = cursor.getString(6);
				String details = cursor.getString(7);
				String pics = cursor.getString(8);
				Plant plant = new Plant(mid, name, alias, latinName, belongs,
						branch, city, details, pics);
				plants.add(plant);
			}
			result.put(Constant.RESULT_LIST, plants);
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * 获取检索条件
	 * 
	 * @param whereArgs
	 * @return
	 */
	public String getWhereClause(String whereArgs) {
		String result = "";
		if (whereArgs == null)
			return "";
		try {
			String[] str = whereArgs.split("&");
			int len = str.length;
			String keyWorld = null; // 关键字
			Map<String, String> keyFileds = new HashMap<String, String>();

			for (int i = 0; i < len; i++) {
				String filed = str[i];
				String[] fileds = filed.split("=");
				if (fileds.length != 2)
					continue;
				if (fileds[0].equals(KEY_WORD)) {
					keyWorld = fileds[1];
				} else if (fileds[0].equals(KEY_WORD_AREA)) {
					if (fileds[1].contains("名称")) {
						keyFileds.put(Plant_T.NAME, "");
					} else if (fileds[1].contains("别名")) {
						keyFileds.put(Plant_T.ALIAS, "");
					} else if (fileds[1].contains("拉丁名")) {
						keyFileds.put(Plant_T.LATINNAME, "");
					} else if (fileds[1].contains("科名")) {
						keyFileds.put(Plant_T.BELONGS, "");
					}
				} else if (fileds[0].equals(KEY_WORD_CITY)) {
					keyFileds.put(Plant_T.CITY, fileds[1]);

				} else if (fileds[0].equals(KEY_WORD_SORT)) {
					keyFileds.put(Plant_T.BELONGS, fileds[1]);
				}
			}

			List<String> whereCause = new ArrayList<String>();
			boolean isAdd = false;
			boolean hasKeyWord = false;
			Iterator iterator = keyFileds.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry pairs = (Map.Entry) iterator.next();
				String sfied = (String) pairs.getKey();
				String svalue = (String) pairs.getValue();

				if (sfied.matches(Plant_T.CITY)
						|| sfied.matches(Plant_T.BELONGS)) {
					whereCause.add(sfied + " like '%" + svalue + "%'");
					isAdd = true;
				} else {
					if (keyWorld != null && !keyWorld.trim().equals("")) {
						whereCause.add(sfied + " like '%" + keyWorld + "%'");
						isAdd = true;
						hasKeyWord = true;
					}
				}
			}
			if (!isAdd && !hasKeyWord) {
				if (keyWorld != null && !keyWorld.trim().equals(""))
					whereCause.add(Plant_T.NAME + " like '%" + keyWorld + "%'"
							+ " or " + Plant_T.ALIAS + " like '%" + keyWorld
							+ "%'" + " or " + Plant_T.LATINNAME + " like '%"
							+ keyWorld + "%'" + " or " + Plant_T.BELONGS
							+ " like '%" + keyWorld + "%'" + " or "
							+ Plant_T.BRANCH + " like '%" + keyWorld + "%'"
							+ " or " + Plant_T.CITY + " like '%" + keyWorld
							+ "%'");
			} else if (isAdd) {
				if (!hasKeyWord) {
					if (keyWorld != null && !keyWorld.trim().equals("")) {
						whereCause.add("(" + Plant_T.NAME + " like '%"
								+ keyWorld + "%'" + " or " + Plant_T.ALIAS
								+ " like '%" + keyWorld + "%'" + " or "
								+ Plant_T.LATINNAME + " like '%" + keyWorld
								+ "%'" + " or " + Plant_T.BELONGS + " like '%"
								+ keyWorld + "%'" + " or " + Plant_T.BRANCH
								+ " like '%" + keyWorld + "%'" + " or "
								+ Plant_T.CITY + " like '%" + keyWorld + "%'"
								+ ")");
					}
				}
			}

			for (int j = 0; j < whereCause.size(); j++) {
				if (j == 0) {
					result += " where " + whereCause.get(j);
				} else {
					result += " and " + whereCause.get(j);
				}
			}

		} catch (Exception e) {
			Log.e("PalntManager", e.getMessage());
		}
		return result;
	}

	/***
	 * 添加植物
	 * 
	 * @param Plant
	 * @return boolean
	 */
	public boolean addPlant(Plant plant) {
		boolean result = false;
		try {
			int id = plant.getId();
			if (id == 0) {
				id = getMaxId() + 1;
				if (id <= 0)
					return false;
			}
			ContentValues values = new ContentValues();
			values.put(Plant_T.ID, id);
			if (plant.getName() != null)
				values.put(Plant_T.NAME, plant.getName());
			if (plant.getAlias() != null)
				values.put(Plant_T.ALIAS, plant.getAlias());
			if (plant.getLatinName() != null)
				values.put(Plant_T.LATINNAME, plant.getLatinName());
			if (plant.getBelongs() != null)
				values.put(Plant_T.BELONGS, plant.getBelongs());
			if (plant.getBranch() != null)
				values.put(Plant_T.BRANCH, plant.getBranch());
			if (plant.getCity() != null)
				values.put(Plant_T.CITY, plant.getCity());
			if (plant.getDetails() != null)
				values.put(Plant_T.DETAILS, plant.getDetails());
			if (plant.getPics() != null)
				values.put(Plant_T.PICS, plant.getPics());
			dbHelper.insert(TABLE_NAME, null, values);
			result = true;
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * 获取最大的ID值
	 * 
	 * @return
	 */
	public int getMaxId() {
		int result = 0;
		try {
			String sql = "SELECT max(" + Plant_T.ID + ") FROM " + TABLE_NAME;
			Cursor cursor = dbHelper.getCursor(sql);
			if (cursor.moveToNext()) {
				int c = cursor.getInt(0);
				result = c;
			}
			cursor.close();
		} catch (Exception exception) {
			Log.e("", exception.getLocalizedMessage());
		}
		return result;
	}

	/**
	 * 更新植物
	 * 
	 * @param plant
	 * @return
	 */
	public boolean updatePlant(Plant plant) {
		boolean result = false;
		try {
			int id = plant.getId();
			ContentValues values = new ContentValues();
			if (plant.getName() != null)
				values.put(Plant_T.NAME, plant.getName());
			if (plant.getAlias() != null)
				values.put(Plant_T.ALIAS, plant.getAlias());
			if (plant.getLatinName() != null)
				values.put(Plant_T.LATINNAME, plant.getLatinName());
			if (plant.getBelongs() != null)
				values.put(Plant_T.BELONGS, plant.getBelongs());
			if (plant.getBranch() != null)
				values.put(Plant_T.BRANCH, plant.getBranch());
			if (plant.getCity() != null)
				values.put(Plant_T.CITY, plant.getCity());
			if (plant.getDetails() != null)
				values.put(Plant_T.DETAILS, plant.getDetails());
			if (plant.getPics() != null)
				values.put(Plant_T.PICS, plant.getPics());
			dbHelper.update(TABLE_NAME, values, Plant_T.ID + "=?",
					new String[] { String.valueOf(id) });
			result = true;
		} catch (Exception e) {
		}
		return result;
	}

	/***
	 * 删除植物
	 * 
	 * @param Plant
	 * @return boolean
	 */
	public void delPlant(Plant plant) {
		try {
			dbHelper.delete(TABLE_NAME, Plant_T.ID + "=?",
					new String[] { String.valueOf(plant.getId()) });
		} catch (Exception e) {
		}
	}

	public boolean dropTable() {
		boolean bs = false;
		try {
			String sql = "drop table " + TABLE_NAME;
			Cursor cursor = dbHelper.getCursor(sql);
			cursor.close();
			bs = true;
		} catch (Exception exception) {
			Log.e("", exception.getLocalizedMessage());
		}
		return bs;
	}
}
