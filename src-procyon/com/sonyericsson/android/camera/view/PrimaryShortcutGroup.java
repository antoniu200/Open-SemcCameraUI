// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view;

import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.setting.UserSettings;
import com.sonyericsson.android.camera.configuration.parameters.FusionMode;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import java.util.HashMap;
import java.util.List;
import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import com.sonyericsson.cameracommon.utility.ResourceUtil;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.ActionMode;
import com.sonyericsson.android.camera.configuration.parameters.LedOptionsResolver;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.PhotoLight;
import com.sonyericsson.android.camera.configuration.parameters.Flash;
import com.sonyericsson.android.camera.configuration.parameters.DisplayFlash;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View$OnClickListener;
import com.sonyericsson.android.camera.view.baselayout.settingshortcut.ShortcutButton;
import com.sonyericsson.android.camera.view.setting.dialog.SettingDialogListener;
import android.widget.FrameLayout;

public class PrimaryShortcutGroup extends FrameLayout implements SettingDialogListener
{
    private ShortcutButton mAspectRatioShortcut;
    private ShortcutButton mContextualSettingShortcut;
    private ShortcutButton mFacingShortcut;
    private ShortcutButton mFlashShortcut;
    private ShortcutButton mHdrShortcut;
    private ShortcutButton mHighSensitivityFusionShortcut;
    private View$OnClickListener mPrimaryShortcutClickListener;
    private ShortcutButton mSelfTimerShortcut;
    private ShortcutButton mSemiAutoShortcut;
    private ShortcutButton mVideoHdrShortcut;
    private ViewFinderImpl.ViewFinderAccessorForShortcut mViewFinderAccessor;
    
    public PrimaryShortcutGroup(final Context context, final AttributeSet set) {
        super(context, set);
        this.mPrimaryShortcutClickListener = (View$OnClickListener)new View$OnClickListener() {
            final PrimaryShortcutGroup this$0;
            
            public void onClick(final View view) {
                if (!this.this$0.mViewFinderAccessor.isShortcutButtonClickable()) {
                    return;
                }
                final int id = view.getId();
                Label_0285: {
                    if (id != 2131296371) {
                        switch (id) {
                            default: {
                                switch (id) {
                                    default: {
                                        break Label_0285;
                                    }
                                    case 2131296513: {
                                        if (!UserSettingKey.VIDEO_HDR.isSelectable()) {
                                            this.this$0.mViewFinderAccessor.showRestrictMessageDialog(UserSettingKey.VIDEO_HDR);
                                            break Label_0285;
                                        }
                                        this.this$0.mViewFinderAccessor.openShorcutDialog(ViewFinder.UiComponentKind.VIDEO_HDR_DIALOG);
                                        break Label_0285;
                                    }
                                    case 2131296512: {
                                        this.this$0.mViewFinderAccessor.switchSemiAutoAvailability();
                                        break Label_0285;
                                    }
                                    case 2131296511: {
                                        this.this$0.mViewFinderAccessor.openShorcutDialog(ViewFinder.UiComponentKind.SELF_TIMER_DIALOG);
                                        break Label_0285;
                                    }
                                    case 2131296510: {
                                        this.this$0.mViewFinderAccessor.openShorcutDialog(ViewFinder.UiComponentKind.HDR_DIALOG);
                                        break Label_0285;
                                    }
                                }
                                break;
                            }
                            case 2131296508: {
                                if (!UserSettingKey.FUSION_MODE.isSelectable()) {
                                    this.this$0.mViewFinderAccessor.showRestrictMessageDialog(UserSettingKey.FUSION_MODE);
                                    break;
                                }
                                this.this$0.mViewFinderAccessor.openShorcutDialog(ViewFinder.UiComponentKind.FUSION_MODE_DIALOG);
                                break;
                            }
                            case 2131296507: {
                                this.this$0.mViewFinderAccessor.openShorcutDialog(ViewFinder.UiComponentKind.FLASH_DIALOG);
                                break;
                            }
                            case 2131296506: {
                                view.setOnClickListener((View$OnClickListener)null);
                                this.this$0.mViewFinderAccessor.switchCamera();
                                break;
                            }
                            case 2131296505: {
                                this.this$0.mViewFinderAccessor.openShorcutDialog(ViewFinder.UiComponentKind.ASPECT_RATIO_DIALOG);
                                break;
                            }
                        }
                    }
                    else if (view.isShown()) {
                        this.this$0.mViewFinderAccessor.openSettingMenuDialog();
                    }
                }
            }
        };
    }
    
