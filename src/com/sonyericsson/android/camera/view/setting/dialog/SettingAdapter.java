package com.sonyericsson.android.camera.view.setting.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.view.setting.dialogitem.SettingDialogItem;
import com.sonyericsson.android.camera.view.setting.dialogitem.SettingDialogItemFactory;
import com.sonyericsson.android.camera.view.setting.settingitem.SettingItem;
import com.sonyericsson.android.camera.view.setting.settingitem.SettingItemBuilder;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SettingAdapter extends ArrayAdapter<SettingItem> {
    public static final int INVALID_VALUE = -1;
    public static final String TAG = "SettingAdapter";
    private final SettingDialogItemFactory mDialogItemFactory;
    private boolean mIsDeviceInSecurityLock;
    private int mItemHeight;
    private boolean mSetRoundBackgroundTop;

    public SettingAdapter(Context context, List<SettingItem> list, SettingDialogItemFactory settingDialogItemFactory, boolean z) {
        super(context, 0, list);
        this.mDialogItemFactory = settingDialogItemFactory;
        this.mSetRoundBackgroundTop = false;
        this.mIsDeviceInSecurityLock = z;
        this.mItemHeight = -1;
    }

    public SettingAdapter(Context context, SettingDialogItemFactory settingDialogItemFactory, boolean z) {
        this(context, new ArrayList(), settingDialogItemFactory, z);
    }

    public SettingAdapter(Context context, boolean z) {
        this(context, new ArrayList(), new SettingDialogItemFactory(), z);
    }

    public void setItemHeight(int i) {
        if (this.mItemHeight != i) {
            this.mItemHeight = i;
            notifyDataSetChanged();
        }
    }

    public void setRoundTopItemBackground(boolean z) {
        if (this.mSetRoundBackgroundTop != z) {
            this.mSetRoundBackgroundTop = z;
            notifyDataSetChanged();
        }
    }

    public <T> void selectByData(T t) {
        SettingItem settingItemCommit = SettingItemBuilder.build(t).commit();
        for (int i = 0; i < getCount(); i++) {
            SettingItem item = getItem(i);
            if (item.isSelectable()) {
                if (settingItemCommit.compareData(item)) {
                    item.setSelected(true);
                } else {
                    item.setSelected(false);
                }
            }
        }
    }

    public void selectByItem(SettingItem settingItem) {
        if (settingItem == null) {
            return;
        }
        for (int i = 0; i < getCount(); i++) {
            SettingItem item = getItem(i);
            if (settingItem.compareData(item)) {
                item.setSelected(true);
            } else {
                item.setSelected(false);
            }
        }
    }

    public SettingItem getSelected() {
        for (int i = 0; i < getCount(); i++) {
            SettingItem item = getItem(i);
            if (item.isSelected()) {
                return item;
            }
        }
        return null;
    }

    public int getSelectedPosition() {
        for (int i = 0; i < getCount(); i++) {
            if (getItem(i).isSelected()) {
                return i;
            }
        }
        return 0;
    }

    public static class ItemLayoutParams {
        public final boolean bottom;
        public final int height;
        public final boolean left;
        public final boolean right;
        public final boolean top;

        public ItemLayoutParams(int i, boolean z, boolean z2, boolean z3, boolean z4) {
            this.height = i;
            this.top = z;
            this.bottom = z2;
            this.left = z3;
            this.right = z4;
        }
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (CamLog.VERBOSE) {
            CamLog.d("getView()");
        }
        SettingItem item = getItem(i);
        ItemLayoutParams itemLayoutParamsGenerateItemLayoutParams = generateItemLayoutParams(viewGroup, i);
        if (view != null) {
            if (CamLog.VERBOSE) {
                CamLog.d("  has convertView");
            }
            if (view.getTag() instanceof SettingDialogItem) {
                if (CamLog.VERBOSE) {
                    CamLog.d("  has dialogItem");
                }
                SettingDialogItem settingDialogItem = (SettingDialogItem) view.getTag();
                if (settingDialogItem.getItem() != item) {
                    if (CamLog.VERBOSE) {
                        CamLog.d("  update item");
                    }
                    settingDialogItem.setItem(item);
                }
                settingDialogItem.update(viewGroup, itemLayoutParamsGenerateItemLayoutParams);
                return settingDialogItem.getView();
            }
        }
        if (CamLog.VERBOSE) {
            CamLog.d("  create new dialogItem");
        }
        SettingDialogItem settingDialogItemCreate = this.mDialogItemFactory.create(item, viewGroup, this.mIsDeviceInSecurityLock);
        settingDialogItemCreate.update(viewGroup, itemLayoutParamsGenerateItemLayoutParams);
        settingDialogItemCreate.getView().setTag(settingDialogItemCreate);
        return settingDialogItemCreate.getView();
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getItemViewType(int i) {
        SettingItem item = getItem(i);
        if (item instanceof SettingItem) {
            return item.getDialogItemType();
        }
        return super.getItemViewType(i);
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getViewTypeCount() {
        return this.mDialogItemFactory.getDialogItemTypeCount();
    }

    private ItemLayoutParams generateItemLayoutParams(ViewGroup viewGroup, int i) {
        int numColumns = viewGroup instanceof GridView ? ((GridView) viewGroup).getNumColumns() : 1;
        int i2 = i / numColumns;
        int i3 = i % numColumns;
        return new ItemLayoutParams(this.mItemHeight, this.mSetRoundBackgroundTop && i2 == 0, i2 == (getCount() / numColumns) - 1, i3 == 0, i3 == numColumns + (-1));
    }
}
