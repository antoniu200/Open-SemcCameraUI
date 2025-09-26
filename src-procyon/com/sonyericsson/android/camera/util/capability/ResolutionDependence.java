// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.util.capability;

import android.content.Context;

public class ResolutionDependence
{
    public static final String TAG = "ResolutionDependence";
    
    public static boolean isDependOnAspect(final Context context) {
        return context.getResources().getBoolean(2131034122);
    }
}
