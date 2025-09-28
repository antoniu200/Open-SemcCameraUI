package com.sonymobile.cameracommon.research.parameters;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class Event {
    public static final String TAG = "Event";

    public interface Action {
    }

    public enum AddonFW implements Action {
        ADD_BUTTON_PRESSED,
        APP_SELECTED_ON_MODE_SELECTOR
    }

    public enum AutoPowerOffAction implements Action {
        DEFAULT,
        ON_LOCKSCREEN,
        LIFT_TRIGGER,
        LIFT_TRIGGER_DIALOG
    }

    public enum CaptureTrigger implements Action {
        CAMERA_KEY,
        CAPTURE_BUTTON,
        TOUCH_CAPTURE,
        VOLUME_KEY,
        FAST_CAPTURING_LAUNCH,
        SMILE_CAPTURE,
        SELF_TIMER,
        WEARABLE,
        GESTURE,
        SIDE_SENSE,
        TOUCH_CAPTURE_PREDICTIVE_LAUNCH,
        OTHER
    }

    public enum Category {
        ADDON_FW,
        THERMAL_MITIGATION,
        CAMERA_NOT_AVAILABLE,
        CAPTURE_OPERATION,
        PANORAMA,
        RECORDING,
        SETTINGS_PHOTO,
        SETTINGS_VIDEO,
        SETTINGS_COMMON,
        ALL_SETTINGS_PHOTO,
        ALL_SETTINGS_VIDEO,
        CHANGED_SETTING,
        SELFTIMER_CANCELLED,
        LOWBATTERY_MITIGATION,
        SLOW_MOTION,
        PREDICTIVE_LAUNCH,
        AUTO_POWEROFF
    }

    public enum CoolMode implements Action {
        HEATED_OVER_COOLING_LOW,
        HEATED_OVER_COOLING_ULTRA_LOW,
        HEATED_OVER_COOLING_LOW_ON_STARTUP,
        HEATED_OVER_COOLING_ULTRA_LOW_ON_STARTUP
    }

    public interface Label {
    }

    public enum PredictiveLaunchAction implements Action {
        TOUCH_UP,
        HW_CAMERA_KEY,
        VOLUME_KEY,
        SIDE_SENSING,
        OTHER
    }

    public enum SelfTimerTrigger implements Action {
        NORMAL,
        GESTURE,
        SIDE_SENSE
    }

    public interface UserOperation extends Action {
        Category getCategory();

        int getValue();

        UserOperation getViewer();

        boolean isShooting();

        UserOperation updateOperation(UserOperation userOperation);
    }

    public enum WizardPage implements Action {
        LOCATION_WIZARD,
        PREDICTIVE_LAUNCH_WIZARD,
        SIDE_SENSING_WIZARD,
        EYE_POSITION_WIZARD,
        HAND_SHUTTER_WIZARD,
        SUPER_SLOWMOTION_WIZARD1,
        SUPER_SLOWMOTION_WIZARD2,
        SUPER_SLOWMOTION_WIZARD3,
        SUPER_SLOWMOTION_WIZARD4,
        ONE_SHOT_WIZARD,
        SLOWMOTION_WIZARD1,
        SLOWMOTION_WIZARD2,
        READMORE_SUPER_SLOWMOTION_WIZARD1,
        READMORE_SUPER_SLOWMOTION_WIZARD2,
        READMORE_SUPER_SLOWMOTION_WIZARD3,
        READMORE_ONE_SHOT_WIZARD,
        READMORE_SLOWMOTION_WIZARD1,
        READMORE_SLOWMOTION_WIZARD2,
        SUPERIOR_AUTO_FUSION_WIZARD,
        VIDEO_FUSION_WIZARD1,
        VIDEO_FUSION_WIZARD2,
        MANUAL_FUSION_WIZARD1,
        MANUAL_FUSION_WIZARD2,
        UNKNOWN
    }

    public enum WizardResult implements Label {
        YES,
        NO,
        GOT_IT,
        PREVIOUS,
        NEXT,
        BACK_KEY,
        SKIP,
        OTHER
    }

    public enum ThermalMitigation implements Action {
        FAIL_TO_START,
        FORCE_QUIT;

        public static ThermalMitigation getType(boolean z) {
            return z ? FAIL_TO_START : FORCE_QUIT;
        }
    }

    public enum LowBatteryMitigation implements Action {
        FAIL_TO_START,
        FORCE_QUIT;

        public static LowBatteryMitigation getType(boolean z) {
            return z ? FAIL_TO_START : FORCE_QUIT;
        }
    }

    public enum CameraNotAvailable implements Action {
        FAILED_TO_OPEN,
        OTHER;

        public static CameraNotAvailable getType(boolean z) {
            return z ? FAILED_TO_OPEN : OTHER;
        }
    }

    public enum StopOperation implements Action {
        USER_STOP,
        THERMAL_STOP,
        LOWBATTERY_STOP,
        SIDE_SENSE_STOP;

        public static StopOperation getType(boolean z, boolean z2) {
            if (z) {
                return THERMAL_STOP;
            }
            if (z2) {
                return LOWBATTERY_STOP;
            }
            return USER_STOP;
        }
    }

    public enum CaptureOperation implements UserOperation {
        EMPTY(0, false),
        SHOOTING(1, true),
        RECORDING(2, false),
        SHOOTING_RECORDING(SHOOTING.mValue | RECORDING.mValue, false),
        BURST(4, false),
        SHOOTING_BURST(SHOOTING.mValue | BURST.mValue, false),
        RECORDING_BURST(RECORDING.mValue | BURST.mValue, false),
        SHOOTING_RECORDING_BURST((SHOOTING.mValue | RECORDING.mValue) | BURST.mValue, false),
        VIEWER(8, false),
        SHOOTING_VIEWER(SHOOTING.mValue | VIEWER.mValue, false),
        RECORDING_VIEWER(RECORDING.mValue | VIEWER.mValue, false),
        SHOOTING_RECORDING_VIEWER((SHOOTING.mValue | RECORDING.mValue) | VIEWER.mValue, false),
        BURST_VIEWER(BURST.mValue | VIEWER.mValue, false),
        SHOOTING_BURST_VIEWER((SHOOTING.mValue | BURST.mValue) | VIEWER.mValue, false),
        RECORDING_BURST_VIEWER((RECORDING.mValue | BURST.mValue) | VIEWER.mValue, false),
        SHOOTING_RECORDING_BURST_VIEWER(((SHOOTING.mValue | RECORDING.mValue) | BURST.mValue) | VIEWER.mValue, false);

        private final boolean mIsShooting;
        private final int mValue;

        CaptureOperation(int i, boolean z) {
            this.mValue = i;
            this.mIsShooting = z;
        }

        @Override // com.sonymobile.cameracommon.research.parameters.Event.UserOperation
        public int getValue() {
            return this.mValue;
        }

        @Override // com.sonymobile.cameracommon.research.parameters.Event.UserOperation
        public boolean isShooting() {
            return this.mIsShooting;
        }

        @Override // com.sonymobile.cameracommon.research.parameters.Event.UserOperation
        public UserOperation getViewer() {
            return VIEWER;
        }

        @Override // com.sonymobile.cameracommon.research.parameters.Event.UserOperation
        public Category getCategory() {
            return Category.CAPTURE_OPERATION;
        }

        @Override // com.sonymobile.cameracommon.research.parameters.Event.UserOperation
        public UserOperation updateOperation(UserOperation userOperation) {
            int value = this.mValue | userOperation.getValue();
            for (CaptureOperation captureOperation : values()) {
                if (captureOperation.mValue == value) {
                    return captureOperation;
                }
            }
            return null;
        }
    }

    public enum ForceQuit implements Label {
        DURING_PREVIEW,
        DURING_RECORDING;

        public static ForceQuit getType(boolean z) {
            return z ? DURING_RECORDING : DURING_PREVIEW;
        }
    }

    public enum ViewerLaunched {
        NOT_LAUNCHED(0),
        LAUNCHED(1);

        public final int mValue;

        ViewerLaunched(int i) {
            this.mValue = i;
        }

        public static ViewerLaunched getType(CaptureOperation captureOperation) {
            return captureOperation == null ? NOT_LAUNCHED : LAUNCHED;
        }
    }

    public enum TimeFromAfDoneToCaptureStart {
        WITHIN_10_MS,
        WITHIN_50_MS,
        WITHIN_100_MS,
        WITHIN_200_MS,
        WITHIN_500_MS,
        WITHIN_1000_MS,
        WITHIN_1500_MS,
        WITHIN_2000_MS,
        OVER_2000_MS,
        CONTINUOUS_CAPTURE,
        NOT_TARGET;

        public static TimeFromAfDoneToCaptureStart getType(long j) {
            if (j >= 2000) {
                return OVER_2000_MS;
            }
            if (j >= 1500) {
                return WITHIN_2000_MS;
            }
            if (j >= 1000) {
                return WITHIN_1500_MS;
            }
            if (j >= 500) {
                return WITHIN_1000_MS;
            }
            if (j >= 200) {
                return WITHIN_500_MS;
            }
            if (j >= 100) {
                return WITHIN_200_MS;
            }
            if (j >= 50) {
                return WITHIN_100_MS;
            }
            if (j >= 10) {
                return WITHIN_50_MS;
            }
            return WITHIN_10_MS;
        }
    }
}
