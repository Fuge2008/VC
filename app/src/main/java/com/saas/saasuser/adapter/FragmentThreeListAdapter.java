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
import com.saas.saasuser.activity.ApplyContactDetailActivity;
import com.saas.saasuser.activity.PlanContactDetailActivity;
import com.saas.saasuser.activity.ReplyContactDetailActivity;
import com.saas.saasuser.activity.TaskContactDetailActivity;
import com.saas.saasuser.global.Constants;
import com.saas.saasuser.global.NetIntent;
import com.saas.saasuser.global.NetIntentCallBackListener;
import com.saas.saasuser.util.StringUtils;
import com.saas.saasuser.util.ToastUtils;
import com.saas.saasuser.view.CircleImageView;
import com.squareup.okhttp.Request;

import java.util.List;


public class FragmentThreeListAdapter extends BaseAdapter implements NetIntentCallBackListener.INetIntentCallBack{

    private Context mContext;
    private List<JSONObject> orderData;
    private LayoutInflater inflater;


    public FragmentThreeListAdapter(Activity context, List<JSONObject> jsonArray) {
        this.mContext = context;
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
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_data1 = (TextView) convertView.findViewById(R.id.tv_data1);
            holder.tv_data2 = (TextView) convertView.findViewById(R.id.tv_data2);
            holder.tv_data3 = (TextView) convertView.findViewById(R.id.tv_data3);
            holder.tv_data4 = (TextView) convertView.findViewById(R.id.tv_data4);
            holder.tv_data5 = (TextView) convertView.findViewById(R.id.tv_data5);
            holder.tv_data6 = (TextView) convertView.findViewById(R.id.tv_data6);
            holder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
            holder.iv_head_image = (CircleImageView) convertView.findViewById(R.id.iv_head_image);
            holder.iv_red_point = (ImageView) convertView.findViewById(R.id.iv_red_point);
            holder.rl_main_item = (RelativeLayout) convertView.findViewById(R.id.rl_main_item);
            convertView.setTag(holder);
        }
        final JSONObject json = orderData.get(position);//.TODO
        final String orderId = json.getString("BOID");
        final String orderType = json.getString("OrderType");


        final String strDate = json.getString("BOUseDate");
        final String strTime = json.getString("BOUStartTime");

        final String strStartAddress = json.getString("BOUpCarPlace");
        final String strEndAddress = json.getString("BODownCarPlace");

        final String strOrderStatue = json.getString("BOOrderStatue");//0审核不通过,2待审核 3审核通过
        final String strTripDestination = json.getString("BODestination");//行程目的地
        final String strTripPurpose = json.getString("BOPurposeDsc");//用途说明（用车目的）
        final String strUserName = json.getString("StaffName");//用车人姓名
        final String strUserPhone = json.getString("StaffPhone");//用车人姓名
        final String strDriverName = json.getString("DriverName");//司机姓名
        final String strDriverPhone = json.getString("DriverPhone");//司机联系方式
        final String strisRead = json.getString("IsRead");//标记是否已读, 0未读 1已读
        final String strHeadPath=  json.getString("headimg");
        String orderStatue="";
        if (StringUtils.equals("0", strisRead)) {
            holder.iv_red_point.setVisibility(View.VISIBLE);
        } else {
            holder.iv_red_point.setVisibility(View.GONE);
        }
        if(StringUtils.isNotEmpty(strHeadPath,true)){
            Glide.with(mContext).load(Constants.ImageHttpRoot+strHeadPath).into(holder.iv_head_image);
        }

