package com.sonyericsson.cameracommon.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ControlSwitch extends Switch {
    public static final String TAG = "ControlSwitch";
    private boolean mIsUpsideDown;

    public ControlSwitch(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public synchronized void setUiOrientation(int i) {
        switch (i) {
            case 1:
                if (!this.mIsUpsideDown) {
                    reverseChildrenViews(this);
                    this.mText.setRotation(-90.0f);
                    synchronized (this.mSwitchBundle) {
                        this.mSwitchBundle.setRotation(-90.0f);
                    }
                    this.mIsUpsideDown = true;
                }
                break;
            case 2:
                if (this.mIsUpsideDown) {
                    reverseChildrenViews(this);
                    this.mText.setRotation(0.0f);
                    synchronized (this.mSwitchBundle) {
                        this.mSwitchBundle.setRotation(0.0f);
                    }
                    this.mIsUpsideDown = false;
                }
                break;
        }
    }

    private void reverseChildrenViews(ViewGroup viewGroup) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            arrayList.add(viewGroup.getChildAt(i));
        }
        viewGroup.removeAllViews();
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            viewGroup.addView((View) arrayList.get(size));
        }
    }
}