    private DisplayFlash getCurrentDisplayFlashSetting(final DisplayFlash[] array, final DisplayFlash displayFlash) {
        if (array == null) {
            return null;
        }
        if (displayFlash == null) {
            return null;
        }
        for (final DisplayFlash displayFlash2 : array) {
            if (displayFlash2 != null && displayFlash.getValue().equals(displayFlash2.getValue())) {
                return displayFlash2;
            }
        }
        return null;
    }
    
    private Flash getCurrentFlashSetting(final Flash[] array, final Flash flash) {
        if (array == null) {
            return null;
        }
        if (flash == null) {
            return null;
        }
        for (final Flash flash2 : array) {
            if (flash2 != null && flash.getValue().equals(flash2.getValue())) {
                return flash2;
            }
        }
        return null;
    }
    
    private PhotoLight getCurrentPhotoLightSetting(final PhotoLight[] array, final PhotoLight photoLight) {
        if (array == null) {
            return null;
        }
        if (photoLight == null) {
            return null;
        }
        for (final PhotoLight photoLight2 : array) {
            if (photoLight2 != null && photoLight.getValue().equals(photoLight2.getValue())) {
                return photoLight2;
            }
        }
        return null;
    }
    
    private DisplayFlash[] getDisplayFlashOptions(final CapturingMode capturingMode) {
        return LedOptionsResolver.getInstance().getDisplayFlashOptions(new ActionMode(false, 1, capturingMode.getCameraId()), PlatformCapability.getSupportedFlashModes(capturingMode.getCameraId()));
    }
    
    private Flash[] getFlashOptions(final CapturingMode capturingMode) {
        return LedOptionsResolver.getInstance().getFlashOptions(new ActionMode(false, 1, capturingMode.getCameraId()), PlatformCapability.getSupportedFlashModes(capturingMode.getCameraId()));
    }
    
    private PhotoLight[] getPhotoLightOptions(final CapturingMode capturingMode) {
        return LedOptionsResolver.getInstance().getPhotoLightOptions(new ActionMode(false, 2, capturingMode.getCameraId()), PlatformCapability.getSupportedFlashModes(capturingMode.getCameraId()));
    }
    
    private String getString(final int n) {
        return ResourceUtil.getString(this.getContext(), n);
    }
    
    private void setSelected(final Object o, final boolean b) {
        if (o == null) {
            return;
        }
        final Map<UserSettingKey, View> primaryShortcutViewMap = this.getPrimaryShortcutViewMap();
        final View view = primaryShortcutViewMap.get(o);
        if (view != null) {
            for (final Map.Entry<K, View> entry : primaryShortcutViewMap.entrySet()) {
                if (b) {
                    if (entry.getValue() == view) {
                        entry.getValue().setSelected(b);
                    }
                    else {
                        entry.getValue().setSelected(false);
                    }
                }
                else {
                    entry.getValue().setSelected(b);
                }
            }
        }
    }
    
