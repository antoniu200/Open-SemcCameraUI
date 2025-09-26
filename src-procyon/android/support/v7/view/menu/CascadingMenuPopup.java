// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.view.menu;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import android.support.v4.view.GravityCompat;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.FrameLayout;
import android.os.Build$VERSION;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.annotation.Nullable;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.HeaderViewListAdapter;
import android.widget.AdapterView$OnItemClickListener;
import android.util.AttributeSet;
import android.support.v7.widget.MenuPopupWindow;
import android.content.res.Resources;
import android.os.SystemClock;
import android.view.MenuItem;
import java.util.Iterator;
import java.util.ArrayList;
import android.support.annotation.StyleRes;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.v7.appcompat.R;
import android.view.ViewTreeObserver;
import android.os.Handler;
import java.util.List;
import android.support.v7.widget.MenuItemHoverListener;
import android.view.ViewTreeObserver$OnGlobalLayoutListener;
import android.content.Context;
import android.view.View$OnAttachStateChangeListener;
import android.view.View;
import android.widget.PopupWindow$OnDismissListener;
import android.view.View$OnKeyListener;

final class CascadingMenuPopup extends MenuPopup implements MenuPresenter, View$OnKeyListener, PopupWindow$OnDismissListener
{
    static final int HORIZ_POSITION_LEFT = 0;
    static final int HORIZ_POSITION_RIGHT = 1;
    private static final int ITEM_LAYOUT;
    static final int SUBMENU_TIMEOUT_MS = 200;
    private View mAnchorView;
    private final View$OnAttachStateChangeListener mAttachStateChangeListener;
    private final Context mContext;
    private int mDropDownGravity;
    private boolean mForceShowIcon;
    final ViewTreeObserver$OnGlobalLayoutListener mGlobalLayoutListener;
    private boolean mHasXOffset;
    private boolean mHasYOffset;
    private int mLastPosition;
    private final MenuItemHoverListener mMenuItemHoverListener;
    private final int mMenuMaxWidth;
    private PopupWindow$OnDismissListener mOnDismissListener;
    private final boolean mOverflowOnly;
    private final List<MenuBuilder> mPendingMenus;
    private final int mPopupStyleAttr;
    private final int mPopupStyleRes;
    private Callback mPresenterCallback;
    private int mRawDropDownGravity;
    boolean mShouldCloseImmediately;
    private boolean mShowTitle;
    final List<CascadingMenuInfo> mShowingMenus;
    View mShownAnchorView;
    final Handler mSubMenuHoverHandler;
    ViewTreeObserver mTreeObserver;
    private int mXOffset;
    private int mYOffset;
    
    static {
        ITEM_LAYOUT = R.layout.abc_cascading_menu_item_layout;
    }
    
