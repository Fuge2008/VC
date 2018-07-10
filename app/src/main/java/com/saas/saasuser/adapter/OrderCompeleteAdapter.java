package com.saas.saasuser.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.saas.saasuser.R;
import com.saas.saasuser.activity.OrderDriverCompeleteActivity;
import com.saas.saasuser.activity.OrderUserCompeleteActivity;
import com.saas.saasuser.util.StringUtils;
import com.saas.saasuser.util.ToastUtils;

import java.util.List;



public class OrderCompeleteAdapter extends BaseAdapter {

    private Context mContext;
    private List<JSONObject> orderData;
    private LayoutInflater inflater;




    public  OrderCompeleteAdapter (Activity context, List<JSONObject> jsonArray) {
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

        convertView = inflater.inflate(R.layout.item_order_record_list, parent, false);
        ViewHolder  viewHolder = (ViewHolder) convertView.getTag();
        if ( viewHolder == null) {
            viewHolder = new ViewHolder();
            //viewHolder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
            viewHolder.tv_type = (TextView)convertView.findViewById(R.id.tv_type);
            viewHolder.ll_menu = (LinearLayout) convertView.findViewById(R.id.ll_menu);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.tv_date = (TextView)convertView.findViewById(R.id.tv_date);
            viewHolder.tv_start_position = (TextView) convertView.findViewById(R.id.tv_start_position);
            viewHolder.tv_end_position = (TextView) convertView.findViewById(R.id.tv_end_position);
            viewHolder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
            convertView.setTag( viewHolder);

        }
        final JSONObject json =  orderData.get(position);//.TODO
        final String orderId=  json.getString("BOID");
        String orderNumber=  json.getString("BOOrderNum");
        final String strTag=  json.getString("OFIsFianl");
        String strDate=  json.getString("BOUseDate");
        String strTime=  json.getString("BOUStartTime");
        String strStartAddress=  json.getString("BOUpCarPlace");
        String strEndAddress=  json.getString("BODownCarPlace");
        final String strRole=  json.getString("IsRloe");
        String strOtherPhone=  json.getString("PhoneNum");
        String strOtherName=  json.getString("Name");
        String strOtherImage=  json.getString("HeadImg");
        viewHolder.tv_time.setText(strTime);
        viewHolder.tv_date.setText("时间： "+StringUtils.tripData(strDate));
        viewHolder.tv_start_position.setText("出发地： "+StringUtils.repalceEmptyString(strStartAddress));
        viewHolder.tv_end_position.setText("目的地： "+StringUtils.repalceEmptyString(strEndAddress));


        String strUser,strStatus;
        if(StringUtils.equals(strRole,"1")){
            strUser="司机： ";
            viewHolder.tv_status.setText(StringUtils.equals(strTag,"9")?"未结束":"待完善");
        }else {
            strUser="乘客： ";
            viewHolder.tv_status.setText(StringUtils.equals(strTag,"9")?"未结束":"待完善");
        }
       // viewHolder.tv_type.setText(strType);
        viewHolder.tv_type.setText(strUser+strOtherName);
        viewHolder.ll_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(StringUtils.equals("1",strRole)){
                  if(StringUtils.equals(strTag,"1")){
                      mContext.startActivity(new Intent(mContext, OrderUserCompeleteActivity.class).putExtra("orderId",orderId));
                  }else {
                      ToastUtils.showShortToast(mContext,"司机尚未结束订单，请稍后在完善！");
                  }

                }else if(StringUtils.equals("2",strRole)){

                  if(StringUtils.equals(strTag,"1")){
                      mContext.startActivity(new Intent(mContext, OrderDriverCompeleteActivity.class).putExtra("orderId",orderId));
                  }else {
                      ToastUtils.showShortToast(mContext,"乘客尚未结束订单，请稍后在完善！");
                  }
                }
            }
        });
        return convertView;

    }




    public static class ViewHolder {
        TextView tv_name,tv_time,tv_date,tv_start_position,tv_end_position,tv_type,tv_status;
        LinearLayout ll_menu;
    }
}