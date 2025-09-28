package com.sonyericsson.android.camera.view.selectabledialog;

import android.animation.Animator;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.view.selectabledialog.ScrollContainer;
import com.sonyericsson.android.camera.view.setting.SettingDialogStack;
import com.sonyericsson.android.camera.view.setting.dialog.SettingDialogInterface;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class AbsSelectableDialog implements SettingDialogInterface, ScrollContainer.OnScrollListener {
    private Animator mAnimation;
    protected final Context mContext;
    protected AbsDialogScrollView mDialogScrollView;
    protected SelectableDialogType mDialogType;
    protected boolean mIsExpandedWhenOpened;
    protected int mOrientation;
    protected final Params mParams;
    protected FrameLayout mParent;
    protected SettingDialogStack mSettingDialogStack = null;
    protected int mBottomMarginHint = 0;

    public enum AnimationType {
        NONE,
        FADE,
        SLIDER
    }

    public enum SelectableDialogType {
        SETTING_MENU,
        SETTING_SECOND_LAYER,
        SETTING_SECOND_LAYER_DETAIL,
        SETTING_FLASH,
        SETTING_MONOCHROME,
        SETTING_SELFTIMER,
        SETTING_ASPECT_RATIO,
        SETTING_FUSION_MODE,
        SETTING_VIDEO_HDR,
        SETTING_HDR,
        SETTING_SCENE,
        MODE_SELECTOR
    }

    @Override // com.sonyericsson.android.camera.view.setting.dialog.SettingDialogInterface
    public boolean hitTest(int i, int i2) {
        return false;
    }

    public boolean isOperationAcceptable() {
        return true;
    }

    @Override // com.sonyericsson.android.camera.view.selectabledialog.ScrollContainer.OnScrollListener
    public void onScrollFinished(ScrollContainer.Status status) {
    }

    public enum HorizontalGravity {
        LEFT(3),
        RIGHT(5);

        public final int value;

        HorizontalGravity(int i) {
            this.value = i;
        }
    }

    public static class Params {
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

        public Params(Params params) {
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

    public AbsSelectableDialog(Context context, Params params, boolean z) {
        this.mContext = context;
        this.mParams = new Params(params);
        this.mIsExpandedWhenOpened = z;
    }

    @Override // com.sonyericsson.android.camera.view.setting.dialog.SettingDialogInterface
    public void close() {
        if (this.mParent == null) {
            return;
        }
        if (this.mDialogScrollView.getScrollStatus() == ScrollContainer.Status.EXIT) {
            closeImmediate();
        } else {
            startCloseAnimation();
        }
    }

    public void closeImmediate() {
        if (this.mParent == null) {
            return;
        }
        this.mParent.removeView(this.mDialogScrollView);
        this.mParent = null;
        if (this.mAnimation != null) {
            this.mAnimation.cancel();
        }
        this.mDialogScrollView = null;
        this.mSettingDialogStack = null;
    }

    @Override // com.sonyericsson.android.camera.view.setting.dialog.SettingDialogInterface
    public void setSensorOrientation(int i) {
        if (this.mOrientation != i) {
            this.mOrientation = i;
            if (this.mDialogScrollView != null) {
                onOrientationChanged(i);
            }
        }
    }

    @Override // com.sonyericsson.android.camera.view.setting.dialog.SettingDialogInterface
    public void setEnabled(boolean z) {
        if (this.mDialogScrollView != null) {
            this.mDialogScrollView.setEnabled(z);
        }
    }

    protected boolean isPortrait() {
        return this.mOrientation == 1;
    }

    protected void onOrientationChanged(int i) {
        this.mDialogScrollView.setUiOrientation(i);
        updateRotatableLayout();
    }

    protected void adjustLayout() {
        this.mDialogScrollView.updateDefaultScrollPosition();
        updateRotatableLayout();
    }

    private void updateRotatableLayout() {
        if (this.mParent == null) {
            return;
        }
        this.mDialogScrollView.updateRotatableLayout(this.mParent, computeWidth(), computeHeight());
    }

    public void addPanel(String str, ArrayAdapter arrayAdapter) {
        this.mDialogScrollView.addContent(str, arrayAdapter);
    }

    public void addPanel(ArrayAdapter arrayAdapter) {
        addPanel(null, arrayAdapter);
    }

    public void setSettingDialogStack(SettingDialogStack settingDialogStack) {
        this.mSettingDialogStack = settingDialogStack;
    }

    public void setBottomMarginHint(int i) {
        this.mDialogScrollView.setBottomMarginHint(i);
    }

    public void setTopMarginHint(int i) {
        this.mDialogScrollView.setTopMarginHint(i);
    }

    protected void startOpenAnimation() {
        this.mDialogScrollView.startOpenAnimation(computeWidth(), computeHeight(), new Animator.AnimatorListener() { // from class: com.sonyericsson.android.camera.view.selectabledialog.AbsSelectableDialog.1
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                AbsSelectableDialog.this.mAnimation = animator;
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                AbsSelectableDialog.this.setEnabled(true);
                AbsSelectableDialog.this.mAnimation = null;
            }
        });
    }

    protected void startCloseAnimation() {
        setEnabled(false);
        this.mDialogScrollView.startCloseAnimation(computeWidth(), computeHeight(), new Animator.AnimatorListener() { // from class: com.sonyericsson.android.camera.view.selectabledialog.AbsSelectableDialog.2
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (AbsSelectableDialog.this.mParams.animationType == AnimationType.SLIDER) {
                    if (AbsSelectableDialog.this.isPortrait()) {
                        AbsSelectableDialog.this.mSettingDialogStack.getBackground().setTranslationX(0.0f);
                    } else {
                        AbsSelectableDialog.this.mSettingDialogStack.getBackground().setTranslationY(0.0f);
                    }
                }
                AbsSelectableDialog.this.closeImmediate();
            }
        });
    }

    protected int computeWidth() {
        return this.mParams.itemWidth + getSideEdgeMargin();
    }

    protected int computeHeight() {
        return Math.min(computeContentsHeight() + getSideEdgeMargin(), isPortrait() ? this.mParams.maxHeightPortrait : this.mParams.maxHeightLandscape);
    }

    private int getSideEdgeMargin() {
        return this.mParams.dropShadowSpace * 2;
    }

    private int computeContentsHeight() {
        if (this.mIsExpandedWhenOpened) {
            return this.mDialogScrollView.getTotalArrangeHeight();
        }
        return this.mDialogScrollView.getArrangeHeightAtIndex(0);
    }

    public boolean isExpanded() {
        return this.mIsExpandedWhenOpened;
    }

    @Override // com.sonyericsson.android.camera.view.selectabledialog.ScrollContainer.OnScrollListener
    public void onScrollProgressChanged(float f) {
        if (CamLog.VERBOSE) {
            CamLog.d("scrollcontainer currentProgress:" + f);
        }
        if (f == 1.0f) {
            this.mIsExpandedWhenOpened = true;
        } else {
            this.mIsExpandedWhenOpened = false;
        }
    }
}
