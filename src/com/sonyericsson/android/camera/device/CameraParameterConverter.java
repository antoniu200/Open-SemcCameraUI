package com.sonyericsson.android.camera.device;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class CameraParameterConverter {

    public enum AwbMode {
        OFF("off", 0),
        AUTO("auto", 1),
        INCANDESCENT(CameraParameters.WHITE_BALANCE_INCANDESCENT, 2),
        FLUORESCENT(CameraParameters.WHITE_BALANCE_FLUORESCENT, 3),
        DAYLIGHT(CameraParameters.WHITE_BALANCE_DAYLIGHT, 5),
        CLOUDY_DAYLIGHT(CameraParameters.WHITE_BALANCE_CLOUDY_DAYLIGHT, 6);

        private String mAwbModeApi1;
        private int mAwbModeApi2;

        AwbMode(String str, int i) {
            this.mAwbModeApi1 = str;
            this.mAwbModeApi2 = i;
        }

        public static String getApi1Value(int i) {
            for (AwbMode awbMode : values()) {
                if (awbMode.mAwbModeApi2 == i) {
                    return awbMode.mAwbModeApi1;
                }
            }
            return null;
        }

        public static int getApi2Value(String str) {
            for (AwbMode awbMode : values()) {
                if (awbMode.mAwbModeApi1.equals(str)) {
                    return awbMode.mAwbModeApi2;
                }
            }
            return 0;
        }
    }

    public enum SceneMode {
        AUTO("auto", 100),
        PORTRAIT("portrait", 3),
        NIGHT_PORTRAIT("night-portrait", 6),
        LANDSCAPE("landscape", 4),
        NIGHT("night", 5),
        SNOW("snow", 9),
        SPORTS("sports", 13),
        PARTY("party", 14),
        BEACH("beach", 8),
        FIREWORKS("fireworks", 12),
        ACTION("action", 2),
        THEATRE("theatre", 7),
        SUNSET("sunset", 10),
        STEADYPHOTO("steadyphoto", 11),
        CANDLELIGHT("candlelight", 15),
        DOCUMENT("document", 101),
        BACKLIGHT("backlight", 102),
        BACKLIGHT_PORTRAIT("backlight-portrait", 103),
        DARK("dark", 104),
        BABY("baby", 105),
        SPOTLIGHT("spot-light", 106),
        DISH("dish", 107),
        BARCODE(CameraParameters.SCENE_MODE_BARCODE, 16);

        private String mSceneModeApi1;
        private int mSceneModeApi2;

        SceneMode(String str, int i) {
            this.mSceneModeApi1 = str;
            this.mSceneModeApi2 = i;
        }

        public static String getApi1Value(int i) {
            for (SceneMode sceneMode : values()) {
                if (sceneMode.mSceneModeApi2 == i) {
                    return sceneMode.mSceneModeApi1;
                }
            }
            return null;
        }

        public static int getApi2Value(String str) {
            for (SceneMode sceneMode : values()) {
                if (sceneMode.mSceneModeApi1.equals(str)) {
                    return sceneMode.mSceneModeApi2;
                }
            }
            return 100;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.mSceneModeApi1;
        }

        private int getSceneValue() {
            return this.mSceneModeApi2;
        }

        public static SceneMode getSceneMode(int i) {
            SceneMode[] sceneModeArrValues = values();
            for (int i2 = 0; i2 < sceneModeArrValues.length; i2++) {
                if (sceneModeArrValues[i2].getSceneValue() == i) {
                    return sceneModeArrValues[i2];
                }
            }
            return null;
        }
    }

    enum FocusMode {
        AUTO("auto", 1),
        INFINITY(CameraParameters.FOCUS_MODE_INFINITY, 0),
        FIXED(CameraParameters.FOCUS_MODE_FIXED, 0),
        CONTINUOUS_VIDEO(CameraParameters.FOCUS_MODE_CONTINUOUS_VIDEO, 3),
        CONTINUOUS_PICTURE(CameraParameters.FOCUS_MODE_CONTINUOUS_PICTURE, 4),
        MANUAL(CameraParameters.FOCUS_MODE_MANUAL, 0);

        private String mApi1Value;
        private int mApi2Value;

        FocusMode(String str, int i) {
            this.mApi1Value = str;
            this.mApi2Value = i;
        }

        static int getApi2Value(String str) {
            for (FocusMode focusMode : values()) {
                if (focusMode.mApi1Value.equals(str)) {
                    return focusMode.mApi2Value;
                }
            }
            return 1;
        }
    }

    enum FocusArea {
        CENTER(CameraParameters.FOCUS_AREA_CENTER, 0),
        FACE("face", 2),
        MULTI("multi", 1),
        USER("user", 3);

        private String mApi1Value;
        private int mApi2Value;

        FocusArea(String str, int i) {
            this.mApi1Value = str;
            this.mApi2Value = i;
        }

        static int getApi2Value(String str) {
            for (FocusArea focusArea : values()) {
                if (focusArea.mApi1Value.equals(str)) {
                    return focusArea.mApi2Value;
                }
            }
            return 0;
        }
    }

    enum PowerSaveMode {
        OFF("off", 0),
        LOW(CameraParameters.POWER_SAVING_MODE_LOW_POWER, 1),
        ULTRA_LOW(CameraParameters.POWER_SAVING_MODE_ULTRA_LOW_POWER, 2);

        private String mApi1Value;
        private int mApi2Value;

        PowerSaveMode(String str, int i) {
            this.mApi1Value = str;
            this.mApi2Value = i;
        }

        static int getApi2Value(String str) {
            for (PowerSaveMode powerSaveMode : values()) {
                if (powerSaveMode.mApi1Value.equals(str)) {
                    return powerSaveMode.mApi2Value;
                }
            }
            return 0;
        }
    }

    enum MeteringMode {
        CENTER(CameraParameters.AE_REGION_MODE_CENTER_WEIGHTED, 0),
        FACE("face", 4),
        AVERAGE(CameraParameters.AE_REGION_MODE_FRAME_AVERAGE, 1),
        MULTI("multi", 3),
        SPOT(CameraParameters.AE_REGION_MODE_SPOT, 2),
        USER("user", 5);

        private String mApi1Value;
        private int mApi2Value;

        MeteringMode(String str, int i) {
            this.mApi1Value = str;
            this.mApi2Value = i;
        }

        static int getApi2Value(String str) {
            for (MeteringMode meteringMode : values()) {
                if (meteringMode.mApi1Value.equals(str)) {
                    return meteringMode.mApi2Value;
                }
            }
            return 0;
        }
    }

    static int getApi2Value(String aeMode, String flashMode) {
        if (flashMode == null || aeMode == null) return 0;

        switch (flashMode) {
            case "display-on":
                return 16;

            case "display-auto":
                return 15;

            case "red-eye":
                switch (aeMode) {
                    case "auto":         return 4;
                    case "iso-prio":     return 8;
                    case "shutter-prio": return 12;
                    case "semi-auto":    return 13;
                    default:             return 0;
                }

            case "torch":
                switch (aeMode) {
                    case "auto":         return 1;
                    case "iso-prio":     return 5;
                    case "shutter-prio": return 9;
                    case "semi-auto":    return 13;
                    default:             return 0;
                }

            case "on":
                switch (aeMode) {
                    case "auto":         return 3;
                    case "iso-prio":     return 7;
                    case "shutter-prio": return 11;
                    case "semi-auto":    return 14;
                    default:             return 0;
                }

            case "off":
                switch (aeMode) {
                    case "auto":         return 1;
                    case "iso-prio":     return 5;
                    case "shutter-prio": return 9;
                    case "semi-auto":    return 13;
                    default:             return 0;
                }

            case "auto":
                switch (aeMode) {
                    case "auto":         return 2;
                    case "iso-prio":     return 6;
                    case "shutter-prio": return 10;
                    case "semi-auto":    return 13;
                    default:             return 0;
                }

            default:
                return 0;
        }
    }

    static class FlashMode {
        FlashMode() {
        }

        static int getApi2Value(String str) {
            return str.equals(CameraParameters.FLASH_MODE_TORCH) ? 2 : 0;
        }
    }

    static class StillHdr {
        StillHdr() {
        }

        static Integer getApi2Value(String mode) {
            if (mode == null) return null;
            switch (mode) {
                case "off":          return Integer.valueOf(0);
                case "on-still-hdr": return Integer.valueOf(1);
                case "auto":         return null;
                default:             return null;
            }
        }
    }

    static class FusionMode {
        FusionMode() {
        }

        static Integer getApi2Value(String mode) {
            if (mode == null) return null;
            switch (mode) {
                case "off":  return Integer.valueOf(0);
                case "on":   return Integer.valueOf(1);
                case "auto": return Integer.valueOf(2);
                default:     return null;
            }
        }
    }

    static class DistortionCorrection {
        DistortionCorrection() {
        }

        static Integer getApi2Value(String mode) {
            if (mode == null) return null;
            switch (mode) {
                case "off": return Integer.valueOf(0);
                case "on":  return Integer.valueOf(1);
                default:    return null;
            }
        }
    }
}
