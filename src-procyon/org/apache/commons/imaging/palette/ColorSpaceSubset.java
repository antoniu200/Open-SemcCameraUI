// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.palette;

import java.io.Serializable;
import java.util.Comparator;
import java.io.PrintStream;

class ColorSpaceSubset
{
    public static final RgbComparator RGB_COMPARATOR;
    private int index;
    final int[] maxs;
    final int[] mins;
    final int precision;
    final int precisionMask;
    int rgb;
    final int total;
    
    static {
        RGB_COMPARATOR = new RgbComparator();
    }
    
    ColorSpaceSubset(int i, final int precision) {
        this.total = i;
        this.precision = precision;
        this.precisionMask = (1 << precision) - 1;
        this.mins = new int[3];
        this.maxs = new int[3];
        for (i = 0; i < 3; ++i) {
            this.mins[i] = 0;
            this.maxs[i] = this.precisionMask;
        }
        this.rgb = -1;
    }
    
    ColorSpaceSubset(final int total, final int precision, final int[] mins, final int[] maxs) {
        this.total = total;
        this.precision = precision;
        this.mins = mins;
        this.maxs = maxs;
        this.precisionMask = (1 << precision) - 1;
        this.rgb = -1;
    }
    
    public final boolean contains(int n, final int n2, final int n3) {
        n >>= 8 - this.precision;
        if (this.mins[0] > n) {
            return false;
        }
        if (this.maxs[0] < n) {
            return false;
        }
        n = n2 >> 8 - this.precision;
        if (this.mins[1] > n) {
            return false;
        }
        if (this.maxs[1] < n) {
            return false;
        }
        n = n3 >> 8 - this.precision;
        return this.mins[2] <= n && this.maxs[2] >= n;
    }
    
    public void dump(final String str) {
        final int i = this.maxs[0] - this.mins[0] + 1;
        final int j = this.maxs[1] - this.mins[1] + 1;
        final int k = this.maxs[2] - this.mins[2] + 1;
        final PrintStream out = System.out;
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(": [");
        sb.append(Integer.toHexString(this.rgb));
        sb.append("] total : ");
        sb.append(this.total);
        out.println(sb.toString());
        final PrintStream out2 = System.out;
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("\trgb: ");
        sb2.append(Integer.toHexString(this.rgb));
        sb2.append(", ");
        sb2.append("red: ");
        sb2.append(Integer.toHexString(this.mins[0] << 8 - this.precision));
        sb2.append(", ");
        sb2.append(Integer.toHexString(this.maxs[0] << 8 - this.precision));
        sb2.append(", ");
        sb2.append("green: ");
        sb2.append(Integer.toHexString(this.mins[1] << 8 - this.precision));
        sb2.append(", ");
        sb2.append(Integer.toHexString(this.maxs[1] << 8 - this.precision));
        sb2.append(", ");
        sb2.append("blue: ");
        sb2.append(Integer.toHexString(this.mins[2] << 8 - this.precision));
        sb2.append(", ");
        sb2.append(Integer.toHexString(this.maxs[2] << 8 - this.precision));
        out2.println(sb2.toString());
        final PrintStream out3 = System.out;
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("\tred: ");
        sb3.append(this.mins[0]);
        sb3.append(", ");
        sb3.append(this.maxs[0]);
        sb3.append(", ");
        sb3.append("green: ");
        sb3.append(this.mins[1]);
        sb3.append(", ");
        sb3.append(this.maxs[1]);
        sb3.append(", ");
        sb3.append("blue: ");
        sb3.append(this.mins[2]);
        sb3.append(", ");
        sb3.append(this.maxs[2]);
        out3.println(sb3.toString());
        final PrintStream out4 = System.out;
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("\trdiff: ");
        sb4.append(i);
        sb4.append(", ");
        sb4.append("gdiff: ");
        sb4.append(j);
        sb4.append(", ");
        sb4.append("bdiff: ");
        sb4.append(k);
        sb4.append(", ");
        sb4.append("colorArea: ");
        sb4.append(i * j * k);
        out4.println(sb4.toString());
    }
    
    public void dumpJustRGB(final String s) {
        final PrintStream out = System.out;
        final StringBuilder sb = new StringBuilder();
        sb.append("\trgb: ");
        sb.append(Integer.toHexString(this.rgb));
        sb.append(", ");
        sb.append("red: ");
        sb.append(Integer.toHexString(this.mins[0] << 8 - this.precision));
        sb.append(", ");
        sb.append(Integer.toHexString(this.maxs[0] << 8 - this.precision));
        sb.append(", ");
        sb.append("green: ");
        sb.append(Integer.toHexString(this.mins[1] << 8 - this.precision));
        sb.append(", ");
        sb.append(Integer.toHexString(this.maxs[1] << 8 - this.precision));
        sb.append(", ");
        sb.append("blue: ");
        sb.append(Integer.toHexString(this.mins[2] << 8 - this.precision));
        sb.append(", ");
        sb.append(Integer.toHexString(this.maxs[2] << 8 - this.precision));
        out.println(sb.toString());
    }
    
    public int getArea() {
        return (this.maxs[0] - this.mins[0] + 1) * (this.maxs[1] - this.mins[1] + 1) * (this.maxs[2] - this.mins[2] + 1);
    }
    
    public final int getIndex() {
        return this.index;
    }
    
    public void setAverageRGB(final int[] array) {
        int i = this.mins[0];
        long n = 0L;
        long n2 = 0L;
        long n3 = 0L;
        while (i <= this.maxs[0]) {
            for (int j = this.mins[1]; j <= this.maxs[1]; ++j) {
                for (int k = this.mins[2]; k <= this.maxs[2]; ++k) {
                    final int n4 = array[k << this.precision * 2 | j << this.precision * 1 | i << this.precision * 0];
                    n += (i << 8 - this.precision) * n4;
                    n2 += n4 * (j << 8 - this.precision);
                    n3 += n4 * (k << 8 - this.precision);
                }
            }
            ++i;
        }
        this.rgb = (int)((n3 / this.total & 0xFFL) << 0 | ((n / this.total & 0xFFL) << 16 | (n2 / this.total & 0xFFL) << 8));
    }
    
    public final void setIndex(final int index) {
        this.index = index;
    }
    
    public static class RgbComparator implements Comparator<ColorSpaceSubset>, Serializable
    {
        private static final long serialVersionUID = 509214838111679029L;
        
        @Override
        public int compare(final ColorSpaceSubset colorSpaceSubset, final ColorSpaceSubset colorSpaceSubset2) {
            return colorSpaceSubset.rgb - colorSpaceSubset2.rgb;
        }
    }
}
