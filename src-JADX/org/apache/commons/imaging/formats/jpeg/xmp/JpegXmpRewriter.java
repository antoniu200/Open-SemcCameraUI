package org.apache.commons.imaging.formats.jpeg.xmp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
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
public class JpegXmpRewriter extends JpegRewriter {
    public void removeXmpXml(File file, OutputStream outputStream) throws IOException, ImageReadException {
        removeXmpXml(new ByteSourceFile(file), outputStream);
    }

    public void removeXmpXml(byte[] bArr, OutputStream outputStream) throws IOException, ImageReadException {
        removeXmpXml(new ByteSourceArray(bArr), outputStream);
    }

    public void removeXmpXml(InputStream inputStream, OutputStream outputStream) throws IOException, ImageReadException {
        removeXmpXml(new ByteSourceInputStream(inputStream, null), outputStream);
    }

    public void removeXmpXml(ByteSource byteSource, OutputStream outputStream) throws IOException, ImageReadException {
        writeSegments(outputStream, removeXmpSegments(analyzeJFIF(byteSource).pieces));
    }

    public void updateXmpXml(byte[] bArr, OutputStream outputStream, String str) throws ImageWriteException, IOException, ImageReadException {
        updateXmpXml(new ByteSourceArray(bArr), outputStream, str);
    }

    public void updateXmpXml(InputStream inputStream, OutputStream outputStream, String str) throws ImageWriteException, IOException, ImageReadException {
        updateXmpXml(new ByteSourceInputStream(inputStream, null), outputStream, str);
    }

    public void updateXmpXml(File file, OutputStream outputStream, String str) throws ImageWriteException, IOException, ImageReadException {
        updateXmpXml(new ByteSourceFile(file), outputStream, str);
    }

    public void updateXmpXml(ByteSource byteSource, OutputStream outputStream, String str) throws ImageWriteException, IOException, ImageReadException {
        List listRemoveXmpSegments = removeXmpSegments(analyzeJFIF(byteSource).pieces);
        ArrayList arrayList = new ArrayList();
        byte[] bytes = str.getBytes("utf-8");
        int i = 0;
        while (i < bytes.length) {
            int iMin = Math.min(bytes.length, 65535);
            arrayList.add(new JpegRewriter.JFIFPieceSegment(JpegConstants.JPEG_APP1_MARKER, writeXmpSegment(bytes, i, iMin)));
            i += iMin;
        }
        writeSegments(outputStream, insertAfterLastAppSegments(listRemoveXmpSegments, arrayList));
    }

    private byte[] writeXmpSegment(byte[] bArr, int i, int i2) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JpegConstants.XMP_IDENTIFIER.writeTo(byteArrayOutputStream);
        byteArrayOutputStream.write(bArr, i, i2);
        return byteArrayOutputStream.toByteArray();
    }
}
