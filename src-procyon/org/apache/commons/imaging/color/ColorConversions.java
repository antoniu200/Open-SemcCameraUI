// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.color;

public final class ColorConversions
{
    private static final double REF_X = 95.047;
    private static final double REF_Y = 100.0;
    private static final double REF_Z = 108.883;
    
    private ColorConversions() {
    }
    
    public static ColorCieLab convertCIELCHtoCIELab(final double n, final double n2, final double n3) {
        return new ColorCieLab(n, Math.cos(degree_2_radian(n3)) * n2, Math.sin(degree_2_radian(n3)) * n2);
    }
    
    public static ColorCieLab convertCIELCHtoCIELab(final ColorCieLch colorCieLch) {
        return convertCIELCHtoCIELab(colorCieLch.L, colorCieLch.C, colorCieLch.H);
    }
    
    public static int convertCIELabtoARGBTest(final int n, final int n2, final int n3) {
        final double n4 = (n * 100.0 / 255.0 + 16.0) / 116.0;
        final double n5 = n2 / 500.0 + n4;
        final double n6 = n4 - n3 / 200.0;
        double cube = cube(n5);
        double cube2 = cube(n4);
        double cube3 = cube(n6);
        if (cube2 <= 0.008856) {
            cube2 = (n4 - 0.13793103448275862) / 7.787;
        }
        if (cube <= 0.008856) {
            cube = (n5 - 0.13793103448275862) / 7.787;
        }
        if (cube3 <= 0.008856) {
            cube3 = (n6 - 0.13793103448275862) / 7.787;
        }
        final double n7 = 95.047 * cube / 100.0;
        final double n8 = cube2 * 100.0 / 100.0;
        final double n9 = 108.883 * cube3 / 100.0;
        final double a = 3.2406 * n7 + -1.5372 * n8 + -0.4986 * n9;
        final double a2 = -0.9689 * n7 + 1.8758 * n8 + 0.0415 * n9;
        final double a3 = n7 * 0.0557 + n8 * -0.204 + n9 * 1.057;
        double n10;
        if (a > 0.0031308) {
            n10 = Math.pow(a, 0.4166666666666667) * 1.055 - 0.055;
        }
        else {
            n10 = a * 12.92;
        }
        double n11;
        if (a2 > 0.0031308) {
            n11 = Math.pow(a2, 0.4166666666666667) * 1.055 - 0.055;
        }
        else {
            n11 = a2 * 12.92;
        }
        double n12;
        if (a3 > 0.0031308) {
            n12 = 1.055 * Math.pow(a3, 0.4166666666666667) - 0.055;
        }
        else {
            n12 = 12.92 * a3;
        }
        return convertRGBtoRGB(n10 * 255.0, n11 * 255.0, n12 * 255.0);
    }
    
    public static ColorCieLch convertCIELabtoCIELCH(final double n, final double x, final double y) {
        final double atan2 = Math.atan2(y, x);
        double n2;
        if (atan2 > 0.0) {
            n2 = atan2 / 3.141592653589793 * 180.0;
        }
        else {
            n2 = 360.0 - radian_2_degree(Math.abs(atan2));
        }
        return new ColorCieLch(n, Math.sqrt(square(x) + square(y)), n2);
    }
    
    public static ColorCieLch convertCIELabtoCIELCH(final ColorCieLab colorCieLab) {
        return convertCIELabtoCIELCH(colorCieLab.L, colorCieLab.a, colorCieLab.b);
    }
    
    public static ColorXyz convertCIELabtoXYZ(double pow, double pow2, double pow3) {
        pow = (pow + 16.0) / 116.0;
        pow2 = pow2 / 500.0 + pow;
        pow3 = pow - pow3 / 200.0;
        if (Math.pow(pow, 3.0) > 0.008856) {
            pow = Math.pow(pow, 3.0);
        }
        else {
            pow = (pow - 0.13793103448275862) / 7.787;
        }
        if (Math.pow(pow2, 3.0) > 0.008856) {
            pow2 = Math.pow(pow2, 3.0);
        }
        else {
            pow2 = (pow2 - 0.13793103448275862) / 7.787;
        }
        if (Math.pow(pow3, 3.0) > 0.008856) {
            pow3 = Math.pow(pow3, 3.0);
        }
        else {
            pow3 = (pow3 - 0.13793103448275862) / 7.787;
        }
        return new ColorXyz(95.047 * pow2, 100.0 * pow, 108.883 * pow3);
    }
    
