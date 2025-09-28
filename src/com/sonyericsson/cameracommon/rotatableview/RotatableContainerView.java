package com.sonyericsson.cameracommon.rotatableview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.sonyericsson.android.camera.R;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class RotatableContainerView extends FrameLayout {
    public static final String TAG = "RotatableContainerView";
    private FrameLayout mContainerView;
    private FrameLayout mCustomizableView;
    private int mUiOrientation;

    public RotatableContainerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mUiOrientation = 0;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mContainerView = (FrameLayout) findViewById(R.id.container);
        this.mCustomizableView = (FrameLayout) findViewById(R.id.overlay);
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        int defaultSize = getDefaultSize(getSuggestedMinimumWidth(), i);
        int defaultSize2 = getDefaultSize(getSuggestedMinimumHeight(), i2);
        if (this.mUiOrientation != 0 && isPortraitUi()) {
            setPortraitUi(defaultSize, defaultSize2, false);
        } else {
            setLandscapeUi(defaultSize, defaultSize2, false);
        }
        super.onMeasure(i, i2);
    }

    public FrameLayout getCustamizableView() {
        return this.mCustomizableView;
    }

    public void setUiOrientation(int i) {
        if (this.mUiOrientation != i) {
            this.mUiOrientation = i;
            requestLayout();
        }
    }

    private void setLandscapeUi(int i, int i2, boolean z) {
        int iMax = Math.max(i, i2);
        this.mContainerView.getLayoutParams().height = iMax;
        this.mContainerView.getLayoutParams().width = iMax;
        requestLayout();
        int measuredWidth = this.mCustomizableView.getMeasuredWidth();
        int measuredHeight = this.mCustomizableView.getMeasuredHeight();
        if (measuredWidth != i || measuredHeight != i2) {
            this.mCustomizableView.getLayoutParams().width = i;
            this.mCustomizableView.getLayoutParams().height = i2;
            this.mCustomizableView.requestLayout();
        }
        this.mCustomizableView.setRotation(0.0f);
    }

    private void setPortraitUi(int i, int i2, boolean z) {
        int iMax = Math.max(i, i2);
        this.mContainerView.getLayoutParams().height = iMax;
        this.mContainerView.getLayoutParams().width = iMax;
        requestLayout();
        int measuredWidth = this.mCustomizableView.getMeasuredWidth();
        int measuredHeight = this.mCustomizableView.getMeasuredHeight();
        if (measuredWidth != i2 || measuredHeight != i) {
            this.mCustomizableView.getLayoutParams().width = i2;
            this.mCustomizableView.getLayoutParams().height = i;
            this.mCustomizableView.requestLayout();
        }
        if (z) {
            this.mCustomizableView.setRotation(90.0f);
        } else {
            this.mCustomizableView.setRotation(270.0f);
        }
    }

    private boolean isPortraitUi() {
        return this.mUiOrientation == 1;
    }
}