    private void updateShortcutLayout() {
        final List<View> allPrimaryShortcutView = this.getAllPrimaryShortcutView();
        final ArrayList list = new ArrayList();
        for (final View view : allPrimaryShortcutView) {
            if (view.getVisibility() == 0) {
                list.add(view);
            }
        }
        final int size = list.size();
        if (size > 0) {
            final View view2 = (View)list.get(0);
            final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)view2.getLayoutParams();
            layoutParams.gravity = 49;
            view2.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
            int i = 1;
            if (size > 1) {
                final int dimensionPixelSize = ResourceUtil.getDimensionPixelSize(this.getContext(), this.getContext().getPackageName(), 2131165428);
                final int n = (this.getHeight() - dimensionPixelSize * size) / (size - 1);
                while (i < list.size()) {
                    final View view3 = (View)list.get(i);
                    final FrameLayout$LayoutParams layoutParams2 = (FrameLayout$LayoutParams)view3.getLayoutParams();
                    layoutParams2.topMargin = (dimensionPixelSize + n) * i;
                    view3.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
                    ++i;
                }
            }
        }
    }
    
    public void disable() {
        this.mFlashShortcut.setOnClickListener((View$OnClickListener)null);
        this.mSemiAutoShortcut.setOnClickListener((View$OnClickListener)null);
        this.mHdrShortcut.setOnClickListener((View$OnClickListener)null);
        this.mSelfTimerShortcut.setOnClickListener((View$OnClickListener)null);
        this.mAspectRatioShortcut.setOnClickListener((View$OnClickListener)null);
        this.mHighSensitivityFusionShortcut.setOnClickListener((View$OnClickListener)null);
        this.mVideoHdrShortcut.setOnClickListener((View$OnClickListener)null);
        this.mFacingShortcut.setOnClickListener((View$OnClickListener)null);
        this.mContextualSettingShortcut.setOnClickListener((View$OnClickListener)null);
    }
    
    public List<View> getAllPrimaryShortcutView() {
        final ArrayList list = new ArrayList();
        list.add(this.mContextualSettingShortcut);
        list.add(this.mFacingShortcut);
        list.add(this.mVideoHdrShortcut);
        list.add(this.mHighSensitivityFusionShortcut);
        list.add(this.mHdrShortcut);
        list.add(this.mSemiAutoShortcut);
        list.add(this.mAspectRatioShortcut);
        list.add(this.mSelfTimerShortcut);
        list.add(this.mFlashShortcut);
        return list;
    }
    
    public Map<UserSettingKey, View> getPrimaryShortcutViewMap() {
        final HashMap hashMap = new HashMap();
        hashMap.put(UserSettingKey.FLASH, this.mFlashShortcut);
        hashMap.put(UserSettingKey.DISPLAY_FLASH, this.mFlashShortcut);
        hashMap.put(UserSettingKey.PHOTO_LIGHT, this.mFlashShortcut);
        hashMap.put(UserSettingKey.SEMI_AUTO, this.mSemiAutoShortcut);
        hashMap.put(UserSettingKey.HDR, this.mHdrShortcut);
        hashMap.put(UserSettingKey.SELF_TIMER, this.mSelfTimerShortcut);
        hashMap.put(UserSettingKey.ASPECT_RATIO, this.mAspectRatioShortcut);
        hashMap.put(UserSettingKey.FUSION_MODE, this.mHighSensitivityFusionShortcut);
        hashMap.put(UserSettingKey.VIDEO_HDR, this.mVideoHdrShortcut);
        hashMap.put(UserSettingKey.FACING, this.mFacingShortcut);
        hashMap.put(UserSettingKey.SETTING_MENU, this.mContextualSettingShortcut);
        return hashMap;
    }
    
    public void hide() {
        this.mFlashShortcut.hide();
        this.mFlashShortcut.setOnClickListener((View$OnClickListener)null);
        this.mSemiAutoShortcut.hide();
        this.mSemiAutoShortcut.setOnClickListener((View$OnClickListener)null);
        this.mHdrShortcut.hide();
        this.mHdrShortcut.setOnClickListener((View$OnClickListener)null);
        this.mSelfTimerShortcut.hide();
        this.mSelfTimerShortcut.setOnClickListener((View$OnClickListener)null);
        this.mAspectRatioShortcut.hide();
        this.mAspectRatioShortcut.setOnClickListener((View$OnClickListener)null);
        this.mHighSensitivityFusionShortcut.hide();
        this.mHighSensitivityFusionShortcut.setOnClickListener((View$OnClickListener)null);
        this.mVideoHdrShortcut.hide();
        this.mVideoHdrShortcut.setOnClickListener((View$OnClickListener)null);
        this.mFacingShortcut.hide();
        this.mFacingShortcut.setOnClickListener((View$OnClickListener)null);
        this.mContextualSettingShortcut.hide();
        this.mContextualSettingShortcut.setOnClickListener((View$OnClickListener)null);
    }
    
    public void onCloseSettingDialog(final Object o) {
        this.setSelected(o, false);
    }
    
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mFlashShortcut = (ShortcutButton)this.findViewById(2131296507);
        this.mSemiAutoShortcut = (ShortcutButton)this.findViewById(2131296512);
        this.mHdrShortcut = (ShortcutButton)this.findViewById(2131296510);
        this.mSelfTimerShortcut = (ShortcutButton)this.findViewById(2131296511);
        this.mAspectRatioShortcut = (ShortcutButton)this.findViewById(2131296505);
        this.mHighSensitivityFusionShortcut = (ShortcutButton)this.findViewById(2131296508);
        this.mVideoHdrShortcut = (ShortcutButton)this.findViewById(2131296513);
        this.mFacingShortcut = (ShortcutButton)this.findViewById(2131296506);
        this.mContextualSettingShortcut = (ShortcutButton)this.findViewById(2131296371);
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        this.updateShortcutLayout();
        super.onLayout(b, n, n2, n3, n4);
    }
    
    public void onOpenSettingDialog(final Object o) {
        this.setSelected(o, true);
    }
    
    public void setUiOrientation(final int uiOrientation) {
        this.mFlashShortcut.setUiOrientation(uiOrientation);
        this.mSemiAutoShortcut.setUiOrientation(uiOrientation);
        this.mHdrShortcut.setUiOrientation(uiOrientation);
        this.mSelfTimerShortcut.setUiOrientation(uiOrientation);
        this.mAspectRatioShortcut.setUiOrientation(uiOrientation);
        this.mHighSensitivityFusionShortcut.setUiOrientation(uiOrientation);
        this.mVideoHdrShortcut.setUiOrientation(uiOrientation);
        this.mFacingShortcut.setUiOrientation(uiOrientation);
        this.mContextualSettingShortcut.setUiOrientation(uiOrientation);
    }
    
    public void setViewFinderAccessor(final ViewFinderImpl.ViewFinderAccessorForShortcut mViewFinderAccessor) {
        this.mViewFinderAccessor = mViewFinderAccessor;
    }
    
    public void show() {
        this.mFlashShortcut.show();
        this.mFlashShortcut.setOnClickListener(this.mPrimaryShortcutClickListener);
        this.mSemiAutoShortcut.show();
        this.mSemiAutoShortcut.setOnClickListener(this.mPrimaryShortcutClickListener);
        this.mHdrShortcut.show();
        this.mHdrShortcut.setOnClickListener(this.mPrimaryShortcutClickListener);
        this.mSelfTimerShortcut.show();
        this.mSelfTimerShortcut.setOnClickListener(this.mPrimaryShortcutClickListener);
        this.mAspectRatioShortcut.show();
        this.mAspectRatioShortcut.setOnClickListener(this.mPrimaryShortcutClickListener);
        this.mHighSensitivityFusionShortcut.show();
        this.mHighSensitivityFusionShortcut.setOnClickListener(this.mPrimaryShortcutClickListener);
        this.mVideoHdrShortcut.show();
        this.mVideoHdrShortcut.setOnClickListener(this.mPrimaryShortcutClickListener);
        this.mFacingShortcut.show();
        this.mFacingShortcut.setOnClickListener(this.mPrimaryShortcutClickListener);
        this.mContextualSettingShortcut.show();
        this.mContextualSettingShortcut.setOnClickListener(this.mPrimaryShortcutClickListener);
    }
    
    public void updatePrimaryShortcutIcon(final UserSettingKey userSettingKey, final int n) {
        switch (PrimaryShortcutGroup$2.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[userSettingKey.ordinal()]) {
            case 8: {
                if (UserSettingKey.VIDEO_HDR.isSelectable()) {
                    this.mVideoHdrShortcut.setImageResource(n);
                    break;
                }
                this.mVideoHdrShortcut.setImageResource(VideoHdr.HDR_OFF.getIconId());
                break;
            }
            case 7: {
                if (UserSettingKey.FUSION_MODE.isSelectable()) {
                    this.mHighSensitivityFusionShortcut.setImageResource(n);
                    break;
                }
                this.mHighSensitivityFusionShortcut.setImageResource(FusionMode.OFF.getIconId());
                break;
            }
            case 6: {
                this.mAspectRatioShortcut.setImageResource(n);
                break;
            }
            case 5: {
                this.mHdrShortcut.setImageResource(n);
                break;
            }
            case 4: {
                this.mSelfTimerShortcut.setImageResource(n);
                break;
            }
            case 1:
            case 2:
            case 3: {
                this.mFlashShortcut.setImageResource(n);
                break;
            }
        }
    }
    
    public void updatePrimaryShortcutIcons(final CapturingMode capturingMode, final UserSettings userSettings, final boolean b) {
        this.mContextualSettingShortcut.set(true);
        final UserSettingValue value = userSettings.get(UserSettingKey.HDR);
        final UserSettingValue value2 = userSettings.get(UserSettingKey.SELF_TIMER);
        final UserSettingValue value3 = userSettings.get(UserSettingKey.ASPECT_RATIO);
        final UserSettingValue value4 = userSettings.get(UserSettingKey.FUSION_MODE);
        final UserSettingValue value5 = userSettings.get(UserSettingKey.VIDEO_HDR);
        switch (PrimaryShortcutGroup$2.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$CapturingMode[capturingMode.ordinal()]) {
            case 7: {
                final PhotoLight currentPhotoLightSetting = this.getCurrentPhotoLightSetting(this.getPhotoLightOptions(capturingMode), (PhotoLight)userSettings.get(UserSettingKey.PHOTO_LIGHT));
                if (currentPhotoLightSetting != null) {
                    this.mFlashShortcut.setContentDescription((CharSequence)this.getString(2131689570));
                    this.mFlashShortcut.setImageResource(currentPhotoLightSetting.getIconId());
                    this.mFlashShortcut.set(true);
                }
                else {
                    this.mFlashShortcut.set(false);
                }
                if (PlatformCapability.isAwbAbCompensationSupported(capturingMode.getCameraId())) {
                    this.mSemiAutoShortcut.set(true);
                }
                else {
                    this.mSemiAutoShortcut.set(false);
                }
                this.mHdrShortcut.set(false);
                this.mSelfTimerShortcut.set(false);
                this.mAspectRatioShortcut.set(false);
                this.mHighSensitivityFusionShortcut.set(false);
                this.mVideoHdrShortcut.set(false);
                this.mFacingShortcut.set(false);
                break;
            }
            case 6: {
                this.mFlashShortcut.set(false);
                if (PlatformCapability.isAwbAbCompensationSupported(capturingMode.getCameraId())) {
                    this.mSemiAutoShortcut.set(true);
                }
                else {
                    this.mSemiAutoShortcut.set(false);
                }
                this.mHdrShortcut.set(false);
                this.mSelfTimerShortcut.set(false);
                this.mAspectRatioShortcut.set(false);
                this.mHighSensitivityFusionShortcut.set(false);
                this.mVideoHdrShortcut.set(false);
                if (PlatformCapability.isFrontCameraSupported()) {
                    this.mFacingShortcut.setContentDescription((CharSequence)this.getString(2131689612));
                    this.mFacingShortcut.set(true);
                    break;
                }
                break;
            }
            case 5: {
                final DisplayFlash currentDisplayFlashSetting = this.getCurrentDisplayFlashSetting(this.getDisplayFlashOptions(capturingMode), (DisplayFlash)userSettings.get(UserSettingKey.DISPLAY_FLASH));
                if (currentDisplayFlashSetting != null) {
                    this.mFlashShortcut.setContentDescription((CharSequence)this.getString(2131689571));
                    this.mFlashShortcut.setImageResource(currentDisplayFlashSetting.getIconId());
                    this.mFlashShortcut.set(true);
                }
                else {
                    this.mFlashShortcut.set(false);
                }
                if (PlatformCapability.isAwbAbCompensationSupported(capturingMode.getCameraId())) {
                    this.mSemiAutoShortcut.set(true);
                }
                else {
                    this.mSemiAutoShortcut.set(false);
                }
                this.mHdrShortcut.set(false);
                if (value2 != null) {
                    this.mSelfTimerShortcut.set(true);
                    this.mSelfTimerShortcut.setImageResource(value2.getIconId());
                }
                else {
                    this.mSelfTimerShortcut.set(false);
                }
                if (value3 != null) {
                    this.mAspectRatioShortcut.setImageResource(value3.getIconId());
                    this.mAspectRatioShortcut.set(true);
                }
                else {
                    this.mAspectRatioShortcut.set(false);
                }
                this.mHighSensitivityFusionShortcut.set(false);
                this.mVideoHdrShortcut.set(false);
                if (PlatformCapability.isFrontCameraSupported()) {
                    this.mFacingShortcut.setContentDescription((CharSequence)this.getString(2131689612));
                    this.mFacingShortcut.set(true);
                    break;
                }
                break;
            }
            case 4: {
                final DisplayFlash currentDisplayFlashSetting2 = this.getCurrentDisplayFlashSetting(this.getDisplayFlashOptions(capturingMode), (DisplayFlash)userSettings.get(UserSettingKey.DISPLAY_FLASH));
                if (currentDisplayFlashSetting2 != null) {
                    this.mFlashShortcut.setContentDescription((CharSequence)this.getString(2131689571));
                    this.mFlashShortcut.setImageResource(currentDisplayFlashSetting2.getIconId());
                    this.mFlashShortcut.set(true);
                }
                else {
                    this.mFlashShortcut.set(false);
                }
                this.mSemiAutoShortcut.set(false);
                if (value != null) {
                    this.mHdrShortcut.setImageResource(value.getIconId());
                    this.mHdrShortcut.set(true);
                }
                else {
                    this.mHdrShortcut.set(false);
                }
                if (value2 != null) {
                    this.mSelfTimerShortcut.setImageResource(value2.getIconId());
                    this.mSelfTimerShortcut.set(true);
                }
                else {
                    this.mSelfTimerShortcut.set(false);
                }
                if (value3 != null) {
                    this.mAspectRatioShortcut.setImageResource(value3.getIconId());
                    this.mAspectRatioShortcut.set(true);
                }
                else {
                    this.mAspectRatioShortcut.set(false);
                }
                this.mHighSensitivityFusionShortcut.set(false);
                this.mVideoHdrShortcut.set(false);
                if (PlatformCapability.isFrontCameraSupported()) {
                    this.mFacingShortcut.setContentDescription((CharSequence)this.getString(2131689612));
                    this.mFacingShortcut.set(true);
                    break;
                }
                break;
            }
            case 3: {
                final PhotoLight currentPhotoLightSetting2 = this.getCurrentPhotoLightSetting(this.getPhotoLightOptions(capturingMode), (PhotoLight)userSettings.get(UserSettingKey.PHOTO_LIGHT));
                if (currentPhotoLightSetting2 != null) {
                    this.mFlashShortcut.setContentDescription((CharSequence)this.getString(2131689570));
                    this.mFlashShortcut.setImageResource(currentPhotoLightSetting2.getIconId());
                    this.mFlashShortcut.set(true);
                }
                else {
                    this.mFlashShortcut.set(false);
                }
                if (PlatformCapability.isAwbAbCompensationSupported(capturingMode.getCameraId())) {
                    this.mSemiAutoShortcut.set(true);
                }
                else {
                    this.mSemiAutoShortcut.set(false);
                }
                this.mHdrShortcut.set(false);
                this.mSelfTimerShortcut.set(false);
                this.mAspectRatioShortcut.set(false);
                if (value4 != null && PlatformCapability.isHighSensitivityFusionSupported(capturingMode.getCameraId())) {
                    if (UserSettingKey.FUSION_MODE.isSelectable()) {
                        this.mHighSensitivityFusionShortcut.setImageResource(value4.getIconId());
                    }
                    else {
                        this.mHighSensitivityFusionShortcut.setImageResource(FusionMode.OFF.getIconId());
                    }
                    this.mHighSensitivityFusionShortcut.set(true);
                }
                else {
                    this.mHighSensitivityFusionShortcut.set(false);
                }
                if (!b && value5 != null && PlatformCapability.isVideoHdrSupported(capturingMode.getCameraId())) {
                    if (UserSettingKey.VIDEO_HDR.isSelectable()) {
                        this.mVideoHdrShortcut.setImageResource(value5.getIconId());
                    }
                    else {
                        this.mVideoHdrShortcut.setImageResource(VideoHdr.HDR_OFF.getIconId());
                    }
                    this.mVideoHdrShortcut.set(true);
                }
                else {
                    this.mVideoHdrShortcut.set(false);
                }
                if (PlatformCapability.isFrontCameraSupported()) {
                    this.mFacingShortcut.setContentDescription((CharSequence)this.getString(2131689610));
                    this.mFacingShortcut.set(true);
                    break;
                }
                break;
            }
            case 2: {
                final Flash currentFlashSetting = this.getCurrentFlashSetting(this.getFlashOptions(capturingMode), (Flash)userSettings.get(UserSettingKey.FLASH));
                if (currentFlashSetting != null) {
                    this.mFlashShortcut.setContentDescription((CharSequence)this.getString(2131689571));
                    this.mFlashShortcut.setImageResource(currentFlashSetting.getIconId());
                    this.mFlashShortcut.set(true);
                }
                else {
                    this.mFlashShortcut.set(false);
                }
                if (PlatformCapability.isAwbAbCompensationSupported(capturingMode.getCameraId())) {
                    this.mSemiAutoShortcut.set(true);
                }
                else {
                    this.mSemiAutoShortcut.set(false);
                }
                this.mHdrShortcut.set(false);
                if (value2 != null) {
                    this.mSelfTimerShortcut.setImageResource(value2.getIconId());
                    this.mSelfTimerShortcut.set(true);
                }
                else {
                    this.mSelfTimerShortcut.set(false);
                }
                if (value3 != null) {
                    this.mAspectRatioShortcut.setImageResource(value3.getIconId());
                    this.mAspectRatioShortcut.set(true);
                }
                else {
                    this.mAspectRatioShortcut.set(false);
                }
                this.mHighSensitivityFusionShortcut.set(false);
                this.mVideoHdrShortcut.set(false);
                if (PlatformCapability.isFrontCameraSupported()) {
                    this.mFacingShortcut.setContentDescription((CharSequence)this.getString(2131689610));
                    this.mFacingShortcut.set(true);
                    break;
                }
                break;
            }
            case 1: {
                final Flash currentFlashSetting2 = this.getCurrentFlashSetting(this.getFlashOptions(capturingMode), (Flash)userSettings.get(UserSettingKey.FLASH));
                if (currentFlashSetting2 != null) {
                    this.mFlashShortcut.setContentDescription((CharSequence)this.getString(2131689571));
                    this.mFlashShortcut.setImageResource(currentFlashSetting2.getIconId());
                    this.mFlashShortcut.set(true);
                }
                else {
                    this.mFlashShortcut.set(false);
                }
                this.mSemiAutoShortcut.set(false);
                if (value != null) {
                    this.mHdrShortcut.setImageResource(value.getIconId());
                    this.mHdrShortcut.set(true);
                }
                else {
                    this.mHdrShortcut.set(false);
                }
                if (value2 != null) {
                    this.mSelfTimerShortcut.setImageResource(value2.getIconId());
                    this.mSelfTimerShortcut.set(true);
                }
                else {
                    this.mSelfTimerShortcut.set(false);
                }
                if (value3 != null) {
                    this.mAspectRatioShortcut.setImageResource(value3.getIconId());
                    this.mAspectRatioShortcut.set(true);
                }
                else {
                    this.mAspectRatioShortcut.set(false);
                }
                this.mHighSensitivityFusionShortcut.set(false);
                this.mVideoHdrShortcut.set(false);
                if (PlatformCapability.isFrontCameraSupported()) {
                    this.mFacingShortcut.setContentDescription((CharSequence)this.getString(2131689610));
                    this.mFacingShortcut.set(true);
                    break;
                }
                break;
            }
        }
    }
}
