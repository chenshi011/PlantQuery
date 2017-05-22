package com.android.cs.plantquery.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	public static final String DATABASE_NAME = "plant.db";
	private static final int DATABASE_VERSION = 2;
	
	private static DBHelper dbHelper = null;

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * 单例模式下获取数据库操作
	 * @param context
	 * @return
	 */
	public static DBHelper getInstance(Context context)
	{
		if(dbHelper==null)
		{
			synchronized (DBHelper.class) {
				dbHelper = new DBHelper(context);
			}
		}
		return dbHelper;
	}
	/**
	 * 执行自定义语句，非查询类，如CREATE TABLE, DELETE, INSERT等
	 * @param sql
	 */
	public void exeSQL(String sql)
	{
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.beginTransaction();
		db.execSQL(sql);
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
	}
	
	/**
	 * 返回Cursor
	 * @param sql
	 * @return
	 */
	public Cursor getCursor(String sql) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		return cursor;
	}

	/**
	 * 创建简单表
	 * @param tableName
	 * @param columns
	 * @return
	 */
	public boolean createSimpleTable(String tableName, DBColumns columns)
	{
		boolean exeResult = true;
		try 
		{
			final String sql = "Create table if not exists "+tableName+"("+columns.columnsToString()+");";
			exeSQL(sql);
		} catch (Exception e) {
			exeResult = false;
		}
		return exeResult;
	}

	/**
	 * 插入数据
	 * @param table
	 * @param nullColumnHack
	 * @param values
	 * @return
	 */
	public long insert(String table, String nullColumnHack, ContentValues values)
	{
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		long l= db.insert(table, nullColumnHack, values);
		db.close();
		return l;
	}
	
	/**
	 * 判断表是否存在
	 * @param table
	 * @return
	 */
	public boolean isTableExist(String table)
	{
		boolean bs=false;
		try
		{
			SQLiteDatabase db = getReadableDatabase();
			String sql = "SELECT COUNT(*) FROM sqlite_master where type='table' and name='"+table+"'";
			Cursor cursor = db.rawQuery(sql, null);
			if(cursor.moveToNext())
			{
				int c = cursor.getInt(0);
				if(c>0) bs = true;
			}
			cursor.close();
		}
		catch(Exception exception)
		{
			Log.e("", exception.getLocalizedMessage());
		}
		return bs;
	}
	
	/**
	 * 删除表
	 * @param String[] tables
	 */
	public void deleteTables(String[] tables)
	{
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		for(int i=tables.length-1; i>-1; i--)
		{
			db.execSQL("DROP TABLE IF EXISTS "+tables[i]+"");
		}
		db.close();
	}
	
	/**
	 * 删除表
	 * @param String table
	 */
	public void deleteTable(String table)
	{
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS "+table+"");
		db.close();
	}
	
	/**
	 * 删除指定表内容
	 * @param table
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	public int delete(String table, String whereClause, String[] whereArgs)
	{
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int i = db.delete(table, whereClause, whereArgs);
		db.close();
		return i;
	}
	
	/**
	 * 更新
	 * @param table
	 * @param values
	 * @param whereClause
	 * @param whereArgs
	 * @return 更新的行数
	 */
	public int update(String table, ContentValues values, String whereClause, String[] whereArgs)
	{
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int i = db.update(table, values, whereClause, whereArgs);
		db.close();
		return i;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public synchronized void close() {
		super.close();
	}

}
