package com.android.cs.plantquery.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.cs.plantquery.R;

/**
 * 搜索输入框
 * @author CHENSHI
 *
 */
public class SearchEditText extends LinearLayout {
	
	EditText edit_keyword;
	View btn_del;
	Context context;

	public SearchEditText(Context context) {
		this(context, null);
	}

	public SearchEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}
	
	public Editable getText(){
		return edit_keyword.getText();
	}
	
	public void setHint(CharSequence hint){
		edit_keyword.setHint(hint);
	}
	
	
	public void init(){
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.search_edit_text, this);
		
		edit_keyword = (EditText) findViewById(R.id.edit_keyword);
		
		btn_del =  findViewById(R.id.btn_deleteall);
		
		edit_keyword.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (null == edit_keyword.getText()
						|| "".equals(edit_keyword.getText().toString())) {
					btn_del.setVisibility(View.GONE);
				} else {
					btn_del.setVisibility(View.VISIBLE);
				}
			}
		});
		
		btn_del.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				edit_keyword.setText("");
				SearchEditText.this.setFocusable(true);
				edit_keyword.clearFocus();
				// 隐藏软键盘
				InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(edit_keyword.getWindowToken(), 0);
			}
		});
	}
}
