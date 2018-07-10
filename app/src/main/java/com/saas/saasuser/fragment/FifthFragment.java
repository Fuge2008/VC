package com.saas.saasuser.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.recker.flybanner.FlyBanner;
import com.saas.saasuser.R;
import com.saas.saasuser.activity.AddTripApplyActivity;
import com.saas.saasuser.activity.CorporateInvoiceActivity;
import com.saas.saasuser.activity.EnterprisesCardActivity;
import com.saas.saasuser.activity.MenuManageActivity;
import com.saas.saasuser.activity.NewOrdersActivity2;
import com.saas.saasuser.activity.SearchActivity;
import com.saas.saasuser.adapter.VVNewsAdapter;
import com.saas.saasuser.application.MyApplication;
import com.saas.saasuser.global.NetIntent;
import com.saas.saasuser.global.NetIntentCallBackListener;
import com.saas.saasuser.util.LogUtils;
import com.saas.saasuser.util.SharedPreferencesUtil;
import com.saas.saasuser.util.StringUtils;
import com.saas.saasuser.util.ToastUtils;
import com.saas.saasuser.view.CustomProgress;
import com.saas.saasuser.view.addViews.AppConfig;
import com.saas.saasuser.view.addViews.adapter.IndexDataAdapter;
import com.saas.saasuser.view.addViews.entity.MenuEntity;
import com.saas.saasuser.view.addViews.widget.LineGridView;
import com.squareup.okhttp.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class FifthFragment extends BaseFragment implements NetIntentCallBackListener.INetIntentCallBack {
    private static MyApplication appContext;
    private LineGridView gridView;
    private List<MenuEntity> indexDataAll = new ArrayList<MenuEntity>();
    private List<MenuEntity> indexDataList = new ArrayList<MenuEntity>();
    private IndexDataAdapter adapter;
    private final static String fileName = "menulist";

    FlyBanner mBannerLocal;
//
//    private UPMarqueeView upview1;
//    List<String> newsData = new ArrayList<>();
//    List<View> views = new ArrayList<>();

    private VVNewsAdapter newsAdapter;

    PullToRefreshListView pullRefreshList;


    private SharedPreferencesUtil util;
    private ListView actualListView;
    private int page = 1;
    private List<JSONObject> orderDatas = new ArrayList<JSONObject>();
    private JSONObject json;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_fifth, null);
        appContext = MyApplication.getInstance();
//        upview1 = (UPMarqueeView) view.findViewById(upview1);
        pullRefreshList= (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);


        mBannerLocal = (FlyBanner) view.findViewById(R.id.fb_banner);
        gridView = (LineGridView) view.findViewById(R.id.gv_lanuch_start);
        view.findViewById(R.id.iv_more2).setVisibility(View.VISIBLE);
        view.findViewById(R.id.head_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startActivity(new Intent(mActivity, SearchActivity.class));
            }
        });
        TextView tvTitle = (TextView) view.findViewById(R.id.head_title);
        tvTitle.setText("租行");
        initLocalBanner();
//        initdata();
//        initViews();
        initXListView();
