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

import com.saas.saasuser.R;
import com.saas.saasuser.activity.OrderRecordDetailActivity;
import com.saas.saasuser.util.StringUtils;
import com.saas.saasuser.view.CircleImageView;
import com.saas.saasuser.view.stickylistview.NewRecordItems;
import com.saas.saasuser.view.stickylistview.OrderRecord;
import com.saas.saasuser.view.stickylistview.PullStickyListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class OrderRecordListAdapter  extends BaseAdapter implements PullStickyListView.PinnedSectionListAdapter {

    private Context mContext;
    private List<OrderRecord> orderData;
    private LayoutInflater inflater;

    //本地分组后的账单
    private TreeMap<String, ArrayList<OrderRecord>> groupBills;
    //Adapter的数据源
    private List<NewRecordItems> newRecordItemses;
    private SimpleDateFormat sdf;


    public OrderRecordListAdapter(Activity context, List<OrderRecord> orderRecord) {
        this. mContext = context;
        this.orderData = orderRecord;
        inflater = LayoutInflater.from(context);

        newRecordItemses=new ArrayList<NewRecordItems>();
        groupBills = new TreeMap<String, ArrayList<OrderRecord>>();
       sdf = new SimpleDateFormat("yyyy-MM", Locale.getDefault());

    }

    /**
     * 初始化列表项
     */
    public void inflaterItems() {
        newRecordItemses.clear();
        groupBills.clear();
        for (OrderRecord records : orderData) {//遍历bills将集合中的所有数据以月份进行分类
            String groupName = StringUtils.tripYearAndMonth(records.getStarttime());
            if (groupBills.containsKey(groupName)) {//如果Map已经存在以该记录的日期为分组名的分组，则将该条记录插入到该分组中
                groupBills.get(groupName).add(records);
            } else {//如果不存在，以该记录的日期作为分组名称创建分组，并将该记录插入到创建的分组中
                ArrayList<OrderRecord> tempOrderRecords = new ArrayList<OrderRecord>();
                tempOrderRecords.add(records);
                groupBills.put(groupName, tempOrderRecords);
            }
        }

        Iterator<Map.Entry<String, ArrayList<OrderRecord>>> iterator = groupBills.descendingMap().entrySet().iterator();
        while (iterator.hasNext()) {//将分组后的数据添加到数据源的集合中
            Map.Entry<String, ArrayList<OrderRecord>> entry = iterator.next();
            newRecordItemses.add(new NewRecordItems(NewRecordItems.SECTION, entry.getKey()));//将分组添加到集合中
            for (OrderRecord orderRecord : entry.getValue()) {//将组中的数据添加到集合中
                newRecordItemses.add(new NewRecordItems(NewRecordItems.ITEM, orderRecord));
            }
        }
    }

    @Override
    public int getCount() {
        return newRecordItemses.size();
    }

    @Override
    public OrderRecord getItem(int position) {
        return newRecordItemses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView,
                        ViewGroup parent) {
            NewRecordItems newRecordItems = (NewRecordItems) getItem(position);
            if (newRecordItems.type == NewRecordItems.SECTION) {
                convertView =  inflater.inflate(R.layout.item_earn_detail_title, parent, false);
                TextView item_month = (TextView) convertView.findViewById(R.id.item_month);

               item_month.setText(newRecordItems.groupName.replace("/","年")+"月");
            } else {
                convertView = inflater.inflate(R.layout.item_order_record, parent, false);
                TextView tv_name = (TextView)  convertView.findViewById(R.id.tv_name);
                TextView tv_time= (TextView)  convertView.findViewById(R.id.tv_time);
                TextView tv_start_position = (TextView)  convertView.findViewById(R.id.tv_start);
                TextView tv_end_position = (TextView)  convertView.findViewById(R.id.tv_end);
                CircleImageView iv_head  = (CircleImageView) convertView.findViewById(R.id.iv_head);
                LinearLayout rl_main_menu = (LinearLayout) convertView.findViewById(R.id.ll_main_item);
                final String orderId=  newRecordItems.getId();//	最终订单信息表ID
                final String strDate=  newRecordItems.getStarttime();//usetime	出行日期

                //endtime	结束时间

                final String strStartAddress=  newRecordItems.getStartsite();//startsite	上车地点
                final String strEndAddress=  newRecordItems.getEndsite();//endsite	下车地点（可为空）
                final String strDriverName=newRecordItems.getDname();//	司机姓名
                final String strUserName=newRecordItems.getCname();//	乘客姓名
                String strDriverPhone=newRecordItems.getDphone();//	司机联系方式
                String strUserPhone=newRecordItems.getCphone();//	乘客联系方式
                //final String strKilometer=orders;//	里程（公里）
                //final String strSpendMoney=orders;//	金额
                //final String strSpendTime=orders;// 	耗时（分钟）
                final String strWeek=newRecordItems.getUseweek();//	星期
                String strOrderStatue=newRecordItems.getOstate();//	订单状态：未完善  已提交 已完善 已计价

                final String strRole= newRecordItems.getOrdertype(); //	订单类型  1用车人  2司机
                if(StringUtils.equals("1",strRole)){
                    iv_head.setImageResource(R.drawable.icon_order_passage);
                    tv_name.setText(StringUtils.repalceEmptyString(strDriverName)+"("+strOrderStatue+")");
                }else if(StringUtils.equals("2",strRole)){
                    iv_head.setImageResource(R.drawable.icon_order_driver);
                    tv_name.setText(StringUtils.repalceEmptyString(strUserName)+"("+strOrderStatue+")");
                }
                tv_time.setText(StringUtils.repalceEmptyString(strDate));
                tv_start_position.setText(StringUtils.repalceEmptyString(strStartAddress));
                tv_end_position.setText(StringUtils.repalceEmptyString(strEndAddress));
                rl_main_menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContext.startActivity(new Intent(mContext, OrderRecordDetailActivity.class)
                                        .putExtra("strStartAddress",strStartAddress)
                                        .putExtra("strEndAddress",strEndAddress)
                                        .putExtra("orderId",orderId)
                                        .putExtra("strDate",strDate)
                                        // .putExtra("strTime",strTime)
                                        .putExtra("strDriverName",strDriverName)
                                        .putExtra("strUserName",strUserName)
                                        //.putExtra("strKilometer",strKilometer)
                                        //.putExtra("strSpendMoney", strSpendMoney)
                                        .putExtra("strWeek",strWeek)
                                        .putExtra("strRole",strRole)
                                        // .putExtra("strSpendTime",strSpendTime)
                        );
                    }
                });

            }
        return convertView;

    }

    @Override
    public int getItemViewType(int position) {
        return newRecordItemses.get(position).type;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == NewRecordItems.SECTION;
    }

    @Override
    public void notifyDataSetChanged() {
        //重新初始化数据
        inflaterItems();
        super.notifyDataSetChanged();
    }

}
