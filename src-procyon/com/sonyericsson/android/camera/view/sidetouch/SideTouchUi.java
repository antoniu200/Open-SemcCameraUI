// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.sidetouch;

import android.content.Context;
import android.view.View;
import android.graphics.Point;
import android.view.ViewGroup;
import android.util.SparseArray;
import com.sonyericsson.android.camera.view.ViewFinderImpl;

public class SideTouchUi
{
    private ViewFinderImpl.AutoReviewContentReceiverProxy mAutoReviewProxy;
    private ViewFinderImpl.SideTouchUiButtonListenerFactory mButtonListenerFactory;
    private final SparseArray<IconLayer> mIconLayer;
    private final boolean mIsOneShot;
    private ViewGroup mMovableArea;
    private final OnDetachedListener mOnDetachedListener;
    private int mOrientation;
    private ViewFinderImpl.RecordingTimeReceiverProxy mRecordingTimeReceiverProxy;
    private ViewFinderImpl.ZoomBarUpdateProxy mZoomBarUpdateProxy;
    
    public SideTouchUi(final ViewGroup viewGroup) {
        this(viewGroup, false);
    }
    
    public SideTouchUi(final ViewGroup mMovableArea, final boolean mIsOneShot) {
        this.mIconLayer = (SparseArray<IconLayer>)new SparseArray();
        this.mOnDetachedListener = (OnDetachedListener)new OnDetachedListener() {
            final SideTouchUi this$0;
            
            private int findIconLayerNum(final Icon icon) {
                for (int i = 0; i < this.this$0.mIconLayer.size(); ++i) {
                    final int key = this.this$0.mIconLayer.keyAt(i);
                    if (((IconLayer)this.this$0.mIconLayer.get(key)).mIcon == icon) {
                        return key;
                    }
                }
                return -1;
            }
            
            @Override
            public void onDetached(final Icon icon) {
                final int iconLayerNum = this.findIconLayerNum(icon);
                if (iconLayerNum != -1) {
                    this.this$0.destroyIcon(iconLayerNum);
                    this.this$0.attemptLayerFocusChange(iconLayerNum);
                }
            }
        };
        this.mMovableArea = mMovableArea;
        this.mIsOneShot = mIsOneShot;
    }
    
    private boolean attachInternal(final Icon tag, final Point point, final Type type) {
        final Point attachPoint = this.getAttachPoint(type.layer);
        if (point == null && attachPoint == null) {
            return false;
        }
        this.removeIconView(type.layer);
        final IconLayer iconLayer = this.getIconLayer(type.layer);
        iconLayer.mIcon = tag;
        iconLayer.mIconType = type;
        if (point == null) {
            iconLayer.mAttachPoint = attachPoint;
        }
        else {
            iconLayer.mAttachPoint = point;
        }
        final View attach = tag.attach(this.mMovableArea, iconLayer.mAttachPoint);
        if (attach != null) {
            attach.setTag((Object)tag);
        }
        tag.setUiOrientation(this.mOrientation);
        this.requestLayerFocus(type.layer);
        return true;
    }
    
    private void attemptLayerFocusChange(final int n) {
        final IconLayer searchNextIconLayer = this.searchNextIconLayer(n);
        if (searchNextIconLayer != null && searchNextIconLayer.isValid()) {
            searchNextIconLayer.mFocused = true;
            searchNextIconLayer.mIcon.onFocusChanged(searchNextIconLayer.mFocused);
        }
    }
    
    private boolean compareTo(final Type type) {
        return this.getIconType(type.layer) == type;
    }
    
    private void destroyIcon(final int n) {
        final IconLayer iconLayer = (IconLayer)this.mIconLayer.get(n);
        this.removeIconView(n);
        iconLayer.invalid();
    }
    
