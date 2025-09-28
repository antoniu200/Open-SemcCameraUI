package com.sonyericsson.android.camera.view.modeselector;

import android.content.Context;
import android.support.annotation.NonNull;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class AddonMode extends Mode {
    private final CapturingModeAttributes mTag;

    public AddonMode(@NonNull Context context, @NonNull CapturingModeAttributes capturingModeAttributes) {
        super(context, null);
        this.mTag = capturingModeAttributes;
        this.mId = generateId(capturingModeAttributes);
    }

    public CapturingModeAttributes getTag() {
        return this.mTag;
    }

    public static String generateId(@NonNull String str, @NonNull String str2) {
        return str + ":" + str2;
    }

    public static String generateId(@NonNull CapturingModeAttributes capturingModeAttributes) {
        return generateId(capturingModeAttributes.getPackageName(), capturingModeAttributes.getModeName());
    }

    @Override // com.sonyericsson.android.camera.view.modeselector.Mode
    public int getSelectorIconResId() {
        return this.mTag.getSelectorIconId();
    }

    @Override // com.sonyericsson.android.camera.view.modeselector.Mode
    protected String generateSmallIconMappingName() {
        return this.mTag.getPackageName();
    }

    @Override // com.sonyericsson.android.camera.view.modeselector.Mode
    protected String getModeName() {
        return ResourceUtil.getString(this.mContext, this.mTag.getPackageName(), this.mTag.getSelectorLabelId());
    }
}
