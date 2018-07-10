package com.saas.saasuser.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.saas.saasuser.R;
import com.saas.saasuser.application.MyApplication;
import com.saas.saasuser.global.NetIntent;
import com.saas.saasuser.global.NetIntentCallBackListener;
import com.saas.saasuser.util.LogUtils;
import com.saas.saasuser.util.SharedPreferencesUtil;
import com.saas.saasuser.util.StringUtils;
import com.saas.saasuser.util.ToastUtils;
import com.saas.saasuser.util.Util;
import com.saas.saasuser.view.CircleImageView;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.saas.saasuser.R.id.iv_avatar;
import static com.saas.saasuser.R.id.tv_hjid;

/**
 * 个人信息主界面
 */
public class ProfileActivity extends BaseActivity implements NetIntentCallBackListener.INetIntentCallBack {
    @BindView(R.id.head_back)
    LinearLayout headBack;
    @BindView(R.id.head_title)
    TextView headTitle;
    @BindView(iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(tv_hjid)
    TextView tvHjid;
    private JSONObject json;
    private SharedPreferencesUtil util;
    private boolean isFresh = false;

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    updateUser();
                    updateView();
                    break;
                case 2:
                    isFresh = false;
                    new NetIntent().client_getPersonInfo(util.getUserId(), new NetIntentCallBackListener(ProfileActivity.this));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        Util.setHeadTitleMore(this, "我的", true);
        util = new SharedPreferencesUtil(ProfileActivity.this);
        initUI();
    }


    private void initUI() {
//        if (StringUtils.isNotEmpty(util.getUserId(), true) && StringUtils.isNotEmpty(util.getNickName(), true) && StringUtils.isNotEmpty(util.getPicture(), true)) {
//            UserInfo userInfo = new UserInfo(util.getUserId(), util.getNickName(), Uri.parse(util.getPicture()));//更新头像昵称
//            RongIM.getInstance().setCurrentUserInfo(userInfo);
//            RongIM.getInstance().setMessageAttachedUserInfo(true);
//        }
        // updateView();

        if (util.getIsLogin()) {
            Glide.with(this).load(util.getPicture()).into(ivAvatar);
            tvName.setText(util.getNickName());
            String vvid = util.getMobilePhone();
            if (TextUtils.isEmpty(vvid)) {
                vvid = "未设置";
            }
            vvid = "VV号:" + vvid;
            tvHjid.setText(vvid);
        } else {
            tvName.setText("未登录");
            tvHjid.setText("未知");
        }
        if (util.getIsLogin()) {
            handler.sendEmptyMessage(2);
        }
    }

    @Override
    public void onError(Request request, Exception e) {

    }

    @Override
    public void onResponse(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            LogUtils.e("返回数据：" + response);
            //ToastUtils.showShortToastSafe(ProfileActivity.this, response);
            if (StringUtils.equals(jsonObject.getString("ErrCode"), "1")) {
                if (jsonObject.has("Data")) {
                    json = jsonObject.getJSONObject("Data");
                    if (json != null && json.length() > 0) {
                        handler.sendEmptyMessage(0);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void updateUser() {
        try {//更新数据

            util.setPicture("http://"+json.getString("pHeadPath"));
            util.setProvince(json.getString("province"));
            util.setCity(json.getString("pAddress"));
            util.setNickName(json.getString("pNickname"));
            util.setRealName(json.getString("StaffName"));
            util.setMotto(json.getString("CompanyName"));
            util.setBirthday(json.getString("pBirthday"));
            util.setSex(json.getInt("Sex"));
            LogUtils.e("头像路径："+util.getPicture());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateView() {//刷新页面数据
        try {
            if (util.getIsLogin()) {
               Glide.with(this).load(util.getPicture()).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.my_headphoto).into(ivAvatar);
                tvName.setText(StringUtils.repalceEmptyString(util.getNickName()));
                tvHjid.setText("VV号:" + util.getMobilePhone());
            } else {
                ivAvatar.setImageResource(R.drawable.my_headphoto);
                tvName.setText("请登录");
                tvHjid.setText("");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
        }
        super.onActivityResult(requestCode, resultCode, data);
    }




    @Override
    public void onStart() {
        super.onStart();
        if (util.getIsLogin()) {
            Glide.with(ProfileActivity.this).load(util.getPicture()).into(ivAvatar);//启动时刷新头像
        }

    }

    @OnClick({R.id.head_back, iv_avatar, R.id.rl_myinfo, R.id.re_xiangce, R.id.re_shoucang, R.id.re_download, R.id.re_setting,R.id.re_card_bag})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back:
                break;
            case iv_avatar:
                break;
            case R.id.rl_myinfo:
                if (util.getIsLogin()) {
                    startActivityForResult(new Intent(ProfileActivity.this, ProfileDetailActivity.class), 0);
                } else {
                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                }
                break;
            case R.id.re_xiangce:
                if (util.getIsLogin()) {
                    startActivity(new Intent(ProfileActivity.this, OrderRecordListActivity.class));
                } else {
                    ToastUtils.showShortToast(ProfileActivity.this, "抱歉，请您先登录！！");
                }
                break;
            case R.id.re_shoucang:
                if (util.getIsLogin()) {
                    startActivity(new Intent(ProfileActivity.this, EnterprisesCardActivity.class));
                } else {
                    ToastUtils.showShortToast(ProfileActivity.this, "抱歉，请您先登录！！");
                }
                break;
            case R.id.re_card_bag:
                if (util.getIsLogin()) {
                    startActivity(new Intent(ProfileActivity.this, FeedBackActivity.class));
                } else {
                    ToastUtils.showShortToast(ProfileActivity.this, "抱歉，请您先登录！！");
                }
                break;
            case R.id.re_download:
                break;
            case R.id.re_setting:
                if (util.getIsLogin()) {
                    startActivity(new Intent(ProfileActivity.this, SetActivity.class));
                } else {
                    ToastUtils.showShortToast(ProfileActivity.this, "抱歉，请您先登录！");
                }
                break;
        }
    }
}
