package com.saas.saasuser.adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.saas.saasuser.R;
import com.saas.saasuser.bean.OrderTotalItem;
import com.saas.saasuser.util.ToastUtils;
import com.saas.saasuser.view.OrderTotalSlideView;

import java.util.ArrayList;
import java.util.List;


public class OrderUnconfirmAdapter extends BaseAdapter implements View.OnClickListener ,  OrderTotalSlideView.OnSlideListener{
    private LayoutInflater mInflater;
    private Activity mContext;
    private List<OrderTotalItem> mOrderTotalItems=new ArrayList<>();
    private OrderTotalSlideView mLastSlideViewWithStatusOn;
    public OrderUnconfirmAdapter(Activity context, List<OrderTotalItem> datas) {
        super();
        mContext=context;
        mInflater = LayoutInflater.from( mContext);
        mOrderTotalItems=datas;
    }

    @Override
    public int getCount() {
        return mOrderTotalItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mOrderTotalItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
      OrderTotalSlideView  mOrderTotalSlideView = (OrderTotalSlideView) convertView;
        if (mOrderTotalSlideView == null) {
            View itemView = mInflater.inflate(R.layout.item_order_total, null);

            mOrderTotalSlideView = new OrderTotalSlideView(mContext);
            mOrderTotalSlideView.setContentView(itemView);

            holder = new ViewHolder(mOrderTotalSlideView);
            mOrderTotalSlideView.setOnSlideListener(OrderUnconfirmAdapter.this);
            mOrderTotalSlideView.setTag(holder);
        } else {
            holder = (ViewHolder)mOrderTotalSlideView.getTag();
        }
        OrderTotalItem item = mOrderTotalItems.get(position);
        item.slideView =mOrderTotalSlideView;
        item.slideView.shrink();

        holder.iv_order_status.setImageResource(item.iconRes);
        holder.tv_date.setText(item.date);
        holder.tv_start_position.setText(item.startLocation);
        holder.tv_mid_position.setText(item.midLocation);
        holder.tv_end_position.setText(item.endLocation);

        return   mOrderTotalSlideView;
    }
    private static class ViewHolder {
        public ImageView iv_order_status;
        public TextView tv_date,tv_start_position,tv_mid_position,tv_end_position;

        public TextView  delete,edit;

        ViewHolder(View view) {
            iv_order_status = (ImageView) view.findViewById(R.id.iv_order_status);
            tv_date = (TextView) view.findViewById(R.id.tv_date);
            tv_start_position= (TextView) view.findViewById(R.id.tv_start_position);
            tv_mid_position = (TextView) view.findViewById(R.id.tv_mid_position);
          tv_end_position = (TextView) view.findViewById(R.id.tv_end_position);
            delete = (TextView) view.findViewById(R.id.delete);
            edit = (TextView) view.findViewById(R.id.edit);
        }
    }
    @Override
    public void onSlide(View view, int status) {
        if (mLastSlideViewWithStatusOn != null && mLastSlideViewWithStatusOn != view) {
            mLastSlideViewWithStatusOn.shrink();
        }

        if (status == SLIDE_STATUS_ON) {
            mLastSlideViewWithStatusOn = (OrderTotalSlideView) view;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_cancle) {
            ToastUtils.showShortToast(mContext,"暂时不能取消！");
        }else if(v.getId() == R.id.tv_edit){
            ToastUtils.showShortToast(mContext,"暂时不能编辑！");
        }else if(v.getId() == R.id.tv_sure){
            ToastUtils.showShortToast(mContext,"暂时不能确定！");
        }
    }

}
