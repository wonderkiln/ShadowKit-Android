package com.flurgle.shadowkit;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class ShadowLayout extends FrameLayout {

    private static final float PERCENT_PADDING = 0.10f;

    private static final int DEFAULT_SHADOW_DX = 0;
    private static final int DEFAULT_SHADOW_DY = 0;
    private static final int DEFAULT_SHADOW_COLOR = Color.argb(0, 0, 0, 0);
    private static final float DEFAULT_SHADOW_ALPHA = 0.66f;
    private static final int DEFAULT_SHADOW_RADIUS = 0;

    private int mShadowDx;
    private int mShadowDy;
    private int mShadowColor;
    private float mShadowAlpha;
    private int mShadowRadius;

    private ImageView mImageView;

    public ShadowLayout(Context context) {
        this(context, null);
    }

    public ShadowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ShadowLayout,
                0, 0);

        try {
            mShadowDx = a.getDimensionPixelSize(R.styleable.ShadowLayout_skShadowDx, DEFAULT_SHADOW_DX);
            mShadowDy = a.getDimensionPixelSize(R.styleable.ShadowLayout_skShadowDy, DEFAULT_SHADOW_DY);
            mShadowColor = a.getColor(R.styleable.ShadowLayout_skShadowColor, DEFAULT_SHADOW_COLOR);
            mShadowAlpha = a.getFloat(R.styleable.ShadowLayout_skShadowAlpha, DEFAULT_SHADOW_ALPHA);
            mShadowRadius = a.getInteger(R.styleable.ShadowLayout_skShadowRadius, DEFAULT_SHADOW_RADIUS);
        } finally {
            a.recycle();
        }

        mImageView = new ImageView(context);
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        mImageView.setScaleX(PERCENT_PADDING * 2 + 1);
        mImageView.setScaleY(PERCENT_PADDING * 2 + 1);
        addView(mImageView);

        setShadowDx(mShadowDx);
        setShadowDy(mShadowDy);
        setShadowColor(mShadowColor);
        setShadowAlpha(mShadowAlpha);
        setShadowRadius(mShadowRadius);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            try {
                Bitmap bitmap = ShadowKit.getBitmapForView(this, PERCENT_PADDING);
                bitmap = ShadowKit.blur(bitmap, mShadowRadius);
                mImageView.setImageBitmap(bitmap);
                Log.v("ShadowKit", "hitttt");
            } catch (Exception e) {
            }
        }
    }

    public void setShadowDx(int shadowDx) {
        this.mShadowDx = shadowDx;
        mImageView.setTranslationX(shadowDx);
    }

    public void setShadowDy(int shadowDy) {
        this.mShadowDy = shadowDy;
        mImageView.setTranslationY(shadowDy);
    }

    public void setShadowColor(int shadowColor) {
        this.mShadowColor = shadowColor;
        mImageView.setColorFilter(shadowColor, PorterDuff.Mode.SRC_ATOP);
    }

    public void setShadowAlpha(float shadowAlpha) {
        this.mShadowAlpha = shadowAlpha;
        mImageView.setAlpha(shadowAlpha);
    }

    public void setShadowRadius(int shadowRadius) {
        this.mShadowRadius = shadowRadius;
    }

}
