// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.tutorial;

import android.view.ViewGroup$LayoutParams;
import android.widget.LinearLayout$LayoutParams;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.support.annotation.Nullable;
import android.widget.TextView;

public class PagingTutorialNormalNavigator extends PagingTutorialNavigator
{
    @Nullable
    private TextView mGotIt;
    @Nullable
    private TextView mNext;
    @Nullable
    private LinearLayout mPageIcons;
    @Nullable
    private ImageView mPrevIcon;
    @Nullable
    private TextView mSkip;
    
    public PagingTutorialNormalNavigator(final Context context) {
        super(context);
    }
    
    public PagingTutorialNormalNavigator(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public PagingTutorialNormalNavigator(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    private void updateClickEventActivation() {
        this.updateClickEventListener((View)this.mSkip);
        this.updateClickEventListener((View)this.mNext);
        this.updateClickEventListener((View)this.mPrevIcon);
        this.updateClickEventListener((View)this.mGotIt);
    }
    
    @Override
    protected void doFirstPage() {
        if (this.mPrevIcon != null && this.mSkip != null && this.mNext != null && this.mGotIt != null) {
            if (this.mPrevIcon.getVisibility() == 0) {
                this.mPrevIcon.setVisibility(4);
                this.mSkip.setVisibility(0);
            }
            if (this.mGotIt.getVisibility() == 0) {
                this.mGotIt.setVisibility(4);
                this.mNext.setVisibility(0);
            }
            this.updateClickEventActivation();
            return;
        }
        throw new IllegalStateException("called before inflation");
    }
    
    @Override
    protected void doLastPage() {
        if (this.mPrevIcon != null && this.mSkip != null && this.mNext != null && this.mGotIt != null) {
            if (this.mSkip.getVisibility() == 0) {
                this.mSkip.setVisibility(4);
                this.mPrevIcon.setVisibility(0);
            }
            if (this.mNext.getVisibility() == 0) {
                this.mNext.setVisibility(4);
                this.mGotIt.setVisibility(0);
            }
            this.updateClickEventActivation();
            return;
        }
        throw new IllegalStateException("called before inflation");
    }
    
    @Override
    protected void doMiddlePage() {
        if (this.mPrevIcon != null && this.mSkip != null && this.mNext != null && this.mGotIt != null) {
            if (this.mSkip.getVisibility() == 0) {
                this.mSkip.setVisibility(4);
                this.mPrevIcon.setVisibility(0);
            }
            if (this.mGotIt.getVisibility() == 0) {
                this.mGotIt.setVisibility(4);
                this.mNext.setVisibility(0);
            }
            this.updateClickEventActivation();
            return;
        }
        throw new IllegalStateException("called before inflation");
    }
    
    @Override
    protected void doSingleContent() {
        if (this.mPrevIcon != null && this.mSkip != null && this.mNext != null && this.mGotIt != null) {
            this.mSkip.setVisibility(4);
            this.mNext.setVisibility(4);
            this.mPrevIcon.setVisibility(4);
            this.mGotIt.setVisibility(0);
            this.mPageIcons.setVisibility(4);
            this.updateClickEventActivation();
            return;
        }
        throw new IllegalStateException("called before inflation");
    }
    
    @Override
    protected int getPageCount() {
        if (this.mPageIcons == null) {
            throw new IllegalStateException("called before inflation");
        }
        return this.mPageIcons.getChildCount();
    }
    
    protected void onFinishInflate() {
        this.mSkip = (TextView)this.findViewById(2131296491);
        this.mNext = (TextView)this.findViewById(2131296488);
        this.mGotIt = (TextView)this.findViewById(2131296487);
        this.mPrevIcon = (ImageView)this.findViewById(2131296490);
        this.mPageIcons = (LinearLayout)this.findViewById(2131296489);
        super.onFinishInflate();
    }
    
    @Override
    protected void setPageSize(final int n) {
        if (this.mPageIcons == null) {
            throw new IllegalStateException("called before inflation");
        }
        for (int i = 0; i < n; ++i) {
            final ImageView imageView = new ImageView(this.getContext());
            final LinearLayout$LayoutParams layoutParams = new LinearLayout$LayoutParams(-1, -1);
            layoutParams.width = (int)this.getResources().getDimension(2131165484);
            layoutParams.height = (int)this.getResources().getDimension(2131165484);
            imageView.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
            imageView.setImageResource(2131231573);
            this.mPageIcons.addView((View)imageView);
        }
    }
    
    public void setRotationY(final float n) {
        super.setRotationY(n);
        this.mSkip.setRotationY(n);
        this.mNext.setRotationY(n);
        this.mGotIt.setRotationY(n);
    }
    
    @Override
    protected void updatePageSelected(final int n) {
        if (this.mPageIcons == null) {
            throw new IllegalStateException("called before inflation");
        }
        for (int i = 0; i < this.getPageCount(); ++i) {
            this.mPageIcons.getChildAt(i).setSelected(false);
        }
        this.mPageIcons.getChildAt(n).setSelected(true);
    }
}
