// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.baselayout;

import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;
import android.graphics.Rect;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.graphics.Color;
import android.content.Context;
import android.graphics.Paint;
import android.view.View;

public class RippleEffectView extends View
{
    private static final String TAG = "RippleEffectView";
    private float mMaxRadius;
    private Paint mPaint;
    private float mRadius;
    
    public RippleEffectView(final Context context) {
        super(context);
        (this.mPaint = new Paint()).setColor(Color.argb(255, 32, 32, 32));
    }
    
    public RippleEffectView(final Context context, final AttributeSet set) {
        super(context, set);
        (this.mPaint = new Paint()).setColor(Color.argb(255, 32, 32, 32));
    }
    
    private void drawRippleEffect(final Canvas canvas) {
        final Rect rect = new Rect();
        this.getGlobalVisibleRect(rect);
        final Rect rectAccordingToLayoutOrientation = LayoutOrientationResolver.getInstance().getRectAccordingToLayoutOrientation(rect);
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("drawRippleEffect canvas.drawCircle() rect:");
            sb.append(rectAccordingToLayoutOrientation);
            CamLog.d(sb.toString());
        }
        canvas.drawCircle(rectAccordingToLayoutOrientation.width() / 2.0f, rectAccordingToLayoutOrientation.height() / 2.0f, this.mRadius, this.mPaint);
    }
    
    public float getMaxRadius() {
        return this.mMaxRadius;
    }
    
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        this.drawRippleEffect(canvas);
    }
    
    public void setMaxRadius(final float mMaxRadius) {
        this.mMaxRadius = mMaxRadius;
    }
    
    public void setRadius(final float mRadius) {
        this.mRadius = mRadius;
        this.postInvalidate();
    }
}
