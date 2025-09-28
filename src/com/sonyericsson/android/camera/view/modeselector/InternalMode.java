package com.sonyericsson.android.camera.view.modeselector;

import android.content.Context;
import android.support.annotation.NonNull;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class InternalMode extends Mode {
    private final ModeSelectorInternalMode mTag;

    public InternalMode(@NonNull Context context, @NonNull ModeSelectorInternalMode modeSelectorInternalMode) {
        super(context, null);
        this.mTag = modeSelectorInternalMode;
        this.mId = generateId(context, modeSelectorInternalMode);
    }

    public ModeSelectorInternalMode getTag() {
        return this.mTag;
    }

    public static String generateId(@NonNull Context context, @NonNull ModeSelectorInternalMode modeSelectorInternalMode) {
        return context.getPackageName() + ":" + modeSelectorInternalMode.name();
    }

    @Override // com.sonyericsson.android.camera.view.modeselector.Mode
    public int getSelectorIconResId() {
        return this.mTag.iconId;
    }

    @Override // com.sonyericsson.android.camera.view.modeselector.Mode
    protected String generateSmallIconMappingName() {
        return this.mContext.getPackageName() + "." + this.mTag.name();
    }

    @Override // com.sonyericsson.android.camera.view.modeselector.Mode
    protected String getModeName() {
        return this.mContext.getString(this.mTag.textId);
    }
}
