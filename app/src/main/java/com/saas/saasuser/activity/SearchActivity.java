package com.saas.saasuser.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.saas.saasuser.R;
import com.saas.saasuser.adapter.HistorySearchAdapter;
import com.saas.saasuser.adapter.HotSearchAdapter;
import com.saas.saasuser.application.MyApplication;
import com.saas.saasuser.util.PreferencesUtils;
import com.saas.saasuser.util.ToastUtils;
import com.saas.saasuser.view.ClearEditText;
import com.saas.saasuser.view.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SearchActivity extends BaseActivity {

    @BindView(R.id.edittxt_phone)
    ClearEditText mEditText;
    @BindView(R.id.hot_search_ry)
    RecyclerView mHotSearchView;
    @BindView(R.id.history_search_ry)
    RecyclerView mHistorySearchView;

    private List<String> hotSearchData;
    private List<String> historySearchData;

    private HotSearchAdapter mHotSearchAdapter;
    private HistorySearchAdapter mHistorySearchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        MyApplication.getInstance().addActivity(this);
        ButterKnife.bind(this);
        hotSearchData = new ArrayList<>();
        historySearchData = new ArrayList<>();
        setHotSearchData();
        getHotSearchData();
    }


    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        getHistorydata();
        setHistorySearchData();
    }

    /**
     * 热门搜索
     */
    private void getHotSearchData() {

        //TODO这里的数据从后台获取

        hotSearchData.add("更多订单");
        hotSearchData.add("免费订单");
        hotSearchData.add("打车优惠券");
        hotSearchData.add("补贴优惠");
        hotSearchData.add("豪车出行");
        mHotSearchAdapter.notifyDataSetChanged();
    }

    /**
     * 初始化热门搜索相关的适配器及其信息
     */
    private void setHotSearchData() {
        mHotSearchAdapter = new HotSearchAdapter(this, hotSearchData);
        mHotSearchView.setAdapter(mHotSearchAdapter);
        mHotSearchView.setLayoutManager(new GridLayoutManager(this, 3));

        mHotSearchAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                String content = hotSearchData.get(position);
                doData(content);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int
                    position) {
                return false;
            }
        });
    }

    /**
     * 初始化历史搜索相关的适配器及其信息
     */
    private void setHistorySearchData() {

        mHistorySearchAdapter = new HistorySearchAdapter(this, historySearchData);
        mHistorySearchView.setAdapter(mHistorySearchAdapter);
        mHistorySearchView.setLayoutManager(new GridLayoutManager(this, 3));

        mHistorySearchAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                String content = historySearchData.get(position);
                doData(content);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int
                    position) {
                return false;
            }
        });
    }





    /**
     * 历史搜索
     * 必须进行判断是不是本来就停留在这个界面进行操作,还是初始化进入
     * 要不然,会重复添加数据
     */
    private void getHistorydata() {

        String histortStr = PreferencesUtils.getString(SearchActivity.this, "histortStr");

        if (histortStr != null) {
            historySearchData = new Gson().fromJson(histortStr, new TypeToken<List<String>>() {
            }.getType());
        }

    }


    /**
     * 操作数据库数据
     */
    private int position = -1;

    private void doData(String content) {

        //有历史数据
        if (historySearchData != null && historySearchData.size() > 0) {
            for (int i = 0; i < historySearchData.size(); i++) {
                if (content.equals(historySearchData.get(i))) {
                    //有重复的
                    position = i;
                }
            }

            if (position != -1) {
                historySearchData.remove(position);
                historySearchData.add(0, content);
            } else {
                historySearchData.add(0, content);
            }

        } else {
            //没有历史数据
            historySearchData.add(content);
        }

        mHistorySearchAdapter.notifyDataSetChanged();
        String histortStr = new Gson().toJson(historySearchData);
        PreferencesUtils.putString(SearchActivity.this, "histortStr", histortStr);

        Bundle bundle = new Bundle();
        bundle.putString("search", content);
        Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);     //跳转到搜索结果界面

    }

    @OnClick({R.id.iv_back, R.id.gosearch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.gosearch:
                String content = mEditText.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    ToastUtils.showShortToast(SearchActivity.this, "还没输入您想搜索的宝贝呢");
                    return;
                }
                doData(content);
                break;
        }
    }
}


