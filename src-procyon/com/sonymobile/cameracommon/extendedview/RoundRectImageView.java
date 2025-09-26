// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.cameracommon.extendedview;

import android.graphics.Canvas;
import android.graphics.Path$Direction;
import android.graphics.Path$FillType;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.RectF;
import android.graphics.Path;
import android.widget.ImageView;

public class RoundRectImageView extends ImageView
{
    public static final String TAG = "RoundRectImageView";
    private Path mClipPath;
    private RectF mDstRect;
    private float[] mRadiusSet;
    
    public RoundRectImageView(final Context context) {
        super(context);
        this.mDstRect = new RectF();
        this.mRadiusSet = new float[8];
        this.mClipPath = new Path();
        this.initialize();
    }
    
    public RoundRectImageView(final Context context, final AttributeSet set) {
        super(context, set);
        this.mDstRect = new RectF();
        this.mRadiusSet = new float[8];
        this.mClipPath = new Path();
        this.initialize();
    }
    
    private void initialize() {
        this.mClipPath.setFillType(Path$FillType.WINDING);
    }
    
    private void updateClipPath() {
        this.mClipPath.addRoundRect(this.mDstRect, this.mRadiusSet, Path$Direction.CCW);
    }
    
    public void onDraw(final Canvas canvas) {
        canvas.save();
        canvas.clipPath(this.mClipPath);
        super.onDraw(canvas);
        canvas.restore();
    }
    
    public void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        super.onSizeChanged(n, n2, n3, n4);
        this.mDstRect.set(0.0f, 0.0f, (float)n, (float)n2);
        this.updateClipPath();
    }
    
    public void setRadius(final float n) {
        for (int i = 0; i < 8; ++i) {
            this.mRadiusSet[i] = n;
        }
        this.updateClipPath();
    }
    
    public void setRadius(final float n, final float n2, final float n3, final float n4) {
        this.mRadiusSet[0] = n;
        this.mRadiusSet[1] = n;
        this.mRadiusSet[2] = n2;
        this.mRadiusSet[3] = n2;
        this.mRadiusSet[4] = n3;
        this.mRadiusSet[5] = n3;
        this.mRadiusSet[6] = n4;
        this.mRadiusSet[7] = n4;
        this.updateClipPath();
    }
}
