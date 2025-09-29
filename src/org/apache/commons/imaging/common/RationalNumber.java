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

    public static RationalNumber valueOf(double abs) {
        if (abs >= 2.147483647E9) {
            return new RationalNumber(Integer.MAX_VALUE, 1);
        }
        if (abs <= -2.147483647E9) {
            return new RationalNumber(-2147483647, 1);
        }
        int n = 0;
        boolean b;
        if (abs < 0.0) {
            abs = Math.abs(abs);
            b = true;
        }
        else {
            b = false;
        }
        if (abs == 0.0) {
            return new RationalNumber(0, 1);
        }
        RationalNumber rationalNumber;
        RationalNumber rationalNumber2;
        if (abs >= 1.0) {
            final int n2 = (int)abs;
            if (n2 < abs) {
                rationalNumber = new RationalNumber(n2, 1);
                rationalNumber2 = new RationalNumber(n2 + 1, 1);
            }
            else {
                rationalNumber = new RationalNumber(n2 - 1, 1);
                rationalNumber2 = new RationalNumber(n2, 1);
            }
        }
        else {
            final int n3 = (int)(1.0 / abs);
            if (1.0 / n3 < abs) {
                rationalNumber = new RationalNumber(1, n3);
                rationalNumber2 = new RationalNumber(1, n3 - 1);
            }
            else {
                rationalNumber = new RationalNumber(1, n3 + 1);
                rationalNumber2 = new RationalNumber(1, n3);
            }
        }
        Option factory = Option.factory(rationalNumber, abs);
        Option factory2 = Option.factory(rationalNumber2, abs);
        Option option;
        if (factory.error < factory2.error) {
            option = factory;
        }
        else {
            option = factory2;
        }
        while (option.error > 1.0E-8 && n < 100) {
            final RationalNumber factoryMethod = factoryMethod(factory.rationalNumber.numerator + (long)factory2.rationalNumber.numerator, factory.rationalNumber.divisor + (long)factory2.rationalNumber.divisor);
            final Option factory3 = Option.factory(factoryMethod, abs);
            if (abs < factoryMethod.doubleValue()) {
                if (factory2.error <= factory3.error) {
                    break;
                }
                factory2 = factory3;
            }
            else {
                if (factory.error <= factory3.error) {
                    break;
                }
                factory = factory3;
            }
            Option option2 = option;
            if (factory3.error < option.error) {
                option2 = factory3;
            }
            ++n;
            option = option2;
        }
        RationalNumber rationalNumber3;
        if (b) {
            rationalNumber3 = option.rationalNumber.negate();
        }
        else {
            rationalNumber3 = option.rationalNumber;
        }
        return rationalNumber3;
    }
}
