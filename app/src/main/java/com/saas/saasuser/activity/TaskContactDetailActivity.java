package com.saas.saasuser.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.saas.saasuser.R;
import com.saas.saasuser.application.MyApplication;
import com.saas.saasuser.global.NetIntent;
import com.saas.saasuser.global.NetIntentCallBackListener;
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

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 任务联络详情
 */
public class TaskContactDetailActivity extends BaseActivity implements NetIntentCallBackListener.INetIntentCallBack{
    @BindView(R.id.head_more)
    LinearLayout headMore;
    @BindView(R.id.el_show_info)
    ExpandableLayout elShowInfo;
    @BindView(R.id.btn_submit)
    TextView btnSubmit;
    @BindView(R.id.btn_refuse)
    TextView btnRefuse;
    private JSONObject json;
    String orderId;
    private SharedPreferencesUtil util;
    private AlertDialog inputDialog;
    private View dialogView;
    private EditText input;
    private TextView sureBtn;
    private TextView cancleBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_contact_detail);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        util=new SharedPreferencesUtil(this);
        Util.setHeadTitleMore(this, "任务联络", true);
       // headMore.setVisibility(View.GONE);

        orderId = this.getIntent().getStringExtra("orderId");
        new NetIntent().client_getOrderDetailById(orderId,util.getUserId(),"2", new NetIntentCallBackListener(this));//  TODO    根据id去拿订单数据显示
        if(dialog==null){
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
        elShowInfo.addSection(getSection3());


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
        FruitCategory fruitCategory = new FruitCategory("订单详情");
        Fruit fruit1 = new Fruit("驾驶车辆:", StringUtils.repalceEmptyString(json.getString("BOVehicleNum").toString()));
        Fruit fruit2 = new Fruit("任务日期:", StringUtils.repalceEmptyString(StringUtils.tripData(json.getString("BOUseDate").toString())));
        Fruit fruit3 = new Fruit("单位简称:", StringUtils.repalceEmptyString(json.getString("BOBusinessName").toString()));
        Fruit fruit4 = new Fruit("用车人姓名:", StringUtils.repalceEmptyString(json.getString("BONeederName").toString()));
        Fruit fruit5 = new Fruit("用车人电话:", StringUtils.repalceEmptyString(json.getString("BONeederPhone").toString()));


        section.parent = fruitCategory;
        section.children.add(fruit1);
        section.children.add(fruit2);
        section.children.add(fruit3);
        section.children.add(fruit4);
        section.children.add(fruit5);
        section.expanded = true;
        return section;
    }

    public Section<FruitCategory, Fruit> getSection1() throws JSONException {
        Section<FruitCategory, Fruit> section = new Section<>();
        String strReturn;
        if(StringUtils.equals("1",json.getString("BOGoBack").toString())){
            strReturn="单程往";
        }else if(StringUtils.equals("2",json.getString("BOGoBack").toString())){
            strReturn="单程返";
        }else if(StringUtils.equals("3",json.getString("BOGoBack").toString())){
            strReturn="往返";
        }else {
            strReturn="留宿";
        }
        FruitCategory fruitCategory = new FruitCategory("行程详情");
        Fruit fruit1 = null;

        fruit1 = new Fruit("日期：",  StringUtils.repalceEmptyString(StringUtils.tripData(json.getString("BOUseDate").toString())));
        Fruit fruit2 = new Fruit("开始时间：", StringUtils.repalceEmptyString(json.getString("BOUStartTime").toString()));
        Fruit fruit3 = new Fruit("结束时间：", StringUtils.repalceEmptyString(json.getString("BOUEndTime").toString()));
        Fruit fruit4 = new Fruit("上车地点：", StringUtils.repalceEmptyString(json.getString("BOUpCarPlace").toString()));
        Fruit fruit5 = new Fruit("行程目的地：", StringUtils.repalceEmptyString(json.getString("BODestination").toString()));
        Fruit fruit6 = new Fruit("下车地点：", StringUtils.repalceEmptyString(json.getString("BODownCarPlace").toString()));
        Fruit fruit7 = new Fruit("乘车人数：", StringUtils.repalceEmptyString(json.getString("BOPassengers").toString()));
        Fruit fruit8 = new Fruit("往返：",strReturn);
       // Fruit fruit9 = new Fruit("用途说明：", json.getString("BOPurposeDsc").toString());

        section.parent = fruitCategory;
        section.children.add(fruit1);
        section.children.add(fruit2);
        section.children.add(fruit3);
        section.children.add(fruit4);
        section.children.add(fruit5);
        section.children.add(fruit6);
        section.children.add(fruit7);
        section.children.add(fruit8);
        //section.children.add(fruit9);
        section.expanded = true;

        return section;
    }

    public Section<FruitCategory, Fruit> getSection2() throws JSONException {
        Section<FruitCategory, Fruit> section = new Section<>();
        FruitCategory fruitCategory = new FruitCategory("任务备注");
        Fruit fruit1 = new Fruit("订单备注：",  StringUtils.repalceEmptyString(json.getString("BOOrderMark").toString()));
        Fruit fruit2 = new Fruit("异地留宿：",  StringUtils.equals("0",json.getString("BOIsSleepover").toString())?"不留宿":"留宿");
        Fruit fruit3 = new Fruit("纸质签单：",   StringUtils.equals("0",json.getString("BOSigning").toString())?"不签单":"签单");
        Fruit fruit4 = new Fruit("接机牌：", StringUtils.repalceEmptyString(json.getString("BOPickUpContent").toString()));
        Fruit fruit5 = new Fruit("联络人",  StringUtils.repalceEmptyString(json.getString("BOPlanName").toString()));
        Fruit fruit6 = new Fruit("联络电话：", StringUtils.repalceEmptyString(json.getString("BOPlanPhone").toString()));

        section.parent = fruitCategory;
        section.children.add(fruit1);
        section.children.add(fruit2);
        section.children.add(fruit3);
        section.children.add(fruit4);
        section.children.add(fruit5);
        section.children.add(fruit6);
        section.expanded = true;
        return section;
    }

    public Section<FruitCategory, Fruit> getSection3() throws JSONException {
        Section<FruitCategory, Fruit> section = new Section<>();
        String strCashType;
        if(StringUtils.equals("1",json.getString("BOReceiveType").toString())){
            strCashType="人民币";
        }else if(StringUtils.equals("2",json.getString("BOReceiveType").toString())){
            strCashType="港币";
        }else if(StringUtils.equals("3",json.getString("BOReceiveType").toString())){
            strCashType="美元";
        }else  if(StringUtils.equals("4",json.getString("BOReceiveType").toString())){
            strCashType="英镑";
        }else  if(StringUtils.equals("5",json.getString("BOReceiveType").toString())){
            strCashType="欧元";
        }else  {
            strCashType="其他";
        }
        FruitCategory fruitCategory = new FruitCategory("费用相关");
        Fruit fruit1 = new Fruit("代收费用：", StringUtils.repalceEmptyString(json.getString("BOOrderReceive").toString()));
        Fruit fruit2 = new Fruit("代收币种：",strCashType);
        Fruit fruit3 = new Fruit("代收说明：", StringUtils.repalceEmptyString(json.getString("BOReceiveTypeExt").toString()));
        Fruit fruit4 = new Fruit("需要发票：",  StringUtils.equals("0",json.getString("ISInvoice").toString())?"不需要":"需要");

        section.parent = fruitCategory;
        section.children.add(fruit1);
        section.children.add(fruit2);
        section.children.add(fruit3);
        section.children.add(fruit4);
        section.expanded = true;
        return section;
    }

    @OnClick({R.id.btn_submit, R.id.btn_refuse})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                new NetIntent().client_driverOrUserConfirmOrder(orderId,util.getUserId(),"2", new NetIntentCallBackListener(this));//TODO    司机接单、乘客出行
                //ToastUtils.showShortToastSafe(TaskContactDetailActivity.this, "此功能还没有接口");
                if(dialog==null){
                    dialog = CustomProgress.show(this, "正在提交..", true, null);
                }
                break;
            case R.id.btn_refuse:

                showDialog();
                break;
        }
    }

    @Override
    public void onError(Request request, Exception e) {
        if(dialog!=null){
            dialog.dismiss();
        }

    }

    @Override
    public void onResponse(String response) {
        if(dialog!=null){
            dialog.dismiss();
        }
        try {
            JSONObject jsonObject = new JSONObject(response);
            ToastUtils.showShortToastSafe(TaskContactDetailActivity.this, jsonObject.getString("ErrMsg"));
            if (StringUtils.equals(jsonObject.getString("ErrCode"), "1")) {
                if(StringUtils.equals("操作成功",jsonObject.getString("ErrMsg"))){//TODO  操作成功结束
                    finish();
                }
                if (jsonObject.has("Data")) {
                    json = jsonObject.getJSONObject("Data");
                    if(json!=null && json.length()>0){
                        handler.sendEmptyMessage(1);
                    }
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
        };
    };


    String msg1;
    public void showDialog() {
        if (inputDialog == null) {
            dialogView = LayoutInflater.from(TaskContactDetailActivity.this).inflate(R.layout.dialog_input, null);
            input = (EditText) dialogView.findViewById(R.id.input);
            sureBtn = (TextView) dialogView.findViewById(R.id.sureBtn);
            cancleBtn = (TextView) dialogView.findViewById(R.id.cancleBtn);
            sureBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    msg1=input.getText().toString();
                    if (StringUtils.isNotEmpty(msg1,true) && StringUtils.getLength(msg1,true)<30) {
                        new NetIntent().client_driverOrUserRefuseOrder(orderId,util.getUserId(),"2", msg1, new NetIntentCallBackListener(TaskContactDetailActivity.this));//TODO    拒绝接单
                        //new NetIntent().client_updateMottoById(util.getUserId(), msg1, new NetIntentCallBackListener(MainEnergyAdapter.this));
                        //util.setMotto(msg1);

                        input.setText("");
                        inputDialog.dismiss();
                    } else if(StringUtils.isEmpty(msg1)){
                        Toast.makeText(TaskContactDetailActivity.this, "输入内容不为空", Toast.LENGTH_SHORT).show();
                    }else if(StringUtils.getLength(msg1,true)>30){
                        Toast.makeText(TaskContactDetailActivity.this, "已超出30个字符！", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            cancleBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    inputDialog.dismiss();
                }
            });

            inputDialog = new AlertDialog.Builder(TaskContactDetailActivity.this)
                    .setView(dialogView)
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            input.setText("");
                        }}).setCancelable(false).create();
            inputDialog.show();
        } else {
            inputDialog.show();
        }
    }
}
