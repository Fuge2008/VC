package com.saas.saasuser.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saas.saasuser.R;
import com.saas.saasuser.application.MyApplication;
import com.saas.saasuser.global.NetIntent;
import com.saas.saasuser.global.NetIntentCallBackListener;
import com.saas.saasuser.util.SharedPreferencesUtil;
import com.saas.saasuser.util.StringUtils;
import com.saas.saasuser.util.ToastUtils;
import com.saas.saasuser.util.Util;
import com.saas.saasuser.view.CustomProgress;
import com.saas.saasuser.view.ItemDialog;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderUserCompeleteActivity extends BaseActivity implements ItemDialog.OnDialogItemClickListener, NetIntentCallBackListener.INetIntentCallBack {

    @BindView(R.id.head_title)
    TextView headTitle;
    @BindView(R.id.head_more)
    LinearLayout headMore;
    @BindView(R.id.iv_settlement_modes)
    ImageView ivSettlementModes;
    @BindView(R.id.et_trip_cost)
    EditText etTripCost;
    @BindView(R.id.iv_payment_currency)
    ImageView ivPaymentCurrency;
    @BindView(R.id.et_bill_num)
    EditText etBillNum;
    @BindView(R.id.et_trip_describe)
    EditText etTripDescribe;
    @BindView(R.id.btn_submit)
    TextView btnSubmit;

    String orderId;
    @BindView(R.id.tv_settlement_modes)
    TextView tvSettlementModes;
    @BindView(R.id.tv_payment_currency)
    TextView tvPaymentCurrency;
    private JSONObject json;
    private int TYPE_TAG, ORDER_SETTLEMENT_MODES_TYPE = 1, ORDER_CURRENCY_TYPE = 1;
    private SharedPreferencesUtil util;
    private boolean isSubmit = false;


    private String[] ORDER_SETTLEMENT_MODES;
    private String[] ORDER_CURRENCY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_user_compelete);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        Util.setHeadTitleMore(this, "订单完善", true);
        btnSubmit.setText("保存");
        util = new SharedPreferencesUtil(this);
        orderId = this.getIntent().getStringExtra("orderId");
        new NetIntent().client_userGetCompeleteOrderDetail(util.getUserId(), orderId, new NetIntentCallBackListener(this));//TODO

        if (dialog == null) {
            dialog = CustomProgress.show(this, "加载中..", true, null);
        }
    }



    @OnClick({R.id.btn_submit,R.id.ll_settlement_modes, R.id.ll_payment_currency})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                isSubmit = true;
                String tripCost = etTripCost.getText().toString();
                String billNum = etBillNum.getText().toString();
                String tripDescribe = etTripDescribe.getText().toString();
                if (StringUtils.isNotEmpty(orderId, true) && StringUtils.isNotEmpty(ORDER_SETTLEMENT_MODES_TYPE + "", true) && StringUtils.isNotEmpty(ORDER_CURRENCY_TYPE + "", true)
                        && StringUtils.isNotEmpty(tripCost, true) && StringUtils.isNotEmpty(billNum, true) && StringUtils.isNotEmpty(tripDescribe, true)
                        && StringUtils.isNotEmpty("userid", true)) {
                    new NetIntent().client_userCompleteOrder(orderId, ORDER_SETTLEMENT_MODES_TYPE + "", tripCost, ORDER_CURRENCY_TYPE + "", billNum, tripDescribe, "", new NetIntentCallBackListener(this));//TODO   用户id
                    if (dialog == null) {
                        dialog = CustomProgress.show(this, "正在提交..", true, null);
                    }
                } else {
                    ToastUtils.showShortToastSafe(OrderUserCompeleteActivity.this, "必填数据不能为空！");
                }

                break;

            case R.id.ll_settlement_modes:
                ORDER_SETTLEMENT_MODES = new String[]{"内部使用", "现金支付", "企业月结"};
                choseCarModel(ORDER_SETTLEMENT_MODES, "结算方式", 1);
                break;
            case R.id.ll_payment_currency:
                ORDER_CURRENCY = new String[]{"人民币", "港币", "美元", "英镑", "欧元", "其他"};
                choseCarModel(ORDER_CURRENCY, "垫付币种", 2);
                break;
        }
    }

    @Override
    public void onError(Request request, Exception e) {
        if (dialog != null) {
            dialog.dismiss();
        }

    }

    @Override
    public void onResponse(String response) {
        if (dialog != null) {
            dialog.dismiss();
        }
        try {
            JSONObject jsonObject = new JSONObject(response);
            if(StringUtils.equals("获取失败，无此订单！",jsonObject.getString("ErrMsg"))){
                ToastUtils.showShortToastSafe(OrderUserCompeleteActivity.this, "司机尚未确认结束，请稍后再完善！");
                finish();
            }
            //new NetIntent().client_underOrder(util.getUserId(), util.getPlatformRole(), new NetIntentCallBackListener(this));//检查是否有正在执行的订单
            if (StringUtils.equals(jsonObject.getString("ErrCode"), "1")) {
                if (isSubmit) {
                    ToastUtils.showShortToastSafe(OrderUserCompeleteActivity.this, jsonObject.getString("ErrMsg"));
                    finish();
                }
            }
            if (jsonObject.has("Data")) {
                json = jsonObject.getJSONObject("Data");
                // handler.sendEmptyMessage(1);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void choseCarModel(String[] array, String title, int tag) {
        new ItemDialog(OrderUserCompeleteActivity.this, array, title, tag, this).show();
        TYPE_TAG = tag;
    }

    @Override
    public void onDialogItemClick(int requestCode, int position, String item) {
        String strName = item;
        if (StringUtils.isNotEmpty(strName, true)) {
            if (1 == TYPE_TAG) {
                tvSettlementModes.setText(strName);
                ORDER_SETTLEMENT_MODES_TYPE = position + 1;
            } else if (2 == TYPE_TAG) {
                tvPaymentCurrency.setText(strName);
                ORDER_CURRENCY_TYPE = position + 1;
            }

        }

    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        showViews();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    break;
            }
        }

        ;
    };

    private void showViews() throws JSONException {
        String strSettleType;
        if (StringUtils.equals("1", json.getString("CASettleType").toString())) {
            strSettleType = "内部使用";
        } else if (StringUtils.equals("2", json.getString("CASettleType").toString())) {
            strSettleType = "现金支付";
        } else {
            strSettleType = "企业月结";
        }
        String strPaymentCurrency = null;
        if (StringUtils.equals("1", json.getString("CAOfferType").toString())) {
            strPaymentCurrency = "人民币";
        } else if (StringUtils.equals("2", json.getString("CAOfferType").toString())) {
            strPaymentCurrency = "港币";
        } else if (StringUtils.equals("3", json.getString("CAOfferType").toString())) {
            strPaymentCurrency = "美元";
        } else if (StringUtils.equals("4", json.getString("CAOfferType").toString())) {
            strPaymentCurrency = "英镑";
        } else if (StringUtils.equals("5", json.getString("CAOfferType").toString())) {
            strPaymentCurrency = "欧元";
        } else if (StringUtils.equals("6", json.getString("CAOfferType").toString())) {
            strPaymentCurrency = "其他";
        }
        etBillNum.setText(StringUtils.repalceEmptyString(json.getString("CATicketNums").toString()));
        etTripCost.setText(StringUtils.repalceEmptyString(json.getString("CARouteCost").toString()));
        etTripDescribe.setText(StringUtils.repalceEmptyString(json.getString("CARouteCostMark").toString()));
        tvSettlementModes.setText(strSettleType);
        tvPaymentCurrency.setText(strPaymentCurrency);

    }


}
