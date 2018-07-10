package com.saas.saasuser.view.stickylistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saas.saasuser.R;


public class ListViewFooter extends LinearLayout {

    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_LOADING = 2;
    private Context mContext;
    private View mContentView;
    private View mProgressBar;
    private TextView mFootTextView;

    public ListViewFooter(Context context) {
        super(context);
        initView(context);
    }
    public ListViewFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    //下拉状态处理
    public void setState(int state) {
        mFootTextView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.GONE);
        if (state == STATE_READY) {
            mFootTextView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            mFootTextView.setText(R.string.pull_footer_hint_ready);
        } else if (state == STATE_LOADING) {
            mProgressBar.setVisibility(View.VISIBLE);
            mFootTextView.setVisibility(View.VISIBLE);
        } else {
            mFootTextView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
            mFootTextView.setText(R.string.pull_footer_hint_normal);
        }
    }

    public void setBottomMargin(int height) {
        if (height < 0) return ;
        LayoutParams lp = (LayoutParams)mContentView.getLayoutParams();
        lp.bottomMargin = height;
        mContentView.setLayoutParams(lp);
    }

    public int getBottomMargin() {
        LayoutParams lp = (LayoutParams)mContentView.getLayoutParams();
        return lp.bottomMargin;
    }

    public void normal() {
        mFootTextView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }


    public void loading() {
        mFootTextView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void hide() {
        LayoutParams lp = (LayoutParams)mContentView.getLayoutParams();
        lp.height = 0;
        mContentView.setLayoutParams(lp);
    }


    public void show() {
        LayoutParams lp = (LayoutParams)mContentView.getLayoutParams();
        lp.height = LayoutParams.WRAP_CONTENT;
        mContentView.setLayoutParams(lp);
    }

    private void initView(Context context) {
        mContext = context;
        LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_listview_footer, null);
        addView(moreView);
        moreView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        mContentView = moreView.findViewById(R.id.footer_view);
        mProgressBar = moreView.findViewById(R.id.footer_progressbar);
        mFootTextView = (TextView)moreView.findViewById(R.id.footer_textview);
    }
    public TextView getmFootTextView() {
        return mFootTextView;
    }
}
