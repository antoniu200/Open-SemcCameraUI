// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.device;

public class CameraParameterConverter
{
    static class AeMode
    {
        static int getApi2Value(final String s, final String s2) {
            final int hashCode = s2.hashCode();
            final int n = 6;
            final int n2 = 4;
            int n3 = -1;
            final int n4 = 3;
            final int n5 = 0;
            int n6 = 0;
            Label_0192: {
                switch (hashCode) {
                    case 1568542682: {
                        if (s2.equals("display-auto")) {
                            n6 = 5;
                            break Label_0192;
                        }
                        break;
                    }
                    case 1081542389: {
                        if (s2.equals("red-eye")) {
                            n6 = 4;
                            break Label_0192;
                        }
                        break;
                    }
                    case 110547964: {
                        if (s2.equals("torch")) {
                            n6 = 3;
                            break Label_0192;
                        }
                        break;
                    }
                    case 3005871: {
                        if (s2.equals("auto")) {
                            n6 = 0;
                            break Label_0192;
                        }
                        break;
                    }
                    case 109935: {
                        if (s2.equals("off")) {
                            n6 = 1;
                            break Label_0192;
                        }
                        break;
                    }
                    case 3551: {
                        if (s2.equals("on")) {
                            n6 = 2;
                            break Label_0192;
                        }
                        break;
                    }
                    case -914567478: {
                        if (s2.equals("display-on")) {
                            n6 = 6;
                            break Label_0192;
                        }
                        break;
                    }
                }
                n6 = -1;
            }
            Label_0378: {
                switch (n6) {
                    default: {
                        return n5;
                    }
                    case 6: {
                        return 16;
                    }
                    case 5: {
                        return 15;
                    }
                    case 4: {
                        final int hashCode2 = s.hashCode();
                        if (hashCode2 != -764369746) {
                            if (hashCode2 != 3005871) {
                                if (hashCode2 != 518043376) {
                                    if (hashCode2 == 2086525064) {
                                        if (s.equals("shutter-prio")) {
                                            n3 = 2;
                                        }
                                    }
                                }
                                else if (s.equals("iso-prio")) {
                                    n3 = 1;
                                }
                            }
                            else if (s.equals("auto")) {
                                n3 = 0;
                            }
                        }
                        else if (s.equals("semi-auto")) {
                            n3 = 3;
                        }
                        int n7 = n2;
                        switch (n3) {
                            default: {
                                n7 = n5;
                                return n7;
                            }
                            case 3: {
                                break Label_0378;
                            }
                            case 0: {
                                return n7;
                            }
                            case 2: {
                                n7 = 12;
                                return n7;
                            }
                            case 1: {
                                n7 = 8;
                                return n7;
                            }
                        }
                        break;
                    }
                    case 3: {
                        final int hashCode3 = s.hashCode();
                        if (hashCode3 != -764369746) {
                            if (hashCode3 != 3005871) {
                                if (hashCode3 != 518043376) {
                                    if (hashCode3 == 2086525064) {
                                        if (s.equals("shutter-prio")) {
                                            n3 = 2;
                                        }
                                    }
                                }
                                else if (s.equals("iso-prio")) {
                                    n3 = 1;
                                }
                            }
                            else if (s.equals("auto")) {
                                n3 = 0;
                            }
                        }
                        else if (s.equals("semi-auto")) {
                            n3 = 3;
                        }
                        switch (n3) {
                            case 3: {
                                break Label_0378;
                            }
                            default: {
                                return n5;
                            }
                            case 2: {
                                return 9;
                            }
                            case 1: {
                                return 5;
                            }
                            case 0: {
                                return 1;
                            }
                        }
                        break;
                    }
                    case 2: {
                        final int hashCode4 = s.hashCode();
                        if (hashCode4 != -764369746) {
                            if (hashCode4 != 3005871) {
                                if (hashCode4 != 518043376) {
                                    if (hashCode4 == 2086525064) {
                                        if (s.equals("shutter-prio")) {
                                            n3 = 2;
                                        }
                                    }
                                }
                                else if (s.equals("iso-prio")) {
                                    n3 = 1;
                                }
                            }
                            else if (s.equals("auto")) {
                                n3 = 0;
                            }
                        }
                        else if (s.equals("semi-auto")) {
                            n3 = 3;
                        }
                        int n7 = n4;
                        switch (n3) {
                            default: {
                                n7 = n5;
                                return n7;
                            }
                            case 0: {
                                return n7;
                            }
                            case 3: {
                                n7 = 14;
                                return n7;
                            }
                            case 2: {
                                n7 = 11;
                                return n7;
                            }
                            case 1: {
                                n7 = 7;
                                return n7;
                            }
                        }
                        break;
                    }
                    case 1: {
                        final int hashCode5 = s.hashCode();
                        if (hashCode5 != -764369746) {
                            if (hashCode5 != 3005871) {
                                if (hashCode5 != 518043376) {
                                    if (hashCode5 == 2086525064) {
                                        if (s.equals("shutter-prio")) {
                                            n3 = 2;
                                        }
                                    }
                                }
                                else if (s.equals("iso-prio")) {
                                    n3 = 1;
                                }
                            }
                            else if (s.equals("auto")) {
                                n3 = 0;
                            }
                        }
                        else if (s.equals("semi-auto")) {
                            n3 = 3;
                        }
                        switch (n3) {
                            case 3: {
                                break Label_0378;
                            }
                            case 2: {
                                return 9;
                            }
                            case 1: {
                                return 5;
                            }
                            case 0: {
                                return 1;
                            }
                            default: {
                                return n5;
                            }
                        }
                        break;
                    }
                    case 0: {
                        final int hashCode6 = s.hashCode();
                        if (hashCode6 != -764369746) {
                            if (hashCode6 != 3005871) {
                                if (hashCode6 != 518043376) {
                                    if (hashCode6 == 2086525064) {
                                        if (s.equals("shutter-prio")) {
                                            n3 = 2;
                                        }
                                    }
                                }
                                else if (s.equals("iso-prio")) {
                                    n3 = 1;
                                }
                            }
                            else if (s.equals("auto")) {
                                n3 = 0;
                            }
                        }
                        else if (s.equals("semi-auto")) {
                            n3 = 3;
                        }
                        int n7 = n;
                        switch (n3) {
                            case 3: {
                                break Label_0378;
                            }
                            default: {
                                n7 = n5;
                                return n7;
                            }
                            case 2: {
                                n7 = 10;
                            }
                            case 1: {
                                return n7;
                            }
                            case 0: {
                                n7 = 2;
                                return n7;
                            }
                        }
                        break;
                    }
                }
            }
            return 13;
            n7 = 9;
            return n7;
            n7 = 5;
            return n7;
            n7 = 1;
            return n7;
        }
    }
    
