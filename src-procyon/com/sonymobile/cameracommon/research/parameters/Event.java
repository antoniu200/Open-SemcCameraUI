// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.cameracommon.research.parameters;

public class Event
{
    public static final String TAG = "Event";
    
    public interface Action
    {
    }
    
    public enum AddonFW implements Action
    {
        private static final AddonFW[] $VALUES;
        
        ADD_BUTTON_PRESSED, 
        APP_SELECTED_ON_MODE_SELECTOR;
        
        static {
            $VALUES = new AddonFW[] { AddonFW.ADD_BUTTON_PRESSED, AddonFW.APP_SELECTED_ON_MODE_SELECTOR };
        }
    }
    
    public enum AutoPowerOffAction implements Action
    {
        private static final AutoPowerOffAction[] $VALUES;
        
        DEFAULT, 
        LIFT_TRIGGER, 
        LIFT_TRIGGER_DIALOG, 
        ON_LOCKSCREEN;
        
        static {
            $VALUES = new AutoPowerOffAction[] { AutoPowerOffAction.DEFAULT, AutoPowerOffAction.ON_LOCKSCREEN, AutoPowerOffAction.LIFT_TRIGGER, AutoPowerOffAction.LIFT_TRIGGER_DIALOG };
        }
    }
    
    public enum CameraNotAvailable implements Action
    {
        private static final CameraNotAvailable[] $VALUES;
        
        FAILED_TO_OPEN, 
        OTHER;
        
        static {
            $VALUES = new CameraNotAvailable[] { CameraNotAvailable.FAILED_TO_OPEN, CameraNotAvailable.OTHER };
        }
        
        public static CameraNotAvailable getType(final boolean b) {
            CameraNotAvailable cameraNotAvailable;
            if (b) {
                cameraNotAvailable = CameraNotAvailable.FAILED_TO_OPEN;
            }
            else {
                cameraNotAvailable = CameraNotAvailable.OTHER;
            }
            return cameraNotAvailable;
        }
    }
    
    public enum CaptureOperation implements UserOperation
    {
        private static final CaptureOperation[] $VALUES;
        
        BURST(4, false), 
        BURST_VIEWER(CaptureOperation.BURST.mValue | CaptureOperation.VIEWER.mValue, false), 
        EMPTY(0, false), 
        RECORDING(2, false), 
        RECORDING_BURST(CaptureOperation.RECORDING.mValue | CaptureOperation.BURST.mValue, false), 
        RECORDING_BURST_VIEWER(CaptureOperation.RECORDING.mValue | CaptureOperation.BURST.mValue | CaptureOperation.VIEWER.mValue, false), 
        RECORDING_VIEWER(CaptureOperation.RECORDING.mValue | CaptureOperation.VIEWER.mValue, false), 
        SHOOTING(1, true), 
        SHOOTING_BURST(CaptureOperation.SHOOTING.mValue | CaptureOperation.BURST.mValue, false), 
        SHOOTING_BURST_VIEWER(CaptureOperation.SHOOTING.mValue | CaptureOperation.BURST.mValue | CaptureOperation.VIEWER.mValue, false), 
        SHOOTING_RECORDING(CaptureOperation.SHOOTING.mValue | CaptureOperation.RECORDING.mValue, false), 
        SHOOTING_RECORDING_BURST(CaptureOperation.SHOOTING.mValue | CaptureOperation.RECORDING.mValue | CaptureOperation.BURST.mValue, false), 
        SHOOTING_RECORDING_BURST_VIEWER(CaptureOperation.SHOOTING.mValue | CaptureOperation.RECORDING.mValue | CaptureOperation.BURST.mValue | CaptureOperation.VIEWER.mValue, false), 
        SHOOTING_RECORDING_VIEWER(CaptureOperation.SHOOTING.mValue | CaptureOperation.RECORDING.mValue | CaptureOperation.VIEWER.mValue, false), 
        SHOOTING_VIEWER(CaptureOperation.SHOOTING.mValue | CaptureOperation.VIEWER.mValue, false), 
        VIEWER(8, false);
        
        private final boolean mIsShooting;
        private final int mValue;
        
