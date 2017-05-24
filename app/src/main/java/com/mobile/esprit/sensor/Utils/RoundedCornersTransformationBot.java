package com.mobile.esprit.sensor.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import com.squareup.picasso.Transformation;

/**
 * Created by Gadour on 25/03/2017.
 */

public class RoundedCornersTransformationBot implements Transformation {

    public enum CornerType {
        BOTTOM,
    }

    private int mRadius;
    private int mDiameter;
    private int mMargin;
    private RoundedCornersTransformationBot.CornerType mCornerType;


    public RoundedCornersTransformationBot(int radius, int margin) {
        this(radius, margin, CornerType.BOTTOM);
    }


    public RoundedCornersTransformationBot(int radius, int margin, RoundedCornersTransformationBot.CornerType cornerType) {
        mRadius = radius;
        mDiameter = radius * 2;
        mMargin = margin;
        mCornerType = cornerType;
    }

    @Override public Bitmap transform(Bitmap source) {

        int width = source.getWidth();
        int height = source.getHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        drawRoundRect(canvas, paint, width, height);
        source.recycle();

        return bitmap;
    }

    private void drawRoundRect(Canvas canvas, Paint paint, float width, float height) {
        float right = width - mMargin;
        float bottom = height - mMargin;

        switch (mCornerType) {
            case BOTTOM:
                drawBottomRoundRect(canvas, paint, right, bottom);
                break;
            default:
                canvas.drawRoundRect(new RectF(mMargin, mMargin, right, bottom), mRadius, mRadius, paint);
                break;
        }
    }

    private void drawBottomRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(mMargin, bottom - mDiameter, right, bottom), mRadius, mRadius,
                paint);
        canvas.drawRect(new RectF(mMargin, mMargin, right, bottom - mRadius), paint);
    }

    @Override public String key() {
        return "RoundedTransformation(radius=" + mRadius + ", margin=" + mMargin + ", diameter="
                + mDiameter + ", cornerType=" + mCornerType.name() + ")";
    }
}