    public enum AwbMode
    {
        private static final AwbMode[] $VALUES;
        
        AUTO("auto", 1), 
        CLOUDY_DAYLIGHT("cloudy-daylight", 6), 
        DAYLIGHT("daylight", 5), 
        FLUORESCENT("fluorescent", 3), 
        INCANDESCENT("incandescent", 2), 
        OFF("off", 0);
        
        private String mAwbModeApi1;
        private int mAwbModeApi2;
        
        static {
            $VALUES = new AwbMode[] { AwbMode.OFF, AwbMode.AUTO, AwbMode.INCANDESCENT, AwbMode.FLUORESCENT, AwbMode.DAYLIGHT, AwbMode.CLOUDY_DAYLIGHT };
        }
        
        private AwbMode(final String mAwbModeApi1, final int mAwbModeApi2) {
            this.mAwbModeApi1 = mAwbModeApi1;
            this.mAwbModeApi2 = mAwbModeApi2;
        }
        
        public static String getApi1Value(final int n) {
            for (final AwbMode awbMode : values()) {
                if (awbMode.mAwbModeApi2 == n) {
                    return awbMode.mAwbModeApi1;
                }
            }
            return null;
        }
        
        public static int getApi2Value(final String anObject) {
            for (final AwbMode awbMode : values()) {
                if (awbMode.mAwbModeApi1.equals(anObject)) {
                    return awbMode.mAwbModeApi2;
                }
            }
            return 0;
        }
    }
    
    static class DistortionCorrection
    {
        static Integer getApi2Value(final String s) {
            final int hashCode = s.hashCode();
            int n = 0;
            Label_0051: {
                if (hashCode != 3551) {
                    if (hashCode == 109935) {
                        if (s.equals("off")) {
                            n = 0;
                            break Label_0051;
                        }
                    }
                }
                else if (s.equals("on")) {
                    n = 1;
                    break Label_0051;
                }
                n = -1;
            }
            Integer n2 = null;
            switch (n) {
                default: {
                    n2 = null;
                    break;
                }
                case 1: {
                    n2 = 1;
                    break;
                }
                case 0: {
                    n2 = 0;
                    break;
                }
            }
            return n2;
        }
    }
    
