// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.utility;

import android.os.Build;

public class BrandConfig
{
    private static final String VERIZON_BRAND = "verizon";
    
    public static boolean isVerizonBrand() {
        return "verizon".equals(Build.BRAND);
    }
}
