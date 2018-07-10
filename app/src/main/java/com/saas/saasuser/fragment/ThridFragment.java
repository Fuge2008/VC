package com.saas.saasuser.fragment;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.saas.saasuser.R;
import com.saas.saasuser.adapter.FragmentThreeListAdapter;
import com.saas.saasuser.global.NetIntent;
import com.saas.saasuser.global.NetIntentCallBackListener;
import com.saas.saasuser.util.LogUtils;
import com.saas.saasuser.util.SharedPreferencesUtil;
import com.saas.saasuser.util.StringUtils;
import com.saas.saasuser.util.ToastUtils;
import com.saas.saasuser.view.CustomProgress;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;


public class ThridFragment extends BaseFragment implements NetIntentCallBackListener.INetIntentCallBack {

    private ListView actualListView;
    private FragmentThreeListAdapter adapter;
    private List<JSONObject> orderDatas = new ArrayList<JSONObject>();
    private JSONObject json;
    private SharedPreferencesUtil util;


    private PullToRefreshListView pull_refresh_list;
    private int page = 1;
    private TextView tvEmptyShow;
    private ImageView iv_error_show;


    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_thrid, null);
        util = new SharedPreferencesUtil(mActivity);
        //lvListView = (ListView) view.findViewById(R.id.lv_info_list);
        pull_refresh_list = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);
        iv_error_show= (ImageView) view.findViewById(R.id.iv_error_show);
        tvEmptyShow= (TextView) view.findViewById(R.id.tv_empty_show);
        TextView tvTitle=(TextView)view.findViewById(R.id.head_title);
        tvTitle.setText("消息");
        initXListView();
        view.findViewById(R.id.head_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShortToastSafe(mActivity,"暂时未开放搜索！");
            }
        });


        return view;
    }

    private void initXListView() {
        pull_refresh_list.setMode(PullToRefreshBase.Mode.BOTH);
        pull_refresh_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
                    @Override
                    public void onRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        String label = DateUtils.formatDateTime(mActivity,
                                System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME
                                        | DateUtils.FORMAT_SHOW_DATE
                                        | DateUtils.FORMAT_ABBREV_ALL);

                        refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                        if (pull_refresh_list.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_START) {
                            page = 1;
                        } else if (pull_refresh_list.getCurrentMode() == PullToRefreshBase.Mode.PULL_FROM_END) {
                            page++;
                        }
                        getData(page);//TODO  刷新获取更多数据
                    }
                });

        actualListView = pull_refresh_list.getRefreshableView();
        adapter = new FragmentThreeListAdapter(mActivity, orderDatas);
        actualListView.setAdapter(adapter);
        actualListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        getData(1);
        pull_refresh_list.setRefreshing(false);
    }

    private void getData(final int page_num) {           //TODO 加载数据逻辑
        new NetIntent().client_mainMenuListThrid(util.getUserId(), page_num + "", new NetIntentCallBackListener(this));//审批信息 批复 安排 任务列表信息
        if (dialog == null) {
            dialog = CustomProgress.show(getActivity(), "加载中..", true, null);
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
        pull_refresh_list.onRefreshComplete();

    }

    @Override
    public void onResponse(String response) {
        if (dialog != null) {
            dialog.dismiss();
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = JSON.parseObject(response);
            //ToastUtils.showShortToastSafe(mActivity,jsonObject.getString("ErrMsg"));
            LogUtils.e("各类联络列表："+jsonObject.toString());
            pull_refresh_list.onRefreshComplete();
            if(StringUtils.equals(jsonObject.getString("ErrMsg"),"没有订单信息！")){
                iv_error_show.setVisibility(View.VISIBLE);
                tvEmptyShow.setVisibility(View.VISIBLE);

                orderDatas.clear();
                adapter.notifyDataSetChanged();
            }else {
                tvEmptyShow.setVisibility(View.GONE);
                iv_error_show.setVisibility(View.GONE);
            }
            if (StringUtils.equals(jsonObject.getString("ErrCode"), "1")) {

                if (jsonObject.containsKey("Data")) {
                    JSONArray users_temp = jsonObject.getJSONArray("Data");
                    orderDatas.clear();
                    if (users_temp != null) {

                            for (int i = 0; i < users_temp.size(); i++) {
                                json = users_temp.getJSONObject(i);
                                orderDatas.add(json);
                            }
//                        else{
//                            for (int i = 0; i < users_temp.size(); i++) {
//                                json = users_temp.getJSONObject(i);
//                                orderDatas.add(json);
//                            }
//                        }
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
