package com.saas.saasuser.adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.saas.saasuser.R;
import com.saas.saasuser.bean.MessageItem;
import com.saas.saasuser.util.ToastUtils;
import com.saas.saasuser.view.SlideView;

import java.util.ArrayList;
import java.util.List;



public class SlideAdapter extends BaseAdapter implements View.OnClickListener ,SlideView.OnSlideListener{
    private LayoutInflater mInflater;
    private Activity mContext;
    private List<MessageItem> mMessageItems=new ArrayList<>();
    private SlideView mLastSlideViewWithStatusOn;
    public SlideAdapter(Activity context, List<MessageItem> datas) {
        super();
        mContext=context;
        mInflater = LayoutInflater.from( mContext);
        mMessageItems=datas;
    }

    @Override
    public int getCount() {
        return mMessageItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mMessageItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        SlideView slideView = (SlideView) convertView;
        if (slideView == null) {
            View itemView = mInflater.inflate(R.layout.list_item, null);

            slideView = new SlideView(mContext);
            slideView.setContentView(itemView);

            holder = new ViewHolder(slideView);
            slideView.setOnSlideListener(SlideAdapter.this);
            slideView.setTag(holder);
        } else {
            holder = (ViewHolder) slideView.getTag();
        }
        MessageItem item = mMessageItems.get(position);
        item.slideView = slideView;
        item.slideView.shrink();

        holder.icon.setImageResource(item.iconRes);
        holder.title.setText(item.title);
        holder.msg.setText(item.msg);
        holder.time.setText(item.time);


        return slideView;
    }
    private static class ViewHolder {
        public ImageView icon;
        public TextView title;
        public TextView msg;
        public TextView time;
        public TextView  delete,edit;

        ViewHolder(View view) {
            icon = (ImageView) view.findViewById(R.id.iv_order_status);
            title = (TextView) view.findViewById(R.id.title);
            msg = (TextView) view.findViewById(R.id.msg);
            time = (TextView) view.findViewById(R.id.time);
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
            mLastSlideViewWithStatusOn = (SlideView) view;
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
