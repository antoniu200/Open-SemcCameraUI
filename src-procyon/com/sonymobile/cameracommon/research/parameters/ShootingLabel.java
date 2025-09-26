// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.cameracommon.research.parameters;

public class ShootingLabel
{
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
    
    public static Parameter getAfDoneKeepingTimeParameter(final String s) {
        return getStringParameter(s, (StringParameter[])AfDoneKeepingTime.values());
    }
    
    public static Parameter getCaptureTriggerParameter(final String s) {
        return getStringParameter(s, (StringParameter[])CaptureTrigger.values());
    }
    
    public static Parameter getFaceNumParameter(final int n) {
        return getIntParameter(n, (IntParameter[])FaceNum.values());
    }
    
    public static Parameter getFlashParameter(final String s) {
        return getStringParameter(s, (StringParameter[])Flash.values());
    }
    
    public static Parameter getFrontAngleParameter(final String s) {
        return getStringParameter(s, (StringParameter[])FrontAngleValue.values());
    }
    
    public static Parameter getHandSignLostParameter(final int n) {
        return getIntParameter(n, (IntParameter[])HandSignLostNum.values());
    }
    
    public static Parameter getIntParameter(final int n, final IntParameter[] array) {
        for (final IntParameter intParameter : array) {
            if (intParameter.equals(n)) {
                return (Parameter)intParameter;
            }
        }
        return array[0].getDefaultValue();
    }
    
    public static Parameter getObjectTrackingParameter(final String s) {
        return getStringParameter(s, (StringParameter[])ObjectTracking.values());
    }
    
    public static Parameter getOrientationParameter(final int n) {
        return getIntParameter(n, (IntParameter[])Orientation.values());
    }
    
    public static Parameter getPredictiveCaptureNumParameter(final int n) {
        return getIntParameter(n, (IntParameter[])PredictiveCaptureNum.values());
    }
    
    public static Parameter getPredictiveLaunchParameter(final String s) {
        return getStringParameter(s, (StringParameter[])PredictiveLaunch.values());
    }
    
    public static Parameter getRecognizedSceneParameter(final String s) {
        return getStringParameter(s, (StringParameter[])RecognizedScene.values());
    }
    
    public static Parameter getSelfTimerParameter(final String s) {
        return getStringParameter(s, (StringParameter[])SelfTimer.values());
    }
    
    public static Parameter getSemiAutoParameter(final int n) {
        return getIntParameter(n, (IntParameter[])SemiAuto.values());
    }
    
    public static Parameter getStringParameter(final String s, final StringParameter[] array) {
        for (final StringParameter stringParameter : array) {
            if (stringParameter.equals(s)) {
                return (Parameter)stringParameter;
            }
        }
        return array[0].getDefaultValue();
    }
    
    public static Parameter getZoomParameter(final int n) {
        return getIntParameter(n, (IntParameter[])Zoom.values());
    }
    
    private enum AfDoneKeepingTime implements StringParameter
    {
        private static final AfDoneKeepingTime[] $VALUES;
        
        CONTINUOUS_CAPTURE("CONTINUOUS_CAPTURE"), 
        OVER_2000_MS("OVER_2000_MS"), 
        WITHIN_1000_MS("WITHIN_1000_MS"), 
        WITHIN_100_MS("WITHIN_100_MS"), 
        WITHIN_10_MS("WITHIN_10_MS"), 
        WITHIN_1500_MS("WITHIN_1500_MS"), 
        WITHIN_2000_MS("WITHIN_2000_MS"), 
        WITHIN_200_MS("WITHIN_200_MS"), 
        WITHIN_500_MS("WITHIN_500_MS"), 
        WITHIN_50_MS("WITHIN_50_MS");
        
        private String mValue;
        
