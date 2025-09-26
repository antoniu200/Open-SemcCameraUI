// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.mediasaving.updator;

import android.content.ContentValues;

public final class CrUpdateParameter
{
    public String[] selectionArgs;
    public ContentValues values;
    public String where;
    
    public CrUpdateParameter() {
        this.values = null;
        this.where = null;
        this.selectionArgs = null;
    }
    
    public void clear() {
        this.values = null;
        this.where = null;
        this.selectionArgs = null;
    }
}
