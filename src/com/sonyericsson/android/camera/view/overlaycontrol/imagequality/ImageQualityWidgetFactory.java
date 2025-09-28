package com.sonyericsson.android.camera.view.overlaycontrol.imagequality;

import android.content.Context;
import android.view.ViewGroup;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.view.setting.dialogitem.SettingDialogItem;
import com.sonyericsson.android.camera.view.setting.dialogitem.SettingDialogItemFactory;
import com.sonyericsson.android.camera.view.setting.settingitem.SettingItem;
import com.sonyericsson.android.camera.view.setting.settingitem.TypedSettingItem;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ImageQualityWidgetFactory extends SettingDialogItemFactory {
    private CameraInfo.CameraId mCameraId;
    private final OnSlideListener mListener;

    public ImageQualityWidgetFactory(OnSlideListener onSlideListener, CameraInfo.CameraId cameraId) {
        this.mListener = onSlideListener;
        this.mCameraId = cameraId;
    }

    @Override // com.sonyericsson.android.camera.view.setting.dialogitem.SettingDialogItemFactory
    public SettingDialogItem create(SettingItem settingItem, ViewGroup viewGroup, boolean z) {
        Context context = viewGroup.getContext();
        UserSettingKey userSettingKey = (UserSettingKey) ((TypedSettingItem) settingItem).getData();
        switch (userSettingKey) {
            case EV:
                return new EvSlider(context, settingItem, this.mListener);
            case SHUTTER_SPEED:
                return new ShutterSpeedSlider(context, settingItem, this.mListener);
            case FOCUS_RANGE:
                return new FocusRangeSlider(context, settingItem, this.mListener, this.mCameraId);
            case ISO:
                return new IsoSlider(context, settingItem, this.mListener);
            case WHITE_BALANCE:
                return new WbList(context, settingItem);
            default:
                throw new IllegalArgumentException("ImageQualityWidgetFactory#create() The UserSettingKey is unknown. key:" + userSettingKey);
        }
    }
}
