package com.saas.saasuser.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.saas.saasuser.R;
import com.saas.saasuser.adapter.BaseFragmentAdapter;
import com.saas.saasuser.fragment.OrderFinishFragment;
import com.saas.saasuser.fragment.OrderNotStartedFragment;
import com.saas.saasuser.fragment.OrderUnconfirmedFragment;
import com.saas.saasuser.util.Util;
import com.saas.saasuser.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



public class OrderTotalShowActivity extends BaseActivity {

    @BindView(R.id.btn_submit)
    TextView btnSubmit;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    NoScrollViewPager viewPager;
    private List<String> mTitles = new ArrayList<String>() {
        {
            add("待确认");
            add("待出发");
            add("已结束");
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_total_show);
        ButterKnife.bind(this);
        Util.setHeadTitleMore(this, "我的订单", true);
        btnSubmit.setVisibility(View.GONE);
        initSelect();
        initViewPager();

    }
    /**
     * 初始化水平选择栏
     */
    private void initSelect() {
        //设置TabLayout的模式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        //为TabLayout添加tab名称
        //需要自定义布局时，需要在绑定viewpager前加入布局
        for (int i = 0; i < mTitles.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(mTitles.get(i)));
        }
    }
    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        final ArrayList<Fragment> fragmentList = new ArrayList<>();
        OrderUnconfirmedFragment unconfirmedFragment=new OrderUnconfirmedFragment();
        OrderNotStartedFragment notStartedFragment=new OrderNotStartedFragment();
        OrderFinishFragment finishFragment=new OrderFinishFragment();
        fragmentList.add(unconfirmedFragment);
        fragmentList.add(notStartedFragment);
        fragmentList.add(finishFragment);

        FragmentPagerAdapter fragmentAdapter = new BaseFragmentAdapter(getSupportFragmentManager(), fragmentList, mTitles);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

        });
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
//                ((MerchandiseListBaseFragment) fragmentList.get(tab.getPosition())).fresh();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

}
