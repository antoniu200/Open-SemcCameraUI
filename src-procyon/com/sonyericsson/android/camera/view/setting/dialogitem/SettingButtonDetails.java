// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.setting.dialogitem;

import com.sonyericsson.android.camera.view.setting.dialog.SettingAdapter;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import com.sonyericsson.android.camera.view.setting.settingitem.SettingItem;
import android.content.Context;
import android.content.res.Resources;
import android.view.View$OnClickListener;
import android.widget.TextView;
import android.view.View;

class SettingButtonDetails extends SettingDialogItem
{
    private final View mBackground;
    private final View mContainer;
    private final TextView mDescription;
    private final View$OnClickListener mOnClickListener;
    private final Resources mResources;
    private final View mSeparator;
    private final TextView mText;
    
    public SettingButtonDetails(final Context context, final SettingItem settingItem) {
        super(settingItem);
        this.mOnClickListener = (View$OnClickListener)new View$OnClickListener() {
            final SettingButtonDetails this$0;
            
            public void onClick(final View view) {
                if (this.this$0.getView().isShown() && this.this$0.getItem().isSelectable()) {
                    this.this$0.select(this.this$0.getItem());
                }
            }
        };
        this.mResources = context.getResources();
        this.mContainer = ((LayoutInflater)context.getSystemService("layout_inflater")).inflate(2131492997, (ViewGroup)null);
        this.mBackground = this.mContainer.findViewById(2131296316);
        this.mSeparator = this.mContainer.findViewById(2131296595);
        this.mText = (TextView)this.mContainer.findViewById(2131296648);
        this.mDescription = (TextView)this.mContainer.findViewById(2131296384);
    }
    
    @Override
    public View getView() {
        return this.mContainer;
    }
    
    @Override
    public void setClickable(final boolean clickable) {
        this.mBackground.setClickable(clickable);
    }
    
    @Override
    public void update(final ViewGroup viewGroup, final SettingAdapter.ItemLayoutParams itemLayoutParams) {
        this.mBackground.setOnClickListener(this.mOnClickListener);
        this.mBackground.setSelected(this.getItem().isSelected());
        this.mBackground.setContentDescription((CharSequence)this.getItem().getContentDescription(this.mResources));
        this.mText.setText((CharSequence)this.getItem().getText(this.mResources));
        final TextView mText = this.mText;
        int visibility = 0;
        mText.setVisibility(0);
        this.mDescription.setText((CharSequence)this.getItem().getSubText(this.mResources));
        this.mDescription.setVisibility(0);
        final View mSeparator = this.mSeparator;
        if (itemLayoutParams.bottom) {
            visibility = 8;
        }
        mSeparator.setVisibility(visibility);
    }
}
