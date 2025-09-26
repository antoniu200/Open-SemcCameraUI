// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.focusview;

import android.graphics.Rect;

public class NamedFace
{
    public final Rect mFacePosition;
    public final String mName;
    public final int mSmileScore;
    public final String mUuid;
    
    public NamedFace(final String mName, final String mUuid, final Rect mFacePosition, final int mSmileScore) {
        this.mName = mName;
        this.mUuid = mUuid;
        this.mFacePosition = mFacePosition;
        this.mSmileScore = mSmileScore;
    }
}
