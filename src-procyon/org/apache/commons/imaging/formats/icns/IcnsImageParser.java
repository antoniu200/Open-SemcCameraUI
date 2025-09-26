// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.icns;

import org.apache.commons.imaging.common.BinaryOutputStream;
import org.apache.commons.imaging.ImageWriteException;
import java.io.OutputStream;
import org.apache.commons.imaging.common.ImageMetadata;
import java.awt.Dimension;
import java.util.HashMap;
import org.apache.commons.imaging.ImageInfo;
import java.util.Map;
import java.awt.image.BufferedImage;
import java.util.List;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageFormat;
import java.io.PrintWriter;
import org.apache.commons.imaging.util.IoUtils;
import java.io.Closeable;
import java.util.ArrayList;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.ImageReadException;
import java.io.IOException;
import org.apache.commons.imaging.common.BinaryFunctions;
import java.io.InputStream;
import java.nio.ByteOrder;
import org.apache.commons.imaging.ImageParser;

public class IcnsImageParser extends ImageParser
{
    private static final String[] ACCEPTED_EXTENSIONS;
    private static final String DEFAULT_EXTENSION = ".icns";
    static final int ICNS_MAGIC;
    
    static {
        ICNS_MAGIC = IcnsType.typeAsInt("icns");
        ACCEPTED_EXTENSIONS = new String[] { ".icns" };
    }
    
    public IcnsImageParser() {
        super.setByteOrder(ByteOrder.BIG_ENDIAN);
    }
    
    private IcnsElement readIcnsElement(final InputStream inputStream) throws IOException {
        final int read4Bytes = BinaryFunctions.read4Bytes("Type", inputStream, "Not a Valid ICNS File", this.getByteOrder());
        final int read4Bytes2 = BinaryFunctions.read4Bytes("ElementSize", inputStream, "Not a Valid ICNS File", this.getByteOrder());
        return new IcnsElement(read4Bytes, read4Bytes2, BinaryFunctions.readBytes("Data", inputStream, read4Bytes2 - 8, "Not a Valid ICNS File"));
    }
    
