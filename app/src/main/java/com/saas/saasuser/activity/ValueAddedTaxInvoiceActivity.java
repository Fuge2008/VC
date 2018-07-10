package com.saas.saasuser.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.saas.saasuser.R;
import com.saas.saasuser.application.MyApplication;
import com.saas.saasuser.global.NetIntent;
import com.saas.saasuser.global.NetIntentCallBackListener;
import com.saas.saasuser.util.LogUtils;
import com.saas.saasuser.util.ScreenShotUtil;
import com.saas.saasuser.util.SharedPreferencesUtil;
import com.saas.saasuser.util.StringUtils;
import com.saas.saasuser.util.TimeUtils;
import com.saas.saasuser.util.ToastUtils;
import com.saas.saasuser.util.Util;
import com.saas.saasuser.view.CustomProgress;
import com.saas.saasuser.view.ScratchCard;
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

public class ValueAddedTaxInvoiceActivity extends BaseActivity  implements NetIntentCallBackListener.INetIntentCallBack{


    @BindView(R.id.sc_view)
    ScratchCard scView;
    @BindView(R.id.iv_chinese_logo)
    ImageView ivChineseLogo;
    @BindView(R.id.head_more)
    LinearLayout headMore;
    @BindView(R.id.tv_company_name)
    TextView tvCompanyName;
    @BindView(R.id.tv_revenue_num)
    TextView tvRevenueNum;
    @BindView(R.id.tv_deposit_bank)
    TextView tvDepositBank;
    @BindView(R.id.tv_bank_account)
    TextView tvBankAccount;
    @BindView(R.id.tv_telephone_num)
    TextView tvTelephoneNum;
    @BindView(R.id.tv_company_address)
    TextView tvCompanyAddress;
    private Bitmap bitmap;

    private SharedPreferencesUtil util;
    private JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_value_added_tax_invoice);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        util = new SharedPreferencesUtil(this);
        Util.setHeadTitleMore(this, "增值税专用发票", true);
        getBitmap();
        initQR();


        new NetIntent().client_getCompanyVnvoice(util.getCompanyId(), new NetIntentCallBackListener(this));//  TODO
        if (dialog == null) {
            dialog = CustomProgress.show(this, "加载中..", true, null);
        }
    }

    private void getBitmap() {
        Glide.with(ValueAddedTaxInvoiceActivity.this).load(util.getPicture()).asBitmap().into(new SimpleTarget<Bitmap>() {
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
                            ValueAddedTaxInvoiceActivity.this.getResources(),
                            R.drawable.saas_logo);
                }
                return QRCodeEncoder.syncEncodeQRCode(util.getMobilePhone(),
                        BGAQRCodeUtil.dp2px(ValueAddedTaxInvoiceActivity.this, 150),
                        Color.BLACK, Color.WHITE, null);// 设置二维码颜色，信息，logo等
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    ivChineseLogo.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(ValueAddedTaxInvoiceActivity.this, "生成二维码失败",
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
                    Toast.makeText(ValueAddedTaxInvoiceActivity.this, errorTip,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ValueAddedTaxInvoiceActivity.this, result,
                            Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    @OnClick({R.id.head_more, R.id.iv_chinese_logo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_more:
                //截屏
                Bitmap b = ScreenShotUtil.takeScreenShot(ValueAddedTaxInvoiceActivity.this);
                String strBitmapName = TimeUtils.getNowTime().trim().replace(":", "").replace("-", "").replace(" ", "");
                String strBitmapPath = ScreenShotUtil.saveMyBitmap(b, strBitmapName);
                //分享
                OnekeyShare oks = new OnekeyShare();
                //关闭sso授权
                oks.disableSSOWhenAuthorize();
                oks.setSite("VV租行");
                oks.setImagePath(strBitmapPath);
                // 启动分享GUI
                oks.show(this);
                break;
            case R.id.iv_chinese_logo:
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
            LogUtils.e("企业名片："+jsonObject.toString());
            ToastUtils.showShortToastSafe(ValueAddedTaxInvoiceActivity.this, jsonObject.getString("ErrMsg"));
            if (StringUtils.equals(jsonObject.getString("ErrCode"), "1")) {
//                if (StringUtils.equals("保存订单成功！", jsonObject.getString("ErrMsg"))) {//TODO  操作成功结束
//                    finish();
//                }
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
        tvRevenueNum.setText(StringUtils.repalceEmptyString(json.getString("ComDuty").toString()));
        tvCompanyName.setText(StringUtils.repalceEmptyString(json.getString("InvoiceTitle").toString()));
        tvDepositBank.setText(StringUtils.repalceEmptyString(json.getString("Bank").toString()));
        tvBankAccount.setText(StringUtils.repalceEmptyString(json.getString("BankAccount").toString()));
        tvTelephoneNum.setText(StringUtils.repalceEmptyString(json.getString("RegLoginPhone").toString()));
        tvCompanyAddress.setText(StringUtils.repalceEmptyString(json.getString("RegAddress").toString()));


    }
}
