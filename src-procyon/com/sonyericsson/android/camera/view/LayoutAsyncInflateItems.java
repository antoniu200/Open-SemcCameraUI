// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view;

import com.sonyericsson.cameracommon.viewfinder.InflateItem;

public class LayoutAsyncInflateItems
{
    public static final String TAG = "LayoutAsyncInflateItems";
    
    public static final void preload() {
    }
    
    public enum CameraInflateItem implements InflateItem
    {
        private static final CameraInflateItem[] $VALUES;
        
        AUTO_REVIEW(2131492936, 1), 
        FAST_CAPTURING_VIEWFINDER_ITEMS(2131492925, 1), 
        HEAD_UP_DISPLAY(2131492899, 1), 
        RECTANGLE_FACE(2131492923, 5), 
        RECTANGLE_FAST_OBJECT_TRACKING(2131492894, 1), 
        RECTANGLE_FAST_SINGLE(2131492924, 1), 
        RECTANGLE_FAST_TOUCH(2131492924, 1);
        
        protected final int mInflateId;
        protected final int mViewCount;
        
        static {
            $VALUES = new CameraInflateItem[] { CameraInflateItem.HEAD_UP_DISPLAY, CameraInflateItem.RECTANGLE_FACE, CameraInflateItem.RECTANGLE_FAST_SINGLE, CameraInflateItem.RECTANGLE_FAST_TOUCH, CameraInflateItem.RECTANGLE_FAST_OBJECT_TRACKING, CameraInflateItem.AUTO_REVIEW, CameraInflateItem.FAST_CAPTURING_VIEWFINDER_ITEMS };
        }
        
        private CameraInflateItem(final int mInflateId, final int mViewCount) {
            this.mInflateId = mInflateId;
            this.mViewCount = mViewCount;
        }
        
        @Override
        public int getLayoutId() {
            return this.mInflateId;
        }
        
        @Override
        public int getViewCount() {
            return this.mViewCount;
        }
    }
}
