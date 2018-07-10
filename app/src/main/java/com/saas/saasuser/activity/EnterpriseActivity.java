package com.saas.saasuser.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.saas.saasuser.R;
import com.saas.saasuser.application.MyApplication;
import com.saas.saasuser.bean.EnterpriseInviteCodeBean;
import com.saas.saasuser.global.Constants;
import com.saas.saasuser.util.DESUitl;
import com.saas.saasuser.util.LogUtils;
import com.saas.saasuser.util.PhoneUtils;
import com.saas.saasuser.util.SPUtils;
import com.saas.saasuser.util.StringUtils;
import com.saas.saasuser.util.ToastUtils;
import com.saas.saasuser.view.MaterialDialog;
import com.saas.saasuser.view.dialog.Dialog2;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


/**
 * Created by tyh on 2017/7/20.
 */

public class EnterpriseActivity extends RegisterBaseActivity implements View.OnClickListener {

    private static final String TAG = "EnterpriseActivity";
    private LinearLayout mLineaarEn;
    private EditText mAdminName;
    private EditText mEtSetPwd;
    private EditText mEtPwdOk;
    private EditText mSmsCode;
    private Button mBtnSend;
    private Button mBtnRegister;
    private EditText mCode;
    private ImageView mBack;
    private String mSetPwd;
    private String mPwdOk;
    private String mSms;
    private String mInvCode;

    private String str_mAdminName;
    private Context mContext;
    private String mEnterShortName;
    private String mPhoneNum;
    private Dialog2 mDialog2;
    private int mMobile_code;
    private boolean isEnFirstClick = true;
//    private boolean isEnSecondClick = true;
    //Handler运行在主线程中(UI线程中)，  它与子线程可以通过Message对象来传递数据
    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ToastUtils.MyToast(mContext, "验证码已发送,请尽快使用", Toast.LENGTH_SHORT);
                  /*  mBtnSend.setBackgroundColor(Color.GRAY);
                    ToastUtils.MyToast(mContext, "短信验证码发送成功~", 3000);*/

                    new CountDownTimer(60000, 1000) {
                        @Override
                        public void onTick(long l) {
                            mBtnSend.setClickable(false);
                            //设置倒计时按钮背景,做倒计时操作
//                            mBtnSend.setBackgroundResource(R.drawable.send_tv_bg);
                            mBtnSend.setText(l / 1000 + "s");
                            mBtnSend.setBackgroundColor(Color.TRANSPARENT);
                        }

                        @Override
                        public void onFinish() {
                            //设置重新发送时,按钮可点击
                            mBtnSend.setClickable(true);
//                            mBtnSend.setBackgroundResource(R.drawable.send_tv_bg);
                            mBtnSend.setText("重新发送");
                            mBtnSend.setBackgroundColor(Color.TRANSPARENT);
                            isEnFirstClick = true;
                        }
                    }.start();

