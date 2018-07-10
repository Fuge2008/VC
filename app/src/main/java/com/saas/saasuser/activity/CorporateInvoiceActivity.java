package com.saas.saasuser.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.saas.saasuser.R;
import com.saas.saasuser.application.MyApplication;
import com.saas.saasuser.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tanlin on 2017/11/28.
 */

public class CorporateInvoiceActivity extends BaseActivity {

    @BindView(R.id.llzengzhishui)
    LinearLayout ll_zengzhishui;
    @BindView(R.id.llputong)
    LinearLayout ll_putong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corporate_invoice);
        MyApplication.getInstance().addActivity(this);
        ButterKnife.bind(this);
        Util.setHeadTitleMore(this, "企业发票", true);

    }
    @OnClick({R.id.llzengzhishui, R.id.llputong})
    public void zengzhishui(View view) {
        switch (view.getId()) {
            case R.id.llzengzhishui:
                Intent intent = new Intent(this, RegularInvoiceActivity.class);
                startActivity(intent);
                break;
            case R.id.llputong:
                Intent intent1 = new Intent(this, ValueAddedTaxInvoiceActivity.class);
                startActivity(intent1);
                break;


        }
    }


}
