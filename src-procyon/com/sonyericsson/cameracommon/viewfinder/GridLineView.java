// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.viewfinder;

import android.view.ViewGroup$LayoutParams;
import com.sonyericsson.android.camera.util.CamLog;
import android.graphics.Canvas;
import android.content.res.Resources$Theme;
import android.content.Context;
import android.graphics.Paint;
import android.view.View;

public class GridLineView extends View
{
    public static final String TAG = "GridLineView";
    private float mBottomHorizontalLinePositionY;
    private boolean mIsGridLineEnabled;
    private float mLeftVerticalLinePositionX;
    private Paint mPaint;
    private float mRightVerticalLinePositionX;
    private float mTopHorizontalLinePositionY;
    private int mViewHeight;
    private int mViewWidth;
    
    public GridLineView(final Context context) {
        super(context);
        this.mViewWidth = 0;
        this.mViewHeight = 0;
        this.mPaint = new Paint();
        this.mIsGridLineEnabled = false;
        this.mPaint.setColor(this.getResources().getColor(2131099712, (Resources$Theme)null));
        this.mPaint.setStrokeWidth((float)this.getResources().getDimensionPixelSize(2131165736));
    }
    
    private void drawGridLine(final Canvas canvas) {
        if (CamLog.VERBOSE) {
            CamLog.d("drawGridLine");
        }
        canvas.drawLine(this.mLeftVerticalLinePositionX, 0.0f, this.mLeftVerticalLinePositionX, (float)this.mViewHeight, this.mPaint);
        canvas.drawLine(this.mRightVerticalLinePositionX, 0.0f, this.mRightVerticalLinePositionX, (float)this.mViewHeight, this.mPaint);
        canvas.drawLine(0.0f, this.mTopHorizontalLinePositionY, (float)this.mViewWidth, this.mTopHorizontalLinePositionY, this.mPaint);
        canvas.drawLine(0.0f, this.mBottomHorizontalLinePositionY, (float)this.mViewWidth, this.mBottomHorizontalLinePositionY, this.mPaint);
    }
    
    public void disable() {
        this.mIsGridLineEnabled = false;
        this.setVisibility(4);
    }
    
    public void enable() {
        this.mIsGridLineEnabled = true;
        this.setVisibility(0);
    }
    
    public void hide() {
        this.setVisibility(4);
    }
    
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (this.mIsGridLineEnabled) {
            this.drawGridLine(canvas);
        }
    }
    
    public void setViewSize(final int width, final int height) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setViewSize width:");
            sb.append(width);
            sb.append(" height:");
            sb.append(height);
            CamLog.d(sb.toString());
        }
        if (this.mViewWidth == width && this.mViewHeight == height) {
            return;
        }
        this.mViewWidth = width;
        this.mViewHeight = height;
        this.mLeftVerticalLinePositionX = this.mViewWidth / 3.0f;
        this.mRightVerticalLinePositionX = this.mViewWidth * 2 / 3.0f;
        this.mTopHorizontalLinePositionY = this.mViewHeight / 3.0f;
        this.mBottomHorizontalLinePositionY = this.mViewHeight * 2 / 3.0f;
        final ViewGroup$LayoutParams layoutParams = this.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.width = width;
            layoutParams.height = height;
            this.requestLayout();
        }
    }
}