    static class FlashMode
    {
        static int getApi2Value(final String s) {
            int n;
            if (s.equals("torch")) {
                n = 2;
            }
            else {
                n = 0;
            }
            return n;
        }
    }
    
    enum FocusArea
    {
        private static final FocusArea[] $VALUES;
        
        CENTER("center", 0), 
        FACE("face", 2), 
        MULTI("multi", 1), 
        USER("user", 3);
        
        private String mApi1Value;
        private int mApi2Value;
        
        static {
            $VALUES = new FocusArea[] { FocusArea.CENTER, FocusArea.FACE, FocusArea.MULTI, FocusArea.USER };
        }
        
        private FocusArea(final String mApi1Value, final int mApi2Value) {
            this.mApi1Value = mApi1Value;
            this.mApi2Value = mApi2Value;
        }
        
        static int getApi2Value(final String anObject) {
            for (final FocusArea focusArea : values()) {
                if (focusArea.mApi1Value.equals(anObject)) {
                    return focusArea.mApi2Value;
                }
            }
            return 0;
        }
    }
    
    enum FocusMode
    {
        private static final FocusMode[] $VALUES;
        
        AUTO("auto", 1), 
        CONTINUOUS_PICTURE("continuous-picture", 4), 
        CONTINUOUS_VIDEO("continuous-video", 3), 
        FIXED("fixed", 0), 
        INFINITY("infinity", 0), 
        MANUAL("manual", 0);
        
        private String mApi1Value;
        private int mApi2Value;
        
        static {
            $VALUES = new FocusMode[] { FocusMode.AUTO, FocusMode.INFINITY, FocusMode.FIXED, FocusMode.CONTINUOUS_VIDEO, FocusMode.CONTINUOUS_PICTURE, FocusMode.MANUAL };
        }
        
        private FocusMode(final String mApi1Value, final int mApi2Value) {
            this.mApi1Value = mApi1Value;
            this.mApi2Value = mApi2Value;
        }
        
        static int getApi2Value(final String anObject) {
            for (final FocusMode focusMode : values()) {
                if (focusMode.mApi1Value.equals(anObject)) {
                    return focusMode.mApi2Value;
                }
            }
            return 1;
        }
    }
    
    static class FusionMode
    {
        static Integer getApi2Value(final String s) {
            final int hashCode = s.hashCode();
            int n = 0;
            Label_0071: {
                if (hashCode != 3551) {
                    if (hashCode != 109935) {
                        if (hashCode == 3005871) {
                            if (s.equals("auto")) {
                                n = 2;
                                break Label_0071;
                            }
                        }
                    }
                    else if (s.equals("off")) {
                        n = 0;
                        break Label_0071;
                    }
                }
                else if (s.equals("on")) {
                    n = 1;
                    break Label_0071;
                }
                n = -1;
            }
            Integer n2 = null;
            switch (n) {
                default: {
                    n2 = null;
                    break;
                }
                case 2: {
                    n2 = 2;
                    break;
                }
                case 1: {
                    n2 = 1;
                    break;
                }
                case 0: {
                    n2 = 0;
                    break;
                }
            }
            return n2;
        }
    }
    
    enum MeteringMode
    {
        private static final MeteringMode[] $VALUES;
        
        AVERAGE("frame-average", 1), 
        CENTER("center-weighted", 0), 
        FACE("face", 4), 
        MULTI("multi", 3), 
        SPOT("spot", 2), 
        USER("user", 5);
        
        private String mApi1Value;
        private int mApi2Value;
        
        static {
            $VALUES = new MeteringMode[] { MeteringMode.CENTER, MeteringMode.FACE, MeteringMode.AVERAGE, MeteringMode.MULTI, MeteringMode.SPOT, MeteringMode.USER };
        }
        
        private MeteringMode(final String mApi1Value, final int mApi2Value) {
            this.mApi1Value = mApi1Value;
            this.mApi2Value = mApi2Value;
        }
        
        static int getApi2Value(final String anObject) {
            for (final MeteringMode meteringMode : values()) {
                if (meteringMode.mApi1Value.equals(anObject)) {
                    return meteringMode.mApi2Value;
                }
            }
            return 0;
        }
    }
    
    enum PowerSaveMode
    {
        private static final PowerSaveMode[] $VALUES;
        
        LOW("low", 1), 
        OFF("off", 0), 
        ULTRA_LOW("ultra-low", 2);
        
        private String mApi1Value;
        private int mApi2Value;
        
        static {
            $VALUES = new PowerSaveMode[] { PowerSaveMode.OFF, PowerSaveMode.LOW, PowerSaveMode.ULTRA_LOW };
        }
        
