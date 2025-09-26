// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.baselayout;

import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import android.view.View;
import android.widget.ImageView;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.view.View$OnClickListener;

public class HighSensitivityFusionIndicator
{
    private View$OnClickListener mListener;
    private final ViewGroup mRootView;
    
    public HighSensitivityFusionIndicator(@NonNull final ViewGroup mRootView) {
        this.mRootView = mRootView;
        final ImageView imageView = (ImageView)this.mRootView.findViewById(2131296694);
        imageView.setOnClickListener((View$OnClickListener)new View$OnClickListener(this) {
            final HighSensitivityFusionIndicator this$0;
            
            public void onClick(final View view) {
                this.this$0.setChecked(view.isActivated() ^ true);
                if (this.this$0.mListener != null) {
                    this.this$0.mListener.onClick(view);
                }
            }
        });
        imageView.setContentDescription((CharSequence)mRootView.getContext().getString(2131689569));
    }
    
    public View getAnimationTarget() {
        return this.mRootView.findViewById(2131296694);
    }
    
    public void hide() {
        if (this.mRootView.getVisibility() != 8) {
            this.mRootView.setVisibility(8);
        }
    }
    
    public void setChecked(final boolean activated) {
        final ImageView imageView = (ImageView)this.mRootView.findViewById(2131296694);
        if (imageView.isActivated() != activated) {
            imageView.setActivated(activated);
        }
    }
    
    public void setEnabled(final boolean b) {
        final ImageView imageView = (ImageView)this.mRootView.findViewById(2131296694);
        if (b) {
            imageView.setImageResource(2131231096);
        }
        else {
            imageView.setImageResource(2131231099);
        }
    }
    
    public void setListener(final View$OnClickListener mListener) {
        this.mListener = mListener;
    }
    
    public void setSensorOrientation(final int n) {
        this.mRootView.setRotation(RotationUtil.getAngle(n));
        final ImageView imageView = (ImageView)this.mRootView.findViewById(2131296694);
        final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)imageView.getLayoutParams();
        layoutParams.gravity = 48;
        imageView.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
    }
    
    public void show() {
        if (this.mRootView.getVisibility() != 0) {
            this.mRootView.setVisibility(0);
        }
    }
}
