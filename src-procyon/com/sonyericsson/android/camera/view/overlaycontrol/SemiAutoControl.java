// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.overlaycontrol;

import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import android.view.ViewGroup;
import com.sonyericsson.android.camera.view.overlaycontrol.semiauto.SemiAutoControlView;

public class SemiAutoControl extends OverlayControl
{
    private ValueAccessor<Float> mBrightness;
    private ValueAccessor<Float> mColor;
    private final SemiAutoControlView mView;
    
    public SemiAutoControl(final ViewGroup viewGroup, final LayoutDependencyResolver.ScreenAspect screenAspect, final StateListener stateListener, final ValueAccessor<Float> mColor, final ValueAccessor<Float> mBrightness, final boolean b) {
        super(stateListener);
        (this.mView = new SemiAutoControlView(viewGroup, screenAspect)).setOnSemiAutoChangeListener((SemiAutoControlView.OnSemiAutoChangeListener)new OnSemiAutoChangeListenerImpl());
        this.mView.setExpanded(b ^ true);
        this.mColor = mColor;
        this.mBrightness = mBrightness;
    }
    
    @Override
    public void disable() {
        super.disable();
        this.mView.disable();
    }
    
    @Override
    public void enable() {
        super.enable();
        this.mView.enable();
    }
    
    @Override
    protected void onOrientationChanged(final int orientation) {
        this.mView.setOrientation(orientation);
    }
    
    @Override
    protected void onVisibilityUpdated() {
        if (this.isVisible()) {
            this.mView.setVisibility(0);
        }
        else {
            this.mView.setVisibility(4);
        }
    }
    
    @Override
    public void refresh() {
    }
    
    @Override
    public void release() {
        this.mView.release();
    }
    
    private class OnSemiAutoChangeListenerImpl implements OnSemiAutoChangeListener
    {
        final SemiAutoControl this$0;
        
        private OnSemiAutoChangeListenerImpl(final SemiAutoControl this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onAmberBlueColorChanged(final int n) {
            if (this.this$0.mColor != null) {
                this.this$0.mColor.set(n / 100.0f);
            }
        }
        
        @Override
        public void onBrightnessChanged(final int n) {
            if (this.this$0.mBrightness != null) {
                this.this$0.mBrightness.set(n / 100.0f);
            }
        }
        
        @Override
        public void onSemiAutoControlStarted() {
            this.this$0.notifyValueUpdateStart();
        }
        
        @Override
        public void onSemiAutoControlStopped() {
            this.this$0.notifyValueUpdateEnd();
        }
        
        @Override
        public void onSemiAutoDisabled() {
        }
        
        @Override
        public void onSemiAutoEnabled() {
        }
        
        @Override
        public void onSemiAutoReset() {
            if (this.this$0.mColor != null) {
                this.this$0.mColor.reset();
            }
            if (this.this$0.mBrightness != null) {
                this.this$0.mBrightness.reset();
            }
        }
    }
}
