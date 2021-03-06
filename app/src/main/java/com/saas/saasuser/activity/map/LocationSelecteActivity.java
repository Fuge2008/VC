package com.saas.saasuser.activity.map;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.saas.saasuser.R;
import com.saas.saasuser.activity.AddTripApplyActivity;
import com.saas.saasuser.activity.CitySelectActivity;
import com.saas.saasuser.adapter.SearchAddressAdapter;
import com.saas.saasuser.application.MyApplication;
import com.saas.saasuser.entity.PoiData;
import com.saas.saasuser.util.LogUtils;
import com.saas.saasuser.util.StringUtils;
import com.saas.saasuser.util.ToastUtils;
import com.saas.saasuser.util.Util;
import com.saas.saasuser.view.ClearEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocationSelecteActivity extends FragmentActivity implements
        AMap.OnMarkerClickListener, AMap.InfoWindowAdapter, PoiSearch.OnPoiSearchListener, Inputtips.InputtipsListener, AMap.OnCameraChangeListener, LocationSource,
        AMapLocationListener {
    private static String TAG = LocationSelecteActivity.class.getSimpleName();
    @BindView(R.id.ce_input_location)
    ClearEditText ceInputLocation;

    @BindView(R.id.lv_search_location)
    ListView lvSearchLocation;

    @BindView(R.id.tv_show_city)
    TextView tvShowCity;
    @BindView(R.id.ll_location)
    LinearLayout llLocation;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;

    public static final int REQUSE1 = 1;
    @BindView(R.id.head_more)
    LinearLayout headMore;
    @BindView(R.id.rl_map)
    RelativeLayout rlMap;


    private String cityName = "深圳";
    private LatLng start_Latlng = new LatLng(22.6650909715, 114.0693295678);
    public static final String DATA_STR = "DATA_STR_CODE";


    private SearchAddressAdapter searchAddressAdapter = null;
    private List<PoiData> searchAddresses = new ArrayList<PoiData>();


    private AMap aMap;
    private MapView mapView;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;


    private String keyWord = "";// 要输入的poi搜索关键字
    private ProgressDialog progDialog = null;// 搜索时进度条

    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private ImageView iv_loaction;
    private AnimationDrawable ad_ocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selecte);
        MyApplication.getInstance().addActivity(this);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        ButterKnife.bind(this);
        Util.setHeadTitleMore(this, "位置选取", true);
        headMore.setVisibility(View.GONE);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//禁止键盘弹出
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.setOnCameraChangeListener(this);
            setUpMap();
        }

        initLocation();


        iv_loaction = (ImageView) findViewById(R.id.iv_loaction);
        /**
         * 这里设置的是setBackgroundResource，那么你获取的时候通过getBackground
         */
        iv_loaction.setBackgroundResource(R.drawable.location_loading);
        ad_ocation = (AnimationDrawable) iv_loaction.getBackground();
        ad_ocation.start();
        //ad_ocation.stop();


    }

    private void setUpMap() {
        aMap.setOnMarkerClickListener(this);// 添加点击marker监听事件
        aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件

        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
         mLocationOption.setOnceLocation(true);
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                LatLng ll=new LatLng(amapLocation.getLatitude(),amapLocation.getLongitude());
                //然后可以移动到定位点,使用animateCamera就有动画效果
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));//参数提示:1.经纬度 2.缩放级别

            } else {

            }
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

            // mLocationOption.setInterval(2000);
            mLocationOption.setNeedAddress(true);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();

        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onCameraChange(CameraPosition position) {


    }


    @Override
    public void onCameraChangeFinish(CameraPosition position) {
        start_Latlng = position.target;
        LogUtils.e("定位返回信息：" + start_Latlng.latitude + start_Latlng.longitude);
//        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(latLng.latitude,
//                latLng.longitude), 1000));//设置周边搜索的中心点以及半径
        handler.sendEmptyMessage(0);

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
                if (requestCode == REQUSE1 && resultCode == RESULT_OK) {
                    String city = data.getStringExtra(CitySelectActivity.DATA_STR);
                    Log.i("info", "返回位置" + city);
                    tvShowCity.setText(city);
                }
                break;

        }

    }


    private void initLocation() {

        ceInputLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence keyword, int start, int before, int count) {
                keyWord = keyword.toString();
                cityName = tvShowCity.getText().toString();
                if (StringUtils.isNotEmpty(keyword, true)) {
                    tvCancel.setVisibility(View.VISIBLE);
                    lvSearchLocation.setVisibility(View.VISIBLE);
                    InputtipsQuery inputquery = new InputtipsQuery(keyWord, cityName);
                    Inputtips inputTips = new Inputtips(LocationSelecteActivity.this, inputquery);
                    inputTips.setInputtipsListener(LocationSelecteActivity.this);
                    inputTips.requestInputtipsAsyn();
                } else {
                    tvCancel.setVisibility(View.GONE);
                    lvSearchLocation.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence keyword, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable keyword) {
            }
        });


        searchAddressAdapter = new SearchAddressAdapter(this, R.layout.item_search_address, searchAddresses);
        lvSearchLocation.setAdapter(searchAddressAdapter);
        lvSearchLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String address = searchAddresses.get(position).getName();
                if (StringUtils.isNotEmpty(address, true)) {
                    Intent intent = new Intent(LocationSelecteActivity.this, AddTripApplyActivity.class);
                    intent.putExtra(DATA_STR, address);
                    setResult(RESULT_OK, intent);
                    finish();// 结束之后会将结果传回
                } else {
                    ToastUtils.showShortToastSafe(LocationSelecteActivity.this, "地址为空，请重新选取！");
                }

            }
        });
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();

        return false;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public View getInfoWindow(final Marker marker) {
        View view = getLayoutInflater().inflate(R.layout.poikeywordsearch_uri, null);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(marker.getTitle());

        TextView snippet = (TextView) view.findViewById(R.id.snippet);
        snippet.setText(marker.getSnippet());
        ImageButton button = (ImageButton) view
                .findViewById(R.id.start_amap_app);
        // 调起高德地图app
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng latLng = marker.getPosition();
                startActivity(new Intent(LocationSelecteActivity.this, ComponentActivity.class).putExtra("lat", latLng.latitude).putExtra("lng", latLng.longitude));
            }
        });
        return view;
    }


    /**
     * poi没有搜索到数据，返回一些推荐城市的信息
     */
    private void showSuggestCity(List<SuggestionCity> cities) {
        String infomation = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
                    + cities.get(i).getCityCode() + "城市编码:"
                    + cities.get(i).getAdCode() + "\n";
        }
        ToastUtils.showShortToastSafe(LocationSelecteActivity.this, infomation);

    }


    /**
     * POI信息查询回调方法
     */
    @Override
    public void onPoiSearched(PoiResult result, int rCode) {

        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    // 取得搜索到的poiitems有多少页
                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始

                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                    searchAddresses.clear();
                    if (poiItems != null && poiItems.size() > 0) {

                        for (int i = 0; i < poiItems.size(); i++) {
                            searchAddresses.add(i, new PoiData(poiItems.get(i).getTitle(), poiItems.get(i).getAdName()));
                            LogUtils.e("搜素结果打印2：" + poiItems.get(i).toString());
                        }
                        ToastUtils.showShortToastSafe(LocationSelecteActivity.this, "提示：" + poiItems.get(0).getAdName());
                        searchAddressAdapter.notifyDataSetChanged();
                        aMap.clear();// 清理之前的图标
                        PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
                        poiOverlay.removeFromMap();
                        poiOverlay.addToMap();
                        poiOverlay.zoomToSpan();
                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
                        showSuggestCity(suggestionCities);
                    } else {

                        ToastUtils.showShortToastSafe(LocationSelecteActivity.this, "没有搜索结果");
                    }
                }
            } else {
                ToastUtils.showShortToastSafe(LocationSelecteActivity.this, "没有搜索结果");
            }
        } else {

            ToastUtils.showShortToastSafe(LocationSelecteActivity.this, rCode);
        }

    }

    @Override
    public void onPoiItemSearched(PoiItem item, int rCode) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {// 正确返回
            // List<String> listString = new ArrayList<String>();
            // searchAddressAdapter.clear();
            searchAddresses.clear();
            for (int i = 0; i < tipList.size(); i++) {
                searchAddresses.add(i, new PoiData(tipList.get(i).getName(), tipList.get(i).getAddress()));
                LogUtils.e("搜素结果打印：" + tipList.get(i).toString());

            }
           // ToastUtils.showShortToastSafe(LocationSelecteActivity.this, "提示：" + tipList.get(0).getName());
            searchAddressAdapter.notifyDataSetChanged();
        } else {
            ToastUtils.showShortToastSafe(LocationSelecteActivity.this, rCode);
        }

    }


    @OnClick({R.id.ll_location, R.id.tv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.ll_location:
                Intent intent = new Intent(LocationSelecteActivity.this, CitySelectActivity.class);
                startActivityForResult(intent, REQUSE1);
                break;
            case R.id.tv_cancel:
                if (StringUtils.isNotEmpty(keyWord, true)) {
                    currentPage = 0;
                    query = new PoiSearch.Query(keyWord, "", cityName);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
                    query.setPageSize(30);// 设置每页最多返回多少条poiitem
                    query.setPageNum(currentPage);// 设置查第一页

                    poiSearch = new PoiSearch(this, query);
                    poiSearch.setOnPoiSearchListener(this);
                    poiSearch.searchPOIAsyn();
                } else {
                    ToastUtils.showShortToastSafe(LocationSelecteActivity.this, "关键字不能为空!");
                }

                break;
        }
    }


    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    GeocodeSearch geocoderSearch = new GeocodeSearch(LocationSelecteActivity.this);
                    geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {

                        @Override
                        public void onGeocodeSearched(GeocodeResult result, int rCode) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onRegeocodeSearched(RegeocodeResult result, int rCode) {

                            String formatAddress = result.getRegeocodeAddress().getFormatAddress();
                            Log.e("formatAddress", "formatAddress:" + formatAddress);
                            Log.e("formatAddress", "rCode:" + rCode);
                            ceInputLocation.setText(formatAddress);

                        }
                    });
                    LatLonPoint lp = new LatLonPoint(start_Latlng.latitude, start_Latlng.longitude);
                    RegeocodeQuery query = new RegeocodeQuery(lp, 100, GeocodeSearch.AMAP);
                    geocoderSearch.getFromLocationAsyn(query);
                    break;
                case 2:

                    break;
                case 3:

                    break;


                default:
                    break;
            }
        }
    };
}


