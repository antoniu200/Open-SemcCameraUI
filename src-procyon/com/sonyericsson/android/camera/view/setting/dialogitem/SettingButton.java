// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.setting.dialogitem;

import android.widget.CompoundButton;
import com.sonyericsson.cameracommon.utility.CommonUtility;
import com.sonyericsson.android.camera.view.setting.settingitem.TypedSettingItem;
import java.util.Iterator;
import com.sonyericsson.android.camera.util.SettingUtil;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.view.setting.dialog.SettingAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ImageView;
import com.sonyericsson.cameracommon.widget.CategorySwitch;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import com.sonyericsson.android.camera.view.setting.settingitem.SettingItem;
import android.content.Context;
import android.content.res.Resources;
import android.view.View$OnClickListener;
import android.widget.CompoundButton$OnCheckedChangeListener;

class SettingButton extends SettingDialogItem
{
    private static final int DISABLED_FILTER = 2131099706;
    private final ViewHolder mHolder;
    private final boolean mIsDeviceInSecurityLock;
    private final CompoundButton$OnCheckedChangeListener mOnCheckedChangeListener;
    private final View$OnClickListener mOnClickListener;
    private final Resources mResources;
    
    public SettingButton(final Context context, final SettingItem settingItem, final boolean mIsDeviceInSecurityLock) {
        super(settingItem);
        this.mOnClickListener = (View$OnClickListener)new View$OnClickListener() {
            final SettingButton this$0;
            
            public void onClick(final View view) {
                if (this.this$0.getView().isShown()) {
                    switch (SettingButton$2.$SwitchMap$com$sonyericsson$android$camera$view$setting$settingitem$SettingItem$Selectability[this.this$0.getItem().getSelectability().ordinal()]) {
                        case 3: {
                            this.this$0.select(this.this$0.getItem());
                            break;
                        }
                        case 1: {
                            this.this$0.select(this.this$0.getItem());
                            break;
                        }
                    }
                }
            }
        };
        this.mOnCheckedChangeListener = (CompoundButton$OnCheckedChangeListener)new SwitchOnCheckedChangeListener();
        final LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService("layout_inflater");
        this.mHolder = new ViewHolder();
        this.mHolder.mContainer = layoutInflater.inflate(2131492999, (ViewGroup)null);
        this.mResources = this.mHolder.mContainer.getContext().getResources();
        this.mHolder.mBackground = (CategorySwitch)this.mHolder.mContainer.findViewById(2131296316);
        this.mHolder.mImage = (ImageView)this.mHolder.mContainer.findViewById(2131296423);
        this.mHolder.mText = (TextView)this.mHolder.mContainer.findViewById(2131296648);
        this.mHolder.mValue = (TextView)this.mHolder.mContainer.findViewById(2131296689);
        this.mHolder.mSwitch = (FrameLayout)this.mHolder.mContainer.findViewById(2131296638);
        this.mHolder.mText.setTextSize(1, 16.0f);
        this.mHolder.mValue.setTextSize(1, 14.0f);
        this.mIsDeviceInSecurityLock = mIsDeviceInSecurityLock;
    }
    
    private void changeToButtonFormat(final ViewGroup viewGroup, final SettingAdapter.ItemLayoutParams itemLayoutParams) {
        this.setText();
        this.setImage();
        this.mHolder.mBackground.setOnClickListener(this.mOnClickListener);
        this.mHolder.mBackground.setSelected(this.getItem().isSelected());
        this.mHolder.mBackground.setContentDescription((CharSequence)this.getItem().getContentDescription(this.mResources));
    }
    
    private void changeToCategoryButtonFormat(final ViewGroup viewGroup, final SettingAdapter.ItemLayoutParams itemLayoutParams) {
        this.setText();
        this.setImage();
        this.setValue();
        int n;
        if (this.mHolder.mImage.getVisibility() == 0) {
            if (this.mHolder.mValue.getVisibility() == 0) {
                n = 2131165609;
            }
            else {
                n = 2131165605;
            }
        }
        else if (this.mHolder.mValue.getVisibility() == 0) {
            n = 2131165608;
        }
        else {
            n = 2131165604;
        }
        this.mHolder.mText.setMaxWidth(this.mResources.getDimensionPixelSize(n));
        this.mHolder.mBackground.setOnClickListener(this.mOnClickListener);
        this.mHolder.mBackground.setContentDescription((CharSequence)this.getItem().getContentDescription(this.mResources));
    }
    
