package com.sonyericsson.android.camera.view.setting.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.view.selectabledialog.AbsSelectableDialog;
import com.sonyericsson.android.camera.view.selectabledialog.ModeSelector;
import com.sonyericsson.android.camera.view.selectabledialog.SettingMenu;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SettingDialogFactory {
    public static ModeSelector createModeSelector(Context context, int i, int i2, boolean z) {
        AbsSelectableDialog.Params params = new AbsSelectableDialog.Params();
        params.itemWidth = dimen(context, R.dimen.mode_selector_grid_column_width);
        params.itemHeight = dimen(context, R.dimen.mode_selector_grid_column_height);
        params.maxHeightLandscape = i2;
        params.maxHeightPortrait = i;
        params.rightMarginLandscape = dimen(context, R.dimen.shortcut_dialog_padding);
        params.scrollBarDefaultDelayBeforeFade = context.getResources().getInteger(R.integer.contextual_setting_scroll_bar_default_delay_before_fade);
        params.panelBackgroundColor = context.getResources().getColor(R.color.setting_1st_layer_background_color);
        params.animationType = AbsSelectableDialog.AnimationType.SLIDER;
        params.horizontalGavity = AbsSelectableDialog.HorizontalGravity.RIGHT;
        return new ModeSelector(context, params, 80, z);
    }

    public static SettingMenu createMonochromeDialog(Context context, boolean z, int i, int i2) {
        AbsSelectableDialog.Params params = new AbsSelectableDialog.Params();
        params.itemWidth = dimen(context, R.dimen.setting_2nd_layer_setting_width);
        params.itemHeight = dimen(context, R.dimen.setting_common_height);
        params.maxHeightLandscape = i2;
        params.maxHeightPortrait = i;
        if (z) {
            params.panelBackgroundColor = context.getResources().getColor(R.color.setting_1st_layer_background_color);
        } else {
            params.panelBackgroundColor = context.getResources().getColor(R.color.setting_2st_layer_background_color);
        }
        params.animationType = AbsSelectableDialog.AnimationType.FADE;
        params.horizontalGavity = AbsSelectableDialog.HorizontalGravity.LEFT;
        return new SettingMenu(context, params, AbsSelectableDialog.SelectableDialogType.SETTING_MONOCHROME);
    }

    public static SettingMenu createSettingMenuDialog(Context context, int i, int i2, boolean z) {
        AbsSelectableDialog.Params params = new AbsSelectableDialog.Params();
        params.itemWidth = i2;
        params.itemHeight = dimen(context, R.dimen.setting_common_height);
        params.maxHeightLandscape = i2;
        params.maxHeightPortrait = i;
        params.rightMarginLandscape = dimen(context, R.dimen.shortcut_dialog_padding);
        params.scrollBarDefaultDelayBeforeFade = context.getResources().getInteger(R.integer.contextual_setting_scroll_bar_default_delay_before_fade);
        params.panelBackgroundColor = context.getResources().getColor(R.color.setting_1st_layer_background_color);
        params.animationType = AbsSelectableDialog.AnimationType.SLIDER;
        params.horizontalGavity = AbsSelectableDialog.HorizontalGravity.RIGHT;
        return new SettingMenu(context, params, 80, z, AbsSelectableDialog.SelectableDialogType.SETTING_MENU);
    }

    public static SettingMenu createSecondLayerDialog(Context context, int i, int i2, boolean z) {
        AbsSelectableDialog.Params params = new AbsSelectableDialog.Params();
        params.itemWidth = dimen(context, R.dimen.setting_2nd_layer_setting_width);
        params.itemHeight = dimen(context, R.dimen.setting_common_height);
        params.maxHeightLandscape = i2 - dimen(context, R.dimen.setting_dialog_menu_max_height_margin_phone);
        params.maxHeightPortrait = i - dimen(context, R.dimen.common_button_container_side_length, R.dimen.setting_dialog_menu_max_height_margin_phone);
        params.panelBackgroundColor = context.getResources().getColor(R.color.setting_2st_layer_background_color);
        params.dropShadowSpace = dimen(context, R.dimen.setting_menu_drop_shadow_size);
        params.animationType = AbsSelectableDialog.AnimationType.FADE;
        return new SettingMenu(context, params, 48, z, AbsSelectableDialog.SelectableDialogType.SETTING_SECOND_LAYER);
    }

    public static SettingMenu createSecondLayerDialogDetails(Context context, int i, int i2, boolean z) {
        AbsSelectableDialog.Params params = new AbsSelectableDialog.Params();
        params.itemWidth = dimen(context, R.dimen.setting_2nd_layer_setting_width);
        params.itemHeight = dimen(context, R.dimen.setting_menu_item_details_subject_height, R.dimen.setting_menu_item_details_description_height);
        params.maxHeightLandscape = i2 - dimen(context, R.dimen.setting_dialog_menu_max_height_margin_phone);
        params.maxHeightPortrait = i - dimen(context, R.dimen.common_button_container_side_length, R.dimen.setting_dialog_menu_max_height_margin_phone);
        params.panelBackgroundColor = context.getResources().getColor(R.color.setting_2st_layer_background_color);
        params.dropShadowSpace = dimen(context, R.dimen.setting_menu_drop_shadow_size);
        params.animationType = AbsSelectableDialog.AnimationType.FADE;
        return new SettingMenu(context, params, 48, z, AbsSelectableDialog.SelectableDialogType.SETTING_SECOND_LAYER_DETAIL);
    }

    public static SettingMenu createShortcutDialog(Context context, UserSettingKey userSettingKey, int i, int i2) {
        AbsSelectableDialog.Params params = new AbsSelectableDialog.Params();
        params.itemWidth = dimen(context, R.dimen.setting_2nd_layer_setting_width);
        params.itemHeight = dimen(context, R.dimen.setting_common_height);
        params.maxHeightLandscape = i2;
        params.maxHeightPortrait = i;
        params.leftMarginLandscape = dimen(context, R.dimen.shortcut_dialog_padding, R.dimen.left_icon_area_height);
        params.leftMarginPortrait = params.leftMarginLandscape;
        params.bottomMarginLandscape = dimen(context, R.dimen.shortcut_dialog_padding);
        params.bottomMarginPortrait = params.bottomMarginLandscape;
        params.scrollBarDefaultDelayBeforeFade = context.getResources().getInteger(R.integer.contextual_setting_scroll_bar_default_delay_before_fade);
        params.panelBackgroundColor = context.getResources().getColor(R.color.setting_1st_layer_background_color);
        params.animationType = AbsSelectableDialog.AnimationType.FADE;
        params.horizontalGavity = AbsSelectableDialog.HorizontalGravity.LEFT;
        switch (userSettingKey) {
            case FLASH:
            case DISPLAY_FLASH:
            case PHOTO_LIGHT:
                return new SettingMenu(context, params, AbsSelectableDialog.SelectableDialogType.SETTING_FLASH);
            case SELF_TIMER:
                return new SettingMenu(context, params, AbsSelectableDialog.SelectableDialogType.SETTING_SELFTIMER);
            case ASPECT_RATIO:
                return new SettingMenu(context, params, AbsSelectableDialog.SelectableDialogType.SETTING_ASPECT_RATIO);
            case FUSION_MODE:
                return new SettingMenu(context, params, AbsSelectableDialog.SelectableDialogType.SETTING_FUSION_MODE);
            case VIDEO_HDR:
                return new SettingMenu(context, params, AbsSelectableDialog.SelectableDialogType.SETTING_VIDEO_HDR);
            case HDR:
                return new SettingMenu(context, params, AbsSelectableDialog.SelectableDialogType.SETTING_HDR);
            default:
                return null;
        }
    }

    public static SettingMenu createSceneDialog(Context context, int i, int i2) {
        AbsSelectableDialog.Params params = new AbsSelectableDialog.Params();
        params.itemWidth = i2;
        params.itemHeight = dimen(context, R.dimen.setting_common_height);
        params.maxHeightLandscape = i2;
        params.maxHeightPortrait = i - dimen(context, R.dimen.left_icon_area_height, R.dimen.second_layer_dialog_padding);
        params.rightMarginLandscape = dimen(context, R.dimen.shortcut_dialog_padding);
        params.scrollBarDefaultDelayBeforeFade = context.getResources().getInteger(R.integer.contextual_setting_scroll_bar_default_delay_before_fade);
        params.panelBackgroundColor = context.getResources().getColor(R.color.setting_1st_layer_background_color);
        params.animationType = AbsSelectableDialog.AnimationType.FADE;
        params.horizontalGavity = AbsSelectableDialog.HorizontalGravity.RIGHT;
        return new SettingMenu(context, params, AbsSelectableDialog.SelectableDialogType.SETTING_SCENE);
    }

    private static View inflate(Context context, int i) {
        return ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(i, (ViewGroup) null);
    }

    private static int dimen(Context context, int... iArr) {
        int dimensionPixelSize = 0;
        for (int i : iArr) {
            dimensionPixelSize += context.getResources().getDimensionPixelSize(i);
        }
        return dimensionPixelSize;
    }
}
