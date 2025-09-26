package com.sonymobile.cameracommon.research.parameters;

import com.sonymobile.cameracommon.research.parameters.Event;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ShootingLabel {
    public static final String PREDICTIVE_LAUNCH_OFF = "False";
    public static final String PREDICTIVE_LAUNCH_ON = "True";
    public static final String RECOGNIZED_SCENE_ACTION = "action";
    public static final String RECOGNIZED_SCENE_AUTO = "auto";
    public static final String RECOGNIZED_SCENE_BABY = "baby";
    public static final String RECOGNIZED_SCENE_BACKLIGHT = "backlight";
    public static final String RECOGNIZED_SCENE_BACKLIGHT_PORTRAIT = "backlight-portrait";
    public static final String RECOGNIZED_SCENE_BEACH = "beach";
    public static final String RECOGNIZED_SCENE_CANDLELIGHT = "candlelight";
    public static final String RECOGNIZED_SCENE_DARK = "dark";
    public static final String RECOGNIZED_SCENE_DISH = "dish";
    public static final String RECOGNIZED_SCENE_DOCUMENT = "document";
    public static final String RECOGNIZED_SCENE_FIREWORKS = "fireworks";
    public static final String RECOGNIZED_SCENE_LANDSCAPE = "landscape";
    public static final String RECOGNIZED_SCENE_MACRO = "macro";
    public static final String RECOGNIZED_SCENE_NIGHT = "night";
    public static final String RECOGNIZED_SCENE_NIGHT_PORTRAIT = "night-portrait";
    public static final String RECOGNIZED_SCENE_PARTY = "party";
    public static final String RECOGNIZED_SCENE_PORTRAIT = "portrait";
    public static final String RECOGNIZED_SCENE_SNOW = "snow";
    public static final String RECOGNIZED_SCENE_SPORTS = "sports";
    public static final String RECOGNIZED_SCENE_SPOTLIGHT = "spot-light";
    public static final String RECOGNIZED_SCENE_STEADYPHOTO = "steadyphoto";
    public static final String RECOGNIZED_SCENE_SUNSET = "sunset";
    public static final String RECOGNIZED_SCENE_THEATRE = "theatre";
    public static final int SEMI_AUTO_OFF = 1;
    public static final int SEMI_AUTO_ON = 0;
    public static final String TAG = "ShootingLabel";

    private interface IntParameter extends Parameter {
        boolean equals(int i);

        Parameter getDefaultValue();
    }

    public interface Parameter {
    }

    private interface StringParameter extends Parameter {
        boolean equals(String str);

        Parameter getDefaultValue();
    }

    public static Parameter getOrientationParameter(int i) {
        return getIntParameter(i, Orientation.values());
    }

    public static Parameter getZoomParameter(int i) {
        return getIntParameter(i, Zoom.values());
    }

    public static Parameter getHandSignLostParameter(int i) {
        return getIntParameter(i, HandSignLostNum.values());
    }

    public static Parameter getFaceNumParameter(int i) {
        return getIntParameter(i, FaceNum.values());
    }

    public static Parameter getRecognizedSceneParameter(String str) {
        return getStringParameter(str, RecognizedScene.values());
    }

    public static Parameter getCaptureTriggerParameter(String str) {
        return getStringParameter(str, CaptureTrigger.values());
    }

    public static Parameter getFlashParameter(String str) {
        return getStringParameter(str, Flash.values());
    }

    public static Parameter getSelfTimerParameter(String str) {
        return getStringParameter(str, SelfTimer.values());
    }

    public static Parameter getSemiAutoParameter(int i) {
        return getIntParameter(i, SemiAuto.values());
    }

    public static Parameter getFrontAngleParameter(String str) {
        return getStringParameter(str, FrontAngleValue.values());
    }

    public static Parameter getAfDoneKeepingTimeParameter(String str) {
        return getStringParameter(str, AfDoneKeepingTime.values());
    }

    public static Parameter getPredictiveCaptureNumParameter(int i) {
        return getIntParameter(i, PredictiveCaptureNum.values());
    }

    public static Parameter getObjectTrackingParameter(String str) {
        return getStringParameter(str, ObjectTracking.values());
    }

    public static Parameter getPredictiveLaunchParameter(String str) {
        return getStringParameter(str, PredictiveLaunch.values());
    }

    public static Parameter getIntParameter(int i, IntParameter[] intParameterArr) {
        for (IntParameter intParameter : intParameterArr) {
            if (intParameter.equals(i)) {
                return intParameter;
            }
        }
        return intParameterArr[0].getDefaultValue();
    }

    public static Parameter getStringParameter(String str, StringParameter[] stringParameterArr) {
        for (StringParameter stringParameter : stringParameterArr) {
            if (stringParameter.equals(str)) {
                return stringParameter;
            }
        }
        return stringParameterArr[0].getDefaultValue();
    }

    private enum Orientation implements IntParameter {
        ORIENTATION_0(0),
        ORIENTATION_90(90),
        ORIENTATION_180(180),
        ORIENTATION_270(270);

        private int mValue;

        Orientation(int i) {
            this.mValue = i;
        }

        @Override // com.sonymobile.cameracommon.research.parameters.ShootingLabel.IntParameter
        public boolean equals(int i) {
            return this.mValue == i;
        }

        @Override // com.sonymobile.cameracommon.research.parameters.ShootingLabel.IntParameter
        public Parameter getDefaultValue() {
            return ORIENTATION_0;
        }
    }

    private enum Zoom implements IntParameter {
        ZOOM_0(0),
        ZOOM_1(1),
        ZOOM_2(2),
        ZOOM_3(3),
        ZOOM_4(4),
        ZOOM_5(5),
        ZOOM_6(6),
        ZOOM_7(7),
        ZOOM_8(8);

        private int mValue;

        Zoom(int i) {
            this.mValue = i;
        }

        @Override // com.sonymobile.cameracommon.research.parameters.ShootingLabel.IntParameter
        public boolean equals(int i) {
            return this.mValue == i;
        }

        @Override // com.sonymobile.cameracommon.research.parameters.ShootingLabel.IntParameter
        public Parameter getDefaultValue() {
            return ZOOM_0;
        }
    }

    private enum HandSignLostNum implements IntParameter {
        NA(-1, -1),
        ZERO(0, 0),
        ONE(1, 1),
        TWO_TO_FOUR(2, 4),
        FIVE_AND_MORE(5, Integer.MAX_VALUE);

        private final int mMaxNum;
        private final int mMinNum;

        HandSignLostNum(int i, int i2) {
            this.mMinNum = i;
            this.mMaxNum = i2;
        }

        @Override // com.sonymobile.cameracommon.research.parameters.ShootingLabel.IntParameter
        public boolean equals(int i) {
            return i >= this.mMinNum && i <= this.mMaxNum;
        }

        @Override // com.sonymobile.cameracommon.research.parameters.ShootingLabel.IntParameter
        public Parameter getDefaultValue() {
            return NA;
        }
    }

    private enum FaceNum implements IntParameter {
        FACE_NUM_0(0),
        FACE_NUM_1(1),
        FACE_NUM_2(2),
        FACE_NUM_3(3),
        FACE_NUM_4(4),
        FACE_NUM_5(5);

        private int mValue;

        FaceNum(int i) {
            this.mValue = i;
        }

        @Override // com.sonymobile.cameracommon.research.parameters.ShootingLabel.IntParameter
        public boolean equals(int i) {
            return this.mValue == i;
        }

        @Override // com.sonymobile.cameracommon.research.parameters.ShootingLabel.IntParameter
        public Parameter getDefaultValue() {
            return FACE_NUM_0;
        }
    }

    private enum RecognizedScene implements StringParameter {
        AUTO("auto"),
        ACTION("action"),
        PORTRAIT("portrait"),
        LANDSCAPE("landscape"),
        NIGHT("night"),
        NIGHT_PORTRAIT("night-portrait"),
        THEATRE("theatre"),
        BEACH("beach"),
        SNOW("snow"),
        SUNSET("sunset"),
        STEADYPHOTO("steadyphoto"),
        FIREWORKS("fireworks"),
        SPORTS("sports"),
        PARTY("party"),
        CANDLELIGHT("candlelight"),
        DOCUMENT("document"),
        BACKLIGHT("backlight"),
        BACKLIGHT_PORTRAIT("backlight-portrait"),
        DARK("dark"),
        BABY("baby"),
        SPOTLIGHT("spot-light"),
        DISH("dish"),
        MACRO(ShootingLabel.RECOGNIZED_SCENE_MACRO);

        private String mValue;

        RecognizedScene(String str) {
            this.mValue = str;
        }

        @Override // com.sonymobile.cameracommon.research.parameters.ShootingLabel.StringParameter
        public boolean equals(String str) {
            return this.mValue.equals(str);
        }

        @Override // com.sonymobile.cameracommon.research.parameters.ShootingLabel.StringParameter
        public Parameter getDefaultValue() {
            return AUTO;
        }
    }

    private enum CaptureTrigger implements StringParameter {
        CAMERA_KEY(Event.CaptureTrigger.CAMERA_KEY.toString()),
        CAPTURE_BUTTON(Event.CaptureTrigger.CAPTURE_BUTTON.toString()),
        TOUCH_CAPTURE(Event.CaptureTrigger.TOUCH_CAPTURE.toString()),
        VOLUME_KEY(Event.CaptureTrigger.VOLUME_KEY.toString()),
        FAST_CAPTURING_LAUNCH(Event.CaptureTrigger.FAST_CAPTURING_LAUNCH.toString()),
        SMILE_CAPTURE(Event.CaptureTrigger.SMILE_CAPTURE.toString()),
        SELF_TIMER(Event.CaptureTrigger.SELF_TIMER.toString()),
        WEARABLE(Event.CaptureTrigger.WEARABLE.toString()),
        GESTURE(Event.CaptureTrigger.GESTURE.toString()),
        SIDE_SENSE(Event.CaptureTrigger.SIDE_SENSE.toString()),
        TOUCH_CAPTURE_PREDICTIVE_LAUNCH(Event.CaptureTrigger.TOUCH_CAPTURE_PREDICTIVE_LAUNCH.toString()),
        OTHER(Event.CaptureTrigger.OTHER.toString());

        private String mValue;

        CaptureTrigger(String str) {
            this.mValue = str;
        }

        @Override // com.sonymobile.cameracommon.research.parameters.ShootingLabel.StringParameter
        public boolean equals(String str) {
            return this.mValue.equals(str);
        }

        @Override // com.sonymobile.cameracommon.research.parameters.ShootingLabel.StringParameter
        public Parameter getDefaultValue() {
            return OTHER;
        }
    }

    private enum Flash implements StringParameter {
        AUTO("AUTO"),
        ON("ON"),
        RED_EYE("RED_EYE"),
        OFF("OFF"),
        LED_ON("LED_ON"),
        LED_OFF("LED_OFF");

        private String mValue;

        Flash(String str) {
            this.mValue = str;
        }

        @Override // com.sonymobile.cameracommon.research.parameters.ShootingLabel.StringParameter
        public boolean equals(String str) {
            return this.mValue.equals(str);
        }

        @Override // com.sonymobile.cameracommon.research.parameters.ShootingLabel.StringParameter
        public Parameter getDefaultValue() {
            return OFF;
        }
    }

    private enum SelfTimer implements StringParameter {
        LONG("LONG"),
        SHORT("SHORT"),
        INSTANT("INSTANT"),
        OFF("OFF");

        private String mValue;

        SelfTimer(String str) {
            this.mValue = str;
        }

        @Override // com.sonymobile.cameracommon.research.parameters.ShootingLabel.StringParameter
        public boolean equals(String str) {
            return this.mValue.equals(str);
        }

        @Override // com.sonymobile.cameracommon.research.parameters.ShootingLabel.StringParameter
        public Parameter getDefaultValue() {
            return OFF;
        }
    }

    private enum SemiAuto implements IntParameter {
        ON(0),
        OFF(1);

        private int mValue;

        SemiAuto(int i) {
            this.mValue = i;
        }

        @Override // com.sonymobile.cameracommon.research.parameters.ShootingLabel.IntParameter
        public boolean equals(int i) {
            return this.mValue == i;
        }

        @Override // com.sonymobile.cameracommon.research.parameters.ShootingLabel.IntParameter
        public Parameter getDefaultValue() {
            return OFF;
        }
    }

    private enum AfDoneKeepingTime implements StringParameter {
        WITHIN_10_MS("WITHIN_10_MS"),
        WITHIN_50_MS("WITHIN_50_MS"),
        WITHIN_100_MS("WITHIN_100_MS"),
        WITHIN_200_MS("WITHIN_200_MS"),
        WITHIN_500_MS("WITHIN_500_MS"),
        WITHIN_1000_MS("WITHIN_1000_MS"),
        WITHIN_1500_MS("WITHIN_1500_MS"),
        WITHIN_2000_MS("WITHIN_2000_MS"),
        OVER_2000_MS("OVER_2000_MS"),
        CONTINUOUS_CAPTURE("CONTINUOUS_CAPTURE");

        private String mValue;

        AfDoneKeepingTime(String str) {
            this.mValue = str;
        }

        @Override // com.sonymobile.cameracommon.research.parameters.ShootingLabel.StringParameter
        public boolean equals(String str) {
            return this.mValue.equals(str);
        }

        @Override // com.sonymobile.cameracommon.research.parameters.ShootingLabel.StringParameter
        public Parameter getDefaultValue() {
            return WITHIN_10_MS;
        }
    }

    private enum PredictiveCaptureNum implements IntParameter {
        PREDICTIVE_CAPTURE_OFF(0),
        PREDICTIVE_CAPTURE_NUM_1(1),
        PREDICTIVE_CAPTURE_NUM_2(2),
        PREDICTIVE_CAPTURE_NUM_3(3),
        PREDICTIVE_CAPTURE_NUM_4(4),
        PREDICTIVE_CAPTURE_NUM_5(5),
        PREDICTIVE_CAPTURE_NUM_6(6),
        PREDICTIVE_CAPTURE_NUM_7(7),
        PREDICTIVE_CAPTURE_NUM_8(8);

        private int mValue;

        PredictiveCaptureNum(int i) {
            this.mValue = i;
        }

        @Override // com.sonymobile.cameracommon.research.parameters.ShootingLabel.IntParameter
        public boolean equals(int i) {
            return this.mValue == i;
        }

        @Override // com.sonymobile.cameracommon.research.parameters.ShootingLabel.IntParameter
        public Parameter getDefaultValue() {
            return PREDICTIVE_CAPTURE_OFF;
        }
    }

    private enum ObjectTracking implements StringParameter {
        OBJECT_TRACKING_OFF("OFF_OFF"),
        OBJECT_TRACKING_AUTO_TARGET_OFF("ON_OFF"),
        OBJECT_TRACKING_AUTO_TARGET_ON("ON_ON");

        private String mValue;

        ObjectTracking(String str) {
            this.mValue = str;
        }

        @Override // com.sonymobile.cameracommon.research.parameters.ShootingLabel.StringParameter
        public boolean equals(String str) {
            return this.mValue.equals(str);
        }

        @Override // com.sonymobile.cameracommon.research.parameters.ShootingLabel.StringParameter
        public Parameter getDefaultValue() {
            return OBJECT_TRACKING_AUTO_TARGET_OFF;
        }
    }

    private enum FrontAngleValue implements StringParameter {
        DEFAULT("DEFAULT"),
        CROPPED("CROPPED"),
        INVALID("INVALID");

        private String mValue;

        FrontAngleValue(String str) {
            this.mValue = str;
        }

        @Override // com.sonymobile.cameracommon.research.parameters.ShootingLabel.StringParameter
        public boolean equals(String str) {
            return this.mValue.equals(str);
        }

        @Override // com.sonymobile.cameracommon.research.parameters.ShootingLabel.StringParameter
        public Parameter getDefaultValue() {
            return DEFAULT;
        }
    }

    private enum PredictiveLaunch implements StringParameter {
        True(ShootingLabel.PREDICTIVE_LAUNCH_ON),
        False(ShootingLabel.PREDICTIVE_LAUNCH_OFF);

        private String mValue;

        PredictiveLaunch(String str) {
            this.mValue = str;
        }

        @Override // com.sonymobile.cameracommon.research.parameters.ShootingLabel.StringParameter
        public boolean equals(String str) {
            return this.mValue.equals(str);
        }

        @Override // com.sonymobile.cameracommon.research.parameters.ShootingLabel.StringParameter
        public Parameter getDefaultValue() {
            return False;
        }
    }
}
