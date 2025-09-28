package org.apache.commons.imaging.formats.tiff;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.imaging.FormatCompliance;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImagingConstants;
import org.apache.commons.imaging.common.BinaryFileParser;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.common.bytesource.ByteSourceFile;
import org.apache.commons.imaging.formats.tiff.TiffDirectory;
import org.apache.commons.imaging.formats.tiff.TiffImageData;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.apache.commons.imaging.util.IoUtils;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class TiffReader extends BinaryFileParser {
    private final boolean strict;

    public interface Listener {
        boolean addDirectory(TiffDirectory tiffDirectory);

        boolean addField(TiffField tiffField);

        boolean readImageData();

        boolean readOffsetDirectories();

        boolean setTiffHeader(TiffHeader tiffHeader);
    }

    public TiffReader(boolean z) {
        this.strict = z;
    }

    private TiffHeader readTiffHeader(ByteSource byteSource) throws Throwable {
        InputStream inputStream;
        try {
            inputStream = byteSource.getInputStream();
        } catch (Throwable th) {
            th = th;
            inputStream = null;
        }
        try {
            TiffHeader tiffHeader = readTiffHeader(inputStream);
            IoUtils.closeQuietly(true, inputStream);
            return tiffHeader;
        } catch (Throwable th2) {
            th = th2;
            IoUtils.closeQuietly(false, inputStream);
            throw th;
        }
    }

    private ByteOrder getTiffByteOrder(int i) throws ImageReadException {
        if (i == 73) {
            return ByteOrder.LITTLE_ENDIAN;
        }
        if (i == 77) {
            return ByteOrder.BIG_ENDIAN;
        }
        throw new ImageReadException("Invalid TIFF byte order " + (i & 255));
    }

    private TiffHeader readTiffHeader(InputStream inputStream) throws IOException, ImageReadException {
        byte b = BinaryFunctions.readByte("BYTE_ORDER_1", inputStream, "Not a Valid TIFF File");
        byte b2 = BinaryFunctions.readByte("BYTE_ORDER_2", inputStream, "Not a Valid TIFF File");
        if (b != b2) {
            throw new ImageReadException("Byte Order bytes don't match (" + ((int) b) + ", " + ((int) b2) + ").");
        }
        ByteOrder tiffByteOrder = getTiffByteOrder(b);
        setByteOrder(tiffByteOrder);
        int i = BinaryFunctions.read2Bytes("tiffVersion", inputStream, "Not a Valid TIFF File", getByteOrder());
        if (i != 42) {
            throw new ImageReadException("Unknown Tiff Version: " + i);
        }
        long j = 4294967295L & BinaryFunctions.read4Bytes("offsetToFirstIFD", inputStream, "Not a Valid TIFF File", getByteOrder());
        BinaryFunctions.skipBytes(inputStream, j - 8, "Not a Valid TIFF File: couldn't find IFDs");
        if (getDebug()) {
            System.out.println("");
        }
        return new TiffHeader(tiffByteOrder, i, j);
    }

    private void readDirectories(ByteSource byteSource, FormatCompliance formatCompliance, Listener listener) throws Throwable {
        TiffHeader tiffHeader = readTiffHeader(byteSource);
        if (listener.setTiffHeader(tiffHeader)) {
            readDirectory(byteSource, tiffHeader.offsetToFirstIFD, 0, formatCompliance, listener, new ArrayList());
        }
    }

    private boolean readDirectory(ByteSource byteSource, long j, int i, FormatCompliance formatCompliance, Listener listener, List<Number> list) throws IOException, ImageReadException {
        return readDirectory(byteSource, j, i, formatCompliance, listener, false, list);
    }

    /* JADX WARN: Code restructure failed: missing block: B:47:0x0134, code lost:
    
        r14 = new org.apache.commons.imaging.formats.tiff.TiffDirectory(r34, r8, r32, 4294967295L & org.apache.commons.imaging.common.BinaryFunctions.read4Bytes("nextDirectoryOffset", r9, "Not a Valid TIFF File", getByteOrder()));
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x0157, code lost:
    
        if (r36.readImageData() == false) goto L55;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x015d, code lost:
    
        if (r14.hasTiffImageData() == false) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x015f, code lost:
    
        r14.setTiffImageData(getTiffRawImageData(r31, r14));
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x016a, code lost:
    
        if (r14.hasJpegImageData() == false) goto L55;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x016c, code lost:
    
        r14.setJpegImageData(getJpegRawImageData(r31, r14));
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x0177, code lost:
    
        if (r36.addDirectory(r14) != false) goto L58;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x0179, code lost:
    
        r1 = true;
        r2 = new java.io.Closeable[]{r9};
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x0185, code lost:
    
        if (r36.readOffsetDirectories() == false) goto L89;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x0187, code lost:
    
        r15 = new org.apache.commons.imaging.formats.tiff.taginfos.TagInfoLong[3];
        r15[0] = org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants.EXIF_TAG_EXIF_OFFSET;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x0191, code lost:
    
        r15[1] = org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants.EXIF_TAG_GPSINFO;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x0194, code lost:
    
        r15[2] = org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants.EXIF_TAG_INTEROP_OFFSET;
        r7 = new int[]{-2, -3, -4};
        r6 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x019f, code lost:
    
        if (r6 >= r15.length) goto L136;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x01a1, code lost:
    
        r5 = r14.findField(r15[r6]);
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x01a7, code lost:
    
        if (r5 == null) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x01b0, code lost:
    
        r28 = r5;
        r18 = r6;
        r20 = r7;
        r29 = r15;
        r15 = r8;
        r19 = r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x01c7, code lost:
    
        r1 = readDirectory(r31, r14.getSingleFieldValue(r1), r7[r6], r35, r36, true, r38);
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x01cc, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x01ce, code lost:
    
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x01cf, code lost:
    
        r28 = r5;
        r18 = r6;
        r20 = r7;
        r19 = r9;
        r29 = r15;
        r15 = r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x01da, code lost:
    
        r1 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x01dd, code lost:
    
        if (r30.strict != false) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x01df, code lost:
    
        throw r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x01e0, code lost:
    
        r1 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x01e1, code lost:
    
        if (r1 == false) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x01e3, code lost:
    
        r15.remove(r28);
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x01e9, code lost:
    
        r18 = r6;
        r20 = r7;
        r19 = r9;
        r29 = r15;
        r15 = r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x01f2, code lost:
    
        r6 = r18 + 1;
        r8 = r15;
        r9 = r19;
        r7 = r20;
        r15 = r29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x01fc, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x01fd, code lost:
    
        r1 = r0;
        r2 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x0202, code lost:
    
        r19 = r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x0204, code lost:
    
        if (r37 != false) goto L94;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x020a, code lost:
    
        if (r14.nextDirectoryOffset <= 0) goto L94;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x020c, code lost:
    
        readDirectory(r31, r14.nextDirectoryOffset, r34 + 1, r35, r36, r38);
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x021a, code lost:
    
        r1 = true;
        r2 = new java.io.Closeable[]{r19};
     */
    /* JADX WARN: Not initialized variable reg: 19, insn: 0x023a: MOVE (r9 I:??[OBJECT, ARRAY]) = (r19 I:??[OBJECT, ARRAY]), block:B:105:0x0239 */
    /* JADX WARN: Unreachable blocks removed: 1, instructions: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean readDirectory(org.apache.commons.imaging.common.bytesource.ByteSource r31, long r32, int r34, org.apache.commons.imaging.FormatCompliance r35, org.apache.commons.imaging.formats.tiff.TiffReader.Listener r36, boolean r37, java.util.List<java.lang.Number> r38) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 604
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.imaging.formats.tiff.TiffReader.readDirectory(org.apache.commons.imaging.common.bytesource.ByteSource, long, int, org.apache.commons.imaging.FormatCompliance, org.apache.commons.imaging.formats.tiff.TiffReader$Listener, boolean, java.util.List):boolean");
    }

    private static class Collector implements Listener {
        private final List<TiffDirectory> directories;
        private final List<TiffField> fields;
        private final boolean readThumbnails;
        private TiffHeader tiffHeader;

        @Override // org.apache.commons.imaging.formats.tiff.TiffReader.Listener
        public boolean readOffsetDirectories() {
            return true;
        }

        public Collector() {
            this(null);
        }

        public Collector(Map<String, Object> map) {
            this.directories = new ArrayList();
            this.fields = new ArrayList();
            this.readThumbnails = (map == null || !map.containsKey(ImagingConstants.PARAM_KEY_READ_THUMBNAILS)) ? true : Boolean.TRUE.equals(map.get(ImagingConstants.PARAM_KEY_READ_THUMBNAILS));
        }

        @Override // org.apache.commons.imaging.formats.tiff.TiffReader.Listener
        public boolean setTiffHeader(TiffHeader tiffHeader) {
            this.tiffHeader = tiffHeader;
            return true;
        }

        @Override // org.apache.commons.imaging.formats.tiff.TiffReader.Listener
        public boolean addDirectory(TiffDirectory tiffDirectory) {
            this.directories.add(tiffDirectory);
            return true;
        }

        @Override // org.apache.commons.imaging.formats.tiff.TiffReader.Listener
        public boolean addField(TiffField tiffField) {
            this.fields.add(tiffField);
            return true;
        }

        @Override // org.apache.commons.imaging.formats.tiff.TiffReader.Listener
        public boolean readImageData() {
            return this.readThumbnails;
        }

        public TiffContents getContents() {
            return new TiffContents(this.tiffHeader, this.directories);
        }
    }

    private static class FirstDirectoryCollector extends Collector {
        private final boolean readImageData;

        public FirstDirectoryCollector(boolean z) {
            this.readImageData = z;
        }

        @Override // org.apache.commons.imaging.formats.tiff.TiffReader.Collector, org.apache.commons.imaging.formats.tiff.TiffReader.Listener
        public boolean addDirectory(TiffDirectory tiffDirectory) {
            super.addDirectory(tiffDirectory);
            return false;
        }

        @Override // org.apache.commons.imaging.formats.tiff.TiffReader.Collector, org.apache.commons.imaging.formats.tiff.TiffReader.Listener
        public boolean readImageData() {
            return this.readImageData;
        }
    }

    public TiffContents readFirstDirectory(ByteSource byteSource, Map<String, Object> map, boolean z, FormatCompliance formatCompliance) throws IOException, ImageReadException {
        FirstDirectoryCollector firstDirectoryCollector = new FirstDirectoryCollector(z);
        read(byteSource, map, formatCompliance, firstDirectoryCollector);
        TiffContents contents = firstDirectoryCollector.getContents();
        if (contents.directories.size() < 1) {
            throw new ImageReadException("Image did not contain any directories.");
        }
        return contents;
    }

    public TiffContents readDirectories(ByteSource byteSource, boolean z, FormatCompliance formatCompliance) throws IOException, ImageReadException {
        Collector collector = new Collector(null);
        readDirectories(byteSource, formatCompliance, collector);
        TiffContents contents = collector.getContents();
        if (contents.directories.size() < 1) {
            throw new ImageReadException("Image did not contain any directories.");
        }
        return contents;
    }

    public TiffContents readContents(ByteSource byteSource, Map<String, Object> map, FormatCompliance formatCompliance) throws IOException, ImageReadException {
        Collector collector = new Collector(map);
        read(byteSource, map, formatCompliance, collector);
        return collector.getContents();
    }

    public void read(ByteSource byteSource, Map<String, Object> map, FormatCompliance formatCompliance, Listener listener) throws Throwable {
        readDirectories(byteSource, formatCompliance, listener);
    }

    private TiffImageData getTiffRawImageData(ByteSource byteSource, TiffDirectory tiffDirectory) throws IOException, ImageReadException {
        List<TiffDirectory.ImageDataElement> tiffRawImageDataElements = tiffDirectory.getTiffRawImageDataElements();
        TiffImageData.Data[] dataArr = new TiffImageData.Data[tiffRawImageDataElements.size()];
        int i = 0;
        if (byteSource instanceof ByteSourceFile) {
            ByteSourceFile byteSourceFile = (ByteSourceFile) byteSource;
            while (i < tiffRawImageDataElements.size()) {
                TiffDirectory.ImageDataElement imageDataElement = tiffRawImageDataElements.get(i);
                dataArr[i] = new TiffImageData.ByteSourceData(imageDataElement.offset, imageDataElement.length, byteSourceFile);
                i++;
            }
        } else {
            while (i < tiffRawImageDataElements.size()) {
                TiffDirectory.ImageDataElement imageDataElement2 = tiffRawImageDataElements.get(i);
                dataArr[i] = new TiffImageData.Data(imageDataElement2.offset, imageDataElement2.length, byteSource.getBlock(imageDataElement2.offset, imageDataElement2.length));
                i++;
            }
        }
        if (tiffDirectory.imageDataInStrips()) {
            TiffField tiffFieldFindField = tiffDirectory.findField(TiffTagConstants.TIFF_TAG_ROWS_PER_STRIP);
            int intValue = Integer.MAX_VALUE;
            if (tiffFieldFindField != null) {
                intValue = tiffFieldFindField.getIntValue();
            } else {
                TiffField tiffFieldFindField2 = tiffDirectory.findField(TiffTagConstants.TIFF_TAG_IMAGE_LENGTH);
                if (tiffFieldFindField2 != null) {
                    intValue = tiffFieldFindField2.getIntValue();
                }
            }
            return new TiffImageData.Strips(dataArr, intValue);
        }
        TiffField tiffFieldFindField3 = tiffDirectory.findField(TiffTagConstants.TIFF_TAG_TILE_WIDTH);
        if (tiffFieldFindField3 == null) {
            throw new ImageReadException("Can't find tile width field.");
        }
        int intValue2 = tiffFieldFindField3.getIntValue();
        TiffField tiffFieldFindField4 = tiffDirectory.findField(TiffTagConstants.TIFF_TAG_TILE_LENGTH);
        if (tiffFieldFindField4 == null) {
            throw new ImageReadException("Can't find tile length field.");
        }
        return new TiffImageData.Tiles(dataArr, intValue2, tiffFieldFindField4.getIntValue());
    }

    private JpegImageData getJpegRawImageData(ByteSource byteSource, TiffDirectory tiffDirectory) throws IOException, ImageReadException {
        TiffDirectory.ImageDataElement jpegRawImageDataElement = tiffDirectory.getJpegRawImageDataElement();
        long j = jpegRawImageDataElement.offset;
        int length = jpegRawImageDataElement.length;
        if (length + j > byteSource.getLength()) {
            length = (int) (byteSource.getLength() - j);
        }
        byte[] block = byteSource.getBlock(j, length);
        if (this.strict && (length < 2 || (((block[block.length - 2] & 255) << 8) | (block[block.length - 1] & 255)) != 65497)) {
            throw new ImageReadException("JPEG EOI marker could not be found at expected location");
        }
        return new JpegImageData(j, length, block);
    }
}
