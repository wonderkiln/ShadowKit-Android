package com.flurgle.shadowkit;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Cache {

    private static Map<String, Bitmap> cache = new HashMap<>();
    private static Map<String, List<BitmapCallback>> pending = new HashMap<>();

    public static void getShadow(String cacheKey, BitmapCallback bitmapCallback) {
        if (cache.get(cacheKey) != null) {
            bitmapCallback.bitmapReady(cache.get(cacheKey));
            return;
        }

        if (pending.get(cacheKey) == null) {
            pending.put(cacheKey, new ArrayList<BitmapCallback>());
            bitmapCallback.createBitmap();
        }

        pending.get(cacheKey).add(bitmapCallback);
    }

    public static void setShadow(String cacheKey, Bitmap bitmap) {
        cache.put(cacheKey, bitmap);
        if (pending.get(cacheKey) != null) {
            for (BitmapCallback bitmapCallback : pending.get(cacheKey)) {
                bitmapCallback.bitmapReady(bitmap);
            }
        }
    }

    public interface BitmapCallback {
        void bitmapReady(Bitmap bitmap);
        void createBitmap();
    }

}
