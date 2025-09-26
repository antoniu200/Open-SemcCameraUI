// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.graphics;

import android.support.annotation.RequiresApi;
import java.util.Objects;
import android.support.annotation.VisibleForTesting;
import android.support.annotation.IntRange;
import android.support.annotation.FloatRange;
import android.support.annotation.ColorInt;
import android.graphics.Color;
import android.support.annotation.NonNull;

public final class ColorUtils
{
    private static final int MIN_ALPHA_SEARCH_MAX_ITERATIONS = 10;
    private static final int MIN_ALPHA_SEARCH_PRECISION = 1;
    private static final ThreadLocal<double[]> TEMP_ARRAY;
    private static final double XYZ_EPSILON = 0.008856;
    private static final double XYZ_KAPPA = 903.3;
    private static final double XYZ_WHITE_REFERENCE_X = 95.047;
    private static final double XYZ_WHITE_REFERENCE_Y = 100.0;
    private static final double XYZ_WHITE_REFERENCE_Z = 108.883;
    
    static {
        TEMP_ARRAY = new ThreadLocal<double[]>();
    }
    
    private ColorUtils() {
    }
    
    @ColorInt
    public static int HSLToColor(@NonNull final float[] array) {
        final float n = array[0];
        final float n2 = array[1];
        final float n3 = array[2];
        final float n4 = (1.0f - Math.abs(2.0f * n3 - 1.0f)) * n2;
        final float n5 = n3 - 0.5f * n4;
        final float n6 = (1.0f - Math.abs(n / 60.0f % 2.0f - 1.0f)) * n4;
        int n7 = 0;
        int n9 = 0;
        int n8 = 0;
        switch ((int)n / 60) {
            default: {
                n7 = 0;
                n8 = (n9 = 0);
                break;
            }
            case 5:
            case 6: {
                n8 = Math.round((n4 + n5) * 255.0f);
                n9 = Math.round(255.0f * n5);
                n7 = Math.round(255.0f * (n6 + n5));
                break;
            }
            case 4: {
                n8 = Math.round((n6 + n5) * 255.0f);
                n9 = Math.round(255.0f * n5);
                n7 = Math.round(255.0f * (n4 + n5));
                break;
            }
            case 3: {
                n8 = Math.round(255.0f * n5);
                n9 = Math.round((n6 + n5) * 255.0f);
                n7 = Math.round(255.0f * (n4 + n5));
                break;
            }
            case 2: {
                n8 = Math.round(255.0f * n5);
                n9 = Math.round((n4 + n5) * 255.0f);
                n7 = Math.round(255.0f * (n6 + n5));
                break;
            }
            case 1: {
                n8 = Math.round((n6 + n5) * 255.0f);
                n9 = Math.round((n4 + n5) * 255.0f);
                n7 = Math.round(255.0f * n5);
                break;
            }
            case 0: {
                n8 = Math.round((n4 + n5) * 255.0f);
                n9 = Math.round((n6 + n5) * 255.0f);
                n7 = Math.round(255.0f * n5);
                break;
            }
        }
        return Color.rgb(constrain(n8, 0, 255), constrain(n9, 0, 255), constrain(n7, 0, 255));
    }
    
    @ColorInt
    public static int LABToColor(@FloatRange(from = 0.0, to = 100.0) final double n, @FloatRange(from = -128.0, to = 127.0) final double n2, @FloatRange(from = -128.0, to = 127.0) final double n3) {
        final double[] tempDouble3Array = getTempDouble3Array();
        LABToXYZ(n, n2, n3, tempDouble3Array);
        return XYZToColor(tempDouble3Array[0], tempDouble3Array[1], tempDouble3Array[2]);
    }
    
    public static void LABToXYZ(@FloatRange(from = 0.0, to = 100.0) double pow, @FloatRange(from = -128.0, to = 127.0) double pow2, @FloatRange(from = -128.0, to = 127.0) double pow3, @NonNull final double[] array) {
        final double a = (pow + 16.0) / 116.0;
        final double a2 = pow2 / 500.0 + a;
        final double a3 = a - pow3 / 200.0;
        pow2 = Math.pow(a2, 3.0);
        if (pow2 <= 0.008856) {
            pow2 = (a2 * 116.0 - 16.0) / 903.3;
        }
        if (pow > 7.9996247999999985) {
            pow = Math.pow(a, 3.0);
        }
        else {
            pow /= 903.3;
        }
        pow3 = Math.pow(a3, 3.0);
        if (pow3 <= 0.008856) {
            pow3 = (116.0 * a3 - 16.0) / 903.3;
        }
        array[0] = pow2 * 95.047;
        array[1] = pow * 100.0;
        array[2] = pow3 * 108.883;
    }
    
