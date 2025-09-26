// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera;

import android.content.Context;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;

public enum NavigatorContents
{
    private static final NavigatorContents[] $VALUES;
    
    SUPERIOR_AUTO(2131230936, 2131230937, 2131689668), 
    VIDEO(2131230938, 2131230939, 2131689631);
    
    private final int mIconId;
    private final int mLargeIconId;
    private final int mTextId;
    
    static {
        $VALUES = new NavigatorContents[] { NavigatorContents.SUPERIOR_AUTO, NavigatorContents.VIDEO };
    }
    
    private NavigatorContents(final int mIconId, final int mLargeIconId, final int mTextId) {
        this.mIconId = mIconId;
        this.mLargeIconId = mLargeIconId;
        this.mTextId = mTextId;
    }
    
    public static int indexOf(final NavigatorContents navigatorContents) {
        for (int i = 0; i < values().length; ++i) {
            if (navigatorContents.equals(values()[i])) {
                return i;
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(navigatorContents.name());
        sb.append(" is not NavigatorContents.");
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static NavigatorContents valueOf(final CapturingMode capturingMode) {
        NavigatorContents navigatorContents = null;
        switch (NavigatorContents$1.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[capturingMode.ordinal()]) {
            default: {
                navigatorContents = NavigatorContents.SUPERIOR_AUTO;
                break;
            }
            case 3:
            case 4:
            case 5: {
                navigatorContents = NavigatorContents.VIDEO;
                break;
            }
            case 1:
            case 2: {
                navigatorContents = NavigatorContents.SUPERIOR_AUTO;
                break;
            }
        }
        return navigatorContents;
    }
    
    public int getIconId() {
        return this.mIconId;
    }
    
    public int getLargeIconId() {
        return this.mLargeIconId;
    }
    
    public String getText(final Context context) {
        return context.getResources().getString(this.mTextId).toUpperCase();
    }
    
    public int getTextId() {
        return this.mTextId;
    }
    
    public boolean hasNext() {
        final int index = indexOf(this);
        final int length = values().length;
        boolean b = true;
        if (index >= length - 1) {
            b = false;
        }
        return b;
    }
    
    public boolean hasPrevious() {
        return indexOf(this) > 0;
    }
    
    public NavigatorContents next() {
        return this.next(1);
    }
    
    public NavigatorContents next(int n) {
        n += indexOf(this);
        if (n < values().length) {
            return values()[n];
        }
        return values()[values().length - 1];
    }
    
    public NavigatorContents previous() {
        return this.previous(1);
    }
    
    public NavigatorContents previous(int n) {
        n = indexOf(this) - n;
        if (n >= 0) {
            return values()[n];
        }
        return values()[0];
    }
}
