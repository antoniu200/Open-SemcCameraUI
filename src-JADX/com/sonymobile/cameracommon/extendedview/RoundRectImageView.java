package com.sonymobile.cameracommon.extendedview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class RoundRectImageView extends ImageView {
    public static final String TAG = "RoundRectImageView";
    private Path mClipPath;
    private RectF mDstRect;
    private float[] mRadiusSet;

    public RoundRectImageView(Context context) {
        super(context);
        this.mDstRect = new RectF();
        this.mRadiusSet = new float[8];
        this.mClipPath = new Path();
        initialize();
    }

    public RoundRectImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mDstRect = new RectF();
        this.mRadiusSet = new float[8];
        this.mClipPath = new Path();
        initialize();
    }

    private void initialize() {
        this.mClipPath.setFillType(Path.FillType.WINDING);
    }

    public void setRadius(float f) {
        for (int i = 0; i < 8; i++) {
            this.mRadiusSet[i] = f;
        }
        updateClipPath();
    }

    public void setRadius(float f, float f2, float f3, float f4) {
        this.mRadiusSet[0] = f;
        this.mRadiusSet[1] = f;
        this.mRadiusSet[2] = f2;
        this.mRadiusSet[3] = f2;
        this.mRadiusSet[4] = f3;
        this.mRadiusSet[5] = f3;
        this.mRadiusSet[6] = f4;
        this.mRadiusSet[7] = f4;
        updateClipPath();
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mDstRect.set(0.0f, 0.0f, i, i2);
        updateClipPath();
    }

    private void updateClipPath() {
        this.mClipPath.addRoundRect(this.mDstRect, this.mRadiusSet, Path.Direction.CCW);
    }

    @Override // android.widget.ImageView, android.view.View
    public void onDraw(Canvas canvas) {
        canvas.save();
        canvas.clipPath(this.mClipPath);
        super.onDraw(canvas);
        canvas.restore();
    }
}
