//package com.saas.saasuser.activity;
//
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//
//import com.saas.saasuser.R;
//import com.saas.saasuser.application.MyApplication;
//import com.saas.saasuser.util.Util;
//import com.saas.saasuser.view.CircleImageView;
//import com.saas.saasuser.view.headerwave.HeaderWaveHelper;
//import com.saas.saasuser.view.headerwave.HeaderWaveView;
//import com.saas.saasuser.view.headerwave.ObservableScrollView;
//import com.saas.saasuser.view.headerwave.ScrollViewListener;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
///**
// * 用户中心界面
// */
//public class UserCenterActivity extends BaseActivity implements ScrollViewListener {
//    @BindView(R.id.ll_my_trip)
//    LinearLayout llMyTrip;
//
//    @BindView(R.id.ll_system_setting)
//    LinearLayout llSystemSetting;
//    @BindView(R.id.iv_imageView)
//    CircleImageView ivImageView;
//    private HeaderWaveHelper mHeaderWaveHelper;
//    ObservableScrollView mScrollView;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_user_center);
//        MyApplication.getInstance().addActivity(this);
//        ButterKnife.bind(this);
//        Util.setHeadTitleMore(this, "个人中心", true);
//        findViewById(R.id.head_more).setVisibility(View.GONE);
//
//
//        HeaderWaveView waveView = (HeaderWaveView) findViewById(R.id.header_wave_view);
//        ImageView imageView = (ImageView) findViewById(R.id.iv_imageView);
//        mScrollView = (ObservableScrollView) findViewById(R.id.sv);
//
//        mScrollView.setScrollViewListener(this);
//
//        mHeaderWaveHelper = new HeaderWaveHelper(waveView, this.getResources().getColor(R.color.pink), this.getResources().getColor(R.color.pink2), imageView);
//
//    }
//
//
//
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mHeaderWaveHelper.cancel();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mHeaderWaveHelper.start();
//    }
//
//
//    @Override
//    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
//        if (mScrollView.getScrollY() > 85) {
//            mHeaderWaveHelper.cancel();
//        } else {
//            mHeaderWaveHelper.start();
//        }
//    }
//
//    @OnClick({R.id.ll_my_trip,R.id.ll_system_setting, R.id.iv_imageView})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.ll_my_trip:
//                startActivity(new Intent(UserCenterActivity.this, OrderTraceRecordActivity.class));
//                break;
//            case R.id.ll_system_setting:
//                startActivity(new Intent(UserCenterActivity.this, SetActivity.class));
//                break;
//            case R.id.iv_imageView:
//                break;
//        }
//    }
//}