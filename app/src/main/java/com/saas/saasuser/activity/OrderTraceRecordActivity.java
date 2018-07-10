//package com.saas.saasuser.activity;
//
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONException;
//import com.alibaba.fastjson.JSONObject;
//import com.saas.saasuser.R;
//import com.saas.saasuser.application.MyApplication;
//import com.saas.saasuser.global.NetIntent;
//import com.saas.saasuser.global.NetIntentCallBackListener;
//import com.saas.saasuser.util.LogUtils;
//import com.saas.saasuser.util.SharedPreferencesUtil;
//import com.saas.saasuser.util.StringUtils;
//import com.saas.saasuser.util.ToastUtils;
//import com.saas.saasuser.util.Util;
//import com.saas.saasuser.view.CustomProgress;
//
//import com.squareup.okhttp.Request;
//
//import java.util.ArrayList;
//
///**
// * 订单轨迹记录
// */
//public class OrderTraceRecordActivity extends BaseActivity implements NetIntentCallBackListener.INetIntentCallBack{
//    private SharedPreferencesUtil util;
//    private ArrayList<Item> items;
//    private  ListView theListView;
//    private OrderTaceRecordAdapter adapter;
//    private TextView tvEmptyShow;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_anim_listview);
//        MyApplication.getInstance().addActivity(this);
//        Util.setHeadTitleMore(this, "行程记录", true);
//        findViewById(R.id.head_more).setVisibility(View.GONE);
//       theListView = (ListView) findViewById(R.id.mainListView);
//        tvEmptyShow= (TextView) findViewById(R.id.tv_empty_show);
//        util = new SharedPreferencesUtil(this);
//        getDataFromNet();
//        //new NetIntent().client_driverOrUserCompeleteOrderList(util.getUserId(),util.getCompanyId(),"2" ,new NetIntentCallBackListener(this));//1-乘客，2-司机
//
//        if (dialog == null) {
//            dialog = CustomProgress.show(this, "加载中..", true, null);
//        }
//      items = new ArrayList<>();
//        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
//                ((FoldingCell) view).toggle(false);
//                adapter.registerToggle(pos);
//            }
//        });
//
//    }
//
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        getDataFromNet();
//    }
//    public void getDataFromNet(){
//        new NetIntent().client_driverOrUserCompeleteOrderList(util.getUserId(),util.getCompanyId(),util.getPlatformRole() ,new NetIntentCallBackListener(this));//1-乘客，2-司机
//    }
//    @Override
//    public void onError(Request request, Exception e) {
//        if (dialog != null) {
//            dialog.dismiss();
//        }
//
//    }
//
//    @Override
//    public void onResponse(String response) {
//        if (dialog != null) {
//            dialog.dismiss();
//        }
//        JSONObject jsonObject = null;
//        try {
//            jsonObject = JSON.parseObject(response);
//            LogUtils.e("待完善订单："+jsonObject.toString());
//            ToastUtils.showShortToastSafe(OrderTraceRecordActivity.this, jsonObject.getString("ErrMsg"));
//            if (StringUtils.equals(jsonObject.getString("ErrCode"), "1")) {
//                if (jsonObject.containsKey("Data")) {
//                    JSONArray users_temp = jsonObject.getJSONArray("Data");
//                    if (users_temp != null) {
//                        items.clear();
//                        for (int i = 0; i < users_temp.size(); i++) {
//                            JSONObject json = users_temp.getJSONObject(i);
//                            String orderId=  json.getString("BOID");
//                             String orderNumber=  json.getString("BOOrderNum");
//                             String strTag=  json.getString("OFIsFianl");
//                             String strDate=  json.getString("BOUseDate");
//                             String strTime=  json.getString("BOUStartTime");
//                             String strStartAddress=  json.getString("BOUpCarPlace");
//                            String strEndAddress=  json.getString("BODownCarPlace");
//                            String strRole=  json.getString("IsRloe");
//                            String strOtherPhone=  json.getString("PhoneNum");
//                            String strOtherName=  json.getString("Name");
//                            String strOtherImage=  json.getString("HeadImg");
//
//
//                            items.add(new Item(orderId,orderNumber,strRole,strTag,strOtherPhone,strOtherImage, strStartAddress, strEndAddress, strOtherName, strDate, strTime));//部分数据暂时为空
//                        }
//                        handler.sendEmptyMessage(0);
//                  }else{
//                        tvEmptyShow.setVisibility(View.GONE);
//                    }
//
//
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    Handler handler = new Handler(Looper.getMainLooper()) {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 0:
//                    items.get(0).setRequestBtnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Toast.makeText(getApplicationContext(), "没有更多数据", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    adapter = new OrderTaceRecordAdapter(OrderTraceRecordActivity.this, items);
//
//                    adapter.setDefaultRequestBtnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Toast.makeText(getApplicationContext(), "没有更多数据", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    theListView.setAdapter(adapter);
//
//                    break;
//            }
//        }
//    };
//}
