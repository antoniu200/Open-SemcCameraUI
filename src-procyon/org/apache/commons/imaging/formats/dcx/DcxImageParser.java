// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.dcx;

import org.apache.commons.imaging.common.BinaryOutputStream;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.PixelDensity;
import java.io.OutputStream;
import org.apache.commons.imaging.common.ImageMetadata;
import java.awt.Dimension;
import org.apache.commons.imaging.ImageInfo;
import java.util.Map;
import java.util.HashMap;
import org.apache.commons.imaging.common.bytesource.ByteSourceInputStream;
import org.apache.commons.imaging.formats.pcx.PcxImageParser;
import java.awt.image.BufferedImage;
import java.util.List;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageFormat;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.imaging.util.IoUtils;
import java.io.Closeable;
import org.apache.commons.imaging.ImageReadException;
import java.util.ArrayList;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import java.nio.ByteOrder;
import org.apache.commons.imaging.ImageParser;

public class DcxImageParser extends ImageParser
{
    private static final String[] ACCEPTED_EXTENSIONS;
    private static final String DEFAULT_EXTENSION = ".dcx";
    
    static {
        ACCEPTED_EXTENSIONS = new String[] { ".dcx" };
    }
    
    public DcxImageParser() {
        super.setByteOrder(ByteOrder.LITTLE_ENDIAN);
    }
    
    private DcxHeader readDcxHeader(final ByteSource byteSource) throws ImageReadException, IOException {
        Closeable closeable;
        try {
            final InputStream inputStream = byteSource.getInputStream();
            try {
                final int read4Bytes = BinaryFunctions.read4Bytes("Id", inputStream, "Not a Valid DCX File", this.getByteOrder());
                final ArrayList list = new ArrayList(1024);
                for (int i = 0; i < 1024; ++i) {
                    final long l = 0xFFFFFFFFL & (long)BinaryFunctions.read4Bytes("PageTable", inputStream, "Not a Valid DCX File", this.getByteOrder());
                    if (l == 0L) {
                        break;
                    }
                    list.add(l);
                }
                if (read4Bytes != 987654321) {
                    throw new ImageReadException("Not a Valid DCX File: file id incorrect");
                }
                if (list.size() == 1024) {
                    throw new ImageReadException("DCX page table not terminated by zero entry");
                }
                final Object[] array = list.toArray();
                final long[] array2 = new long[array.length];
                for (int j = 0; j < array.length; ++j) {
                    array2[j] = (long)array[j];
                }
                final DcxHeader dcxHeader = new DcxHeader(read4Bytes, array2);
                IoUtils.closeQuietly(true, inputStream);
                return dcxHeader;
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
        this.readDcxHeader(byteSource).dump(printWriter);
        return true;
    }
    
    @Override
    protected String[] getAcceptedExtensions() {
        return DcxImageParser.ACCEPTED_EXTENSIONS;
    }
    
    @Override
    protected ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[] { ImageFormats.DCX };
    }
    
    @Override
    public List<BufferedImage> getAllBufferedImages(final ByteSource byteSource) throws ImageReadException, IOException {
        final DcxHeader dcxHeader = this.readDcxHeader(byteSource);
        final ArrayList list = new ArrayList();
        final PcxImageParser pcxImageParser = new PcxImageParser();
        final long[] pageTable = dcxHeader.pageTable;
        final int length = pageTable.length;
        int i = 0;
        while (i < length) {
            final long n = pageTable[i];
            Closeable closeable = null;
            try {
                final InputStream inputStream = byteSource.getInputStream(n);
                try {
                    list.add(pcxImageParser.getBufferedImage(new ByteSourceInputStream(inputStream, null), new HashMap<String, Object>()));
                    IoUtils.closeQuietly(true, inputStream);
                    ++i;
                }
                finally {}
            }
            finally {
                closeable = null;
            }
            IoUtils.closeQuietly(false, closeable);
        }
        return list;
    }
    
    @Override
    public final BufferedImage getBufferedImage(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final List<BufferedImage> allBufferedImages = this.getAllBufferedImages(byteSource);
        if (allBufferedImages.isEmpty()) {
            return null;
        }
        return allBufferedImages.get(0);
    }
    
    @Override
    public String getDefaultExtension() {
        return ".dcx";
    }
    
    @Override
    public byte[] getICCProfileBytes(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        return null;
    }
    
    @Override
    public ImageInfo getImageInfo(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        return null;
    }
    
    @Override
    public Dimension getImageSize(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        return null;
    }
    
    @Override
    public ImageMetadata getMetadata(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        return null;
    }
    
    @Override
    public String getName() {
        return "Dcx-Custom";
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
        final HashMap<String, Object> hashMap2 = new HashMap<String, Object>();
        if (hashMap.containsKey("FORMAT")) {
            hashMap.remove("FORMAT");
        }
        if (hashMap.containsKey("PCX_COMPRESSION")) {
            hashMap2.put("PCX_COMPRESSION", hashMap.remove("PCX_COMPRESSION"));
        }
        if (hashMap.containsKey("PIXEL_DENSITY")) {
            final Object remove = hashMap.remove("PIXEL_DENSITY");
            if (remove != null) {
                if (!(remove instanceof PixelDensity)) {
                    throw new ImageWriteException("Invalid pixel density parameter");
                }
                hashMap2.put("PIXEL_DENSITY", remove);
            }
        }
        if (!hashMap.isEmpty()) {
            final Object next = hashMap.keySet().iterator().next();
            final StringBuilder sb = new StringBuilder();
            sb.append("Unknown parameter: ");
            sb.append(next);
            throw new ImageWriteException(sb.toString());
        }
        final BinaryOutputStream binaryOutputStream = new BinaryOutputStream(outputStream, ByteOrder.LITTLE_ENDIAN);
        binaryOutputStream.write4Bytes(987654321);
        binaryOutputStream.write4Bytes(4100);
        for (int i = 0; i < 1023; ++i) {
            binaryOutputStream.write4Bytes(0);
        }
        new PcxImageParser().writeImage(bufferedImage, binaryOutputStream, hashMap2);
    }
    
    private static class DcxHeader
    {
        public static final int DCX_ID = 987654321;
        public final int id;
        public final long[] pageTable;
        
        public DcxHeader(final int id, final long[] pageTable) {
            this.id = id;
            this.pageTable = pageTable;
        }
        
        public void dump(final PrintWriter printWriter) {
            printWriter.println("DcxHeader");
            final StringBuilder sb = new StringBuilder();
            sb.append("Id: 0x");
            sb.append(Integer.toHexString(this.id));
            printWriter.println(sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Pages: ");
            sb2.append(this.pageTable.length);
            printWriter.println(sb2.toString());
            printWriter.println();
        }
    }
}
