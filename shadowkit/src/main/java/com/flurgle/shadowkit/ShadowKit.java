package com.flurgle.shadowkit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.view.View;

public class ShadowKit {

    private static RenderScript renderScript;

    public static void init(Context context) {
        renderScript = RenderScript.create(context);
    }

    public static Bitmap blur(Bitmap src, int radius) {
        final Allocation input = Allocation.createFromBitmap(renderScript, src);
        final Allocation output = Allocation.createTyped(renderScript, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));

        script.setRadius(radius);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(src);
        return src;
    }

    public static Bitmap getBitmapForView(View src, float percentPadding) {
        Bitmap bitmap = Bitmap.createBitmap(
                (int) (src.getWidth() + src.getWidth() * percentPadding * 2),
                (int) (src.getHeight() + src.getHeight() * percentPadding * 2),
                Bitmap.Config.ARGB_8888
        );

        src.setDrawingCacheEnabled(true);
        src.buildDrawingCache();
        Bitmap srcBitmap = src.getDrawingCache();

        Canvas canvas = new Canvas(bitmap);

        int dstLeft = (int) (src.getWidth() * percentPadding);
        int dstTop = (int) (src.getHeight() * percentPadding);
        int dstRight = dstLeft + src.getWidth();
        int dstBottom = dstTop + src.getHeight();

        canvas.drawBitmap(
                srcBitmap,
                new Rect(0, 0, srcBitmap.getWidth(), srcBitmap.getHeight()),
                new Rect(dstLeft, dstTop, dstRight, dstBottom),
                null
        );

        return bitmap;
    }

}
