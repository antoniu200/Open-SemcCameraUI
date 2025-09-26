// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.widget;

import android.os.Parcel;
import android.os.Parcelable$ClassLoaderCreator;
import android.os.Parcelable$Creator;
import android.support.v4.view.AbsSavedState;
import android.support.annotation.NonNull;
import android.support.v7.view.menu.SubMenuBuilder;
import android.support.v7.view.menu.MenuView;
import android.view.ViewParent;
import android.support.v7.view.CollapsibleActionView;
import android.support.annotation.ColorInt;
import android.text.TextUtils$TruncateAt;
import android.view.ContextThemeWrapper;
import android.support.annotation.StyleRes;
import android.support.annotation.StringRes;
import android.support.v7.content.res.AppCompatResources;
import android.support.annotation.DrawableRes;
import android.os.Build$VERSION;
import android.os.Parcelable;
import android.view.MotionEvent;
import android.text.Layout;
import android.support.annotation.MenuRes;
import android.view.Menu;
import android.support.v7.app.ActionBar;
import android.view.View$OnClickListener;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.annotation.RestrictTo;
import android.view.View$MeasureSpec;
import android.support.v7.view.SupportMenuInflater;
import android.view.MenuInflater;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.view.ViewGroup$MarginLayoutParams;
import android.view.ViewGroup$LayoutParams;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import java.util.List;
import android.text.TextUtils;
import android.view.MenuItem;
import android.support.v7.appcompat.R;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;
import android.content.Context;
import android.support.v7.view.menu.MenuBuilder;
import android.widget.ImageView;
import java.util.ArrayList;
import android.view.View;
import android.graphics.drawable.Drawable;
import android.widget.ImageButton;
import android.support.v7.view.menu.MenuPresenter;
import android.view.ViewGroup;

public class Toolbar extends ViewGroup
{
    private static final String TAG = "Toolbar";
    private MenuPresenter.Callback mActionMenuPresenterCallback;
    int mButtonGravity;
    ImageButton mCollapseButtonView;
    private CharSequence mCollapseDescription;
    private Drawable mCollapseIcon;
    private boolean mCollapsible;
    private int mContentInsetEndWithActions;
    private int mContentInsetStartWithNavigation;
    private RtlSpacingHelper mContentInsets;
    private boolean mEatingHover;
    private boolean mEatingTouch;
    View mExpandedActionView;
    private ExpandedActionViewMenuPresenter mExpandedMenuPresenter;
    private int mGravity;
    private final ArrayList<View> mHiddenViews;
    private ImageView mLogoView;
    private int mMaxButtonHeight;
    private MenuBuilder.Callback mMenuBuilderCallback;
    private ActionMenuView mMenuView;
    private final ActionMenuView.OnMenuItemClickListener mMenuViewItemClickListener;
    private ImageButton mNavButtonView;
    OnMenuItemClickListener mOnMenuItemClickListener;
    private ActionMenuPresenter mOuterActionMenuPresenter;
    private Context mPopupContext;
    private int mPopupTheme;
    private final Runnable mShowOverflowMenuRunnable;
    private CharSequence mSubtitleText;
    private int mSubtitleTextAppearance;
    private int mSubtitleTextColor;
    private TextView mSubtitleTextView;
    private final int[] mTempMargins;
    private final ArrayList<View> mTempViews;
    private int mTitleMarginBottom;
    private int mTitleMarginEnd;
    private int mTitleMarginStart;
    private int mTitleMarginTop;
    private CharSequence mTitleText;
    private int mTitleTextAppearance;
    private int mTitleTextColor;
    private TextView mTitleTextView;
    private ToolbarWidgetWrapper mWrapper;
    
    public Toolbar(final Context context) {
        this(context, null);
    }
    
    public Toolbar(final Context context, @Nullable final AttributeSet set) {
        this(context, set, R.attr.toolbarStyle);
    }
    
