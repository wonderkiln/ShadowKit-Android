package com.flurgle.shadowkit;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class ShadowLayout extends FrameLayout {

    private ImageView mImageView;

    public ShadowLayout(Context context) {
        this(context, null);
    }

    public ShadowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mImageView = new ImageView(context);
        mImageView.setTranslationX(20);
        mImageView.setTranslationY(20);
        addView(mImageView);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            try {
                Bitmap bitmap = ShadowKit.getBitmapForView(this);
                mImageView.setImageBitmap(bitmap);
            } catch (Exception e) {
            }
        }
    }

}
