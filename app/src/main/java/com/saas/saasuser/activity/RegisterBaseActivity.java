package com.saas.saasuser.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.gyf.barlibrary.ImmersionBar;
import com.saas.saasuser.R;

import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by tyl on 2017/6/28.
 */

public abstract class RegisterBaseActivity extends AppCompatActivity {
    //定义缓存
    private List<RegisterBaseActivity> mActivities = new LinkedList<>();
    public static RegisterBaseActivity activity;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        synchronized (mActivities) {
            mActivities.add(this);
        }
        setContentView(getLayoutResId());
        ButterKnife.bind(this);
        init();
    }

    /***
     * 设置状态栏
     */
    private void setStatus() {

        ImmersionBar.with(this).statusBarColor(R.color.status_color)
                .fitsSystemWindows(true).init();
    }


    /**
 * 初始化公共的功能
 */

    protected  void init(){

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    /**
     * 初始化布局
     * @return
     */
    //
    public abstract int getLayoutResId();





    /**
     * 抽取跳转到摸个activity的功能
     * @param activity
     */
    public void navigateTo(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    /*   */
    }
    public  void finishActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onPause() {
        activity = null;
        super.onPause();
    }

    @Override
    protected void onResume() {
        activity = this;
        super.onResume();
    }

    /**
     * 关闭所有的activity
     */
    public void finishAll() {
        List<RegisterBaseActivity> copyActivities = null;
        synchronized (mActivities) {
            copyActivities = new LinkedList<RegisterBaseActivity>(mActivities);
        }
        if (copyActivities != null && copyActivities.size() > 0) {
            for (RegisterBaseActivity activity : copyActivities) {
                activity.finish();
            }
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }


    @Override
    protected void onDestroy() {
        synchronized (mActivities) {
            mActivities.remove(this);
        }

        super.onDestroy();
    }
    //点击当前界面edittext之外任意地方隐藏软键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;

        }
        return onTouchEvent(ev);
    }
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {

                return true;
            }
        }
        return false;
    }
}