        private PowerSaveMode(final String mApi1Value, final int mApi2Value) {
            this.mApi1Value = mApi1Value;
            this.mApi2Value = mApi2Value;
        }
        
        static int getApi2Value(final String anObject) {
            for (final PowerSaveMode powerSaveMode : values()) {
                if (powerSaveMode.mApi1Value.equals(anObject)) {
                    return powerSaveMode.mApi2Value;
                }
            }
            return 0;
        }
    }
    
    public enum SceneMode
    {
        private static final SceneMode[] $VALUES;
        
        ACTION("action", 2), 
        AUTO("auto", 100), 
        BABY("baby", 105), 
        BACKLIGHT("backlight", 102), 
        BACKLIGHT_PORTRAIT("backlight-portrait", 103), 
        BARCODE("barcode", 16), 
        BEACH("beach", 8), 
        CANDLELIGHT("candlelight", 15), 
        DARK("dark", 104), 
        DISH("dish", 107), 
        DOCUMENT("document", 101), 
        FIREWORKS("fireworks", 12), 
        LANDSCAPE("landscape", 4), 
        NIGHT("night", 5), 
        NIGHT_PORTRAIT("night-portrait", 6), 
        PARTY("party", 14), 
        PORTRAIT("portrait", 3), 
        SNOW("snow", 9), 
        SPORTS("sports", 13), 
        SPOTLIGHT("spot-light", 106), 
        STEADYPHOTO("steadyphoto", 11), 
        SUNSET("sunset", 10), 
        THEATRE("theatre", 7);
        
        private String mSceneModeApi1;
        private int mSceneModeApi2;
        
        static {
            $VALUES = new SceneMode[] { SceneMode.AUTO, SceneMode.PORTRAIT, SceneMode.NIGHT_PORTRAIT, SceneMode.LANDSCAPE, SceneMode.NIGHT, SceneMode.SNOW, SceneMode.SPORTS, SceneMode.PARTY, SceneMode.BEACH, SceneMode.FIREWORKS, SceneMode.ACTION, SceneMode.THEATRE, SceneMode.SUNSET, SceneMode.STEADYPHOTO, SceneMode.CANDLELIGHT, SceneMode.DOCUMENT, SceneMode.BACKLIGHT, SceneMode.BACKLIGHT_PORTRAIT, SceneMode.DARK, SceneMode.BABY, SceneMode.SPOTLIGHT, SceneMode.DISH, SceneMode.BARCODE };
        }
        
        private SceneMode(final String mSceneModeApi1, final int mSceneModeApi2) {
            this.mSceneModeApi1 = mSceneModeApi1;
            this.mSceneModeApi2 = mSceneModeApi2;
        }
        
        public static String getApi1Value(final int n) {
            for (final SceneMode sceneMode : values()) {
                if (sceneMode.mSceneModeApi2 == n) {
                    return sceneMode.mSceneModeApi1;
                }
            }
            return null;
        }
        
        public static int getApi2Value(final String anObject) {
            for (final SceneMode sceneMode : values()) {
                if (sceneMode.mSceneModeApi1.equals(anObject)) {
                    return sceneMode.mSceneModeApi2;
                }
            }
            return 100;
        }
        
        public static SceneMode getSceneMode(final int n) {
            final SceneMode[] values = values();
            for (int i = 0; i < values.length; ++i) {
                if (values[i].getSceneValue() == n) {
                    return values[i];
                }
            }
            return null;
        }
        
        private int getSceneValue() {
            return this.mSceneModeApi2;
        }
        
        @Override
        public String toString() {
            return this.mSceneModeApi1;
        }
    }
    
    static class StillHdr
    {
        static Integer getApi2Value(final String s) {
            final int hashCode = s.hashCode();
            int n = 0;
            Label_0070: {
                if (hashCode != 109935) {
                    if (hashCode != 3005871) {
                        if (hashCode == 1589394147) {
                            if (s.equals("on-still-hdr")) {
                                n = 1;
                                break Label_0070;
                            }
                        }
                    }
                    else if (s.equals("auto")) {
                        n = 2;
                        break Label_0070;
                    }
                }
                else if (s.equals("off")) {
                    n = 0;
                    break Label_0070;
                }
                n = -1;
            }
            Integer n2 = null;
            switch (n) {
                case 1: {
                    n2 = 1;
                    break;
                }
                case 0: {
                    n2 = 0;
                    break;
                }
                default:
                case 2: {
                    n2 = null;
                    break;
                }
            }
            return n2;
        }
    }
}
