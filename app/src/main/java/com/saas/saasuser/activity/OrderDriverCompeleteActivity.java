package com.saas.saasuser.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.liangmutian.mypicker.TimePickerDialog;
import com.saas.saasuser.R;
import com.saas.saasuser.activity.map.LocationSelecteActivity;
import com.saas.saasuser.application.MyApplication;
import com.saas.saasuser.global.NetIntent;
import com.saas.saasuser.global.NetIntentCallBackListener;
import com.saas.saasuser.util.LogUtils;
import com.saas.saasuser.util.SharedPreferencesUtil;
import com.saas.saasuser.util.StringUtils;
import com.saas.saasuser.util.ToastUtils;
import com.saas.saasuser.util.Util;
import com.saas.saasuser.view.CustomProgress;
import com.saas.saasuser.view.ExpandLayout;
import com.saas.saasuser.view.ItemDialog;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.saas.saasuser.R.id.et_calculate_kilometre;
import static com.saas.saasuser.R.id.et_start_kilometre;

/**
 * 司机订单完善
 */
public class OrderDriverCompeleteActivity extends BaseActivity implements ItemDialog.OnDialogItemClickListener, NetIntentCallBackListener.INetIntentCallBack {

    @BindView(R.id.head_title)
    TextView headTitle;
    @BindView(R.id.head_more)
    LinearLayout headMore;
    @BindView(R.id.et_start_time)
    TextView etStartTime;
    @BindView(R.id.et_end_time)
    TextView etEndTime;
    @BindView(R.id.et_duration_account)
    EditText etDurationAccount;
    @BindView(et_start_kilometre)
    EditText etStartKilometre;
    @BindView(R.id.et_end_kilometre)
    EditText etEndKilometre;
    @BindView(et_calculate_kilometre)
    EditText etCalculateKilometre;
    @BindView(R.id.et_start_address)
    EditText etStartAddress;
    @BindView(R.id.et_mid_address)
    EditText etMidAddress;
    @BindView(R.id.et_end_address)
    EditText etEndAddress;
    @BindView(R.id.et_road_bill_total)
    EditText etRoadBillTotal;
    @BindView(R.id.tv_road_payment_modes)
    TextView tvRoadPaymentModes;
    @BindView(R.id.iv_road_payment_modes)
    LinearLayout ivRoadPaymentModes;
    @BindView(R.id.et_road_bill_num)
    EditText etRoadBillNum;


    @BindView(R.id.et_oil_money_num)
    EditText etOilMoneyNum;
    @BindView(R.id.tv_oil_paymnet_modes)
    TextView tvOilPaymnetModes;
    @BindView(R.id.iv_oil_payment_modes)
    LinearLayout ivOilPaymentModes;
    @BindView(R.id.et_oil_bill_num)
    EditText etOilBillNum;
    @BindView(R.id.et_oil_litre)
    EditText etOilLitre;
    @BindView(R.id.tv_oil_grade)
    TextView tvOilGrade;
    @BindView(R.id.iv_oil_grade)
    LinearLayout ivOilGrade;
    @BindView(R.id.et_parking_bill)
    EditText etParkingBill;
    @BindView(R.id.tv_parking_payment_modes)
    TextView tvParkingPaymentModes;
    @BindView(R.id.iv_parking_payment_modes)
    LinearLayout ivParkingPaymentModes;
    @BindView(R.id.et_parking_bill_num)
    EditText etParkingBillNum;
    @BindView(R.id.et_other_start_kilometre)
    EditText etOtherStartKilometre;
    @BindView(R.id.et_other_end_kilometre)
    EditText etOtherEndKilometre;
    @BindView(R.id.et_other_driver_kilometre)
    EditText etOtherDriverKilometre;
    @BindView(R.id.et_task_overtime)
    EditText etTaskOvertime;
    @BindView(R.id.et_not_passager_kilometre)
    EditText etNotPassagerKilometre;
    @BindView(R.id.et_real_income)
    EditText etRealIncome;
    @BindView(R.id.et_incomes_describe)
    EditText etIncomesDescribe;
    @BindView(R.id.btn_submit)
    TextView btnMore;
    @BindView(R.id.tv_income_currency)
    TextView tvIncomeCurrency;
    @BindView(R.id.iv_income_currency)
    LinearLayout ivIncomeCurrency;
    SharedPreferencesUtil util;

