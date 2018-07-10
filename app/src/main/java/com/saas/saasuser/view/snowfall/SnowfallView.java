package com.saas.saasuser.view.snowfall;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.saas.saasuser.R;

import java.util.ArrayList;
import java.util.List;


public class SnowfallView extends View {

    private static final int DEFAULT_SNOWFLAKES_NUM = 300;
    private static final int DEFAULT_SNOWFLAKE_ALPHA_MIN = 150;
    private static final int DEFAULT_SNOWFLAKE_ALPHA_MAX = 250;
    private static final int DEFAULT_SNOWFLAKE_ANGLE_MAX = 10;
    private static final int DEFAULT_SNOWFLAKE_SIZE_MIN_IN_DP = 6;
    private static final int DEFAULT_SNOWFLAKE_SIZE_MAX_IN_DP = 20;
    private static final int DEFAULT_SNOWFLAKE_SPEED_MIN = 2;
    private static final int DEFAULT_SNOWFLAKE_SPEED_MAX = 12;
    private static final boolean DEFAULT_SNOWFLAKES_FADING_ENABLED = false;
    private static final boolean DEFAULT_SNOWFLAKES_ALREADY_FALLING = false;

    private Bitmap snowflakeImage;
    private int snowflakesNum;
    private int snowflakeAlphaMin;
    private int snowflakeAlphaMax;
    private int snowflakeAngleMax;
    private int snowflakeSizeMinInPx;
    private int snowflakeSizeMaxInPx;
    private int snowflakeSpeedMin;
    private int snowflakeSpeedMax;
    private boolean snowflakesFadingEnabled;
    private boolean snowflakesAlreadyFalling;

    private List<Snowflake> snowflakes;
    private UpdateSnowflakesThread updateSnowflakesThread;

    public SnowfallView(Context context) {
        this(context, null);
    }

    public SnowfallView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SnowfallView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SnowfallView);
        try {
            snowflakesNum = typedArray.getInt(R.styleable.SnowfallView_snowflakesNum, DEFAULT_SNOWFLAKES_NUM);
            snowflakeImage = DrawableHelper.toBitmap(typedArray.getDrawable(R.styleable.SnowfallView_snowflakeImage));
            snowflakeAlphaMin = typedArray.getInt(R.styleable.SnowfallView_snowflakeAlphaMin, DEFAULT_SNOWFLAKE_ALPHA_MIN);
            snowflakeAlphaMax = typedArray.getInt(R.styleable.SnowfallView_snowflakeAlphaMax, DEFAULT_SNOWFLAKE_ALPHA_MAX);
            snowflakeAngleMax = typedArray.getInt(R.styleable.SnowfallView_snowflakeAngleMax, DEFAULT_SNOWFLAKE_ANGLE_MAX);
            snowflakeSizeMinInPx = typedArray.getDimensionPixelSize(R.styleable.SnowfallView_snowflakeSizeMin, dpToPx(DEFAULT_SNOWFLAKE_SIZE_MIN_IN_DP));
            snowflakeSizeMaxInPx = typedArray.getDimensionPixelSize(R.styleable.SnowfallView_snowflakeSizeMax, dpToPx(DEFAULT_SNOWFLAKE_SIZE_MAX_IN_DP));
            snowflakeSpeedMin = typedArray.getInt(R.styleable.SnowfallView_snowflakeSpeedMin, DEFAULT_SNOWFLAKE_SPEED_MIN);
            snowflakeSpeedMax = typedArray.getInt(R.styleable.SnowfallView_snowflakeSpeedMax, DEFAULT_SNOWFLAKE_SPEED_MAX);
            snowflakesFadingEnabled = typedArray.getBoolean(R.styleable.SnowfallView_snowflakesFadingEnabled, DEFAULT_SNOWFLAKES_FADING_ENABLED);
            snowflakesAlreadyFalling = typedArray.getBoolean(R.styleable.SnowfallView_snowflakesAlreadyFalling, DEFAULT_SNOWFLAKES_ALREADY_FALLING);
        } finally {
            typedArray.recycle();
        }

        updateSnowflakesThread = new UpdateSnowflakesThread();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        snowflakes = createSnowflakes();
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (changedView == this && visibility == GONE) {
            for (Snowflake snowflake : snowflakes) {
                snowflake.reset(null);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode()) {
            return;
        }
        for (Snowflake snowflake : snowflakes) {
            snowflake.draw(canvas);
        }
        updateSnowflakes();
    }

    private void updateSnowflakes() {
        updateSnowflakesThread.handler.post(new Runnable() {
            @Override
            public void run() {
                for (Snowflake snowflake : snowflakes) {
                    snowflake.update();
                }
                postInvalidateOnAnimation();
            }
        });
    }

    private List<Snowflake> createSnowflakes() {
        Snowflake.Params snowflakeParams = new Snowflake.Params();
        snowflakeParams.parentWidth = getWidth();
        snowflakeParams.parentHeight = getHeight();
        snowflakeParams.image = snowflakeImage;
        snowflakeParams.alphaMin = snowflakeAlphaMin;
        snowflakeParams.alphaMax = snowflakeAlphaMax;
        snowflakeParams.angleMax = snowflakeAngleMax;
        snowflakeParams.sizeMinInPx = snowflakeSizeMinInPx;
        snowflakeParams.sizeMaxInPx = snowflakeSizeMaxInPx;
        snowflakeParams.speedMin = snowflakeSpeedMin;
        snowflakeParams.speedMax = snowflakeSpeedMax;
        snowflakeParams.fadingEnabled = snowflakesFadingEnabled;
        snowflakeParams.alreadyFalling = snowflakesAlreadyFalling;
        List<Snowflake> snowflakes = new ArrayList<>(snowflakesNum);
        for (int i = 0; i < snowflakesNum; i++) {
            snowflakes.add(new Snowflake(snowflakeParams));
        }
        return snowflakes;
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    private static final class UpdateSnowflakesThread extends HandlerThread {

        private Handler handler = new Handler(Looper.myLooper());

        UpdateSnowflakesThread() {
            super("SnowflakesComputations");
        }

    }
}
