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

    static class AeMode {
        AeMode() {
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:26:0x0056  */
        /* JADX WARN: Removed duplicated region for block: B:52:0x00ad  */
        /* JADX WARN: Removed duplicated region for block: B:76:0x00f1  */
        /* JADX WARN: Removed duplicated region for block: B:77:0x00f4  */
        /* JADX WARN: Removed duplicated region for block: B:78:0x00f7  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        static int getApi2Value(java.lang.String r14, java.lang.String r15) {
            /*
                Method dump skipped, instructions count: 544
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sonyericsson.android.camera.device.CameraParameterConverter.AeMode.getApi2Value(java.lang.String, java.lang.String):int");
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

        /* JADX WARN: Removed duplicated region for block: B:18:0x0034  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        static java.lang.Integer getApi2Value(java.lang.String r4) {
            /*
                int r0 = r4.hashCode()
                r1 = 109935(0x1ad6f, float:1.54052E-40)
                r2 = 0
                r3 = 1
                if (r0 == r1) goto L2a
                r1 = 3005871(0x2dddaf, float:4.212122E-39)
                if (r0 == r1) goto L20
                r1 = 1589394147(0x5ebc3ae3, float:6.7817014E18)
                if (r0 == r1) goto L16
                goto L34
            L16:
                java.lang.String r0 = "on-still-hdr"
                boolean r4 = r4.equals(r0)
                if (r4 == 0) goto L34
                r4 = r3
                goto L35
            L20:
                java.lang.String r0 = "auto"
                boolean r4 = r4.equals(r0)
                if (r4 == 0) goto L34
                r4 = 2
                goto L35
            L2a:
                java.lang.String r0 = "off"
                boolean r4 = r4.equals(r0)
                if (r4 == 0) goto L34
                r4 = r2
                goto L35
            L34:
                r4 = -1
            L35:
                switch(r4) {
                    case 0: goto L3e;
                    case 1: goto L39;
                    case 2: goto L43;
                    default: goto L38;
                }
            L38:
                goto L43
            L39:
                java.lang.Integer r4 = java.lang.Integer.valueOf(r3)
                goto L44
            L3e:
                java.lang.Integer r4 = java.lang.Integer.valueOf(r2)
                goto L44
            L43:
                r4 = 0
            L44:
                return r4
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sonyericsson.android.camera.device.CameraParameterConverter.StillHdr.getApi2Value(java.lang.String):java.lang.Integer");
        }
    }

    static class FusionMode {
        FusionMode() {
        }

        /* JADX WARN: Removed duplicated region for block: B:18:0x0034  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        static java.lang.Integer getApi2Value(java.lang.String r5) {
            /*
                int r0 = r5.hashCode()
                r1 = 3551(0xddf, float:4.976E-42)
                r2 = 1
                r3 = 0
                r4 = 2
                if (r0 == r1) goto L2a
                r1 = 109935(0x1ad6f, float:1.54052E-40)
                if (r0 == r1) goto L20
                r1 = 3005871(0x2dddaf, float:4.212122E-39)
                if (r0 == r1) goto L16
                goto L34
            L16:
                java.lang.String r0 = "auto"
                boolean r5 = r5.equals(r0)
                if (r5 == 0) goto L34
                r5 = r4
                goto L35
            L20:
                java.lang.String r0 = "off"
                boolean r5 = r5.equals(r0)
                if (r5 == 0) goto L34
                r5 = r3
                goto L35
            L2a:
                java.lang.String r0 = "on"
                boolean r5 = r5.equals(r0)
                if (r5 == 0) goto L34
                r5 = r2
                goto L35
            L34:
                r5 = -1
            L35:
                switch(r5) {
                    case 0: goto L44;
                    case 1: goto L3f;
                    case 2: goto L3a;
                    default: goto L38;
                }
            L38:
                r5 = 0
                goto L48
            L3a:
                java.lang.Integer r5 = java.lang.Integer.valueOf(r4)
                goto L48
            L3f:
                java.lang.Integer r5 = java.lang.Integer.valueOf(r2)
                goto L48
            L44:
                java.lang.Integer r5 = java.lang.Integer.valueOf(r3)
            L48:
                return r5
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sonyericsson.android.camera.device.CameraParameterConverter.FusionMode.getApi2Value(java.lang.String):java.lang.Integer");
        }
    }

    static class DistortionCorrection {
        DistortionCorrection() {
        }

        /* JADX WARN: Removed duplicated region for block: B:13:0x0024  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        static java.lang.Integer getApi2Value(java.lang.String r4) {
            /*
                int r0 = r4.hashCode()
                r1 = 3551(0xddf, float:4.976E-42)
                r2 = 1
                r3 = 0
                if (r0 == r1) goto L1a
                r1 = 109935(0x1ad6f, float:1.54052E-40)
                if (r0 == r1) goto L10
                goto L24
            L10:
                java.lang.String r0 = "off"
                boolean r4 = r4.equals(r0)
                if (r4 == 0) goto L24
                r4 = r3
                goto L25
            L1a:
                java.lang.String r0 = "on"
                boolean r4 = r4.equals(r0)
                if (r4 == 0) goto L24
                r4 = r2
                goto L25
            L24:
                r4 = -1
            L25:
                switch(r4) {
                    case 0: goto L2f;
                    case 1: goto L2a;
                    default: goto L28;
                }
            L28:
                r4 = 0
                goto L33
            L2a:
                java.lang.Integer r4 = java.lang.Integer.valueOf(r2)
                goto L33
            L2f:
                java.lang.Integer r4 = java.lang.Integer.valueOf(r3)
            L33:
                return r4
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sonyericsson.android.camera.device.CameraParameterConverter.DistortionCorrection.getApi2Value(java.lang.String):java.lang.Integer");
        }
    }
}
