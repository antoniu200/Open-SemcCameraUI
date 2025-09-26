// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.overlaycontrol.imagequality;

import android.content.Context;
import com.sonyericsson.android.camera.view.setting.settingitem.TypedSettingItem;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.view.setting.dialogitem.SettingDialogItem;
import android.view.ViewGroup;
import com.sonyericsson.android.camera.view.setting.settingitem.SettingItem;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.view.setting.dialogitem.SettingDialogItemFactory;

public class ImageQualityWidgetFactory extends SettingDialogItemFactory
{
    private CameraInfo.CameraId mCameraId;
    private final OnSlideListener mListener;
    
    public ImageQualityWidgetFactory(final OnSlideListener mListener, final CameraInfo.CameraId mCameraId) {
        this.mListener = mListener;
        this.mCameraId = mCameraId;
    }
    
    @Override
    public SettingDialogItem create(final SettingItem settingItem, final ViewGroup viewGroup, final boolean b) {
        final Context context = viewGroup.getContext();
        final UserSettingKey obj = ((TypedSettingItem)settingItem).getData();
        switch (ImageQualityWidgetFactory$1.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[obj.ordinal()]) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("ImageQualityWidgetFactory#create() The UserSettingKey is unknown. key:");
                sb.append(obj);
                throw new IllegalArgumentException(sb.toString());
            }
            case 5: {
                return new WbList(context, settingItem);
            }
            case 4: {
                return new IsoSlider(context, settingItem, this.mListener);
            }
            case 3: {
                return new FocusRangeSlider(context, settingItem, this.mListener, this.mCameraId);
            }
            case 2: {
                return new ShutterSpeedSlider(context, settingItem, this.mListener);
            }
            case 1: {
                return new EvSlider(context, settingItem, this.mListener);
            }
        }
    }
}
