// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.palette;

import org.apache.commons.imaging.ImageWriteException;
import java.util.Comparator;
import java.util.Collections;
import java.util.HashSet;
import java.util.Arrays;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class PaletteFactory
{
    public static final int COMPONENTS = 3;
    private static final boolean DEBUG = false;
    
    private List<ColorSpaceSubset> divide(final List<ColorSpaceSubset> list, final int n, final int[] array, final int n2) {
        final ArrayList list2 = new ArrayList();
        do {
            int n3 = -1;
            ColorSpaceSubset colorSpaceSubset = null;
            for (final ColorSpaceSubset colorSpaceSubset2 : list) {
                if (list2.contains(colorSpaceSubset2)) {
                    continue;
                }
                final int total = colorSpaceSubset2.total;
                if (colorSpaceSubset != null) {
                    if (total <= n3) {
                        continue;
                    }
                }
                colorSpaceSubset = colorSpaceSubset2;
                n3 = total;
            }
            if (colorSpaceSubset == null) {
                return list;
            }
            final DivisionCandidate divideSubset2 = this.divideSubset2(array, colorSpaceSubset, n2);
            if (divideSubset2 != null) {
                list.remove(colorSpaceSubset);
                list.add(divideSubset2.dst_a);
                list.add(divideSubset2.dst_b);
            }
            else {
                list2.add(colorSpaceSubset);
            }
        } while (list.size() != n);
        return list;
    }
    
    private List<DivisionCandidate> divideSubset2(final int[] array, final ColorSpaceSubset colorSpaceSubset, final int n, final int n2) {
        final int total = colorSpaceSubset.total;
        final int[] array2 = new int[colorSpaceSubset.mins.length];
        final int[] mins = colorSpaceSubset.mins;
        final int length = colorSpaceSubset.mins.length;
        int n3 = 0;
        System.arraycopy(mins, 0, array2, 0, length);
        final int[] array3 = new int[colorSpaceSubset.maxs.length];
        System.arraycopy(colorSpaceSubset.maxs, 0, array3, 0, colorSpaceSubset.maxs.length);
        int i = colorSpaceSubset.mins[n];
        int frequencyTotal = 0;
        while (i != colorSpaceSubset.maxs[n] + 1) {
            array3[n] = (array2[n] = i);
            frequencyTotal = this.getFrequencyTotal(array, array2, array3, n2);
            n3 += frequencyTotal;
            if (n3 >= total / 2) {
                break;
            }
            ++i;
        }
        final DivisionCandidate finishDivision = this.finishDivision(colorSpaceSubset, n, n2, n3, i);
        final DivisionCandidate finishDivision2 = this.finishDivision(colorSpaceSubset, n, n2, n3 - frequencyTotal, i - 1);
        final ArrayList list = new ArrayList();
        if (finishDivision != null) {
            list.add(finishDivision);
        }
        if (finishDivision2 != null) {
            list.add(finishDivision2);
        }
        return list;
    }
    
    private DivisionCandidate divideSubset2(final int[] array, final ColorSpaceSubset colorSpaceSubset, int total) {
        final ArrayList list = new ArrayList();
        list.addAll(this.divideSubset2(array, colorSpaceSubset, 0, total));
        list.addAll(this.divideSubset2(array, colorSpaceSubset, 1, total));
        list.addAll(this.divideSubset2(array, colorSpaceSubset, 2, total));
        final Iterator iterator = list.iterator();
        DivisionCandidate divisionCandidate = null;
        double n = Double.MAX_VALUE;
        while (iterator.hasNext()) {
            final DivisionCandidate divisionCandidate2 = (DivisionCandidate)iterator.next();
            final ColorSpaceSubset access$000 = divisionCandidate2.dst_a;
            final ColorSpaceSubset access$2 = divisionCandidate2.dst_b;
            final int total2 = access$000.total;
            total = access$2.total;
            final double n2 = Math.abs(total2 - total) / (double)Math.max(total2, total);
            if (divisionCandidate != null) {
                if (n2 >= n) {
                    continue;
                }
            }
            divisionCandidate = divisionCandidate2;
            n = n2;
        }
        return divisionCandidate;
    }
    
    private DivisionCandidate finishDivision(final ColorSpaceSubset colorSpaceSubset, final int n, final int n2, final int n3, final int n4) {
        final int total = colorSpaceSubset.total;
        if (n4 < colorSpaceSubset.mins[n] || n4 >= colorSpaceSubset.maxs[n]) {
            return null;
        }
        if (n3 < 1 || n3 >= total) {
            return null;
        }
        final int n5 = total - n3;
        if (n5 >= 1 && n5 < total) {
            final int[] array = new int[colorSpaceSubset.mins.length];
            System.arraycopy(colorSpaceSubset.mins, 0, array, 0, colorSpaceSubset.mins.length);
            final int[] array2 = new int[colorSpaceSubset.maxs.length];
            System.arraycopy(colorSpaceSubset.maxs, 0, array2, 0, colorSpaceSubset.maxs.length);
            array[n] = (array2[n] = n4) + 1;
            return new DivisionCandidate(new ColorSpaceSubset(n3, n2, colorSpaceSubset.mins, array2), new ColorSpaceSubset(n5, n2, array, colorSpaceSubset.maxs));
        }
        return null;
    }
    
    private int getFrequencyTotal(final int[] array, final int[] array2, final int[] array3, final int n) {
        int i = array2[2];
        int n2 = 0;
        while (i <= array3[2]) {
            for (int j = array2[1]; j <= array3[1]; ++j) {
                for (int k = array2[0]; k <= array3[0]; ++k) {
                    n2 += array[i << 2 * n | j << 1 * n | k];
                }
            }
            ++i;
        }
        return n2;
    }
    
    private int pixelToQuantizationTableIndex(int n, final int n2) {
        int n3 = 0;
        int n4 = 0;
        while (true) {
            final int n5 = n;
            if (n3 >= 3) {
                break;
            }
            n = n5 >> 8;
            n4 = (n4 << n2 | ((n5 & 0xFF) >> 8 - n2 & (1 << n2) - 1));
            ++n3;
        }
        return n4;
    }
    
    public int countTransparentColors(final BufferedImage bufferedImage) {
        if (!bufferedImage.getColorModel().hasAlpha()) {
            return 0;
        }
        final int width = bufferedImage.getWidth();
        final int height = bufferedImage.getHeight();
        int n = -1;
        for (int i = 0; i < height; ++i) {
            int n2;
            for (int j = 0; j < width; ++j, n = n2) {
                final int rgb = bufferedImage.getRGB(j, i);
                n2 = n;
                if ((rgb >> 24 & 0xFF) < 255) {
                    if (n < 0) {
                        n2 = rgb;
                    }
                    else if (rgb != (n2 = n)) {
                        return 2;
                    }
                }
            }
        }
        if (n < 0) {
            return 0;
        }
        return 1;
    }
    
    public int countTrasparentColors(final int[] array) {
        final int length = array.length;
        int n = -1;
        int n3;
        for (int i = 0; i < length; ++i, n = n3) {
            final int n2 = array[i];
            n3 = n;
            if ((n2 >> 24 & 0xFF) < 255) {
                if (n < 0) {
                    n3 = n2;
                }
                else if (n2 != (n3 = n)) {
                    return 2;
                }
            }
        }
        if (n < 0) {
            return 0;
        }
        return 1;
    }
    
    public boolean hasTransparency(final BufferedImage bufferedImage) {
        return this.hasTransparency(bufferedImage, 255);
    }
    
    public boolean hasTransparency(final BufferedImage bufferedImage, final int n) {
        final int width = bufferedImage.getWidth();
        final int height = bufferedImage.getHeight();
        if (!bufferedImage.getColorModel().hasAlpha()) {
            return false;
        }
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if ((bufferedImage.getRGB(j, i) >> 24 & 0xFF) < n) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean isGrayscale(final BufferedImage bufferedImage) {
        final int width = bufferedImage.getWidth();
        final int height = bufferedImage.getHeight();
        if (6 == bufferedImage.getColorModel().getColorSpace().getType()) {
            return true;
        }
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                final int rgb = bufferedImage.getRGB(j, i);
                final int n = rgb >> 16 & 0xFF;
                if (n != (rgb >> 8 & 0xFF) || n != (rgb >> 0 & 0xFF)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public Palette makeExactRgbPaletteFancy(final BufferedImage bufferedImage) {
        final byte[] array = new byte[2097152];
        final int width = bufferedImage.getWidth();
        for (int height = bufferedImage.getHeight(), i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                final int rgb = bufferedImage.getRGB(j, i);
                final int n = 0x1FFFFF & rgb;
                array[n] |= (byte)(1 << (rgb >> 21 & 0x7));
            }
        }
        final int length = array.length;
        int k = 0;
        int n2 = 0;
        while (k < length) {
            n2 += Integer.bitCount(0xFF & array[k]);
            ++k;
        }
        final int[] a = new int[n2];
        int l = 0;
        int n3 = 0;
        while (l < array.length) {
            final byte b = array[l];
            int n4 = 128;
            int n5 = 0;
            while (true) {
                final int n6 = n4;
                if (n5 >= 8) {
                    break;
                }
                n4 = n6 >>> 1;
                int n7 = n3;
                if ((b & 0xFF & n6) > 0) {
                    a[n3] = (7 - n5 << 21 | l);
                    n7 = n3 + 1;
                }
                ++n5;
                n3 = n7;
            }
            ++l;
        }
        Arrays.sort(a);
        return new SimplePalette(a);
    }
    
    public SimplePalette makeExactRgbPaletteSimple(final BufferedImage bufferedImage, int n) {
        final HashSet set = new HashSet();
        final int width = bufferedImage.getWidth();
        final int height = bufferedImage.getHeight();
        final int n2 = 0;
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if (set.add(bufferedImage.getRGB(j, i) & 0xFFFFFF) && set.size() > n) {
                    return null;
                }
            }
        }
        final int[] a = new int[set.size()];
        final Iterator iterator = set.iterator();
        n = n2;
        while (iterator.hasNext()) {
            a[n] = (int)iterator.next();
            ++n;
        }
        Arrays.sort(a);
        return new SimplePalette(a);
    }
    
    public Palette makeQuantizedRgbPalette(final BufferedImage bufferedImage, int i) {
        final int[] averageRGB = new int[262144];
        final int width = bufferedImage.getWidth();
        final int height = bufferedImage.getHeight();
        final ArrayList list = new ArrayList();
        list.add(new ColorSpaceSubset(width * height, 6));
        final int n = 0;
        for (int j = 0; j < height; ++j) {
            for (int k = 0; k < width; ++k) {
                final int pixelToQuantizationTableIndex = this.pixelToQuantizationTableIndex(bufferedImage.getRGB(k, j), 6);
                ++averageRGB[pixelToQuantizationTableIndex];
            }
        }
        List<ColorSpaceSubset> divide;
        for (divide = this.divide(list, i, averageRGB, 6), i = n; i < divide.size(); ++i) {
            ((ColorSpaceSubset)divide.get(i)).setAverageRGB(averageRGB);
        }
        Collections.sort((List<Object>)divide, (Comparator<? super Object>)ColorSpaceSubset.RGB_COMPARATOR);
        return new QuantizedPalette(divide, 6);
    }
    
    public Palette makeQuantizedRgbaPalette(final BufferedImage bufferedImage, final boolean b, final int n) throws ImageWriteException {
        return new MedianCutQuantizer(b ^ true).process(bufferedImage, n, new LongestAxisMedianCut(), false);
    }
    
    private static class DivisionCandidate
    {
        private final ColorSpaceSubset dst_a;
        private final ColorSpaceSubset dst_b;
        
        public DivisionCandidate(final ColorSpaceSubset dst_a, final ColorSpaceSubset dst_b) {
            this.dst_a = dst_a;
            this.dst_b = dst_b;
        }
    }
}
