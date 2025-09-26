// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.status;

import android.content.ContentValues;

public abstract class EnumValue<T extends Enum<T>> implements CameraStatusValue
{
    private final String mValueString;
    
    public EnumValue(final T t) {
        this.mValueString = t.toString();
    }
    
    @Override
    public String getValueForDebug() {
        return this.mValueString;
    }
    
    @Override
    public void putInto(final ContentValues contentValues, final String str) {
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(this.getKey());
        contentValues.put(sb.toString(), this.mValueString);
    }
}
