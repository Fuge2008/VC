//package com.saas.saasuser.fragment;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.saas.saasuser.R;
//
//import com.saas.saasuser.activity.CorporateInvoiceActivity;
//import com.saas.saasuser.activity.EnterprisesCardActivity;
//import com.saas.saasuser.activity.NewOrdersActivity2;
//import com.saas.saasuser.activity.OrderMakeByPictureActivity;
//import com.saas.saasuser.activity.SetActivity;
//import com.saas.saasuser.activity.VVNewsActivity;
//import com.saas.saasuser.activity.VVThreeJiuActivity;
//import com.saas.saasuser.easemob.chat.IMMainActivity;
//import com.saas.saasuser.util.SharedPreferencesUtil;
//import com.saas.saasuser.view.CircleMenuLayout;
//
//
//public class FirstFragment extends BaseFragment implements OnClickListener {
//	private CircleMenuLayout mCircleMenuLayout;
//	private TextView tvtIitle, tvContent;
//
//
//	private int[] mItemImgs = new int[]{
//			R.drawable.icon_vehicle_handover,
//			R.drawable.icon_personal_settings,
//			R.drawable.icon_more_functions,
//			R.drawable.icon_corporate_invoice,
//			R.drawable.icon_enterprisebusinesscard,
//			R.drawable.icon_news,
//			R.drawable.icon_neworders,
//			R.drawable.icon_vvdynamic,
//
//	};
//
//
//	private SharedPreferencesUtil util;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//
//	}
//
//	@Override
//	public View initView() {
//
//		View view = View.inflate(mActivity, R.layout.fragment_first, null);
//		tvContent = (TextView) view.findViewById(R.id.tv_content);
//		tvtIitle = (TextView) view.findViewById(R.id.tv_title);
//		TextView tvTitle=(TextView)view.findViewById(R.id.head_title);
//		tvTitle.setText("功能");
//		util=new SharedPreferencesUtil(mActivity);
//		view.findViewById(R.id.head_more).setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				startActivity(new Intent(mActivity,IMMainActivity.class));
//			}
//		});
//		final String[] mItemContent = getResources().getStringArray(R.array.rent_enter_content);
//		final String[] mItemTitle = getResources().getStringArray(R.array.rent_enter_title);
//		mCircleMenuLayout = (CircleMenuLayout) view.findViewById(R.id.id_menulayout);
//		mCircleMenuLayout.setMenuItemIconsAndTexts(mItemImgs, mItemTitle);
//		mCircleMenuLayout.Rotating();
//
//		mCircleMenuLayout.setOnMenuItemClickListener(new CircleMenuLayout.OnMenuItemClickListener() {
//			@Override
//			public void itemClick(View view, int pos) {
//				Toast.makeText(mActivity, mItemTitle[pos], Toast.LENGTH_SHORT).show();
//				tvtIitle.setText(mItemTitle[pos]);
//				tvContent.setText(mItemContent[pos]);
//				if(mItemTitle[pos].equals("更多功能")){
//					startActivity(new Intent(mActivity, OrderMakeByPictureActivity.class));
//				}
//				if (mItemTitle[pos].equals("个人设置")) {
//					startActivity(new Intent(mActivity, SetActivity.class));
//				}
//				if(mItemTitle[pos].equals("企业名片")){
//					startActivity(new Intent(mActivity, EnterprisesCardActivity.class));
//
//				}
//				if (mItemTitle[pos].equals("VV新闻")) {
//					startActivity(new Intent(mActivity,VVNewsActivity.class));
//
//				}
//				if (mItemTitle[pos].equals("新增订单")) {
//					startActivity(new Intent(mActivity, NewOrdersActivity2.class));
//
//				}
//				if (mItemTitle[pos].equals("企业发票")) {
//					startActivity(new Intent(mActivity,CorporateInvoiceActivity.class));
//
//				}
//
//			}
//
//
//			@Override
//			public void itemCenterClick(View view) {
//                Intent intent = new Intent(mActivity, VVThreeJiuActivity.class);
//                startActivity(intent);
//            }
//		});
//
//
//		return view;
//	}
//
//	@Override
//	public void onClick(View view) {
//		switch (view.getId()) {
//
//		}
//	}
//
//
//}
