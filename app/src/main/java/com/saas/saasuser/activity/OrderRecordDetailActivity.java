package com.saas.saasuser.activity;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.trace.LBSTraceClient;
import com.amap.api.trace.TraceListener;
import com.amap.api.trace.TraceLocation;
import com.saas.saasuser.R;
import com.saas.saasuser.activity.map.AMapUtil;
import com.saas.saasuser.activity.map.DbAdapter;
import com.saas.saasuser.activity.map.PathRecord;
import com.saas.saasuser.activity.map.TraceRePlay;
import com.saas.saasuser.application.MyApplication;
import com.saas.saasuser.util.LogUtils;
import com.saas.saasuser.util.Util;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 订单记录详情
 */
public class OrderRecordDetailActivity extends BaseActivity implements
        AMap.OnMapLoadedListener, TraceListener {

    @BindView(R.id.displaybtn)
    ToggleButton displaybtn;

    MapView mMapView;
    @BindView(R.id.rb_record_original)
    RadioButton rbRecordOriginal;
    @BindView(R.id.rb_record_rectify)
    RadioButton rbRecordRectify;
    @BindView(R.id.rg_record)
    RadioGroup rgRecord;
    private String strStartAddress, strEndAddress, orderId, strDate, strTime, strDriverName, strUserName, strKilometer, strSpendMoney, strWeek, strRole, strSpendTime;

    private final static int AMAP_LOADED = 2;
    private AMap mAMap;
    private Marker mOriginStartMarker, mOriginEndMarker, mOriginRoleMarker;
    private Marker mGraspStartMarker, mGraspEndMarker, mGraspRoleMarker;
    private Polyline mOriginPolyline, mGraspPolyline;

    private int mRecordItemId;
    private List<LatLng> mOriginLatLngList;
    private List<LatLng> mGraspLatLngList;
    private boolean mGraspChecked = false;
    private boolean mOriginChecked = true;
    private ExecutorService mThreadPool;
    private TraceRePlay mRePlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_record_detail);
        ButterKnife.bind(this);
        mMapView = (MapView) findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        MyApplication.getInstance().addActivity(this);

        Util.setHeadTitleMore(this, "记录详情", true);

        initData();
 //       Intent recordIntent = getIntent();
        int threadPoolSize = Runtime.getRuntime().availableProcessors() * 2 + 3;
        mThreadPool = Executors.newFixedThreadPool(threadPoolSize);
