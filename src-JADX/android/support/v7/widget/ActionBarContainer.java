package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.RestrictTo;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ActionBarContainer extends FrameLayout {
    private View mActionBarView;
    Drawable mBackground;
    private View mContextView;
    private int mHeight;
    boolean mIsSplit;
    boolean mIsStacked;
    private boolean mIsTransitioning;
    Drawable mSplitBackground;
    Drawable mStackedBackground;
    private View mTabContainer;

    @Override // android.view.ViewGroup, android.view.ViewParent
    public ActionMode startActionModeForChild(View view, ActionMode.Callback callback) {
        return null;
    }

    public ActionBarContainer(Context context) {
        this(context, null);
    }

    public ActionBarContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        ViewCompat.setBackground(this, new ActionBarBackgroundDrawable(this));
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.ActionBar);
        this.mBackground = typedArrayObtainStyledAttributes.getDrawable(R.styleable.ActionBar_background);
        this.mStackedBackground = typedArrayObtainStyledAttributes.getDrawable(R.styleable.ActionBar_backgroundStacked);
        this.mHeight = typedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.ActionBar_height, -1);
        if (getId() == R.id.split_action_bar) {
            this.mIsSplit = true;
            this.mSplitBackground = typedArrayObtainStyledAttributes.getDrawable(R.styleable.ActionBar_backgroundSplit);
        }
        typedArrayObtainStyledAttributes.recycle();
        boolean z = false;
        if (!this.mIsSplit ? !(this.mBackground != null || this.mStackedBackground != null) : this.mSplitBackground == null) {
            z = true;
        }
        setWillNotDraw(z);
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mActionBarView = findViewById(R.id.action_bar);
        this.mContextView = findViewById(R.id.action_context_bar);
    }

    public void setPrimaryBackground(Drawable drawable) {
        if (this.mBackground != null) {
            this.mBackground.setCallback(null);
            unscheduleDrawable(this.mBackground);
        }
        this.mBackground = drawable;
        if (drawable != null) {
            drawable.setCallback(this);
            if (this.mActionBarView != null) {
                this.mBackground.setBounds(this.mActionBarView.getLeft(), this.mActionBarView.getTop(), this.mActionBarView.getRight(), this.mActionBarView.getBottom());
            }
        }
        boolean z = false;
        if (!this.mIsSplit ? !(this.mBackground != null || this.mStackedBackground != null) : this.mSplitBackground == null) {
            z = true;
        }
        setWillNotDraw(z);
        invalidate();
    }

    public void setStackedBackground(Drawable drawable) {
        if (this.mStackedBackground != null) {
            this.mStackedBackground.setCallback(null);
            unscheduleDrawable(this.mStackedBackground);
        }
        this.mStackedBackground = drawable;
        if (drawable != null) {
            drawable.setCallback(this);
            if (this.mIsStacked && this.mStackedBackground != null) {
                this.mStackedBackground.setBounds(this.mTabContainer.getLeft(), this.mTabContainer.getTop(), this.mTabContainer.getRight(), this.mTabContainer.getBottom());
            }
        }
        boolean z = false;
        if (!this.mIsSplit ? !(this.mBackground != null || this.mStackedBackground != null) : this.mSplitBackground == null) {
            z = true;
        }
        setWillNotDraw(z);
        invalidate();
    }

    public void setSplitBackground(Drawable drawable) {
        if (this.mSplitBackground != null) {
            this.mSplitBackground.setCallback(null);
            unscheduleDrawable(this.mSplitBackground);
        }
        this.mSplitBackground = drawable;
        boolean z = false;
        if (drawable != null) {
            drawable.setCallback(this);
            if (this.mIsSplit && this.mSplitBackground != null) {
                this.mSplitBackground.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
            }
        }
        if (!this.mIsSplit ? !(this.mBackground != null || this.mStackedBackground != null) : this.mSplitBackground == null) {
            z = true;
        }
        setWillNotDraw(z);
        invalidate();
    }

    @Override // android.view.View
    public void setVisibility(int i) {
        super.setVisibility(i);
        boolean z = i == 0;
        if (this.mBackground != null) {
            this.mBackground.setVisible(z, false);
        }
        if (this.mStackedBackground != null) {
            this.mStackedBackground.setVisible(z, false);
        }
        if (this.mSplitBackground != null) {
            this.mSplitBackground.setVisible(z, false);
        }
    }

    @Override // android.view.View
    protected boolean verifyDrawable(Drawable drawable) {
        return (drawable == this.mBackground && !this.mIsSplit) || (drawable == this.mStackedBackground && this.mIsStacked) || ((drawable == this.mSplitBackground && this.mIsSplit) || super.verifyDrawable(drawable));
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.mBackground != null && this.mBackground.isStateful()) {
            this.mBackground.setState(getDrawableState());
        }
        if (this.mStackedBackground != null && this.mStackedBackground.isStateful()) {
            this.mStackedBackground.setState(getDrawableState());
        }
        if (this.mSplitBackground == null || !this.mSplitBackground.isStateful()) {
            return;
        }
        this.mSplitBackground.setState(getDrawableState());
    }

    @Override // android.view.ViewGroup, android.view.View
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (this.mBackground != null) {
            this.mBackground.jumpToCurrentState();
        }
        if (this.mStackedBackground != null) {
            this.mStackedBackground.jumpToCurrentState();
        }
        if (this.mSplitBackground != null) {
            this.mSplitBackground.jumpToCurrentState();
        }
    }

    public void setTransitioning(boolean z) {
        this.mIsTransitioning = z;
        setDescendantFocusability(z ? 393216 : 262144);
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return this.mIsTransitioning || super.onInterceptTouchEvent(motionEvent);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        return true;
    }

    @Override // android.view.View
    public boolean onHoverEvent(MotionEvent motionEvent) {
        super.onHoverEvent(motionEvent);
        return true;
    }

    public void setTabContainer(ScrollingTabContainerView scrollingTabContainerView) {
        if (this.mTabContainer != null) {
            removeView(this.mTabContainer);
        }
        this.mTabContainer = scrollingTabContainerView;
        if (scrollingTabContainerView != null) {
            addView(scrollingTabContainerView);
            ViewGroup.LayoutParams layoutParams = scrollingTabContainerView.getLayoutParams();
            layoutParams.width = -1;
            layoutParams.height = -2;
            scrollingTabContainerView.setAllowCollapse(false);
        }
    }

    public View getTabContainer() {
        return this.mTabContainer;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public ActionMode startActionModeForChild(View view, ActionMode.Callback callback, int i) {
        if (i != 0) {
            return super.startActionModeForChild(view, callback, i);
        }
        return null;
    }

    private boolean isCollapsed(View view) {
        return view == null || view.getVisibility() == 8 || view.getMeasuredHeight() == 0;
    }

    private int getMeasuredHeightWithMargins(View view) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        return view.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
    }

    @Override // android.widget.FrameLayout, android.view.View
    public void onMeasure(int i, int i2) {
        int measuredHeightWithMargins;
        if (this.mActionBarView == null && View.MeasureSpec.getMode(i2) == Integer.MIN_VALUE && this.mHeight >= 0) {
            i2 = View.MeasureSpec.makeMeasureSpec(Math.min(this.mHeight, View.MeasureSpec.getSize(i2)), Integer.MIN_VALUE);
        }
        super.onMeasure(i, i2);
        if (this.mActionBarView == null) {
            return;
        }
        int mode = View.MeasureSpec.getMode(i2);
        if (this.mTabContainer == null || this.mTabContainer.getVisibility() == 8 || mode == 1073741824) {
            return;
        }
        if (!isCollapsed(this.mActionBarView)) {
            measuredHeightWithMargins = getMeasuredHeightWithMargins(this.mActionBarView);
        } else {
            measuredHeightWithMargins = !isCollapsed(this.mContextView) ? getMeasuredHeightWithMargins(this.mContextView) : 0;
        }
        setMeasuredDimension(getMeasuredWidth(), Math.min(measuredHeightWithMargins + getMeasuredHeightWithMargins(this.mTabContainer), mode == Integer.MIN_VALUE ? View.MeasureSpec.getSize(i2) : Integer.MAX_VALUE));
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x004c A[PHI: r0
  0x004c: PHI (r0v8 boolean) = (r0v1 boolean), (r0v1 boolean), (r0v0 boolean) binds: [B:31:0x00ab, B:33:0x00af, B:15:0x003b] A[DONT_GENERATE, DONT_INLINE]] */
    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void onLayout(boolean r6, int r7, int r8, int r9, int r10) {
        /*
            r5 = this;
            super.onLayout(r6, r7, r8, r9, r10)
            android.view.View r6 = r5.mTabContainer
            r8 = 8
            r10 = 1
            r0 = 0
            if (r6 == 0) goto L13
            int r1 = r6.getVisibility()
            if (r1 == r8) goto L13
            r1 = r10
            goto L14
        L13:
            r1 = r0
        L14:
            if (r6 == 0) goto L35
            int r2 = r6.getVisibility()
            if (r2 == r8) goto L35
            int r8 = r5.getMeasuredHeight()
            android.view.ViewGroup$LayoutParams r2 = r6.getLayoutParams()
            android.widget.FrameLayout$LayoutParams r2 = (android.widget.FrameLayout.LayoutParams) r2
            int r3 = r6.getMeasuredHeight()
            int r3 = r8 - r3
            int r4 = r2.bottomMargin
            int r3 = r3 - r4
            int r2 = r2.bottomMargin
            int r8 = r8 - r2
            r6.layout(r7, r3, r9, r8)
        L35:
            boolean r7 = r5.mIsSplit
            if (r7 == 0) goto L4f
            android.graphics.drawable.Drawable r6 = r5.mSplitBackground
            if (r6 == 0) goto L4c
            android.graphics.drawable.Drawable r6 = r5.mSplitBackground
            int r7 = r5.getMeasuredWidth()
            int r8 = r5.getMeasuredHeight()
            r6.setBounds(r0, r0, r7, r8)
            goto Lc6
        L4c:
            r10 = r0
            goto Lc6
        L4f:
            android.graphics.drawable.Drawable r7 = r5.mBackground
            if (r7 == 0) goto La9
            android.view.View r7 = r5.mActionBarView
            int r7 = r7.getVisibility()
            if (r7 != 0) goto L79
            android.graphics.drawable.Drawable r7 = r5.mBackground
            android.view.View r8 = r5.mActionBarView
            int r8 = r8.getLeft()
            android.view.View r9 = r5.mActionBarView
            int r9 = r9.getTop()
            android.view.View r0 = r5.mActionBarView
            int r0 = r0.getRight()
            android.view.View r2 = r5.mActionBarView
            int r2 = r2.getBottom()
            r7.setBounds(r8, r9, r0, r2)
            goto La8
        L79:
            android.view.View r7 = r5.mContextView
            if (r7 == 0) goto La3
            android.view.View r7 = r5.mContextView
            int r7 = r7.getVisibility()
            if (r7 != 0) goto La3
            android.graphics.drawable.Drawable r7 = r5.mBackground
            android.view.View r8 = r5.mContextView
            int r8 = r8.getLeft()
            android.view.View r9 = r5.mContextView
            int r9 = r9.getTop()
            android.view.View r0 = r5.mContextView
            int r0 = r0.getRight()
            android.view.View r2 = r5.mContextView
            int r2 = r2.getBottom()
            r7.setBounds(r8, r9, r0, r2)
            goto La8
        La3:
            android.graphics.drawable.Drawable r7 = r5.mBackground
            r7.setBounds(r0, r0, r0, r0)
        La8:
            r0 = r10
        La9:
            r5.mIsStacked = r1
            if (r1 == 0) goto L4c
            android.graphics.drawable.Drawable r7 = r5.mStackedBackground
            if (r7 == 0) goto L4c
            android.graphics.drawable.Drawable r7 = r5.mStackedBackground
            int r8 = r6.getLeft()
            int r9 = r6.getTop()
            int r0 = r6.getRight()
            int r6 = r6.getBottom()
            r7.setBounds(r8, r9, r0, r6)
        Lc6:
            if (r10 == 0) goto Lcb
            r5.invalidate()
        Lcb:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.widget.ActionBarContainer.onLayout(boolean, int, int, int, int):void");
    }
}
