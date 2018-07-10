package com.saas.saasuser.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.saas.saasuser.R;
import com.saas.saasuser.activity.ForgetPasswordActivity;
import com.saas.saasuser.activity.LoginActivity;
import com.saas.saasuser.activity.PersonActivity;
import com.saas.saasuser.activity.RegisterActivity;
import com.saas.saasuser.bean.PersonReapplyOrGiveUpBean;
import com.saas.saasuser.bean.PersonRegisBean;
import com.saas.saasuser.global.Constants;
import com.saas.saasuser.slidecode.SwipeCaptchaView;
import com.saas.saasuser.util.LogUtils;
import com.saas.saasuser.util.PhoneUtils;
import com.saas.saasuser.util.RegisterClickUtils;
import com.saas.saasuser.util.ToastUtils;
import com.saas.saasuser.view.MaterialDialog;
import com.saas.saasuser.view.MaterialDialogThree;
import com.saas.saasuser.view.dialog.Dialog2;
import com.saas.saasuser.view.dialog.DialogView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

import static com.saas.saasuser.R.id.swipeCaptchaView;
import static com.saas.saasuser.application.MyApplication.mContext;


/**
 * Created by tyh on 2017/9/7.
 */

public class PeFragment extends Fragment implements View.OnClickListener {

    private View mView;
    private String realCode;
    private String mPhone;
    private String mCode;
    private DialogView mDialogView;
    private LinearLayout mPerlinear;
    private EditText mSmsCode;
    private EditText mM2;
    private EditText mEtPhoneName;
    private EditText mLoginPwd;
    private EditText mOkPwd;
    private EditText mInvitationCode;
    private Button mBtnRegister;
    private ImageView mIvCode;
    private EditText mEtPhone;
    private EditText mEtCode;
    private Button mBtnNext;
    private LinearLayout mMEn;
    private String mPhoneNume;
    private String mPwds;
    private String mPwdOk;
    private String mIcode;
    private String mSms;
    private Button mBtnSend;
    private Dialog2 mDialog2;
    private String mEnterShortName;
    private SwipeCaptchaView mSwipeCaptchaView;
    private SeekBar mSeekBar;
    //    private boolean isPerRegFirst = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_person, container, false);
        initView();
        initLister();
        return mView;
    }

    //点击监听
    private void initLister() {
        mSwipeCaptchaView.setOnCaptchaMatchCallback(new SwipeCaptchaView.OnCaptchaMatchCallback() {
            @Override
            public void matchSuccess(SwipeCaptchaView swipeCaptchaView) {
                Toast.makeText(mContext, "恭喜你啊 验证成功 可以搞事情了", Toast.LENGTH_SHORT).show();
                //swipeCaptcha.createCaptcha();
                mSeekBar.setEnabled(false);
                doOnclick();

            }

            @Override
            public void matchFailed(SwipeCaptchaView swipeCaptchaView) {
               /* Log.d("zxt", "matchFailed() called with: swipeCaptchaView = [" + swipeCaptchaView + "]");
                Toast.makeText(getContext(), "你有80%的可能是机器人，现在走还来得及", Toast.LENGTH_SHORT).show();*/
                swipeCaptchaView.resetCaptcha();
                mSeekBar.setProgress(0);
                mSwipeCaptchaView.createCaptcha();
                mSeekBar.setEnabled(true);
                mSeekBar.setProgress(0);

                return;
            }
        });
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSwipeCaptchaView.setCurrentSwipeValue(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //随便放这里是因为控件
                mSeekBar.setMax(mSwipeCaptchaView.getMaxSwipeValue());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("zxt", "onStopTrackingTouch() called with: seekBar = [" + seekBar + "]");
                mSwipeCaptchaView.matchCaptcha();
            }
        });

        //测试从网络加载图片是否ok
       /* Glide.with(getContext())
//                .load("http://www.investide.cn/data/edata/image/20151201/20151201180507_281.jpg")
                .load(R.drawable.pic11)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mSwipeCaptchaView.setImageBitmap(resource);
                        mSwipeCaptchaView.createCaptcha();
                    }
                });*/

       /* Glide.with(getContext())
                .asBitmap()
                .load(R.drawable.pic11)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        mSwipeCaptchaView.setImageBitmap(resource);
                        mSwipeCaptchaView.createCaptcha();
                    }
                });*/
        Glide.with(getContext())