    public Toolbar(final Context context, @Nullable final AttributeSet set, int n) {
        super(context, set, n);
        this.mGravity = 8388627;
        this.mTempViews = new ArrayList<View>();
        this.mHiddenViews = new ArrayList<View>();
        this.mTempMargins = new int[2];
        this.mMenuViewItemClickListener = new ActionMenuView.OnMenuItemClickListener() {
            final Toolbar this$0;
            
            @Override
            public boolean onMenuItemClick(final MenuItem menuItem) {
                return this.this$0.mOnMenuItemClickListener != null && this.this$0.mOnMenuItemClickListener.onMenuItemClick(menuItem);
            }
        };
        this.mShowOverflowMenuRunnable = new Runnable() {
            final Toolbar this$0;
            
            @Override
            public void run() {
                this.this$0.showOverflowMenu();
            }
        };
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(this.getContext(), set, R.styleable.Toolbar, n, 0);
        this.mTitleTextAppearance = obtainStyledAttributes.getResourceId(R.styleable.Toolbar_titleTextAppearance, 0);
        this.mSubtitleTextAppearance = obtainStyledAttributes.getResourceId(R.styleable.Toolbar_subtitleTextAppearance, 0);
        this.mGravity = obtainStyledAttributes.getInteger(R.styleable.Toolbar_android_gravity, this.mGravity);
        this.mButtonGravity = obtainStyledAttributes.getInteger(R.styleable.Toolbar_buttonGravity, 48);
        final int n2 = n = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.Toolbar_titleMargin, 0);
        if (obtainStyledAttributes.hasValue(R.styleable.Toolbar_titleMargins)) {
            n = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.Toolbar_titleMargins, n2);
        }
        this.mTitleMarginBottom = n;
        this.mTitleMarginTop = n;
        this.mTitleMarginEnd = n;
        this.mTitleMarginStart = n;
        n = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginStart, -1);
        if (n >= 0) {
            this.mTitleMarginStart = n;
        }
        n = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginEnd, -1);
        if (n >= 0) {
            this.mTitleMarginEnd = n;
        }
        n = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginTop, -1);
        if (n >= 0) {
            this.mTitleMarginTop = n;
        }
        n = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginBottom, -1);
        if (n >= 0) {
            this.mTitleMarginBottom = n;
        }
        this.mMaxButtonHeight = obtainStyledAttributes.getDimensionPixelSize(R.styleable.Toolbar_maxButtonHeight, -1);
        final int dimensionPixelOffset = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetStart, Integer.MIN_VALUE);
        n = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetEnd, Integer.MIN_VALUE);
        final int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.Toolbar_contentInsetLeft, 0);
        final int dimensionPixelSize2 = obtainStyledAttributes.getDimensionPixelSize(R.styleable.Toolbar_contentInsetRight, 0);
        this.ensureContentInsets();
        this.mContentInsets.setAbsolute(dimensionPixelSize, dimensionPixelSize2);
        if (dimensionPixelOffset != Integer.MIN_VALUE || n != Integer.MIN_VALUE) {
            this.mContentInsets.setRelative(dimensionPixelOffset, n);
        }
        this.mContentInsetStartWithNavigation = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetStartWithNavigation, Integer.MIN_VALUE);
        this.mContentInsetEndWithActions = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetEndWithActions, Integer.MIN_VALUE);
        this.mCollapseIcon = obtainStyledAttributes.getDrawable(R.styleable.Toolbar_collapseIcon);
        this.mCollapseDescription = obtainStyledAttributes.getText(R.styleable.Toolbar_collapseContentDescription);
        final CharSequence text = obtainStyledAttributes.getText(R.styleable.Toolbar_title);
        if (!TextUtils.isEmpty(text)) {
            this.setTitle(text);
        }
        final CharSequence text2 = obtainStyledAttributes.getText(R.styleable.Toolbar_subtitle);
        if (!TextUtils.isEmpty(text2)) {
            this.setSubtitle(text2);
        }
        this.mPopupContext = this.getContext();
        this.setPopupTheme(obtainStyledAttributes.getResourceId(R.styleable.Toolbar_popupTheme, 0));
        final Drawable drawable = obtainStyledAttributes.getDrawable(R.styleable.Toolbar_navigationIcon);
        if (drawable != null) {
            this.setNavigationIcon(drawable);
        }
        final CharSequence text3 = obtainStyledAttributes.getText(R.styleable.Toolbar_navigationContentDescription);
        if (!TextUtils.isEmpty(text3)) {
            this.setNavigationContentDescription(text3);
        }
        final Drawable drawable2 = obtainStyledAttributes.getDrawable(R.styleable.Toolbar_logo);
        if (drawable2 != null) {
            this.setLogo(drawable2);
        }
        final CharSequence text4 = obtainStyledAttributes.getText(R.styleable.Toolbar_logoDescription);
        if (!TextUtils.isEmpty(text4)) {
            this.setLogoDescription(text4);
        }
        if (obtainStyledAttributes.hasValue(R.styleable.Toolbar_titleTextColor)) {
            this.setTitleTextColor(obtainStyledAttributes.getColor(R.styleable.Toolbar_titleTextColor, -1));
        }
        if (obtainStyledAttributes.hasValue(R.styleable.Toolbar_subtitleTextColor)) {
            this.setSubtitleTextColor(obtainStyledAttributes.getColor(R.styleable.Toolbar_subtitleTextColor, -1));
        }
        obtainStyledAttributes.recycle();
    }
    
    private void addCustomViewsWithGravity(final List<View> list, int i) {
        final int layoutDirection = ViewCompat.getLayoutDirection((View)this);
        final int n = 0;
        final boolean b = layoutDirection == 1;
        final int childCount = this.getChildCount();
        final int absoluteGravity = GravityCompat.getAbsoluteGravity(i, ViewCompat.getLayoutDirection((View)this));
        list.clear();
        i = n;
        if (b) {
            View child;
            LayoutParams layoutParams;
            for (i = childCount - 1; i >= 0; --i) {
                child = this.getChildAt(i);
                layoutParams = (LayoutParams)child.getLayoutParams();
                if (layoutParams.mViewType == 0 && this.shouldLayout(child) && this.getChildHorizontalGravity(layoutParams.gravity) == absoluteGravity) {
                    list.add(child);
                }
            }
        }
        else {
            while (i < childCount) {
                final View child2 = this.getChildAt(i);
                final LayoutParams layoutParams2 = (LayoutParams)child2.getLayoutParams();
                if (layoutParams2.mViewType == 0 && this.shouldLayout(child2) && this.getChildHorizontalGravity(layoutParams2.gravity) == absoluteGravity) {
                    list.add(child2);
                }
                ++i;
            }
        }
    }
    
    private void addSystemView(final View e, final boolean b) {
        final ViewGroup$LayoutParams layoutParams = e.getLayoutParams();
        LayoutParams layoutParams2;
        if (layoutParams == null) {
            layoutParams2 = this.generateDefaultLayoutParams();
        }
        else if (!this.checkLayoutParams(layoutParams)) {
            layoutParams2 = this.generateLayoutParams(layoutParams);
        }
        else {
            layoutParams2 = (LayoutParams)layoutParams;
        }
        layoutParams2.mViewType = 1;
        if (b && this.mExpandedActionView != null) {
            e.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
            this.mHiddenViews.add(e);
        }
        else {
            this.addView(e, (ViewGroup$LayoutParams)layoutParams2);
        }
    }
    
    private void ensureContentInsets() {
        if (this.mContentInsets == null) {
            this.mContentInsets = new RtlSpacingHelper();
        }
    }
    
    private void ensureLogoView() {
        if (this.mLogoView == null) {
            this.mLogoView = new AppCompatImageView(this.getContext());
        }
    }
    
    private void ensureMenu() {
        this.ensureMenuView();
        if (this.mMenuView.peekMenu() == null) {
            final MenuBuilder menuBuilder = (MenuBuilder)this.mMenuView.getMenu();
            if (this.mExpandedMenuPresenter == null) {
                this.mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter();
            }
            this.mMenuView.setExpandedActionViewsExclusive(true);
            menuBuilder.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
        }
    }
    
    private void ensureMenuView() {
        if (this.mMenuView == null) {
            (this.mMenuView = new ActionMenuView(this.getContext())).setPopupTheme(this.mPopupTheme);
            this.mMenuView.setOnMenuItemClickListener(this.mMenuViewItemClickListener);
            this.mMenuView.setMenuCallbacks(this.mActionMenuPresenterCallback, this.mMenuBuilderCallback);
            final LayoutParams generateDefaultLayoutParams = this.generateDefaultLayoutParams();
            generateDefaultLayoutParams.gravity = (0x800005 | (this.mButtonGravity & 0x70));
            this.mMenuView.setLayoutParams((ViewGroup$LayoutParams)generateDefaultLayoutParams);
            this.addSystemView((View)this.mMenuView, false);
        }
    }
    
    private void ensureNavButtonView() {
        if (this.mNavButtonView == null) {
            this.mNavButtonView = new AppCompatImageButton(this.getContext(), null, R.attr.toolbarNavigationButtonStyle);
            final LayoutParams generateDefaultLayoutParams = this.generateDefaultLayoutParams();
            generateDefaultLayoutParams.gravity = (0x800003 | (this.mButtonGravity & 0x70));
            this.mNavButtonView.setLayoutParams((ViewGroup$LayoutParams)generateDefaultLayoutParams);
        }
    }
    
    private int getChildHorizontalGravity(int n) {
        final int layoutDirection = ViewCompat.getLayoutDirection((View)this);
        final int n2 = GravityCompat.getAbsoluteGravity(n, layoutDirection) & 0x7;
        if (n2 != 1) {
            n = 3;
            if (n2 != 3 && n2 != 5) {
                if (layoutDirection == 1) {
                    n = 5;
                }
                return n;
            }
        }
        return n2;
    }
    
    private int getChildTop(final View view, int n) {
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        final int measuredHeight = view.getMeasuredHeight();
        if (n > 0) {
            n = (measuredHeight - n) / 2;
        }
        else {
            n = 0;
        }
        final int childVerticalGravity = this.getChildVerticalGravity(layoutParams.gravity);
        if (childVerticalGravity == 48) {
            return this.getPaddingTop() - n;
        }
        if (childVerticalGravity != 80) {
            final int paddingTop = this.getPaddingTop();
            final int paddingBottom = this.getPaddingBottom();
            n = this.getHeight();
            final int n2 = (n - paddingTop - paddingBottom - measuredHeight) / 2;
            if (n2 < layoutParams.topMargin) {
                n = layoutParams.topMargin;
            }
            else {
                final int n3 = n - paddingBottom - measuredHeight - n2 - paddingTop;
                n = n2;
                if (n3 < layoutParams.bottomMargin) {
                    n = Math.max(0, n2 - (layoutParams.bottomMargin - n3));
                }
            }
            return paddingTop + n;
        }
        return this.getHeight() - this.getPaddingBottom() - measuredHeight - layoutParams.bottomMargin - n;
    }
    
    private int getChildVerticalGravity(int n) {
        n &= 0x70;
        if (n != 16 && n != 48 && n != 80) {
            return this.mGravity & 0x70;
        }
        return n;
    }
    
    private int getHorizontalMargins(final View view) {
        final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams = (ViewGroup$MarginLayoutParams)view.getLayoutParams();
        return MarginLayoutParamsCompat.getMarginStart(viewGroup$MarginLayoutParams) + MarginLayoutParamsCompat.getMarginEnd(viewGroup$MarginLayoutParams);
    }
    
    private MenuInflater getMenuInflater() {
        return new SupportMenuInflater(this.getContext());
    }
    
    private int getVerticalMargins(final View view) {
        final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams = (ViewGroup$MarginLayoutParams)view.getLayoutParams();
        return viewGroup$MarginLayoutParams.topMargin + viewGroup$MarginLayoutParams.bottomMargin;
    }
    
    private int getViewListMeasuredWidth(final List<View> list, final int[] array) {
        int max = array[0];
        int max2 = array[1];
        final int size = list.size();
        int n = 0;
        for (int i = 0; i < size; ++i) {
            final View view = list.get(i);
            final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            final int b = layoutParams.leftMargin - max;
            final int b2 = layoutParams.rightMargin - max2;
            final int max3 = Math.max(0, b);
            final int max4 = Math.max(0, b2);
            max = Math.max(0, -b);
            max2 = Math.max(0, -b2);
            n += max3 + view.getMeasuredWidth() + max4;
        }
        return n;
    }
    
    private boolean isChildOrHidden(final View o) {
        return o.getParent() == this || this.mHiddenViews.contains(o);
    }
    
    private static boolean isCustomView(final View view) {
        return ((LayoutParams)view.getLayoutParams()).mViewType == 0;
    }
    
    private int layoutChildLeft(final View view, int n, final int[] array, int childTop) {
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        final int b = layoutParams.leftMargin - array[0];
        n += Math.max(0, b);
        array[0] = Math.max(0, -b);
        childTop = this.getChildTop(view, childTop);
        final int measuredWidth = view.getMeasuredWidth();
        view.layout(n, childTop, n + measuredWidth, view.getMeasuredHeight() + childTop);
        return n + (measuredWidth + layoutParams.rightMargin);
    }
    
    private int layoutChildRight(final View view, int n, final int[] array, int measuredWidth) {
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        final int b = layoutParams.rightMargin - array[1];
        n -= Math.max(0, b);
        array[1] = Math.max(0, -b);
        final int childTop = this.getChildTop(view, measuredWidth);
        measuredWidth = view.getMeasuredWidth();
        view.layout(n - measuredWidth, childTop, n, view.getMeasuredHeight() + childTop);
        return n - (measuredWidth + layoutParams.leftMargin);
    }
    
    private int measureChildCollapseMargins(final View view, final int n, final int n2, final int n3, final int n4, final int[] array) {
        final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams = (ViewGroup$MarginLayoutParams)view.getLayoutParams();
        final int b = viewGroup$MarginLayoutParams.leftMargin - array[0];
        final int b2 = viewGroup$MarginLayoutParams.rightMargin - array[1];
        final int n5 = Math.max(0, b) + Math.max(0, b2);
        array[0] = Math.max(0, -b);
        array[1] = Math.max(0, -b2);
        view.measure(getChildMeasureSpec(n, this.getPaddingLeft() + this.getPaddingRight() + n5 + n2, viewGroup$MarginLayoutParams.width), getChildMeasureSpec(n3, this.getPaddingTop() + this.getPaddingBottom() + viewGroup$MarginLayoutParams.topMargin + viewGroup$MarginLayoutParams.bottomMargin + n4, viewGroup$MarginLayoutParams.height));
        return view.getMeasuredWidth() + n5;
    }
    
    private void measureChildConstrained(final View view, int n, int childMeasureSpec, int mode, final int n2, final int b) {
        final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams = (ViewGroup$MarginLayoutParams)view.getLayoutParams();
        final int childMeasureSpec2 = getChildMeasureSpec(n, this.getPaddingLeft() + this.getPaddingRight() + viewGroup$MarginLayoutParams.leftMargin + viewGroup$MarginLayoutParams.rightMargin + childMeasureSpec, viewGroup$MarginLayoutParams.width);
        childMeasureSpec = getChildMeasureSpec(mode, this.getPaddingTop() + this.getPaddingBottom() + viewGroup$MarginLayoutParams.topMargin + viewGroup$MarginLayoutParams.bottomMargin + n2, viewGroup$MarginLayoutParams.height);
        mode = View$MeasureSpec.getMode(childMeasureSpec);
        n = childMeasureSpec;
        if (mode != 1073741824) {
            n = childMeasureSpec;
            if (b >= 0) {
                n = b;
                if (mode != 0) {
                    n = Math.min(View$MeasureSpec.getSize(childMeasureSpec), b);
                }
                n = View$MeasureSpec.makeMeasureSpec(n, 1073741824);
            }
        }
        view.measure(childMeasureSpec2, n);
    }
    
    private void postShowOverflowMenu() {
        this.removeCallbacks(this.mShowOverflowMenuRunnable);
        this.post(this.mShowOverflowMenuRunnable);
    }
    
    private boolean shouldCollapse() {
        if (!this.mCollapsible) {
            return false;
        }
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            if (this.shouldLayout(child) && child.getMeasuredWidth() > 0 && child.getMeasuredHeight() > 0) {
                return false;
            }
        }
        return true;
    }
    
    private boolean shouldLayout(final View view) {
        return view != null && view.getParent() == this && view.getVisibility() != 8;
    }
    
    void addChildrenForExpandedActionView() {
        for (int i = this.mHiddenViews.size() - 1; i >= 0; --i) {
            this.addView((View)this.mHiddenViews.get(i));
        }
        this.mHiddenViews.clear();
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public boolean canShowOverflowMenu() {
        return this.getVisibility() == 0 && this.mMenuView != null && this.mMenuView.isOverflowReserved();
    }
    
    protected boolean checkLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return super.checkLayoutParams(viewGroup$LayoutParams) && viewGroup$LayoutParams instanceof LayoutParams;
    }
    
    public void collapseActionView() {
        MenuItemImpl mCurrentExpandedItem;
        if (this.mExpandedMenuPresenter == null) {
            mCurrentExpandedItem = null;
        }
        else {
            mCurrentExpandedItem = this.mExpandedMenuPresenter.mCurrentExpandedItem;
        }
        if (mCurrentExpandedItem != null) {
            mCurrentExpandedItem.collapseActionView();
        }
    }
    
    public void dismissPopupMenus() {
        if (this.mMenuView != null) {
            this.mMenuView.dismissPopupMenus();
        }
    }
    
    void ensureCollapseButtonView() {
        if (this.mCollapseButtonView == null) {
            (this.mCollapseButtonView = new AppCompatImageButton(this.getContext(), null, R.attr.toolbarNavigationButtonStyle)).setImageDrawable(this.mCollapseIcon);
            this.mCollapseButtonView.setContentDescription(this.mCollapseDescription);
            final LayoutParams generateDefaultLayoutParams = this.generateDefaultLayoutParams();
            generateDefaultLayoutParams.gravity = (0x800003 | (this.mButtonGravity & 0x70));
            generateDefaultLayoutParams.mViewType = 2;
            this.mCollapseButtonView.setLayoutParams((ViewGroup$LayoutParams)generateDefaultLayoutParams);
            this.mCollapseButtonView.setOnClickListener((View$OnClickListener)new View$OnClickListener(this) {
                final Toolbar this$0;
                
                public void onClick(final View view) {
                    this.this$0.collapseActionView();
                }
            });
        }
    }
    
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }
    
    public LayoutParams generateLayoutParams(final AttributeSet set) {
        return new LayoutParams(this.getContext(), set);
    }
    
    protected LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        if (viewGroup$LayoutParams instanceof LayoutParams) {
            return new LayoutParams((LayoutParams)viewGroup$LayoutParams);
        }
        if (viewGroup$LayoutParams instanceof ActionBar.LayoutParams) {
            return new LayoutParams((ActionBar.LayoutParams)viewGroup$LayoutParams);
        }
        if (viewGroup$LayoutParams instanceof ViewGroup$MarginLayoutParams) {
            return new LayoutParams((ViewGroup$MarginLayoutParams)viewGroup$LayoutParams);
        }
        return new LayoutParams(viewGroup$LayoutParams);
    }
    
    public int getContentInsetEnd() {
        int end;
        if (this.mContentInsets != null) {
            end = this.mContentInsets.getEnd();
        }
        else {
            end = 0;
        }
        return end;
    }
    
    public int getContentInsetEndWithActions() {
        int n;
        if (this.mContentInsetEndWithActions != Integer.MIN_VALUE) {
            n = this.mContentInsetEndWithActions;
        }
        else {
            n = this.getContentInsetEnd();
        }
        return n;
    }
    
    public int getContentInsetLeft() {
        int left;
        if (this.mContentInsets != null) {
            left = this.mContentInsets.getLeft();
        }
        else {
            left = 0;
        }
        return left;
    }
    
    public int getContentInsetRight() {
        int right;
        if (this.mContentInsets != null) {
            right = this.mContentInsets.getRight();
        }
        else {
            right = 0;
        }
        return right;
    }
    
    public int getContentInsetStart() {
        int start;
        if (this.mContentInsets != null) {
            start = this.mContentInsets.getStart();
        }
        else {
            start = 0;
        }
        return start;
    }
    
    public int getContentInsetStartWithNavigation() {
        int n;
        if (this.mContentInsetStartWithNavigation != Integer.MIN_VALUE) {
            n = this.mContentInsetStartWithNavigation;
        }
        else {
            n = this.getContentInsetStart();
        }
        return n;
    }
    
    public int getCurrentContentInsetEnd() {
        boolean b = false;
        Label_0033: {
            if (this.mMenuView != null) {
                final MenuBuilder peekMenu = this.mMenuView.peekMenu();
                if (peekMenu != null && peekMenu.hasVisibleItems()) {
                    b = true;
                    break Label_0033;
                }
            }
            b = false;
        }
        int n;
        if (b) {
            n = Math.max(this.getContentInsetEnd(), Math.max(this.mContentInsetEndWithActions, 0));
        }
        else {
            n = this.getContentInsetEnd();
        }
        return n;
    }
    
    public int getCurrentContentInsetLeft() {
        int n;
        if (ViewCompat.getLayoutDirection((View)this) == 1) {
            n = this.getCurrentContentInsetEnd();
        }
        else {
            n = this.getCurrentContentInsetStart();
        }
        return n;
    }
    
    public int getCurrentContentInsetRight() {
        int n;
        if (ViewCompat.getLayoutDirection((View)this) == 1) {
            n = this.getCurrentContentInsetStart();
        }
        else {
            n = this.getCurrentContentInsetEnd();
        }
        return n;
    }
    
    public int getCurrentContentInsetStart() {
        int n;
        if (this.getNavigationIcon() != null) {
            n = Math.max(this.getContentInsetStart(), Math.max(this.mContentInsetStartWithNavigation, 0));
        }
        else {
            n = this.getContentInsetStart();
        }
        return n;
    }
    
    public Drawable getLogo() {
        Drawable drawable;
        if (this.mLogoView != null) {
            drawable = this.mLogoView.getDrawable();
        }
        else {
            drawable = null;
        }
        return drawable;
    }
    
    public CharSequence getLogoDescription() {
        CharSequence contentDescription;
        if (this.mLogoView != null) {
            contentDescription = this.mLogoView.getContentDescription();
        }
        else {
            contentDescription = null;
        }
        return contentDescription;
    }
    
    public Menu getMenu() {
        this.ensureMenu();
        return this.mMenuView.getMenu();
    }
    
    @Nullable
    public CharSequence getNavigationContentDescription() {
        CharSequence contentDescription;
        if (this.mNavButtonView != null) {
            contentDescription = this.mNavButtonView.getContentDescription();
        }
        else {
            contentDescription = null;
        }
        return contentDescription;
    }
    
    @Nullable
    public Drawable getNavigationIcon() {
        Drawable drawable;
        if (this.mNavButtonView != null) {
            drawable = this.mNavButtonView.getDrawable();
        }
        else {
            drawable = null;
        }
        return drawable;
    }
    
    ActionMenuPresenter getOuterActionMenuPresenter() {
        return this.mOuterActionMenuPresenter;
    }
    
    @Nullable
    public Drawable getOverflowIcon() {
        this.ensureMenu();
        return this.mMenuView.getOverflowIcon();
    }
    
    Context getPopupContext() {
        return this.mPopupContext;
    }
    
    public int getPopupTheme() {
        return this.mPopupTheme;
    }
    
    public CharSequence getSubtitle() {
        return this.mSubtitleText;
    }
    
    public CharSequence getTitle() {
        return this.mTitleText;
    }
    
    public int getTitleMarginBottom() {
        return this.mTitleMarginBottom;
    }
    
    public int getTitleMarginEnd() {
        return this.mTitleMarginEnd;
    }
    
    public int getTitleMarginStart() {
        return this.mTitleMarginStart;
    }
    
    public int getTitleMarginTop() {
        return this.mTitleMarginTop;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public DecorToolbar getWrapper() {
        if (this.mWrapper == null) {
            this.mWrapper = new ToolbarWidgetWrapper(this, true);
        }
        return this.mWrapper;
    }
    
    public boolean hasExpandedActionView() {
        return this.mExpandedMenuPresenter != null && this.mExpandedMenuPresenter.mCurrentExpandedItem != null;
    }
    
    public boolean hideOverflowMenu() {
        return this.mMenuView != null && this.mMenuView.hideOverflowMenu();
    }
    
    public void inflateMenu(@MenuRes final int n) {
        this.getMenuInflater().inflate(n, this.getMenu());
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public boolean isOverflowMenuShowPending() {
        return this.mMenuView != null && this.mMenuView.isOverflowMenuShowPending();
    }
    
    public boolean isOverflowMenuShowing() {
        return this.mMenuView != null && this.mMenuView.isOverflowMenuShowing();
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public boolean isTitleTruncated() {
        if (this.mTitleTextView == null) {
            return false;
        }
        final Layout layout = this.mTitleTextView.getLayout();
        if (layout == null) {
            return false;
        }
        for (int lineCount = layout.getLineCount(), i = 0; i < lineCount; ++i) {
            if (layout.getEllipsisCount(i) > 0) {
                return true;
            }
        }
        return false;
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.removeCallbacks(this.mShowOverflowMenuRunnable);
    }
    
    public boolean onHoverEvent(final MotionEvent motionEvent) {
        final int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 9) {
            this.mEatingHover = false;
        }
        if (!this.mEatingHover) {
            final boolean onHoverEvent = super.onHoverEvent(motionEvent);
            if (actionMasked == 9 && !onHoverEvent) {
                this.mEatingHover = true;
            }
        }
        if (actionMasked == 10 || actionMasked == 3) {
            this.mEatingHover = false;
        }
        return true;
    }
    
    protected void onLayout(final boolean b, int n, int i, int a, int a2) {
        final boolean b2 = ViewCompat.getLayoutDirection((View)this) == 1;
        final int width = this.getWidth();
        final int height = this.getHeight();
        final int paddingLeft = this.getPaddingLeft();
        final int paddingRight = this.getPaddingRight();
        final int paddingTop = this.getPaddingTop();
        final int paddingBottom = this.getPaddingBottom();
        final int n2 = width - paddingRight;
        final int[] mTempMargins = this.mTempMargins;
        mTempMargins[mTempMargins[1] = 0] = 0;
        n = ViewCompat.getMinimumHeight((View)this);
        int min;
        if (n >= 0) {
            min = Math.min(n, a2 - i);
        }
        else {
            min = 0;
        }
        Label_0167: {
            if (this.shouldLayout((View)this.mNavButtonView)) {
                if (b2) {
                    a = this.layoutChildRight((View)this.mNavButtonView, n2, mTempMargins, min);
                    a2 = paddingLeft;
                    break Label_0167;
                }
                a2 = this.layoutChildLeft((View)this.mNavButtonView, paddingLeft, mTempMargins, min);
            }
            else {
                a2 = paddingLeft;
            }
            a = n2;
        }
        i = a;
        n = a2;
        if (this.shouldLayout((View)this.mCollapseButtonView)) {
            if (b2) {
                i = this.layoutChildRight((View)this.mCollapseButtonView, a, mTempMargins, min);
                n = a2;
            }
            else {
                n = this.layoutChildLeft((View)this.mCollapseButtonView, a2, mTempMargins, min);
                i = a;
            }
        }
        a = i;
        a2 = n;
        if (this.shouldLayout((View)this.mMenuView)) {
            if (b2) {
                a2 = this.layoutChildLeft((View)this.mMenuView, n, mTempMargins, min);
                a = i;
            }
            else {
                a = this.layoutChildRight((View)this.mMenuView, i, mTempMargins, min);
                a2 = n;
            }
        }
        i = this.getCurrentContentInsetLeft();
        n = this.getCurrentContentInsetRight();
        mTempMargins[0] = Math.max(0, i - a2);
        mTempMargins[1] = Math.max(0, n - (n2 - a));
        i = Math.max(a2, i);
        a = Math.min(a, n2 - n);
        n = i;
        a2 = a;
        if (this.shouldLayout(this.mExpandedActionView)) {
            if (b2) {
                a2 = this.layoutChildRight(this.mExpandedActionView, a, mTempMargins, min);
                n = i;
            }
            else {
                n = this.layoutChildLeft(this.mExpandedActionView, i, mTempMargins, min);
                a2 = a;
            }
        }
        a = n;
        i = a2;
        if (this.shouldLayout((View)this.mLogoView)) {
            if (b2) {
                i = this.layoutChildRight((View)this.mLogoView, a2, mTempMargins, min);
                a = n;
            }
            else {
                a = this.layoutChildLeft((View)this.mLogoView, n, mTempMargins, min);
                i = a2;
            }
        }
        final boolean shouldLayout = this.shouldLayout((View)this.mTitleTextView);
        final boolean shouldLayout2 = this.shouldLayout((View)this.mSubtitleTextView);
        if (shouldLayout) {
            final LayoutParams layoutParams = (LayoutParams)this.mTitleTextView.getLayoutParams();
            n = layoutParams.topMargin + this.mTitleTextView.getMeasuredHeight() + layoutParams.bottomMargin + 0;
        }
        else {
            n = 0;
        }
        if (shouldLayout2) {
            final LayoutParams layoutParams2 = (LayoutParams)this.mSubtitleTextView.getLayoutParams();
            n += layoutParams2.topMargin + this.mSubtitleTextView.getMeasuredHeight() + layoutParams2.bottomMargin;
        }
        Label_1322: {
            if (!shouldLayout && !shouldLayout2) {
                n = a;
            }
            else {
                TextView textView;
                if (shouldLayout) {
                    textView = this.mTitleTextView;
                }
                else {
                    textView = this.mSubtitleTextView;
                }
                TextView textView2;
                if (shouldLayout2) {
                    textView2 = this.mSubtitleTextView;
                }
                else {
                    textView2 = this.mTitleTextView;
                }
                final LayoutParams layoutParams3 = (LayoutParams)((View)textView).getLayoutParams();
                final LayoutParams layoutParams4 = (LayoutParams)((View)textView2).getLayoutParams();
                if ((shouldLayout && this.mTitleTextView.getMeasuredWidth() > 0) || (shouldLayout2 && this.mSubtitleTextView.getMeasuredWidth() > 0)) {
                    a2 = 1;
                }
                else {
                    a2 = 0;
                }
                final int n3 = this.mGravity & 0x70;
                if (n3 != 48) {
                    if (n3 != 80) {
                        final int n4 = (height - paddingTop - paddingBottom - n) / 2;
                        if (n4 < layoutParams3.topMargin + this.mTitleMarginTop) {
                            n = layoutParams3.topMargin + this.mTitleMarginTop;
                        }
                        else {
                            final int n5 = height - paddingBottom - n - n4 - paddingTop;
                            n = n4;
                            if (n5 < layoutParams3.bottomMargin + this.mTitleMarginBottom) {
                                n = Math.max(0, n4 - (layoutParams4.bottomMargin + this.mTitleMarginBottom - n5));
                            }
                        }
                        n += paddingTop;
                    }
                    else {
                        n = height - paddingBottom - layoutParams4.bottomMargin - this.mTitleMarginBottom - n;
                    }
                }
                else {
                    n = this.getPaddingTop() + layoutParams3.topMargin + this.mTitleMarginTop;
                }
                if (b2) {
                    int mTitleMarginStart;
                    if (a2 != 0) {
                        mTitleMarginStart = this.mTitleMarginStart;
                    }
                    else {
                        mTitleMarginStart = 0;
                    }
                    final int b3 = mTitleMarginStart - mTempMargins[1];
                    i -= Math.max(0, b3);
                    mTempMargins[1] = Math.max(0, -b3);
                    int n8;
                    if (shouldLayout) {
                        final LayoutParams layoutParams5 = (LayoutParams)this.mTitleTextView.getLayoutParams();
                        final int n6 = i - this.mTitleTextView.getMeasuredWidth();
                        final int n7 = this.mTitleTextView.getMeasuredHeight() + n;
                        this.mTitleTextView.layout(n6, n, i, n7);
                        n = n6 - this.mTitleMarginEnd;
                        n8 = n7 + layoutParams5.bottomMargin;
                    }
                    else {
                        final int n9 = i;
                        n8 = n;
                        n = n9;
                    }
                    int b4;
                    if (shouldLayout2) {
                        final LayoutParams layoutParams6 = (LayoutParams)this.mSubtitleTextView.getLayoutParams();
                        final int n10 = n8 + layoutParams6.topMargin;
                        this.mSubtitleTextView.layout(i - this.mSubtitleTextView.getMeasuredWidth(), n10, i, this.mSubtitleTextView.getMeasuredHeight() + n10);
                        b4 = i - this.mTitleMarginEnd;
                        final int bottomMargin = layoutParams6.bottomMargin;
                    }
                    else {
                        b4 = i;
                    }
                    if (a2 != 0) {
                        i = Math.min(n, b4);
                    }
                    n = a;
                }
                else {
                    int mTitleMarginStart2;
                    if (a2 != 0) {
                        mTitleMarginStart2 = this.mTitleMarginStart;
                    }
                    else {
                        mTitleMarginStart2 = 0;
                    }
                    final int b5 = mTitleMarginStart2 - mTempMargins[0];
                    a += Math.max(0, b5);
                    mTempMargins[0] = Math.max(0, -b5);
                    int a3;
                    if (shouldLayout) {
                        final LayoutParams layoutParams7 = (LayoutParams)this.mTitleTextView.getLayoutParams();
                        final int n11 = this.mTitleTextView.getMeasuredWidth() + a;
                        final int n12 = this.mTitleTextView.getMeasuredHeight() + n;
                        this.mTitleTextView.layout(a, n, n11, n12);
                        a3 = n11 + this.mTitleMarginEnd;
                        n = n12 + layoutParams7.bottomMargin;
                    }
                    else {
                        a3 = a;
                    }
                    int b6;
                    if (shouldLayout2) {
                        final LayoutParams layoutParams8 = (LayoutParams)this.mSubtitleTextView.getLayoutParams();
                        final int n13 = n + layoutParams8.topMargin;
                        final int n14 = this.mSubtitleTextView.getMeasuredWidth() + a;
                        n = this.mSubtitleTextView.getMeasuredHeight();
                        this.mSubtitleTextView.layout(a, n13, n14, n + n13);
                        b6 = n14 + this.mTitleMarginEnd;
                        n = layoutParams8.bottomMargin;
                    }
                    else {
                        b6 = a;
                    }
                    n = a;
                    a = i;
                    if (a2 != 0) {
                        n = Math.max(a3, b6);
                        a = i;
                    }
                    break Label_1322;
                }
            }
            a = i;
        }
        a2 = 0;
        this.addCustomViewsWithGravity(this.mTempViews, 3);
        int size;
        for (size = this.mTempViews.size(), i = 0; i < size; ++i) {
            n = this.layoutChildLeft(this.mTempViews.get(i), n, mTempMargins, min);
        }
        this.addCustomViewsWithGravity(this.mTempViews, 5);
        int size2;
        for (size2 = this.mTempViews.size(), i = 0; i < size2; ++i) {
            a = this.layoutChildRight(this.mTempViews.get(i), a, mTempMargins, min);
        }
        this.addCustomViewsWithGravity(this.mTempViews, 1);
        final int viewListMeasuredWidth = this.getViewListMeasuredWidth(this.mTempViews, mTempMargins);
        i = paddingLeft + (width - paddingLeft - paddingRight) / 2 - viewListMeasuredWidth / 2;
        final int n15 = viewListMeasuredWidth + i;
        if (i >= n) {
            if (n15 > a) {
                n = i - (n15 - a);
            }
            else {
                n = i;
            }
        }
        for (a = this.mTempViews.size(), i = a2; i < a; ++i) {
            n = this.layoutChildLeft(this.mTempViews.get(i), n, mTempMargins, min);
        }
        this.mTempViews.clear();
    }
    
    protected void onMeasure(int resolveSizeAndState, final int n) {
        final int[] mTempMargins = this.mTempMargins;
        int n2;
        int n3;
        if (ViewUtils.isLayoutRtl((View)this)) {
            n2 = 1;
            n3 = 0;
        }
        else {
            n3 = 1;
            n2 = 0;
        }
        int n4;
        int max;
        int combineMeasuredStates;
        if (this.shouldLayout((View)this.mNavButtonView)) {
            this.measureChildConstrained((View)this.mNavButtonView, resolveSizeAndState, 0, n, 0, this.mMaxButtonHeight);
            n4 = this.mNavButtonView.getMeasuredWidth() + this.getHorizontalMargins((View)this.mNavButtonView);
            max = Math.max(0, this.mNavButtonView.getMeasuredHeight() + this.getVerticalMargins((View)this.mNavButtonView));
            combineMeasuredStates = View.combineMeasuredStates(0, this.mNavButtonView.getMeasuredState());
        }
        else {
            n4 = 0;
            max = (combineMeasuredStates = 0);
        }
        int b = n4;
        int n5 = max;
        int n6 = combineMeasuredStates;
        if (this.shouldLayout((View)this.mCollapseButtonView)) {
            this.measureChildConstrained((View)this.mCollapseButtonView, resolveSizeAndState, 0, n, 0, this.mMaxButtonHeight);
            b = this.mCollapseButtonView.getMeasuredWidth() + this.getHorizontalMargins((View)this.mCollapseButtonView);
            n5 = Math.max(max, this.mCollapseButtonView.getMeasuredHeight() + this.getVerticalMargins((View)this.mCollapseButtonView));
            n6 = View.combineMeasuredStates(combineMeasuredStates, this.mCollapseButtonView.getMeasuredState());
        }
        final int currentContentInsetStart = this.getCurrentContentInsetStart();
        final int n7 = 0 + Math.max(currentContentInsetStart, b);
        mTempMargins[n2] = Math.max(0, currentContentInsetStart - b);
        int b2;
        if (this.shouldLayout((View)this.mMenuView)) {
            this.measureChildConstrained((View)this.mMenuView, resolveSizeAndState, n7, n, 0, this.mMaxButtonHeight);
            b2 = this.mMenuView.getMeasuredWidth() + this.getHorizontalMargins((View)this.mMenuView);
            n5 = Math.max(n5, this.mMenuView.getMeasuredHeight() + this.getVerticalMargins((View)this.mMenuView));
            n6 = View.combineMeasuredStates(n6, this.mMenuView.getMeasuredState());
        }
        else {
            b2 = 0;
        }
        final int currentContentInsetEnd = this.getCurrentContentInsetEnd();
        final int n8 = n7 + Math.max(currentContentInsetEnd, b2);
        mTempMargins[n3] = Math.max(0, currentContentInsetEnd - b2);
        int n9 = n8;
        int max2 = n5;
        int combineMeasuredStates2 = n6;
        if (this.shouldLayout(this.mExpandedActionView)) {
            n9 = n8 + this.measureChildCollapseMargins(this.mExpandedActionView, resolveSizeAndState, n8, n, 0, mTempMargins);
            max2 = Math.max(n5, this.mExpandedActionView.getMeasuredHeight() + this.getVerticalMargins(this.mExpandedActionView));
            combineMeasuredStates2 = View.combineMeasuredStates(n6, this.mExpandedActionView.getMeasuredState());
        }
        int n10 = n9;
        int max3 = max2;
        int n11 = combineMeasuredStates2;
        if (this.shouldLayout((View)this.mLogoView)) {
            n10 = n9 + this.measureChildCollapseMargins((View)this.mLogoView, resolveSizeAndState, n9, n, 0, mTempMargins);
            max3 = Math.max(max2, this.mLogoView.getMeasuredHeight() + this.getVerticalMargins((View)this.mLogoView));
            n11 = View.combineMeasuredStates(combineMeasuredStates2, this.mLogoView.getMeasuredState());
        }
        final int childCount = this.getChildCount();
        int n12 = n10;
        final int n13 = 0;
        int n14 = max3;
        int n15;
        int combineMeasuredStates3;
        int max4;
        for (int i = n13; i < childCount; ++i, n12 = n15, n11 = combineMeasuredStates3, n14 = max4) {
            final View child = this.getChildAt(i);
            n15 = n12;
            combineMeasuredStates3 = n11;
            max4 = n14;
            if (((LayoutParams)child.getLayoutParams()).mViewType == 0) {
                if (!this.shouldLayout(child)) {
                    n15 = n12;
                    combineMeasuredStates3 = n11;
                    max4 = n14;
                }
                else {
                    n15 = n12 + this.measureChildCollapseMargins(child, resolveSizeAndState, n12, n, 0, mTempMargins);
                    max4 = Math.max(n14, child.getMeasuredHeight() + this.getVerticalMargins(child));
                    combineMeasuredStates3 = View.combineMeasuredStates(n11, child.getMeasuredState());
                }
            }
        }
        final int n16 = this.mTitleMarginTop + this.mTitleMarginBottom;
        final int n17 = this.mTitleMarginStart + this.mTitleMarginEnd;
        int b3;
        int max5;
        if (this.shouldLayout((View)this.mTitleTextView)) {
            this.measureChildCollapseMargins((View)this.mTitleTextView, resolveSizeAndState, n12 + n17, n, n16, mTempMargins);
            final int measuredWidth = this.mTitleTextView.getMeasuredWidth();
            final int horizontalMargins = this.getHorizontalMargins((View)this.mTitleTextView);
            final int measuredHeight = this.mTitleTextView.getMeasuredHeight();
            final int verticalMargins = this.getVerticalMargins((View)this.mTitleTextView);
            n11 = View.combineMeasuredStates(n11, this.mTitleTextView.getMeasuredState());
            b3 = measuredHeight + verticalMargins;
            max5 = measuredWidth + horizontalMargins;
        }
        else {
            b3 = 0;
            max5 = 0;
        }
        if (this.shouldLayout((View)this.mSubtitleTextView)) {
            max5 = Math.max(max5, this.measureChildCollapseMargins((View)this.mSubtitleTextView, resolveSizeAndState, n12 + n17, n, b3 + n16, mTempMargins));
            b3 += this.mSubtitleTextView.getMeasuredHeight() + this.getVerticalMargins((View)this.mSubtitleTextView);
            n11 = View.combineMeasuredStates(n11, this.mSubtitleTextView.getMeasuredState());
        }
        final int max6 = Math.max(n14, b3);
        final int paddingLeft = this.getPaddingLeft();
        final int paddingRight = this.getPaddingRight();
        final int paddingTop = this.getPaddingTop();
        final int paddingBottom = this.getPaddingBottom();
        final int resolveSizeAndState2 = View.resolveSizeAndState(Math.max(n12 + max5 + (paddingLeft + paddingRight), this.getSuggestedMinimumWidth()), resolveSizeAndState, 0xFF000000 & n11);
        resolveSizeAndState = View.resolveSizeAndState(Math.max(max6 + (paddingTop + paddingBottom), this.getSuggestedMinimumHeight()), n, n11 << 16);
        if (this.shouldCollapse()) {
            resolveSizeAndState = 0;
        }
        this.setMeasuredDimension(resolveSizeAndState2, resolveSizeAndState);
    }
    
    protected void onRestoreInstanceState(final Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        final SavedState savedState = (SavedState)parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        Object peekMenu;
        if (this.mMenuView != null) {
            peekMenu = this.mMenuView.peekMenu();
        }
        else {
            peekMenu = null;
        }
        if (savedState.expandedMenuItemId != 0 && this.mExpandedMenuPresenter != null && peekMenu != null) {
            final MenuItem item = ((Menu)peekMenu).findItem(savedState.expandedMenuItemId);
            if (item != null) {
                item.expandActionView();
            }
        }
        if (savedState.isOverflowOpen) {
            this.postShowOverflowMenu();
        }
    }
    
    public void onRtlPropertiesChanged(final int n) {
        if (Build$VERSION.SDK_INT >= 17) {
            super.onRtlPropertiesChanged(n);
        }
        this.ensureContentInsets();
        final RtlSpacingHelper mContentInsets = this.mContentInsets;
        boolean direction = true;
        if (n != 1) {
            direction = false;
        }
        mContentInsets.setDirection(direction);
    }
    
    protected Parcelable onSaveInstanceState() {
        final SavedState savedState = new SavedState(super.onSaveInstanceState());
        if (this.mExpandedMenuPresenter != null && this.mExpandedMenuPresenter.mCurrentExpandedItem != null) {
            savedState.expandedMenuItemId = this.mExpandedMenuPresenter.mCurrentExpandedItem.getItemId();
        }
        savedState.isOverflowOpen = this.isOverflowMenuShowing();
        return (Parcelable)savedState;
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        final int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.mEatingTouch = false;
        }
        if (!this.mEatingTouch) {
            final boolean onTouchEvent = super.onTouchEvent(motionEvent);
            if (actionMasked == 0 && !onTouchEvent) {
                this.mEatingTouch = true;
            }
        }
        if (actionMasked == 1 || actionMasked == 3) {
            this.mEatingTouch = false;
        }
        return true;
    }
    
    void removeChildrenForExpandedActionView() {
        for (int i = this.getChildCount() - 1; i >= 0; --i) {
            final View child = this.getChildAt(i);
            if (((LayoutParams)child.getLayoutParams()).mViewType != 2 && child != this.mMenuView) {
                this.removeViewAt(i);
                this.mHiddenViews.add(child);
            }
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setCollapsible(final boolean mCollapsible) {
        this.mCollapsible = mCollapsible;
        this.requestLayout();
    }
    
    public void setContentInsetEndWithActions(final int n) {
        int mContentInsetEndWithActions = n;
        if (n < 0) {
            mContentInsetEndWithActions = Integer.MIN_VALUE;
        }
        if (mContentInsetEndWithActions != this.mContentInsetEndWithActions) {
            this.mContentInsetEndWithActions = mContentInsetEndWithActions;
            if (this.getNavigationIcon() != null) {
                this.requestLayout();
            }
        }
    }
    
    public void setContentInsetStartWithNavigation(final int n) {
        int mContentInsetStartWithNavigation = n;
        if (n < 0) {
            mContentInsetStartWithNavigation = Integer.MIN_VALUE;
        }
        if (mContentInsetStartWithNavigation != this.mContentInsetStartWithNavigation) {
            this.mContentInsetStartWithNavigation = mContentInsetStartWithNavigation;
            if (this.getNavigationIcon() != null) {
                this.requestLayout();
            }
        }
    }
    
    public void setContentInsetsAbsolute(final int n, final int n2) {
        this.ensureContentInsets();
        this.mContentInsets.setAbsolute(n, n2);
    }
    
    public void setContentInsetsRelative(final int n, final int n2) {
        this.ensureContentInsets();
        this.mContentInsets.setRelative(n, n2);
    }
    
    public void setLogo(@DrawableRes final int n) {
        this.setLogo(AppCompatResources.getDrawable(this.getContext(), n));
    }
    
    public void setLogo(final Drawable imageDrawable) {
        if (imageDrawable != null) {
            this.ensureLogoView();
            if (!this.isChildOrHidden((View)this.mLogoView)) {
                this.addSystemView((View)this.mLogoView, true);
            }
        }
        else if (this.mLogoView != null && this.isChildOrHidden((View)this.mLogoView)) {
            this.removeView((View)this.mLogoView);
            this.mHiddenViews.remove(this.mLogoView);
        }
        if (this.mLogoView != null) {
            this.mLogoView.setImageDrawable(imageDrawable);
        }
    }
    
    public void setLogoDescription(@StringRes final int n) {
        this.setLogoDescription(this.getContext().getText(n));
    }
    
    public void setLogoDescription(final CharSequence contentDescription) {
        if (!TextUtils.isEmpty(contentDescription)) {
            this.ensureLogoView();
        }
        if (this.mLogoView != null) {
            this.mLogoView.setContentDescription(contentDescription);
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setMenu(final MenuBuilder menuBuilder, final ActionMenuPresenter actionMenuPresenter) {
        if (menuBuilder == null && this.mMenuView == null) {
            return;
        }
        this.ensureMenuView();
        final MenuBuilder peekMenu = this.mMenuView.peekMenu();
        if (peekMenu == menuBuilder) {
            return;
        }
        if (peekMenu != null) {
            peekMenu.removeMenuPresenter(this.mOuterActionMenuPresenter);
            peekMenu.removeMenuPresenter(this.mExpandedMenuPresenter);
        }
        if (this.mExpandedMenuPresenter == null) {
            this.mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter();
        }
        actionMenuPresenter.setExpandedActionViewsExclusive(true);
        if (menuBuilder != null) {
            menuBuilder.addMenuPresenter(actionMenuPresenter, this.mPopupContext);
            menuBuilder.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
        }
        else {
            actionMenuPresenter.initForMenu(this.mPopupContext, null);
            this.mExpandedMenuPresenter.initForMenu(this.mPopupContext, null);
            actionMenuPresenter.updateMenuView(true);
            this.mExpandedMenuPresenter.updateMenuView(true);
        }
        this.mMenuView.setPopupTheme(this.mPopupTheme);
        this.mMenuView.setPresenter(actionMenuPresenter);
        this.mOuterActionMenuPresenter = actionMenuPresenter;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setMenuCallbacks(final MenuPresenter.Callback mActionMenuPresenterCallback, final MenuBuilder.Callback mMenuBuilderCallback) {
        this.mActionMenuPresenterCallback = mActionMenuPresenterCallback;
        this.mMenuBuilderCallback = mMenuBuilderCallback;
        if (this.mMenuView != null) {
            this.mMenuView.setMenuCallbacks(mActionMenuPresenterCallback, mMenuBuilderCallback);
        }
    }
    
    public void setNavigationContentDescription(@StringRes final int n) {
        CharSequence text;
        if (n != 0) {
            text = this.getContext().getText(n);
        }
        else {
            text = null;
        }
        this.setNavigationContentDescription(text);
    }
    
    public void setNavigationContentDescription(@Nullable final CharSequence contentDescription) {
        if (!TextUtils.isEmpty(contentDescription)) {
            this.ensureNavButtonView();
        }
        if (this.mNavButtonView != null) {
            this.mNavButtonView.setContentDescription(contentDescription);
        }
    }
    
    public void setNavigationIcon(@DrawableRes final int n) {
        this.setNavigationIcon(AppCompatResources.getDrawable(this.getContext(), n));
    }
    
    public void setNavigationIcon(@Nullable final Drawable imageDrawable) {
        if (imageDrawable != null) {
            this.ensureNavButtonView();
            if (!this.isChildOrHidden((View)this.mNavButtonView)) {
                this.addSystemView((View)this.mNavButtonView, true);
            }
        }
        else if (this.mNavButtonView != null && this.isChildOrHidden((View)this.mNavButtonView)) {
            this.removeView((View)this.mNavButtonView);
            this.mHiddenViews.remove(this.mNavButtonView);
        }
        if (this.mNavButtonView != null) {
            this.mNavButtonView.setImageDrawable(imageDrawable);
        }
    }
    
    public void setNavigationOnClickListener(final View$OnClickListener onClickListener) {
        this.ensureNavButtonView();
        this.mNavButtonView.setOnClickListener(onClickListener);
    }
    
    public void setOnMenuItemClickListener(final OnMenuItemClickListener mOnMenuItemClickListener) {
        this.mOnMenuItemClickListener = mOnMenuItemClickListener;
    }
    
    public void setOverflowIcon(@Nullable final Drawable overflowIcon) {
        this.ensureMenu();
        this.mMenuView.setOverflowIcon(overflowIcon);
    }
    
    public void setPopupTheme(@StyleRes final int mPopupTheme) {
        if (this.mPopupTheme != mPopupTheme) {
            if ((this.mPopupTheme = mPopupTheme) == 0) {
                this.mPopupContext = this.getContext();
            }
            else {
                this.mPopupContext = (Context)new ContextThemeWrapper(this.getContext(), mPopupTheme);
            }
        }
    }
    
    public void setSubtitle(@StringRes final int n) {
        this.setSubtitle(this.getContext().getText(n));
    }
    
    public void setSubtitle(final CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            if (this.mSubtitleTextView == null) {
                final Context context = this.getContext();
                (this.mSubtitleTextView = new AppCompatTextView(context)).setSingleLine();
                this.mSubtitleTextView.setEllipsize(TextUtils$TruncateAt.END);
                if (this.mSubtitleTextAppearance != 0) {
                    this.mSubtitleTextView.setTextAppearance(context, this.mSubtitleTextAppearance);
                }
                if (this.mSubtitleTextColor != 0) {
                    this.mSubtitleTextView.setTextColor(this.mSubtitleTextColor);
                }
            }
            if (!this.isChildOrHidden((View)this.mSubtitleTextView)) {
                this.addSystemView((View)this.mSubtitleTextView, true);
            }
        }
        else if (this.mSubtitleTextView != null && this.isChildOrHidden((View)this.mSubtitleTextView)) {
            this.removeView((View)this.mSubtitleTextView);
            this.mHiddenViews.remove(this.mSubtitleTextView);
        }
        if (this.mSubtitleTextView != null) {
            this.mSubtitleTextView.setText(charSequence);
        }
        this.mSubtitleText = charSequence;
    }
    
    public void setSubtitleTextAppearance(final Context context, @StyleRes final int mSubtitleTextAppearance) {
        this.mSubtitleTextAppearance = mSubtitleTextAppearance;
        if (this.mSubtitleTextView != null) {
            this.mSubtitleTextView.setTextAppearance(context, mSubtitleTextAppearance);
        }
    }
    
    public void setSubtitleTextColor(@ColorInt final int n) {
        this.mSubtitleTextColor = n;
        if (this.mSubtitleTextView != null) {
            this.mSubtitleTextView.setTextColor(n);
        }
    }
    
    public void setTitle(@StringRes final int n) {
        this.setTitle(this.getContext().getText(n));
    }
    
    public void setTitle(final CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            if (this.mTitleTextView == null) {
                final Context context = this.getContext();
                (this.mTitleTextView = new AppCompatTextView(context)).setSingleLine();
                this.mTitleTextView.setEllipsize(TextUtils$TruncateAt.END);
                if (this.mTitleTextAppearance != 0) {
                    this.mTitleTextView.setTextAppearance(context, this.mTitleTextAppearance);
                }
                if (this.mTitleTextColor != 0) {
                    this.mTitleTextView.setTextColor(this.mTitleTextColor);
                }
            }
            if (!this.isChildOrHidden((View)this.mTitleTextView)) {
                this.addSystemView((View)this.mTitleTextView, true);
            }
        }
        else if (this.mTitleTextView != null && this.isChildOrHidden((View)this.mTitleTextView)) {
            this.removeView((View)this.mTitleTextView);
            this.mHiddenViews.remove(this.mTitleTextView);
        }
        if (this.mTitleTextView != null) {
            this.mTitleTextView.setText(charSequence);
        }
        this.mTitleText = charSequence;
    }
    
    public void setTitleMargin(final int mTitleMarginStart, final int mTitleMarginTop, final int mTitleMarginEnd, final int mTitleMarginBottom) {
        this.mTitleMarginStart = mTitleMarginStart;
        this.mTitleMarginTop = mTitleMarginTop;
        this.mTitleMarginEnd = mTitleMarginEnd;
        this.mTitleMarginBottom = mTitleMarginBottom;
        this.requestLayout();
    }
    
    public void setTitleMarginBottom(final int mTitleMarginBottom) {
        this.mTitleMarginBottom = mTitleMarginBottom;
        this.requestLayout();
    }
    
    public void setTitleMarginEnd(final int mTitleMarginEnd) {
        this.mTitleMarginEnd = mTitleMarginEnd;
        this.requestLayout();
    }
    
    public void setTitleMarginStart(final int mTitleMarginStart) {
        this.mTitleMarginStart = mTitleMarginStart;
        this.requestLayout();
    }
    
    public void setTitleMarginTop(final int mTitleMarginTop) {
        this.mTitleMarginTop = mTitleMarginTop;
        this.requestLayout();
    }
    
    public void setTitleTextAppearance(final Context context, @StyleRes final int mTitleTextAppearance) {
        this.mTitleTextAppearance = mTitleTextAppearance;
        if (this.mTitleTextView != null) {
            this.mTitleTextView.setTextAppearance(context, mTitleTextAppearance);
        }
    }
    
    public void setTitleTextColor(@ColorInt final int n) {
        this.mTitleTextColor = n;
        if (this.mTitleTextView != null) {
            this.mTitleTextView.setTextColor(n);
        }
    }
    
    public boolean showOverflowMenu() {
        return this.mMenuView != null && this.mMenuView.showOverflowMenu();
    }
    
    private class ExpandedActionViewMenuPresenter implements MenuPresenter
    {
        MenuItemImpl mCurrentExpandedItem;
        MenuBuilder mMenu;
        final Toolbar this$0;
        
        ExpandedActionViewMenuPresenter(final Toolbar this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public boolean collapseItemActionView(final MenuBuilder menuBuilder, final MenuItemImpl menuItemImpl) {
            if (this.this$0.mExpandedActionView instanceof CollapsibleActionView) {
                ((CollapsibleActionView)this.this$0.mExpandedActionView).onActionViewCollapsed();
            }
            this.this$0.removeView(this.this$0.mExpandedActionView);
            this.this$0.removeView((View)this.this$0.mCollapseButtonView);
            this.this$0.mExpandedActionView = null;
            this.this$0.addChildrenForExpandedActionView();
            this.mCurrentExpandedItem = null;
            this.this$0.requestLayout();
            menuItemImpl.setActionViewExpanded(false);
            return true;
        }
        
        @Override
        public boolean expandItemActionView(final MenuBuilder menuBuilder, final MenuItemImpl mCurrentExpandedItem) {
            this.this$0.ensureCollapseButtonView();
            final ViewParent parent = this.this$0.mCollapseButtonView.getParent();
            if (parent != this.this$0) {
                if (parent instanceof ViewGroup) {
                    ((ViewGroup)parent).removeView((View)this.this$0.mCollapseButtonView);
                }
                this.this$0.addView((View)this.this$0.mCollapseButtonView);
            }
            this.this$0.mExpandedActionView = mCurrentExpandedItem.getActionView();
            this.mCurrentExpandedItem = mCurrentExpandedItem;
            final ViewParent parent2 = this.this$0.mExpandedActionView.getParent();
            if (parent2 != this.this$0) {
                if (parent2 instanceof ViewGroup) {
                    ((ViewGroup)parent2).removeView(this.this$0.mExpandedActionView);
                }
                final LayoutParams generateDefaultLayoutParams = this.this$0.generateDefaultLayoutParams();
                generateDefaultLayoutParams.gravity = (0x800003 | (this.this$0.mButtonGravity & 0x70));
                generateDefaultLayoutParams.mViewType = 2;
                this.this$0.mExpandedActionView.setLayoutParams((ViewGroup$LayoutParams)generateDefaultLayoutParams);
                this.this$0.addView(this.this$0.mExpandedActionView);
            }
            this.this$0.removeChildrenForExpandedActionView();
            this.this$0.requestLayout();
            mCurrentExpandedItem.setActionViewExpanded(true);
            if (this.this$0.mExpandedActionView instanceof CollapsibleActionView) {
                ((CollapsibleActionView)this.this$0.mExpandedActionView).onActionViewExpanded();
            }
            return true;
        }
        
        @Override
        public boolean flagActionItems() {
            return false;
        }
        
        @Override
        public int getId() {
            return 0;
        }
        
        @Override
        public MenuView getMenuView(final ViewGroup viewGroup) {
            return null;
        }
        
        @Override
        public void initForMenu(final Context context, final MenuBuilder mMenu) {
            if (this.mMenu != null && this.mCurrentExpandedItem != null) {
                this.mMenu.collapseItemActionView(this.mCurrentExpandedItem);
            }
            this.mMenu = mMenu;
        }
        
        @Override
        public void onCloseMenu(final MenuBuilder menuBuilder, final boolean b) {
        }
        
        @Override
        public void onRestoreInstanceState(final Parcelable parcelable) {
        }
        
        @Override
        public Parcelable onSaveInstanceState() {
            return null;
        }
        
        @Override
        public boolean onSubMenuSelected(final SubMenuBuilder subMenuBuilder) {
            return false;
        }
        
        @Override
        public void setCallback(final Callback callback) {
        }
        
        @Override
        public void updateMenuView(final boolean b) {
            if (this.mCurrentExpandedItem != null) {
                final MenuBuilder mMenu = this.mMenu;
                int n = 0;
                if (mMenu != null) {
                    final int size = this.mMenu.size();
                    int n2 = 0;
                    while (true) {
                        n = n;
                        if (n2 >= size) {
                            break;
                        }
                        if (this.mMenu.getItem(n2) == this.mCurrentExpandedItem) {
                            n = 1;
                            break;
                        }
                        ++n2;
                    }
                }
                if (n == 0) {
                    this.collapseItemActionView(this.mMenu, this.mCurrentExpandedItem);
                }
            }
        }
    }
    
    public static class LayoutParams extends ActionBar.LayoutParams
    {
        static final int CUSTOM = 0;
        static final int EXPANDED = 2;
        static final int SYSTEM = 1;
        int mViewType;
        
        public LayoutParams(final int n) {
            this(-2, -1, n);
        }
        
        public LayoutParams(final int n, final int n2) {
            super(n, n2);
            this.mViewType = 0;
            this.gravity = 8388627;
        }
        
        public LayoutParams(final int n, final int n2, final int gravity) {
            super(n, n2);
            this.mViewType = 0;
            this.gravity = gravity;
        }
        
        public LayoutParams(@NonNull final Context context, final AttributeSet set) {
            super(context, set);
            this.mViewType = 0;
        }
        
        public LayoutParams(final ActionBar.LayoutParams layoutParams) {
            super(layoutParams);
            this.mViewType = 0;
        }
        
        public LayoutParams(final LayoutParams layoutParams) {
            super((ActionBar.LayoutParams)layoutParams);
            this.mViewType = 0;
            this.mViewType = layoutParams.mViewType;
        }
        
        public LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
            this.mViewType = 0;
        }
        
        public LayoutParams(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams) {
            super((ViewGroup$LayoutParams)viewGroup$MarginLayoutParams);
            this.mViewType = 0;
            this.copyMarginsFromCompat(viewGroup$MarginLayoutParams);
        }
        
        void copyMarginsFromCompat(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams) {
            this.leftMargin = viewGroup$MarginLayoutParams.leftMargin;
            this.topMargin = viewGroup$MarginLayoutParams.topMargin;
            this.rightMargin = viewGroup$MarginLayoutParams.rightMargin;
            this.bottomMargin = viewGroup$MarginLayoutParams.bottomMargin;
        }
    }
    
    public interface OnMenuItemClickListener
    {
        boolean onMenuItemClick(final MenuItem p0);
    }
    
    public static class SavedState extends AbsSavedState
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        int expandedMenuItemId;
        boolean isOverflowOpen;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$ClassLoaderCreator<SavedState>() {
                public SavedState createFromParcel(final Parcel parcel) {
                    return new SavedState(parcel, null);
                }
                
                public SavedState createFromParcel(final Parcel parcel, final ClassLoader classLoader) {
                    return new SavedState(parcel, classLoader);
                }
                
                public SavedState[] newArray(final int n) {
                    return new SavedState[n];
                }
            };
        }
        
        public SavedState(final Parcel parcel) {
            this(parcel, null);
        }
        
        public SavedState(final Parcel parcel, final ClassLoader classLoader) {
            super(parcel, classLoader);
            this.expandedMenuItemId = parcel.readInt();
            this.isOverflowOpen = (parcel.readInt() != 0);
        }
        
        public SavedState(final Parcelable parcelable) {
            super(parcelable);
        }
        
        @Override
        public void writeToParcel(final Parcel parcel, final int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.expandedMenuItemId);
            parcel.writeInt((int)(this.isOverflowOpen ? 1 : 0));
        }
    }
}
