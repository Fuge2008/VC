//package com.saas.saasuser.activity;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.ViewFlipper;
//
//import com.saas.saasuser.R;
//import com.saas.saasuser.async.AsyncTaskManager;
//import com.saas.saasuser.async.HttpException;
//import com.saas.saasuser.async.OnDataListener;
//
//import java.io.IOException;
//
///**
// * Created by tanlin on 2017/10/12.
// */
//
//public class BaseActivity3 extends FragmentActivity implements OnDataListener {
//
//    protected Context mContext;//
//    protected RelativeLayout layout_head;
//    protected ImageView btn_left;
//    protected ImageView btn_right;
//    protected TextView tv_title;
//    private AsyncTaskManager mAsyncTaskManager;
//    private ViewFlipper mContentView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        super.setContentView(R.layout.layout_base2);
//        mContext = this;
//
//        //初始化公共头部
//        mContentView = (ViewFlipper) super.findViewById(R.id.layout_container);
//        layout_head = (RelativeLayout) super.findViewById(R.id.layout_head);
//        btn_left = (ImageView) super.findViewById(R.id.btn_left);
//        btn_left.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        btn_right = (ImageView) super.findViewById(R.id.btn_right);
//
//        btn_right.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                checkRight();
//            }
//        });
//
//
//        tv_title = (TextView) super.findViewById(R.id.tv_title);
//
//        //初始化异步框架
//        mAsyncTaskManager = AsyncTaskManager.getInstance(mContext.getApplicationContext());
//        //Activity管理
//       // ActivityPageManager.getInstance().addActivity(this);
//    }
//
//    @Override
//    public void setContentView(View view) {
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
//        mContentView.addView(view, lp);
//    }
//    @SuppressWarnings("unchecked")
//    protected <T extends View> T getViewById(int id) {
//        return (T) findViewById(id);
//    }
//
//    @Override
//    public void setContentView(int layoutResID) {
//        View view = LayoutInflater.from(this).inflate(layoutResID, null);
//        setContentView(view);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    //    ActivityPageManager.unbindReferences(mContentView);
//     //   ActivityPageManager.getInstance().removeActivity(this);
//        mContentView = null;
//        mContext = null;
//    }
//
//    /**
//     * 发送请求（需要检查网络）
//     * @param requestCode 请求码
//     */
//    public void request(int requestCode){
//        mAsyncTaskManager.request(requestCode, this);
//    }
//
//    /**
//     * 发送请求
//     * @param requestCode 请求码
//     * @param isCheckNetwork 是否需检查网络，true检查，false不检查，主要是用于有时候网络请求从缓存里面取的
//     */
//    public void request(int requestCode, boolean isCheckNetwork){
//        mAsyncTaskManager.request(requestCode, isCheckNetwork, this);
//    }
//
//
//    @Override
//    public Object doInBackground(int requestCode) throws HttpException, IOException {
//
//        return null;
//    }
//
//    @Override
//    public boolean onIntercept(int requestCode, Object result) {
//        // 返回true表示打断，false表示继续执行onSuccess方法
//        return false;
//    }
//
//    @Override
//    public void onSuccess(int requestCode, Object result) {
//        // 处理成功的逻辑
//    }
//
//    @Override
//    public void onFailure(int requestCode, int state, Object result) {
//        switch(state){
//            //网络不可用给出提示
//            case AsyncTaskManager.HTTP_NULL_CODE:
//              //  NToast.shortToast(mContext, R.string.common_network_unavailable);
//                break;
//
//            //网络有问题给出提示
//            case AsyncTaskManager.HTTP_ERROR_CODE:
//              //  NToast.shortToast(mContext, R.string.common_network_error);
//                break;
//
//            //请求有问题给出提示
//            case AsyncTaskManager.REQUEST_ERROR_CODE:
//             //   NToast.shortToast(mContext, R.string.common_request_error);
//                break;
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    /**
//     * 设置头部是否可见
//     * @param visibility
//     */
//    public void setHeadVisibility(int visibility) {
//        layout_head.setVisibility(visibility);
//    }
//
//    /**
//     *设置标题
//     */
//    public void setTitle(int titleId) {
//        tv_title.setText(getString(titleId));
//    }
//
//    /**
//     * 设置标题
//     * @param title
//     */
//    public void setTitle(String title) {
//        tv_title.setText(title);
//    }
//
//    public void checkRight(){
//
//
//
//    }
//
//}
