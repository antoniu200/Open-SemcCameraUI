package org.apache.commons.imaging.formats.jpeg.iptc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.common.bytesource.ByteSourceArray;
import org.apache.commons.imaging.common.bytesource.ByteSourceFile;
import org.apache.commons.imaging.common.bytesource.ByteSourceInputStream;
import org.apache.commons.imaging.formats.jpeg.JpegConstants;
import org.apache.commons.imaging.formats.jpeg.xmp.JpegRewriter;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class JpegIptcRewriter extends JpegRewriter {
    public void removeIPTC(File file, OutputStream outputStream) throws ImageWriteException, IOException, ImageReadException {
        removeIPTC(new ByteSourceFile(file), outputStream);
    }

    public void removeIPTC(byte[] bArr, OutputStream outputStream) throws ImageWriteException, IOException, ImageReadException {
        removeIPTC(new ByteSourceArray(bArr), outputStream);
    }

    public void removeIPTC(InputStream inputStream, OutputStream outputStream) throws ImageWriteException, IOException, ImageReadException {
        removeIPTC(new ByteSourceInputStream(inputStream, null), outputStream);
    }

    public void removeIPTC(ByteSource byteSource, OutputStream outputStream) throws ImageWriteException, IOException, ImageReadException {
        List<JpegRewriter.JFIFPiece> list = analyzeJFIF(byteSource).pieces;
        List listFindPhotoshopApp13Segments = findPhotoshopApp13Segments(list);
        if (listFindPhotoshopApp13Segments.size() > 1) {
            throw new ImageReadException("Image contains more than one Photoshop App13 segment.");
        }
        List<? extends JpegRewriter.JFIFPiece> listRemovePhotoshopApp13Segments = removePhotoshopApp13Segments(list);
        if (listFindPhotoshopApp13Segments.size() == 1) {
            JpegRewriter.JFIFPieceSegment jFIFPieceSegment = (JpegRewriter.JFIFPieceSegment) listFindPhotoshopApp13Segments.get(0);
            listRemovePhotoshopApp13Segments.add(list.indexOf(jFIFPieceSegment), new JpegRewriter.JFIFPieceSegment(jFIFPieceSegment.marker, new IptcParser().writePhotoshopApp13Segment(new PhotoshopApp13Data(new ArrayList(), new IptcParser().parsePhotoshopSegment(jFIFPieceSegment.segmentData, new HashMap()).getNonIptcBlocks()))));
        }
        writeSegments(outputStream, listRemovePhotoshopApp13Segments);
    }

    public void writeIPTC(byte[] bArr, OutputStream outputStream, PhotoshopApp13Data photoshopApp13Data) throws ImageWriteException, IOException, ImageReadException {
        writeIPTC(new ByteSourceArray(bArr), outputStream, photoshopApp13Data);
    }

    public void writeIPTC(InputStream inputStream, OutputStream outputStream, PhotoshopApp13Data photoshopApp13Data) throws ImageWriteException, IOException, ImageReadException {
        writeIPTC(new ByteSourceInputStream(inputStream, null), outputStream, photoshopApp13Data);
    }

    public void writeIPTC(File file, OutputStream outputStream, PhotoshopApp13Data photoshopApp13Data) throws ImageWriteException, IOException, ImageReadException {
        writeIPTC(new ByteSourceFile(file), outputStream, photoshopApp13Data);
    }

    public void writeIPTC(ByteSource byteSource, OutputStream outputStream, PhotoshopApp13Data photoshopApp13Data) throws ImageWriteException, IOException, ImageReadException {
        List<JpegRewriter.JFIFPiece> list = analyzeJFIF(byteSource).pieces;
        if (findPhotoshopApp13Segments(list).size() > 1) {
            throw new ImageReadException("Image contains more than one Photoshop App13 segment.");
        }
        List listRemovePhotoshopApp13Segments = removePhotoshopApp13Segments(list);
        List<IptcBlock> nonIptcBlocks = photoshopApp13Data.getNonIptcBlocks();
        nonIptcBlocks.add(new IptcBlock(IptcConstants.IMAGE_RESOURCE_BLOCK_IPTC_DATA, new byte[0], new IptcParser().writeIPTCBlock(photoshopApp13Data.getRecords())));
        writeSegments(outputStream, insertAfterLastAppSegments(listRemovePhotoshopApp13Segments, Arrays.asList(new JpegRewriter.JFIFPieceSegment(JpegConstants.JPEG_APP13_MARKER, new IptcParser().writePhotoshopApp13Segment(new PhotoshopApp13Data(photoshopApp13Data.getRecords(), nonIptcBlocks))))));
    }
}
