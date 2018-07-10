package com.saas.saasuser.activity;

import android.graphics.Color;
import android.os.Bundle;

import com.saas.saasuser.R;
import com.saas.saasuser.util.Util;
import com.saas.saasuser.view.RingStatisticsView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 订单数据统计
 */
public class OrderStatisticsActivity extends BaseActivity {

    @BindView(R.id.rsv_statistics)
    RingStatisticsView rsvStatistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_statistics);
        ButterKnife.bind(this);
        Util.setHeadTitleMore(this, "订单统计", true);
        rsvStatistics.setPercentAndColors(new float[]{0.46f,0.54f},new int[]{ Color.parseColor("#2EC1FB"), Color.parseColor("#FA6723")},new String[]{"乘车   ","载客   "});
        rsvStatistics.refresh();

    }
}
