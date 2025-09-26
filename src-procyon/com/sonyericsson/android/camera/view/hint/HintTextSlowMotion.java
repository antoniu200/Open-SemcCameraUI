// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.hint;

public abstract class HintTextSlowMotion extends HintTextContent
{
    private final int mNameId;
    
    public HintTextSlowMotion(final int mNameId) {
        this.mNameId = mNameId;
    }
    
    @Override
    public int getButtonDescriptionResourceId() {
        return -1;
    }
    
    @Override
    public int getButtonMessageResourceId() {
        return -1;
    }
    
    @Override
    public int getMessageDescriptionResourceId() {
        return -1;
    }
    
    @Override
    public int getMessageResourceId() {
        return this.mNameId;
    }
}
