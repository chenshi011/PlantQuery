package com.android.cs.plantquery.bean;

import java.io.Serializable;

/**
 * 植物类
 * 
 * @author CHENSHI
 * 
 */
public class Plant implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int id; // id
	String name; // 名字
	String alias;
	String latinName;
	String belongs;
	String branch;
	String city;
	String details;
	String pics; // 图片路径，多个用分号隔开

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getLatinName() {
		return latinName;
	}

	public void setLatinName(String latinName) {
		this.latinName = latinName;
	}

	public String getBelongs() {
		return belongs;
	}

	public void setBelongs(String belongs) {
		this.belongs = belongs;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getPics() {
		return pics;
	}

	public void setPics(String pics) {
		this.pics = pics;
	}

	public Plant() {

	}
	
	public Plant(int id, String name, String alias, String latinName,
			String belongs, String branch, String city, String details) {
		this(id, name, alias, latinName, belongs, branch, city, details, null);
	}
	
	public Plant(int id, String name, String alias, String latinName,
			String belongs, String branch, String city, String details,String pics) {
		this.id = id;
		this.name = name;
		this.alias = alias;
		this.latinName = latinName;
		this.belongs = belongs;
		this.branch = branch;
		this.city = city;
		this.details = details;
		this.pics = pics;
	}
}
