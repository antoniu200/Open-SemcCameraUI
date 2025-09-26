// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.baselayout;

import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;
import android.graphics.Rect;
import com.sonyericsson.android.camera.util.CamLog;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.graphics.Xfermode;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff$Mode;
import android.graphics.Color;
import android.content.Context;
import android.graphics.Paint;
import android.view.View;

public class SwitchAnimationView extends View
{
    private static final String TAG = "SwitchAnimationView";
    private final float mDraggingEndRadius;
    private final float mDraggingStartRadius;
    private Paint mHolePaint;
    private float mHoleRadius;
    private float mMaxRadius;
    private Paint mPaint;
    private int mPositionX;
    private int mPositionY;
    private float mRadius;
    
    public SwitchAnimationView(final Context context) {
        super(context);
        this.mPaint = new Paint();
        this.mHolePaint = new Paint();
        this.mHoleRadius = 0.0f;
        this.mPositionX = 0;
        this.mPositionY = 0;
        this.mPaint.setColor(Color.argb(255, 238, 238, 238));
        this.mHolePaint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff$Mode.DST_OUT));
        this.mDraggingStartRadius = (float)context.getResources().getDimensionPixelSize(2131165320);
        this.mDraggingEndRadius = (float)context.getResources().getDimensionPixelSize(2131165319);
    }
    
    public SwitchAnimationView(final Context context, final AttributeSet set) {
        super(context, set);
        this.mPaint = new Paint();
        this.mHolePaint = new Paint();
        this.mHoleRadius = 0.0f;
        this.mPositionX = 0;
        this.mPositionY = 0;
        this.mPaint.setColor(Color.argb(255, 238, 238, 238));
        this.mHolePaint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff$Mode.DST_OUT));
        this.mDraggingStartRadius = (float)context.getResources().getDimensionPixelSize(2131165320);
        this.mDraggingEndRadius = (float)context.getResources().getDimensionPixelSize(2131165319);
    }
    
    private void drawAfterSwitchEffect(final Canvas canvas) {
        canvas.drawCircle((float)this.mPositionX, (float)this.mPositionY, this.mHoleRadius, this.mHolePaint);
    }
    
    private void drawSwitchEffect(final Canvas canvas) {
        if (CamLog.VERBOSE) {
            CamLog.d("drawSwitchEffect canvas.drawCircle()");
        }
        canvas.drawCircle((float)this.mPositionX, (float)this.mPositionY, this.mRadius, this.mPaint);
    }
    
    public void draw(final Canvas canvas) {
        super.draw(canvas);
        this.drawSwitchEffect(canvas);
        this.drawAfterSwitchEffect(canvas);
    }
    
    public float getDraggingStartRadius() {
        return this.mDraggingStartRadius;
    }
    
    public float getMaxRadius() {
        return this.mMaxRadius;
    }
    
    public float getRadius() {
        return this.mRadius;
    }
    
    public void setHoleRadius(final float mHoleRadius) {
        this.mHoleRadius = mHoleRadius;
        this.postInvalidate();
    }
    
    public void setMaxRadius(final Rect rect) {
        final int width = rect.width();
        final int height = rect.height();
        this.mPositionX = rect.centerY();
        int mPositionX;
        int n;
        if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
            mPositionX = this.mPositionX;
            n = width - this.mPositionY;
        }
        else {
            mPositionX = width - this.mPositionX;
            n = height - this.mPositionY;
        }
        this.mMaxRadius = (float)Math.sqrt(Math.pow(mPositionX, 2.0) + Math.pow(n, 2.0));
    }
    
    public void setRadius(final float mRadius) {
        this.mRadius = mRadius;
        this.postInvalidate();
    }
    
    public void startDraggingAnimation(final float n) {
        if (CamLog.VERBOSE) {
            CamLog.d("startDraggingAnimation");
        }
        this.setAlpha(1.0f);
        this.mRadius = this.mDraggingStartRadius + this.mDraggingEndRadius * n;
        this.postInvalidate();
    }
    
    public void startDraggingStartedAnimation() {
        if (CamLog.VERBOSE) {
            CamLog.d("startDraggingStartedAnimation");
        }
        this.setAlpha(1.0f);
        this.mRadius = this.mDraggingStartRadius;
        this.postInvalidate();
    }
}
