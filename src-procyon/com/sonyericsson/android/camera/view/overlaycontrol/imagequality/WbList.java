// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.overlaycontrol.imagequality;

import android.content.res.Resources;
import android.widget.LinearLayout$LayoutParams;
import com.sonyericsson.android.camera.util.CoordinateUtil;
import android.util.DisplayMetrics;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.view.setting.dialog.SettingAdapter;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import com.sonyericsson.android.camera.configuration.parameters.WhiteBalance;
import java.util.Iterator;
import android.view.View;
import android.view.View$OnClickListener;
import android.widget.ImageView$ScaleType;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.sonyericsson.android.camera.view.setting.settingitem.SettingItem;
import android.content.Context;
import com.sonyericsson.android.camera.view.setting.dialogitem.SettingDialogItem;

class WbList extends SettingDialogItem
{
    private final Context mContext;
    private final ViewHolder mHolder;
    
    public WbList(final Context mContext, final SettingItem settingItem) {
        super(settingItem);
        this.mContext = mContext;
        this.mHolder = new ViewHolder();
        this.mHolder.mContainer = (ViewGroup)LayoutInflater.from(mContext).inflate(2131493005, (ViewGroup)null);
        this.mHolder.mList = (LinearLayout)this.mHolder.mContainer.findViewById(2131296452);
    }
    
    private ImageView createIcon(final SettingItem tag) {
        final Context context = this.mHolder.mContainer.getContext();
        final ImageView imageView = new ImageView(context);
        imageView.setTag((Object)tag);
        imageView.setSelected(tag.isSelected());
        imageView.setImageResource(tag.getIconId());
        imageView.setBackgroundResource(2131231544);
        imageView.setScaleType(ImageView$ScaleType.CENTER);
        imageView.setContentDescription((CharSequence)tag.getContentDescription(context.getResources()));
        imageView.setClickable(true);
        imageView.setOnClickListener((View$OnClickListener)new View$OnClickListener(this, tag) {
            final WbList this$0;
            final SettingItem val$item;
            
            public void onClick(final View view) {
                if (this.this$0.getView().isShown()) {
                    this.this$0.updateSelected(this.val$item);
                }
            }
        });
        return imageView;
    }
    
    private void updateSelected(final SettingItem settingItem) {
        for (final SettingItem settingItem2 : this.getItem().getChildren()) {
            if (settingItem2 != settingItem) {
                settingItem2.setSelected(false);
            }
        }
        settingItem.select();
        for (int i = 0; i < this.mHolder.mList.getChildCount(); ++i) {
            final View child = this.mHolder.mList.getChildAt(i);
            if (child.getTag() == settingItem) {
                child.setSelected(true);
            }
            else {
                child.setSelected(false);
            }
        }
    }
    
    @Override
    public View getView() {
        return (View)this.mHolder.mContainer;
    }
    
    @Override
    public void refresh() {
        for (int i = 0; i < this.mHolder.mList.getChildCount(); ++i) {
            final View child = this.mHolder.mList.getChildAt(i);
            if (((SettingItem)child.getTag()).isSelected()) {
                child.setSelected(true);
            }
            else {
                child.setSelected(false);
            }
        }
    }
    
    @Override
    public void reset() {
        this.updateSelected(this.getItem().getChildren().get(WhiteBalance.AUTO.ordinal()));
    }
    
    @Override
    public void setUiOrientation(int i) {
        final float angle = RotationUtil.getAngle(i);
        for (i = 0; i < this.mHolder.mList.getChildCount(); ++i) {
            this.mHolder.mList.getChildAt(i).setRotation(angle);
        }
    }
    
    @Override
    public void update(final ViewGroup viewGroup, final SettingAdapter.ItemLayoutParams itemLayoutParams) {
        if (CamLog.VERBOSE) {
            CamLog.d("update()");
        }
        final Resources resources = this.mContext.getResources();
        final int dimensionPixelSize = resources.getDimensionPixelSize(2131165384);
        final int dimensionPixelSize2 = resources.getDimensionPixelSize(2131165382);
        final int dimensionPixelSize3 = resources.getDimensionPixelSize(2131165383);
        final int n = resources.getDimensionPixelSize(2131165369) - dimensionPixelSize2;
        int height = dimensionPixelSize2;
        int bottomMargin = dimensionPixelSize3;
        int n2 = n;
        int width = dimensionPixelSize;
        if (resources.getDisplayMetrics().densityDpi > DisplayMetrics.DENSITY_DEVICE_STABLE) {
            final float n3 = DisplayMetrics.DENSITY_DEVICE_STABLE * 1.0f / 160.0f;
            width = (int)(CoordinateUtil.convertPx2Dip(this.mContext, dimensionPixelSize) * n3);
            height = (int)(CoordinateUtil.convertPx2Dip(this.mContext, dimensionPixelSize2) * n3);
            bottomMargin = (int)(CoordinateUtil.convertPx2Dip(this.mContext, dimensionPixelSize3) * n3);
            n2 = (int)(CoordinateUtil.convertPx2Dip(this.mContext, n) * n3);
        }
        this.mHolder.mList.removeAllViews();
        for (int i = 0; i < this.getItem().getChildren().size(); ++i) {
            final ImageView icon = this.createIcon(this.getItem().getChildren().get(i));
            this.mHolder.mList.addView((View)icon);
            final LinearLayout$LayoutParams linearLayout$LayoutParams = (LinearLayout$LayoutParams)icon.getLayoutParams();
            linearLayout$LayoutParams.width = width;
            linearLayout$LayoutParams.height = height;
            linearLayout$LayoutParams.setMargins(n2, n2, n2, n2);
            if (i != this.getItem().getChildren().size() - 1) {
                linearLayout$LayoutParams.bottomMargin = bottomMargin;
            }
        }
    }
    
    private static class ViewHolder
    {
        ViewGroup mContainer;
        LinearLayout mList;
    }
}
