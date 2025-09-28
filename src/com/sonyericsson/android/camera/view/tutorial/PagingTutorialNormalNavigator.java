package com.sonyericsson.android.camera.view.tutorial;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sonyericsson.android.camera.R;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class PagingTutorialNormalNavigator extends PagingTutorialNavigator {

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

    public PagingTutorialNormalNavigator(Context context) {
        super(context);
    }

    public PagingTutorialNormalNavigator(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public PagingTutorialNormalNavigator(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    public void setRotationY(float f) {
        super.setRotationY(f);
        this.mSkip.setRotationY(f);
        this.mNext.setRotationY(f);
        this.mGotIt.setRotationY(f);
    }

    @Override // com.sonyericsson.android.camera.view.tutorial.PagingTutorialNavigator
    protected void setPageSize(int i) {
        if (this.mPageIcons == null) {
            throw new IllegalStateException("called before inflation");
        }
        for (int i2 = 0; i2 < i; i2++) {
            ImageView imageView = new ImageView(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
            layoutParams.width = (int) getResources().getDimension(R.dimen.paging_tutorial_navigator_icon_one_side);
            layoutParams.height = (int) getResources().getDimension(R.dimen.paging_tutorial_navigator_icon_one_side);
            imageView.setLayoutParams(layoutParams);
            imageView.setImageResource(R.drawable.tutorial_page_navigator);
            this.mPageIcons.addView(imageView);
        }
    }

    @Override // com.sonyericsson.android.camera.view.tutorial.PagingTutorialNavigator
    protected int getPageCount() {
        if (this.mPageIcons == null) {
            throw new IllegalStateException("called before inflation");
        }
        return this.mPageIcons.getChildCount();
    }

    @Override // com.sonyericsson.android.camera.view.tutorial.PagingTutorialNavigator
    protected void updatePageSelected(int i) {
        if (this.mPageIcons == null) {
            throw new IllegalStateException("called before inflation");
        }
        for (int i2 = 0; i2 < getPageCount(); i2++) {
            this.mPageIcons.getChildAt(i2).setSelected(false);
        }
        this.mPageIcons.getChildAt(i).setSelected(true);
    }

    @Override // com.sonyericsson.android.camera.view.tutorial.PagingTutorialNavigator
    protected void doSingleContent() {
        if (this.mPrevIcon == null || this.mSkip == null || this.mNext == null || this.mGotIt == null) {
            throw new IllegalStateException("called before inflation");
        }
        this.mSkip.setVisibility(4);
        this.mNext.setVisibility(4);
        this.mPrevIcon.setVisibility(4);
        this.mGotIt.setVisibility(0);
        this.mPageIcons.setVisibility(4);
        updateClickEventActivation();
    }

    @Override // com.sonyericsson.android.camera.view.tutorial.PagingTutorialNavigator
    protected void doFirstPage() {
        if (this.mPrevIcon == null || this.mSkip == null || this.mNext == null || this.mGotIt == null) {
            throw new IllegalStateException("called before inflation");
        }
        if (this.mPrevIcon.getVisibility() == 0) {
            this.mPrevIcon.setVisibility(4);
            this.mSkip.setVisibility(0);
        }
        if (this.mGotIt.getVisibility() == 0) {
            this.mGotIt.setVisibility(4);
            this.mNext.setVisibility(0);
        }
        updateClickEventActivation();
    }

    @Override // com.sonyericsson.android.camera.view.tutorial.PagingTutorialNavigator
    protected void doMiddlePage() {
        if (this.mPrevIcon == null || this.mSkip == null || this.mNext == null || this.mGotIt == null) {
            throw new IllegalStateException("called before inflation");
        }
        if (this.mSkip.getVisibility() == 0) {
            this.mSkip.setVisibility(4);
            this.mPrevIcon.setVisibility(0);
        }
        if (this.mGotIt.getVisibility() == 0) {
            this.mGotIt.setVisibility(4);
            this.mNext.setVisibility(0);
        }
        updateClickEventActivation();
    }

    @Override // com.sonyericsson.android.camera.view.tutorial.PagingTutorialNavigator
    protected void doLastPage() {
        if (this.mPrevIcon == null || this.mSkip == null || this.mNext == null || this.mGotIt == null) {
            throw new IllegalStateException("called before inflation");
        }
        if (this.mSkip.getVisibility() == 0) {
            this.mSkip.setVisibility(4);
            this.mPrevIcon.setVisibility(0);
        }
        if (this.mNext.getVisibility() == 0) {
            this.mNext.setVisibility(4);
            this.mGotIt.setVisibility(0);
        }
        updateClickEventActivation();
    }

    private void updateClickEventActivation() {
        updateClickEventListener(this.mSkip);
        updateClickEventListener(this.mNext);
        updateClickEventListener(this.mPrevIcon);
        updateClickEventListener(this.mGotIt);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        this.mSkip = (TextView) findViewById(R.id.page_tutorial_skip_button);
        this.mNext = (TextView) findViewById(R.id.page_tutorial_next_button);
        this.mGotIt = (TextView) findViewById(R.id.page_tutorial_gotit_button);
        this.mPrevIcon = (ImageView) findViewById(R.id.page_tutorial_prev_icon_button);
        this.mPageIcons = (LinearLayout) findViewById(R.id.page_tutorial_paging_navigation);
        super.onFinishInflate();
    }
}
