// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.tutorial;

import android.util.AttributeSet;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.annotation.Nullable;
import android.view.View;

public class PagingTutorialConfirmNavigator extends PagingTutorialNavigator
{
    @Nullable
    private View mConfirm;
    @Nullable
    private TextView mNext;
    private int mPageSize;
    @Nullable
    private ImageView mPrevIcon;
    
    public PagingTutorialConfirmNavigator(final Context context) {
        super(context);
    }
    
    public PagingTutorialConfirmNavigator(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public PagingTutorialConfirmNavigator(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    private void updateClickEventActivation() {
        this.updateClickEventListener((View)this.mNext);
        this.updateClickEventListener((View)this.mPrevIcon);
        this.updateClickEventListener(this.findViewById(2131296676));
        this.updateClickEventListener(this.findViewById(2131296684));
    }
    
    @Override
    protected void doFirstPage() {
        if (this.mPrevIcon != null && this.mNext != null && this.mConfirm != null) {
            if (this.mPrevIcon.getVisibility() == 0) {
                this.mPrevIcon.setVisibility(4);
            }
            if (this.mConfirm.getVisibility() == 0) {
                this.mConfirm.setVisibility(4);
                this.mNext.setVisibility(0);
            }
            this.updateClickEventActivation();
            return;
        }
        throw new IllegalStateException("called before inflation");
    }
    
    @Override
    protected void doLastPage() {
        if (this.mPrevIcon != null && this.mNext != null && this.mConfirm != null) {
            if (this.mPrevIcon.getVisibility() == 4) {
                this.mPrevIcon.setVisibility(0);
            }
            if (this.mNext.getVisibility() == 0) {
                this.mNext.setVisibility(4);
                this.mConfirm.setVisibility(0);
            }
            this.updateClickEventActivation();
            return;
        }
        throw new IllegalStateException("called before inflation");
    }
    
    @Override
    protected void doMiddlePage() {
        if (this.mPrevIcon != null && this.mNext != null && this.mConfirm != null) {
            if (this.mPrevIcon.getVisibility() == 4) {
                this.mPrevIcon.setVisibility(0);
            }
            if (this.mConfirm.getVisibility() == 0) {
                this.mConfirm.setVisibility(4);
                this.mNext.setVisibility(0);
            }
            this.updateClickEventActivation();
            return;
        }
        throw new IllegalStateException("called before inflation");
    }
    
    @Override
    protected void doSingleContent() {
        if (this.mPrevIcon != null && this.mNext != null && this.mConfirm != null) {
            this.mNext.setVisibility(4);
            this.mConfirm.setVisibility(0);
            this.mPrevIcon.setVisibility(4);
            this.updateClickEventActivation();
            return;
        }
        throw new IllegalStateException("called before inflation");
    }
    
    @Override
    protected int getPageCount() {
        return this.mPageSize;
    }
    
    protected void onFinishInflate() {
        this.mNext = (TextView)this.findViewById(2131296488);
        this.mConfirm = this.findViewById(2131296327);
        this.mPrevIcon = (ImageView)this.findViewById(2131296490);
        super.onFinishInflate();
    }
    
    @Override
    protected void setPageSize(final int mPageSize) {
        this.mPageSize = mPageSize;
    }
    
    public void setRotationY(final float rotationY) {
        if (this.mNext != null && this.mConfirm != null) {
            super.setRotationY(rotationY);
            this.mNext.setRotationY(rotationY);
            this.mConfirm.setRotationY(rotationY);
            return;
        }
        throw new IllegalStateException("called before inflation");
    }
}
