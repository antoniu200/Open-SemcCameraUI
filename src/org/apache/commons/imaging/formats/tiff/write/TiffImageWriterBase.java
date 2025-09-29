package org.apache.commons.imaging.formats.tiff.write;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.common.BinaryOutputStream;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.TiffConstants;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class TiffImageWriterBase {
    protected final ByteOrder byteOrder;

    public abstract void write(OutputStream outputStream, TiffOutputSet tiffOutputSet) throws ImageWriteException, IOException;

    public TiffImageWriterBase() {
        this.byteOrder = TiffConstants.DEFAULT_TIFF_BYTE_ORDER;
    }

    public TiffImageWriterBase(ByteOrder byteOrder) {
        this.byteOrder = byteOrder;
    }

    protected static int imageDataPaddingLength(int i) {
        return (4 - (i % 4)) % 4;
    }

    protected TiffOutputSummary validateDirectories(TiffOutputSet tiffOutputSet) throws ImageWriteException {
        List<TiffOutputDirectory> directories = tiffOutputSet.getDirectories();
        if (directories.isEmpty()) {
            throw new ImageWriteException("No directories.");
        }
        ArrayList arrayList = new ArrayList();
        HashMap map = new HashMap();
        TiffOutputDirectory tiffOutputDirectory = null;
        TiffOutputDirectory tiffOutputDirectory2 = null;
        TiffOutputDirectory tiffOutputDirectoryAddExifDirectory = null;
        TiffOutputField tiffOutputFieldCreateOffsetField = null;
        TiffOutputField tiffOutputFieldCreateOffsetField2 = null;
        TiffOutputField tiffOutputFieldCreateOffsetField3 = null;
        for (TiffOutputDirectory tiffOutputDirectory3 : directories) {
            int i = tiffOutputDirectory3.type;
            map.put(Integer.valueOf(i), tiffOutputDirectory3);
            if (i < 0) {
                switch (i) {
                    case -4:
                        if (tiffOutputDirectory != null) {
                            throw new ImageWriteException("More than one Interoperability directory.");
                        }
                        tiffOutputDirectory = tiffOutputDirectory3;
                        break;
                    case -3:
                        if (tiffOutputDirectory2 != null) {
                            throw new ImageWriteException("More than one GPS directory.");
                        }
                        tiffOutputDirectory2 = tiffOutputDirectory3;
                        break;
                    case -2:
                        if (tiffOutputDirectoryAddExifDirectory != null) {
                            throw new ImageWriteException("More than one EXIF directory.");
                        }
                        tiffOutputDirectoryAddExifDirectory = tiffOutputDirectory3;
                        break;
                    default:
                        throw new ImageWriteException("Unknown directory: " + i);
                }
            } else {
                if (arrayList.contains(Integer.valueOf(i))) {
                    throw new ImageWriteException("More than one directory with index: " + i + ".");
                }
                arrayList.add(Integer.valueOf(i));
            }
            HashSet hashSet = new HashSet();
            for (TiffOutputField tiffOutputField : tiffOutputDirectory3.getFields()) {
                if (hashSet.contains(Integer.valueOf(tiffOutputField.tag))) {
                    throw new ImageWriteException("Tag (" + tiffOutputField.tagInfo.getDescription() + ") appears twice in directory.");
                }
                hashSet.add(Integer.valueOf(tiffOutputField.tag));
                if (tiffOutputField.tag == ExifTagConstants.EXIF_TAG_EXIF_OFFSET.tag) {
                    if (tiffOutputFieldCreateOffsetField2 != null) {
                        throw new ImageWriteException("More than one Exif directory offset field.");
                    }
                    tiffOutputFieldCreateOffsetField2 = tiffOutputField;
                } else if (tiffOutputField.tag == ExifTagConstants.EXIF_TAG_INTEROP_OFFSET.tag) {
                    if (tiffOutputFieldCreateOffsetField != null) {
                        throw new ImageWriteException("More than one Interoperability directory offset field.");
                    }
                    tiffOutputFieldCreateOffsetField = tiffOutputField;
                } else if (tiffOutputField.tag != ExifTagConstants.EXIF_TAG_GPSINFO.tag) {
                    continue;
                } else {
                    if (tiffOutputFieldCreateOffsetField3 != null) {
                        throw new ImageWriteException("More than one GPS directory offset field.");
                    }
                    tiffOutputFieldCreateOffsetField3 = tiffOutputField;
                }
            }
        }
        if (arrayList.isEmpty()) {
            throw new ImageWriteException("Missing root directory.");
        }
        Collections.sort(arrayList);
        TiffOutputDirectory tiffOutputDirectory4 = null;
        int i2 = 0;
        while (i2 < arrayList.size()) {
            Integer num = (Integer) arrayList.get(i2);
            if (num.intValue() != i2) {
                throw new ImageWriteException("Missing directory: " + i2 + ".");
            }
            TiffOutputDirectory tiffOutputDirectory5 = (TiffOutputDirectory) map.get(num);
            if (tiffOutputDirectory4 != null) {
                tiffOutputDirectory4.setNextDirectory(tiffOutputDirectory5);
            }
            i2++;
            tiffOutputDirectory4 = tiffOutputDirectory5;
        }
        TiffOutputDirectory tiffOutputDirectory6 = (TiffOutputDirectory) map.get(0);
        TiffOutputSummary tiffOutputSummary = new TiffOutputSummary(this.byteOrder, tiffOutputDirectory6, map);
        if (tiffOutputDirectory == null && tiffOutputFieldCreateOffsetField != null) {
            throw new ImageWriteException("Output set has Interoperability Directory Offset field, but no Interoperability Directory");
        }
        if (tiffOutputDirectory != null) {
            if (tiffOutputDirectoryAddExifDirectory == null) {
                tiffOutputDirectoryAddExifDirectory = tiffOutputSet.addExifDirectory();
            }
            if (tiffOutputFieldCreateOffsetField == null) {
                tiffOutputFieldCreateOffsetField = TiffOutputField.createOffsetField(ExifTagConstants.EXIF_TAG_INTEROP_OFFSET, this.byteOrder);
                tiffOutputDirectoryAddExifDirectory.add(tiffOutputFieldCreateOffsetField);
            }
            tiffOutputSummary.add(tiffOutputDirectory, tiffOutputFieldCreateOffsetField);
        }
        if (tiffOutputDirectoryAddExifDirectory == null && tiffOutputFieldCreateOffsetField2 != null) {
            throw new ImageWriteException("Output set has Exif Directory Offset field, but no Exif Directory");
        }
        if (tiffOutputDirectoryAddExifDirectory != null) {
            if (tiffOutputFieldCreateOffsetField2 == null) {
                tiffOutputFieldCreateOffsetField2 = TiffOutputField.createOffsetField(ExifTagConstants.EXIF_TAG_EXIF_OFFSET, this.byteOrder);
                tiffOutputDirectory6.add(tiffOutputFieldCreateOffsetField2);
            }
            tiffOutputSummary.add(tiffOutputDirectoryAddExifDirectory, tiffOutputFieldCreateOffsetField2);
        }
        if (tiffOutputDirectory2 == null && tiffOutputFieldCreateOffsetField3 != null) {
            throw new ImageWriteException("Output set has GPS Directory Offset field, but no GPS Directory");
        }
        if (tiffOutputDirectory2 != null) {
            if (tiffOutputFieldCreateOffsetField3 == null) {
                tiffOutputFieldCreateOffsetField3 = TiffOutputField.createOffsetField(ExifTagConstants.EXIF_TAG_GPSINFO, this.byteOrder);
                tiffOutputDirectory6.add(tiffOutputFieldCreateOffsetField3);
            }
            tiffOutputSummary.add(tiffOutputDirectory2, tiffOutputFieldCreateOffsetField3);
        }
        return tiffOutputSummary;
    }

    public void writeImage(final BufferedImage bufferedImage, final OutputStream outputStream, final Map<String, Object> m) throws ImageWriteException, IOException {
        final HashMap i = new HashMap((Map<? extends K, ? extends V>)m);
        if (i.containsKey("FORMAT")) {
            i.remove("FORMAT");
        }
        final boolean containsKey = i.containsKey("EXIF");
        String s = null;
        TiffOutputSet set;
        if (containsKey) {
            set = (TiffOutputSet)i.remove("EXIF");
        }
        else {
            set = null;
        }
        if (i.containsKey("XMP_XML")) {
            s = (String)i.get("XMP_XML");
            i.remove("XMP_XML");
        }
        PixelDensity fromPixelsPerInch;
        if ((fromPixelsPerInch = (PixelDensity)i.remove("PIXEL_DENSITY")) == null) {
            fromPixelsPerInch = PixelDensity.createFromPixelsPerInch(72.0, 72.0);
        }
        final int width = bufferedImage.getWidth();
        final int height = bufferedImage.getHeight();
        int n = 64000;
        int n2;
        if (i.containsKey("COMPRESSION")) {
            final Object value = i.get("COMPRESSION");
            int intValue;
            if (value != null) {
                if (!(value instanceof Number)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Invalid compression parameter, must be numeric: ");
                    sb.append(value);
                    throw new ImageWriteException(sb.toString());
                }
                intValue = ((Number)value).intValue();
            }
            else {
                intValue = 5;
            }
            i.remove("COMPRESSION");
            n2 = intValue;
            if (i.containsKey("PARAM_KEY_LZW_COMPRESSION_BLOCK_SIZE")) {
                final Object value2 = i.get("PARAM_KEY_LZW_COMPRESSION_BLOCK_SIZE");
                if (!(value2 instanceof Number)) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Invalid compression block-size parameter: ");
                    sb2.append(value);
                    throw new ImageWriteException(sb2.toString());
                }
                final int intValue2 = ((Number)value2).intValue();
                if (intValue2 < 8000) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("Block size parameter ");
                    sb3.append(intValue2);
                    sb3.append(" is less than 8000 minimum");
                    throw new ImageWriteException(sb3.toString());
                }
                n = intValue2 * 8;
                i.remove("PARAM_KEY_LZW_COMPRESSION_BLOCK_SIZE");
                n2 = intValue;
            }
        }
        else {
            n2 = 5;
        }
        final HashMap hashMap = new HashMap<Object, Integer>(i);
        i.remove("T4_OPTIONS");
        i.remove("T6_OPTIONS");
        if (!i.isEmpty()) {
            final Object next = i.keySet().iterator().next();
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Unknown parameter: ");
            sb4.append(next);
            throw new ImageWriteException(sb4.toString());
        }
        int n3;
        int n4;
        int n5;
        if (n2 != 2 && n2 != 3 && n2 != 4) {
            n3 = 8;
            n4 = 3;
            n5 = 2;
        }
        else {
            n4 = 1;
            n3 = 1;
            n5 = 0;
        }
        final int max = Math.max(1, n / (width * n3 * n4));
        final byte[][] strips = this.getStrips(bufferedImage, n4, n3, max);
        TiffOutputSet set2 = null;
        int n10 = 0;
        int n11 = 0;
        Label_1105: {
            Label_1102: {
                if (n2 == 2) {
                    int n6 = 0;
                    while (true) {
                        set2 = set;
                        if (n6 >= strips.length) {
                            break;
                        }
                        strips[n6] = T4AndT6Compression.compressModifiedHuffman(strips[n6], width, strips[n6].length / ((width + 7) / 8));
                        ++n6;
                    }
                }
                else if (n2 == 3) {
                    final Integer n7 = hashMap.get("T4_OPTIONS");
                    int intValue3;
                    if (n7 != null) {
                        intValue3 = n7;
                    }
                    else {
                        intValue3 = 0;
                    }
                    final int n8 = intValue3 & 0x7;
                    final boolean b = (n8 & 0x1) != 0x0;
                    if ((n8 & 0x2) != 0x0) {
                        throw new ImageWriteException("T.4 compression with the uncompressed mode extension is not yet supported");
                    }
                    final boolean b2 = (n8 & 0x4) != 0x0;
                    int j = 0;
                    final int n9 = n8;
                    while (j < strips.length) {
                        if (b) {
                            strips[j] = T4AndT6Compression.compressT4_2D(strips[j], width, strips[j].length / ((width + 7) / 8), b2, max);
                        }
                        else {
                            strips[j] = T4AndT6Compression.compressT4_1D(strips[j], width, strips[j].length / ((width + 7) / 8), b2);
                        }
                        ++j;
                    }
                    n10 = n9;
                    set2 = set;
                    n11 = 0;
                    break Label_1105;
                }
                else if (n2 == 4) {
                    final Integer n12 = hashMap.get("T6_OPTIONS");
                    int intValue4;
                    if (n12 != null) {
                        intValue4 = n12;
                    }
                    else {
                        intValue4 = 0;
                    }
                    final int n13 = 0x4 & intValue4;
                    if ((n13 & 0x2) != 0x0) {
                        throw new ImageWriteException("T.6 compression with the uncompressed mode extension is not yet supported");
                    }
                    for (int k = 0; k < strips.length; ++k) {
                        strips[k] = T4AndT6Compression.compressT6(strips[k], width, strips[k].length / ((width + 7) / 8));
                    }
                    set2 = set;
                    n11 = n13;
                    break Label_1102;
                }
                else if (n2 == 32773) {
                    int n14 = 0;
                    while (true) {
                        set2 = set;
                        if (n14 >= strips.length) {
                            break;
                        }
                        strips[n14] = new PackBits().compress(strips[n14]);
                        ++n14;
                    }
                }
                else if (n2 == 5) {
                    int n15 = 0;
                    while (true) {
                        set2 = set;
                        if (n15 >= strips.length) {
                            break;
                        }
                        strips[n15] = new MyLzwCompressor(8, ByteOrder.BIG_ENDIAN, true).compress(strips[n15]);
                        ++n15;
                    }
                }
                else {
                    set2 = set;
                    if (n2 != 1) {
                        throw new ImageWriteException("Invalid compression parameter (Only CCITT 1D/Group 3/Group 4, LZW, Packbits and uncompressed supported).");
                    }
                }
                n11 = 0;
            }
            n10 = 0;
        }
        final TiffElement.DataElement[] array = new TiffElement.DataElement[strips.length];
        for (int l = 0; l < strips.length; ++l) {
            array[l] = new TiffImageData.Data(0L, strips[l].length, strips[l]);
        }
        final TiffOutputSet set3 = new TiffOutputSet(this.byteOrder);
        final TiffOutputDirectory addRootDirectory = set3.addRootDirectory();
        addRootDirectory.add(TiffTagConstants.TIFF_TAG_IMAGE_WIDTH, width);
        addRootDirectory.add(TiffTagConstants.TIFF_TAG_IMAGE_LENGTH, height);
        addRootDirectory.add(TiffTagConstants.TIFF_TAG_PHOTOMETRIC_INTERPRETATION, (short)n5);
        addRootDirectory.add(TiffTagConstants.TIFF_TAG_COMPRESSION, (short)n2);
        addRootDirectory.add(TiffTagConstants.TIFF_TAG_SAMPLES_PER_PIXEL, (short)n4);
        if (n4 == 3) {
            final TagInfoShort tiff_TAG_BITS_PER_SAMPLE = TiffTagConstants.TIFF_TAG_BITS_PER_SAMPLE;
            final short n16 = (short)n3;
            addRootDirectory.add(tiff_TAG_BITS_PER_SAMPLE, n16, n16, n16);
        }
        else if (n4 == 1) {
            addRootDirectory.add(TiffTagConstants.TIFF_TAG_BITS_PER_SAMPLE, (short)n3);
        }
        addRootDirectory.add(TiffTagConstants.TIFF_TAG_ROWS_PER_STRIP, max);
        if (fromPixelsPerInch.isUnitless()) {
            addRootDirectory.add(TiffTagConstants.TIFF_TAG_RESOLUTION_UNIT, 0);
            addRootDirectory.add(TiffTagConstants.TIFF_TAG_XRESOLUTION, RationalNumber.valueOf(fromPixelsPerInch.getRawHorizontalDensity()));
            addRootDirectory.add(TiffTagConstants.TIFF_TAG_YRESOLUTION, RationalNumber.valueOf(fromPixelsPerInch.getRawVerticalDensity()));
        }
        else if (fromPixelsPerInch.isInInches()) {
            addRootDirectory.add(TiffTagConstants.TIFF_TAG_RESOLUTION_UNIT, 2);
            addRootDirectory.add(TiffTagConstants.TIFF_TAG_XRESOLUTION, RationalNumber.valueOf(fromPixelsPerInch.horizontalDensityInches()));
            addRootDirectory.add(TiffTagConstants.TIFF_TAG_YRESOLUTION, RationalNumber.valueOf(fromPixelsPerInch.verticalDensityInches()));
        }
        else {
            addRootDirectory.add(TiffTagConstants.TIFF_TAG_RESOLUTION_UNIT, 1);
            addRootDirectory.add(TiffTagConstants.TIFF_TAG_XRESOLUTION, RationalNumber.valueOf(fromPixelsPerInch.horizontalDensityCentimetres()));
            addRootDirectory.add(TiffTagConstants.TIFF_TAG_YRESOLUTION, RationalNumber.valueOf(fromPixelsPerInch.verticalDensityCentimetres()));
        }
        if (n10 != 0) {
            addRootDirectory.add(TiffTagConstants.TIFF_TAG_T4_OPTIONS, n10);
        }
        if (n11 != 0) {
            addRootDirectory.add(TiffTagConstants.TIFF_TAG_T6_OPTIONS, n11);
        }
        if (s != null) {
            addRootDirectory.add(TiffTagConstants.TIFF_TAG_XMP, s.getBytes("utf-8"));
        }
        addRootDirectory.setTiffImageData(new TiffImageData.Strips(array, max));
        if (set2 != null) {
            this.combineUserExifIntoFinalExif(set2, set3);
        }
        this.write(outputStream, set3);
    }

    private void combineUserExifIntoFinalExif(TiffOutputSet tiffOutputSet, TiffOutputSet tiffOutputSet2) throws ImageWriteException {
        List<TiffOutputDirectory> directories = tiffOutputSet2.getDirectories();
        Collections.sort(directories, TiffOutputDirectory.COMPARATOR);
        for (TiffOutputDirectory tiffOutputDirectory : tiffOutputSet.getDirectories()) {
            int iBinarySearch = Collections.binarySearch(directories, tiffOutputDirectory, TiffOutputDirectory.COMPARATOR);
            if (iBinarySearch < 0) {
                tiffOutputSet2.addDirectory(tiffOutputDirectory);
            } else {
                TiffOutputDirectory tiffOutputDirectory2 = directories.get(iBinarySearch);
                for (TiffOutputField tiffOutputField : tiffOutputDirectory.getFields()) {
                    if (tiffOutputDirectory2.findField(tiffOutputField.tagInfo) == null) {
                        tiffOutputDirectory2.add(tiffOutputField);
                    }
                }
            }
        }
    }

    private byte[][] getStrips(BufferedImage bufferedImage, int i, int i2, int i3) {
        char c;
        int i4 = i3;
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        boolean z = true;
        int i5 = ((height + i4) - 1) / i4;
        byte[][] bArr = new byte[i5][];
        int i6 = 0;
        int i7 = height;
        int i8 = 0;
        while (i8 < i5) {
            int iMin = Math.min(i4, i7);
            i7 -= iMin;
            byte[] bArr2 = new byte[iMin * ((((i2 * i) * width) + 7) / 8)];
            int i9 = i8 * i4;
            int i10 = i9 + i4;
            int i11 = i6;
            while (i9 < height && i9 < i10) {
                int i12 = i6;
                int i13 = i12;
                int i14 = i11;
                int i15 = i13;
                while (i15 < width) {
                    int rgb = bufferedImage.getRGB(i15, i9);
                    int i16 = 255 & (rgb >> 16);
                    int i17 = 255 & (rgb >> 8);
                    int i18 = 255 & (rgb >> 0);
                    int i19 = width;
                    if (i2 == 1) {
                        int i20 = (i13 << 1) | (((i16 + i17) + i18) / 3 > 127 ? 0 : 1);
                        int i21 = i12 + 1;
                        if (i21 == 8) {
                            bArr2[i14] = (byte) i20;
                            i14++;
                            i21 = 0;
                            i20 = 0;
                        }
                        i12 = i21;
                        i13 = i20;
                    } else {
                        int i22 = i14 + 1;
                        bArr2[i14] = (byte) i16;
                        int i23 = i22 + 1;
                        bArr2[i22] = (byte) i17;
                        bArr2[i23] = (byte) i18;
                        i14 = i23 + 1;
                    }
                    i15++;
                    z = true;
                    width = i19;
                }
                int i24 = width;
                boolean z2 = z;
                if (i12 > 0) {
                    c = '\b';
                    bArr2[i14] = (byte) (i13 << (8 - i12));
                    i11 = i14 + 1;
                } else {
                    c = '\b';
                    i11 = i14;
                }
                i9++;
                z = z2;
                width = i24;
                i6 = 0;
            }
            bArr[i8] = bArr2;
            i8++;
            z = z;
            width = width;
            i4 = i3;
            i6 = 0;
        }
        return bArr;
    }

    protected void writeImageFileHeader(BinaryOutputStream binaryOutputStream) throws IOException {
        writeImageFileHeader(binaryOutputStream, 8L);
    }

    protected void writeImageFileHeader(BinaryOutputStream binaryOutputStream, long j) throws IOException {
        if (this.byteOrder == ByteOrder.LITTLE_ENDIAN) {
            binaryOutputStream.write(73);
            binaryOutputStream.write(73);
        } else {
            binaryOutputStream.write(77);
            binaryOutputStream.write(77);
        }
        binaryOutputStream.write2Bytes(42);
        binaryOutputStream.write4Bytes((int) j);
    }
}