    public CascadingMenuPopup(@NonNull final Context mContext, @NonNull final View mAnchorView, @AttrRes final int mPopupStyleAttr, @StyleRes final int mPopupStyleRes, final boolean mOverflowOnly) {
        this.mPendingMenus = new ArrayList<MenuBuilder>();
        this.mShowingMenus = new ArrayList<CascadingMenuInfo>();
        this.mGlobalLayoutListener = (ViewTreeObserver$OnGlobalLayoutListener)new ViewTreeObserver$OnGlobalLayoutListener() {
            final CascadingMenuPopup this$0;
            
            public void onGlobalLayout() {
                if (this.this$0.isShowing() && this.this$0.mShowingMenus.size() > 0 && !((CascadingMenuInfo)this.this$0.mShowingMenus.get(0)).window.isModal()) {
                    final View mShownAnchorView = this.this$0.mShownAnchorView;
                    if (mShownAnchorView != null && mShownAnchorView.isShown()) {
                        final Iterator<CascadingMenuInfo> iterator = this.this$0.mShowingMenus.iterator();
                        while (iterator.hasNext()) {
                            ((CascadingMenuInfo)iterator.next()).window.show();
                        }
                    }
                    else {
                        this.this$0.dismiss();
                    }
                }
            }
        };
        this.mAttachStateChangeListener = (View$OnAttachStateChangeListener)new View$OnAttachStateChangeListener() {
            final CascadingMenuPopup this$0;
            
            public void onViewAttachedToWindow(final View view) {
            }
            
            public void onViewDetachedFromWindow(final View view) {
                if (this.this$0.mTreeObserver != null) {
                    if (!this.this$0.mTreeObserver.isAlive()) {
                        this.this$0.mTreeObserver = view.getViewTreeObserver();
                    }
                    this.this$0.mTreeObserver.removeGlobalOnLayoutListener(this.this$0.mGlobalLayoutListener);
                }
                view.removeOnAttachStateChangeListener((View$OnAttachStateChangeListener)this);
            }
        };
        this.mMenuItemHoverListener = new MenuItemHoverListener() {
            final CascadingMenuPopup this$0;
            
            @Override
            public void onItemHoverEnter(@NonNull final MenuBuilder menuBuilder, @NonNull final MenuItem menuItem) {
                final Handler mSubMenuHoverHandler = this.this$0.mSubMenuHoverHandler;
                CascadingMenuInfo cascadingMenuInfo = null;
                mSubMenuHoverHandler.removeCallbacksAndMessages((Object)null);
                final int size = this.this$0.mShowingMenus.size();
                int i = 0;
                while (true) {
                    while (i < size) {
                        if (menuBuilder == ((CascadingMenuInfo)this.this$0.mShowingMenus.get(i)).menu) {
                            if (i == -1) {
                                return;
                            }
                            if (++i < this.this$0.mShowingMenus.size()) {
                                cascadingMenuInfo = (CascadingMenuInfo)this.this$0.mShowingMenus.get(i);
                            }
                            this.this$0.mSubMenuHoverHandler.postAtTime((Runnable)new Runnable(this, cascadingMenuInfo, menuItem, menuBuilder) {
                                final CascadingMenuPopup$3 this$1;
                                final MenuItem val$item;
                                final MenuBuilder val$menu;
                                final CascadingMenuInfo val$nextInfo;
                                
                                @Override
                                public void run() {
                                    if (this.val$nextInfo != null) {
                                        this.this$1.this$0.mShouldCloseImmediately = true;
                                        this.val$nextInfo.menu.close(false);
                                        this.this$1.this$0.mShouldCloseImmediately = false;
                                    }
                                    if (this.val$item.isEnabled() && this.val$item.hasSubMenu()) {
                                        this.val$menu.performItemAction(this.val$item, 4);
                                    }
                                }
                            }, (Object)menuBuilder, SystemClock.uptimeMillis() + 200L);
                            return;
                        }
                        else {
                            ++i;
                        }
                    }
                    i = -1;
                    continue;
                }
            }
            
            @Override
            public void onItemHoverExit(@NonNull final MenuBuilder menuBuilder, @NonNull final MenuItem menuItem) {
                this.this$0.mSubMenuHoverHandler.removeCallbacksAndMessages((Object)menuBuilder);
            }
        };
        this.mRawDropDownGravity = 0;
        this.mDropDownGravity = 0;
        this.mContext = mContext;
        this.mAnchorView = mAnchorView;
        this.mPopupStyleAttr = mPopupStyleAttr;
        this.mPopupStyleRes = mPopupStyleRes;
        this.mOverflowOnly = mOverflowOnly;
        this.mForceShowIcon = false;
        this.mLastPosition = this.getInitialMenuPosition();
        final Resources resources = mContext.getResources();
        this.mMenuMaxWidth = Math.max(resources.getDisplayMetrics().widthPixels / 2, resources.getDimensionPixelSize(R.dimen.abc_config_prefDialogWidth));
        this.mSubMenuHoverHandler = new Handler();
    }
    
