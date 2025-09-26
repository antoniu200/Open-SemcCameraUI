// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.rotatableview;

import android.util.AttributeSet;
import android.content.Context;
import android.widget.FrameLayout;

public class RotatableContainerView extends FrameLayout
{
    public static final String TAG = "RotatableContainerView";
    private FrameLayout mContainerView;
    private FrameLayout mCustomizableView;
    private int mUiOrientation;
    
    public RotatableContainerView(final Context context, final AttributeSet set) {
        super(context, set);
        this.mUiOrientation = 0;
    }
    
    private boolean isPortraitUi() {
        final int mUiOrientation = this.mUiOrientation;
        boolean b = true;
        if (mUiOrientation != 1) {
            b = false;
        }
        return b;
    }
    
    private void setLandscapeUi(final int n, final int n2, final boolean b) {
        final int max = Math.max(n, n2);
        this.mContainerView.getLayoutParams().height = max;
        this.mContainerView.getLayoutParams().width = max;
        this.requestLayout();
        final int measuredWidth = this.mCustomizableView.getMeasuredWidth();
        final int measuredHeight = this.mCustomizableView.getMeasuredHeight();
        if (measuredWidth != n || measuredHeight != n2) {
            this.mCustomizableView.getLayoutParams().width = n;
            this.mCustomizableView.getLayoutParams().height = n2;
            this.mCustomizableView.requestLayout();
        }
        this.mCustomizableView.setRotation(0.0f);
    }
    
    private void setPortraitUi(final int n, final int n2, final boolean b) {
        final int max = Math.max(n, n2);
        this.mContainerView.getLayoutParams().height = max;
        this.mContainerView.getLayoutParams().width = max;
        this.requestLayout();
        final int measuredWidth = this.mCustomizableView.getMeasuredWidth();
        final int measuredHeight = this.mCustomizableView.getMeasuredHeight();
        if (measuredWidth != n2 || measuredHeight != n) {
            this.mCustomizableView.getLayoutParams().width = n2;
            this.mCustomizableView.getLayoutParams().height = n;
            this.mCustomizableView.requestLayout();
        }
        if (b) {
            this.mCustomizableView.setRotation(90.0f);
        }
        else {
            this.mCustomizableView.setRotation(270.0f);
        }
    }
    
    public FrameLayout getCustamizableView() {
        return this.mCustomizableView;
    }
    
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mContainerView = (FrameLayout)this.findViewById(2131296363);
        this.mCustomizableView = (FrameLayout)this.findViewById(2131296485);
    }
    
    protected void onMeasure(final int n, final int n2) {
        final int defaultSize = getDefaultSize(this.getSuggestedMinimumWidth(), n);
        final int defaultSize2 = getDefaultSize(this.getSuggestedMinimumHeight(), n2);
        if (this.mUiOrientation == 0) {
            this.setLandscapeUi(defaultSize, defaultSize2, false);
        }
        else if (this.isPortraitUi()) {
            this.setPortraitUi(defaultSize, defaultSize2, false);
        }
        else {
            this.setLandscapeUi(defaultSize, defaultSize2, false);
        }
        super.onMeasure(n, n2);
    }
    
    public void setUiOrientation(final int mUiOrientation) {
        if (this.mUiOrientation != mUiOrientation) {
            this.mUiOrientation = mUiOrientation;
            this.requestLayout();
        }
    }
}