                    break;
                case 2:
                    ToastUtils.MyToast(mContext, "今天验证码已超过5次~", 3000);
                    break;
                case 3:
                    ToastUtils.MyToast(mContext, "验证码获取失败请重新获取~", 3000);
                    break;
            }
        }
    };
    private Message mMMessage;
    private String mSettingPwd;
    private ImageView mEninvitationcode;
    private EnterpriseInviteCodeBean mEnterpriseInviteCodeBean;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        mContext = this;

        if (getIntent() != null){
            mPhoneNum = getIntent().getStringExtra("phoneNum");
        }
        initView();
        initData();

    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_enterprise;
    }

    //点击监听
    private void initData() {
        mBack.setOnClickListener(this);
        mBtnSend.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);
    }

    //初始化控件
    private void initView() {

        //容器布局
        mLineaarEn = (LinearLayout) findViewById(R.id.ll_en);

        mAdminName = (EditText) findViewById(R.id.edit_admin_name);

        //登录密码
        mEtSetPwd = (EditText) findViewById(R.id.et_setpwd_en);
        //确认密码
        mEtPwdOk = (EditText) findViewById(R.id.et_input_pwd);

        //短信验证码
        mSmsCode = (EditText) findViewById(R.id.et2_sms2_code2);

        //发送
        mBtnSend = (Button) findViewById(R.id.btn2_sms2_send2);

        //邀请码
        mCode = (EditText) findViewById(R.id.et_input_code_en);

        //注册
        mBtnRegister = (Button) findViewById(R.id.btn_en_fragment);

        mBack = (ImageView) findViewById(R.id.iv_register_back2);
        mEninvitationcode = (ImageView) findViewById(R.id.en_invitationcode);

        mBtnSend.setBackgroundColor(Color.TRANSPARENT);

        mBack.setOnClickListener(this);
        mEninvitationcode.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {

        LogUtils.d(" 被修改后还未加密的密码===" + mSetPwd +  ", ====mConfirmPwd==" + mPwdOk);
        //对输入的新密码加密提交到服务器
        mSettingPwd = DESUitl.tran2cypher(mSetPwd);
//        mSettingPwd = MD5Util.MD5Encode(mSetPwd, "utf-8");
        LogUtils.d(" 被修改后加密的后密码：" + mSettingPwd);

        str_mAdminName = mAdminName.getText().toString().trim();
        mSetPwd = mEtSetPwd.getText().toString().trim();
        mPwdOk = mEtPwdOk.getText().toString().trim();
        mSms = mSmsCode.getText().toString().trim();
        mInvCode = mCode.getText().toString().trim();
        switch (view.getId()) {
            case R.id.btn_en_fragment:    //注册
                    onBtnNext();

                break;

            case R.id.iv_register_back2:  //返回
                onBack();
                break;

            case R.id.btn2_sms2_send2:    //发送
                if (isEnFirstClick) {
                    onBtnSend();
                    isEnFirstClick = false;
                }
                break;

            case R.id.en_invitationcode:    //邀请码icon

                onInvitationCode();
                break;
        }
    }

    private void onInvitationCode() {
        final MaterialDialog materialDialog = new MaterialDialog(this)
                      /*  .setContentIV(
                                R.drawable.gantanhao)*/
                .setContentView(
                        R.layout.custom_about_invitecode_message_contentone);
        materialDialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        });
        materialDialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        });
        materialDialog.show();

        Window window = materialDialog.mAlertDialog.getWindow();
        WindowManager m = this.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.34); // 高度设置为屏幕的0.6，根据实际情况调整
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.65，根据实际情况调整
        window.setAttributes(p);
    }

    private void onBtnSend() {

        if (TextUtils.isEmpty(str_mAdminName) || TextUtils.isEmpty(mSetPwd) || TextUtils.isEmpty(mPwdOk)) {

            ToastUtils.MyToast(this, "管理员或是密码不能为空~", 2000);
            return;

        }

        if (6 > StringUtils.getLength(mSetPwd, true)
                || 17 < StringUtils.getLength(mSetPwd, true)) {
            ToastUtils.showShortToastSafe(this,
                    "请输入6~16位的密码！");
            return;
        }

        if (!mPwdOk.equals(mSetPwd)) {
            ToastUtils.MyToast(this, "两次输入的密码不一致,请重新输入~", 2000);
            return;
        }

        if (!PhoneUtils.isPhoneNumber(getIntent().getStringExtra("phoneNum"))) {

            ToastUtils.MyToast(this, "输入的手机号码格式错误哦~", 2000);

            return;

        }

        mMobile_code = (int) ((Math.random() * 9 + 1) * 100000);
        final String content = new String("您的验证码是：" + mMobile_code + "。请不要把验证码泄露给其他人。");

        //网络请求获取短信验证码
        mMMessage = new Message();
        //该手机号已注册平台.可以找回密码,发送验证码
        synchronized (this) {
            LogUtils.d("第一个走-----------");
//POST的URL
            LogUtils.d("第二-----------");

            new Thread(new Runnable() {

                @Override
                public void run() {

                    HttpPost httppost = new HttpPost(Constants.SMSSUBMIT);
//建立HttpPost对象
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
//建立一个NameValuePair数组，用于存储欲传送的参数
                    params.add(new BasicNameValuePair("account", "cf_rch"));
                    params.add(new BasicNameValuePair("password", "327b65cb3e0e65e7e479dd84664b2259"));
                    params.add(new BasicNameValuePair("mobile", mPhoneNum));
                    params.add(new BasicNameValuePair("content", content));
//添加参数
                    try {
                        httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
//设置编码
                    HttpResponse response = null;
                    try {
                        response = new DefaultHttpClient().execute(httppost);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//发送Post,并返回一个HttpResponse对象
                    if (response.getStatusLine().getStatusCode() == 200) {//如果状态码为200,就是正常返回
                        try {
                            String SubmitResult = EntityUtils.toString(response.getEntity());


                            Document doc = null;
                            try {
                                doc = DocumentHelper.parseText(SubmitResult);
                            } catch (DocumentException e) {
                                e.printStackTrace();
                            }
                            Element root = doc.getRootElement();

                            String code = root.elementText("code");
                            LogUtils.d("code==" + code);
                            String msg = root.elementText("msg");
                            String smsid = root.elementText("smsid");


                            LogUtils.d("第三个走-----------");
                            if ("2".equals(code)) {
                                SPUtils.setSMScode(mContext, SPUtils
                                        .SMSCODE, String.valueOf
                                        (mMobile_code));
                                LogUtils.d("验证码是-----------" + content);
                                LogUtils.d("第四个走-----------");

                                mMMessage.what = 1;
                                handler.sendMessage(mMMessage);


                            } else if ("4085".equals(code)) {
                                mMMessage.what = 2;
                                handler.sendMessage(mMMessage);
                                LogUtils.d("第五个走-----------");


                            } else if ("0".equals(code)) {
                                mMMessage.what = 3;
                                handler.sendMessage(mMMessage);
                                LogUtils.d("第五个走-----------");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                }
            }).start();
        }
    }

    private int iType;
    private DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                return true;
            } else {
                return false;
            }
        }
    };

    private void onBtnNext() {
        //判空操作
        if (TextUtils.isEmpty(str_mAdminName) || TextUtils.isEmpty(mSetPwd) || TextUtils.isEmpty(mPwdOk) || TextUtils.isEmpty(mSms)) {
            ToastUtils.MyToast(this, "管理员,密码或验证码不能为空~", 2000);
            return;
        }

        if (!PhoneUtils.isPhoneNumber(getIntent().getStringExtra("phoneNum"))) {
            ToastUtils.MyToast(this, "手机号码格式不合格哦~", 2000);
            return;
        }

        if (!mPwdOk.equals(mSetPwd)) {
            ToastUtils.MyToast(this, "两次输入的密码不一致,请重新输入~", 2000);
            return;
        }

        if (!mSms.equals(mMobile_code+"")) {
            ToastUtils.MyToast(this, "验证码输入错误，请重新输入~", 2000);
            return;
        }

        if (TextUtils.isEmpty(mInvCode)) {
            iType = 1;
            Intent intent=new Intent(EnterpriseActivity.this,EnterpriseInfoActivity.class);
            Bundle b = new Bundle();

            b.putString("PhoneNum",mPhoneNum);
            b.putString("Name",str_mAdminName);
            b.putString("Pwd", mSettingPwd);
            com.apkfuns.logutils.LogUtils.tag("linge").d(mSetPwd);
            b.putString("InviteCode",mInvCode);
            b.putInt("iType",iType);
            intent.putExtras(b);
            startActivity(intent);
        } else {
            iType = 2;
            /*Dialog2.Builder builder = new Dialog2.Builder(mContext);
            builder.setTopImage(R.mipmap.message)
                    .setBackid(R.drawable.clean, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mDialog2.dismiss();
                        }
                    })
                    .setMessage(R.string.tv_invitecode)
                    .setPositiveButton(R.string.btn_queding, new DialogInterface.OnClickListener() {
                        @Override
                        // TODO: 2017/10/15
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //确定*/
                            OkHttpUtils.post()
                                    .url(Constants.ENTERPRISINVITECODE)
                                    .addParams("InviteCode", mInvCode)
                                    .build()
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onError(Call call, Exception e, int id) {
                                            ToastUtils.MyToast(mContext, "网络或服务器出错，请重新操作", Toast.LENGTH_SHORT);
                                        }

                                        @Override
                                        public void onResponse(String response, int id) {
                                            if (response.toString() != null){
                                                String ICString = response.toString();
                                                Log.d("yangyan", "onResponse: " + ICString);
                                                Gson gson = new Gson();
                                                mEnterpriseInviteCodeBean = gson.fromJson(ICString, EnterpriseInviteCodeBean.class);
                                                int code = mEnterpriseInviteCodeBean.getErrCode();
                                                if (code == 0){
                                                    ToastUtils.MyToast(mContext, "邀请码不存在，请重新输入", Toast.LENGTH_SHORT);
                                                    LogUtils.d(mEnterpriseInviteCodeBean.getErrMsg());
                                                }else if (code == 1){
                                                    mEnterShortName = mEnterpriseInviteCodeBean.getData().getEnterShortName();
                                                    Intent intent=new Intent(EnterpriseActivity.this,EnterpriseInfoActivity.class);
                                                    Bundle b = new Bundle();

                                                    b.putString("PhoneNum",mPhoneNum);
                                                    b.putString("Name",str_mAdminName);
                                                    b.putString("Pwd", mSettingPwd);
                                                    com.apkfuns.logutils.LogUtils.tag("linge").d(mSetPwd);
                                                    b.putString("InviteCode",mInvCode);
                                                    b.putInt("iType",iType);
                                                    b.putString("ShortName", mEnterShortName);

                                                    intent.putExtras(b);
                                                    startActivity(intent);

                                                }
                                            }
                                        }
                                    });
            /*            }
                    });
            mDialog2 = builder.create();
            mDialog2.setOnKeyListener(keylistener);
            mDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mDialog2.setCanceledOnTouchOutside(false);
            mDialog2.show();*/



        }

    }

    private void onBack() {
        finish();
    }
}


