package com.android.cs.plantquery.bean;

import java.io.Serializable;

/***
 * 导入返回结果
 * 
 * @author CHENSHI
 * 
 */
public class InputResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 导入成功 */
	public final static int status_ok = 0;
	/** 导入失败 */
	public final static int status_err = 1;
	int status = status_ok;
	int total;
	int progress;
	int index;
	int add = 0;
	int update = 0;
	String msg;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getAdd() {
		return add;
	}

	public void setAdd(int add) {
		this.add = add;
	}

	public int getUpdate() {
		return update;
	}

	public void setUpdate(int update) {
		this.update = update;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 *  导入返回结果
	 */
	public InputResult() {

	}

	/**
	 * 导入返回结果
	 * @param total 总数
	 * @param index 当前index
	 * @param progress 进度
	 * @param add 增加数
	 * @param update 更新数
	 * @param status 状态
	 * @param msg 消息
	 */
	public InputResult(int total,int index, int progress, int add, int update,int status,String msg) {
		this.total = total;
		this.index = index;
		this.progress = progress;
		this.add = add;
		this.update = update;
		this.status = status;
		this.msg = msg;
	}
}
