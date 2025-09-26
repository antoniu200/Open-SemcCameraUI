package com.sonyericsson.cameracommon.rotatableview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class EdgeRotatableContainerView extends FrameLayout {
    private int mOrientation;

    public EdgeRotatableContainerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mOrientation = 2;
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        setOrientation(getMeasuredWidth(), getMeasuredHeight());
    }

    public void setOrientation(int i) {
        this.mOrientation = i;
        setOrientation(getWidth(), getHeight());
    }

    private void setOrientation(float f, float f2) {
        if (this.mOrientation == 2) {
            setRotation(0.0f);
            setTranslationX(0.0f);
            setTranslationY(0.0f);
        } else {
            setRotation(-90.0f);
            float f3 = (f - f2) / 2.0f;
            setTranslationX(f3);
            setTranslationY(f3);
        }
    }
}