    private MenuPopupWindow createPopupWindow() {
        final MenuPopupWindow menuPopupWindow = new MenuPopupWindow(this.mContext, null, this.mPopupStyleAttr, this.mPopupStyleRes);
        menuPopupWindow.setHoverListener(this.mMenuItemHoverListener);
        menuPopupWindow.setOnItemClickListener((AdapterView$OnItemClickListener)this);
        menuPopupWindow.setOnDismissListener((PopupWindow$OnDismissListener)this);
        menuPopupWindow.setAnchorView(this.mAnchorView);
        menuPopupWindow.setDropDownGravity(this.mDropDownGravity);
        menuPopupWindow.setModal(true);
        menuPopupWindow.setInputMethodMode(2);
        return menuPopupWindow;
    }
    
    private int findIndexOfAddedMenu(@NonNull final MenuBuilder menuBuilder) {
        for (int size = this.mShowingMenus.size(), i = 0; i < size; ++i) {
            if (menuBuilder == this.mShowingMenus.get(i).menu) {
                return i;
            }
        }
        return -1;
    }
    
    private MenuItem findMenuItemForSubmenu(@NonNull final MenuBuilder menuBuilder, @NonNull final MenuBuilder menuBuilder2) {
        for (int size = menuBuilder.size(), i = 0; i < size; ++i) {
            final MenuItem item = menuBuilder.getItem(i);
            if (item.hasSubMenu() && menuBuilder2 == item.getSubMenu()) {
                return item;
            }
        }
        return null;
    }
    
    @Nullable
    private View findParentViewForSubmenu(@NonNull final CascadingMenuInfo cascadingMenuInfo, @NonNull final MenuBuilder menuBuilder) {
        final MenuItem menuItemForSubmenu = this.findMenuItemForSubmenu(cascadingMenuInfo.menu, menuBuilder);
        if (menuItemForSubmenu == null) {
            return null;
        }
        final ListView listView = cascadingMenuInfo.getListView();
        final ListAdapter adapter = listView.getAdapter();
        final boolean b = adapter instanceof HeaderViewListAdapter;
        int i = 0;
        int headersCount;
        MenuAdapter menuAdapter;
        if (b) {
            final HeaderViewListAdapter headerViewListAdapter = (HeaderViewListAdapter)adapter;
            headersCount = headerViewListAdapter.getHeadersCount();
            menuAdapter = (MenuAdapter)headerViewListAdapter.getWrappedAdapter();
        }
        else {
            menuAdapter = (MenuAdapter)adapter;
            headersCount = 0;
        }
        while (true) {
            while (i < menuAdapter.getCount()) {
                if (menuItemForSubmenu == menuAdapter.getItem(i)) {
                    if (i == -1) {
                        return null;
                    }
                    final int n = i + headersCount - listView.getFirstVisiblePosition();
                    if (n >= 0 && n < listView.getChildCount()) {
                        return listView.getChildAt(n);
                    }
                    return null;
                }
                else {
                    ++i;
                }
            }
            i = -1;
            continue;
        }
    }
    
    private int getInitialMenuPosition() {
        final int layoutDirection = ViewCompat.getLayoutDirection(this.mAnchorView);
        int n = 1;
        if (layoutDirection == 1) {
            n = 0;
        }
        return n;
    }
    
    private int getNextMenuPosition(final int n) {
        final ListView listView = this.mShowingMenus.get(this.mShowingMenus.size() - 1).getListView();
        final int[] array = new int[2];
        listView.getLocationOnScreen(array);
        final Rect rect = new Rect();
        this.mShownAnchorView.getWindowVisibleDisplayFrame(rect);
        if (this.mLastPosition == 1) {
            if (array[0] + listView.getWidth() + n > rect.right) {
                return 0;
            }
            return 1;
        }
        else {
            if (array[0] - n < 0) {
                return 1;
            }
            return 0;
        }
    }
    
