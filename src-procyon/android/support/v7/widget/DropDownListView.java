// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.widget;

import android.support.v7.graphics.drawable.DrawableWrapper;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.ViewGroup$LayoutParams;
import android.view.View$MeasureSpec;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.os.Build$VERSION;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.graphics.drawable.Drawable;
import android.graphics.Canvas;
import android.view.View;
import android.widget.AbsListView;
import android.util.AttributeSet;
import android.support.v7.appcompat.R;
import android.content.Context;
import android.graphics.Rect;
import android.support.v4.widget.ListViewAutoScrollHelper;
import java.lang.reflect.Field;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.widget.ListView;

class DropDownListView extends ListView
{
    public static final int INVALID_POSITION = -1;
    public static final int NO_POSITION = -1;
    private ViewPropertyAnimatorCompat mClickAnimation;
    private boolean mDrawsInPressedState;
    private boolean mHijackFocus;
    private Field mIsChildViewEnabled;
    private boolean mListSelectionHidden;
    private int mMotionPosition;
    ResolveHoverRunnable mResolveHoverRunnable;
    private ListViewAutoScrollHelper mScrollHelper;
    private int mSelectionBottomPadding;
    private int mSelectionLeftPadding;
    private int mSelectionRightPadding;
    private int mSelectionTopPadding;
    private GateKeeperDrawable mSelector;
    private final Rect mSelectorRect;
    
    DropDownListView(final Context context, final boolean mHijackFocus) {
        super(context, (AttributeSet)null, R.attr.dropDownListViewStyle);
        this.mSelectorRect = new Rect();
        this.mSelectionLeftPadding = 0;
        this.mSelectionTopPadding = 0;
        this.mSelectionRightPadding = 0;
        this.mSelectionBottomPadding = 0;
        this.mHijackFocus = mHijackFocus;
        this.setCacheColorHint(0);
        try {
            (this.mIsChildViewEnabled = AbsListView.class.getDeclaredField("mIsChildViewEnabled")).setAccessible(true);
        }
        catch (final NoSuchFieldException ex) {
            ex.printStackTrace();
        }
    }
    
    private void clearPressedItem() {
        this.setPressed(this.mDrawsInPressedState = false);
        this.drawableStateChanged();
        final View child = this.getChildAt(this.mMotionPosition - this.getFirstVisiblePosition());
        if (child != null) {
            child.setPressed(false);
        }
        if (this.mClickAnimation != null) {
            this.mClickAnimation.cancel();
            this.mClickAnimation = null;
        }
    }
    
    private void clickPressedItem(final View view, final int n) {
        this.performItemClick(view, n, this.getItemIdAtPosition(n));
    }
    
    private void drawSelectorCompat(final Canvas canvas) {
        if (!this.mSelectorRect.isEmpty()) {
            final Drawable selector = this.getSelector();
            if (selector != null) {
                selector.setBounds(this.mSelectorRect);
                selector.draw(canvas);
            }
        }
    }
    