    private void changeToCategorySwitchFormat(final ViewGroup viewGroup, final SettingAdapter.ItemLayoutParams itemLayoutParams) {
        this.setText();
        this.setImage();
        int n;
        if (this.mHolder.mImage.getVisibility() == 0) {
            n = 2131165607;
        }
        else {
            n = 2131165606;
        }
        this.mHolder.mText.setMaxWidth(this.mResources.getDimensionPixelSize(n));
        this.updateContentDescription(this.getOnItem().isSelected());
        this.mHolder.mBackground.setOnCheckedChangeListener(null);
        this.mHolder.mBackground.setChecked(this.getOnItem().isSelected());
        if (this.mIsDeviceInSecurityLock && (this.getItem().compareData(UserSettingKey.GEO_TAG) || (!SettingUtil.isSideSenseEnabled(true) && this.getItem().compareData(UserSettingKey.SIDE_SENSE)))) {
            this.mHolder.mBackground.setOnClickListener(this.mOnClickListener);
        }
        else {
            this.mHolder.mBackground.setOnCheckedChangeListener(this.mOnCheckedChangeListener);
        }
        this.mHolder.mSwitch.setVisibility(0);
    }
    
    private void changeToRestrictFormat(final ViewGroup viewGroup, final SettingAdapter.ItemLayoutParams itemLayoutParams) {
        this.setText();
        this.setImage();
        if (this.getSelectedItem() == null) {
            this.mHolder.mValue.setText(2131690038);
        }
        else {
            this.setValue();
        }
        this.mHolder.mValue.setVisibility(0);
        this.mHolder.mBackground.setOnClickListener(this.mOnClickListener);
        this.mHolder.mBackground.setSelected(this.getItem().isSelected());
        this.mHolder.mBackground.setContentDescription((CharSequence)this.getItem().getContentDescription(this.mResources));
    }
    
    private void changeToValueButtonFormat(final ViewGroup viewGroup, final SettingAdapter.ItemLayoutParams itemLayoutParams) {
        this.setText();
        this.setImage();
        this.mHolder.mBackground.setOnClickListener(this.mOnClickListener);
        this.mHolder.mBackground.setSelected(this.getItem().isSelected());
        this.mHolder.mBackground.setContentDescription((CharSequence)this.getItem().getContentDescription(this.mResources));
    }
    
    private SettingItem getOffItem() {
        return this.getItem().getChildren().get(1);
    }
    
    private SettingItem getOnItem() {
        return this.getItem().getChildren().get(0);
    }
    
    private SettingItem getSelectedItem() {
        for (final SettingItem settingItem : this.getItem().getChildren()) {
            if (settingItem.isSelectable() && settingItem.isSelected()) {
                return settingItem;
            }
        }
        return null;
    }
    
    private void setIconColorGrayOrNot() {
        if (this.getItem().isSelectable()) {
            this.mHolder.mImage.clearColorFilter();
        }
        else {
            this.mHolder.mImage.setColorFilter(2131099706);
        }
    }
    
    private void setImage() {
        if (this.getItem().getIconId() != -1) {
            this.mHolder.mImage.setImageResource(this.getItem().getIconId());
            this.mHolder.mImage.setVisibility(0);
        }
        else {
            this.mHolder.mImage.setVisibility(8);
        }
    }
    
    private void setText() {
        this.mHolder.mText.setText((CharSequence)this.getItem().getText(this.mResources));
        this.mHolder.mText.setVisibility(0);
    }
    