    private void detachIcon(final int n) {
        final Icon icon = this.getIcon(n);
        if (icon == null) {
            return;
        }
        icon.detach(this.mMovableArea);
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
    
    private Point getAttachPoint(final int n) {
        if (this.mIconLayer.get(n) == null) {
            return null;
        }
        return ((IconLayer)this.mIconLayer.get(n)).mAttachPoint;
    }
    
    private Icon getIcon(final int n) {
        if (this.mIconLayer.get(n) == null) {
            return null;
        }
        return ((IconLayer)this.mIconLayer.get(n)).mIcon;
    }
    
    private IconLayer getIconLayer(final int n) {
        IconLayer iconLayer;
        if ((iconLayer = (IconLayer)this.mIconLayer.get(n)) == null) {
            iconLayer = new IconLayer();
            this.mIconLayer.append(n, (Object)iconLayer);
        }
        return iconLayer;
    }
    
    private Type getIconType(final int n) {
        if (this.mIconLayer.get(n) == null) {
            return null;
        }
        return ((IconLayer)this.mIconLayer.get(n)).mIconType;
    }
    
    private void removeIconView(final int n) {
        final Icon icon = this.getIcon(n);
        if (icon == null) {
            return;
        }
        final View viewWithTag = this.mMovableArea.findViewWithTag((Object)icon);
        if (viewWithTag != null) {
            this.mMovableArea.removeView(viewWithTag);
        }
    }
    
    private void requestLayerFocus(final int n) {
        for (int i = 0; i < this.mIconLayer.size(); ++i) {
            final int key = this.mIconLayer.keyAt(i);
            final IconLayer iconLayer = (IconLayer)this.mIconLayer.valueAt(i);
            if (iconLayer != null && iconLayer.isValid()) {
                iconLayer.mFocused = (n == key);
                iconLayer.mIcon.onFocusChanged(iconLayer.mFocused);
            }
        }
    }
    
    private IconLayer searchNextIconLayer(final int n) {
        final IconLayer nextIconLayer = this.findNextIconLayer(n, true);
        if (nextIconLayer != null) {
            final IconLayer nextIconLayer2 = nextIconLayer;
            if (nextIconLayer.isValid()) {
                return nextIconLayer2;
            }
        }
        return this.findNextIconLayer(n, false);
    }
    
    public void attachIcon(final Type type, Point point) {
        final Context context = this.mMovableArea.getContext();
        SideTouchUiIcon sideTouchUiIcon = null;
        switch (SideTouchUi$2.$SwitchMap$com$sonyericsson$android$camera$view$sidetouch$SideTouchUi$Type[type.ordinal()]) {
            default: {
                return;
            }
            case 10: {
                sideTouchUiIcon = new SideTouchUiIcon.TransparentCoverIcon(context);
                ((Icon)sideTouchUiIcon).setOnDetachedListener(this.mOnDetachedListener);
                point = new Point(0, 0);
                break;
            }
            case 9: {
                sideTouchUiIcon = new SideTouchUiIcon.RestrictedRecordingPauseIcon(context, this.mOrientation, this.mButtonListenerFactory, this.mRecordingTimeReceiverProxy);
                break;
            }
            case 8: {
                if (!this.mIsOneShot) {
                    sideTouchUiIcon = new SideTouchUiIcon.RecordingPauseIcon(context, this.mOrientation, this.mButtonListenerFactory, this.mRecordingTimeReceiverProxy);
                    break;
                }
                sideTouchUiIcon = new SideTouchUiIcon.RestrictedRecordingPauseIcon(context, this.mOrientation, this.mButtonListenerFactory, this.mRecordingTimeReceiverProxy);
                break;
            }
            case 7: {
                sideTouchUiIcon = new SideTouchUiIcon.RestrictedRecordingIcon(context, this.mOrientation, this.mButtonListenerFactory, this.mRecordingTimeReceiverProxy);
                break;
            }
            case 6: {
                if (!this.mIsOneShot) {
                    sideTouchUiIcon = new SideTouchUiIcon.RecordingIcon(context, this.mOrientation, this.mButtonListenerFactory, this.mRecordingTimeReceiverProxy);
                    break;
                }
                sideTouchUiIcon = new SideTouchUiIcon.RestrictedRecordingIcon(context, this.mOrientation, this.mButtonListenerFactory, this.mRecordingTimeReceiverProxy);
                break;
            }
            case 5: {
                sideTouchUiIcon = new SideTouchUiIcon.ZoomIcon(context, this.mZoomBarUpdateProxy);
                ((Icon)sideTouchUiIcon).setOnDetachedListener(this.mOnDetachedListener);
                break;
            }
            case 4: {
                sideTouchUiIcon = new SideTouchUiIcon.AutoReviewIcon(context, this.mAutoReviewProxy);
                ((Icon)sideTouchUiIcon).setOnDetachedListener(this.mOnDetachedListener);
                break;
            }
            case 3: {
                sideTouchUiIcon = new SideTouchUiIcon.SelfTimerCancelIcon(context, this.mOrientation, this.mButtonListenerFactory);
                break;
            }
            case 1:
            case 2: {
                sideTouchUiIcon = new SideTouchUiIcon.CountDownIcon(context);
                break;
            }
        }
        this.attachInternal((Icon)sideTouchUiIcon, point, type);
    }
    
    public boolean containsAll(final Type... array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            if (!this.compareTo(array[i])) {
                return false;
            }
        }
        return true;
    }
    
