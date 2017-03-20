package com.flurgle.shadowkit;

import android.graphics.Bitmap;
import android.view.View;

public class ShadowKit {

    public static Bitmap getBitmapForView(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

}