        static {
            $VALUES = new CaptureOperation[] { CaptureOperation.EMPTY, CaptureOperation.SHOOTING, CaptureOperation.RECORDING, CaptureOperation.SHOOTING_RECORDING, CaptureOperation.BURST, CaptureOperation.SHOOTING_BURST, CaptureOperation.RECORDING_BURST, CaptureOperation.SHOOTING_RECORDING_BURST, CaptureOperation.VIEWER, CaptureOperation.SHOOTING_VIEWER, CaptureOperation.RECORDING_VIEWER, CaptureOperation.SHOOTING_RECORDING_VIEWER, CaptureOperation.BURST_VIEWER, CaptureOperation.SHOOTING_BURST_VIEWER, CaptureOperation.RECORDING_BURST_VIEWER, CaptureOperation.SHOOTING_RECORDING_BURST_VIEWER };
        }
        
        private CaptureOperation(final int mValue, final boolean mIsShooting) {
            this.mValue = mValue;
            this.mIsShooting = mIsShooting;
        }
        
        @Override
        public Category getCategory() {
            return Category.CAPTURE_OPERATION;
        }
        
        @Override
        public int getValue() {
            return this.mValue;
        }
        
        @Override
        public UserOperation getViewer() {
            return CaptureOperation.VIEWER;
        }
        
        @Override
        public boolean isShooting() {
            return this.mIsShooting;
        }
        
        @Override
        public UserOperation updateOperation(final UserOperation userOperation) {
            final int value = userOperation.getValue();
            final int mValue = this.mValue;
            for (final CaptureOperation captureOperation : values()) {
                if (captureOperation.mValue == (mValue | value)) {
                    return captureOperation;
                }
            }
            return null;
        }
    }
    
    public enum CaptureTrigger implements Action
    {
        private static final CaptureTrigger[] $VALUES;
        
        CAMERA_KEY, 
        CAPTURE_BUTTON, 
        FAST_CAPTURING_LAUNCH, 
        GESTURE, 
        OTHER, 
        SELF_TIMER, 
        SIDE_SENSE, 
        SMILE_CAPTURE, 
        TOUCH_CAPTURE, 
        TOUCH_CAPTURE_PREDICTIVE_LAUNCH, 
        VOLUME_KEY, 
        WEARABLE;
        
        static {
            $VALUES = new CaptureTrigger[] { CaptureTrigger.CAMERA_KEY, CaptureTrigger.CAPTURE_BUTTON, CaptureTrigger.TOUCH_CAPTURE, CaptureTrigger.VOLUME_KEY, CaptureTrigger.FAST_CAPTURING_LAUNCH, CaptureTrigger.SMILE_CAPTURE, CaptureTrigger.SELF_TIMER, CaptureTrigger.WEARABLE, CaptureTrigger.GESTURE, CaptureTrigger.SIDE_SENSE, CaptureTrigger.TOUCH_CAPTURE_PREDICTIVE_LAUNCH, CaptureTrigger.OTHER };
        }
    }
    
    public enum Category
    {
        private static final Category[] $VALUES;
        
        ADDON_FW, 
        ALL_SETTINGS_PHOTO, 
        ALL_SETTINGS_VIDEO, 
        AUTO_POWEROFF, 
        CAMERA_NOT_AVAILABLE, 
        CAPTURE_OPERATION, 
        CHANGED_SETTING, 
        LOWBATTERY_MITIGATION, 
        PANORAMA, 
        PREDICTIVE_LAUNCH, 
        RECORDING, 
        SELFTIMER_CANCELLED, 
        SETTINGS_COMMON, 
        SETTINGS_PHOTO, 
        SETTINGS_VIDEO, 
        SLOW_MOTION, 
        THERMAL_MITIGATION;
        
