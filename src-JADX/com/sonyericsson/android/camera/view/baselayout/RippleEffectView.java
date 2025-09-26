package com.sonyericsson.android.camera.view.baselayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class RippleEffectView extends View {
    private static final String TAG = "RippleEffectView";
    private float mMaxRadius;
    private Paint mPaint;
    private float mRadius;

    public RippleEffectView(Context context) {
        super(context);
        this.mPaint = new Paint();
        this.mPaint.setColor(Color.argb(255, 32, 32, 32));
    }

    public RippleEffectView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mPaint = new Paint();
        this.mPaint.setColor(Color.argb(255, 32, 32, 32));
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRippleEffect(canvas);
    }

    private void drawRippleEffect(Canvas canvas) {
        Rect rect = new Rect();
        getGlobalVisibleRect(rect);
        Rect rectAccordingToLayoutOrientation = LayoutOrientationResolver.getInstance().getRectAccordingToLayoutOrientation(rect);
        if (CamLog.VERBOSE) {
            CamLog.d("drawRippleEffect canvas.drawCircle() rect:" + rectAccordingToLayoutOrientation);
        }
        canvas.drawCircle(rectAccordingToLayoutOrientation.width() / 2.0f, rectAccordingToLayoutOrientation.height() / 2.0f, this.mRadius, this.mPaint);
    }

    public void setRadius(float f) {
        this.mRadius = f;
        postInvalidate();
    }

    public void setMaxRadius(float f) {
        this.mMaxRadius = f;
    }

    public float getMaxRadius() {
        return this.mMaxRadius;
    }
}
