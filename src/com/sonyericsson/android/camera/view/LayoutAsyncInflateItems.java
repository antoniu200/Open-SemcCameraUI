package com.sonyericsson.android.camera.view;

import com.sonyericsson.android.camera.R;
import com.sonyericsson.cameracommon.viewfinder.InflateItem;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class LayoutAsyncInflateItems {
    public static final String TAG = "LayoutAsyncInflateItems";

    public static final void preload() {
    }

    public enum CameraInflateItem implements InflateItem {
        HEAD_UP_DISPLAY(R.layout.baselayout, 1),
        RECTANGLE_FACE(R.layout.face_rectangle, 5),
        RECTANGLE_FAST_SINGLE(R.layout.fast_capturing_auto_focus_rectangles, 1),
        RECTANGLE_FAST_TOUCH(R.layout.fast_capturing_auto_focus_rectangles, 1),
        RECTANGLE_FAST_OBJECT_TRACKING(R.layout.af_rectangle, 1),
        AUTO_REVIEW(R.layout.instant_viewer, 1),
        FAST_CAPTURING_VIEWFINDER_ITEMS(R.layout.fast_capturing_viewfinder_items, 1);

        protected final int mInflateId;
        protected final int mViewCount;

        CameraInflateItem(int i, int i2) {
            this.mInflateId = i;
            this.mViewCount = i2;
        }

        @Override // com.sonyericsson.cameracommon.viewfinder.InflateItem
        public int getLayoutId() {
            return this.mInflateId;
        }

        @Override // com.sonyericsson.cameracommon.viewfinder.InflateItem
        public int getViewCount() {
            return this.mViewCount;
        }
    }
}
