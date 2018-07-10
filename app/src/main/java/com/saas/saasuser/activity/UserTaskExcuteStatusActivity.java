package com.saas.saasuser.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.trace.LBSTraceClient;
import com.amap.api.trace.TraceListener;
import com.amap.api.trace.TraceLocation;
import com.amap.api.trace.TraceOverlay;
import com.saas.saasuser.R;
import com.saas.saasuser.activity.map.AMapUtil;
import com.saas.saasuser.activity.map.ComponentActivity;
import com.saas.saasuser.activity.map.DbAdapter;
import com.saas.saasuser.activity.map.PathRecord;
import com.saas.saasuser.activity.map.RecordActivity;
import com.saas.saasuser.application.MyApplication;
import com.saas.saasuser.global.NetIntent;
import com.saas.saasuser.global.NetIntentCallBackListener;
import com.saas.saasuser.util.LogUtils;
import com.saas.saasuser.util.SharedPreferencesUtil;
import com.saas.saasuser.util.StringUtils;
import com.saas.saasuser.util.ToastUtils;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserTaskExcuteStatusActivity extends AppCompatActivity implements LocationSource,
        AMapLocationListener, NetIntentCallBackListener.INetIntentCallBack, TraceListener {
    @BindView(R.id.head_more)
    LinearLayout headMore;
    @BindView(R.id.map)
    MapView mapView;
    @BindView(R.id.cb_my_location)
    CheckBox cbMyLocation;
    @BindView(R.id.cb_return_menu)
    CheckBox cbReturnMenu;
    @BindView(R.id.cb_navigation)
    CheckBox cbNavigation;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.btn_compelete_task)
    Button btnCompeleteTask;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.cb_detail)
    CheckBox cbDetail;
    @BindView(R.id.show_all_dis)
    TextView mResultShow;
    private String strAddress;
    private double lng, lat;
    String orderId,strStatue;
    private SharedPreferencesUtil util;

    private AlertDialog inputDialog;
    private View dialogView;
    private TextView input;
    private Button sureBtn;
    private Button cancleBtn;
    private boolean isStartTrip = false;

    private final static int CALLTRACE = 0;
    private MapView mMapView;
    private AMap mAMap;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private PolylineOptions mPolyoptions, tracePolytion;
    private Polyline mpolyline;
    private PathRecord record;
    private long mStartTime;
    private long mEndTime;
    //private ToggleButton btn;
    private DbAdapter DbHepler;
    private List<TraceLocation> mTracelocationlist = new ArrayList<TraceLocation>();
    private List<TraceOverlay> mOverlayList = new ArrayList<TraceOverlay>();
    private List<AMapLocation> recordList = new ArrayList<AMapLocation>();
    private int tracesize = 30;
    private int mDistance = 0;
    private TraceOverlay mTraceoverlay;
    private Marker mlocMarker;
   // private String strOrderStatus;//标记订单状态

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);// 不显示程序的标题栏
        setContentView(R.layout.activity_user_task_excute_status);
        MyApplication.getInstance().addActivity(this);
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        ButterKnife.bind(this);
        util = new SharedPreferencesUtil(this);
        orderId = this.getIntent().getStringExtra("orderId");
        strStatue = this.getIntent().getStringExtra("orderStatue");
        if(!StringUtils.equals("0",strStatue)){
            btnSubmit.setText("结束");
        }
        init();
        initpolyline();

    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (mAMap == null) {
            mAMap = mMapView.getMap();
            setUpMap();
        }
        mTraceoverlay = new TraceOverlay(mAMap);
    }

    protected void saveRecord(List<AMapLocation> list, String time) {
        if (list != null && list.size() > 0) {
            DbHepler = new DbAdapter(this);
            DbHepler.open();
            String duration = getDuration();
            float distance = getDistance(list);
            String average = getAverage(distance);
            String pathlineSring = getPathLineString(list);
            AMapLocation firstLocaiton = list.get(0);
            AMapLocation lastLocaiton = list.get(list.size() - 1);
            String stratpoint = amapLocationToString(firstLocaiton);
            String endpoint = amapLocationToString(lastLocaiton);
            DbHepler.createrecord(String.valueOf(distance), duration, average,
                    pathlineSring, stratpoint, endpoint, time,orderId);
            DbHepler.close();
        } else {
            Toast.makeText(UserTaskExcuteStatusActivity.this, "没有记录到路径", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private String getDuration() {
        return String.valueOf((mEndTime - mStartTime) / 1000f);
    }

    private String getAverage(float distance) {
        return String.valueOf(distance / (float) (mEndTime - mStartTime));
    }

    private float getDistance(List<AMapLocation> list) {
        float distance = 0;
        if (list == null || list.size() == 0) {
            return distance;
        }
        for (int i = 0; i < list.size() - 1; i++) {
            AMapLocation firstpoint = list.get(i);
            AMapLocation secondpoint = list.get(i + 1);
            LatLng firstLatLng = new LatLng(firstpoint.getLatitude(),
                    firstpoint.getLongitude());
            LatLng secondLatLng = new LatLng(secondpoint.getLatitude(),
                    secondpoint.getLongitude());
            double betweenDis = AMapUtils.calculateLineDistance(firstLatLng,
                    secondLatLng);
            distance = (float) (distance + betweenDis);
        }
        return distance;
    }

    private String getPathLineString(List<AMapLocation> list) {
        if (list == null || list.size() == 0) {
            return "";
        }
        StringBuffer pathline = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            AMapLocation location = list.get(i);
            String locString = amapLocationToString(location);
            pathline.append(locString).append(";");
        }
        String pathLineString = pathline.toString();
        pathLineString = pathLineString.substring(0,
                pathLineString.length() - 1);
        return pathLineString;
    }

    private String amapLocationToString(AMapLocation location) {
        StringBuffer locString = new StringBuffer();
        locString.append(location.getLatitude()).append(",");
        locString.append(location.getLongitude()).append(",");
        locString.append(location.getProvider()).append(",");
        locString.append(location.getTime()).append(",");
        locString.append(location.getSpeed()).append(",");
        locString.append(location.getBearing());
        return locString.toString();
    }

    private void initpolyline() {
        mPolyoptions = new PolylineOptions();
        mPolyoptions.width(10f);
        mPolyoptions.color(Color.GRAY);
        tracePolytion = new PolylineOptions();
        tracePolytion.width(40);
        tracePolytion.setCustomTexture(BitmapDescriptorFactory.fromResource(R.drawable.grasp_trace_line));
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        mAMap.setLocationSource(this);// 设置定位监听
        mAMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        mAMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        //mAMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        startlocation();
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();

        }
        mLocationClient = null;
    }

    /**
     * 定位结果回调
     *
     * @param amapLocation 位置信息类
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                LatLng mylocation = new LatLng(amapLocation.getLatitude(),
                        amapLocation.getLongitude());
                strAddress = amapLocation.getAddress();
                lat = amapLocation.getLatitude();
                lng = amapLocation.getLongitude();
                mAMap.moveCamera(CameraUpdateFactory.changeLatLng(mylocation));
                if (isStartTrip) {
                    record.addpoint(amapLocation);
                    mPolyoptions.add(mylocation);
                    mTracelocationlist.add(AMapUtil.parseTraceLocation(amapLocation));
                    redrawline();
                    if (mTracelocationlist.size() > tracesize - 1) {
                        trace();
                    }
                }
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": "
                        + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    /**
     * 开始定位。
     */
    private void startlocation() {
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            // 设置定位监听
            mLocationClient.setLocationListener(this);
            // 设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

            mLocationOption.setInterval(2000);
            mLocationOption.setNeedAddress(true);

            // 设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mLocationClient.startLocation();

        }
    }

    /**
     * 实时轨迹画线
     */
    private void redrawline() {
        if (mPolyoptions.getPoints().size() > 1) {
            if (mpolyline != null) {
                mpolyline.setPoints(mPolyoptions.getPoints());
            } else {
                mpolyline = mAMap.addPolyline(mPolyoptions);
            }
        }
//		if (mpolyline != null) {
//			mpolyline.remove();
//		}
//		mPolyoptions.visible(true);
//		mpolyline = mAMap.addPolyline(mPolyoptions);
//			PolylineOptions newpoly = new PolylineOptions();
//			mpolyline = mAMap.addPolyline(newpoly.addAll(mPolyoptions.getPoints()));
//		}
    }

    @SuppressLint("SimpleDateFormat")
    private String getcueDate(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd  HH:mm:ss ");
        Date curDate = new Date(time);
        String date = formatter.format(curDate);
        return date;
    }

    public void record(View view) {
        Intent intent = new Intent(UserTaskExcuteStatusActivity.this, RecordActivity.class);
        startActivity(intent);
    }

    private void trace() {
        List<TraceLocation> locationList = new ArrayList<>(mTracelocationlist);
        LBSTraceClient mTraceClient = new LBSTraceClient(getApplicationContext());
        mTraceClient.queryProcessedTrace(1, locationList, LBSTraceClient.TYPE_AMAP, this);
        TraceLocation lastlocation = mTracelocationlist.get(mTracelocationlist.size() - 1);
        mTracelocationlist.clear();
        mTracelocationlist.add(lastlocation);
    }

    /**
     * 轨迹纠偏失败回调。
     *
     * @param i
     * @param s
     */
    @Override
    public void onRequestFailed(int i, String s) {
        mOverlayList.add(mTraceoverlay);
        mTraceoverlay = new TraceOverlay(mAMap);
    }

    @Override
    public void onTraceProcessing(int i, int i1, List<LatLng> list) {

    }

    /**
     * 轨迹纠偏成功回调。
     *
     * @param lineID      纠偏的线路ID
     * @param linepoints  纠偏结果
     * @param distance    总距离
     * @param waitingtime 等待时间
     */
    @Override
    public void onFinished(int lineID, List<LatLng> linepoints, int distance, int waitingtime) {
        if (lineID == 1) {
            if (linepoints != null && linepoints.size() > 0) {
                mTraceoverlay.add(linepoints);
                mDistance += distance;
                mTraceoverlay.setDistance(mTraceoverlay.getDistance() + distance);
                if (mlocMarker == null) {
                    mlocMarker = mAMap.addMarker(new MarkerOptions().position(linepoints.get(linepoints.size() - 1))
                            .icon(BitmapDescriptorFactory
                                    .fromResource(R.drawable.point))
                            .title("距离：" + mDistance + "米"));
                    mlocMarker.showInfoWindow();
                } else {
                    mlocMarker.setTitle("距离：" + mDistance + "米");
                    Toast.makeText(UserTaskExcuteStatusActivity.this, "距离" + mDistance, Toast.LENGTH_SHORT).show();
                    mlocMarker.setPosition(linepoints.get(linepoints.size() - 1));
                    mlocMarker.showInfoWindow();
                }
            }
        } else if (lineID == 2) {
            if (linepoints != null && linepoints.size() > 0) {
                mAMap.addPolyline(new PolylineOptions()
                        .color(Color.RED)
                        .width(40).addAll(linepoints));
            }
        }

    }

    /**
     * 最后获取总距离
     *
     * @return
     */
    private int getTotalDistance() {
        int distance = 0;
        for (TraceOverlay to : mOverlayList) {
            distance = distance + to.getDistance();
        }
        return distance;
    }

    @OnClick({R.id.cb_my_location, R.id.cb_return_menu, R.id.cb_navigation, R.id.cb_detail, R.id.btn_submit, R.id.btn_compelete_task, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cb_my_location:
                break;
            case R.id.cb_return_menu:
                util.setOrderRole("1");
                startActivity(new Intent(UserTaskExcuteStatusActivity.this, MainActivity.class).putExtra("showFloatView", "1"));
                break;
            case R.id.cb_navigation:
                startActivity(new Intent(UserTaskExcuteStatusActivity.this, ComponentActivity.class));
                break;
            case R.id.cb_detail:
                //startActivity(new Intent(UserTaskExcuteStatusActivity.this, TraceUpdateActivity.class));
                ToastUtils.showShortToastSafe(UserTaskExcuteStatusActivity.this,"当前服务器正忙！");
                break;
            case R.id.btn_submit:
                dialogView = LayoutInflater.from(UserTaskExcuteStatusActivity.this).inflate(R.layout.dialog_confirm, null);
                input = (TextView) dialogView.findViewById(R.id.input);
                sureBtn = (Button) dialogView.findViewById(R.id.sureBtn);
                cancleBtn = (Button) dialogView.findViewById(R.id.cancleBtn);
                if (StringUtils.equals(btnSubmit.getText().toString(), "结束")) {
                    input.setText("     确认已经抵达目的地，并结束订单？");
                    sureBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(isStartTrip){
                                handler.sendEmptyMessage(2);
                            }
                            btnSubmit.setVisibility(View.GONE);
                            btnCompeleteTask.setVisibility(View.VISIBLE);
                            btnNext.setVisibility(View.VISIBLE);

                            LogUtils.e("orderid:" + orderId + "strAddress:" + strAddress + "lng:" + String.valueOf(lng) + "lat:" + String.valueOf(lat));
                            new NetIntent().client_userFinishTrip(util.getUserId(),util.getCompanyId(),orderId, "2", strAddress, String.valueOf(lng), String.valueOf(lat), "20", "25", new NetIntentCallBackListener(UserTaskExcuteStatusActivity.this));//TODO 乘客： 结束用车，计划里数，实际里数待定 乘客：开始用车
                            // new NetIntent().client_driverFinishSubmitData(util.getUserId(),orderId,"1","乘客经纬度",new NetIntentCallBackListener(this));//  TODO       乘客：到目的地，提交数据，经纬度数据待定
                            inputDialog.dismiss();
                            dialogView = null;
                        }
                    });
                    cancleBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            inputDialog.dismiss();
                            dialogView = null;
                        }
                    });

                    inputDialog = new AlertDialog.Builder(UserTaskExcuteStatusActivity.this)
                            .setView(dialogView)
                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {

                                }
                            }).setCancelable(false).create();
                    inputDialog.show();


                } else if (StringUtils.equals(btnSubmit.getText().toString(), "开始")) {
                    input.setText("      一旦开始行程，就不能取消，会产生额外费用，确认开启行程？");
                    sureBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            btnSubmit.setText("结束");
                                handler.sendEmptyMessage(1);

                            LogUtils.e("orderid:" + orderId + "strAddress:" + strAddress + "lng:" + String.valueOf(lng) + "lat:" + String.valueOf(lat));
                            new NetIntent().client_userFinishTrip(util.getUserId(),util.getCompanyId(),orderId, "1", strAddress, String.valueOf(lng), String.valueOf(lat), "0", "0", new NetIntentCallBackListener(UserTaskExcuteStatusActivity.this));//TODO  乘客：开始用车
                            inputDialog.dismiss();
                            dialogView = null;
                        }
                    });
                    cancleBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            inputDialog.dismiss();
                            dialogView = null;
                        }
                    });

                    inputDialog = new AlertDialog.Builder(UserTaskExcuteStatusActivity.this)
                            .setView(dialogView)
                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {

                                }
                            }).setCancelable(false).create();
                    inputDialog.show();
                }
                break;
            case R.id.btn_compelete_task:
                //startActivity(new Intent(UserTaskExcuteStatusActivity.this, OrderUserCompeleteActivity.class).putExtra("orderId", orderId));
                startActivity(new Intent(UserTaskExcuteStatusActivity.this, OrderCompeleteListActivity.class));

                break;
            case R.id.btn_next:
                finish();
                break;

        }
    }


    @Override
    public void onError(Request request, Exception e) {
    }

    @Override
    public void onResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            LogUtils.e("乘客执行订单数据："+jsonObject.toString());
            ToastUtils.showShortToastSafe(UserTaskExcuteStatusActivity.this, jsonObject.getString("ErrMsg"));
            if (StringUtils.equals(jsonObject.getString("ErrCode"), "1")) {
                if (jsonObject.has("Data")) {

                } else {
                     finish();
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
                    //开始记录行程
                    isStartTrip = true;
                    mAMap.clear(true);
                    if (record != null) {
                        record = null;
                    }
                    record = new PathRecord();
                    mStartTime = System.currentTimeMillis();
                    record.setDate(getcueDate(mStartTime));
                    mResultShow.setVisibility(View.VISIBLE);
                    mResultShow.setText("总距离");
                    break;
                case 2:
                    //停止记录行程
                    isStartTrip = false;
                    mEndTime = System.currentTimeMillis();
                    mOverlayList.add(mTraceoverlay);
                    DecimalFormat decimalFormat = new DecimalFormat("0.0");
                    mResultShow.setText(
                            decimalFormat.format(getTotalDistance() / 1000d) + "KM");
                    LBSTraceClient mTraceClient = new LBSTraceClient(getApplicationContext());
                    mTraceClient.queryProcessedTrace(2, AMapUtil.parseTraceLocationList(record.getPathline()), LBSTraceClient.TYPE_AMAP, UserTaskExcuteStatusActivity.this);
                    saveRecord(record.getPathline(), record.getDate());

                    break;
                case 3:

                    break;


                default:
                    break;
            }
        }
    };
    @Override
    protected void onStop() {
        super.onStop();
        util.setOrderId(orderId);
        util.setOrderRole("1");//标志未完成订单所属身份
        //util.setOrderStatus();
    }


}
