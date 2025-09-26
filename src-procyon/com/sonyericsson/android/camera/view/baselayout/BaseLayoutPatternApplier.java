// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.baselayout;

import java.util.HashMap;
import com.sonyericsson.cameracommon.viewfinder.LayoutPattern;
import java.util.Map;

public class BaseLayoutPatternApplier implements LayoutPatternApplier
{
    public static final int D = 3;
    public static final int H = 2;
    public static final int N = 0;
    public static final int S = 1;
    private BaseLayout mLayout;
    private final Map<LayoutPattern, Visibilities> mMap;
    
    public BaseLayoutPatternApplier() {
        this.mMap = new HashMap<LayoutPattern, Visibilities>();
    }
    
    private void set(final LayoutPattern layoutPattern, final LayoutPattern layoutPattern2) {
        this.mMap.put(layoutPattern, this.mMap.get(layoutPattern2));
    }
    
    private void set(final LayoutPattern layoutPattern, final int... array) {
        this.mMap.put(layoutPattern, new Visibilities(array));
    }
    
    private void setupVisibilities(final boolean b) {
        this.mMap.clear();
        if (b) {
            this.set(BaseLayoutPattern.PREVIEW, 1, 1, 2, 1, 1, 3, 2, 2, 1, 1, 0, 1, 2, 1, 1);
            this.set(BaseLayoutPattern.PREVIEW_NO_RECORDING, 1, 1, 2, 1, 1, 3, 2, 2, 1, 1, 0, 1, 2, 1, 1);
            this.set(BaseLayoutPattern.CLEAR, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2);
            this.set(BaseLayoutPattern.ZOOMING, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2);
            this.set(BaseLayoutPattern.ZOOMING_IN_RECORDING, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2);
            this.set(BaseLayoutPattern.ZOOMING_IN_PAUSE_RECORDING, BaseLayoutPattern.ZOOMING_IN_RECORDING);
            this.set(BaseLayoutPattern.FOCUS_SEARCHING, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2);
            this.set(BaseLayoutPattern.FOCUS_DONE, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2);
            this.set(BaseLayoutPattern.CAPTURE, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2);
            this.set(BaseLayoutPattern.RECORDING, 1, 1, 2, 2, 1, 3, 2, 1, 1, 2, 2, 1, 2, 2, 2);
            this.set(BaseLayoutPattern.BURST_SHOOTING, 0, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 1, 2, 2, 2);
            this.set(BaseLayoutPattern.SETTING, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 2, 2, 1, 0);
            this.set(BaseLayoutPattern.SELFTIMER, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2);
            this.set(BaseLayoutPattern.PAUSE_RECORDING, 1, 1, 2, 2, 1, 3, 2, 1, 1, 2, 2, 1, 2, 2, 2);
            this.set(BaseLayoutPattern.OVERLAY_CONTROL_SEEKING, 1, 1, 2, 1, 1, 2, 2, 2, 1, 1, 2, 1, 2, 2, 2);
            this.set(BaseLayoutPattern.HIGH_FRAME_RATE_RECORDING_IN_SUPER_SLOW_MOTION, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2);
            this.set(BaseLayoutPattern.MODE_CHANGING, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        }
        else {
            this.set(BaseLayoutPattern.PREVIEW, 1, 1, 1, 1, 1, 3, 1, 2, 1, 1, 0, 1, 1, 1, 1);
            this.set(BaseLayoutPattern.PREVIEW_NO_RECORDING, 1, 1, 2, 1, 1, 3, 1, 2, 1, 1, 0, 1, 1, 1, 1);
            this.set(BaseLayoutPattern.CLEAR, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2);
            this.set(BaseLayoutPattern.ZOOMING, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2);
            this.set(BaseLayoutPattern.ZOOMING_IN_RECORDING, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2);
            this.set(BaseLayoutPattern.ZOOMING_IN_PAUSE_RECORDING, BaseLayoutPattern.ZOOMING_IN_RECORDING);
            this.set(BaseLayoutPattern.FOCUS_SEARCHING, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2);
            this.set(BaseLayoutPattern.FOCUS_DONE, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2);
            this.set(BaseLayoutPattern.CAPTURE, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2);
            this.set(BaseLayoutPattern.RECORDING, 1, 1, 1, 2, 1, 3, 1, 1, 1, 2, 2, 1, 2, 2, 2);
            this.set(BaseLayoutPattern.BURST_SHOOTING, 0, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 1, 2, 2, 2);
            this.set(BaseLayoutPattern.SETTING, 1, 2, 2, 2, 2, 2, 1, 2, 2, 1, 0, 2, 2, 1, 0);
            this.set(BaseLayoutPattern.SELFTIMER, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2);
            this.set(BaseLayoutPattern.PAUSE_RECORDING, 1, 1, 1, 2, 1, 3, 2, 1, 1, 2, 2, 1, 2, 2, 2);
            this.set(BaseLayoutPattern.OVERLAY_CONTROL_SEEKING, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 0, 1, 2, 1, 1);
            this.set(BaseLayoutPattern.HIGH_FRAME_RATE_RECORDING_IN_SUPER_SLOW_MOTION, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2);
            this.set(BaseLayoutPattern.MODE_CHANGING, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        }
    }
    
    @Override
    public void apply(final LayoutPattern layoutPattern) {
        final Visibilities visibilities = this.mMap.get(layoutPattern);
        switch (visibilities.captureButton) {
            default: {
                if (this.mLayout.getOnScreenButtonGroup().isMainButtonTouched()) {
                    this.mLayout.getOnScreenButtonGroup().setVisibility(0);
                    break;
                }
                this.mLayout.getOnScreenButtonGroup().setVisibility(4);
                break;
            }
            case 2: {
                this.mLayout.getOnScreenButtonGroup().setVisibility(4);
                break;
            }
            case 1: {
                this.mLayout.getOnScreenButtonGroup().setVisibility(0);
                break;
            }
        }
        switch (visibilities.contentView) {
            case 2: {
                this.mLayout.hideContentsViewController();
                break;
            }
            case 1: {
                this.mLayout.showContentsViewController();
                break;
            }
        }
        switch (visibilities.modeButtonShortcut) {
            case 2: {
                this.mLayout.getModeButtonShortcut().hide();
                break;
            }
            case 1: {
                this.mLayout.getModeButtonShortcut().show();
                break;
            }
        }
        switch (visibilities.mruShortcut) {
            case 2: {
                this.mLayout.getMruButtonContainer().hide();
                break;
            }
            case 1: {
                this.mLayout.getMruButtonContainer().show();
                break;
            }
        }
        switch (visibilities.captureMethodIndicator) {
            case 2: {
                this.mLayout.getPhotoSmileCaptureIndicator().hide();
                break;
            }
            case 1: {
                this.mLayout.getPhotoSmileCaptureIndicator().show();
                break;
            }
        }
        switch (visibilities.sceneIndicator) {
            case 2: {
                this.mLayout.getSceneIndicator().hide();
                this.mLayout.getConditionIndicator().hide();
                break;
            }
            case 1: {
                this.mLayout.getSceneIndicator().show();
                this.mLayout.getConditionIndicator().show();
                break;
            }
        }
        switch (visibilities.geotagIndicator) {
            case 2: {
                this.mLayout.getGeoTagIndicator().hide();
                break;
            }
            case 1: {
                this.mLayout.getGeoTagIndicator().show();
                break;
            }
        }
        switch (visibilities.storageIndicator) {
            case 2: {
                this.mLayout.getLowMemoryInternalIndicator().hide();
                this.mLayout.getLowMemorySdIndicator().hide();
                break;
            }
            case 1: {
                this.mLayout.getLowMemoryInternalIndicator().show();
                this.mLayout.getLowMemorySdIndicator().show();
                break;
            }
        }
        switch (visibilities.zoombar) {
            case 3: {
                this.mLayout.getZoomBar().hideDelayed();
                break;
            }
            case 2: {
                this.mLayout.getZoomBar().hideImmediately();
                break;
            }
            case 1: {
                this.mLayout.getZoomBar().show();
                break;
            }
        }
        switch (visibilities.recordingProgress) {
            case 2: {
                this.mLayout.getRecordingIndicator().setVisibility(4);
                break;
            }
            case 1: {
                this.mLayout.getRecordingIndicator().setVisibility(0);
                break;
            }
        }
        switch (visibilities.thermalIndicator) {
            case 2: {
                this.mLayout.getThermalIndicator().hide();
                break;
            }
            case 1: {
                this.mLayout.getThermalIndicator().show();
                break;
            }
        }
        switch (visibilities.primarySettingShortcut) {
            case 2: {
                this.mLayout.getPrimaryShortcut().hide();
                break;
            }
            case 1: {
                this.mLayout.getPrimaryShortcut().show();
                break;
            }
        }
        switch (visibilities.secondarySettingShortcut) {
            case 2: {
                this.mLayout.getOnScreenSubButton().setVisibility(4);
                this.mLayout.getOnScreenExtraButton().setVisibility(4);
                break;
            }
            case 1: {
                this.mLayout.getOnScreenSubButton().setVisibility(0);
                this.mLayout.getOnScreenExtraButton().setVisibility(0);
                break;
            }
        }
        switch (visibilities.batteryIndicator) {
            case 2: {
                this.mLayout.getBatteryIndicator().hide();
                break;
            }
            case 1: {
                this.mLayout.getBatteryIndicator().show();
                break;
            }
        }
        switch (visibilities.predictiveCaptureIndicator) {
            case 2: {
                this.mLayout.getPredictiveCaptureIndicatorController().hide();
                break;
            }
            case 1: {
                this.mLayout.getPredictiveCaptureIndicatorController().show();
                break;
            }
        }
        this.mLayout.refresh();
    }
    
    @Override
    public void setup(final BaseLayout mLayout, final boolean b) {
        this.mLayout = mLayout;
        this.setupVisibilities(b);
    }
    
    private static class Visibilities
    {
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
        
        public Visibilities(final int[] array) {
            this.captureButton = array[0];
            this.captureMethodIndicator = array[1];
            this.sceneIndicator = array[2];
            this.geotagIndicator = array[3];
            this.storageIndicator = array[4];
            this.zoombar = array[5];
            this.contentView = array[6];
            this.recordingProgress = array[7];
            this.thermalIndicator = array[8];
            this.primarySettingShortcut = array[9];
            this.secondarySettingShortcut = array[10];
            this.batteryIndicator = array[11];
            this.predictiveCaptureIndicator = array[12];
            this.modeButtonShortcut = array[13];
            this.mruShortcut = array[14];
        }
    }
}
