package com.saas.saasuser.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.saas.saasuser.R;
import com.saas.saasuser.adapter.OrderRecordListAdapter;
import com.saas.saasuser.application.MyApplication;
import com.saas.saasuser.global.NetIntent;
import com.saas.saasuser.global.NetIntentCallBackListener;
import com.saas.saasuser.util.LogUtils;
import com.saas.saasuser.util.SharedPreferencesUtil;
import com.saas.saasuser.util.StringUtils;
import com.saas.saasuser.util.Util;
import com.saas.saasuser.view.CustomProgress;
import com.saas.saasuser.view.stickylistview.OrderRecord;
import com.saas.saasuser.view.stickylistview.PullFreshListView;
import com.saas.saasuser.view.stickylistview.PullStickyListView;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 租行记录
 */
public class OrderRecordListActivity extends BaseActivity implements NetIntentCallBackListener.INetIntentCallBack,PullFreshListView.ListViewPlusListener{
    @BindView(R.id.tv_head_more)
    TextView tvHeadMore;
    @BindView(R.id.head_more)
    LinearLayout headMore;
//    @BindView(R.id.pull_refresh_list)
//    PullToRefreshListView pullRefreshList;
    @BindView(R.id.tv_empty_show)
    TextView tvEmptyShow;

    private ListView actualListView;
    private OrderRecordListAdapter adapter;
    private List<OrderRecord> orderDatas = new ArrayList<OrderRecord>();
    private JSONObject json;
    private SharedPreferencesUtil util;
    private int page = 1;

    private PullStickyListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_record_list);
        MyApplication.getInstance().addActivity(this);
        util=new SharedPreferencesUtil(this);
        ButterKnife.bind(this);
        //initXListView();
        Util.setHeadTitleMore(this, "租行记录", true);


        listView = (PullStickyListView) findViewById(R.id.list);
        listView.setLoadEnable(true);
        listView.setListViewPlusListener(OrderRecordListActivity.this);
        adapter = new OrderRecordListAdapter(OrderRecordListActivity.this, orderDatas);
        listView.setAdapter(adapter);
        loadData();
    }

private void loadData() {
    listView.stopLoadMore();

    new NetIntent().client_orderRecord(util.getUserId(),page+"","1","",new NetIntentCallBackListener(this));
        if (dialog == null) {
            dialog = CustomProgress.show(OrderRecordListActivity.this, "加载中..", true, null);
       }
    page++;

}
@Override
public void onLoadMore() {
    loadData();
}
    @Override
    public void onResume() {
        super.onResume();
        loadData();

    }

    @Override
    public void onError(Request request, Exception e) {
        if (dialog != null) {
            dialog.dismiss();
        }
        //pullRefreshList.onRefreshComplete();

    }

    @Override
    public void onResponse(String response) {

        if (dialog != null) {
            dialog.dismiss();
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = JSON.parseObject(response);
            LogUtils.e("行程记录："+ jsonObject.toString());

            //ToastUtils.showShortToastSafe(mActivity, jsonObject.getString("ErrMsg"));
            if(StringUtils.equals(jsonObject.getString("ErrMsg"),"没有订单信息！")){
                tvEmptyShow.setVisibility(View.VISIBLE);
                orderDatas.clear();
                adapter.notifyDataSetChanged();
            }else {
                tvEmptyShow.setVisibility(View.GONE);
            }
            //pullRefreshList.onRefreshComplete();
            if (StringUtils.equals(jsonObject.getString("ErrCode"), "1")) {
                if (jsonObject.containsKey("Data")) {
                    JSONArray users_temp = jsonObject.getJSONArray("Data");
                    //orderDatas.clear();
                    if (users_temp != null) {
                        for (int i = 0; i < users_temp.size(); i++) {
                            json = users_temp.getJSONObject(i);
                            OrderRecord b=new OrderRecord(
                                    json.getString("vehiclename"),
                                    json.getString("ordertype"),
                                    json.getString("dname"),
                                    json.getString("usetime"),
                                    json.getString("endsite"),
                                    json.getString("ostate"),
                                    json.getString("dphone"),
                                    json.getString("cname"),
                                    json.getString("vehiclenum"),
                                    json.getString("id"),
                                    json.getString("starttime"),
                                    json.getString("cphone"),
                                    json.getString("businseename"),
                                    json.getString("useweek"),
                                    json.getString("endtime"),
                                    json.getString("startsite")
                            );
                            orderDatas.add(b);
                        }

                        adapter.notifyDataSetChanged();
                        //handler.sendEmptyMessage(0);

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
                    LogUtils.e("集合数据："+orderDatas.toString());

                    //adapter.notifyDataSetChanged();
                    break;
            }
        }
    };



    @OnClick(R.id.head_more)
    public void onViewClicked() {
    }
}