    @BindView(R.id.iv_start_time)
    LinearLayout ivStartTime;
    @BindView(R.id.iv_end_time)
    LinearLayout ivEndTime;
    @BindView(R.id.iv_start_address)
    LinearLayout ivStartAddress;
    @BindView(R.id.iv_mid_address)
    LinearLayout ivMidAddress;
    @BindView(R.id.iv_end_address)
    LinearLayout ivEndAddress;

    private JSONObject json;
    private boolean isSubmit = false;
    private Dialog timeDialog;
    public static final int REQUSE8 = 8;
    public static final int REQUSE9 = 9;
    public static final int REQUSE10 = 10;
    public static final int REQUSE11 = 11;


    String orderId;
    private int TYPE_TAG, INCOMES_CURRENCY_TYPE = 1, PAYMENT_MODES_ROAD = 1, PAYMENT_MODES_OIL = 1, PAYMENT_MODES_PARKING = 1, OIL_TYPE_TAG = 1;

    private String[] INCOMES_CURRENCY = new String[]{"人民币", "港币", "美元", "英镑", "欧元", "其他",};
    private String[] PAYMENT_MODES = new String[]{"现金垫付", "电子消费", "企业结算"};
    private String[] OIL_TYPE = new String[]{"92#汽油", "95#汽油", "98#汽油", "0#柴油"};
    private boolean isTitleShow1 = true, isTitleShow2 = true, isTitleShow3 = true;


    private LinearLayout expand_img1;
    private LinearLayout expand_img2;
    private LinearLayout expand_img3;

    private ExpandLayout mExpandableLayout1;
    private ExpandLayout mExpandableLayout2;
    private ExpandLayout mExpandableLayout3;


    private boolean isChecked1 = false;
    private boolean isChecked2 = false;
    private boolean isChecked3 = false;

