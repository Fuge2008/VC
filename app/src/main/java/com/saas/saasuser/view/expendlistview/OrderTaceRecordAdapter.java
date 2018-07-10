//package com.saas.saasuser.view.expendlistview;
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.saas.saasuser.R;
//import com.saas.saasuser.activity.FeedBackActivity;
//import com.saas.saasuser.activity.OrderDriverCompeleteActivity;
//import com.saas.saasuser.activity.OrderUserCompeleteActivity;
//import com.saas.saasuser.activity.map.RecordActivity;
//import com.saas.saasuser.global.Constants;
//import com.saas.saasuser.util.StringUtils;
//import com.saas.saasuser.util.ToastUtils;
//import com.saas.saasuser.view.CircleImageView;
//
//import java.util.HashSet;
//import java.util.List;
//
//import static com.saas.saasuser.R.id.tv_driver_name;
//import static com.saas.saasuser.R.id.tv_end_time;
//import static com.saas.saasuser.R.id.tv_start_time;
//
///**
// * Simple example of ListAdapter for using with Folding Cell
// * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
// */
//public class OrderTaceRecordAdapter extends ArrayAdapter<Item> {
//
//    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
//    private View.OnClickListener defaultRequestBtnClickListener;
//    private Context mContext;
//
//    private AlertDialog inputDialog;
//    private View dialogView;
//    private TextView input;
//    private Button sureBtn;
//    private Button cancleBtn;
//
//
//    public OrderTaceRecordAdapter(Context context, List<Item> objects) {
//        super(context, 0, objects);
//        this.mContext=context;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        // get item for selected view
//        final Item item = getItem(position);
//        // if cell is exists - reuse it, if not - create the new one from resource
//        FoldingCell cell = (FoldingCell) convertView;
//        ViewHolder viewHolder;
//        if (cell == null) {
//            viewHolder = new ViewHolder();
//            LayoutInflater vi = LayoutInflater.from(getContext());
//            cell = (FoldingCell) vi.inflate(R.layout.cell, parent, false);
//            // 收起页面
//            viewHolder.tv_name = (TextView) cell.findViewById(R.id.tv_name);
//            viewHolder.tv_time = (TextView) cell.findViewById(R.id.tv_time);
//            viewHolder.tv_date = (TextView) cell.findViewById(R.id.tv_date);
//            //viewHolder.fromAddress = (TextView) cell.findViewById(R.id.title_from_address);
//           // viewHolder.toAddress = (TextView) cell.findViewById(R.id.title_to_address);
//            viewHolder.tv_start_position = (TextView) cell.findViewById(R.id.tv_start_position);
//            viewHolder.tv_end_position = (TextView) cell.findViewById(R.id.tv_end_position);
//            viewHolder.ivDeleteData = (ImageView) cell.findViewById(R.id.iv_delete_data);
//
//            //展开页面
//
//            viewHolder.tvTrackPlayback = (TextView) cell.findViewById(R.id.tv_track_playback);
//            viewHolder.contentRequestBtn = (TextView) cell.findViewById(R.id.content_request_btn);
//            viewHolder.ivHeadMap = (ImageView) cell.findViewById(R.id.iv_head_map);
//            viewHolder.ivDriverAvatar= (CircleImageView) cell.findViewById(R.id.iv_driver_avatar);
//            viewHolder.tv_spend_time = (TextView) cell.findViewById(R.id.tv_spend_time);
//            viewHolder.tv_diver_kilomite = (TextView) cell.findViewById(R.id.tv_diver_kilomite);
//            viewHolder.tv_spend_money= (TextView) cell.findViewById(R.id.tv_spend_money);
//            viewHolder.tv_start_address2 = (TextView) cell.findViewById(R.id.tv_start_address2);
//            //viewHolder. tv_start_address3 = (TextView) cell.findViewById(R.id.tv_start_address3);
//            viewHolder. tv_end_address2 = (TextView) cell.findViewById(R.id.tv_end_address2);
//            //viewHolder.tv_end_address3 = (TextView) cell.findViewById(R.id.tv_end_address3);
//            viewHolder.tv_driver_name = (TextView) cell.findViewById(tv_driver_name);
//            viewHolder.tv_start_time = (TextView) cell.findViewById(tv_start_time);
//            viewHolder.tv_start_week = (TextView) cell.findViewById(R.id.tv_start_week);
//            viewHolder.tv_end_time = (TextView) cell.findViewById(tv_end_time);
//            viewHolder.tv_end_week = (TextView) cell.findViewById(R.id.tv_end_week);
//            viewHolder.tv_doubt = (TextView) cell.findViewById(R.id.tv_doubt);
//
//
//            cell.setTag(viewHolder);
//        } else {
//            // for existing cell set valid valid state(without animation)
//            if (unfoldedIndexes.contains(position)) {
//                cell.unfold(true);
//            } else {
//                cell.fold(true);
//            }
//            viewHolder = (ViewHolder) cell.getTag();
//        }
//
//        // 收起页面
//
//        viewHolder.tv_time.setText(item.getTime());
//        viewHolder.tv_date.setText("时间： "+StringUtils.tripData(item.getDate()));
//        viewHolder.tv_start_position.setText("出发地： "+StringUtils.repalceEmptyString(item.getFromAddress()));
//        viewHolder.tv_end_position.setText("目的地： "+StringUtils.repalceEmptyString(item.getToAddress()));
//        String strType;
//      if(StringUtils.equals(item.getIsRole(),"1")){
//          strType="乘车";
//      }else {
//          strType="载客";
//        }
//        viewHolder.tv_name.setText(strType);
//
//
//        //展开页面
//        viewHolder.tv_spend_time.setText("未知");
//        viewHolder.tv_diver_kilomite.setText("未知");
//        viewHolder.tv_spend_money.setText("未知");
//        viewHolder.tv_start_address2.setText(item.getFromAddress());
//       // viewHolder. tv_start_address3 .setText("未知");
//        viewHolder. tv_end_address2.setText(item.getToAddress());
//        //viewHolder.tv_end_address3 .setText("未知");
//        viewHolder.tv_driver_name .setText(item.getname());
//        viewHolder.tv_start_time.setText(StringUtils.tripData(item.getDate()).replace("-","/")+"  "+item.getTime());
//        //viewHolder.tv_start_week .setText("星期三");
//        viewHolder.tv_end_time.setText(StringUtils.tripData(item.getDate())+"  "+item.getTime());
//        //viewHolder.tv_end_week .setText("星期三");
//        Glide.with(mContext).load(Constants.ImageHttpRoot+item.getimage()).diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.my_headphoto).into(viewHolder.ivDriverAvatar);
//        viewHolder.tv_doubt = (TextView) cell.findViewById(R.id.tv_doubt);
//        viewHolder.tvTrackPlayback.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mContext.startActivity(new Intent(mContext, RecordActivity.class));
//            }
//        });
//        viewHolder.ivDeleteData.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showDialog();
//            }
//        });
//        viewHolder.tv_doubt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mContext.startActivity(new Intent(mContext, FeedBackActivity.class));
//            }
//        });
//        viewHolder.contentRequestBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(StringUtils.equals("0",item.getIsRole())){
//                    mContext.startActivity(new Intent(mContext, OrderUserCompeleteActivity.class).putExtra("orderId",item.getOrderId()));
//                }else if(StringUtils.equals("1",item.getIsRole())){
//                    mContext.startActivity(new Intent(mContext, OrderUserCompeleteActivity.class).putExtra("orderId",item.getOrderId()));
//                }else if(StringUtils.equals("2",item.getIsRole())){
//                    mContext.startActivity(new Intent(mContext, OrderDriverCompeleteActivity.class).putExtra("orderId",item.getOrderId()));
//                }
//
//            }
//        });
//
//        // set custom btn handler for list item from that item
////        if (item.getRequestBtnClickListener() != null) {
////            viewHolder.contentRequestBtn.setOnClickListener(item.getRequestBtnClickListener());
////        } else {
////            viewHolder.contentRequestBtn.setOnClickListener(defaultRequestBtnClickListener);
////        }
//
//
//        return cell;
//    }
//
//    // simple methods for register cell state changes
//    public void registerToggle(int position) {
//        if (unfoldedIndexes.contains(position))
//            registerFold(position);
//        else
//            registerUnfold(position);
//    }
//
//    public void registerFold(int position) {
//        unfoldedIndexes.remove(position);
//    }
//
//    public void registerUnfold(int position) {
//        unfoldedIndexes.add(position);
//    }
//
//    public View.OnClickListener getDefaultRequestBtnClickListener() {
//        return defaultRequestBtnClickListener;
//    }
//
//    public void setDefaultRequestBtnClickListener(View.OnClickListener defaultRequestBtnClickListener) {
//        this.defaultRequestBtnClickListener = defaultRequestBtnClickListener;
//    }
//
//    // View lookup cache
//    private static class ViewHolder {
//        TextView tv_name,tv_time,tv_date,tv_start_position,tv_end_position;
//         ImageView ivDeleteData,ivHeadMap;
//        CircleImageView ivDriverAvatar;
//        TextView tv_spend_time,tv_diver_kilomite,tv_start_address2,tv_spend_money,tv_end_address2,tv_driver_name,tv_start_time,tv_start_week,tv_end_time,tv_end_week,tv_doubt,tvTrackPlayback,contentRequestBtn;
//
//    }
//
//    public void showDialog() {
//        if (inputDialog == null) {
//            dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_confirm, null);
//            input = (TextView) dialogView.findViewById(R.id.input);
//            sureBtn = (Button) dialogView.findViewById(R.id.sureBtn);
//            cancleBtn = (Button) dialogView.findViewById(R.id.cancleBtn);
//            input.setText("      一旦删除该条行程记录，将不能重新恢复，是否确认删除该记录？");
//            sureBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                        inputDialog.dismiss();
//                    ToastUtils.showShortToastSafe(mContext,"抱歉，暂时无法删除！");
//
//                }
//            });
//            cancleBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    inputDialog.dismiss();
//                }
//            });
//
//            inputDialog = new AlertDialog.Builder(mContext)
//                    .setView(dialogView)
//                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
//                        @Override
//                        public void onCancel(DialogInterface dialog) {
//
//                        }}).setCancelable(false).create();
//            inputDialog.show();
//        } else {
//            inputDialog.show();
//        }
//    }
//}
