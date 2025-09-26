// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.view.menu;

import android.view.ViewGroup;
import android.view.View;
import java.util.ArrayList;
import android.view.LayoutInflater;
import android.support.annotation.RestrictTo;
import android.widget.BaseAdapter;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class MenuAdapter extends BaseAdapter
{
    MenuBuilder mAdapterMenu;
    private int mExpandedIndex;
    private boolean mForceShowIcon;
    private final LayoutInflater mInflater;
    private final int mItemLayoutRes;
    private final boolean mOverflowOnly;
    
    public MenuAdapter(final MenuBuilder mAdapterMenu, final LayoutInflater mInflater, final boolean mOverflowOnly, final int mItemLayoutRes) {
        this.mExpandedIndex = -1;
        this.mOverflowOnly = mOverflowOnly;
        this.mInflater = mInflater;
        this.mAdapterMenu = mAdapterMenu;
        this.mItemLayoutRes = mItemLayoutRes;
        this.findExpandedIndex();
    }
    
    void findExpandedIndex() {
        final MenuItemImpl expandedItem = this.mAdapterMenu.getExpandedItem();
        if (expandedItem != null) {
            final ArrayList<MenuItemImpl> nonActionItems = this.mAdapterMenu.getNonActionItems();
            for (int size = nonActionItems.size(), i = 0; i < size; ++i) {
                if (nonActionItems.get(i) == expandedItem) {
                    this.mExpandedIndex = i;
                    return;
                }
            }
        }
        this.mExpandedIndex = -1;
    }
    
    public MenuBuilder getAdapterMenu() {
        return this.mAdapterMenu;
    }
    
    public int getCount() {
        ArrayList<MenuItemImpl> list;
        if (this.mOverflowOnly) {
            list = this.mAdapterMenu.getNonActionItems();
        }
        else {
            list = this.mAdapterMenu.getVisibleItems();
        }
        if (this.mExpandedIndex < 0) {
            return list.size();
        }
        return list.size() - 1;
    }
    
    public boolean getForceShowIcon() {
        return this.mForceShowIcon;
    }
    
    public MenuItemImpl getItem(final int n) {
        ArrayList<MenuItemImpl> list;
        if (this.mOverflowOnly) {
            list = this.mAdapterMenu.getNonActionItems();
        }
        else {
            list = this.mAdapterMenu.getVisibleItems();
        }
        int index = n;
        if (this.mExpandedIndex >= 0 && (index = n) >= this.mExpandedIndex) {
            index = n + 1;
        }
        return list.get(index);
    }
    
    public long getItemId(final int n) {
        return n;
    }
    
    public View getView(final int n, final View view, final ViewGroup viewGroup) {
        View inflate = view;
        if (view == null) {
            inflate = this.mInflater.inflate(this.mItemLayoutRes, viewGroup, false);
        }
        final int groupId = this.getItem(n).getGroupId();
        final int n2 = n - 1;
        int groupId2;
        if (n2 >= 0) {
            groupId2 = this.getItem(n2).getGroupId();
        }
        else {
            groupId2 = groupId;
        }
        final ListMenuItemView listMenuItemView = (ListMenuItemView)inflate;
        listMenuItemView.setGroupDividerEnabled(this.mAdapterMenu.isGroupDividerEnabled() && groupId != groupId2);
        final MenuView.ItemView itemView = (MenuView.ItemView)inflate;
        if (this.mForceShowIcon) {
            listMenuItemView.setForceShowIcon(true);
        }
        itemView.initialize(this.getItem(n), 0);
        return inflate;
    }
    
    public void notifyDataSetChanged() {
        this.findExpandedIndex();
        super.notifyDataSetChanged();
    }
    
    public void setForceShowIcon(final boolean mForceShowIcon) {
        this.mForceShowIcon = mForceShowIcon;
    }
}
