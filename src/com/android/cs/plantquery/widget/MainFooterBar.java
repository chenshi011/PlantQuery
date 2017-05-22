package com.android.cs.plantquery.widget;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.cs.plantquery.R;
import com.android.cs.plantquery.bean.MainFooterWidget;

/***
 * 自定义LinearLayout实现类似listview加载 数据源（Business）
 * 
 * @author chenshi
 * 
 */
public class MainFooterBar extends LinearLayout {

	private List<MainFooterWidget> data;
	private int selectItem = -1;
	OnItemClickLinstener onItemClickLinstener;
	private Context context;

	public MainFooterBar(Context context) {
		this(context, null);
	}

	public int getCount(){
		return data!=null?data.size():-1;
	}
	
	public MainFooterBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		this.setOrientation(LinearLayout.HORIZONTAL);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, 48);
		this.setBackgroundResource(R.drawable.frame_button_bg);
		this.setGravity(Gravity.CENTER_VERTICAL);
		this.setLayoutParams(params);
	}

	/**
	 * 初始化加载
	 */
	public void postInit() {
		if (data != null) {
			int count = data.size();
			for (int i = 0; i < count; i++) {
				MainFooterWidget widgets = data.get(i);
				Button btn = new Button(context);
				btn.setTextSize(16.0f);
				btn.setTextColor(context.getResources().getColor(
						R.color.frame_button_text_nor));
				btn.setBackgroundResource(R.drawable.frame_button_bg);

				Drawable drawableTop = null;
				try {
					// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
					drawableTop = context.getResources().getDrawable(
							widgets.getIcon());
					// drawableTop
					// =context.getResources().getDrawable(R.drawable.main_map_icon_route);
					drawableTop.setBounds(0, 0, drawableTop.getMinimumWidth(),
							drawableTop.getMinimumHeight());
				} catch (Exception e) {
				}
				btn.setTag(i);
				btn.setCompoundDrawables(null, drawableTop, null, null);
				btn.setText(widgets.getName());

				btn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						int index = (Integer) v.getTag();
						if (selectItem != index) {
							setSelectItem(index);
							if (onItemClickLinstener != null) {
								onItemClickLinstener.OnClick((Button) v,
										data.get(index));
							}
						}
					}
				});

				LayoutParams params = new LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT,
						1.0f);
				params.gravity = Gravity.CENTER_VERTICAL;
				this.addView(btn, params);

				ImageView image_cutline = new ImageView(context);
				image_cutline
						.setBackgroundResource(R.drawable.frame_button_cutline);

				LayoutParams params_cutline = new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
				if (i < count - 1)
					this.addView(image_cutline, params_cutline);
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

	public void setSelectItem(int selectItem) {
		this.selectItem = selectItem;
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			View view = getChildAt(i);
			if (view instanceof Button) {
				int index = (Integer) view.getTag();
				if (index != selectItem) {
					((Button) view).setTextColor(context.getResources()
							.getColor(R.color.frame_button_text_nor));
					getChildAt(i).setEnabled(true);
				} else {
					((Button) view).setTextColor(context.getResources()
							.getColor(R.color.frame_button_text_select));
					getChildAt(i).setEnabled(false);
				}
			}
		}
	}

	public int getSelectItem() {
		return selectItem;
	}

	public List<MainFooterWidget> getData() {
		return data;
	}

	public void setData(List<MainFooterWidget> data) {
		this.data = data;
	}

	public void setOnItemClickLinstener(
			OnItemClickLinstener onItemClickLinstener) {
		this.onItemClickLinstener = onItemClickLinstener;
	}

	/**
	 * 
	 * 自定义事件
	 * 
	 * */
	public interface OnItemClickLinstener {
		/**
		 * 点击事件
		 * 
		 * @param view
		 * @param Business
		 */
		void OnClick(Button btn, MainFooterWidget business);
	}

}
