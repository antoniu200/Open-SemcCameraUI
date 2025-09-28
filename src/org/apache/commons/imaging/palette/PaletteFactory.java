package org.apache.commons.imaging.palette;

import android.support.v4.view.ViewCompat;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.imaging.ImageWriteException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class PaletteFactory {
    public static final int COMPONENTS = 3;
    private static final boolean DEBUG = false;

    private int pixelToQuantizationTableIndex(int i, int i2) {
        int i3 = (1 << i2) - 1;
        int i4 = 0;
        for (int i5 = 0; i5 < 3; i5++) {
            int i6 = i & 255;
            i >>= 8;
            i4 = (i4 << i2) | ((i6 >> (8 - i2)) & i3);
        }
        return i4;
    }

    public Palette makeExactRgbPaletteFancy(BufferedImage bufferedImage) {
        byte[] bArr = new byte[2097152];
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        for (int i = 0; i < height; i++) {
            for (int i2 = 0; i2 < width; i2++) {
                int rgb = bufferedImage.getRGB(i2, i);
                int i3 = 2097151 & rgb;
                bArr[i3] = (byte) ((1 << ((rgb >> 21) & 7)) | bArr[i3]);
            }
        }
        int iBitCount = 0;
        for (byte b : bArr) {
            iBitCount += Integer.bitCount(255 & b);
        }
        int[] iArr = new int[iBitCount];
        int i4 = 0;
        int i5 = 0;
        while (i4 < bArr.length) {
            int i6 = bArr[i4] & 255;
            int i7 = 128;
            int i8 = i5;
            for (int i9 = 0; i9 < 8; i9++) {
                int i10 = i6 & i7;
                i7 >>>= 1;
                if (i10 > 0) {
                    iArr[i8] = ((7 - i9) << 21) | i4;
                    i8++;
                }
            }
            i4++;
            i5 = i8;
        }
        Arrays.sort(iArr);
        return new SimplePalette(iArr);
    }

    private int getFrequencyTotal(int[] iArr, int[] iArr2, int[] iArr3, int i) {
        int i2 = 0;
        for (int i3 = iArr2[2]; i3 <= iArr3[2]; i3++) {
            int i4 = i3 << (2 * i);
            for (int i5 = iArr2[1]; i5 <= iArr3[1]; i5++) {
                int i6 = i5 << (1 * i);
                for (int i7 = iArr2[0]; i7 <= iArr3[0]; i7++) {
                    i2 += iArr[i4 | i6 | i7];
                }
            }
        }
        return i2;
    }

    private DivisionCandidate finishDivision(ColorSpaceSubset colorSpaceSubset, int i, int i2, int i3, int i4) {
        int i5;
        int i6 = colorSpaceSubset.total;
        if (i4 < colorSpaceSubset.mins[i] || i4 >= colorSpaceSubset.maxs[i] || i3 < 1 || i3 >= i6 || (i5 = i6 - i3) < 1 || i5 >= i6) {
            return null;
        }
        int[] iArr = new int[colorSpaceSubset.mins.length];
        System.arraycopy(colorSpaceSubset.mins, 0, iArr, 0, colorSpaceSubset.mins.length);
        int[] iArr2 = new int[colorSpaceSubset.maxs.length];
        System.arraycopy(colorSpaceSubset.maxs, 0, iArr2, 0, colorSpaceSubset.maxs.length);
        iArr2[i] = i4;
        iArr[i] = i4 + 1;
        return new DivisionCandidate(new ColorSpaceSubset(i3, i2, colorSpaceSubset.mins, iArr2), new ColorSpaceSubset(i5, i2, iArr, colorSpaceSubset.maxs));
    }

    private List<DivisionCandidate> divideSubset2(int[] iArr, ColorSpaceSubset colorSpaceSubset, int i, int i2) {
        PaletteFactory paletteFactory;
        int i3;
        int i4 = colorSpaceSubset.total;
        int[] iArr2 = new int[colorSpaceSubset.mins.length];
        int i5 = 0;
        System.arraycopy(colorSpaceSubset.mins, 0, iArr2, 0, colorSpaceSubset.mins.length);
        int[] iArr3 = new int[colorSpaceSubset.maxs.length];
        System.arraycopy(colorSpaceSubset.maxs, 0, iArr3, 0, colorSpaceSubset.maxs.length);
        int i6 = colorSpaceSubset.mins[i];
        int frequencyTotal = 0;
        while (true) {
            if (i6 == colorSpaceSubset.maxs[i] + 1) {
                paletteFactory = this;
                i3 = i2;
                break;
            }
            iArr2[i] = i6;
            iArr3[i] = i6;
            paletteFactory = this;
            i3 = i2;
            frequencyTotal = paletteFactory.getFrequencyTotal(iArr, iArr2, iArr3, i3);
            i5 += frequencyTotal;
            if (i5 >= i4 / 2) {
                break;
            }
            i6++;
        }
        PaletteFactory paletteFactory2 = paletteFactory;
        int i7 = i3;
        DivisionCandidate divisionCandidateFinishDivision = paletteFactory2.finishDivision(colorSpaceSubset, i, i7, i5, i6);
        DivisionCandidate divisionCandidateFinishDivision2 = paletteFactory2.finishDivision(colorSpaceSubset, i, i7, i5 - frequencyTotal, i6 - 1);
        ArrayList arrayList = new ArrayList();
        if (divisionCandidateFinishDivision != null) {
            arrayList.add(divisionCandidateFinishDivision);
        }
        if (divisionCandidateFinishDivision2 != null) {
            arrayList.add(divisionCandidateFinishDivision2);
        }
        return arrayList;
    }

    private DivisionCandidate divideSubset2(int[] iArr, ColorSpaceSubset colorSpaceSubset, int i) {
        ArrayList<DivisionCandidate> arrayList = new ArrayList();
        arrayList.addAll(divideSubset2(iArr, colorSpaceSubset, 0, i));
        arrayList.addAll(divideSubset2(iArr, colorSpaceSubset, 1, i));
        arrayList.addAll(divideSubset2(iArr, colorSpaceSubset, 2, i));
        DivisionCandidate divisionCandidate = null;
        double d = Double.MAX_VALUE;
        for (DivisionCandidate divisionCandidate2 : arrayList) {
            ColorSpaceSubset colorSpaceSubset2 = divisionCandidate2.dst_a;
            ColorSpaceSubset colorSpaceSubset3 = divisionCandidate2.dst_b;
            int i2 = colorSpaceSubset2.total;
            int i3 = colorSpaceSubset3.total;
            double dAbs = Math.abs(i2 - i3) / Math.max(i2, i3);
            if (divisionCandidate == null || dAbs < d) {
                divisionCandidate = divisionCandidate2;
                d = dAbs;
            }
        }
        return divisionCandidate;
    }

    private static class DivisionCandidate {
        private final ColorSpaceSubset dst_a;
        private final ColorSpaceSubset dst_b;

        public DivisionCandidate(ColorSpaceSubset colorSpaceSubset, ColorSpaceSubset colorSpaceSubset2) {
            this.dst_a = colorSpaceSubset;
            this.dst_b = colorSpaceSubset2;
        }
    }

    private List<ColorSpaceSubset> divide(List<ColorSpaceSubset> list, int i, int[] iArr, int i2) {
        ArrayList arrayList = new ArrayList();
        do {
            int i3 = -1;
            ColorSpaceSubset colorSpaceSubset = null;
            for (ColorSpaceSubset colorSpaceSubset2 : list) {
                if (!arrayList.contains(colorSpaceSubset2)) {
                    int i4 = colorSpaceSubset2.total;
                    if (colorSpaceSubset == null || i4 > i3) {
                        colorSpaceSubset = colorSpaceSubset2;
                        i3 = i4;
                    }
                }
            }
            if (colorSpaceSubset == null) {
                return list;
            }
            DivisionCandidate divisionCandidateDivideSubset2 = divideSubset2(iArr, colorSpaceSubset, i2);
            if (divisionCandidateDivideSubset2 != null) {
                list.remove(colorSpaceSubset);
                list.add(divisionCandidateDivideSubset2.dst_a);
                list.add(divisionCandidateDivideSubset2.dst_b);
            } else {
                arrayList.add(colorSpaceSubset);
            }
        } while (list.size() != i);
        return list;
    }

    public Palette makeQuantizedRgbPalette(BufferedImage bufferedImage, int i) {
        int[] iArr = new int[262144];
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ColorSpaceSubset(width * height, 6));
        for (int i2 = 0; i2 < height; i2++) {
            for (int i3 = 0; i3 < width; i3++) {
                int iPixelToQuantizationTableIndex = pixelToQuantizationTableIndex(bufferedImage.getRGB(i3, i2), 6);
                iArr[iPixelToQuantizationTableIndex] = iArr[iPixelToQuantizationTableIndex] + 1;
            }
        }
        List<ColorSpaceSubset> listDivide = divide(arrayList, i, iArr, 6);
        for (int i4 = 0; i4 < listDivide.size(); i4++) {
            listDivide.get(i4).setAverageRGB(iArr);
        }
        Collections.sort(listDivide, ColorSpaceSubset.RGB_COMPARATOR);
        return new QuantizedPalette(listDivide, 6);
    }

    public Palette makeQuantizedRgbaPalette(BufferedImage bufferedImage, boolean z, int i) throws ImageWriteException {
        return new MedianCutQuantizer(!z).process(bufferedImage, i, new LongestAxisMedianCut(), false);
    }

    public SimplePalette makeExactRgbPaletteSimple(BufferedImage bufferedImage, int i) {
        HashSet hashSet = new HashSet();
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int i2 = 0;
        for (int i3 = 0; i3 < height; i3++) {
            for (int i4 = 0; i4 < width; i4++) {
                if (hashSet.add(Integer.valueOf(bufferedImage.getRGB(i4, i3) & ViewCompat.MEASURED_SIZE_MASK)) && hashSet.size() > i) {
                    return null;
                }
            }
        }
        int[] iArr = new int[hashSet.size()];
        Iterator it = hashSet.iterator();
        while (it.hasNext()) {
            iArr[i2] = ((Integer) it.next()).intValue();
            i2++;
        }
        Arrays.sort(iArr);
        return new SimplePalette(iArr);
    }

    public boolean isGrayscale(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        if (6 == bufferedImage.getColorModel().getColorSpace().getType()) {
            return true;
        }
        for (int i = 0; i < height; i++) {
            for (int i2 = 0; i2 < width; i2++) {
                int rgb = bufferedImage.getRGB(i2, i);
                int i3 = (rgb >> 16) & 255;
                int i4 = (rgb >> 8) & 255;
                int i5 = (rgb >> 0) & 255;
                if (i3 != i4 || i3 != i5) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean hasTransparency(BufferedImage bufferedImage) {
        return hasTransparency(bufferedImage, 255);
    }

    public boolean hasTransparency(BufferedImage bufferedImage, int i) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        if (!bufferedImage.getColorModel().hasAlpha()) {
            return false;
        }
        for (int i2 = 0; i2 < height; i2++) {
            for (int i3 = 0; i3 < width; i3++) {
                if (((bufferedImage.getRGB(i3, i2) >> 24) & 255) < i) {
                    return true;
                }
            }
        }
        return false;
    }

    public int countTrasparentColors(int[] iArr) {
        int i = -1;
        for (int i2 : iArr) {
            if (((i2 >> 24) & 255) < 255) {
                if (i < 0) {
                    i = i2;
                } else if (i2 != i) {
                    return 2;
                }
            }
        }
        return i < 0 ? 0 : 1;
    }

    public int countTransparentColors(BufferedImage bufferedImage) {
        if (!bufferedImage.getColorModel().hasAlpha()) {
            return 0;
        }
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int i = -1;
        int i2 = 0;
        while (i2 < height) {
            int i3 = i;
            for (int i4 = 0; i4 < width; i4++) {
                int rgb = bufferedImage.getRGB(i4, i2);
                if (((rgb >> 24) & 255) < 255) {
                    if (i3 < 0) {
                        i3 = rgb;
                    } else if (rgb != i3) {
                        return 2;
                    }
                }
            }
            i2++;
            i = i3;
        }
        return i < 0 ? 0 : 1;
    }
}
