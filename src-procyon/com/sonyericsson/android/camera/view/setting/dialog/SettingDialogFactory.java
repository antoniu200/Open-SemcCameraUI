// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.setting.dialog;

import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.view.selectabledialog.SettingMenu;
import com.sonyericsson.android.camera.view.selectabledialog.AbsSelectableDialog;
import com.sonyericsson.android.camera.view.selectabledialog.ModeSelector;
import android.content.Context;

public class SettingDialogFactory
{
    public static ModeSelector createModeSelector(final Context context, final int maxHeightPortrait, final int maxHeightLandscape, final boolean b) {
        final AbsSelectableDialog.Params params = new AbsSelectableDialog.Params();
        params.itemWidth = dimen(context, 2131165443);
        params.itemHeight = dimen(context, 2131165442);
        params.maxHeightLandscape = maxHeightLandscape;
        params.maxHeightPortrait = maxHeightPortrait;
        params.rightMarginLandscape = dimen(context, 2131165617);
        params.scrollBarDefaultDelayBeforeFade = context.getResources().getInteger(2131361798);
        params.panelBackgroundColor = context.getResources().getColor(2131099756);
        params.animationType = AbsSelectableDialog.AnimationType.SLIDER;
        params.horizontalGavity = AbsSelectableDialog.HorizontalGravity.RIGHT;
        return new ModeSelector(context, params, 80, b);
    }
    
    public static SettingMenu createMonochromeDialog(final Context context, final boolean b, final int maxHeightPortrait, final int maxHeightLandscape) {
        final AbsSelectableDialog.Params params = new AbsSelectableDialog.Params();
        params.itemWidth = dimen(context, 2131165585);
        params.itemHeight = dimen(context, 2131165586);
        params.maxHeightLandscape = maxHeightLandscape;
        params.maxHeightPortrait = maxHeightPortrait;
        if (b) {
            params.panelBackgroundColor = context.getResources().getColor(2131099756);
        }
        else {
            params.panelBackgroundColor = context.getResources().getColor(2131099757);
        }
        params.animationType = AbsSelectableDialog.AnimationType.FADE;
        params.horizontalGavity = AbsSelectableDialog.HorizontalGravity.LEFT;
        return new SettingMenu(context, params, AbsSelectableDialog.SelectableDialogType.SETTING_MONOCHROME);
    }
    
    public static SettingMenu createSceneDialog(final Context context, final int n, final int n2) {
        final AbsSelectableDialog.Params params = new AbsSelectableDialog.Params();
        params.itemWidth = n2;
        params.itemHeight = dimen(context, 2131165586);
        params.maxHeightLandscape = n2;
        params.maxHeightPortrait = n - dimen(context, 2131165428, 2131165565);
        params.rightMarginLandscape = dimen(context, 2131165617);
        params.scrollBarDefaultDelayBeforeFade = context.getResources().getInteger(2131361798);
        params.panelBackgroundColor = context.getResources().getColor(2131099756);
        params.animationType = AbsSelectableDialog.AnimationType.FADE;
        params.horizontalGavity = AbsSelectableDialog.HorizontalGravity.RIGHT;
        return new SettingMenu(context, params, AbsSelectableDialog.SelectableDialogType.SETTING_SCENE);
    }
    
    public static SettingMenu createSecondLayerDialog(final Context context, final int n, final int n2, final boolean b) {
        final AbsSelectableDialog.Params params = new AbsSelectableDialog.Params();
        params.itemWidth = dimen(context, 2131165585);
        params.itemHeight = dimen(context, 2131165586);
        params.maxHeightLandscape = n2 - dimen(context, 2131165591);
        params.maxHeightPortrait = n - dimen(context, 2131165292, 2131165591);
        params.panelBackgroundColor = context.getResources().getColor(2131099757);
        params.dropShadowSpace = dimen(context, 2131165610);
        params.animationType = AbsSelectableDialog.AnimationType.FADE;
        return new SettingMenu(context, params, 48, b, AbsSelectableDialog.SelectableDialogType.SETTING_SECOND_LAYER);
    }
    
