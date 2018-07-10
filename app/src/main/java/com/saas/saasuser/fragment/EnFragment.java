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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.saas.saasuser.R;
import com.saas.saasuser.activity.EnterpriseActivity;
import com.saas.saasuser.activity.LoginActivity;
import com.saas.saasuser.activity.RegisterActivity;
import com.saas.saasuser.bean.EnterpriseRegisterBean;
import com.saas.saasuser.bean.PersonReapplyOrGiveUpBean;
import com.saas.saasuser.global.Constants;
import com.saas.saasuser.slidecode.SwipeCaptchaView;
import com.saas.saasuser.util.LogUtils;
import com.saas.saasuser.util.NetUtils;
import com.saas.saasuser.util.PhoneUtils;
import com.saas.saasuser.util.RegisterClickUtils;
import com.saas.saasuser.util.ToastUtils;
import com.saas.saasuser.view.MaterialDialog;
import com.saas.saasuser.view.SingleMaterialDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

import static com.saas.saasuser.R.id.swipeCaptchaView;
import static com.saas.saasuser.application.MyApplication.mContext;


/**
 * Created by tyh on 2017/9/7.
 */

public class EnFragment extends Fragment implements View.OnClickListener {
    private View mView;
    private Button mBtn;
    private EditText mEditPhone;
    private ImageView mIvCode;
    private String realCode;
    private EditText mPhoneCode;
    private String mPhone;
    private String mImagCode;
//    private AlertDialog mDialog;
    private MaterialDialog mMDialog;
    private SingleMaterialDialog mSingleMaterialDialog;
    private String mEnterShortName;
    private SwipeCaptchaView mSwipeCaptchaView;
    private SeekBar mSeekBar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_enterprise, null);
        initView();