//        new NetIntent().client_getCarNews(new NetIntentCallBackListener(this));
        gridView.setFocusable(false);
        String strByJson = getJson(mActivity, fileName);
        //Json的解析类对象
        JsonParser parser = new JsonParser();
        //将JSON的String 转成一个JsonArray对象
        JsonArray jsonArray = parser.parse(strByJson).getAsJsonArray();
        Gson gson = new Gson();
        //加强for循环遍历JsonArray
        for (JsonElement indexArr : jsonArray) {
            //使用GSON，直接转成Bean对象
            MenuEntity menuEntity = gson.fromJson(indexArr, MenuEntity.class);
            indexDataAll.add(menuEntity);
        }
        //appContext.delFileData(AppConfig.KEY_All);

        String key = AppConfig.KEY_All;
        String keyUser = AppConfig.KEY_USER;
        appContext.saveObject((Serializable) indexDataAll, AppConfig.KEY_All);

        List<MenuEntity> indexDataUser = (List<MenuEntity>) appContext.readObject(AppConfig.KEY_USER);
        if (indexDataUser == null || indexDataUser.size() == 0) {
            appContext.saveObject((Serializable) indexDataAll, AppConfig.KEY_USER);
        }
        indexDataList = (List<MenuEntity>) appContext.readObject(AppConfig.KEY_USER);

        MenuEntity allMenuEntity = new MenuEntity();
        allMenuEntity.setIco("");
        allMenuEntity.setId("all");
        allMenuEntity.setTitle("全部");
        indexDataList.add(allMenuEntity);
        adapter = new IndexDataAdapter(mActivity, indexDataList);
        gridView.setAdapter(adapter);
        Log.e("", strByJson);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();

                String title = indexDataList.get(position).getTitle();
                String strId = indexDataList.get(position).getId();
                Log.i("", title + strId);
                if (strId.equals("all")) {// 更多
                    intent.setClass(mActivity, MenuManageActivity.class);
                    startActivity(intent);
                } else if (StringUtils.equals("VV通信", title)) {
                    //startActivity(new Intent(mActivity, IMMainActivity.class));
                    ToastUtils.showShortToast(mActivity, "当前功能尚未上线，敬请关注！");
                } else if (StringUtils.equals("个人中心", title)) {
                    //startActivity(new Intent(mActivity, OrderStatisticsActivity.class));
                    // startActivity(new Intent(mActivity, RegularInvoiceActivity.class));
                    ToastUtils.showShortToast(mActivity, "当前功能尚未上线，敬请关注！");
                } else if (StringUtils.equals("企业发票", title)) {
                    startActivity(new Intent(mActivity, CorporateInvoiceActivity.class));
                } else if (StringUtils.equals("企业名片", title)) {
                    startActivity(new Intent(mActivity, EnterprisesCardActivity.class));
                } else if (StringUtils.equals("新增订单", title)) {
                    startActivity(new Intent(mActivity, NewOrdersActivity2.class));
                } else if (StringUtils.equals("租行申请", title)) {
                    startActivity(new Intent(mActivity, AddTripApplyActivity.class).putExtra("reBuiltOrder", "0").putExtra("orderId", ""));
                } else {
                    ToastUtils.showShortToast(mActivity, "当前功能尚未上线，敬请关注！");
                }


            }
        });

        return view;
    }
    private void initXListView() {
        pullRefreshList.setMode(PullToRefreshBase.Mode.BOTH);
        pullRefreshList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(mActivity,
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

        newsAdapter = new VVNewsAdapter( mActivity, orderDatas);
        actualListView.setAdapter(newsAdapter);
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
            dialog = CustomProgress.show(mActivity, "加载中..", true, null);
        }
        page = page_num;
    }



    public static String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("", e.toString());
        }
        return stringBuilder.toString();
    }

    @Override
    public void onResume() {
        super.onResume();
        indexDataList.clear();
        indexDataList = (List<MenuEntity>) appContext.readObject(AppConfig.KEY_USER);
        MenuEntity allMenuEntity = new MenuEntity();
        allMenuEntity.setIco("all_big_ico");
        allMenuEntity.setId("all");
        allMenuEntity.setTitle("全部");
        indexDataList.add(allMenuEntity);
        adapter = new IndexDataAdapter(mActivity, indexDataList);
        gridView.setAdapter(adapter);

        getData(1);
    }


//    /**
//     * 初始化界面程序
//     */
//    private void initViews() {
//        setView();
//        upview1.setViews(views);
//        /**
//         * 设置item_view的监听
//         */
//        upview1.setOnItemClickListener(new UPMarqueeView.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position, View view) {
//                mActivity.startActivity(new Intent(mActivity, VVNewsActivity.class));
//            }
//        });
//    }

    /**
     * 初始化需要循环的View
     * 为了灵活的使用滚动的View，所以把滚动的内容让用户自定义
     * 假如滚动的是三条或者一条，或者是其他，只需要把对应的布局，和这个方法稍微改改就可以了，
     */
