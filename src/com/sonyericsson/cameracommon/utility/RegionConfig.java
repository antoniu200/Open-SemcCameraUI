package com.sonyericsson.cameracommon.utility;

import android.content.Context;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class RegionConfig {
    private static final String CHINA_REGION_PACKAGE = "com.sonymobile.cta";

    public static boolean isChinaRegion(Context context) {
        return CommonUtility.isPackageExist(CHINA_REGION_PACKAGE, context);
    }
}
