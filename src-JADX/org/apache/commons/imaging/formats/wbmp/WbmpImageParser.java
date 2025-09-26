package org.apache.commons.imaging.formats.wbmp;

import android.support.v4.view.ViewCompat;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import kotlin.jvm.internal.ByteCompanionObject;
import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageParser;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.ImagingConstants;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.util.IoUtils;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class WbmpImageParser extends ImageParser {
    private static final String DEFAULT_EXTENSION = ".wbmp";
    private static final String[] ACCEPTED_EXTENSIONS = {DEFAULT_EXTENSION};

    @Override // org.apache.commons.imaging.ImageParser
    public String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    @Override // org.apache.commons.imaging.ImageParser
    public byte[] getICCProfileBytes(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        return null;
    }

    @Override // org.apache.commons.imaging.ImageParser
    public ImageMetadata getMetadata(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        return null;
    }

    @Override // org.apache.commons.imaging.ImageParser
    public String getName() {
        return "Wireless Application Protocol Bitmap Format";
    }

    @Override // org.apache.commons.imaging.ImageParser
    public String getXmpXml(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        return null;
    }

    @Override // org.apache.commons.imaging.ImageParser
    protected String[] getAcceptedExtensions() {
        return ACCEPTED_EXTENSIONS;
    }

    @Override // org.apache.commons.imaging.ImageParser
    protected ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[]{ImageFormats.WBMP};
    }

    @Override // org.apache.commons.imaging.ImageParser
    public ImageInfo getImageInfo(ByteSource byteSource, Map<String, Object> map) throws Throwable {
        WbmpHeader wbmpHeader = readWbmpHeader(byteSource);
        return new ImageInfo("WBMP", 1, new ArrayList(), ImageFormats.WBMP, "Wireless Application Protocol Bitmap", wbmpHeader.height, "image/vnd.wap.wbmp", 1, 0, 0.0f, 0, 0.0f, wbmpHeader.width, false, false, false, ImageInfo.ColorType.BW, ImageInfo.CompressionAlgorithm.NONE);
    }

    @Override // org.apache.commons.imaging.ImageParser
    public Dimension getImageSize(ByteSource byteSource, Map<String, Object> map) throws Throwable {
        WbmpHeader wbmpHeader = readWbmpHeader(byteSource);
        return new Dimension(wbmpHeader.width, wbmpHeader.height);
    }

    static class WbmpHeader {
        byte fixHeaderField;
        int height;
        int typeField;
        int width;

        public WbmpHeader(int i, byte b, int i2, int i3) {
            this.typeField = i;
            this.fixHeaderField = b;
            this.width = i2;
            this.height = i3;
        }

        public void dump(PrintWriter printWriter) {
            printWriter.println("WbmpHeader");
            printWriter.println("TypeField: " + this.typeField);
            printWriter.println("FixHeaderField: 0x" + Integer.toHexString(this.fixHeaderField & 255));
            printWriter.println("Width: " + this.width);
            printWriter.println("Height: " + this.height);
        }
    }

    private int readMultiByteInteger(InputStream inputStream) throws IOException, ImageReadException {
        byte b;
        int i = 0;
        int i2 = 0;
        do {
            b = BinaryFunctions.readByte("Header", inputStream, "Error reading WBMP header");
            i = (i << 7) | (b & ByteCompanionObject.MAX_VALUE);
            i2 += 7;
            if (i2 > 31) {
                throw new ImageReadException("Overflow reading WBMP multi-byte field");
            }
        } while ((b & ByteCompanionObject.MIN_VALUE) != 0);
        return i;
    }

    private void writeMultiByteInteger(OutputStream outputStream, int i) throws IOException {
        boolean z = false;
        for (int i2 = 28; i2 > 0; i2 -= 7) {
            int i3 = 127 & (i >>> i2);
            if (i3 != 0 || z) {
                outputStream.write(128 | i3);
                z = true;
            }
        }
        outputStream.write(127 & i);
    }

    private WbmpHeader readWbmpHeader(ByteSource byteSource) throws Throwable {
        InputStream inputStream;
        try {
            inputStream = byteSource.getInputStream();
        } catch (Throwable th) {
            th = th;
            inputStream = null;
        }
        try {
            WbmpHeader wbmpHeader = readWbmpHeader(inputStream);
            IoUtils.closeQuietly(true, inputStream);
            return wbmpHeader;
        } catch (Throwable th2) {
            th = th2;
            IoUtils.closeQuietly(false, inputStream);
            throw th;
        }
    }

    private WbmpHeader readWbmpHeader(InputStream inputStream) throws IOException, ImageReadException {
        int multiByteInteger = readMultiByteInteger(inputStream);
        if (multiByteInteger != 0) {
            throw new ImageReadException("Invalid/unsupported WBMP type " + multiByteInteger);
        }
        byte b = BinaryFunctions.readByte("FixHeaderField", inputStream, "Invalid WBMP File");
        if ((b & 159) != 0) {
            throw new ImageReadException("Invalid/unsupported WBMP FixHeaderField 0x" + Integer.toHexString(255 & b));
        }
        return new WbmpHeader(multiByteInteger, b, readMultiByteInteger(inputStream), readMultiByteInteger(inputStream));
    }

    @Override // org.apache.commons.imaging.ImageParser
    public boolean dumpImageFile(PrintWriter printWriter, ByteSource byteSource) throws IOException, ImageReadException {
        readWbmpHeader(byteSource).dump(printWriter);
        return true;
    }

    private BufferedImage readImage(WbmpHeader wbmpHeader, InputStream inputStream) throws IOException {
        byte[] bytes = BinaryFunctions.readBytes("Pixels", inputStream, ((wbmpHeader.width + 7) / 8) * wbmpHeader.height, "Error reading image pixels");
        WritableRaster writableRasterCreatePackedRaster = Raster.createPackedRaster(new DataBufferByte(bytes, bytes.length), wbmpHeader.width, wbmpHeader.height, 1, (Point) null);
        IndexColorModel indexColorModel = new IndexColorModel(1, 2, new int[]{0, ViewCompat.MEASURED_SIZE_MASK}, 0, false, -1, 0);
        return new BufferedImage(indexColorModel, writableRasterCreatePackedRaster, indexColorModel.isAlphaPremultiplied(), new Properties());
    }

    @Override // org.apache.commons.imaging.ImageParser
    public final BufferedImage getBufferedImage(ByteSource byteSource, Map<String, Object> map) throws Throwable {
        InputStream inputStream;
        try {
            inputStream = byteSource.getInputStream();
        } catch (Throwable th) {
            th = th;
            inputStream = null;
        }
        try {
            BufferedImage image = readImage(readWbmpHeader(inputStream), inputStream);
            IoUtils.closeQuietly(true, inputStream);
            return image;
        } catch (Throwable th2) {
            th = th2;
            IoUtils.closeQuietly(false, inputStream);
            throw th;
        }
    }

    @Override // org.apache.commons.imaging.ImageParser
    public void writeImage(BufferedImage bufferedImage, OutputStream outputStream, Map<String, Object> map) throws ImageWriteException, IOException {
        HashMap map2 = map == null ? new HashMap() : new HashMap(map);
        if (map2.containsKey(ImagingConstants.PARAM_KEY_FORMAT)) {
            map2.remove(ImagingConstants.PARAM_KEY_FORMAT);
        }
        if (!map2.isEmpty()) {
            throw new ImageWriteException("Unknown parameter: " + map2.keySet().iterator().next());
        }
        writeMultiByteInteger(outputStream, 0);
        outputStream.write(0);
        writeMultiByteInteger(outputStream, bufferedImage.getWidth());
        writeMultiByteInteger(outputStream, bufferedImage.getHeight());
        for (int i = 0; i < bufferedImage.getHeight(); i++) {
            int i2 = 0;
            int i3 = 128;
            for (int i4 = 0; i4 < bufferedImage.getWidth(); i4++) {
                int rgb = bufferedImage.getRGB(i4, i);
                if (((((rgb >> 16) & 255) + ((rgb >> 8) & 255)) + ((rgb >> 0) & 255)) / 3 > 127) {
                    i2 |= i3;
                }
                i3 >>>= 1;
                if (i3 == 0) {
                    outputStream.write(i2);
                    i2 = 0;
                    i3 = 128;
                }
            }
            if (i3 != 128) {
                outputStream.write(i2);
            }
        }
    }
}
