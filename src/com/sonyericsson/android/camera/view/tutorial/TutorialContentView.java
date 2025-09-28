package com.sonyericsson.android.camera.view.tutorial;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import com.sonyericsson.android.camera.setting.StoredSettings;
import com.sonyericsson.android.camera.view.tutorial.TutorialController;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class TutorialContentView extends RelativeLayout implements View.OnClickListener {
    protected TutorialContent mContent;
    private OnClickCloseButtonListener mOnClickCloseButtonListener;

    public interface OnClickCloseButtonListener {
        void onClickCloseButton(View view);
    }

    protected abstract void onUpdateViewContent();

    public TutorialContentView(Context context) {
        super(context);
    }

    public TutorialContentView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public TutorialContentView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    protected final void setContent(TutorialContent tutorialContent) {
        this.mContent = tutorialContent;
        if (this.mContent.isPortrait()) {
            onLayoutToPortrait();
        } else {
            onLayoutToLandscape();
        }
    }

    protected final void setOnClickCloseButtonListener(OnClickCloseButtonListener onClickCloseButtonListener) {
        this.mOnClickCloseButtonListener = onClickCloseButtonListener;
    }

    protected void onLayoutToLandscape() {
        onUpdateViewContent();
    }

    protected void onLayoutToPortrait() {
        onUpdateViewContent();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        notifyOnDoneClicked(view);
    }

    protected void notifyOnDoneClicked(View view) {
        if (this.mOnClickCloseButtonListener != null) {
            this.mOnClickCloseButtonListener.onClickCloseButton(view);
        }
    }

    protected static class TutorialPageInfo {
        final int pageIndexByType;
        final TutorialController.TutorialType type;

        public TutorialPageInfo(TutorialController.TutorialType tutorialType, int i) {
            this.type = tutorialType;
            this.pageIndexByType = i;
        }
    }

    public static abstract class TutorialContent {
        protected int mLayoutId;
        protected int mOrientation = 0;
        protected Object[] mParams;

        protected boolean canShowContent(StoredSettings storedSettings) {
            return true;
        }

        protected abstract TutorialPageInfo getCurrentTutorialPageInfo();

        protected abstract TutorialPageInfo getCurrentTutorialPageInfo(int i);

        protected abstract int getPages();

        protected abstract TutorialContent getTutorialContent(TutorialController.TutorialType tutorialType);

        protected abstract List<TutorialController.TutorialType> getTutorialTypes();

        protected abstract boolean isSimpleTutorialContent();

        protected abstract void setupResource();

        protected TutorialContent(int i) {
            changeOrientation(i);
        }

        protected TutorialContent(int i, Object... objArr) {
            changeOrientation(i);
            this.mParams = objArr;
        }

        protected final boolean isPortrait() {
            return this.mOrientation == 1;
        }

        protected final boolean changeOrientation(int i) {
            boolean z = this.mOrientation != i;
            if (z) {
                this.mOrientation = i;
                setupResource();
            }
            return z;
        }

        protected boolean equalsWith(TutorialContent tutorialContent) {
            return tutorialContent != null && getClass().equals(tutorialContent.getClass()) && this.mOrientation == tutorialContent.mOrientation;
        }
    }
}
