// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.wbmp;

import org.apache.commons.imaging.ImageWriteException;
import java.util.HashMap;
import org.apache.commons.imaging.common.ImageMetadata;
import java.awt.Dimension;
import java.util.List;
import java.util.ArrayList;
import org.apache.commons.imaging.ImageInfo;
import java.util.Map;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageFormat;
import java.io.PrintWriter;
import java.io.OutputStream;
import org.apache.commons.imaging.util.IoUtils;
import java.io.Closeable;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.ImageReadException;
import java.io.IOException;
import java.awt.image.WritableRaster;
import java.util.Hashtable;
import java.awt.image.ColorModel;
import java.util.Properties;
import java.awt.image.IndexColorModel;
import java.awt.Point;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.DataBufferByte;
import org.apache.commons.imaging.common.BinaryFunctions;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import org.apache.commons.imaging.ImageParser;

public class WbmpImageParser extends ImageParser
{
    private static final String[] ACCEPTED_EXTENSIONS;
    private static final String DEFAULT_EXTENSION = ".wbmp";
    
    static {
        ACCEPTED_EXTENSIONS = new String[] { ".wbmp" };
    }
    
    private BufferedImage readImage(final WbmpHeader wbmpHeader, final InputStream inputStream) throws IOException {
        final byte[] bytes = BinaryFunctions.readBytes("Pixels", inputStream, (wbmpHeader.width + 7) / 8 * wbmpHeader.height, "Error reading image pixels");
        final WritableRaster packedRaster = Raster.createPackedRaster(new DataBufferByte(bytes, bytes.length), wbmpHeader.width, wbmpHeader.height, 1, null);
        final IndexColorModel cm = new IndexColorModel(1, 2, new int[] { 0, 16777215 }, 0, false, -1, 0);
        return new BufferedImage(cm, packedRaster, cm.isAlphaPremultiplied(), new Properties());
    }
    
    private int readMultiByteInteger(final InputStream inputStream) throws ImageReadException, IOException {
        int n = 0;
        int n2 = 0;
        byte byte1;
        int n3;
        do {
            byte1 = BinaryFunctions.readByte("Header", inputStream, "Error reading WBMP header");
            n3 = (n << 7 | (byte1 & 0x7F));
            n2 += 7;
            if (n2 > 31) {
                throw new ImageReadException("Overflow reading WBMP multi-byte field");
            }
            n = n3;
        } while ((byte1 & 0x80) != 0x0);
        return n3;
    }
    
