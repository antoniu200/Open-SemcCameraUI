// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.overlaycontrol.imagequality;

import com.sonyericsson.android.camera.view.setting.settingitem.SettingItem;
import android.view.ViewGroup$LayoutParams;
import android.view.View$OnClickListener;
import com.sonyericsson.android.camera.util.CoordinateUtil;
import android.util.DisplayMetrics;
import android.widget.LinearLayout$LayoutParams;
import android.view.MotionEvent;
import com.sonyericsson.android.camera.configuration.parameters.WhiteBalance;
import com.sonyericsson.android.camera.view.setting.SettingUi;
import com.sonyericsson.android.camera.view.setting.dialogitem.SettingDialogItem;
import java.util.Iterator;
import com.sonyericsson.android.camera.view.setting.settingitem.TypedSettingItem;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.view.overlaycontrol.EnumValueAccessor;
import java.util.Map;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import android.view.LayoutInflater;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import android.graphics.Rect;
import android.view.ViewGroup;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.view.View;
import android.content.Context;
import com.sonyericsson.android.camera.view.setting.dialog.SettingAdapter;

public class ImageQualityControlView extends SettingDialog
{
    private SettingAdapter mAdapter;
    private final Context mContext;
    private OnImageQualityControlDialogTabSelectListener mListener;
    private View mResetButton;
    private LinearLayout mTabContainer;
    private LinearLayout mWidgetContainer;
    
    public ImageQualityControlView(final Context mContext, final AttributeSet set) {
        super(mContext, set);
        this.mContext = mContext;
    }
    
    public static ImageQualityControlView create(final ViewGroup viewGroup, final Rect rect, final LayoutDependencyResolver.ScreenAspect screenAspect) {
        final ImageQualityControlView imageQualityControlView = (ImageQualityControlView)((LayoutInflater)viewGroup.getContext().getSystemService("layout_inflater")).inflate(2131492998, (ViewGroup)null);
        imageQualityControlView.setLayoutCoordinator(new ControlLayoutCoordinator(imageQualityControlView, new Rect(0, 0, rect.height(), rect.width()), screenAspect));
        viewGroup.addView((View)imageQualityControlView);
        imageQualityControlView.setVisibility(4);
        return imageQualityControlView;
    }
    
    private int getDimensionPixelSize(final int n) {
        return this.mContext.getResources().getDimensionPixelSize(n);
    }
    
