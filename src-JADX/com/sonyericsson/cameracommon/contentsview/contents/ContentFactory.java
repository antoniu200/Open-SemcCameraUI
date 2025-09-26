package com.sonyericsson.cameracommon.contentsview.contents;

import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.contentsview.contents.Content;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ContentFactory {
    public static final String TAG = "ContentFactory";

    public static Content create(Content.ContentInfo contentInfo) {
        if (CamLog.VERBOSE) {
            CamLog.d("create() has been called. Content type = " + contentInfo.mContentType);
        }
        return new Content(contentInfo, ExtraIconResources.get(contentInfo.mContentType), PlayIconResources.get(contentInfo.mContentType));
    }
}
