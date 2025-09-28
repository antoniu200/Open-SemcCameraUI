package com.sonyericsson.android.camera.view.tutorial;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.view.tutorial.PagingTutorialContentView;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class PagingTutorialNavigator extends FrameLayout implements ViewPager.OnPageChangeListener {
    private PagingTutorialContentView.PagingTutorialController mController;
    protected final View.OnClickListener mOnClickListener;

    protected abstract void doFirstPage();

    protected abstract void doLastPage();

    protected abstract void doMiddlePage();

    protected abstract void doSingleContent();

    protected abstract int getPageCount();

    @Override // android.support.v4.view.ViewPager.OnPageChangeListener
    public void onPageScrollStateChanged(int i) {
    }

    @Override // android.support.v4.view.ViewPager.OnPageChangeListener
    public void onPageScrolled(int i, float f, int i2) {
    }

    protected abstract void setPageSize(int i);

    protected void updatePageSelected(int i) {
    }

    public PagingTutorialNavigator(Context context) {
        super(context);
        this.mOnClickListener = new View.OnClickListener() { // from class: com.sonyericsson.android.camera.view.tutorial.PagingTutorialNavigator.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (PagingTutorialNavigator.this.mController == null) {
                }
                switch (view.getId()) {
                    case R.id.page_tutorial_gotit_button /* 2131296487 */:
                    case R.id.page_tutorial_skip_button /* 2131296491 */:
                    case R.id.tutorial_no_button /* 2131296676 */:
                    case R.id.tutorial_yes_button /* 2131296684 */:
                        PagingTutorialNavigator.this.mController.closeTutorial(view);
                        break;
                    case R.id.page_tutorial_next_button /* 2131296488 */:
                        PagingTutorialNavigator.this.mController.movePageToNext();
                        break;
                    case R.id.page_tutorial_prev_icon_button /* 2131296490 */:
                        PagingTutorialNavigator.this.mController.movePageToBack();
                        break;
                }
            }
        };
    }

    public PagingTutorialNavigator(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mOnClickListener = new View.OnClickListener() { // from class: com.sonyericsson.android.camera.view.tutorial.PagingTutorialNavigator.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (PagingTutorialNavigator.this.mController == null) {
                }
                switch (view.getId()) {
                    case R.id.page_tutorial_gotit_button /* 2131296487 */:
                    case R.id.page_tutorial_skip_button /* 2131296491 */:
                    case R.id.tutorial_no_button /* 2131296676 */:
                    case R.id.tutorial_yes_button /* 2131296684 */:
                        PagingTutorialNavigator.this.mController.closeTutorial(view);
                        break;
                    case R.id.page_tutorial_next_button /* 2131296488 */:
                        PagingTutorialNavigator.this.mController.movePageToNext();
                        break;
                    case R.id.page_tutorial_prev_icon_button /* 2131296490 */:
                        PagingTutorialNavigator.this.mController.movePageToBack();
                        break;
                }
            }
        };
    }

    public PagingTutorialNavigator(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mOnClickListener = new View.OnClickListener() { // from class: com.sonyericsson.android.camera.view.tutorial.PagingTutorialNavigator.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (PagingTutorialNavigator.this.mController == null) {
                }
                switch (view.getId()) {
                    case R.id.page_tutorial_gotit_button /* 2131296487 */:
                    case R.id.page_tutorial_skip_button /* 2131296491 */:
                    case R.id.tutorial_no_button /* 2131296676 */:
                    case R.id.tutorial_yes_button /* 2131296684 */:
                        PagingTutorialNavigator.this.mController.closeTutorial(view);
                        break;
                    case R.id.page_tutorial_next_button /* 2131296488 */:
                        PagingTutorialNavigator.this.mController.movePageToNext();
                        break;
                    case R.id.page_tutorial_prev_icon_button /* 2131296490 */:
                        PagingTutorialNavigator.this.mController.movePageToBack();
                        break;
                }
            }
        };
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setMotionEventSplittingEnabled(false);
    }

    protected void setViewController(PagingTutorialContentView.PagingTutorialController pagingTutorialController) {
        this.mController = pagingTutorialController;
    }

    @Override // android.support.v4.view.ViewPager.OnPageChangeListener
    public void onPageSelected(int i) {
        if (getPageCount() == 1) {
            doSingleContent();
            return;
        }
        updatePageSelected(i);
        if (i == 0) {
            doFirstPage();
        } else if (i == getPageCount() - 1) {
            doLastPage();
        } else {
            doMiddlePage();
        }
    }

    protected void updateClickEventListener(View view) {
        view.setOnClickListener(view.getVisibility() == 0 ? this.mOnClickListener : null);
    }
}