        static {
            $VALUES = new Category[] { Category.ADDON_FW, Category.THERMAL_MITIGATION, Category.CAMERA_NOT_AVAILABLE, Category.CAPTURE_OPERATION, Category.PANORAMA, Category.RECORDING, Category.SETTINGS_PHOTO, Category.SETTINGS_VIDEO, Category.SETTINGS_COMMON, Category.ALL_SETTINGS_PHOTO, Category.ALL_SETTINGS_VIDEO, Category.CHANGED_SETTING, Category.SELFTIMER_CANCELLED, Category.LOWBATTERY_MITIGATION, Category.SLOW_MOTION, Category.PREDICTIVE_LAUNCH, Category.AUTO_POWEROFF };
        }
    }
    
    public enum CoolMode implements Action
    {
        private static final CoolMode[] $VALUES;
        
        HEATED_OVER_COOLING_LOW, 
        HEATED_OVER_COOLING_LOW_ON_STARTUP, 
        HEATED_OVER_COOLING_ULTRA_LOW, 
        HEATED_OVER_COOLING_ULTRA_LOW_ON_STARTUP;
        
        static {
            $VALUES = new CoolMode[] { CoolMode.HEATED_OVER_COOLING_LOW, CoolMode.HEATED_OVER_COOLING_ULTRA_LOW, CoolMode.HEATED_OVER_COOLING_LOW_ON_STARTUP, CoolMode.HEATED_OVER_COOLING_ULTRA_LOW_ON_STARTUP };
        }
    }
    
    public enum ForceQuit implements Label
    {
        private static final ForceQuit[] $VALUES;
        
        DURING_PREVIEW, 
        DURING_RECORDING;
        
        static {
            $VALUES = new ForceQuit[] { ForceQuit.DURING_PREVIEW, ForceQuit.DURING_RECORDING };
        }
        
        public static ForceQuit getType(final boolean b) {
            ForceQuit forceQuit;
            if (b) {
                forceQuit = ForceQuit.DURING_RECORDING;
            }
            else {
                forceQuit = ForceQuit.DURING_PREVIEW;
            }
            return forceQuit;
        }
    }
    
    public interface Label
    {
    }
    
    public enum LowBatteryMitigation implements Action
    {
        private static final LowBatteryMitigation[] $VALUES;
        
        FAIL_TO_START, 
        FORCE_QUIT;
        
        static {
            $VALUES = new LowBatteryMitigation[] { LowBatteryMitigation.FAIL_TO_START, LowBatteryMitigation.FORCE_QUIT };
        }
        
        public static LowBatteryMitigation getType(final boolean b) {
            LowBatteryMitigation lowBatteryMitigation;
            if (b) {
                lowBatteryMitigation = LowBatteryMitigation.FAIL_TO_START;
            }
            else {
                lowBatteryMitigation = LowBatteryMitigation.FORCE_QUIT;
            }
            return lowBatteryMitigation;
        }
    }
    
    public enum PredictiveLaunchAction implements Action
    {
        private static final PredictiveLaunchAction[] $VALUES;
        
        HW_CAMERA_KEY, 
        OTHER, 
        SIDE_SENSING, 
        TOUCH_UP, 
        VOLUME_KEY;
        
        static {
            $VALUES = new PredictiveLaunchAction[] { PredictiveLaunchAction.TOUCH_UP, PredictiveLaunchAction.HW_CAMERA_KEY, PredictiveLaunchAction.VOLUME_KEY, PredictiveLaunchAction.SIDE_SENSING, PredictiveLaunchAction.OTHER };
        }
    }
    
    public enum SelfTimerTrigger implements Action
    {
        private static final SelfTimerTrigger[] $VALUES;
        
        GESTURE, 
        NORMAL, 
        SIDE_SENSE;
        
        static {
            $VALUES = new SelfTimerTrigger[] { SelfTimerTrigger.NORMAL, SelfTimerTrigger.GESTURE, SelfTimerTrigger.SIDE_SENSE };
        }
    }
    
    public enum StopOperation implements Action
    {
        private static final StopOperation[] $VALUES;
        
        LOWBATTERY_STOP, 
        SIDE_SENSE_STOP, 
        THERMAL_STOP, 
        USER_STOP;
        
        static {
            $VALUES = new StopOperation[] { StopOperation.USER_STOP, StopOperation.THERMAL_STOP, StopOperation.LOWBATTERY_STOP, StopOperation.SIDE_SENSE_STOP };
        }
        