        static {
            $VALUES = new AfDoneKeepingTime[] { AfDoneKeepingTime.WITHIN_10_MS, AfDoneKeepingTime.WITHIN_50_MS, AfDoneKeepingTime.WITHIN_100_MS, AfDoneKeepingTime.WITHIN_200_MS, AfDoneKeepingTime.WITHIN_500_MS, AfDoneKeepingTime.WITHIN_1000_MS, AfDoneKeepingTime.WITHIN_1500_MS, AfDoneKeepingTime.WITHIN_2000_MS, AfDoneKeepingTime.OVER_2000_MS, AfDoneKeepingTime.CONTINUOUS_CAPTURE };
        }
        
        private AfDoneKeepingTime(final String mValue) {
            this.mValue = mValue;
        }
        
        @Override
        public boolean equals(final String anObject) {
            return this.mValue.equals(anObject);
        }
        
        @Override
        public Parameter getDefaultValue() {
            return AfDoneKeepingTime.WITHIN_10_MS;
        }
    }
    
    private enum CaptureTrigger implements StringParameter
    {
        private static final CaptureTrigger[] $VALUES;
        
        CAMERA_KEY(Event.CaptureTrigger.CAMERA_KEY.toString()), 
        CAPTURE_BUTTON(Event.CaptureTrigger.CAPTURE_BUTTON.toString()), 
        FAST_CAPTURING_LAUNCH(Event.CaptureTrigger.FAST_CAPTURING_LAUNCH.toString()), 
        GESTURE(Event.CaptureTrigger.GESTURE.toString()), 
        OTHER(Event.CaptureTrigger.OTHER.toString()), 
        SELF_TIMER(Event.CaptureTrigger.SELF_TIMER.toString()), 
        SIDE_SENSE(Event.CaptureTrigger.SIDE_SENSE.toString()), 
        SMILE_CAPTURE(Event.CaptureTrigger.SMILE_CAPTURE.toString()), 
        TOUCH_CAPTURE(Event.CaptureTrigger.TOUCH_CAPTURE.toString()), 
        TOUCH_CAPTURE_PREDICTIVE_LAUNCH(Event.CaptureTrigger.TOUCH_CAPTURE_PREDICTIVE_LAUNCH.toString()), 
        VOLUME_KEY(Event.CaptureTrigger.VOLUME_KEY.toString()), 
        WEARABLE(Event.CaptureTrigger.WEARABLE.toString());
        
        private String mValue;
        
        static {
            $VALUES = new CaptureTrigger[] { CaptureTrigger.CAMERA_KEY, CaptureTrigger.CAPTURE_BUTTON, CaptureTrigger.TOUCH_CAPTURE, CaptureTrigger.VOLUME_KEY, CaptureTrigger.FAST_CAPTURING_LAUNCH, CaptureTrigger.SMILE_CAPTURE, CaptureTrigger.SELF_TIMER, CaptureTrigger.WEARABLE, CaptureTrigger.GESTURE, CaptureTrigger.SIDE_SENSE, CaptureTrigger.TOUCH_CAPTURE_PREDICTIVE_LAUNCH, CaptureTrigger.OTHER };
        }
        
        private CaptureTrigger(final String mValue) {
            this.mValue = mValue;
        }
        
        @Override
        public boolean equals(final String anObject) {
            return this.mValue.equals(anObject);
        }
        
        @Override
        public Parameter getDefaultValue() {
            return CaptureTrigger.OTHER;
        }
    }
    
    private enum FaceNum implements IntParameter
    {
        private static final FaceNum[] $VALUES;
        
        FACE_NUM_0(0), 
        FACE_NUM_1(1), 
        FACE_NUM_2(2), 
        FACE_NUM_3(3), 
        FACE_NUM_4(4), 
        FACE_NUM_5(5);
        
        private int mValue;
        
        static {
            $VALUES = new FaceNum[] { FaceNum.FACE_NUM_0, FaceNum.FACE_NUM_1, FaceNum.FACE_NUM_2, FaceNum.FACE_NUM_3, FaceNum.FACE_NUM_4, FaceNum.FACE_NUM_5 };
        }
        
        private FaceNum(final int mValue) {
            this.mValue = mValue;
        }
        
