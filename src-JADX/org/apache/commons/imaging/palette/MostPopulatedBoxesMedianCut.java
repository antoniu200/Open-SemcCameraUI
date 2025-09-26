package org.apache.commons.imaging.palette;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.imaging.ImageWriteException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class MostPopulatedBoxesMedianCut implements MedianCut {
    @Override // org.apache.commons.imaging.palette.MedianCut
    public boolean performNextMedianCut(List<ColorGroup> list, boolean z) throws ImageWriteException {
        int i;
        int i2;
        ColorComponent[] colorComponentArr;
        int i3;
        int i4 = 0;
        ColorGroup colorGroup = null;
        int i5 = 0;
        for (ColorGroup colorGroup2 : list) {
            if (colorGroup2.maxDiff > 0 && colorGroup2.totalPoints > i5) {
                i5 = colorGroup2.totalPoints;
                colorGroup = colorGroup2;
            }
        }
        if (colorGroup == null) {
            return false;
        }
        ColorComponent[] colorComponentArrValues = ColorComponent.values();
        int length = colorComponentArrValues.length;
        int i6 = -1;
        double d = Double.MAX_VALUE;
        ColorComponent colorComponent = null;
        int i7 = 0;
        while (i7 < length) {
            ColorComponent colorComponent2 = colorComponentArrValues[i7];
            if (z && colorComponent2 == ColorComponent.ALPHA) {
                colorComponentArr = colorComponentArrValues;
            } else {
                Collections.sort(colorGroup.colorCounts, new ColorComparer(colorComponent2));
                int iRound = (int) Math.round(colorGroup.totalPoints / 2.0d);
                int i8 = i4;
                int i9 = i8;
                while (true) {
                    i2 = i9;
                    if (i8 >= colorGroup.colorCounts.size()) {
                        break;
                    }
                    int i10 = colorGroup.colorCounts.get(i8).count + i9;
                    if (i10 >= iRound) {
                        i9 = i10;
                        break;
                    }
                    i8++;
                    i9 = i10;
                }
                if (i8 == colorGroup.colorCounts.size() - 1) {
                    i8--;
                } else if (i8 > 0) {
                    if (Math.abs(iRound - i2) < Math.abs(i9 - iRound)) {
                        i8--;
                    }
                }
                int i11 = i8 + 1;
                ArrayList arrayList = new ArrayList(colorGroup.colorCounts.subList(0, i11));
                colorComponentArr = colorComponentArrValues;
                ArrayList arrayList2 = new ArrayList(colorGroup.colorCounts.subList(i11, colorGroup.colorCounts.size()));
                if (!arrayList.isEmpty() && !arrayList2.isEmpty()) {
                    ColorGroup colorGroup3 = new ColorGroup(arrayList, z);
                    ColorGroup colorGroup4 = new ColorGroup(arrayList2, z);
                    i3 = length;
                    double dAbs = Math.abs(colorGroup3.totalPoints - colorGroup4.totalPoints) / Math.max(colorGroup3.totalPoints, colorGroup4.totalPoints);
                    if (dAbs < d) {
                        d = dAbs;
                        i6 = i8;
                        colorComponent = colorComponent2;
                    }
                }
                i7++;
                colorComponentArrValues = colorComponentArr;
                length = i3;
                i4 = 0;
            }
            i3 = length;
            i7++;
            colorComponentArrValues = colorComponentArr;
            length = i3;
            i4 = 0;
        }
        if (colorComponent == null) {
            return false;
        }
        Collections.sort(colorGroup.colorCounts, new ColorComparer(colorComponent));
        int i12 = i6 + 1;
        ArrayList arrayList3 = new ArrayList(colorGroup.colorCounts.subList(0, i12));
        ArrayList arrayList4 = new ArrayList(colorGroup.colorCounts.subList(i12, colorGroup.colorCounts.size()));
        ColorGroup colorGroup5 = new ColorGroup(arrayList3, z);
        ColorGroup colorGroup6 = new ColorGroup(arrayList4, z);
        list.remove(colorGroup);
        list.add(colorGroup5);
        list.add(colorGroup6);
        ColorCount colorCount = colorGroup.colorCounts.get(i6);
        switch (colorComponent) {
            case ALPHA:
                i = colorCount.alpha;
                break;
            case RED:
                i = colorCount.red;
                break;
            case GREEN:
                i = colorCount.green;
                break;
            case BLUE:
                i = colorCount.blue;
                break;
            default:
                throw new Error("Bad mode.");
        }
        colorGroup.cut = new ColorGroupCut(colorGroup5, colorGroup6, colorComponent, i);
        return true;
    }

    private static class ColorComparer implements Comparator<ColorCount>, Serializable {
        private static final long serialVersionUID = 1;
        private final ColorComponent colorComponent;

        public ColorComparer(ColorComponent colorComponent) {
            this.colorComponent = colorComponent;
        }

        @Override // java.util.Comparator
        public int compare(ColorCount colorCount, ColorCount colorCount2) {
            switch (this.colorComponent) {
                case ALPHA:
                    return colorCount.alpha - colorCount2.alpha;
                case RED:
                    return colorCount.red - colorCount2.red;
                case GREEN:
                    return colorCount.green - colorCount2.green;
                case BLUE:
                    return colorCount.blue - colorCount2.blue;
                default:
                    return 0;
            }
        }
    }
}
