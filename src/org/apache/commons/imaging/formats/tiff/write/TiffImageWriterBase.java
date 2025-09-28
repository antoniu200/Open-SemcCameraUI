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

    /* JADX WARN: Removed duplicated region for block: B:119:0x025e A[LOOP:1: B:117:0x025b->B:119:0x025e, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:122:0x02bd  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x02cd  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x02ea  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x0316  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x0376  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x0381  */
    /* JADX WARN: Removed duplicated region for block: B:137:0x038c  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x03a3  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void writeImage(java.awt.image.BufferedImage r24, java.io.OutputStream r25, java.util.Map<java.lang.String, java.lang.Object> r26) throws org.apache.commons.imaging.ImageWriteException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 950
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.imaging.formats.tiff.write.TiffImageWriterBase.writeImage(java.awt.image.BufferedImage, java.io.OutputStream, java.util.Map):void");
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
