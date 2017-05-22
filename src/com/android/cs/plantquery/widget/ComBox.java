package com.android.cs.plantquery.widget;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.cs.plantquery.R;
import com.android.cs.plantquery.bean.ComBoxBean;

public class ComBox extends LinearLayout {

	private Context context;
	private PopupWindow selectPopupWindow = null;
	private ListView listView;
	private Button btnSpiner;
	private List<ComBoxBean> items = null;
	private float textSize = 18.0f;
	
	public float getTextSize() {
		return textSize;
	}

	public void setTextSize(float textSize) {
		this.textSize = textSize;
		btnSpiner.setTextSize(textSize);
	}

	public List<ComBoxBean> getItems() {
		return items;
	}

	public void setItems(List<ComBoxBean> items) {
		if (items != null && items.size() > 0) {
			this.items = items;
			refreshDatas();
			setSelectIndex(0);
		}
	}
	
	
	private int selectIndex = -1;

	public int getSelectIndex() {
		return selectIndex;
	}

	public Object getSelectItem() {
		return items.get(selectIndex);
	}
	public void setSelectIndex(int selectIndex) {
		this.selectIndex = selectIndex;
		String value = "";
		if(selectIndex>-1){
			items.get(selectIndex).getTitle();
		}
		btnSpiner.setText(value);
	}

	public ComBox(Context context) {
		this(context, null, null);
	}

	public ComBox(Context context, AttributeSet attrs) {
		this(context, attrs, null);
	}

	public ComBox(Context context,  List<ComBoxBean> items) {
		this(context, null, items);
	}
	
	public ComBox(Context context, AttributeSet attrs, List<ComBoxBean> items) {
		super(context, attrs);
		initViews(context);
		this.context = context;
		this.items = items;
	}

	private void refreshDatas() {
		ComBoxAdapter optionsAdapter = new ComBoxAdapter(context);
		listView.setAdapter(optionsAdapter);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		selectPopupWindow.setWidth(btnSpiner.getWidth() - 8);
	}

	private void initViews(Context context) {
		this.setGravity(Gravity.CENTER_VERTICAL);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		this.setLayoutParams(params);
		btnSpiner = new Button(context);
		btnSpiner.setTextSize(textSize);
		btnSpiner.setBackgroundResource(R.drawable.spinner);
		
		this.addView(btnSpiner);
		btnSpiner.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				selectPopupWindow.showAsDropDown(v, 4, -1);
			}
		});
		
		listView = new ListView(context);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long arg3) {
				setSelectIndex(pos);
				selectPopupWindow.dismiss();
			}
		});
		
		selectPopupWindow = new PopupWindow(listView,150,
				LayoutParams.WRAP_CONTENT, true);
		selectPopupWindow.setOutsideTouchable(true);
		selectPopupWindow.setBackgroundDrawable(new ColorDrawable(
				Color.TRANSPARENT));
	}
	
	public class ComBoxAdapter extends BaseAdapter {


		private Context context = null;

		public ComBoxAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			LinearLayout view = null;

			if (convertView == null) {
				view = new LinearLayout(context);
			} else {
				view = (LinearLayout) convertView;
				view.removeAllViews();
			}
			TextView textView = new TextView(context);
			
			textView.setText(items.get(position).getTitle());
			textView.setTextSize(18.0f);
			textView.setTextColor(context.getResources().getColor(R.color.black));
			view.addView(textView);
			if (position % 2 == 0) {
				view.setBackgroundResource(R.drawable.list_selector_childview_1);
			} else {
				view.setBackgroundResource(R.drawable.list_selector_childview_2);
			}
			return view;
		}

	}
	
	
}
