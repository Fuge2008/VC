package com.saas.saasuser.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.saas.saasuser.R;
import com.saas.saasuser.application.MyApplication;

/**
 * Created by tanlin on 2017/12/19.
 */

public class AdvertisingActivity extends Activity {

    private TextView mTextView;
    private CountDownTimer timer;

    private Handler handler = new Handler();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ad);
        MyApplication.getInstance().addActivity(this);
        mTextView= (TextView) findViewById(R.id.ui_button);

        timer = new CountDownTimer(4000,1000) {
            int num = 3;
            @Override
            public void onTick(long millisUntilFinished) {
                mTextView.setText("跳过\n"+String.valueOf(num));
                num--;
            }
            @Override
            public void onFinish() {



                    startActivity(new Intent(AdvertisingActivity.this, LoginActivity.class));
                    finish();


            }
        };
        timer.start();

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    startActivity(new Intent(AdvertisingActivity.this, LoginActivity.class));
                    timer.cancel();
                    finish();


            }
        });


    }
}
