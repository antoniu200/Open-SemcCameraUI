package com.sonyericsson.android.camera.view.modeselector;

import android.net.Uri;
import android.provider.BaseColumns;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class CameraCommonProviderConstants {
    public static final String AUTHORITY = "com.sonymobile.camerauicommon.provider";
    private static final Uri BASE_URI = Uri.parse("content://com.sonymobile.camerauicommon.provider");
    public static final Uri CAPTURINGMODE_CONTENT_URI = Uri.withAppendedPath(BASE_URI, "capturingmodes");
    public static final String CAPTURINGMODE_TABLE = "capturingmodes";

    public interface CapturingModeColumns extends BaseColumns {
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

    private CameraCommonProviderConstants() {
    }
}
