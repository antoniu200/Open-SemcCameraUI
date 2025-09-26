package org.apache.commons.imaging.formats.png;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class GammaCorrection {
    private static final boolean DEBUG = false;
    private final int[] lookupTable = new int[256];

    public GammaCorrection(double d, double d2) {
        for (int i = 0; i < 256; i++) {
            this.lookupTable[i] = correctSample(i, d, d2);
        }
    }

    public int correctSample(int i) {
        return this.lookupTable[i];
    }

    public int correctARGB(int i) {
        int i2 = (-16777216) & i;
        int iCorrectSample = correctSample((i >> 16) & 255);
        int iCorrectSample2 = correctSample((i >> 8) & 255);
        return ((correctSample((i >> 0) & 255) & 255) << 0) | ((255 & iCorrectSample) << 16) | i2 | ((255 & iCorrectSample2) << 8);
    }

    private int correctSample(int i, double d, double d2) {
        return (int) Math.round(255.0d * Math.pow(i / 255.0d, d / d2));
    }
}
