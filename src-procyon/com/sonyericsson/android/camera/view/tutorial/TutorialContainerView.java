// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.tutorial;

import android.graphics.Point;
import android.view.ViewGroup$LayoutParams;
import android.graphics.Rect;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import android.view.ViewGroup;
import android.view.View;
import android.widget.RelativeLayout;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.ViewFlipper;
import android.widget.FrameLayout;

public class TutorialContainerView extends FrameLayout
{
    private ViewFlipper mViewFlipper;
    
    public TutorialContainerView(final Context context) {
        super(context);
    }
    
    public TutorialContainerView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public TutorialContainerView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    public ViewFlipper getViewFlipper() {
        return this.mViewFlipper;
    }
    
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mViewFlipper = (ViewFlipper)this.findViewById(2131296403);
    }
    
    public static final class TutorialView extends RelativeLayout
    {
        private static final int ANGLE_PORTRAIT_DEGREE = -90;
        private TutorialContentView.TutorialContent mContent;
        private TutorialContentView mContentView;
        private TutorialContentView.OnClickCloseButtonListener mOnClickCloseButtonListener;
        
        public TutorialView(final Context context) {
            super(context);
        }
        
        public TutorialView(final Context context, final AttributeSet set) {
            super(context, set);
        }
        
        public TutorialView(final Context context, final AttributeSet set, final int n) {
            super(context, set, n);
        }
        
        private void alignView(final View view, final int n, final int n2, final int n3) {
            if (n3 == -90) {
                view.setTranslationY((float)n);
            }
            else {
                view.setTranslationY(0.0f);
            }
        }
        
        private TutorialContentView attachContentView(final int n) {
            inflate(this.getContext(), n, (ViewGroup)this);
            final View child = this.getChildAt(this.getChildCount() - 1);
            if (!TutorialContentView.class.isAssignableFrom(child.getClass())) {
                this.removeAllViews();
                return null;
            }
            return (TutorialContentView)child;
        }
        
        private void onLayoutToLandscape() {
            this.mContentView = this.attachContentView(this.mContent.mLayoutId);
            if (this.mContentView == null) {
                return;
            }
            final Rect viewFinderSize = LayoutDependencyResolver.getViewFinderSize(this.getContext());
            this.setCustomParameters((View)this, viewFinderSize.width() - LayoutDependencyResolver.getNavigationBarMargin(this.getContext()), viewFinderSize.height(), 0, 0, 0);
            this.mContentView.setContent(this.mContent);
            this.mContentView.setOnClickCloseButtonListener(this.mOnClickCloseButtonListener);
        }
        
        private void onLayoutToPortrait() {
            this.mContentView = this.attachContentView(this.mContent.mLayoutId);
            if (this.mContentView == null) {
                return;
            }
            final Rect viewFinderSize = LayoutDependencyResolver.getViewFinderSize(this.getContext());
            this.setCustomParameters((View)this, viewFinderSize.height(), viewFinderSize.width() - LayoutDependencyResolver.getNavigationBarMargin(this.getContext()), 0, 0, -90);
            this.mContentView.setContent(this.mContent);
            this.mContentView.setOnClickCloseButtonListener(this.mOnClickCloseButtonListener);
        }
        
        private void setCustomParameters(final View view, final int width, final int height, final int n, final int n2, final int n3) {
            final ViewGroup$LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = height;
            view.setLayoutParams(layoutParams);
            view.setPivotX((float)n);
            view.setPivotY((float)n2);
            view.setRotation((float)n3);
            this.alignView(view, layoutParams.width, layoutParams.height, n3);
        }
        
        protected TutorialContentView.TutorialContent getContent() {
            return this.mContent;
        }
        
        public boolean getGlobalVisibleRect(final Rect rect, final Point point) {
            if (this.mContentView == null) {
                rect.set(0, 0, 0, 0);
                return false;
            }
            return this.mContentView.getGlobalVisibleRect(rect, point);
        }
        
        protected boolean isPortrait() {
            return this.mContent != null && this.mContent.isPortrait();
        }
        
        protected void release() {
            this.removeAllViews();
            this.mContentView = null;
            this.mContent = null;
        }
        
        protected boolean setContent(final TutorialContentView.TutorialContent mContent) {
            if (this.mContent != null && this.mContent.equalsWith(mContent)) {
                return false;
            }
            this.mContent = mContent;
            if (this.mContent == null) {
                return false;
            }
            if (this.mContent.isPortrait()) {
                this.onLayoutToPortrait();
            }
            else {
                this.onLayoutToLandscape();
            }
            if (this.mContentView == null) {
                return false;
            }
            this.mContentView.setVisibility(0);
            return true;
        }
        
        protected void setOnClickCloseButtonListener(final TutorialContentView.OnClickCloseButtonListener mOnClickCloseButtonListener) {
            if (this.mContent == null) {
                return;
            }
            this.mOnClickCloseButtonListener = mOnClickCloseButtonListener;
            if (this.mContentView != null) {
                this.mContentView.setOnClickCloseButtonListener(this.mOnClickCloseButtonListener);
            }
        }
        
        protected void setUiOrientation(final int n) {
            if (this.mContent == null) {
                return;
            }
            if (this.mContent.changeOrientation(n)) {
                this.removeAllViews();
                if (this.mContent.isPortrait()) {
                    this.onLayoutToPortrait();
                }
                else {
                    this.onLayoutToLandscape();
                }
            }
        }
    }
}
