package com.android.cs.plantquery.common;

import java.io.File;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.os.IBinder;
import android.os.StatFs;
import android.view.inputmethod.InputMethodManager;

public class UIHelper {

	/***
	 * 判断SD卡是否存在
	 * @return
	 */
	public static boolean isExistSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else
			return false;
	}

	public static long getSDFreeSize() {
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 空闲的数据块的数量
		long freeBlocks = sf.getAvailableBlocks();
		// 返回SD卡空闲大小
		// return freeBlocks * blockSize; //单位Byte
		// return (freeBlocks * blockSize)/1024; //单位KB
		return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
	}

	/**
	 * 显示简单提示对话框
	 */
	public static void ShowSimpleDiaLog(final Context context, String msg) {
		final AlertDialog.Builder msg_dialog = new AlertDialog.Builder(context);
		msg_dialog.setTitle("提示");
		msg_dialog.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
					}
				});
		msg_dialog.setMessage(msg);
		msg_dialog.show();
	}

	/**
	 * 隐藏软键盘
	 */
	public static void hideSoftInput(Context context, IBinder windowToken) {
		// 隐藏软键盘
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(windowToken, 0);
	}

}
