// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.mediasaving.updator;

public final class CrDeleteParameter
{
    public String[] selectionArgs;
    public String where;
    
    public CrDeleteParameter() {
        this.where = null;
        this.selectionArgs = null;
    }
    
    public void clear() {
        this.where = null;
        this.selectionArgs = null;
    }
}