    public static ColorXyz convertCIELabtoXYZ(final ColorCieLab colorCieLab) {
        return convertCIELabtoXYZ(colorCieLab.L, colorCieLab.a, colorCieLab.b);
    }
    
    public static ColorXyz convertCIELuvtoXYZ(double n, double n2, double n3) {
        final double n4 = (n + 16.0) / 116.0;
        double pow;
        if (Math.pow(n4, 3.0) > 0.008856) {
            pow = Math.pow(n4, 3.0);
        }
        else {
            pow = (n4 - 0.0) / 7.787;
        }
        final double n5 = 13.0 * n;
        n = n2 / n5 + 0.19783982482140777;
        n2 = n3 / n5 + 0.46833630293240974;
        final double n6 = pow * 100.0;
        n3 = 9.0 * n6;
        n = -(n3 * n) / ((n - 4.0) * n2 - n * n2);
        return new ColorXyz(n, n6, (n3 - 15.0 * n2 * n6 - n2 * n) / (3.0 * n2));
    }
    
    public static ColorXyz convertCIELuvtoXYZ(final ColorCieLuv colorCieLuv) {
        return convertCIELuvtoXYZ(colorCieLuv.L, colorCieLuv.u, colorCieLuv.v);
    }
    
    public static ColorCmy convertCMYKtoCMY(final double n, final double n2, final double n3, final double n4) {
        final double n5 = 1.0 - n4;
        return new ColorCmy(n * n5 + n4, n2 * n5 + n4, n5 * n3 + n4);
    }
    
    public static ColorCmy convertCMYKtoCMY(final ColorCmyk colorCmyk) {
        return convertCMYKtoCMY(colorCmyk.C, colorCmyk.M, colorCmyk.Y, colorCmyk.K);
    }
    
    public static int convertCMYKtoRGB(final int n, final int n2, final int n3, final int n4) {
        return convertCMYtoRGB(convertCMYKtoCMY(n / 255.0, n2 / 255.0, n3 / 255.0, n4 / 255.0));
    }
    
    public static int convertCMYKtoRGB_Adobe(final int n, final int n2, final int n3, final int n4) {
        return convertRGBtoRGB(255 - (n + n4), 255 - (n2 + n4), 255 - (n3 + n4));
    }
    
    public static ColorCmyk convertCMYtoCMYK(final ColorCmy colorCmy) {
        final double c = colorCmy.C;
        final double m = colorCmy.M;
        final double y = colorCmy.Y;
        double n;
        if (c < 1.0) {
            n = c;
        }
        else {
            n = 1.0;
        }
        double n2 = n;
        if (m < n) {
            n2 = m;
        }
        double n3;
        if (y < n2) {
            n3 = y;
        }
        else {
            n3 = n2;
        }
        double n4;
        double n6;
        double n5;
        if (n3 == 1.0) {
            n4 = 0.0;
            n5 = (n6 = 0.0);
        }
        else {
            final double n7 = 1.0 - n3;
            n4 = (c - n3) / n7;
            final double n8 = (m - n3) / n7;
            final double n9 = (y - n3) / n7;
            n5 = n8;
            n6 = n9;
        }
        return new ColorCmyk(n4, n5, n6, n3);
    }
    
    public static int convertCMYtoRGB(final ColorCmy colorCmy) {
        return convertRGBtoRGB((1.0 - colorCmy.C) * 255.0, (1.0 - colorCmy.M) * 255.0, (1.0 - colorCmy.Y) * 255.0);
    }
    
    public static int convertHSLtoRGB(double convertHuetoRGB, double n, double convertHuetoRGB2) {
        double n2;
        if (n == 0.0) {
            n2 = convertHuetoRGB2 * 255.0;
            convertHuetoRGB = (n = (convertHuetoRGB2 = n2));
            convertHuetoRGB = convertHuetoRGB2;
        }
        else {
            if (convertHuetoRGB2 < 0.5) {
                n = convertHuetoRGB2 * (1.0 + n);
            }
            else {
                n = convertHuetoRGB2 + n - n * convertHuetoRGB2;
            }
            final double n3 = 2.0 * convertHuetoRGB2 - n;
            convertHuetoRGB2 = convertHuetoRGB(n3, n, convertHuetoRGB + 0.3333333333333333);
            final double convertHuetoRGB3 = convertHuetoRGB(n3, n, convertHuetoRGB);
            convertHuetoRGB = convertHuetoRGB(n3, n, convertHuetoRGB - 0.3333333333333333);
            n = convertHuetoRGB3 * 255.0;
            convertHuetoRGB *= 255.0;
            n2 = 255.0 * convertHuetoRGB2;
        }
        return convertRGBtoRGB(n2, n, convertHuetoRGB);
    }
    
