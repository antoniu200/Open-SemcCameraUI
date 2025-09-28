package com.sonyericsson.cameracommon.focusview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.util.CamLog;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SmileScore extends ImageView {
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
    private static final Paint[] sColorPaints = new Paint[100];
    private int mFrameHeight;
    private float mIndicatorStep;
    private int mIndicatorWidth;
    private int mSmileScore;

    public static final void preload() {
    }

    static {
        for (int i = 0; i < 100; i++) {
            Paint paint = new Paint();
            float f = i;
            paint.setARGB((int) (178.0f + (0.0f * f)), (int) (0.0f + (0.51f * f)), (int) (153.0f + (0.28f * f)), (int) (204.0f + (0.25f * f)));
            sColorPaints[i] = paint;
        }
    }

    public SmileScore(Context context) {
        this(context, null);
    }

    public SmileScore(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SmileScore(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // android.view.View
    public void onFinishInflate() {
        if (CamLog.VERBOSE) {
            CamLog.v("onFinishInflate() is called.");
        }
        super.onFinishInflate();
        this.mSmileScore = 0;
        this.mFrameHeight = getResources().getDimensionPixelSize(R.dimen.smile_gauge_frame_height);
        if (CamLog.VERBOSE) {
            CamLog.v("onFinishInflate: Frame :height: " + this.mFrameHeight);
        }
        this.mIndicatorWidth = getResources().getDimensionPixelSize(R.dimen.smile_gauge_indicator_width);
        this.mIndicatorStep = this.mFrameHeight / 100.0f;
        if (CamLog.VERBOSE) {
            CamLog.v("onFinishInflate: Indicator step: " + this.mIndicatorStep);
        }
    }

    @Override // android.widget.ImageView, android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (CamLog.VERBOSE) {
            CamLog.v("onDraw() is called.");
        }
    }

    public void setSmileScore(int i) {
        if (CamLog.VERBOSE) {
            CamLog.v("setSmileScore(" + i + ")");
        }
        this.mSmileScore = i;
    }

    @Override // android.view.View
    public void draw(Canvas canvas) throws Resources.NotFoundException {
        if (CamLog.VERBOSE) {
            CamLog.v("draw() is called.");
        }
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.smile_gauge_padding_left);
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(R.dimen.smile_gauge_frame_padding_left);
        int dimensionPixelSize3 = getResources().getDimensionPixelSize(R.dimen.smile_gauge_frame_padding_bottom);
        int dimensionPixelSize4 = getResources().getDimensionPixelSize(R.dimen.smile_gauge_frame_padding_top);
        if (!isLayoutOrientationLandscape() && !isForLandscape()) {
            float f = dimensionPixelSize + dimensionPixelSize2;
            float f2 = this.mIndicatorWidth + f;
            float f3 = dimensionPixelSize4;
            for (int i = 0; i < 100 && this.mSmileScore >= i; i++) {
                float f4 = this.mIndicatorStep + f3;
                if (f4 < this.mFrameHeight - dimensionPixelSize4) {
                    canvas.drawRect(getSmileScoreRect(f, f3, f2, f4), sColorPaints[i]);
                }
                if (CamLog.VERBOSE) {
                    CamLog.d("draw: row: " + i + ": drawRect(" + f + ", " + f3 + ", " + f2 + ", " + f4 + ", " + sColorPaints[i].getColor() + ")");
                }
                f3 += this.mIndicatorStep;
            }
        } else {
            float f5 = dimensionPixelSize + dimensionPixelSize2;
            float f6 = this.mIndicatorWidth + f5;
            float f7 = this.mFrameHeight - dimensionPixelSize3;
            for (int i2 = 0; i2 < 100 && this.mSmileScore >= i2; i2++) {
                float f8 = this.mIndicatorStep + f7;
                if (f7 > dimensionPixelSize3) {
                    canvas.drawRect(getSmileScoreRect(f5, f7, f6, f8), sColorPaints[i2]);
                }
                if (CamLog.VERBOSE) {
                    CamLog.d("draw: row: " + i2 + ": drawRect(" + f5 + ", " + f7 + ", " + f6 + ", " + f8 + ", " + sColorPaints[i2].getColor() + ")");
                }
                f7 -= this.mIndicatorStep;
            }
        }
        super.draw(canvas);
        if (CamLog.VERBOSE) {
            CamLog.d("draw end: " + this.mSmileScore);
        }
    }

    private RectF getSmileScoreRect(float f, float f2, float f3, float f4) {
        RectF rectF = new RectF();
        if (isForLandscape()) {
            rectF.set(f, f2, f3, f4);
        } else {
            rectF.set(f2, f, f4, f3);
            if (rectF.left > rectF.right) {
                rectF.left = f4;
                rectF.right = f2;
            }
            if (rectF.top > rectF.bottom) {
                rectF.top = f3;
                rectF.bottom = f;
            }
        }
        return rectF;
    }

    protected boolean isForLandscape() {
        return ((SmileGauge) getParent()).isForLandscape();
    }

    private boolean isLayoutOrientationLandscape() {
        return ((SmileGauge) getParent()).isLayoutOrientationLandscape();
    }
}
