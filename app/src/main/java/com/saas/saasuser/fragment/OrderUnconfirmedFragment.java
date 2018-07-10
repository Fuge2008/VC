package com.saas.saasuser.fragment;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.saas.saasuser.R;
import com.saas.saasuser.adapter.OrderUnconfirmAdapter;
import com.saas.saasuser.bean.OrderTotalItem;
import com.saas.saasuser.global.NetIntentCallBackListener;
import com.saas.saasuser.util.LogUtils;
import com.saas.saasuser.util.SharedPreferencesUtil;
import com.saas.saasuser.view.ListViewCompat;
import com.saas.saasuser.view.ListViewOrderTotal;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;


public class OrderUnconfirmedFragment extends BaseFragment implements NetIntentCallBackListener.INetIntentCallBack,AdapterView.OnItemClickListener,ListViewOrderTotal.OnRefreshListener,ListViewOrderTotal.OnLoadListener {
    private SharedPreferencesUtil util;
    private ListViewOrderTotal mListView;
    private List<OrderTotalItem> mOrderTotalItems = new ArrayList<>();
    private ScrollView scrollview;
    private OrderUnconfirmAdapter adapter;
    private int allCount = 40;
    private LinearLayout llOrder;
    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_order_unconfirmed, null);
        mListView = (ListViewOrderTotal) view.findViewById(R.id.list);
        util=new SharedPreferencesUtil(mActivity);
        mListView.setOnItemClickListener(this);
        mListView.setOnRefreshListener(this);
        mListView.setOnLoadListener(this);
        loadData(ListViewCompat.REFRESH);
        getData();
        adapter=new OrderUnconfirmAdapter(mActivity, mOrderTotalItems);
        mListView.setAdapter(adapter);

        return view;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

    }

    @Override
    public void onError(Request request, Exception e) {

    }

    @Override
    public void onResponse(String response) {
        JSONObject jsonObject = null;
        try {
            jsonObject = JSON.parseObject(response);
            LogUtils.e("数据：" + jsonObject.toString());

        } catch (com.alibaba.fastjson.JSONException e) {
            e.printStackTrace();
        }

    }
    private void loadData(final int what) {
        // 这里模拟从服务器获取数据
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = handler.obtainMessage();
                msg.what = what;
                msg.obj = getData();
                handler.sendMessage(msg);
            }
        }).start();
    }

    // 测试数据
    public List<OrderTotalItem> getData() {
        List<OrderTotalItem> result = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            OrderTotalItem item = new OrderTotalItem();
            if (i % 3 == 0) {
                item.iconRes = R.drawable.icon_head_image;
                item.date = "2018/02/10  12:23-15:18";
                item.startLocation = "深圳西火车站";
                item.midLocation = "深圳大学北门";
                item.endLocation="南头关十八巷99号";
            } else if(i%5==0){
                item.iconRes = R.drawable.today;
                item.date = "2018/02/03  15:13-18:38";
                item.startLocation = "深圳罗湖东门";
                item.midLocation = "宝安西乡";
                item.endLocation="龙华村";
            }else {
                item.iconRes = R.drawable.touxiang;
                item.date = "2018/02/09  02:23-05:12";
                item.startLocation = "深圳北站";
                item.midLocation = "梅林检查站";
                item.endLocation="东莞松山湖";
            }
            result.add(item);
        }
        return result;
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<OrderTotalItem> result = (List<OrderTotalItem>) msg.obj;
            switch (msg.what) {
                case ListViewCompat.REFRESH:
                    mListView.onRefreshComplete();
                    mOrderTotalItems.clear();
                    mOrderTotalItems.addAll(result);
                    break;
                case ListViewCompat.LOAD:
                    mListView.onLoadComplete();
                    mOrderTotalItems.addAll(result);
                    break;
                case 2:
                    mListView.onLoadComplete();
                    Toast.makeText(mActivity, "已加载全部！", Toast.LENGTH_SHORT).show();
                    break;
            }
            mListView.setResultSize(result.size());
            adapter.notifyDataSetChanged();
        };
    };

    public void onLoad() {
        // TODO Auto-generated method stub
        if(adapter.getCount()<allCount){
            loadData(ListViewCompat.LOAD);
        }else{
            Message msg = handler.obtainMessage();
            msg.what = 2;
            msg.obj = mOrderTotalItems;
            handler.sendMessage(msg);

        }
    }
    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        loadData(ListViewCompat.REFRESH);
    }
}