    public static int convertHSLtoRGB(final ColorHsl colorHsl) {
        return convertHSLtoRGB(colorHsl.H, colorHsl.S, colorHsl.L);
    }
    
    public static int convertHSVtoRGB(double n, double n2, double n3) {
        double n4;
        if (n2 == 0.0) {
            n4 = n3 * 255.0;
            n = (n2 = (n3 = n4));
            n = n3;
        }
        else {
            double a;
            n = (a = n * 6.0);
            if (n == 6.0) {
                a = 0.0;
            }
            final double floor = Math.floor(a);
            n = (1.0 - n2) * n3;
            final double n5 = a - floor;
            final double n6 = (1.0 - n2 * n5) * n3;
            n2 = (1.0 - n2 * (1.0 - n5)) * n3;
            Label_0196: {
                if (floor == 0.0) {
                    final double n7 = n2;
                    n2 = n;
                    n = n7;
                }
                else {
                    if (floor == 1.0) {
                        n2 = n;
                        n = n6;
                        break Label_0196;
                    }
                    if (floor == 2.0) {
                        break Label_0196;
                    }
                    if (floor == 3.0) {
                        n2 = n3;
                        n3 = n6;
                        break Label_0196;
                    }
                    if (floor == 4.0) {
                        final double n8 = n2;
                        n2 = n3;
                        n3 = n;
                        n = n8;
                        break Label_0196;
                    }
                    n2 = n6;
                }
                final double n9 = n3;
                n3 = n;
                n = n9;
            }
            n2 *= 255.0;
            n3 *= 255.0;
            n4 = n * 255.0;
            n = n2;
            n2 = n3;
        }
        return convertRGBtoRGB(n4, n2, n);
    }
    
    public static int convertHSVtoRGB(final ColorHsv colorHsv) {
        return convertHSVtoRGB(colorHsv.H, colorHsv.S, colorHsv.V);
    }
    
    private static double convertHuetoRGB(final double n, final double n2, double n3) {
        double n4 = n3;
        if (n3 < 0.0) {
            n4 = n3 + 1.0;
        }
        n3 = n4;
        if (n4 > 1.0) {
            n3 = n4 - 1.0;
        }
        if (6.0 * n3 < 1.0) {
            return n + (n2 - n) * 6.0 * n3;
        }
        if (2.0 * n3 < 1.0) {
            return n2;
        }
        if (3.0 * n3 < 2.0) {
            return n + (n2 - n) * (0.6666666666666666 - n3) * 6.0;
        }
        return n;
    }
    
    public static ColorXyz convertHunterLabtoXYZ(double n, double n2, double pow) {
        final double a = n / 10.0;
        n2 = n2 / 17.5 * n / 10.0;
        n = pow / 7.0 * n / 10.0;
        pow = Math.pow(a, 2.0);
        return new ColorXyz((n2 + pow) / 1.02, pow, -(n - pow) / 0.847);
    }
    
    public static ColorXyz convertHunterLabtoXYZ(final ColorHunterLab colorHunterLab) {
        return convertHunterLabtoXYZ(colorHunterLab.L, colorHunterLab.a, colorHunterLab.b);
    }
    
    public static ColorCmy convertRGBtoCMY(final int n) {
        return new ColorCmy(1.0 - (n >> 16 & 0xFF) / 255.0, 1.0 - (n >> 8 & 0xFF) / 255.0, 1.0 - (n >> 0 & 0xFF) / 255.0);
    }
    
