package com.android.cs.plantquery.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.cs.plantquery.Constant;
import com.android.cs.plantquery.R;
import com.android.cs.plantquery.bean.MessageObj;
import com.android.cs.plantquery.bean.Plant;
import com.android.cs.plantquery.common.ImageUtils;

/***
 * 植物详情
 * 
 * @author CHENSHI
 * 
 */
public class PlantDetail extends Activity {

	TextView txt_plant_detail;
	ViewGroup plant_container;
	String picPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.plant_detail);
		initViews();
	}

	private void initViews() {
		txt_plant_detail = (TextView) findViewById(R.id.txt_plant_detail);
		plant_container = (ViewGroup) findViewById(R.id.plant_container);
		if (getIntent().hasExtra("data")) {
			try {
				Plant plant = (Plant) getIntent().getSerializableExtra("data");
				if(plant.getDetails()!=null)
					txt_plant_detail.setText(plant.getDetails().trim());
				picPath = plant.getPics();
				new Thread() {
					@Override
					public void run() {
						loadImage();
					}
				}.start();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}


	private void loadImage() {
		if (picPath == null)
			return;
		plant_container.removeAllViews();
		String[] pics = picPath.split(";");
		for (int i = 0; i < pics.length; i++) {
			Bitmap bitmap = ImageUtils.loadImgThumbnail(pics[i], Constant.IMGTHUMB_WIDTH, Constant.IMGTHUMB_HEIGHT);
			if (bitmap != null) {
				Message msg = new Message();
				msg.what = 1;
				msg.obj = new MessageObj(bitmap,pics[i]);
				handler.sendMessage(msg);
			}
		}
	}

	@SuppressLint("HandlerLeak")
	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1 && msg.obj != null) {
				// 显示图片
				ImageView imageView = new ImageView(PlantDetail.this);
				imageView.setImageBitmap(((MessageObj) msg.obj).getBitmap());
				imageView.setVisibility(View.VISIBLE);
				imageView.setTag(msg.obj);
				imageView.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(PlantDetail.this,
								ImageZoomDialog.class);
						intent.putExtra("img_url", ((MessageObj) v.getTag()).getImage_url());
						startActivity(intent);
					}
				});
				plant_container.addView(imageView);
			}
		}
	};
}