        public static StopOperation getType(final boolean b, final boolean b2) {
            if (b) {
                return StopOperation.THERMAL_STOP;
            }
            if (b2) {
                return StopOperation.LOWBATTERY_STOP;
            }
            return StopOperation.USER_STOP;
        }
    }
    
    public enum ThermalMitigation implements Action
    {
        private static final ThermalMitigation[] $VALUES;
        
        FAIL_TO_START, 
        FORCE_QUIT;
        
        static {
            $VALUES = new ThermalMitigation[] { ThermalMitigation.FAIL_TO_START, ThermalMitigation.FORCE_QUIT };
        }
        
        public static ThermalMitigation getType(final boolean b) {
            ThermalMitigation thermalMitigation;
            if (b) {
                thermalMitigation = ThermalMitigation.FAIL_TO_START;
            }
            else {
                thermalMitigation = ThermalMitigation.FORCE_QUIT;
            }
            return thermalMitigation;
        }
    }
    
    public enum TimeFromAfDoneToCaptureStart
    {
        private static final TimeFromAfDoneToCaptureStart[] $VALUES;
        
        CONTINUOUS_CAPTURE, 
        NOT_TARGET, 
        OVER_2000_MS, 
        WITHIN_1000_MS, 
        WITHIN_100_MS, 
        WITHIN_10_MS, 
        WITHIN_1500_MS, 
        WITHIN_2000_MS, 
        WITHIN_200_MS, 
        WITHIN_500_MS, 
        WITHIN_50_MS;
        
        static {
            $VALUES = new TimeFromAfDoneToCaptureStart[] { TimeFromAfDoneToCaptureStart.WITHIN_10_MS, TimeFromAfDoneToCaptureStart.WITHIN_50_MS, TimeFromAfDoneToCaptureStart.WITHIN_100_MS, TimeFromAfDoneToCaptureStart.WITHIN_200_MS, TimeFromAfDoneToCaptureStart.WITHIN_500_MS, TimeFromAfDoneToCaptureStart.WITHIN_1000_MS, TimeFromAfDoneToCaptureStart.WITHIN_1500_MS, TimeFromAfDoneToCaptureStart.WITHIN_2000_MS, TimeFromAfDoneToCaptureStart.OVER_2000_MS, TimeFromAfDoneToCaptureStart.CONTINUOUS_CAPTURE, TimeFromAfDoneToCaptureStart.NOT_TARGET };
        }
        
        public static TimeFromAfDoneToCaptureStart getType(final long n) {
            if (n >= 2000L) {
                return TimeFromAfDoneToCaptureStart.OVER_2000_MS;
            }
            if (n >= 1500L) {
                return TimeFromAfDoneToCaptureStart.WITHIN_2000_MS;
            }
            if (n >= 1000L) {
                return TimeFromAfDoneToCaptureStart.WITHIN_1500_MS;
            }
            if (n >= 500L) {
                return TimeFromAfDoneToCaptureStart.WITHIN_1000_MS;
            }
            if (n >= 200L) {
                return TimeFromAfDoneToCaptureStart.WITHIN_500_MS;
            }
            if (n >= 100L) {
                return TimeFromAfDoneToCaptureStart.WITHIN_200_MS;
            }
            if (n >= 50L) {
                return TimeFromAfDoneToCaptureStart.WITHIN_100_MS;
            }
            if (n >= 10L) {
                return TimeFromAfDoneToCaptureStart.WITHIN_50_MS;
            }
            return TimeFromAfDoneToCaptureStart.WITHIN_10_MS;
        }
    }
    
    public interface UserOperation extends Action
    {
        Category getCategory();
        
        int getValue();
        
        UserOperation getViewer();
        
        boolean isShooting();
        
        UserOperation updateOperation(final UserOperation p0);
    }
    
    public enum ViewerLaunched
    {
        private static final ViewerLaunched[] $VALUES;
        
        LAUNCHED(1), 
        NOT_LAUNCHED(0);
        
        public final int mValue;
        
        static {
            $VALUES = new ViewerLaunched[] { ViewerLaunched.NOT_LAUNCHED, ViewerLaunched.LAUNCHED };
        }
        
