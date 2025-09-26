// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.jpeg.xmp;

import org.apache.commons.imaging.formats.jpeg.iptc.IptcParser;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.common.ByteConversions;
import org.apache.commons.imaging.util.IoUtils;
import java.io.Closeable;
import org.apache.commons.imaging.formats.jpeg.JpegConstants;
import java.io.OutputStream;
import org.apache.commons.imaging.ImageWriteException;
import java.util.Collection;
import java.util.Iterator;
import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import java.util.List;
import org.apache.commons.imaging.formats.jpeg.JpegUtils;
import java.util.ArrayList;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import java.nio.ByteOrder;
import org.apache.commons.imaging.common.BinaryFileParser;

public class JpegRewriter extends BinaryFileParser
{
    private static final SegmentFilter EXIF_SEGMENT_FILTER;
    private static final ByteOrder JPEG_BYTE_ORDER;
    private static final SegmentFilter PHOTOSHOP_APP13_SEGMENT_FILTER;
    private static final SegmentFilter XMP_SEGMENT_FILTER;
    
    static {
        JPEG_BYTE_ORDER = ByteOrder.BIG_ENDIAN;
        EXIF_SEGMENT_FILTER = (SegmentFilter)new SegmentFilter() {
            @Override
            public boolean filter(final JFIFPieceSegment jfifPieceSegment) {
                return jfifPieceSegment.isExifSegment();
            }
        };
        XMP_SEGMENT_FILTER = (SegmentFilter)new SegmentFilter() {
            @Override
            public boolean filter(final JFIFPieceSegment jfifPieceSegment) {
                return jfifPieceSegment.isXmpSegment();
            }
        };
        PHOTOSHOP_APP13_SEGMENT_FILTER = (SegmentFilter)new SegmentFilter() {
            @Override
            public boolean filter(final JFIFPieceSegment jfifPieceSegment) {
                return jfifPieceSegment.isPhotoshopApp13Segment();
            }
        };
    }
    
    public JpegRewriter() {
        this.setByteOrder(JpegRewriter.JPEG_BYTE_ORDER);
    }
    
    protected JFIFPieces analyzeJFIF(final ByteSource byteSource) throws ImageReadException, IOException {
        final ArrayList list = new ArrayList();
        final ArrayList list2 = new ArrayList();
        new JpegUtils().traverseJFIF(byteSource, (JpegUtils.Visitor)new JpegUtils.Visitor(this, list, list2) {
            final JpegRewriter this$0;
            final List val$pieces;
            final List val$segmentPieces;
            
            @Override
            public boolean beginSOS() {
                return true;
            }
            
            @Override
            public void visitSOS(final int n, final byte[] array, final byte[] array2) {
                this.val$pieces.add(new JFIFPieceImageData(array, array2));
            }
            
            @Override
            public boolean visitSegment(final int n, final byte[] array, final int n2, final byte[] array2, final byte[] array3) throws ImageReadException, IOException {
                final JFIFPieceSegment jfifPieceSegment = new JFIFPieceSegment(n, array, array2, array3);
                this.val$pieces.add(jfifPieceSegment);
                this.val$segmentPieces.add(jfifPieceSegment);
                return true;
            }
        });
        return new JFIFPieces(list, list2);
    }
    
    protected <T extends JFIFPiece> List<T> filterSegments(final List<T> list, final SegmentFilter segmentFilter) {
        return this.filterSegments(list, segmentFilter, false);
    }
    
    protected <T extends JFIFPiece> List<T> filterSegments(final List<T> list, final SegmentFilter segmentFilter, final boolean b) {
        final ArrayList list2 = new ArrayList();
        for (final JFIFPiece jfifPiece : list) {
            if (jfifPiece instanceof JFIFPieceSegment) {
                if (!(segmentFilter.filter((JFIFPieceSegment)jfifPiece) ^ (b ^ true))) {
                    continue;
                }
                list2.add(jfifPiece);
            }
            else {
                if (b) {
                    continue;
                }
                list2.add(jfifPiece);
            }
        }
        return list2;
    }
    
    protected <T extends JFIFPiece> List<T> findPhotoshopApp13Segments(final List<T> list) {
        return this.filterSegments(list, JpegRewriter.PHOTOSHOP_APP13_SEGMENT_FILTER, true);
    }
    
    protected <T extends JFIFPiece, U extends JFIFPiece> List<JFIFPiece> insertAfterLastAppSegments(final List<T> c, final List<U> list) throws ImageWriteException {
        int i = 0;
        int n = -1;
        while (i < c.size()) {
            final JFIFPiece jfifPiece = c.get(i);
            if (jfifPiece instanceof JFIFPieceSegment) {
                if (((JFIFPieceSegment)jfifPiece).isAppSegment()) {
                    n = i;
                }
            }
            ++i;
        }
        final ArrayList list2 = new ArrayList(c);
        if (n == -1) {
            if (c.size() < 1) {
                throw new ImageWriteException("JPEG file has no APP segments.");
            }
            list2.addAll(1, (Collection)list);
        }
        else {
            list2.addAll(n + 1, (Collection)list);
        }
        return (List<JFIFPiece>)list2;
    }
    
