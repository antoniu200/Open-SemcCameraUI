package com.sonyericsson.android.camera.view.baselayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SwitchAnimationView extends View {
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

    public SwitchAnimationView(Context context) {
        super(context);
        this.mPaint = new Paint();
        this.mHolePaint = new Paint();
        this.mHoleRadius = 0.0f;
        this.mPositionX = 0;
        this.mPositionY = 0;
        this.mPaint.setColor(Color.argb(255, 238, 238, 238));
        this.mHolePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        this.mDraggingStartRadius = context.getResources().getDimensionPixelSize(R.dimen.dragging_start_radius);
        this.mDraggingEndRadius = context.getResources().getDimensionPixelSize(R.dimen.dragging_end_radius);
    }

    public SwitchAnimationView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mPaint = new Paint();
        this.mHolePaint = new Paint();
        this.mHoleRadius = 0.0f;
        this.mPositionX = 0;
        this.mPositionY = 0;
        this.mPaint.setColor(Color.argb(255, 238, 238, 238));
        this.mHolePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        this.mDraggingStartRadius = context.getResources().getDimensionPixelSize(R.dimen.dragging_start_radius);
        this.mDraggingEndRadius = context.getResources().getDimensionPixelSize(R.dimen.dragging_end_radius);
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawSwitchEffect(canvas);
        drawAfterSwitchEffect(canvas);
    }

    private void drawSwitchEffect(Canvas canvas) {
        if (CamLog.VERBOSE) {
            CamLog.d("drawSwitchEffect canvas.drawCircle()");
        }
        canvas.drawCircle(this.mPositionX, this.mPositionY, this.mRadius, this.mPaint);
    }

    private void drawAfterSwitchEffect(Canvas canvas) {
        canvas.drawCircle(this.mPositionX, this.mPositionY, this.mHoleRadius, this.mHolePaint);
    }

    public void setRadius(float f) {
        this.mRadius = f;
        postInvalidate();
    }

    public void setHoleRadius(float f) {
        this.mHoleRadius = f;
        postInvalidate();
    }

    public void setMaxRadius(Rect rect) {
        int i;
        int i2;
        int iWidth = rect.width();
        int iHeight = rect.height();
        this.mPositionX = rect.centerY();
        if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
            i = this.mPositionX;
            i2 = iWidth - this.mPositionY;
        } else {
            i = iWidth - this.mPositionX;
            i2 = iHeight - this.mPositionY;
        }
        this.mMaxRadius = (float) Math.sqrt(Math.pow(i, 2.0d) + Math.pow(i2, 2.0d));
    }

    public float getMaxRadius() {
        return this.mMaxRadius;
    }

    public float getRadius() {
        return this.mRadius;
    }

    public float getDraggingStartRadius() {
        return this.mDraggingStartRadius;
    }

    public void startDraggingStartedAnimation() {
        if (CamLog.VERBOSE) {
            CamLog.d("startDraggingStartedAnimation");
        }
        setAlpha(1.0f);
        this.mRadius = this.mDraggingStartRadius;
        postInvalidate();
    }

    public void startDraggingAnimation(float f) {
        if (CamLog.VERBOSE) {
            CamLog.d("startDraggingAnimation");
        }
        setAlpha(1.0f);
        this.mRadius = this.mDraggingStartRadius + (this.mDraggingEndRadius * f);
        postInvalidate();
    }
}
