package com.android.cs.plantquery.bean;

/**
 * 读取Excel的模板
 * @author CHENSHI
 *
 */
public class ExcelBean {
	String sheetName; //sheet的名字
	int beginRow; //开始的行
	int id_column;  //id的列号
	int name_column; //名字的列号
	int alias_column;
	int latinName_column;
	int belongs_column;
	int branch_column;
	int city_column;
	int details_column;
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	public int getBeginRow() {
		return beginRow;
	}
	public void setBeginRow(int beginRow) {
		this.beginRow = beginRow;
	}
	public int getId_column() {
		return id_column;
	}
	public void setId_column(int id_column) {
		this.id_column = id_column;
	}
	public int getName_column() {
		return name_column;
	}
	public void setName_column(int name_column) {
		this.name_column = name_column;
	}
	public int getAlias_column() {
		return alias_column;
	}
	public void setAlias_column(int alias_column) {
		this.alias_column = alias_column;
	}
	public int getLatinName_column() {
		return latinName_column;
	}
	public void setLatinName_column(int latinName_column) {
		this.latinName_column = latinName_column;
	}
	public int getBelongs_column() {
		return belongs_column;
	}
	public void setBelongs_column(int belongs_column) {
		this.belongs_column = belongs_column;
	}
	public int getBranch_column() {
		return branch_column;
	}
	public void setBranch_column(int branch_column) {
		this.branch_column = branch_column;
	}
	public int getCity_column() {
		return city_column;
	}
	public void setCity_column(int city_column) {
		this.city_column = city_column;
	}
	public int getDetails_column() {
		return details_column;
	}
	public void setDetails_column(int details_column) {
		this.details_column = details_column;
	}
}
