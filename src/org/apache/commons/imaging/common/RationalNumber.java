package org.apache.commons.imaging.common;

import com.sonyericsson.android.camera.util.capability.SharedPrefsTranslator;
import java.text.NumberFormat;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class RationalNumber extends Number {
    private static final double TOLERANCE = 1.0E-8d;
    private static final long serialVersionUID = -8412262656468158691L;
    public final int divisor;
    public final int numerator;

    public RationalNumber(int i, int i2) {
        this.numerator = i;
        this.divisor = i2;
    }

    static RationalNumber factoryMethod(long j, long j2) {
        if (j > 2147483647L || j < -2147483648L || j2 > 2147483647L || j2 < -2147483648L) {
            while (true) {
                if ((j <= 2147483647L && j >= -2147483648L && j2 <= 2147483647L && j2 >= -2147483648L) || Math.abs(j) <= 1 || Math.abs(j2) <= 1) {
                    break;
                }
                j >>= 1;
                j2 >>= 1;
            }
            if (j2 == 0) {
                throw new NumberFormatException("Invalid value, numerator: " + j + ", divisor: " + j2);
            }
        }
        long jGcd = gcd(j, j2);
        return new RationalNumber((int) (j / jGcd), (int) (j2 / jGcd));
    }

    private static long gcd(long j, long j2) {
        return j2 == 0 ? j : gcd(j2, j % j2);
    }

    public RationalNumber negate() {
        return new RationalNumber(-this.numerator, this.divisor);
    }

    @Override // java.lang.Number
    public double doubleValue() {
        return this.numerator / this.divisor;
    }

    @Override // java.lang.Number
    public float floatValue() {
        return this.numerator / this.divisor;
    }

    @Override // java.lang.Number
    public int intValue() {
        return this.numerator / this.divisor;
    }

    @Override // java.lang.Number
    public long longValue() {
        return this.numerator / this.divisor;
    }

    public String toString() {
        if (this.divisor == 0) {
            return "Invalid rational (" + this.numerator + SharedPrefsTranslator.CONNECTOR_SLASH + this.divisor + ")";
        }
        NumberFormat numberFormat = NumberFormat.getInstance();
        if (this.numerator % this.divisor == 0) {
            return numberFormat.format(this.numerator / this.divisor);
        }
        return this.numerator + SharedPrefsTranslator.CONNECTOR_SLASH + this.divisor + " (" + numberFormat.format(this.numerator / this.divisor) + ")";
    }

    public String toDisplayString() {
        if (this.numerator % this.divisor == 0) {
            return Integer.toString(this.numerator / this.divisor);
        }
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(3);
        return numberFormat.format(this.numerator / this.divisor);
    }

    private static class Option {
        public final double error;
        public final RationalNumber rationalNumber;

        private Option(RationalNumber rationalNumber, double d) {
            this.rationalNumber = rationalNumber;
            this.error = d;
        }

        public static Option factory(RationalNumber rationalNumber, double d) {
            return new Option(rationalNumber, Math.abs(rationalNumber.doubleValue() - d));
        }

        public String toString() {
            return this.rationalNumber.toString();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:49:0x00e9  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x00ea A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static org.apache.commons.imaging.common.RationalNumber valueOf(double r11) {
        /*
            Method dump skipped, instructions count: 249
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.imaging.common.RationalNumber.valueOf(double):org.apache.commons.imaging.common.RationalNumber");
    }
}
