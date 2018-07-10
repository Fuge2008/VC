package com.saas.saasuser.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.saas.saasuser.R;
import com.saas.saasuser.application.MyApplication;
import com.saas.saasuser.global.Constants;
import com.saas.saasuser.global.NetIntent;
import com.saas.saasuser.global.NetIntentCallBackListener;
import com.saas.saasuser.util.DataCleanManagerUtils;
import com.saas.saasuser.util.LogUtils;
import com.saas.saasuser.util.SharedPreferencesUtil;
import com.saas.saasuser.util.StringUtils;
import com.saas.saasuser.util.ToastUtils;
import com.saas.saasuser.util.UpdateAppHttpUtil;
import com.saas.saasuser.util.Util;
import com.saas.saasuser.view.DragPointView;
import com.squareup.okhttp.Request;
import com.vector.update_app.UpdateAppManager;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设置界面
 */
public class SetActivity extends BaseActivity implements NetIntentCallBackListener.INetIntentCallBack {
    private static String TAG = SetActivity.class.getSimpleName();
    @BindView(R.id.tv_modify_psw)
    TextView tvModifyPsw;
    @BindView(R.id.tv_app_update)
    TextView tvAppUpdate;
    @BindView(R.id.tv_cache_clear)
    TextView tvCacheClear;
    @BindView(R.id.tv_cache_storage)
    TextView tvCacheStorage;
    @BindView(R.id.ll_cache_clear)
    LinearLayout llCacheClear;
    @BindView(R.id.tv_call_customer)
    TextView tvCallCustomer;
    @BindView(R.id.tv_about_haoji)
    TextView tvAboutHaoji;
    @BindView(R.id.btn_exit)
    Button btnExit;
    @BindView(R.id.item_info_tip)
    DragPointView itemInfoTip;

    private SharedPreferencesUtil util;
    private JSONObject jsonkf;
    private JSONObject jsonabout;
    private String totalCache;
    private JSONObject jsonObject;
    private boolean isNewVersion=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        Util.setHeadTitleMore(this, "设置", true);
        findViewById(R.id.head_more).setVisibility(View.GONE);
        util = new SharedPreferencesUtil(this);
        new NetIntent().client_checkAPKVersion(StringUtils.getVersionName(this)+"", new NetIntentCallBackListener(this));   //TODO  请求最新版本代码
       // LogUtils.e("版本号："+StringUtils.getVersionName(this));

        try {
            totalCache = DataCleanManagerUtils.getTotalCacheSize(SetActivity.this);
            if (StringUtils.isNotEmpty(totalCache, true)) {
                tvCacheStorage.setText(totalCache);
            } else {
                tvCacheStorage.setText("无缓存");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OnClick({R.id.tv_modify_psw, R.id.tv_app_update, R.id.ll_cache_clear, R.id.tv_call_customer, R.id.tv_about_haoji, R.id.btn_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_modify_psw:
                startActivity(new Intent(SetActivity.this, ForgetPasswordActivity.class));
                break;
            case R.id.tv_app_update:
                if(isNewVersion){
                    new AlertDialog.Builder(SetActivity.this).setTitle("更新提示").setMessage("有最新版本，需要升级吗?")
                            .setPositiveButton("下载", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = null;
                                    try {
                                        i = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.HttpRoot + Constants.downloadAPK));
                                        itemInfoTip.setVisibility(View.GONE);
                                        isNewVersion=false;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    startActivity(i);
                                }
                            }).setNegativeButton("取消", null).create().show();
                    ToastUtils.showShortToastSafe(SetActivity.this, "已经是最新版本");
                }


                break;


            case R.id.ll_cache_clear:
                if (StringUtils.equals(tvCacheStorage.getText(), "无缓存")) {
                    ToastUtils.showShortToastSafe(SetActivity.this, "暂无缓存");
                } else {
                    try {
                        DataCleanManagerUtils.clearAllCache(SetActivity.this);
                        ToastUtils.showShortToastSafe(SetActivity.this, "已清理缓存" + totalCache);
                        tvCacheStorage.setText("无缓存");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
            case R.id.tv_call_customer:
                try {
                    new AlertDialog.Builder(SetActivity.this).setTitle("客服").setMessage("客服电话:" + "075588824112").setPositiveButton("拨打", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent phoneIntent = null;
                            phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + "075588824112"));
                            startActivity(phoneIntent);
                        }
                    }).setNegativeButton("取消", null).create().show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_about_haoji:

                Intent myIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString(StringUtils.TITLE, "V V租行");
                bundle.putString(StringUtils.URL, "http://vv-che.com/");
                myIntent.putExtras(bundle);
                myIntent.setClass(SetActivity.this, WebViewActivity.class);
                startActivity(myIntent);
                break;
            case R.id.btn_exit:
                exitApp();
                break;

        }
    }

    private void exitApp() {
        new AlertDialog.Builder(this).setTitle("提示").setMessage("确定注销登录？").setPositiveButton("退出软件", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyApplication.getInstance().exit();
                SetActivity.this.finish();

            }
        }).setNegativeButton("退出登录", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EMClient.getInstance().logout(true);//退出环信
                util.setMobilePhone("");
                util.setPicture("");
                util.setPassWord("");
                util.setIsLogin(false);
                startActivity(new Intent(SetActivity.this, LoginActivity.class));
               finish();
            }
        }).create().show();


    }



    @Override
    public void onError(Request request, Exception e) {

    }

    @Override
    public void onResponse(String response) {
        System.out.println(response);
        try {
            jsonObject = new JSONObject(response);
            ToastUtils.showShortToastSafe(SetActivity.this, jsonObject.getString("ErrMsg"));

            if (StringUtils.equals(jsonObject.getString("ErrCode"), "1")) {

                if (jsonObject.has("Data")) {
                    if(StringUtils.equals(jsonObject.getString("Data"),"true")){

                        LogUtils.e("版本："+jsonObject.getString("Data"));
                        itemInfoTip.setVisibility(View.VISIBLE);
                        isNewVersion=true;

                        //提示弹框
                        new UpdateAppManager
                                .Builder()
                                //当前Activity
                                .setActivity(this)
                                //更新地址
                                .setUpdateUrl("https://raw.githubusercontent.com/WVector/AppUpdateDemo/master/json/json.txt")
                                //实现httpManager接口的对象
                                .setHttpManager(new UpdateAppHttpUtil())
                                .build()
                                .update();

                    }



                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
