package com.saas.saasuser.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.nearby.NearbyInfo;
import com.amap.api.services.nearby.NearbySearch;
import com.amap.api.services.nearby.NearbySearchFunctionType;
import com.amap.api.services.nearby.NearbySearchResult;
import com.amap.api.services.nearby.UploadInfo;
import com.amap.api.services.nearby.UploadInfoCallback;
import com.saas.saasuser.R;
import com.saas.saasuser.application.MyApplication;
import com.saas.saasuser.util.LogUtils;
import com.saas.saasuser.util.SharedPreferencesUtil;
import com.saas.saasuser.util.Util;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 内部安排
 */
public class IndoorContactActivity extends BaseActivity implements NearbySearch.NearbyListener {
    @BindView(R.id.head_more)
    LinearLayout headMore;
    @BindView(R.id.tv_text_show)
    TextView tvTextShow;
    @BindView(R.id.tv_title2)
    TextView tvTitle2;
    private JSONObject json;
    String orderId;
    private SharedPreferencesUtil util;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor_contact);
        final LatLonPoint llp = new LatLonPoint(22.544025, 114.055143);
        ButterKnife.bind(this);
        util = new SharedPreferencesUtil(this);
        Util.setHeadTitleMore(this, "内部安排", true);
        headMore.setVisibility(View.GONE);

        //ToastUtils.showShortToastSafe(IndoorContactActivity.this, "界面还没有设计好");
        /**
         *   信息上传
         */
        NearbySearch mNearbySearch = NearbySearch.getInstance(MyApplication.getInstance2());
        mNearbySearch.startUploadNearbyInfoAuto(new UploadInfoCallback() {
            //设置自动上传数据和上传的间隔时间
            @Override
            public UploadInfo OnUploadInfoCallback() {
                UploadInfo loadInfo = new UploadInfo();
                loadInfo.setCoordType(NearbySearch.AMAP);
                //位置信息
                loadInfo.setPoint(llp);
                //用户id信息
                loadInfo.setUserID(util.getMobilePhone());
                return loadInfo;
            }
        }, 10000);
        tvTitle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 *   附近搜索
                 */
                //获取附近实例（单例模式）
                NearbySearch nearbySearch = NearbySearch.getInstance(MyApplication.getInstance2());
                //设置附近监听
                NearbySearch.getInstance(MyApplication.getInstance2()).addNearbyListener(IndoorContactActivity.this);
                //设置搜索条件
                NearbySearch.NearbyQuery query = new NearbySearch.NearbyQuery();
                //设置搜索的中心点
                query.setCenterPoint(new LatLonPoint(22.544025, 114.055143));
                //设置搜索的坐标体系
                query.setCoordType(NearbySearch.AMAP);
                //设置搜索半径
                query.setRadius(10000);
                //设置查询的时间
                query.setTimeRange(10000);
                //设置查询的方式驾车还是距离
                query.setType(NearbySearchFunctionType.DRIVING_DISTANCE_SEARCH);
                //调用异步查询接口
                NearbySearch.getInstance(MyApplication.getInstance2()).searchNearbyInfoAsyn(query);
            }
        });

        //获取附近实例，并设置要清楚用户的id
        //NearbySearch.getInstance(MyApplication.getInstance2()).setUserID("18588458890");
        //调用异步清除用户接口
        //  NearbySearch.getInstance(MyApplication.getInstance2()).clearUserInfoAsyn();
    }

    @Override
    public void onUserInfoCleared(int i) {
        LogUtils.e("onUserInfoCleared:" + i);
    }

    @Override
    public void onNearbyInfoUploaded(int i) {
        LogUtils.e("onNearbyInfoUploaded:" + i);
    }

    //周边检索的回调函数
    public void onNearbyInfoSearched(NearbySearchResult nearbySearchResult, int resultCode) {
        //搜索周边附近用户回调处理
        if (resultCode == 1000) {
            if (nearbySearchResult != null & nearbySearchResult.getNearbyInfoList() != null
                    & nearbySearchResult.getNearbyInfoList().size() > 0) {
                NearbyInfo nearbyInfo = nearbySearchResult.getNearbyInfoList().get(0);

                tvTextShow.setText("周边搜索结果为size " + nearbySearchResult.getNearbyInfoList().size() + " first：" + nearbyInfo.getUserID() + "  " + nearbyInfo.getDistance() + "  "
                        + nearbyInfo.getDrivingDistance() + "  " + nearbyInfo.getTimeStamp() + "  " +
                        nearbyInfo.getPoint().toString());
            } else {
                tvTextShow.setText("周边搜索结果为空");
            }
        } else {
            tvTextShow.setText("周边搜索出现异常，异常码为：" + resultCode);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //调用销毁功能，在应用的合适生命周期需要销毁附近功能
        NearbySearch.destroy();
    }
}
