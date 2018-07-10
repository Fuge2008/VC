package com.saas.saasuser.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.saas.saasuser.R;



public class Util {

    public static void setHeadTitleMore(final Context context, String title, boolean leftOnClick) {
        try {
            TextView mHeadTitle = (TextView) ((Activity) context).findViewById(R.id.head_title);
            mHeadTitle.setText(title);
            if (leftOnClick) {
                ((Activity) context).findViewById(R.id.head_back).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((Activity) context).finish();
                    }
                });
            }
        } catch (Exception e) {
            ToastUtils.showShortToastSafe(context, "初始化标题出错" + e.toString());
        }
    }

}
