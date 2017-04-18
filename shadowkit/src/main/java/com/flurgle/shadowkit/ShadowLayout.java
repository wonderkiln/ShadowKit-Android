package com.flurgle.shadowkit;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class ShadowLayout extends FrameLayout {

    private static final float PERCENT_PADDING = 0.25f;

    private static final int DEFAULT_SHADOW_DX = 0;
    private static final int DEFAULT_SHADOW_DY = 0;
    private static final int DEFAULT_SHADOW_COLOR = Color.argb(0, 0, 0, 0);
    private static final float DEFAULT_SHADOW_ALPHA = 0.66f;
    private static final int DEFAULT_SHADOW_RADIUS = 0;
    private static final int DEFAULT_SHADOW_SCALE = 1;

    private int mShadowDx;
    private int mShadowDy;
    private int mShadowColor;
    private float mShadowAlpha;
    private int mShadowRadius;
    private float mShadowScale;
    private String mReuseKey;

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
            mShadowScale = a.getFloat(R.styleable.ShadowLayout_skShadowScale, DEFAULT_SHADOW_SCALE);
            mReuseKey = a.getString(R.styleable.ShadowLayout_skReuseKey);
        } finally {
            a.recycle();
        }

        mImageView = new ImageView(context);
        mImageView.setLayoutParams(new FrameLayout.LayoutParams(0, 0));
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        addView(mImageView);

        setShadowDx(mShadowDx);
        setShadowDy(mShadowDy);
        setShadowColor(mShadowColor);
        setShadowAlpha(mShadowAlpha);
        setShadowRadius(mShadowRadius);
        setShadowScale(mShadowScale);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                invalidate();
            }
        });
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (mReuseKey != null) {
            Cache.getShadow(mReuseKey, new Cache.BitmapCallback() {
                @Override
                public void bitmapReady(Bitmap bitmap) {
                    mImageView.setImageBitmap(bitmap);
                }

                @Override
                public void createBitmap() {
                    try {
                        Bitmap bitmap = ShadowKit.getBitmapForView(ShadowLayout.this, PERCENT_PADDING);
                        ShadowKit.blur(bitmap, mShadowRadius, new ShadowKit.OnBlurListener() {
                            @Override
                            public void onBlur(final Bitmap bitmap) {
                                post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Cache.setShadow(mReuseKey, bitmap);
                                    }
                                });
                            }
                        });
                    } catch (Exception e) {
                    }
                }
            });

            return;
        }

        mImageView.setVisibility(GONE);
        try {
            Bitmap bitmap = ShadowKit.getBitmapForView(ShadowLayout.this, PERCENT_PADDING);
            ShadowKit.blur(bitmap, mShadowRadius, new ShadowKit.OnBlurListener() {
                @Override
                public void onBlur(final Bitmap bitmap) {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            mImageView.setImageBitmap(bitmap);
                            mImageView.setVisibility(VISIBLE);
                        }
                    });
                }
            });
        } catch (Exception e) {
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mImageView.setLayoutParams(new FrameLayout.LayoutParams(w, h));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        traverseViewTreeDisablingClipping(this);
    }

    private void traverseViewTreeDisablingClipping(ViewGroup viewGroup) {
        viewGroup.setClipChildren(false);
        try {
            traverseViewTreeDisablingClipping((ViewGroup) viewGroup.getParent());
        } catch (Exception e) {
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
        this.mShadowRadius =
                shadowRadius < 1 ? 1
                        : shadowRadius > 25 ? 25
                        : shadowRadius;
    }

    public void setShadowScale(float shadowScale) {
        this.mShadowScale = shadowScale;
        mImageView.setScaleX((PERCENT_PADDING * 2 + 1) * mShadowScale);
        mImageView.setScaleY((PERCENT_PADDING * 2 + 1) * mShadowScale);
    }

}