//    private void setView() {
//        for (int i = 0; i < newsData.size(); i = i + 2) {
//            final int position = i;
//            //设置滚动的单个布局
//            LinearLayout moreView = (LinearLayout) LayoutInflater.from(mActivity).inflate(R.layout.item_view, null);
//            //初始化布局的控件
//            TextView tv1 = (TextView) moreView.findViewById(R.id.tv1);
//            TextView tv2 = (TextView) moreView.findViewById(R.id.tv2);
//
//            /**
//             * 设置监听
//             */
//            moreView.findViewById(R.id.rl).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // Toast.makeText(mActivity, position + "你点击了" + newsData.get(position).toString(), Toast.LENGTH_SHORT).show();
//                    mActivity.startActivity(new Intent(mActivity, VVNewsActivity.class));
//                }
//            });
//            /**
//             * 设置监听
//             */
//            moreView.findViewById(R.id.rl2).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // Toast.makeText(mActivity, position + "你点击了" + newsData.get(position + 1).toString(), Toast.LENGTH_SHORT).show();
//                    mActivity.startActivity(new Intent(mActivity, VVNewsActivity.class));
//                }
//            });
//            //进行对控件赋值
//            tv1.setText(newsData.get(i).toString());
//            if (newsData.size() > i + 1) {
//                //因为淘宝那儿是两条数据，但是当数据是奇数时就不需要赋值第二个，所以加了一个判断，还应该把第二个布局给隐藏掉
//                tv2.setText(newsData.get(i + 1).toString());
//            } else {
//                moreView.findViewById(R.id.rl2).setVisibility(View.GONE);
//            }
//
//            //添加到循环滚动数组里面去
//            views.add(moreView);
//        }
//    }


    private void initLocalBanner() {


        List<Integer> images = new ArrayList<>();

        images.add(R.drawable.icon_picture_two);
        images.add(R.drawable.icon_picture_three);

        images.add(R.drawable.icon_picture_two);
        images.add(R.drawable.icon_picture_three);


        mBannerLocal.setImages(images);

        mBannerLocal.setOnItemClickListener(new FlyBanner.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //  toast("点击了第"+position+"张图片");
            }
        });

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

//        JSONObject jsonObject = null;
//
//        try {
//            jsonObject = JSON.parseObject(response);
//            LogUtils.e("新闻数据：" + jsonObject.toString());
//            if (jsonObject.containsKey("resultcode")) {
//                initdata();
//                //  return;
//            }
//
//            JSONObject obj = (JSONObject) jsonObject.get("result");
//
//            if (StringUtils.equals(obj.getString("stat"), "1")) {
//                if (obj.containsKey("data")) {
//                    JSONArray users_temp = obj.getJSONArray("data");
//                    if (users_temp != null) {
//                        newsData.clear();
//                        for (int i = 0; i < users_temp.size(); i++) {
//                            String s = users_temp.getJSONObject(i).getString("title");
//                            newsData.add(s);
//                            LogUtils.e("新闻数据标题:-->" + s);
//                        }
//                        initViews();
//
//
//                    }
//
//                }
//            }
//        } catch (com.alibaba.fastjson.JSONException e) {
//            e.printStackTrace();
//        }
        JSONObject jsonObject = null;
        try {
            jsonObject = JSON.parseObject(response);
            pullRefreshList.onRefreshComplete();
            LogUtils.e("新闻数据："+ jsonObject.toString());
            if(jsonObject.containsKey("resultcode")){
                ToastUtils.showShortToast(mActivity,"当日访问次数已超过规定次数，请明天继续查看！");
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
                        newsAdapter.notifyDataSetChanged();

                    }

                }
            }
        } catch (com.alibaba.fastjson.JSONException e) {
            e.printStackTrace();
        }

    }

//    private void initdata() {
//        newsData.add("家人给2岁孩子喝这个，孩子智力倒退10岁!!!");
//        newsData.add("iPhone8最感人变化成真，必须买买买买!!!!");
//        newsData.add("简直是白菜价！日本玩家33万甩卖15万张游戏王卡");
//        newsData.add("iPhone7价格曝光了！看完感觉我的腰子有点疼...");
//        newsData.add("主人内疚逃命时没带够，回废墟狂挖30小时！");
//    }
}