    public static void RGBToHSL(@IntRange(from = 0L, to = 255L) final int n, @IntRange(from = 0L, to = 255L) final int n2, @IntRange(from = 0L, to = 255L) final int n3, @NonNull final float[] array) {
        final float n4 = n / 255.0f;
        final float n5 = n2 / 255.0f;
        final float n6 = n3 / 255.0f;
        final float max = Math.max(n4, Math.max(n5, n6));
        final float min = Math.min(n4, Math.min(n5, n6));
        final float n7 = max - min;
        final float n8 = (max + min) / 2.0f;
        float n9;
        float n10;
        if (max == min) {
            n9 = 0.0f;
            n10 = 0.0f;
        }
        else {
            if (max == n4) {
                n9 = (n5 - n6) / n7 % 6.0f;
            }
            else if (max == n5) {
                n9 = (n6 - n4) / n7 + 2.0f;
            }
            else {
                n9 = (n4 - n5) / n7 + 4.0f;
            }
            n10 = n7 / (1.0f - Math.abs(2.0f * n8 - 1.0f));
        }
        float n12;
        final float n11 = n12 = n9 * 60.0f % 360.0f;
        if (n11 < 0.0f) {
            n12 = n11 + 360.0f;
        }
        array[0] = constrain(n12, 0.0f, 360.0f);
        array[1] = constrain(n10, 0.0f, 1.0f);
        array[2] = constrain(n8, 0.0f, 1.0f);
    }
    
    public static void RGBToLAB(@IntRange(from = 0L, to = 255L) final int n, @IntRange(from = 0L, to = 255L) final int n2, @IntRange(from = 0L, to = 255L) final int n3, @NonNull final double[] array) {
        RGBToXYZ(n, n2, n3, array);
        XYZToLAB(array[0], array[1], array[2], array);
    }
    
    public static void RGBToXYZ(@IntRange(from = 0L, to = 255L) final int n, @IntRange(from = 0L, to = 255L) final int n2, @IntRange(from = 0L, to = 255L) final int n3, @NonNull final double[] array) {
        if (array.length != 3) {
            throw new IllegalArgumentException("outXyz must have a length of 3.");
        }
        final double n4 = n / 255.0;
        double pow;
        if (n4 < 0.04045) {
            pow = n4 / 12.92;
        }
        else {
            pow = Math.pow((n4 + 0.055) / 1.055, 2.4);
        }
        final double n5 = n2 / 255.0;
        double pow2;
        if (n5 < 0.04045) {
            pow2 = n5 / 12.92;
        }
        else {
            pow2 = Math.pow((n5 + 0.055) / 1.055, 2.4);
        }
        final double n6 = n3 / 255.0;
        double pow3;
        if (n6 < 0.04045) {
            pow3 = n6 / 12.92;
        }
        else {
            pow3 = Math.pow((n6 + 0.055) / 1.055, 2.4);
        }
        array[0] = (0.4124 * pow + 0.3576 * pow2 + 0.1805 * pow3) * 100.0;
        array[1] = (0.2126 * pow + 0.7152 * pow2 + 0.0722 * pow3) * 100.0;
        array[2] = 100.0 * (pow * 0.0193 + pow2 * 0.1192 + pow3 * 0.9505);
    }
    
    @ColorInt
    public static int XYZToColor(@FloatRange(from = 0.0, to = 95.047) double n, @FloatRange(from = 0.0, to = 100.0) double n2, @FloatRange(from = 0.0, to = 108.883) double a) {
        final double a2 = (3.2406 * n + -1.5372 * n2 + -0.4986 * a) / 100.0;
        final double a3 = (-0.9689 * n + 1.8758 * n2 + 0.0415 * a) / 100.0;
        a = (n * 0.0557 + n2 * -0.204 + 1.057 * a) / 100.0;
        if (a2 > 0.0031308) {
            n = Math.pow(a2, 0.4166666666666667) * 1.055 - 0.055;
        }
        else {
            n = 12.92 * a2;
        }
        if (a3 > 0.0031308) {
            n2 = Math.pow(a3, 0.4166666666666667) * 1.055 - 0.055;
        }
        else {
            n2 = 12.92 * a3;
        }
        if (a > 0.0031308) {
            a = 1.055 * Math.pow(a, 0.4166666666666667) - 0.055;
        }
        else {
            a *= 12.92;
        }
        return Color.rgb(constrain((int)Math.round(n * 255.0), 0, 255), constrain((int)Math.round(n2 * 255.0), 0, 255), constrain((int)Math.round(255.0 * a), 0, 255));
    }
    
