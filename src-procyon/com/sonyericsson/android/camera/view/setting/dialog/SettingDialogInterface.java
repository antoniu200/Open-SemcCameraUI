// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.setting.dialog;

import android.view.ViewGroup;

public interface SettingDialogInterface
{
    void close();
    
    boolean hitTest(final int p0, final int p1);
    
    void open(final ViewGroup p0);
    
    void setEnabled(final boolean p0);
    
    void setSensorOrientation(final int p0);
}
