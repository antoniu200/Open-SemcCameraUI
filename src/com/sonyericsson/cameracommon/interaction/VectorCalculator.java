package com.sonyericsson.cameracommon.interaction;

import android.graphics.PointF;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class VectorCalculator {
    private static final float PARALLEL_ANGLE_FORWARD_DIRECTION = 0.0f;
    private static final float PARALLEL_ANGLE_INVERSE_DIRECTION = 3.1415927f;
    private static final float PARALLEL_ANGLE_TOLERANCE = 1.0471976f;
    private static final float RIGHT_ANGLE = 1.5707964f;
    private static final float RIGHT_ANGLE_TOLERANCE = 1.0471976f;
    public static final String TAG = "VectorCalculator";

    public static float getRadianFrom2Vector(PointF pointF, PointF pointF2) {
        if (0.0f >= pointF.length() || 0.0f >= pointF2.length()) {
            return 0.0f;
        }
        float length = (((pointF.x * pointF2.x) + (pointF.y * pointF2.y)) / pointF.length()) / pointF2.length();
        return (float) Math.acos(length >= -1.0f ? 1.0f < length ? 1.0f : length : -1.0f);
    }

    public static boolean isSquare(PointF pointF, PointF pointF2) {
        float radianFrom2Vector = getRadianFrom2Vector(pointF, pointF2);
        return 0.5235988f < radianFrom2Vector && radianFrom2Vector < 2.6179938f;
    }

    public static boolean isParallel(PointF pointF, PointF pointF2) {
        float radianFrom2Vector = getRadianFrom2Vector(pointF, pointF2);
        return isNearlyEquals(PARALLEL_ANGLE_INVERSE_DIRECTION, radianFrom2Vector, 1.0471976f) || isNearlyEquals(0.0f, radianFrom2Vector, 1.0471976f);
    }

    private static boolean isNearlyEquals(float f, float f2, float f3) {
        return Math.abs(f - f2) < f3;
    }
}
