package com.saas.saasuser.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.saas.saasuser.R;
import com.saas.saasuser.adapter.ViewPagerAdapter;
import com.saas.saasuser.application.MyApplication;
import com.saas.saasuser.global.Constants;
import com.saas.saasuser.util.PreferencesManager;

import java.util.ArrayList;



public class GuideActivity extends BaseActivity2 implements ViewPager.OnPageChangeListener {

    private ViewPager guidePages;
    private ArrayList<View> pageViews;
    private ImageView imageView;
    private ImageView[] imageViews;

    private TextView tv_skip;
    private boolean isFirstRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        isFirstRun = PreferencesManager.getInstance(this).get(Constants.IS_FIRST_RUN, true);

        setContentView(R.layout.layout_guide);
        MyApplication.getInstance().addActivity(this);
        setHeadVisibility(View.GONE);
        PreferencesManager.getInstance(mContext.getApplicationContext()).put(Constants.IS_FIRST_RUN, false);


        if(!isFirstRun){

            Intent intent = new Intent(GuideActivity.this, SplashActivity.class);
            startActivity(intent);
            finish();
            return ;


        }

        LayoutInflater inflater = getLayoutInflater();
        pageViews = new ArrayList<View>();
        pageViews.add(inflater.inflate(R.layout.layout_guide_item1, null));
        pageViews.add(inflater.inflate(R.layout.layout_guide_item2, null));
        pageViews.add(inflater.inflate(R.layout.layout_guide_item3, null));

        tv_skip= (TextView) findViewById(R.id.skip);

        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuideActivity.this, SplashActivity.class);
                startActivity(intent);
                finish();
            }
        });




        imageViews = new ImageView[pageViews.size()];
        guidePages = (ViewPager) findViewById(R.id.guidePages);




        guidePages.setAdapter(new ViewPagerAdapter(pageViews));
        guidePages.setOnPageChangeListener(this);
    }


    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int position) {


        //点击最后一页，跳转到主页
        if(position == 2){
            View itemView = pageViews.get(position);
           TextView tt= (TextView) itemView.findViewById(R.id.click_experice);
            tt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(mContext, SplashActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
}