        @Override
        public boolean equals(final int n) {
            return this.mValue == n;
        }
        
        @Override
        public Parameter getDefaultValue() {
            return FaceNum.FACE_NUM_0;
        }
    }
    
    private enum Flash implements StringParameter
    {
        private static final Flash[] $VALUES;
        
        AUTO("AUTO"), 
        LED_OFF("LED_OFF"), 
        LED_ON("LED_ON"), 
        OFF("OFF"), 
        ON("ON"), 
        RED_EYE("RED_EYE");
        
        private String mValue;
        
        static {
            $VALUES = new Flash[] { Flash.AUTO, Flash.ON, Flash.RED_EYE, Flash.OFF, Flash.LED_ON, Flash.LED_OFF };
        }
        
        private Flash(final String mValue) {
            this.mValue = mValue;
        }
        
        @Override
        public boolean equals(final String anObject) {
            return this.mValue.equals(anObject);
        }
        
        @Override
        public Parameter getDefaultValue() {
            return Flash.OFF;
        }
    }
    
    private enum FrontAngleValue implements StringParameter
    {
        private static final FrontAngleValue[] $VALUES;
        
        CROPPED("CROPPED"), 
        DEFAULT("DEFAULT"), 
        INVALID("INVALID");
        
        private String mValue;
        
        static {
            $VALUES = new FrontAngleValue[] { FrontAngleValue.DEFAULT, FrontAngleValue.CROPPED, FrontAngleValue.INVALID };
        }
        
        private FrontAngleValue(final String mValue) {
            this.mValue = mValue;
        }
        
        @Override
        public boolean equals(final String anObject) {
            return this.mValue.equals(anObject);
        }
        
        @Override
        public Parameter getDefaultValue() {
            return FrontAngleValue.DEFAULT;
        }
    }
    
    private enum HandSignLostNum implements IntParameter
    {
        private static final HandSignLostNum[] $VALUES;
        
        FIVE_AND_MORE(5, Integer.MAX_VALUE), 
        NA(-1, -1), 
        ONE(1, 1), 
        TWO_TO_FOUR(2, 4), 
        ZERO(0, 0);
        
        private final int mMaxNum;
        private final int mMinNum;
        
        static {
            $VALUES = new HandSignLostNum[] { HandSignLostNum.NA, HandSignLostNum.ZERO, HandSignLostNum.ONE, HandSignLostNum.TWO_TO_FOUR, HandSignLostNum.FIVE_AND_MORE };
        }
        
        private HandSignLostNum(final int mMinNum, final int mMaxNum) {
            this.mMinNum = mMinNum;
            this.mMaxNum = mMaxNum;
        }
        
        @Override
        public boolean equals(final int n) {
            return n >= this.mMinNum && n <= this.mMaxNum;
        }
        
        @Override
        public Parameter getDefaultValue() {
            return HandSignLostNum.NA;
        }
    }
    
    private interface IntParameter extends Parameter
    {
        boolean equals(final int p0);
        
        Parameter getDefaultValue();
    }
    
    private enum ObjectTracking implements StringParameter
    {
        private static final ObjectTracking[] $VALUES;
        
        OBJECT_TRACKING_AUTO_TARGET_OFF("ON_OFF"), 
        OBJECT_TRACKING_AUTO_TARGET_ON("ON_ON"), 
        OBJECT_TRACKING_OFF("OFF_OFF");
        
        private String mValue;
        
        static {
            $VALUES = new ObjectTracking[] { ObjectTracking.OBJECT_TRACKING_OFF, ObjectTracking.OBJECT_TRACKING_AUTO_TARGET_OFF, ObjectTracking.OBJECT_TRACKING_AUTO_TARGET_ON };
        }
        
        private ObjectTracking(final String mValue) {
            this.mValue = mValue;
        }
        
        @Override
        public boolean equals(final String anObject) {
            return this.mValue.equals(anObject);
        }
        
        @Override
        public Parameter getDefaultValue() {
            return ObjectTracking.OBJECT_TRACKING_AUTO_TARGET_OFF;
        }
    }
    
