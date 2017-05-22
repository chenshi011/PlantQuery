package com.android.cs.plantquery.bean;

import android.graphics.Bitmap;

public class MessageObj {
	Bitmap bitmap;
	String image_url;

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public MessageObj() {

	}

	public MessageObj(Bitmap bitmap, String image_url) {
		this.bitmap = bitmap;
		this.image_url = image_url;
	}
}
