package com.android.cs.plantquery.common;

import com.android.cs.plantquery.Constant;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 设置
 * @author CHENSHI
 *
 */
public class SettingUtils {
	
	
	public static String getString(Context context,  String key, String defValue) {
		return context.getSharedPreferences(Constant.APP_SETTING, 0).getString(key, defValue);
	}
	
	public static void putString(Context context,  String key, String value) {
		SharedPreferences.Editor editor = context.getSharedPreferences(Constant.APP_SETTING, 0).edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public static boolean getBoolean(Context context,  String key, boolean defValue) {
		return context.getSharedPreferences(Constant.APP_SETTING, 0).getBoolean(key, defValue);
	}
	
	public static void putBoolean(Context context,  String key, boolean value) {
		SharedPreferences.Editor editor = context.getSharedPreferences(Constant.APP_SETTING, 0).edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
}