    public static ColorHsl convertRGBtoHSL(int n) {
        final double a = (n >> 16 & 0xFF) / 255.0;
        final double a2 = (n >> 8 & 0xFF) / 255.0;
        final double b = (n >> 0 & 0xFF) / 255.0;
        final double min = Math.min(a, Math.min(a2, b));
        boolean b2;
        double n2;
        if (a >= a2 && a >= b) {
            n = 1;
            b2 = false;
            n2 = a;
        }
        else if (a2 > b) {
            b2 = true;
            n = 0;
            n2 = a2;
        }
        else {
            n = 0;
            b2 = false;
            n2 = b;
        }
        final double n3 = n2 - min;
        final double n4 = n2 + min;
        final double n5 = n4 / 2.0;
        double n6;
        double n7;
        if (n3 == 0.0) {
            n6 = 0.0;
            n7 = 0.0;
        }
        else {
            if (n5 < 0.5) {
                n6 = n3 / n4;
            }
            else {
                n6 = n3 / (2.0 - n2 - min);
            }
            final double n8 = (n2 - a) / 6.0;
            final double n9 = n3 / 2.0;
            final double n10 = (n8 + n9) / n3;
            final double n11 = ((n2 - a2) / 6.0 + n9) / n3;
            final double n12 = ((n2 - b) / 6.0 + n9) / n3;
            double n13;
            if (n != 0) {
                n13 = n12 - n11;
            }
            else if (b2) {
                n13 = 0.3333333333333333 + n10 - n12;
            }
            else {
                n13 = 0.6666666666666666 + n11 - n10;
            }
            double n14 = n13;
            if (n13 < 0.0) {
                n14 = n13 + 1.0;
            }
            n7 = n14;
            if (n14 > 1.0) {
                n7 = n14 - 1.0;
            }
        }
        return new ColorHsl(n7, n6, n5);
    }
    
    public static ColorHsv convertRGBtoHSV(int n) {
        boolean b = false;
        final double a = (n >> 16 & 0xFF) / 255.0;
        final double a2 = (n >> 8 & 0xFF) / 255.0;
        final double b2 = (n >> 0 & 0xFF) / 255.0;
        final double min = Math.min(a, Math.min(a2, b2));
        n = 1;
        double n2;
        if (a >= a2 && a >= b2) {
            n2 = a;
            b = true;
            n = 0;
        }
        else if (a2 > b2) {
            n2 = a2;
        }
        else {
            n = 0;
            n2 = b2;
        }
        final double n3 = n2 - min;
        double n4;
        double n5;
        if (n3 == 0.0) {
            n4 = 0.0;
            n5 = 0.0;
        }
        else {
            final double n6 = n3 / n2;
            final double n7 = (n2 - a) / 6.0;
            final double n8 = n3 / 2.0;
            final double n9 = (n7 + n8) / n3;
            final double n10 = ((n2 - a2) / 6.0 + n8) / n3;
            final double n11 = ((n2 - b2) / 6.0 + n8) / n3;
            double n12;
            if (b) {
                n12 = n11 - n10;
            }
            else if (n != 0) {
                n12 = 0.3333333333333333 + n9 - n11;
            }
            else {
                n12 = 0.6666666666666666 + n10 - n9;
            }
            double n13 = n12;
            if (n12 < 0.0) {
                n13 = n12 + 1.0;
            }
            n5 = n13;
            if (n13 > 1.0) {
                n5 = n13 - 1.0;
            }
            n4 = n6;
        }
        return new ColorHsv(n5, n4, n2);
    }
    
    private static int convertRGBtoRGB(final double a, final double a2, final double a3) {
        return Math.min(255, Math.max(0, (int)Math.round(a))) << 16 | 0xFF000000 | Math.min(255, Math.max(0, (int)Math.round(a2))) << 8 | Math.min(255, Math.max(0, (int)Math.round(a3))) << 0;
    }
    
    private static int convertRGBtoRGB(final int b, final int b2, final int b3) {
        return Math.min(255, Math.max(0, b)) << 16 | 0xFF000000 | Math.min(255, Math.max(0, b2)) << 8 | Math.min(255, Math.max(0, b3)) << 0;
    }
    
    public static ColorXyz convertRGBtoXYZ(final int n) {
        final double n2 = (n >> 16 & 0xFF) / 255.0;
        final double n3 = (n >> 8 & 0xFF) / 255.0;
        final double n4 = (n >> 0 & 0xFF) / 255.0;
        double pow;
        if (n2 > 0.04045) {
            pow = Math.pow((n2 + 0.055) / 1.055, 2.4);
        }
        else {
            pow = n2 / 12.92;
        }
        double pow2;
        if (n3 > 0.04045) {
            pow2 = Math.pow((n3 + 0.055) / 1.055, 2.4);
        }
        else {
            pow2 = n3 / 12.92;
        }
        double pow3;
        if (n4 > 0.04045) {
            pow3 = Math.pow((n4 + 0.055) / 1.055, 2.4);
        }
        else {
            pow3 = n4 / 12.92;
        }
        final double n5 = pow * 100.0;
        final double n6 = pow2 * 100.0;
        final double n7 = pow3 * 100.0;
        return new ColorXyz(0.4124 * n5 + 0.3576 * n6 + 0.1805 * n7, 0.2126 * n5 + 0.7152 * n6 + 0.0722 * n7, n5 * 0.0193 + n6 * 0.1192 + n7 * 0.9505);
    }
    
