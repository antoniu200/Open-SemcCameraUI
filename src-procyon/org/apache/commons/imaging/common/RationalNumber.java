// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.common;

import java.text.NumberFormat;

public class RationalNumber extends Number
{
    private static final double TOLERANCE = 1.0E-8;
    private static final long serialVersionUID = -8412262656468158691L;
    public final int divisor;
    public final int numerator;
    
    public RationalNumber(final int numerator, final int divisor) {
        this.numerator = numerator;
        this.divisor = divisor;
    }
    
    static RationalNumber factoryMethod(long gcd, long n) {
        long n2 = gcd;
        long n3 = n;
        long n4 = 0L;
        long n5 = 0L;
        Label_0202: {
            if (gcd <= 2147483647L) {
                n2 = gcd;
                n3 = n;
                if (gcd >= -2147483648L) {
                    n2 = gcd;
                    n3 = n;
                    if (n <= 2147483647L) {
                        n4 = gcd;
                        n5 = n;
                        if (n >= -2147483648L) {
                            break Label_0202;
                        }
                        n3 = n;
                        n2 = gcd;
                    }
                }
            }
            while ((n2 > 2147483647L || n2 < -2147483648L || n3 > 2147483647L || n3 < -2147483648L) && Math.abs(n2) > 1L && Math.abs(n3) > 1L) {
                n2 >>= 1;
                n3 >>= 1;
            }
            n4 = n2;
            n5 = n3;
            if (n3 == 0L) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Invalid value, numerator: ");
                sb.append(n2);
                sb.append(", divisor: ");
                sb.append(n3);
                throw new NumberFormatException(sb.toString());
            }
        }
        gcd = gcd(n4, n5);
        n = n5 / gcd;
        return new RationalNumber((int)(n4 / gcd), (int)n);
    }
    
    private static long gcd(final long n, final long n2) {
        if (n2 == 0L) {
            return n;
        }
        return gcd(n2, n % n2);
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
    
    @Override
    public double doubleValue() {
        return this.numerator / (double)this.divisor;
    }
    
    @Override
    public float floatValue() {
        return this.numerator / (float)this.divisor;
    }
    
    @Override
    public int intValue() {
        return this.numerator / this.divisor;
    }
    
    @Override
    public long longValue() {
        return this.numerator / (long)this.divisor;
    }
    
    public RationalNumber negate() {
        return new RationalNumber(-this.numerator, this.divisor);
    }
    
    public String toDisplayString() {
        if (this.numerator % this.divisor == 0) {
            return Integer.toString(this.numerator / this.divisor);
        }
        final NumberFormat instance = NumberFormat.getInstance();
        instance.setMaximumFractionDigits(3);
        return instance.format(this.numerator / (double)this.divisor);
    }
    
    @Override
    public String toString() {
        if (this.divisor == 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid rational (");
            sb.append(this.numerator);
            sb.append("/");
            sb.append(this.divisor);
            sb.append(")");
            return sb.toString();
        }
        final NumberFormat instance = NumberFormat.getInstance();
        if (this.numerator % this.divisor == 0) {
            return instance.format(this.numerator / this.divisor);
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(this.numerator);
        sb2.append("/");
        sb2.append(this.divisor);
        sb2.append(" (");
        sb2.append(instance.format(this.numerator / (double)this.divisor));
        sb2.append(")");
        return sb2.toString();
    }
    
    private static class Option
    {
        public final double error;
        public final RationalNumber rationalNumber;
        
        private Option(final RationalNumber rationalNumber, final double error) {
            this.rationalNumber = rationalNumber;
            this.error = error;
        }
        
        public static Option factory(final RationalNumber rationalNumber, final double n) {
            return new Option(rationalNumber, Math.abs(rationalNumber.doubleValue() - n));
        }
        
        @Override
        public String toString() {
            return this.rationalNumber.toString();
        }
    }
}
