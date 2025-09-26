package com.sonyericsson.android.camera.view.sidetouch;

import android.content.Context;
import android.graphics.Point;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import com.sonyericsson.android.camera.view.ViewFinderImpl;
import com.sonyericsson.android.camera.view.sidetouch.SideTouchUiIcon;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SideTouchUi {
    private ViewFinderImpl.AutoReviewContentReceiverProxy mAutoReviewProxy;
    private ViewFinderImpl.SideTouchUiButtonListenerFactory mButtonListenerFactory;
    private final SparseArray<IconLayer> mIconLayer;
    private final boolean mIsOneShot;
    private ViewGroup mMovableArea;
    private final Icon.OnDetachedListener mOnDetachedListener;
    private int mOrientation;
    private ViewFinderImpl.RecordingTimeReceiverProxy mRecordingTimeReceiverProxy;
    private ViewFinderImpl.ZoomBarUpdateProxy mZoomBarUpdateProxy;

    public interface Icon {

        public interface OnDetachedListener {
            void onDetached(Icon icon);
        }

        View attach(ViewGroup viewGroup, Point point);

        void detach(ViewGroup viewGroup);

        void onFocusChanged(boolean z);

        void setOnDetachedListener(OnDetachedListener onDetachedListener);

        void setUiOrientation(int i);

        void show();
    }

    private static class IconLayer {
        private Point mAttachPoint;
        private boolean mFocused;
        private Icon mIcon;
        private Type mIconType;

        private IconLayer() {
            this.mIconType = Type.NONE;
            this.mFocused = false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void invalid() {
            this.mIconType = Type.NONE;
            this.mFocused = false;
            this.mIcon = null;
            this.mAttachPoint = null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isValid() {
            return this.mIcon != null;
        }
    }

    public SideTouchUi(ViewGroup viewGroup) {
        this(viewGroup, false);
    }

    public SideTouchUi(ViewGroup viewGroup, boolean z) {
        this.mIconLayer = new SparseArray<>();
        this.mOnDetachedListener = new Icon.OnDetachedListener() { // from class: com.sonyericsson.android.camera.view.sidetouch.SideTouchUi.1
            @Override // com.sonyericsson.android.camera.view.sidetouch.SideTouchUi.Icon.OnDetachedListener
            public void onDetached(Icon icon) {
                int iFindIconLayerNum = findIconLayerNum(icon);
                if (iFindIconLayerNum != -1) {
                    SideTouchUi.this.destroyIcon(iFindIconLayerNum);
                    SideTouchUi.this.attemptLayerFocusChange(iFindIconLayerNum);
                }
            }

            private int findIconLayerNum(Icon icon) {
                for (int i = 0; i < SideTouchUi.this.mIconLayer.size(); i++) {
                    int iKeyAt = SideTouchUi.this.mIconLayer.keyAt(i);
                    if (((IconLayer) SideTouchUi.this.mIconLayer.get(iKeyAt)).mIcon == icon) {
                        return iKeyAt;
                    }
                }
                return -1;
            }
        };
        this.mMovableArea = viewGroup;
        this.mIsOneShot = z;
    }

    public void setScreenButtonListenerFactory(ViewFinderImpl.SideTouchUiButtonListenerFactory sideTouchUiButtonListenerFactory) {
        this.mButtonListenerFactory = sideTouchUiButtonListenerFactory;
    }

    public void setZoomBarUpdateProxy(ViewFinderImpl.ZoomBarUpdateProxy zoomBarUpdateProxy) {
        this.mZoomBarUpdateProxy = zoomBarUpdateProxy;
    }

    public void setRecordingTimeReceiverProxy(ViewFinderImpl.RecordingTimeReceiverProxy recordingTimeReceiverProxy) {
        this.mRecordingTimeReceiverProxy = recordingTimeReceiverProxy;
    }

    public void setAutoReviewProxy(ViewFinderImpl.AutoReviewContentReceiverProxy autoReviewContentReceiverProxy) {
        this.mAutoReviewProxy = autoReviewContentReceiverProxy;
    }

    public void setUiOrientation(int i) {
        this.mOrientation = i;
        for (int i2 = 0; i2 < this.mIconLayer.size(); i2++) {
            IconLayer iconLayerValueAt = this.mIconLayer.valueAt(i2);
            if (iconLayerValueAt != null && iconLayerValueAt.isValid()) {
                iconLayerValueAt.mIcon.setUiOrientation(this.mOrientation);
            }
        }
    }

    public boolean showIcon() {
        for (int i = 0; i < this.mIconLayer.size(); i++) {
            IconLayer iconLayerValueAt = this.mIconLayer.valueAt(i);
            if (iconLayerValueAt.mFocused) {
                iconLayerValueAt.mIcon.show();
                return true;
            }
        }
        return false;
    }

    public boolean containsIn(Type... typeArr) {
        for (Type type : typeArr) {
            if (compareTo(type)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsAll(Type... typeArr) {
        for (Type type : typeArr) {
            if (!compareTo(type)) {
                return false;
            }
        }
        return true;
    }

    public void attachIcon(Type type, Point point) {
        Icon countDownIcon;
        Context context = this.mMovableArea.getContext();
        switch (type) {
            case CAPTURE_COUNTDOWN:
            case VIDEO_COUNTDOWN:
                countDownIcon = new SideTouchUiIcon.CountDownIcon(context);
                break;
            case SELF_TIMER_COUNTDOWN_CANCEL:
                countDownIcon = new SideTouchUiIcon.SelfTimerCancelIcon(context, this.mOrientation, this.mButtonListenerFactory);
                break;
            case AUTO_REVIEW:
                countDownIcon = new SideTouchUiIcon.AutoReviewIcon(context, this.mAutoReviewProxy);
                countDownIcon.setOnDetachedListener(this.mOnDetachedListener);
                break;
            case ZOOM_BAR:
                countDownIcon = new SideTouchUiIcon.ZoomIcon(context, this.mZoomBarUpdateProxy);
                countDownIcon.setOnDetachedListener(this.mOnDetachedListener);
                break;
            case RECORDING:
                if (!this.mIsOneShot) {
                    countDownIcon = new SideTouchUiIcon.RecordingIcon(context, this.mOrientation, this.mButtonListenerFactory, this.mRecordingTimeReceiverProxy);
                    break;
                } else {
                    countDownIcon = new SideTouchUiIcon.RestrictedRecordingIcon(context, this.mOrientation, this.mButtonListenerFactory, this.mRecordingTimeReceiverProxy);
                    break;
                }
            case RECORDING_HDR:
                countDownIcon = new SideTouchUiIcon.RestrictedRecordingIcon(context, this.mOrientation, this.mButtonListenerFactory, this.mRecordingTimeReceiverProxy);
                break;
            case RECORDING_PAUSE:
                if (!this.mIsOneShot) {
                    countDownIcon = new SideTouchUiIcon.RecordingPauseIcon(context, this.mOrientation, this.mButtonListenerFactory, this.mRecordingTimeReceiverProxy);
                    break;
                } else {
                    countDownIcon = new SideTouchUiIcon.RestrictedRecordingPauseIcon(context, this.mOrientation, this.mButtonListenerFactory, this.mRecordingTimeReceiverProxy);
                    break;
                }
            case RECORDING_HDR_PAUSE:
                countDownIcon = new SideTouchUiIcon.RestrictedRecordingPauseIcon(context, this.mOrientation, this.mButtonListenerFactory, this.mRecordingTimeReceiverProxy);
                break;
            case COVERING:
                SideTouchUiIcon.TransparentCoverIcon transparentCoverIcon = new SideTouchUiIcon.TransparentCoverIcon(context);
                transparentCoverIcon.setOnDetachedListener(this.mOnDetachedListener);
                countDownIcon = transparentCoverIcon;
                point = new Point(0, 0);
                break;
            default:
                return;
        }
        attachInternal(countDownIcon, point, type);
    }

    public boolean detachTo(Type type) {
        if (!compareTo(type)) {
            return false;
        }
        detachIcon(type.layer);
        return true;
    }

    private void detachIcon(int i) {
        Icon icon = getIcon(i);
        if (icon == null) {
            return;
        }
        icon.detach(this.mMovableArea);
    }

    public void destroyIcon() {
        for (int i = 0; i < this.mIconLayer.size(); i++) {
            destroyIcon(this.mIconLayer.keyAt(i));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void destroyIcon(int i) {
        IconLayer iconLayer = this.mIconLayer.get(i);
        removeIconView(i);
        iconLayer.invalid();
    }

    public boolean destroyTo(Type type) {
        if (!compareTo(type)) {
            return false;
        }
        destroyIcon(type.layer);
        return true;
    }

    private boolean compareTo(Type type) {
        return getIconType(type.layer) == type;
    }

    private Type getIconType(int i) {
        if (this.mIconLayer.get(i) == null) {
            return null;
        }
        return this.mIconLayer.get(i).mIconType;
    }

    private Icon getIcon(int i) {
        if (this.mIconLayer.get(i) == null) {
            return null;
        }
        return this.mIconLayer.get(i).mIcon;
    }

    private Point getAttachPoint(int i) {
        if (this.mIconLayer.get(i) == null) {
            return null;
        }
        return this.mIconLayer.get(i).mAttachPoint;
    }

    private IconLayer getIconLayer(int i) {
        IconLayer iconLayer = this.mIconLayer.get(i);
        if (iconLayer != null) {
            return iconLayer;
        }
        IconLayer iconLayer2 = new IconLayer();
        this.mIconLayer.append(i, iconLayer2);
        return iconLayer2;
    }

    private boolean attachInternal(Icon icon, Point point, Type type) {
        Point attachPoint = getAttachPoint(type.layer);
        if (point == null && attachPoint == null) {
            return false;
        }
        removeIconView(type.layer);
        IconLayer iconLayer = getIconLayer(type.layer);
        iconLayer.mIcon = icon;
        iconLayer.mIconType = type;
        if (point == null) {
            iconLayer.mAttachPoint = attachPoint;
        } else {
            iconLayer.mAttachPoint = point;
        }
        View viewAttach = icon.attach(this.mMovableArea, iconLayer.mAttachPoint);
        if (viewAttach != null) {
            viewAttach.setTag(icon);
        }
        icon.setUiOrientation(this.mOrientation);
        requestLayerFocus(type.layer);
        return true;
    }

    private void removeIconView(int i) {
        View viewFindViewWithTag;
        Icon icon = getIcon(i);
        if (icon == null || (viewFindViewWithTag = this.mMovableArea.findViewWithTag(icon)) == null) {
            return;
        }
        this.mMovableArea.removeView(viewFindViewWithTag);
    }

    private void requestLayerFocus(int i) {
        for (int i2 = 0; i2 < this.mIconLayer.size(); i2++) {
            int iKeyAt = this.mIconLayer.keyAt(i2);
            IconLayer iconLayerValueAt = this.mIconLayer.valueAt(i2);
            if (iconLayerValueAt != null && iconLayerValueAt.isValid()) {
                iconLayerValueAt.mFocused = i == iKeyAt;
                iconLayerValueAt.mIcon.onFocusChanged(iconLayerValueAt.mFocused);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void attemptLayerFocusChange(int i) {
        IconLayer iconLayerSearchNextIconLayer = searchNextIconLayer(i);
        if (iconLayerSearchNextIconLayer == null || !iconLayerSearchNextIconLayer.isValid()) {
            return;
        }
        iconLayerSearchNextIconLayer.mFocused = true;
        iconLayerSearchNextIconLayer.mIcon.onFocusChanged(iconLayerSearchNextIconLayer.mFocused);
    }

    private IconLayer searchNextIconLayer(int i) {
        IconLayer iconLayerFindNextIconLayer = findNextIconLayer(i, true);
        return (iconLayerFindNextIconLayer == null || !iconLayerFindNextIconLayer.isValid()) ? findNextIconLayer(i, false) : iconLayerFindNextIconLayer;
    }

    private IconLayer findNextIconLayer(int n, final boolean b) {
        int n2;
        for (int i = 0; i < this.mIconLayer.size(); ++i, n = n2) {
            final int key = this.mIconLayer.keyAt(i);
            if (b) {
                if ((n2 = n) >= key) {
                    continue;
                }
            }
            else if (key >= (n2 = n)) {
                continue;
            }
            n2 = key;
        }
        return (IconLayer)this.mIconLayer.get(n);
    }

    public enum Type {
        NONE(-1),
        CAPTURE_COUNTDOWN(0),
        VIDEO_COUNTDOWN(0),
        SELF_TIMER_COUNTDOWN_CANCEL(0),
        AUTO_REVIEW(0),
        RECORDING(0),
        RECORDING_HDR(0),
        RECORDING_PAUSE(0),
        RECORDING_HDR_PAUSE(0),
        ZOOM_BAR(1),
        COVERING(9);

        final int layer;

        Type(int i) {
            this.layer = i;
        }
    }
}
