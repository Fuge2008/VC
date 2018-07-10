package com.saas.saasuser.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
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
import com.saas.saasuser.bean.PersonRegisDetailBean;
import com.saas.saasuser.global.Constants;
import com.saas.saasuser.util.DESUitl;
import com.saas.saasuser.util.LogUtils;
import com.saas.saasuser.util.PhoneUtils;
import com.saas.saasuser.util.SPUtils;
import com.saas.saasuser.util.SharedPreferencesUtil;
import com.saas.saasuser.util.StringUtils;
import com.saas.saasuser.util.ToastUtils;
import com.saas.saasuser.view.MaterialDialog;
import com.saas.saasuser.view.SingleMaterialDialog;
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
import java.util.LinkedList;
import java.util.List;

import okhttp3.Call;


/**
 * 个人注册
 */

public class PersonActivity extends RegisterBaseActivity implements View.OnClickListener {

    private static final String TAG = "PersonActivity";
    private ImageView mBack;

    private EditText mSmsCode;
    private EditText mEtPersonName;
    private EditText mLoginPwd;
    private EditText mOkPwd;
    private EditText mInvitationCode;
    private Button mBtnRegister;
    private LinearLayout mMEn;
    private String mPhoneNume;
    private String mPwds;
    private String mPwdOk;
    private String mIcode;
    private String mSms;
    private Button mBtnSend;
    private SharedPreferencesUtil mUtil;
    private boolean isFirstClick = true;
    //Handler运行在主线程中(UI线程中)，  它与子线程可以通过Message对象来传递数据
    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
//                    mBtnSend.setClickable(false);
                    ToastUtils.MyToast(mContext, "验证码已发送,请尽快使用", Toast.LENGTH_SHORT);
                /*    mBtnSend.setBackgroundColor(Color.GRAY);
                    ToastUtils.MyToast(PersonActivity.this, "短信验证码发送成功,请尽快使用~", 3000);*/

                    new CountDownTimer(60000, 1000) {
                        @Override
                        public void onTick(long l) {
                            mBtnSend.setClickable(false);
                            //设置倒计时按钮背景,做倒计时操作
//                            mBtnSend.setBackgroundResource(R.drawable.send_tv_bg);
                            mBtnSend.setText(l / 1000 + "s");
//                            mBtnSend.setBackgroundColor(Color.TRANSPARENT);
                        }

                        @Override
                        public void onFinish() {
                            //设置重新发送时,按钮可点击
                            mBtnSend.setClickable(true);
//                            mBtnSend.setBackgroundResource(R.drawable.send_tv_bg);
                            mBtnSend.setText("重新发送");
                            mBtnSend.setBackgroundColor(Color.TRANSPARENT);
                            isFirstClick = true;

                        }
                    }.start();

                    break;
                case 2:
                    ToastUtils.MyToast(PersonActivity.this,
                            "今天验证码已超过5次~", 3000);
                    break;
                case 3:
                    ToastUtils.MyToast(PersonActivity.this, "验证码获取失败请重新获取~", 3000);
                    break;
            }
        }
    };
    private int MsgResult;
    private int mISMsgResult;
    private LinkedList<BasicNameValuePair> params;
    private OkHttpUtils httpClient;
    private Message mMMessage;
    private Context mContext;
    private Dialog2 mDialog2;
    //禁止用户通过按返回键消失dialog,只有通过dialog的按钮才能操作
    private DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                return true;
            } else {
                return false;
            }
        }
    };
    private String mPhone;
    private String mPersonName;
    private PersonRegisDetailBean mPersonRegisDetailBean;
    private int mMobile_code;
    private String mSettingPwd;
    private ImageView mPercorporatecode;
