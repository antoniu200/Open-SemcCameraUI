// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.controller;

import android.graphics.Rect;

public class ChapterThumbnail
{
    public final Integer format;
    private int mOrientation;
    public final Rect rect;
    public final byte[] yuvData;
    
    public ChapterThumbnail(final byte[] yuvData, final Integer format, final Rect rect) {
        this.yuvData = yuvData;
        this.format = format;
        this.rect = rect;
        this.mOrientation = 0;
    }
    
    public int orientation() {
        return this.mOrientation;
    }
    
    public void setOrientation(final int mOrientation) {
        this.mOrientation = mOrientation;
    }
}
