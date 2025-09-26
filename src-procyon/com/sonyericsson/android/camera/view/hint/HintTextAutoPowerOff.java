// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.hint;

public class HintTextAutoPowerOff extends HintTextContent
{
    @Override
    public int getButtonMessageResourceId() {
        return -1;
    }
    
    @Override
    public int getMessageResourceId() {
        return 2131689642;
    }
    
    @Override
    public HintPriority getPriority() {
        return HintPriority.HIGH;
    }
    
    @Override
    public boolean isTransparentBackground() {
        return false;
    }
}
