package com.sonyericsson.android.camera.view.setting.dialogitem;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.view.setting.dialog.SettingAdapter;
import com.sonyericsson.android.camera.view.setting.settingitem.SettingItem;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class SettingButtonDetails extends SettingDialogItem {
    private final View mBackground;
    private final View mContainer;
    private final TextView mDescription;
    private final View.OnClickListener mOnClickListener;
    private final Resources mResources;
    private final View mSeparator;
    private final TextView mText;

    public SettingButtonDetails(Context context, SettingItem settingItem) {
        super(settingItem);
        this.mOnClickListener = new View.OnClickListener() { // from class: com.sonyericsson.android.camera.view.setting.dialogitem.SettingButtonDetails.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (SettingButtonDetails.this.getView().isShown() && SettingButtonDetails.this.getItem().isSelectable()) {
                    SettingButtonDetails.this.select(SettingButtonDetails.this.getItem());
                }
            }
        };
        this.mResources = context.getResources();
        this.mContainer = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.setting_button_details, (ViewGroup) null);
        this.mBackground = this.mContainer.findViewById(R.id.background);
        this.mSeparator = this.mContainer.findViewById(R.id.separator);
        this.mText = (TextView) this.mContainer.findViewById(2131296648);
        this.mDescription = (TextView) this.mContainer.findViewById(R.id.description);
    }

    @Override // com.sonyericsson.android.camera.view.setting.dialogitem.SettingDialogItem
    public void update(ViewGroup viewGroup, SettingAdapter.ItemLayoutParams itemLayoutParams) {
        this.mBackground.setOnClickListener(this.mOnClickListener);
        this.mBackground.setSelected(getItem().isSelected());
        this.mBackground.setContentDescription(getItem().getContentDescription(this.mResources));
        this.mText.setText(getItem().getText(this.mResources));
        this.mText.setVisibility(0);
        this.mDescription.setText(getItem().getSubText(this.mResources));
        this.mDescription.setVisibility(0);
        this.mSeparator.setVisibility(itemLayoutParams.bottom ? 8 : 0);
    }

    @Override // com.sonyericsson.android.camera.view.setting.dialogitem.SettingDialogItem
    public View getView() {
        return this.mContainer;
    }

    @Override // com.sonyericsson.android.camera.view.setting.dialogitem.SettingDialogItem
    public void setClickable(boolean z) {
        this.mBackground.setClickable(z);
    }
}
