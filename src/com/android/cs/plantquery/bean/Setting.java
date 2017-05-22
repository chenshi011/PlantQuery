package com.android.cs.plantquery.bean;

/**
 * 设置
 * @author CHENSHI
 *
 */
public class Setting {
	String title;
	String summary;
	String setting;
	String summary_checked;
	
	boolean useCheckbox;
	
	/**执行Action*/
	public static final int MODE_ACTION = 0;
	/**Activity*/
	public static final int MODE_ACTIVITY = 1;
	/**startActivityForResult*/
	public static final int MODE_ACTIVITY_FORRESULT = 2;
	private int mode = MODE_ACTION;
	private String action;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getSetting() {
		return setting;
	}
	public void setSetting(String setting) {
		this.setting = setting;
	}
	public String getSummary_checked() {
		return summary_checked;
	}
	public void setSummary_checked(String summary_checked) {
		this.summary_checked = summary_checked;
	}
	public boolean isUseCheckbox() {
		return useCheckbox;
	}
	public void setUseCheckbox(boolean useCheckbox) {
		this.useCheckbox = useCheckbox;
	}
	public int getMode() {
		return mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
}
