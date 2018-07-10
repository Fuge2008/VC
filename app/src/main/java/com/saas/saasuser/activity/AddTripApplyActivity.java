package com.saas.saasuser.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.saas.saasuser.util.SharedPreferencesUtil;
import com.saas.saasuser.util.StringUtils;
import com.saas.saasuser.util.TimeUtils;
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

/**
 * 租行申请
 */
public class AddTripApplyActivity extends BaseActivity implements ItemDialog.OnDialogItemClickListener, NetIntentCallBackListener.INetIntentCallBack, CompoundButton.OnCheckedChangeListener {

    public static final int REQUSE1 = 1;
    public static final int REQUSE2 = 2;
    public static final int REQUSE3 = 3;
    public static final int REQUSE4 = 4;
    public static final int REQUSE5 = 5;
    public static final int REQUSE6 = 6;
    public static final int REQUSE7 = 7;
    @BindView(R.id.head_title)
    TextView headTitle;
    @BindView(R.id.head_more)
    LinearLayout headMore;
    @BindView(R.id.tv_trip_modes)
    TextView tvTripModes;
    @BindView(R.id.iv_trip_modes)
    ImageView ivTripModes;
    @BindView(R.id.et_calculate_cost)
    EditText etCalculateCost;
    @BindView(R.id.tv_trip_date)
    TextView tvTripDate;
    @BindView(R.id.iv_trip_date)
    ImageView ivTripDate;
    @BindView(R.id.tv_trip_go_reture)
    TextView tvTripGoReture;
    @BindView(R.id.iv_trip_go_reture)
    ImageView ivTripGoReture;
    @BindView(R.id.et_trip_start_address)
    EditText etTripStartAddress;
    @BindView(R.id.tv_trip_start_time)
    TextView tvTripStartTime;
    @BindView(R.id.iv_trip_start_time)
    ImageView ivTripStartTime;
    @BindView(R.id.et_trip_end_address)
    EditText etTripEndAddress;
    @BindView(R.id.et_trip_purpose)
    EditText etTripPurpose;
    @BindView(R.id.et_trip_passagers_num)
    EditText etTripPassagersNum;
    @BindView(R.id.et_trip_mid_address)
    EditText etTripMidAddress;
    @BindView(R.id.et_trip_end_time)
    TextView etTripEndTime;
    @BindView(R.id.tv_trip_car_grade)
    TextView tvTripCarGrade;
    @BindView(R.id.iv_trip_car_grade)
    ImageView ivTripCarGrade;
    @BindView(R.id.tv_trip_passager_bestow)
    TextView tvTripPassagerBestow;
    @BindView(R.id.cb_trip_passager_bestow)
    CheckBox cbTripPassagerBestow;
    @BindView(R.id.tv_trip_cross_border)
    TextView tvTripCrossBorder;
    @BindView(R.id.cb_trip_cross_border)
    CheckBox cbTripCrossBorder;
    @BindView(R.id.tv_trip_inner_car)
    TextView tvTripInnerCar;
    @BindView(R.id.cb_trip_trip_inner_car)
    CheckBox cbTripTripInnerCar;
    @BindView(R.id.et_trip_peer)
    EditText etTripPeer;
    @BindView(R.id.et_trip_car_requst)
    EditText etTripCarRequst;
    @BindView(R.id.et_trip_driver_requst)
    EditText etTripDriverRequst;
    @BindView(R.id.et_trip_other_requst)
    EditText etTripOtherRequst;
    @BindView(R.id.btn_submit)
    TextView btnSubmit;
    @BindView(R.id.et_trip_week)
    TextView etTripWeek;
    @BindView(R.id.iv_trip_start_address)
    ImageView ivTripStartAddress;
    @BindView(R.id.iv_trip_end_address)
    ImageView ivTripEndAddress;
    @BindView(R.id.iv_trip_mid_address)
    ImageView ivTripMidAddress;
    @BindView(R.id.iv_trip_end_time)
    ImageView ivTripEndTime;
    @BindView(R.id.iv_trip_week)
    ImageView ivTripWeek;
    @BindView(R.id.cb_title_show_hind1)
    ImageView cbTitleShowHind1;
    @BindView(R.id.ll_title_show_hind1)
    LinearLayout llTitleShowHind1;
    @BindView(R.id.cb_title_show_hind2)
    ImageView cbTitleShowHind2;
    @BindView(R.id.ll_title_show_hind2)
    LinearLayout llTitleShowHind2;
    @BindView(R.id.ll_title_1)
    LinearLayout llTitle1;
    @BindView(R.id.ll_title_2)
    LinearLayout llTitle2;
    @BindView(R.id.ll_trip_start_time)
    LinearLayout llTripStartTime;
    @BindView(R.id.ll_trip_end_time)
    LinearLayout llTripEndTime;


