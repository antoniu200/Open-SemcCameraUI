// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.palette;

import org.apache.commons.imaging.ImageWriteException;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;

public class LongestAxisMedianCut implements MedianCut
{
    private static final Comparator<ColorGroup> COMPARATOR;
    
    static {
        COMPARATOR = new Comparator<ColorGroup>() {
            @Override
            public int compare(final ColorGroup colorGroup, final ColorGroup colorGroup2) {
                if (colorGroup.maxDiff == colorGroup2.maxDiff) {
                    return colorGroup2.diffTotal - colorGroup.diffTotal;
                }
                return colorGroup2.maxDiff - colorGroup.maxDiff;
            }
        };
    }
    
    private void doCut(final ColorGroup colorGroup, final ColorComponent colorComponent, final List<ColorGroup> list, final boolean b) throws ImageWriteException {
        Collections.sort(colorGroup.colorCounts, new Comparator<ColorCount>(this, colorComponent) {
            final LongestAxisMedianCut this$0;
            final ColorComponent val$mode;
            
            @Override
            public int compare(final ColorCount colorCount, final ColorCount colorCount2) {
                switch (LongestAxisMedianCut$3.$SwitchMap$org$apache$commons$imaging$palette$ColorComponent[this.val$mode.ordinal()]) {
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
        });
        final int n = (int)Math.round(colorGroup.totalPoints / 2.0);
        int n2 = 0;
        int n3 = 0;
        int n5;
        int n4;
        while (true) {
            n4 = (n5 = n3);
            if (n2 >= colorGroup.colorCounts.size()) {
                break;
            }
            final int n6 = n4 + colorGroup.colorCounts.get(n2).count;
            if ((n5 = n6) >= n) {
                break;
            }
            ++n2;
            n3 = n6;
        }
        final int n7 = n5;
        int n8;
        if (n2 == colorGroup.colorCounts.size() - 1) {
            n8 = n2 - 1;
        }
        else if ((n8 = n2) > 0) {
            final int abs = Math.abs(n7 - n);
            n8 = n2;
            if (Math.abs(n - n4) < abs) {
                n8 = n2 - 1;
            }
        }
        list.remove(colorGroup);
        final List<ColorCount> colorCounts = colorGroup.colorCounts;
        final int n9 = n8 + 1;
        final ArrayList c = new ArrayList<ColorCount>(colorCounts.subList(0, n9));
        final ArrayList c2 = new ArrayList<ColorCount>(colorGroup.colorCounts.subList(n9, colorGroup.colorCounts.size()));
        final ColorGroup colorGroup2 = new ColorGroup(new ArrayList<ColorCount>((Collection<? extends ColorCount>)c), b);
        list.add(colorGroup2);
        final ColorGroup colorGroup3 = new ColorGroup(new ArrayList<ColorCount>((Collection<? extends ColorCount>)c2), b);
        list.add(colorGroup3);
        final ColorCount colorCount = colorGroup.colorCounts.get(n8);
        int n10 = 0;
        switch (LongestAxisMedianCut$3.$SwitchMap$org$apache$commons$imaging$palette$ColorComponent[colorComponent.ordinal()]) {
            default: {
                throw new Error("Bad mode.");
            }
            case 4: {
                n10 = colorCount.blue;
                break;
            }
            case 3: {
                n10 = colorCount.green;
                break;
            }
            case 2: {
                n10 = colorCount.red;
                break;
            }
            case 1: {
                n10 = colorCount.alpha;
                break;
            }
        }
        colorGroup.cut = new ColorGroupCut(colorGroup2, colorGroup3, colorComponent, n10);
    }
    
    @Override
    public boolean performNextMedianCut(final List<ColorGroup> list, final boolean b) throws ImageWriteException {
        Collections.sort((List<Object>)list, (Comparator<? super Object>)LongestAxisMedianCut.COMPARATOR);
        final ColorGroup colorGroup = list.get(0);
        if (colorGroup.maxDiff == 0) {
            return false;
        }
        if (!b && colorGroup.alphaDiff > colorGroup.redDiff && colorGroup.alphaDiff > colorGroup.greenDiff && colorGroup.alphaDiff > colorGroup.blueDiff) {
            this.doCut(colorGroup, ColorComponent.ALPHA, list, b);
        }
        else if (colorGroup.redDiff > colorGroup.greenDiff && colorGroup.redDiff > colorGroup.blueDiff) {
            this.doCut(colorGroup, ColorComponent.RED, list, b);
        }
        else if (colorGroup.greenDiff > colorGroup.blueDiff) {
            this.doCut(colorGroup, ColorComponent.GREEN, list, b);
        }
        else {
            this.doCut(colorGroup, ColorComponent.BLUE, list, b);
        }
        return true;
    }
}