    public boolean containsIn(final Type... array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            if (this.compareTo(array[i])) {
                return true;
            }
        }
        return false;
    }
    
    public void destroyIcon() {
        for (int i = 0; i < this.mIconLayer.size(); ++i) {
            this.destroyIcon(this.mIconLayer.keyAt(i));
        }
    }
    
    public boolean destroyTo(final Type type) {
        if (this.compareTo(type)) {
            this.destroyIcon(type.layer);
            return true;
        }
        return false;
    }
    
    public boolean detachTo(final Type type) {
        if (this.compareTo(type)) {
            this.detachIcon(type.layer);
            return true;
        }
        return false;
    }
    
    public void setAutoReviewProxy(final ViewFinderImpl.AutoReviewContentReceiverProxy mAutoReviewProxy) {
        this.mAutoReviewProxy = mAutoReviewProxy;
    }
    
    public void setRecordingTimeReceiverProxy(final ViewFinderImpl.RecordingTimeReceiverProxy mRecordingTimeReceiverProxy) {
        this.mRecordingTimeReceiverProxy = mRecordingTimeReceiverProxy;
    }
    
    public void setScreenButtonListenerFactory(final ViewFinderImpl.SideTouchUiButtonListenerFactory mButtonListenerFactory) {
        this.mButtonListenerFactory = mButtonListenerFactory;
    }
    
    public void setUiOrientation(int i) {
        this.mOrientation = i;
        IconLayer iconLayer;
        for (i = 0; i < this.mIconLayer.size(); ++i) {
            iconLayer = (IconLayer)this.mIconLayer.valueAt(i);
            if (iconLayer != null && iconLayer.isValid()) {
                iconLayer.mIcon.setUiOrientation(this.mOrientation);
            }
        }
    }
    
    public void setZoomBarUpdateProxy(final ViewFinderImpl.ZoomBarUpdateProxy mZoomBarUpdateProxy) {
        this.mZoomBarUpdateProxy = mZoomBarUpdateProxy;
    }
    
    public boolean showIcon() {
        for (int i = 0; i < this.mIconLayer.size(); ++i) {
            final IconLayer iconLayer = (IconLayer)this.mIconLayer.valueAt(i);
            if (iconLayer.mFocused) {
                iconLayer.mIcon.show();
                return true;
            }
        }
        return false;
    }
    
    public interface Icon
    {
        View attach(final ViewGroup p0, final Point p1);
        
        void detach(final ViewGroup p0);
        
        void onFocusChanged(final boolean p0);
        
        void setOnDetachedListener(final OnDetachedListener p0);
        
        void setUiOrientation(final int p0);
        
        void show();
        
        public interface OnDetachedListener
        {
            void onDetached(final Icon p0);
        }
    }
    
    private static class IconLayer
    {
        private Point mAttachPoint;
        private boolean mFocused;
        private Icon mIcon;
        private Type mIconType;
        
        private IconLayer() {
            this.mIconType = Type.NONE;
            this.mFocused = false;
        }
        
        private void invalid() {
            this.mIconType = Type.NONE;
            this.mFocused = false;
            this.mIcon = null;
            this.mAttachPoint = null;
        }
        
        private boolean isValid() {
            return this.mIcon != null;
        }
    }
    
    public enum Type
    {
        private static final Type[] $VALUES;
        
        AUTO_REVIEW(0), 
        CAPTURE_COUNTDOWN(0), 
        COVERING(9), 
        NONE(-1), 
        RECORDING(0), 
        RECORDING_HDR(0), 
        RECORDING_HDR_PAUSE(0), 
        RECORDING_PAUSE(0), 
        SELF_TIMER_COUNTDOWN_CANCEL(0), 
        VIDEO_COUNTDOWN(0), 
        ZOOM_BAR(1);
        
        final int layer;
        
        static {
            $VALUES = new Type[] { Type.NONE, Type.CAPTURE_COUNTDOWN, Type.VIDEO_COUNTDOWN, Type.SELF_TIMER_COUNTDOWN_CANCEL, Type.AUTO_REVIEW, Type.RECORDING, Type.RECORDING_HDR, Type.RECORDING_PAUSE, Type.RECORDING_HDR_PAUSE, Type.ZOOM_BAR, Type.COVERING };
        }
        
        private Type(final int layer) {
            this.layer = layer;
        }
    }
}
