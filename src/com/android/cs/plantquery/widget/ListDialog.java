package com.android.cs.plantquery.widget;

import com.android.cs.plantquery.R;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;

public class ListDialog extends Builder {

	public ListDialog(Context context) {
		super(context);
		init();
	}

	private void init() {
		this.setTitle("列表对话框");
		// 添加按钮，android.content.DialogInterface.OnClickListener.OnClickListener
		this.setItems(R.array.list_item_long_click_alert,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// String
						// hoddy=getResources().getStringArray(R.array.hobby)[which];
						// editText.setText("您选择了： "+hoddy);
					}
				});
	}
}
