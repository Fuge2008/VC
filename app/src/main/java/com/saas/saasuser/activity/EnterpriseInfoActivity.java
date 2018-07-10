package com.saas.saasuser.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lljjcoder.city_20170724.CityPickerView;
import com.lljjcoder.city_20170724.bean.CityBean;
import com.lljjcoder.city_20170724.bean.DistrictBean;
import com.lljjcoder.city_20170724.bean.ProvinceBean;
import com.saas.saasuser.R;
import com.saas.saasuser.application.MyApplication;
import com.saas.saasuser.bean.EnterpriseRegisterDetailBean;
import com.saas.saasuser.global.Constants;
import com.saas.saasuser.util.LogUtils;
import com.saas.saasuser.util.SharedPreferencesUtil;
import com.saas.saasuser.util.ToastUtils;
import com.saas.saasuser.view.MaterialDialog;
import com.saas.saasuser.view.SuccessMaterialDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * 作者：张毅阳 on 2017/10/21 11:58
 */

public class EnterpriseInfoActivity extends BaseActivity implements  View.OnClickListener{
    private EditText en_shortname;
    private Spinner spinner_industry;//行业
    private Spinner spinner_industrycategory;//行业细分
    private TextView tv_location;
    private Spinner spinner_tenantrycategory;//租行
    private TextView tv_enterpriseinfo_instructions;
    private Button btn_enter_info_register;

    private String mProvince;
    private String mCity;
    private String mCounty;

    //intent传入参数
    private String mPhoneNum;
    private int iType;
    private String str_mAdminName;
    private String mSetPwd;
    private String mInvCode;
    private String mEnterShortName;

    //全局的jsonObject
    private JSONObject mJSONObject;//把全行业的信息以json的格式保存
    private String[] allIndustry;//所有的行业

    private ArrayAdapter<String> industryAdapter;//行业数据适配器
    private ArrayAdapter<String> industryCategoryAdapter;//行业种类细分适配器
    private ArrayAdapter<String> tenantryCategoryAdapter;

    private String[] allIndustryList;//在spinner中选出来的行业，后面需要用空格隔开
    private String[] mItems;

    private String industry;//用来接收intent的参数
    private String allIndustrys;//用来接收intent参数


    private String industryName;//行业的名字
    private String industryCategoryName;//细分的名字
    private String tenantryCategoryName;//租车类型的名字
    private Boolean isFirstLoad = true;//判断是不是最近进入对话框

    //行业细分的集合
    private Map<String, String[]> industryMap = new HashMap<String, String[]>();
    private String mEnShortName;
    private Context mContext;

    private int tenantryType;
    private SharedPreferencesUtil mUtil;
    private TextView mEninstructions;
    private MaterialDialog mMaterialDialog;
    private SuccessMaterialDialog mSuccessMaterialDialog;
//    private CheckBox mEnchkselector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_enterpriseinfo);
        MyApplication.getInstance().addActivity(this);
        mContext = this;
        if (getIntent() != null){
            mPhoneNum = getIntent().getStringExtra("PhoneNum");
            str_mAdminName = getIntent().getStringExtra("Name");
            mSetPwd = getIntent().getStringExtra("Pwd");
            mInvCode = getIntent().getStringExtra("InviteCode");
            iType = getIntent().getIntExtra("iType", 1);
        }

        en_shortname = (EditText) findViewById(R.id.edit_en_shortname);
        tv_location = (TextView) findViewById(R.id.tv_location);
        tv_location.setOnClickListener(this);
        en_shortname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    hideSoftKeyboard(en_shortname, mContext);
                }
            }
        });

        mEninstructions = (TextView) findViewById(R.id.enterpriseinfo_instructions);
        mEninstructions.setOnClickListener(this);
