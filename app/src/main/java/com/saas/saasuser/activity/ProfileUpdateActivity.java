package com.saas.saasuser.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.saas.saasuser.R;
import com.saas.saasuser.application.MyApplication;
import com.saas.saasuser.global.NetIntentCallBackListener;
import com.saas.saasuser.util.KeyboardUtil;
import com.saas.saasuser.util.SharedPreferencesUtil;
import com.saas.saasuser.util.StringUtils;
import com.saas.saasuser.util.ToastUtils;
import com.saas.saasuser.util.Util;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 个人信息修改界面
 */
public class ProfileUpdateActivity extends BaseActivity implements NetIntentCallBackListener.INetIntentCallBack {
    private static String TAG = ProfileUpdateActivity.class.getSimpleName();
    public static final int TYPE_NICK = 0;
    public static final int TYPE_ADDRESS = 1;
    public static final int TYPE_SIGN = 2;
    @BindView(R.id.head_title)
    TextView headTitle;
    @BindView(R.id.btn_submit)
    TextView btnSubmit;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.tv_note)
    TextView tvNote;
    @BindView(R.id.et_info)
    EditText etInfo;

    private String defaultStr;

    private SharedPreferencesUtil util;
    private ProgressDialog progressDialog;
    private int MAX_COUNT = 30;
   
    private int type;
    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Intent intent = new Intent(ProfileUpdateActivity.this, ProfileDetailActivity.class);
                    intent.putExtra("value", etInfo.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        util = new SharedPreferencesUtil(this);
        Util.setHeadTitleMore(this, "更改信息", true);
        btnSubmit.setText("保存");
        type = getIntent().getIntExtra("type", 0);
        defaultStr = getIntent().getStringExtra("default");
        initUi();
        findViewById(R.id.head_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInServer(etInfo.getText().toString().trim());
                KeyboardUtil.closeKeybord(etInfo,ProfileUpdateActivity.this);
            }
        });

    }


    private void initUi() {

        etInfo.addTextChangedListener(mTextWatcher);
        etInfo.setSelection(etInfo.length()); // 将光标移动最后一个字符后面
        iv_back = (ImageView) findViewById(R.id.iv_back);
        if (type == 0) {
            MAX_COUNT = 16;
            tvCount.setText(MAX_COUNT + "");
        } else if (type == 1) {
            MAX_COUNT = 20;
            tvCount.setText(MAX_COUNT + "");
        } else if (type == 2) {
            MAX_COUNT = 30;
            tvCount.setText(MAX_COUNT + "");
        }
        if (defaultStr != null) {

            etInfo.setText(defaultStr);
        }
        initView(type, headTitle, btnSubmit, etInfo);
    }


    /**
     * 监听统计字数
     */
    private TextWatcher mTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
            int editStart = etInfo.getSelectionStart();
            int editEnd = etInfo.getSelectionEnd();

            // 先去掉监听器，否则会出现栈溢出
            etInfo.removeTextChangedListener(mTextWatcher);

            // 注意这里只能每次都对整个EditText的内容求长度，不能对删除的单个字符求长度
            // 因为是中英文混合，单个字符而言，calculateLength函数都会返回1
            if (calculateLength(s.toString()) >= MAX_COUNT) {
                ToastUtils.showShortToast(ProfileUpdateActivity.this, "字数已达" + MAX_COUNT);
            }

            while (calculateLength(s.toString()) > MAX_COUNT) { // 当输入字符个数超过限制的大小时，进行截断操作
                s.delete(editStart - 1, editEnd);
                editStart--;
                editEnd--;
            }
            etInfo.setText(s);
            etInfo.setSelection(editStart);

            // 恢复监听器
            etInfo.addTextChangedListener(mTextWatcher);

            setLeftCount();
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

    };

    /**
     * 计算分享内容的字数
     */
    private long calculateLength(CharSequence c) {
        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            int tmp = (int) c.charAt(i);
//            if (tmp > 0 && tmp < 127) {
//                len += 0.5;
//            } else {
            len++;
        }
//        }
        return Math.round(len);
    }

    /**
     * 刷新剩余输入字数,最大值100个字
     */
    private void setLeftCount() {
        tvCount.setText(String.valueOf((MAX_COUNT - getInputCount())));
    }

    /**
     * 获取用户输入的内容字数
     */
    private long getInputCount() {
        return calculateLength(etInfo.getText().toString());
    }

    private void initView(int type, TextView headTitle, TextView btnSubmit, final EditText etInfo) {
        String title = "";
        switch (type) {
            case TYPE_NICK:
                title = "修改昵称";
                break;
            case TYPE_ADDRESS:
                title = "修改地址";
                break;
            case TYPE_SIGN:
                title = "修改个人签名";
                break;
        }
        headTitle.setText(title);


    }

    private void updateInServer(String value) {
        if (TextUtils.isEmpty(value) || ((defaultStr != null) && value.equals(defaultStr))) {
            return;
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在更新...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        if (type == 2 & StringUtils.isNotEmpty(etInfo.getText().toString(), true)) {
            util.setMotto(etInfo.getText().toString());
            handler.sendEmptyMessage(0);
           // NetIntent netIntent = new NetIntent();
//            netIntent.client_updateMottoById(util.getUserId(), etInfo.getText().toString(), new NetIntentCallBackListener(this));
        } else if (type == 0 & StringUtils.isNotEmpty(etInfo.getText().toString(), true)) {
            util.setNickName(etInfo.getText().toString());
           // NetIntent netIntent = new NetIntent();
//            netIntent.client_updateUserById(util.getUserId(), "", etInfo.getText().toString(),  "","", "", "","",new NetIntentCallBackListener(this));
            handler.sendEmptyMessage(0);
        } else if (type == 1 & StringUtils.isNotEmpty(etInfo.getText().toString(), true)) {
            util.setAddress(etInfo.getText().toString());

            finish();
        }

        progressDialog.dismiss();

    }

    @Override
    public void onError(Request request, Exception e) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

    }

    @Override
    public void onResponse(String response) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getBoolean("code")) {
                handler.sendEmptyMessage(0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        KeyboardUtil.closeKeybord(etInfo,ProfileUpdateActivity.this);//返回的时候关闭键盘

    }
}