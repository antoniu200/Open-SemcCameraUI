package com.sonyericsson.android.camera.view.overlaycontrol.imagequality;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.sonyericsson.android.camera.view.setting.dialog.SettingAdapter;
import com.sonyericsson.android.camera.view.setting.dialog.SettingDialogInterface;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
abstract class SettingDialog extends RelativeLayout implements SettingDialogInterface {
    public static final String TAG = "SettingDialog";
    private LayoutCoordinator mLayoutCoordinator;
    protected int mOrientation;
    private ViewGroup mParentView;

    public abstract void setAdapter(SettingAdapter settingAdapter);

    public SettingDialog(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mOrientation = 2;
    }

    @Override // android.widget.RelativeLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.mLayoutCoordinator != null) {
            this.mLayoutCoordinator.coordinatePosition(this.mOrientation);
        }
    }

    public void setSensorOrientation(int i) {
        this.mOrientation = i;
        if (this.mLayoutCoordinator != null) {
            this.mLayoutCoordinator.coordinateSize(i);
        }
    }

    public void setLayoutCoordinator(LayoutCoordinator layoutCoordinator) {
        this.mLayoutCoordinator = layoutCoordinator;
    }

    public void open(ViewGroup viewGroup) {
        open(viewGroup, false);
    }

    public void open(ViewGroup viewGroup, boolean z) {
        if (viewGroup == null) {
            throw new IllegalArgumentException("Parent view shouldn't be null");
        }
        this.mParentView = viewGroup;
        this.mParentView.addView(this);
    }

    public void close() {
        Handler handler = getHandler();
        if (handler != null) {
            handler.post(new Runnable() { // from class: com.sonyericsson.android.camera.view.overlaycontrol.imagequality.SettingDialog.1
                @Override // java.lang.Runnable
                public void run() {
                    if (SettingDialog.this.mParentView != null) {
                        SettingDialog.this.mParentView.removeView(SettingDialog.this);
                    }
                }
            });
        }
    }

    public boolean hitTest(int i, int i2) {
        Rect rect = new Rect();
        if (getGlobalVisibleRect(rect)) {
            return rect.contains(i, i2);
        }
        return false;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (isEnabled()) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        return true;
    }
}