//        mEnchkselector = (CheckBox) findViewById(R.id.en_chk_selector);

        initJsonData();//初始化json数据
        initDatas();//初始化数据
        initView();//初始化控件
        setSpinnerData();//为spinner设置值
    }

    /**
     * 隐藏软键盘
     * @param editText
     * @param context
     */
    private void hideSoftKeyboard(EditText editText, Context context) {
        if (editText != null && context != null) {
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }



    private void setLocationData() {
        CityPickerView cityPicker = new CityPickerView.Builder(EnterpriseInfoActivity.this)
                .textSize(20)
                .title("地址选择")
                .backgroundPop(0xa0000000)
                .titleBackgroundColor("#234Dfa")
                .titleTextColor("#4e69f4")
                .backgroundPop(0xa0000000)
                .confirTextColor("#000000")
                .cancelTextColor("#000000")
                .province("北京")
                .city("北京")
                .district("东城区")
                .textColor(Color.parseColor("#000000"))
                .provinceCyclic(true)
                .cityCyclic(false)
                .districtCyclic(false)
                .visibleItemsCount(7)
                .itemPadding(10)
                .onlyShowProvinceAndCity(false)
                .build();
        cityPicker.show();

        //监听方法，获取选择结果
        cityPicker.setOnCityItemClickListener(new CityPickerView.OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                //返回结果
                //ProvinceBean 省份信息
                //CityBean     城市信息
                //DistrictBean 区县信息
                tv_location.setText(province.getName() + city.getName() + district.getName());
                mProvince = province.getName();
                mCity = city.getName();
                mCounty = district.getName();
            }
            @Override
            public void onCancel() {

            }
        });
    }

    private void register() {

        mEnShortName = en_shortname.getText().toString().trim();

        if (TextUtils.isEmpty(mEnShortName)){
            ToastUtils.MyToast(mContext, "企业简称不能为空~", Toast.LENGTH_SHORT);
            return;
        }else {
            mEnterShortName = mEnShortName;
        }

        OkHttpUtils.post()
                .url(Constants.ENTERPRISEDETAIL)
                .addParams("PhoneNum", mPhoneNum)
                .addParams("Name", str_mAdminName)
                .addParams("Pwd", mSetPwd)
                .addParams("InviteCode", mInvCode)
                .addParams("Type", iType+"")
                .addParams("ShortName", mEnterShortName)
                .addParams("Industrial", spinner_industry.getSelectedItem()+ "")
                .addParams("Industry", spinner_industrycategory.getSelectedItem()+"")
                .addParams("Province", mProvince)
                .addParams("City", mCity)
                .addParams("Region", mCounty)
                .addParams("RentType", tenantryType + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.MyToast(mContext, "网络错误或服务器繁忙,请重新操作", Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response.toString() != null){
                            String detailString = response.toString();

//                            Log.d("love", "onResponse: " + detailString);

                            Gson gson = new Gson();

                            EnterpriseRegisterDetailBean detailBean = gson.fromJson(detailString, EnterpriseRegisterDetailBean.class);

                            Log.d("love", "onResponse: " + detailBean.getErrCode());

                            if (detailBean.getErrCode() == 0){
                                ToastUtils.MyToast(mContext, "注册失败,请重新注册", Toast.LENGTH_SHORT);
                                String errMsg = detailBean.getErrMsg();
                              /*  Log.d("love", "onResponse: " + errMsg.toString());

                                Log.d("love", "onResponse: " + mPhoneNum);
                                Log.d("love", "onResponse: " + str_mAdminName);
                                Log.d("love", "onResponse: " + mSetPwd);
                                Log.d("love", "onResponse: " + mInvCode);
                                Log.d("love", "onResponse: " + iType+"");
                                Log.d("love", "onResponse: " + mEnterShortName);

                                Log.d("love", "onResponse: " + spinner_industry.getSelectedItem()+ "");
                                Log.d("love", "onResponse: " + spinner_industrycategory.getSelectedItem()+"");
                                Log.d("love", "onResponse: " + mProvince);
                                Log.d("love", "onResponse: " + mCity);
                                Log.d("love", "onResponse: " + mCounty);
                                Log.d("love", "onResponse: " + tenantryType + "");*/


                                LogUtils.d(errMsg);
                            }else if (detailBean.getErrCode() == 1){

                               /* ToastUtils.MyToast(mContext, "注册成功~", Toast.LENGTH_SHORT);
                                Intent intent = new Intent(mContext, LoginActivity.class);
                                startActivity(intent);*/
                                successShowDialogs();
                            }
                        }

                    }
                });


    }

    private void successShowDialogs() {
        mSuccessMaterialDialog = new SuccessMaterialDialog(this)
                .setContentView(
                        R.layout.custom_success_message_contentone);
        mSuccessMaterialDialog.setPositiveButton("我要登录", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            }
        });
        mSuccessMaterialDialog.show();

        Window window = mSuccessMaterialDialog.mAlertDialog.getWindow();
        WindowManager m = this.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.45); // 高度设置为屏幕的0.6，根据实际情况调整
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.65，根据实际情况调整
        window.setAttributes(p);
    }

    private void onInstructions() {
        /*  .setContentIV(
                  R.drawable.gantanhao)*/
        mMaterialDialog = new MaterialDialog(this)
                      /*  .setContentIV(
                                R.drawable.gantanhao)*/
                .setContentView(
                        R.layout.custom_car_message_content);
        mMaterialDialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
            }
        });
        mMaterialDialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
            }
        });
        mMaterialDialog.show();

        Window window = mMaterialDialog.mAlertDialog.getWindow();
        WindowManager m = this.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高度
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.49); // 高度设置为屏幕的0.6，根据实际情况调整
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.65，根据实际情况调整
        window.setAttributes(p);
    }

    private void setSpinnerData() {
        int selectPosition = 0;//有数据传入时

        industry = getIntent().getStringExtra("industry");
        allIndustrys = getIntent().getStringExtra("allIndustrys");

        if (industry != null && !industry.equals("") && allIndustrys != null && !allIndustrys.equals("")) {
            allIndustryList = industry.split(" ");//用空格隔开allSpinList地址
        }

        /**
         * 设置行业的适配器,进行动态设置
         */
        industryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);//系统默认的
        for (int i = 0; i < allIndustry.length; i++) {
            //给spinner省赋值,设置默认值
            if (industry != null && !industry.equals("") && allIndustrys != null && !allIndustrys.equals("")&& allIndustryList.length > 0 && allIndustryList[0].equals(allIndustry[i])) {
                selectPosition = i;
            }
            industryAdapter.add(allIndustry[i]);
        }
        //行业
        industryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//按下的效果
        spinner_industry.setAdapter(industryAdapter);
        spinner_industry.setSelection(selectPosition);//设置选中的行业，默认

        //行业细分
        industryCategoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        industryCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_industrycategory.setAdapter(industryCategoryAdapter);

        //租行类别
        tenantryCategoryAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
        tenantryCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tenantrycategory.setAdapter(tenantryCategoryAdapter);

        //设置spinner的点击监听
        setIndustryListener();
        setTenantryListener();


    }

    private void setTenantryListener() {
        spinner_tenantrycategory.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (mInputMethodManager.isActive()) {
                    mInputMethodManager.hideSoftInputFromWindow(en_shortname.getWindowToken(), 0);// 隐藏输入法
                }
                return false;
            }
        });
        spinner_tenantrycategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                tenantryCategoryName = parent.getSelectedItem() + "";
                if (tenantryCategoryName.equals("租行使用-所有企业")){
                    tenantryType = 1;
                }else if (tenantryCategoryName.equals("租行使用-所有企业")){
                    tenantryType = 2;
                }else if (tenantryCategoryName.equals("租行协助-汽车产业相关")){
                    tenantryType = 3;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setIndustryListener() {

        spinner_industry.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (mInputMethodManager.isActive()) {
                    mInputMethodManager.hideSoftInputFromWindow(en_shortname.getWindowToken(), 0);// 隐藏输入法
                }
                return false;
            }
        });
        spinner_industry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                industryName = parent.getSelectedItem() + "";
                if (!isFirstLoad) {
                    updateIndustry(industryName + "", null);
                } else {
                    updateIndustry(industryName + "", null);
                }
                isFirstLoad = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_industrycategory.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (mInputMethodManager.isActive()) {
                    mInputMethodManager.hideSoftInputFromWindow(en_shortname.getWindowToken(), 0);// 隐藏输入法
                }
                return false;
            }
        });
        spinner_industrycategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                industryCategoryName = parent.getSelectedItem() + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void updateIndustry(Object object, Object myIndustry) {
        boolean isArea = false;
        int selectPosition = 0;
        String[] industryCategory = industryMap.get(object);
        industryCategoryAdapter.clear();//清空
        if (industryCategory != null) {
            for (int i = 0; i < industryCategory.length; i++) {
                if (myIndustry != null && myIndustry.toString().equals(industryCategory[i])) {//去集合中匹配
                    selectPosition = i;
                    isArea = true;//地区
                }
                industryCategoryAdapter.add(industryCategory[i]);//填入到这个列表

            }
            industryCategoryAdapter.notifyDataSetChanged();//刷新
            spinner_industrycategory.setSelection(selectPosition);//默认选中
        }
    }

    //初始化控件
    private void initView() {

        spinner_industry = (Spinner) findViewById(R.id.industry);
        spinner_industrycategory = (Spinner) findViewById(R.id.industrycategory);
        spinner_tenantrycategory = (Spinner) findViewById(R.id.tenantrycategory);

        tv_enterpriseinfo_instructions = (TextView) findViewById(R.id.enterpriseinfo_instructions);
        tv_enterpriseinfo_instructions.setOnClickListener(this);

        btn_enter_info_register = (Button) findViewById(R.id.enter_info_register);
        btn_enter_info_register.setOnClickListener(this);
    }

    //初始化数据
    private void initDatas() {
        //企业简称
        if (iType == 2){
            if (getIntent() != null) {
                Log.d("yangyan", "initDatas: " + getIntent().getStringExtra("ShortName"));
                en_shortname.setText(getIntent().getStringExtra("ShortName"));
                mEnterShortName = getIntent().getStringExtra("ShortName");
            }
        }else if (iType == 1){
            mEnShortName = en_shortname.getText().toString().trim();

        }
        //行业
        try {
            JSONArray array = mJSONObject.getJSONArray("industrylist");
            allIndustry = new String[array.length()];
            for (int j = 0; j < array.length(); j++) {
                JSONObject jsonName = array.getJSONObject(j);//jsonArray转jsonObject
                String induStr = jsonName.getString("name");//获取所有的行业
                allIndustry[j] = induStr;//封装所有的行业
                JSONArray arrayCity = null;
                try {
                    arrayCity = jsonName.getJSONArray("city");//在所有的行业中取细分，转jsonArray
                } catch (Exception e) {
                    continue;
                }

                String[] allCategoryList = new String[arrayCity.length()];//所有行业细分的长度
                for (int k = 0; k < arrayCity.length(); k++) {
                    JSONObject jsonCategory = arrayCity.getJSONObject(k);//转jsonObject
                    String CategoryStr = jsonCategory.getString("category");//取出所有的细分
                    allCategoryList[k] = CategoryStr;//封装市集合
                }
                industryMap.put(induStr, allCategoryList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mJSONObject = null;//清空数据

        //租车类别
        mItems = getResources().getStringArray(R.array.tenantry);

    }


    private void initJsonData() {

        //行业
        try {
            StringBuffer buffer = new StringBuffer();
            InputStream inputStream = getAssets().open("lus.json");
            byte[] bytes = new byte[1024];
            int length = -1;
            while ((length = inputStream.read(bytes)) != -1){
                buffer.append(new String(bytes, 0, length, "utf-8"));
            }
            inputStream.close();
            mJSONObject = new JSONObject(buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.tv_location:
                hideSoftKeyboard(en_shortname, mContext);
                setLocationData();
                break;

            case R.id.enterpriseinfo_instructions: //租行类别说明
                onInstructions();
                break;

            case R.id.enter_info_register:
//                if (mEnchkselector.isChecked() && ClickUtils.isFastClick()) {
                register();
//                }else {
//                    ToastUtils.MyToast(mContext, "请点击确定VV服务条款", Toast.LENGTH_SHORT);
//                }
                break;

        }
    }

   /* @Override
    protected void onDestroy() {
        super.onDestroy();
        mMaterialDialog.dismiss();
        mSuccessMaterialDialog.dismiss();
    }*/
}
