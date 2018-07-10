package com.saas.saasuser.activity.map;

import android.app.Activity;
import android.os.Bundle;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;
import com.saas.saasuser.R;
import com.saas.saasuser.application.MyApplication;

public class ComponentActivity extends Activity implements INaviInfoCallback {
    private AmapTTSController amapTTSController;

    LatLng latLng;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        MyApplication.getInstance().addActivity(this);
        initView();
        // SpeechUtils.getInstance(this).speakText();系统自带的语音播报

//       double lat = this.getIntent().getDoubleExtra("lat",114.06482756);
//        double lng = this.getIntent().getDoubleExtra("lng",22.52316159);
        latLng = new LatLng(22.52316159,114.06482756);
        amapTTSController = AmapTTSController.getInstance(getApplicationContext());
        amapTTSController.init();

    }

    private void initView() {
        setTitle("行程导航" );
        AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), new AmapNaviParams(null, null, new Poi("福民地铁站", latLng, ""), AmapNaviType.DRIVER), ComponentActivity.this);
        finish();
    }


    @Override
    public void onInitNaviFailure() {


    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onArriveDestination(boolean b) {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onGetNavigationText(String s) {
        amapTTSController.onGetNavigationText(s);
    }

    @Override
    public void onStopSpeaking() {
        amapTTSController.stopSpeaking();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        amapTTSController.destroy();
    }
}
