package com.saas.saasuser.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 批复联络详情
 */
public class ReplyContactDetailActivity extends BaseActivity implements NetIntentCallBackListener.INetIntentCallBack{
    @BindView(R.id.head_more)
    LinearLayout headMore;
    @BindView(R.id.el_show_info)
    ExpandableLayout elShowInfo;
    @BindView(R.id.btn_submit)
    TextView btnSubmit;


    String orderId,orderStatue;
    private JSONObject json;
    private SharedPreferencesUtil util;
    private AlertDialog inputDialog;
    private View dialogView;
    private EditText input;
    private TextView sureBtn;
    private TextView cancleBtn;
    private boolean isApproval=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_contact_detail);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        util=new SharedPreferencesUtil(this);
        Util.setHeadTitleMore(this, "批复联络", true);
        //headMore.setVisibility(View.GONE);
        orderId = this.getIntent().getStringExtra("orderId");
        orderStatue = this.getIntent().getStringExtra("orderStatue");
        new NetIntent().client_masterReplyOrderDetail(util.getUserId(),orderId, new NetIntentCallBackListener(this));//  TODO  批复联络详情
        if(dialog==null){
            dialog = CustomProgress.show(this, "加载中..", true, null);
        }
        if(StringUtils.equals("0",orderStatue)){
            btnSubmit.setText("重新申请");
            isApproval=false;
        }else {
            btnSubmit.setText("确认");

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
        FruitCategory fruitCategory = new FruitCategory("批复详情");
        String strApplyResult;
        if(StringUtils.equals("1",json.getString("BOOrderStatue").toString())){
            strApplyResult="审核不通过";
        }else if(StringUtils.equals("2",json.getString("BOOrderStatue").toString())){
            strApplyResult="审核中";
        }else if(StringUtils.equals("3",json.getString("BOOrderStatue").toString())){
            strApplyResult="审核通过";
        }else {
            strApplyResult="暂无";
        }
        Fruit fruit1 = new Fruit("申请结果:",strApplyResult);
        Fruit fruit2,fruit3;

        if(json.getJSONArray("BOAuditObject")!=null && json.getJSONArray("BOAuditObject").length()>0){
            fruit2 = new Fruit("批复人:", json.getJSONArray("BOAuditObject").getJSONObject(0).getString("AuditName"));
            fruit3 = new Fruit("批复日期:", json.getJSONArray("BOAuditObject").getJSONObject(0).getString("AuditDate").toString()+"  "+ json.getJSONArray("BOAuditObject").getJSONObject(0).getString("AuditTime").toString());
        }else{
            fruit2 = new Fruit("批复人:", "暂无");
            fruit3 = new Fruit("批复日期:","暂无");
        }

        Fruit fruit4 = new Fruit("批复说明:","暂无");
        section.parent = fruitCategory;
        section.children.add(fruit1);
        section.children.add(fruit2);
        section.children.add(fruit3);
        section.children.add(fruit4);

        section.expanded = true;
        return section;
    }

    public Section<FruitCategory, Fruit> getSection1()  throws JSONException {
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
        Fruit fruit7 = new Fruit("往返：",strReturn);
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
        section.expanded = true;

        return section;
    }

    public Section<FruitCategory, Fruit> getSection2() throws JSONException {
        Section<FruitCategory, Fruit> section = new Section<>();
        String strCarModel,strCarLevel;
        if(StringUtils.equals("1",json.getString("BOAssignCarModel").toString())){
            strCarModel="小轿车(5座以下)";
        }else if(StringUtils.equals("2",json.getString("BOAssignCarModel").toString())){
            strCarModel="商务车(6-8座)";
        }else if(StringUtils.equals("3",json.getString("BOAssignCarModel").toString())){
            strCarModel="商旅车(9-15座)";
        }else  if(StringUtils.equals("4",json.getString("BOAssignCarModel").toString())){
            strCarModel="中巴车(16-25座)";
        }else  if(StringUtils.equals("5",json.getString("BOAssignCarModel").toString())){
            strCarModel="小型巴士(26-37座)";
        }else  if(StringUtils.equals("6",json.getString("BOAssignCarModel").toString())){
            strCarModel="大型巴士(38-55座)";
        }else  {
            strCarModel="其他车";
        }

        if(StringUtils.equals("1",json.getString("BOAssignCarLevel").toString())){
            strCarLevel="经济车型";
        }else if(StringUtils.equals("2",json.getString("BOAssignCarLevel").toString())){
            strCarLevel="舒适车型";
        }else if(StringUtils.equals("3",json.getString("BOAssignCarLevel").toString())){
            strCarLevel="商务车型";
        }else if(StringUtils.equals("4",json.getString("BOAssignCarLevel").toString())){
            strCarLevel="豪华车型";
        }else {
            strCarLevel="巴士车型";
        }
        FruitCategory fruitCategory = new FruitCategory("行程备注");
        Fruit fruit1 = new Fruit("所需车型：",strCarModel);
        Fruit fruit2 = new Fruit("同行人：", StringUtils.repalceEmptyString(json.getString("BOAssociate").toString()));
        Fruit fruit3 = new Fruit("司机要求：", StringUtils.repalceEmptyString(json.getString("BODriverRequire").toString()));
        Fruit fruit4 = new Fruit("车辆要求：", StringUtils.repalceEmptyString(json.getString("BOVehicleRequire").toString()));
        Fruit fruit5 = new Fruit("其他要求", StringUtils.repalceEmptyString(json.getString("OtherRequire").toString()));
        Fruit fruit6 = new Fruit("异地留宿：", StringUtils.equals("0",json.getString("BOIsSleepover").toString())?"不留宿":"留宿");
        Fruit fruit7 = new Fruit("跨境车辆：", StringUtils.equals("0",json.getString("BOIsTransnational").toString())?"不跨境":"跨境");
        Fruit fruit8 = new Fruit("指定车属：", StringUtils.equals("0",json.getString("BOIsSelfCar").toString())?"不指定":"指定" );
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
        section.expanded = true;
        return section;
    }

    @OnClick({R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                if(isApproval){
                    new NetIntent().client_replyOrderApproval(orderId,new NetIntentCallBackListener(this));
                }else {
                    startActivity(new Intent(ReplyContactDetailActivity.this,AddTripApplyActivity.class).putExtra("reBuiltOrder","1").putExtra("orderId",orderId));
                    finish();
                }
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
            JSONObject jsonObject=new JSONObject(response);
            ToastUtils.showShortToastSafe(ReplyContactDetailActivity.this,  jsonObject.getString("ErrMsg"));
            LogUtils.e("批复联络返回数据："+jsonObject.toString());

            if(StringUtils.equals(jsonObject.getString("ErrCode"),"1")){
                if(StringUtils.equals("操作成功！",jsonObject.getString("ErrMsg"))){//TODO  操作成功结束
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
            dialogView = LayoutInflater.from(ReplyContactDetailActivity.this).inflate(R.layout.dialog_input, null);
            input = (EditText) dialogView.findViewById(R.id.input);
            sureBtn = (TextView) dialogView.findViewById(R.id.sureBtn);
            cancleBtn = (TextView) dialogView.findViewById(R.id.cancleBtn);
            sureBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    msg1=input.getText().toString();
                    if (StringUtils.isNotEmpty(msg1,true) && StringUtils.getLength(msg1,true)<30) {


                        input.setText("");
                        inputDialog.dismiss();
                    } else if(StringUtils.isEmpty(msg1)){
                        Toast.makeText(ReplyContactDetailActivity.this, "输入内容不为空", Toast.LENGTH_SHORT).show();
                    }else if(StringUtils.getLength(msg1,true)>30){
                        Toast.makeText(ReplyContactDetailActivity.this, "已超出30个字符！", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            cancleBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    inputDialog.dismiss();
                }
            });

            inputDialog = new AlertDialog.Builder(ReplyContactDetailActivity.this)
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