    private void setTextColorGrayOrNot() {
        if (this.getItem().isSelectable()) {
            this.mHolder.mText.setTextColor(this.mResources.getColor(2131099700));
            this.mHolder.mValue.setTextColor(this.mResources.getColor(2131099759));
        }
        else {
            this.mHolder.mText.setTextColor(this.mResources.getColor(2131099711));
            this.mHolder.mValue.setTextColor(this.mResources.getColor(2131099760));
        }
    }
    
    private void setValue() {
        final SettingItem selectedItem = this.getSelectedItem();
        if (selectedItem != null) {
            this.mHolder.mValue.setText((CharSequence)selectedItem.getText(this.mResources));
            this.mHolder.mValue.setVisibility(0);
        }
        else {
            final SettingItem item = this.getItem();
            if (item instanceof TypedSettingItem) {
                final String valueText = ((TypedSettingItem)item).getValueText();
                if (valueText != null && valueText.length() > 0) {
                    this.mHolder.mValue.setText((CharSequence)valueText);
                    this.mHolder.mValue.setVisibility(0);
                }
                else {
                    this.mHolder.mValue.setVisibility(8);
                }
            }
            else {
                this.mHolder.mValue.setVisibility(8);
            }
        }
    }
    
    private void updateContentDescription(final boolean b) {
        final String text = this.getItem().getText(this.mResources);
        String contentDescription;
        if (b) {
            final StringBuilder sb = new StringBuilder();
            sb.append(text);
            sb.append(" ");
            sb.append(this.getOnItem().getContentDescription(this.mResources));
            contentDescription = sb.toString();
        }
        else {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(text);
            sb2.append(" ");
            sb2.append(this.getOffItem().getContentDescription(this.mResources));
            contentDescription = sb2.toString();
        }
        this.mHolder.mContainer.setContentDescription((CharSequence)contentDescription);
    }
    
    @Override
    public View getView() {
        return this.mHolder.mContainer;
    }
    
    @Override
    public void setClickable(final boolean clickable) {
        this.mHolder.mBackground.setClickable(clickable);
    }
    
    @Override
    public void update(final ViewGroup viewGroup, final SettingAdapter.ItemLayoutParams itemLayoutParams) {
        if (this.getItem().getSelectability() != SettingItem.Selectability.RESTRICTED) {
            switch (this.getItem().getDialogItemType()) {
                case 4: {
                    this.changeToCategorySwitchFormat(viewGroup, itemLayoutParams);
                    break;
                }
                case 3: {
                    this.changeToCategoryButtonFormat(viewGroup, itemLayoutParams);
                    break;
                }
                case 2: {
                    this.changeToValueButtonFormat(viewGroup, itemLayoutParams);
                    break;
                }
                case 1: {
                    this.changeToButtonFormat(viewGroup, itemLayoutParams);
                    break;
                }
            }
            this.setTextColorGrayOrNot();
            this.setIconColorGrayOrNot();
        }
        else {
            this.changeToRestrictFormat(viewGroup, itemLayoutParams);
        }
        if (CommonUtility.isMirroringRequired(this.mHolder.mContainer.getContext())) {
            this.mHolder.mText.setGravity(21);
            this.mHolder.mValue.setGravity(19);
        }
        else {
            this.mHolder.mText.setGravity(19);
            this.mHolder.mValue.setGravity(21);
        }
    }
    
    private final class SwitchOnCheckedChangeListener implements CompoundButton$OnCheckedChangeListener
    {
        final SettingButton this$0;
        
        private SwitchOnCheckedChangeListener(final SettingButton this$0) {
            this.this$0 = this$0;
        }
        
        public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
            if (this.this$0.getView().isShown()) {
                if (!this.this$0.getItem().isSelectable()) {
                    this.this$0.mHolder.mBackground.setChecked(false);
                    return;
                }
                if (b) {
                    this.this$0.select(this.this$0.getOnItem());
                }
                else {
                    this.this$0.select(this.this$0.getOffItem());
                }
                this.this$0.updateContentDescription(b);
            }
        }
    }
    
    private static class ViewHolder
    {
        CategorySwitch mBackground;
        View mContainer;
        ImageView mImage;
        FrameLayout mSwitch;
        TextView mText;
        TextView mValue;
    }
}
