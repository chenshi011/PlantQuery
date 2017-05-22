package com.android.cs.plantquery.activity;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.cs.plantquery.BusinessActivity;
import com.android.cs.plantquery.Constant;
import com.android.cs.plantquery.R;
import com.android.cs.plantquery.bean.MessageObj;
import com.android.cs.plantquery.bean.Plant;
import com.android.cs.plantquery.common.FileUtils;
import com.android.cs.plantquery.common.ImageUtils;
import com.android.cs.plantquery.common.SettingUtils;
import com.android.cs.plantquery.widget.ChoosePicDialog;
import com.android.cs.plantquery.widget.ImageDel;

/**
 * 新建(编辑)植物
 * 
 * @author CHENSHI
 * 
 */
public class PlantAdd extends BusinessActivity implements OnClickListener {

	EditText editText_name;
	EditText editText_alias;
	EditText editText_latinName;
	EditText editText_belongs;
	EditText editText_branch;
	EditText editText_city;
	EditText editText_detail;

	TextView textView_title;
	/** 照片容器 */
	ViewGroup image_container;

	View btn_add;

	String old_name, old_alias, old_latinName, old_belongs, old_branch,
			old_city, old_detail;

	/** 拍照返回 */
	private final static int RESULT_TAKE_PHOTO = 0x11;

	private final static int RESULT_LOAD_IMAGE = 0x12;

	String m_picPath = "", camera_picPath = "";

	private Plant m_plant;

	/** 加载图片 */
	private final static int IMAGE_LOAD = 0x3;

	private final static int DIALOG_EXIT = 0x1;

