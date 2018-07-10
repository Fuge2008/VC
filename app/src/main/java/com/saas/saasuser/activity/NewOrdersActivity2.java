package com.saas.saasuser.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.example.liangmutian.mypicker.DatePickerDialog;
import com.example.liangmutian.mypicker.DateUtil;
import com.example.liangmutian.mypicker.TimePickerDialog;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.saas.saasuser.R;
import com.saas.saasuser.activity.map.LocationSelecteActivity;
import com.saas.saasuser.application.MyApplication;
import com.saas.saasuser.async.HttpException;
import com.saas.saasuser.async.HttpOkUtils;
import com.saas.saasuser.entity.GoongSiJianChengInfo;
import com.saas.saasuser.entity.JJRolesInfo;
import com.saas.saasuser.entity.KeHuInfo;
import com.saas.saasuser.entity.PhoneInfo;
import com.saas.saasuser.global.Constants;
import com.saas.saasuser.util.SharedPreferencesUtil;
import com.saas.saasuser.util.StringUtils;
import com.saas.saasuser.view.ExpandLayout;
import com.saas.saasuser.view.ItemDialog;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by tanlin on 2017/10/12.
 */

public class NewOrdersActivity2 extends BaseActivity2 implements View.OnClickListener, ItemDialog.OnDialogItemClickListener {

    private LinearLayout expand_img;
    private LinearLayout expand_img1;

    private LinearLayout expand_img3;

    private ExpandLayout mExpandableLayout;
    private ExpandLayout mExpandableLayout1;
    private ExpandLayout mExpandableLayout3;

    private final int GONGSIJIANCHENG = 1001;
    private final int KEHUXINXI = 1002;
    private final int PHONE = 1003;
    private final int SUBJECT = 1004;
    private final int JJROLES = 1005;

    private SharedPreferencesUtil util;
    List<GoongSiJianChengInfo.DataBean> gsjcList;

    List<KeHuInfo.DataBean> kehujcList;
    List<JJRolesInfo.DataBean> joleList;

    List<String> jcList;
    List<String> keList;
    List<String> phoneList;
    List<String> jjList;


    private AlertDialog inputDialog;
    private View dialogView;
    private EditText input;
    private Button sureBtn;
    private Button cancleBtn;

    private Dialog dateDialog, timeDialog, chooseDialog, timeDialog1,timeDialog2;

//    private CheckBox mCheckBox1;
//    private CheckBox mCheckBox2;
//    private CheckBox mCheckBox3;


    private LinearLayout iv_danweijiancheng;
    private TextView tv_danweijiancheng;


    private LinearLayout iv_chengcheyonghu;
    private TextView tv_chengcheyonghu;

    private LinearLayout iv_yongcheriqi;
    private TextView tv_yongcheriqi;

    private LinearLayout iv_kaishishijian;
    private TextView tv_kaishishijian;

    private LinearLayout iv_wangfanliusu;
    private TextView tv_wangfanliusu;

    private LinearLayout iv_lianxidianhua;
    private TextView tv_lianxidianhua;

    private LinearLayout iv_yewuleibie;
    private TextView tv_yewuleibie;

    private LinearLayout iv_jiesuanfangshi;
    private TextView tv_jiesuanfangshi;

    private LinearLayout iv_jieshushijian;
    private TextView tv_jieshushijian;


    private LinearLayout iv_suoxuchexing;
    private TextView tv_suoxuchexing;


    private LinearLayout iv_zhidingcheliangdengji;
    private TextView tv_zhidingcheliangdengji;


    private LinearLayout iv_jijiaguize;
    private TextView tv_jijiaguize;

    private LinearLayout iv_baobibizhong;
    private TextView tv_baobibizhong;


    private LinearLayout iv_daishoubizhong;
    private TextView tv_daishoubizhong;

    private LinearLayout iv_yidiliusu;
    private TextView tv_yidiliusu;

    private LinearLayout iv_kuanjincheliang;
    private TextView tv_kuanjincheliang;

    private LinearLayout iv_zhiziqiandan;
    private TextView tv_zhiziqiandan;

    private ImageView iv_1;

    private ImageView iv_2;
    private ImageView iv_3;




    private EditText ed_shangchedidian;
    private EditText ed_xingchengmudidi;
    private EditText ed_chengcherenshu;
    private EditText ed_yongtushuoming;
    private EditText ed_xiachedidian;
    private EditText ed_dianhua;
    private EditText ed_xingming;
    private EditText ed_tongxingren;
    private EditText ed_sijiyaoqiu;
    private EditText ed_cheliangyaoqiu;
    private EditText ed_qitayaoqiu;
    private EditText ed_dingdantishi;
    private EditText ed_jiejipaineirong;
    private EditText ed_dingdanbaojia;
    private EditText ed_sijidaishou;
    private EditText ed_daishoushuoming;


    private Button bt_tijiaoxinxi;
    private boolean isChecked = false;
    private boolean isChecked1 = false;
    private boolean isChecked3 = false;

    private LinkedHashMap<String, String[]> mLinkHashMap ;
    private LinkedHashMap<String, String[]> mLinkHashMap1;
    String[] s;
    String[] s1;
    private List<String[]> mList;
    private List<String[]> mList1;

