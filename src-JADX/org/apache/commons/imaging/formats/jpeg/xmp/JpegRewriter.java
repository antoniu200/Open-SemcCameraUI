package org.apache.commons.imaging.formats.jpeg.xmp;

import java.io.IOException;
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
import org.apache.commons.imaging.formats.jpeg.JpegConstants;
import org.apache.commons.imaging.formats.jpeg.JpegUtils;
import org.apache.commons.imaging.formats.jpeg.iptc.IptcParser;
import org.apache.commons.imaging.util.IoUtils;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class JpegRewriter extends BinaryFileParser {
    private static final ByteOrder JPEG_BYTE_ORDER = ByteOrder.BIG_ENDIAN;
    private static final SegmentFilter EXIF_SEGMENT_FILTER = new SegmentFilter() { // from class: org.apache.commons.imaging.formats.jpeg.xmp.JpegRewriter.1
        @Override // org.apache.commons.imaging.formats.jpeg.xmp.JpegRewriter.SegmentFilter
        public boolean filter(JFIFPieceSegment jFIFPieceSegment) {
            return jFIFPieceSegment.isExifSegment();
        }
    };
    private static final SegmentFilter XMP_SEGMENT_FILTER = new SegmentFilter() { // from class: org.apache.commons.imaging.formats.jpeg.xmp.JpegRewriter.2
        @Override // org.apache.commons.imaging.formats.jpeg.xmp.JpegRewriter.SegmentFilter
        public boolean filter(JFIFPieceSegment jFIFPieceSegment) {
            return jFIFPieceSegment.isXmpSegment();
        }
    };
    private static final SegmentFilter PHOTOSHOP_APP13_SEGMENT_FILTER = new SegmentFilter() { // from class: org.apache.commons.imaging.formats.jpeg.xmp.JpegRewriter.3
        @Override // org.apache.commons.imaging.formats.jpeg.xmp.JpegRewriter.SegmentFilter
        public boolean filter(JFIFPieceSegment jFIFPieceSegment) {
            return jFIFPieceSegment.isPhotoshopApp13Segment();
        }
    };

    private interface SegmentFilter {
        boolean filter(JFIFPieceSegment jFIFPieceSegment);
    }

    public JpegRewriter() {
        setByteOrder(JPEG_BYTE_ORDER);
    }

    protected static class JFIFPieces {
        public final List<JFIFPiece> pieces;
        public final List<JFIFPiece> segmentPieces;

        public JFIFPieces(List<JFIFPiece> list, List<JFIFPiece> list2) {
            this.pieces = list;
            this.segmentPieces = list2;
        }
    }

    protected static abstract class JFIFPiece {
        protected abstract void write(OutputStream outputStream) throws IOException;

        protected JFIFPiece() {
        }

        public String toString() {
            return "[" + getClass().getName() + "]";
        }
    }

    protected static class JFIFPieceSegment extends JFIFPiece {
        public final int marker;
        public final byte[] markerBytes;
        public final byte[] segmentData;
        public final byte[] segmentLengthBytes;

        public JFIFPieceSegment(int i, byte[] bArr) {
            this(i, ByteConversions.toBytes((short) i, JpegRewriter.JPEG_BYTE_ORDER), ByteConversions.toBytes((short) (bArr.length + 2), JpegRewriter.JPEG_BYTE_ORDER), bArr);
        }

        public JFIFPieceSegment(int i, byte[] bArr, byte[] bArr2, byte[] bArr3) {
            this.marker = i;
            this.markerBytes = bArr;
            this.segmentLengthBytes = bArr2;
            this.segmentData = bArr3;
        }

        @Override // org.apache.commons.imaging.formats.jpeg.xmp.JpegRewriter.JFIFPiece
        public String toString() {
            return "[" + getClass().getName() + " (0x" + Integer.toHexString(this.marker) + ")]";
        }

        @Override // org.apache.commons.imaging.formats.jpeg.xmp.JpegRewriter.JFIFPiece
        protected void write(OutputStream outputStream) throws IOException {
            outputStream.write(this.markerBytes);
            outputStream.write(this.segmentLengthBytes);
            outputStream.write(this.segmentData);
        }

        public boolean isApp1Segment() {
            return this.marker == 65505;
        }

        public boolean isAppSegment() {
            return this.marker >= 65504 && this.marker <= 65519;
        }

        public boolean isExifSegment() {
            return this.marker == 65505 && BinaryFunctions.startsWith(this.segmentData, JpegConstants.EXIF_IDENTIFIER_CODE);
        }

        public boolean isPhotoshopApp13Segment() {
            return this.marker == 65517 && new IptcParser().isPhotoshopJpegSegment(this.segmentData);
        }

        public boolean isXmpSegment() {
            return this.marker == 65505 && BinaryFunctions.startsWith(this.segmentData, JpegConstants.XMP_IDENTIFIER);
        }
    }

    protected static class JFIFPieceImageData extends JFIFPiece {
        public final byte[] imageData;
        public final byte[] markerBytes;

        public JFIFPieceImageData(byte[] bArr, byte[] bArr2) {
            this.markerBytes = bArr;
            this.imageData = bArr2;
        }

        @Override // org.apache.commons.imaging.formats.jpeg.xmp.JpegRewriter.JFIFPiece
        protected void write(OutputStream outputStream) throws IOException {
            outputStream.write(this.markerBytes);
            outputStream.write(this.imageData);
        }
    }

    protected JFIFPieces analyzeJFIF(ByteSource byteSource) throws Throwable {
        final ArrayList arrayList = new ArrayList();
        final ArrayList arrayList2 = new ArrayList();
        new JpegUtils().traverseJFIF(byteSource, new JpegUtils.Visitor() { // from class: org.apache.commons.imaging.formats.jpeg.xmp.JpegRewriter.4
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
                JFIFPieceSegment jFIFPieceSegment = new JFIFPieceSegment(i, bArr, bArr2, bArr3);
                arrayList.add(jFIFPieceSegment);
                arrayList2.add(jFIFPieceSegment);
                return true;
            }
        });
        return new JFIFPieces(arrayList, arrayList2);
    }

    protected <T extends JFIFPiece> List<T> removeXmpSegments(List<T> list) {
        return filterSegments(list, XMP_SEGMENT_FILTER);
    }

    protected <T extends JFIFPiece> List<T> removePhotoshopApp13Segments(List<T> list) {
        return filterSegments(list, PHOTOSHOP_APP13_SEGMENT_FILTER);
    }

    protected <T extends JFIFPiece> List<T> findPhotoshopApp13Segments(List<T> list) {
        return filterSegments(list, PHOTOSHOP_APP13_SEGMENT_FILTER, true);
    }

    protected <T extends JFIFPiece> List<T> removeExifSegments(List<T> list) {
        return filterSegments(list, EXIF_SEGMENT_FILTER);
    }

    protected <T extends JFIFPiece> List<T> filterSegments(List<T> list, SegmentFilter segmentFilter) {
        return filterSegments(list, segmentFilter, false);
    }

    protected <T extends JFIFPiece> List<T> filterSegments(List<T> list, SegmentFilter segmentFilter, boolean z) {
        ArrayList arrayList = new ArrayList();
        for (T t : list) {
            if (t instanceof JFIFPieceSegment) {
                if (segmentFilter.filter((JFIFPieceSegment) t) ^ (!z)) {
                    arrayList.add(t);
                }
            } else if (!z) {
                arrayList.add(t);
            }
        }
        return arrayList;
    }

    protected <T extends JFIFPiece, U extends JFIFPiece> List<JFIFPiece> insertBeforeFirstAppSegments(List<T> list, List<U> list2) throws ImageWriteException {
        int i = -1;
        for (int i2 = 0; i2 < list.size(); i2++) {
            T t = list.get(i2);
            if ((t instanceof JFIFPieceSegment) && ((JFIFPieceSegment) t).isAppSegment() && i == -1) {
                i = i2;
            }
        }
        ArrayList arrayList = new ArrayList(list);
        if (i == -1) {
            throw new ImageWriteException("JPEG file has no APP segments.");
        }
        arrayList.addAll(i, list2);
        return arrayList;
    }

    protected <T extends JFIFPiece, U extends JFIFPiece> List<JFIFPiece> insertAfterLastAppSegments(List<T> list, List<U> list2) throws ImageWriteException {
        int i = -1;
        for (int i2 = 0; i2 < list.size(); i2++) {
            T t = list.get(i2);
            if ((t instanceof JFIFPieceSegment) && ((JFIFPieceSegment) t).isAppSegment()) {
                i = i2;
            }
        }
        ArrayList arrayList = new ArrayList(list);
        if (i == -1) {
            if (list.size() < 1) {
                throw new ImageWriteException("JPEG file has no APP segments.");
            }
            arrayList.addAll(1, list2);
        } else {
            arrayList.addAll(i + 1, list2);
        }
        return arrayList;
    }

    protected void writeSegments(OutputStream outputStream, List<? extends JFIFPiece> list) throws IOException {
        try {
            JpegConstants.SOI.writeTo(outputStream);
            Iterator<? extends JFIFPiece> it = list.iterator();
            while (it.hasNext()) {
                it.next().write(outputStream);
            }
            IoUtils.closeQuietly(true, outputStream);
        } catch (Throwable th) {
            IoUtils.closeQuietly(false, outputStream);
            throw th;
        }
    }

    public static class JpegSegmentOverflowException extends ImageWriteException {
        private static final long serialVersionUID = -1062145751550646846L;

        public JpegSegmentOverflowException(String str) {
            super(str);
        }
    }
}
