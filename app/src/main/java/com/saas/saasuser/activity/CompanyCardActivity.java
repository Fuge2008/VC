//package com.saas.saasuser.activity;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.text.method.ScrollingMovementMethod;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.saas.saasuser.R;
//import com.saas.saasuser.application.MyApplication;
//import com.saas.saasuser.global.NetIntent;
//import com.saas.saasuser.global.NetIntentCallBackListener;
//import com.saas.saasuser.util.LogUtils;
//import com.saas.saasuser.util.SharedPreferencesUtil;
//import com.saas.saasuser.util.StringUtils;
//import com.saas.saasuser.util.ToastUtils;
//import com.saas.saasuser.util.Util;
//import com.saas.saasuser.view.CustomProgress;
//import com.squareup.okhttp.Request;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import cn.sharesdk.onekeyshare.OnekeyShare;
//
///**
// * 企业名片
// */
//public class CompanyCardActivity extends BaseActivity implements NetIntentCallBackListener.INetIntentCallBack {
//
//    @BindView(R.id.head_more)
//    LinearLayout headMore;
//    @BindView(R.id.iv_company_background)
//    ImageView ivCompanyBackground;
//   /* @BindView(R.id.iv_company_logo)
//    ImageView ivCompanyLogo;*/
//    @BindView(R.id.tv_company_name)
//    TextView tvCompanyName;
//    @BindView(R.id.en_background_color)
//    RelativeLayout enBackgroundColor;
//    @BindView(R.id.tv_company_code_name)
//    TextView tvCompanyCodeName;
//    @BindView(R.id.tv_company_code)
//    TextView tvCompanyCode;
//    @BindView(R.id.rl_company_code)
//    RelativeLayout rlCompanyCode;
//
//    @BindView(R.id.tv_company_type)
//    TextView tvCompanyType;
//    @BindView(R.id.rl_company_type)
//    RelativeLayout rlCompanyType;
//
//    @BindView(R.id.tv_company_webpage)
//    TextView tvCompanyWebpage;
//    @BindView(R.id.rl_company_webpage)
//    RelativeLayout rlCompanyWebpage;
//
//    @BindView(R.id.tv_company_telephone)
//    TextView tvCompanyTelephone;
//    @BindView(R.id.rl_company_telephone)
//    RelativeLayout rlCompanyTelephone;
//    @BindView(R.id.tv_company_email)
//    TextView tvCompanyEmail;
//    @BindView(R.id.rl_company_email)
//    RelativeLayout rlCompanyEmail;
//    @BindView(R.id.tv_company_describe)
//    TextView tvCompanyDescribe;
//    @BindView(R.id.rl_company_describe)
//    RelativeLayout rlCompanyDescribe;
//    private SharedPreferencesUtil util;
//    private JSONObject json;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_company_card);
//        ButterKnife.bind(this);
//        MyApplication.getInstance().addActivity(this);
//        util = new SharedPreferencesUtil(this);
//        Util.setHeadTitleMore(this, "企业名片", true);
//        new NetIntent().client_companyCardInfo(util.getCompanyId(), new NetIntentCallBackListener(this));//  TODO  获取公司名片信息
//        if (dialog == null) {
//            dialog = CustomProgress.show(this, "加载中..", true, null);
//        }
//        tvCompanyDescribe.setMovementMethod(ScrollingMovementMethod.getInstance());
//
//    }
//
//
//    @Override
//    public void onError(Request request, Exception e) {
//        if (dialog != null) {
//            dialog.dismiss();
//            dialog = null;
//        }
//
//    }
//
//    @Override
//    public void onResponse(String response) {
//        if (dialog != null) {
//            dialog.dismiss();
//            dialog = null;
//        }
//        try {
//            JSONObject jsonObject = new JSONObject(response);
//            ToastUtils.showShortToastSafe(CompanyCardActivity.this, jsonObject.getString("ErrMsg"));
//            if (StringUtils.equals(jsonObject.getString("ErrCode"), "1")) {
//                if (StringUtils.equals("保存订单成功！", jsonObject.getString("ErrMsg"))) {//TODO  操作成功结束
//                    finish();
//                }
//                if (jsonObject.has("Data")) {
//                    json = jsonObject.getJSONObject("Data");
//                    if (json != null && json.length() > 0) {
//                        handler.sendEmptyMessage(1);
//                    }
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 1:
//                    try {
//                        updateViews();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    break;
//
//                default:
//                    break;
//            }
//        }
//    };
//
//    private void updateViews()throws JSONException  {
//        Glide.with(CompanyCardActivity.this).load("http://" + json.getString("LogoPath")).into(ivCompanyBackground);
//        tvCompanyDescribe.setText(StringUtils.repalceEmptyString(json.getString("EnterDescript").toString()));
//        tvCompanyCode.setText(StringUtils.repalceEmptyString(json.getString("EnterNum").toString()));
//        tvCompanyName.setText(StringUtils.repalceEmptyString(json.getString("Contacts").toString()));
//        tvCompanyType.setText(StringUtils.repalceEmptyString(json.getString("Profession1").toString()));
//        tvCompanyWebpage.setText(StringUtils.repalceEmptyString(json.getString("Website").toString()));
//        tvCompanyTelephone.setText(StringUtils.repalceEmptyString(json.getString("EnterPhone").toString()));
//        tvCompanyEmail.setText(StringUtils.repalceEmptyString(json.getString("EnterEmail").toString()));
//    }
//
//
//
//
//        private void showShare() {
//            OnekeyShare oks = new OnekeyShare();
//            //关闭sso授权
//            oks.disableSSOWhenAuthorize();
//            String mm=util.getCompanyId();
//            //String str= Constants.ImageHttpRoot+"/AppComInfo/EnterpriseIntroduce?companyId="+mm;
//            String str= "http://vv-che.com/";
//            LogUtils.e("名片信息："+str);
//            // 分享时Notification的图标和文字  2.5.9以后的版本不     调用此方法
//            //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
//            // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//
//            oks.setTitle("自组网约，助力出行！欢迎访问：http://vv-che.com/");
//            // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//            oks.setTitleUrl(str);
//            // text是分享文本，所有平台都需要这个字段
//            oks.setText("自组网约，助力出行1");
//            // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//           // oks.setImagePath("http://vv-che.com/Images/header-logo.png");//确保SDcard下面存在此张图片
//            //oks.setImageUrl("http://vv-che.com/Images/header-logo.png");
//            // url仅在微信（包括好友和朋友圈）中使用
//            oks.setUrl(str);
//            // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//            oks.setComment("自组网约，助力出行");
//            // site是分享此内容的网站名称，仅在QQ空间使用
//            oks.setSite("VV租行");
//            // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//            oks.setSiteUrl(str);
//
//            // 启动分享GUI
//            oks.show(CompanyCardActivity.this);
//
//
//
//
//
//
//
//        }
//
//        @OnClick({R.id.head_more, R.id.tv_company_name, R.id.en_background_color, R.id.rl_company_code, R.id.rl_company_type, R.id.rl_company_webpage, R.id.rl_company_telephone, R.id.rl_company_email, R.id.rl_company_describe})
//        public void onViewClicked(View view) {
//            switch (view.getId()) {
//                case R.id.head_more:
//                    showShare();
//                    break;
//                case R.id.tv_company_name:
//                    break;
//                case R.id.en_background_color:
//                    break;
//                case R.id.rl_company_code:
//                    break;
//                case R.id.rl_company_type:
//                    break;
//                case R.id.rl_company_webpage:
//                    if(StringUtils.isUrl(tvCompanyWebpage.getText().toString())){
//                        Uri uri=Uri.parse(tvCompanyWebpage.getText().toString());
//                        Intent intent=new Intent();
//                        intent.setAction(Intent.ACTION_VIEW);
//                        intent.setData(uri);
//                        CompanyCardActivity.this.startActivity(intent);
//                    }
//
//                    break;
//                case R.id.rl_company_telephone:
//                    if(StringUtils.isNotEmpty(tvCompanyTelephone.getText().toString(),true)){
//                        String telString= tvCompanyTelephone.getText().toString();
//                        Uri uriTel=Uri.parse("tel:"+telString);
//                        Intent intentTel=new Intent();
//                        intentTel.setAction(Intent.ACTION_CALL);
//                        intentTel.setData(uriTel);
//                        CompanyCardActivity.this.startActivity(intentTel);
//                    }
//                    break;
//                case R.id.rl_company_email:
//                    if(StringUtils.isEmail(tvCompanyEmail.getText().toString())){
//                        Intent data=new Intent(Intent.ACTION_SENDTO);
//                        data.setData(Uri.parse(tvCompanyEmail.getText().toString()));
//                        data.putExtra(Intent.EXTRA_SUBJECT, "这是标题");
//                        data.putExtra(Intent.EXTRA_TEXT, "这是内容");
//                        startActivity(Intent.createChooser(data, "请选择邮件发送软件"));
//                    }
//
//                    break;
//                case R.id.rl_company_describe:
//                    break;
//            }
//        }
//}
//
//
