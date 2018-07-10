//package com.saas.saasuser.activity;
//
//import android.os.Bundle;
//import android.widget.TextView;
//
//import com.apkfuns.logutils.LogUtils;
//import com.google.gson.Gson;
//import com.gyf.barlibrary.ImmersionBar;
//import com.saas.saasuser.R;
//import com.saas.saasuser.application.MyApplication;
//import com.saas.saasuser.async.HttpException;
//import com.saas.saasuser.async.HttpOkUtils;
//import com.saas.saasuser.bean.SpecialIvotesInfo;
//import com.saas.saasuser.global.Constants;
//import com.saas.saasuser.util.SharedPreferencesUtil;
//
//import java.io.IOException;
//import java.util.HashMap;
//
//import cn.sharesdk.onekeyshare.OnekeyShare;
//
///**
// * Created by tanlin on 2017/11/28.
// */
//
//public class VATSpecialVotesActivity extends BaseActivity3 {
//
//    private TextView tv_name_company;
//    private TextView tv_enterprise_id_number;
//    private TextView tv_bank;
//    private TextView tv_bank_account;
//    private TextView tv_contact_phone_number;
//    private TextView tv_adress;
//
//    private final int ZENGZHIFAPIAO = 1005;
//    private SharedPreferencesUtil util;
//
//    SpecialIvotesInfo.DataBean gsjcList;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_special_votes);
//        MyApplication.getInstance().addActivity(this);
//        setTitle("增值税专用票");
//
//        ImmersionBar.with(this).statusBarColor(R.color.status_color)
//                .fitsSystemWindows(true).init();
//
//        btn_left.setImageResource(R.drawable.icon_back);
//        btn_right.setImageResource(R.drawable.encard_share);
//
//        gsjcList=new SpecialIvotesInfo.DataBean();
//
//        tv_name_company= (TextView) findViewById(R.id.name_company);
//        tv_enterprise_id_number= (TextView) findViewById(R.id.enterprise_id_number);
//        tv_bank= (TextView) findViewById(R.id.bank);
//        tv_bank_account= (TextView) findViewById(R.id.bank_account);
//        tv_contact_phone_number= (TextView) findViewById(R.id.contact_phone_number);
//        tv_adress= (TextView) findViewById(R.id.adress);
//
//
//        util = new SharedPreferencesUtil(this);
//        request(ZENGZHIFAPIAO);
//    }
//
//    @Override
//    public void onSuccess(int requestCode, Object result) {
//        super.onSuccess(requestCode, result);
//
//        switch (requestCode){
//
//            case ZENGZHIFAPIAO:
//
//                String ss= (String) result;
//
//                gsjcList = new Gson().fromJson(ss,SpecialIvotesInfo.class).getData();
//
//
//                tv_name_company.setText(gsjcList.getInvoiceTitle()==null ? null:gsjcList.getInvoiceTitle()+"");
//
//                tv_enterprise_id_number.setText(gsjcList.getComDuty()==null ? null:gsjcList.getComDuty()+"");
//
//                tv_bank.setText(gsjcList.getBank()==null ? null:gsjcList.getBank()+"");
//                tv_bank_account.setText(gsjcList.getBankAccount()==null ? null:gsjcList.getBankAccount()+"");
//                tv_contact_phone_number.setText(gsjcList.getRegLoginPhone()==null ? null:gsjcList.getRegLoginPhone()+"");
//                tv_adress.setText(gsjcList.getRegAddress()==null ? null:gsjcList.getRegAddress()+"");
//        }
//
//
//
//    }
//
//    @Override
//    public void onFailure(int requestCode, int state, Object result) {
//        super.onFailure(requestCode, state, result);
//    }
//
//    @Override
//    public Object doInBackground(int requestCode) throws HttpException, IOException {
//
//        switch (requestCode) {
//            case ZENGZHIFAPIAO:
//                HashMap<String, String> map = new HashMap<>();
//                map.put("companyID", util.getCompanyId());
//                String result = HttpOkUtils.post(Constants.HttpRoot + Constants.QIYEFAPIAO, map);
//
//                LogUtils.tag("companyID").d(result);
//                return result;
//        }
//        return super.doInBackground(requestCode);
//    }
//
//    @Override
//    public void checkRight() {
//        super.checkRight();
//
//        showShare();
//
//    }
//    private void showShare() {
//        OnekeyShare oks = new OnekeyShare();
//        //关闭sso授权
//        oks.disableSSOWhenAuthorize();
//
//        // 分享时Notification的图标和文字  2.5.9以后的版本不     调用此方法
//        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
//        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//        oks.setTitle("VV租行即将颠覆出租车行业1");
//        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//        oks.setTitleUrl("http://www.vv-che.com/");
//        // text是分享文本，所有平台都需要这个字段
//        oks.setText("VV租行即将颠覆出租车行业2");
//        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("http://p1.so.qhmsg.com/bdr/326__/t01c68128cae7585c06.jpg");//确保SDcard下面存在此张图片
//        // url仅在微信（包括好友和朋友圈）中使用
//
//        String mm=util.getCompanyId();
//        String str= Constants.ImageHttpRoot+"/AppComInfo/EnterpriseInvoice?companyId="+mm+"&InvoiceType=0";
//        oks.setUrl(str);
//
//        // oks.setUrl("http://www.vv-che.com/");
//        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("VV租行即将颠覆出租车行业3");
//        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite("VV租行");
//        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//        oks.setSiteUrl("http://www.vv-che.com/");
//
//        // 启动分享GUI
//        oks.show(this);
//    }
//
//
//
//
//}
