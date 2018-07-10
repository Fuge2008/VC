package com.saas.saasuser.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.saas.saasuser.R;
import com.saas.saasuser.adapter.VVNewsAdapter;
import com.saas.saasuser.application.MyApplication;
import com.saas.saasuser.global.NetIntent;
import com.saas.saasuser.global.NetIntentCallBackListener;
import com.saas.saasuser.util.LogUtils;
import com.saas.saasuser.util.SharedPreferencesUtil;
import com.saas.saasuser.util.StringUtils;
import com.saas.saasuser.util.ToastUtils;
import com.saas.saasuser.util.Util;
import com.saas.saasuser.view.CustomProgress;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * VV新闻
 */
public class VVNewsActivity extends BaseActivity implements NetIntentCallBackListener.INetIntentCallBack{
    @BindView(R.id.pull_refresh_list)
    PullToRefreshListView pullRefreshList;


    private SharedPreferencesUtil util;
    private ListView actualListView;

    private ListView theListView;
    private VVNewsAdapter adapter;
    private int page = 1;
    private List<JSONObject> orderDatas = new ArrayList<JSONObject>();
    private JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vv_news);
        MyApplication.getInstance().addActivity(this);
        ButterKnife.bind(this);
        util = new SharedPreferencesUtil(this);
        Util.setHeadTitleMore(this, "VV新闻", true);
        initXListView();
    }
    private void initXListView() {
        pullRefreshList.setMode(PullToRefreshBase.Mode.BOTH);
        pullRefreshList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(VVNewsActivity.this,
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

        adapter = new VVNewsAdapter(VVNewsActivity.this, orderDatas);
        actualListView.setAdapter(adapter);
        actualListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        getData(1);
        pullRefreshList.setRefreshing(false);

    }

    private void getData(final int page_num) {
        new NetIntent().client_getCarNews(new NetIntentCallBackListener(this));
        if (dialog == null) {
            dialog = CustomProgress.show(VVNewsActivity.this, "加载中..", true, null);
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
            pullRefreshList.onRefreshComplete();
            LogUtils.e("新闻数据："+ jsonObject.toString());
            if(jsonObject.containsKey("resultcode")){
                ToastUtils.showShortToast(VVNewsActivity.this,"当日访问次数已超过规定次数，请明天继续查看！");
                return;
            }

            JSONObject obj= (JSONObject) jsonObject.get("result");

            if (StringUtils.equals(obj.getString("stat"), "1")) {
                if (obj.containsKey("data")) {
                    JSONArray users_temp = obj.getJSONArray("data");
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
