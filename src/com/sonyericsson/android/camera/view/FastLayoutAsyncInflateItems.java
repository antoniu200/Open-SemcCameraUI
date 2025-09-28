package com.sonyericsson.android.camera.view;

import com.sonyericsson.android.camera.view.LayoutAsyncInflateItems;
import com.sonyericsson.cameracommon.viewfinder.InflateItem;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class FastLayoutAsyncInflateItems {
    private static final List<InflateItem> INFLATE_ITEMS_FOR_FAST = new ArrayList();
    public static final String TAG = "FastLayoutAsyncInflateItems";

    public static List<InflateItem> getInflateItemsForFast() {
        if (INFLATE_ITEMS_FOR_FAST.size() == 0) {
            INFLATE_ITEMS_FOR_FAST.add(LayoutAsyncInflateItems.CameraInflateItem.HEAD_UP_DISPLAY);
            INFLATE_ITEMS_FOR_FAST.add(LayoutAsyncInflateItems.CameraInflateItem.RECTANGLE_FACE);
            INFLATE_ITEMS_FOR_FAST.add(LayoutAsyncInflateItems.CameraInflateItem.RECTANGLE_FAST_SINGLE);
            INFLATE_ITEMS_FOR_FAST.add(LayoutAsyncInflateItems.CameraInflateItem.RECTANGLE_FAST_TOUCH);
            INFLATE_ITEMS_FOR_FAST.add(LayoutAsyncInflateItems.CameraInflateItem.RECTANGLE_FAST_OBJECT_TRACKING);
            INFLATE_ITEMS_FOR_FAST.add(LayoutAsyncInflateItems.CameraInflateItem.FAST_CAPTURING_VIEWFINDER_ITEMS);
            INFLATE_ITEMS_FOR_FAST.add(LayoutAsyncInflateItems.CameraInflateItem.AUTO_REVIEW);
        }
        return INFLATE_ITEMS_FOR_FAST;
    }
}
