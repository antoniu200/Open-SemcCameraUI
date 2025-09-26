// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.widget;

import android.graphics.drawable.Drawable;
import android.support.v4.view.GravityCompat;
import android.support.v7.appcompat.R;
import android.view.ViewGroup;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.View$MeasureSpec;
import android.util.AttributeSet;
import android.support.annotation.Nullable;
import android.content.Context;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class AlertDialogLayout extends LinearLayoutCompat
{
    public AlertDialogLayout(@Nullable final Context context) {
        super(context);
    }
    
    public AlertDialogLayout(@Nullable final Context context, @Nullable final AttributeSet set) {
        super(context, set);
    }
    
    private void forceUniformWidth(final int n, final int n2) {
        final int measureSpec = View$MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), 1073741824);
        for (int i = 0; i < n; ++i) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                if (layoutParams.width == -1) {
                    final int height = layoutParams.height;
                    layoutParams.height = child.getMeasuredHeight();
                    this.measureChildWithMargins(child, measureSpec, 0, n2, 0);
                    layoutParams.height = height;
                }
            }
        }
    }
    
    private static int resolveMinimumHeight(final View view) {
        final int minimumHeight = ViewCompat.getMinimumHeight(view);
        if (minimumHeight > 0) {
            return minimumHeight;
        }
        if (view instanceof ViewGroup) {
            final ViewGroup viewGroup = (ViewGroup)view;
            if (viewGroup.getChildCount() == 1) {
                return resolveMinimumHeight(viewGroup.getChildAt(0));
            }
        }
        return 0;
    }
    
    private void setChildFrame(final View view, final int n, final int n2, final int n3, final int n4) {
        view.layout(n, n2, n3 + n, n4 + n2);
    }
    
    private boolean tryOnMeasure(final int n, final int n2) {
        final int childCount = this.getChildCount();
        View view = null;
        View view3;
        View view2 = view3 = null;
        for (int i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                final int id = child.getId();
                if (id == R.id.topPanel) {
                    view = child;
                }
                else if (id == R.id.buttonPanel) {
                    view2 = child;
                }
                else {
                    if (id != R.id.contentPanel && id != R.id.customPanel) {
                        return false;
                    }
                    if (view3 != null) {
                        return false;
                    }
                    view3 = child;
                }
            }
        }
        final int mode = View$MeasureSpec.getMode(n2);
        final int size = View$MeasureSpec.getSize(n2);
        final int mode2 = View$MeasureSpec.getMode(n);
        int n3 = this.getPaddingTop() + this.getPaddingBottom();
        int n4;
        if (view != null) {
            view.measure(n, 0);
            n3 += view.getMeasuredHeight();
            n4 = View.combineMeasuredStates(0, view.getMeasuredState());
        }
        else {
            n4 = 0;
        }
        int resolveMinimumHeight;
        int b;
        if (view2 != null) {
            view2.measure(n, 0);
            resolveMinimumHeight = resolveMinimumHeight(view2);
            b = view2.getMeasuredHeight() - resolveMinimumHeight;
            n3 += resolveMinimumHeight;
            n4 = View.combineMeasuredStates(n4, view2.getMeasuredState());
        }
        else {
            resolveMinimumHeight = 0;
            b = 0;
        }
        int measuredHeight;
        if (view3 != null) {
            int measureSpec;
            if (mode == 0) {
                measureSpec = 0;
            }
            else {
                measureSpec = View$MeasureSpec.makeMeasureSpec(Math.max(0, size - n3), mode);
            }
            view3.measure(n, measureSpec);
            measuredHeight = view3.getMeasuredHeight();
            n3 += measuredHeight;
            n4 = View.combineMeasuredStates(n4, view3.getMeasuredState());
        }
        else {
            measuredHeight = 0;
        }
        final int a = size - n3;
        int n5 = n4;
        int n6 = a;
        int n7 = n3;
        if (view2 != null) {
            final int min = Math.min(a, b);
            int n8 = a;
            int n9 = resolveMinimumHeight;
            if (min > 0) {
                n8 = a - min;
                n9 = resolveMinimumHeight + min;
            }
            view2.measure(n, View$MeasureSpec.makeMeasureSpec(n9, 1073741824));
            n7 = n3 - resolveMinimumHeight + view2.getMeasuredHeight();
            final int combineMeasuredStates = View.combineMeasuredStates(n4, view2.getMeasuredState());
            n6 = n8;
            n5 = combineMeasuredStates;
        }
        int combineMeasuredStates2 = n5;
        int n10 = n7;
        if (view3 != null) {
            combineMeasuredStates2 = n5;
            n10 = n7;
            if (n6 > 0) {
                view3.measure(n, View$MeasureSpec.makeMeasureSpec(measuredHeight + n6, mode));
                n10 = n7 - measuredHeight + view3.getMeasuredHeight();
                combineMeasuredStates2 = View.combineMeasuredStates(n5, view3.getMeasuredState());
            }
        }
        int j = 0;
        int a2 = 0;
        while (j < childCount) {
            final View child2 = this.getChildAt(j);
            int max = a2;
            if (child2.getVisibility() != 8) {
                max = Math.max(a2, child2.getMeasuredWidth());
            }
            ++j;
            a2 = max;
        }
        this.setMeasuredDimension(View.resolveSizeAndState(a2 + (this.getPaddingLeft() + this.getPaddingRight()), n, combineMeasuredStates2), View.resolveSizeAndState(n10, n2, 0));
        if (mode2 != 1073741824) {
            this.forceUniformWidth(childCount, n2);
        }
        return true;
    }
    
    @Override
    protected void onLayout(final boolean b, int n, int gravity, int n2, int i) {
        final int paddingLeft = this.getPaddingLeft();
        final int n3 = n2 - n;
        final int paddingRight = this.getPaddingRight();
        final int paddingRight2 = this.getPaddingRight();
        n = this.getMeasuredHeight();
        final int childCount = this.getChildCount();
        final int gravity2 = this.getGravity();
        n2 = (gravity2 & 0x70);
        if (n2 != 16) {
            if (n2 != 80) {
                n = this.getPaddingTop();
            }
            else {
                n = this.getPaddingTop() + i - gravity - n;
            }
        }
        else {
            n2 = this.getPaddingTop();
            n = (i - gravity - n) / 2 + n2;
        }
        final Drawable dividerDrawable = this.getDividerDrawable();
        if (dividerDrawable == null) {
            n2 = 0;
        }
        else {
            n2 = dividerDrawable.getIntrinsicHeight();
        }
        View child;
        int measuredWidth;
        int measuredHeight;
        LayoutParams layoutParams;
        int n4;
        for (i = 0; i < childCount; ++i, n = gravity) {
            child = this.getChildAt(i);
            gravity = n;
            if (child != null) {
                gravity = n;
                if (child.getVisibility() != 8) {
                    measuredWidth = child.getMeasuredWidth();
                    measuredHeight = child.getMeasuredHeight();
                    layoutParams = (LayoutParams)child.getLayoutParams();
                    if ((gravity = layoutParams.gravity) < 0) {
                        gravity = (gravity2 & 0x800007);
                    }
                    gravity = (GravityCompat.getAbsoluteGravity(gravity, ViewCompat.getLayoutDirection((View)this)) & 0x7);
                    if (gravity != 1) {
                        if (gravity != 5) {
                            gravity = layoutParams.leftMargin + paddingLeft;
                        }
                        else {
                            gravity = n3 - paddingRight - measuredWidth - layoutParams.rightMargin;
                        }
                    }
                    else {
                        gravity = (n3 - paddingLeft - paddingRight2 - measuredWidth) / 2 + paddingLeft + layoutParams.leftMargin - layoutParams.rightMargin;
                    }
                    n4 = n;
                    if (this.hasDividerBeforeChildAt(i)) {
                        n4 = n + n2;
                    }
                    n = n4 + layoutParams.topMargin;
                    this.setChildFrame(child, gravity, n, measuredWidth, measuredHeight);
                    gravity = n + (measuredHeight + layoutParams.bottomMargin);
                }
            }
        }
    }
    
    @Override
    protected void onMeasure(final int n, final int n2) {
        if (!this.tryOnMeasure(n, n2)) {
            super.onMeasure(n, n2);
        }
    }
}
