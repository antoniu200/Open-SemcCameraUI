// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.modeselector;

import android.support.annotation.NonNull;
import android.content.Context;

public class InternalMode extends Mode
{
    private final ModeSelectorInternalMode mTag;
    
    public InternalMode(@NonNull final Context context, @NonNull final ModeSelectorInternalMode mTag) {
        super(context, null);
        this.mTag = mTag;
        this.mId = generateId(context, mTag);
    }
    
    public static String generateId(@NonNull final Context context, @NonNull final ModeSelectorInternalMode modeSelectorInternalMode) {
        final StringBuilder sb = new StringBuilder();
        sb.append(context.getPackageName());
        sb.append(":");
        sb.append(modeSelectorInternalMode.name());
        return sb.toString();
    }
    
    @Override
    protected String generateSmallIconMappingName() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.mContext.getPackageName());
        sb.append(".");
        sb.append(this.mTag.name());
        return sb.toString();
    }
    
    @Override
    protected String getModeName() {
        return this.mContext.getString(this.mTag.textId);
    }
    
    @Override
    public int getSelectorIconResId() {
        return this.mTag.iconId;
    }
    
    public ModeSelectorInternalMode getTag() {
        return this.mTag;
    }
}