//               .load("http://www.investide.cn/data/edata/image/20151201/20151201180507_281.jpg ")
                .load(R.drawable.pic11)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mSwipeCaptchaView.setImageBitmap(resource);
                        mSwipeCaptchaView.createCaptcha();
                    }
                });

    }

    private void doOnclick() {
        mBtnNext.setOnClickListener(this);
       /* mIvCode.setOnClickListener(this);*/
    }


    private void initView() {
        //个人注册第一个页面

       /* //图片验证码图片
        mIvCode = (ImageView) mView.findViewById(R.id.iv_code);*/

        //手机号码
        mEtPhone = (EditText) mView.findViewById(R.id.et_phone_register_person);

        mSwipeCaptchaView = (SwipeCaptchaView) mView.findViewById(swipeCaptchaView);
        mSeekBar = (SeekBar) mView.findViewById(R.id.dragBar);
       /* //图片验证码
        mEtCode = (EditText) mView.findViewById(R.id.edittext_phoneCode_person);*/

        //下一步
        mBtnNext = (Button) mView.findViewById(R.id.btn_reg);
       /* getCode();*/
    }

    @Override
    public void onClick(View view) {
        mPhone = mEtPhone.getText().toString().trim();
//        mCode = mEtCode.getText().toString().trim();

        switch (view.getId()) {

            case R.id.btn_reg:  //注册按钮

                if (RegisterClickUtils.isFastClick()) {
                    onBtnNext();
                }
                break;

          /*  case R.id.iv_code:     //图片验证码
                getCode();
                break;*/

        }
    }

  /*  private void getCode() {
        mIvCode.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode();
    }*/

    private void onBtnNext() {
        //判断
        if (TextUtils.isEmpty(mPhone) /*|| TextUtils.isEmpty(mCode)*/) {

            ToastUtils.MyToast(getActivity(), "手机号码不能为空或为点击滑动验证", 3000);
            return;
        }
        if (!PhoneUtils.isPhoneNumber(mPhone)) {

            ToastUtils.MyToast(getActivity(), "手机号码格式错误哦~", 3000);
            return;
        }
       /* if (!mCode.equalsIgnoreCase(realCode)) {   //图片验证码不区分大小写

            Toast.makeText(getActivity(), "验证码错误!", Toast.LENGTH_SHORT).show();
            return;
        }*/

        //网络请求
        OkHttpUtils
                .post()
                .url(Constants.PERSON)  //个人注册URL
                .addParams("PhoneNum", mPhone)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
//                        LogUtils.debug("onError ===" + e);
                        LogUtils.d("onError ===" + e);
                        ToastUtils.MyToast(getContext(), "网络无法连接,请查看后重试~", Toast.LENGTH_SHORT);

                    }

                    @Override
                    public void onResponse(String response, int id) {

                        LogUtils.d("PeFragment" + response.toString());
                        String register = response.toString();
                        Log.d("yang", "onResponse: " + register);
                        Gson gson = new Gson();    //gson 解析bean类
                        PersonRegisBean personRegisBean = gson.fromJson(register, PersonRegisBean.class);
                        int errCode = personRegisBean.getErrCode();
                        if (personRegisBean.getData() != null) {
                            mEnterShortName = personRegisBean.getData().getEnterShortName();
                        }

                        if (errCode == 0) {

                            ToastUtils.MyToast(getActivity(), "操作失败,请重新操作~", 3000);
                        } else if (errCode == 1) {

                            onNotReg();  //用户已注册时,弹出自定义dialog
                            ToastUtils.MyToast(getActivity(), "已注册到平台，跳转到已注册到平台页面", 3000);
                        } else if (errCode == 2) {

                            onRegistered();  //已登记,未审核
                            ToastUtils.MyToast(getActivity(), "已登记，跳转到已登记页面", 3000);

                        } else if (errCode == 3) {
                            Intent intent = new Intent(getActivity(), PersonActivity.class);
                            Bundle b = new Bundle();
                            b.putString("PhoneNum", mPhone);
                            intent.putExtras(b);
                            startActivity(intent);

                            ToastUtils.MyToast(getActivity(), "未登记，第一次注册页面", Toast.LENGTH_SHORT);

                        }
                    }
                });

    }

    //已登记,未审核自定义dialog
    private void onRegistered() {
        // TODO: 2017/11/15 新的
        View view = LayoutInflater.from(getContext()).inflate(R.layout.custom_per_check_message_contentone, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_per_check_content);
        textView.setText("您的申请尚未通过" + mEnterShortName + "\n 企业审核，请与企业负责人联系");
        final MaterialDialog materialDialog = new MaterialDialog(getContext())
                .setContentIV(R.mipmap.exclamatorymark)
               /* .setContentView(
                        R.layout.custom_per_check_message_contentone);*/
               .setContentView(view);
        materialDialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        });
        materialDialog.setNegativeButton("放弃申请", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //放弃申请
                OkHttpUtils.post()
                        .url(Constants.PERSONGIVEUP)
                        .addParams("phone", mPhone)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                ToastUtils.MyToast(mContext, "网络或服务器出错,请重新操作~", Toast.LENGTH_SHORT);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                if (response.toString() != null) {
                                    String giveUp = response.toString();
                                    Gson gson = new Gson();
                                    PersonReapplyOrGiveUpBean reapplyOrGiveUpBean = gson.fromJson(giveUp, PersonReapplyOrGiveUpBean.class);
                                    int errCode = reapplyOrGiveUpBean.getErrCode();
                                    if (errCode == 0) {
                                        String errMsg = reapplyOrGiveUpBean.getErrMsg();
                                        LogUtils.d(errMsg);
                                    } else if (errCode == 1) {
                                        Intent intent = new Intent(getContext(), RegisterActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            }
                        });
            }
        });
        materialDialog.show();

        materialDialog.show();

        Window window = materialDialog.mAlertDialog.getWindow();
        WindowManager m = getActivity().getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.36); // 高度设置为屏幕的0.6，根据实际情况调整
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.65，根据实际情况调整
        window.setAttributes(p);

       /* // TODO: 2017/11/15 旧的
        Dialog2.Builder builder = new Dialog2.Builder(getContext());
        builder.setTopImage(R.mipmap.message)
                .setBackid(R.drawable.clean, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDialog2.dismiss();
                    }
                })
                .setMessage(R.string.tv_shenqing)
                .setPositiveButton(R.string.btn_queding, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDialog2.dismiss();
                    }
                })
                .setNegativeButton(R.string.btn_abandon, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //放弃申请
                        OkHttpUtils.post()
                                .url(Constants.PERSONGIVEUP)
                                .addParams("phone", mPhone)
                                .build()
                                .execute(new StringCallback() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        ToastUtils.MyToast(mContext, "网络或服务器出错,请重新操作~", Toast.LENGTH_SHORT);
                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        if (response.toString() != null) {
                                            String giveUp = response.toString();
                                            Gson gson = new Gson();
                                            PersonReapplyOrGiveUpBean reapplyOrGiveUpBean = gson.fromJson(giveUp, PersonReapplyOrGiveUpBean.class);
                                            int errCode = reapplyOrGiveUpBean.getErrCode();
                                            if (errCode == 0) {
                                                String errMsg = reapplyOrGiveUpBean.getErrMsg();
                                                LogUtils.d(errMsg);
                                            } else if (errCode == 1) {
                                                Intent intent = new Intent(getContext(), RegisterActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    }
                                });

                    }
                });
        mDialog2 = builder.create();
        mDialog2.setOnKeyListener(keylistener);
        mDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog2.setCanceledOnTouchOutside(false);
        mDialog2.show();*/

    }

    //自定义dialog,调用接口用户已注册时弹出dialog
    private void onNotReg() {
        // TODO: 2017/11/14 新的
        View view = LayoutInflater.from(getContext()).inflate(R.layout.custom_per_message_contentone, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_per_apply_content);
        textView.setText("您的申请已通过"+mEnterShortName +"\n审核通过，可登录平台进行相关操作");
        final MaterialDialogThree alert = new MaterialDialogThree(getContext())
                .setContentIV(R.mipmap.exclamatorymark)
//                .setContentView(R.layout.custom_per_message_contentone);
                .setContentView(view);
        alert.setPositiveButton("设置密码", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击跳转到设置密码页面
                Intent intent = new Intent(getActivity(), ForgetPasswordActivity.class);
                intent.putExtra("PhoneNum", mPhone);
                startActivity(intent);
            }
        });
        alert.setNegativeButton("立即登录", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击跳转到登录页面
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        alert.setIndeterminateButton("放弃申请", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/10/23 待填写完整
                OkHttpUtils.post()
                        .url(Constants.PERSONREAPPLY)
                        .addParams("Phone", mPhone)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                ToastUtils.MyToast(mContext, "网络或服务器出错,请重新操作~", Toast.LENGTH_SHORT);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                if (response.toString() != null){
                                    String reapllyString = response.toString();
                                    Gson gson = new Gson();
                                    PersonReapplyOrGiveUpBean reapplyOrGiveUpBean = gson.fromJson(reapllyString, PersonReapplyOrGiveUpBean.class);
                                    if (reapplyOrGiveUpBean.getErrCode() == -1){
                                        ToastUtils.MyToast(mContext, "该手机已经关联平台企业不可重新注册", Toast.LENGTH_SHORT);
                                    }else if (reapplyOrGiveUpBean.getErrCode() == 0){
                                        LogUtils.d(reapplyOrGiveUpBean.getErrMsg());
                                    }else if (reapplyOrGiveUpBean.getErrCode() == 1){
                                        Intent intent = new Intent(getContext(), RegisterActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            }
                        });
            }
        });
        alert.show();

        Window window = alert.mAlertDialog.getWindow();
        WindowManager m = getActivity().getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.4); // 高度设置为屏幕的0.6，根据实际情况调整
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.65，根据实际情况调整
        window.setAttributes(p);

        /*// TODO: 2017/11/14 以前的
        DialogView.Builder builder = new DialogView.Builder(getActivity());
        builder.setTopImage(R.mipmap.message)   //设置图标
                .setMessage(R.string.tv_reg_texts) //设置内容
                .setBackid(R.drawable.clean, new DialogInterface.OnClickListener() { //设置监听dialog右上角图标点击事件
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDialogView.dismiss();
                    }
                })
                .setNegativeButton(R.string.set_pwd, new DialogInterface.OnClickListener() { //底部设置密码按钮
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //点击跳转到设置密码页面
                        Intent intent = new Intent(getActivity(), ForgetPasswordActivity.class);
                        intent.putExtra("PhoneNum", mPhone);
                        startActivity(intent);
                    }
                })
                .setPositiveButton(R.string.dialog_login, new DialogInterface.OnClickListener() { //底部登录按钮
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //点击跳转到登录页面
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    }
                })
                .setGiveUp(R.string.btn_abandon, new DialogInterface.OnClickListener() {//底部放弃申请按钮
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // TODO: 2017/10/23 待填写完整
                        OkHttpUtils.post()
                                .url(Constants.PERSONREAPPLY)
                                .addParams("Phone", mPhone)
                                .build()
                                .execute(new StringCallback() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        ToastUtils.MyToast(mContext, "网络或服务器出错,请重新操作~", Toast.LENGTH_SHORT);
                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        if (response.toString() != null){
                                            String reapllyString = response.toString();
                                            Gson gson = new Gson();
                                            PersonReapplyOrGiveUpBean reapplyOrGiveUpBean = gson.fromJson(reapllyString, PersonReapplyOrGiveUpBean.class);
                                            if (reapplyOrGiveUpBean.getErrCode() == -1){
                                                ToastUtils.MyToast(mContext, "该手机已经关联平台企业不可重新注册", Toast.LENGTH_SHORT);
                                            }else if (reapplyOrGiveUpBean.getErrCode() == 0){
                                                LogUtils.d(reapplyOrGiveUpBean.getErrMsg());
                                            }else if (reapplyOrGiveUpBean.getErrCode() == 1){
                                                Intent intent = new Intent(getContext(), RegisterActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    }
                                });

                    }
                });

        mDialogView = builder.create();
        mDialogView.setOnKeyListener(keylistener);
        mDialogView.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialogView.setCanceledOnTouchOutside(false);
        mDialogView.show();*/

    }

    /*//禁止用户通过按返回键消失dialog,只有通过dialog的按钮才能操作
    private DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                return true;
            } else {
                return false;
            }
        }
    };*/
}
