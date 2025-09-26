// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.data;

public interface Freezable<T>
{
    T freeze();
    
    boolean isDataValid();
}
