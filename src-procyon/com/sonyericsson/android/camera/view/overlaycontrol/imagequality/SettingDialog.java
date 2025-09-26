// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.overlaycontrol.imagequality;

import com.sonyericsson.android.camera.view.setting.dialog.SettingAdapter;
import android.view.MotionEvent;
import android.graphics.Rect;
import android.os.Handler;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import android.view.ViewGroup;
import com.sonyericsson.android.camera.view.setting.dialog.SettingDialogInterface;
import android.widget.RelativeLayout;

abstract class SettingDialog extends RelativeLayout implements SettingDialogInterface
{
    public static final String TAG = "SettingDialog";
    private LayoutCoordinator mLayoutCoordinator;
    protected int mOrientation;
    private ViewGroup mParentView;
    
    public SettingDialog(final Context context, final AttributeSet set) {
        super(context, set);
        this.mOrientation = 2;
    }
    
    public void close() {
        final Handler handler = this.getHandler();
        if (handler != null) {
            handler.post((Runnable)new Runnable(this) {
                final SettingDialog this$0;
                
                @Override
                public void run() {
                    if (this.this$0.mParentView != null) {
                        this.this$0.mParentView.removeView((View)this.this$0);
                    }
                }
            });
        }
    }
    
    public boolean hitTest(final int n, final int n2) {
        final Rect rect = new Rect();
        return this.getGlobalVisibleRect(rect) && rect.contains(n, n2);
    }
    
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        return !this.isEnabled() || super.onInterceptTouchEvent(motionEvent);
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        if (this.mLayoutCoordinator != null) {
            this.mLayoutCoordinator.coordinatePosition(this.mOrientation);
        }
    }
    
    public void open(final ViewGroup viewGroup) {
        this.open(viewGroup, false);
    }
    
    public void open(final ViewGroup mParentView, final boolean b) {
        if (mParentView == null) {
            throw new IllegalArgumentException("Parent view shouldn't be null");
        }
        (this.mParentView = mParentView).addView((View)this);
    }
    
    public abstract void setAdapter(final SettingAdapter p0);
    
    public void setLayoutCoordinator(final LayoutCoordinator mLayoutCoordinator) {
        this.mLayoutCoordinator = mLayoutCoordinator;
    }
    
    public void setSensorOrientation(final int mOrientation) {
        this.mOrientation = mOrientation;
        if (this.mLayoutCoordinator != null) {
            this.mLayoutCoordinator.coordinateSize(mOrientation);
        }
    }
}
