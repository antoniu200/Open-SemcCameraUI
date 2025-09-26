// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.Xfermode;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff$Mode;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Bitmap$Config;
import android.graphics.Bitmap;

public final class zzlt
{
    public static Bitmap zza(final Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        int n = 0;
        int n2;
        if (width >= height) {
            n = (height - width) / 2;
            width = height;
            n2 = 0;
        }
        else {
            n2 = (width - height) / 2;
        }
        final Bitmap bitmap2 = Bitmap.createBitmap(width, width, Bitmap$Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap2);
        final Paint paint = new Paint(1);
        paint.setColor(-16777216);
        final float n3 = (float)(width / 2);
        canvas.drawCircle(n3, n3, n3, paint);
        paint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff$Mode.SRC_IN));
        canvas.drawBitmap(bitmap, (float)n, (float)n2, paint);
        return bitmap2;
    }
    
    private static Bitmap zza(final Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }
        final Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap$Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
    
    public static Drawable zza(final Resources resources, final Drawable drawable) {
        return (Drawable)new BitmapDrawable(resources, zza(zza(drawable)));
    }
}
