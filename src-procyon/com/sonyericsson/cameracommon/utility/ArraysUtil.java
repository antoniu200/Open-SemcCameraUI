// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.utility;

public class ArraysUtil
{
    public static final String TAG = "ArraysUtil";
    
    public static void swap(final float[] array, final int n, final int n2) {
        final float n3 = array[n2];
        array[n2] = array[n];
        array[n] = n3;
    }
}
