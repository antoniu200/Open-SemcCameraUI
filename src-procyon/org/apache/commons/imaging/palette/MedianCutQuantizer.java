// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.palette;

import org.apache.commons.imaging.ImageWriteException;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import org.apache.commons.imaging.util.Debug;
import java.util.HashMap;
import java.util.Map;
import java.awt.image.BufferedImage;

public class MedianCutQuantizer
{
    private final boolean ignoreAlpha;
    
    public MedianCutQuantizer(final boolean ignoreAlpha) {
        this.ignoreAlpha = ignoreAlpha;
    }
    
    private Map<Integer, ColorCount> groupColors1(final BufferedImage bufferedImage, final int n, final int n2) {
        final HashMap hashMap = new HashMap();
        final int width = bufferedImage.getWidth();
        final int height = bufferedImage.getHeight();
        final int[] rgbArray = new int[width];
        for (int i = 0; i < height; ++i) {
            bufferedImage.getRGB(0, i, width, 1, rgbArray, 0, width);
            for (int n3 : rgbArray) {
                if (this.ignoreAlpha) {
                    n3 &= 0xFFFFFF;
                }
                final int n4 = n3 & n2;
                ColorCount colorCount = (ColorCount)hashMap.get(n4);
                if (colorCount == null) {
                    colorCount = new ColorCount(n4);
                    hashMap.put(n4, colorCount);
                    if (hashMap.keySet().size() > n) {
                        return null;
                    }
                }
                ++colorCount.count;
            }
        }
        return hashMap;
    }
    
    public Map<Integer, ColorCount> groupColors(final BufferedImage bufferedImage, int i) {
        int n;
        int n2;
        StringBuilder sb;
        Map<Integer, ColorCount> groupColors1;
        for (i = 0; i < 8; ++i) {
            n = (0xFF & 255 << i);
            n2 = (n << 24 | (n << 8 | n | n << 16));
            sb = new StringBuilder();
            sb.append("mask(");
            sb.append(i);
            sb.append("): ");
            sb.append(n2);
            sb.append(" (");
            sb.append(Integer.toHexString(n2));
            sb.append(")");
            Debug.debug(sb.toString());
            groupColors1 = this.groupColors1(bufferedImage, Integer.MAX_VALUE, n2);
            if (groupColors1 != null) {
                return groupColors1;
            }
        }
        throw new Error("");
    }
    
    public Palette process(final BufferedImage bufferedImage, int i, final MedianCut medianCut, final boolean b) throws ImageWriteException {
        final Map<Integer, ColorCount> groupColors = this.groupColors(bufferedImage, i);
        final int size = groupColors.keySet().size();
        final int n = 0;
        final int n2 = 0;
        if (size <= i) {
            if (b) {
                final StringBuilder sb = new StringBuilder();
                sb.append("lossless palette: ");
                sb.append(size);
                Debug.debug(sb.toString());
            }
            final int[] array = new int[size];
            ArrayList list;
            for (list = new ArrayList(groupColors.values()), i = n2; i < list.size(); ++i) {
                array[i] = ((ColorCount)list.get(i)).argb;
                if (this.ignoreAlpha) {
                    array[i] |= 0xFF000000;
                }
            }
            return new SimplePalette(array);
        }
        if (b) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("discrete colors: ");
            sb2.append(size);
            Debug.debug(sb2.toString());
        }
        final ArrayList list2 = new ArrayList();
        final ColorGroup colorGroup = new ColorGroup(new ArrayList<ColorCount>(groupColors.values()), this.ignoreAlpha);
        list2.add(colorGroup);
        while (list2.size() < i && medianCut.performNextMedianCut(list2, this.ignoreAlpha)) {}
        final int size2 = list2.size();
        if (b) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("palette size: ");
            sb3.append(size2);
            Debug.debug(sb3.toString());
        }
        final int[] array2 = new int[size2];
        ColorGroup obj;
        StringBuilder sb4;
        for (i = n; i < list2.size(); ++i) {
            obj = (ColorGroup)list2.get(i);
            array2[i] = obj.getMedianValue();
            obj.paletteIndex = i;
            if (obj.colorCounts.size() < 1) {
                sb4 = new StringBuilder();
                sb4.append("empty color_group: ");
                sb4.append(obj);
                throw new ImageWriteException(sb4.toString());
            }
        }
        if (size2 > size) {
            throw new ImageWriteException("palette_size > discrete_colors");
        }
        return new MedianCutPalette(colorGroup, array2);
    }
}
