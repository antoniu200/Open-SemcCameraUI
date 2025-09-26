package com.sonyericsson.android.camera.view.overlaycontrol.imagequality;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.parameters.WhiteBalance;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.CoordinateUtil;
import com.sonyericsson.android.camera.view.setting.dialog.SettingAdapter;
import com.sonyericsson.android.camera.view.setting.dialogitem.SettingDialogItem;
import com.sonyericsson.android.camera.view.setting.settingitem.SettingItem;
import com.sonyericsson.cameracommon.utility.RotationUtil;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class WbList extends SettingDialogItem {
    private final Context mContext;
    private final ViewHolder mHolder;

    private static class ViewHolder {
        ViewGroup mContainer;
        LinearLayout mList;

        private ViewHolder() {
        }
    }

    public WbList(Context context, SettingItem settingItem) {
        super(settingItem);
        this.mContext = context;
        this.mHolder = new ViewHolder();
        this.mHolder.mContainer = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.setting_item_icon_list, (ViewGroup) null);
        this.mHolder.mList = (LinearLayout) this.mHolder.mContainer.findViewById(R.id.list);
    }

    @Override // com.sonyericsson.android.camera.view.setting.dialogitem.SettingDialogItem
    public void update(ViewGroup viewGroup, SettingAdapter.ItemLayoutParams itemLayoutParams) throws Resources.NotFoundException {
        if (CamLog.VERBOSE) {
            CamLog.d("update()");
        }
        Resources resources = this.mContext.getResources();
        int dimensionPixelSize = resources.getDimensionPixelSize(R.dimen.image_quality_control_wb_icon_width);
        int dimensionPixelSize2 = resources.getDimensionPixelSize(R.dimen.image_quality_control_wb_icon_height);
        int dimensionPixelSize3 = resources.getDimensionPixelSize(R.dimen.image_quality_control_wb_icon_margin);
        int dimensionPixelSize4 = resources.getDimensionPixelSize(R.dimen.image_quality_control_ev_wb_height) - dimensionPixelSize2;
        if (resources.getDisplayMetrics().densityDpi > DisplayMetrics.DENSITY_DEVICE_STABLE) {
            float f = (DisplayMetrics.DENSITY_DEVICE_STABLE * 1.0f) / 160.0f;
            dimensionPixelSize = (int) (CoordinateUtil.convertPx2Dip(this.mContext, dimensionPixelSize) * f);
            dimensionPixelSize2 = (int) (CoordinateUtil.convertPx2Dip(this.mContext, dimensionPixelSize2) * f);
            dimensionPixelSize3 = (int) (CoordinateUtil.convertPx2Dip(this.mContext, dimensionPixelSize3) * f);
            dimensionPixelSize4 = (int) (CoordinateUtil.convertPx2Dip(this.mContext, dimensionPixelSize4) * f);
        }
        this.mHolder.mList.removeAllViews();
        for (int i = 0; i < getItem().getChildren().size(); i++) {
            ImageView imageViewCreateIcon = createIcon(getItem().getChildren().get(i));
            this.mHolder.mList.addView(imageViewCreateIcon);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageViewCreateIcon.getLayoutParams();
            layoutParams.width = dimensionPixelSize;
            layoutParams.height = dimensionPixelSize2;
            layoutParams.setMargins(dimensionPixelSize4, dimensionPixelSize4, dimensionPixelSize4, dimensionPixelSize4);
            if (i != getItem().getChildren().size() - 1) {
                layoutParams.bottomMargin = dimensionPixelSize3;
            }
        }
    }

    @Override // com.sonyericsson.android.camera.view.setting.dialogitem.SettingDialogItem
    public View getView() {
        return this.mHolder.mContainer;
    }

    @Override // com.sonyericsson.android.camera.view.setting.dialogitem.SettingDialogItem
    public void setUiOrientation(int i) {
        float angle = RotationUtil.getAngle(i);
        for (int i2 = 0; i2 < this.mHolder.mList.getChildCount(); i2++) {
            this.mHolder.mList.getChildAt(i2).setRotation(angle);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSelected(SettingItem settingItem) {
        for (SettingItem settingItem2 : getItem().getChildren()) {
            if (settingItem2 != settingItem) {
                settingItem2.setSelected(false);
            }
        }
        settingItem.select();
        for (int i = 0; i < this.mHolder.mList.getChildCount(); i++) {
            View childAt = this.mHolder.mList.getChildAt(i);
            if (childAt.getTag() == settingItem) {
                childAt.setSelected(true);
            } else {
                childAt.setSelected(false);
            }
        }
    }

    private ImageView createIcon(final SettingItem settingItem) {
        Context context = this.mHolder.mContainer.getContext();
        ImageView imageView = new ImageView(context);
        imageView.setTag(settingItem);
        imageView.setSelected(settingItem.isSelected());
        imageView.setImageResource(settingItem.getIconId());
        imageView.setBackgroundResource(R.drawable.setting_item_icon_selector);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setContentDescription(settingItem.getContentDescription(context.getResources()));
        imageView.setClickable(true);
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.sonyericsson.android.camera.view.overlaycontrol.imagequality.WbList.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (WbList.this.getView().isShown()) {
                    WbList.this.updateSelected(settingItem);
                }
            }
        });
        return imageView;
    }

    @Override // com.sonyericsson.android.camera.view.setting.dialogitem.SettingDialogItem
    public void reset() {
        updateSelected(getItem().getChildren().get(WhiteBalance.AUTO.ordinal()));
    }

    @Override // com.sonyericsson.android.camera.view.setting.dialogitem.SettingDialogItem
    public void refresh() {
        for (int i = 0; i < this.mHolder.mList.getChildCount(); i++) {
            View childAt = this.mHolder.mList.getChildAt(i);
            if (((SettingItem) childAt.getTag()).isSelected()) {
                childAt.setSelected(true);
            } else {
                childAt.setSelected(false);
            }
        }
    }
}