    public static SettingMenu createSecondLayerDialogDetails(final Context context, final int n, final int n2, final boolean b) {
        final AbsSelectableDialog.Params params = new AbsSelectableDialog.Params();
        params.itemWidth = dimen(context, 2131165585);
        params.itemHeight = dimen(context, 2131165612, 2131165611);
        params.maxHeightLandscape = n2 - dimen(context, 2131165591);
        params.maxHeightPortrait = n - dimen(context, 2131165292, 2131165591);
        params.panelBackgroundColor = context.getResources().getColor(2131099757);
        params.dropShadowSpace = dimen(context, 2131165610);
        params.animationType = AbsSelectableDialog.AnimationType.FADE;
        return new SettingMenu(context, params, 48, b, AbsSelectableDialog.SelectableDialogType.SETTING_SECOND_LAYER_DETAIL);
    }
    
    public static SettingMenu createSettingMenuDialog(final Context context, final int maxHeightPortrait, final int n, final boolean b) {
        final AbsSelectableDialog.Params params = new AbsSelectableDialog.Params();
        params.itemWidth = n;
        params.itemHeight = dimen(context, 2131165586);
        params.maxHeightLandscape = n;
        params.maxHeightPortrait = maxHeightPortrait;
        params.rightMarginLandscape = dimen(context, 2131165617);
        params.scrollBarDefaultDelayBeforeFade = context.getResources().getInteger(2131361798);
        params.panelBackgroundColor = context.getResources().getColor(2131099756);
        params.animationType = AbsSelectableDialog.AnimationType.SLIDER;
        params.horizontalGavity = AbsSelectableDialog.HorizontalGravity.RIGHT;
        return new SettingMenu(context, params, 80, b, AbsSelectableDialog.SelectableDialogType.SETTING_MENU);
    }
    
    public static SettingMenu createShortcutDialog(final Context context, final UserSettingKey userSettingKey, final int maxHeightPortrait, final int maxHeightLandscape) {
        final AbsSelectableDialog.Params params = new AbsSelectableDialog.Params();
        params.itemWidth = dimen(context, 2131165585);
        params.itemHeight = dimen(context, 2131165586);
        params.maxHeightLandscape = maxHeightLandscape;
        params.maxHeightPortrait = maxHeightPortrait;
        params.leftMarginLandscape = dimen(context, 2131165617, 2131165428);
        params.leftMarginPortrait = params.leftMarginLandscape;
        params.bottomMarginLandscape = dimen(context, 2131165617);
        params.bottomMarginPortrait = params.bottomMarginLandscape;
        params.scrollBarDefaultDelayBeforeFade = context.getResources().getInteger(2131361798);
        params.panelBackgroundColor = context.getResources().getColor(2131099756);
        params.animationType = AbsSelectableDialog.AnimationType.FADE;
        params.horizontalGavity = AbsSelectableDialog.HorizontalGravity.LEFT;
        switch (SettingDialogFactory$1.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[userSettingKey.ordinal()]) {
            default: {
                return null;
            }
            case 8: {
                return new SettingMenu(context, params, AbsSelectableDialog.SelectableDialogType.SETTING_HDR);
            }
            case 7: {
                return new SettingMenu(context, params, AbsSelectableDialog.SelectableDialogType.SETTING_VIDEO_HDR);
            }
            case 6: {
                return new SettingMenu(context, params, AbsSelectableDialog.SelectableDialogType.SETTING_FUSION_MODE);
            }
            case 5: {
                return new SettingMenu(context, params, AbsSelectableDialog.SelectableDialogType.SETTING_ASPECT_RATIO);
            }
            case 4: {
                return new SettingMenu(context, params, AbsSelectableDialog.SelectableDialogType.SETTING_SELFTIMER);
            }
            case 1:
            case 2:
            case 3: {
                return new SettingMenu(context, params, AbsSelectableDialog.SelectableDialogType.SETTING_FLASH);
            }
        }
    }
    
    private static int dimen(final Context context, final int... array) {
        final int length = array.length;
        int i = 0;
        int n = 0;
        while (i < length) {
            n += context.getResources().getDimensionPixelSize(array[i]);
            ++i;
        }
        return n;
    }
    
    private static View inflate(final Context context, final int n) {
        return ((LayoutInflater)context.getSystemService("layout_inflater")).inflate(n, (ViewGroup)null);
    }
}
