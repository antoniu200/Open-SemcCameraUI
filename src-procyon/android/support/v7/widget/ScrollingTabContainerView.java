// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.ViewParent;
import android.text.TextUtils$TruncateAt;
import android.text.TextUtils;
import android.widget.LinearLayout$LayoutParams;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityEvent;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.view.View$MeasureSpec;
import android.widget.AdapterView;
import android.content.res.Configuration;
import android.view.View$OnClickListener;
import android.widget.AbsListView$LayoutParams;
import android.graphics.drawable.Drawable;
import android.animation.Animator$AnimatorListener;
import android.animation.TimeInterpolator;
import android.support.v7.app.ActionBar;
import android.widget.SpinnerAdapter;
import android.util.AttributeSet;
import android.support.v7.appcompat.R;
import android.view.View;
import android.view.ViewGroup$LayoutParams;
import android.support.v7.view.ActionBarPolicy;
import android.content.Context;
import android.view.animation.DecelerateInterpolator;
import android.view.ViewPropertyAnimator;
import android.widget.Spinner;
import android.view.animation.Interpolator;
import android.support.annotation.RestrictTo;
import android.widget.AdapterView$OnItemSelectedListener;
import android.widget.HorizontalScrollView;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class ScrollingTabContainerView extends HorizontalScrollView implements AdapterView$OnItemSelectedListener
{
    private static final int FADE_DURATION = 200;
    private static final String TAG = "ScrollingTabContainerView";
    private static final Interpolator sAlphaInterpolator;
    private boolean mAllowCollapse;
    private int mContentHeight;
    int mMaxTabWidth;
    private int mSelectedTabIndex;
    int mStackedTabMaxWidth;
    private TabClickListener mTabClickListener;
    LinearLayoutCompat mTabLayout;
    Runnable mTabSelector;
    private Spinner mTabSpinner;
    protected final VisibilityAnimListener mVisAnimListener;
    protected ViewPropertyAnimator mVisibilityAnim;
    
    static {
        sAlphaInterpolator = (Interpolator)new DecelerateInterpolator();
    }
    
    public ScrollingTabContainerView(final Context context) {
        super(context);
        this.mVisAnimListener = new VisibilityAnimListener();
        this.setHorizontalScrollBarEnabled(false);
        final ActionBarPolicy value = ActionBarPolicy.get(context);
        this.setContentHeight(value.getTabContainerHeight());
        this.mStackedTabMaxWidth = value.getStackedTabMaxWidth();
        this.addView((View)(this.mTabLayout = this.createTabLayout()), new ViewGroup$LayoutParams(-2, -1));
    }
    
    private Spinner createSpinner() {
        final AppCompatSpinner appCompatSpinner = new AppCompatSpinner(this.getContext(), null, R.attr.actionDropDownStyle);
        appCompatSpinner.setLayoutParams((ViewGroup$LayoutParams)new LinearLayoutCompat.LayoutParams(-2, -1));
        appCompatSpinner.setOnItemSelectedListener((AdapterView$OnItemSelectedListener)this);
        return appCompatSpinner;
    }
    
    private LinearLayoutCompat createTabLayout() {
        final LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(this.getContext(), null, R.attr.actionBarTabBarStyle);
        linearLayoutCompat.setMeasureWithLargestChildEnabled(true);
        linearLayoutCompat.setGravity(17);
        linearLayoutCompat.setLayoutParams((ViewGroup$LayoutParams)new LinearLayoutCompat.LayoutParams(-2, -1));
        return linearLayoutCompat;
    }
    
    private boolean isCollapsed() {
        return this.mTabSpinner != null && this.mTabSpinner.getParent() == this;
    }
    
    private void performCollapse() {
        if (this.isCollapsed()) {
            return;
        }
        if (this.mTabSpinner == null) {
            this.mTabSpinner = this.createSpinner();
        }
        this.removeView((View)this.mTabLayout);
        this.addView((View)this.mTabSpinner, new ViewGroup$LayoutParams(-2, -1));
        if (this.mTabSpinner.getAdapter() == null) {
            this.mTabSpinner.setAdapter((SpinnerAdapter)new TabAdapter());
        }
        if (this.mTabSelector != null) {
            this.removeCallbacks(this.mTabSelector);
            this.mTabSelector = null;
        }
        this.mTabSpinner.setSelection(this.mSelectedTabIndex);
    }
    
    private boolean performExpand() {
        if (!this.isCollapsed()) {
            return false;
        }
        this.removeView((View)this.mTabSpinner);
        this.addView((View)this.mTabLayout, new ViewGroup$LayoutParams(-2, -1));
        this.setTabSelected(this.mTabSpinner.getSelectedItemPosition());
        return false;
    }
    
    public void addTab(final ActionBar.Tab tab, final int n, final boolean b) {
        final TabView tabView = this.createTabView(tab, false);
        this.mTabLayout.addView((View)tabView, n, (ViewGroup$LayoutParams)new LinearLayoutCompat.LayoutParams(0, -1, 1.0f));
        if (this.mTabSpinner != null) {
            ((TabAdapter)this.mTabSpinner.getAdapter()).notifyDataSetChanged();
        }
        if (b) {
            tabView.setSelected(true);
        }
        if (this.mAllowCollapse) {
            this.requestLayout();
        }
    }
    
    public void addTab(final ActionBar.Tab tab, final boolean b) {
        final TabView tabView = this.createTabView(tab, false);
        this.mTabLayout.addView((View)tabView, (ViewGroup$LayoutParams)new LinearLayoutCompat.LayoutParams(0, -1, 1.0f));
        if (this.mTabSpinner != null) {
            ((TabAdapter)this.mTabSpinner.getAdapter()).notifyDataSetChanged();
        }
        if (b) {
            tabView.setSelected(true);
        }
        if (this.mAllowCollapse) {
            this.requestLayout();
        }
    }
    
    public void animateToTab(final int n) {
        final View child = this.mTabLayout.getChildAt(n);
        if (this.mTabSelector != null) {
            this.removeCallbacks(this.mTabSelector);
        }
        this.post(this.mTabSelector = new Runnable(this, child) {
            final ScrollingTabContainerView this$0;
            final View val$tabView;
            
            @Override
            public void run() {
                this.this$0.smoothScrollTo(this.val$tabView.getLeft() - (this.this$0.getWidth() - this.val$tabView.getWidth()) / 2, 0);
                this.this$0.mTabSelector = null;
            }
        });
    }
    
    public void animateToVisibility(final int n) {
        if (this.mVisibilityAnim != null) {
            this.mVisibilityAnim.cancel();
        }
        if (n == 0) {
            if (this.getVisibility() != 0) {
                this.setAlpha(0.0f);
            }
            final ViewPropertyAnimator alpha = this.animate().alpha(1.0f);
            alpha.setDuration(200L);
            alpha.setInterpolator((TimeInterpolator)ScrollingTabContainerView.sAlphaInterpolator);
            alpha.setListener((Animator$AnimatorListener)this.mVisAnimListener.withFinalVisibility(alpha, n));
            alpha.start();
        }
        else {
            final ViewPropertyAnimator alpha2 = this.animate().alpha(0.0f);
            alpha2.setDuration(200L);
            alpha2.setInterpolator((TimeInterpolator)ScrollingTabContainerView.sAlphaInterpolator);
            alpha2.setListener((Animator$AnimatorListener)this.mVisAnimListener.withFinalVisibility(alpha2, n));
            alpha2.start();
        }
    }
    
    TabView createTabView(final ActionBar.Tab tab, final boolean b) {
        final TabView tabView = new TabView(this.getContext(), tab, b);
        if (b) {
            tabView.setBackgroundDrawable((Drawable)null);
            tabView.setLayoutParams((ViewGroup$LayoutParams)new AbsListView$LayoutParams(-1, this.mContentHeight));
        }
        else {
            tabView.setFocusable(true);
            if (this.mTabClickListener == null) {
                this.mTabClickListener = new TabClickListener();
            }
            tabView.setOnClickListener((View$OnClickListener)this.mTabClickListener);
        }
        return tabView;
    }
    
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mTabSelector != null) {
            this.post(this.mTabSelector);
        }
    }
    
    protected void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        final ActionBarPolicy value = ActionBarPolicy.get(this.getContext());
        this.setContentHeight(value.getTabContainerHeight());
        this.mStackedTabMaxWidth = value.getStackedTabMaxWidth();
    }
    
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mTabSelector != null) {
            this.removeCallbacks(this.mTabSelector);
        }
    }
    
    public void onItemSelected(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
        ((TabView)view).getTab().select();
    }
    
    public void onMeasure(int measuredWidth, int measuredWidth2) {
        final int mode = View$MeasureSpec.getMode(measuredWidth);
        measuredWidth2 = 1;
        final boolean fillViewport = mode == 1073741824;
        this.setFillViewport(fillViewport);
        final int childCount = this.mTabLayout.getChildCount();
        if (childCount > 1 && (mode == 1073741824 || mode == Integer.MIN_VALUE)) {
            if (childCount > 2) {
                this.mMaxTabWidth = (int)(View$MeasureSpec.getSize(measuredWidth) * 0.4f);
            }
            else {
                this.mMaxTabWidth = View$MeasureSpec.getSize(measuredWidth) / 2;
            }
            this.mMaxTabWidth = Math.min(this.mMaxTabWidth, this.mStackedTabMaxWidth);
        }
        else {
            this.mMaxTabWidth = -1;
        }
        final int measureSpec = View$MeasureSpec.makeMeasureSpec(this.mContentHeight, 1073741824);
        if (fillViewport || !this.mAllowCollapse) {
            measuredWidth2 = 0;
        }
        if (measuredWidth2 != 0) {
            this.mTabLayout.measure(0, measureSpec);
            if (this.mTabLayout.getMeasuredWidth() > View$MeasureSpec.getSize(measuredWidth)) {
                this.performCollapse();
            }
            else {
                this.performExpand();
            }
        }
        else {
            this.performExpand();
        }
        measuredWidth2 = this.getMeasuredWidth();
        super.onMeasure(measuredWidth, measureSpec);
        measuredWidth = this.getMeasuredWidth();
        if (fillViewport && measuredWidth2 != measuredWidth) {
            this.setTabSelected(this.mSelectedTabIndex);
        }
    }
    
    public void onNothingSelected(final AdapterView<?> adapterView) {
    }
    
    public void removeAllTabs() {
        this.mTabLayout.removeAllViews();
        if (this.mTabSpinner != null) {
            ((TabAdapter)this.mTabSpinner.getAdapter()).notifyDataSetChanged();
        }
        if (this.mAllowCollapse) {
            this.requestLayout();
        }
    }
    
    public void removeTabAt(final int n) {
        this.mTabLayout.removeViewAt(n);
        if (this.mTabSpinner != null) {
            ((TabAdapter)this.mTabSpinner.getAdapter()).notifyDataSetChanged();
        }
        if (this.mAllowCollapse) {
            this.requestLayout();
        }
    }
    
    public void setAllowCollapse(final boolean mAllowCollapse) {
        this.mAllowCollapse = mAllowCollapse;
    }
    
    public void setContentHeight(final int mContentHeight) {
        this.mContentHeight = mContentHeight;
        this.requestLayout();
    }
    
    public void setTabSelected(final int n) {
        this.mSelectedTabIndex = n;
        for (int childCount = this.mTabLayout.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.mTabLayout.getChildAt(i);
            final boolean selected = i == n;
            child.setSelected(selected);
            if (selected) {
                this.animateToTab(n);
            }
        }
        if (this.mTabSpinner != null && n >= 0) {
            this.mTabSpinner.setSelection(n);
        }
    }
    
    public void updateTab(final int n) {
        ((TabView)this.mTabLayout.getChildAt(n)).update();
        if (this.mTabSpinner != null) {
            ((TabAdapter)this.mTabSpinner.getAdapter()).notifyDataSetChanged();
        }
        if (this.mAllowCollapse) {
            this.requestLayout();
        }
    }
    
    private class TabAdapter extends BaseAdapter
    {
        final ScrollingTabContainerView this$0;
        
        TabAdapter(final ScrollingTabContainerView this$0) {
            this.this$0 = this$0;
        }
        
        public int getCount() {
            return this.this$0.mTabLayout.getChildCount();
        }
        
        public Object getItem(final int n) {
            return ((TabView)this.this$0.mTabLayout.getChildAt(n)).getTab();
        }
        
        public long getItemId(final int n) {
            return n;
        }
        
        public View getView(final int n, View tabView, final ViewGroup viewGroup) {
            if (tabView == null) {
                tabView = (View)this.this$0.createTabView((ActionBar.Tab)this.getItem(n), true);
            }
            else {
                ((TabView)tabView).bindTab((ActionBar.Tab)this.getItem(n));
            }
            return tabView;
        }
    }
    
    private class TabClickListener implements View$OnClickListener
    {
        final ScrollingTabContainerView this$0;
        
        TabClickListener(final ScrollingTabContainerView this$0) {
            this.this$0 = this$0;
        }
        
        public void onClick(final View view) {
            ((TabView)view).getTab().select();
            for (int childCount = this.this$0.mTabLayout.getChildCount(), i = 0; i < childCount; ++i) {
                final View child = this.this$0.mTabLayout.getChildAt(i);
                child.setSelected(child == view);
            }
        }
    }
    
    private class TabView extends LinearLayout
    {
        private final int[] BG_ATTRS;
        private View mCustomView;
        private ImageView mIconView;
        private ActionBar.Tab mTab;
        private TextView mTextView;
        final ScrollingTabContainerView this$0;
        
        public TabView(final ScrollingTabContainerView this$0, final Context context, final ActionBar.Tab mTab, final boolean b) {
            this.this$0 = this$0;
            super(context, (AttributeSet)null, R.attr.actionBarTabStyle);
            this.BG_ATTRS = new int[] { 16842964 };
            this.mTab = mTab;
            final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, null, this.BG_ATTRS, R.attr.actionBarTabStyle, 0);
            if (obtainStyledAttributes.hasValue(0)) {
                this.setBackgroundDrawable(obtainStyledAttributes.getDrawable(0));
            }
            obtainStyledAttributes.recycle();
            if (b) {
                this.setGravity(8388627);
            }
            this.update();
        }
        
        public void bindTab(final ActionBar.Tab mTab) {
            this.mTab = mTab;
            this.update();
        }
        
        public ActionBar.Tab getTab() {
            return this.mTab;
        }
        
        public void onInitializeAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(accessibilityEvent);
            accessibilityEvent.setClassName((CharSequence)ActionBar.Tab.class.getName());
        }
        
        public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setClassName((CharSequence)ActionBar.Tab.class.getName());
        }
        
        public void onMeasure(final int n, final int n2) {
            super.onMeasure(n, n2);
            if (this.this$0.mMaxTabWidth > 0 && this.getMeasuredWidth() > this.this$0.mMaxTabWidth) {
                super.onMeasure(View$MeasureSpec.makeMeasureSpec(this.this$0.mMaxTabWidth, 1073741824), n2);
            }
        }
        
        public void setSelected(final boolean selected) {
            final boolean b = this.isSelected() != selected;
            super.setSelected(selected);
            if (b && selected) {
                this.sendAccessibilityEvent(4);
            }
        }
        
        public void update() {
            final ActionBar.Tab mTab = this.mTab;
            final View customView = mTab.getCustomView();
            CharSequence contentDescription = null;
            if (customView != null) {
                final ViewParent parent = customView.getParent();
                if (parent != this) {
                    if (parent != null) {
                        ((ViewGroup)parent).removeView(customView);
                    }
                    this.addView(customView);
                }
                this.mCustomView = customView;
                if (this.mTextView != null) {
                    this.mTextView.setVisibility(8);
                }
                if (this.mIconView != null) {
                    this.mIconView.setVisibility(8);
                    this.mIconView.setImageDrawable((Drawable)null);
                }
            }
            else {
                if (this.mCustomView != null) {
                    this.removeView(this.mCustomView);
                    this.mCustomView = null;
                }
                final Drawable icon = mTab.getIcon();
                final CharSequence text = mTab.getText();
                if (icon != null) {
                    if (this.mIconView == null) {
                        final AppCompatImageView mIconView = new AppCompatImageView(this.getContext());
                        final LinearLayout$LayoutParams layoutParams = new LinearLayout$LayoutParams(-2, -2);
                        layoutParams.gravity = 16;
                        mIconView.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
                        this.addView((View)mIconView, 0);
                        this.mIconView = mIconView;
                    }
                    this.mIconView.setImageDrawable(icon);
                    this.mIconView.setVisibility(0);
                }
                else if (this.mIconView != null) {
                    this.mIconView.setVisibility(8);
                    this.mIconView.setImageDrawable((Drawable)null);
                }
                final boolean b = TextUtils.isEmpty(text) ^ true;
                if (b) {
                    if (this.mTextView == null) {
                        final AppCompatTextView mTextView = new AppCompatTextView(this.getContext(), null, R.attr.actionBarTabTextStyle);
                        mTextView.setEllipsize(TextUtils$TruncateAt.END);
                        final LinearLayout$LayoutParams layoutParams2 = new LinearLayout$LayoutParams(-2, -2);
                        layoutParams2.gravity = 16;
                        mTextView.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
                        this.addView((View)mTextView);
                        this.mTextView = mTextView;
                    }
                    this.mTextView.setText(text);
                    this.mTextView.setVisibility(0);
                }
                else if (this.mTextView != null) {
                    this.mTextView.setVisibility(8);
                    this.mTextView.setText((CharSequence)null);
                }
                if (this.mIconView != null) {
                    this.mIconView.setContentDescription(mTab.getContentDescription());
                }
                if (!b) {
                    contentDescription = mTab.getContentDescription();
                }
                TooltipCompat.setTooltipText((View)this, contentDescription);
            }
        }
    }
    
    protected class VisibilityAnimListener extends AnimatorListenerAdapter
    {
        private boolean mCanceled;
        private int mFinalVisibility;
        final ScrollingTabContainerView this$0;
        
        protected VisibilityAnimListener(final ScrollingTabContainerView this$0) {
            this.this$0 = this$0;
            this.mCanceled = false;
        }
        
        public void onAnimationCancel(final Animator animator) {
            this.mCanceled = true;
        }
        
        public void onAnimationEnd(final Animator animator) {
            if (this.mCanceled) {
                return;
            }
            this.this$0.mVisibilityAnim = null;
            this.this$0.setVisibility(this.mFinalVisibility);
        }
        
        public void onAnimationStart(final Animator animator) {
            this.this$0.setVisibility(0);
            this.mCanceled = false;
        }
        
        public VisibilityAnimListener withFinalVisibility(final ViewPropertyAnimator mVisibilityAnim, final int mFinalVisibility) {
            this.mFinalVisibility = mFinalVisibility;
            this.this$0.mVisibilityAnim = mVisibilityAnim;
            return this;
        }
    }
}
