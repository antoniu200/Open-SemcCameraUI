// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.jpeg.iptc;

import java.util.Arrays;
import org.apache.commons.imaging.common.bytesource.ByteSourceArray;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import org.apache.commons.imaging.common.bytesource.ByteSourceInputStream;
import java.io.InputStream;
import org.apache.commons.imaging.ImageWriteException;
import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.common.bytesource.ByteSourceFile;
import java.io.OutputStream;
import java.io.File;
import org.apache.commons.imaging.formats.jpeg.xmp.JpegRewriter;

public class JpegIptcRewriter extends JpegRewriter
{
    public void removeIPTC(final File file, final OutputStream outputStream) throws ImageReadException, IOException, ImageWriteException {
        this.removeIPTC(new ByteSourceFile(file), outputStream);
    }
    
    public void removeIPTC(final InputStream inputStream, final OutputStream outputStream) throws ImageReadException, IOException, ImageWriteException {
        this.removeIPTC(new ByteSourceInputStream(inputStream, null), outputStream);
    }
    
    public void removeIPTC(final ByteSource byteSource, final OutputStream outputStream) throws ImageReadException, IOException, ImageWriteException {
        final List<JFIFPiece> pieces = this.analyzeJFIF(byteSource).pieces;
        final List<JFIFPiece> photoshopApp13Segments = this.findPhotoshopApp13Segments(pieces);
        if (photoshopApp13Segments.size() > 1) {
            throw new ImageReadException("Image contains more than one Photoshop App13 segment.");
        }
        final List<JFIFPiece> removePhotoshopApp13Segments = this.removePhotoshopApp13Segments(pieces);
        if (photoshopApp13Segments.size() == 1) {
            final JFIFPieceSegment jfifPieceSegment = (JFIFPieceSegment)photoshopApp13Segments.get(0);
            removePhotoshopApp13Segments.add(pieces.indexOf(jfifPieceSegment), (JFIFPiece)new JFIFPieceSegment(jfifPieceSegment.marker, new IptcParser().writePhotoshopApp13Segment(new PhotoshopApp13Data(new ArrayList<IptcRecord>(), new IptcParser().parsePhotoshopSegment(jfifPieceSegment.segmentData, new HashMap<String, Object>()).getNonIptcBlocks()))));
        }
        this.writeSegments(outputStream, removePhotoshopApp13Segments);
    }
    
    public void removeIPTC(final byte[] array, final OutputStream outputStream) throws ImageReadException, IOException, ImageWriteException {
        this.removeIPTC(new ByteSourceArray(array), outputStream);
    }
    
    public void writeIPTC(final File file, final OutputStream outputStream, final PhotoshopApp13Data photoshopApp13Data) throws ImageReadException, IOException, ImageWriteException {
        this.writeIPTC(new ByteSourceFile(file), outputStream, photoshopApp13Data);
    }
    
    public void writeIPTC(final InputStream inputStream, final OutputStream outputStream, final PhotoshopApp13Data photoshopApp13Data) throws ImageReadException, IOException, ImageWriteException {
        this.writeIPTC(new ByteSourceInputStream(inputStream, null), outputStream, photoshopApp13Data);
    }
    
    public void writeIPTC(final ByteSource byteSource, final OutputStream outputStream, PhotoshopApp13Data photoshopApp13Data) throws ImageReadException, IOException, ImageWriteException {
        final List<JFIFPiece> pieces = this.analyzeJFIF(byteSource).pieces;
        if (this.findPhotoshopApp13Segments(pieces).size() > 1) {
            throw new ImageReadException("Image contains more than one Photoshop App13 segment.");
        }
        final List<T> removePhotoshopApp13Segments = this.removePhotoshopApp13Segments((List<T>)pieces);
        final List<IptcBlock> nonIptcBlocks = photoshopApp13Data.getNonIptcBlocks();
        nonIptcBlocks.add(new IptcBlock(1028, new byte[0], new IptcParser().writeIPTCBlock(photoshopApp13Data.getRecords())));
        photoshopApp13Data = new PhotoshopApp13Data(photoshopApp13Data.getRecords(), nonIptcBlocks);
        this.writeSegments(outputStream, this.insertAfterLastAppSegments((List<JFIFPiece>)removePhotoshopApp13Segments, (List<JFIFPiece>)Arrays.asList(new JFIFPieceSegment(65517, new IptcParser().writePhotoshopApp13Segment(photoshopApp13Data)))));
    }
    
    public void writeIPTC(final byte[] array, final OutputStream outputStream, final PhotoshopApp13Data photoshopApp13Data) throws ImageReadException, IOException, ImageWriteException {
        this.writeIPTC(new ByteSourceArray(array), outputStream, photoshopApp13Data);
    }
}