	private final static int DIALOG_ADD_PIC = 0x2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.plant_add);
		initViews();
		initData();
	}

	private void initViews() {
		textView_title = (TextView) findViewById(R.id.txt_title);
		editText_name = (EditText) findViewById(R.id.txt_name);
		editText_alias = (EditText) findViewById(R.id.txt_alias);
		editText_latinName = (EditText) findViewById(R.id.txt_latinName);
		editText_belongs = (EditText) findViewById(R.id.txt_belongs);
		editText_branch = (EditText) findViewById(R.id.txt_branch);
		editText_city = (EditText) findViewById(R.id.txt_city);
		editText_detail = (EditText) findViewById(R.id.txt_detail);
		image_container = (ViewGroup) findViewById(R.id.image_container);
		View btn_ok = findViewById(R.id.btn_ok);
		View btn_cancel = findViewById(R.id.btn_cancel);
		btn_add =  findViewById(R.id.btn_add);
		btn_ok.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		btn_add.setOnClickListener(this);
	}

	private void initData() {
		if (getIntent().hasExtra("data")) {
			try {
				m_plant = (Plant) getIntent().getSerializableExtra("data");
				loadData();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	private void loadData() {
		textView_title.setText("编辑植物");
		if (m_plant.getName() != null)
			editText_name.setText(m_plant.getName());
		if (m_plant.getAlias() != null)
			editText_alias.setText(m_plant.getAlias());
		if (m_plant.getLatinName() != null)
			editText_latinName.setText(m_plant.getLatinName());
		if (m_plant.getBelongs() != null)
			editText_belongs.setText(m_plant.getBelongs());
		if (m_plant.getBranch() != null)
			editText_branch.setText(m_plant.getBranch());
		if (m_plant.getCity() != null)
			editText_city.setText(m_plant.getCity());
		if (m_plant.getDetails() != null)
			editText_detail.setText(m_plant.getDetails());
		if (m_plant.getPics() != null) {
			String[] pics = m_plant.getPics().split(";");
			for (int i = 0; i < pics.length; i++) {
				Bitmap bitmap = ImageUtils.loadImgThumbnail(pics[i], 105, 105);
				if (bitmap != null) {
					Message msg = new Message();
					msg.what = IMAGE_LOAD;
					msg.obj = new MessageObj(bitmap, pics[i]);
					load_image_handler.sendMessage(msg);
				}
			}
		}
		initEditTextData();
		if (m_plant != null) {
			index = m_plant.getPics().split(";").length + 1;
		}
	}

	

	private void initEditTextData() {
		old_name = editText_name.getText() != null ? editText_name.getText()
				.toString() : null;
		old_alias = editText_alias.getText() != null ? editText_alias.getText()
				.toString() : null;
		old_latinName = editText_latinName.getText() != null ? editText_latinName
				.getText().toString() : null;
		old_belongs = editText_belongs.getText() != null ? editText_belongs
				.getText().toString() : null;
		old_branch = editText_branch.getText() != null ? editText_branch
				.getText().toString() : null;
		old_city = editText_city.getText() != null ? editText_city.getText()
				.toString() : null;
		old_detail = editText_detail.getText() != null ? editText_detail
				.getText().toString() : null;
	}

	@Override
	protected void onResume() {
		initEditTextData();
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			if (m_plant == null) {
				m_plant = new Plant();
			}
			boolean hasData = false;
			if (editText_name.getText() != null && !editText_name.getText().toString().trim().equals("")){
				m_plant.setName(editText_name.getText().toString());
				hasData = true;
			}
			if (editText_alias.getText() != null && !editText_alias.getText().toString().trim().equals("")){
				m_plant.setAlias(editText_alias.getText().toString());
				hasData = true;
			}
			if (editText_latinName.getText() != null && !editText_latinName.getText().toString().trim().equals("")){
				m_plant.setLatinName(editText_latinName.getText().toString());
				hasData = true;
			}
			if (editText_belongs.getText() != null && !editText_belongs.getText().toString().trim().equals("")){
				m_plant.setBelongs(editText_belongs.getText().toString());
				hasData = true;
			}
			if (editText_branch.getText() != null && !editText_branch.getText().toString().trim().equals("")){
				m_plant.setBranch(editText_branch.getText().toString());
				hasData = true;
			}
			if (editText_city.getText() != null && !editText_city.getText().toString().trim().equals("")){
				m_plant.setCity(editText_city.getText().toString());
				hasData = true;
			}
			if (editText_detail.getText() != null && !editText_detail.getText().toString().trim().equals("")){
				m_plant.setDetails(editText_detail.getText().toString());
				hasData = true;
			}
			if (m_picPath != null && !m_picPath.equals("")) {
				if (m_picPath.contains(";")) {
					m_picPath = m_picPath.substring(0, m_picPath.length() - 1);
				}
				if (m_plant.getPics() != null && !m_plant.getPics().equals(""))
					m_plant.setPics(m_plant.getPics() + ";" + m_picPath);
				else {
					m_plant.setPics(m_picPath);
				}
				hasData = true;
			}
			if(hasData){
				Intent intent = getIntent();
				intent.putExtra("data", m_plant);
				setResult(RESULT_OK, intent);
			}
			finish();
			break;

		case R.id.btn_cancel:
			onBack();
			break;
		case R.id.btn_add:
			showDialog(DIALOG_ADD_PIC);
			// onCamera();
			break;
		default:
			break;
		}
	}

	// 拍照
	private void takePhotos() {
		Intent intent = new Intent();
		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(makePicPath()));

		startActivityForResult(intent, RESULT_TAKE_PHOTO);
	}

	//图片放到指定的文件夹下面
	private File makePicPath(){
		String picPath = SettingUtils.getString(this,Constant.PIC_PATH,
				Constant.PIC_PATH_DEFAULT);
		if (!FileUtils.checkFileExists(picPath)) {
			FileUtils.createFolder(picPath);
		}
		int id = -1;
		if (m_plant != null) {
			id = m_plant.getId();
		} else {
			id = plantManager.getMaxId() + 1;
		}
		String name = String.valueOf(id);
		if (index > 0) {
			name = id + "-" + index;
		}
		camera_picPath = picPath + "/" + name + ".jpg";

		if (FileUtils.checkFileExists(camera_picPath)) {
			FileUtils.deleteFile(camera_picPath);
		}
		
		return new File(picPath, name + ".jpg");
	}
	
	/**
	 * 从手机相册里面选择
	 */
	private void chooseAlbum() {
		Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(makePicPath()));
		
		
		intent.putExtra("crop", "true");// 才能出剪辑的小方框，不然没有剪辑功能，只能选取图片  
		intent.putExtra("aspectX", 1); // 出现放大和缩小  
		intent.setType("image/*"); // 查看类型 详细的类型在 com.google.android.mms.ContentType   
	          
        intent.putExtra("output", Uri.fromFile(makePicPath()));  // 专入目标文件 
		
		startActivityForResult(intent, RESULT_LOAD_IMAGE);
	}

	private void onBack() {
		if (isChanged()) {
			showDialog(DIALOG_EXIT);
		} else {
			setResult(RESULT_CANCELED);
			finish();
		}
	}

	int index = 0;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case RESULT_TAKE_PHOTO:
			case RESULT_LOAD_IMAGE:
				loadImage();
				break;
			default:
				break;
			}
		} else if (resultCode == RESULT_CANCELED) {
			// ImageUtils.deleteImage(curTrack.getPic());
		}
	}

	private void loadImage() {
		if (camera_picPath != null
				&& FileUtils.checkFileExists(camera_picPath)) {
			// image_container.
			m_picPath += camera_picPath + ";";
			index++;
			new Thread() {
				@Override
				public void run() {
					Bitmap bitmap = ImageUtils.loadImgThumbnail(camera_picPath, Constant.IMGTHUMB_WIDTH,
							Constant.IMGTHUMB_HEIGHT);
					if (bitmap != null) {
						Message msg = new Message();
						msg.what = IMAGE_LOAD;
						msg.obj = new MessageObj(bitmap, camera_picPath);
						load_image_handler.sendMessage(msg);
					}
				}
			}.start();
		}
	}
	
	@SuppressLint("HandlerLeak")
	final Handler load_image_handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == IMAGE_LOAD && msg.obj != null) {
				// 显示图片
				final ImageDel imageDel = new ImageDel(PlantAdd.this);
				//ImageView imageView = new ImageView(PlantAdd.this);
				imageDel.imageView.setImageBitmap(((MessageObj) msg.obj).getBitmap());
				imageDel.imageView.setVisibility(View.VISIBLE);
				imageDel.setTag(msg.obj);
				imageDel.delView.setTag(msg.obj);
				imageDel.imageView.setTag(msg.obj);
				imageDel.imageView.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(PlantAdd.this,
								ImageZoomDialog.class);
						intent.putExtra("img_url",
								((MessageObj) v.getTag()).getImage_url());
						startActivity(intent);
					}
				});
				
				imageDel.delView.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						AlertDialog.Builder del_builder = new AlertDialog.Builder(
								PlantAdd.this);
						del_builder
								.setTitle(getString(R.string.plant_del_pic_title));
						del_builder.setMessage(getString(R.string.plant_del_pic_msg));
						del_builder
								.setPositiveButton(
										"确定",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface arg0,
													int arg1) {
												String image_url = ((MessageObj)imageDel.getTag()).getImage_url();
												
												String pics = "";
												if (m_picPath != null && !m_picPath.equals("")) {
													if (m_picPath.contains(";")) {
														m_picPath = m_picPath.substring(0, m_picPath.length() - 1);
													}
												}
												if (m_plant!=null && m_plant.getPics() != null && !m_plant.getPics().equals("")){
													if (m_picPath != null && !m_picPath.equals("")) {
														pics = m_plant.getPics() + ";" + m_picPath;
													}else {
														pics = m_plant.getPics();
													}
												}else {
													if (m_picPath != null && !m_picPath.equals("")) {
														pics = m_picPath;
													}
												}
												pics = pics.replace(image_url, "");
												if(pics.lastIndexOf(";")==pics.length()-1){
													pics = pics.substring(0, pics.length() - 1);
												}
												m_plant.setPics(pics);
												FileUtils.deleteFile(image_url);
												image_container.removeView(imageDel);
											}
										});
						del_builder
								.setNegativeButton(
										"取消",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface arg0,
													int arg1) {
											}
										});
						del_builder.create().show();
					}
				});
				
				int count = image_container.getChildCount();
				image_container.addView(imageDel, count - 1);
			}
		}
	};