    private void positionSelectorCompat(final int n, final View view) {
        final Rect mSelectorRect = this.mSelectorRect;
        mSelectorRect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        mSelectorRect.left -= this.mSelectionLeftPadding;
        mSelectorRect.top -= this.mSelectionTopPadding;
        mSelectorRect.right += this.mSelectionRightPadding;
        mSelectorRect.bottom += this.mSelectionBottomPadding;
        try {
            final boolean boolean1 = this.mIsChildViewEnabled.getBoolean(this);
            if (view.isEnabled() != boolean1) {
                this.mIsChildViewEnabled.set(this, boolean1 ^ true);
                if (n != -1) {
                    this.refreshDrawableState();
                }
            }
        }
        catch (final IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }
    
    private void positionSelectorLikeFocusCompat(final int n, final View view) {
        final Drawable selector = this.getSelector();
        boolean b = true;
        final boolean b2 = selector != null && n != -1;
        if (b2) {
            selector.setVisible(false, false);
        }
        this.positionSelectorCompat(n, view);
        if (b2) {
            final Rect mSelectorRect = this.mSelectorRect;
            final float exactCenterX = mSelectorRect.exactCenterX();
            final float exactCenterY = mSelectorRect.exactCenterY();
            if (this.getVisibility() != 0) {
                b = false;
            }
            selector.setVisible(b, false);
            DrawableCompat.setHotspot(selector, exactCenterX, exactCenterY);
        }
    }
    
    private void positionSelectorLikeTouchCompat(final int n, final View view, final float n2, final float n3) {
        this.positionSelectorLikeFocusCompat(n, view);
        final Drawable selector = this.getSelector();
        if (selector != null && n != -1) {
            DrawableCompat.setHotspot(selector, n2, n3);
        }
    }
    
    private void setPressedItem(final View view, final int mMotionPosition, final float n, final float n2) {
        this.mDrawsInPressedState = true;
        if (Build$VERSION.SDK_INT >= 21) {
            this.drawableHotspotChanged(n, n2);
        }
        if (!this.isPressed()) {
            this.setPressed(true);
        }
        this.layoutChildren();
        if (this.mMotionPosition != -1) {
            final View child = this.getChildAt(this.mMotionPosition - this.getFirstVisiblePosition());
            if (child != null && child != view && child.isPressed()) {
                child.setPressed(false);
            }
        }
        this.mMotionPosition = mMotionPosition;
        final float n3 = (float)view.getLeft();
        final float n4 = (float)view.getTop();
        if (Build$VERSION.SDK_INT >= 21) {
            view.drawableHotspotChanged(n - n3, n2 - n4);
        }
        if (!view.isPressed()) {
            view.setPressed(true);
        }
        this.positionSelectorLikeTouchCompat(mMotionPosition, view, n, n2);
        this.setSelectorEnabled(false);
        this.refreshDrawableState();
    }
    
    private void setSelectorEnabled(final boolean enabled) {
        if (this.mSelector != null) {
            this.mSelector.setEnabled(enabled);
        }
    }
    
    private boolean touchModeDrawsInPressedStateCompat() {
        return this.mDrawsInPressedState;
    }
    
    private void updateSelectorStateCompat() {
        final Drawable selector = this.getSelector();
        if (selector != null && this.touchModeDrawsInPressedStateCompat() && this.isPressed()) {
            selector.setState(this.getDrawableState());
        }
    }
    
    protected void dispatchDraw(final Canvas canvas) {
        this.drawSelectorCompat(canvas);
        super.dispatchDraw(canvas);
    }
    
    protected void drawableStateChanged() {
        if (this.mResolveHoverRunnable != null) {
            return;
        }
        super.drawableStateChanged();
        this.setSelectorEnabled(true);
        this.updateSelectorStateCompat();
    }
    
    public boolean hasFocus() {
        return this.mHijackFocus || super.hasFocus();
    }
    
    public boolean hasWindowFocus() {
        return this.mHijackFocus || super.hasWindowFocus();
    }
    
    public boolean isFocused() {
        return this.mHijackFocus || super.isFocused();
    }
    
    public boolean isInTouchMode() {
        return (this.mHijackFocus && this.mListSelectionHidden) || super.isInTouchMode();
    }
    
    public int lookForSelectablePosition(int n, final boolean b) {
        final ListAdapter adapter = this.getAdapter();
        if (adapter == null || this.isInTouchMode()) {
            return -1;
        }
        final int count = adapter.getCount();
        if (!this.getAdapter().areAllItemsEnabled()) {
            int n2;
            if (b) {
                n = Math.max(0, n);
                while (true) {
                    n2 = n;
                    if (n >= count) {
                        break;
                    }
                    n2 = n;
                    if (adapter.isEnabled(n)) {
                        break;
                    }
                    ++n;
                }
            }
            else {
                n = Math.min(n, count - 1);
                while (true) {
                    n2 = n;
                    if (n < 0) {
                        break;
                    }
                    n2 = n;
                    if (adapter.isEnabled(n)) {
                        break;
                    }
                    --n;
                }
            }
            if (n2 >= 0 && n2 < count) {
                return n2;
            }
            return -1;
        }
        else {
            if (n >= 0 && n < count) {
                return n;
            }
            return -1;
        }
    }
    
    public int measureHeightOfChildrenCompat(int n, int listPaddingBottom, int listPaddingTop, final int n2, final int n3) {
        listPaddingTop = this.getListPaddingTop();
        listPaddingBottom = this.getListPaddingBottom();
        this.getListPaddingLeft();
        this.getListPaddingRight();
        int dividerHeight = this.getDividerHeight();
        final Drawable divider = this.getDivider();
        final ListAdapter adapter = this.getAdapter();
        if (adapter == null) {
            return listPaddingTop + listPaddingBottom;
        }
        if (dividerHeight <= 0 || divider == null) {
            dividerHeight = 0;
        }
        final int count = adapter.getCount();
        listPaddingTop += listPaddingBottom;
        int i = 0;
        int n4 = listPaddingBottom = 0;
        View view = null;
        while (i < count) {
            final int itemViewType = adapter.getItemViewType(i);
            int n5;
            if (itemViewType != (n5 = n4)) {
                view = null;
                n5 = itemViewType;
            }
            final View view2 = adapter.getView(i, view, (ViewGroup)this);
            ViewGroup$LayoutParams layoutParams;
            if ((layoutParams = view2.getLayoutParams()) == null) {
                layoutParams = this.generateDefaultLayoutParams();
                view2.setLayoutParams(layoutParams);
            }
            int n6;
            if (layoutParams.height > 0) {
                n6 = View$MeasureSpec.makeMeasureSpec(layoutParams.height, 1073741824);
            }
            else {
                n6 = View$MeasureSpec.makeMeasureSpec(0, 0);
            }
            view2.measure(n, n6);
            view2.forceLayout();
            int n7 = listPaddingTop;
            if (i > 0) {
                n7 = listPaddingTop + dividerHeight;
            }
            listPaddingTop = n7 + view2.getMeasuredHeight();
            if (listPaddingTop >= n2) {
                n = n2;
                if (n3 >= 0) {
                    n = n2;
                    if (i > n3) {
                        n = n2;
                        if (listPaddingBottom > 0 && listPaddingTop != (n = n2)) {
                            n = listPaddingBottom;
                        }
                    }
                }
                return n;
            }
            int n8 = listPaddingBottom;
            if (n3 >= 0) {
                n8 = listPaddingBottom;
                if (i >= n3) {
                    n8 = listPaddingTop;
                }
            }
            ++i;
            n4 = n5;
            view = view2;
            listPaddingBottom = n8;
        }
        return listPaddingTop;
    }
    
    protected void onDetachedFromWindow() {
        this.mResolveHoverRunnable = null;
        super.onDetachedFromWindow();
    }
    
    public boolean onForwardedEvent(final MotionEvent motionEvent, int n) {
        final int actionMasked = motionEvent.getActionMasked();
        boolean b = false;
        int pointerIndex;
        int n2;
        int pointToPosition;
        View child;
        Label_0151:Label_0040_Outer:
        while (true) {
            while (true) {
                switch (actionMasked) {
                    default: {
                        n = 0;
                        b = true;
                        break Label_0151;
                    }
                    case 3: {
                        n = 0;
                        b = false;
                        break Label_0151;
                    }
                    case 2: {
                        b = true;
                        break;
                    }
                    case 1: {
                        b = false;
                        break;
                    }
                }
                pointerIndex = motionEvent.findPointerIndex(n);
                if (pointerIndex < 0) {
                    continue;
                }
                break;
            }
            n = (int)motionEvent.getX(pointerIndex);
            n2 = (int)motionEvent.getY(pointerIndex);
            pointToPosition = this.pointToPosition(n, n2);
            if (pointToPosition == -1) {
                n = 1;
            }
            else {
                child = this.getChildAt(pointToPosition - this.getFirstVisiblePosition());
                this.setPressedItem(child, pointToPosition, (float)n, (float)n2);
                if (actionMasked == 1) {
                    this.clickPressedItem(child, pointToPosition);
                }
                continue Label_0040_Outer;
            }
            break;
        }
        if (!b || n != 0) {
            this.clearPressedItem();
        }
        if (b) {
            if (this.mScrollHelper == null) {
                this.mScrollHelper = new ListViewAutoScrollHelper(this);
            }
            this.mScrollHelper.setEnabled(true);
            this.mScrollHelper.onTouch((View)this, motionEvent);
        }
        else if (this.mScrollHelper != null) {
            this.mScrollHelper.setEnabled(false);
        }
        return b;
    }
    
    public boolean onHoverEvent(@NonNull final MotionEvent motionEvent) {
        if (Build$VERSION.SDK_INT < 26) {
            return super.onHoverEvent(motionEvent);
        }
        final int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 10 && this.mResolveHoverRunnable == null) {
            (this.mResolveHoverRunnable = new ResolveHoverRunnable()).post();
        }
        final boolean onHoverEvent = super.onHoverEvent(motionEvent);
        if (actionMasked != 9 && actionMasked != 7) {
            this.setSelection(-1);
        }
        else {
            final int pointToPosition = this.pointToPosition((int)motionEvent.getX(), (int)motionEvent.getY());
            if (pointToPosition != -1 && pointToPosition != this.getSelectedItemPosition()) {
                final View child = this.getChildAt(pointToPosition - this.getFirstVisiblePosition());
                if (child.isEnabled()) {
                    this.setSelectionFromTop(pointToPosition, child.getTop() - this.getTop());
                }
                this.updateSelectorStateCompat();
            }
        }
        return onHoverEvent;
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.mMotionPosition = this.pointToPosition((int)motionEvent.getX(), (int)motionEvent.getY());
        }
        if (this.mResolveHoverRunnable != null) {
            this.mResolveHoverRunnable.cancel();
        }
        return super.onTouchEvent(motionEvent);
    }
    
    void setListSelectionHidden(final boolean mListSelectionHidden) {
        this.mListSelectionHidden = mListSelectionHidden;
    }
    
    public void setSelector(final Drawable drawable) {
        GateKeeperDrawable mSelector;
        if (drawable != null) {
            mSelector = new GateKeeperDrawable(drawable);
        }
        else {
            mSelector = null;
        }
        super.setSelector((Drawable)(this.mSelector = mSelector));
        final Rect rect = new Rect();
        if (drawable != null) {
            drawable.getPadding(rect);
        }
        this.mSelectionLeftPadding = rect.left;
        this.mSelectionTopPadding = rect.top;
        this.mSelectionRightPadding = rect.right;
        this.mSelectionBottomPadding = rect.bottom;
    }
    
    private static class GateKeeperDrawable extends DrawableWrapper
    {
        private boolean mEnabled;
        
        GateKeeperDrawable(final Drawable drawable) {
            super(drawable);
            this.mEnabled = true;
        }
        
        @Override
        public void draw(final Canvas canvas) {
            if (this.mEnabled) {
                super.draw(canvas);
            }
        }
        
        void setEnabled(final boolean mEnabled) {
            this.mEnabled = mEnabled;
        }
        
        @Override
        public void setHotspot(final float n, final float n2) {
            if (this.mEnabled) {
                super.setHotspot(n, n2);
            }
        }
        
        @Override
        public void setHotspotBounds(final int n, final int n2, final int n3, final int n4) {
            if (this.mEnabled) {
                super.setHotspotBounds(n, n2, n3, n4);
            }
        }
        
        @Override
        public boolean setState(final int[] state) {
            return this.mEnabled && super.setState(state);
        }
        
        @Override
        public boolean setVisible(final boolean b, final boolean b2) {
            return this.mEnabled && super.setVisible(b, b2);
        }
    }
    
    private class ResolveHoverRunnable implements Runnable
    {
        final DropDownListView this$0;
        
        ResolveHoverRunnable(final DropDownListView this$0) {
            this.this$0 = this$0;
        }
        
        public void cancel() {
            this.this$0.mResolveHoverRunnable = null;
            this.this$0.removeCallbacks((Runnable)this);
        }
        
        public void post() {
            this.this$0.post((Runnable)this);
        }
        
        @Override
        public void run() {
            this.this$0.mResolveHoverRunnable = null;
            this.this$0.drawableStateChanged();
        }
    }
}
