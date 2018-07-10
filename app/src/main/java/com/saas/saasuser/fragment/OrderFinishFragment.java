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
import com.saas.saasuser.adapter.SlideAdapter;
import com.saas.saasuser.bean.MessageItem;
import com.saas.saasuser.global.NetIntentCallBackListener;
import com.saas.saasuser.util.LogUtils;
import com.saas.saasuser.util.SharedPreferencesUtil;
import com.saas.saasuser.view.ListViewCompat;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

public class OrderFinishFragment extends BaseFragment  implements NetIntentCallBackListener.INetIntentCallBack,AdapterView.OnItemClickListener, ListViewCompat.OnRefreshListener,ListViewCompat.OnLoadListener{
    private SharedPreferencesUtil util;
    private ListViewCompat mListView;
    private List<MessageItem> mMessageItems = new ArrayList<MessageItem>();
    private ScrollView scrollview;
    private SlideAdapter adapter;
    private int allCount = 40;
    private LinearLayout llOrder;

    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_order_finish, null);
        mListView = (ListViewCompat) view.findViewById(R.id.list);
        util=new SharedPreferencesUtil(mActivity);
        mListView.setOnItemClickListener(this);
        mListView.setOnRefreshListener(this);
        mListView.setOnLoadListener(this);
        loadData(ListViewCompat.REFRESH);
        for (int i = 0; i < 20; i++) {
            MessageItem item = new MessageItem();
            if (i % 3 == 0) {
                item.iconRes = R.drawable.saas_logo;
                item.title = "当前订单";
                item.msg = "司机正在去接您的路上";
                item.time = "晚上18:18";
            } else {
                item.iconRes = R.drawable.app_icon;
                item.title = "待开始订单";
                item.msg = "司机已经确认订单，还有3个小时即将出发";
                item.time = "12月18日";
            }
            mMessageItems.add(item);
            adapter=new SlideAdapter(mActivity, mMessageItems);
            mListView.setAdapter(adapter);
        }

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
    public List<MessageItem> getData() {
        List<MessageItem> result = new ArrayList<MessageItem>();
        for (int i = 0; i < 10; i++) {
            MessageItem item = new MessageItem();
            if (i % 3 == 0) {
                item.iconRes = R.drawable.icon_head_image;
                item.title = "订单来了";
                item.msg = "深圳西站--南头关";
                item.time = "下午14:15";
            } else {
                item.iconRes = R.drawable.icon_my_head;
                item.title = "订单结束了";
                item.msg = "松岗汽车站-龙岗中心城";
                item.time = "12:28";
            }
            result.add(item);
        }
        return result;
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            List<MessageItem> result = (List<MessageItem>) msg.obj;
            switch (msg.what) {
                case ListViewCompat.REFRESH:
                    mListView.onRefreshComplete();
                    mMessageItems.clear();
                    mMessageItems.addAll(result);
                    break;
                case ListViewCompat.LOAD:
                    mListView.onLoadComplete();
                    mMessageItems.addAll(result);
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
            msg.obj = mMessageItems;
            handler.sendMessage(msg);

        }
    }
    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        loadData(ListViewCompat.REFRESH);
    }
}
