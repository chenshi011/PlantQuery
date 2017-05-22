package com.android.cs.plantquery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.android.cs.plantquery.activity.Main;
import com.android.cs.plantquery.common.SettingUtils;
import com.android.cs.plantquery.common.UIHelper;

/**
 * 应用程序启动类：显示欢迎界面并跳转到主界面
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class AppStart extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View view = View.inflate(this, R.layout.start, null);
		setContentView(view);

		// 渐变展示启动屏
		AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
		aa.setDuration(2000);
		view.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				redirectTo();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}

		});
	}

	/**
	 * 跳转到...
	 */
	private void redirectTo() {
		if (UIHelper.isExistSDCard()) {
			boolean isFirstRunApp = SettingUtils.getBoolean(this,
					Constant.FISRT_RUN, true);
			if (isFirstRunApp) {
				if (UIHelper.getSDFreeSize() < 200) {
					UIHelper.ShowSimpleDiaLog(this, getString(R.string.sdcad_not_exist));
				}
			}
			Intent intent = new Intent(this, Main.class);
			startActivity(intent);
			finish();
		}else {
			UIHelper.ShowSimpleDiaLog(this, getString(R.string.sdcad_not_exist));
		}
	}
}