    private enum Orientation implements IntParameter
    {
        private static final Orientation[] $VALUES;
        
        ORIENTATION_0(0), 
        ORIENTATION_180(180), 
        ORIENTATION_270(270), 
        ORIENTATION_90(90);
        
        private int mValue;
        
        static {
            $VALUES = new Orientation[] { Orientation.ORIENTATION_0, Orientation.ORIENTATION_90, Orientation.ORIENTATION_180, Orientation.ORIENTATION_270 };
        }
        
        private Orientation(final int mValue) {
            this.mValue = mValue;
        }
        
        @Override
        public boolean equals(final int n) {
            return this.mValue == n;
        }
        
        @Override
        public Parameter getDefaultValue() {
            return Orientation.ORIENTATION_0;
        }
    }
    
    public interface Parameter
    {
    }
    
    private enum PredictiveCaptureNum implements IntParameter
    {
        private static final PredictiveCaptureNum[] $VALUES;
        
        PREDICTIVE_CAPTURE_NUM_1(1), 
        PREDICTIVE_CAPTURE_NUM_2(2), 
        PREDICTIVE_CAPTURE_NUM_3(3), 
        PREDICTIVE_CAPTURE_NUM_4(4), 
        PREDICTIVE_CAPTURE_NUM_5(5), 
        PREDICTIVE_CAPTURE_NUM_6(6), 
        PREDICTIVE_CAPTURE_NUM_7(7), 
        PREDICTIVE_CAPTURE_NUM_8(8), 
        PREDICTIVE_CAPTURE_OFF(0);
        
        private int mValue;
        
        static {
            $VALUES = new PredictiveCaptureNum[] { PredictiveCaptureNum.PREDICTIVE_CAPTURE_OFF, PredictiveCaptureNum.PREDICTIVE_CAPTURE_NUM_1, PredictiveCaptureNum.PREDICTIVE_CAPTURE_NUM_2, PredictiveCaptureNum.PREDICTIVE_CAPTURE_NUM_3, PredictiveCaptureNum.PREDICTIVE_CAPTURE_NUM_4, PredictiveCaptureNum.PREDICTIVE_CAPTURE_NUM_5, PredictiveCaptureNum.PREDICTIVE_CAPTURE_NUM_6, PredictiveCaptureNum.PREDICTIVE_CAPTURE_NUM_7, PredictiveCaptureNum.PREDICTIVE_CAPTURE_NUM_8 };
        }
        
        private PredictiveCaptureNum(final int mValue) {
            this.mValue = mValue;
        }
        
        @Override
        public boolean equals(final int n) {
            return this.mValue == n;
        }
        
        @Override
        public Parameter getDefaultValue() {
            return PredictiveCaptureNum.PREDICTIVE_CAPTURE_OFF;
        }
    }
    
    private enum PredictiveLaunch implements StringParameter
    {
        private static final PredictiveLaunch[] $VALUES;
        
        False("False"), 
        True("True");
        
        private String mValue;
        
        static {
            $VALUES = new PredictiveLaunch[] { PredictiveLaunch.True, PredictiveLaunch.False };
        }
        
        private PredictiveLaunch(final String mValue) {
            this.mValue = mValue;
        }
        
        @Override
        public boolean equals(final String anObject) {
            return this.mValue.equals(anObject);
        }
        
        @Override
        public Parameter getDefaultValue() {
            return PredictiveLaunch.False;
        }
    }
    
    private enum RecognizedScene implements StringParameter
    {
        private static final RecognizedScene[] $VALUES;
        
