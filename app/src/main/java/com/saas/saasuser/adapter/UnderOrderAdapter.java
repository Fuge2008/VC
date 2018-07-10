//package com.saas.saasuser.adapter;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.alibaba.fastjson.JSONObject;
//import com.saas.saasuser.R;
//import com.saas.saasuser.activity.DriverTaskExcuteStatusActivity;
//import com.saas.saasuser.activity.UserTaskExcuteStatusActivity;
//import com.saas.saasuser.util.StringUtils;
//
//import java.util.List;
//
//
///**
// * 当前正在执行的订单
// */
//public class UnderOrderAdapter extends BaseAdapter {
//    private Context mContext;
//    private List<JSONObject> orderData;
//    private LayoutInflater inflater;
//
//    public UnderOrderAdapter(Activity context, List<JSONObject> jsonArray) {
//        this. mContext = context;
//        this.orderData = jsonArray;
//        inflater = LayoutInflater.from(context);
//
//    }
//
//    @Override
//    public int getCount() {
//        return orderData.size();
//    }
//
//    @Override
//    public JSONObject getItem(int position) {
//        return orderData.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//
//    @Override
//    public View getView(final int position, View convertView,
//                        ViewGroup parent) {
//
//        convertView = inflater.inflate(R.layout.item_emnu_card, parent, false);
//        ViewHolder holder = (ViewHolder) convertView.getTag();
//        if (holder == null) {
//            holder = new ViewHolder();
//            holder.cardTitle = (TextView)  convertView.findViewById(R.id.card_title);
//            holder.tvData = (TextView)  convertView.findViewById(R.id.tv_data);
//            holder.tvTime = (TextView)  convertView.findViewById(R.id.tv_time);
//            holder.tvStartAddress = (TextView)  convertView.findViewById(R.id.tv_start_address);
//            holder.tvEndAddress = (TextView)  convertView.findViewById(R.id.tv_end_address);
//            holder.ivMenuCard= (ImageView)  convertView.findViewById(R.id.iv_menu_card);
//            convertView.setTag(holder);
//        }
//        final JSONObject json =  orderData.get(position);//.TODO
//        final String orderId=  json.getString("BOID");
//        final String orderType=  json.getString("OrderType");
//
//        final String strOrderStatue=  json.getString("BOOrderStatue");
//        final String strDate=  json.getString("BOUseDate");
//        final String strTime=  json.getString("BOUStartTime");
//
//        final String strStartAddress=  json.getString("BOUpCarPlace");
//        final String strEndAddress=  json.getString("BODownCarPlace");
//        final String strStatue=  json.getString("BOOrderStatue");
//        final String strUserTyype=  json.getString("OrderType");
//
//        holder.cardTitle.setText("当前订单");
//        holder.tvData.setText("日期："+ StringUtils.repalceEmptyString(StringUtils.tripData(strDate)));
//        holder.tvTime.setText("时间："+ StringUtils.repalceEmptyString(strTime));
//        holder.tvStartAddress.setText("起点："+ StringUtils.repalceEmptyString(strStartAddress));
//        holder.tvEndAddress.setText("终点："+ StringUtils.repalceEmptyString(strEndAddress));
//
//        String strTag= StringUtils.equals("1",strUserTyype)?"乘车中":"载客中";
//
//        if(StringUtils.equals("1",strStatue)){
//            holder.ivMenuCard.setBackgroundResource(R.drawable.cankaolianluo);
//            holder.cardTitle.setText(strTag+"(任务开始)");
//        }else if(StringUtils.equals("2",strStatue)){
//            holder.ivMenuCard.setBackgroundResource(R.drawable.pifulianluo);
//            holder.cardTitle.setText(strTag+"(抵达)");
//
//        }else if(StringUtils.equals("3",strStatue)){
//            holder.ivMenuCard.setBackgroundResource(R.drawable.anpailianluo);
//            holder.cardTitle.setText(strTag+"(开始订单)");
//        }
//
//        //mContext.startActivity(new Intent(mContext, IndoorContactActivity.class).putExtra("orderId",orderId));
//        holder.ivMenuCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(StringUtils.equals("1",strUserTyype)){
//                    mContext.startActivity(new Intent(mContext, UserTaskExcuteStatusActivity.class).putExtra("orderId",orderId).putExtra("orderStatue",strStatue));
//                }else if(StringUtils.equals("2",strUserTyype)){
//                    mContext.startActivity(new Intent(mContext, DriverTaskExcuteStatusActivity.class).putExtra("orderId",orderId).putExtra("orderStatue",strStatue));
//                }
//
//            }
//        });
//        return convertView;
//
//    }
//
//
//    public static class ViewHolder {
//        TextView cardTitle,tvData,tvTime,tvStartAddress,tvEndAddress;
//        ImageView ivMenuCard;
//    }
//
//}
