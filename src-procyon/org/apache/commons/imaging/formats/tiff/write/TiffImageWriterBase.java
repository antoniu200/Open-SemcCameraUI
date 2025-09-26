// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.write;

import org.apache.commons.imaging.common.BinaryOutputStream;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoShort;
import org.apache.commons.imaging.common.RationalNumber;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.apache.commons.imaging.formats.tiff.TiffImageData;
import org.apache.commons.imaging.formats.tiff.TiffElement;
import org.apache.commons.imaging.common.mylzw.MyLzwCompressor;
import org.apache.commons.imaging.common.PackBits;
import org.apache.commons.imaging.common.itu_t4.T4AndT6Compression;
import org.apache.commons.imaging.PixelDensity;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import java.util.Map;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import org.apache.commons.imaging.ImageWriteException;
import java.util.Iterator;
import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import org.apache.commons.imaging.formats.tiff.constants.TiffConstants;
import java.nio.ByteOrder;

public abstract class TiffImageWriterBase
{
    protected final ByteOrder byteOrder;
    
    public TiffImageWriterBase() {
        this.byteOrder = TiffConstants.DEFAULT_TIFF_BYTE_ORDER;
    }
    
    public TiffImageWriterBase(final ByteOrder byteOrder) {
        this.byteOrder = byteOrder;
    }
    
    private void combineUserExifIntoFinalExif(final TiffOutputSet set, final TiffOutputSet set2) throws ImageWriteException {
        final List<TiffOutputDirectory> directories = set2.getDirectories();
        Collections.sort((List<Object>)directories, (Comparator<? super Object>)TiffOutputDirectory.COMPARATOR);
        for (final TiffOutputDirectory key : set.getDirectories()) {
            final int binarySearch = Collections.binarySearch(directories, key, TiffOutputDirectory.COMPARATOR);
            if (binarySearch < 0) {
                set2.addDirectory(key);
            }
            else {
                final TiffOutputDirectory tiffOutputDirectory = directories.get(binarySearch);
                for (final TiffOutputField tiffOutputField : key.getFields()) {
                    if (tiffOutputDirectory.findField(tiffOutputField.tagInfo) == null) {
                        tiffOutputDirectory.add(tiffOutputField);
                    }
                }
            }
        }
    }
    
    private byte[][] getStrips(final BufferedImage bufferedImage, final int n, final int n2, final int a) {
        final int width = bufferedImage.getWidth();
        final int height = bufferedImage.getHeight();
        final int n3 = (height + a - 1) / a;
        final byte[][] array = new byte[n3][];
        int b = height;
        int n4;
        for (int i = 0; i < n3; ++i, b = n4) {
            final int min = Math.min(a, b);
            n4 = b - min;
            final byte[] array2 = new byte[min * ((n2 * n * width + 7) / 8)];
            final int n5 = i * a;
            int n6 = 0;
            int n7 = n5;
            while (true) {
                final int y = n7;
                if (y >= height || y >= n5 + a) {
                    break;
                }
                int n8 = 0;
                int j;
                int n9;
                int n14;
                for (n9 = (j = 0); j < width; ++j, n8 = n14) {
                    final int rgb = bufferedImage.getRGB(j, y);
                    final int n10 = 0xFF & rgb >> 16;
                    final int n11 = 0xFF & rgb >> 8;
                    final int n12 = 0xFF & rgb >> 0;
                    if (n2 == 1) {
                        final int n13 = n9 << 1 | (((n10 + n11 + n12) / 3 <= 127) ? 1 : 0);
                        n14 = n8 + 1;
                        n9 = n13;
                        int n15 = n6;
                        if (n14 == 8) {
                            array2[n6] = (byte)n13;
                            n15 = n6 + 1;
                            n14 = 0;
                            n9 = 0;
                        }
                        n6 = n15;
                    }
                    else {
                        final int n16 = n6 + 1;
                        array2[n6] = (byte)n10;
                        n6 = n16 + 1;
                        array2[n16] = (byte)n11;
                        array2[n6] = (byte)n12;
                        ++n6;
                        n14 = n8;
                    }
                }
                if (n8 > 0) {
                    array2[n6] = (byte)(n9 << 8 - n8);
                    ++n6;
                }
                n7 = y + 1;
            }
            array[i] = array2;
        }
        return array;
    }
    
    protected static int imageDataPaddingLength(final int n) {
        return (4 - n % 4) % 4;
    }
    
