// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.overlaycontrol.imagequality;

import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;
import com.sonyericsson.cameracommon.utility.ResourceUtil;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import android.graphics.Rect;

class ControlLayoutCoordinator implements LayoutCoordinator
{
    private final Rect mContainerRect;
    public Rect mDialogRect;
    private final LayoutDependencyResolver.ScreenAspect mScreenAspect;
    private final ImageQualityControlView mView;
    
    public ControlLayoutCoordinator(final ImageQualityControlView mView, final Rect mContainerRect, final LayoutDependencyResolver.ScreenAspect mScreenAspect) {
        this.mView = mView;
        this.mContainerRect = mContainerRect;
        this.mScreenAspect = mScreenAspect;
    }
    
    private void coordinatePositionPhone() {
        final int height = LayoutDependencyResolver.getViewFinderSize(this.mView.getContext()).height();
        int dimensionPixelSize;
        if (this.mScreenAspect == LayoutDependencyResolver.ScreenAspect.EIGHTEEN_NINE) {
            dimensionPixelSize = ResourceUtil.getDimensionPixelSize(this.mView.getContext(), this.mView.getContext().getPackageName(), 2131165428);
        }
        else {
            dimensionPixelSize = 0;
        }
        if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
            this.mView.setX((float)(height * 4 / 3 + dimensionPixelSize - this.mView.getWidth()));
            this.mView.setY(this.mContainerRect.top + (this.mContainerRect.width() - this.mView.getHeight()) / 2.0f);
        }
        else {
            this.mView.setX((float)(height * 4 / 3 + dimensionPixelSize - this.mView.getWidth()));
            this.mView.setY(this.mContainerRect.top + (this.mContainerRect.height() - this.mView.getHeight()) / 2.0f);
        }
        this.mDialogRect = new Rect((int)this.mView.getX(), (int)this.mView.getY(), (int)this.mView.getX() + this.mView.getLayoutParams().width, (int)this.mView.getY() + this.mView.getLayoutParams().height);
    }
    
    private void coordinatePositionTablet() {
        this.coordinatePositionPhone();
    }
    
    @Override
    public void coordinatePosition(final int n) {
        if (LayoutDependencyResolver.isTablet(this.mView.getContext())) {
            this.coordinatePositionTablet();
        }
        else {
            this.coordinatePositionPhone();
        }
    }
    
    @Override
    public void coordinateSize(final int n) {
        this.mView.getLayoutParams().width = -2;
        this.mView.getLayoutParams().height = -2;
    }
}
