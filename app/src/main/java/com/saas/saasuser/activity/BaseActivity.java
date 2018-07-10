package com.saas.saasuser.activity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.gyf.barlibrary.ImmersionBar;
import com.saas.saasuser.R;
import com.saas.saasuser.receiver.ConnectionChangeReceiver;
import com.saas.saasuser.view.CustomProgress;

import cn.jpush.android.api.JPushInterface;


/**
 * 界面基类
 */
public  class BaseActivity extends FragmentActivity  {

	protected ImageView iv_back;// 返回图片
	protected CustomProgress dialog;
	private ConnectionChangeReceiver mReceiver;// 网络连接状态监听广播


	@Override
	protected  void onCreate(Bundle savedInstanceState) {
		ImmersionBar.with(this).statusBarColor(R.color.app_color).init();
		registerReceiver();
		super.onCreate(savedInstanceState);
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);// 注销网络广播监听


	}

	private void registerReceiver() {
		IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		mReceiver = new ConnectionChangeReceiver();
		this.registerReceiver(mReceiver, filter);
	}


	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

}
