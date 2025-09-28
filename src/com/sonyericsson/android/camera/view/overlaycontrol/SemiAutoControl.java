package com.sonyericsson.android.camera.view.overlaycontrol;

import android.view.ViewGroup;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import com.sonyericsson.android.camera.view.overlaycontrol.OverlayControl;
import com.sonyericsson.android.camera.view.overlaycontrol.semiauto.SemiAutoControlView;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SemiAutoControl extends OverlayControl {
    private ValueAccessor<Float> mBrightness;
    private ValueAccessor<Float> mColor;
    private final SemiAutoControlView mView;

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.OverlayControl
    public void refresh() {
    }

    public SemiAutoControl(ViewGroup viewGroup, LayoutDependencyResolver.ScreenAspect screenAspect, OverlayControl.StateListener stateListener, ValueAccessor<Float> valueAccessor, ValueAccessor<Float> valueAccessor2, boolean z) {
        super(stateListener);
        this.mView = new SemiAutoControlView(viewGroup, screenAspect);
        this.mView.setOnSemiAutoChangeListener(new OnSemiAutoChangeListenerImpl());
        this.mView.setExpanded(!z);
        this.mColor = valueAccessor;
        this.mBrightness = valueAccessor2;
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.OverlayControl
    public void enable() {
        super.enable();
        this.mView.enable();
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.OverlayControl
    public void disable() {
        super.disable();
        this.mView.disable();
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.OverlayControl
    public void release() {
        this.mView.release();
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.OverlayControl
    protected void onVisibilityUpdated() {
        if (isVisible()) {
            this.mView.setVisibility(0);
        } else {
            this.mView.setVisibility(4);
        }
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.OverlayControl
    protected void onOrientationChanged(int i) {
        this.mView.setOrientation(i);
    }

    private class OnSemiAutoChangeListenerImpl implements SemiAutoControlView.OnSemiAutoChangeListener {
        @Override // com.sonyericsson.android.camera.view.overlaycontrol.semiauto.SemiAutoControlView.OnSemiAutoChangeListener
        public void onSemiAutoDisabled() {
        }

        @Override // com.sonyericsson.android.camera.view.overlaycontrol.semiauto.SemiAutoControlView.OnSemiAutoChangeListener
        public void onSemiAutoEnabled() {
        }

        private OnSemiAutoChangeListenerImpl() {
        }

        @Override // com.sonyericsson.android.camera.view.overlaycontrol.semiauto.SemiAutoControlView.OnSemiAutoChangeListener
        public void onSemiAutoControlStarted() {
            SemiAutoControl.this.notifyValueUpdateStart();
        }

        @Override // com.sonyericsson.android.camera.view.overlaycontrol.semiauto.SemiAutoControlView.OnSemiAutoChangeListener
        public void onSemiAutoControlStopped() {
            SemiAutoControl.this.notifyValueUpdateEnd();
        }

        @Override // com.sonyericsson.android.camera.view.overlaycontrol.semiauto.SemiAutoControlView.OnSemiAutoChangeListener
        public void onBrightnessChanged(int i) {
            if (SemiAutoControl.this.mBrightness != null) {
                SemiAutoControl.this.mBrightness.set(Float.valueOf(i / 100.0f));
            }
        }

        @Override // com.sonyericsson.android.camera.view.overlaycontrol.semiauto.SemiAutoControlView.OnSemiAutoChangeListener
        public void onAmberBlueColorChanged(int i) {
            if (SemiAutoControl.this.mColor != null) {
                SemiAutoControl.this.mColor.set(Float.valueOf(i / 100.0f));
            }
        }

        @Override // com.sonyericsson.android.camera.view.overlaycontrol.semiauto.SemiAutoControlView.OnSemiAutoChangeListener
        public void onSemiAutoReset() {
            if (SemiAutoControl.this.mColor != null) {
                SemiAutoControl.this.mColor.reset();
            }
            if (SemiAutoControl.this.mBrightness != null) {
                SemiAutoControl.this.mBrightness.reset();
            }
        }
    }
}
