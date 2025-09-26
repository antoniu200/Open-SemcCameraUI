// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.overlaycontrol.imagequality;

import java.util.Iterator;
import com.sonyericsson.android.camera.view.setting.settingitem.TypedSettingItem;
import com.sonyericsson.android.camera.view.setting.dialog.SettingAdapter;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import com.sonymobile.cameracommon.font.FontUtil;
import android.view.ViewGroup$LayoutParams;
import com.sonyericsson.android.camera.util.CoordinateUtil;
import android.widget.FrameLayout$LayoutParams;
import android.util.DisplayMetrics;
import android.widget.TextView;
import android.view.View$OnClickListener;
import android.widget.ImageView;
import android.view.MotionEvent;
import android.view.View;
import android.view.View$OnTouchListener;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import com.sonyericsson.android.camera.util.CamLog;
import android.widget.FrameLayout;
import com.sonyericsson.android.camera.view.setting.settingitem.SettingItem;
import android.content.Context;
import com.sonyericsson.android.camera.view.setting.dialogitem.SettingDialogItem;

abstract class SettingItemSlider extends SettingDialogItem
{
    private int mBottomPadding;
    private final Context mContext;
    private int mCurrentPosition;
    private final ViewHolder mHolder;
    private final OnSlideListener mOnSlideListener;
    private final boolean mShowAutoSettingItemAsButton;
    private final boolean mShowMaxMinValue;
    private int mTopPadding;
    