        private ViewerLaunched(final int mValue) {
            this.mValue = mValue;
        }
        
        public static ViewerLaunched getType(final CaptureOperation captureOperation) {
            ViewerLaunched viewerLaunched;
            if (captureOperation == null) {
                viewerLaunched = ViewerLaunched.NOT_LAUNCHED;
            }
            else {
                viewerLaunched = ViewerLaunched.LAUNCHED;
            }
            return viewerLaunched;
        }
    }
    
    public enum WizardPage implements Action
    {
        private static final WizardPage[] $VALUES;
        
        EYE_POSITION_WIZARD, 
        HAND_SHUTTER_WIZARD, 
        LOCATION_WIZARD, 
        MANUAL_FUSION_WIZARD1, 
        MANUAL_FUSION_WIZARD2, 
        ONE_SHOT_WIZARD, 
        PREDICTIVE_LAUNCH_WIZARD, 
        READMORE_ONE_SHOT_WIZARD, 
        READMORE_SLOWMOTION_WIZARD1, 
        READMORE_SLOWMOTION_WIZARD2, 
        READMORE_SUPER_SLOWMOTION_WIZARD1, 
        READMORE_SUPER_SLOWMOTION_WIZARD2, 
        READMORE_SUPER_SLOWMOTION_WIZARD3, 
        SIDE_SENSING_WIZARD, 
        SLOWMOTION_WIZARD1, 
        SLOWMOTION_WIZARD2, 
        SUPERIOR_AUTO_FUSION_WIZARD, 
        SUPER_SLOWMOTION_WIZARD1, 
        SUPER_SLOWMOTION_WIZARD2, 
        SUPER_SLOWMOTION_WIZARD3, 
        SUPER_SLOWMOTION_WIZARD4, 
        UNKNOWN, 
        VIDEO_FUSION_WIZARD1, 
        VIDEO_FUSION_WIZARD2;
        
        static {
            $VALUES = new WizardPage[] { WizardPage.LOCATION_WIZARD, WizardPage.PREDICTIVE_LAUNCH_WIZARD, WizardPage.SIDE_SENSING_WIZARD, WizardPage.EYE_POSITION_WIZARD, WizardPage.HAND_SHUTTER_WIZARD, WizardPage.SUPER_SLOWMOTION_WIZARD1, WizardPage.SUPER_SLOWMOTION_WIZARD2, WizardPage.SUPER_SLOWMOTION_WIZARD3, WizardPage.SUPER_SLOWMOTION_WIZARD4, WizardPage.ONE_SHOT_WIZARD, WizardPage.SLOWMOTION_WIZARD1, WizardPage.SLOWMOTION_WIZARD2, WizardPage.READMORE_SUPER_SLOWMOTION_WIZARD1, WizardPage.READMORE_SUPER_SLOWMOTION_WIZARD2, WizardPage.READMORE_SUPER_SLOWMOTION_WIZARD3, WizardPage.READMORE_ONE_SHOT_WIZARD, WizardPage.READMORE_SLOWMOTION_WIZARD1, WizardPage.READMORE_SLOWMOTION_WIZARD2, WizardPage.SUPERIOR_AUTO_FUSION_WIZARD, WizardPage.VIDEO_FUSION_WIZARD1, WizardPage.VIDEO_FUSION_WIZARD2, WizardPage.MANUAL_FUSION_WIZARD1, WizardPage.MANUAL_FUSION_WIZARD2, WizardPage.UNKNOWN };
        }
    }
    
    public enum WizardResult implements Label
    {
        private static final WizardResult[] $VALUES;
        
        BACK_KEY, 
        GOT_IT, 
        NEXT, 
        NO, 
        OTHER, 
        PREVIOUS, 
        SKIP, 
        YES;
        
        static {
            $VALUES = new WizardResult[] { WizardResult.YES, WizardResult.NO, WizardResult.GOT_IT, WizardResult.PREVIOUS, WizardResult.NEXT, WizardResult.BACK_KEY, WizardResult.SKIP, WizardResult.OTHER };
        }
    }
}
