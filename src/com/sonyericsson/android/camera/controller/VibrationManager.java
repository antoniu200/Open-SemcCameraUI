package com.sonyericsson.android.camera.controller;

import android.content.Context;
import android.content.res.Resources;
import android.media.AudioAttributes;
import android.os.Vibrator;
import com.sonymobile.vibrationeffect.VibrationEffect;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class VibrationManager {
    private static final long[] DEFAULT_VIBRATION_PATTERN = {0, 1, 20, 21};

    public enum VibrationPattern {
        EFFECT_STANDARD(VibrationEffect.get(10005)),
        EFFECT_FOR_CAPTURE(android.os.VibrationEffect.createOneShot(10, 100));

        public final android.os.VibrationEffect mVibrationEffect;

        VibrationPattern(android.os.VibrationEffect vibrationEffect) {
            this.mVibrationEffect = vibrationEffect;
        }
    }

    public static void vibrate(Context context, VibrationPattern vibrationPattern) {
        int[] intArray;
        Vibrator vibrator = (Vibrator) context.getSystemService("vibrator");
        if (vibrator.hasAmplitudeControl()) {
            vibrator.vibrate(vibrationPattern.mVibrationEffect, new AudioAttributes.Builder().setUsage(5).setContentType(4).build());
            return;
        }
        long[] jArr = DEFAULT_VIBRATION_PATTERN;
        int identifier = Resources.getSystem().getIdentifier("config_longPressVibePattern", "array", "android");
        if (identifier != 0 && (intArray = Resources.getSystem().getIntArray(identifier)) != null && intArray.length > 0) {
            jArr = new long[intArray.length];
            for (int i = 0; i < intArray.length; i++) {
                jArr[i] = intArray[i];
            }
        }
        vibrator.vibrate(jArr, -1);
    }
}
