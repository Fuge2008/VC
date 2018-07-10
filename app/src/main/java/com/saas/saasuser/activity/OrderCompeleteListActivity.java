package com.saas.saasuser.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.saas.saasuser.R;
import com.saas.saasuser.adapter.OrderCompeleteAdapter;
import com.saas.saasuser.application.MyApplication;
import com.saas.saasuser.global.NetIntent;
import com.saas.saasuser.global.NetIntentCallBackListener;
import com.saas.saasuser.util.LogUtils;
import com.saas.saasuser.util.SharedPreferencesUtil;
import com.saas.saasuser.util.StringUtils;
import com.saas.saasuser.util.Util;
import com.saas.saasuser.view.CustomProgress;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 待完善订单列表
 */
public class OrderCompeleteListActivity extends BaseActivity implements NetIntentCallBackListener.INetIntentCallBack {
    @BindView(R.id.pull_refresh_list)
    PullToRefreshListView pullRefreshList;
    
    @BindView(R.id.tv_empty_show)
    TextView tvEmptyShow;
    private SharedPreferencesUtil util;
    private ListView actualListView;

    private ListView theListView;
    private OrderCompeleteAdapter adapter;
    private int page = 1;
    private List<JSONObject> orderDatas = new ArrayList<JSONObject>();
    private JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_compelete_list);
        MyApplication.getInstance().addActivity(this);
        ButterKnife.bind(this);
        util = new SharedPreferencesUtil(this);
        Util.setHeadTitleMore(this, "待完善订单", true);
        initXListView();
    }

    private void initXListView() {
       pullRefreshList.setMode(PullToRefreshBase.Mode.BOTH);
       pullRefreshList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(OrderCompeleteListActivity.this,
                        System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME
                                | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                if (pullRefreshList.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
                    page = 1;
                } else if (pullRefreshList.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
                    page++;
                }
                getData(page);//TODO  刷新获取更多数据
            }
        });

        actualListView=pullRefreshList.getRefreshableView();

        adapter = new OrderCompeleteAdapter(OrderCompeleteListActivity.this, orderDatas);
        actualListView.setAdapter(adapter);
        actualListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        getData(1);
       pullRefreshList.setRefreshing(false);

    }

    private void getData(final int page_num) {           //TODO 加载数据逻辑
        new NetIntent().client_driverOrUserCompeleteOrderList(util.getUserId(),util.getCompanyId(),util.getPlatformRole() ,new NetIntentCallBackListener(this));//1-乘客，2-司机
        if (dialog == null) {
            dialog = CustomProgress.show(OrderCompeleteListActivity.this, "加载中..", true, null);
        }
        page = page_num;
    }

    @Override
    public void onResume() {
        super.onResume();
        getData(1);
    }

    @Override
    public void onError(Request request, Exception e) {
        if (dialog != null) {
            dialog.dismiss();
        }
       pullRefreshList.onRefreshComplete();

    }

    @Override
    public void onResponse(String response) {

        if (dialog != null) {
            dialog.dismiss();
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = JSON.parseObject(response);
            LogUtils.e("待完善订单列表："+ jsonObject.toString());

            //ToastUtils.showShortToastSafe(OrderCompeleteListActivity.thisy, jsonObject.getString("ErrMsg"));
            if(StringUtils.equals(jsonObject.getString("ErrMsg"),"没有已结束的订单")){
                tvEmptyShow.setVisibility(View.VISIBLE);
                orderDatas.clear();
                adapter.notifyDataSetChanged();
            }else {
                tvEmptyShow.setVisibility(View.GONE);
            }
           pullRefreshList.onRefreshComplete();
            if (StringUtils.equals(jsonObject.getString("ErrCode"), "1")) {
                if (jsonObject.containsKey("Data")) {
                    JSONArray users_temp = jsonObject.getJSONArray("Data");
                    orderDatas.clear();
                    if (users_temp != null) {
                        for (int i = 0; i < users_temp.size(); i++) {
                            json = users_temp.getJSONObject(i);
                            orderDatas.add(json);
                        }
                        adapter.notifyDataSetChanged();

                    }

                }
            }
        } catch (com.alibaba.fastjson.JSONException e) {
            e.printStackTrace();
        }

    }

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:

                    break;
            }
        }
    };
}
