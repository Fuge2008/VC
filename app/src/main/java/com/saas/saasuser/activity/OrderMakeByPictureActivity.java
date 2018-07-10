package com.saas.saasuser.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.liangmutian.mypicker.TimePickerDialog;
import com.saas.saasuser.R;
import com.saas.saasuser.application.MyApplication;
import com.saas.saasuser.global.NetIntent;
import com.saas.saasuser.global.NetIntentCallBackListener;
import com.saas.saasuser.util.LogUtils;
import com.saas.saasuser.util.SharedPreferencesUtil;
import com.saas.saasuser.util.StringUtils;
import com.saas.saasuser.util.TimeUtils;
import com.saas.saasuser.util.Util;
import com.saas.saasuser.view.AvatarStudio;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 图片下单
 */
public class OrderMakeByPictureActivity extends BaseActivity implements NetIntentCallBackListener.INetIntentCallBack {

    @BindView(R.id.head_more)
    LinearLayout headMore;
    @BindView(R.id.tv_add_driver)
    TextView tvAddDriver;
    @BindView(R.id.iv_add_driver)
    ImageView ivAddDriver;
    @BindView(R.id.ll_add_driver)
    LinearLayout llAddDriver;
    @BindView(R.id.tv_add_car)
    TextView tvAddCar;
    @BindView(R.id.iv_add_car)
    ImageView ivAddCar;
    @BindView(R.id.ll_add_car)
    LinearLayout llAddCar;
    @BindView(R.id.tv_trip_date)
    TextView tvTripDate;
    @BindView(R.id.iv_add_date)
    ImageView ivAddDate;
    @BindView(R.id.ll_add_date)
    LinearLayout llAddDate;
    @BindView(R.id.tv_add_passenger)
    TextView tvAddPassenger;
    @BindView(R.id.iv_add_passenger)
    ImageView ivAddPassenger;
    @BindView(R.id.ll_add_passenger)
    LinearLayout llAddPassenger;
    @BindView(R.id.tv_trip_start_time)
    TextView tvTripStartTime;
    @BindView(R.id.iv_trip_start_time)
    ImageView ivTripStartTime;
    @BindView(R.id.ll_trip_start_time)
    LinearLayout llTripStartTime;
    @BindView(R.id.tv_car_modes)
    TextView tvCarModes;
    @BindView(R.id.iv_car_modes)
    ImageView ivCarModes;
    @BindView(R.id.ll_car_modes)
    LinearLayout llCarModes;
    @BindView(R.id.btn_compelete)
    Button btnCompelete;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.ll_contral_show)
    LinearLayout llContralShow;
    @BindView(R.id.iv_car_image)
    ImageView ivCarImage;
    private boolean isFirstMakeOrder = true;
    private SharedPreferencesUtil util;
    private JSONObject json;

    public static final int REQUSE27 = 27;
    private Dialog timeDialog;
    private String filePath;


    private AlertDialog inputDialog;
    private View dialogView;
    private EditText input;
    private Button sureBtn;
    private Button cancleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_make_by_picture);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        util = new SharedPreferencesUtil(this);
        Util.setHeadTitleMore(this, "图片下单", true);
        tvTripStartTime.setText(StringUtils.tripTime(TimeUtils.getNowTime()));
        tvTripDate.setText(StringUtils.tripData(TimeUtils.getNowTime()));
        new NetIntent().client_getDataBeforeMakeOrder(util.getCompanyId(), new NetIntentCallBackListener(OrderMakeByPictureActivity.this));   //TODO  初始化数据
    }

    @OnClick({R.id.head_more, R.id.iv_add_driver, R.id.ll_add_driver, R.id.iv_add_car, R.id.ll_add_car, R.id.iv_add_date, R.id.ll_add_date, R.id.iv_add_passenger, R.id.ll_add_passenger, R.id.iv_trip_start_time, R.id.ll_trip_start_time, R.id.iv_car_modes, R.id.ll_car_modes, R.id.btn_compelete, R.id.btn_submit, R.id.ll_contral_show,R.id.iv_car_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_more:
                //showDialog();
                showPhotoDialog();
                break;
            case R.id.iv_add_driver:
                break;
            case R.id.ll_add_driver:
                break;
            case R.id.iv_add_car:
                break;
            case R.id.ll_add_car:
                break;
//            case R.id.iv_add_date:
//                break;
            case R.id.ll_add_date://获取日期
                Intent intent = new Intent(OrderMakeByPictureActivity.this, CalenderActivity.class).putExtra("activity", "OrderMakeByPictureActivity");
                startActivityForResult(intent, REQUSE27);
                break;
            case R.id.iv_add_passenger:
                break;
            case R.id.ll_add_passenger:
                break;
//            case R.id.iv_trip_start_time:
//                break;
            case R.id.ll_trip_start_time://获取时间
                showTimePick(tvTripStartTime);
                timeDialog = null;
                break;
            case R.id.iv_car_modes:
                break;
            case R.id.ll_car_modes:
                break;
            case R.id.btn_compelete:
                break;
            case R.id.btn_submit:
                // new NetIntent().client_submitOrderByPicture(util.getUserId(),util.getCompanyId(), new NetIntentCallBackListener(OrderMakeByPictureActivity.this));   //TODO  提交订单
                showSubmitDialog();
                break;
            case R.id.ll_contral_show:
                break;
            case R.id.iv_car_image:
                if(StringUtils.isNotEmpty(filePath,true)){
                    startActivity(new Intent(OrderMakeByPictureActivity.this,ShowPictureActivity.class).putExtra("type", 2).putExtra("path", filePath));
                }else {
                    showPhotoDialog();
                }

                break;
        }
    }

    private void showSubmitDialog() {
        dialogView = LayoutInflater.from(OrderMakeByPictureActivity.this).inflate(R.layout.dialog_picture_confirm, null);
        sureBtn = (Button) dialogView.findViewById(R.id.sureBtn);
        cancleBtn = (Button) dialogView.findViewById(R.id.cancleBtn);
        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //todo  确认订单
                    inputDialog.dismiss();
                finish();

            }
        });
        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo 继续转发订单
                inputDialog.dismiss();
            }
        });

        inputDialog = new AlertDialog.Builder(OrderMakeByPictureActivity.this)
                .setView(dialogView)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                     //todo
                    }}).setCancelable(false).create();
        inputDialog.show();
    }

    @Override
    public void onError(Request request, Exception e) {

    }

    @Override
    public void onResponse(String response) {
        System.out.println(response);

        try {
            JSONObject jsonObject = new JSONObject(response);
            json = jsonObject.getJSONObject("Data");

            LogUtils.e("返回数据：" + response);
            if (StringUtils.equals(jsonObject.getString("ErrCode"), "1")) {
                if (json.has("m_Item1")) {


                }

                if (json.has("m_Item2")) {


                }
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
            case REQUSE27:
                if (requestCode == REQUSE27 && resultCode == RESULT_OK) {
                    String strWeek = data.getStringExtra(CalenderActivity.DATA_STR_WEEK);
                    String strDate = data.getStringExtra(CalenderActivity.DATA_STR_DATE);
                    tvTripDate.setText(strDate);

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

    private void showPhotoDialog() {
        new AvatarStudio.Builder(OrderMakeByPictureActivity.this)
                .needCrop(false)
                .setTextColor(Color.BLUE)
                .dimEnabled(true)
                .setAspect(1, 1)
                .setOutput(500, 500)
                .setText("打开相机", "从相册中选取", "取消")
                .show(new AvatarStudio.CallBack() {
                    @Override
                    public void callback(final String uri) {
                        // Picasso.with(MainActivity.this).load(new File(uri)).into(mImageView);
                        setAvataor(uri);
                    }
                });
    }

    private void setAvataor(final String uri) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(uri));
                    new Handler(Looper.getMainLooper())
                            .post(new Runnable() {
                                @Override
                                public void run() {
                                    filePath=uri;
                                    ivCarImage.setImageBitmap(bitmap);
                                }
                            });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