    private JSONObject json;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        updateViews();//更新界面
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    break;
            }
        }

        private void updateViews() throws JSONException {
            String strTripModes, strTripCarLevel, strTripGoBack;
            if (StringUtils.equals("1", json.getString("BOTripWay").toString())) {
                strTripModes = "内部车";
            } else if (StringUtils.equals("2", json.getString("BOTripWay").toString())) {
                strTripModes = "租赁车";
            } else if (StringUtils.equals("3", json.getString("BOTripWay").toString())) {
                strTripModes = "出租车";
            } else if (StringUtils.equals("4", json.getString("BOTripWay").toString())) {
                strTripModes = "公交车";
            } else {
                strTripModes = "网约车";

            }
            if (StringUtils.equals("1", json.getString("BOGoBack").toString())) {
                strTripGoBack = "单程往";
            } else if (StringUtils.equals("2", json.getString("BOGoBack").toString())) {
                strTripGoBack = "单程返";
            } else if (StringUtils.equals("3", json.getString("BOGoBack").toString())) {
                strTripGoBack = "往返";
            } else {
                strTripGoBack = "留宿";
            }
            if (StringUtils.equals("1", json.getString("BOAssignCarLevel").toString())) {
                strTripCarLevel = "经济车型";
            } else if (StringUtils.equals("2", json.getString("BOAssignCarLevel").toString())) {
                strTripCarLevel = "舒适车型";
            } else if (StringUtils.equals("3", json.getString("BOAssignCarLevel").toString())) {
                strTripCarLevel = "商务车型";
            } else if (StringUtils.equals("4", json.getString("BOAssignCarLevel").toString())) {
                strTripCarLevel = "豪华车型";
            } else {
                strTripCarLevel = "巴士车型";
            }
            tvTripModes.setText(strTripModes);
            etCalculateCost.setText(StringUtils.repalceEmptyString(json.getString("BOPlanCost").toString()));
            tvTripDate.setText(StringUtils.repalceEmptyString(StringUtils.tripData(json.getString("BOUseDate").toString())));
            etTripWeek.setText(StringUtils.repalceEmptyString(json.getString("BOUseWeek").toString()));
            etCalculateCost.setText(strTripGoBack);
            tvTripStartTime.setText(json.getString("BOUStartTime").toString());//TODO  暂时不填
            etTripStartAddress.setText(StringUtils.repalceEmptyString(json.getString("BOUpCarPlace").toString()));
            etTripEndAddress.setText(StringUtils.repalceEmptyString(json.getString("BODestination").toString()));
            etTripPurpose.setText(StringUtils.repalceEmptyString(json.getString("BOPurposeDsc").toString()));
            etTripPassagersNum.setText(StringUtils.repalceEmptyString(json.getString("BOPassengers").toString()));
            etTripMidAddress.setText(StringUtils.repalceEmptyString(json.getString("BODownCarPlace").toString()));
            etTripEndTime.setText(json.getString(" BOUEndTime").toString());//TODO  暂时不填
            tvTripCarGrade.setText(strTripCarLevel);
            tvTripPassagerBestow.setText(StringUtils.equals("0", json.getString("BOIsSleepover").toString()) ? "不留宿" : "留宿");
            tvTripCrossBorder.setText(StringUtils.equals("0", json.getString("BOIsTransnational").toString()) ? "不跨境" : "跨境");
            tvTripInnerCar.setText(StringUtils.equals("0", json.getString("BOIsSelfCar").toString()) ? "不指定" : "指定");
            etTripPeer.setText(StringUtils.repalceEmptyString(json.getString("BOAssociate").toString()));
            etTripCarRequst.setText(StringUtils.repalceEmptyString(json.getString("BOVehicleRequire").toString()));
            etTripDriverRequst.setText(StringUtils.repalceEmptyString(json.getString("BODriverRequire").toString()));
            etTripOtherRequst.setText(StringUtils.repalceEmptyString(json.getString("OtherRequire").toString()));

        }
    };
    private String[] TRIP_CAR_GRADE;
    private String[] TRIP_MODES;
    private String[] TRIP_GO_RETURN;
    private String[] TRIP_WEEK;
    private int TYPE_TAG, INNER_CAR = 0, CROSS_BORDER = 0, PASSAGER_BESTOW = 0, TRIP_MODES_TYPR = 1, TRIP_GO_RETURN_TYPE = 1, TRIP_CAR_GRADE_TYPE = 1, TRIP_WEEK_TYPE = 1;
    // private TimeSelector timeSelector;
    // private final static String STARTTIME = "1980-01-01 00:00"; //时间控件 开始时间
    //private final static String ENDTIME = "2150-12-31 23:59";//时间控件 结束时间
    //private final static String DAY = "2150-12-31";//日期控件 结束时间
    private SharedPreferencesUtil util;
    private boolean isTitleShow1 = true, isTitleShow2 = true;
    private String reBuiltOrder, orderId;
    private Dialog timeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip_apply);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        Util.setHeadTitleMore(this, "租行申请", true);
        //headMore.setVisibility(View.GONE);
        btnSubmit.setText("确认申请");
        reBuiltOrder = this.getIntent().getStringExtra("reBuiltOrder");
        orderId = this.getIntent().getStringExtra("orderId");
        util = new SharedPreferencesUtil(this);
        //timeSelector = new TimeSelector(this, null, STARTTIME, ENDTIME);
        //etTripEndTime.setText(StringUtils.tripTime(TimeUtils.getNowTime()));
        tvTripStartTime.setText(StringUtils.tripTime(TimeUtils.getNowTime()));
        tvTripDate.setText(StringUtils.tripData(TimeUtils.getNowTime()));
        etTripWeek.setText(TimeUtils.getWeek());
        cbTripCrossBorder.setOnCheckedChangeListener(this);
        cbTripPassagerBestow.setOnCheckedChangeListener(this);
        cbTripTripInnerCar.setOnCheckedChangeListener(this);
        if (StringUtils.equals("1", reBuiltOrder)) {
            new NetIntent().client_replyContactResubmit(util.getUserId(), orderId, new NetIntentCallBackListener(this));//    批复联络详情
            btnSubmit.setText("重新申请");

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
            ToastUtils.showShortToastSafe(AddTripApplyActivity.this, jsonObject.getString("ErrMsg"));
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

    @OnClick({ R.id.iv_trip_date, R.id.iv_trip_start_time, R.id.btn_submit, R.id.iv_trip_start_address, R.id.iv_trip_end_address, R.id.iv_trip_mid_address, R.id.iv_trip_end_time, R.id.iv_trip_week, R.id.ll_title_1, R.id.ll_title_2, R.id.ll_trip_start_time, R.id.ll_trip_end_time,R.id.ll_trip_modes, R.id.ll_trip_go_reture, R.id.ll_trip_car_grade})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.iv_trip_date:
                // timeSelector.show(tvTripDate);
                Intent intent = new Intent(AddTripApplyActivity.this, CalenderActivity.class).putExtra("activity","AddTripApplyActivity");
                startActivityForResult(intent, REQUSE7);
                break;

            case R.id.iv_trip_start_time:
                //timeSelector.show(tvTripStartTime);
                showTimePick(tvTripStartTime);
                timeDialog = null;
                break;

            case R.id.btn_submit:
                String calculateCost = etCalculateCost.getText().toString();

                String tripDate = tvTripDate.getText().toString();

                //ImageView ivTripDate;
                String tripWeek = etTripWeek.getText().toString();

                String tripGoReture = tvTripGoReture.getText().toString();

                //ImageView ivTripGoReture;

                String tripStartAddress = etTripStartAddress.getText().toString();

                String tripStartTime = tvTripStartTime.getText().toString();

                //ImageView ivTripStartTime;

                String tripEndAddress = etTripEndAddress.getText().toString();

                String tripPurpose = etTripPurpose.getText().toString();

                String tripPassagersNum = etTripPassagersNum.getText().toString();

                String tripMidAddress = etTripMidAddress.getText().toString();

                String tripEndTime = etTripEndTime.getText().toString();

                String tripCarGrade = tvTripCarGrade.getText().toString();

                //ImageView ivTripCarGrade;

                String tripPassagerBestow = tvTripPassagerBestow.getText().toString();

                //ImageView ivTripPassagerBestow;

                String tripCrossBorder = tvTripCrossBorder.getText().toString();

                ImageView ivTripCrossBorder;

                String tripInnerCar = tvTripInnerCar.getText().toString();

                //ImageView ivTripTripInnerCar;

                String tripPeer = etTripPeer.getText().toString();

                String tripCarRequst = etTripCarRequst.getText().toString();

                String tripDriverRequst = etTripDriverRequst.getText().toString();

                String tripOtherRequst = etTripOtherRequst.getText().toString();
                if (StringUtils.isNotEmpty("userId", true) && StringUtils.isNotEmpty("userCompanyId", true) && StringUtils.isNotEmpty(TRIP_MODES_TYPR + "", true)
                        && StringUtils.isNotEmpty(calculateCost, true) && StringUtils.isNotEmpty(tripDate, true) && StringUtils.isNotEmpty(tripWeek, true)
                        && StringUtils.isNotEmpty(TRIP_GO_RETURN_TYPE + "", true) && StringUtils.isNotEmpty(tripStartTime, true) && StringUtils.isNotEmpty(tripStartAddress, true)
                        && StringUtils.isNotEmpty(tripEndAddress, true) && StringUtils.isNotEmpty(tripPurpose, true) && StringUtils.isNotEmpty(tripPassagersNum, true)
                        ) {
                    if (dialog == null) {
                        dialog = CustomProgress.show(this, "正在提交..", true, null);
                    }
                    if (StringUtils.equals("0", reBuiltOrder)) {
                        new NetIntent().client_submitOrder(util.getUserId(), util.getCompanyId(), TRIP_MODES_TYPR + "", calculateCost, tripDate,
                                tripWeek, TRIP_GO_RETURN_TYPE + "", tripStartTime, tripStartAddress, tripEndAddress, tripPurpose,
                                tripPassagersNum, tripMidAddress, tripEndTime, TRIP_CAR_GRADE_TYPE + "", PASSAGER_BESTOW + "", CROSS_BORDER + "",
                                INNER_CAR + "", tripPeer, tripCarRequst, tripDriverRequst, tripOtherRequst, new NetIntentCallBackListener(this));//提交订单
                    } else if (StringUtils.equals("1", reBuiltOrder)) {
                        new NetIntent().client_replyContactConfirm(orderId, util.getUserId(), util.getCompanyId(), TRIP_MODES_TYPR + "", calculateCost, tripDate,
                                tripWeek, TRIP_GO_RETURN_TYPE + "", tripStartTime, tripStartAddress, tripEndAddress, tripPurpose,
                                tripPassagersNum, tripMidAddress, tripEndTime, TRIP_CAR_GRADE_TYPE + "", PASSAGER_BESTOW + "", CROSS_BORDER + "",
                                INNER_CAR + "", tripPeer, tripCarRequst, tripDriverRequst, tripOtherRequst, new NetIntentCallBackListener(this));//重新提交订单
                    }


                } else {
                    ToastUtils.showShortToastSafe(AddTripApplyActivity.this, "必填部分不能留空！");
                }

                break;
            case R.id.iv_trip_start_address:
                Intent intent1 = new Intent(AddTripApplyActivity.this, LocationSelecteActivity.class);
                startActivityForResult(intent1, REQUSE1);

                break;
            case R.id.iv_trip_end_address:
                Intent intent2 = new Intent(AddTripApplyActivity.this, LocationSelecteActivity.class);
                startActivityForResult(intent2, REQUSE2);
                break;
            case R.id.iv_trip_mid_address:
                Intent intent3 = new Intent(AddTripApplyActivity.this, LocationSelecteActivity.class);
                startActivityForResult(intent3, REQUSE3);
                break;
            case R.id.iv_trip_end_time:
                //timeSelector.show(etTripEndTime);
                showTimePick(etTripEndTime);
                timeDialog = null;
                break;
            case R.id.iv_trip_week:
                TRIP_MODES = new String[]{"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期天"};
                choseCarModel(TRIP_MODES, "星期选择", 4);
                break;
            case R.id.ll_title_1:
                // ToastUtils.showShortToastSafe(AddTripApplyActivity.this, "点击提示！");
                if (isTitleShow1) {
                    cbTitleShowHind1.setImageDrawable(getResources().getDrawable(R.drawable.icon_expend_down));
                    llTitleShowHind1.setVisibility(View.GONE);
                    isTitleShow1 = false;
                } else {
                    cbTitleShowHind1.setImageDrawable(getResources().getDrawable(R.drawable.icon_expend_up));
                    llTitleShowHind1.setVisibility(View.VISIBLE);
                    isTitleShow1 = true;
                }
                break;
            case R.id.ll_title_2:
                //ToastUtils.showShortToastSafe(AddTripApplyActivity.this, "点击提示！");
                if (isTitleShow2) {
                    cbTitleShowHind2.setImageDrawable(getResources().getDrawable(R.drawable.icon_expend_down));
                    llTitleShowHind2.setVisibility(View.GONE);
                    isTitleShow2 = false;
                } else {
                    cbTitleShowHind2.setImageDrawable(getResources().getDrawable(R.drawable.icon_expend_up));
                    llTitleShowHind2.setVisibility(View.VISIBLE);
                    isTitleShow2 = true;
                }
                break;
            case R.id.ll_trip_start_time:
                showTimePick(tvTripStartTime);
                break;
            case R.id.ll_trip_end_time:
                showTimePick(etTripEndTime);
                break;
            case R.id.ll_trip_modes:
                TRIP_MODES = new String[]{"内部车", "租赁车", "出租车", "公交车", "网约车"};
                choseCarModel(TRIP_MODES, "出行方式", 1);
                break;
            case R.id.ll_trip_go_reture:
                TRIP_GO_RETURN = new String[]{"单程往", "单程返", "往返", "留宿"};
                choseCarModel(TRIP_GO_RETURN, "往返", 2);
                break;
            case R.id.ll_trip_car_grade:
                TRIP_CAR_GRADE = new String[]{"经济车型", "舒适车型", "商务车型", "豪华车型", "巴士车型"};
                choseCarModel(TRIP_CAR_GRADE, "车辆等级", 3);
                break;
        }
    }

    private void choseCarModel(String[] array, String title, int tag) {
        new ItemDialog(AddTripApplyActivity.this, array, title, tag, this).show();
        TYPE_TAG = tag;
    }

    @Override
    public void onDialogItemClick(int requestCode, int position, String item) {
        String strName = item;
        if (StringUtils.isNotEmpty(strName, true)) {
            if (1 == TYPE_TAG) {
                tvTripModes.setText(strName);
                TRIP_MODES_TYPR = position + 1;
            } else if (2 == TYPE_TAG) {
                tvTripGoReture.setText(strName);
                TRIP_GO_RETURN_TYPE = position + 1;
            } else if (3 == TYPE_TAG) {
                tvTripCarGrade.setText(strName);
                TRIP_CAR_GRADE_TYPE = position + 1;
            } else if (4 == TYPE_TAG) {
                etTripWeek.setText(strName);
                TRIP_WEEK_TYPE = position + 1;
            }

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
            case REQUSE1:
                if (requestCode == AddTripApplyActivity.REQUSE1 && resultCode == RESULT_OK) {
                    String strLocation = data.getStringExtra(LocationSelecteActivity.DATA_STR);
                    Log.i("info", "返回位置" + strLocation);
                    etTripStartAddress.setText(strLocation);
                }
                break;
            case REQUSE2:
                if (requestCode == AddTripApplyActivity.REQUSE2 && resultCode == RESULT_OK) {
                    String strLocation = data.getStringExtra(LocationSelecteActivity.DATA_STR);
                    Log.i("info", "返回位置" + strLocation);
                    etTripEndAddress.setText(strLocation);
                }
                break;
            case REQUSE3:
                if (requestCode == AddTripApplyActivity.REQUSE3 && resultCode == RESULT_OK) {
                    String strLocation = data.getStringExtra(LocationSelecteActivity.DATA_STR);
                    Log.i("info", "返回位置" + strLocation);
                    etTripMidAddress.setText(strLocation);
                }

                break;
            case REQUSE4:
                if (requestCode == AddTripApplyActivity.REQUSE4 && resultCode == RESULT_OK) {
                    String strLocation = data.getStringExtra(LocationSelecteActivity.DATA_STR);
                    Log.i("info", "返回位置" + strLocation);
                }
                break;
            case REQUSE5:
                if (requestCode == AddTripApplyActivity.REQUSE5 && resultCode == RESULT_OK) {
                    String strLocation = data.getStringExtra(LocationSelecteActivity.DATA_STR);
                    Log.i("info", "返回位置" + strLocation);
                }
                break;
            case REQUSE6:
                if (requestCode == AddTripApplyActivity.REQUSE6 && resultCode == RESULT_OK) {
                    String strLocation = data.getStringExtra(LocationSelecteActivity.DATA_STR);
                    Log.i("info", "返回位置" + strLocation);
                }
                break;
            case REQUSE7:
                if (requestCode == AddTripApplyActivity.REQUSE7 && resultCode == RESULT_OK) {
                    String strWeek = data.getStringExtra(CalenderActivity.DATA_STR_WEEK);
                    String strDate = data.getStringExtra(CalenderActivity.DATA_STR_DATE);
                    tvTripDate.setText(strDate);
                    etTripWeek.setText(strWeek);
                }
                break;
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_trip_passager_bestow:
                if (isChecked) {
                    tvTripPassagerBestow.setText("留宿");
                    PASSAGER_BESTOW = 1;
                } else {
                    tvTripPassagerBestow.setText("不留宿");
                    PASSAGER_BESTOW = 0;
                }
                break;
            case R.id.cb_trip_cross_border:
                if (isChecked) {
                    tvTripCrossBorder.setText("跨境");
                    CROSS_BORDER = 1;
                } else {
                    tvTripCrossBorder.setText("不跨境");
                    CROSS_BORDER = 0;
                }
                break;
            case R.id.cb_trip_trip_inner_car:
                if (isChecked) {
                    tvTripInnerCar.setText("使用");
                    INNER_CAR = 1;
                } else {
                    tvTripInnerCar.setText("不使用");
                    INNER_CAR = 0;
                }
                break;
        }

    }

    private void showTimePick(final TextView tv) {
        if (timeDialog == null) {
            TimePickerDialog.Builder builder = new TimePickerDialog.Builder(this);
            timeDialog = builder.setOnTimeSelectedListener(new TimePickerDialog.OnTimeSelectedListener() {
                @Override
                public void onTimeSelected(int[] times) {
                    String s = times[0] + "";
                    String s1 = times[1] + "";
                    if (times[0] < 10) {
                        s = "0" + times[0];
                    }
                    if (times[1] < 10) {
                        s1 = "0" + times[1];
                    }
                    String strTime = s + ":" + s1;
                    tv.setText(strTime);


                }
            }).create();
        }

        timeDialog.show();

    }



}
