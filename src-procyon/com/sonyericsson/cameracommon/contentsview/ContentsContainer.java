// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.contentsview;

import com.sonyericsson.android.camera.util.CamLog;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.FrameLayout;

public class ContentsContainer extends FrameLayout
{
    public static final String TAG = "ContentsContainer";
    
    public ContentsContainer(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public void cancelRequestHide() {
        for (int i = 0; i < this.getChildCount(); ++i) {
            ((ContentPallet)this.getChildAt(i)).cancelRequestHide();
        }
    }
    
    public void disableClick() {
        for (int i = 0; i < this.getChildCount(); ++i) {
            ((ContentPallet)this.getChildAt(i)).disableClick();
        }
    }
    
    public void enableClick() {
        for (int i = 0; i < this.getChildCount(); ++i) {
            ((ContentPallet)this.getChildAt(i)).enableClick();
        }
    }
    
    public void pause() {
        for (int i = 0; i < this.getChildCount(); ++i) {
            ((ContentPallet)this.getChildAt(i)).release();
        }
        this.removeAllViews();
    }
    
    public void setSensorOrientation(int i) {
        final boolean verbose = CamLog.VERBOSE;
        int j = 0;
        if (verbose) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setSensorOrientation() has been called. orientation = ");
            sb.append(i);
            CamLog.d("ContentsContainer", sb.toString());
        }
        if (i == 1) {
            i = -90;
        }
        else {
            i = 0;
        }
        while (j < this.getChildCount()) {
            this.getChildAt(j).setRotation((float)i);
            ++j;
        }
    }
}
