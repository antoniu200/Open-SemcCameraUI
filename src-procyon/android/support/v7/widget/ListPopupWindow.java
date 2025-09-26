// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.widget;

import android.view.MotionEvent;
import android.widget.AbsListView;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.PopupWindowCompat;
import android.widget.PopupWindow$OnDismissListener;
import android.view.KeyEvent$DispatcherState;
import android.view.KeyEvent;
import android.support.annotation.RestrictTo;
import android.widget.ListView;
import android.view.View$OnTouchListener;
import android.view.ViewParent;
import android.view.ViewGroup;
import android.view.View$MeasureSpec;
import android.view.ViewGroup$LayoutParams;
import android.widget.LinearLayout$LayoutParams;
import android.widget.LinearLayout;
import android.widget.AbsListView$OnScrollListener;
import android.widget.AdapterView;
import android.content.res.TypedArray;
import android.support.annotation.StyleRes;
import android.support.annotation.AttrRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.support.v7.appcompat.R;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.PopupWindow;
import android.database.DataSetObserver;
import android.widget.AdapterView$OnItemSelectedListener;
import android.widget.AdapterView$OnItemClickListener;
import android.os.Handler;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.content.Context;
import android.widget.ListAdapter;
import java.lang.reflect.Method;
import android.support.v7.view.menu.ShowableListMenu;

public class ListPopupWindow implements ShowableListMenu
{
    private static final boolean DEBUG = false;
    static final int EXPAND_LIST_TIMEOUT = 250;
    public static final int INPUT_METHOD_FROM_FOCUSABLE = 0;
    public static final int INPUT_METHOD_NEEDED = 1;
    public static final int INPUT_METHOD_NOT_NEEDED = 2;
    public static final int MATCH_PARENT = -1;
    public static final int POSITION_PROMPT_ABOVE = 0;
    public static final int POSITION_PROMPT_BELOW = 1;
    private static final String TAG = "ListPopupWindow";
    public static final int WRAP_CONTENT = -2;
    private static Method sClipToWindowEnabledMethod;
    private static Method sGetMaxAvailableHeightMethod;
    private static Method sSetEpicenterBoundsMethod;
    private ListAdapter mAdapter;
    private Context mContext;
    private boolean mDropDownAlwaysVisible;
    private View mDropDownAnchorView;
    private int mDropDownGravity;
    private int mDropDownHeight;
    private int mDropDownHorizontalOffset;
    DropDownListView mDropDownList;
    private Drawable mDropDownListHighlight;
    private int mDropDownVerticalOffset;
    private boolean mDropDownVerticalOffsetSet;
    private int mDropDownWidth;
    private int mDropDownWindowLayoutType;
    private Rect mEpicenterBounds;
    private boolean mForceIgnoreOutsideTouch;
    final Handler mHandler;
    private final ListSelectorHider mHideSelector;
    private boolean mIsAnimatedFromAnchor;
    private AdapterView$OnItemClickListener mItemClickListener;
    private AdapterView$OnItemSelectedListener mItemSelectedListener;
    int mListItemExpandMaximum;
    private boolean mModal;
    private DataSetObserver mObserver;
    private boolean mOverlapAnchor;
    private boolean mOverlapAnchorSet;
    PopupWindow mPopup;
    private int mPromptPosition;
    private View mPromptView;
    final ResizePopupRunnable mResizePopupRunnable;
    private final PopupScrollListener mScrollListener;
    private Runnable mShowDropDownRunnable;
    private final Rect mTempRect;
    private final PopupTouchInterceptor mTouchInterceptor;
    
    static {
        try {
            ListPopupWindow.sClipToWindowEnabledMethod = PopupWindow.class.getDeclaredMethod("setClipToScreenEnabled", Boolean.TYPE);
        }
        catch (final NoSuchMethodException ex) {
            Log.i("ListPopupWindow", "Could not find method setClipToScreenEnabled() on PopupWindow. Oh well.");
        }
        try {
            ListPopupWindow.sGetMaxAvailableHeightMethod = PopupWindow.class.getDeclaredMethod("getMaxAvailableHeight", View.class, Integer.TYPE, Boolean.TYPE);
        }
        catch (final NoSuchMethodException ex2) {
            Log.i("ListPopupWindow", "Could not find method getMaxAvailableHeight(View, int, boolean) on PopupWindow. Oh well.");
        }
        try {
            ListPopupWindow.sSetEpicenterBoundsMethod = PopupWindow.class.getDeclaredMethod("setEpicenterBounds", Rect.class);
        }
        catch (final NoSuchMethodException ex3) {
            Log.i("ListPopupWindow", "Could not find method setEpicenterBounds(Rect) on PopupWindow. Oh well.");
        }
    }
    
