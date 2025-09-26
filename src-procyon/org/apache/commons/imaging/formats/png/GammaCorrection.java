// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.png;

public class GammaCorrection
{
    private static final boolean DEBUG = false;
    private final int[] lookupTable;
    
    public GammaCorrection(final double n, final double n2) {
        this.lookupTable = new int[256];
        for (int i = 0; i < 256; ++i) {
            this.lookupTable[i] = this.correctSample(i, n, n2);
        }
    }
    
    private int correctSample(final int n, final double n2, final double n3) {
        return (int)Math.round(255.0 * Math.pow(n / 255.0, n2 / n3));
    }
    
    public int correctARGB(final int n) {
        return (this.correctSample(n >> 0 & 0xFF) & 0xFF) << 0 | ((0xFF & this.correctSample(n >> 16 & 0xFF)) << 16 | (0xFF000000 & n) | (0xFF & this.correctSample(n >> 8 & 0xFF)) << 8);
    }
    
    public int correctSample(final int n) {
        return this.lookupTable[n];
    }
}
