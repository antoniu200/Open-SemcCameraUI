// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.tutorial;

import android.util.AttributeSet;
import android.view.View;
import android.content.Context;
import android.view.View$OnClickListener;
import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;

public abstract class PagingTutorialNavigator extends FrameLayout implements OnPageChangeListener
{
    private PagingTutorialContentView.PagingTutorialController mController;
    protected final View$OnClickListener mOnClickListener;
    
    public PagingTutorialNavigator(final Context context) {
        super(context);
        this.mOnClickListener = (View$OnClickListener)new View$OnClickListener() {
            final PagingTutorialNavigator this$0;
            
            public void onClick(final View view) {
                if (this.this$0.mController == null) {
                    return;
                }
                switch (view.getId()) {
                    case 2131296490: {
                        this.this$0.mController.movePageToBack();
                        break;
                    }
                    case 2131296488: {
                        this.this$0.mController.movePageToNext();
                        break;
                    }
                    case 2131296487:
                    case 2131296491:
                    case 2131296676:
                    case 2131296684: {
                        this.this$0.mController.closeTutorial(view);
                        break;
                    }
                }
            }
        };
    }
    
    public PagingTutorialNavigator(final Context context, final AttributeSet set) {
        super(context, set);
        this.mOnClickListener = (View$OnClickListener)new View$OnClickListener() {
            final PagingTutorialNavigator this$0;
            
            public void onClick(final View view) {
                if (this.this$0.mController == null) {
                    return;
                }
                switch (view.getId()) {
                    case 2131296490: {
                        this.this$0.mController.movePageToBack();
                        break;
                    }
                    case 2131296488: {
                        this.this$0.mController.movePageToNext();
                        break;
                    }
                    case 2131296487:
                    case 2131296491:
                    case 2131296676:
                    case 2131296684: {
                        this.this$0.mController.closeTutorial(view);
                        break;
                    }
                }
            }
        };
    }
    
    public PagingTutorialNavigator(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mOnClickListener = (View$OnClickListener)new View$OnClickListener() {
            final PagingTutorialNavigator this$0;
            
            public void onClick(final View view) {
                if (this.this$0.mController == null) {
                    return;
                }
                switch (view.getId()) {
                    case 2131296490: {
                        this.this$0.mController.movePageToBack();
                        break;
                    }
                    case 2131296488: {
                        this.this$0.mController.movePageToNext();
                        break;
                    }
                    case 2131296487:
                    case 2131296491:
                    case 2131296676:
                    case 2131296684: {
                        this.this$0.mController.closeTutorial(view);
                        break;
                    }
                }
            }
        };
    }
    
    protected abstract void doFirstPage();
    
    protected abstract void doLastPage();
    
    protected abstract void doMiddlePage();
    
    protected abstract void doSingleContent();
    
    protected abstract int getPageCount();
    
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.setMotionEventSplittingEnabled(false);
    }
    
    public void onPageScrollStateChanged(final int n) {
    }
    
    public void onPageScrolled(final int n, final float n2, final int n3) {
    }
    
    public void onPageSelected(final int n) {
        if (this.getPageCount() == 1) {
            this.doSingleContent();
        }
        else {
            this.updatePageSelected(n);
            if (n == 0) {
                this.doFirstPage();
            }
            else if (n == this.getPageCount() - 1) {
                this.doLastPage();
            }
            else {
                this.doMiddlePage();
            }
        }
    }
    
    protected abstract void setPageSize(final int p0);
    
    protected void setViewController(final PagingTutorialContentView.PagingTutorialController mController) {
        this.mController = mController;
    }
    
    protected void updateClickEventListener(final View view) {
        View$OnClickListener mOnClickListener;
        if (view.getVisibility() == 0) {
            mOnClickListener = this.mOnClickListener;
        }
        else {
            mOnClickListener = null;
        }
        view.setOnClickListener(mOnClickListener);
    }
    
    protected void updatePageSelected(final int n) {
    }
}
