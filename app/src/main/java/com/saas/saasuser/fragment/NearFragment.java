package com.saas.saasuser.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
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
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.saas.saasuser.R;
import com.saas.saasuser.activity.map.ComponentActivity;
import com.saas.saasuser.activity.map.PoiOverlay;
import com.saas.saasuser.entity.PoiData;
import com.saas.saasuser.util.LogUtils;
import com.saas.saasuser.util.SharedPreferencesUtil;
import com.saas.saasuser.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class NearFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, AMapLocationListener,AMap.OnMarkerClickListener, AMap.InfoWindowAdapter, PoiSearch.OnPoiSearchListener, Inputtips.InputtipsListener, AMap.OnCameraChangeListener, LocationSource{
    private SharedPreferencesUtil util;


    private static NearFragment fragment = null;
    private View view;
    private MapView mapView;
    private AMap aMap;
    private MyLocationStyle myLocationStyle;
    private OnLocationChangedListener mListener;
    private AMapLocationClient locationClient;
    private AMapLocationClientOption clientOption;

    private List<PoiData> searchAddresses = new ArrayList<PoiData>();


    private String keyWord = "汽车站";// 要输入的poi搜索关键字
    private ProgressDialog progDialog = null;// 搜索时进度条

    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private AnimationDrawable ad_ocation;

    private  boolean first=true;


    private String cityName = "深圳";
    private LatLng start_Latlng = new LatLng(22.6650909715, 114.0693295678);


    public static NearFragment newInstance() {
        if (fragment == null) {
            synchronized (NearFragment.class) {
                if (fragment == null) {
                    fragment = new NearFragment();
                }
            }
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            view = View.inflate(getActivity(), R.layout.fragment_near, null);
            initview(savedInstanceState, view);
        util = new SharedPreferencesUtil(getActivity());
        TextView tvTitle = (TextView) view.findViewById(R.id.head_title);
        tvTitle.setText("附近");
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private void initview(Bundle savedInstanceState, View view) {
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.setOnCameraChangeListener(this);
        }
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(12));
        aMap.setLocationSource(this);
        aMap.setMyLocationEnabled(true);

        aMap.setOnMarkerClickListener(this);// 添加点击marker监听事件
        aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件

        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);



    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (locationClient == null) {
            locationClient = new AMapLocationClient(getActivity());
            clientOption = new AMapLocationClientOption();
            locationClient.setLocationListener(this);
            clientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//高精度定位
            clientOption.setNeedAddress(true);
            clientOption.setOnceLocationLatest(true);//设置单次精确定位
            locationClient.setLocationOption(clientOption);
            locationClient.startLocation();
        }

    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (locationClient != null) {
            locationClient.stopLocation();
            locationClient.onDestroy();
        }
        locationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                start_Latlng=new LatLng(amapLocation.getLatitude(),amapLocation.getLongitude());
                keyWord=amapLocation.getCity();
                //然后可以移动到定位点,使用animateCamera就有动画效果
               // aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(start_Latlng, 12));//参数提示:1.经纬度 2.缩放级别

            } else {

            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
        } else {
            aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        }
    }

    /**
     * 必须重写以下方法
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();


    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (locationClient != null) {
            locationClient.onDestroy();
        }
    }

    @Override
    public View getInfoWindow(final Marker marker) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.poikeywordsearch_uri, null);
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
                startActivity(new Intent(getActivity(), ComponentActivity.class).putExtra("lat", latLng.latitude).putExtra("lng", latLng.longitude));
            }
        });
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
         start_Latlng = cameraPosition.target;
        LogUtils.e("定位返回信息：" + start_Latlng.latitude + start_Latlng.longitude);
//        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(latLng.latitude,
//                latLng.lotude), 1000));//设置周边搜索的中心点以及半径

    currentPage = 0;
    query = new PoiSearch.Query(keyWord, "", cityName);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
    query.setPageSize(20);// 设置每页最多返回多少条poiitem
    query.setPageNum(currentPage);// 设置查第一页

    poiSearch = new PoiSearch(getActivity(), query);
    poiSearch.setOnPoiSearchListener(NearFragment.this);
    poiSearch.searchPOIAsyn();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();

        return false;
    }

    @Override
    public void onGetInputtips(List<Tip> list, int i) {

    }

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
                            //LogUtils.e("搜素结果打印2：" + poiItems.get(i).toString());
                        }
                        //ToastUtils.showShortToastSafe(getActivity(), "提示：" + poiItems.get(0).getAdName());
//                        searchAddressAdapter.notifyDataSetChanged();
                        aMap.clear();// 清理之前的图标
                        PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
                        poiOverlay.removeFromMap();
                        poiOverlay.addToMap();
                        poiOverlay.zoomToSpan();
                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
                        showSuggestCity(suggestionCities);
                    } else {

                        ToastUtils.showShortToastSafe(getActivity(), "没有搜索结果");
                    }
                }
            } else {
                ToastUtils.showShortToastSafe(getActivity(), "没有搜索结果");
            }
        } else {

            ToastUtils.showShortToastSafe(getActivity(), rCode);
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

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
        ToastUtils.showShortToastSafe(getActivity(), infomation);

    }
}
