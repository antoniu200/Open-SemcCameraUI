// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.jpeg.exif;

import org.apache.commons.imaging.formats.tiff.write.TiffImageWriterLossy;
import org.apache.commons.imaging.formats.tiff.write.TiffImageWriterLossless;
import org.apache.commons.imaging.common.bytesource.ByteSourceArray;
import org.apache.commons.imaging.common.bytesource.ByteSourceInputStream;
import java.io.InputStream;
import org.apache.commons.imaging.common.bytesource.ByteSourceFile;
import java.io.File;
import java.util.Iterator;
import org.apache.commons.imaging.util.IoUtils;
import java.io.Closeable;
import org.apache.commons.imaging.common.ByteConversions;
import org.apache.commons.imaging.ImageWriteException;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;
import org.apache.commons.imaging.formats.tiff.write.TiffImageWriterBase;
import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.formats.jpeg.JpegConstants;
import java.util.List;
import org.apache.commons.imaging.formats.jpeg.JpegUtils;
import java.util.ArrayList;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import java.nio.ByteOrder;
import org.apache.commons.imaging.common.BinaryFileParser;

public class ExifRewriter extends BinaryFileParser
{
    public ExifRewriter() {
        this(ByteOrder.BIG_ENDIAN);
    }
    
    public ExifRewriter(final ByteOrder byteOrder) {
        this.setByteOrder(byteOrder);
    }
    
