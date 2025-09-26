// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.view.menu;

import android.view.MotionEvent;
import android.os.Parcelable;
import android.view.View$MeasureSpec;
import android.view.View;
import android.support.v7.widget.TooltipCompat;
import android.text.TextUtils;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.content.res.Resources;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.ForwardingListener;
import android.support.annotation.RestrictTo;
import android.support.v7.widget.ActionMenuView;
import android.view.View$OnClickListener;
import android.support.v7.widget.AppCompatTextView;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class ActionMenuItemView extends AppCompatTextView implements ItemView, View$OnClickListener, ActionMenuChildView
{
    private static final int MAX_ICON_SIZE = 32;
    private static final String TAG = "ActionMenuItemView";
    private boolean mAllowTextWithIcon;
    private boolean mExpandedFormat;
    private ForwardingListener mForwardingListener;
    private Drawable mIcon;
    MenuItemImpl mItemData;
    ItemInvoker mItemInvoker;
    private int mMaxIconSize;
    private int mMinWidth;
    PopupCallback mPopupCallback;
    private int mSavedPaddingLeft;
    private CharSequence mTitle;
    
    public ActionMenuItemView(final Context context) {
        this(context, null);
    }
    
    public ActionMenuItemView(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public ActionMenuItemView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        final Resources resources = context.getResources();
        this.mAllowTextWithIcon = this.shouldAllowTextWithIcon();
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.ActionMenuItemView, n, 0);
        this.mMinWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.ActionMenuItemView_android_minWidth, 0);
        obtainStyledAttributes.recycle();
        this.mMaxIconSize = (int)(32.0f * resources.getDisplayMetrics().density + 0.5f);
        this.setOnClickListener((View$OnClickListener)this);
        this.mSavedPaddingLeft = -1;
        this.setSaveEnabled(false);
    }
    
    private boolean shouldAllowTextWithIcon() {
        final Configuration configuration = this.getContext().getResources().getConfiguration();
        final int screenWidthDp = configuration.screenWidthDp;
        final int screenHeightDp = configuration.screenHeightDp;
        return screenWidthDp >= 480 || (screenWidthDp >= 640 && screenHeightDp >= 480) || configuration.orientation == 2;
    }
    
    private void updateTextButtonVisibility() {
        final boolean empty = TextUtils.isEmpty(this.mTitle);
        boolean b2;
        final boolean b = b2 = true;
        Label_0052: {
            if (this.mIcon != null) {
                if (this.mItemData.showsTextAsAction()) {
                    b2 = b;
                    if (this.mAllowTextWithIcon) {
                        break Label_0052;
                    }
                    if (this.mExpandedFormat) {
                        b2 = b;
                        break Label_0052;
                    }
                }
                b2 = false;
            }
        }
        final boolean b3 = (empty ^ true) & b2;
        final CharSequence charSequence = null;
        CharSequence mTitle;
        if (b3) {
            mTitle = this.mTitle;
        }
        else {
            mTitle = null;
        }
        this.setText(mTitle);
        final CharSequence contentDescription = this.mItemData.getContentDescription();
        if (TextUtils.isEmpty(contentDescription)) {
            CharSequence title;
            if (b3) {
                title = null;
            }
            else {
                title = this.mItemData.getTitle();
            }
            this.setContentDescription(title);
        }
        else {
            this.setContentDescription(contentDescription);
        }
        final CharSequence tooltipText = this.mItemData.getTooltipText();
        if (TextUtils.isEmpty(tooltipText)) {
            CharSequence title2;
            if (b3) {
                title2 = charSequence;
            }
            else {
                title2 = this.mItemData.getTitle();
            }
            TooltipCompat.setTooltipText((View)this, title2);
        }
        else {
            TooltipCompat.setTooltipText((View)this, tooltipText);
        }
    }
    
    @Override
    public MenuItemImpl getItemData() {
        return this.mItemData;
    }
    
    public boolean hasText() {
        return TextUtils.isEmpty(this.getText()) ^ true;
    }
    
    @Override
    public void initialize(final MenuItemImpl mItemData, int visibility) {
        this.mItemData = mItemData;
        this.setIcon(mItemData.getIcon());
        this.setTitle(mItemData.getTitleForItemView(this));
        this.setId(mItemData.getItemId());
        if (mItemData.isVisible()) {
            visibility = 0;
        }
        else {
            visibility = 8;
        }
        this.setVisibility(visibility);
        ((MenuView.ItemView)this).setEnabled(mItemData.isEnabled());
        if (mItemData.hasSubMenu() && this.mForwardingListener == null) {
            this.mForwardingListener = new ActionMenuItemForwardingListener();
        }
    }
    
    public boolean needsDividerAfter() {
        return this.hasText();
    }
    
    public boolean needsDividerBefore() {
        return this.hasText() && this.mItemData.getIcon() == null;
    }
    
    public void onClick(final View view) {
        if (this.mItemInvoker != null) {
            this.mItemInvoker.invokeItem(this.mItemData);
        }
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mAllowTextWithIcon = this.shouldAllowTextWithIcon();
        this.updateTextButtonVisibility();
    }
    
    @Override
    protected void onMeasure(int a, final int n) {
        final boolean hasText = this.hasText();
        if (hasText && this.mSavedPaddingLeft >= 0) {
            super.setPadding(this.mSavedPaddingLeft, this.getPaddingTop(), this.getPaddingRight(), this.getPaddingBottom());
        }
        super.onMeasure(a, n);
        final int mode = View$MeasureSpec.getMode(a);
        a = View$MeasureSpec.getSize(a);
        final int measuredWidth = this.getMeasuredWidth();
        if (mode == Integer.MIN_VALUE) {
            a = Math.min(a, this.mMinWidth);
        }
        else {
            a = this.mMinWidth;
        }
        if (mode != 1073741824 && this.mMinWidth > 0 && measuredWidth < a) {
            super.onMeasure(View$MeasureSpec.makeMeasureSpec(a, 1073741824), n);
        }
        if (!hasText && this.mIcon != null) {
            super.setPadding((this.getMeasuredWidth() - this.mIcon.getBounds().width()) / 2, this.getPaddingTop(), this.getPaddingRight(), this.getPaddingBottom());
        }
    }
    
    public void onRestoreInstanceState(final Parcelable parcelable) {
        super.onRestoreInstanceState((Parcelable)null);
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        return (this.mItemData.hasSubMenu() && this.mForwardingListener != null && this.mForwardingListener.onTouch((View)this, motionEvent)) || super.onTouchEvent(motionEvent);
    }
    
    @Override
    public boolean prefersCondensedTitle() {
        return true;
    }
    
    @Override
    public void setCheckable(final boolean b) {
    }
    
    @Override
    public void setChecked(final boolean b) {
    }
    
    public void setExpandedFormat(final boolean mExpandedFormat) {
        if (this.mExpandedFormat != mExpandedFormat) {
            this.mExpandedFormat = mExpandedFormat;
            if (this.mItemData != null) {
                this.mItemData.actionFormatChanged();
            }
        }
    }
    
    @Override
    public void setIcon(final Drawable mIcon) {
        this.mIcon = mIcon;
        if (mIcon != null) {
            final int intrinsicWidth = mIcon.getIntrinsicWidth();
            final int intrinsicHeight = mIcon.getIntrinsicHeight();
            int mMaxIconSize = intrinsicWidth;
            int n = intrinsicHeight;
            if (intrinsicWidth > this.mMaxIconSize) {
                final float n2 = this.mMaxIconSize / (float)intrinsicWidth;
                mMaxIconSize = this.mMaxIconSize;
                n = (int)(intrinsicHeight * n2);
            }
            int n3 = mMaxIconSize;
            int mMaxIconSize2;
            if ((mMaxIconSize2 = n) > this.mMaxIconSize) {
                final float n4 = this.mMaxIconSize / (float)n;
                mMaxIconSize2 = this.mMaxIconSize;
                n3 = (int)(mMaxIconSize * n4);
            }
            mIcon.setBounds(0, 0, n3, mMaxIconSize2);
        }
        this.setCompoundDrawables(mIcon, (Drawable)null, (Drawable)null, (Drawable)null);
        this.updateTextButtonVisibility();
    }
    
    public void setItemInvoker(final ItemInvoker mItemInvoker) {
        this.mItemInvoker = mItemInvoker;
    }
    
    public void setPadding(final int mSavedPaddingLeft, final int n, final int n2, final int n3) {
        super.setPadding(this.mSavedPaddingLeft = mSavedPaddingLeft, n, n2, n3);
    }
    
    public void setPopupCallback(final PopupCallback mPopupCallback) {
        this.mPopupCallback = mPopupCallback;
    }
    
    @Override
    public void setShortcut(final boolean b, final char c) {
    }
    
    @Override
    public void setTitle(final CharSequence mTitle) {
        this.mTitle = mTitle;
        this.updateTextButtonVisibility();
    }
    
    @Override
    public boolean showsIcon() {
        return true;
    }
    
    private class ActionMenuItemForwardingListener extends ForwardingListener
    {
        final ActionMenuItemView this$0;
        
        public ActionMenuItemForwardingListener(final ActionMenuItemView this$0) {
            super((View)(this.this$0 = this$0));
        }
        
        @Override
        public ShowableListMenu getPopup() {
            if (this.this$0.mPopupCallback != null) {
                return this.this$0.mPopupCallback.getPopup();
            }
            return null;
        }
        
        @Override
        protected boolean onForwardingStarted() {
            final ItemInvoker mItemInvoker = this.this$0.mItemInvoker;
            final boolean b = false;
            if (mItemInvoker != null && this.this$0.mItemInvoker.invokeItem(this.this$0.mItemData)) {
                final ShowableListMenu popup = this.getPopup();
                boolean b2 = b;
                if (popup != null) {
                    b2 = b;
                    if (popup.isShowing()) {
                        b2 = true;
                    }
                }
                return b2;
            }
            return false;
        }
    }
    
    public abstract static class PopupCallback
    {
        public abstract ShowableListMenu getPopup();
    }
}