    protected <T extends JFIFPiece, U extends JFIFPiece> List<JFIFPiece> insertBeforeFirstAppSegments(final List<T> c, final List<U> list) throws ImageWriteException {
        int i = 0;
        int n = -1;
        while (i < c.size()) {
            final JFIFPiece jfifPiece = c.get(i);
            int n2;
            if (!(jfifPiece instanceof JFIFPieceSegment)) {
                n2 = n;
            }
            else {
                n2 = n;
                if (((JFIFPieceSegment)jfifPiece).isAppSegment() && (n2 = n) == -1) {
                    n2 = i;
                }
            }
            ++i;
            n = n2;
        }
        final ArrayList list2 = new ArrayList(c);
        if (n == -1) {
            throw new ImageWriteException("JPEG file has no APP segments.");
        }
        list2.addAll(n, (Collection)list);
        return (List<JFIFPiece>)list2;
    }
    
    protected <T extends JFIFPiece> List<T> removeExifSegments(final List<T> list) {
        return this.filterSegments(list, JpegRewriter.EXIF_SEGMENT_FILTER);
    }
    
    protected <T extends JFIFPiece> List<T> removePhotoshopApp13Segments(final List<T> list) {
        return this.filterSegments(list, JpegRewriter.PHOTOSHOP_APP13_SEGMENT_FILTER);
    }
    
    protected <T extends JFIFPiece> List<T> removeXmpSegments(final List<T> list) {
        return this.filterSegments(list, JpegRewriter.XMP_SEGMENT_FILTER);
    }
    
    protected void writeSegments(final OutputStream outputStream, final List<? extends JFIFPiece> list) throws IOException {
        try {
            JpegConstants.SOI.writeTo(outputStream);
            final Iterator<? extends JFIFPiece> iterator = list.iterator();
            while (iterator.hasNext()) {
                ((JFIFPiece)iterator.next()).write(outputStream);
            }
            IoUtils.closeQuietly(true, outputStream);
        }
        finally {
            IoUtils.closeQuietly(false, outputStream);
        }
    }
    
    protected abstract static class JFIFPiece
    {
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("[");
            sb.append(this.getClass().getName());
            sb.append("]");
            return sb.toString();
        }
        
        protected abstract void write(final OutputStream p0) throws IOException;
    }
    
    protected static class JFIFPieceImageData extends JFIFPiece
    {
        public final byte[] imageData;
        public final byte[] markerBytes;
        
        public JFIFPieceImageData(final byte[] markerBytes, final byte[] imageData) {
            this.markerBytes = markerBytes;
            this.imageData = imageData;
        }
        
        @Override
        protected void write(final OutputStream outputStream) throws IOException {
            outputStream.write(this.markerBytes);
            outputStream.write(this.imageData);
        }
    }
    
    protected static class JFIFPieceSegment extends JFIFPiece
    {
        public final int marker;
        public final byte[] markerBytes;
        public final byte[] segmentData;
        public final byte[] segmentLengthBytes;
        
        public JFIFPieceSegment(final int n, final byte[] array) {
            this(n, ByteConversions.toBytes((short)n, JpegRewriter.JPEG_BYTE_ORDER), ByteConversions.toBytes((short)(array.length + 2), JpegRewriter.JPEG_BYTE_ORDER), array);
        }
        
        public JFIFPieceSegment(final int marker, final byte[] markerBytes, final byte[] segmentLengthBytes, final byte[] segmentData) {
            this.marker = marker;
            this.markerBytes = markerBytes;
            this.segmentLengthBytes = segmentLengthBytes;
            this.segmentData = segmentData;
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
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("[");
            sb.append(this.getClass().getName());
            sb.append(" (0x");
            sb.append(Integer.toHexString(this.marker));
            sb.append(")]");
            return sb.toString();
        }
        
        @Override
        protected void write(final OutputStream outputStream) throws IOException {
            outputStream.write(this.markerBytes);
            outputStream.write(this.segmentLengthBytes);
            outputStream.write(this.segmentData);
        }
    }
    
    protected static class JFIFPieces
    {
        public final List<JFIFPiece> pieces;
        public final List<JFIFPiece> segmentPieces;
        
        public JFIFPieces(final List<JFIFPiece> pieces, final List<JFIFPiece> segmentPieces) {
            this.pieces = pieces;
            this.segmentPieces = segmentPieces;
        }
    }
    
    public static class JpegSegmentOverflowException extends ImageWriteException
    {
        private static final long serialVersionUID = -1062145751550646846L;
        
        public JpegSegmentOverflowException(final String s) {
            super(s);
        }
    }
    
    private interface SegmentFilter
    {
        boolean filter(final JFIFPieceSegment p0);
    }
}