    private void showMenu(@NonNull final MenuBuilder menuBuilder) {
        final LayoutInflater from = LayoutInflater.from(this.mContext);
        final MenuAdapter adapter = new MenuAdapter(menuBuilder, from, this.mOverflowOnly, CascadingMenuPopup.ITEM_LAYOUT);
        if (!this.isShowing() && this.mForceShowIcon) {
            adapter.setForceShowIcon(true);
        }
        else if (this.isShowing()) {
            adapter.setForceShowIcon(MenuPopup.shouldPreserveIconSpacing(menuBuilder));
        }
        final int measureIndividualMenuWidth = MenuPopup.measureIndividualMenuWidth((ListAdapter)adapter, null, this.mContext, this.mMenuMaxWidth);
        final MenuPopupWindow popupWindow = this.createPopupWindow();
        popupWindow.setAdapter((ListAdapter)adapter);
        popupWindow.setContentWidth(measureIndividualMenuWidth);
        popupWindow.setDropDownGravity(this.mDropDownGravity);
        CascadingMenuInfo cascadingMenuInfo;
        View parentViewForSubmenu;
        if (this.mShowingMenus.size() > 0) {
            cascadingMenuInfo = this.mShowingMenus.get(this.mShowingMenus.size() - 1);
            parentViewForSubmenu = this.findParentViewForSubmenu(cascadingMenuInfo, menuBuilder);
        }
        else {
            cascadingMenuInfo = null;
            parentViewForSubmenu = null;
        }
        if (parentViewForSubmenu != null) {
            popupWindow.setTouchModal(false);
            popupWindow.setEnterTransition(null);
            final int nextMenuPosition = this.getNextMenuPosition(measureIndividualMenuWidth);
            final boolean b = nextMenuPosition == 1;
            this.mLastPosition = nextMenuPosition;
            int verticalOffset;
            int n;
            if (Build$VERSION.SDK_INT >= 26) {
                popupWindow.setAnchorView(parentViewForSubmenu);
                verticalOffset = 0;
                n = 0;
            }
            else {
                final int[] array = new int[2];
                this.mAnchorView.getLocationOnScreen(array);
                final int[] array2 = new int[2];
                parentViewForSubmenu.getLocationOnScreen(array2);
                if ((this.mDropDownGravity & 0x7) == 0x5) {
                    array[0] += this.mAnchorView.getWidth();
                    array2[0] += parentViewForSubmenu.getWidth();
                }
                n = array2[0] - array[0];
                verticalOffset = array2[1] - array[1];
            }
            int horizontalOffset;
            if ((this.mDropDownGravity & 0x5) == 0x5) {
                if (b) {
                    horizontalOffset = n + measureIndividualMenuWidth;
                }
                else {
                    horizontalOffset = n - parentViewForSubmenu.getWidth();
                }
            }
            else if (b) {
                horizontalOffset = n + parentViewForSubmenu.getWidth();
            }
            else {
                horizontalOffset = n - measureIndividualMenuWidth;
            }
            popupWindow.setHorizontalOffset(horizontalOffset);
            popupWindow.setOverlapAnchor(true);
            popupWindow.setVerticalOffset(verticalOffset);
        }
        else {
            if (this.mHasXOffset) {
                popupWindow.setHorizontalOffset(this.mXOffset);
            }
            if (this.mHasYOffset) {
                popupWindow.setVerticalOffset(this.mYOffset);
            }
            popupWindow.setEpicenterBounds(this.getEpicenterBounds());
        }
        this.mShowingMenus.add(new CascadingMenuInfo(popupWindow, menuBuilder, this.mLastPosition));
        popupWindow.show();
        final ListView listView = popupWindow.getListView();
        listView.setOnKeyListener((View$OnKeyListener)this);
        if (cascadingMenuInfo == null && this.mShowTitle && menuBuilder.getHeaderTitle() != null) {
            final FrameLayout frameLayout = (FrameLayout)from.inflate(R.layout.abc_popup_menu_header_item_layout, (ViewGroup)listView, false);
            final TextView textView = (TextView)frameLayout.findViewById(16908310);
            frameLayout.setEnabled(false);
            textView.setText(menuBuilder.getHeaderTitle());
            listView.addHeaderView((View)frameLayout, (Object)null, false);
            popupWindow.show();
        }
    }
    
