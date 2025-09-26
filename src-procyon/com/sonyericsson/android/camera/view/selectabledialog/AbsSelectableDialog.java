// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.selectabledialog;

import android.animation.Animator$AnimatorListener;
import com.sonyericsson.android.camera.util.CamLog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.view.ViewGroup;
import com.sonyericsson.android.camera.view.setting.SettingDialogStack;
import android.widget.FrameLayout;
import android.content.Context;
import android.animation.Animator;
import com.sonyericsson.android.camera.view.setting.dialog.SettingDialogInterface;

public abstract class AbsSelectableDialog implements SettingDialogInterface, OnScrollListener
{
    private Animator mAnimation;
    protected int mBottomMarginHint;
    protected final Context mContext;
    protected AbsDialogScrollView mDialogScrollView;
    protected SelectableDialogType mDialogType;
    protected boolean mIsExpandedWhenOpened;
    protected int mOrientation;
    protected final Params mParams;
    protected FrameLayout mParent;
    protected SettingDialogStack mSettingDialogStack;
    
    public AbsSelectableDialog(final Context mContext, final Params params, final boolean mIsExpandedWhenOpened) {
        this.mSettingDialogStack = null;
        this.mContext = mContext;
        this.mParams = new Params(params);
        this.mBottomMarginHint = 0;
        this.mIsExpandedWhenOpened = mIsExpandedWhenOpened;
    }
    
    private int computeContentsHeight() {
        if (this.mIsExpandedWhenOpened) {
            return this.mDialogScrollView.getTotalArrangeHeight();
        }
        return this.mDialogScrollView.getArrangeHeightAtIndex(0);
    }
    
    private int getSideEdgeMargin() {
        return this.mParams.dropShadowSpace * 2;
    }
    
    private void updateRotatableLayout() {
        if (this.mParent == null) {
            return;
        }
        this.mDialogScrollView.updateRotatableLayout((ViewGroup)this.mParent, this.computeWidth(), this.computeHeight());
    }
    
    public void addPanel(final ArrayAdapter arrayAdapter) {
        this.addPanel(null, arrayAdapter);
    }
    
    public void addPanel(final String s, final ArrayAdapter arrayAdapter) {
        this.mDialogScrollView.addContent(s, arrayAdapter);
    }
    
    protected void adjustLayout() {
        this.mDialogScrollView.updateDefaultScrollPosition();
        this.updateRotatableLayout();
    }
    
    @Override
    public void close() {
        if (this.mParent == null) {
            return;
        }
        if (this.mDialogScrollView.getScrollStatus() == Status.EXIT) {
            this.closeImmediate();
        }
        else {
            this.startCloseAnimation();
        }
    }
    
    public void closeImmediate() {
        if (this.mParent == null) {
            return;
        }
        this.mParent.removeView((View)this.mDialogScrollView);
        this.mParent = null;
        if (this.mAnimation != null) {
            this.mAnimation.cancel();
        }
        this.mDialogScrollView = null;
        this.mSettingDialogStack = null;
    }
    
    protected int computeHeight() {
        final int computeContentsHeight = this.computeContentsHeight();
        final int sideEdgeMargin = this.getSideEdgeMargin();
        int b;
        if (this.isPortrait()) {
            b = this.mParams.maxHeightPortrait;
        }
        else {
            b = this.mParams.maxHeightLandscape;
        }
        return Math.min(computeContentsHeight + sideEdgeMargin, b);
    }
    
    protected int computeWidth() {
        return this.mParams.itemWidth + this.getSideEdgeMargin();
    }
    
    @Override
    public boolean hitTest(final int n, final int n2) {
        return false;
    }
    
    public boolean isExpanded() {
        return this.mIsExpandedWhenOpened;
    }
    
    public boolean isOperationAcceptable() {
        return true;
    }
    
    protected boolean isPortrait() {
        final int mOrientation = this.mOrientation;
        boolean b = true;
        if (mOrientation != 1) {
            b = false;
        }
        return b;
    }
    
    protected void onOrientationChanged(final int uiOrientation) {
        this.mDialogScrollView.setUiOrientation(uiOrientation);
        this.updateRotatableLayout();
    }
    
    @Override
    public void onScrollFinished(final Status status) {
    }
    