    private WbmpHeader readWbmpHeader(final InputStream inputStream) throws ImageReadException, IOException {
        final int multiByteInteger = this.readMultiByteInteger(inputStream);
        if (multiByteInteger != 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid/unsupported WBMP type ");
            sb.append(multiByteInteger);
            throw new ImageReadException(sb.toString());
        }
        final byte byte1 = BinaryFunctions.readByte("FixHeaderField", inputStream, "Invalid WBMP File");
        if ((byte1 & 0x9F) != 0x0) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Invalid/unsupported WBMP FixHeaderField 0x");
            sb2.append(Integer.toHexString(0xFF & byte1));
            throw new ImageReadException(sb2.toString());
        }
        return new WbmpHeader(multiByteInteger, byte1, this.readMultiByteInteger(inputStream), this.readMultiByteInteger(inputStream));
    }
    
    private WbmpHeader readWbmpHeader(final ByteSource byteSource) throws ImageReadException, IOException {
        Closeable closeable;
        try {
            final InputStream inputStream = byteSource.getInputStream();
            try {
                final WbmpHeader wbmpHeader = this.readWbmpHeader(inputStream);
                IoUtils.closeQuietly(true, inputStream);
                return wbmpHeader;
            }
            finally {}
        }
        finally {
            closeable = null;
        }
        IoUtils.closeQuietly(false, closeable);
    }
    
    private void writeMultiByteInteger(final OutputStream outputStream, final int n) throws IOException {
        int n2 = 0;
        int n4;
        for (int i = 28; i > 0; i -= 7, n2 = n4) {
            final int n3 = 0x7F & n >>> i;
            if (n3 != 0 || (n4 = n2) != 0) {
                outputStream.write(0x80 | n3);
                n4 = 1;
            }
        }
        outputStream.write(0x7F & n);
    }
    
    @Override
    public boolean dumpImageFile(final PrintWriter printWriter, final ByteSource byteSource) throws ImageReadException, IOException {
        this.readWbmpHeader(byteSource).dump(printWriter);
        return true;
    }
    
    @Override
    protected String[] getAcceptedExtensions() {
        return WbmpImageParser.ACCEPTED_EXTENSIONS;
    }
    
    @Override
    protected ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[] { ImageFormats.WBMP };
    }
    
    @Override
    public final BufferedImage getBufferedImage(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        Closeable closeable;
        try {
            final InputStream inputStream = byteSource.getInputStream();
            try {
                final BufferedImage image = this.readImage(this.readWbmpHeader(inputStream), inputStream);
                IoUtils.closeQuietly(true, inputStream);
                return image;
            }
            finally {}
        }
        finally {
            closeable = null;
        }
        IoUtils.closeQuietly(false, closeable);
    }
    
    @Override
    public String getDefaultExtension() {
        return ".wbmp";
    }
    
    @Override
    public byte[] getICCProfileBytes(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        return null;
    }
    
    @Override
    public ImageInfo getImageInfo(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final WbmpHeader wbmpHeader = this.readWbmpHeader(byteSource);
        return new ImageInfo("WBMP", 1, new ArrayList<String>(), ImageFormats.WBMP, "Wireless Application Protocol Bitmap", wbmpHeader.height, "image/vnd.wap.wbmp", 1, 0, 0.0f, 0, 0.0f, wbmpHeader.width, false, false, false, ImageInfo.ColorType.BW, ImageInfo.CompressionAlgorithm.NONE);
    }
    
    @Override
    public Dimension getImageSize(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final WbmpHeader wbmpHeader = this.readWbmpHeader(byteSource);
        return new Dimension(wbmpHeader.width, wbmpHeader.height);
    }
    
    @Override
    public ImageMetadata getMetadata(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        return null;
    }
    
    @Override
    public String getName() {
        return "Wireless Application Protocol Bitmap Format";
    }
    
    @Override
    public String getXmpXml(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        return null;
    }
    
    @Override
    public void writeImage(final BufferedImage bufferedImage, final OutputStream outputStream, final Map<String, Object> m) throws ImageWriteException, IOException {
        HashMap hashMap;
        if (m == null) {
            hashMap = new HashMap();
        }
        else {
            hashMap = new HashMap((Map<? extends K, ? extends V>)m);
        }
        if (hashMap.containsKey("FORMAT")) {
            hashMap.remove("FORMAT");
        }
        if (!hashMap.isEmpty()) {
            final Object next = hashMap.keySet().iterator().next();
            final StringBuilder sb = new StringBuilder();
            sb.append("Unknown parameter: ");
            sb.append(next);
            throw new ImageWriteException(sb.toString());
        }
        this.writeMultiByteInteger(outputStream, 0);
        outputStream.write(0);
        this.writeMultiByteInteger(outputStream, bufferedImage.getWidth());
        this.writeMultiByteInteger(outputStream, bufferedImage.getHeight());
        for (int i = 0; i < bufferedImage.getHeight(); ++i) {
            int j = 0;
            int n = 0;
            int n2 = 128;
            while (j < bufferedImage.getWidth()) {
                final int rgb = bufferedImage.getRGB(j, i);
                int n3 = n;
                if (((rgb >> 16 & 0xFF) + (rgb >> 8 & 0xFF) + (rgb >> 0 & 0xFF)) / 3 > 127) {
                    n3 = (n | n2);
                }
                final int n4 = n2 >>>= 1;
                n = n3;
                if (n4 == 0) {
                    outputStream.write(n3);
                    n = 0;
                    n2 = 128;
                }
                ++j;
            }
            if (n2 != 128) {
                outputStream.write(n);
            }
        }
    }
    
    static class WbmpHeader
    {
        byte fixHeaderField;
        int height;
        int typeField;
        int width;
        
        public WbmpHeader(final int typeField, final byte fixHeaderField, final int width, final int height) {
            this.typeField = typeField;
            this.fixHeaderField = fixHeaderField;
            this.width = width;
            this.height = height;
        }
        
        public void dump(final PrintWriter printWriter) {
            printWriter.println("WbmpHeader");
            final StringBuilder sb = new StringBuilder();
            sb.append("TypeField: ");
            sb.append(this.typeField);
            printWriter.println(sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("FixHeaderField: 0x");
            sb2.append(Integer.toHexString(this.fixHeaderField & 0xFF));
            printWriter.println(sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Width: ");
            sb3.append(this.width);
            printWriter.println(sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Height: ");
            sb4.append(this.height);
            printWriter.println(sb4.toString());
        }
    }
}