    public SettingItemSlider(final Context mContext, final SettingItem settingItem, final boolean mShowAutoSettingItemAsButton, final boolean mShowMaxMinValue, final OnSlideListener mOnSlideListener) {
        super(settingItem);
        this.mTopPadding = -1;
        this.mBottomPadding = -1;
        this.mCurrentPosition = -1;
        this.mContext = mContext;
        this.mShowAutoSettingItemAsButton = mShowAutoSettingItemAsButton;
        this.mShowMaxMinValue = mShowMaxMinValue;
        this.mOnSlideListener = mOnSlideListener;
        this.mHolder = new ViewHolder();
        this.mHolder.mContainer = (ViewGroup)new FrameLayout(this, mContext) {
            final SettingItemSlider this$0;
            
            public void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
                super.onLayout(b, n, n2, n3, n4);
                if (CamLog.VERBOSE) {
                    CamLog.d(this.this$0.getTag(), "onLayout(): refresh");
                }
                this.this$0.refresh();
            }
        };
        final View inflate = LayoutInflater.from(mContext).inflate(2131493006, (ViewGroup)null);
        this.mHolder.mContainer.addView(inflate);
        (this.mHolder.mBackground = inflate.findViewById(2131296316)).setOnTouchListener((View$OnTouchListener)new View$OnTouchListener(this) {
            final SettingItemSlider this$0;
            
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case 2: {
                        if (view.isPressed() && view.isShown()) {
                            this.this$0.update(motionEvent.getY());
                            break;
                        }
                        break;
                    }
                    case 1: {
                        if (view.isPressed() && view.isShown()) {
                            this.this$0.update(motionEvent.getY());
                            this.this$0.mOnSlideListener.onSlideStopped();
                        }
                        view.setPressed(false);
                        break;
                    }
                    case 0: {
                        view.setPressed(true);
                        this.this$0.mOnSlideListener.onSlideStarted();
                        this.this$0.update(motionEvent.getY());
                        return true;
                    }
                }
                return false;
            }
        });
        (this.mHolder.mIndicator = (ImageView)inflate.findViewById(2131296429)).setVisibility(0);
        if (this.mShowAutoSettingItemAsButton) {
            (this.mHolder.mAutoButton = (ImageView)inflate.findViewById(2131296296)).setVisibility(0);
            this.mHolder.mAutoButton.setOnClickListener((View$OnClickListener)new View$OnClickListener(this) {
                final SettingItemSlider this$0;
                
                public void onClick(final View view) {
                    final boolean selected = view.isSelected() ^ true;
                    this.this$0.onAutoCheckedChanged(selected);
                    view.setSelected(selected);
                }
            });
        }
        if (this.mShowMaxMinValue) {
            this.mHolder.mMaxValue = (TextView)inflate.findViewById(2131296461);
            this.mHolder.mMinValue = (TextView)inflate.findViewById(2131296466);
            if (this.mContext.getResources().getDisplayMetrics().densityDpi > DisplayMetrics.DENSITY_DEVICE_STABLE) {
                final float n = DisplayMetrics.DENSITY_DEVICE_STABLE * 1.0f / 160.0f;
                final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.mHolder.mMaxValue.getLayoutParams();
                final FrameLayout$LayoutParams layoutParams2 = (FrameLayout$LayoutParams)this.mHolder.mMinValue.getLayoutParams();
                final int n2 = (int)(CoordinateUtil.convertPx2Dip(this.mContext, layoutParams.leftMargin) * n);
                final int n3 = (int)(CoordinateUtil.convertPx2Dip(this.mContext, layoutParams2.leftMargin) * n);
                layoutParams.setMargins(0, n2, 0, 0);
                layoutParams2.setMargins(0, 0, 0, n3);
                layoutParams.height = (int)(CoordinateUtil.convertPx2Dip(this.mContext, layoutParams.height) * n);
                layoutParams2.height = (int)(CoordinateUtil.convertPx2Dip(this.mContext, layoutParams2.height) * n);
                this.mHolder.mMaxValue.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
                this.mHolder.mMinValue.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
            }
            FontUtil.setRobotoFont(this.mHolder.mMaxValue, FontUtil.RobotoFontType.MEDIUM);
            FontUtil.setRobotoFont(this.mHolder.mMinValue, FontUtil.RobotoFontType.MEDIUM);
            this.mHolder.mMaxValue.setText((CharSequence)this.getMaxValue());
            this.mHolder.mMinValue.setText((CharSequence)this.getMinValue());
            this.mHolder.mMaxValue.setVisibility(0);
            this.mHolder.mMinValue.setVisibility(0);
        }
    }
    
    private String getTag() {
        return this.getClass().getSimpleName();
    }
    
    protected String getAutoButtonContentDescription(final boolean b) {
        int n;
        if (b) {
            n = 2131689576;
        }
        else {
            n = 2131689575;
        }
        return this.getString(n);
    }
    
    protected int getAutoSettingItemPosition() {
        if (!this.mShowAutoSettingItemAsButton) {
            throw new IllegalStateException("This method should not be called when auto is unsupported");
        }
        return this.getMemoryStepCount() + 1;
    }
    
    protected abstract int getBackgroundImageResource(final int p0);
    
    protected int getBottomPadding() {
        if (this.mBottomPadding == -1) {
            if (!this.mShowAutoSettingItemAsButton) {
                this.mBottomPadding = this.getDimension(2131165629);
            }
            else {
                this.mBottomPadding = this.getDimension(2131165628);
            }
            if (this.mContext.getResources().getDisplayMetrics().densityDpi > DisplayMetrics.DENSITY_DEVICE_STABLE) {
                this.mBottomPadding = (int)(CoordinateUtil.convertPx2Dip(this.mContext, this.mBottomPadding) * (DisplayMetrics.DENSITY_DEVICE_STABLE * 1.0f / 160.0f));
            }
        }
        return this.mBottomPadding;
    }
    
    public Context getContext() {
        return this.mContext;
    }
    
    public int getCurrentPosition() {
        return this.mCurrentPosition;
    }
    
    protected int getDefaultSettingItemPosition() {
        return 0;
    }
    
    protected int getDimension(final int n) {
        return this.mContext.getResources().getDimensionPixelSize(n);
    }
    
    protected String getIndicatorContentDescription(int index) {
        index = this.indexOf(index);
        return this.getItem().getChildren().get(index).getContentDescription(this.mContext.getResources());
    }
    
    protected int getIndicatorImageResource(final int n) {
        if (!this.mShowAutoSettingItemAsButton && n == this.getDefaultSettingItemPosition()) {
            return 2131231541;
        }
        return 2131231540;
    }
    
    protected abstract String getMaxValue();
    
    protected int getMemoryStepCount() {
        final int size = this.getItem().getChildren().size();
        final boolean mShowAutoSettingItemAsButton = this.mShowAutoSettingItemAsButton;
        int n = 1;
        if (mShowAutoSettingItemAsButton) {
            if (size <= 2) {
                throw new IllegalStateException("This method should be overwritten for your use case");
            }
        }
        else if (size <= 1) {
            throw new IllegalStateException("This method should be overwritten for your use case");
        }
        if (this.mShowAutoSettingItemAsButton) {
            n = 2;
        }
        return size - n;
    }
    
    protected float getMemoryStepSize() {
        return (this.mHolder.mBackground.getMeasuredHeight() - this.getTopPadding() - this.getBottomPadding()) / (float)this.getMemoryStepCount();
    }
    
    protected abstract String getMinValue();
    
    protected int getSelectedSettingItemPosition() {
        final int memoryStepCount = this.getMemoryStepCount();
        int n;
        if (this.mShowAutoSettingItemAsButton) {
            n = 2;
        }
        else {
            n = 1;
        }
        for (int i = 0; i < memoryStepCount + n; ++i) {
            final int index = this.indexOf(i);
            if (this.getItem().getChildren().get(index).isSelected()) {
                if (CamLog.VERBOSE) {
                    final String tag = this.getTag();
                    final StringBuilder sb = new StringBuilder();
                    sb.append("getSelectedSettingItemPosition: position = ");
                    sb.append(i);
                    CamLog.d(tag, sb.toString());
                    final String tag2 = this.getTag();
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("getSelectedSettingItemPosition:    index = ");
                    sb2.append(index);
                    CamLog.d(tag2, sb2.toString());
                }
                return i;
            }
        }
        if (CamLog.VERBOSE) {
            CamLog.d(this.getTag(), "getSelectedSettingItemPosition: position = 0");
            final String tag3 = this.getTag();
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("getSelectedSettingItemPosition:    index = ");
            sb3.append(this.indexOf(0));
            CamLog.d(tag3, sb3.toString());
        }
        return 0;
    }
    
    protected String getString(final int n) {
        return this.mContext.getResources().getString(n);
    }
    
    protected int getTopPadding() {
        if (this.mTopPadding == -1) {
            this.mTopPadding = this.getDimension(2131165630);
            if (this.mContext.getResources().getDisplayMetrics().densityDpi > DisplayMetrics.DENSITY_DEVICE_STABLE) {
                this.mTopPadding = (int)(CoordinateUtil.convertPx2Dip(this.mContext, this.mTopPadding) * (DisplayMetrics.DENSITY_DEVICE_STABLE * 1.0f / 160.0f));
            }
        }
        return this.mTopPadding;
    }
    
    @Override
    public View getView() {
        return (View)this.mHolder.mContainer;
    }
    
    protected int indexOf(final int n) {
        if (n >= 0 && n < this.getItem().getChildren().size()) {
            return this.getItem().getChildren().size() - 1 - n;
        }
        throw new IllegalStateException("This method should be overwritten for your use case");
    }
    
    protected void onAutoCheckedChanged(final boolean b) {
        int n;
        if (b) {
            n = this.getAutoSettingItemPosition();
        }
        else {
            n = this.getDefaultSettingItemPosition();
        }
        this.updateIndicator(n, true);
    }
    
    @Override
    public void refresh() {
        final int selectedSettingItemPosition = this.getSelectedSettingItemPosition();
        if (CamLog.VERBOSE) {
            final String tag = this.getTag();
            final StringBuilder sb = new StringBuilder();
            sb.append("refresh: position = ");
            sb.append(selectedSettingItemPosition);
            CamLog.d(tag, sb.toString());
        }
        this.updateIndicator(selectedSettingItemPosition, false);
    }
    
    @Override
    public void reset() {
        int n;
        if (this.mShowAutoSettingItemAsButton) {
            n = this.getAutoSettingItemPosition();
        }
        else {
            n = this.getDefaultSettingItemPosition();
        }
        this.updateIndicator(n, true);
    }
    
    @Override
    public void setUiOrientation(final int n) {
        this.mHolder.mBackground.setBackgroundResource(this.getBackgroundImageResource(n));
        final float angle = RotationUtil.getAngle(n);
        if (this.mShowAutoSettingItemAsButton) {
            this.mHolder.mAutoButton.setRotation(angle);
        }
        if (this.mShowMaxMinValue) {
            this.mHolder.mMaxValue.setRotation(angle);
            this.mHolder.mMinValue.setRotation(angle);
        }
    }
    
    protected void update(final float f) {
        if (CamLog.VERBOSE) {
            final String tag = this.getTag();
            final StringBuilder sb = new StringBuilder();
            sb.append("update: y = ");
            sb.append(f);
            CamLog.d(tag, sb.toString());
        }
        this.updateIndicator(Math.min(Math.max((int)((f - this.getTopPadding()) / this.getMemoryStepSize()), 0), this.getMemoryStepCount()), true);
    }
    
    @Override
    public void update(final ViewGroup viewGroup, final SettingAdapter.ItemLayoutParams itemLayoutParams) {
        this.refresh();
    }
    
    protected void updateIndicator(final int n, final boolean b) {
        if (CamLog.VERBOSE) {
            final String tag = this.getTag();
            final StringBuilder sb = new StringBuilder();
            sb.append("updateIndicator: position = ");
            sb.append(n);
            CamLog.d(tag, sb.toString());
        }
        if (n >= 0 && n <= this.getMemoryStepCount()) {
            this.mHolder.mIndicator.setVisibility(0);
            this.mHolder.mIndicator.setY(this.getMemoryStepSize() * n + this.getTopPadding() - this.mHolder.mIndicator.getMeasuredHeight() / 2.0f);
            if (CamLog.VERBOSE) {
                final String tag2 = this.getTag();
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("updateIndicator: setY = ");
                sb2.append(this.mHolder.mIndicator.getY());
                CamLog.d(tag2, sb2.toString());
            }
            this.mHolder.mIndicator.setImageResource(this.getIndicatorImageResource(n));
            this.mHolder.mIndicator.setContentDescription((CharSequence)this.getIndicatorContentDescription(n));
        }
        if (this.mShowAutoSettingItemAsButton) {
            if (n == this.getAutoSettingItemPosition()) {
                this.mHolder.mIndicator.setVisibility(4);
                this.mHolder.mAutoButton.setSelected(true);
                this.mHolder.mAutoButton.setContentDescription((CharSequence)this.getAutoButtonContentDescription(true));
            }
            else {
                this.mHolder.mAutoButton.setSelected(false);
                this.mHolder.mAutoButton.setContentDescription((CharSequence)this.getAutoButtonContentDescription(false));
            }
        }
        if (n != this.mCurrentPosition) {
            this.updateSelectedSettingItem(n, b);
            this.mCurrentPosition = n;
        }
    }
    
    protected void updateSelectedSettingItem(final int i, final boolean b) {
        final Iterator<SettingItem> iterator = this.getItem().getChildren().iterator();
        while (iterator.hasNext()) {
            iterator.next().setSelected(false);
        }
        final int index = this.indexOf(i);
        if (CamLog.VERBOSE) {
            final String tag = this.getTag();
            final StringBuilder sb = new StringBuilder();
            sb.append("updateSelectedSettingItem: position = ");
            sb.append(i);
            CamLog.d(tag, sb.toString());
            final String tag2 = this.getTag();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("updateSelectedSettingItem:    index = ");
            sb2.append(index);
            CamLog.d(tag2, sb2.toString());
        }
        final SettingItem settingItem = this.getItem().getChildren().get(index);
        settingItem.setSelected(true);
        if (b) {
            if (CamLog.VERBOSE) {
                final TypedSettingItem typedSettingItem = (TypedSettingItem)settingItem;
                final String tag3 = this.getTag();
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("updateSelectedSettingItem: apply ");
                sb3.append(typedSettingItem.getData());
                CamLog.d(tag3, sb3.toString());
            }
            settingItem.select();
        }
    }
    
    private static final class ViewHolder
    {
        ImageView mAutoButton;
        View mBackground;
        ViewGroup mContainer;
        ImageView mIndicator;
        TextView mMaxValue;
        TextView mMinValue;
    }
}