    public static ColorCieLab convertXYZtoCIELab(double pow, double pow2, double pow3) {
        pow /= 95.047;
        pow2 /= 100.0;
        pow3 /= 108.883;
        if (pow > 0.008856) {
            pow = Math.pow(pow, 0.3333333333333333);
        }
        else {
            pow = pow * 7.787 + 0.13793103448275862;
        }
        if (pow2 > 0.008856) {
            pow2 = Math.pow(pow2, 0.3333333333333333);
        }
        else {
            pow2 = pow2 * 7.787 + 0.13793103448275862;
        }
        if (pow3 > 0.008856) {
            pow3 = Math.pow(pow3, 0.3333333333333333);
        }
        else {
            pow3 = 7.787 * pow3 + 0.13793103448275862;
        }
        return new ColorCieLab(116.0 * pow2 - 16.0, 500.0 * (pow - pow2), 200.0 * (pow2 - pow3));
    }
    
    public static ColorCieLab convertXYZtoCIELab(final ColorXyz colorXyz) {
        return convertXYZtoCIELab(colorXyz.X, colorXyz.Y, colorXyz.Z);
    }
    
    public static ColorCieLuv convertXYZtoCIELuv(double pow, double n, double n2) {
        final double n3 = pow + 15.0 * n + 3.0 * n2;
        n2 = 4.0 * pow / n3;
        final double n4 = 9.0 * n / n3;
        pow = n / 100.0;
        if (pow > 0.008856) {
            pow = Math.pow(pow, 0.3333333333333333);
        }
        else {
            pow = 7.787 * pow + 0.13793103448275862;
        }
        pow = 116.0 * pow - 16.0;
        n = 13.0 * pow;
        return new ColorCieLuv(pow, n * (n2 - 0.19783982482140777), n * (n4 - 0.46833630293240974));
    }
    
    public static ColorCieLuv convertXYZtoCIELuv(final ColorXyz colorXyz) {
        return convertXYZtoCIELuv(colorXyz.X, colorXyz.Y, colorXyz.Z);
    }
    
    public static ColorHunterLab convertXYZtoHunterLab(final double n, final double a, final double n2) {
        return new ColorHunterLab(10.0 * Math.sqrt(a), 17.5 * ((1.02 * n - a) / Math.sqrt(a)), 7.0 * ((a - 0.847 * n2) / Math.sqrt(a)));
    }
    
    public static ColorHunterLab convertXYZtoHunterLab(final ColorXyz colorXyz) {
        return convertXYZtoHunterLab(colorXyz.X, colorXyz.Y, colorXyz.Z);
    }
    
    public static int convertXYZtoRGB(double n, double a, double a2) {
        n /= 100.0;
        final double n2 = a / 100.0;
        a2 /= 100.0;
        final double a3 = 3.2406 * n + -1.5372 * n2 + -0.4986 * a2;
        a = -0.9689 * n + 1.8758 * n2 + 0.0415 * a2;
        a2 = n * 0.0557 + n2 * -0.204 + a2 * 1.057;
        if (a3 > 0.0031308) {
            n = Math.pow(a3, 0.4166666666666667) * 1.055 - 0.055;
        }
        else {
            n = 12.92 * a3;
        }
        if (a > 0.0031308) {
            a = Math.pow(a, 0.4166666666666667) * 1.055 - 0.055;
        }
        else {
            a *= 12.92;
        }
        if (a2 > 0.0031308) {
            a2 = 1.055 * Math.pow(a2, 0.4166666666666667) - 0.055;
        }
        else {
            a2 *= 12.92;
        }
        return convertRGBtoRGB(n * 255.0, a * 255.0, a2 * 255.0);
    }
    
    public static int convertXYZtoRGB(final ColorXyz colorXyz) {
        return convertXYZtoRGB(colorXyz.X, colorXyz.Y, colorXyz.Z);
    }
    
    private static double cube(final double n) {
        return n * n * n;
    }
    
    public static double degree_2_radian(final double n) {
        return n * 3.141592653589793 / 180.0;
    }
    
    public static double radian_2_degree(final double n) {
        return n * 180.0 / 3.141592653589793;
    }
    
    private static double square(final double n) {
        return n * n;
    }
}
