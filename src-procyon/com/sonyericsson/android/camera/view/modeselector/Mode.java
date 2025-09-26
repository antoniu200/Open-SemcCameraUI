// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.modeselector;

import android.widget.ImageView;
import android.support.annotation.NonNull;
import android.content.Context;

public abstract class Mode
{
    public static final int INVALID_ID = 0;
    protected final Context mContext;
    protected String mId;
    protected OnStateChangeListener mStateChangeListener;
    
    protected Mode(@NonNull final Context mContext, @NonNull final String mId) {
        this.mContext = mContext;
        this.mId = mId;
    }
    
    public boolean compare(final Mode mode) {
        return mode != null && mode.getId().equals(this.getId());
    }
    
    protected abstract String generateSmallIconMappingName();
    
    public String getId() {
        return this.mId;
    }
    
    protected abstract String getModeName();
    
    public abstract int getSelectorIconResId();
    
    public boolean isAvailable() {
        return true;
    }
    
    public void loadSmallIcon(@NonNull final ImageView imageView) {
        final String generateSmallIconMappingName = this.generateSmallIconMappingName();
        final int identifier = this.mContext.getResources().getIdentifier(generateSmallIconMappingName, "drawable", this.mContext.getPackageName());
        if (identifier != 0) {
            imageView.setPadding(0, 0, 0, 0);
            imageView.setImageResource(identifier);
        }
        else {
            final int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(2131165452);
            imageView.setPadding(dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize);
            final int selectorIconResId = this.getSelectorIconResId();
            final int dimensionPixelSize2 = this.mContext.getResources().getDimensionPixelSize(2131165451);
            imageView.setImageBitmap(ResourceUtil.getBitmap(this.mContext, generateSmallIconMappingName, selectorIconResId, dimensionPixelSize2, dimensionPixelSize2));
        }
        final String modeName = this.getModeName();
        if (modeName != null) {
            imageView.setContentDescription((CharSequence)modeName);
        }
    }
    
    public void setOnStateChangeListener(final OnStateChangeListener mStateChangeListener) {
        this.mStateChangeListener = mStateChangeListener;
    }
    
    public interface OnStateChangeListener
    {
        void onAvailabilityChanged(final Mode p0, final boolean p1);
    }
}