    @Override
    public void addMenu(final MenuBuilder menuBuilder) {
        menuBuilder.addMenuPresenter(this, this.mContext);
        if (this.isShowing()) {
            this.showMenu(menuBuilder);
        }
        else {
            this.mPendingMenus.add(menuBuilder);
        }
    }
    
    @Override
    protected boolean closeMenuOnSubMenuOpened() {
        return false;
    }
    
    public void dismiss() {
        int i = this.mShowingMenus.size();
        if (i > 0) {
            final CascadingMenuInfo[] array = this.mShowingMenus.toArray(new CascadingMenuInfo[i]);
            --i;
            while (i >= 0) {
                final CascadingMenuInfo cascadingMenuInfo = array[i];
                if (cascadingMenuInfo.window.isShowing()) {
                    cascadingMenuInfo.window.dismiss();
                }
                --i;
            }
        }
    }
    
    @Override
    public boolean flagActionItems() {
        return false;
    }
    
    public ListView getListView() {
        ListView listView;
        if (this.mShowingMenus.isEmpty()) {
            listView = null;
        }
        else {
            listView = this.mShowingMenus.get(this.mShowingMenus.size() - 1).getListView();
        }
        return listView;
    }
    
    public boolean isShowing() {
        final int size = this.mShowingMenus.size();
        boolean b = false;
        if (size > 0) {
            b = b;
            if (this.mShowingMenus.get(0).window.isShowing()) {
                b = true;
            }
        }
        return b;
    }
    
    @Override
    public void onCloseMenu(final MenuBuilder menuBuilder, final boolean b) {
        final int indexOfAddedMenu = this.findIndexOfAddedMenu(menuBuilder);
        if (indexOfAddedMenu < 0) {
            return;
        }
        final int n = indexOfAddedMenu + 1;
        if (n < this.mShowingMenus.size()) {
            this.mShowingMenus.get(n).menu.close(false);
        }
        final CascadingMenuInfo cascadingMenuInfo = this.mShowingMenus.remove(indexOfAddedMenu);
        cascadingMenuInfo.menu.removeMenuPresenter(this);
        if (this.mShouldCloseImmediately) {
            cascadingMenuInfo.window.setExitTransition(null);
            cascadingMenuInfo.window.setAnimationStyle(0);
        }
        cascadingMenuInfo.window.dismiss();
        final int size = this.mShowingMenus.size();
        if (size > 0) {
            this.mLastPosition = this.mShowingMenus.get(size - 1).position;
        }
        else {
            this.mLastPosition = this.getInitialMenuPosition();
        }
        if (size == 0) {
            this.dismiss();
            if (this.mPresenterCallback != null) {
                this.mPresenterCallback.onCloseMenu(menuBuilder, true);
            }
            if (this.mTreeObserver != null) {
                if (this.mTreeObserver.isAlive()) {
                    this.mTreeObserver.removeGlobalOnLayoutListener(this.mGlobalLayoutListener);
                }
                this.mTreeObserver = null;
            }
            this.mShownAnchorView.removeOnAttachStateChangeListener(this.mAttachStateChangeListener);
            this.mOnDismissListener.onDismiss();
        }
        else if (b) {
            this.mShowingMenus.get(0).menu.close(false);
        }
    }
    
    public void onDismiss() {
        while (true) {
            for (int size = this.mShowingMenus.size(), i = 0; i < size; ++i) {
                final CascadingMenuInfo cascadingMenuInfo = this.mShowingMenus.get(i);
                if (!cascadingMenuInfo.window.isShowing()) {
                    if (cascadingMenuInfo != null) {
                        cascadingMenuInfo.menu.close(false);
                    }
                    return;
                }
            }
            final CascadingMenuInfo cascadingMenuInfo = null;
            continue;
        }
    }
    
