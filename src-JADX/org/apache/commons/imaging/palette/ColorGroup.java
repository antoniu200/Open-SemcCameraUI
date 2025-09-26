package org.apache.commons.imaging.palette;

import java.util.List;
import org.apache.commons.imaging.ImageWriteException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class ColorGroup {
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
    public int paletteIndex = -1;
    public final int redDiff;
    public final int totalPoints;

    public ColorGroup(List<ColorCount> list, boolean z) throws ImageWriteException {
        this.minRed = Integer.MAX_VALUE;
        this.maxRed = Integer.MIN_VALUE;
        this.minGreen = Integer.MAX_VALUE;
        this.maxGreen = Integer.MIN_VALUE;
        this.minBlue = Integer.MAX_VALUE;
        this.maxBlue = Integer.MIN_VALUE;
        this.minAlpha = Integer.MAX_VALUE;
        this.maxAlpha = Integer.MIN_VALUE;
        this.colorCounts = list;
        this.ignoreAlpha = z;
        if (list.size() < 1) {
            throw new ImageWriteException("empty color_group");
        }
        int i = 0;
        for (ColorCount colorCount : list) {
            i += colorCount.count;
            this.minAlpha = Math.min(this.minAlpha, colorCount.alpha);
            this.maxAlpha = Math.max(this.maxAlpha, colorCount.alpha);
            this.minRed = Math.min(this.minRed, colorCount.red);
            this.maxRed = Math.max(this.maxRed, colorCount.red);
            this.minGreen = Math.min(this.minGreen, colorCount.green);
            this.maxGreen = Math.max(this.maxGreen, colorCount.green);
            this.minBlue = Math.min(this.minBlue, colorCount.blue);
            this.maxBlue = Math.max(this.maxBlue, colorCount.blue);
        }
        this.totalPoints = i;
        this.alphaDiff = this.maxAlpha - this.minAlpha;
        this.redDiff = this.maxRed - this.minRed;
        this.greenDiff = this.maxGreen - this.minGreen;
        this.blueDiff = this.maxBlue - this.minBlue;
        this.maxDiff = Math.max(z ? this.redDiff : Math.max(this.alphaDiff, this.redDiff), Math.max(this.greenDiff, this.blueDiff));
        this.diffTotal = (z ? 0 : this.alphaDiff) + this.redDiff + this.greenDiff + this.blueDiff;
    }

    public boolean contains(int i) {
        int i2 = (i >> 24) & 255;
        int i3 = (i >> 16) & 255;
        int i4 = (i >> 8) & 255;
        int i5 = (i >> 0) & 255;
        return (this.ignoreAlpha || (i2 >= this.minAlpha && i2 <= this.maxAlpha)) && i3 >= this.minRed && i3 <= this.maxRed && i4 >= this.minGreen && i4 <= this.maxGreen && i5 >= this.minBlue && i5 <= this.maxBlue;
    }

    public int getMedianValue() {
        long j = 0;
        long j2 = 0;
        long j3 = 0;
        long j4 = 0;
        long j5 = 0;
        for (ColorCount colorCount : this.colorCounts) {
            j += colorCount.count;
            j2 += colorCount.count * colorCount.alpha;
            j3 += colorCount.count * colorCount.red;
            j4 += colorCount.count * colorCount.green;
            j5 += colorCount.count * colorCount.blue;
        }
        double d = j;
        return ((this.ignoreAlpha ? 255 : (int) Math.round(j2 / j)) << 24) | (((int) Math.round(j3 / d)) << 16) | (((int) Math.round(j4 / d)) << 8) | ((int) Math.round(j5 / d));
    }

    public String toString() {
        return "{ColorGroup. minRed: " + Integer.toHexString(this.minRed) + ", maxRed: " + Integer.toHexString(this.maxRed) + ", minGreen: " + Integer.toHexString(this.minGreen) + ", maxGreen: " + Integer.toHexString(this.maxGreen) + ", minBlue: " + Integer.toHexString(this.minBlue) + ", maxBlue: " + Integer.toHexString(this.maxBlue) + ", minAlpha: " + Integer.toHexString(this.minAlpha) + ", maxAlpha: " + Integer.toHexString(this.maxAlpha) + ", maxDiff: " + Integer.toHexString(this.maxDiff) + ", diffTotal: " + this.diffTotal + "}";
    }
}