//    private CheckBox mPerchkselector;
    private MaterialDialog mMaterialDialog;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_person;
    }

    @Override
    protected void init() {
        super.init();
        MyApplication.getInstance().addActivity(this);
        mContext = this;
        initView();
        initListener();
    }

    //按钮点击事件的监听
    private void initListener() {

        mBack.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);
        mBtnSend.setOnClickListener(this);
        mPercorporatecode.setOnClickListener(this);

    }

    //初始化控件
    private void initView() {
        //短信验证码
        mSmsCode = (EditText) findViewById(R.id.et2_sms2_code2);
        //个人名称
        mEtPersonName = (EditText) findViewById(R.id.et_person_phones);
        //登录密码
        mLoginPwd = (EditText) findViewById(R.id.edit_loginpwd_person);
        //确认密码
        mOkPwd = (EditText) findViewById(R.id.edit_ok_pwd_person);
        //邀请码
        mInvitationCode = (EditText) findViewById(R.id.et_person_code);
        //注册
        mBtnRegister = (Button) findViewById(R.id.btnnext_person_register);
        //发送按钮
        mBtnSend = (Button) findViewById(R.id.btn2_sms2_send2);
        mBack = (ImageView) findViewById(R.id.iv_person_back);
        mPercorporatecode = (ImageView) findViewById(R.id.per_corporatecode);
//        mPerchkselector = (CheckBox) findViewById(R.id.per_chk_selector);

        mBtnSend.setBackgroundColor(Color.TRANSPARENT);
        if (getIntent() != null) {
            Intent intent = getIntent();
            mPhone = intent.getStringExtra("PhoneNum");
//            mEtPersonName.setText(mPhone);
        }
    }

    //定义点击事件的方法
    @Override
    public void onClick(View v) {

        LogUtils.d(" 被修改后还未加密的密码===" + mPwds + ", ====mConfirmPwd==" + mPwdOk);
        //对输入的新密码加密提交到服务器
        mSettingPwd = DESUitl.tran2cypher(mPwds);
        LogUtils.d(" 被修改后加密的后密码：" + mSettingPwd);

//        mPhoneNume = mEtPersonName.getText().toString().trim();
        mPersonName = mEtPersonName.getText().toString().trim();
        mPwds = this.mLoginPwd.getText().toString().trim();
        mPwdOk = mOkPwd.getText().toString().trim();
        mIcode = this.mInvitationCode.getText().toString().trim();
        mSms = this.mSmsCode.getText().toString().trim();
        LogUtils.d("个人注册验证码==" + mSms);
        switch (v.getId()) {

            case R.id.btn2_sms2_send2:
                if (isFirstClick) {
                    onBtnSend();
                    isFirstClick = false;
                }
                break;

            case R.id.iv_person_back:
                onBack();
                break;

            case R.id.btnnext_person_register:
//                if (mPerchkselector.isChecked() && ClickUtils.isFastClick()) {
                    onBtnRegister();
//                } else {
//                    ToastUtils.MyToast(mContext, "请点击确定VV服务条款", Toast.LENGTH_SHORT);
//                }
                break;

            case R.id.per_corporatecode:
                onCorporateCodeShow();
                break;
        }

    }

    private void onBtnSend() {
        if (TextUtils.isEmpty(mPersonName) || TextUtils.isEmpty(mPwds) || TextUtils.isEmpty(mPwdOk)) {
            ToastUtils.MyToast(this, "账号或密码不能为空~", 2000);
            return;
        }

        if (6 > StringUtils.getLength(mPwds, true)
                || 17 < StringUtils.getLength(mPwds, true)) {
            ToastUtils.showShortToastSafe(this,
                    "请输入6~16位的密码！");
            return;
        }

        if (!PhoneUtils.isPhoneNumber(mPhone)) {
            ToastUtils.MyToast(this, "您输入的手机号码不合格哦~", 2000);
            return;
        }

        mMMessage = new Message();
        //该手机号已注册平台.可以找回密码,发送验证码
        synchronized (this) {
            LogUtils.d("第一个走-----------");
            mMobile_code = (int) ((Math.random() * 9 + 1) * 100000);
            final String content = new String("您的验证码是：" + mMobile_code + "。请不要把验证码泄露给其他人。");
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

    private void onBtnRegister() {
        if (TextUtils.isEmpty(mPersonName) || TextUtils.isEmpty(mPwds) || TextUtils.isEmpty(mPwdOk) || TextUtils.isEmpty(mSms) || TextUtils.isEmpty(mIcode)) {
            ToastUtils.MyToast(this, "不能为空~", 2000);
            return;
        }
        if (!PhoneUtils.isPhoneNumber(mPhone)) {
            ToastUtils.MyToast(this, "您输入的手机号码不合格哦~", 2000);
            return;
        }

        if (!mPwdOk.equals(mPwds)) {
            ToastUtils.MyToast(this, "两次输入的密码不一致哦~", 2000);
            return;
        }
        if (!mSms.equals(mMobile_code + "")) {
            ToastUtils.MyToast(this, "验证码输入错误，请重新输入", Toast.LENGTH_SHORT);
            return;
        }
        //网络请求
        OkHttpUtils.get()
                .url(Constants.PERSONDETAIL)
                .addParams("phone", mPhone)
                .addParams("enterCode", mIcode)
                .addParams("name", mPersonName)
                .addParams("desPwd", mSettingPwd)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.MyToast(mContext, "网络或服务器出错,请重新操作~", Toast.LENGTH_SHORT);
                        Log.d("yangy", "onError: " + e.getMessage());
                        Log.d("yangy", "onError: " + e.getCause());
                        Log.d("yangy", "onError: " + mPhone);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response.toString() != null) {
                            String string = response.toString();
                            Log.d("yangy", "onResponse: " + string);
                            Gson gson = new Gson();
                            mPersonRegisDetailBean = gson.fromJson(string, PersonRegisDetailBean.class);
                            int errCode = mPersonRegisDetailBean.getErrCode();
                            if (errCode == 0) {
                                ToastUtils.MyToast(mContext, mPersonRegisDetailBean.getErrMsg() + "请重新输入~", 2000);
                                LogUtils.d(mPersonRegisDetailBean.getErrMsg());
                            } else if (errCode == 1) {
                                //保存企业信息
                                String enterShortName = mPersonRegisDetailBean.getData().getEnterShortName();
                                SPUtils.setCompanyID(mContext, "EnterpriseName", enterShortName);
//                                mUtil.setPassWord(mPwds);
                                //
                               /* ToastUtils.MyToast(mContext, "注册成功~", 2000);

                                //界面跳转
                                Intent intent = new Intent(mContext, LoginActivity.class);
                                startActivity(intent);*/
                                successShowDialogs();

                            }
                        }
                    }
                });
    }

    private void successShowDialogs() {
        final SingleMaterialDialog singleMaterialDialog = new SingleMaterialDialog(this)
                .setContentView( R.layout.custom_per_success_message_contentone)
                ;
        singleMaterialDialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            }
        });
        singleMaterialDialog.show();

        Window window = singleMaterialDialog.mAlertDialog.getWindow();
        WindowManager m = this.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.34); // 高度设置为屏幕的0.6，根据实际情况调整
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.65，根据实际情况调整
        window.setAttributes(p);
    }

    private void onBack() {
        finish();
    }

    private void onCorporateCodeShow() {
        /*  .setContentIV(
                  R.drawable.gantanhao)*/
        mMaterialDialog = new MaterialDialog(this)
                      /*  .setContentIV(
                                R.drawable.gantanhao)*/
                .setContentView(
                        R.layout.custom_about_encode_message_contentone);
        mMaterialDialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
            }
        });
        mMaterialDialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
            }
        });
        mMaterialDialog.show();

        Window window = mMaterialDialog.mAlertDialog.getWindow();
        WindowManager m = this.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.3); // 高度设置为屏幕的0.6，根据实际情况调整
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.65，根据实际情况调整
        window.setAttributes(p);
    }

   /* @Override
    protected void onDestroy() {
        super.onDestroy();
        mSuccessMaterialDialog.dismiss();
        mMaterialDialog.dismiss();
    }*/
}
