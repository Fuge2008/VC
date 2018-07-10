package com.saas.saasuser.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saas.saasuser.R;
import com.saas.saasuser.application.MyApplication;
import com.saas.saasuser.global.NetIntent;
import com.saas.saasuser.global.NetIntentCallBackListener;
import com.saas.saasuser.util.LogUtils;
import com.saas.saasuser.util.SharedPreferencesUtil;
import com.saas.saasuser.util.StringUtils;
import com.saas.saasuser.util.ToastUtils;
import com.saas.saasuser.util.Util;
import com.saas.saasuser.view.CustomProgress;
import com.saas.saasuser.view.expandableview.ExpandCollapseListener;
import com.saas.saasuser.view.expandableview.ExpandableLayout;
import com.saas.saasuser.view.expandableview.Fruit;
import com.saas.saasuser.view.expandableview.FruitCategory;
import com.saas.saasuser.view.expandableview.Section;
import com.squareup.okhttp.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.saas.saasuser.application.MyApplication.mContext;

/**
 * 已知安排详情
 */
public class ConfirmPlanDetailActivity extends BaseActivity implements NetIntentCallBackListener.INetIntentCallBack {
    @BindView(R.id.head_more)
    LinearLayout headMore;
    @BindView(R.id.el_show_info)
    ExpandableLayout elShowInfo;
    @BindView(R.id.btn_submit)
    TextView btnSubmit;
    private JSONObject json;
    String orderId, statue;
    private SharedPreferencesUtil util;
    private boolean isHasUnderOrder = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_plan_detail);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        util = new SharedPreferencesUtil(this);
        Util.setHeadTitleMore(this, "已知安排", true);
        //headMore.setVisibility(View.GONE);
        btnSubmit.setText("开始用车");
        orderId = this.getIntent().getStringExtra("orderId");
        statue = this.getIntent().getStringExtra("statue");
        Log.e("订单id===>", orderId.toString());
        new NetIntent().client_driverOrUserGetConfirmOrder(util.getUserId(), orderId, new NetIntentCallBackListener(this));//  TODO     根据id去拿订单数据显示
        new NetIntent().client_underOrder(util.getUserId(), util.getPlatformRole(), new NetIntentCallBackListener(this));//检查是否有正在执行的订单
        if (dialog == null) {
            dialog = CustomProgress.show(this, "加载中..", true, null);
        }


    }

    private void initViews() throws JSONException {
        elShowInfo.setRenderer(new ExpandableLayout.Renderer<FruitCategory, Fruit>() {
            @Override
            public void renderParent(View view, FruitCategory model, boolean isExpanded, int parentPosition) {
                ((TextView) view.findViewById(R.id.tv_parent)).setText(model.name);
                view.findViewById(R.id.iv_arrow).setBackgroundResource(isExpanded ? R.drawable.icon_expend_up : R.drawable.icon_expend_down);
            }

            @Override
            public void renderChild(View view, final Fruit model, int parentPosition, int childPosition) {
                ((TextView) view.findViewById(R.id.tv_name)).setText(model.name);
                ((TextView) view.findViewById(R.id.tv_data)).setText(model.data);


            }
        });

        elShowInfo.addSection(getSection());
        elShowInfo.addSection(getSection1());
        elShowInfo.addSection(getSection2());


        elShowInfo.setExpandListener(new ExpandCollapseListener.ExpandListener<FruitCategory>() {
            @Override
            public void onExpanded(int parentIndex, FruitCategory parent, View view) {
                view.findViewById(R.id.iv_arrow).setBackgroundResource(R.drawable.icon_expend_up);


            }
        });

        elShowInfo.setCollapseListener(new ExpandCollapseListener.CollapseListener<FruitCategory>() {
            @Override
            public void onCollapsed(int parentIndex, FruitCategory parent, View view) {

                view.findViewById(R.id.iv_arrow).setBackgroundResource(R.drawable.icon_expend_down);

            }
        });
    }


    public Section<FruitCategory, Fruit> getSection() throws JSONException {
        Section<FruitCategory, Fruit> section = new Section<>();
        FruitCategory fruitCategory = new FruitCategory("安排详情");
        Fruit fruit1 = new Fruit("安排类型:", "内部安排");
        Fruit fruit2 = new Fruit("安排司机:", StringUtils.repalceEmptyString(json.getString("BODriverName").toString()));
        Fruit fruit3 = new Fruit("司机电话:", StringUtils.repalceEmptyString(json.getString("DriverPhone").toString()));
        Fruit fruit4 = new Fruit("安排车辆:", StringUtils.repalceEmptyString(json.getString("BOVehicleNum").toString()));
        Fruit fruit5 = new Fruit("车型名称:", StringUtils.repalceEmptyString(json.getString("CXName").toString()));
        Fruit fruit6 = new Fruit("车身颜色:", StringUtils.repalceEmptyString(json.getString("VehicleColor").toString()));
        Fruit fruit7 = new Fruit("联络人:", StringUtils.repalceEmptyString(json.getString("BONeederName").toString()));
        Fruit fruit8 = new Fruit("联络电话:", StringUtils.repalceEmptyString(json.getString("BONeederPhone").toString()));


        section.parent = fruitCategory;
        section.children.add(fruit1);
        section.children.add(fruit2);
        section.children.add(fruit3);
        section.children.add(fruit4);
        section.children.add(fruit5);
        section.children.add(fruit6);
        section.children.add(fruit7);
        section.children.add(fruit8);


        section.expanded = true;
        return section;
    }

    public Section<FruitCategory, Fruit> getSection1() throws JSONException {
        Section<FruitCategory, Fruit> section = new Section<>();
        String strReturn;
        if (StringUtils.equals("1", json.getString("BOGoBack").toString())) {
            strReturn = "单程往";
        } else if (StringUtils.equals("2", json.getString("BOGoBack").toString())) {
            strReturn = "单程返";
        } else if (StringUtils.equals("3", json.getString("BOGoBack").toString())) {
            strReturn = "往返";
        } else {
            strReturn = "留宿";
        }
        FruitCategory fruitCategory = new FruitCategory("行程详情");
        Fruit fruit1 = null;

        fruit1 = new Fruit("日期：", StringUtils.repalceEmptyString(StringUtils.tripData(json.getString("BOUseDate").toString())));
        Fruit fruit2 = new Fruit("开始时间：", StringUtils.repalceEmptyString(json.getString("BOUStartTime").toString()));
        Fruit fruit3 = new Fruit("结束时间：", StringUtils.repalceEmptyString(json.getString("BOUEndTime").toString()));
        Fruit fruit4 = new Fruit("上车地点：", StringUtils.repalceEmptyString(json.getString("BOUpCarPlace").toString()));
        Fruit fruit5 = new Fruit("行程目的地：", StringUtils.repalceEmptyString(json.getString("BODestination").toString()));
        Fruit fruit6 = new Fruit("下车地点：", StringUtils.repalceEmptyString(json.getString("BODownCarPlace").toString()));
        Fruit fruit7 = new Fruit("往返：", strReturn);
        Fruit fruit8 = new Fruit("用途说明：", StringUtils.repalceEmptyString(json.getString("BOPurposeDsc").toString()));

        section.parent = fruitCategory;
        section.children.add(fruit1);
        section.children.add(fruit2);
        section.children.add(fruit3);
        section.children.add(fruit4);
        section.children.add(fruit5);
        section.children.add(fruit6);
        section.children.add(fruit7);
        section.children.add(fruit8);
        section.expanded = false;

        return section;
    }

    public Section<FruitCategory, Fruit> getSection2() throws JSONException {
        Section<FruitCategory, Fruit> section = new Section<>();
        String strCarModel, strCarLevel;
        if (StringUtils.equals("1", json.getString("BOAssignCarModel").toString())) {
            strCarModel = "小轿车(5座以下)";
        } else if (StringUtils.equals("2", json.getString("BOAssignCarModel").toString())) {
            strCarModel = "商务车(6-8座)";
        } else if (StringUtils.equals("3", json.getString("BOAssignCarModel").toString())) {
            strCarModel = "商旅车(9-15座)";
        } else if (StringUtils.equals("4", json.getString("BOAssignCarModel").toString())) {
            strCarModel = "中巴车(16-25座)";
        } else if (StringUtils.equals("5", json.getString("BOAssignCarModel").toString())) {
            strCarModel = "小型巴士(26-37座)";
        } else if (StringUtils.equals("6", json.getString("BOAssignCarModel").toString())) {
            strCarModel = "大型巴士(38-55座)";
        } else {
            strCarModel = "其他车";
        }

        if (StringUtils.equals("1", json.getString("BOAssignCarLevel").toString())) {
            strCarLevel = "经济车型";
        } else if (StringUtils.equals("2", json.getString("BOAssignCarLevel").toString())) {
            strCarLevel = "舒适车型";
        } else if (StringUtils.equals("3", json.getString("BOAssignCarLevel").toString())) {
            strCarLevel = "商务车型";
        } else if (StringUtils.equals("4", json.getString("BOAssignCarLevel").toString())) {
            strCarLevel = "豪华车型";
        } else {
            strCarLevel = "巴士车型";
        }
        FruitCategory fruitCategory = new FruitCategory("行程备注");
        Fruit fruit1 = new Fruit("所需车型：", strCarModel);
        Fruit fruit2 = new Fruit("同行人：", StringUtils.repalceEmptyString(json.getString("BOAssociate").toString()));
        Fruit fruit3 = new Fruit("司机要求：", StringUtils.repalceEmptyString(json.getString("BODriverRequire").toString()));
        Fruit fruit4 = new Fruit("车辆要求：", StringUtils.repalceEmptyString(json.getString("BOVehicleRequire").toString()));
        Fruit fruit5 = new Fruit("其他要求", StringUtils.repalceEmptyString(json.getString("OtherRequire").toString()));
        Fruit fruit6 = new Fruit("异地留宿：", StringUtils.equals("0", json.getString("BOIsSleepover").toString()) ? "不留宿" : "留宿");
        Fruit fruit7 = new Fruit("跨境车辆：", StringUtils.equals("0", json.getString("BOIsTransnational").toString()) ? "不跨境" : "跨境");
        Fruit fruit8 = new Fruit("指定车属：", StringUtils.equals("0", json.getString("BOIsSelfCar").toString()) ? "不指定" : "指定");
        Fruit fruit9 = new Fruit("指定车辆等级：", strCarLevel);

        section.parent = fruitCategory;
        section.children.add(fruit1);
        section.children.add(fruit2);
        section.children.add(fruit3);
        section.children.add(fruit4);
        section.children.add(fruit5);
        section.children.add(fruit6);
        section.children.add(fruit7);
        section.children.add(fruit8);
        section.children.add(fruit9);
        section.expanded = false;
        return section;
    }

    @Override
    public void onError(Request request, Exception e) {
        if (dialog != null) {
            dialog.dismiss();
        }

    }

    @Override
    public void onResponse(String response) {
        if (dialog != null) {
            dialog.dismiss();
        }
        try {
            JSONObject jsonObject = new JSONObject(response);
            ToastUtils.showShortToastSafe(ConfirmPlanDetailActivity.this, jsonObject.getString("ErrMsg"));
            LogUtils.e("已知安排：" + jsonObject.toString());
            if (StringUtils.equals(jsonObject.getString("ErrCode"), "1")) {

                if (jsonObject.has("Data")) {
                    json = jsonObject.getJSONObject("Data");
                    if (json != null && json.length() > 0) {
                        handler.sendEmptyMessage(1);
                    }

                } else if (jsonObject.has("Data_one")) {
                    JSONArray users_temp = jsonObject.getJSONArray("Data_one");
                    if (users_temp != null && users_temp.length() > 0) {//TODO  是否有数据
                        isHasUnderOrder = true;
                    }
                } else {
                    finish();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        initViews();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    break;
            }
        }

        ;
    };


    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        if (isHasUnderOrder) {
            ToastUtils.showLongToastSafe(ConfirmPlanDetailActivity.this, "您有订单尚未结束，请先结束！");
        } else {
            if (StringUtils.equals("0", statue)) {//对方尚未确认订单
                ToastUtils.showShortToast(mContext, "司机尚未确认订单，请稍后！");
            } else {//对方已经确认
                startActivity(new Intent(ConfirmPlanDetailActivity.this, UserTaskExcuteStatusActivity.class).putExtra("orderId", orderId).putExtra("orderStatue", "0"));
            }

            finish();
        }
    }
}
