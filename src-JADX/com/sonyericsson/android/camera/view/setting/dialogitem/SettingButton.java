package com.sonyericsson.android.camera.view.setting.dialogitem;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.util.SettingUtil;
import com.sonyericsson.android.camera.view.setting.dialog.SettingAdapter;
import com.sonyericsson.android.camera.view.setting.settingitem.SettingItem;
import com.sonyericsson.android.camera.view.setting.settingitem.TypedSettingItem;
import com.sonyericsson.cameracommon.utility.CommonUtility;
import com.sonyericsson.cameracommon.widget.CategorySwitch;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class SettingButton extends SettingDialogItem {
    private static final int DISABLED_FILTER = 2131099706;
    private final ViewHolder mHolder;
    private final boolean mIsDeviceInSecurityLock;
    private final CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener;
    private final View.OnClickListener mOnClickListener;
    private final Resources mResources;

    /* renamed from: com.sonyericsson.android.camera.view.setting.dialogitem.SettingButton$2, reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$sonyericsson$android$camera$view$setting$settingitem$SettingItem$Selectability = new int[SettingItem.Selectability.values().length];

        static {
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$setting$settingitem$SettingItem$Selectability[SettingItem.Selectability.SELECTABLE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$setting$settingitem$SettingItem$Selectability[SettingItem.Selectability.UNSELECTABLE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$setting$settingitem$SettingItem$Selectability[SettingItem.Selectability.RESTRICTED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    private final class SwitchOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        private SwitchOnCheckedChangeListener() {
        }

        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            if (SettingButton.this.getView().isShown()) {
                if (!SettingButton.this.getItem().isSelectable()) {
                    SettingButton.this.mHolder.mBackground.setChecked(false);
                    return;
                }
                if (z) {
                    SettingButton.this.select(SettingButton.this.getOnItem());
                } else {
                    SettingButton.this.select(SettingButton.this.getOffItem());
                }
                SettingButton.this.updateContentDescription(z);
            }
        }
    }

    private static class ViewHolder {
        CategorySwitch mBackground;
        View mContainer;
        ImageView mImage;
        FrameLayout mSwitch;
        TextView mText;
        TextView mValue;

        private ViewHolder() {
        }
    }

    public SettingButton(Context context, SettingItem settingItem, boolean z) {
        super(settingItem);
        this.mOnClickListener = new View.OnClickListener() { // from class: com.sonyericsson.android.camera.view.setting.dialogitem.SettingButton.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (SettingButton.this.getView().isShown()) {
                    switch (AnonymousClass2.$SwitchMap$com$sonyericsson$android$camera$view$setting$settingitem$SettingItem$Selectability[SettingButton.this.getItem().getSelectability().ordinal()]) {
                        case 1:
                            SettingButton.this.select(SettingButton.this.getItem());
                            break;
                        case 3:
                            SettingButton.this.select(SettingButton.this.getItem());
                            break;
                    }
                }
            }
        };
        this.mOnCheckedChangeListener = new SwitchOnCheckedChangeListener();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.mHolder = new ViewHolder();
        this.mHolder.mContainer = layoutInflater.inflate(R.layout.setting_dialog_item, (ViewGroup) null);
        this.mResources = this.mHolder.mContainer.getContext().getResources();
        this.mHolder.mBackground = (CategorySwitch) this.mHolder.mContainer.findViewById(R.id.background);
        this.mHolder.mImage = (ImageView) this.mHolder.mContainer.findViewById(2131296423);
        this.mHolder.mText = (TextView) this.mHolder.mContainer.findViewById(2131296648);
        this.mHolder.mValue = (TextView) this.mHolder.mContainer.findViewById(R.id.value);
        this.mHolder.mSwitch = (FrameLayout) this.mHolder.mContainer.findViewById(R.id.switch_bundle);
        this.mHolder.mText.setTextSize(1, 16.0f);
        this.mHolder.mValue.setTextSize(1, 14.0f);
        this.mIsDeviceInSecurityLock = z;
    }

    @Override // com.sonyericsson.android.camera.view.setting.dialogitem.SettingDialogItem
    public void update(ViewGroup viewGroup, SettingAdapter.ItemLayoutParams itemLayoutParams) {
        if (getItem().getSelectability() != SettingItem.Selectability.RESTRICTED) {
            switch (getItem().getDialogItemType()) {
                case 1:
                    changeToButtonFormat(viewGroup, itemLayoutParams);
                    break;
                case 2:
                    changeToValueButtonFormat(viewGroup, itemLayoutParams);
                    break;
                case 3:
                    changeToCategoryButtonFormat(viewGroup, itemLayoutParams);
                    break;
                case 4:
                    changeToCategorySwitchFormat(viewGroup, itemLayoutParams);
                    break;
            }
            setTextColorGrayOrNot();
            setIconColorGrayOrNot();
        } else {
            changeToRestrictFormat(viewGroup, itemLayoutParams);
        }
        if (CommonUtility.isMirroringRequired(this.mHolder.mContainer.getContext())) {
            this.mHolder.mText.setGravity(21);
            this.mHolder.mValue.setGravity(19);
        } else {
            this.mHolder.mText.setGravity(19);
            this.mHolder.mValue.setGravity(21);
        }
    }

    @Override // com.sonyericsson.android.camera.view.setting.dialogitem.SettingDialogItem
    public View getView() {
        return this.mHolder.mContainer;
    }

    private SettingItem getSelectedItem() {
        for (SettingItem settingItem : getItem().getChildren()) {
            if (settingItem.isSelectable() && settingItem.isSelected()) {
                return settingItem;
            }
        }
        return null;
    }

    private void setTextColorGrayOrNot() {
        if (getItem().isSelectable()) {
            this.mHolder.mText.setTextColor(this.mResources.getColor(R.color.default_text_col));
            this.mHolder.mValue.setTextColor(this.mResources.getColor(R.color.setting_menu_value_color));
        } else {
            this.mHolder.mText.setTextColor(this.mResources.getColor(R.color.grayout_text_col));
            this.mHolder.mValue.setTextColor(this.mResources.getColor(R.color.setting_menu_value_grayout_color));
        }
    }

    private void setIconColorGrayOrNot() {
        if (getItem().isSelectable()) {
            this.mHolder.mImage.clearColorFilter();
        } else {
            this.mHolder.mImage.setColorFilter(2131099706);
        }
    }

    private void changeToButtonFormat(ViewGroup viewGroup, SettingAdapter.ItemLayoutParams itemLayoutParams) {
        setText();
        setImage();
        this.mHolder.mBackground.setOnClickListener(this.mOnClickListener);
        this.mHolder.mBackground.setSelected(getItem().isSelected());
        this.mHolder.mBackground.setContentDescription(getItem().getContentDescription(this.mResources));
    }

    private void changeToCategoryButtonFormat(ViewGroup viewGroup, SettingAdapter.ItemLayoutParams itemLayoutParams) {
        int i;
        setText();
        setImage();
        setValue();
        if (this.mHolder.mImage.getVisibility() == 0) {
            i = this.mHolder.mValue.getVisibility() == 0 ? R.dimen.setting_menu_category_max_width_with_value_and_icon : R.dimen.setting_menu_category_max_width_with_icon;
        } else {
            i = this.mHolder.mValue.getVisibility() == 0 ? R.dimen.setting_menu_category_max_width_with_value : R.dimen.setting_menu_category_max_width;
        }
        this.mHolder.mText.setMaxWidth(this.mResources.getDimensionPixelSize(i));
        this.mHolder.mBackground.setOnClickListener(this.mOnClickListener);
        this.mHolder.mBackground.setContentDescription(getItem().getContentDescription(this.mResources));
    }

    private void changeToValueButtonFormat(ViewGroup viewGroup, SettingAdapter.ItemLayoutParams itemLayoutParams) {
        setText();
        setImage();
        this.mHolder.mBackground.setOnClickListener(this.mOnClickListener);
        this.mHolder.mBackground.setSelected(getItem().isSelected());
        this.mHolder.mBackground.setContentDescription(getItem().getContentDescription(this.mResources));
    }

    private void changeToCategorySwitchFormat(ViewGroup viewGroup, SettingAdapter.ItemLayoutParams itemLayoutParams) {
        setText();
        setImage();
        this.mHolder.mText.setMaxWidth(this.mResources.getDimensionPixelSize(this.mHolder.mImage.getVisibility() == 0 ? R.dimen.setting_menu_category_max_width_with_switch_and_icon : R.dimen.setting_menu_category_max_width_with_switch));
        updateContentDescription(getOnItem().isSelected());
        this.mHolder.mBackground.setOnCheckedChangeListener(null);
        this.mHolder.mBackground.setChecked(getOnItem().isSelected());
        if (this.mIsDeviceInSecurityLock && (getItem().compareData(UserSettingKey.GEO_TAG) || (!SettingUtil.isSideSenseEnabled(true) && getItem().compareData(UserSettingKey.SIDE_SENSE)))) {
            this.mHolder.mBackground.setOnClickListener(this.mOnClickListener);
        } else {
            this.mHolder.mBackground.setOnCheckedChangeListener(this.mOnCheckedChangeListener);
        }
        this.mHolder.mSwitch.setVisibility(0);
    }

    private void changeToRestrictFormat(ViewGroup viewGroup, SettingAdapter.ItemLayoutParams itemLayoutParams) {
        setText();
        setImage();
        if (getSelectedItem() == null) {
            this.mHolder.mValue.setText(R.string.cam_strings_restricted_setting_hyphen_txt);
        } else {
            setValue();
        }
        this.mHolder.mValue.setVisibility(0);
        this.mHolder.mBackground.setOnClickListener(this.mOnClickListener);
        this.mHolder.mBackground.setSelected(getItem().isSelected());
        this.mHolder.mBackground.setContentDescription(getItem().getContentDescription(this.mResources));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public SettingItem getOnItem() {
        return getItem().getChildren().get(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public SettingItem getOffItem() {
        return getItem().getChildren().get(1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateContentDescription(boolean z) {
        String str;
        String text = getItem().getText(this.mResources);
        if (z) {
            str = text + " " + getOnItem().getContentDescription(this.mResources);
        } else {
            str = text + " " + getOffItem().getContentDescription(this.mResources);
        }
        this.mHolder.mContainer.setContentDescription(str);
    }

    private void setText() {
        this.mHolder.mText.setText(getItem().getText(this.mResources));
        this.mHolder.mText.setVisibility(0);
    }

    private void setImage() {
        if (getItem().getIconId() != -1) {
            this.mHolder.mImage.setImageResource(getItem().getIconId());
            this.mHolder.mImage.setVisibility(0);
        } else {
            this.mHolder.mImage.setVisibility(8);
        }
    }

    private void setValue() {
        SettingItem selectedItem = getSelectedItem();
        if (selectedItem != null) {
            this.mHolder.mValue.setText(selectedItem.getText(this.mResources));
            this.mHolder.mValue.setVisibility(0);
            return;
        }
        SettingItem item = getItem();
        if (item instanceof TypedSettingItem) {
            String valueText = ((TypedSettingItem) item).getValueText();
            if (valueText != null && valueText.length() > 0) {
                this.mHolder.mValue.setText(valueText);
                this.mHolder.mValue.setVisibility(0);
                return;
            } else {
                this.mHolder.mValue.setVisibility(8);
                return;
            }
        }
        this.mHolder.mValue.setVisibility(8);
    }

    @Override // com.sonyericsson.android.camera.view.setting.dialogitem.SettingDialogItem
    public void setClickable(boolean z) {
        this.mHolder.mBackground.setClickable(z);
    }
}
