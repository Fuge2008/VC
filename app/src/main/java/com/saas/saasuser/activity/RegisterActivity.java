package com.saas.saasuser.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gyf.barlibrary.ImmersionBar;
import com.saas.saasuser.R;
import com.saas.saasuser.adapter.RegisterFragmentAdapter;
import com.saas.saasuser.application.MyApplication;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.OnClick;


public class RegisterActivity extends RegisterBaseActivity {

    @BindView(R.id.iv_reg_back)
    ImageView mIvRegBack;
    @BindView(R.id.rela_title)
    RelativeLayout mRelaTitle;
    @BindView(R.id.tablayout)
    TabLayout mTablayout;
    @BindView(R.id.viewpager_reg)
    ViewPager mViewpagerReg;
    private RegisterFragmentAdapter mAdapter;

    private TabLayout.Tab one;
    private TabLayout.Tab two;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        setStatus();
        initView();
    }

    /***
     * 设置状态栏
     */
    private void setStatus() {
        ImmersionBar.with(this).statusBarColor(R.color.status_color)
                .fitsSystemWindows(true).init();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_register;
    }

    private void initView() {
        //使用适配器将ViewPager与Fragment绑定在一起
        mViewpagerReg = (ViewPager) findViewById(R.id.viewpager_reg);
        mAdapter = new RegisterFragmentAdapter(getSupportFragmentManager());
        mViewpagerReg.setAdapter(mAdapter);

        //将TabLayout与ViewPager绑定在一起
        mTablayout = (TabLayout) findViewById(R.id.tablayout);
        mTablayout.setupWithViewPager(mViewpagerReg);

        mTablayout.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(mTablayout, 50, 50);
            }
        });

        //指定Tab的位置
        one = mTablayout.getTabAt(0);
        two = mTablayout.getTabAt(1);

    }

    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    //标题栏按钮
    @OnClick(R.id.iv_reg_back)
    public void onViewClicked(View v) {
        finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
