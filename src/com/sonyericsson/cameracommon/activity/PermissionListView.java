package com.sonyericsson.cameracommon.activity;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class PermissionListView extends ListView {
    public PermissionListView(Context context) {
        super(context);
    }

    public PermissionListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public PermissionListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // android.widget.ListView, android.widget.AbsListView, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(ViewCompat.MEASURED_SIZE_MASK, Integer.MIN_VALUE));
        getLayoutParams().height = getMeasuredHeight();
    }
}
