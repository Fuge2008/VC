package com.saas.saasuser.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.gyf.barlibrary.ImmersionBar;
import com.saas.saasuser.R;
import com.saas.saasuser.application.MyApplication;

/**
 * 作者：张毅阳 on 2017/11/30 16:07
 */
public class VVThreeJiuActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIvbackthreejiu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threejiu);
        MyApplication.getInstance().addActivity(this);
        setStatusBar();
        mIvbackthreejiu = (ImageView) findViewById(R.id.iv_back_threejiu);
        mIvbackthreejiu.setOnClickListener(this);
    }

    private void setStatusBar() {
        ImmersionBar.with(this).statusBarColor(R.color.status_color)
                .fitsSystemWindows(true).init();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back_threejiu:
                finish();
                break;
        }
    }
}
