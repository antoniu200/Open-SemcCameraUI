// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.focusview;

import com.sonyericsson.android.camera.util.CamLog;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Paint;
import android.widget.ImageView;

public class SmileScore extends ImageView
{
    private static final int GAUGE_BOTTOM_A = 178;
    private static final int GAUGE_BOTTOM_B = 229;
    private static final int GAUGE_BOTTOM_G = 181;
    private static final int GAUGE_BOTTOM_R = 51;
    private static final int GAUGE_TOP_A = 178;
    private static final int GAUGE_TOP_B = 204;
    private static final int GAUGE_TOP_G = 153;
    private static final int GAUGE_TOP_R = 0;
    public static final int SMILE_MAX = 100;
    public static final int SMILE_MIN = 0;
    private static final int SMILE_UNIT = 100;
    public static final String TAG = "SmileScore";
    private static final Paint[] sColorPaints;
    private int mFrameHeight;
    private float mIndicatorStep;
    private int mIndicatorWidth;
    private int mSmileScore;
    
    static {
        sColorPaints = new Paint[100];
        for (int i = 0; i < 100; ++i) {
            final Paint paint = new Paint();
            final float n = (float)i;
            paint.setARGB((int)(178.0f + 0.0f * n), (int)(0.0f + 0.51f * n), (int)(153.0f + 0.28f * n), (int)(204.0f + 0.25f * n));
            SmileScore.sColorPaints[i] = paint;
        }
    }
    
    public SmileScore(final Context context) {
        this(context, null);
    }
    
    public SmileScore(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public SmileScore(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    private RectF getSmileScoreRect(final float bottom, final float right, final float top, final float left) {
        final RectF rectF = new RectF();
        if (this.isForLandscape()) {
            rectF.set(bottom, right, top, left);
        }
        else {
            rectF.set(right, bottom, left, top);
            if (rectF.left > rectF.right) {
                rectF.left = left;
                rectF.right = right;
            }
            if (rectF.top > rectF.bottom) {
                rectF.top = top;
                rectF.bottom = bottom;
            }
        }
        return rectF;
    }
    
    private boolean isLayoutOrientationLandscape() {
        return ((SmileGauge)this.getParent()).isLayoutOrientationLandscape();
    }
    
    public static final void preload() {
    }
    
    public void draw(final Canvas canvas) {
        if (CamLog.VERBOSE) {
            CamLog.v("draw() is called.");
        }
        final int dimensionPixelSize = this.getResources().getDimensionPixelSize(2131165642);
        final int dimensionPixelSize2 = this.getResources().getDimensionPixelSize(2131165636);
        final int dimensionPixelSize3 = this.getResources().getDimensionPixelSize(2131165635);
        final int dimensionPixelSize4 = this.getResources().getDimensionPixelSize(2131165637);
        if (!this.isLayoutOrientationLandscape() && !this.isForLandscape()) {
            final float f = (float)(dimensionPixelSize + dimensionPixelSize2);
            float f2 = (float)dimensionPixelSize4;
            final float f3 = this.mIndicatorWidth + f;
            for (int i = 0; i < 100; ++i) {
                if (this.mSmileScore < i) {
                    break;
                }
                final float f4 = this.mIndicatorStep + f2;
                if (f4 < this.mFrameHeight - dimensionPixelSize4) {
                    canvas.drawRect(this.getSmileScoreRect(f, f2, f3, f4), SmileScore.sColorPaints[i]);
                }
                if (CamLog.VERBOSE) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("draw: row: ");
                    sb.append(i);
                    sb.append(": drawRect(");
                    sb.append(f);
                    sb.append(", ");
                    sb.append(f2);
                    sb.append(", ");
                    sb.append(f3);
                    sb.append(", ");
                    sb.append(f4);
                    sb.append(", ");
                    sb.append(SmileScore.sColorPaints[i].getColor());
                    sb.append(")");
                    CamLog.d(sb.toString());
                }
                f2 += this.mIndicatorStep;
            }
        }
        else {
            final float f5 = (float)(dimensionPixelSize + dimensionPixelSize2);
            float f6 = (float)(this.mFrameHeight - dimensionPixelSize3);
            final float f7 = this.mIndicatorWidth + f5;
            for (int j = 0; j < 100; ++j) {
                if (this.mSmileScore < j) {
                    break;
                }
                final float f8 = this.mIndicatorStep + f6;
                if (f6 > dimensionPixelSize3) {
                    canvas.drawRect(this.getSmileScoreRect(f5, f6, f7, f8), SmileScore.sColorPaints[j]);
                }
                if (CamLog.VERBOSE) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("draw: row: ");
                    sb2.append(j);
                    sb2.append(": drawRect(");
                    sb2.append(f5);
                    sb2.append(", ");
                    sb2.append(f6);
                    sb2.append(", ");
                    sb2.append(f7);
                    sb2.append(", ");
                    sb2.append(f8);
                    sb2.append(", ");
                    sb2.append(SmileScore.sColorPaints[j].getColor());
                    sb2.append(")");
                    CamLog.d(sb2.toString());
                }
                f6 -= this.mIndicatorStep;
            }
        }
        super.draw(canvas);
        if (CamLog.VERBOSE) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("draw end: ");
            sb3.append(this.mSmileScore);
            CamLog.d(sb3.toString());
        }
    }
    
    protected boolean isForLandscape() {
        return ((SmileGauge)this.getParent()).isForLandscape();
    }
    
    public void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (CamLog.VERBOSE) {
            CamLog.v("onDraw() is called.");
        }
    }
    
    public void onFinishInflate() {
        if (CamLog.VERBOSE) {
            CamLog.v("onFinishInflate() is called.");
        }
        super.onFinishInflate();
        this.mSmileScore = 0;
        this.mFrameHeight = this.getResources().getDimensionPixelSize(2131165634);
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("onFinishInflate: Frame :height: ");
            sb.append(this.mFrameHeight);
            CamLog.v(sb.toString());
        }
        this.mIndicatorWidth = this.getResources().getDimensionPixelSize(2131165641);
        this.mIndicatorStep = this.mFrameHeight / 100.0f;
        if (CamLog.VERBOSE) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("onFinishInflate: Indicator step: ");
            sb2.append(this.mIndicatorStep);
            CamLog.v(sb2.toString());
        }
    }
    
    public void setSmileScore(final int n) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setSmileScore(");
            sb.append(n);
            sb.append(")");
            CamLog.v(sb.toString());
        }
        this.mSmileScore = n;
    }
}