        if (StringUtils.equals("1", orderType)) {//申请联络
            holder.tv_title.setText("申请联络");
            holder.tv_status.setText("待审核");
            holder.tv_data1.setText("申 请 人： " + StringUtils.repalceEmptyString(strUserName));
            holder.tv_data2.setText("申请人电话： " + StringUtils.repalceEmptyString( strUserPhone));
            holder.tv_data3.setText("出行时间： " + StringUtils.repalceEmptyString(StringUtils.tripData(strDate)) + "   " + StringUtils.repalceEmptyString(strTime));
            holder.tv_data4.setText("上车地点： " + StringUtils.repalceEmptyString(strStartAddress));
            holder.tv_data5.setText("行程目的地： " + StringUtils.repalceEmptyString(strTripDestination));
        } else if (StringUtils.equals("2", orderType)) {//批复联络

            if(StringUtils.equals("0", strOrderStatue)){
                orderStatue="审批不通过";
            }else if(StringUtils.equals("2", strOrderStatue)){
                orderStatue="审批中";
            }else{
                orderStatue="审批通过";
            }
            holder.tv_title.setText("批复联络");
            holder.tv_data1.setText("领    导： " +  StringUtils.repalceEmptyString(strUserName));
            holder.tv_data2.setVisibility(View.GONE);
            holder.tv_status.setText(orderStatue);
            holder.tv_data3.setText("领导电话： " +  StringUtils.repalceEmptyString( strUserPhone));
            holder.tv_data4.setText("出行时间： " + StringUtils.repalceEmptyString(StringUtils.tripData(strDate)) + "   " + StringUtils.repalceEmptyString(strTime));
            holder.tv_data5.setText("上车地点： " + StringUtils.repalceEmptyString(strStartAddress));
            holder.tv_data6.setVisibility(View.VISIBLE);
            holder.tv_data6.setText("行程目的地： " + StringUtils.repalceEmptyString(strTripDestination));
        } else if (StringUtils.equals("3", orderType)) {//安排联络
            holder.tv_title.setText("安排联络");
            holder.tv_status.setText("待确认");
            holder.tv_data1.setText("司    机： " + StringUtils.repalceEmptyString(strDriverName));
            holder.tv_data2.setText("司机电话： " + StringUtils.repalceEmptyString(strDriverPhone));
            holder.tv_data3.setText("用车日期： " + StringUtils.repalceEmptyString(StringUtils.tripData(strDate)) + "   " + StringUtils.repalceEmptyString(strTime));
            holder.tv_data4.setText("上车地点： " + StringUtils.repalceEmptyString(strStartAddress));
            holder.tv_data5.setText("行程目的地： " + StringUtils.repalceEmptyString(strTripDestination));

        } else if (StringUtils.equals("4", orderType)) {//任务联络
            holder.tv_title.setText("任务联络");
            holder.tv_status.setText("待确认");
            holder.tv_data1.setText("乘 车 人： " + StringUtils.repalceEmptyString(strUserName));
            holder.tv_data2.setText("乘客电话： " + StringUtils.repalceEmptyString( strUserPhone));
            holder.tv_data3.setText("出行时间： " + StringUtils.repalceEmptyString(StringUtils.tripData(strDate)) + "   " + StringUtils.repalceEmptyString(strTime));
            holder.tv_data4.setText("上车地点： " + StringUtils.repalceEmptyString(strStartAddress));
            holder.tv_data5.setText("行程目的地： " + StringUtils.repalceEmptyString(strTripDestination));
        }



        holder.rl_main_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new NetIntent().client_markOrderIsReaded(orderId, orderType, new NetIntentCallBackListener(FragmentThreeListAdapter.this));
                if (StringUtils.equals("1", orderType)) {
                    mContext.startActivity(new Intent(mContext, ApplyContactDetailActivity.class).putExtra("orderId", orderId));
                } else if (StringUtils.equals("2", orderType)) {
                    if(StringUtils.equals("2",strOrderStatue)){
                        ToastUtils.showShortToast(mContext,"订单正在审核中，请稍后！");
                    }else{
                        mContext.startActivity(new Intent(mContext, ReplyContactDetailActivity.class).putExtra("orderId", orderId).putExtra("orderStatue", strOrderStatue));
                    }

                } else if (StringUtils.equals("3", orderType)) {
                    mContext.startActivity(new Intent(mContext, PlanContactDetailActivity.class).putExtra("orderId", orderId));
                } else if (StringUtils.equals("4", orderType)) {
                    mContext.startActivity(new Intent(mContext, TaskContactDetailActivity.class).putExtra("orderId", orderId));
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
        TextView tv_title, tv_data1, tv_data2, tv_data3, tv_data4,tv_data5,tv_data6,tv_status;
        ImageView iv_red_point;
        RelativeLayout rl_main_item;
        CircleImageView iv_head_image;
    }


}
