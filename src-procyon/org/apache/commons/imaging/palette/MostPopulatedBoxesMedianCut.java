// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.palette;

import java.io.Serializable;
import org.apache.commons.imaging.ImageWriteException;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.List;

public class MostPopulatedBoxesMedianCut implements MedianCut
{
    @Override
    public boolean performNextMedianCut(final List<ColorGroup> list, final boolean b) throws ImageWriteException {
        final Iterator iterator = list.iterator();
        ColorGroup colorGroup = null;
        int totalPoints = 0;
        while (iterator.hasNext()) {
            final ColorGroup colorGroup2 = (ColorGroup)iterator.next();
            if (colorGroup2.maxDiff > 0 && colorGroup2.totalPoints > totalPoints) {
                totalPoints = colorGroup2.totalPoints;
                colorGroup = colorGroup2;
            }
        }
        if (colorGroup == null) {
            return false;
        }
        final ColorComponent[] values = ColorComponent.values();
        final int length = values.length;
        int n = -1;
        double n2 = Double.MAX_VALUE;
        ColorComponent colorComponent = null;
        double n11;
        for (int i = 0; i < length; ++i, n2 = n11) {
            final ColorComponent colorComponent2 = values[i];
            if (!b || colorComponent2 != ColorComponent.ALPHA) {
                Collections.sort(colorGroup.colorCounts, new ColorComparer(colorComponent2));
                final int n3 = (int)Math.round(colorGroup.totalPoints / 2.0);
                int n4 = 0;
                int n5 = 0;
                int n6;
                while (true) {
                    n6 = n5;
                    if (n4 >= colorGroup.colorCounts.size()) {
                        break;
                    }
                    n6 = colorGroup.colorCounts.get(n4).count + n5;
                    if (n6 >= n3) {
                        break;
                    }
                    ++n4;
                    n5 = n6;
                }
                final int n7 = n6;
                int n8;
                if (n4 == colorGroup.colorCounts.size() - 1) {
                    n8 = n4 - 1;
                }
                else if ((n8 = n4) > 0) {
                    final int abs = Math.abs(n7 - n3);
                    n8 = n4;
                    if (Math.abs(n3 - n5) < abs) {
                        n8 = n4 - 1;
                    }
                }
                final List<ColorCount> colorCounts = colorGroup.colorCounts;
                final int n9 = n8 + 1;
                final ArrayList list2 = new ArrayList(colorCounts.subList(0, n9));
                final ArrayList list3 = new ArrayList(colorGroup.colorCounts.subList(n9, colorGroup.colorCounts.size()));
                if (!list2.isEmpty()) {
                    if (!list3.isEmpty()) {
                        final ColorGroup colorGroup3 = new ColorGroup((List<ColorCount>)list2, b);
                        final ColorGroup colorGroup4 = new ColorGroup((List<ColorCount>)list3, b);
                        final double n10 = Math.abs(colorGroup3.totalPoints - colorGroup4.totalPoints) / (double)Math.max(colorGroup3.totalPoints, colorGroup4.totalPoints);
                        n11 = n2;
                        if (n10 < n2) {
                            n11 = n10;
                            colorComponent = colorComponent2;
                            n = n8;
                        }
                        continue;
                    }
                }
            }
            n11 = n2;
        }
        if (colorComponent == null) {
            return false;
        }
        Collections.sort(colorGroup.colorCounts, new ColorComparer(colorComponent));
        final List<ColorCount> colorCounts2 = colorGroup.colorCounts;
        final int n12 = n + 1;
        final ArrayList list4 = new ArrayList<ColorCount>(colorCounts2.subList(0, n12));
        final ArrayList list5 = new ArrayList<ColorCount>(colorGroup.colorCounts.subList(n12, colorGroup.colorCounts.size()));
        final ColorGroup colorGroup5 = new ColorGroup((List<ColorCount>)list4, b);
        final ColorGroup colorGroup6 = new ColorGroup((List<ColorCount>)list5, b);
        list.remove(colorGroup);
        list.add(colorGroup5);
        list.add(colorGroup6);
        final ColorCount colorCount = colorGroup.colorCounts.get(n);
        int n13 = 0;
        switch (MostPopulatedBoxesMedianCut$1.$SwitchMap$org$apache$commons$imaging$palette$ColorComponent[colorComponent.ordinal()]) {
            default: {
                throw new Error("Bad mode.");
            }
            case 4: {
                n13 = colorCount.blue;
                break;
            }
            case 3: {
                n13 = colorCount.green;
                break;
            }
            case 2: {
                n13 = colorCount.red;
                break;
            }
            case 1: {
                n13 = colorCount.alpha;
                break;
            }
        }
        colorGroup.cut = new ColorGroupCut(colorGroup5, colorGroup6, colorComponent, n13);
        return true;
    }
    
    private static class ColorComparer implements Comparator<ColorCount>, Serializable
    {
        private static final long serialVersionUID = 1L;
        private final ColorComponent colorComponent;
        
        public ColorComparer(final ColorComponent colorComponent) {
            this.colorComponent = colorComponent;
        }
        
        @Override
        public int compare(final ColorCount colorCount, final ColorCount colorCount2) {
            switch (MostPopulatedBoxesMedianCut$1.$SwitchMap$org$apache$commons$imaging$palette$ColorComponent[this.colorComponent.ordinal()]) {
                default: {
                    return 0;
                }
                case 4: {
                    return colorCount.blue - colorCount2.blue;
                }
                case 3: {
                    return colorCount.green - colorCount2.green;
                }
                case 2: {
                    return colorCount.red - colorCount2.red;
                }
                case 1: {
                    return colorCount.alpha - colorCount2.alpha;
                }
            }
        }
    }
}
