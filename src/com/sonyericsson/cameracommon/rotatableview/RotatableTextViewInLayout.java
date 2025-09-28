package com.sonyericsson.cameracommon.rotatableview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sonyericsson.android.camera.R;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class RotatableTextViewInLayout extends RelativeLayout {
    private static final int ROTATE_DEGREE = -90;
    public static final String TAG = "RotatableTextViewInLayout";
    private ImageView mBackground;
    private int mLayoutHeight;
    private int mLayoutWidth;
    private int mSensorOrientation;
    private TextView mText;
    private int mTextViewHeightOnLandscape;
    private int mTextViewHeightOnPortrait;
    private int mTextViewWidthOnLandscape;
    private int mTextViewWidthOnPortrait;

    public RotatableTextViewInLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mText = null;
        this.mBackground = null;
        this.mSensorOrientation = 2;
        this.mLayoutWidth = 0;
        this.mLayoutHeight = 0;
        this.mTextViewWidthOnLandscape = 0;
        this.mTextViewHeightOnLandscape = 0;
        this.mTextViewWidthOnPortrait = 0;
        this.mTextViewHeightOnPortrait = 0;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mText = (TextView) findViewById(R.id.rotatable_text_view_text);
        this.mText.setGravity(17);
        this.mBackground = (ImageView) findViewById(R.id.rotatable_text_view_background);
        this.mBackground.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        this.mLayoutWidth = i;
        this.mLayoutHeight = i2;
        updateLayout();
    }

    public void setTextResId(int i) {
        this.mText.setText(i);
    }

    public void setBackgroundDrawableId(int i) {
        this.mBackground.setImageResource(i);
    }

    public void setTextSizeOnLandscape(int i, int i2) {
        this.mTextViewWidthOnLandscape = i;
        this.mTextViewHeightOnLandscape = i2;
    }

    public void setTextSizeOnPortrait(int i, int i2) {
        this.mTextViewWidthOnPortrait = i;
        this.mTextViewHeightOnPortrait = i2;
    }

    public void updateLayout() {
        ((RelativeLayout.LayoutParams) this.mText.getLayoutParams()).addRule(13);
        ((RelativeLayout.LayoutParams) this.mBackground.getLayoutParams()).addRule(13);
        if (this.mSensorOrientation == 1) {
            this.mText.getLayoutParams().width = this.mTextViewWidthOnPortrait;
            this.mBackground.getLayoutParams().width = this.mLayoutHeight;
            this.mBackground.getLayoutParams().height = this.mTextViewHeightOnPortrait;
            scrollTo((-1) * ((this.mLayoutWidth / 2) - (this.mTextViewHeightOnPortrait / 2)), 0);
        } else {
            this.mText.getLayoutParams().width = this.mTextViewWidthOnLandscape;
            this.mBackground.getLayoutParams().width = this.mLayoutWidth;
            this.mBackground.getLayoutParams().height = this.mTextViewHeightOnLandscape;
            scrollTo(0, (-1) * ((this.mLayoutHeight / 2) - (this.mTextViewHeightOnLandscape / 2)));
        }
        requestLayout();
        invalidate();
    }

    public void setSensorOrientation(int i) {
        this.mSensorOrientation = i;
        updateLayout();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        if (this.mSensorOrientation == 1) {
            canvas.rotate(-90.0f, getWidth() / 2.0f, getHeight() / 2.0f);
        }
        super.dispatchDraw(canvas);
    }
}
