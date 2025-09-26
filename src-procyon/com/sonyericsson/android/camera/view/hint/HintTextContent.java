// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.hint;

public abstract class HintTextContent
{
    public static final long INFINITE_TIMEOUT_MILLIS = -1L;
    public static final int NO_FADE = -1;
    protected boolean mTransparentBackground;
    
    public HintTextContent() {
        this.mTransparentBackground = true;
    }
    
    public void attach(final HintTextView hintTextView) {
        if (hintTextView == null) {
            return;
        }
        hintTextView.setContent(this);
        if (hintTextView.getVisibility() != 0) {
            hintTextView.setVisibility(0);
        }
    }
    
    public void detach(final HintTextView hintTextView) {
        if (hintTextView == null) {
            return;
        }
        if (hintTextView.getVisibility() != 4) {
            hintTextView.setVisibility(4);
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof HintTextContent && this.getTag().equals(((HintTextContent)o).getTag());
    }
    
    public int getButtonDescriptionResourceId() {
        return -1;
    }
    
    public abstract int getButtonMessageResourceId();
    
    public int getFadeDuration() {
        return -1;
    }
    
    public int getMessageDescriptionResourceId() {
        return -1;
    }
    
    public abstract int getMessageResourceId();
    
    public HintTextContent getNext() {
        return null;
    }
    
    public HintPriority getPriority() {
        return HintPriority.LOW;
    }
    
    public String getSubMessage() {
        return null;
    }
    
    public String getTag() {
        return this.getClass().getSimpleName();
    }
    
    public long getTimedOutDuration() {
        return -1L;
    }
    
    @Override
    public int hashCode() {
        return this.getTag().hashCode();
    }
    
    public boolean isToast() {
        return false;
    }
    
    public boolean isTransparentBackground() {
        return this.mTransparentBackground;
    }
    
    public enum HintPriority
    {
        private static final HintPriority[] $VALUES;
        
        HIGH, 
        LOW, 
        MIDDLE;
        
        static {
            $VALUES = new HintPriority[] { HintPriority.HIGH, HintPriority.MIDDLE, HintPriority.LOW };
        }
    }
}