    public ListPopupWindow(@NonNull final Context context) {
        this(context, null, R.attr.listPopupWindowStyle);
    }
    
    public ListPopupWindow(@NonNull final Context context, @Nullable final AttributeSet set) {
        this(context, set, R.attr.listPopupWindowStyle);
    }
    
    public ListPopupWindow(@NonNull final Context context, @Nullable final AttributeSet set, @AttrRes final int n) {
        this(context, set, n, 0);
    }
    
    public ListPopupWindow(@NonNull final Context mContext, @Nullable final AttributeSet set, @AttrRes final int n, @StyleRes final int n2) {
        this.mDropDownHeight = -2;
        this.mDropDownWidth = -2;
        this.mDropDownWindowLayoutType = 1002;
        this.mIsAnimatedFromAnchor = true;
        this.mDropDownGravity = 0;
        this.mDropDownAlwaysVisible = false;
        this.mForceIgnoreOutsideTouch = false;
        this.mListItemExpandMaximum = Integer.MAX_VALUE;
        this.mPromptPosition = 0;
        this.mResizePopupRunnable = new ResizePopupRunnable();
        this.mTouchInterceptor = new PopupTouchInterceptor();
        this.mScrollListener = new PopupScrollListener();
        this.mHideSelector = new ListSelectorHider();
        this.mTempRect = new Rect();
        this.mContext = mContext;
        this.mHandler = new Handler(mContext.getMainLooper());
        final TypedArray obtainStyledAttributes = mContext.obtainStyledAttributes(set, R.styleable.ListPopupWindow, n, n2);
        this.mDropDownHorizontalOffset = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.ListPopupWindow_android_dropDownHorizontalOffset, 0);
        this.mDropDownVerticalOffset = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.ListPopupWindow_android_dropDownVerticalOffset, 0);
        if (this.mDropDownVerticalOffset != 0) {
            this.mDropDownVerticalOffsetSet = true;
        }
        obtainStyledAttributes.recycle();
        (this.mPopup = new AppCompatPopupWindow(mContext, set, n, n2)).setInputMethodMode(1);
    }
    
    private int buildDropDown() {
        final DropDownListView mDropDownList = this.mDropDownList;
        boolean b = true;
        int n2;
        if (mDropDownList == null) {
            final Context mContext = this.mContext;
            this.mShowDropDownRunnable = new Runnable(this) {
                final ListPopupWindow this$0;
                
                @Override
                public void run() {
                    final View anchorView = this.this$0.getAnchorView();
                    if (anchorView != null && anchorView.getWindowToken() != null) {
                        this.this$0.show();
                    }
                }
            };
            this.mDropDownList = this.createDropDownListView(mContext, this.mModal ^ true);
            if (this.mDropDownListHighlight != null) {
                this.mDropDownList.setSelector(this.mDropDownListHighlight);
            }
            this.mDropDownList.setAdapter(this.mAdapter);
            this.mDropDownList.setOnItemClickListener(this.mItemClickListener);
            this.mDropDownList.setFocusable(true);
            this.mDropDownList.setFocusableInTouchMode(true);
            this.mDropDownList.setOnItemSelectedListener((AdapterView$OnItemSelectedListener)new AdapterView$OnItemSelectedListener(this) {
                final ListPopupWindow this$0;
                
                public void onItemSelected(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                    if (n != -1) {
                        final DropDownListView mDropDownList = this.this$0.mDropDownList;
                        if (mDropDownList != null) {
                            mDropDownList.setListSelectionHidden(false);
                        }
                    }
                }
                
                public void onNothingSelected(final AdapterView<?> adapterView) {
                }
            });
            this.mDropDownList.setOnScrollListener((AbsListView$OnScrollListener)this.mScrollListener);
            if (this.mItemSelectedListener != null) {
                this.mDropDownList.setOnItemSelectedListener(this.mItemSelectedListener);
            }
            final DropDownListView mDropDownList2 = this.mDropDownList;
            final View mPromptView = this.mPromptView;
            Object contentView;
            if (mPromptView != null) {
                contentView = new LinearLayout(mContext);
                ((LinearLayout)contentView).setOrientation(1);
                final LinearLayout$LayoutParams linearLayout$LayoutParams = new LinearLayout$LayoutParams(-1, 0, 1.0f);
                switch (this.mPromptPosition) {
                    default: {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Invalid hint position ");
                        sb.append(this.mPromptPosition);
                        Log.e("ListPopupWindow", sb.toString());
                        break;
                    }
                    case 1: {
                        ((LinearLayout)contentView).addView((View)mDropDownList2, (ViewGroup$LayoutParams)linearLayout$LayoutParams);
                        ((LinearLayout)contentView).addView(mPromptView);
                        break;
                    }
                    case 0: {
                        ((LinearLayout)contentView).addView(mPromptView);
                        ((LinearLayout)contentView).addView((View)mDropDownList2, (ViewGroup$LayoutParams)linearLayout$LayoutParams);
                        break;
                    }
                }
                int mDropDownWidth;
                int n;
                if (this.mDropDownWidth >= 0) {
                    mDropDownWidth = this.mDropDownWidth;
                    n = Integer.MIN_VALUE;
                }
                else {
                    mDropDownWidth = 0;
                    n = 0;
                }
                mPromptView.measure(View$MeasureSpec.makeMeasureSpec(mDropDownWidth, n), 0);
                final LinearLayout$LayoutParams linearLayout$LayoutParams2 = (LinearLayout$LayoutParams)mPromptView.getLayoutParams();
                n2 = mPromptView.getMeasuredHeight() + linearLayout$LayoutParams2.topMargin + linearLayout$LayoutParams2.bottomMargin;
            }
            else {
                n2 = 0;
                contentView = mDropDownList2;
            }
            this.mPopup.setContentView((View)contentView);
        }
        else {
            final ViewGroup viewGroup = (ViewGroup)this.mPopup.getContentView();
            final View mPromptView2 = this.mPromptView;
            if (mPromptView2 != null) {
                final LinearLayout$LayoutParams linearLayout$LayoutParams3 = (LinearLayout$LayoutParams)mPromptView2.getLayoutParams();
                n2 = mPromptView2.getMeasuredHeight() + linearLayout$LayoutParams3.topMargin + linearLayout$LayoutParams3.bottomMargin;
            }
            else {
                n2 = 0;
            }
        }
        final Drawable background = this.mPopup.getBackground();
        int n4;
        if (background != null) {
            background.getPadding(this.mTempRect);
            final int n3 = n4 = this.mTempRect.top + this.mTempRect.bottom;
            if (!this.mDropDownVerticalOffsetSet) {
                this.mDropDownVerticalOffset = -this.mTempRect.top;
                n4 = n3;
            }
        }
        else {
            this.mTempRect.setEmpty();
            n4 = 0;
        }
        if (this.mPopup.getInputMethodMode() != 2) {
            b = false;
        }
        final int maxAvailableHeight = this.getMaxAvailableHeight(this.getAnchorView(), this.mDropDownVerticalOffset, b);
        if (!this.mDropDownAlwaysVisible && this.mDropDownHeight != -1) {
            int n5 = 0;
            switch (this.mDropDownWidth) {
                default: {
                    n5 = View$MeasureSpec.makeMeasureSpec(this.mDropDownWidth, 1073741824);
                    break;
                }
                case -1: {
                    n5 = View$MeasureSpec.makeMeasureSpec(this.mContext.getResources().getDisplayMetrics().widthPixels - (this.mTempRect.left + this.mTempRect.right), 1073741824);
                    break;
                }
                case -2: {
                    n5 = View$MeasureSpec.makeMeasureSpec(this.mContext.getResources().getDisplayMetrics().widthPixels - (this.mTempRect.left + this.mTempRect.right), Integer.MIN_VALUE);
                    break;
                }
            }
            final int measureHeightOfChildrenCompat = this.mDropDownList.measureHeightOfChildrenCompat(n5, 0, -1, maxAvailableHeight - n2, -1);
            int n6 = n2;
            if (measureHeightOfChildrenCompat > 0) {
                n6 = n2 + (n4 + (this.mDropDownList.getPaddingTop() + this.mDropDownList.getPaddingBottom()));
            }
            return measureHeightOfChildrenCompat + n6;
        }
        return maxAvailableHeight + n4;
    }
    
    private int getMaxAvailableHeight(final View view, final int i, final boolean b) {
        if (ListPopupWindow.sGetMaxAvailableHeightMethod != null) {
            try {
                return (int)ListPopupWindow.sGetMaxAvailableHeightMethod.invoke(this.mPopup, view, i, b);
            }
            catch (final Exception ex) {
                Log.i("ListPopupWindow", "Could not call getMaxAvailableHeightMethod(View, int, boolean) on PopupWindow. Using the public version.");
            }
        }
        return this.mPopup.getMaxAvailableHeight(view, i);
    }
    
    private static boolean isConfirmKey(final int n) {
        return n == 66 || n == 23;
    }
    
    private void removePromptView() {
        if (this.mPromptView != null) {
            final ViewParent parent = this.mPromptView.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup)parent).removeView(this.mPromptView);
            }
        }
    }
    
    private void setPopupClipToScreenEnabled(final boolean b) {
        if (ListPopupWindow.sClipToWindowEnabledMethod != null) {
            try {
                ListPopupWindow.sClipToWindowEnabledMethod.invoke(this.mPopup, b);
            }
            catch (final Exception ex) {
                Log.i("ListPopupWindow", "Could not call setClipToScreenEnabled() on PopupWindow. Oh well.");
            }
        }
    }
    
    public void clearListSelection() {
        final DropDownListView mDropDownList = this.mDropDownList;
        if (mDropDownList != null) {
            mDropDownList.setListSelectionHidden(true);
            mDropDownList.requestLayout();
        }
    }
    
    public View$OnTouchListener createDragToOpenListener(final View view) {
        return (View$OnTouchListener)new ForwardingListener(this, view) {
            final ListPopupWindow this$0;
            
            @Override
            public ListPopupWindow getPopup() {
                return this.this$0;
            }
        };
    }
    
    @NonNull
    DropDownListView createDropDownListView(final Context context, final boolean b) {
        return new DropDownListView(context, b);
    }
    
    @Override
    public void dismiss() {
        this.mPopup.dismiss();
        this.removePromptView();
        this.mPopup.setContentView((View)null);
        this.mDropDownList = null;
        this.mHandler.removeCallbacks((Runnable)this.mResizePopupRunnable);
    }
    
    @Nullable
    public View getAnchorView() {
        return this.mDropDownAnchorView;
    }
    
    @StyleRes
    public int getAnimationStyle() {
        return this.mPopup.getAnimationStyle();
    }
    
    @Nullable
    public Drawable getBackground() {
        return this.mPopup.getBackground();
    }
    
    public int getHeight() {
        return this.mDropDownHeight;
    }
    
    public int getHorizontalOffset() {
        return this.mDropDownHorizontalOffset;
    }
    
    public int getInputMethodMode() {
        return this.mPopup.getInputMethodMode();
    }
    
    @Nullable
    @Override
    public ListView getListView() {
        return this.mDropDownList;
    }
    
    public int getPromptPosition() {
        return this.mPromptPosition;
    }
    
    @Nullable
    public Object getSelectedItem() {
        if (!this.isShowing()) {
            return null;
        }
        return this.mDropDownList.getSelectedItem();
    }
    
    public long getSelectedItemId() {
        if (!this.isShowing()) {
            return Long.MIN_VALUE;
        }
        return this.mDropDownList.getSelectedItemId();
    }
    
    public int getSelectedItemPosition() {
        if (!this.isShowing()) {
            return -1;
        }
        return this.mDropDownList.getSelectedItemPosition();
    }
    
    @Nullable
    public View getSelectedView() {
        if (!this.isShowing()) {
            return null;
        }
        return this.mDropDownList.getSelectedView();
    }
    
    public int getSoftInputMode() {
        return this.mPopup.getSoftInputMode();
    }
    
    public int getVerticalOffset() {
        if (!this.mDropDownVerticalOffsetSet) {
            return 0;
        }
        return this.mDropDownVerticalOffset;
    }
    
    public int getWidth() {
        return this.mDropDownWidth;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public boolean isDropDownAlwaysVisible() {
        return this.mDropDownAlwaysVisible;
    }
    
    public boolean isInputMethodNotNeeded() {
        return this.mPopup.getInputMethodMode() == 2;
    }
    
    public boolean isModal() {
        return this.mModal;
    }
    
    @Override
    public boolean isShowing() {
        return this.mPopup.isShowing();
    }
    
    public boolean onKeyDown(final int n, @NonNull final KeyEvent keyEvent) {
        if (this.isShowing() && n != 62 && (this.mDropDownList.getSelectedItemPosition() >= 0 || !isConfirmKey(n))) {
            final int selectedItemPosition = this.mDropDownList.getSelectedItemPosition();
            final boolean b = this.mPopup.isAboveAnchor() ^ true;
            final ListAdapter mAdapter = this.mAdapter;
            int lookForSelectablePosition = Integer.MAX_VALUE;
            int lookForSelectablePosition2;
            if (mAdapter != null) {
                final boolean allItemsEnabled = mAdapter.areAllItemsEnabled();
                if (allItemsEnabled) {
                    lookForSelectablePosition = 0;
                }
                else {
                    lookForSelectablePosition = this.mDropDownList.lookForSelectablePosition(0, true);
                }
                if (allItemsEnabled) {
                    lookForSelectablePosition2 = mAdapter.getCount() - 1;
                }
                else {
                    lookForSelectablePosition2 = this.mDropDownList.lookForSelectablePosition(mAdapter.getCount() - 1, false);
                }
            }
            else {
                lookForSelectablePosition2 = Integer.MIN_VALUE;
            }
            if ((b && n == 19 && selectedItemPosition <= lookForSelectablePosition) || (!b && n == 20 && selectedItemPosition >= lookForSelectablePosition2)) {
                this.clearListSelection();
                this.mPopup.setInputMethodMode(1);
                this.show();
                return true;
            }
            this.mDropDownList.setListSelectionHidden(false);
            if (this.mDropDownList.onKeyDown(n, keyEvent)) {
                this.mPopup.setInputMethodMode(2);
                this.mDropDownList.requestFocusFromTouch();
                this.show();
                if (n != 23 && n != 66) {
                    switch (n) {
                        default: {
                            return false;
                        }
                        case 19:
                        case 20: {
                            break;
                        }
                    }
                }
                return true;
            }
            if (b && n == 20) {
                if (selectedItemPosition == lookForSelectablePosition2) {
                    return true;
                }
            }
            else if (!b && n == 19 && selectedItemPosition == lookForSelectablePosition) {
                return true;
            }
        }
        return false;
    }
    
    public boolean onKeyPreIme(final int n, @NonNull final KeyEvent keyEvent) {
        if (n == 4 && this.isShowing()) {
            final View mDropDownAnchorView = this.mDropDownAnchorView;
            if (keyEvent.getAction() == 0 && keyEvent.getRepeatCount() == 0) {
                final KeyEvent$DispatcherState keyDispatcherState = mDropDownAnchorView.getKeyDispatcherState();
                if (keyDispatcherState != null) {
                    keyDispatcherState.startTracking(keyEvent, (Object)this);
                }
                return true;
            }
            if (keyEvent.getAction() == 1) {
                final KeyEvent$DispatcherState keyDispatcherState2 = mDropDownAnchorView.getKeyDispatcherState();
                if (keyDispatcherState2 != null) {
                    keyDispatcherState2.handleUpEvent(keyEvent);
                }
                if (keyEvent.isTracking() && !keyEvent.isCanceled()) {
                    this.dismiss();
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean onKeyUp(final int n, @NonNull final KeyEvent keyEvent) {
        if (this.isShowing() && this.mDropDownList.getSelectedItemPosition() >= 0) {
            final boolean onKeyUp = this.mDropDownList.onKeyUp(n, keyEvent);
            if (onKeyUp && isConfirmKey(n)) {
                this.dismiss();
            }
            return onKeyUp;
        }
        return false;
    }
    
    public boolean performItemClick(final int n) {
        if (this.isShowing()) {
            if (this.mItemClickListener != null) {
                final DropDownListView mDropDownList = this.mDropDownList;
                this.mItemClickListener.onItemClick((AdapterView)mDropDownList, mDropDownList.getChildAt(n - mDropDownList.getFirstVisiblePosition()), n, mDropDownList.getAdapter().getItemId(n));
            }
            return true;
        }
        return false;
    }
    
    public void postShow() {
        this.mHandler.post(this.mShowDropDownRunnable);
    }
    
    public void setAdapter(@Nullable final ListAdapter mAdapter) {
        if (this.mObserver == null) {
            this.mObserver = new PopupDataSetObserver();
        }
        else if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(this.mObserver);
        }
        if ((this.mAdapter = mAdapter) != null) {
            mAdapter.registerDataSetObserver(this.mObserver);
        }
        if (this.mDropDownList != null) {
            this.mDropDownList.setAdapter(this.mAdapter);
        }
    }
    
    public void setAnchorView(@Nullable final View mDropDownAnchorView) {
        this.mDropDownAnchorView = mDropDownAnchorView;
    }
    
    public void setAnimationStyle(@StyleRes final int animationStyle) {
        this.mPopup.setAnimationStyle(animationStyle);
    }
    
    public void setBackgroundDrawable(@Nullable final Drawable backgroundDrawable) {
        this.mPopup.setBackgroundDrawable(backgroundDrawable);
    }
    
    public void setContentWidth(final int width) {
        final Drawable background = this.mPopup.getBackground();
        if (background != null) {
            background.getPadding(this.mTempRect);
            this.mDropDownWidth = this.mTempRect.left + this.mTempRect.right + width;
        }
        else {
            this.setWidth(width);
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setDropDownAlwaysVisible(final boolean mDropDownAlwaysVisible) {
        this.mDropDownAlwaysVisible = mDropDownAlwaysVisible;
    }
    
    public void setDropDownGravity(final int mDropDownGravity) {
        this.mDropDownGravity = mDropDownGravity;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setEpicenterBounds(final Rect mEpicenterBounds) {
        this.mEpicenterBounds = mEpicenterBounds;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setForceIgnoreOutsideTouch(final boolean mForceIgnoreOutsideTouch) {
        this.mForceIgnoreOutsideTouch = mForceIgnoreOutsideTouch;
    }
    
    public void setHeight(final int mDropDownHeight) {
        if (mDropDownHeight < 0 && -2 != mDropDownHeight && -1 != mDropDownHeight) {
            throw new IllegalArgumentException("Invalid height. Must be a positive value, MATCH_PARENT, or WRAP_CONTENT.");
        }
        this.mDropDownHeight = mDropDownHeight;
    }
    
    public void setHorizontalOffset(final int mDropDownHorizontalOffset) {
        this.mDropDownHorizontalOffset = mDropDownHorizontalOffset;
    }
    
    public void setInputMethodMode(final int inputMethodMode) {
        this.mPopup.setInputMethodMode(inputMethodMode);
    }
    
    void setListItemExpandMax(final int mListItemExpandMaximum) {
        this.mListItemExpandMaximum = mListItemExpandMaximum;
    }
    
    public void setListSelector(final Drawable mDropDownListHighlight) {
        this.mDropDownListHighlight = mDropDownListHighlight;
    }
    
    public void setModal(final boolean b) {
        this.mModal = b;
        this.mPopup.setFocusable(b);
    }
    
    public void setOnDismissListener(@Nullable final PopupWindow$OnDismissListener onDismissListener) {
        this.mPopup.setOnDismissListener(onDismissListener);
    }
    
    public void setOnItemClickListener(@Nullable final AdapterView$OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    public void setOnItemSelectedListener(@Nullable final AdapterView$OnItemSelectedListener mItemSelectedListener) {
        this.mItemSelectedListener = mItemSelectedListener;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setOverlapAnchor(final boolean mOverlapAnchor) {
        this.mOverlapAnchorSet = true;
        this.mOverlapAnchor = mOverlapAnchor;
    }
    
    public void setPromptPosition(final int mPromptPosition) {
        this.mPromptPosition = mPromptPosition;
    }
    
    public void setPromptView(@Nullable final View mPromptView) {
        final boolean showing = this.isShowing();
        if (showing) {
            this.removePromptView();
        }
        this.mPromptView = mPromptView;
        if (showing) {
            this.show();
        }
    }
    
    public void setSelection(final int selection) {
        final DropDownListView mDropDownList = this.mDropDownList;
        if (this.isShowing() && mDropDownList != null) {
            mDropDownList.setListSelectionHidden(false);
            mDropDownList.setSelection(selection);
            if (mDropDownList.getChoiceMode() != 0) {
                mDropDownList.setItemChecked(selection, true);
            }
        }
    }
    
    public void setSoftInputMode(final int softInputMode) {
        this.mPopup.setSoftInputMode(softInputMode);
    }
    
    public void setVerticalOffset(final int mDropDownVerticalOffset) {
        this.mDropDownVerticalOffset = mDropDownVerticalOffset;
        this.mDropDownVerticalOffsetSet = true;
    }
    
    public void setWidth(final int mDropDownWidth) {
        this.mDropDownWidth = mDropDownWidth;
    }
    
    public void setWindowLayoutType(final int mDropDownWindowLayoutType) {
        this.mDropDownWindowLayoutType = mDropDownWindowLayoutType;
    }
    
    @Override
    public void show() {
        int height = this.buildDropDown();
        final boolean inputMethodNotNeeded = this.isInputMethodNotNeeded();
        PopupWindowCompat.setWindowLayoutType(this.mPopup, this.mDropDownWindowLayoutType);
        final boolean showing = this.mPopup.isShowing();
        boolean outsideTouchable = true;
        if (showing) {
            if (!ViewCompat.isAttachedToWindow(this.getAnchorView())) {
                return;
            }
            int n;
            if (this.mDropDownWidth == -1) {
                n = -1;
            }
            else if (this.mDropDownWidth == -2) {
                n = this.getAnchorView().getWidth();
            }
            else {
                n = this.mDropDownWidth;
            }
            if (this.mDropDownHeight == -1) {
                if (!inputMethodNotNeeded) {
                    height = -1;
                }
                if (inputMethodNotNeeded) {
                    final PopupWindow mPopup = this.mPopup;
                    int width;
                    if (this.mDropDownWidth == -1) {
                        width = -1;
                    }
                    else {
                        width = 0;
                    }
                    mPopup.setWidth(width);
                    this.mPopup.setHeight(0);
                }
                else {
                    final PopupWindow mPopup2 = this.mPopup;
                    int width2;
                    if (this.mDropDownWidth == -1) {
                        width2 = -1;
                    }
                    else {
                        width2 = 0;
                    }
                    mPopup2.setWidth(width2);
                    this.mPopup.setHeight(-1);
                }
            }
            else if (this.mDropDownHeight != -2) {
                height = this.mDropDownHeight;
            }
            final PopupWindow mPopup3 = this.mPopup;
            if (this.mForceIgnoreOutsideTouch || this.mDropDownAlwaysVisible) {
                outsideTouchable = false;
            }
            mPopup3.setOutsideTouchable(outsideTouchable);
            final PopupWindow mPopup4 = this.mPopup;
            final View anchorView = this.getAnchorView();
            final int mDropDownHorizontalOffset = this.mDropDownHorizontalOffset;
            final int mDropDownVerticalOffset = this.mDropDownVerticalOffset;
            if (n < 0) {
                n = -1;
            }
            if (height < 0) {
                height = -1;
            }
            mPopup4.update(anchorView, mDropDownHorizontalOffset, mDropDownVerticalOffset, n, height);
        }
        else {
            int width3;
            if (this.mDropDownWidth == -1) {
                width3 = -1;
            }
            else if (this.mDropDownWidth == -2) {
                width3 = this.getAnchorView().getWidth();
            }
            else {
                width3 = this.mDropDownWidth;
            }
            if (this.mDropDownHeight == -1) {
                height = -1;
            }
            else if (this.mDropDownHeight != -2) {
                height = this.mDropDownHeight;
            }
            this.mPopup.setWidth(width3);
            this.mPopup.setHeight(height);
            this.setPopupClipToScreenEnabled(true);
            this.mPopup.setOutsideTouchable(!this.mForceIgnoreOutsideTouch && !this.mDropDownAlwaysVisible);
            this.mPopup.setTouchInterceptor((View$OnTouchListener)this.mTouchInterceptor);
            if (this.mOverlapAnchorSet) {
                PopupWindowCompat.setOverlapAnchor(this.mPopup, this.mOverlapAnchor);
            }
            if (ListPopupWindow.sSetEpicenterBoundsMethod != null) {
                try {
                    ListPopupWindow.sSetEpicenterBoundsMethod.invoke(this.mPopup, this.mEpicenterBounds);
                }
                catch (final Exception ex) {
                    Log.e("ListPopupWindow", "Could not invoke setEpicenterBounds on PopupWindow", (Throwable)ex);
                }
            }
            PopupWindowCompat.showAsDropDown(this.mPopup, this.getAnchorView(), this.mDropDownHorizontalOffset, this.mDropDownVerticalOffset, this.mDropDownGravity);
            this.mDropDownList.setSelection(-1);
            if (!this.mModal || this.mDropDownList.isInTouchMode()) {
                this.clearListSelection();
            }
            if (!this.mModal) {
                this.mHandler.post((Runnable)this.mHideSelector);
            }
        }
    }
    
    private class ListSelectorHider implements Runnable
    {
        final ListPopupWindow this$0;
        
        ListSelectorHider(final ListPopupWindow this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            this.this$0.clearListSelection();
        }
    }
    
    private class PopupDataSetObserver extends DataSetObserver
    {
        final ListPopupWindow this$0;
        
        PopupDataSetObserver(final ListPopupWindow this$0) {
            this.this$0 = this$0;
        }
        
        public void onChanged() {
            if (this.this$0.isShowing()) {
                this.this$0.show();
            }
        }
        
        public void onInvalidated() {
            this.this$0.dismiss();
        }
    }
    
    private class PopupScrollListener implements AbsListView$OnScrollListener
    {
        final ListPopupWindow this$0;
        
        PopupScrollListener(final ListPopupWindow this$0) {
            this.this$0 = this$0;
        }
        
        public void onScroll(final AbsListView absListView, final int n, final int n2, final int n3) {
        }
        
        public void onScrollStateChanged(final AbsListView absListView, final int n) {
            if (n == 1 && !this.this$0.isInputMethodNotNeeded() && this.this$0.mPopup.getContentView() != null) {
                this.this$0.mHandler.removeCallbacks((Runnable)this.this$0.mResizePopupRunnable);
                this.this$0.mResizePopupRunnable.run();
            }
        }
    }
    
    private class PopupTouchInterceptor implements View$OnTouchListener
    {
        final ListPopupWindow this$0;
        
        PopupTouchInterceptor(final ListPopupWindow this$0) {
            this.this$0 = this$0;
        }
        
        public boolean onTouch(final View view, final MotionEvent motionEvent) {
            final int action = motionEvent.getAction();
            final int n = (int)motionEvent.getX();
            final int n2 = (int)motionEvent.getY();
            if (action == 0 && this.this$0.mPopup != null && this.this$0.mPopup.isShowing() && n >= 0 && n < this.this$0.mPopup.getWidth() && n2 >= 0 && n2 < this.this$0.mPopup.getHeight()) {
                this.this$0.mHandler.postDelayed((Runnable)this.this$0.mResizePopupRunnable, 250L);
            }
            else if (action == 1) {
                this.this$0.mHandler.removeCallbacks((Runnable)this.this$0.mResizePopupRunnable);
            }
            return false;
        }
    }
    
    private class ResizePopupRunnable implements Runnable
    {
        final ListPopupWindow this$0;
        
        ResizePopupRunnable(final ListPopupWindow this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void run() {
            if (this.this$0.mDropDownList != null && ViewCompat.isAttachedToWindow((View)this.this$0.mDropDownList) && this.this$0.mDropDownList.getCount() > this.this$0.mDropDownList.getChildCount() && this.this$0.mDropDownList.getChildCount() <= this.this$0.mListItemExpandMaximum) {
                this.this$0.mPopup.setInputMethodMode(2);
                this.this$0.show();
            }
        }
    }
}