    public static void XYZToLAB(@FloatRange(from = 0.0, to = 95.047) double pivotXyzComponent, @FloatRange(from = 0.0, to = 100.0) double pivotXyzComponent2, @FloatRange(from = 0.0, to = 108.883) double pivotXyzComponent3, @NonNull final double[] array) {
        if (array.length != 3) {
            throw new IllegalArgumentException("outLab must have a length of 3.");
        }
        pivotXyzComponent = pivotXyzComponent(pivotXyzComponent / 95.047);
        pivotXyzComponent2 = pivotXyzComponent(pivotXyzComponent2 / 100.0);
        pivotXyzComponent3 = pivotXyzComponent(pivotXyzComponent3 / 108.883);
        array[0] = Math.max(0.0, 116.0 * pivotXyzComponent2 - 16.0);
        array[1] = 500.0 * (pivotXyzComponent - pivotXyzComponent2);
        array[2] = 200.0 * (pivotXyzComponent2 - pivotXyzComponent3);
    }
    
    @ColorInt
    public static int blendARGB(@ColorInt final int n, @ColorInt final int n2, @FloatRange(from = 0.0, to = 1.0) final float n3) {
        final float n4 = 1.0f - n3;
        return Color.argb((int)(Color.alpha(n) * n4 + Color.alpha(n2) * n3), (int)(Color.red(n) * n4 + Color.red(n2) * n3), (int)(Color.green(n) * n4 + Color.green(n2) * n3), (int)(Color.blue(n) * n4 + Color.blue(n2) * n3));
    }
    
    public static void blendHSL(@NonNull final float[] array, @NonNull final float[] array2, @FloatRange(from = 0.0, to = 1.0) final float n, @NonNull final float[] array3) {
        if (array3.length != 3) {
            throw new IllegalArgumentException("result must have a length of 3.");
        }
        final float n2 = 1.0f - n;
        array3[0] = circularInterpolate(array[0], array2[0], n);
        array3[1] = array[1] * n2 + array2[1] * n;
        array3[2] = array[2] * n2 + array2[2] * n;
    }
    
    public static void blendLAB(@NonNull final double[] array, @NonNull final double[] array2, @FloatRange(from = 0.0, to = 1.0) final double n, @NonNull final double[] array3) {
        if (array3.length != 3) {
            throw new IllegalArgumentException("outResult must have a length of 3.");
        }
        final double n2 = 1.0 - n;
        array3[0] = array[0] * n2 + array2[0] * n;
        array3[1] = array[1] * n2 + array2[1] * n;
        array3[2] = array[2] * n2 + array2[2] * n;
    }
    
    public static double calculateContrast(@ColorInt final int n, @ColorInt final int i) {
        if (Color.alpha(i) != 255) {
            final StringBuilder sb = new StringBuilder();
            sb.append("background can not be translucent: #");
            sb.append(Integer.toHexString(i));
            throw new IllegalArgumentException(sb.toString());
        }
        int compositeColors = n;
        if (Color.alpha(n) < 255) {
            compositeColors = compositeColors(n, i);
        }
        final double n2 = calculateLuminance(compositeColors) + 0.05;
        final double n3 = calculateLuminance(i) + 0.05;
        return Math.max(n2, n3) / Math.min(n2, n3);
    }
    
    @FloatRange(from = 0.0, to = 1.0)
    public static double calculateLuminance(@ColorInt final int n) {
        final double[] tempDouble3Array = getTempDouble3Array();
        colorToXYZ(n, tempDouble3Array);
        return tempDouble3Array[1] / 100.0;
    }
    
    public static int calculateMinimumAlpha(@ColorInt final int n, @ColorInt final int i, final float n2) {
        final int alpha = Color.alpha(i);
        int n3 = 255;
        if (alpha != 255) {
            final StringBuilder sb = new StringBuilder();
            sb.append("background can not be translucent: #");
            sb.append(Integer.toHexString(i));
            throw new IllegalArgumentException(sb.toString());
        }
        final double calculateContrast = calculateContrast(setAlphaComponent(n, 255), i);
        final double n4 = n2;
        if (calculateContrast < n4) {
            return -1;
        }
        for (int n5 = 0, n6 = 0; n5 <= 10 && n3 - n6 > 1; ++n5) {
            final int n7 = (n6 + n3) / 2;
            if (calculateContrast(setAlphaComponent(n, n7), i) < n4) {
                n6 = n7;
            }
            else {
                n3 = n7;
            }
        }
        return n3;
    }
    
