package com.saas.saasuser.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.saas.saasuser.R;
import com.saas.saasuser.activity.WebViewActivity;
import com.saas.saasuser.util.StringUtils;

import java.util.List;



public class VVNewsAdapter extends BaseAdapter {
    private Context mContext;
    private List<JSONObject> orderData;
    private LayoutInflater inflater;





    public   VVNewsAdapter (Activity context, List<JSONObject> jsonArray) {
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

        final JSONObject json =  orderData.get(position);//.TODO
        String strPic1=  json.getString("thumbnail_pic_s");
        String strTitle=  json.getString("title");
        String strDate=  json.getString("date");
        String strAuthorName=  json.getString("author_name");
        final String strURL=  json.getString("url");
        int picNum=0;
        if(json.containsKey("thumbnail_pic_s") && json.containsKey("thumbnail_pic_s02") && json.containsKey("thumbnail_pic_s03")){
            picNum=3;
        }else if(json.containsKey("thumbnail_pic_s") && json.containsKey("thumbnail_pic_s02")){
            picNum=2;
        }else if(json.containsKey("thumbnail_pic_s")){
            picNum=1;
        }

        if(picNum==3){
            String strPic2=  json.getString("thumbnail_pic_s02");
            String strPic3=  json.getString("thumbnail_pic_s03");
            convertView = inflater.inflate(R.layout.item_three_pics_news, parent, false);
            ViewHolder holder = (ViewHolder) convertView.getTag();
            if (holder == null) {
                holder = new ViewHolder();
                holder.title = (TextView)convertView.findViewById(R.id.tv_title);
                holder.date = (TextView)convertView.findViewById(R.id.tv_time);
                holder.author = (TextView)convertView.findViewById(R.id.tv_author);

                holder.pic1 = (ImageView) convertView.findViewById(R.id.iv_img1);
                holder.pic2 = (ImageView) convertView.findViewById(R.id.iv_img2);
                holder.pic3 = (ImageView) convertView.findViewById(R.id.iv_img3);
                holder.title.setText(strTitle);
                holder.date.setText(strAuthorName);
                holder.author.setText(strDate);

                Glide.with(mContext).load(strPic1).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.pic1);
                Glide.with(mContext).load(strPic2).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.pic2);
                Glide.with(mContext).load(strPic3).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.pic3);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myIntent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString(StringUtils.TITLE, "V V新闻");
                        bundle.putString(StringUtils.URL, strURL);
                        myIntent.putExtras(bundle);
                        myIntent.setClass(mContext, WebViewActivity.class);
                        mContext.startActivity(myIntent);
                    }
                });
                convertView.setTag(holder);
            }

        }else if(picNum==2){

            View  view =  inflater.inflate(R.layout.item_pic_video_news, parent, false);
            //Glide.with(context).load(myAvatar).diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_avatar);
            TextView title = (TextView)view.findViewById(R.id.tv_title);
            TextView date = (TextView)view.findViewById(R.id.tv_time);
            TextView author = (TextView)view.findViewById(R.id.tv_author);

            ImageView pic = (ImageView) view.findViewById(R.id.iv_img);

            title.setText(strTitle);
            date.setText(strAuthorName);
            author.setText(strDate);
            Glide.with(mContext).load(strPic1).diskCacheStrategy(DiskCacheStrategy.ALL).into(pic);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString(StringUtils.TITLE, "V V新闻");
                    bundle.putString(StringUtils.URL, strURL);
                    myIntent.putExtras(bundle);
                    myIntent.setClass(mContext, WebViewActivity.class);
                    mContext.startActivity(myIntent);
                }
            });
            return view;

        }else if(picNum==1){
            View  view =  inflater.inflate(R.layout.item_center_pic_news, parent, false);
            TextView title = (TextView)view.findViewById(R.id.tv_title);
            TextView date = (TextView)view.findViewById(R.id.tv_time);
            TextView author = (TextView)view.findViewById(R.id.tv_author);

            ImageView pic = (ImageView) view.findViewById(R.id.iv_img);

            title.setText(strTitle);
            date.setText(strAuthorName);
            author.setText(strDate);
            Glide.with(mContext).load(strPic1).diskCacheStrategy(DiskCacheStrategy.ALL).into(pic);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString(StringUtils.TITLE, "V V新闻");
                    bundle.putString(StringUtils.URL, strURL);
                    myIntent.putExtras(bundle);
                    myIntent.setClass(mContext, WebViewActivity.class);
                    mContext.startActivity(myIntent);
                }
            });

            return view;
        }else {
            View  view =  inflater.inflate(R.layout.item_text_news, parent, false);
            TextView title = (TextView)view.findViewById(R.id.tv_title);
            TextView date = (TextView)view.findViewById(R.id.tv_time);
            TextView author = (TextView)view.findViewById(R.id.tv_author);

            title.setText(strTitle);
            date.setText(strAuthorName);
            author.setText(strDate);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString(StringUtils.TITLE, "V V新闻");
                    bundle.putString(StringUtils.URL, strURL);
                    myIntent.putExtras(bundle);
                    myIntent.setClass(mContext, WebViewActivity.class);
                    mContext.startActivity(myIntent);
                }
            });
            return view;
        }

        return convertView;

    }
    public static class ViewHolder {
        ImageView pic1,pic2,pic3;
        TextView title,author,date;
    }




}
