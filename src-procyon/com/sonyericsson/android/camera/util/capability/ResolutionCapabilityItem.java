// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.util.capability;

public class ResolutionCapabilityItem extends CapabilityItem<ResolutionOptions>
{
    ResolutionCapabilityItem(final ResolutionOptions resolutionOptions) {
        super("", resolutionOptions);
    }
    
    @Override
    ResolutionOptions getDefaultValue() {
        return new ResolutionOptions();
    }
}
