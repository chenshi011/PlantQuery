package com.android.cs.plantquery.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.cs.plantquery.R;

/**
 * 长按出现删除按钮的图片空间
 * 
 * @author CHENSHI
 * 
 */
public class ImageDel extends LinearLayout {
	private int mImageBackground;
	public ImageView imageView;
	public View delView;
	private boolean useAttrs = false;
	private boolean useDel = true;
	
	public int getmImageBackground() {
		return mImageBackground;
	}
	public void setmImageBackground(int mImageBackground) {
		this.mImageBackground = mImageBackground;
	}
	public ImageDel(Context context) {
		this(context, null);
		
	}
	public ImageDel(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.image_del, this);
		try {
			 TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageDel);  
			 this.mImageBackground = a.getResourceId(R.styleable.ImageDel_ImagemBackground, mImageBackground);  
			 this.useDel = a.getBoolean(R.styleable.ImageDel_useDel, useDel);  
			 a.recycle();  
			 useAttrs = true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		initView();
	}
	
	private void initView(){
		imageView = (ImageView) findViewById(R.id.image);
		if(useAttrs)
			imageView.setImageResource(mImageBackground);
		delView = findViewById(R.id.item_del);
		delView.setVisibility(View.GONE);
		imageView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ImageDel.this.performClick();
			}
		});
		
		if(useDel){
			imageView.setOnLongClickListener(new View.OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View arg0) {
					if(delView.getVisibility()!=View.VISIBLE){
						delView.setVisibility(View.VISIBLE);
					}else {
						delView.setVisibility(View.GONE);
					}
					return true;
				}
			});
		}
	}

}
