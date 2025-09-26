package com.sonyericsson.cameracommon.viewfinder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.util.CamLog;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class GridLineView extends View {
    public static final String TAG = "GridLineView";
    private float mBottomHorizontalLinePositionY;
    private boolean mIsGridLineEnabled;
    private float mLeftVerticalLinePositionX;
    private Paint mPaint;
    private float mRightVerticalLinePositionX;
    private float mTopHorizontalLinePositionY;
    private int mViewHeight;
    private int mViewWidth;

    public GridLineView(Context context) {
        super(context);
        this.mViewWidth = 0;
        this.mViewHeight = 0;
        this.mPaint = new Paint();
        this.mIsGridLineEnabled = false;
        this.mPaint.setColor(getResources().getColor(R.color.gridline_color, null));
        this.mPaint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.viewfinder_grid_line_width));
    }

    public void setViewSize(int i, int i2) {
        if (CamLog.VERBOSE) {
            CamLog.d("setViewSize width:" + i + " height:" + i2);
        }
        if (this.mViewWidth == i && this.mViewHeight == i2) {
            return;
        }
        this.mViewWidth = i;
        this.mViewHeight = i2;
        this.mLeftVerticalLinePositionX = this.mViewWidth / 3.0f;
        this.mRightVerticalLinePositionX = (this.mViewWidth * 2) / 3.0f;
        this.mTopHorizontalLinePositionY = this.mViewHeight / 3.0f;
        this.mBottomHorizontalLinePositionY = (this.mViewHeight * 2) / 3.0f;
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams != null) {
            layoutParams.width = i;
            layoutParams.height = i2;
            requestLayout();
        }
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mIsGridLineEnabled) {
            drawGridLine(canvas);
        }
    }

    private void drawGridLine(Canvas canvas) {
        if (CamLog.VERBOSE) {
            CamLog.d("drawGridLine");
        }
        canvas.drawLine(this.mLeftVerticalLinePositionX, 0.0f, this.mLeftVerticalLinePositionX, this.mViewHeight, this.mPaint);
        canvas.drawLine(this.mRightVerticalLinePositionX, 0.0f, this.mRightVerticalLinePositionX, this.mViewHeight, this.mPaint);
        canvas.drawLine(0.0f, this.mTopHorizontalLinePositionY, this.mViewWidth, this.mTopHorizontalLinePositionY, this.mPaint);
        canvas.drawLine(0.0f, this.mBottomHorizontalLinePositionY, this.mViewWidth, this.mBottomHorizontalLinePositionY, this.mPaint);
    }

    public void hide() {
        setVisibility(4);
    }

    public void enable() {
        this.mIsGridLineEnabled = true;
        setVisibility(0);
    }

    public void disable() {
        this.mIsGridLineEnabled = false;
        setVisibility(4);
    }
}