    @VisibleForTesting
    static float circularInterpolate(final float n, final float n2, final float n3) {
        float n4 = n;
        float n5 = n2;
        if (Math.abs(n2 - n) > 180.0f) {
            if (n2 > n) {
                n4 = n + 360.0f;
                n5 = n2;
            }
            else {
                n5 = n2 + 360.0f;
                n4 = n;
            }
        }
        return (n4 + (n5 - n4) * n3) % 360.0f;
    }
    
    public static void colorToHSL(@ColorInt final int n, @NonNull final float[] array) {
        RGBToHSL(Color.red(n), Color.green(n), Color.blue(n), array);
    }
    
    public static void colorToLAB(@ColorInt final int n, @NonNull final double[] array) {
        RGBToLAB(Color.red(n), Color.green(n), Color.blue(n), array);
    }
    
    public static void colorToXYZ(@ColorInt final int n, @NonNull final double[] array) {
        RGBToXYZ(Color.red(n), Color.green(n), Color.blue(n), array);
    }
    
    private static int compositeAlpha(final int n, final int n2) {
        return 255 - (255 - n2) * (255 - n) / 255;
    }
    
    public static int compositeColors(@ColorInt final int n, @ColorInt final int n2) {
        final int alpha = Color.alpha(n2);
        final int alpha2 = Color.alpha(n);
        final int compositeAlpha = compositeAlpha(alpha2, alpha);
        return Color.argb(compositeAlpha, compositeComponent(Color.red(n), alpha2, Color.red(n2), alpha, compositeAlpha), compositeComponent(Color.green(n), alpha2, Color.green(n2), alpha, compositeAlpha), compositeComponent(Color.blue(n), alpha2, Color.blue(n2), alpha, compositeAlpha));
    }
    
    @NonNull
    @RequiresApi(26)
    public static Color compositeColors(@NonNull Color convert, @NonNull final Color color) {
        if (!Objects.equals(convert.getModel(), color.getModel())) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Color models must match (");
            sb.append(convert.getModel());
            sb.append(" vs. ");
            sb.append(color.getModel());
            sb.append(")");
            throw new IllegalArgumentException(sb.toString());
        }
        if (!Objects.equals(color.getColorSpace(), convert.getColorSpace())) {
            convert = convert.convert(color.getColorSpace());
        }
        final float[] components = convert.getComponents();
        final float[] components2 = color.getComponents();
        final float alpha = convert.alpha();
        final float n = color.alpha() * (1.0f - alpha);
        final int n2 = color.getComponentCount() - 1;
        components2[n2] = alpha + n;
        float n3 = n;
        float n4 = alpha;
        if (components2[n2] > 0.0f) {
            n4 = alpha / components2[n2];
            n3 = n / components2[n2];
        }
        for (int i = 0; i < n2; ++i) {
            components2[i] = components[i] * n4 + components2[i] * n3;
        }
        return Color.valueOf(components2, color.getColorSpace());
    }
    
    private static int compositeComponent(final int n, final int n2, final int n3, final int n4, final int n5) {
        if (n5 == 0) {
            return 0;
        }
        return (n * 255 * n2 + n3 * n4 * (255 - n2)) / (n5 * 255);
    }
    
    private static float constrain(final float n, float n2, final float n3) {
        if (n >= n2) {
            n2 = n;
            if (n > n3) {
                n2 = n3;
            }
        }
        return n2;
    }
    
    private static int constrain(final int n, int n2, final int n3) {
        if (n >= n2) {
            if ((n2 = n) > n3) {
                n2 = n3;
            }
        }
        return n2;
    }
    
    public static double distanceEuclidean(@NonNull final double[] array, @NonNull final double[] array2) {
        return Math.sqrt(Math.pow(array[0] - array2[0], 2.0) + Math.pow(array[1] - array2[1], 2.0) + Math.pow(array[2] - array2[2], 2.0));
    }
    
    private static double[] getTempDouble3Array() {
        double[] value;
        if ((value = ColorUtils.TEMP_ARRAY.get()) == null) {
            value = new double[3];
            ColorUtils.TEMP_ARRAY.set(value);
        }
        return value;
    }
    
    private static double pivotXyzComponent(double pow) {
        if (pow > 0.008856) {
            pow = Math.pow(pow, 0.3333333333333333);
        }
        else {
            pow = (903.3 * pow + 16.0) / 116.0;
        }
        return pow;
    }
    
    @ColorInt
    public static int setAlphaComponent(@ColorInt final int n, @IntRange(from = 0L, to = 255L) final int n2) {
        if (n2 >= 0 && n2 <= 255) {
            return (n & 0xFFFFFF) | n2 << 24;
        }
        throw new IllegalArgumentException("alpha must be between 0 and 255.");
    }
}
