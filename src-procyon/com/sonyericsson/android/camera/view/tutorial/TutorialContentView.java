// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.tutorial;

import java.util.List;
import com.sonyericsson.android.camera.setting.StoredSettings;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View$OnClickListener;
import android.widget.RelativeLayout;

public abstract class TutorialContentView extends RelativeLayout implements View$OnClickListener
{
    protected TutorialContent mContent;
    private OnClickCloseButtonListener mOnClickCloseButtonListener;
    
    public TutorialContentView(final Context context) {
        super(context);
    }
    
    public TutorialContentView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public TutorialContentView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    protected void notifyOnDoneClicked(final View view) {
        if (this.mOnClickCloseButtonListener != null) {
            this.mOnClickCloseButtonListener.onClickCloseButton(view);
        }
    }
    
    public void onClick(final View view) {
        this.notifyOnDoneClicked(view);
    }
    
    protected void onLayoutToLandscape() {
        this.onUpdateViewContent();
    }
    
    protected void onLayoutToPortrait() {
        this.onUpdateViewContent();
    }
    
    protected abstract void onUpdateViewContent();
    
    protected final void setContent(final TutorialContent mContent) {
        this.mContent = mContent;
        if (this.mContent.isPortrait()) {
            this.onLayoutToPortrait();
        }
        else {
            this.onLayoutToLandscape();
        }
    }
    
    protected final void setOnClickCloseButtonListener(final OnClickCloseButtonListener mOnClickCloseButtonListener) {
        this.mOnClickCloseButtonListener = mOnClickCloseButtonListener;
    }
    
    public interface OnClickCloseButtonListener
    {
        void onClickCloseButton(final View p0);
    }
    
    public abstract static class TutorialContent
    {
        protected int mLayoutId;
        protected int mOrientation;
        protected Object[] mParams;
        
        protected TutorialContent(final int n) {
            this.mOrientation = 0;
            this.changeOrientation(n);
        }
        
        protected TutorialContent(final int n, final Object... mParams) {
            this.mOrientation = 0;
            this.changeOrientation(n);
            this.mParams = mParams;
        }
        
        protected boolean canShowContent(final StoredSettings storedSettings) {
            return true;
        }
        
        protected final boolean changeOrientation(final int mOrientation) {
            final boolean b = this.mOrientation != mOrientation;
            if (b) {
                this.mOrientation = mOrientation;
                this.setupResource();
            }
            return b;
        }
        
        protected boolean equalsWith(final TutorialContent tutorialContent) {
            return tutorialContent != null && this.getClass().equals(tutorialContent.getClass()) && this.mOrientation == tutorialContent.mOrientation;
        }
        
        protected abstract TutorialPageInfo getCurrentTutorialPageInfo();
        
        protected abstract TutorialPageInfo getCurrentTutorialPageInfo(final int p0);
        
        protected abstract int getPages();
        
        protected abstract TutorialContent getTutorialContent(final TutorialController.TutorialType p0);
        
        protected abstract List<TutorialController.TutorialType> getTutorialTypes();
        
        protected final boolean isPortrait() {
            final int mOrientation = this.mOrientation;
            boolean b = true;
            if (mOrientation != 1) {
                b = false;
            }
            return b;
        }
        
        protected abstract boolean isSimpleTutorialContent();
        
        protected abstract void setupResource();
    }
    
    protected static class TutorialPageInfo
    {
        final int pageIndexByType;
        final TutorialController.TutorialType type;
        
        public TutorialPageInfo(final TutorialController.TutorialType type, final int pageIndexByType) {
            this.type = type;
            this.pageIndexByType = pageIndexByType;
        }
    }
}
