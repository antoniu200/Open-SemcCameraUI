package com.sonyericsson.android.camera.view.selectabledialog;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.view.selectabledialog.AbsSelectableDialog;
import com.sonyericsson.android.camera.view.selectabledialog.ScrollContainer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class AbsDialogScrollView extends FrameLayout {
    protected final List<ContentArrange> mArranges;
    protected FrameLayout mBackground;
    protected int mBottomMarginHint;
    protected LinearLayout mContentsBinder;
    private ScrollView mDefaultScroller;
    private final Interpolator mInterporater;
    protected boolean mIsExpandedWhenOpened;
    protected int mOrientation;
    protected AbsSelectableDialog.Params mParams;
    protected ScrollContainer mSomcScroller;
    protected int mTopMarginHint;

    protected interface AnimationFactory {
        void close(int i, int i2, Animator.AnimatorListener animatorListener);

        void open(int i, int i2, Animator.AnimatorListener animatorListener);
    }

    protected abstract void addContent(String str, ArrayAdapter arrayAdapter);

    protected abstract int getScrollableContentLength();

    public void selectTagItem(Object obj) {
    }

    public abstract void updateRotatableLayout(ViewGroup viewGroup, int i, int i2);

    public AbsDialogScrollView(@NonNull Context context) {
        super(context);
        this.mOrientation = 2;
        this.mInterporater = new PathInterpolator(0.645f, 0.045f, 0.355f, 1.0f);
        this.mArranges = new ArrayList();
    }

    protected int dimenToPixel(int i) {
        return getResources().getDimensionPixelSize(i);
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (isEnabled()) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        return true;
    }

    protected void setup(boolean z, AbsSelectableDialog.Params params, ScrollContainer.OnScrollListener onScrollListener, int i, int i2, boolean z2) {
        this.mIsExpandedWhenOpened = z2;
        this.mParams = new AbsSelectableDialog.Params(params);
        inflate(getContext(), R.layout.selectable_dialog_layout, this);
        this.mBackground = (FrameLayout) findViewById(R.id.selectable_dialog_inner_background);
        this.mBackground.getLayoutParams().width = i;
        ((FrameLayout.LayoutParams) this.mBackground.getLayoutParams()).gravity = i2;
        if (this.mParams.dropShadowSpace > 0) {
            this.mBackground.setBackgroundResource(R.drawable.cam_core_dialog_dropshadow_icn);
            this.mBackground.setPadding(this.mParams.dropShadowSpace, this.mParams.dropShadowSpace, this.mParams.dropShadowSpace, this.mParams.dropShadowSpace);
        }
        if (z) {
            this.mSomcScroller = (ScrollContainer) findViewById(R.id.somc_scroller);
            this.mSomcScroller.setSettingMenuParams(this.mParams);
            this.mSomcScroller.setOnScrollListener(onScrollListener);
            this.mSomcScroller.setVisibility(0);
            this.mContentsBinder = (LinearLayout) this.mSomcScroller.findViewById(R.id.somc_scroller_content_binder);
            this.mSomcScroller.post(new Runnable() { // from class: com.sonyericsson.android.camera.view.selectabledialog.AbsDialogScrollView.1
                @Override // java.lang.Runnable
                public void run() {
                    AbsDialogScrollView.this.onInitializeScroll();
                }
            });
            return;
        }
        this.mDefaultScroller = (ScrollView) findViewById(R.id.default_scroller);
        this.mDefaultScroller.setVisibility(0);
        if (this.mParams.scrollbarFadeDuration > 0) {
            this.mDefaultScroller.setScrollBarFadeDuration(this.mParams.scrollbarFadeDuration);
        }
        if (this.mParams.scrollBarDefaultDelayBeforeFade > 0) {
            this.mDefaultScroller.setScrollBarDefaultDelayBeforeFade(this.mParams.scrollBarDefaultDelayBeforeFade);
        }
        this.mContentsBinder = (LinearLayout) this.mDefaultScroller.findViewById(R.id.default_scroller_content_binder);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        Iterator<ContentArrange> it = this.mArranges.iterator();
        while (it.hasNext()) {
            it.next().release();
        }
        this.mArranges.clear();
        super.onDetachedFromWindow();
    }

    protected void onInitializeScroll() {
        int initialDisplayHeight;
        if (this.mArranges.size() == 0) {
            if (CamLog.VERBOSE) {
                CamLog.d("panel size is 0");
            }
        } else {
            if (this.mIsExpandedWhenOpened) {
                int iComputeScrollOffset = this.mArranges.get(0).computeScrollOffset();
                if (isPortrait()) {
                    initialDisplayHeight = this.mParams.maxHeightPortrait - getInitialDisplayHeight();
                } else {
                    initialDisplayHeight = this.mParams.maxHeightLandscape - getInitialDisplayHeight();
                }
                scrollTo(0, iComputeScrollOffset + initialDisplayHeight);
                return;
            }
            scrollTo(0, 0);
        }
    }

    @Override // android.view.View
    public void scrollTo(int i, int i2) {
        if (this.mSomcScroller != null) {
            this.mSomcScroller.scrollTo(i, i2);
        }
        if (this.mDefaultScroller != null) {
            this.mDefaultScroller.scrollTo(i, i2);
        }
    }

    protected void setUiOrientation(int i) {
        this.mOrientation = i;
        if (this.mSomcScroller != null) {
            this.mSomcScroller.setOrientation(i);
        }
    }

    protected void setBottomMarginHint(int i) {
        if (this.mBottomMarginHint != i) {
            this.mBottomMarginHint = i;
        }
    }

    protected void setTopMarginHint(int i) {
        if (this.mTopMarginHint != i) {
            this.mTopMarginHint = i;
        }
    }

    protected void setScrollStatus(ScrollContainer.Status status) {
        this.mSomcScroller.setCurrentStatus(status);
    }

    protected void addContentView(View view) {
        this.mContentsBinder.addView(view);
    }

    protected boolean isPortrait() {
        return this.mOrientation == 1;
    }

    protected void updateDefaultScrollPosition() {
        this.mSomcScroller.setSettingDefaultHeight(getInitialDisplayHeight());
        this.mSomcScroller.setChildHeight(getScrollableContentLength());
    }

    protected int getArrangeHeightAtIndex(int i) {
        if (this.mArranges.isEmpty()) {
            return 0;
        }
        return this.mArranges.get(i).computeHeight();
    }

    protected int getTotalArrangeHeight() {
        if (this.mArranges.isEmpty()) {
            return 0;
        }
        int iComputeHeight = 0;
        for (int i = 0; i < this.mArranges.size(); i++) {
            iComputeHeight += this.mArranges.get(i).computeHeight();
        }
        return iComputeHeight;
    }

    protected FrameLayout getBackgroundLayout() {
        return this.mBackground;
    }

    protected LinearLayout getContentLayout() {
        return this.mContentsBinder;
    }

    protected int getInitialDisplayHeight() {
        return calculateInitialDisplayHeight(R.dimen.setting_1st_layer_value_default_height);
    }

    protected int calculateInitialDisplayHeight(int i) {
        int iDimenToPixel = dimenToPixel(i);
        int scrollableContentLength = getScrollableContentLength();
        return iDimenToPixel > scrollableContentLength ? scrollableContentLength : iDimenToPixel;
    }

    protected int getScrolledHeight() {
        if (this.mSomcScroller == null) {
            return 0;
        }
        return this.mSomcScroller.getScrolledHeight();
    }

    protected ScrollContainer.Status getScrollStatus() {
        if (this.mSomcScroller == null) {
            return null;
        }
        return this.mSomcScroller.getCurrentStatus();
    }

    protected View findItemViewWithTag(Object obj) {
        Iterator<ContentArrange> it = this.mArranges.iterator();
        while (it.hasNext()) {
            View viewFindViewWithTag = it.next().findViewWithTag(obj);
            if (viewFindViewWithTag != null) {
                return viewFindViewWithTag;
            }
        }
        return null;
    }

    protected void startOpenAnimation(int i, int i2, Animator.AnimatorListener animatorListener) {
        getAnimationFactory().open(i, i2, animatorListener);
    }

    protected void startCloseAnimation(int i, int i2, Animator.AnimatorListener animatorListener) {
        getAnimationFactory().close(i, i2, animatorListener);
    }

    protected AnimationFactory getAnimationFactory() {
        switch (this.mParams.animationType) {
            case FADE:
                return new FadeAnimation();
            case SLIDER:
                return new SliderAnimation();
            default:
                return new AnimationFactory() { // from class: com.sonyericsson.android.camera.view.selectabledialog.AbsDialogScrollView.2
                    @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsDialogScrollView.AnimationFactory
                    public void open(int i, int i2, Animator.AnimatorListener animatorListener) {
                    }

                    @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsDialogScrollView.AnimationFactory
                    public void close(int i, int i2, Animator.AnimatorListener animatorListener) {
                        if (animatorListener != null) {
                            animatorListener.onAnimationEnd(null);
                        }
                    }
                };
        }
    }

    private class FadeAnimation implements AnimationFactory {
        private final int mDuration;

        private FadeAnimation() {
            this.mDuration = AbsDialogScrollView.this.getResources().getInteger(R.integer.setting_fade_animation_duration);
        }

        @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsDialogScrollView.AnimationFactory
        public void open(int i, int i2, Animator.AnimatorListener animatorListener) {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(ObjectAnimator.ofFloat(AbsDialogScrollView.this.getBackgroundLayout(), "alpha", 0.0f, 1.0f), ObjectAnimator.ofFloat(AbsDialogScrollView.this.getBackgroundLayout(), "translationY", i2 / 4.0f, 0.0f));
            animatorSet.setDuration(this.mDuration);
            animatorSet.setInterpolator(AbsDialogScrollView.this.mInterporater);
            if (animatorListener != null) {
                animatorSet.addListener(animatorListener);
            }
            animatorSet.start();
        }

        @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsDialogScrollView.AnimationFactory
        public void close(int i, int i2, Animator.AnimatorListener animatorListener) {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(ObjectAnimator.ofFloat(AbsDialogScrollView.this.getBackgroundLayout(), "alpha", 1.0f, 0.0f), ObjectAnimator.ofFloat(AbsDialogScrollView.this.getBackgroundLayout(), "translationY", 0.0f, i2 / 4.0f));
            animatorSet.setDuration(this.mDuration);
            animatorSet.setInterpolator(AbsDialogScrollView.this.mInterporater);
            if (animatorListener != null) {
                animatorSet.addListener(animatorListener);
            }
            animatorSet.start();
        }
    }

    private class SliderAnimation implements AnimationFactory {
        private final int mDuration;

        private SliderAnimation() {
            this.mDuration = AbsDialogScrollView.this.getResources().getInteger(R.integer.setting_fade_animation_duration);
        }

        @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsDialogScrollView.AnimationFactory
        public void open(int i, int i2, Animator.AnimatorListener animatorListener) {
            ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(AbsDialogScrollView.this.getBackgroundLayout(), "translationY", i2, 0.0f);
            objectAnimatorOfFloat.setDuration(this.mDuration);
            objectAnimatorOfFloat.setInterpolator(AbsDialogScrollView.this.mInterporater);
            if (animatorListener != null) {
                objectAnimatorOfFloat.addListener(animatorListener);
            }
            objectAnimatorOfFloat.start();
        }

        @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsDialogScrollView.AnimationFactory
        public void close(int i, int i2, Animator.AnimatorListener animatorListener) {
            ObjectAnimator objectAnimatorOfFloat = ObjectAnimator.ofFloat(AbsDialogScrollView.this.getBackgroundLayout(), "translationY", 0.0f, i2);
            objectAnimatorOfFloat.setDuration(this.mDuration);
            objectAnimatorOfFloat.setInterpolator(AbsDialogScrollView.this.mInterporater);
            if (animatorListener != null) {
                objectAnimatorOfFloat.addListener(animatorListener);
            }
            objectAnimatorOfFloat.start();
        }
    }

    protected static abstract class ContentArrange extends DataSetObserver {
        protected ViewGroup container;
        protected ArrayAdapter mAdapter;
        protected LinearLayout mRowItems;

        protected int computeArrangeHeight() {
            return 0;
        }

        protected int computeHeight() {
            return 0;
        }

        protected int computeScrollOffset() {
            return 0;
        }

        protected View findViewWithTag(Object obj) {
            return null;
        }

        protected abstract void setup();

        protected ContentArrange(ArrayAdapter arrayAdapter) {
            this.mAdapter = arrayAdapter;
            this.mAdapter.registerDataSetObserver(this);
        }

        protected View getPositionItemView(int i) {
            return this.mRowItems.getChildAt(i);
        }

        protected ViewGroup getPositionItemContainerView(int i) {
            return this.mRowItems;
        }

        protected void addItemView(View view, int i) {
            this.mRowItems.addView(view);
        }

        protected void updateItems(ArrayAdapter arrayAdapter) {
            this.mAdapter.unregisterDataSetObserver(this);
            this.mAdapter = arrayAdapter;
            this.mAdapter.registerDataSetObserver(this);
            fetchItems();
        }

        protected int getItemCount() {
            return this.mAdapter.getCount();
        }

        protected View getView() {
            return this.container;
        }

        protected void fetchItems() {
            if (CamLog.VERBOSE) {
                CamLog.d("fetchItems() E prev-items:" + this.mRowItems.getChildCount() + " adapter-size:" + this.mAdapter.getCount());
            }
            if (this.mRowItems.getChildCount() > this.mAdapter.getCount()) {
                this.mRowItems.removeViews(this.mAdapter.getCount(), this.mRowItems.getChildCount() - this.mAdapter.getCount());
            }
            for (int i = 0; i < this.mAdapter.getCount(); i++) {
                if (i < this.mRowItems.getChildCount()) {
                    this.mAdapter.getView(i, getPositionItemView(i), getPositionItemContainerView(i));
                    if (CamLog.VERBOSE) {
                        CamLog.d("Update existing item, size:" + this.mRowItems.getChildCount());
                    }
                } else {
                    addItemView(this.mAdapter.getView(i, null, getPositionItemContainerView(i)), i);
                    if (CamLog.VERBOSE) {
                        CamLog.d("Add new item, size:" + this.mRowItems.getChildCount());
                    }
                }
            }
            if (CamLog.VERBOSE) {
                CamLog.d("fetchItems() X");
            }
        }

        protected void release() {
            this.mAdapter.unregisterDataSetObserver(this);
        }
    }
}