    private JFIFPieces analyzeJFIF(final ByteSource byteSource) throws ImageReadException, IOException {
        final ArrayList list = new ArrayList();
        final ArrayList list2 = new ArrayList();
        new JpegUtils().traverseJFIF(byteSource, (JpegUtils.Visitor)new JpegUtils.Visitor(this, list, list2) {
            final ExifRewriter this$0;
            final List val$exifPieces;
            final List val$pieces;
            
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
                if (n != 65505) {
                    this.val$pieces.add(new JFIFPieceSegment(n, array, array2, array3));
                }
                else if (!BinaryFunctions.startsWith(array3, JpegConstants.EXIF_IDENTIFIER_CODE)) {
                    this.val$pieces.add(new JFIFPieceSegment(n, array, array2, array3));
                }
                else {
                    final JFIFPieceSegmentExif jfifPieceSegmentExif = new JFIFPieceSegmentExif(n, array, array2, array3);
                    this.val$pieces.add(jfifPieceSegmentExif);
                    this.val$exifPieces.add(jfifPieceSegmentExif);
                }
                return true;
            }
        });
        return new JFIFPieces(list, list2);
    }
    
    private byte[] writeExifSegment(final TiffImageWriterBase tiffImageWriterBase, final TiffOutputSet set, final boolean b) throws IOException, ImageWriteException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (b) {
            JpegConstants.EXIF_IDENTIFIER_CODE.writeTo(byteArrayOutputStream);
            byteArrayOutputStream.write(0);
            byteArrayOutputStream.write(0);
        }
        tiffImageWriterBase.write(byteArrayOutputStream, set);
        return byteArrayOutputStream.toByteArray();
    }
    
    private void writeSegmentsReplacingExif(final OutputStream outputStream, final List<JFIFPiece> list, final byte[] b) throws ImageWriteException, IOException {
        try {
            JpegConstants.SOI.writeTo(outputStream);
            final Iterator<JFIFPiece> iterator = list.iterator();
            boolean b2 = false;
            while (iterator.hasNext()) {
                if (iterator.next() instanceof JFIFPieceSegmentExif) {
                    b2 = true;
                }
            }
            if (!b2 && b != null) {
                final byte[] bytes = ByteConversions.toBytes((short)(-31), this.getByteOrder());
                if (b.length > 65535) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("APP1 Segment is too long: ");
                    sb.append(b.length);
                    throw new ExifOverflowException(sb.toString());
                }
                final byte[] bytes2 = ByteConversions.toBytes((short)(b.length + 2), this.getByteOrder());
                int n;
                if (((JFIFPieceSegment)list.get(0)).marker == 65504) {
                    n = 1;
                }
                else {
                    n = 0;
                }
                list.add(n, (JFIFPiece)new JFIFPieceSegmentExif(65505, bytes, bytes2, b));
            }
            final Iterator<JFIFPiece> iterator2 = list.iterator();
            int n2 = 0;
            while (iterator2.hasNext()) {
                final JFIFPiece jfifPiece = iterator2.next();
                if (jfifPiece instanceof JFIFPieceSegmentExif) {
                    if (n2 != 0) {
                        continue;
                    }
                    if (b != null) {
                        final byte[] bytes3 = ByteConversions.toBytes((short)(-31), this.getByteOrder());
                        if (b.length > 65535) {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("APP1 Segment is too long: ");
                            sb2.append(b.length);
                            throw new ExifOverflowException(sb2.toString());
                        }
                        final byte[] bytes4 = ByteConversions.toBytes((short)(b.length + 2), this.getByteOrder());
                        outputStream.write(bytes3);
                        outputStream.write(bytes4);
                        outputStream.write(b);
                    }
                    n2 = 1;
                }
                else {
                    jfifPiece.write(outputStream);
                }
            }
            IoUtils.closeQuietly(true, outputStream);
        }
        finally {
            IoUtils.closeQuietly(false, outputStream);
        }
    }
    
    public void removeExifMetadata(final File file, final OutputStream outputStream) throws ImageReadException, IOException, ImageWriteException {
        this.removeExifMetadata(new ByteSourceFile(file), outputStream);
    }
    
    public void removeExifMetadata(final InputStream inputStream, final OutputStream outputStream) throws ImageReadException, IOException, ImageWriteException {
        this.removeExifMetadata(new ByteSourceInputStream(inputStream, null), outputStream);
    }
    
    public void removeExifMetadata(final ByteSource byteSource, final OutputStream outputStream) throws ImageReadException, IOException, ImageWriteException {
        this.writeSegmentsReplacingExif(outputStream, this.analyzeJFIF(byteSource).pieces, null);
    }
    
    public void removeExifMetadata(final byte[] array, final OutputStream outputStream) throws ImageReadException, IOException, ImageWriteException {
        this.removeExifMetadata(new ByteSourceArray(array), outputStream);
    }
    
    public void updateExifMetadataLossless(final File file, final OutputStream outputStream, final TiffOutputSet set) throws ImageReadException, IOException, ImageWriteException {
        this.updateExifMetadataLossless(new ByteSourceFile(file), outputStream, set);
    }
    
    public void updateExifMetadataLossless(final InputStream inputStream, final OutputStream outputStream, final TiffOutputSet set) throws ImageReadException, IOException, ImageWriteException {
        this.updateExifMetadataLossless(new ByteSourceInputStream(inputStream, null), outputStream, set);
    }
    
    public void updateExifMetadataLossless(final ByteSource byteSource, final OutputStream outputStream, final TiffOutputSet set) throws ImageReadException, IOException, ImageWriteException {
        final JFIFPieces analyzeJFIF = this.analyzeJFIF(byteSource);
        final List<JFIFPiece> pieces = analyzeJFIF.pieces;
        TiffImageWriterBase tiffImageWriterBase;
        if (analyzeJFIF.exifPieces.size() > 0) {
            tiffImageWriterBase = new TiffImageWriterLossless(set.byteOrder, BinaryFunctions.remainingBytes("trimmed exif bytes", analyzeJFIF.exifPieces.get(0).segmentData, 6));
        }
        else {
            tiffImageWriterBase = new TiffImageWriterLossy(set.byteOrder);
        }
        this.writeSegmentsReplacingExif(outputStream, pieces, this.writeExifSegment(tiffImageWriterBase, set, true));
    }
    
    public void updateExifMetadataLossless(final byte[] array, final OutputStream outputStream, final TiffOutputSet set) throws ImageReadException, IOException, ImageWriteException {
        this.updateExifMetadataLossless(new ByteSourceArray(array), outputStream, set);
    }
    
    public void updateExifMetadataLossy(final File file, final OutputStream outputStream, final TiffOutputSet set) throws ImageReadException, IOException, ImageWriteException {
        this.updateExifMetadataLossy(new ByteSourceFile(file), outputStream, set);
    }
    
    public void updateExifMetadataLossy(final InputStream inputStream, final OutputStream outputStream, final TiffOutputSet set) throws ImageReadException, IOException, ImageWriteException {
        this.updateExifMetadataLossy(new ByteSourceInputStream(inputStream, null), outputStream, set);
    }
    
    public void updateExifMetadataLossy(final ByteSource byteSource, final OutputStream outputStream, final TiffOutputSet set) throws ImageReadException, IOException, ImageWriteException {
        this.writeSegmentsReplacingExif(outputStream, this.analyzeJFIF(byteSource).pieces, this.writeExifSegment(new TiffImageWriterLossy(set.byteOrder), set, true));
    }
    
    public void updateExifMetadataLossy(final byte[] array, final OutputStream outputStream, final TiffOutputSet set) throws ImageReadException, IOException, ImageWriteException {
        this.updateExifMetadataLossy(new ByteSourceArray(array), outputStream, set);
    }
    
    public static class ExifOverflowException extends ImageWriteException
    {
        private static final long serialVersionUID = 1401484357224931218L;
        
        public ExifOverflowException(final String s) {
            super(s);
        }
    }
    
    private abstract static class JFIFPiece
    {
        protected abstract void write(final OutputStream p0) throws IOException;
    }
    
    private static class JFIFPieceImageData extends JFIFPiece
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
    
    private static class JFIFPieceSegment extends JFIFPiece
    {
        public final int marker;
        public final byte[] markerBytes;
        public final byte[] markerLengthBytes;
        public final byte[] segmentData;
        
        public JFIFPieceSegment(final int marker, final byte[] markerBytes, final byte[] markerLengthBytes, final byte[] segmentData) {
            this.marker = marker;
            this.markerBytes = markerBytes;
            this.markerLengthBytes = markerLengthBytes;
            this.segmentData = segmentData;
        }
        
        @Override
        protected void write(final OutputStream outputStream) throws IOException {
            outputStream.write(this.markerBytes);
            outputStream.write(this.markerLengthBytes);
            outputStream.write(this.segmentData);
        }
    }
    
    private static class JFIFPieceSegmentExif extends JFIFPieceSegment
    {
        public JFIFPieceSegmentExif(final int n, final byte[] array, final byte[] array2, final byte[] array3) {
            super(n, array, array2, array3);
        }
    }
    
    private static class JFIFPieces
    {
        public final List<JFIFPiece> exifPieces;
        public final List<JFIFPiece> pieces;
        
        public JFIFPieces(final List<JFIFPiece> pieces, final List<JFIFPiece> exifPieces) {
            this.pieces = pieces;
            this.exifPieces = exifPieces;
        }
    }
}