//        initLister();
        return mView;
    }

    //测试看看性能
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initLister();
    }

    //点击事件监听
    private void initLister() {
//        mIvCode.setOnClickListener(this);
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
        /*Glide.with(getContext())
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
     /*   Glide.with(getContext())
                .load(R.drawable.pic11)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mSwipeCaptchaView.setImageBitmap(resource);
                        mSwipeCaptchaView.createCaptcha();
                    }
                });*/
//        Glide.with(getContext())
//                .asBitmap()
//                .load(R.drawable.pic11)
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
//                        mSwipeCaptchaView.setImageBitmap(resource);
//                        mSwipeCaptchaView.createCaptcha();
//                    }
//                });

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
        mBtn.setOnClickListener(this);
    }

    //初始化控件
    private void initView() {
        //下一步
        mBtn = (Button) mView.findViewById(R.id.btn_en_register);
        //容器布局
        //手机号
        mEditPhone = (EditText) mView.findViewById(R.id.phone_reg_et);
      /*  //图片验证码
        mPhoneCode = (EditText) mView.findViewById(R.id.edittext_phoneCode_person);
        //图片验证码图片
        mIvCode = (ImageView) mView.findViewById(R.id.iv_code);

        //显示图片验证码
        getCode();*/
        mSwipeCaptchaView = (SwipeCaptchaView) mView.findViewById(swipeCaptchaView);
        mSeekBar = (SeekBar) mView.findViewById(R.id.dragBar);

    }

    @Override
    public void onClick(View view) {
        mPhone = mEditPhone.getText().toString().trim();
    /*    mImagCode = mPhoneCode.getText().toString().trim();*/

        switch (view.getId()) {

            case R.id.btn_en_register:  //下一步
                if (RegisterClickUtils.isFastClick()) {
                    onBtnNext();
                }

                break;

        /*    case R.id.iv_code:          //点击图片刷新验证码
                getCode();              //图片验证码
                break;*/

        }
    }

  /*  private void getCode() {
        mIvCode.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode();
    }*/

    private void onBtnNext() {

        if (TextUtils.isEmpty(mPhone)/* || TextUtils.isEmpty(mImagCode)*/) {

            ToastUtils.MyToast(getActivity(), "手机号不能为空或未滑动验证",2000);

        } else if (!PhoneUtils.isPhoneNumber(mPhone)) {

            ToastUtils.MyToast(getActivity(), "手机号码不合格哦~",2000);

        } /*else if (!mImagCode.equalsIgnoreCase(realCode)) { //不区分大小写

            ToastUtils.MyToast(getActivity(), "图片验证码错误哦~",2000);
        }*/ else if (!NetUtils.hasNetwork(getContext())) {
            ToastUtils.MyToast(getContext(), "网络无法连接,请查看后重试",2000);
        } else {
            //网络请求,判断是否注册过
            OkHttpUtils
                    .post()
                    .url(Constants.REGISTER)
                    .addParams("phoneNum", mPhone)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            LogUtils.d("onError ====" + e);
                            ToastUtils.MyToast(getActivity(), "网络或服务器出错，请重新操作~", Toast.LENGTH_SHORT);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            LogUtils.d("EnFragment" + response.toString());
                            if (response.toString() != null) {
                                String register = response.toString();
                                Log.d("young", "onResponse: "+ register);
                                Gson gson = new Gson();  //解析bean类
                                EnterpriseRegisterBean registerBean = gson.fromJson(register, EnterpriseRegisterBean.class);
                                Log.d("young", "onResponse: "+ registerBean);
                                int errCode = registerBean.getErrCode();
                                if(registerBean.getData() != null) {
                                    mEnterShortName = registerBean.getData().getEnterShortName();
                                }
                                if (errCode == -1){
                                    ToastUtils.MyToast(getActivity(), "数据异常，请重操作~", Toast.LENGTH_SHORT);
                                } else if (errCode == 1) {
                                    //如果用户已注册点击下一步按钮时弹出dialog
                                    onDialogView();
                                    ToastUtils.MyToast(getActivity(), "已在平台注册", 2000);

                                } else if (errCode == 0) {
                                    //传递参数到要跳转的activity
                                    Intent intent = new Intent(getActivity().getApplicationContext(), EnterpriseActivity.class);
                                    Bundle b = new Bundle();
                                    b.putString("phoneNum", mPhone);
                                    intent.putExtras(b);
                                    startActivity(intent);
                                    ToastUtils.MyToast(getActivity(), "账号未注册", 2000);
                                }
                            }
                        }
                    });
        }

    }


    //自定义dialog弹窗
    private void onDialogView() {
        //// TODO: 2017/11/14 新的
        View view = LayoutInflater.from(getContext()).inflate(R.layout.custom_en_message_contentone, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_en_content);
        textView.setText("  您注册的手机号已存在"+ mEnterShortName +"企业，继续注册原单位不能使用此手机号登录VV租行管理平台，如需帮助请联系客户平台");
        mMDialog = new MaterialDialog(getContext())
                .setContentIV(R.mipmap.exclamatorymark)
//                .setContentView(R.layout.custom_en_message_contentone);
                .setContentView(view);
        mMDialog.setPositiveButton("继续注册", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
//                                        ToastUtils.MyToast(mContext, "该手机已经关联平台企业不可重新注册", Toast.LENGTH_SHORT);
                                        mMDialog.dismiss();
                                        enExitShowDialogs();
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

        mMDialog.setNegativeButton("立即登录", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到登录页面
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        mMDialog.show();
        Window window = mMDialog.mAlertDialog.getWindow();
        WindowManager m = getActivity().getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.43); // 高度设置为屏幕的0.6，根据实际情况调整
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.65，根据实际情况调整
        window.setAttributes(p);
        /*//todo 原来的
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTopImage(R.mipmap.message)  //设置dialog内部图标
                .setMessage(R.string.tv_dialog_login)  //设置dialog内容
                .setBackid(R.drawable.clean, new DialogInterface.OnClickListener() { //设置顶部取消按钮
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDialog.dismiss();
                    }
                })
                //dialog注册按钮
                .setNegativeButton(R.string.dialog_register, new DialogInterface.OnClickListener() {
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
                })
                //dialog登录按钮
                .setPositiveButton(R.string.dialog_login, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //跳转到登录页面
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);

                    }
                });
        mDialog = builder.create();
        mDialog.setOnKeyListener(keylistener);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();*/
    }

    private void enExitShowDialogs() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.custom_en_exist_message_contentone, null);
        TextView textView = (TextView) inflate.findViewById(R.id.tv_en_exists_content);
        textView.setText("     您注册的手机号已注册"+ mEnterShortName +"企业，无法继续注册，如需帮助，请联系平台客服人员");
        mSingleMaterialDialog = new SingleMaterialDialog(getContext())
               /* .setContentView(
                        R.layout.custom_en_exist_message_contentone);*/
                .setContentView(
                        inflate);
        mSingleMaterialDialog.setPositiveButton("立即登录", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        mSingleMaterialDialog.show();

        Window window = mSingleMaterialDialog.mAlertDialog.getWindow();
        WindowManager m = getActivity().getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.36); // 高度设置为屏幕的0.6，根据实际情况调整
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.65，根据实际情况调整
        window.setAttributes(p);
    }

    /*//禁止用户通过按返回键消失dialog,只有通过dialog的按钮才能操作dialog
    private DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener() {

        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                return true;
            } else {
                return false;
            }
        }
    };*/

   /* @Override
    public void onDestroy() {
        super.onDestroy();
        mSingleMaterialDialog.dismiss();
    }*/
}