    private int getImageQualityControlTabIcon(final UserSettingKey obj) {
        switch (ImageQualityControlView$3.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[obj.ordinal()]) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Undefined title for ");
                sb.append(obj);
                throw new IllegalArgumentException(sb.toString());
            }
            case 5: {
                return 2131231078;
            }
            case 4: {
                return 2131231080;
            }
            case 3: {
                return 2131231077;
            }
            case 2: {
                return 2131231079;
            }
            case 1: {
                return 2131231081;
            }
        }
    }
    
    private String getString(final int n) {
        return this.mContext.getResources().getString(n);
    }
    
    private boolean isUpdateAdapterNeeded(final Map<UserSettingKey, EnumValueAccessor<? extends UserSettingValue>> map) {
        final Iterator<Map.Entry<UserSettingKey, EnumValueAccessor<? extends UserSettingValue>>> iterator = map.entrySet().iterator();
        int n = 0;
        while (iterator.hasNext()) {
            if (((Map.Entry<UserSettingKey, V>)iterator.next()).getKey().isSelectable()) {
                ++n;
            }
        }
        if (this.mAdapter.getCount() != n) {
            if (CamLog.DEBUG) {
                CamLog.d("Count of tab is modified. So update this view.");
            }
            return true;
        }
        for (int i = 0; i < this.mAdapter.getCount(); ++i) {
            final TypedSettingItem typedSettingItem = (TypedSettingItem)this.mAdapter.getItem(i);
            final EnumValueAccessor enumValueAccessor = map.get(typedSettingItem.getData());
            if (enumValueAccessor == null) {
                if (CamLog.DEBUG) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("The item is removed. So update this view. key:");
                    sb.append(typedSettingItem.getData());
                    CamLog.d(sb.toString());
                }
                return true;
            }
            if (typedSettingItem.getChildren().size() != ((UserSettingValue[])enumValueAccessor.values()).length) {
                if (CamLog.DEBUG) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Child count of the item is modified. So update this view. key:");
                    sb2.append(typedSettingItem.getData());
                    CamLog.d(sb2.toString());
                }
                return true;
            }
            for (int j = 0; j < ((UserSettingValue[])enumValueAccessor.values()).length; ++j) {
                final TypedSettingItem typedSettingItem2 = (TypedSettingItem)typedSettingItem.getChildren().get(j);
                if (!((UserSettingValue[])enumValueAccessor.values())[j].equals(typedSettingItem2.getData())) {
                    if (CamLog.DEBUG) {
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("Child item of the item is modified. So update this view. key:");
                        sb3.append(typedSettingItem.getData());
                        sb3.append(" index:");
                        sb3.append(j);
                        CamLog.d(sb3.toString());
                    }
                    if (CamLog.DEBUG) {
                        final StringBuilder sb4 = new StringBuilder();
                        sb4.append("  value1:");
                        sb4.append(((UserSettingValue[])enumValueAccessor.values())[j]);
                        CamLog.d(sb4.toString());
                    }
                    if (CamLog.DEBUG) {
                        final StringBuilder sb5 = new StringBuilder();
                        sb5.append("  value2:");
                        sb5.append(typedSettingItem2.getData());
                        CamLog.d(sb5.toString());
                    }
                    return true;
                }
            }
        }
        return false;
    }
    
    private void onResetButtonClicked() {
        for (int i = 0; i < this.mWidgetContainer.getChildCount(); ++i) {
            final Object tag = this.mWidgetContainer.getChildAt(i).getTag();
            if (tag instanceof SettingDialogItem) {
                ((SettingDialogItem)tag).reset();
            }
        }
    }
    
    private void setTabContentDescription(final ImageQualityControlTab imageQualityControlTab, final UserSettingKey userSettingKey, final UserSettingValue userSettingValue) {
        final int imageQualityControlTabDescription = SettingUi.getImageQualityControlTabDescription(userSettingKey);
        final int textId = userSettingValue.getTextId();
        final String string = this.getString(imageQualityControlTabDescription);
        final StringBuilder sb = new StringBuilder();
        sb.append(string);
        sb.append(" ");
        sb.append(this.getString(textId));
        imageQualityControlTab.setContentDescription((CharSequence)sb.toString());
    }
    
    private void setTabIcon(final ImageQualityControlTab imageQualityControlTab, final UserSettingKey userSettingKey) {
        imageQualityControlTab.setIcon(this.getImageQualityControlTabIcon(userSettingKey));
    }
    
    private void setTabValue(final ImageQualityControlTab imageQualityControlTab, final UserSettingKey userSettingKey, final UserSettingValue userSettingValue) {
        switch (ImageQualityControlView$3.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[userSettingKey.ordinal()]) {
            case 2:
            case 3:
            case 4:
            case 5: {
                imageQualityControlTab.setValueText(this.getString(userSettingValue.getTextId()), userSettingValue != SettingUi.getImageQualityControlDefaultValue(userSettingKey));
                break;
            }
            case 1: {
                imageQualityControlTab.setValueIcon(((WhiteBalance)userSettingValue).getTabIconId());
                break;
            }
        }
    }
    
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.setWillNotDraw(false);
        this.mTabContainer = (LinearLayout)this.findViewById(2131296644);
        this.mWidgetContainer = (LinearLayout)this.findViewById(2131296697);
    }
    
    @Override
    public void setAdapter(final SettingAdapter mAdapter) {
        this.mAdapter = mAdapter;
        this.mTabContainer.removeAllViews();
        this.mWidgetContainer.removeAllViews();
        final LinearLayout$LayoutParams linearLayout$LayoutParams = new LinearLayout$LayoutParams(-2, -2);
        linearLayout$LayoutParams.gravity = 17;
        linearLayout$LayoutParams.width = this.getDimensionPixelSize(2131165381);
        linearLayout$LayoutParams.height = this.getDimensionPixelSize(2131165380);
        final LinearLayout$LayoutParams linearLayout$LayoutParams2 = new LinearLayout$LayoutParams(-2, -2);
        linearLayout$LayoutParams2.gravity = 17;
        linearLayout$LayoutParams2.width = this.getDimensionPixelSize(2131165386);
        linearLayout$LayoutParams2.height = this.getDimensionPixelSize(2131165385);
        if (this.getResources().getDisplayMetrics().densityDpi > DisplayMetrics.DENSITY_DEVICE_STABLE) {
            final float n = DisplayMetrics.DENSITY_DEVICE_STABLE * 1.0f / 160.0f;
            linearLayout$LayoutParams.height = (int)(CoordinateUtil.convertPx2Dip(this.mContext, linearLayout$LayoutParams.height) * n);
            linearLayout$LayoutParams2.width = (int)(CoordinateUtil.convertPx2Dip(this.mContext, linearLayout$LayoutParams2.width) * n);
            linearLayout$LayoutParams2.height = (int)(CoordinateUtil.convertPx2Dip(this.mContext, linearLayout$LayoutParams2.height) * n);
        }
        final LayoutInflater from = LayoutInflater.from(this.mContext);
        for (int i = 0; i < this.mAdapter.getCount(); ++i) {
            final UserSettingKey tag = ((TypedSettingItem)this.mAdapter.getItem(i)).getData();
            final View inflate = from.inflate(2131493009, (ViewGroup)null);
            inflate.setClickable(true);
            inflate.setTag((Object)tag);
            inflate.setOnClickListener((View$OnClickListener)new View$OnClickListener(this) {
                final ImageQualityControlView this$0;
                
                public void onClick(final View view) {
                    this.this$0.mListener.onSelect((UserSettingKey)view.getTag());
                }
            });
            inflate.setBackgroundResource(2131231558);
            this.setTabIcon((ImageQualityControlTab)inflate, tag);
            this.mTabContainer.addView(inflate, i, (ViewGroup$LayoutParams)linearLayout$LayoutParams);
            final View view = this.mAdapter.getView(i, null, (ViewGroup)this.mWidgetContainer);
            view.setVisibility(8);
            this.mWidgetContainer.addView(view, (ViewGroup$LayoutParams)linearLayout$LayoutParams2);
        }
        (this.mResetButton = from.inflate(2131493008, (ViewGroup)null)).setClickable(true);
        this.mResetButton.setOnClickListener((View$OnClickListener)new View$OnClickListener(this) {
            final ImageQualityControlView this$0;
            
            public void onClick(final View view) {
                this.this$0.onResetButtonClicked();
            }
        });
        this.mTabContainer.addView(this.mResetButton, (ViewGroup$LayoutParams)linearLayout$LayoutParams);
    }
    
    public void setOnImageQualityControlDialogTabSelectListener(final OnImageQualityControlDialogTabSelectListener mListener) {
        this.mListener = mListener;
    }
    
    @Override
    public void setSensorOrientation(final int n) {
        this.requestLayout();
        final int n2 = 0;
        int n3 = 0;
        int i;
        while (true) {
            i = n2;
            if (n3 >= this.mTabContainer.getChildCount()) {
                break;
            }
            final View child = this.mTabContainer.getChildAt(n3);
            if (child instanceof ImageQualityControlTab) {
                ((ImageQualityControlTab)child).setUiOrientation(n);
            }
            else if (child instanceof ImageQualityControlResetButton) {
                ((ImageQualityControlResetButton)child).setUiOrientation(n);
            }
            ++n3;
        }
        while (i < this.mWidgetContainer.getChildCount()) {
            final Object tag = this.mWidgetContainer.getChildAt(i).getTag();
            if (tag instanceof SettingDialogItem) {
                ((SettingDialogItem)tag).setUiOrientation(n);
            }
            ++i;
        }
        super.setSensorOrientation(n);
    }
    
    public boolean update(final UserSettingKey userSettingKey, final Map<UserSettingKey, EnumValueAccessor<? extends UserSettingValue>> map) {
        if (this.isUpdateAdapterNeeded(map)) {
            return false;
        }
        int i = 0;
        int enabled = 0;
        while (i < this.mAdapter.getCount()) {
            final TypedSettingItem typedSettingItem = (TypedSettingItem)this.mAdapter.getItem(i);
            final UserSettingKey userSettingKey2 = typedSettingItem.getData();
            final UserSettingValue userSettingValue = map.get(userSettingKey2).get();
            int n = enabled;
            if (userSettingValue != null) {
                n = enabled;
                if (enabled == 0) {
                    n = enabled;
                    if (userSettingValue != SettingUi.getImageQualityControlDefaultValue(userSettingKey2)) {
                        n = 1;
                    }
                }
                final View child = this.mTabContainer.getChildAt(i);
                if (child instanceof ImageQualityControlTab) {
                    final ImageQualityControlTab imageQualityControlTab = (ImageQualityControlTab)child;
                    this.setTabValue(imageQualityControlTab, userSettingKey2, userSettingValue);
                    this.setTabContentDescription(imageQualityControlTab, userSettingKey2, userSettingValue);
                    child.setSelected(userSettingKey2 == userSettingKey);
                }
                final View child2 = this.mWidgetContainer.getChildAt(i);
                final Object tag = child2.getTag();
                if (tag instanceof SettingDialogItem) {
                    for (final SettingItem settingItem : typedSettingItem.getChildren()) {
                        if (settingItem.compareData(userSettingValue)) {
                            settingItem.setSelected(true);
                        }
                        else {
                            settingItem.setSelected(false);
                        }
                    }
                    ((SettingDialogItem)tag).refresh();
                }
                int visibility;
                if (userSettingKey2 == userSettingKey) {
                    visibility = 0;
                }
                else {
                    visibility = 8;
                }
                child2.setVisibility(visibility);
            }
            ++i;
            enabled = n;
        }
        if (this.mResetButton != null) {
            this.mResetButton.setEnabled((boolean)(enabled != 0));
        }
        return true;
    }
    
    public interface OnImageQualityControlDialogTabSelectListener
    {
        void onSelect(final UserSettingKey p0);
    }
}
