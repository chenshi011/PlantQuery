package com.android.cs.plantquery;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppContext extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}


	/**
	 * 是否左右滑动
	 * 
	 * @return
	 */
	public boolean isScroll() {
		return getProperty(Constant.CONF_SCROLL);
	}

	/**
	 * 设置是否左右滑动
	 * 
	 * @param b
	 */
	public void setConfigScroll(boolean b) {

		setProperty(Constant.CONF_SCROLL, b);
	}

	@SuppressLint("WorldReadableFiles")
	private Boolean getProperty(String name) {
		return getSharedPreferences(Constant.APP_SETTING,
				Context.MODE_WORLD_READABLE).getBoolean(name, false);
	}

	@SuppressLint("CommitPrefEdits")
	private void setProperty(String name, boolean b) {
		Editor editor = getSharedPreferences(this).edit();// 编辑管理
		editor.putBoolean(name, b);
	}

	@SuppressLint({ "WorldReadableFiles", "WorldWriteableFiles" })
	public SharedPreferences getSharedPreferences(Context context) {
		return context.getSharedPreferences("config",
				Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
	}

}
