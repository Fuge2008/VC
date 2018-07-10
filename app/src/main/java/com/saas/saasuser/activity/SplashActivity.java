package com.saas.saasuser.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.saas.saasuser.R;
import com.saas.saasuser.application.MyApplication;

/**
 * 启动页面
 */
public class SplashActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏应用程序的标题栏，即当前activity的label
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); // 隐藏android系统的状态栏
        setContentView(R.layout.activity_splash);
        MyApplication.getInstance().addActivity(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, AdvertisingActivity.class);
                startActivity(intent);
                finish();
            }
        },1000);



    }

}
