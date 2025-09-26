package com.sonyericsson.android.camera.view.overlaycontrol.imagequality;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.configuration.parameters.WhiteBalance;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.CoordinateUtil;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import com.sonyericsson.android.camera.view.overlaycontrol.EnumValueAccessor;
import com.sonyericsson.android.camera.view.setting.SettingUi;
import com.sonyericsson.android.camera.view.setting.dialog.SettingAdapter;
import com.sonyericsson.android.camera.view.setting.dialogitem.SettingDialogItem;
import com.sonyericsson.android.camera.view.setting.settingitem.SettingItem;
import com.sonyericsson.android.camera.view.setting.settingitem.TypedSettingItem;
import java.util.Iterator;
import java.util.Map;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ImageQualityControlView extends SettingDialog {
    private SettingAdapter mAdapter;
    private final Context mContext;
    private OnImageQualityControlDialogTabSelectListener mListener;
    private View mResetButton;
    private LinearLayout mTabContainer;
    private LinearLayout mWidgetContainer;

    public interface OnImageQualityControlDialogTabSelectListener {
        void onSelect(UserSettingKey userSettingKey);
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.imagequality.SettingDialog, com.sonyericsson.android.camera.view.setting.dialog.SettingDialogInterface
    public /* bridge */ /* synthetic */ void close() {
        super.close();
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.imagequality.SettingDialog, com.sonyericsson.android.camera.view.setting.dialog.SettingDialogInterface
    public /* bridge */ /* synthetic */ boolean hitTest(int i, int i2) {
        return super.hitTest(i, i2);
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.imagequality.SettingDialog, android.view.ViewGroup
    public /* bridge */ /* synthetic */ boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return super.onInterceptTouchEvent(motionEvent);
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.imagequality.SettingDialog, com.sonyericsson.android.camera.view.setting.dialog.SettingDialogInterface
    public /* bridge */ /* synthetic */ void open(ViewGroup viewGroup) {
        super.open(viewGroup);
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.imagequality.SettingDialog
    public /* bridge */ /* synthetic */ void open(ViewGroup viewGroup, boolean z) {
        super.open(viewGroup, z);
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.imagequality.SettingDialog
    public /* bridge */ /* synthetic */ void setLayoutCoordinator(LayoutCoordinator layoutCoordinator) {
        super.setLayoutCoordinator(layoutCoordinator);
    }

    public static ImageQualityControlView create(ViewGroup viewGroup, Rect rect, LayoutDependencyResolver.ScreenAspect screenAspect) {
        ImageQualityControlView imageQualityControlView = (ImageQualityControlView) ((LayoutInflater) viewGroup.getContext().getSystemService("layout_inflater")).inflate(R.layout.setting_dialog_image_quality_control, (ViewGroup) null);
        imageQualityControlView.setLayoutCoordinator(new ControlLayoutCoordinator(imageQualityControlView, new Rect(0, 0, rect.height(), rect.width()), screenAspect));
        viewGroup.addView(imageQualityControlView);
        imageQualityControlView.setVisibility(4);
        return imageQualityControlView;
    }

    public ImageQualityControlView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        setWillNotDraw(false);
        this.mTabContainer = (LinearLayout) findViewById(R.id.tab_container);
        this.mWidgetContainer = (LinearLayout) findViewById(R.id.widget_container);
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.imagequality.SettingDialog, com.sonyericsson.android.camera.view.setting.dialog.SettingDialogInterface
    public void setSensorOrientation(int i) {
        requestLayout();
        for (int i2 = 0; i2 < this.mTabContainer.getChildCount(); i2++) {
            View childAt = this.mTabContainer.getChildAt(i2);
            if (childAt instanceof ImageQualityControlTab) {
                ((ImageQualityControlTab) childAt).setUiOrientation(i);
            } else if (childAt instanceof ImageQualityControlResetButton) {
                ((ImageQualityControlResetButton) childAt).setUiOrientation(i);
            }
        }
        for (int i3 = 0; i3 < this.mWidgetContainer.getChildCount(); i3++) {
            Object tag = this.mWidgetContainer.getChildAt(i3).getTag();
            if (tag instanceof SettingDialogItem) {
                ((SettingDialogItem) tag).setUiOrientation(i);
            }
        }
        super.setSensorOrientation(i);
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.imagequality.SettingDialog
    public void setAdapter(SettingAdapter settingAdapter) {
        this.mAdapter = settingAdapter;
        this.mTabContainer.removeAllViews();
        this.mWidgetContainer.removeAllViews();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.gravity = 17;
        layoutParams.width = getDimensionPixelSize(R.dimen.image_quality_control_tab_width);
        layoutParams.height = getDimensionPixelSize(R.dimen.image_quality_control_tab_height);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-2, -2);
        layoutParams2.gravity = 17;
        layoutParams2.width = getDimensionPixelSize(R.dimen.image_quality_control_widget_width);
        layoutParams2.height = getDimensionPixelSize(R.dimen.image_quality_control_widget_height);
        if (getResources().getDisplayMetrics().densityDpi > DisplayMetrics.DENSITY_DEVICE_STABLE) {
            float f = (DisplayMetrics.DENSITY_DEVICE_STABLE * 1.0f) / 160.0f;
            layoutParams.height = (int) (CoordinateUtil.convertPx2Dip(this.mContext, layoutParams.height) * f);
            layoutParams2.width = (int) (CoordinateUtil.convertPx2Dip(this.mContext, layoutParams2.width) * f);
            layoutParams2.height = (int) (CoordinateUtil.convertPx2Dip(this.mContext, layoutParams2.height) * f);
        }
        LayoutInflater layoutInflaterFrom = LayoutInflater.from(this.mContext);
        for (int i = 0; i < this.mAdapter.getCount(); i++) {
            UserSettingKey userSettingKey = (UserSettingKey) ((TypedSettingItem) this.mAdapter.getItem(i)).getData();
            View viewInflate = layoutInflaterFrom.inflate(R.layout.setting_tab_image_quality_control, (ViewGroup) null);
            viewInflate.setClickable(true);
            viewInflate.setTag(userSettingKey);
            viewInflate.setOnClickListener(new View.OnClickListener() { // from class: com.sonyericsson.android.camera.view.overlaycontrol.imagequality.ImageQualityControlView.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    ImageQualityControlView.this.mListener.onSelect((UserSettingKey) view.getTag());
                }
            });
            viewInflate.setBackgroundResource(R.drawable.setting_tab_selector);
            setTabIcon((ImageQualityControlTab) viewInflate, userSettingKey);
            this.mTabContainer.addView(viewInflate, i, layoutParams);
            View view = this.mAdapter.getView(i, null, this.mWidgetContainer);
            view.setVisibility(8);
            this.mWidgetContainer.addView(view, layoutParams2);
        }
        this.mResetButton = layoutInflaterFrom.inflate(R.layout.setting_reset_button_image_quality_control, (ViewGroup) null);
        this.mResetButton.setClickable(true);
        this.mResetButton.setOnClickListener(new View.OnClickListener() { // from class: com.sonyericsson.android.camera.view.overlaycontrol.imagequality.ImageQualityControlView.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ImageQualityControlView.this.onResetButtonClicked();
            }
        });
        this.mTabContainer.addView(this.mResetButton, layoutParams);
    }

    private void setTabIcon(ImageQualityControlTab imageQualityControlTab, UserSettingKey userSettingKey) {
        imageQualityControlTab.setIcon(getImageQualityControlTabIcon(userSettingKey));
    }

    private int getImageQualityControlTabIcon(UserSettingKey userSettingKey) {
        switch (userSettingKey) {
            case WHITE_BALANCE:
                return R.drawable.cam_core_image_quality_control_tab_white_balance_icn;
            case ISO:
                return R.drawable.cam_core_image_quality_control_tab_iso_icn;
            case EV:
                return R.drawable.cam_core_image_quality_control_tab_exposure_value_icn;
            case SHUTTER_SPEED:
                return R.drawable.cam_core_image_quality_control_tab_shutter_speed_icn;
            case FOCUS_RANGE:
                return R.drawable.cam_core_image_quality_control_tab_focus_icn;
            default:
                throw new IllegalArgumentException("Undefined title for " + userSettingKey);
        }
    }

    private void setTabValue(ImageQualityControlTab imageQualityControlTab, UserSettingKey userSettingKey, UserSettingValue userSettingValue) {
        switch (userSettingKey) {
            case WHITE_BALANCE:
                imageQualityControlTab.setValueIcon(((WhiteBalance) userSettingValue).getTabIconId());
                break;
            case ISO:
            case EV:
            case SHUTTER_SPEED:
            case FOCUS_RANGE:
                imageQualityControlTab.setValueText(getString(userSettingValue.getTextId()), userSettingValue != SettingUi.getImageQualityControlDefaultValue(userSettingKey));
                break;
        }
    }

    private void setTabContentDescription(ImageQualityControlTab imageQualityControlTab, UserSettingKey userSettingKey, UserSettingValue userSettingValue) {
        int imageQualityControlTabDescription = SettingUi.getImageQualityControlTabDescription(userSettingKey);
        int textId = userSettingValue.getTextId();
        imageQualityControlTab.setContentDescription(getString(imageQualityControlTabDescription) + " " + getString(textId));
    }

    private boolean isUpdateAdapterNeeded(Map<UserSettingKey, EnumValueAccessor<? extends UserSettingValue>> map) {
        Iterator<Map.Entry<UserSettingKey, EnumValueAccessor<? extends UserSettingValue>>> it = map.entrySet().iterator();
        int i = 0;
        while (it.hasNext()) {
            if (it.next().getKey().isSelectable()) {
                i++;
            }
        }
        if (this.mAdapter.getCount() != i) {
            if (CamLog.DEBUG) {
                CamLog.d("Count of tab is modified. So update this view.");
            }
            return true;
        }
        for (int i2 = 0; i2 < this.mAdapter.getCount(); i2++) {
            TypedSettingItem typedSettingItem = (TypedSettingItem) this.mAdapter.getItem(i2);
            EnumValueAccessor<? extends UserSettingValue> enumValueAccessor = map.get(typedSettingItem.getData());
            if (enumValueAccessor == null) {
                if (CamLog.DEBUG) {
                    CamLog.d("The item is removed. So update this view. key:" + typedSettingItem.getData());
                }
                return true;
            }
            if (typedSettingItem.getChildren().size() != ((UserSettingValue[]) enumValueAccessor.values()).length) {
                if (CamLog.DEBUG) {
                    CamLog.d("Child count of the item is modified. So update this view. key:" + typedSettingItem.getData());
                }
                return true;
            }
            for (int i3 = 0; i3 < ((UserSettingValue[]) enumValueAccessor.values()).length; i3++) {
                TypedSettingItem typedSettingItem2 = (TypedSettingItem) typedSettingItem.getChildren().get(i3);
                if (!((UserSettingValue[]) enumValueAccessor.values())[i3].equals(typedSettingItem2.getData())) {
                    if (CamLog.DEBUG) {
                        CamLog.d("Child item of the item is modified. So update this view. key:" + typedSettingItem.getData() + " index:" + i3);
                    }
                    if (CamLog.DEBUG) {
                        CamLog.d("  value1:" + ((UserSettingValue[]) enumValueAccessor.values())[i3]);
                    }
                    if (CamLog.DEBUG) {
                        CamLog.d("  value2:" + typedSettingItem2.getData());
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public boolean update(UserSettingKey userSettingKey, Map<UserSettingKey, EnumValueAccessor<? extends UserSettingValue>> map) {
        if (isUpdateAdapterNeeded(map)) {
            return false;
        }
        boolean z = false;
        for (int i = 0; i < this.mAdapter.getCount(); i++) {
            TypedSettingItem typedSettingItem = (TypedSettingItem) this.mAdapter.getItem(i);
            UserSettingKey userSettingKey2 = (UserSettingKey) typedSettingItem.getData();
            UserSettingValue userSettingValue = map.get(userSettingKey2).get();
            if (userSettingValue != null) {
                if (!z && userSettingValue != SettingUi.getImageQualityControlDefaultValue(userSettingKey2)) {
                    z = true;
                }
                View childAt = this.mTabContainer.getChildAt(i);
                if (childAt instanceof ImageQualityControlTab) {
                    ImageQualityControlTab imageQualityControlTab = (ImageQualityControlTab) childAt;
                    setTabValue(imageQualityControlTab, userSettingKey2, userSettingValue);
                    setTabContentDescription(imageQualityControlTab, userSettingKey2, userSettingValue);
                    childAt.setSelected(userSettingKey2 == userSettingKey);
                }
                View childAt2 = this.mWidgetContainer.getChildAt(i);
                Object tag = childAt2.getTag();
                if (tag instanceof SettingDialogItem) {
                    for (SettingItem settingItem : typedSettingItem.getChildren()) {
                        if (settingItem.compareData(userSettingValue)) {
                            settingItem.setSelected(true);
                        } else {
                            settingItem.setSelected(false);
                        }
                    }
                    ((SettingDialogItem) tag).refresh();
                }
                childAt2.setVisibility(userSettingKey2 == userSettingKey ? 0 : 8);
            }
        }
        if (this.mResetButton != null) {
            this.mResetButton.setEnabled(z);
        }
        return true;
    }

    private String getString(int i) {
        return this.mContext.getResources().getString(i);
    }

    private int getDimensionPixelSize(int i) {
        return this.mContext.getResources().getDimensionPixelSize(i);
    }

    public void setOnImageQualityControlDialogTabSelectListener(OnImageQualityControlDialogTabSelectListener onImageQualityControlDialogTabSelectListener) {
        this.mListener = onImageQualityControlDialogTabSelectListener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onResetButtonClicked() {
        for (int i = 0; i < this.mWidgetContainer.getChildCount(); i++) {
            Object tag = this.mWidgetContainer.getChildAt(i).getTag();
            if (tag instanceof SettingDialogItem) {
                ((SettingDialogItem) tag).reset();
            }
        }
    }
}
