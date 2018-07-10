package com.saas.saasuser.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.saas.saasuser.R;
import com.saas.saasuser.application.MyApplication;
import com.saas.saasuser.global.NetIntent;
import com.saas.saasuser.global.NetIntentCallBackListener;
import com.saas.saasuser.util.ScreenShotUtil;
import com.saas.saasuser.util.SharedPreferencesUtil;
import com.saas.saasuser.util.StringUtils;
import com.saas.saasuser.util.TimeUtils;
import com.saas.saasuser.util.ToastUtils;
import com.saas.saasuser.util.Util;
import com.saas.saasuser.view.CustomProgress;
import com.saas.saasuser.view.zxing.core.BGAQRCodeUtil;
import com.saas.saasuser.view.zxing.zx.QRCodeDecoder;
import com.saas.saasuser.view.zxing.zx.QRCodeEncoder;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class EnterprisesCardActivity extends BaseActivity implements NetIntentCallBackListener.INetIntentCallBack{


    @BindView(R.id.iv_my_head)
    ImageView ivMyHead;
    @BindView(R.id.iv_chinese_logo)
    ImageView ivChineseLogo;
    @BindView(R.id.tv_company_name)
    TextView tvCompanyName;
    @BindView(R.id.tv_company_tel)
    TextView tvCompanyTel;
    @BindView(R.id.tv_company_email)
    TextView tvCompanyEmail;
    @BindView(R.id.tv_company_code)
    TextView tvCompanyCode;
    @BindView(R.id.tv_company_type)
    TextView tvCompanyType;
    @BindView(R.id.tv_company_website)
    TextView tvCompanyWebsite;
    @BindView(R.id.tv_company_describe)
    TextView tvCompanyDescribe;
    @BindView(R.id.cardView)
    CardView cardView;
    private SharedPreferencesUtil util;
    private JSONObject json;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterprises_card);
        MyApplication.getInstance().addActivity(this);
        util = new SharedPreferencesUtil(this);
        Util.setHeadTitleMore(this, "企业名片", true);
        ButterKnife.bind(this);
        new NetIntent().client_companyCardInfo(util.getCompanyId(), new NetIntentCallBackListener(this));//  TODO  获取公司名片信息
        if (dialog == null) {
            dialog = CustomProgress.show(this, "加载中..", true, null);
        }

        getBitmap();
        initQR();
    }


    @OnClick({R.id.head_more, R.id.iv_my_head})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_more:
                //截屏
                Bitmap b=ScreenShotUtil.takeScreenShot(EnterprisesCardActivity.this);
                String strBitmapName= TimeUtils.getNowTime().trim().replace(":","").replace("-","").replace(" ","");
                String strBitmapPath= ScreenShotUtil.saveMyBitmap(b,strBitmapName);
                //分享
                OnekeyShare oks = new OnekeyShare();
                //关闭sso授权
                oks.disableSSOWhenAuthorize();
                oks.setSite("VV租行");
                oks.setImagePath(strBitmapPath);
                // 启动分享GUI
                oks.show(this);

                break;
              
            case R.id.iv_my_head:
                break;
        }
    }

    @Override
    public void onError(Request request, Exception e) {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }

    }

    @Override
    public void onResponse(String response) {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        try {
            JSONObject jsonObject = new JSONObject(response);
            ToastUtils.showShortToastSafe(EnterprisesCardActivity.this, jsonObject.getString("ErrMsg"));
            if (StringUtils.equals(jsonObject.getString("ErrCode"), "1")) {
                if (StringUtils.equals("保存订单成功！", jsonObject.getString("ErrMsg"))) {//TODO  操作成功结束
                    finish();
                }
                if (jsonObject.has("Data")) {
                    json = jsonObject.getJSONObject("Data");
                    if (json != null && json.length() > 0) {
                        handler.sendEmptyMessage(1);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        updateViews();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    break;
            }
        }
    };

    private void updateViews()throws JSONException  {
        Glide.with(EnterprisesCardActivity.this).load("http://" + json.getString("LogoPath")).into(ivMyHead);
        tvCompanyDescribe.setText(StringUtils.repalceEmptyString(json.getString("EnterDescript").toString()));
        tvCompanyCode.setText(StringUtils.repalceEmptyString(json.getString("EnterNum").toString()));
        tvCompanyName.setText(StringUtils.repalceEmptyString(json.getString("Contacts").toString()));
        tvCompanyType.setText(StringUtils.repalceEmptyString(json.getString("Profession1").toString()));
        tvCompanyWebsite.setText(StringUtils.repalceEmptyString(json.getString("Website").toString()));
        tvCompanyTel.setText(StringUtils.repalceEmptyString(json.getString("EnterPhone").toString()));
        tvCompanyEmail.setText(StringUtils.repalceEmptyString(json.getString("EnterEmail").toString()));
    }


    private void getBitmap() {
        Glide.with(EnterprisesCardActivity.this).load(util.getPicture()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                bitmap = resource;
            }
        });
    }

    private void initQR() {//二维码初始化

        // TODO AsyncTask 需要解决内存泄露问题
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                if (bitmap == null) {
                    bitmap = BitmapFactory.decodeResource(
                            EnterprisesCardActivity.this.getResources(),
                            R.drawable.saas_logo);
                }
                return QRCodeEncoder.syncEncodeQRCode(util.getMobilePhone(),
                        BGAQRCodeUtil.dp2px(EnterprisesCardActivity.this, 150),
                        Color.BLACK, Color.WHITE, null);// 设置二维码颜色，信息，logo等
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    ivChineseLogo.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(EnterprisesCardActivity.this, "生成二维码失败",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    public void decodeChineseWithLogo(View v) {
        ivChineseLogo.setDrawingCacheEnabled(true);
        Bitmap bitmap = ivChineseLogo.getDrawingCache();
        decode(bitmap, "解析二维码失败");
    }

    @SuppressLint("NewApi")
    private void decode(final Bitmap bitmap, final String errorTip) {
        // AsyncTask 需要解决内存泄露问题
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                return QRCodeDecoder.syncDecodeQRCode(bitmap);
            }

            @Override
            protected void onPostExecute(String result) {
                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(EnterprisesCardActivity.this, errorTip,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EnterprisesCardActivity.this, result,
                            Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }


}