    private ImageView iv_vv1;
    private ImageView iv_vv2;
    private ImageView iv_vv3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_driver_compelete);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        Util.setHeadTitleMore(this, "订单完善", true);
        // headMore.setVisibility(View.GONE);
        btnMore.setText("保存");
        util = new SharedPreferencesUtil(this);
        orderId = this.getIntent().getStringExtra("orderId");
        new NetIntent().client_driverGetCompeleteOrderDetail(util.getUserId(), orderId, new NetIntentCallBackListener(this));
        if (dialog == null) {
            dialog = CustomProgress.show(this, "加载中..", true, null);
        }
        iv_vv1 = (ImageView) findViewById(R.id.iv_vv1);
        iv_vv2 = (ImageView) findViewById(R.id.iv_vv2);
        iv_vv3 = (ImageView) findViewById(R.id.iv_vv3);

        expand_img1 = (LinearLayout) findViewById(R.id.expand_img1);
        expand_img2 = (LinearLayout) findViewById(R.id.expand_img2);
        expand_img3 = (LinearLayout) findViewById(R.id.expand_img3);


        mExpandableLayout1 = (ExpandLayout) findViewById(R.id.expandLayout1);
        mExpandableLayout2 = (ExpandLayout) findViewById(R.id.expandLayout2);
        mExpandableLayout3 = (ExpandLayout) findViewById(R.id.expandLayout3);

        mExpandableLayout1.post(new Runnable() {
            @Override
            public void run() {
                mExpandableLayout1.initExpand(true);
            }
        });
        mExpandableLayout2.post(new Runnable() {
            @Override
            public void run() {
                mExpandableLayout2.initExpand(false);
            }
        });
        mExpandableLayout3.post(new Runnable() {
            @Override
            public void run() {
                mExpandableLayout3.initExpand(false);
            }
        });

        expand_img1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mExpandableLayout1.toggleExpand();
                isChecked1 = !isChecked1;
                if (isChecked1) {
                    iv_vv1.setImageResource(R.drawable.shangsanjiao);

                } else {

                    iv_vv1.setImageResource(R.drawable.xiasanjiao);
                }


            }
        });
        expand_img2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mExpandableLayout2.toggleExpand();
                isChecked2 = !isChecked2;
                if (isChecked2) {
                    iv_vv2.setImageResource(R.drawable.shangsanjiao);

                } else {

                    iv_vv2.setImageResource(R.drawable.xiasanjiao);
                }

            }
        });

        expand_img3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mExpandableLayout3.toggleExpand();
                isChecked3 = !isChecked3;
                if (isChecked3) {
                    iv_vv3.setImageResource(R.drawable.shangsanjiao);

                } else {

                    iv_vv3.setImageResource(R.drawable.xiasanjiao);
                }

            }
        });

        etStartKilometre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtils.isNotEmpty(etEndKilometre.getText(), true) && StringUtils.isNotEmpty(etEndKilometre.getText(), true)) {
                    Double fKilometre = Double.valueOf(etEndKilometre.getText().toString()) - Double.valueOf(etStartKilometre.getText().toString());
                    if (fKilometre > 0) {
                       DecimalFormat df   =new DecimalFormat("#.00");
                        etCalculateKilometre.setText( df.format(fKilometre).toString());
                    } else {
                        ToastUtils.showShortToast(OrderDriverCompeleteActivity.this, "输入的始/终公里数有误，请重新输入！");
                    }
                }
            }
        });
        etEndKilometre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtils.isNotEmpty(etEndKilometre.getText(), true) && StringUtils.isNotEmpty(etEndKilometre.getText(), true)) {
                    Double fKilometre = Double.valueOf(etEndKilometre.getText().toString()) - Double.valueOf(etStartKilometre.getText().toString());
                    LogUtils.e("行程公里数：" + fKilometre.toString());
                    if (fKilometre > 0) {
                       DecimalFormat   df   =new  DecimalFormat("#.00");
                        etCalculateKilometre.setText( df.format(fKilometre).toString());

                    } else {
                        ToastUtils.showShortToast(OrderDriverCompeleteActivity.this, "输入的始/终公里数有误，请重新输入！");
                    }
                }
            }
        });

    }


    @OnClick({R.id.iv_road_payment_modes, R.id.iv_oil_payment_modes, R.id.iv_oil_grade, R.id.iv_parking_payment_modes, R.id.btn_submit, R.id.iv_income_currency, R.id.iv_start_time, R.id.iv_end_time, R.id.iv_start_address, R.id.iv_mid_address, R.id.iv_end_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_road_payment_modes:

                choseCarModel(PAYMENT_MODES, "垫付方式", 1);
                break;
            case R.id.iv_oil_payment_modes:
                choseCarModel(PAYMENT_MODES, "垫付方式", 3);
                break;
            case R.id.iv_oil_grade:
                choseCarModel(OIL_TYPE, "油品", 5);
                break;
            case R.id.iv_parking_payment_modes:

                choseCarModel(PAYMENT_MODES, "垫付方式", 2);
                break;
            case R.id.iv_income_currency:
                choseCarModel(INCOMES_CURRENCY, "垫付币种", 4);
                break;
            case R.id.btn_submit:
                isSubmit = true;
                String startTime = etStartTime.getText().toString();

                String endTime = etEndTime.getText().toString();

                String durationAccount = etDurationAccount.getText().toString();

                String startKilometre = etStartKilometre.getText().toString();

                String endKilometre = etEndKilometre.getText().toString();

                String calculateKilometre = etCalculateKilometre.getText().toString();

                String startAddress = etStartAddress.getText().toString();

                String midAddress = etMidAddress.getText().toString();

                String endAddress = etEndAddress.getText().toString();

                String roadBillTotal = etRoadBillTotal.getText().toString();

                String roadPaymentModes = tvRoadPaymentModes.getText().toString();

                //LinearLayout ivRoadPaymentModes;

                String roadBillNum = etRoadBillNum.getText().toString();

                //  String oilBillTotal = etOilBillTotal.getText().toString();

                String oilMoneyNum = etOilMoneyNum.getText().toString();

                String oilPaymnetModes = tvOilPaymnetModes.getText().toString();

                //LinearLayout ivOilPaymentModes;

                String oilBillNum = etOilBillNum.getText().toString();

                String oilLitre = etOilLitre.getText().toString();

                String oilGrade = tvOilGrade.getText().toString();

                //LinearLayout ivOilGrade;

                String parkingBill = etParkingBill.getText().toString();

                String parkingPaymentModes = tvParkingPaymentModes.getText().toString();

                //LinearLayout ivParkingPaymentModes;

                String parkingBillNum = etParkingBillNum.getText().toString();

                String otherStartKilometre = etOtherStartKilometre.getText().toString();

                String otherEndKilometre = etOtherEndKilometre.getText().toString();

                String otherDriverKilometre = etOtherDriverKilometre.getText().toString();

                String taskOvertime = etTaskOvertime.getText().toString();

                String notPassagerKilometre = etNotPassagerKilometre.getText().toString();

                String realIncome = etRealIncome.getText().toString();

                String incomesDescribe = etIncomesDescribe.getText().toString();

                String incomeCurrency = tvIncomeCurrency.getText().toString();
                if (StringUtils.isNotEmpty(orderId, true) && StringUtils.isNotEmpty(util.getCompanyId(), true) && StringUtils.isNotEmpty(util.getUserId(), true)
                        && StringUtils.isNotEmpty(startTime, true) && StringUtils.isNotEmpty(endTime, true) && StringUtils.isNotEmpty(durationAccount, true)
                        && StringUtils.isNotEmpty(startKilometre + "", true) && StringUtils.isNotEmpty(endKilometre, true) && StringUtils.isNotEmpty(calculateKilometre, true)
                        && StringUtils.isNotEmpty(midAddress, true)) {
                    new NetIntent().client_drivercompleteOrder(orderId, util.getCompanyId(), util.getUserId(), startTime, endTime,
                            durationAccount, startKilometre, endKilometre, calculateKilometre, startAddress,
                            midAddress, endAddress, roadBillTotal, PAYMENT_MODES_ROAD + "", roadBillNum,
                            parkingBill, PAYMENT_MODES_PARKING + "", parkingBillNum, oilMoneyNum, PAYMENT_MODES_OIL + "",
                            oilBillNum, OIL_TYPE_TAG + "", oilLitre, realIncome, INCOMES_CURRENCY_TYPE + "",
                            otherStartKilometre, otherEndKilometre, taskOvertime, otherDriverKilometre, notPassagerKilometre,
                            incomesDescribe, new NetIntentCallBackListener(this));//完善订单
                    if (dialog == null) {
                        dialog = CustomProgress.show(this, "正在提交..", true, null);
                    }
                } else {
                    ToastUtils.showShortToastSafe(OrderDriverCompeleteActivity.this, "必填数据不能为空！");
                }
                break;
//            case R.id.ll_time_kilometer:
////                if (isTitleShow1) {
////                    cbTitleShowHind1.setBackgroundResource(R.drawable.arrow_down);
////                    llMainShow1.setVisibility(View.GONE);
////                    isTitleShow1 = false;
////                } else {
////                    cbTitleShowHind1.setBackgroundResource(R.drawable.arrow_up);
////                    llMainShow1.setVisibility(View.VISIBLE);
////                    isTitleShow1 = true;
////                }
//                break;
            case R.id.iv_start_time:
                showTimePick(etStartTime);
                timeDialog = null;
                break;
            case R.id.iv_end_time:
                showTimePick(etEndTime);
                timeDialog = null;
                break;
            case R.id.iv_start_address:
                Intent intent1 = new Intent(OrderDriverCompeleteActivity.this, LocationSelecteActivity.class);
                startActivityForResult(intent1, REQUSE8);
                break;
            case R.id.iv_mid_address:
                Intent intent3 = new Intent(OrderDriverCompeleteActivity.this, LocationSelecteActivity.class);
                startActivityForResult(intent3, REQUSE10);
                break;
            case R.id.iv_end_address:
                Intent intent2 = new Intent(OrderDriverCompeleteActivity.this, LocationSelecteActivity.class);
                startActivityForResult(intent2, REQUSE9);
                break;
//            case R.id.ll_trip_bill:
////                if (isTitleShow2) {
////                    cbTitleShowHind2.setBackgroundResource(R.drawable.arrow_down);
////                    llMainShow2.setVisibility(View.GONE);
////                    isTitleShow2 = false;
////                } else {
////                    cbTitleShowHind2.setBackgroundResource(R.drawable.arrow_up);
////                    llMainShow2.setVisibility(View.VISIBLE);
////                    isTitleShow2 = true;
////                }
//                break;
//            case R.id.ll_trip_other:
////                if (isTitleShow3) {
////                    cbTitleShowHind3.setBackgroundResource(R.drawable.arrow_down);
////                    llMainShow3.setVisibility(View.GONE);
////                    isTitleShow3 = false;
////                } else {
////                    cbTitleShowHind3.setBackgroundResource(R.drawable.arrow_up);
////                    llMainShow3.setVisibility(View.VISIBLE);
////                    isTitleShow3 = true;
////                }
//                break;
        }
    }

    private void choseCarModel(String[] array, String title, int tag) {
        new ItemDialog(OrderDriverCompeleteActivity.this, array, title, tag, this).show();
        TYPE_TAG = tag;
    }

    @Override
    public void onDialogItemClick(int requestCode, int position, String item) {
        String strName = item;
        if (StringUtils.isNotEmpty(strName, true)) {
            if (1 == TYPE_TAG) {
                tvRoadPaymentModes.setText(strName);
                PAYMENT_MODES_ROAD = position + 1;
            } else if (2 == TYPE_TAG) {
                tvParkingPaymentModes.setText(strName);
                PAYMENT_MODES_PARKING = position + 1;
            } else if (3 == TYPE_TAG) {
                tvOilPaymnetModes.setText(strName);
                PAYMENT_MODES_OIL = position + 1;
            } else if (4 == TYPE_TAG) {
                tvIncomeCurrency.setText(strName);
                INCOMES_CURRENCY_TYPE = position + 1;
            } else if (5 == TYPE_TAG) {
                tvOilGrade.setText(strName);
                OIL_TYPE_TAG = position + 1;
            }

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
            //LogUtils.e("订单完善："+jsonObject.toString());

            if (StringUtils.equals("获取失败，无此订单！", jsonObject.getString("ErrMsg"))) {
                ToastUtils.showShortToastSafe(OrderDriverCompeleteActivity.this, "乘客尚未确认结束，请稍后再完善！");
                finish();
            }
            if (StringUtils.equals(jsonObject.getString("ErrCode"), "1")) {
                ToastUtils.showShortToastSafe(OrderDriverCompeleteActivity.this, jsonObject.getString("ErrMsg"));
                if (isSubmit) {
                    finish();
                }
            }
            if (jsonObject.has("Data")) {
                json = jsonObject.getJSONObject("Data");
                //handler.sendEmptyMessage(1);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 接收返回数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // requestCode标示请求的标示 resultCode表示有数据
        switch (requestCode) {
            case REQUSE8:
                if (requestCode == REQUSE8 && resultCode == RESULT_OK) {
                    String strLocation = data.getStringExtra(LocationSelecteActivity.DATA_STR);
                    Log.i("info", "返回位置" + strLocation);
                    etStartAddress.setText(strLocation);
                }
                break;
            case REQUSE9:
                if (requestCode == REQUSE9 && resultCode == RESULT_OK) {
                    String strLocation = data.getStringExtra(LocationSelecteActivity.DATA_STR);
                    Log.i("info", "返回位置" + strLocation);
                    etEndAddress.setText(strLocation);
                }
                break;
            case REQUSE10:
                if (requestCode == REQUSE10 && resultCode == RESULT_OK) {
                    String strLocation = data.getStringExtra(LocationSelecteActivity.DATA_STR);
                    Log.i("info", "返回位置" + strLocation);
                    etMidAddress.setText(strLocation);
                }

                break;
            case REQUSE11:

                break;

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

    };

    private void showViews() throws JSONException {
        String strRoadBridgeCostType;
        if (StringUtils.equals("1", json.getString("OFCalcETCCalWay").toString())) {
            strRoadBridgeCostType = "现金垫付";
        } else if (StringUtils.equals("2", json.getString("OFCalcETCCalWay").toString())) {
            strRoadBridgeCostType = "电子消费";
        } else {
            strRoadBridgeCostType = "企业结算";
        }
        String strParkingCostType;
        if (StringUtils.equals("1", json.getString("OFCalcETCCalWay").toString())) {
            strParkingCostType = "现金垫付";
        } else if (StringUtils.equals("2", json.getString("OFCalcETCCalWay").toString())) {
            strParkingCostType = "电子消费";
        } else {
            strParkingCostType = "企业结算";
        }
        String strFuelCostType;
        if (StringUtils.equals("1", json.getString("OFCalcETCCalWay").toString())) {
            strFuelCostType = "现金垫付";
        } else if (StringUtils.equals("2", json.getString("OFCalcETCCalWay").toString())) {
            strFuelCostType = "电子消费";
        } else {
            strFuelCostType = "企业结算";
        }
        String strPaymentCurrency;
        if (StringUtils.equals("1", json.getString("OFORealCurrency").toString())) {
            strPaymentCurrency = "人民币";
        } else if (StringUtils.equals("2", json.getString("OFORealCurrency").toString())) {
            strPaymentCurrency = "港币";
        } else if (StringUtils.equals("3", json.getString("OFORealCurrency").toString())) {
            strPaymentCurrency = "美元";
        } else if (StringUtils.equals("4", json.getString("OFORealCurrency").toString())) {
            strPaymentCurrency = "英镑";
        } else if (StringUtils.equals("5", json.getString("OFORealCurrency").toString())) {
            strPaymentCurrency = "欧元";
        } else {
            strPaymentCurrency = "其他";
        }
        etStartTime.setText(StringUtils.repalceEmptyString(StringUtils.tripTime(json.getString("OFStartTime").toString())));
        etEndTime.setText(StringUtils.repalceEmptyString(StringUtils.tripTime(json.getString("OFEndTime").toString())));
        etDurationAccount.setText(StringUtils.repalceEmptyString(json.getString("OFCalcTiems").toString()));
        etStartKilometre.setText(StringUtils.repalceEmptyString(json.getString("OFStartMileage").toString()));
        etEndKilometre.setText(StringUtils.repalceEmptyString(json.getString("OFEndMileage").toString()));
        etCalculateKilometre.setText(StringUtils.repalceEmptyString(json.getString("OFCalcMileage").toString()));
        etStartAddress.setText(StringUtils.repalceEmptyString(json.getString("OFRealStartSite").toString()));
        etEndAddress.setText(StringUtils.repalceEmptyString(json.getString("OFRealEndSite").toString()));
        etMidAddress.setText(StringUtils.repalceEmptyString(json.getString("OFRealGobySite").toString()));

        etRoadBillTotal.setText(StringUtils.repalceEmptyString(json.getString("OFETCCal").toString()));
        tvRoadPaymentModes.setText(strRoadBridgeCostType);
        etRoadBillNum.setText(StringUtils.repalceEmptyString(json.getString("OFCalcetcCalRelated").toString()));
        etParkingBill.setText(StringUtils.repalceEmptyString(json.getString("OFParkingCal").toString()));
        tvParkingPaymentModes.setText(strParkingCostType);
        etParkingBillNum.setText(StringUtils.repalceEmptyString(json.getString("OFCalcParkingCalRelated").toString()));

        etOilMoneyNum.setText(StringUtils.repalceEmptyString(json.getString("OFFuelCal").toString()));
        tvOilPaymnetModes.setText(strFuelCostType);
        etOilBillNum.setText(StringUtils.repalceEmptyString(json.getString("OFCalcfuelCalRelated").toString()));
        tvOilGrade.setText(StringUtils.repalceEmptyString(json.getString("OFSubmitApplyOil").toString()));
        etOilLitre.setText(StringUtils.repalceEmptyString(json.getString("OFSubmitApplyOilLPM").toString()));
        etRealIncome.setText(StringUtils.repalceEmptyString(json.getString("OFORealCollection").toString()));
        tvIncomeCurrency.setText(strPaymentCurrency);

        etStartKilometre.setText(StringUtils.repalceEmptyString(json.getString("OFOVehicleStartMiles").toString()));
        etEndKilometre.setText(StringUtils.repalceEmptyString(json.getString("OFOVehicleEndMiles").toString()));
        etTaskOvertime.setText(StringUtils.repalceEmptyString(json.getString("OFOTaskOverTime").toString()));
        etOtherDriverKilometre.setText(StringUtils.repalceEmptyString(json.getString("OFORealDriving").toString()));
        etNotPassagerKilometre.setText(StringUtils.repalceEmptyString(json.getString("OFOEmptyRunning").toString()));
        etIncomesDescribe.setText(StringUtils.repalceEmptyString(json.getString("OFOCollectionRemark").toString()));


    }

    String strTime;

    private void showTimePick(final TextView tv) {

        if (timeDialog == null) {

            TimePickerDialog.Builder builder = new TimePickerDialog.Builder(this);
            timeDialog = builder.setOnTimeSelectedListener(new TimePickerDialog.OnTimeSelectedListener() {
                @Override
                public void onTimeSelected(int[] times) {

                    String s = times[0] + "";
                    String s1 = times[1] + "";

                    if (times[1] < 10) {
                        s1 = "0" + times[1];
                    }
                    strTime = s + ":" + s1;
                    tv.setText(strTime);

                }
            }).create();

        }
        timeDialog.show();

    }


}