    private IcnsHeader readIcnsHeader(final InputStream inputStream) throws ImageReadException, IOException {
        final int read4Bytes = BinaryFunctions.read4Bytes("Magic", inputStream, "Not a Valid ICNS File", this.getByteOrder());
        final int read4Bytes2 = BinaryFunctions.read4Bytes("FileSize", inputStream, "Not a Valid ICNS File", this.getByteOrder());
        if (read4Bytes != IcnsImageParser.ICNS_MAGIC) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Not a Valid ICNS File: magic is 0x");
            sb.append(Integer.toHexString(read4Bytes));
            throw new ImageReadException(sb.toString());
        }
        return new IcnsHeader(read4Bytes, read4Bytes2);
    }
    
    private IcnsContents readImage(final ByteSource byteSource) throws ImageReadException, IOException {
        Closeable closeable;
        try {
            final InputStream inputStream = byteSource.getInputStream();
            try {
                final IcnsHeader icnsHeader = this.readIcnsHeader(inputStream);
                final ArrayList list = new ArrayList();
                IcnsElement icnsElement;
                for (int i = icnsHeader.fileSize - 8; i > 0; i -= icnsElement.elementSize) {
                    icnsElement = this.readIcnsElement(inputStream);
                    list.add(icnsElement);
                }
                final IcnsElement[] array = new IcnsElement[list.size()];
                for (int j = 0; j < array.length; ++j) {
                    array[j] = (IcnsElement)list.get(j);
                }
                final IcnsContents icnsContents = new IcnsContents(icnsHeader, array);
                IoUtils.closeQuietly(true, inputStream);
                return icnsContents;
            }
            finally {}
        }
        finally {
            closeable = null;
        }
        IoUtils.closeQuietly(false, closeable);
    }
    
    @Override
    public boolean dumpImageFile(final PrintWriter printWriter, final ByteSource byteSource) throws ImageReadException, IOException {
        final IcnsContents image = this.readImage(byteSource);
        image.icnsHeader.dump(printWriter);
        final IcnsElement[] icnsElements = image.icnsElements;
        for (int length = icnsElements.length, i = 0; i < length; ++i) {
            icnsElements[i].dump(printWriter);
        }
        return true;
    }
    
    @Override
    protected String[] getAcceptedExtensions() {
        return IcnsImageParser.ACCEPTED_EXTENSIONS;
    }
    
    @Override
    protected ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[] { ImageFormats.ICNS };
    }
    
    @Override
    public List<BufferedImage> getAllBufferedImages(final ByteSource byteSource) throws ImageReadException, IOException {
        return IcnsDecoder.decodeAllImages(this.readImage(byteSource).icnsElements);
    }
    
    @Override
    public final BufferedImage getBufferedImage(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final List<BufferedImage> decodeAllImages = IcnsDecoder.decodeAllImages(this.readImage(byteSource).icnsElements);
        if (!decodeAllImages.isEmpty()) {
            return (BufferedImage)decodeAllImages.get(0);
        }
        throw new ImageReadException("No icons in ICNS file");
    }
    
    @Override
    public String getDefaultExtension() {
        return ".icns";
    }
    
    @Override
    public byte[] getICCProfileBytes(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        return null;
    }
    
    @Override
    public ImageInfo getImageInfo(final ByteSource byteSource, final Map<String, Object> m) throws ImageReadException, IOException {
        HashMap hashMap;
        if (m == null) {
            hashMap = new HashMap();
        }
        else {
            hashMap = new HashMap((Map<? extends K, ? extends V>)m);
        }
        if (hashMap.containsKey("VERBOSE")) {
            hashMap.remove("VERBOSE");
        }
        if (!hashMap.isEmpty()) {
            final Object next = hashMap.keySet().iterator().next();
            final StringBuilder sb = new StringBuilder();
            sb.append("Unknown parameter: ");
            sb.append(next);
            throw new ImageReadException(sb.toString());
        }
        final List<BufferedImage> decodeAllImages = IcnsDecoder.decodeAllImages(this.readImage(byteSource).icnsElements);
        if (decodeAllImages.isEmpty()) {
            throw new ImageReadException("No icons in ICNS file");
        }
        final BufferedImage bufferedImage = decodeAllImages.get(0);
        return new ImageInfo("Icns", 32, new ArrayList<String>(), ImageFormats.ICNS, "ICNS Apple Icon Image", bufferedImage.getHeight(), "image/x-icns", decodeAllImages.size(), 0, 0.0f, 0, 0.0f, bufferedImage.getWidth(), false, true, false, ImageInfo.ColorType.RGB, ImageInfo.CompressionAlgorithm.UNKNOWN);
    }
    
    @Override
    public Dimension getImageSize(final ByteSource byteSource, final Map<String, Object> m) throws ImageReadException, IOException {
        HashMap hashMap;
        if (m == null) {
            hashMap = new HashMap();
        }
        else {
            hashMap = new HashMap((Map<? extends K, ? extends V>)m);
        }
        if (hashMap.containsKey("VERBOSE")) {
            hashMap.remove("VERBOSE");
        }
        if (!hashMap.isEmpty()) {
            final Object next = hashMap.keySet().iterator().next();
            final StringBuilder sb = new StringBuilder();
            sb.append("Unknown parameter: ");
            sb.append(next);
            throw new ImageReadException(sb.toString());
        }
        final List<BufferedImage> decodeAllImages = IcnsDecoder.decodeAllImages(this.readImage(byteSource).icnsElements);
        if (decodeAllImages.isEmpty()) {
            throw new ImageReadException("No icons in ICNS file");
        }
        final BufferedImage bufferedImage = decodeAllImages.get(0);
        return new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight());
    }
    
    @Override
    public ImageMetadata getMetadata(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        return null;
    }
    
    @Override
    public String getName() {
        return "Apple Icon Image";
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
        IcnsType icnsType;
        if (bufferedImage.getWidth() == 16 && bufferedImage.getHeight() == 16) {
            icnsType = IcnsType.ICNS_16x16_32BIT_IMAGE;
        }
        else if (bufferedImage.getWidth() == 32 && bufferedImage.getHeight() == 32) {
            icnsType = IcnsType.ICNS_32x32_32BIT_IMAGE;
        }
        else if (bufferedImage.getWidth() == 48 && bufferedImage.getHeight() == 48) {
            icnsType = IcnsType.ICNS_48x48_32BIT_IMAGE;
        }
        else {
            if (bufferedImage.getWidth() != 128 || bufferedImage.getHeight() != 128) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Invalid/unsupported source width ");
                sb2.append(bufferedImage.getWidth());
                sb2.append(" and height ");
                sb2.append(bufferedImage.getHeight());
                throw new ImageWriteException(sb2.toString());
            }
            icnsType = IcnsType.ICNS_128x128_32BIT_IMAGE;
        }
        final BinaryOutputStream binaryOutputStream = new BinaryOutputStream(outputStream, ByteOrder.BIG_ENDIAN);
        binaryOutputStream.write4Bytes(IcnsImageParser.ICNS_MAGIC);
        binaryOutputStream.write4Bytes(16 + icnsType.getWidth() * 4 * icnsType.getHeight() + 4 + 4 + icnsType.getWidth() * icnsType.getHeight());
        binaryOutputStream.write4Bytes(icnsType.getType());
        binaryOutputStream.write4Bytes(4 * icnsType.getWidth() * icnsType.getHeight() + 8);
        for (int i = 0; i < bufferedImage.getHeight(); ++i) {
            for (int j = 0; j < bufferedImage.getWidth(); ++j) {
                final int rgb = bufferedImage.getRGB(j, i);
                binaryOutputStream.write(0);
                binaryOutputStream.write(rgb >> 16);
                binaryOutputStream.write(rgb >> 8);
                binaryOutputStream.write(rgb);
            }
        }
        binaryOutputStream.write4Bytes(IcnsType.find8BPPMaskType(icnsType).getType());
        binaryOutputStream.write4Bytes(8 + icnsType.getWidth() * icnsType.getWidth());
        for (int k = 0; k < bufferedImage.getHeight(); ++k) {
            for (int l = 0; l < bufferedImage.getWidth(); ++l) {
                binaryOutputStream.write(bufferedImage.getRGB(l, k) >> 24);
            }
        }
        binaryOutputStream.close();
    }
    
    private static class IcnsContents
    {
        public final IcnsElement[] icnsElements;
        public final IcnsHeader icnsHeader;
        
        public IcnsContents(final IcnsHeader icnsHeader, final IcnsElement[] icnsElements) {
            this.icnsHeader = icnsHeader;
            this.icnsElements = icnsElements;
        }
    }
    
    static class IcnsElement
    {
        public final byte[] data;
        public final int elementSize;
        public final int type;
        
        public IcnsElement(final int type, final int elementSize, final byte[] data) {
            this.type = type;
            this.elementSize = elementSize;
            this.data = data;
        }
        
        public void dump(final PrintWriter printWriter) {
            printWriter.println("IcnsElement");
            final IcnsType anyType = IcnsType.findAnyType(this.type);
            String string;
            if (anyType == null) {
                string = "";
            }
            else {
                final StringBuilder sb = new StringBuilder();
                sb.append(" ");
                sb.append(anyType.toString());
                string = sb.toString();
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Type: 0x");
            sb2.append(Integer.toHexString(this.type));
            sb2.append(" (");
            sb2.append(IcnsType.describeType(this.type));
            sb2.append(")");
            sb2.append(string);
            printWriter.println(sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("ElementSize: ");
            sb3.append(this.elementSize);
            printWriter.println(sb3.toString());
            printWriter.println("");
        }
    }
    
    private static class IcnsHeader
    {
        public final int fileSize;
        public final int magic;
        
        public IcnsHeader(final int magic, final int fileSize) {
            this.magic = magic;
            this.fileSize = fileSize;
        }
        
        public void dump(final PrintWriter printWriter) {
            printWriter.println("IcnsHeader");
            final StringBuilder sb = new StringBuilder();
            sb.append("Magic: 0x");
            sb.append(Integer.toHexString(this.magic));
            sb.append(" (");
            sb.append(IcnsType.describeType(this.magic));
            sb.append(")");
            printWriter.println(sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("FileSize: ");
            sb2.append(this.fileSize);
            printWriter.println(sb2.toString());
            printWriter.println("");
        }
    }
}
