package com.sonyericsson.cameracommon.contentsview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.sonyericsson.android.camera.util.CamLog;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ContentsContainer extends FrameLayout {
    public static final String TAG = "ContentsContainer";

    public ContentsContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setSensorOrientation(int i) {
        if (CamLog.VERBOSE) {
            CamLog.d(TAG, "setSensorOrientation() has been called. orientation = " + i);
        }
        int i2 = i == 1 ? -90 : 0;
        for (int i3 = 0; i3 < getChildCount(); i3++) {
            getChildAt(i3).setRotation(i2);
        }
    }

    public void pause() {
        for (int i = 0; i < getChildCount(); i++) {
            ((ContentPallet) getChildAt(i)).release();
        }
        removeAllViews();
    }

    public void enableClick() {
        for (int i = 0; i < getChildCount(); i++) {
            ((ContentPallet) getChildAt(i)).enableClick();
        }
    }

    public void disableClick() {
        for (int i = 0; i < getChildCount(); i++) {
            ((ContentPallet) getChildAt(i)).disableClick();
        }
    }

    public void cancelRequestHide() {
        for (int i = 0; i < getChildCount(); i++) {
            ((ContentPallet) getChildAt(i)).cancelRequestHide();
        }
    }
}
