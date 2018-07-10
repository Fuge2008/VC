package com.saas.saasuser.view.snowfall;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


final class Snowflake {

    private Params params;
    private int size = 0;
    private int alpha = 255;
    private Bitmap bitmap = null;
    private double speedX = 0.0;
    private double speedY = 0.0;
    private double positionX = 0.0;
    private double positionY = 0.0;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Randomizer randomizer = new Randomizer();

    Snowflake(Params params) {
        this.params = params;
        init();
    }

    void init() {
        paint.setColor(Color.rgb(255, 255, 255));
        paint.setStyle(Paint.Style.FILL);
        reset(null);
    }

    void reset(Double positionY) {
        size = randomizer.randomInt(params.sizeMinInPx, params.sizeMaxInPx, true);
        if (params.image != null) {
            bitmap = Bitmap.createScaledBitmap(params.image, size, size, false);
        }

        double speed = ((float) (size - params.sizeMinInPx) / (params.sizeMaxInPx - params.sizeMinInPx) *
                (params.speedMax - params.speedMin) + params.speedMin);
        double angle = Math.toRadians(randomizer.randomDouble(params.angleMax) * randomizer.randomSignum());
        speedX = speed * Math.sin(angle);
        speedY = speed * Math.cos(angle);

        alpha = randomizer.randomInt(params.alphaMin, params.alphaMax);
        paint.setAlpha(alpha);

        positionX = randomizer.randomDouble(params.parentWidth);
        if (positionY != null) {
            this.positionY = positionY;
        } else {
            this.positionY = randomizer.randomDouble(params.parentHeight);
            if (!params.alreadyFalling) {
                this.positionY = this.positionY - params.parentHeight - size;
            }
        }
    }

    void update() {
        positionX += speedX;
        positionY += speedY;
        if (positionY > params.parentHeight) {
            positionY = (double) -size;
            reset(positionY);
        }
        if (params.fadingEnabled) {
            paint.setAlpha((int) (alpha * ((float) (params.parentHeight - positionY) / params.parentHeight)));
        }
    }

    void draw(Canvas canvas) {
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, (float) positionX, (float) positionY, paint);
        } else {
            canvas.drawCircle((float) positionX, (float) positionY, (float) size, paint);
        }
    }

    static final class Params {
        Bitmap image;
        int parentWidth;
        int parentHeight;
        int alphaMin;
        int alphaMax;
        int angleMax;
        int sizeMinInPx;
        int sizeMaxInPx;
        int speedMin;
        int speedMax;
        boolean fadingEnabled;
        boolean alreadyFalling;
    }
}
