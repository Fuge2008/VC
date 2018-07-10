package com.saas.saasuser.activity.map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.saas.saasuser.R;
import com.saas.saasuser.activity.BaseActivity;
import com.saas.saasuser.application.MyApplication;
import com.saas.saasuser.util.LogUtils;
import com.saas.saasuser.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 所有轨迹list展示activity
 */
public class RecordActivity extends BaseActivity implements OnItemClickListener {

    @BindView(R.id.head_more)
    LinearLayout headMore;
    private TraceRecordAdapter mAdapter;
    private ListView mAllRecordListView;
    private DbAdapter mDataBaseHelper;
    private List<PathRecord> mAllRecord = new ArrayList<PathRecord>();
    public static final String RECORD_ID = "record_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trace_record_list);
        MyApplication.getInstance().addActivity(this);
        ButterKnife.bind(this);
        Util.setHeadTitleMore(this, "轨迹记录", true);
        headMore.setVisibility(View.GONE);
        mAllRecordListView = (ListView) findViewById(R.id.recordlist);
        mDataBaseHelper = new DbAdapter(this);
        mDataBaseHelper.open();
        searchAllRecordFromDB();
        mAdapter = new TraceRecordAdapter(this, mAllRecord);
        mAllRecordListView.setAdapter(mAdapter);
        mAllRecordListView.setOnItemClickListener(this);
    }

    private void searchAllRecordFromDB() {
        mAllRecord = mDataBaseHelper.queryRecordAll();
        for(int i=0;i<mAllRecord.size();i++){
            PathRecord p=mAllRecord.get(i);
            LogUtils.e("打印轨迹数据："+p.toString());

        }

    }

    public void onBackClick(View view) {
        this.finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        PathRecord recorditem = (PathRecord) parent.getAdapter().getItem(
                position);
        Intent intent = new Intent(RecordActivity.this,
                TraceRecordActivity.class);
        intent.putExtra(RECORD_ID, recorditem.getId());
        startActivity(intent);
    }
}
