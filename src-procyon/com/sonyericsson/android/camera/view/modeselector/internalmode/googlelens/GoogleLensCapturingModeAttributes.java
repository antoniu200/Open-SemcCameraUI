// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.modeselector.internalmode.googlelens;

import android.content.pm.PackageManager;
import android.content.pm.PackageManager$NameNotFoundException;
import com.sonyericsson.android.camera.view.modeselector.ResourceUtil;
import android.content.Context;
import com.sonyericsson.android.camera.view.modeselector.CapturingModeAttributes;

public class GoogleLensCapturingModeAttributes extends CapturingModeAttributes
{
    private Context mContext;
    
    public GoogleLensCapturingModeAttributes(final Context mContext, final CapturingModeAttributes capturingModeAttributes) {
        super(capturingModeAttributes.getPackageName(), capturingModeAttributes.getActivityName(), capturingModeAttributes.getModeName(), capturingModeAttributes.getSelectorIconId(), capturingModeAttributes.getSelectorLabelId(), capturingModeAttributes.getDescriptionLabelId(), capturingModeAttributes.getShortcutIconId(), capturingModeAttributes.getShortcutLabelId(), capturingModeAttributes.getInternalCaptureType(), capturingModeAttributes.isVisibleNormal(), capturingModeAttributes.isVisibleOneshot(), capturingModeAttributes.isVisibleShortcut(), capturingModeAttributes.getTag());
        this.mContext = mContext;
    }
    
    @Override
    public String getSelectorLabel() {
        final PackageManager packageManager = this.mContext.getPackageManager();
        try {
            return (String)packageManager.getApplicationLabel(packageManager.getApplicationInfo("com.google.ar.lens", 0));
        }
        catch (final PackageManager$NameNotFoundException ex) {
            return ResourceUtil.getString(this.mContext, this.getPackageName(), this.getSelectorLabelId(), "", 100);
        }
    }
}
