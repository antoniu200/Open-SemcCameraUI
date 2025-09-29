package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.RestrictTo;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class LinearLayoutCompat extends ViewGroup {
    public static final int HORIZONTAL = 0;
    private static final int INDEX_BOTTOM = 2;
    private static final int INDEX_CENTER_VERTICAL = 0;
    private static final int INDEX_FILL = 3;
    private static final int INDEX_TOP = 1;
    public static final int SHOW_DIVIDER_BEGINNING = 1;
    public static final int SHOW_DIVIDER_END = 4;
    public static final int SHOW_DIVIDER_MIDDLE = 2;
    public static final int SHOW_DIVIDER_NONE = 0;
    public static final int VERTICAL = 1;
    private static final int VERTICAL_GRAVITY_COUNT = 4;
    private boolean mBaselineAligned;
    private int mBaselineAlignedChildIndex;
    private int mBaselineChildTop;
    private Drawable mDivider;
    private int mDividerHeight;
    private int mDividerPadding;
    private int mDividerWidth;
    private int mGravity;
    private int[] mMaxAscent;
    private int[] mMaxDescent;
    private int mOrientation;
    private int mShowDividers;
    private int mTotalLength;
    private boolean mUseLargestChild;
    private float mWeightSum;

    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public @interface DividerMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public @interface OrientationMode {
    }

    int getChildrenSkipCount(View view, int i) {
        return 0;
    }

    int getLocationOffset(View view) {
        return 0;
    }

    int getNextLocationOffset(View view) {
        return 0;
    }

    int measureNullChild(int i) {
        return 0;
    }

    @Override // android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    public LinearLayoutCompat(Context context) {
        this(context, null);
    }

    public LinearLayoutCompat(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public LinearLayoutCompat(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mBaselineAligned = true;
        this.mBaselineAlignedChildIndex = -1;
        this.mBaselineChildTop = 0;
        this.mGravity = 8388659;
        TintTypedArray tintTypedArrayObtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, attributeSet, R.styleable.LinearLayoutCompat, i, 0);
        int i2 = tintTypedArrayObtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_android_orientation, -1);
        if (i2 >= 0) {
            setOrientation(i2);
        }
        int i3 = tintTypedArrayObtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_android_gravity, -1);
        if (i3 >= 0) {
            setGravity(i3);
        }
        boolean z = tintTypedArrayObtainStyledAttributes.getBoolean(R.styleable.LinearLayoutCompat_android_baselineAligned, true);
        if (!z) {
            setBaselineAligned(z);
        }
        this.mWeightSum = tintTypedArrayObtainStyledAttributes.getFloat(R.styleable.LinearLayoutCompat_android_weightSum, -1.0f);
        this.mBaselineAlignedChildIndex = tintTypedArrayObtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_android_baselineAlignedChildIndex, -1);
        this.mUseLargestChild = tintTypedArrayObtainStyledAttributes.getBoolean(R.styleable.LinearLayoutCompat_measureWithLargestChild, false);
        setDividerDrawable(tintTypedArrayObtainStyledAttributes.getDrawable(R.styleable.LinearLayoutCompat_divider));
        this.mShowDividers = tintTypedArrayObtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_showDividers, 0);
        this.mDividerPadding = tintTypedArrayObtainStyledAttributes.getDimensionPixelSize(R.styleable.LinearLayoutCompat_dividerPadding, 0);
        tintTypedArrayObtainStyledAttributes.recycle();
    }

    public void setShowDividers(int i) {
        if (i != this.mShowDividers) {
            requestLayout();
        }
        this.mShowDividers = i;
    }

    public int getShowDividers() {
        return this.mShowDividers;
    }

    public Drawable getDividerDrawable() {
        return this.mDivider;
    }

    public void setDividerDrawable(Drawable drawable) {
        if (drawable == this.mDivider) {
            return;
        }
        this.mDivider = drawable;
        if (drawable != null) {
            this.mDividerWidth = drawable.getIntrinsicWidth();
            this.mDividerHeight = drawable.getIntrinsicHeight();
        } else {
            this.mDividerWidth = 0;
            this.mDividerHeight = 0;
        }
        setWillNotDraw(drawable == null);
        requestLayout();
    }

    public void setDividerPadding(int i) {
        this.mDividerPadding = i;
    }

    public int getDividerPadding() {
        return this.mDividerPadding;
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public int getDividerWidth() {
        return this.mDividerWidth;
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        if (this.mDivider == null) {
            return;
        }
        if (this.mOrientation == 1) {
            drawDividersVertical(canvas);
        } else {
            drawDividersHorizontal(canvas);
        }
    }

    void drawDividersVertical(Canvas canvas) {
        int bottom;
        int virtualChildCount = getVirtualChildCount();
        for (int i = 0; i < virtualChildCount; i++) {
            View virtualChildAt = getVirtualChildAt(i);
            if (virtualChildAt != null && virtualChildAt.getVisibility() != 8 && hasDividerBeforeChildAt(i)) {
                drawHorizontalDivider(canvas, (virtualChildAt.getTop() - ((LayoutParams) virtualChildAt.getLayoutParams()).topMargin) - this.mDividerHeight);
            }
        }
        if (hasDividerBeforeChildAt(virtualChildCount)) {
            View virtualChildAt2 = getVirtualChildAt(virtualChildCount - 1);
            if (virtualChildAt2 == null) {
                bottom = (getHeight() - getPaddingBottom()) - this.mDividerHeight;
            } else {
                bottom = virtualChildAt2.getBottom() + ((LayoutParams) virtualChildAt2.getLayoutParams()).bottomMargin;
            }
            drawHorizontalDivider(canvas, bottom);
        }
    }

    void drawDividersHorizontal(Canvas canvas) {
        int right;
        int left;
        int virtualChildCount = getVirtualChildCount();
        boolean zIsLayoutRtl = ViewUtils.isLayoutRtl(this);
        for (int i = 0; i < virtualChildCount; i++) {
            View virtualChildAt = getVirtualChildAt(i);
            if (virtualChildAt != null && virtualChildAt.getVisibility() != 8 && hasDividerBeforeChildAt(i)) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                if (zIsLayoutRtl) {
                    left = virtualChildAt.getRight() + layoutParams.rightMargin;
                } else {
                    left = (virtualChildAt.getLeft() - layoutParams.leftMargin) - this.mDividerWidth;
                }
                drawVerticalDivider(canvas, left);
            }
        }
        if (hasDividerBeforeChildAt(virtualChildCount)) {
            View virtualChildAt2 = getVirtualChildAt(virtualChildCount - 1);
            if (virtualChildAt2 != null) {
                LayoutParams layoutParams2 = (LayoutParams) virtualChildAt2.getLayoutParams();
                if (zIsLayoutRtl) {
                    right = (virtualChildAt2.getLeft() - layoutParams2.leftMargin) - this.mDividerWidth;
                } else {
                    right = virtualChildAt2.getRight() + layoutParams2.rightMargin;
                }
            } else if (zIsLayoutRtl) {
                right = getPaddingLeft();
            } else {
                right = (getWidth() - getPaddingRight()) - this.mDividerWidth;
            }
            drawVerticalDivider(canvas, right);
        }
    }

    void drawHorizontalDivider(Canvas canvas, int i) {
        this.mDivider.setBounds(getPaddingLeft() + this.mDividerPadding, i, (getWidth() - getPaddingRight()) - this.mDividerPadding, this.mDividerHeight + i);
        this.mDivider.draw(canvas);
    }

    void drawVerticalDivider(Canvas canvas, int i) {
        this.mDivider.setBounds(i, getPaddingTop() + this.mDividerPadding, this.mDividerWidth + i, (getHeight() - getPaddingBottom()) - this.mDividerPadding);
        this.mDivider.draw(canvas);
    }

    public boolean isBaselineAligned() {
        return this.mBaselineAligned;
    }

    public void setBaselineAligned(boolean z) {
        this.mBaselineAligned = z;
    }

    public boolean isMeasureWithLargestChildEnabled() {
        return this.mUseLargestChild;
    }

    public void setMeasureWithLargestChildEnabled(boolean z) {
        this.mUseLargestChild = z;
    }

    @Override // android.view.View
    public int getBaseline() {
        int i;
        if (this.mBaselineAlignedChildIndex < 0) {
            return super.getBaseline();
        }
        if (getChildCount() <= this.mBaselineAlignedChildIndex) {
            throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.");
        }
        View childAt = getChildAt(this.mBaselineAlignedChildIndex);
        int baseline = childAt.getBaseline();
        if (baseline == -1) {
            if (this.mBaselineAlignedChildIndex == 0) {
                return -1;
            }
            throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout points to a View that doesn't know how to get its baseline.");
        }
        int bottom = this.mBaselineChildTop;
        if (this.mOrientation == 1 && (i = this.mGravity & 112) != 48) {
            if (i == 16) {
                bottom += ((((getBottom() - getTop()) - getPaddingTop()) - getPaddingBottom()) - this.mTotalLength) / 2;
            } else if (i == 80) {
                bottom = ((getBottom() - getTop()) - getPaddingBottom()) - this.mTotalLength;
            }
        }
        return bottom + ((LayoutParams) childAt.getLayoutParams()).topMargin + baseline;
    }

    public int getBaselineAlignedChildIndex() {
        return this.mBaselineAlignedChildIndex;
    }

    public void setBaselineAlignedChildIndex(int i) {
        if (i < 0 || i >= getChildCount()) {
            throw new IllegalArgumentException("base aligned child index out of range (0, " + getChildCount() + ")");
        }
        this.mBaselineAlignedChildIndex = i;
    }

    View getVirtualChildAt(int i) {
        return getChildAt(i);
    }

    int getVirtualChildCount() {
        return getChildCount();
    }

    public float getWeightSum() {
        return this.mWeightSum;
    }

    public void setWeightSum(float f) {
        this.mWeightSum = Math.max(0.0f, f);
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        if (this.mOrientation == 1) {
            measureVertical(i, i2);
        } else {
            measureHorizontal(i, i2);
        }
    }

    @RestrictTo({RestrictTo.Scope.LIBRARY})
    protected boolean hasDividerBeforeChildAt(int i) {
        if (i == 0) {
            return (this.mShowDividers & 1) != 0;
        }
        if (i == getChildCount()) {
            return (this.mShowDividers & 4) != 0;
        }
        if ((this.mShowDividers & 2) == 0) {
            return false;
        }
        for (int i2 = i - 1; i2 >= 0; i2--) {
            if (getChildAt(i2).getVisibility() != 8) {
                return true;
            }
        }
        return false;
    }

    void measureVertical(int widthMeasureSpec, int heightMeasureSpec) {
        mTotalLength = 0;

        final int count = getVirtualChildCount();
        final int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);

        final int baselineChildIndex = mBaselineAlignedChildIndex;
        final boolean useLargestChild = mUseLargestChild;

        int largestChildHeight = 0;
        int childState = 0;
        int maxWidth = 0;
        int alternativeMaxWidth = 0;
        int weightedMaxWidth = 0;
        boolean allFillParent = true;
        boolean skippedMeasure = false;
        float totalWeight = 0f;

        for (int i = 0; i < count; i++) {
            final View child = getVirtualChildAt(i);
            if (child == null) {
                mTotalLength += measureNullChild(i);
                continue;
            }
            if (child.getVisibility() == View.GONE) {
                i += getChildrenSkipCount(child, i);
                continue;
            }

            if (hasDividerBeforeChildAt(i)) {
                mTotalLength += mDividerHeight;
            }

            final LayoutParams lp = (LayoutParams) child.getLayoutParams();
            totalWeight += lp.weight;

            if (heightMode == View.MeasureSpec.EXACTLY && lp.height == 0 && lp.weight > 0f) {
                // Defer measuring this child until we distribute remaining space
                final int totalLength = mTotalLength;
                mTotalLength = Math.max(totalLength, totalLength + lp.topMargin + lp.bottomMargin);
                skippedMeasure = true;
            } else {
                int oldHeight = Integer.MIN_VALUE;
                if (lp.height == 0 && lp.weight > 0f) {
                    // Measure with WRAP_CONTENT first so we can get a height for useLargestChild
                    oldHeight = lp.height;
                    lp.height = LayoutParams.WRAP_CONTENT;
                }

                final int usedTotal = (totalWeight == 0f) ? mTotalLength : 0;
                measureChildBeforeLayout(child, i, widthMeasureSpec, 0, heightMeasureSpec, usedTotal);

                if (oldHeight != Integer.MIN_VALUE) {
                    lp.height = oldHeight;
                }

                final int childHeight = child.getMeasuredHeight();
                final int total = mTotalLength + childHeight + lp.topMargin + lp.bottomMargin + getNextLocationOffset(child);
                mTotalLength = Math.max(mTotalLength, total);

                if (useLargestChild) {
                    largestChildHeight = Math.max(largestChildHeight, childHeight);
                }
            }

            if (baselineChildIndex >= 0 && baselineChildIndex == i + 1) {
                mBaselineChildTop = mTotalLength;
            }

            if (i < baselineChildIndex && lp.weight > 0f) {
                throw new RuntimeException(
                    "A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, " +
                    "which won't work.  Either remove the weight, or don't set mBaselineAlignedChildIndex.");
            }

            final boolean matchWidthLocally = (widthMode != View.MeasureSpec.EXACTLY && lp.width == LayoutParams.MATCH_PARENT);
            if (matchWidthLocally) {
                allFillParent = true;
            } else {
                allFillParent = false;
            }

            final int margin = lp.leftMargin + lp.rightMargin;
            final int measuredWidth = child.getMeasuredWidth() + margin;
            maxWidth = Math.max(maxWidth, measuredWidth);
            childState = View.combineMeasuredStates(childState, child.getMeasuredState());

            final boolean matchWidth = (lp.width == LayoutParams.MATCH_PARENT);
            if (matchWidth) {
                alternativeMaxWidth = Math.max(alternativeMaxWidth, margin);
            } else {
                alternativeMaxWidth = Math.max(alternativeMaxWidth, measuredWidth);
            }

            if (lp.weight > 0f) {
                // Width for weighted child: use margins only if we will remeasure later
                weightedMaxWidth = Math.max(weightedMaxWidth, matchWidth ? margin : measuredWidth);
            }

            i += getChildrenSkipCount(child, i);
        }

        if (mTotalLength > 0 && hasDividerBeforeChildAt(count)) {
            mTotalLength += mDividerHeight;
        }

        // If using largest child, ensure total length at least accounts for it
        if (useLargestChild && (heightMode == View.MeasureSpec.AT_MOST || heightMode == View.MeasureSpec.UNSPECIFIED)) {
            mTotalLength = 0;
            for (int i = 0; i < count; i++) {
                final View child = getVirtualChildAt(i);
                if (child == null) {
                    mTotalLength += measureNullChild(i);
                    continue;
                }
                if (child.getVisibility() == View.GONE) {
                    i += getChildrenSkipCount(child, i);
                    continue;
                }
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                final int total = mTotalLength + largestChildHeight + lp.topMargin + lp.bottomMargin + getNextLocationOffset(child);
                mTotalLength = Math.max(mTotalLength, total);
            }
        }

        mTotalLength += getPaddingTop() + getPaddingBottom();
        int heightSize = Math.max(mTotalLength, getSuggestedMinimumHeight());
        int heightSizeAndState = View.resolveSizeAndState(heightSize, heightMeasureSpec, 0);
        int heightAvailable = heightSizeAndState & 0x00FFFFFF;
        int remaining = heightAvailable - mTotalLength;

        if (!skippedMeasure && remaining != 0 && totalWeight > 0f) {
            float weightSum = mWeightSum > 0f ? mWeightSum : totalWeight;
            mTotalLength = 0;

            for (int i = 0; i < count; i++) {
                final View child = getVirtualChildAt(i);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (lp.weight > 0f) {
                    int share = (int) (remaining * lp.weight / weightSum);
                    int childWidthSpec = getChildMeasureSpec(widthMeasureSpec,
                            getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin,
                            lp.width);
                    int childHeight;
                    if (lp.height == 0 && heightMode == View.MeasureSpec.EXACTLY) {
                        childHeight = Math.max(0, child.getMeasuredHeight() + share);
                    } else {
                        childHeight = child.getMeasuredHeight() + share;
                        if (childHeight < 0) childHeight = 0;
                    }
                    final int childHeightSpec = View.MeasureSpec.makeMeasureSpec(childHeight, View.MeasureSpec.EXACTLY);
                    child.measure(childWidthSpec, childHeightSpec);

                    childState = View.combineMeasuredStates(childState, child.getMeasuredState() & 0xFF00);
                }

                final int margin = lp.leftMargin + lp.rightMargin;
                final int measuredWidth = child.getMeasuredWidth() + margin;
                maxWidth = Math.max(maxWidth, measuredWidth);

                final int total = mTotalLength + child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin + getNextLocationOffset(child);
                mTotalLength = Math.max(mTotalLength, total);

                final boolean matchWidth = (lp.width == LayoutParams.MATCH_PARENT);
                if (matchWidth) {
                    alternativeMaxWidth = Math.max(alternativeMaxWidth, margin);
                } else {
                    alternativeMaxWidth = Math.max(alternativeMaxWidth, measuredWidth);
                }
            }

            mTotalLength += getPaddingTop() + getPaddingBottom();
            heightSizeAndState = View.resolveSizeAndState(Math.max(mTotalLength, getSuggestedMinimumHeight()),
                    heightMeasureSpec, childState & 0xFF000000);
        } else {
            // take max of widths
            maxWidth = Math.max(maxWidth, alternativeMaxWidth);
            if (useLargestChild && heightMode != View.MeasureSpec.EXACTLY) {
                // Remeasure children with largest height
                for (int i = 0; i < count; i++) {
                    final View child = getVirtualChildAt(i);
                    if (child == null || child.getVisibility() == View.GONE) continue;
                    final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                    if (lp.weight > 0f) {
                        int childWidthSpec = View.MeasureSpec.makeMeasureSpec(child.getMeasuredWidth(), View.MeasureSpec.EXACTLY);
                        final int childHeightSpec = View.MeasureSpec.makeMeasureSpec(largestChildHeight, View.MeasureSpec.EXACTLY);
                        child.measure(childWidthSpec, childHeightSpec);
                    }
                }
            }
        }

        int widthSize;
        if (!allFillParent && widthMode != View.MeasureSpec.EXACTLY) {
            widthSize = Math.max(maxWidth, weightedMaxWidth);
        } else {
            widthSize = maxWidth;
        }

        widthSize += getPaddingLeft() + getPaddingRight();
        widthSize = Math.max(widthSize, getSuggestedMinimumWidth());

        setMeasuredDimension(View.resolveSizeAndState(widthSize, widthMeasureSpec, childState), heightSizeAndState);

        if (allFillParent) {
            forceUniformWidth(count, heightMeasureSpec);
        }
    }

    private void forceUniformWidth(int i, int i2) {
        int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
        for (int i3 = 0; i3 < i; i3++) {
            View virtualChildAt = getVirtualChildAt(i3);
            if (virtualChildAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                if (layoutParams.width == -1) {
                    int i4 = layoutParams.height;
                    layoutParams.height = virtualChildAt.getMeasuredHeight();
                    measureChildWithMargins(virtualChildAt, iMakeMeasureSpec, 0, i2, 0);
                    layoutParams.height = i4;
                }
            }
        }
    }

    void measureHorizontal(int widthMeasureSpec, int heightMeasureSpec) {
        mTotalLength = 0;

        final int count = getVirtualChildCount();
        final int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);

        if (mMaxAscent == null || mMaxDescent == null) {
            mMaxAscent = new int[4];
            mMaxDescent = new int[4];
        }
        final int[] maxAscent = mMaxAscent;
        final int[] maxDescent = mMaxDescent;
        for (int i = 0; i < 4; i++) {
            maxAscent[i] = -1;
            maxDescent[i] = -1;
        }

        final boolean baselineAligned = mBaselineAligned;
        final boolean useLargestChild = mUseLargestChild;
        final boolean isExactly = widthMode == View.MeasureSpec.EXACTLY;

        int largestChildWidth = 0;
        boolean fromLargest = false;

        int childState = 0;
        int totalHeight = 0;
        int alternativeMaxHeight = 0;
        int weightedMaxHeight = 0;
        boolean allFillParent = true;
        boolean skippedMeasure = false;
        float totalWeight = 0f;

        for (int i = 0; i < count; i++) {
            final View child = getVirtualChildAt(i);
            if (child == null) {
                mTotalLength += measureNullChild(i);
                continue;
            }
            if (child.getVisibility() == View.GONE) {
                i += getChildrenSkipCount(child, i);
                continue;
            }

            if (hasDividerBeforeChildAt(i)) {
                mTotalLength += mDividerWidth;
            }

            final LayoutParams lp = (LayoutParams) child.getLayoutParams();
            totalWeight += lp.weight;

            if (widthMode == View.MeasureSpec.EXACTLY && lp.width == 0 && lp.weight > 0f) {
                // We defer measuring this child until second pass.
                if (isExactly) {
                    mTotalLength += lp.leftMargin + lp.rightMargin;
                } else {
                    mTotalLength = Math.max(mTotalLength,
                            mTotalLength + lp.leftMargin + lp.rightMargin + getNextLocationOffset(child));
                }
                skippedMeasure = true;

                if (baselineAligned) {
                    // still need to set a 0x0 measure to keep baseline arrays in sync
                    final int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    child.measure(spec, spec);
                }
            } else {
                int oldWidth = Integer.MIN_VALUE;
                if (lp.width == 0 && lp.weight > 0f) {
                    oldWidth = lp.width;
                    lp.width = LayoutParams.WRAP_CONTENT;
                }

                final int usedTotal = (totalWeight == 0f) ? mTotalLength : 0;
                measureChildBeforeLayout(child, i, widthMeasureSpec, usedTotal, heightMeasureSpec, 0);

                if (oldWidth != Integer.MIN_VALUE) {
                    lp.width = oldWidth;
                }

                final int childWidth = child.getMeasuredWidth();
                if (isExactly) {
                    mTotalLength += childWidth + lp.leftMargin + lp.rightMargin + getNextLocationOffset(child);
                } else {
                    mTotalLength = Math.max(mTotalLength,
                            mTotalLength + childWidth + lp.leftMargin + lp.rightMargin + getNextLocationOffset(child));
                }

                if (useLargestChild) {
                    largestChildWidth = Math.max(largestChildWidth, childWidth);
                }
            }

            final boolean matchHeightLocally = (heightMode != View.MeasureSpec.EXACTLY && lp.height == LayoutParams.MATCH_PARENT);
            if (matchHeightLocally) {
                allFillParent = true;
            } else {
                allFillParent = false;
            }

            int childHeight = child.getMeasuredHeight();
            final int childStateLocal = child.getMeasuredState();
            childState = View.combineMeasuredStates(childState, childStateLocal);

            if (baselineAligned) {
                final int childBaseline = child.getBaseline();
                if (childBaseline != -1) {
                    final int gravity = (lp.gravity >= 0 ? lp.gravity : mGravity) & 0x70;
                    final int index = ((gravity >> 4) & ~0x1) >> 1;
                    maxAscent[index] = Math.max(maxAscent[index], childBaseline);
                    maxDescent[index] = Math.max(maxDescent[index], childHeight - childBaseline);
                }
            }

            totalHeight = Math.max(totalHeight, childHeight + lp.topMargin + lp.bottomMargin);
            if (heightMode != View.MeasureSpec.EXACTLY && lp.height == LayoutParams.MATCH_PARENT) {
                alternativeMaxHeight = Math.max(alternativeMaxHeight, lp.topMargin + lp.bottomMargin);
            } else {
                alternativeMaxHeight = Math.max(alternativeMaxHeight, childHeight + lp.topMargin + lp.bottomMargin);
            }

            if (lp.weight > 0f) {
                weightedMaxHeight = Math.max(weightedMaxHeight,
                        matchHeightLocally ? (lp.topMargin + lp.bottomMargin)
                                           : (childHeight + lp.topMargin + lp.bottomMargin));
            }

            i += getChildrenSkipCount(child, i);
        }

        if (mTotalLength > 0 && hasDividerBeforeChildAt(count)) {
            mTotalLength += mDividerWidth;
        }

        // Account for baseline alignment
        int ascent = Math.max(maxAscent[0], Math.max(maxAscent[1], Math.max(maxAscent[2], maxAscent[3])));
        int descent = Math.max(maxDescent[0], Math.max(maxDescent[1], Math.max(maxDescent[2], maxDescent[3])));
        totalHeight = Math.max(totalHeight, ascent + descent);

        if (useLargestChild && (widthMode == View.MeasureSpec.AT_MOST || widthMode == View.MeasureSpec.UNSPECIFIED)) {
            mTotalLength = 0;
            for (int i = 0; i < count; i++) {
                final View child = getVirtualChildAt(i);
                if (child == null) {
                    mTotalLength += measureNullChild(i);
                    continue;
                }
                if (child.getVisibility() == View.GONE) {
                    i += getChildrenSkipCount(child, i);
                    continue;
                }
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (isExactly) {
                    mTotalLength += largestChildWidth + lp.leftMargin + lp.rightMargin + getNextLocationOffset(child);
                } else {
                    mTotalLength = Math.max(mTotalLength,
                            mTotalLength + largestChildWidth + lp.leftMargin + lp.rightMargin + getNextLocationOffset(child));
                }
            }
        }

        mTotalLength += getPaddingLeft() + getPaddingRight();
        int widthSize = Math.max(mTotalLength, getSuggestedMinimumWidth());
        int widthSizeAndState = View.resolveSizeAndState(widthSize, widthMeasureSpec, 0);
        int widthAvailable = widthSizeAndState & 0x00FFFFFF;
        int remaining = widthAvailable - mTotalLength;

        if (!skippedMeasure && remaining != 0 && totalWeight > 0f) {
            float weightSum = mWeightSum > 0f ? mWeightSum : totalWeight;
            for (int i = 0; i < count; i++) {
                final View child = getVirtualChildAt(i);
                if (child.getVisibility() == View.GONE) continue;
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (lp.weight > 0f) {
                    int share = (int) (remaining * lp.weight / weightSum);
                    int childWidth = child.getMeasuredWidth() + share;
                    if (childWidth < 0) childWidth = 0;

                    final int childWidthSpec = View.MeasureSpec.makeMeasureSpec(childWidth, View.MeasureSpec.EXACTLY);
                    final int childHeightSpec = getChildMeasureSpec(heightMeasureSpec,
                            getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin, lp.height);
                    child.measure(childWidthSpec, childHeightSpec);

                    childState = View.combineMeasuredStates(childState, child.getMeasuredState() & 0xFF000000);
                }
            }
        }

        int heightSize;
        if (!allFillParent && heightMode != View.MeasureSpec.EXACTLY) {
            heightSize = Math.max(totalHeight, Math.max(alternativeMaxHeight, weightedMaxHeight));
        } else {
            heightSize = Math.max(totalHeight, alternativeMaxHeight);
        }

        heightSize += getPaddingTop() + getPaddingBottom();
        heightSize = Math.max(heightSize, getSuggestedMinimumHeight());

        setMeasuredDimension(widthSizeAndState, View.resolveSizeAndState(heightSize, heightMeasureSpec, childState << 16));

        if (allFillParent) {
            forceUniformHeight(count, widthMeasureSpec);
        }
    }

    private void forceUniformHeight(int i, int i2) {
        int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824);
        for (int i3 = 0; i3 < i; i3++) {
            View virtualChildAt = getVirtualChildAt(i3);
            if (virtualChildAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                if (layoutParams.height == -1) {
                    int i4 = layoutParams.width;
                    layoutParams.width = virtualChildAt.getMeasuredWidth();
                    measureChildWithMargins(virtualChildAt, i2, 0, iMakeMeasureSpec, 0);
                    layoutParams.width = i4;
                }
            }
        }
    }

    void measureChildBeforeLayout(View view, int i, int i2, int i3, int i4, int i5) {
        measureChildWithMargins(view, i2, i3, i4, i5);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (this.mOrientation == 1) {
            layoutVertical(i, i2, i3, i4);
        } else {
            layoutHorizontal(i, i2, i3, i4);
        }
    }

    void layoutVertical(int i, int i2, int i3, int i4) {
        int paddingTop;
        int i5;
        int i6;
        int paddingLeft = getPaddingLeft();
        int i7 = i3 - i;
        int paddingRight = i7 - getPaddingRight();
        int paddingRight2 = (i7 - paddingLeft) - getPaddingRight();
        int virtualChildCount = getVirtualChildCount();
        int i8 = this.mGravity & 112;
        int i9 = this.mGravity & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        if (i8 == 16) {
            paddingTop = (((i4 - i2) - this.mTotalLength) / 2) + getPaddingTop();
        } else if (i8 == 80) {
            paddingTop = ((getPaddingTop() + i4) - i2) - this.mTotalLength;
        } else {
            paddingTop = getPaddingTop();
        }
        int childrenSkipCount = 0;
        while (childrenSkipCount < virtualChildCount) {
            View virtualChildAt = getVirtualChildAt(childrenSkipCount);
            if (virtualChildAt == null) {
                paddingTop += measureNullChild(childrenSkipCount);
            } else {
                if (virtualChildAt.getVisibility() != 8) {
                    int measuredWidth = virtualChildAt.getMeasuredWidth();
                    int measuredHeight = virtualChildAt.getMeasuredHeight();
                    LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                    int i10 = layoutParams.gravity;
                    if (i10 < 0) {
                        i10 = i9;
                    }
                    int absoluteGravity = GravityCompat.getAbsoluteGravity(i10, ViewCompat.getLayoutDirection(this)) & 7;
                    if (absoluteGravity == 1) {
                        i5 = ((((paddingRight2 - measuredWidth) / 2) + paddingLeft) + layoutParams.leftMargin) - layoutParams.rightMargin;
                    } else if (absoluteGravity == 5) {
                        i5 = (paddingRight - measuredWidth) - layoutParams.rightMargin;
                    } else {
                        i5 = layoutParams.leftMargin + paddingLeft;
                    }
                    int i11 = i5;
                    if (hasDividerBeforeChildAt(childrenSkipCount)) {
                        paddingTop += this.mDividerHeight;
                    }
                    int i12 = paddingTop + layoutParams.topMargin;
                    setChildFrame(virtualChildAt, i11, i12 + getLocationOffset(virtualChildAt), measuredWidth, measuredHeight);
                    int nextLocationOffset = i12 + measuredHeight + layoutParams.bottomMargin + getNextLocationOffset(virtualChildAt);
                    childrenSkipCount += getChildrenSkipCount(virtualChildAt, childrenSkipCount);
                    paddingTop = nextLocationOffset;
                    i6 = 1;
                }
                childrenSkipCount += i6;
            }
            i6 = 1;
            childrenSkipCount += i6;
        }
    }

    void layoutHorizontal(int left, int top, int right, int bottom) {
        final boolean isRtl = android.support.v7.widget.ViewUtils.isLayoutRtl(this);

        final int paddingTop = getPaddingTop();
        final int height = bottom - top;
        final int paddingBottom = getPaddingBottom();
        final int bottomEdge = height - paddingBottom;
        final int innerHeight = height - paddingTop - paddingBottom;

        final int count = getVirtualChildCount();

        int majorGravity = mGravity & 0x800007;
        final int minorGravity = mGravity & 0x70;

        final int layoutDir = android.support.v4.view.ViewCompat.getLayoutDirection(this);
        majorGravity = android.support.v4.view.GravityCompat.getAbsoluteGravity(majorGravity, layoutDir);

        int childLeft;
        switch (majorGravity) {
            case android.view.Gravity.CENTER_HORIZONTAL:
                childLeft = getPaddingLeft() + (right - left - mTotalLength) / 2;
                break;
            case android.view.Gravity.RIGHT:
                childLeft = getPaddingLeft() + (right - left) - mTotalLength;
                break;
            case android.view.Gravity.LEFT:
            default:
                childLeft = getPaddingLeft();
                break;
        }

        int start, dir;
        if (isRtl) {
            start = count - 1;
            dir = -1;
        } else {
            start = 0;
            dir = 1;
        }

        for (int i = 0, index = start; i < count; i++, index += dir) {
            final View child = getVirtualChildAt(index);
            if (child == null) {
                childLeft += measureNullChild(index);
                continue;
            }
            if (child.getVisibility() == View.GONE) {
                i += getChildrenSkipCount(child, index);
                continue;
            }

            final int childWidth = child.getMeasuredWidth();
            final int childHeight = child.getMeasuredHeight();
            final LayoutParams lp = (LayoutParams) child.getLayoutParams();

            int childTop;
            int layoutGravity = lp.gravity;
            if (layoutGravity < 0) {
                layoutGravity = minorGravity;
            }
            switch (layoutGravity & 0x70) {
                case android.view.Gravity.TOP: {
                    childTop = paddingTop + lp.topMargin;
                    final int baseline = (mBaselineAligned && lp.height != LayoutParams.MATCH_PARENT) ? child.getBaseline() : -1;
                    if (baseline != -1) {
                        final int indexA = (((layoutGravity & 0x70) >> 4) & ~0x1) >> 1;
                        childTop += (mMaxAscent[1] - baseline); // aligns to ascent bucket per fallback
                    }
                    break;
                }
                case android.view.Gravity.BOTTOM: {
                    childTop = bottomEdge - childHeight - lp.bottomMargin;
                    final int baseline = (mBaselineAligned && lp.height != LayoutParams.MATCH_PARENT) ? child.getBaseline() : -1;
                    if (baseline != -1) {
                        final int delta = childHeight - baseline;
                        childTop -= (mMaxDescent[2] - delta);
                    }
                    break;
                }
                case android.view.Gravity.CENTER_VERTICAL:
                default: {
                    childTop = paddingTop + (innerHeight - childHeight) / 2
                            + lp.topMargin - lp.bottomMargin;
                    break;
                }
            }

            if (hasDividerBeforeChildAt(index)) {
                childLeft += mDividerWidth;
            }

            final int leftMargin = lp.leftMargin;
            final int locationOffset = getLocationOffset(child);
            final int cl = childLeft + leftMargin + locationOffset;

            setChildFrame(child, cl, childTop, childWidth, childHeight);

            childLeft = cl + childWidth + lp.rightMargin + getNextLocationOffset(child);
            i += getChildrenSkipCount(child, index);
        }
    }

    private void setChildFrame(View view, int i, int i2, int i3, int i4) {
        view.layout(i, i2, i3 + i, i4 + i2);
    }

    public void setOrientation(int i) {
        if (this.mOrientation != i) {
            this.mOrientation = i;
            requestLayout();
        }
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public void setGravity(int i) {
        if (this.mGravity != i) {
            if ((8388615 & i) == 0) {
                i |= GravityCompat.START;
            }
            if ((i & 112) == 0) {
                i |= 48;
            }
            this.mGravity = i;
            requestLayout();
        }
    }

    public int getGravity() {
        return this.mGravity;
    }

    public void setHorizontalGravity(int i) {
        int i2 = i & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        if ((8388615 & this.mGravity) != i2) {
            this.mGravity = i2 | (this.mGravity & (-8388616));
            requestLayout();
        }
    }

    public void setVerticalGravity(int i) {
        int i2 = i & 112;
        if ((this.mGravity & 112) != i2) {
            this.mGravity = i2 | (this.mGravity & (-113));
            requestLayout();
        }
    }

    @Override // android.view.ViewGroup
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public LayoutParams generateDefaultLayoutParams() {
        if (this.mOrientation == 0) {
            return new LayoutParams(-2, -2);
        }
        if (this.mOrientation == 1) {
            return new LayoutParams(-1, -2);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    @Override // android.view.ViewGroup
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName(LinearLayoutCompat.class.getName());
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(LinearLayoutCompat.class.getName());
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        public int gravity;
        public float weight;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.gravity = -1;
            TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.LinearLayoutCompat_Layout);
            this.weight = typedArrayObtainStyledAttributes.getFloat(R.styleable.LinearLayoutCompat_Layout_android_layout_weight, 0.0f);
            this.gravity = typedArrayObtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_Layout_android_layout_gravity, -1);
            typedArrayObtainStyledAttributes.recycle();
        }

        public LayoutParams(int i, int i2) {
            super(i, i2);
            this.gravity = -1;
            this.weight = 0.0f;
        }

        public LayoutParams(int i, int i2, float f) {
            super(i, i2);
            this.gravity = -1;
            this.weight = f;
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.gravity = -1;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
            this.gravity = -1;
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((ViewGroup.MarginLayoutParams) layoutParams);
            this.gravity = -1;
            this.weight = layoutParams.weight;
            this.gravity = layoutParams.gravity;
        }
    }
}
