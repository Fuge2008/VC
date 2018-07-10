package com.saas.saasuser.adapter;

import android.content.Context;

import com.saas.saasuser.R;
import com.saas.saasuser.view.recyclerview.CommonAdapter;
import com.saas.saasuser.view.recyclerview.base.ViewHolder;

import java.util.List;



public class HistorySearchAdapter extends CommonAdapter<String> {

    public HistorySearchAdapter(Context context, List<String> datas) {
        super(context, R.layout.item_search, datas);
    }

    @Override
    protected void convert(ViewHolder holder, String s, int position) {
        holder.setText(R.id.tv_content, s);
    }
}