//	@SuppressLint("HandlerLeak")
//	final Handler handler = new Handler() {
//		public void handleMessage(Message msg) {
//			if (msg.what == 1 && msg.obj != null) {
//				// 显示图片
//				ImageDel imageDel = new ImageDel(PlantAdd.this);
//				
//				//ImageView imageView = new ImageView(PlantAdd.this);
//				imageDel.imageView.setImageBitmap(((MessageObj) msg.obj).getBitmap());
//				imageDel.imageView.setVisibility(View.VISIBLE);
//				imageDel.imageView.setTag(msg.obj);
//				imageDel.imageView.setOnClickListener(new View.OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						Intent intent = new Intent(PlantAdd.this,
//								ImageZoomDialog.class);
//						intent.putExtra("img_url",
//								((MessageObj) v.getTag()).getImage_url());
//						startActivity(intent);
//					}
//				});
//				int count = image_container.getChildCount();
//				image_container.addView(imageDel, count - 1);
//			}
//		}
//	};

	@Override
	public void onBackPressed() {
		onBack();
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		switch (id) {
		case DIALOG_EXIT:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("提示");
			builder.setMessage("确认要放弃当前编辑？");
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
							setResult(RESULT_CANCELED);
							finish();
						}
					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface arg0, int arg1) {
							dismissDialog(DIALOG_EXIT);
						}
					});

			return builder.create();
		case DIALOG_ADD_PIC:
			ChoosePicDialog dialog = new ChoosePicDialog(this);
			dialog.setOnItemClickListener(dialog_onItemClickListener);
			return dialog;
		}
		return super.onCreateDialog(id);
	}

	private ChoosePicDialog.onItemClickListener dialog_onItemClickListener = new ChoosePicDialog.onItemClickListener() {

		@Override
		public void onClick(View v, int pos) {
			switch (pos) {
			case 0:
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){//判断是否有SD卡 
					takePhotos();
				}else {
					Toast.makeText(PlantAdd.this, "没有SD卡", Toast.LENGTH_SHORT).show();
				}
				break;
			case 1:
				chooseAlbum();
				break;
			case 2:
				break;
			default:
				break;
			}
			dismissDialog(DIALOG_ADD_PIC);
		}
	};

	/**
	 * 判断是否修改了配置
	 * 
	 * @return
	 */
	private boolean isChanged() {
		String name = null, alias = null, latinName = null, belongs = null, branch = null, city = null, detail = null;

		if (editText_name.getText() != null)
			name = editText_name.getText().toString();
		if (editText_alias.getText() != null)
			alias = editText_alias.getText().toString();
		if (editText_latinName.getText() != null)
			latinName = editText_latinName.getText().toString();
		if (editText_belongs.getText() != null)
			belongs = editText_belongs.getText().toString();
		if (editText_branch.getText() != null)
			branch = editText_branch.getText().toString();
		if (editText_city.getText() != null)
			city = editText_city.getText().toString();
		if (editText_detail.getText() != null)
			detail = editText_detail.getText().toString();

		return !name.equals(old_name) | !alias.equals(old_alias)
				| !latinName.equals(old_latinName)
				| !belongs.equals(old_belongs) | !branch.equals(old_branch)
				| !city.equals(old_city) | !detail.equals(old_detail);
	}

}
