// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.widget;

import android.support.annotation.Nullable;
import android.support.annotation.MenuRes;
import android.support.annotation.RestrictTo;
import android.widget.ListView;
import android.support.v7.view.SupportMenuInflater;
import android.view.MenuInflater;
import android.view.Menu;
import android.support.v7.view.menu.ShowableListMenu;
import android.widget.PopupWindow$OnDismissListener;
import android.view.MenuItem;
import android.support.annotation.StyleRes;
import android.support.annotation.AttrRes;
import android.support.v7.appcompat.R;
import android.support.annotation.NonNull;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.view.menu.MenuBuilder;
import android.view.View$OnTouchListener;
import android.content.Context;
import android.view.View;

public class PopupMenu
{
    private final View mAnchor;
    private final Context mContext;
    private View$OnTouchListener mDragListener;
    private final MenuBuilder mMenu;
    OnMenuItemClickListener mMenuItemClickListener;
    OnDismissListener mOnDismissListener;
    final MenuPopupHelper mPopup;
    
    public PopupMenu(@NonNull final Context context, @NonNull final View view) {
        this(context, view, 0);
    }
    
    public PopupMenu(@NonNull final Context context, @NonNull final View view, final int n) {
        this(context, view, n, R.attr.popupMenuStyle, 0);
    }
    
    public PopupMenu(@NonNull final Context mContext, @NonNull final View mAnchor, final int gravity, @AttrRes final int n, @StyleRes final int n2) {
        this.mContext = mContext;
        this.mAnchor = mAnchor;
        (this.mMenu = new MenuBuilder(mContext)).setCallback((MenuBuilder.Callback)new MenuBuilder.Callback(this) {
            final PopupMenu this$0;
            
            @Override
            public boolean onMenuItemSelected(final MenuBuilder menuBuilder, final MenuItem menuItem) {
                return this.this$0.mMenuItemClickListener != null && this.this$0.mMenuItemClickListener.onMenuItemClick(menuItem);
            }
            
            @Override
            public void onMenuModeChange(final MenuBuilder menuBuilder) {
            }
        });
        (this.mPopup = new MenuPopupHelper(mContext, this.mMenu, mAnchor, false, n, n2)).setGravity(gravity);
        this.mPopup.setOnDismissListener((PopupWindow$OnDismissListener)new PopupWindow$OnDismissListener(this) {
            final PopupMenu this$0;
            
            public void onDismiss() {
                if (this.this$0.mOnDismissListener != null) {
                    this.this$0.mOnDismissListener.onDismiss(this.this$0);
                }
            }
        });
    }
    
    public void dismiss() {
        this.mPopup.dismiss();
    }
    
    @NonNull
    public View$OnTouchListener getDragToOpenListener() {
        if (this.mDragListener == null) {
            this.mDragListener = (View$OnTouchListener)new ForwardingListener(this, this.mAnchor) {
                final PopupMenu this$0;
                
                @Override
                public ShowableListMenu getPopup() {
                    return this.this$0.mPopup.getPopup();
                }
                
                @Override
                protected boolean onForwardingStarted() {
                    this.this$0.show();
                    return true;
                }
                
                @Override
                protected boolean onForwardingStopped() {
                    this.this$0.dismiss();
                    return true;
                }
            };
        }
        return this.mDragListener;
    }
    
    public int getGravity() {
        return this.mPopup.getGravity();
    }
    
    @NonNull
    public Menu getMenu() {
        return (Menu)this.mMenu;
    }
    
    @NonNull
    public MenuInflater getMenuInflater() {
        return new SupportMenuInflater(this.mContext);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    ListView getMenuListView() {
        if (!this.mPopup.isShowing()) {
            return null;
        }
        return this.mPopup.getListView();
    }
    
    public void inflate(@MenuRes final int n) {
        this.getMenuInflater().inflate(n, (Menu)this.mMenu);
    }
    
    public void setGravity(final int gravity) {
        this.mPopup.setGravity(gravity);
    }
    
    public void setOnDismissListener(@Nullable final OnDismissListener mOnDismissListener) {
        this.mOnDismissListener = mOnDismissListener;
    }
    
    public void setOnMenuItemClickListener(@Nullable final OnMenuItemClickListener mMenuItemClickListener) {
        this.mMenuItemClickListener = mMenuItemClickListener;
    }
    
    public void show() {
        this.mPopup.show();
    }
    
    public interface OnDismissListener
    {
        void onDismiss(final PopupMenu p0);
    }
    
    public interface OnMenuItemClickListener
    {
        boolean onMenuItemClick(final MenuItem p0);
    }
}
