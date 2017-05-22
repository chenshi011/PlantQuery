package com.android.cs.plantquery.db;

import java.util.ArrayList;
import java.util.List;

public  class DBColumns {
	
	public static final String TEXT_TYPE = "TEXT";
	public static final String INTEGER_TYPE = "INTEGER";
	public static final String NUMERIC_TYPE = "NUMERIC";
	public static final String BLOB_TYPE = "BLOB";
	public static final String INTEGER_PRIMARY_KEY_TYPE = "INTEGER PRIMARY KEY";
	
	private List<DBColumn> columnList= new ArrayList<DBColumns.DBColumn>();
	
	/**
	 * 添加至列表中
	 * @param columnName
	 * @param columnType
	 */
	public void addToColumns(String columnName, String columnType)
	{
		DBColumn column = new DBColumn(columnName, columnType);
		columnList.add(column);
	}
	
	/**
	 * 转化语句
	 * @return
	 */
	public String columnsToString()
	{
		String sql="";
		for (DBColumn column : columnList) {
			sql += column.getColName()+" "+column.getColType().toUpperCase()+",";
		}
		sql = sql.substring(0,sql.length()-1);
		return sql;
	}
	
	class DBColumn
	{
		private String colName;
		private String colType;
		
		DBColumn(String columnName, String columnType)
		{
			colName = columnName;
			colType = columnType;
		}

		/**
		 * @return the colName
		 */
		public String getColName() {
			return colName;
		}

		/**
		 * @param colName the colName to set
		 */
		public void setColName(String colName) {
			this.colName = colName;
		}

		/**
		 * @return the colType
		 */
		public String getColType() {
			return colType;
		}

		/**
		 * @param colType the colType to set
		 */
		public void setColType(String colType) {
			this.colType = colType;
		}
	}

}