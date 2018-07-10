
package com.saas.saasuser.easemob.chat;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.ImageView;

import com.gyf.barlibrary.ImmersionBar;
import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.saas.saasuser.R;
import com.saas.saasuser.application.MyApplication;
import com.saas.saasuser.receiver.ConnectionChangeReceiver;
import com.saas.saasuser.view.CustomProgress;

import cn.jpush.android.api.JPushInterface;

@SuppressLint("Registered")
public class IMBaseActivity extends EaseBaseActivity {
    protected ImageView iv_back;// 返回图片
    protected CustomProgress dialog;
    private ConnectionChangeReceiver mReceiver;// 网络连接状态监听广播
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        ImmersionBar.with(this).statusBarColor(R.color.app_color).init();
        registerReceiver();
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
        // umeng
//        MobclickAgent.onResume(this);
    }

    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
        // umeng
//        MobclickAgent.onPause(this);
    }


}
