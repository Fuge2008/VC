package com.saas.saasuser.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.saas.saasuser.R;
import com.saas.saasuser.application.MyApplication;
import com.saas.saasuser.bean.PasswordSettingBean;
import com.saas.saasuser.global.Constants;
import com.saas.saasuser.util.DESUitl;
import com.saas.saasuser.util.LogUtils;
import com.saas.saasuser.util.PhoneUtils;
import com.saas.saasuser.util.StringUtils;
import com.saas.saasuser.util.ToastUtils;
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
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static com.saas.saasuser.application.MyApplication.mContext;


/**
 * Created by tyh on 2017/9/15.
 */

public class ForgetPasswordActivity extends RegisterBaseActivity implements View.OnClickListener {


    private ImageView mBack;
    private EditText mEtPhone;
    private EditText mEtPwd;
    private EditText mPwdOk;
    private EditText mSmsCode;
    private Button mBtnSend;
    private Button mBtnModify;
    private String mPhone;
    private String mPwd;
    private String mConfirmPwd;
    private String mCode;
    private String mSettingPwd;
    private SharedPreferences sp = null;
    private boolean mSendResult;
    private int MsgResult;
    private Message mMMessage;
    private boolean isO;
    private int mMobile_code;
    private String mPhoneNum;
    private boolean isFirstForgetClick = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        sp = this.getSharedPreferences("userInfo", MODE_PRIVATE);
        if (getIntent() != null){
            mPhoneNum = getIntent().getStringExtra("PhoneNum");
        }
        initView();
        initListener();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_forgetpwd;
    }

    //点击监听
    private void initListener() {
        mBack.setOnClickListener(this);
        mBtnModify.setOnClickListener(this);//修改
        mBtnSend.setOnClickListener(this);//发送验证码
    }

    //控件初始化
    private void initView() {

        mBack = (ImageView) findViewById(R.id.iv_back_forget); //标题按钮
        mEtPhone = (EditText) findViewById(R.id.et_phone_pwd); //手机号码
        mEtPwd = (EditText) findViewById(R.id.et_setpwd);       //密码
        mPwdOk = (EditText) findViewById(R.id.et_ok_pwd);       //确认密码
        mSmsCode = (EditText) findViewById(R.id.edittext_phoneCode);  //短信验证码
        mBtnSend = (Button) findViewById(R.id.btn_send_pwd);        //发送
        mBtnModify = (Button) findViewById(R.id.btn_modify);        //修改

        mBtnSend.setBackgroundColor(Color.TRANSPARENT);
        mEtPhone.setText(mPhoneNum);

    }

    @Override
    public void onClick(View v) {
        LogUtils.d(" 被修改后还未加密的密码===" + mPwd + "====mConfirmPwd==" + mConfirmPwd);
        //对输入的新密码加密提交到服务器
        mSettingPwd = DESUitl.tran2cypher(mPwd);
        LogUtils.d(" 被修改后加密的后密码：" + mSettingPwd);

        switch (v.getId()) {
            case R.id.iv_back_forget:  //标题栏返回键
                onBack();

                break;

            case R.id.btn_send_pwd:   //发送
                if (isFirstForgetClick) {
                    onBtnSend();
                    isFirstForgetClick = false;
                }
                break;
            case R.id.btn_modify:      //修改
                onModify();
                break;
        }
    }

    //点击发送获取验证码
    private void onBtnSend() {

        //获取输入的内容,并去掉两端的空格
        mPhone = mEtPhone.getText().toString().trim();//被修改电话号码
        mPwd = mEtPwd.getText().toString().trim();//新密码

        mConfirmPwd = mPwdOk.getText().toString().trim();//确认新密码

        if (TextUtils.isEmpty(mPhone) || TextUtils.isEmpty(mPwd) || TextUtils.isEmpty(mConfirmPwd)) {

            ToastUtils.MyToast(this, "手机号码和密码不能为空哦~", 3000);
            return;
        }

        if (6 > StringUtils.getLength(mPwd, true)
                || 17 < StringUtils.getLength(mPwd, true)) {
            ToastUtils.showShortToastSafe(this,
                    "请输入6~16位的密码！");
            return;
        }

        if (!PhoneUtils.isPhoneNumber(mPhone)) {

            ToastUtils.MyToast(this, "手机号码格式错误哦~", 3000);
            return;
        }
        if (!mConfirmPwd.equals(mPwd)) {

            ToastUtils.MyToast(this, "两次输入的密码不一致哦~", 3000);

            return;
        }

        // TODO: 2017/10/21 手机验证码的网络请求,SDK就是这样无法更改
        mMMessage = new Message();
        synchronized (this) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpPost httppost = new HttpPost(Constants.REGISTER);
//建立HttpPost对象
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
//建立一个NameValuePair数组，用于存储欲传送的参数
                    params.add(new BasicNameValuePair("PhoneNum", mPhone));
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
                            JSONObject jsonObject = new JSONObject(SubmitResult);
                            String mErrCode = jsonObject.getString("ErrCode");


                            if (mErrCode.equals("0")) {
                                mMMessage.what = 0;
                                handler.sendMessage(mMMessage);

                                LogUtils.d("mErrCode==========" + mErrCode);
//									ToastUtils.MyToast(ForgetPasswordActivity.this, "手机号码未注册，无需发送验证码~", 3000);

                            } else if (mErrCode.equals("1")) {
                                //该手机号已注册平台.可以找回密码,发送验证码
                                synchronized (this) {
                                    mMobile_code = (int) ((Math.random() * 9 + 1) * 100000);
                                    final String content = new String("您的验证码是：" + mMobile_code + "。请不要把验证码泄露给其他人。");
//POST的URL

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            HttpPost httppost = new HttpPost(Constants.SMSSUBMIT);
//建立HttpPost对象
                                            List<NameValuePair> params = new ArrayList<NameValuePair>();
//建立一个NameValuePair数组，用于存储欲传送的参数
                                            params.add(new BasicNameValuePair("account", "cf_rch"));
                                            params.add(new BasicNameValuePair("password", "327b65cb3e0e65e7e479dd84664b2259"));
                                            params.add(new BasicNameValuePair("mobile", mPhone));
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
                                                        MsgResult = 1;
                                                        SharedPreferences.Editor editor = sp.edit();
                                                        editor.putString("mobileCode", String.valueOf(mMobile_code));
                                                        editor.commit();
                                                        LogUtils.d("验证码是-----------" + content);
                                                        LogUtils.d("第四个走-----------");

                                                        mMMessage.what = 1;
                                                        handler.sendMessage(mMMessage);


                                                    } else if ("4085".equals(code)) {
                                                        mMMessage.what = 2;
                                                        handler.sendMessage(mMMessage);
                                                        LogUtils.d("第五个走-----------");


//                                        LogUtils.debug("短信提交成功---第四个走");
                                                    } else if ("0".equals(code)) {
                                                        mMMessage.what = 3;
                                                        handler.sendMessage(mMMessage);
                                                        LogUtils.d("第五个走-----------");
//                                        ToastUtils.MyToast(ForgetPasswordActivity.this, "获取失败,或今天已达上限~", 3000);
                                                    }
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                            }

                                        }
                                    }).start();
                                }
                            }
                        } catch (Exception E) {
                        }
                    }
                }
            }).start();
        }


    }


    //Handler运行在主线程中(UI线程中)，  它与子线程可以通过Message对象来传递数据
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ToastUtils.MyToast(ForgetPasswordActivity.this, "手机号码未注册，无需发送验证码~", 3000);
                    break;
                case 1:

                    ToastUtils.MyToast(mContext, "验证码已发送,请尽快使用", Toast.LENGTH_SHORT);
                  /*  mBtnSend.setBackgroundColor(Color.GRAY);
                    ToastUtils.MyToast(ForgetPasswordActivity.this, "短信验证码发送成功,请尽快使用~", 3000);*/

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
                            isFirstForgetClick = true;

                        }
                    }.start();

                    break;
                case 2:
                    ToastUtils.MyToast(ForgetPasswordActivity.this,
                            "今天验证码已超过5次~", 3000);
                    break;
                case 3:
                    ToastUtils.MyToast(ForgetPasswordActivity.this, "验证码获取失败~", 3000);
                    break;
            }
        }
    };

    //点击按钮调用接口,完成对密码的修改
    private void onModify() {

        mCode = mSmsCode.getText().toString().trim();//短信验证码

        //对输入框中的内容进行判断
        if (TextUtils.isEmpty(mPhone) || TextUtils.isEmpty(mPwd) || TextUtils.isEmpty(mConfirmPwd) || TextUtils.isEmpty(mCode)) {
            ToastUtils.MyToast(this, "手机号码，密码或短信验证码不能为空", 3000);

        } else if (!PhoneUtils.isPhoneNumber(mPhone)) {
            ToastUtils.MyToast(this, "手机号码格式错误哦~", 3000);

        } else if (!mConfirmPwd.equals(mPwd)) {
            ToastUtils.MyToast(this, "两次输入的密码不一致哦~", 3000);

        } else if (!mCode.equals(mMobile_code+"")) {
            ToastUtils.MyToast(this, "验证码输入错误，请重新输入~", 3000);
        } else {
            SharedPreferences sp = this.getSharedPreferences("userInfo", MODE_PRIVATE);
            LogUtils.d("验证码:" + sp.getString("mobileCode", ""));
            //网络请求修改密码
            OkHttpUtils
                    .post()
                    .url(Constants.SETTINGPWD)      //修改密码url
                    .addParams("loginName", mPhone) //手机号码参数
                    .addParams("pwd", mSettingPwd)   //密码参数
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            LogUtils.d("onError ===" + e);
                        }

                        //// TODO: 2017/10/21 bean类还没有完全的设置好
                        @Override
                        public void onResponse(String response, int id) {
                            if (response.toString() != null) {
                                String settingPwd = response.toString();
                                Gson gson = new Gson();   //gson解析bean类
                                PasswordSettingBean settingBean = gson.fromJson(settingPwd, PasswordSettingBean.class);
                                int errCode = settingBean.getErrCode();

                                if (errCode == 1) {

                                    Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    ToastUtils.MyToast(ForgetPasswordActivity.this, "修改成功", 3000);

                                } else if (errCode == 0) {
                                    ToastUtils.MyToast(ForgetPasswordActivity.this, "修改失败，你还未注册", 3000);
                                }
                            }
                        }
                    });
        }
    }

    //标题栏回退按钮
    private void onBack() {
        finish();
    }
}