    String item = "";
    String item1 = "";
    String item2 = "";
    String item3 = "";

    private ImageView ll1;
    private ImageView ll2;

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order2);
        MyApplication.getInstance().addActivity(this);
        mLinkHashMap = new LinkedHashMap<>();
        mLinkHashMap1 = new LinkedHashMap<>();
        setTitle("新增订单");
        showSubmitDialog();// todo 提示是否图片下单

        btn_left.setImageResource(R.drawable.icon_back);

        ImmersionBar.with(this).statusBarColor(R.color.status_color)
                .fitsSystemWindows(true).init();


        btn_right.setText("提交订单");
        btn_right.setOnClickListener(this);

        initView();
        util = new SharedPreferencesUtil(this);

        gsjcList = new ArrayList<>();
        kehujcList = new ArrayList<>();

        jcList = new ArrayList<>();
        keList = new ArrayList<>();
        phoneList = new ArrayList<>();
        jjList = new ArrayList<>();


        mList = new ArrayList<>();
        mList1 = new ArrayList<>();


        expand_img = (LinearLayout) findViewById(R.id.expand_img);
        expand_img1 = (LinearLayout) findViewById(R.id.expand_img1);
        expand_img3 = (LinearLayout) findViewById(R.id.expand_img3);

        iv_1= (ImageView) findViewById(R.id.iviv_1);
        iv_2= (ImageView) findViewById(R.id.iviv_2);
        iv_3= (ImageView) findViewById(R.id.iviv_3);

        mExpandableLayout = (ExpandLayout) findViewById(R.id.expandLayout);
        mExpandableLayout1 = (ExpandLayout) findViewById(R.id.expandLayout1);
        mExpandableLayout3 = (ExpandLayout) findViewById(R.id.expandLayout3);

        mExpandableLayout.post(new Runnable() {
            @Override
            public void run() {
                mExpandableLayout.initExpand(false);
            }
        });
        mExpandableLayout1.post(new Runnable() {
            @Override
            public void run() {
                mExpandableLayout1.initExpand(false);
            }
        });
        mExpandableLayout3.post(new Runnable() {
            @Override
            public void run() {
                mExpandableLayout3.initExpand(true);
            }
        });

        expand_img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mExpandableLayout.toggleExpand();
                isChecked = !isChecked;
                if (isChecked) {
                    iv_1.setImageResource(R.drawable.shangsanjiao);

                } else {

                    iv_1.setImageResource(R.drawable.xiasanjiao);
                }


            }
        });

        expand_img1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mExpandableLayout1.toggleExpand();
                isChecked1 = !isChecked1;
                if (isChecked1) {
                    iv_2.setImageResource(R.drawable.shangsanjiao);

                } else {

                    iv_2.setImageResource(R.drawable.xiasanjiao);
                }

            }
        });
        expand_img3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mExpandableLayout3.toggleExpand();
                isChecked1 = !isChecked1;
                if (isChecked3) {
                    iv_3.setImageResource(R.drawable.shangsanjiao);

                } else {

                    iv_3.setImageResource(R.drawable.xiasanjiao);
                }

            }
        });

        initNetWork();

    }

    String str_yidilousu = "0";
    String str_kuanjingcheliang = "0";
    String str_zizhiqiandan = "0";

    private void initView() {

        iv_danweijiancheng = (LinearLayout) findViewById(R.id.danweijiancheng);
        tv_danweijiancheng = (TextView) findViewById(R.id.tvdanweijiancheng);
        iv_danweijiancheng.setOnClickListener(this);

        iv_chengcheyonghu = (LinearLayout) findViewById(R.id.chengcheyonghu);
        tv_chengcheyonghu = (TextView) findViewById(R.id.tvchengcheyonghu);
        iv_chengcheyonghu.setOnClickListener(this);

        iv_yongcheriqi = (LinearLayout) findViewById(R.id.yongcheriqi);
        tv_yongcheriqi = (TextView) findViewById(R.id.tvyongcheriqi);
        iv_yongcheriqi.setOnClickListener(this);

        iv_kaishishijian = (LinearLayout) findViewById(R.id.kaishishijian);
        tv_kaishishijian = (TextView) findViewById(R.id.tvkaishishijian);
        iv_kaishishijian.setOnClickListener(this);


        iv_wangfanliusu = (LinearLayout) findViewById(R.id.wangfanliusu);
        tv_wangfanliusu = (TextView) findViewById(R.id.tvwangfanliusu);
        iv_wangfanliusu.setOnClickListener(this);


        iv_lianxidianhua = (LinearLayout) findViewById(R.id.lianxidianhua);
        tv_lianxidianhua = (TextView) findViewById(R.id.tvlianxidianhua);
        iv_lianxidianhua.setOnClickListener(this);


        iv_yewuleibie = (LinearLayout) findViewById(R.id.yewuleibie);
        tv_yewuleibie = (TextView) findViewById(R.id.tvyewuleibie);
        iv_yewuleibie.setOnClickListener(this);


        iv_jiesuanfangshi = (LinearLayout) findViewById(R.id.jiesuanfangshi);
        tv_jiesuanfangshi = (TextView) findViewById(R.id.tvjiesuanfangshi);
        iv_jiesuanfangshi.setOnClickListener(this);


        iv_suoxuchexing = (LinearLayout) findViewById(R.id.suoxuchexing);
        tv_suoxuchexing = (TextView) findViewById(R.id.tvsuoxuchexing);
        iv_suoxuchexing.setOnClickListener(this);


        iv_zhidingcheliangdengji = (LinearLayout) findViewById(R.id.zhidingcheliangdengji);
        tv_zhidingcheliangdengji = (TextView) findViewById(R.id.tvzhidingcheliangdengji);
        iv_zhidingcheliangdengji.setOnClickListener(this);

        iv_jijiaguize= (LinearLayout) findViewById(R.id.jijiaguize);
        tv_jijiaguize = (TextView) findViewById(R.id.tvjijiaguize);
        iv_jijiaguize.setOnClickListener(this);

        iv_baobibizhong= (LinearLayout) findViewById(R.id.baobibizhong);
        tv_baobibizhong = (TextView) findViewById(R.id.tvbaobibizhong);
        iv_baobibizhong.setOnClickListener(this);

        iv_daishoubizhong= (LinearLayout) findViewById(R.id.daishoubizhong);
        tv_daishoubizhong = (TextView) findViewById(R.id.tvdaishoubizhong);
        iv_daishoubizhong.setOnClickListener(this);


        iv_jieshushijian= (LinearLayout) findViewById(R.id.jieshushijian);
        tv_jieshushijian = (TextView) findViewById(R.id.tvjieshushijian);
        iv_jieshushijian.setOnClickListener(this);

        //////////////////
        iv_yidiliusu= (LinearLayout) findViewById(R.id.yidiliusu);
        tv_yidiliusu = (TextView) findViewById(R.id.tvyidiliusu);
        iv_yidiliusu.setOnClickListener(this);

        iv_kuanjincheliang= (LinearLayout) findViewById(R.id.kuanjincheliang);
        tv_kuanjincheliang = (TextView) findViewById(R.id.tvkuanjincheliang);
        iv_kuanjincheliang.setOnClickListener(this);


        iv_zhiziqiandan= (LinearLayout) findViewById(R.id.zhiziqiandan);
        tv_zhiziqiandan = (TextView) findViewById(R.id.tvzhiziqiandan);
        iv_zhiziqiandan.setOnClickListener(this);







        ed_shangchedidian = (EditText) findViewById(R.id.shangchedidian);
        ed_shangchedidian.setOnClickListener(this);
        ed_xingchengmudidi = (EditText) findViewById(R.id.xingchengmudidi);
        ed_xingchengmudidi.setOnClickListener(this);
        ed_chengcherenshu = (EditText) findViewById(R.id.chengcherenshu);
        ed_chengcherenshu.setOnClickListener(this);
        ed_yongtushuoming = (EditText) findViewById(R.id.yongtushuoming);
        ed_yongtushuoming.setOnClickListener(this);

        ed_xiachedidian = (EditText) findViewById(R.id.xiachedidian);
        ed_xiachedidian.setOnClickListener(this);

        ed_xiachedidian = (EditText) findViewById(R.id.xiachedidian);
        ed_xiachedidian.setOnClickListener(this);
        ed_dianhua = (EditText) findViewById(R.id.dianhua);
        ed_dianhua.setOnClickListener(this);
        ed_xingming = (EditText) findViewById(R.id.xingming);
        ed_xingming.setOnClickListener(this);
        ed_tongxingren = (EditText) findViewById(R.id.tongxingren);
        ed_tongxingren.setOnClickListener(this);
        ed_sijiyaoqiu = (EditText) findViewById(R.id.sijiyaoqiu);
        ed_sijiyaoqiu.setOnClickListener(this);
        ed_cheliangyaoqiu = (EditText) findViewById(R.id.cheliangyaoqiu);
        ed_cheliangyaoqiu.setOnClickListener(this);
        ed_qitayaoqiu = (EditText) findViewById(R.id.qitayaoqiu);
        ed_qitayaoqiu.setOnClickListener(this);
        ed_dingdantishi = (EditText) findViewById(R.id.dingdantishi);
        ed_dingdantishi.setOnClickListener(this);
        ed_jiejipaineirong = (EditText) findViewById(R.id.jiejipaineirong);
        ed_jiejipaineirong.setOnClickListener(this);
        ed_dingdanbaojia = (EditText) findViewById(R.id.dingdanbaojia);
        ed_dingdanbaojia.setOnClickListener(this);

        ed_sijidaishou = (EditText) findViewById(R.id.sijidaishou);
        ed_sijidaishou.setOnClickListener(this);
        ed_daishoushuoming = (EditText) findViewById(R.id.daishoushuoming);
        ed_daishoushuoming.setOnClickListener(this);


        ll1= (ImageView) findViewById(R.id.ll1);
        ll2= (ImageView) findViewById(R.id.ll2);

        ll1.setOnClickListener(this);
        ll2.setOnClickListener(this);

    }

    private void initNetWork() {
        request(GONGSIJIANCHENG);


    }

    String sss="";


    @Override
    public Object doInBackground(int requestCode) throws HttpException, IOException {

        switch (requestCode) {
            case GONGSIJIANCHENG:
                HashMap<String, String> map = new HashMap<>();
                map.put("vvId", util.getUserId());
                String result = HttpOkUtils.post(Constants.HttpRoot + Constants.GONGSIJIANCHENG, map);


                return result;
            case KEHUXINXI:
                HashMap<String, String> map1 = new HashMap<>();
                map1.put("vvComId", mLinkHashMap.get(item)[0]);

                strqiyeId=mLinkHashMap.get(item)[0];


                map1.put("vvType", mLinkHashMap.get(item)[1]);


                sss=mLinkHashMap.get(item)[1];


                LogUtils.tag("baba").d(mLinkHashMap.get(item)[1]);

                String result1 = HttpOkUtils.post(Constants.HttpRoot + Constants.KEHUXINXI, map1);




                return result1;
            case JJROLES:
                HashMap<String, String> map5 = new HashMap<>();
                //   map5.put("vvComId",util.getCompanyId());
                map5.put("vvComId", "4");


                String result5 = HttpOkUtils.post(Constants.HttpRoot + Constants.JJROLES, map5);

                return result5;
            case PHONE:
                HashMap<String, String> map2 = new HashMap<>();
                map2.put("vvId", mLinkHashMap1.get(item1)[0]);
                map2.put("vvType", mLinkHashMap1.get(item1)[1]);

                String result2 = HttpOkUtils.post(Constants.HttpRoot + Constants.PHONE, map2);

                return result2;
            case SUBJECT:
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                        if (TextUtils.isEmpty(item)) {

                            Toast.makeText(NewOrdersActivity2.this, "单位简称不能为空", Toast.LENGTH_SHORT).show();

                        } else if (TextUtils.isEmpty(item1)) {


                            Toast.makeText(NewOrdersActivity2.this, "乘车用户不能为空", Toast.LENGTH_SHORT).show();


                        } else if (TextUtils.isEmpty(strriqi)) {


                            Toast.makeText(NewOrdersActivity2.this, "用车日期不能为空", Toast.LENGTH_SHORT).show();


                        } else if (TextUtils.isEmpty(stshijian)) {


                            Toast.makeText(NewOrdersActivity2.this, "开始时间不能为空", Toast.LENGTH_SHORT).show();


                        } else if (TextUtils.isEmpty(str_shangchedidian)) {


                            Toast.makeText(NewOrdersActivity2.this, "上车地点不能为空", Toast.LENGTH_SHORT).show();


                        } else if (TextUtils.isEmpty(str_xingchengmudidi)) {


                            Toast.makeText(NewOrdersActivity2.this, "行程终点不能为空", Toast.LENGTH_SHORT).show();


                        } else if (TextUtils.isEmpty(str_wangfanliusu)) {


                            Toast.makeText(NewOrdersActivity2.this, "往返留宿不能为空", Toast.LENGTH_SHORT).show();


                        } else if (TextUtils.isEmpty(str_chengcherenshu)) {


                            Toast.makeText(NewOrdersActivity2.this, "乘车人数不能为空", Toast.LENGTH_SHORT).show();


                        } else if (TextUtils.isEmpty(item2)) {


                            Toast.makeText(NewOrdersActivity2.this, "联系电话不能为空", Toast.LENGTH_SHORT).show();


                        } else if (TextUtils.isEmpty(str_yewuleibie)) {


                            Toast.makeText(NewOrdersActivity2.this, "业务类别不能为空", Toast.LENGTH_SHORT).show();


                        } else if (TextUtils.isEmpty(str_jiesuanfangshi)) {


                            Toast.makeText(NewOrdersActivity2.this, "结算方式不能为空", Toast.LENGTH_SHORT).show();


                        } else if (TextUtils.isEmpty(str_yongtushuoming)) {


                            Toast.makeText(NewOrdersActivity2.this, "用途说明不能为空", Toast.LENGTH_SHORT).show();


                        }
//                        else if(!isMobileNO(strdianhua)){
//
//                            Toast.makeText(NewOrdersActivity2.this, "预订人电话号码格式不正确", Toast.LENGTH_SHORT).show();
//
//                        }

                    }


                });


                HashMap<String, String> map3 = new HashMap<>();
                map3.put("BOBusinessName", tv_danweijiancheng.getText().toString());//单位简称
                map3.put("BONeederName", tv_chengcheyonghu.getText().toString());//乘车用户

                map3.put("BOBusinessType",sss);/////////////////////

                LogUtils.tag("babama").d(sss);

                map3.put("BOUseDate", strriqi);//用车日期
                map3.put("BOUStartTime", stshijian);//用车时间
                map3.put("BOUpCarPlace", str_shangchedidian);//上车地点
                map3.put("BODestination", str_xingchengmudidi);//行程目的地
                map3.put("BOGoBack", str_wangfanliusu);//往返留宿

                map3.put("BOPassengers", str_chengcherenshu);//乘车人数
                map3.put("BONeederPhone", tv_lianxidianhua.getText().toString());//用车人电话
                map3.put("BOServiceType", str_yewuleibie);//业务类别
                map3.put("BOSettleType", str_jiesuanfangshi);//结算方式
                map3.put("BOPurposeDsc", str_yongtushuoming);//用途说明

                map3.put("BOIsSleepover", str_yidilousu);//异地留宿
                map3.put("BOIsTransnational", str_kuanjingcheliang);//是否跨境
                map3.put("BOSigning", str_zizhiqiandan);//纸质签单

                map3.put("BOBusinessID", strqiyeId);//企业Id
                map3.put("BONeederID", stryongcherenId);//用车人Id

                //  map3.put("BONeederID", util.getUserId());//登录人Id LoginID
                map3.put("BOBelongCompany", util.getCompanyId());//公司Id


                //非必要字段
                map3.put("BODownCarPlace", strxiachedidian);//下车地点
                map3.put("BOUEndTime", strjieshushijian);//结束时间
                map3.put("BOAssignCarModel", str_suoxuchexing);//所需车型
                map3.put("BOAssignCarLevel", str_zhidingcheliang);//指定车辆等级

                map3.put("BOPlanPhone", strdianhua);//预订人电话

                map3.put("BOAssociate", strtongxingren);//同行人
                map3.put("BODriverRequire", strsijiyaoqiu);//司机要求
                map3.put("BOVehicleRequire", strcheliangyaoqiu);//车辆要求
                map3.put("OtherRequire ", strqitayaoqiu);//其他要求
                map3.put("BOOrderMark  ", strdingdantishi);//订单提示
                map3.put("BOPickUpContent  ", strjiejipaineirong);//接机牌内容

                map3.put("BOOrderOffer", strdingdanbaojia);//订单报价
                map3.put("BOOrderReceive  ", strsijidaishou);//司机代收
                map3.put("BOReceiveDesc  ", strdaishoushuoming);//代收说明
                map3.put("BOValuesRule  ", item3);//计价规则

                map3.put("BOOfferType", str_baobibizhong);//报币币种
                map3.put("BOReceiveType ", str_daishoubizhong);//代收币种


                LogUtils.tag("maozhuxi").d(map3);


                String result3 = HttpOkUtils.post(Constants.HttpRoot + Constants.NEW_ORDER, map3);


                Looper.prepare();

                LogUtils.tag("maozhuxisssss").d(result3);


                if(result3.contains("成功")){
                    Toast.makeText(NewOrdersActivity2.this,"保存订单成功",Toast.LENGTH_SHORT).show();
                    finish();

                }else if(result3.contains("参数有误")){

                    // Toast.makeText(NewOrdersActivity2.this,"参数有误",Toast.LENGTH_SHORT).show();
                }

                Looper.loop();

                return result3;

        }
        return super.doInBackground(requestCode);
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        super.onSuccess(requestCode, result);

        switch (requestCode) {
            case GONGSIJIANCHENG:
                String str = (String) result;

                jcList.clear();

                gsjcList = new Gson().fromJson(str, GoongSiJianChengInfo.class).getData();

                for (int i = 0; i < gsjcList.size(); i++) {

                    String ss = gsjcList.get(i).getSName();

                    if (!TextUtils.isEmpty(ss)) {
                        jcList.add(ss);

                    }
                    s = new String[2];
                    s[0] = gsjcList.get(i).getID() + "";

                    s[1] = gsjcList.get(i).getVvType() + "";

                    mList.add(s);


                    mLinkHashMap.put(gsjcList.get(i).getSName(), mList.get(i));
                }
                LogUtils.tag("haoma").d(jcList);

                break;

            case KEHUXINXI:
                keList.clear();

                String str1 = (String) result;

                kehujcList = new Gson().fromJson(str1, KeHuInfo.class).getData();

                for (int i = 0; i < kehujcList.size(); i++) {

                    keList.add(kehujcList.get(i).getSName());
                    s1 = new String[2];
                    s1[0] = kehujcList.get(i).getID() + "";

                    s1[1] = kehujcList.get(i).getVvType() + "";

                    mList1.add(s1);


                    mLinkHashMap1.put(kehujcList.get(i).getSName(), mList1.get(i));
                }


                break;
            case JJROLES:

                jjList.clear();

                String str2 = (String) result;

                joleList = (new Gson().fromJson(str2, JJRolesInfo.class).getData());

                for (int i = 0; i < joleList.size(); i++) {

                    jjList.add(joleList.get(i).getSName());


                }


                break;

            case PHONE:
                // joleList.clear();
                phoneList.clear();

                str5 = (String) result;


//                try {
//                    JSONObject jsonObject=new JSONObject(phoneString);
//
//                    phoneString=jsonObject.getString("Data");
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }


                LogUtils.tag("made").d(str5);

                phoneList.add(new Gson().fromJson(str5, PhoneInfo.class).getData());

                break;


        }

    }

    String str5;

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        super.onFailure(requestCode, state, result);
    }

    private String[] arr_danweijiancheng;
    private String[] arr_chengcheyonghu;
    private String[] arr_wangfanliusu;
    private String[] arr_lianxidianhua;
    private String[] arr_yewuleibie;
    private String[] arr_jiesuanfangshi;
    private String[] arr_suoxuchexing;
    private String[] arr_zhidingcheliangdengji;
    private String[] arr_jijiaguize;
    private String[] arr_baobibizhong;
    private String[] arr_daishoubizhong;
    private String[] arr_yidiliusu;
    private String[] arr_kuanjincheliang;
    private String[] arr_zhiziqiandan;
    private int TYPE_TAG;

    private void choseCarModel(String[] array, String title, int tag) {
        new ItemDialog(NewOrdersActivity2.this, array, title, tag, this).show();
        TYPE_TAG = tag;
    }


    public void showListDialog1(List<String> list) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选中您喜欢的数字:");
        builder.setIcon(R.drawable.saas_logo);/*设置图标*/

        final String[] items = list.toArray(new String[list.size()]);

        builder.setItems(items, new DialogInterface.OnClickListener() {/*设置列表的点击事件*/
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(NewOrdersActivity2.this, items[which], Toast.LENGTH_SHORT).show();

                item1 = items[which];
                tv_chengcheyonghu.setText(item1);


            }
        });
        builder.setCancelable(true);
        builder.show();

    }
    public static final int REQUSE14 = 14;
    public static final int REQUSE15 = 15;



    /**
     * 接收返回数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // requestCode标示请求的标示 resultCode表示有数据
        switch (requestCode) {
            case REQUSE14:
                if (requestCode == NewOrdersActivity2.REQUSE14 && resultCode == RESULT_OK) {
                    String strLocation = data.getStringExtra(LocationSelecteActivity.DATA_STR);
                    Log.i("info", "返回位置" + strLocation);
                    ed_shangchedidian.setText(strLocation);

                }
                break;
            case REQUSE15:
                if (requestCode == NewOrdersActivity2.REQUSE15 && resultCode == RESULT_OK) {
                    String strLocation = data.getStringExtra(LocationSelecteActivity.DATA_STR);
                    Log.i("info", "返回位置" + strLocation);
                    ed_xingchengmudidi.setText(strLocation);

                }
                break;

        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.ll1:
                Intent intent1 = new Intent(NewOrdersActivity2.this, LocationSelecteActivity.class);
                startActivityForResult(intent1, REQUSE14);

                break;

            case R.id.ll2:

                Intent intent2= new Intent(NewOrdersActivity2.this, LocationSelecteActivity.class);
                startActivityForResult(intent2, REQUSE15);
                break;



            case R.id.danweijiancheng:

                arr_danweijiancheng = (String[]) jcList.toArray(new String[jcList.size()]);
                choseCarModel(arr_danweijiancheng, "单位简称", 1);
                break;


            case R.id.jijiaguize:

                arr_jijiaguize = (String[]) jjList.toArray(new String[jjList.size()]);
                choseCarModel(arr_jijiaguize, "计价规则", 11);


                break;


            case R.id.chengcheyonghu:


                arr_chengcheyonghu = (String[]) keList.toArray(new String[keList.size()]);
                choseCarModel(arr_chengcheyonghu, "乘车用户", 2);

                break;
            case R.id.lianxidianhua:


                arr_lianxidianhua = (String[]) phoneList.toArray(new String[phoneList.size()]);
                choseCarModel(arr_lianxidianhua, "联系电话", 4);
                break;
            case R.id.yongcheriqi:

                Date date = new Date();
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String time = format.format(date);
                showDateDialog(DateUtil.getDateForString(time));

                break;
            case R.id.kaishishijian:

                showTimePick();

                break;
            case R.id.jieshushijian:

                showTimePick1();

                break;
            case R.id.wangfanliusu:


                List<String> mList = new ArrayList<>();
                mList.add("单程往");
                mList.add("单程返");
                mList.add("往返");

                arr_wangfanliusu = (String[]) mList.toArray(new String[mList.size()]);
                choseCarModel(arr_wangfanliusu, "往返留宿", 3);




                break;
            case R.id.yidiliusu:


                List<String> mList100 = new ArrayList<>();

                mList100.add("否");
                mList100.add("是");

                arr_yidiliusu = (String[]) mList100.toArray(new String[mList100.size()]);
                choseCarModel(arr_yidiliusu, "异地留宿", 100);
                break;


            case R.id.kuanjincheliang:


                List<String> mList101 = new ArrayList<>();

                mList101.add("否");
                mList101.add("是");

                arr_kuanjincheliang = (String[]) mList101.toArray(new String[mList101.size()]);
                choseCarModel(arr_kuanjincheliang, "跨境车辆", 101);
                break;

            case R.id.zhiziqiandan:

                List<String> mList102 = new ArrayList<>();
                mList102.add("否");
                mList102.add("是");
                arr_zhiziqiandan = (String[]) mList102.toArray(new String[mList102.size()]);
                choseCarModel(arr_zhiziqiandan, "纸质签单", 102);
                break;

            case R.id.yewuleibie:
                List<String> mList1 = new ArrayList<>();
                mList1.add("长租业务");
                mList1.add("短租业务");
                mList1.add("班车业务");
                mList1.add("其他业务");

                arr_yewuleibie = (String[]) mList1.toArray(new String[mList1.size()]);
                choseCarModel(arr_yewuleibie, "业务类别", 5);

                break;
            case R.id.jiesuanfangshi:
                List<String> mList2 = new ArrayList<>();
                mList2.add("内部");
                mList2.add("现金");
                mList2.add("月结");
                mList2.add("挂账");
                mList2.add("免费");

                arr_jiesuanfangshi = (String[]) mList2.toArray(new String[mList2.size()]);
                choseCarModel(arr_jiesuanfangshi, "结算方式", 6);

                break;
            case R.id.suoxuchexing:
                List<String> mList3 = new ArrayList<>();

                mList3.add("小桥车");
                mList3.add("商务车");
                mList3.add("中巴车");
                mList3.add("大巴车");
                mList3.add("其他车");




                arr_suoxuchexing = (String[]) mList3.toArray(new String[mList3.size()]);
                choseCarModel(arr_suoxuchexing, "所需车型", 7);
                break;
            case R.id.zhidingcheliangdengji:
                List<String> mList4 = new ArrayList<>();

                mList4.add("经济车型");
                mList4.add("舒适车型");
                mList4.add("商务车型");
                mList4.add("豪华车型");
                mList4.add("巴士车型");


                arr_zhidingcheliangdengji = (String[]) mList4.toArray(new String[mList4.size()]);
                choseCarModel(arr_zhidingcheliangdengji, "指定车辆等级", 8);

                break;
            case R.id.baobibizhong:
                List<String> mList5 = new ArrayList<>();
                mList5.add("人民币");
                mList5.add("港币");
                mList5.add("美元");
                mList5.add("英镑");
                mList5.add("欧罗");
                mList5.add("其他");

                arr_baobibizhong = (String[]) mList5.toArray(new String[mList5.size()]);
                choseCarModel(arr_baobibizhong, "报币币种", 9);

                break;
            case R.id.daishoubizhong:
                List<String> mList6 = new ArrayList<>();
                mList6.add("人民币");
                mList6.add("港币");
                mList6.add("美元");
                mList6.add("英镑");
                mList6.add("欧罗");
                mList6.add("其他");

                arr_daishoubizhong = (String[]) mList6.toArray(new String[mList6.size()]);
                choseCarModel(arr_daishoubizhong, "代收币种", 10);

                break;
            case R.id.btn_right:

                str_shangchedidian = ed_shangchedidian.getText().toString().trim();
                str_xingchengmudidi = ed_xingchengmudidi.getText().toString().trim();
                str_chengcherenshu = ed_chengcherenshu.getText().toString().trim();
                str_yongtushuoming = ed_yongtushuoming.getText().toString().trim();

                strxiachedidian = ed_xiachedidian.getText().toString().trim();
                strdianhua = ed_dianhua.getText().toString().trim();
                strxingming = ed_xingming.getText().toString().trim();
                strtongxingren = ed_tongxingren.getText().toString().trim();
                strsijiyaoqiu = ed_sijiyaoqiu.getText().toString().trim();

                strcheliangyaoqiu = ed_cheliangyaoqiu.getText().toString().trim();
                strqitayaoqiu = ed_qitayaoqiu.getText().toString().trim();
                strdingdantishi = ed_dingdantishi.getText().toString().trim();
                strjiejipaineirong = ed_jiejipaineirong.getText().toString().trim();

                strdingdanbaojia = ed_dingdanbaojia.getText().toString().trim();
                strsijidaishou = ed_sijidaishou.getText().toString().trim();
                strdaishoushuoming = ed_daishoushuoming.getText().toString().trim();


                request(SUBJECT);


                break;


        }


    }

    String strriqi = "";
    String stshijian = "";

    String str_shangchedidian = "";
    String str_xingchengmudidi = "";
    String str_wangfanliusu = "";
    String str_chengcherenshu = "";
    String str_yewuleibie = "";
    String str_jiesuanfangshi = "";
    String str_yongtushuoming = "";

    String str_suoxuchexing = "";
    String str_zhidingcheliang = "";

    String strqiyeId = "";//企业ID
    String stryongcherenId = "";//企业ID
    String strxiachedidian = "";//企业ID
    String strjieshushijian = "";//企业ID
    String strdianhua = "";//企业ID
    String strxingming = "";//企业ID
    String strtongxingren = "";//企业ID
    String strsijiyaoqiu = "";//企业ID
    String strcheliangyaoqiu = "";//企业ID
    String strqitayaoqiu = "";//企业ID
    String strdingdantishi = "";//企业ID
    String strjiejipaineirong = "";//企业ID
    String strdingdanbaojia = "";//企业ID
    String strsijidaishou = "";//企业ID
    String strdaishoushuoming = "";//企业ID
    String str_baobibizhong = "";//企业ID
    String str_daishoubizhong = "";//企业ID


    private void showDateDialog(List<Integer> date) {
        DatePickerDialog.Builder builder = new DatePickerDialog.Builder(this);
        builder.setOnDateSelectedListener(new DatePickerDialog.OnDateSelectedListener() {
            @Override
            public void onDateSelected(int[] dates) {

                strriqi = (dates[0] + "-" + (dates[1] > 9 ? dates[1] : ("0" + dates[1])) + "-"
                        + (dates[2] > 9 ? dates[2] : ("0" + dates[2])));

                tv_yongcheriqi.setText(strriqi);

            }

            @Override
            public void onCancel() {

            }
        })

                .setSelectYear(date.get(0) - 1)
                .setSelectMonth(date.get(1) - 1)
                .setSelectDay(date.get(2) - 1);

        builder.setMaxYear(DateUtil.getYear());
        builder.setMaxMonth(DateUtil.getDateForString(DateUtil.getToday()).get(1));
        builder.setMaxDay(DateUtil.getDateForString(DateUtil.getToday()).get(2));
        dateDialog = builder.create();
        dateDialog.show();
    }

    private void showTimePick() {

        if (timeDialog1 == null) {

            TimePickerDialog.Builder builder = new TimePickerDialog.Builder(this);
            timeDialog1 = builder.setOnTimeSelectedListener(new TimePickerDialog.OnTimeSelectedListener() {
                @Override
                public void onTimeSelected(int[] times) {

                    String s = times[0] + "";
                    String s1 = times[1] + "";

                    if (times[0] < 10 ) {
                        s = "0" + times[0];
                    }

                    if (times[1] < 10) {
                        s1 = "0" + times[1];
                    }

                    stshijian = s + ":" + s1;
                    tv_kaishishijian.setText(stshijian);


                }
            }).create();
        }

        timeDialog1.show();

    }
    private void showTimePick1() {

        if (timeDialog2== null) {

            TimePickerDialog.Builder builder = new TimePickerDialog.Builder(this);
            timeDialog2 = builder.setOnTimeSelectedListener(new TimePickerDialog.OnTimeSelectedListener() {
                @Override
                public void onTimeSelected(int[] times) {

                    String s = times[0] + "";
                    String s1 = times[1] + "";

                    if (times[0] < 10  ) {
                        s = "0" + times[0];
                    }

                    if (times[1] < 10) {
                        s1 = "0" + times[1];
                    }

                    strjieshushijian = s + ":" + s1;
                    tv_jieshushijian.setText(strjieshushijian);


                }
            }).create();
        }

        timeDialog2.show();

    }



    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
		/*
		移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		联通：130、131、132、152、155、156、185、186
		电信：133、153、180、189、（1349卫通）
		总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		*/
        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }




    @Override
    public void onDialogItemClick(int requestCode, int position, String i) {
        item = i;
        item1 = i;
        item2 = i;
        item3 = i;

        if (StringUtils.isNotEmpty(item, true)) {
            if (1 == TYPE_TAG) {
                tv_danweijiancheng.setText(i);
                request(KEHUXINXI);
                request(JJROLES);

            } else if (2 == TYPE_TAG) {

                tv_chengcheyonghu.setText(i);
                stryongcherenId = mLinkHashMap1.get(item1)[0];

                request(PHONE);

            } else if (3 == TYPE_TAG) {

                str_wangfanliusu = position + 1 + "";

                tv_wangfanliusu.setText(i);

            } else if (4 == TYPE_TAG) {


                tv_lianxidianhua.setText(i);


            } else if (5 == TYPE_TAG) {


                str_yewuleibie = position + 1 + "";

                tv_yewuleibie.setText(i);


            } else if (6 == TYPE_TAG) {


                str_jiesuanfangshi = position + 1 + "";

                tv_jiesuanfangshi.setText(i);

            }else if(7 == TYPE_TAG){


                str_suoxuchexing = position + 1 + "";

                tv_suoxuchexing.setText(i);


            }else if(8 == TYPE_TAG){


                str_zhidingcheliang = position + 1 + "";

                tv_zhidingcheliangdengji.setText(i);


            }else if(9 == TYPE_TAG){


                str_baobibizhong = position + 1 + "";

                tv_baobibizhong.setText(i);


            }else if(10 == TYPE_TAG){


                str_daishoubizhong = position + 1 + "";

                tv_daishoubizhong.setText(i);


            }else if(11 == TYPE_TAG){

                tv_jijiaguize.setText(i);


            }else if(100 == TYPE_TAG){

                str_yidilousu = position  + "";

                tv_yidiliusu.setText(i);


            }else if(101 == TYPE_TAG){

                str_kuanjingcheliang = position  + "";

                tv_kuanjincheliang.setText(i);


            }else if(102 == TYPE_TAG){

                str_zizhiqiandan = position  + "";

                tv_zhiziqiandan.setText(i);

            }

        }

    }

    private void showSubmitDialog() {
        dialogView = LayoutInflater.from(NewOrdersActivity2.this).inflate(R.layout.dialog_make_order_by_picture, null);
        sureBtn = (Button) dialogView.findViewById(R.id.sureBtn);
        cancleBtn = (Button) dialogView.findViewById(R.id.cancleBtn);
        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo  立即体验
                startActivity(new Intent(NewOrdersActivity2.this,OrderMakeByPictureActivity.class));
                inputDialog.dismiss();
                finish();

            }
        });
        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo 手动下单
                inputDialog.dismiss();
            }
        });

        inputDialog = new AlertDialog.Builder(NewOrdersActivity2.this)
                .setView(dialogView)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        //todo
                    }}).setCancelable(false).create();
        inputDialog.show();
    }


}
