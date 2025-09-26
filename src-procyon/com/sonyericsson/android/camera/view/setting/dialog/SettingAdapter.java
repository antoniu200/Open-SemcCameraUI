// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.setting.dialog;

import com.sonyericsson.android.camera.view.setting.settingitem.SettingItemBuilder;
import com.sonyericsson.android.camera.view.setting.dialogitem.SettingDialogItem;
import com.sonyericsson.android.camera.util.CamLog;
import android.view.View;
import android.widget.GridView;
import android.view.ViewGroup;
import java.util.List;
import java.util.ArrayList;
import android.content.Context;
import com.sonyericsson.android.camera.view.setting.dialogitem.SettingDialogItemFactory;
import com.sonyericsson.android.camera.view.setting.settingitem.SettingItem;
import android.widget.ArrayAdapter;

public class SettingAdapter extends ArrayAdapter<SettingItem>
{
    public static final int INVALID_VALUE = -1;
    public static final String TAG = "SettingAdapter";
    private final SettingDialogItemFactory mDialogItemFactory;
    private boolean mIsDeviceInSecurityLock;
    private int mItemHeight;
    private boolean mSetRoundBackgroundTop;
    
    public SettingAdapter(final Context context, final SettingDialogItemFactory settingDialogItemFactory, final boolean b) {
        this(context, new ArrayList<SettingItem>(), settingDialogItemFactory, b);
    }
    
    public SettingAdapter(final Context context, final List<SettingItem> list, final SettingDialogItemFactory mDialogItemFactory, final boolean mIsDeviceInSecurityLock) {
        super(context, 0, (List)list);
        this.mDialogItemFactory = mDialogItemFactory;
        this.mSetRoundBackgroundTop = false;
        this.mIsDeviceInSecurityLock = mIsDeviceInSecurityLock;
        this.mItemHeight = -1;
    }
    
    public SettingAdapter(final Context context, final boolean b) {
        this(context, new ArrayList<SettingItem>(), new SettingDialogItemFactory(), b);
    }
    
    private ItemLayoutParams generateItemLayoutParams(final ViewGroup viewGroup, int n) {
        int numColumns;
        if (viewGroup instanceof GridView) {
            numColumns = ((GridView)viewGroup).getNumColumns();
        }
        else {
            numColumns = 1;
        }
        final int n2 = this.getCount() / numColumns;
        final int n3 = n / numColumns;
        n %= numColumns;
        return new ItemLayoutParams(this.mItemHeight, this.mSetRoundBackgroundTop && n3 == 0, n3 == n2 - 1, n == 0, n == numColumns - 1);
    }
    
    public int getItemViewType(final int n) {
        final Object item = this.getItem(n);
        if (item instanceof SettingItem) {
            return ((SettingItem)item).getDialogItemType();
        }
        return super.getItemViewType(n);
    }
    
    public SettingItem getSelected() {
        for (int i = 0; i < this.getCount(); ++i) {
            final SettingItem settingItem = (SettingItem)this.getItem(i);
            if (settingItem.isSelected()) {
                return settingItem;
            }
        }
        return null;
    }
    
    public int getSelectedPosition() {
        for (int i = 0; i < this.getCount(); ++i) {
            if (((SettingItem)this.getItem(i)).isSelected()) {
                return i;
            }
        }
        return 0;
    }
    
    public View getView(final int n, final View view, final ViewGroup viewGroup) {
        if (CamLog.VERBOSE) {
            CamLog.d("getView()");
        }
        final SettingItem item = (SettingItem)this.getItem(n);
        final ItemLayoutParams generateItemLayoutParams = this.generateItemLayoutParams(viewGroup, n);
        if (view != null) {
            if (CamLog.VERBOSE) {
                CamLog.d("  has convertView");
            }
            if (view.getTag() instanceof SettingDialogItem) {
                if (CamLog.VERBOSE) {
                    CamLog.d("  has dialogItem");
                }
                final SettingDialogItem settingDialogItem = (SettingDialogItem)view.getTag();
                if (settingDialogItem.getItem() != item) {
                    if (CamLog.VERBOSE) {
                        CamLog.d("  update item");
                    }
                    settingDialogItem.setItem(item);
                }
                settingDialogItem.update(viewGroup, generateItemLayoutParams);
                return settingDialogItem.getView();
            }
        }
        if (CamLog.VERBOSE) {
            CamLog.d("  create new dialogItem");
        }
        final SettingDialogItem create = this.mDialogItemFactory.create(item, viewGroup, this.mIsDeviceInSecurityLock);
        create.update(viewGroup, generateItemLayoutParams);
        create.getView().setTag((Object)create);
        return create.getView();
    }
    
    public int getViewTypeCount() {
        return this.mDialogItemFactory.getDialogItemTypeCount();
    }
    
    public <T> void selectByData(final T t) {
        final SettingItem commit = SettingItemBuilder.build(t).commit();
        for (int i = 0; i < this.getCount(); ++i) {
            final SettingItem settingItem = (SettingItem)this.getItem(i);
            if (settingItem.isSelectable()) {
                if (commit.compareData(settingItem)) {
                    settingItem.setSelected(true);
                }
                else {
                    settingItem.setSelected(false);
                }
            }
        }
    }
    
    public void selectByItem(final SettingItem settingItem) {
        if (settingItem == null) {
            return;
        }
        for (int i = 0; i < this.getCount(); ++i) {
            final SettingItem settingItem2 = (SettingItem)this.getItem(i);
            if (settingItem.compareData(settingItem2)) {
                settingItem2.setSelected(true);
            }
            else {
                settingItem2.setSelected(false);
            }
        }
    }
    
    public void setItemHeight(final int mItemHeight) {
        if (this.mItemHeight != mItemHeight) {
            this.mItemHeight = mItemHeight;
            this.notifyDataSetChanged();
        }
    }
    
    public void setRoundTopItemBackground(final boolean mSetRoundBackgroundTop) {
        if (this.mSetRoundBackgroundTop != mSetRoundBackgroundTop) {
            this.mSetRoundBackgroundTop = mSetRoundBackgroundTop;
            this.notifyDataSetChanged();
        }
    }
    
    public static class ItemLayoutParams
    {
        public final boolean bottom;
        public final int height;
        public final boolean left;
        public final boolean right;
        public final boolean top;
        
        public ItemLayoutParams(final int height, final boolean top, final boolean bottom, final boolean left, final boolean right) {
            this.height = height;
            this.top = top;
            this.bottom = bottom;
            this.left = left;
            this.right = right;
        }
    }
}