    public boolean onKey(final View view, final int n, final KeyEvent keyEvent) {
        if (keyEvent.getAction() == 1 && n == 82) {
            this.dismiss();
            return true;
        }
        return false;
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
        for (final CascadingMenuInfo cascadingMenuInfo : this.mShowingMenus) {
            if (subMenuBuilder == cascadingMenuInfo.menu) {
                cascadingMenuInfo.getListView().requestFocus();
                return true;
            }
        }
        if (subMenuBuilder.hasVisibleItems()) {
            this.addMenu(subMenuBuilder);
            if (this.mPresenterCallback != null) {
                this.mPresenterCallback.onOpenSubMenu(subMenuBuilder);
            }
            return true;
        }
        return false;
    }
    
    @Override
    public void setAnchorView(@NonNull final View mAnchorView) {
        if (this.mAnchorView != mAnchorView) {
            this.mAnchorView = mAnchorView;
            this.mDropDownGravity = GravityCompat.getAbsoluteGravity(this.mRawDropDownGravity, ViewCompat.getLayoutDirection(this.mAnchorView));
        }
    }
    
    @Override
    public void setCallback(final Callback mPresenterCallback) {
        this.mPresenterCallback = mPresenterCallback;
    }
    
    @Override
    public void setForceShowIcon(final boolean mForceShowIcon) {
        this.mForceShowIcon = mForceShowIcon;
    }
    
    @Override
    public void setGravity(final int mRawDropDownGravity) {
        if (this.mRawDropDownGravity != mRawDropDownGravity) {
            this.mRawDropDownGravity = mRawDropDownGravity;
            this.mDropDownGravity = GravityCompat.getAbsoluteGravity(mRawDropDownGravity, ViewCompat.getLayoutDirection(this.mAnchorView));
        }
    }
    
    @Override
    public void setHorizontalOffset(final int mxOffset) {
        this.mHasXOffset = true;
        this.mXOffset = mxOffset;
    }
    
    @Override
    public void setOnDismissListener(final PopupWindow$OnDismissListener mOnDismissListener) {
        this.mOnDismissListener = mOnDismissListener;
    }
    
    @Override
    public void setShowTitle(final boolean mShowTitle) {
        this.mShowTitle = mShowTitle;
    }
    
    @Override
    public void setVerticalOffset(final int myOffset) {
        this.mHasYOffset = true;
        this.mYOffset = myOffset;
    }
    
    public void show() {
        if (this.isShowing()) {
            return;
        }
        final Iterator<MenuBuilder> iterator = this.mPendingMenus.iterator();
        while (iterator.hasNext()) {
            this.showMenu(iterator.next());
        }
        this.mPendingMenus.clear();
        this.mShownAnchorView = this.mAnchorView;
        if (this.mShownAnchorView != null) {
            final boolean b = this.mTreeObserver == null;
            this.mTreeObserver = this.mShownAnchorView.getViewTreeObserver();
            if (b) {
                this.mTreeObserver.addOnGlobalLayoutListener(this.mGlobalLayoutListener);
            }
            this.mShownAnchorView.addOnAttachStateChangeListener(this.mAttachStateChangeListener);
        }
    }
    
    @Override
    public void updateMenuView(final boolean b) {
        final Iterator<CascadingMenuInfo> iterator = this.mShowingMenus.iterator();
        while (iterator.hasNext()) {
            MenuPopup.toMenuAdapter(iterator.next().getListView().getAdapter()).notifyDataSetChanged();
        }
    }
    
    private static class CascadingMenuInfo
    {
        public final MenuBuilder menu;
        public final int position;
        public final MenuPopupWindow window;
        
        public CascadingMenuInfo(@NonNull final MenuPopupWindow window, @NonNull final MenuBuilder menu, final int position) {
            this.window = window;
            this.menu = menu;
            this.position = position;
        }
        
        public ListView getListView() {
            return this.window.getListView();
        }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public @interface HorizPosition {
    }
}
