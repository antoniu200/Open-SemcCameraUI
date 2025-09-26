// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.interaction;

import android.graphics.PointF;

public class VectorCalculator
{
    private static final float PARALLEL_ANGLE_FORWARD_DIRECTION = 0.0f;
    private static final float PARALLEL_ANGLE_INVERSE_DIRECTION = 3.1415927f;
    private static final float PARALLEL_ANGLE_TOLERANCE = 1.0471976f;
    private static final float RIGHT_ANGLE = 1.5707964f;
    private static final float RIGHT_ANGLE_TOLERANCE = 1.0471976f;
    public static final String TAG = "VectorCalculator";
    
    public static float getRadianFrom2Vector(final PointF pointF, final PointF pointF2) {
        if (0.0f < pointF.length() && 0.0f < pointF2.length()) {
            final float n = (pointF.x * pointF2.x + pointF.y * pointF2.y) / pointF.length() / pointF2.length();
            float n2 = -1.0f;
            if (n >= -1.0f) {
                if (1.0f < n) {
                    n2 = 1.0f;
                }
                else {
                    n2 = n;
                }
            }
            return (float)Math.acos(n2);
        }
        return 0.0f;
    }
    
    private static boolean isNearlyEquals(final float n, final float n2, final float n3) {
        return Math.abs(n - n2) < n3;
    }
    
    public static boolean isParallel(final PointF pointF, final PointF pointF2) {
        final float radianFrom2Vector = getRadianFrom2Vector(pointF, pointF2);
        return isNearlyEquals(3.1415927f, radianFrom2Vector, 1.0471976f) || isNearlyEquals(0.0f, radianFrom2Vector, 1.0471976f);
    }
    
    public static boolean isSquare(final PointF pointF, final PointF pointF2) {
        final float radianFrom2Vector = getRadianFrom2Vector(pointF, pointF2);
        return 0.5235988f < radianFrom2Vector && radianFrom2Vector < 2.6179938f;
    }
}
