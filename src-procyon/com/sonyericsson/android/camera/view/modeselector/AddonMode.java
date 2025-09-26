// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.modeselector;

import android.support.annotation.NonNull;
import android.content.Context;

public class AddonMode extends Mode
{
    private final CapturingModeAttributes mTag;
    
    public AddonMode(@NonNull final Context context, @NonNull final CapturingModeAttributes mTag) {
        super(context, null);
        this.mTag = mTag;
        this.mId = generateId(mTag);
    }
    
    public static String generateId(@NonNull final CapturingModeAttributes capturingModeAttributes) {
        return generateId(capturingModeAttributes.getPackageName(), capturingModeAttributes.getModeName());
    }
    
    public static String generateId(@NonNull final String str, @NonNull final String str2) {
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(":");
        sb.append(str2);
        return sb.toString();
    }
    
    @Override
    protected String generateSmallIconMappingName() {
        return this.mTag.getPackageName();
    }
    
    @Override
    protected String getModeName() {
        return ResourceUtil.getString(this.mContext, this.mTag.getPackageName(), this.mTag.getSelectorLabelId());
    }
    
    @Override
    public int getSelectorIconResId() {
        return this.mTag.getSelectorIconId();
    }
    
    public CapturingModeAttributes getTag() {
        return this.mTag;
    }
}
