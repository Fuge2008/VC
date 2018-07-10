package com.saas.saasuser.adapter;


import android.content.Context;

import com.saas.saasuser.R;
import com.saas.saasuser.entity.PoiData;

import java.util.List;

/**
 * 地址列表Adapter
 */
public class SearchAddressAdapter extends MyBaseAdapter<PoiData> {

    public  SearchAddressAdapter(Context context, int resource, List<PoiData> list) {
        super(context, resource, list);
    }

    @Override
    public void setConvert(BaseViewHolder viewHolder, PoiData data) {
        viewHolder.setTextView(R.id.item_address_name_tv, data.getName());
        viewHolder.setTextView(R.id.item_address_detail_tv, data.getAddress());
    }

}
