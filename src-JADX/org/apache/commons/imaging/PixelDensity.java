package org.apache.commons.imaging;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class PixelDensity {
    private static final int PIXEL_NO_UNIT = 0;
    private static final int PIXEL_PER_CENTIMETRE = 100;
    private static final int PIXEL_PER_INCH = 254;
    private static final int PIXEL_PER_METRE = 10000;
    private final double horizontalDensity;
    private final int unitLength;
    private final double verticalDensity;

    private PixelDensity(double d, double d2, int i) {
        this.horizontalDensity = d;
        this.verticalDensity = d2;
        this.unitLength = i;
    }

    public static PixelDensity createUnitless(double d, double d2) {
        return new PixelDensity(d, d2, 0);
    }

    public static PixelDensity createFromPixelsPerInch(double d, double d2) {
        return new PixelDensity(d, d2, 254);
    }

    public static PixelDensity createFromPixelsPerMetre(double d, double d2) {
        return new PixelDensity(d, d2, 10000);
    }

    public static PixelDensity createFromPixelsPerCentimetre(double d, double d2) {
        return new PixelDensity(d, d2, 100);
    }

    public boolean isUnitless() {
        return this.unitLength == 0;
    }

    public boolean isInInches() {
        return this.unitLength == 254;
    }

    public boolean isInCentimetres() {
        return this.unitLength == 100;
    }

    public boolean isInMetres() {
        return this.unitLength == 10000;
    }

    public double getRawHorizontalDensity() {
        return this.horizontalDensity;
    }

    public double getRawVerticalDensity() {
        return this.verticalDensity;
    }

    public double horizontalDensityInches() {
        if (isInInches()) {
            return this.horizontalDensity;
        }
        return (this.horizontalDensity * 254.0d) / this.unitLength;
    }

    public double verticalDensityInches() {
        if (isInInches()) {
            return this.verticalDensity;
        }
        return (this.verticalDensity * 254.0d) / this.unitLength;
    }

    public double horizontalDensityMetres() {
        if (isInMetres()) {
            return this.horizontalDensity;
        }
        return (this.horizontalDensity * 10000.0d) / this.unitLength;
    }

    public double verticalDensityMetres() {
        if (isInMetres()) {
            return this.verticalDensity;
        }
        return (this.verticalDensity * 10000.0d) / this.unitLength;
    }

    public double horizontalDensityCentimetres() {
        if (isInCentimetres()) {
            return this.horizontalDensity;
        }
        return (this.horizontalDensity * 100.0d) / this.unitLength;
    }

    public double verticalDensityCentimetres() {
        if (isInCentimetres()) {
            return this.verticalDensity;
        }
        return (this.verticalDensity * 100.0d) / this.unitLength;
    }
}
