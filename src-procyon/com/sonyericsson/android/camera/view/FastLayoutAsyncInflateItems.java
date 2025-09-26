// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view;

import java.util.ArrayList;
import com.sonyericsson.cameracommon.viewfinder.InflateItem;
import java.util.List;

public class FastLayoutAsyncInflateItems
{
    private static final List<InflateItem> INFLATE_ITEMS_FOR_FAST;
    public static final String TAG = "FastLayoutAsyncInflateItems";
    
    static {
        INFLATE_ITEMS_FOR_FAST = new ArrayList<InflateItem>();
    }
    
    public static List<InflateItem> getInflateItemsForFast() {
        if (FastLayoutAsyncInflateItems.INFLATE_ITEMS_FOR_FAST.size() == 0) {
            FastLayoutAsyncInflateItems.INFLATE_ITEMS_FOR_FAST.add(LayoutAsyncInflateItems.CameraInflateItem.HEAD_UP_DISPLAY);
            FastLayoutAsyncInflateItems.INFLATE_ITEMS_FOR_FAST.add(LayoutAsyncInflateItems.CameraInflateItem.RECTANGLE_FACE);
            FastLayoutAsyncInflateItems.INFLATE_ITEMS_FOR_FAST.add(LayoutAsyncInflateItems.CameraInflateItem.RECTANGLE_FAST_SINGLE);
            FastLayoutAsyncInflateItems.INFLATE_ITEMS_FOR_FAST.add(LayoutAsyncInflateItems.CameraInflateItem.RECTANGLE_FAST_TOUCH);
            FastLayoutAsyncInflateItems.INFLATE_ITEMS_FOR_FAST.add(LayoutAsyncInflateItems.CameraInflateItem.RECTANGLE_FAST_OBJECT_TRACKING);
            FastLayoutAsyncInflateItems.INFLATE_ITEMS_FOR_FAST.add(LayoutAsyncInflateItems.CameraInflateItem.FAST_CAPTURING_VIEWFINDER_ITEMS);
            FastLayoutAsyncInflateItems.INFLATE_ITEMS_FOR_FAST.add(LayoutAsyncInflateItems.CameraInflateItem.AUTO_REVIEW);
        }
        return FastLayoutAsyncInflateItems.INFLATE_ITEMS_FOR_FAST;
    }
}
