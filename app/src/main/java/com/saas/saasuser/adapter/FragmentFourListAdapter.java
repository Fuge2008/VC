package com.saas.saasuser.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.saas.saasuser.R;
import com.saas.saasuser.activity.ConfirmPlanDetailActivity;
import com.saas.saasuser.activity.ConfirmTaskDetailActivity;
import com.saas.saasuser.global.Constants;
import com.saas.saasuser.global.NetIntent;
import com.saas.saasuser.global.NetIntentCallBackListener;
import com.saas.saasuser.util.StringUtils;
import com.saas.saasuser.view.CircleImageView;
import com.squareup.okhttp.Request;

import java.util.List;


public class FragmentFourListAdapter extends BaseAdapter  implements NetIntentCallBackListener.INetIntentCallBack{

    private Context mContext;
    private List<JSONObject> orderData;
    private LayoutInflater inflater;




    public FragmentFourListAdapter(Activity context, List<JSONObject> jsonArray) {
        this. mContext = context;
        this.orderData = jsonArray;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return orderData.size();
    }

    @Override
    public JSONObject getItem(int position) {
            return orderData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView,
                        ViewGroup parent) {

            convertView = inflater.inflate(R.layout.item_contact_message, parent, false);
            ViewHolder holder = (ViewHolder) convertView.getTag();
            if (holder == null) {
                holder = new ViewHolder();
                holder.tv_title = (TextView)  convertView.findViewById(R.id.tv_title);
                holder.tv_data1= (TextView)  convertView.findViewById(R.id.tv_data1);
                holder.tv_data2 = (TextView)  convertView.findViewById(R.id.tv_data2);
                holder.tv_data3 = (TextView)  convertView.findViewById(R.id.tv_data3);
                holder.tv_data4 = (TextView)  convertView.findViewById(R.id.tv_data4);
                holder.tv_data5 = (TextView)  convertView.findViewById(R.id.tv_data5);
                holder.tv_status = (TextView)  convertView.findViewById(R.id.tv_status);
                holder.iv_head_image= (CircleImageView)  convertView.findViewById(R.id.iv_head_image);
                holder.iv_red_point= (ImageView)  convertView.findViewById(R.id.iv_red_point);
                holder.rl_main_item = (RelativeLayout) convertView.findViewById(R.id.rl_main_item);
                convertView.setTag(holder);
            }
        final JSONObject json =  orderData.get(position);//.TODO
        final String orderId=  json.getString("BOID");
        final String orderType=  json.getString("OrderType");
        final String strDate=  json.getString("BOUseDate");
        final String strTime=  json.getString("BOUStartTime");
        final String strStartAddress=  json.getString("BOUpCarPlace");
        final String strEndAddress=  json.getString("BODownCarPlace");
        final String strOrderStatue=  json.getString("BOOrderStatue");
        final String strTripDestination=  json.getString("BODestination");//行程目的地
        final String strDriverName=  json.getString("DriverName");//司机姓名
        final String strDriverPhone=  json.getString("DriverPhone");//司机联系方式
        final String strisRead=  json.getString("IsRead");//标记是否已读,  0未读 1已读
        final String strHeadPath=  json.getString("headimg");
        final String strOrderComfireStatue=  json.getString("AffirmState");





        if (StringUtils.equals("0", strisRead)) {
            holder.iv_red_point.setVisibility(View.VISIBLE);
        } else {
            holder.iv_red_point.setVisibility(View.GONE);
        }
        if (StringUtils.equals("0", strOrderComfireStatue)) {
            holder.tv_status.setText("待确认");
        } else if(StringUtils.equals("1", strOrderComfireStatue)){
            holder.tv_status.setText("已确认");
        }else{
            holder.tv_status.setText("未知");
        }
        if(StringUtils.isNotEmpty(strHeadPath,true)){
            Glide.with(mContext).load(Constants.ImageHttpRoot+strHeadPath).into(holder.iv_head_image);
        }

        if(StringUtils.equals("6",orderType)){
            holder.tv_title.setText("已知安排");
            holder.tv_data1.setText("司    机： " + StringUtils.repalceEmptyString(strDriverName));
            holder.tv_data2.setText("司机电话： " + StringUtils.repalceEmptyString(strDriverPhone));
            holder.tv_data3.setText("出行时间： " + StringUtils.repalceEmptyString(StringUtils.tripData(strDate)) + "   " + StringUtils.repalceEmptyString(strTime));
            holder.tv_data4.setText("上车地点： " + StringUtils.repalceEmptyString(strStartAddress));
            holder.tv_data5.setText("行程目的地： " + StringUtils.repalceEmptyString(strTripDestination));

        }else if(StringUtils.equals("5",orderType)){
            holder.tv_title.setText("已知任务");
            holder.tv_data1.setText("乘    客： " + StringUtils.repalceEmptyString(strDriverName));
            holder.tv_data2.setText("乘客电话： " + StringUtils.repalceEmptyString(strDriverPhone));
            holder.tv_data3.setText("出行时间： " + StringUtils.repalceEmptyString(StringUtils.tripData(strDate)) + "   " + StringUtils.repalceEmptyString(strTime));
            holder.tv_data4.setText("上车地点： " + StringUtils.repalceEmptyString(strStartAddress));
            holder.tv_data5.setText("行程目的地： " + StringUtils.repalceEmptyString(strTripDestination));

        }






        holder.rl_main_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//已知安排
                new NetIntent().client_markOrderIsReaded(orderId, orderType, new NetIntentCallBackListener(FragmentFourListAdapter.this));
                if(StringUtils.equals("6",orderType)){
//                        if(StringUtils.equals("0",strOrderComfireStatue)){//对方尚未确认订单
//                            ToastUtils.showShortToast(mContext,"司机尚未确认订单，请稍后！");
//                        }else {//对方已经确认
//
//                        }
                    mContext.startActivity(new Intent(mContext, ConfirmPlanDetailActivity.class).putExtra("orderId",orderId).putExtra("statue",strOrderComfireStatue));

                }else if(StringUtils.equals("5",orderType)){//已知任务
//                    if(StringUtils.equals("0",strOrderComfireStatue)){//对方尚未确认订单
//                        ToastUtils.showShortToast(mContext,"乘客尚未确认订单，请稍后！");
//                    }else {//对方已经确认
//
//                    }
                    mContext.startActivity(new Intent(mContext, ConfirmTaskDetailActivity.class).putExtra("orderId",orderId).putExtra("statue",strOrderComfireStatue));

                }

            }
        });
        return convertView;

    }

    @Override
    public void onError(Request request, Exception e) {

    }

    @Override
    public void onResponse(String response) {

    }


    public static class ViewHolder {
        TextView tv_title,tv_data1,tv_data2,tv_data3,tv_data4,tv_data5,tv_status;
        ImageView iv_red_point;
        CircleImageView iv_head_image;
        RelativeLayout rl_main_item;
    }


}
