// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.controller;

import android.os.VibrationEffect;
import android.content.res.Resources;
import android.media.AudioAttributes$Builder;
import android.os.Vibrator;
import android.content.Context;

public class VibrationManager
{
    private static final long[] DEFAULT_VIBRATION_PATTERN;
    
    static {
        DEFAULT_VIBRATION_PATTERN = new long[] { 0L, 1L, 20L, 21L };
    }
    
    public static void vibrate(final Context context, final VibrationPattern vibrationPattern) {
        final Vibrator vibrator = (Vibrator)context.getSystemService("vibrator");
        if (vibrator.hasAmplitudeControl()) {
            vibrator.vibrate(vibrationPattern.mVibrationEffect, new AudioAttributes$Builder().setUsage(5).setContentType(4).build());
        }
        else {
            final long[] default_VIBRATION_PATTERN = VibrationManager.DEFAULT_VIBRATION_PATTERN;
            final int identifier = Resources.getSystem().getIdentifier("config_longPressVibePattern", "array", "android");
            long[] array = default_VIBRATION_PATTERN;
            if (identifier != 0) {
                final int[] intArray = Resources.getSystem().getIntArray(identifier);
                array = default_VIBRATION_PATTERN;
                if (intArray != null) {
                    array = default_VIBRATION_PATTERN;
                    if (intArray.length > 0) {
                        final long[] array2 = new long[intArray.length];
                        int n = 0;
                        while (true) {
                            array = array2;
                            if (n >= intArray.length) {
                                break;
                            }
                            array2[n] = intArray[n];
                            ++n;
                        }
                    }
                }
            }
            vibrator.vibrate(array, -1);
        }
    }
    
    public enum VibrationPattern
    {
        private static final VibrationPattern[] $VALUES;
        
        EFFECT_FOR_CAPTURE(VibrationEffect.createOneShot(10L, 100)), 
        EFFECT_STANDARD(com.sonymobile.vibrationeffect.VibrationEffect.get(10005));
        
        public final VibrationEffect mVibrationEffect;
        
        static {
            $VALUES = new VibrationPattern[] { VibrationPattern.EFFECT_STANDARD, VibrationPattern.EFFECT_FOR_CAPTURE };
        }
        
        private VibrationPattern(final VibrationEffect mVibrationEffect) {
            this.mVibrationEffect = mVibrationEffect;
        }
    }
}
