package com.saas.saasuser.activity;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.saas.saasuser.R;
import com.saas.saasuser.application.MyApplication;
import com.saas.saasuser.util.ToastUtils;
import com.saas.saasuser.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.saas.saasuser.application.MyApplication.mContext;

/**
 * 意见反馈
 */
public class FeedBackActivity extends BaseActivity {


    @BindView(R.id.btn_submit)
    TextView btnSubmit;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.et_info)
    EditText etInfo;
    private int MAX_COUNT = 300;
    private String type_opinion = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        ButterKnife.bind(this);
        MyApplication.getInstance().addActivity(this);
        mContext = this;
        Util.setHeadTitleMore(this, "反馈意见", true);
        countWord();
    }


    private void countWord() {
        etInfo.addTextChangedListener(mTextWatcher);
        etInfo.setSelection(etInfo.length()); // 将光标移动最后一个字符后面
        tvCount.setText(MAX_COUNT + "");

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
                ToastUtils.showShortToast(FeedBackActivity.this, "字数已达" + MAX_COUNT);
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
     *
     * @param c
     * @return
     */
    private long calculateLength(CharSequence c) {
        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            int tmp = (int) c.charAt(i);
//            if (tmp > 0 && tmp < 127) {
//                len += 0.5;
//            } else {
            len++;
//            }
        }
        return Math.round(len);
    }

    /**
     * 刷新剩余输入字数,最大值300个字
     */
    private void setLeftCount() {
        tvCount.setText(String.valueOf((MAX_COUNT - getInputCount())));
    }

    /**
     * 获取用户输入的内容字数
     *
     * @return
     */
    private long getInputCount() {
        return calculateLength(etInfo.getText().toString());
    }





    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        ToastUtils.showLongToastSafe(FeedBackActivity.this,"你的疑问已经提交，稍后工作人员将会联系您");
        finish();
    }
}