    protected TiffOutputSummary validateDirectories(final TiffOutputSet set) throws ImageWriteException {
        final List<TiffOutputDirectory> directories = set.getDirectories();
        if (directories.isEmpty()) {
            throw new ImageWriteException("No directories.");
        }
        final ArrayList list = new ArrayList();
        final HashMap hashMap = new HashMap();
        final Iterator<TiffOutputDirectory> iterator = directories.iterator();
        TiffOutputDirectory tiffOutputDirectory = null;
        TiffOutputItem tiffOutputItem = null;
        Object o;
        TiffOutputDirectory tiffOutputDirectory2 = (TiffOutputDirectory)(o = tiffOutputItem);
        Object o2;
        TiffOutputField tiffOutputField = (TiffOutputField)(o2 = o);
    Label_0070:
        while (iterator.hasNext()) {
            final TiffOutputDirectory tiffOutputDirectory3 = iterator.next();
            final int type = tiffOutputDirectory3.type;
            hashMap.put(type, tiffOutputDirectory3);
            TiffOutputDirectory tiffOutputDirectory4 = null;
            TiffOutputDirectory tiffOutputDirectory5 = null;
            TiffOutputDirectory tiffOutputDirectory6 = null;
            if (type < 0) {
                switch (type) {
                    default: {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Unknown directory: ");
                        sb.append(type);
                        throw new ImageWriteException(sb.toString());
                    }
                    case -2: {
                        if (tiffOutputDirectory2 != null) {
                            throw new ImageWriteException("More than one EXIF directory.");
                        }
                        tiffOutputDirectory4 = tiffOutputDirectory3;
                        tiffOutputDirectory5 = tiffOutputDirectory;
                        tiffOutputDirectory6 = (TiffOutputDirectory)tiffOutputItem;
                        break;
                    }
                    case -3: {
                        if (tiffOutputItem != null) {
                            throw new ImageWriteException("More than one GPS directory.");
                        }
                        tiffOutputDirectory6 = tiffOutputDirectory3;
                        tiffOutputDirectory5 = tiffOutputDirectory;
                        tiffOutputDirectory4 = tiffOutputDirectory2;
                        break;
                    }
                    case -4: {
                        if (tiffOutputDirectory != null) {
                            throw new ImageWriteException("More than one Interoperability directory.");
                        }
                        tiffOutputDirectory5 = tiffOutputDirectory3;
                        tiffOutputDirectory6 = (TiffOutputDirectory)tiffOutputItem;
                        tiffOutputDirectory4 = tiffOutputDirectory2;
                        break;
                    }
                }
            }
            else {
                if (list.contains(type)) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("More than one directory with index: ");
                    sb2.append(type);
                    sb2.append(".");
                    throw new ImageWriteException(sb2.toString());
                }
                list.add(type);
                tiffOutputDirectory4 = tiffOutputDirectory2;
                tiffOutputDirectory6 = (TiffOutputDirectory)tiffOutputItem;
                tiffOutputDirectory5 = tiffOutputDirectory;
            }
            final HashSet set2 = new HashSet();
            final Iterator<TiffOutputField> iterator2 = tiffOutputDirectory3.getFields().iterator();
            Object o3 = o2;
            TiffOutputField tiffOutputField2 = tiffOutputField;
            Object o4 = o;
            TiffOutputField tiffOutputField3;
            while (true) {
                tiffOutputDirectory = tiffOutputDirectory5;
                tiffOutputItem = tiffOutputDirectory6;
                tiffOutputDirectory2 = tiffOutputDirectory4;
                o = o4;
                tiffOutputField = tiffOutputField2;
                o2 = o3;
                if (!iterator2.hasNext()) {
                    continue Label_0070;
                }
                tiffOutputField3 = iterator2.next();
                if (set2.contains(tiffOutputField3.tag)) {
                    break;
                }
                set2.add(tiffOutputField3.tag);
                if (tiffOutputField3.tag == ExifTagConstants.EXIF_TAG_EXIF_OFFSET.tag) {
                    if (tiffOutputField2 != null) {
                        throw new ImageWriteException("More than one Exif directory offset field.");
                    }
                    tiffOutputField2 = tiffOutputField3;
                }
                else if (tiffOutputField3.tag == ExifTagConstants.EXIF_TAG_INTEROP_OFFSET.tag) {
                    if (o4 != null) {
                        throw new ImageWriteException("More than one Interoperability directory offset field.");
                    }
                    o4 = tiffOutputField3;
                }
                else {
                    if (tiffOutputField3.tag != ExifTagConstants.EXIF_TAG_GPSINFO.tag) {
                        continue;
                    }
                    if (o3 != null) {
                        throw new ImageWriteException("More than one GPS directory offset field.");
                    }
                    o3 = tiffOutputField3;
                }
            }
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Tag (");
            sb3.append(tiffOutputField3.tagInfo.getDescription());
            sb3.append(") appears twice in directory.");
            throw new ImageWriteException(sb3.toString());
        }
        if (list.isEmpty()) {
            throw new ImageWriteException("Missing root directory.");
        }
        Collections.sort((List<Comparable>)list);
        TiffOutputDirectory tiffOutputDirectory7 = null;
        TiffOutputDirectory nextDirectory;
        for (int i = 0; i < list.size(); ++i, tiffOutputDirectory7 = nextDirectory) {
            final Integer n = (Integer)list.get(i);
            if (n != i) {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("Missing directory: ");
                sb4.append(i);
                sb4.append(".");
                throw new ImageWriteException(sb4.toString());
            }
            nextDirectory = (TiffOutputDirectory)hashMap.get(n);
            if (tiffOutputDirectory7 != null) {
                tiffOutputDirectory7.setNextDirectory(nextDirectory);
            }
        }
        final TiffOutputDirectory tiffOutputDirectory8 = (TiffOutputDirectory)hashMap.get(0);
        final TiffOutputSummary tiffOutputSummary = new TiffOutputSummary(this.byteOrder, tiffOutputDirectory8, hashMap);
        if (tiffOutputDirectory == null && o != null) {
            throw new ImageWriteException("Output set has Interoperability Directory Offset field, but no Interoperability Directory");
        }
        TiffOutputDirectory addExifDirectory = tiffOutputDirectory2;
        if (tiffOutputDirectory != null) {
            if ((addExifDirectory = tiffOutputDirectory2) == null) {
                addExifDirectory = set.addExifDirectory();
            }
            TiffOutputField offsetField;
            if ((offsetField = (TiffOutputField)o) == null) {
                offsetField = TiffOutputField.createOffsetField(ExifTagConstants.EXIF_TAG_INTEROP_OFFSET, this.byteOrder);
                addExifDirectory.add(offsetField);
            }
            tiffOutputSummary.add(tiffOutputDirectory, offsetField);
        }
        if (addExifDirectory == null && tiffOutputField != null) {
            throw new ImageWriteException("Output set has Exif Directory Offset field, but no Exif Directory");
        }
        if (addExifDirectory != null) {
            TiffOutputField offsetField2;
            if ((offsetField2 = tiffOutputField) == null) {
                offsetField2 = TiffOutputField.createOffsetField(ExifTagConstants.EXIF_TAG_EXIF_OFFSET, this.byteOrder);
                tiffOutputDirectory8.add(offsetField2);
            }
            tiffOutputSummary.add(addExifDirectory, offsetField2);
        }
        if (tiffOutputItem == null && o2 != null) {
            throw new ImageWriteException("Output set has GPS Directory Offset field, but no GPS Directory");
        }
        if (tiffOutputItem != null) {
            TiffOutputField offsetField3;
            if ((offsetField3 = (TiffOutputField)o2) == null) {
                offsetField3 = TiffOutputField.createOffsetField(ExifTagConstants.EXIF_TAG_GPSINFO, this.byteOrder);
                tiffOutputDirectory8.add(offsetField3);
            }
            tiffOutputSummary.add(tiffOutputItem, offsetField3);
        }
        return tiffOutputSummary;
    }
    
    public abstract void write(final OutputStream p0, final TiffOutputSet p1) throws IOException, ImageWriteException;
    
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
    
    protected void writeImageFileHeader(final BinaryOutputStream binaryOutputStream) throws IOException {
        this.writeImageFileHeader(binaryOutputStream, 8L);
    }
    
    protected void writeImageFileHeader(final BinaryOutputStream binaryOutputStream, final long n) throws IOException {
        if (this.byteOrder == ByteOrder.LITTLE_ENDIAN) {
            binaryOutputStream.write(73);
            binaryOutputStream.write(73);
        }
        else {
            binaryOutputStream.write(77);
            binaryOutputStream.write(77);
        }
        binaryOutputStream.write2Bytes(42);
        binaryOutputStream.write4Bytes((int)n);
    }
}
