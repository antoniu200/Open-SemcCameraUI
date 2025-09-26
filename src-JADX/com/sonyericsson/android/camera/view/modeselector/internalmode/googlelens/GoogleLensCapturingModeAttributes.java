package com.sonyericsson.android.camera.view.modeselector.internalmode.googlelens;

import android.content.Context;
import android.content.pm.PackageManager;
import com.google.android.apps.lens.library.base.LensSdkParamsReader;
import com.sonyericsson.android.camera.view.modeselector.CapturingModeAttributes;
import com.sonyericsson.android.camera.view.modeselector.ResourceUtil;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class GoogleLensCapturingModeAttributes extends CapturingModeAttributes {
    private Context mContext;

    public GoogleLensCapturingModeAttributes(Context context, CapturingModeAttributes capturingModeAttributes) {
        super(capturingModeAttributes.getPackageName(), capturingModeAttributes.getActivityName(), capturingModeAttributes.getModeName(), capturingModeAttributes.getSelectorIconId(), capturingModeAttributes.getSelectorLabelId(), capturingModeAttributes.getDescriptionLabelId(), capturingModeAttributes.getShortcutIconId(), capturingModeAttributes.getShortcutLabelId(), capturingModeAttributes.getInternalCaptureType(), capturingModeAttributes.isVisibleNormal(), capturingModeAttributes.isVisibleOneshot(), capturingModeAttributes.isVisibleShortcut(), capturingModeAttributes.getTag());
        this.mContext = context;
    }

    @Override // com.sonyericsson.android.camera.view.modeselector.CapturingModeAttributes
    public String getSelectorLabel() {
        PackageManager packageManager = this.mContext.getPackageManager();
        try {
            return (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(LensSdkParamsReader.LENS_AR_STICKERS_PACKAGE, 0));
        } catch (PackageManager.NameNotFoundException unused) {
            return ResourceUtil.getString(this.mContext, getPackageName(), getSelectorLabelId(), "", 100);
        }
    }
}
