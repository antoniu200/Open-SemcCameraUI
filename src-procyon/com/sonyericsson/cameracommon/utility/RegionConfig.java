// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.utility;

import android.content.Context;

public class RegionConfig
{
    private static final String CHINA_REGION_PACKAGE = "com.sonymobile.cta";
    
    public static boolean isChinaRegion(final Context context) {
        return CommonUtility.isPackageExist("com.sonymobile.cta", context);
    }
}
