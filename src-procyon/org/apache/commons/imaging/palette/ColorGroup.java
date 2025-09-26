// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.palette;

import java.util.Iterator;
import org.apache.commons.imaging.ImageWriteException;
import java.util.List;

class ColorGroup
{
    public final int alphaDiff;
    public final int blueDiff;
    public final List<ColorCount> colorCounts;
    public ColorGroupCut cut;
    public final int diffTotal;
    public final int greenDiff;
    public final boolean ignoreAlpha;
    public int maxAlpha;
    public int maxBlue;
    public final int maxDiff;
    public int maxGreen;
    public int maxRed;
    public int minAlpha;
    public int minBlue;
    public int minGreen;
    public int minRed;
    public int paletteIndex;
    public final int redDiff;
    public final int totalPoints;
    
    public ColorGroup(final List<ColorCount> colorCounts, final boolean ignoreAlpha) throws ImageWriteException {
        this.paletteIndex = -1;
        this.minRed = Integer.MAX_VALUE;
        this.maxRed = Integer.MIN_VALUE;
        this.minGreen = Integer.MAX_VALUE;
        this.maxGreen = Integer.MIN_VALUE;
        this.minBlue = Integer.MAX_VALUE;
        this.maxBlue = Integer.MIN_VALUE;
        this.minAlpha = Integer.MAX_VALUE;
        this.maxAlpha = Integer.MIN_VALUE;
        this.colorCounts = colorCounts;
        this.ignoreAlpha = ignoreAlpha;
        if (colorCounts.size() < 1) {
            throw new ImageWriteException("empty color_group");
        }
        final Iterator iterator = colorCounts.iterator();
        final int n = 0;
        int totalPoints = 0;
        while (iterator.hasNext()) {
            final ColorCount colorCount = (ColorCount)iterator.next();
            totalPoints += colorCount.count;
            this.minAlpha = Math.min(this.minAlpha, colorCount.alpha);
            this.maxAlpha = Math.max(this.maxAlpha, colorCount.alpha);
            this.minRed = Math.min(this.minRed, colorCount.red);
            this.maxRed = Math.max(this.maxRed, colorCount.red);
            this.minGreen = Math.min(this.minGreen, colorCount.green);
            this.maxGreen = Math.max(this.maxGreen, colorCount.green);
            this.minBlue = Math.min(this.minBlue, colorCount.blue);
            this.maxBlue = Math.max(this.maxBlue, colorCount.blue);
        }
        this.totalPoints = totalPoints;
        this.alphaDiff = this.maxAlpha - this.minAlpha;
        this.redDiff = this.maxRed - this.minRed;
        this.greenDiff = this.maxGreen - this.minGreen;
        this.blueDiff = this.maxBlue - this.minBlue;
        int a;
        if (ignoreAlpha) {
            a = this.redDiff;
        }
        else {
            a = Math.max(this.alphaDiff, this.redDiff);
        }
        this.maxDiff = Math.max(a, Math.max(this.greenDiff, this.blueDiff));
        int alphaDiff;
        if (ignoreAlpha) {
            alphaDiff = n;
        }
        else {
            alphaDiff = this.alphaDiff;
        }
        this.diffTotal = alphaDiff + this.redDiff + this.greenDiff + this.blueDiff;
    }
    
    public boolean contains(int n) {
        final int n2 = n >> 24 & 0xFF;
        final int n3 = n >> 16 & 0xFF;
        final int n4 = n >> 8 & 0xFF;
        n = (n >> 0 & 0xFF);
        return (this.ignoreAlpha || (n2 >= this.minAlpha && n2 <= this.maxAlpha)) && (n3 >= this.minRed && n3 <= this.maxRed) && (n4 >= this.minGreen && n4 <= this.maxGreen) && (n >= this.minBlue && n <= this.maxBlue);
    }
    
    public int getMedianValue() {
        final Iterator<ColorCount> iterator = this.colorCounts.iterator();
        long n = 0L;
        long n2 = 0L;
        long n3 = 0L;
        long n5;
        long n4 = n5 = n3;
        while (iterator.hasNext()) {
            final ColorCount colorCount = iterator.next();
            n += colorCount.count;
            n2 += colorCount.count * colorCount.alpha;
            n3 += colorCount.count * colorCount.red;
            n4 += colorCount.count * colorCount.green;
            n5 += colorCount.count * colorCount.blue;
        }
        int n6;
        if (this.ignoreAlpha) {
            n6 = 255;
        }
        else {
            n6 = (int)Math.round(n2 / (double)n);
        }
        final double n7 = (double)n3;
        final double n8 = (double)n;
        return n6 << 24 | (int)Math.round(n7 / n8) << 16 | (int)Math.round(n4 / n8) << 8 | (int)Math.round(n5 / n8);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{ColorGroup. minRed: ");
        sb.append(Integer.toHexString(this.minRed));
        sb.append(", maxRed: ");
        sb.append(Integer.toHexString(this.maxRed));
        sb.append(", minGreen: ");
        sb.append(Integer.toHexString(this.minGreen));
        sb.append(", maxGreen: ");
        sb.append(Integer.toHexString(this.maxGreen));
        sb.append(", minBlue: ");
        sb.append(Integer.toHexString(this.minBlue));
        sb.append(", maxBlue: ");
        sb.append(Integer.toHexString(this.maxBlue));
        sb.append(", minAlpha: ");
        sb.append(Integer.toHexString(this.minAlpha));
        sb.append(", maxAlpha: ");
        sb.append(Integer.toHexString(this.maxAlpha));
        sb.append(", maxDiff: ");
        sb.append(Integer.toHexString(this.maxDiff));
        sb.append(", diffTotal: ");
        sb.append(this.diffTotal);
        sb.append("}");
        return sb.toString();
    }
}
