package ustc.sse.water.home;

import ustc.sse.water.activity.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * 
 * Activity类. <br>
 * 应用启动动画.
 * <p>
 * Copyright: Copyright (c) 2015-4-28 下午10:15:59
 * <p>
 * Company: 中国科学技术大学软件学院
 * <p>
 * 
 * @author 周晶鑫
 * @version 1.0.0
 */
public class WelcomeShow extends Activity implements AnimationListener {

	private ImageView imageView = null; // 显示的图片
	private Animation alphaAnimation = null; // 透明度动画

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_show);

		imageView = (ImageView) findViewById(R.id.welcome_image_view);
		// 初始化动画
		alphaAnimation = AnimationUtils.loadAnimation(this,
				R.anim.welcome_alpha);
		alphaAnimation.setFillEnabled(true); // 启动Fill保持
		alphaAnimation.setFillAfter(true); // 设置动画的最后一帧在view上
		imageView.setAnimation(alphaAnimation);
		alphaAnimation.setAnimationListener(this);
	}

	@Override
	public void onAnimationStart(Animation animation) {
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// 动画结束时结束欢迎界面并转到软件的主界面
		Intent intent = new Intent(this, DriverMainScreen.class);
		startActivity(intent);
		this.finish();
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 在欢迎界面屏蔽BACK键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return false;
	}

}
