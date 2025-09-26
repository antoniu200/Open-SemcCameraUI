package com.sonyericsson.android.camera.view.tutorial;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.sonyericsson.android.camera.R;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class PagingTutorialConfirmNavigator extends PagingTutorialNavigator {

    @Nullable
    private View mConfirm;

    @Nullable
    private TextView mNext;
    private int mPageSize;

    @Nullable
    private ImageView mPrevIcon;

    public PagingTutorialConfirmNavigator(Context context) {
        super(context);
    }

    public PagingTutorialConfirmNavigator(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public PagingTutorialConfirmNavigator(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    public void setRotationY(float f) {
        if (this.mNext == null || this.mConfirm == null) {
            throw new IllegalStateException("called before inflation");
        }
        super.setRotationY(f);
        this.mNext.setRotationY(f);
        this.mConfirm.setRotationY(f);
    }

    @Override // com.sonyericsson.android.camera.view.tutorial.PagingTutorialNavigator
    protected void setPageSize(int i) {
        this.mPageSize = i;
    }

    @Override // com.sonyericsson.android.camera.view.tutorial.PagingTutorialNavigator
    protected int getPageCount() {
        return this.mPageSize;
    }

    @Override // com.sonyericsson.android.camera.view.tutorial.PagingTutorialNavigator
    protected void doSingleContent() {
        if (this.mPrevIcon == null || this.mNext == null || this.mConfirm == null) {
            throw new IllegalStateException("called before inflation");
        }
        this.mNext.setVisibility(4);
        this.mConfirm.setVisibility(0);
        this.mPrevIcon.setVisibility(4);
        updateClickEventActivation();
    }

    @Override // com.sonyericsson.android.camera.view.tutorial.PagingTutorialNavigator
    protected void doFirstPage() {
        if (this.mPrevIcon == null || this.mNext == null || this.mConfirm == null) {
            throw new IllegalStateException("called before inflation");
        }
        if (this.mPrevIcon.getVisibility() == 0) {
            this.mPrevIcon.setVisibility(4);
        }
        if (this.mConfirm.getVisibility() == 0) {
            this.mConfirm.setVisibility(4);
            this.mNext.setVisibility(0);
        }
        updateClickEventActivation();
    }

    @Override // com.sonyericsson.android.camera.view.tutorial.PagingTutorialNavigator
    protected void doMiddlePage() {
        if (this.mPrevIcon == null || this.mNext == null || this.mConfirm == null) {
            throw new IllegalStateException("called before inflation");
        }
        if (this.mPrevIcon.getVisibility() == 4) {
            this.mPrevIcon.setVisibility(0);
        }
        if (this.mConfirm.getVisibility() == 0) {
            this.mConfirm.setVisibility(4);
            this.mNext.setVisibility(0);
        }
        updateClickEventActivation();
    }

    @Override // com.sonyericsson.android.camera.view.tutorial.PagingTutorialNavigator
    protected void doLastPage() {
        if (this.mPrevIcon == null || this.mNext == null || this.mConfirm == null) {
            throw new IllegalStateException("called before inflation");
        }
        if (this.mPrevIcon.getVisibility() == 4) {
            this.mPrevIcon.setVisibility(0);
        }
        if (this.mNext.getVisibility() == 0) {
            this.mNext.setVisibility(4);
            this.mConfirm.setVisibility(0);
        }
        updateClickEventActivation();
    }

    private void updateClickEventActivation() {
        updateClickEventListener(this.mNext);
        updateClickEventListener(this.mPrevIcon);
        updateClickEventListener(findViewById(R.id.tutorial_no_button));
        updateClickEventListener(findViewById(R.id.tutorial_yes_button));
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        this.mNext = (TextView) findViewById(R.id.page_tutorial_next_button);
        this.mConfirm = findViewById(R.id.cam_core_tutorial_button_container);
        this.mPrevIcon = (ImageView) findViewById(R.id.page_tutorial_prev_icon_button);
        super.onFinishInflate();
    }
}