//        if (recordIntent != null) {
//            mRecordItemId = recordIntent.getIntExtra(RecordActivity.RECORD_ID,
//                    -1);
//        }
        initMap();
        LogUtils.e("订单id:"+orderId);
    }

    private void initMap() {
        if (mAMap == null) {
            mAMap = mMapView.getMap();
            mAMap.setOnMapLoadedListener(this);
        }
    }


    private void startMove() {
        if (mRePlay != null) {
            mRePlay.stopTrace();
        }
        if (mOriginChecked) {
            mRePlay = rePlayTrace(mOriginLatLngList, mOriginRoleMarker);
        } else if (mGraspChecked) {
            mRePlay = rePlayTrace(mGraspLatLngList, mGraspRoleMarker);
        }
    }

    /**
     * 轨迹回放方法
     */
    private TraceRePlay rePlayTrace(List<LatLng> list, final Marker updateMarker) {
        TraceRePlay replay = new TraceRePlay(list, 100,
                new TraceRePlay.TraceRePlayListener() {

                    @Override
                    public void onTraceUpdating(LatLng latLng) {
                        if (updateMarker != null) {
                            updateMarker.setPosition(latLng); // 更新现轨迹回放
                        }
                    }

                    @Override
                    public void onTraceUpdateFinish() {
                        displaybtn.setChecked(false);
                        displaybtn.setClickable(true);
                    }
                });
        mThreadPool.execute(replay);
        return replay;
    }

    /**
     * 将纠偏后轨迹小车设置到起点
     */
    private void resetGraspRole() {
        if (mGraspLatLngList == null) {
            return;
        }
        LatLng startLatLng = mGraspLatLngList.get(0);
        if (mGraspRoleMarker != null) {
            mGraspRoleMarker.setPosition(startLatLng);
        }
    }

    /**
     * 将原始轨迹小车设置到起点
     */
    private void resetOriginRole() {
        if (mOriginLatLngList == null) {
            return;
        }
        LatLng startLatLng = mOriginLatLngList.get(0);
        if (mOriginRoleMarker != null) {
            mOriginRoleMarker.setPosition(startLatLng);
        }
    }


    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case AMAP_LOADED:
                    setupRecord();
                    break;
                default:
                    break;
            }
        }

    };

    public void onBackClick(View view) {
        this.finish();
        if (mThreadPool != null) {
            mThreadPool.shutdownNow();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mThreadPool != null) {
            mThreadPool.shutdownNow();
        }
    }

    private LatLngBounds getBounds() {
        LatLngBounds.Builder b = LatLngBounds.builder();
        if (mOriginLatLngList == null) {
            return b.build();
        }
        for (int i = 0; i < mOriginLatLngList.size(); i++) {
            b.include(mOriginLatLngList.get(i));
        }
        return b.build();

    }

    /**
     * 轨迹数据初始化
     */
    private void setupRecord() {
        // 轨迹纠偏初始化
        LBSTraceClient mTraceClient = new LBSTraceClient(
                getApplicationContext());
        DbAdapter dbhelper = new DbAdapter(this.getApplicationContext());
        dbhelper.open();
        PathRecord mRecord = dbhelper.queryRecordByOrderId(orderId);
        LogUtils.e("轨迹："+mRecord.toString());
        dbhelper.close();
        if (mRecord != null) {
            List<AMapLocation> recordList = mRecord.getPathline();
            AMapLocation startLoc = mRecord.getStartpoint();
            AMapLocation endLoc = mRecord.getEndpoint();
            if (recordList == null || startLoc == null || endLoc == null) {
                return;
            }
            LatLng startLatLng = new LatLng(startLoc.getLatitude(),
                    startLoc.getLongitude());
            LatLng endLatLng = new LatLng(endLoc.getLatitude(),
                    endLoc.getLongitude());
            mOriginLatLngList = AMapUtil.parseLatLngList(recordList);
            addOriginTrace(startLatLng, endLatLng, mOriginLatLngList);

            List<TraceLocation> mGraspTraceLocationList = AMapUtil
                    .parseTraceLocationList(recordList);
            // 调用轨迹纠偏，将mGraspTraceLocationList进行轨迹纠偏处理
            mTraceClient.queryProcessedTrace(1, mGraspTraceLocationList,
                    LBSTraceClient.TYPE_AMAP, this);

            for(int i=0;i<recordList.size();i++){
                LogUtils.e("轨迹参数："+recordList.get(i).toString());
            }
        } else {
        }

    }

    /**
     * 地图上添加原始轨迹线路及起终点、轨迹动画车
     *
     * @param startPoint
     * @param endPoint
     * @param originList
     */
    private void addOriginTrace(LatLng startPoint, LatLng endPoint,
                                List<LatLng> originList) {
        mOriginPolyline = mAMap.addPolyline(new PolylineOptions().color(
                Color.BLUE).addAll(originList));
        mOriginStartMarker = mAMap.addMarker(new MarkerOptions().position(
                startPoint).icon(
                BitmapDescriptorFactory.fromResource(R.drawable.start)));
        mOriginEndMarker = mAMap.addMarker(new MarkerOptions().position(
                endPoint).icon(
                BitmapDescriptorFactory.fromResource(R.drawable.end)));

        try {
            mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(getBounds(),
                    50));
        } catch (Exception e) {
            e.printStackTrace();
        }

        mOriginRoleMarker = mAMap.addMarker(new MarkerOptions().position(
                startPoint).icon(
                BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.drawable.icon_car))));
    }

    /**
     * 设置是否显示原始轨迹
     *
     * @param enable
     */
    private void setOriginEnable(boolean enable) {
        displaybtn.setClickable(true);
        if (mOriginPolyline == null || mOriginStartMarker == null
                || mOriginEndMarker == null || mOriginRoleMarker == null) {
            return;
        }
        if (enable) {
            mOriginPolyline.setVisible(true);
            mOriginStartMarker.setVisible(true);
            mOriginEndMarker.setVisible(true);
            mOriginRoleMarker.setVisible(true);
        } else {
            mOriginPolyline.setVisible(false);
            mOriginStartMarker.setVisible(false);
            mOriginEndMarker.setVisible(false);
            mOriginRoleMarker.setVisible(false);
        }
    }

    /**
     * 地图上添加纠偏后轨迹线路及起终点、轨迹动画车
     */
    private void addGraspTrace(List<LatLng> graspList, boolean mGraspChecked) {
        if (graspList == null || graspList.size() < 2) {
            return;
        }
        LatLng startPoint = graspList.get(0);
        LatLng endPoint = graspList.get(graspList.size() - 1);
        mGraspPolyline = mAMap.addPolyline(new PolylineOptions()
                .setCustomTexture(
                        BitmapDescriptorFactory
                                .fromResource(R.drawable.grasp_trace_line))
                .width(40).addAll(graspList));
        mGraspStartMarker = mAMap.addMarker(new MarkerOptions().position(
                startPoint).icon(
                BitmapDescriptorFactory.fromResource(R.drawable.start)));
        mGraspEndMarker = mAMap.addMarker(new MarkerOptions()
                .position(endPoint).icon(
                        BitmapDescriptorFactory.fromResource(R.drawable.end)));
        mGraspRoleMarker = mAMap.addMarker(new MarkerOptions().position(
                startPoint).icon(
                BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.drawable.icon_car))));
        if (!mGraspChecked) {
            mGraspPolyline.setVisible(false);
            mGraspStartMarker.setVisible(false);
            mGraspEndMarker.setVisible(false);
            mGraspRoleMarker.setVisible(false);
        }
    }

    /**
     * 设置是否显示纠偏后轨迹
     *
     * @param enable
     */
    private void setGraspEnable(boolean enable) {
        displaybtn.setClickable(true);
        if (mGraspPolyline == null || mGraspStartMarker == null
                || mGraspEndMarker == null || mGraspRoleMarker == null) {
            return;
        }
        if (enable) {
            mGraspPolyline.setVisible(true);
            mGraspStartMarker.setVisible(true);
            mGraspEndMarker.setVisible(true);
            mGraspRoleMarker.setVisible(true);
        } else {
            mGraspPolyline.setVisible(false);
            mGraspStartMarker.setVisible(false);
            mGraspEndMarker.setVisible(false);
            mGraspRoleMarker.setVisible(false);
        }
    }

    @Override
    public void onMapLoaded() {
        Message msg = handler.obtainMessage();
        msg.what = AMAP_LOADED;
        handler.sendMessage(msg);
    }

    /**
     * 轨迹纠偏完成数据回调
     */
    @Override
    public void onFinished(int arg0, List<LatLng> list, int arg2, int arg3) {
        addGraspTrace(list, mGraspChecked);
        mGraspLatLngList = list;
    }

    @Override
    public void onRequestFailed(int arg0, String arg1) {
        Toast.makeText(this.getApplicationContext(), "轨迹纠偏失败:" + arg1,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onTraceProcessing(int arg0, int arg1, List<LatLng> arg2) {

    }


   

    private void initData() {//初始化数据
        strStartAddress = this.getIntent().getStringExtra("strStartAddress");
        strEndAddress = this.getIntent().getStringExtra("strEndAddress");
        orderId = this.getIntent().getStringExtra("orderId");
        strDate = this.getIntent().getStringExtra("strDate");
        strTime = this.getIntent().getStringExtra("strTime");
        strWeek = this.getIntent().getStringExtra("strWeek");
        strDriverName = this.getIntent().getStringExtra("strDriverName");
        strUserName = this.getIntent().getStringExtra("strUserName");
       // strKilometer = this.getIntent().getStringExtra("strKilometer");
        //strSpendMoney = this.getIntent().getStringExtra("strSpendMoney");
        strRole = this.getIntent().getStringExtra("strRole");
        //strSpendTime = this.getIntent().getStringExtra("strSpendTime");
    }

//    private void initViews() {//更新数据到页面
//        if(StringUtils.equals("1",strRole)){
//            //ivDriverAvatar
//             tvDriverName.setText(StringUtils.repalceEmptyString(strDriverName));
//        }else if(StringUtils.equals("2",strRole)){
//            tvDriverName.setText(StringUtils.repalceEmptyString(strUserName));
//        }
//        tvSpendTime.setText(StringUtils.repalceEmptyString(strSpendTime)+"min");
//        tvDiverKilomite.setText(StringUtils.repalceEmptyString(strKilometer)+"km");
//        tvSpendMoney.setText("¥"+StringUtils.repalceEmptyString(strSpendMoney));
//        tvStartAddress.setText(StringUtils.repalceEmptyString(strStartAddress));
//        tvEndAddress.setText(StringUtils.repalceEmptyString(strEndAddress));
//        tvStartTime.setText(StringUtils.repalceEmptyString(StringUtils.tripData(strDate)+"   "+StringUtils.repalceEmptyString(strTime)));
//        tvStartWeek.setText(StringUtils.repalceEmptyString(strWeek));
//        tvEndTime.setText("暂无");//待定
//        tvEndWeek.setText("暂无");//待定
//    }


    @OnClick({R.id.displaybtn, R.id.rb_record_original, R.id.rb_record_rectify})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.displaybtn:
                if (displaybtn.isChecked()) {
                    startMove();
                    displaybtn.setClickable(false);
                }
                break;
            case R.id.rb_record_original:
                mGraspChecked = true;
                mOriginChecked = false;
                rbRecordRectify.setChecked(true);
                rbRecordOriginal.setChecked(false);
                setGraspEnable(true);
                setOriginEnable(false);
                displaybtn.setChecked(false);
                resetGraspRole();
                break;
            case R.id.rb_record_rectify:
                mOriginChecked = true;
                mGraspChecked = false;
                rbRecordRectify.setChecked(false);
                rbRecordOriginal.setChecked(true);
                setGraspEnable(false);
                setOriginEnable(true);
                displaybtn.setChecked(false);
                resetOriginRole();
                break;
        }
    }
}