    @Override
    public void onScrollProgressChanged(final float f) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("scrollcontainer currentProgress:");
            sb.append(f);
            CamLog.d(sb.toString());
        }
        if (f == 1.0f) {
            this.mIsExpandedWhenOpened = true;
        }
        else {
            this.mIsExpandedWhenOpened = false;
        }
    }
    
    public void setBottomMarginHint(final int bottomMarginHint) {
        this.mDialogScrollView.setBottomMarginHint(bottomMarginHint);
    }
    
    @Override
    public void setEnabled(final boolean enabled) {
        if (this.mDialogScrollView != null) {
            this.mDialogScrollView.setEnabled(enabled);
        }
    }
    
    @Override
    public void setSensorOrientation(final int mOrientation) {
        if (this.mOrientation != mOrientation) {
            this.mOrientation = mOrientation;
            if (this.mDialogScrollView != null) {
                this.onOrientationChanged(mOrientation);
            }
        }
    }
    
    public void setSettingDialogStack(final SettingDialogStack mSettingDialogStack) {
        this.mSettingDialogStack = mSettingDialogStack;
    }
    
    public void setTopMarginHint(final int topMarginHint) {
        this.mDialogScrollView.setTopMarginHint(topMarginHint);
    }
    
    protected void startCloseAnimation() {
        this.setEnabled(false);
        this.mDialogScrollView.startCloseAnimation(this.computeWidth(), this.computeHeight(), (Animator$AnimatorListener)new Animator$AnimatorListener(this) {
            final AbsSelectableDialog this$0;
            
            public void onAnimationCancel(final Animator animator) {
            }
            
            public void onAnimationEnd(final Animator animator) {
                if (this.this$0.mParams.animationType == AnimationType.SLIDER) {
                    if (this.this$0.isPortrait()) {
                        this.this$0.mSettingDialogStack.getBackground().setTranslationX(0.0f);
                    }
                    else {
                        this.this$0.mSettingDialogStack.getBackground().setTranslationY(0.0f);
                    }
                }
                this.this$0.closeImmediate();
            }
            
            public void onAnimationRepeat(final Animator animator) {
            }
            
            public void onAnimationStart(final Animator animator) {
            }
        });
    }
    
    protected void startOpenAnimation() {
        this.mDialogScrollView.startOpenAnimation(this.computeWidth(), this.computeHeight(), (Animator$AnimatorListener)new Animator$AnimatorListener(this) {
            final AbsSelectableDialog this$0;
            
            public void onAnimationCancel(final Animator animator) {
            }
            
            public void onAnimationEnd(final Animator animator) {
                this.this$0.setEnabled(true);
                this.this$0.mAnimation = null;
            }
            
            public void onAnimationRepeat(final Animator animator) {
            }
            
            public void onAnimationStart(final Animator animator) {
                this.this$0.mAnimation = animator;
            }
        });
    }
    
    public enum AnimationType
    {
        private static final AnimationType[] $VALUES;
        
        FADE, 
        NONE, 
        SLIDER;
        
        static {
            $VALUES = new AnimationType[] { AnimationType.NONE, AnimationType.FADE, AnimationType.SLIDER };
        }
    }
    
    public enum HorizontalGravity
    {
        private static final HorizontalGravity[] $VALUES;
        
        LEFT(3), 
        RIGHT(5);
        
        public final int value;
        
        static {
            $VALUES = new HorizontalGravity[] { HorizontalGravity.LEFT, HorizontalGravity.RIGHT };
        }
        
        private HorizontalGravity(final int value) {
            this.value = value;
        }
    }
    
    public static class Params
    {
        public AnimationType animationType;
        public int bottomMarginLandscape;
        public int bottomMarginPortrait;
        public int dropShadowSpace;
        public HorizontalGravity horizontalGavity;
        public int itemHeight;
        public int itemWidth;
        public int leftMarginLandscape;
        public int leftMarginPortrait;
        public int maxHeightLandscape;
        public int maxHeightPortrait;
        public int panelBackgroundColor;
        public int rightMarginLandscape;
        public int rightMarginPortrait;
        public int scrollBarDefaultDelayBeforeFade;
        public int scrollbarFadeDuration;
        
        public Params() {
            this.animationType = AnimationType.NONE;
            this.horizontalGavity = HorizontalGravity.RIGHT;
        }
        
        public Params(final Params params) {
            this.animationType = AnimationType.NONE;
            this.horizontalGavity = HorizontalGravity.RIGHT;
            this.itemWidth = params.itemWidth;
            this.itemHeight = params.itemHeight;
            this.maxHeightLandscape = params.maxHeightLandscape;
            this.maxHeightPortrait = params.maxHeightPortrait;
            this.rightMarginLandscape = params.rightMarginLandscape;
            this.rightMarginPortrait = params.rightMarginPortrait;
            this.leftMarginLandscape = params.leftMarginLandscape;
            this.leftMarginPortrait = params.leftMarginPortrait;
            this.bottomMarginLandscape = params.bottomMarginLandscape;
            this.bottomMarginPortrait = params.bottomMarginPortrait;
            this.panelBackgroundColor = params.panelBackgroundColor;
            this.scrollbarFadeDuration = params.scrollbarFadeDuration;
            this.scrollBarDefaultDelayBeforeFade = params.scrollBarDefaultDelayBeforeFade;
            this.dropShadowSpace = params.dropShadowSpace;
            this.animationType = params.animationType;
            this.horizontalGavity = params.horizontalGavity;
        }
    }
    
    public enum SelectableDialogType
    {
        private static final SelectableDialogType[] $VALUES;
        
        MODE_SELECTOR, 
        SETTING_ASPECT_RATIO, 
        SETTING_FLASH, 
        SETTING_FUSION_MODE, 
        SETTING_HDR, 
        SETTING_MENU, 
        SETTING_MONOCHROME, 
        SETTING_SCENE, 
        SETTING_SECOND_LAYER, 
        SETTING_SECOND_LAYER_DETAIL, 
        SETTING_SELFTIMER, 
        SETTING_VIDEO_HDR;
        
        static {
            $VALUES = new SelectableDialogType[] { SelectableDialogType.SETTING_MENU, SelectableDialogType.SETTING_SECOND_LAYER, SelectableDialogType.SETTING_SECOND_LAYER_DETAIL, SelectableDialogType.SETTING_FLASH, SelectableDialogType.SETTING_MONOCHROME, SelectableDialogType.SETTING_SELFTIMER, SelectableDialogType.SETTING_ASPECT_RATIO, SelectableDialogType.SETTING_FUSION_MODE, SelectableDialogType.SETTING_VIDEO_HDR, SelectableDialogType.SETTING_HDR, SelectableDialogType.SETTING_SCENE, SelectableDialogType.MODE_SELECTOR };
        }
    }
}
