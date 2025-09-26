package com.sonyericsson.android.camera.view.overlaycontrol.imagequality;

import android.graphics.Rect;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;
import com.sonyericsson.cameracommon.utility.ResourceUtil;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class ControlLayoutCoordinator implements LayoutCoordinator {
    private final Rect mContainerRect;
    public Rect mDialogRect;
    private final LayoutDependencyResolver.ScreenAspect mScreenAspect;
    private final ImageQualityControlView mView;

    public ControlLayoutCoordinator(ImageQualityControlView imageQualityControlView, Rect rect, LayoutDependencyResolver.ScreenAspect screenAspect) {
        this.mView = imageQualityControlView;
        this.mContainerRect = rect;
        this.mScreenAspect = screenAspect;
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.imagequality.LayoutCoordinator
    public void coordinatePosition(int i) {
        if (LayoutDependencyResolver.isTablet(this.mView.getContext())) {
            coordinatePositionTablet();
        } else {
            coordinatePositionPhone();
        }
    }

    private void coordinatePositionPhone() {
        int iHeight = LayoutDependencyResolver.getViewFinderSize(this.mView.getContext()).height();
        int dimensionPixelSize = this.mScreenAspect == LayoutDependencyResolver.ScreenAspect.EIGHTEEN_NINE ? ResourceUtil.getDimensionPixelSize(this.mView.getContext(), this.mView.getContext().getPackageName(), R.dimen.left_icon_area_height) : 0;
        if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
            this.mView.setX((((iHeight * 4) / 3) + dimensionPixelSize) - this.mView.getWidth());
            this.mView.setY(this.mContainerRect.top + ((this.mContainerRect.width() - this.mView.getHeight()) / 2.0f));
        } else {
            this.mView.setX((((iHeight * 4) / 3) + dimensionPixelSize) - this.mView.getWidth());
            this.mView.setY(this.mContainerRect.top + ((this.mContainerRect.height() - this.mView.getHeight()) / 2.0f));
        }
        this.mDialogRect = new Rect((int) this.mView.getX(), (int) this.mView.getY(), ((int) this.mView.getX()) + this.mView.getLayoutParams().width, ((int) this.mView.getY()) + this.mView.getLayoutParams().height);
    }

    private void coordinatePositionTablet() {
        coordinatePositionPhone();
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.imagequality.LayoutCoordinator
    public void coordinateSize(int i) {
        this.mView.getLayoutParams().width = -2;
        this.mView.getLayoutParams().height = -2;
    }
}
