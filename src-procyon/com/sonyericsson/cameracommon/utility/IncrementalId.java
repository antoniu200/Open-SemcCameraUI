// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.utility;

import com.sonyericsson.android.camera.util.CamLog;

public final class IncrementalId
{
    public static final int INCREMENTAL_INVALID = -1;
    private static final int INCREMENTAL_MAX = 2147483646;
    private static final int INCREMENTAL_MIN = 0;
    private int mId;
    
    public IncrementalId() {
        this.mId = 0;
    }
    
    public void clear() {
        synchronized (this) {
            this.mId = 0;
        }
    }
    
    public int generateNext() {
        synchronized (this) {
            if (this.mId >= 2147483646) {
                this.mId = 0;
            }
            ++this.mId;
            if (CamLog.VERBOSE) {
                final String name = IncrementalId.class.getName();
                final StringBuilder sb = new StringBuilder();
                sb.append("New ID :");
                sb.append(Integer.toString(this.mId));
                CamLog.d(name, sb.toString());
            }
            return this.mId;
        }
    }
    
    public int getNext() {
        return this.generateNext();
    }
}
