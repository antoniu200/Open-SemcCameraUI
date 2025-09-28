package org.apache.commons.imaging.formats.jpeg.exif;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.common.BinaryFileParser;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.common.ByteConversions;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.common.bytesource.ByteSourceArray;
import org.apache.commons.imaging.common.bytesource.ByteSourceFile;
import org.apache.commons.imaging.common.bytesource.ByteSourceInputStream;
import org.apache.commons.imaging.formats.jpeg.JpegConstants;
import org.apache.commons.imaging.formats.jpeg.JpegUtils;
import org.apache.commons.imaging.formats.tiff.write.TiffImageWriterBase;
import org.apache.commons.imaging.formats.tiff.write.TiffImageWriterLossless;
import org.apache.commons.imaging.formats.tiff.write.TiffImageWriterLossy;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;
import org.apache.commons.imaging.util.IoUtils;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ExifRewriter extends BinaryFileParser {
    public ExifRewriter() {
        this(ByteOrder.BIG_ENDIAN);
    }

    public ExifRewriter(ByteOrder byteOrder) {
        setByteOrder(byteOrder);
    }

    private static class JFIFPieces {
        public final List<JFIFPiece> exifPieces;
        public final List<JFIFPiece> pieces;

        public JFIFPieces(List<JFIFPiece> list, List<JFIFPiece> list2) {
            this.pieces = list;
            this.exifPieces = list2;
        }
    }

    private static abstract class JFIFPiece {
        protected abstract void write(OutputStream outputStream) throws IOException;

        private JFIFPiece() {
        }
    }

    private static class JFIFPieceSegment extends JFIFPiece {
        public final int marker;
        public final byte[] markerBytes;
        public final byte[] markerLengthBytes;
        public final byte[] segmentData;

        public JFIFPieceSegment(int i, byte[] bArr, byte[] bArr2, byte[] bArr3) {
            super();
            this.marker = i;
            this.markerBytes = bArr;
            this.markerLengthBytes = bArr2;
            this.segmentData = bArr3;
        }

        @Override // org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter.JFIFPiece
        protected void write(OutputStream outputStream) throws IOException {
            outputStream.write(this.markerBytes);
            outputStream.write(this.markerLengthBytes);
            outputStream.write(this.segmentData);
        }
    }

    private static class JFIFPieceSegmentExif extends JFIFPieceSegment {
        public JFIFPieceSegmentExif(int i, byte[] bArr, byte[] bArr2, byte[] bArr3) {
            super(i, bArr, bArr2, bArr3);
        }
    }

    private static class JFIFPieceImageData extends JFIFPiece {
        public final byte[] imageData;
        public final byte[] markerBytes;

        public JFIFPieceImageData(byte[] bArr, byte[] bArr2) {
            super();
            this.markerBytes = bArr;
            this.imageData = bArr2;
        }

        @Override // org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter.JFIFPiece
        protected void write(OutputStream outputStream) throws IOException {
            outputStream.write(this.markerBytes);
            outputStream.write(this.imageData);
        }
    }

    private JFIFPieces analyzeJFIF(ByteSource byteSource) throws Throwable {
        final ArrayList arrayList = new ArrayList();
        final ArrayList arrayList2 = new ArrayList();
        new JpegUtils().traverseJFIF(byteSource, new JpegUtils.Visitor() { // from class: org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter.1
            @Override // org.apache.commons.imaging.formats.jpeg.JpegUtils.Visitor
            public boolean beginSOS() {
                return true;
            }

            @Override // org.apache.commons.imaging.formats.jpeg.JpegUtils.Visitor
            public void visitSOS(int i, byte[] bArr, byte[] bArr2) {
                arrayList.add(new JFIFPieceImageData(bArr, bArr2));
            }

            @Override // org.apache.commons.imaging.formats.jpeg.JpegUtils.Visitor
            public boolean visitSegment(int i, byte[] bArr, int i2, byte[] bArr2, byte[] bArr3) throws IOException, ImageReadException {
                if (i != 65505) {
                    arrayList.add(new JFIFPieceSegment(i, bArr, bArr2, bArr3));
                    return true;
                }
                if (!BinaryFunctions.startsWith(bArr3, JpegConstants.EXIF_IDENTIFIER_CODE)) {
                    arrayList.add(new JFIFPieceSegment(i, bArr, bArr2, bArr3));
                    return true;
                }
                JFIFPieceSegmentExif jFIFPieceSegmentExif = new JFIFPieceSegmentExif(i, bArr, bArr2, bArr3);
                arrayList.add(jFIFPieceSegmentExif);
                arrayList2.add(jFIFPieceSegmentExif);
                return true;
            }
        });
        return new JFIFPieces(arrayList, arrayList2);
    }

    public void removeExifMetadata(File file, OutputStream outputStream) throws ImageWriteException, IOException, ImageReadException {
        removeExifMetadata(new ByteSourceFile(file), outputStream);
    }

    public void removeExifMetadata(byte[] bArr, OutputStream outputStream) throws ImageWriteException, IOException, ImageReadException {
        removeExifMetadata(new ByteSourceArray(bArr), outputStream);
    }

    public void removeExifMetadata(InputStream inputStream, OutputStream outputStream) throws ImageWriteException, IOException, ImageReadException {
        removeExifMetadata(new ByteSourceInputStream(inputStream, null), outputStream);
    }

    public void removeExifMetadata(ByteSource byteSource, OutputStream outputStream) throws ImageWriteException, IOException, ImageReadException {
        writeSegmentsReplacingExif(outputStream, analyzeJFIF(byteSource).pieces, null);
    }

    public void updateExifMetadataLossless(File file, OutputStream outputStream, TiffOutputSet tiffOutputSet) throws ImageWriteException, IOException, ImageReadException {
        updateExifMetadataLossless(new ByteSourceFile(file), outputStream, tiffOutputSet);
    }

    public void updateExifMetadataLossless(byte[] bArr, OutputStream outputStream, TiffOutputSet tiffOutputSet) throws ImageWriteException, IOException, ImageReadException {
        updateExifMetadataLossless(new ByteSourceArray(bArr), outputStream, tiffOutputSet);
    }

    public void updateExifMetadataLossless(InputStream inputStream, OutputStream outputStream, TiffOutputSet tiffOutputSet) throws ImageWriteException, IOException, ImageReadException {
        updateExifMetadataLossless(new ByteSourceInputStream(inputStream, null), outputStream, tiffOutputSet);
    }

    public void updateExifMetadataLossless(ByteSource byteSource, OutputStream outputStream, TiffOutputSet tiffOutputSet) throws ImageWriteException, IOException, ImageReadException {
        TiffImageWriterBase tiffImageWriterLossy;
        JFIFPieces jFIFPiecesAnalyzeJFIF = analyzeJFIF(byteSource);
        List<JFIFPiece> list = jFIFPiecesAnalyzeJFIF.pieces;
        if (jFIFPiecesAnalyzeJFIF.exifPieces.size() > 0) {
            tiffImageWriterLossy = new TiffImageWriterLossless(tiffOutputSet.byteOrder, BinaryFunctions.remainingBytes("trimmed exif bytes", ((JFIFPieceSegment) jFIFPiecesAnalyzeJFIF.exifPieces.get(0)).segmentData, 6));
        } else {
            tiffImageWriterLossy = new TiffImageWriterLossy(tiffOutputSet.byteOrder);
        }
        writeSegmentsReplacingExif(outputStream, list, writeExifSegment(tiffImageWriterLossy, tiffOutputSet, true));
    }

    public void updateExifMetadataLossy(byte[] bArr, OutputStream outputStream, TiffOutputSet tiffOutputSet) throws ImageWriteException, IOException, ImageReadException {
        updateExifMetadataLossy(new ByteSourceArray(bArr), outputStream, tiffOutputSet);
    }

    public void updateExifMetadataLossy(InputStream inputStream, OutputStream outputStream, TiffOutputSet tiffOutputSet) throws ImageWriteException, IOException, ImageReadException {
        updateExifMetadataLossy(new ByteSourceInputStream(inputStream, null), outputStream, tiffOutputSet);
    }

    public void updateExifMetadataLossy(File file, OutputStream outputStream, TiffOutputSet tiffOutputSet) throws ImageWriteException, IOException, ImageReadException {
        updateExifMetadataLossy(new ByteSourceFile(file), outputStream, tiffOutputSet);
    }

    public void updateExifMetadataLossy(ByteSource byteSource, OutputStream outputStream, TiffOutputSet tiffOutputSet) throws ImageWriteException, IOException, ImageReadException {
        writeSegmentsReplacingExif(outputStream, analyzeJFIF(byteSource).pieces, writeExifSegment(new TiffImageWriterLossy(tiffOutputSet.byteOrder), tiffOutputSet, true));
    }

    private void writeSegmentsReplacingExif(OutputStream outputStream, List<JFIFPiece> list, byte[] bArr) throws ImageWriteException, IOException {
        try {
            JpegConstants.SOI.writeTo(outputStream);
            Iterator<JFIFPiece> it = list.iterator();
            boolean z = false;
            while (it.hasNext()) {
                if (it.next() instanceof JFIFPieceSegmentExif) {
                    z = true;
                }
            }
            if (!z && bArr != null) {
                byte[] bytes = ByteConversions.toBytes((short) -31, getByteOrder());
                if (bArr.length > 65535) {
                    throw new ExifOverflowException("APP1 Segment is too long: " + bArr.length);
                }
                list.add(((JFIFPieceSegment) list.get(0)).marker == 65504 ? 1 : 0, new JFIFPieceSegmentExif(JpegConstants.JPEG_APP1_MARKER, bytes, ByteConversions.toBytes((short) (bArr.length + 2), getByteOrder()), bArr));
            }
            boolean z2 = false;
            for (JFIFPiece jFIFPiece : list) {
                if (!(jFIFPiece instanceof JFIFPieceSegmentExif)) {
                    jFIFPiece.write(outputStream);
                } else if (!z2) {
                    if (bArr != null) {
                        byte[] bytes2 = ByteConversions.toBytes((short) -31, getByteOrder());
                        if (bArr.length > 65535) {
                            throw new ExifOverflowException("APP1 Segment is too long: " + bArr.length);
                        }
                        byte[] bytes3 = ByteConversions.toBytes((short) (bArr.length + 2), getByteOrder());
                        outputStream.write(bytes2);
                        outputStream.write(bytes3);
                        outputStream.write(bArr);
                    }
                    z2 = true;
                }
            }
            IoUtils.closeQuietly(true, outputStream);
        } catch (Throwable th) {
            IoUtils.closeQuietly(false, outputStream);
            throw th;
        }
    }

    public static class ExifOverflowException extends ImageWriteException {
        private static final long serialVersionUID = 1401484357224931218L;

        public ExifOverflowException(String str) {
            super(str);
        }
    }

    private byte[] writeExifSegment(TiffImageWriterBase tiffImageWriterBase, TiffOutputSet tiffOutputSet, boolean z) throws ImageWriteException, IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (z) {
            JpegConstants.EXIF_IDENTIFIER_CODE.writeTo(byteArrayOutputStream);
            byteArrayOutputStream.write(0);
            byteArrayOutputStream.write(0);
        }
        tiffImageWriterBase.write(byteArrayOutputStream, tiffOutputSet);
        return byteArrayOutputStream.toByteArray();
    }
}
