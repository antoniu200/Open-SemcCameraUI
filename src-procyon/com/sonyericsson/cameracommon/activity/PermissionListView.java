// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.activity;

import android.view.View$MeasureSpec;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.ListView;

public class PermissionListView extends ListView
{
    public PermissionListView(final Context context) {
        super(context);
    }
    
    public PermissionListView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public PermissionListView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(n, View$MeasureSpec.makeMeasureSpec(16777215, Integer.MIN_VALUE));
        this.getLayoutParams().height = this.getMeasuredHeight();
    }
}
