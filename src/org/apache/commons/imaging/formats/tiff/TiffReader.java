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

    private boolean readDirectory(final ByteSource byteSource, final long offset,
            final int dirType, final FormatCompliance formatCompliance, final Listener listener,
            final List<Number> visited) throws ImageReadException, IOException {
        final boolean ignoreNextDirectory = false;
        return readDirectory(byteSource, offset, dirType, formatCompliance,
                listener, ignoreNextDirectory, visited);
    }

    private boolean readDirectory(final ByteSource byteSource, final long directoryOffset,
            final int dirType, final FormatCompliance formatCompliance, final Listener listener,
            final boolean ignoreNextDirectory, final List<Number> visited)
            throws ImageReadException, IOException {

        if (visited.contains(directoryOffset)) {
            return false;
        }
        visited.add(directoryOffset);

        InputStream is = null;
        boolean canThrow = false;
        try {
            if (directoryOffset >= byteSource.getLength()) {
                canThrow = true;
                return true;
            }

            is = byteSource.getInputStream();
            skipBytes(is, directoryOffset);

            final List<TiffField> fields = new ArrayList<TiffField>();

            int entryCount;
            try {
                entryCount = read2Bytes("DirectoryEntryCount", is,
                        "Not a Valid TIFF File");
            } catch (final IOException e) {
                if (strict) {
                    throw e;
                } else {
                    canThrow = true;
                    return true;
                }
            }

            for (int i = 0; i < entryCount; i++) {
                final int tag = read2Bytes("Tag", is, "Not a Valid TIFF File");
                final int type = read2Bytes("Type", is, "Not a Valid TIFF File");
                final long count = 0xFFFFffffL & read4Bytes("Count", is, "Not a Valid TIFF File");
                final byte offsetBytes[] = readBytes("Offset", is, 4,
                        "Not a Valid TIFF File");
                final long offset = 0xFFFFffffL & toInt(offsetBytes);

                if (tag == 0) {
                    // skip invalid fields.
                    // These are seen very rarely, but can have invalid value
                    // lengths,
                    // which can cause OOM problems.
                    continue;
                }
                
                final FieldType fieldType;
                try {
                    fieldType = FieldType.getFieldType(type);
                } catch (final ImageReadException imageReadEx) {
                    // skip over unknown fields types, since we
                    // can't calculate their size without
                    // knowing their type
                    continue;
                }
                final long valueLength = count * fieldType.getSize();
                final byte[] value;
                if (valueLength > TIFF_ENTRY_MAX_VALUE_LENGTH) {
                    if ((offset < 0) ||
                            (offset + valueLength) > byteSource.getLength()) {
                        if (strict) {
                            throw new IOException(
                                    "Attempt to read byte range starting from " + offset + " " +
                                            "of length " + valueLength + " " +
                                            "which is outside the file's size of " +
                                            byteSource.getLength());
                        } else {
                            // corrupt field, ignore it
                            continue;
                        }
                    }
                    value = byteSource.getBlock(offset, (int)valueLength);
                } else {
                    value = offsetBytes;
                }

                final TiffField field = new TiffField(tag, dirType, fieldType, count,
                        offset, value, getByteOrder(), i);

                fields.add(field);

                if (!listener.addField(field)) {
                    canThrow = true;
                    return true;
                }
            }

            final long nextDirectoryOffset = 0xFFFFffffL & read4Bytes("nextDirectoryOffset", is,
                    "Not a Valid TIFF File");

            final TiffDirectory directory = new TiffDirectory(dirType, fields,
                    directoryOffset, nextDirectoryOffset);

            if (listener.readImageData()) {
                if (directory.hasTiffImageData()) {
                    final TiffImageData rawImageData = getTiffRawImageData(
                            byteSource, directory);
                    directory.setTiffImageData(rawImageData);
                }
                if (directory.hasJpegImageData()) {
                    final JpegImageData rawJpegImageData = getJpegRawImageData(
                            byteSource, directory);
                    directory.setJpegImageData(rawJpegImageData);
                }
            }

            if (!listener.addDirectory(directory)) {
                canThrow = true;
                return true;
            }

            if (listener.readOffsetDirectories()) {
                final TagInfoLong[] offsetFields = {
                        EXIF_TAG_EXIF_OFFSET,
                        EXIF_TAG_GPSINFO,
                        EXIF_TAG_INTEROP_OFFSET
                };
                final int[] directoryTypes = {
                        TiffDirectoryConstants.DIRECTORY_TYPE_EXIF,
                        TiffDirectoryConstants.DIRECTORY_TYPE_GPS,
                        TiffDirectoryConstants.DIRECTORY_TYPE_INTEROPERABILITY
                };
                for (int i = 0; i < offsetFields.length; i++) {
                    final TagInfoLong offsetField = offsetFields[i];
                    final TiffField field = directory.findField(offsetField);
                    if (field != null) {
                        long subDirectoryOffset;
                        int subDirectoryType;
                        boolean subDirectoryRead = false;
                        try {
                            subDirectoryOffset = directory.getSingleFieldValue(offsetField);
                            subDirectoryType = directoryTypes[i];
                            subDirectoryRead = readDirectory(byteSource,
                                    subDirectoryOffset, subDirectoryType,
                                    formatCompliance, listener, true, visited);
    
                        } catch (final ImageReadException imageReadException) {
                            if (strict) {
                                throw imageReadException;
                            }
                        }
                        if (!subDirectoryRead) {
                            fields.remove(field);
                        }
                    }
                }
            }

            if (!ignoreNextDirectory && directory.nextDirectoryOffset > 0) {
                // Debug.debug("next dir", directory.nextDirectoryOffset );
                readDirectory(byteSource, directory.nextDirectoryOffset,
                        dirType + 1, formatCompliance, listener, visited);
            }

            canThrow = true;
            return true;
        } finally {
            IoUtils.closeQuietly(canThrow, is);
        }
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
