package com.sonyericsson.android.camera.view.baselayout;

import com.sonyericsson.cameracommon.viewfinder.LayoutPattern;
import java.util.HashMap;
import java.util.Map;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class BaseLayoutPatternApplier implements LayoutPatternApplier {
    public static final int D = 3;
    public static final int H = 2;
    public static final int N = 0;
    public static final int S = 1;
    private BaseLayout mLayout;
    private final Map<LayoutPattern, Visibilities> mMap = new HashMap();

    @Override // com.sonyericsson.android.camera.view.baselayout.LayoutPatternApplier
    public void setup(BaseLayout baseLayout, boolean z) {
        this.mLayout = baseLayout;
        setupVisibilities(z);
    }

    @Override // com.sonyericsson.android.camera.view.baselayout.LayoutPatternApplier
    public void apply(LayoutPattern layoutPattern) {
        Visibilities visibilities = this.mMap.get(layoutPattern);
        switch (visibilities.captureButton) {
            case 1:
                this.mLayout.getOnScreenButtonGroup().setVisibility(0);
                break;
            case 2:
                this.mLayout.getOnScreenButtonGroup().setVisibility(4);
                break;
            default:
                if (this.mLayout.getOnScreenButtonGroup().isMainButtonTouched()) {
                    this.mLayout.getOnScreenButtonGroup().setVisibility(0);
                    break;
                } else {
                    this.mLayout.getOnScreenButtonGroup().setVisibility(4);
                    break;
                }
        }
        switch (visibilities.contentView) {
            case 1:
                this.mLayout.showContentsViewController();
                break;
            case 2:
                this.mLayout.hideContentsViewController();
                break;
        }
        switch (visibilities.modeButtonShortcut) {
            case 1:
                this.mLayout.getModeButtonShortcut().show();
                break;
            case 2:
                this.mLayout.getModeButtonShortcut().hide();
                break;
        }
        switch (visibilities.mruShortcut) {
            case 1:
                this.mLayout.getMruButtonContainer().show();
                break;
            case 2:
                this.mLayout.getMruButtonContainer().hide();
                break;
        }
        switch (visibilities.captureMethodIndicator) {
            case 1:
                this.mLayout.getPhotoSmileCaptureIndicator().show();
                break;
            case 2:
                this.mLayout.getPhotoSmileCaptureIndicator().hide();
                break;
        }
        switch (visibilities.sceneIndicator) {
            case 1:
                this.mLayout.getSceneIndicator().show();
                this.mLayout.getConditionIndicator().show();
                break;
            case 2:
                this.mLayout.getSceneIndicator().hide();
                this.mLayout.getConditionIndicator().hide();
                break;
        }
        switch (visibilities.geotagIndicator) {
            case 1:
                this.mLayout.getGeoTagIndicator().show();
                break;
            case 2:
                this.mLayout.getGeoTagIndicator().hide();
                break;
        }
        switch (visibilities.storageIndicator) {
            case 1:
                this.mLayout.getLowMemoryInternalIndicator().show();
                this.mLayout.getLowMemorySdIndicator().show();
                break;
            case 2:
                this.mLayout.getLowMemoryInternalIndicator().hide();
                this.mLayout.getLowMemorySdIndicator().hide();
                break;
        }
        switch (visibilities.zoombar) {
            case 1:
                this.mLayout.getZoomBar().show();
                break;
            case 2:
                this.mLayout.getZoomBar().hideImmediately();
                break;
            case 3:
                this.mLayout.getZoomBar().hideDelayed();
                break;
        }
        switch (visibilities.recordingProgress) {
            case 1:
                this.mLayout.getRecordingIndicator().setVisibility(0);
                break;
            case 2:
                this.mLayout.getRecordingIndicator().setVisibility(4);
                break;
        }
        switch (visibilities.thermalIndicator) {
            case 1:
                this.mLayout.getThermalIndicator().show();
                break;
            case 2:
                this.mLayout.getThermalIndicator().hide();
                break;
        }
        switch (visibilities.primarySettingShortcut) {
            case 1:
                this.mLayout.getPrimaryShortcut().show();
                break;
            case 2:
                this.mLayout.getPrimaryShortcut().hide();
                break;
        }
        switch (visibilities.secondarySettingShortcut) {
            case 1:
                this.mLayout.getOnScreenSubButton().setVisibility(0);
                this.mLayout.getOnScreenExtraButton().setVisibility(0);
                break;
            case 2:
                this.mLayout.getOnScreenSubButton().setVisibility(4);
                this.mLayout.getOnScreenExtraButton().setVisibility(4);
                break;
        }
        switch (visibilities.batteryIndicator) {
            case 1:
                this.mLayout.getBatteryIndicator().show();
                break;
            case 2:
                this.mLayout.getBatteryIndicator().hide();
                break;
        }
        switch (visibilities.predictiveCaptureIndicator) {
            case 1:
                this.mLayout.getPredictiveCaptureIndicatorController().show();
                break;
            case 2:
                this.mLayout.getPredictiveCaptureIndicatorController().hide();
                break;
        }
        this.mLayout.refresh();
    }

    private static class Visibilities {
        public final int batteryIndicator;
        public final int captureButton;
        public final int captureMethodIndicator;
        public final int contentView;
        public final int geotagIndicator;
        public final int modeButtonShortcut;
        public final int mruShortcut;
        public final int predictiveCaptureIndicator;
        public final int primarySettingShortcut;
        public final int recordingProgress;
        public final int sceneIndicator;
        public final int secondarySettingShortcut;
        public final int storageIndicator;
        public final int thermalIndicator;
        public final int zoombar;

        public Visibilities(int[] iArr) {
            this.captureButton = iArr[0];
            this.captureMethodIndicator = iArr[1];
            this.sceneIndicator = iArr[2];
            this.geotagIndicator = iArr[3];
            this.storageIndicator = iArr[4];
            this.zoombar = iArr[5];
            this.contentView = iArr[6];
            this.recordingProgress = iArr[7];
            this.thermalIndicator = iArr[8];
            this.primarySettingShortcut = iArr[9];
            this.secondarySettingShortcut = iArr[10];
            this.batteryIndicator = iArr[11];
            this.predictiveCaptureIndicator = iArr[12];
            this.modeButtonShortcut = iArr[13];
            this.mruShortcut = iArr[14];
        }
    }

    private void setupVisibilities(boolean z) {
        this.mMap.clear();
        if (z) {
            set(BaseLayoutPattern.PREVIEW, 1, 1, 2, 1, 1, 3, 2, 2, 1, 1, 0, 1, 2, 1, 1);
            set(BaseLayoutPattern.PREVIEW_NO_RECORDING, 1, 1, 2, 1, 1, 3, 2, 2, 1, 1, 0, 1, 2, 1, 1);
            set(BaseLayoutPattern.CLEAR, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2);
            set(BaseLayoutPattern.ZOOMING, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2);
            set(BaseLayoutPattern.ZOOMING_IN_RECORDING, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2);
            set(BaseLayoutPattern.ZOOMING_IN_PAUSE_RECORDING, BaseLayoutPattern.ZOOMING_IN_RECORDING);
            set(BaseLayoutPattern.FOCUS_SEARCHING, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2);
            set(BaseLayoutPattern.FOCUS_DONE, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2);
            set(BaseLayoutPattern.CAPTURE, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2);
            set(BaseLayoutPattern.RECORDING, 1, 1, 2, 2, 1, 3, 2, 1, 1, 2, 2, 1, 2, 2, 2);
            set(BaseLayoutPattern.BURST_SHOOTING, 0, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 1, 2, 2, 2);
            set(BaseLayoutPattern.SETTING, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 2, 2, 1, 0);
            set(BaseLayoutPattern.SELFTIMER, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2);
            set(BaseLayoutPattern.PAUSE_RECORDING, 1, 1, 2, 2, 1, 3, 2, 1, 1, 2, 2, 1, 2, 2, 2);
            set(BaseLayoutPattern.OVERLAY_CONTROL_SEEKING, 1, 1, 2, 1, 1, 2, 2, 2, 1, 1, 2, 1, 2, 2, 2);
            set(BaseLayoutPattern.HIGH_FRAME_RATE_RECORDING_IN_SUPER_SLOW_MOTION, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2);
            set(BaseLayoutPattern.MODE_CHANGING, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
            return;
        }
        set(BaseLayoutPattern.PREVIEW, 1, 1, 1, 1, 1, 3, 1, 2, 1, 1, 0, 1, 1, 1, 1);
        set(BaseLayoutPattern.PREVIEW_NO_RECORDING, 1, 1, 2, 1, 1, 3, 1, 2, 1, 1, 0, 1, 1, 1, 1);
        set(BaseLayoutPattern.CLEAR, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2);
        set(BaseLayoutPattern.ZOOMING, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2);
        set(BaseLayoutPattern.ZOOMING_IN_RECORDING, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2);
        set(BaseLayoutPattern.ZOOMING_IN_PAUSE_RECORDING, BaseLayoutPattern.ZOOMING_IN_RECORDING);
        set(BaseLayoutPattern.FOCUS_SEARCHING, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2);
        set(BaseLayoutPattern.FOCUS_DONE, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2);
        set(BaseLayoutPattern.CAPTURE, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2);
        set(BaseLayoutPattern.RECORDING, 1, 1, 1, 2, 1, 3, 1, 1, 1, 2, 2, 1, 2, 2, 2);
        set(BaseLayoutPattern.BURST_SHOOTING, 0, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 1, 2, 2, 2);
        set(BaseLayoutPattern.SETTING, 1, 2, 2, 2, 2, 2, 1, 2, 2, 1, 0, 2, 2, 1, 0);
        set(BaseLayoutPattern.SELFTIMER, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2);
        set(BaseLayoutPattern.PAUSE_RECORDING, 1, 1, 1, 2, 1, 3, 2, 1, 1, 2, 2, 1, 2, 2, 2);
        set(BaseLayoutPattern.OVERLAY_CONTROL_SEEKING, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 0, 1, 2, 1, 1);
        set(BaseLayoutPattern.HIGH_FRAME_RATE_RECORDING_IN_SUPER_SLOW_MOTION, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2);
        set(BaseLayoutPattern.MODE_CHANGING, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    private void set(LayoutPattern layoutPattern, int... iArr) {
        this.mMap.put(layoutPattern, new Visibilities(iArr));
    }

    private void set(LayoutPattern layoutPattern, LayoutPattern layoutPattern2) {
        this.mMap.put(layoutPattern, this.mMap.get(layoutPattern2));
    }
}