        ACTION("action"), 
        AUTO("auto"), 
        BABY("baby"), 
        BACKLIGHT("backlight"), 
        BACKLIGHT_PORTRAIT("backlight-portrait"), 
        BEACH("beach"), 
        CANDLELIGHT("candlelight"), 
        DARK("dark"), 
        DISH("dish"), 
        DOCUMENT("document"), 
        FIREWORKS("fireworks"), 
        LANDSCAPE("landscape"), 
        MACRO("macro"), 
        NIGHT("night"), 
        NIGHT_PORTRAIT("night-portrait"), 
        PARTY("party"), 
        PORTRAIT("portrait"), 
        SNOW("snow"), 
        SPORTS("sports"), 
        SPOTLIGHT("spot-light"), 
        STEADYPHOTO("steadyphoto"), 
        SUNSET("sunset"), 
        THEATRE("theatre");
        
        private String mValue;
        
        static {
            $VALUES = new RecognizedScene[] { RecognizedScene.AUTO, RecognizedScene.ACTION, RecognizedScene.PORTRAIT, RecognizedScene.LANDSCAPE, RecognizedScene.NIGHT, RecognizedScene.NIGHT_PORTRAIT, RecognizedScene.THEATRE, RecognizedScene.BEACH, RecognizedScene.SNOW, RecognizedScene.SUNSET, RecognizedScene.STEADYPHOTO, RecognizedScene.FIREWORKS, RecognizedScene.SPORTS, RecognizedScene.PARTY, RecognizedScene.CANDLELIGHT, RecognizedScene.DOCUMENT, RecognizedScene.BACKLIGHT, RecognizedScene.BACKLIGHT_PORTRAIT, RecognizedScene.DARK, RecognizedScene.BABY, RecognizedScene.SPOTLIGHT, RecognizedScene.DISH, RecognizedScene.MACRO };
        }
        
        private RecognizedScene(final String mValue) {
            this.mValue = mValue;
        }
        
        @Override
        public boolean equals(final String anObject) {
            return this.mValue.equals(anObject);
        }
        
        @Override
        public Parameter getDefaultValue() {
            return RecognizedScene.AUTO;
        }
    }
    
    private enum SelfTimer implements StringParameter
    {
        private static final SelfTimer[] $VALUES;
        
        INSTANT("INSTANT"), 
        LONG("LONG"), 
        OFF("OFF"), 
        SHORT("SHORT");
        
        private String mValue;
        
        static {
            $VALUES = new SelfTimer[] { SelfTimer.LONG, SelfTimer.SHORT, SelfTimer.INSTANT, SelfTimer.OFF };
        }
        
        private SelfTimer(final String mValue) {
            this.mValue = mValue;
        }
        
        @Override
        public boolean equals(final String anObject) {
            return this.mValue.equals(anObject);
        }
        
        @Override
        public Parameter getDefaultValue() {
            return SelfTimer.OFF;
        }
    }
    
    private enum SemiAuto implements IntParameter
    {
        private static final SemiAuto[] $VALUES;
        
        OFF(1), 
        ON(0);
        
        private int mValue;
        
        static {
            $VALUES = new SemiAuto[] { SemiAuto.ON, SemiAuto.OFF };
        }
        
        private SemiAuto(final int mValue) {
            this.mValue = mValue;
        }
        
        @Override
        public boolean equals(final int n) {
            return this.mValue == n;
        }
        
        @Override
        public Parameter getDefaultValue() {
            return SemiAuto.OFF;
        }
    }
    
    private interface StringParameter extends Parameter
    {
        boolean equals(final String p0);
        
        Parameter getDefaultValue();
    }
    
    private enum Zoom implements IntParameter
    {
        private static final Zoom[] $VALUES;
        
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
        
        static {
            $VALUES = new Zoom[] { Zoom.ZOOM_0, Zoom.ZOOM_1, Zoom.ZOOM_2, Zoom.ZOOM_3, Zoom.ZOOM_4, Zoom.ZOOM_5, Zoom.ZOOM_6, Zoom.ZOOM_7, Zoom.ZOOM_8 };
        }
        
        private Zoom(final int mValue) {
            this.mValue = mValue;
        }
        
        @Override
        public boolean equals(final int n) {
            return this.mValue == n;
        }
        
        @Override
        public Parameter getDefaultValue() {
            return Zoom.ZOOM_0;
        }
    }
}
