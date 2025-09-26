// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.jpeg.xmp;

import java.util.ArrayList;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.common.bytesource.ByteSourceArray;
import java.util.List;
import org.apache.commons.imaging.common.bytesource.ByteSourceInputStream;
import java.io.InputStream;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.common.bytesource.ByteSourceFile;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.imaging.formats.jpeg.JpegConstants;
import java.io.ByteArrayOutputStream;

public class JpegXmpRewriter extends JpegRewriter
{
    private byte[] writeXmpSegment(final byte[] b, final int off, final int len) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JpegConstants.XMP_IDENTIFIER.writeTo(byteArrayOutputStream);
        byteArrayOutputStream.write(b, off, len);
        return byteArrayOutputStream.toByteArray();
    }
    
    public void removeXmpXml(final File file, final OutputStream outputStream) throws ImageReadException, IOException {
        this.removeXmpXml(new ByteSourceFile(file), outputStream);
    }
    
    public void removeXmpXml(final InputStream inputStream, final OutputStream outputStream) throws ImageReadException, IOException {
        this.removeXmpXml(new ByteSourceInputStream(inputStream, null), outputStream);
    }
    
    public void removeXmpXml(final ByteSource byteSource, final OutputStream outputStream) throws ImageReadException, IOException {
        this.writeSegments(outputStream, (List<? extends JFIFPiece>)this.removeXmpSegments(this.analyzeJFIF(byteSource).pieces));
    }
    
    public void removeXmpXml(final byte[] array, final OutputStream outputStream) throws ImageReadException, IOException {
        this.removeXmpXml(new ByteSourceArray(array), outputStream);
    }
    
    public void updateXmpXml(final File file, final OutputStream outputStream, final String s) throws ImageReadException, IOException, ImageWriteException {
        this.updateXmpXml(new ByteSourceFile(file), outputStream, s);
    }
    
    public void updateXmpXml(final InputStream inputStream, final OutputStream outputStream, final String s) throws ImageReadException, IOException, ImageWriteException {
        this.updateXmpXml(new ByteSourceInputStream(inputStream, null), outputStream, s);
    }
    
    public void updateXmpXml(final ByteSource byteSource, final OutputStream outputStream, final String s) throws ImageReadException, IOException, ImageWriteException {
        final List<JFIFPiece> removeXmpSegments = this.removeXmpSegments(this.analyzeJFIF(byteSource).pieces);
        final ArrayList list = new ArrayList();
        final byte[] bytes = s.getBytes("utf-8");
        int min;
        for (int i = 0; i < bytes.length; i += min) {
            min = Math.min(bytes.length, 65535);
            list.add(new JFIFPieceSegment(65505, this.writeXmpSegment(bytes, i, min)));
        }
        this.writeSegments(outputStream, this.insertAfterLastAppSegments(removeXmpSegments, (List<JFIFPiece>)list));
    }
    
    public void updateXmpXml(final byte[] array, final OutputStream outputStream, final String s) throws ImageReadException, IOException, ImageWriteException {
        this.updateXmpXml(new ByteSourceArray(array), outputStream, s);
    }
}
