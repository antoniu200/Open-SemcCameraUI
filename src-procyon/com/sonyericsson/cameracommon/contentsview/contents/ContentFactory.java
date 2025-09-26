// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.contentsview.contents;

import com.sonyericsson.android.camera.util.CamLog;

public class ContentFactory
{
    public static final String TAG = "ContentFactory";
    
    public static Content create(final Content.ContentInfo contentInfo) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("create() has been called. Content type = ");
            sb.append(contentInfo.mContentType);
            CamLog.d(sb.toString());
        }
        return new Content(contentInfo, ExtraIconResources.get(contentInfo.mContentType), PlayIconResources.get(contentInfo.mContentType));
    }
}
