package com.sonyericsson.android.camera.util.capability;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ResolutionCapabilityItem extends CapabilityItem<ResolutionOptions> {
    ResolutionCapabilityItem(ResolutionOptions resolutionOptions) {
        super("", resolutionOptions);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sonyericsson.android.camera.util.capability.CapabilityItem
    public ResolutionOptions getDefaultValue() {
        return new ResolutionOptions();
    }
}
