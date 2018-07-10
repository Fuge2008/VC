package com.saas.saasuser.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.saas.saasuser.R;
import com.saas.saasuser.application.MyApplication;
import com.saas.saasuser.util.ToastUtils;
import com.saas.saasuser.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SearchResultActivity extends BaseActivity {

    @BindView(R.id.tv_result)
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        MyApplication.getInstance().addActivity(this);
        ButterKnife.bind(this);
        Util.setHeadTitleMore(this, "搜索结果", true);
        String search = getIntent().getStringExtra("search");
        if (!TextUtils.isEmpty(search)) {
            mTextView.setText("暂无结果");
            ToastUtils.showShortToast(SearchResultActivity.this,"搜不到关于“"+search+"”的结果！");
        }
    }


}