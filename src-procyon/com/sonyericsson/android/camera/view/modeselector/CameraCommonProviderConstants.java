// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.modeselector;

import android.provider.BaseColumns;
import android.net.Uri;

public class CameraCommonProviderConstants
{
    public static final String AUTHORITY = "com.sonymobile.camerauicommon.provider";
    private static final Uri BASE_URI;
    public static final Uri CAPTURINGMODE_CONTENT_URI;
    public static final String CAPTURINGMODE_TABLE = "capturingmodes";
    
    static {
        BASE_URI = Uri.parse("content://com.sonymobile.camerauicommon.provider");
        CAPTURINGMODE_CONTENT_URI = Uri.withAppendedPath(CameraCommonProviderConstants.BASE_URI, "capturingmodes");
    }
    
    private CameraCommonProviderConstants() {
    }
    
    public interface CapturingModeColumns extends BaseColumns
    {
        public static final String ACTIVITY = "activity";
        public static final String CAPTURE_TYPE = "capture_type";
        public static final String DESCRIPTIONLABEL_ID = "descriptionlabel_id";
        public static final String MODE_NAME = "mode_name";
        public static final String PACKAGE = "package";
        public static final String SELECTORICON_ID = "selectoricon_id";
        public static final String SELECTORLABEL_ID = "selectorlabel_id";
        public static final String SHORTCUTICON_ID = "shortcuticon_id";
        public static final String SHORTCUTLABEL_ID = "shortcutlabel_id";
        public static final String SORT_ORDER = "sort_order";
        public static final String VISIBILITY_NORMAL = "visibility_normal";
        public static final String VISIBILITY_ONESHOT = "visibility_oneshot";
        public static final String VISIBILITY_SHORTCUT = "visibility_shortcut";
    }
}
