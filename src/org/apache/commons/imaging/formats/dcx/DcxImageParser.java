package org.apache.commons.imaging.formats.dcx;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageParser;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.ImagingConstants;
import org.apache.commons.imaging.PixelDensity;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.common.BinaryOutputStream;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.common.bytesource.ByteSourceInputStream;
import org.apache.commons.imaging.formats.pcx.PcxConstants;
import org.apache.commons.imaging.formats.pcx.PcxImageParser;
import org.apache.commons.imaging.util.IoUtils;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class DcxImageParser extends ImageParser {
    private static final String DEFAULT_EXTENSION = ".dcx";
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
    public ImageInfo getImageInfo(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        return null;
    }

    @Override // org.apache.commons.imaging.ImageParser
    public Dimension getImageSize(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        return null;
    }

    @Override // org.apache.commons.imaging.ImageParser
    public ImageMetadata getMetadata(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        return null;
    }

    @Override // org.apache.commons.imaging.ImageParser
    public String getName() {
        return "Dcx-Custom";
    }

    @Override // org.apache.commons.imaging.ImageParser
    public String getXmpXml(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        return null;
    }

    public DcxImageParser() {
        super.setByteOrder(ByteOrder.LITTLE_ENDIAN);
    }

    @Override // org.apache.commons.imaging.ImageParser
    protected String[] getAcceptedExtensions() {
        return ACCEPTED_EXTENSIONS;
    }

    @Override // org.apache.commons.imaging.ImageParser
    protected ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[]{ImageFormats.DCX};
    }

    private static class DcxHeader {
        public static final int DCX_ID = 987654321;
        public final int id;
        public final long[] pageTable;

        public DcxHeader(int i, long[] jArr) {
            this.id = i;
            this.pageTable = jArr;
        }

        public void dump(PrintWriter printWriter) {
            printWriter.println("DcxHeader");
            printWriter.println("Id: 0x" + Integer.toHexString(this.id));
            printWriter.println("Pages: " + this.pageTable.length);
            printWriter.println();
        }
    }

    private DcxHeader readDcxHeader(ByteSource byteSource) throws Throwable {
        InputStream inputStream;
        try {
            inputStream = byteSource.getInputStream();
        } catch (Throwable th) {
            th = th;
            inputStream = null;
        }
        try {
            int i = BinaryFunctions.read4Bytes("Id", inputStream, "Not a Valid DCX File", getByteOrder());
            ArrayList arrayList = new ArrayList(1024);
            for (int i2 = 0; i2 < 1024; i2++) {
                long j = 4294967295L & BinaryFunctions.read4Bytes("PageTable", inputStream, "Not a Valid DCX File", getByteOrder());
                if (j == 0) {
                    break;
                }
                arrayList.add(Long.valueOf(j));
            }
            if (i != 987654321) {
                throw new ImageReadException("Not a Valid DCX File: file id incorrect");
            }
            if (arrayList.size() == 1024) {
                throw new ImageReadException("DCX page table not terminated by zero entry");
            }
            Object[] array = arrayList.toArray();
            long[] jArr = new long[array.length];
            for (int i3 = 0; i3 < array.length; i3++) {
                jArr[i3] = ((Long) array[i3]).longValue();
            }
            DcxHeader dcxHeader = new DcxHeader(i, jArr);
            IoUtils.closeQuietly(true, inputStream);
            return dcxHeader;
        } catch (Throwable th2) {
            th = th2;
            IoUtils.closeQuietly(false, inputStream);
            throw th;
        }
    }

    @Override // org.apache.commons.imaging.ImageParser
    public boolean dumpImageFile(PrintWriter printWriter, ByteSource byteSource) throws IOException, ImageReadException {
        readDcxHeader(byteSource).dump(printWriter);
        return true;
    }

    @Override // org.apache.commons.imaging.ImageParser
    public final BufferedImage getBufferedImage(ByteSource byteSource, Map<String, Object> map) throws Throwable {
        List<BufferedImage> allBufferedImages = getAllBufferedImages(byteSource);
        if (allBufferedImages.isEmpty()) {
            return null;
        }
        return allBufferedImages.get(0);
    }

    @Override // org.apache.commons.imaging.ImageParser
    public List<BufferedImage> getAllBufferedImages(ByteSource byteSource) throws Throwable {
        InputStream inputStream;
        DcxHeader dcxHeader = readDcxHeader(byteSource);
        ArrayList arrayList = new ArrayList();
        PcxImageParser pcxImageParser = new PcxImageParser();
        for (long j : dcxHeader.pageTable) {
            try {
                inputStream = byteSource.getInputStream(j);
            } catch (Throwable th) {
                th = th;
                inputStream = null;
            }
            try {
                arrayList.add(pcxImageParser.getBufferedImage(new ByteSourceInputStream(inputStream, null), new HashMap()));
                IoUtils.closeQuietly(true, inputStream);
            } catch (Throwable th2) {
                th = th2;
                IoUtils.closeQuietly(false, inputStream);
                throw th;
            }
        }
        return arrayList;
    }

    @Override // org.apache.commons.imaging.ImageParser
    public void writeImage(BufferedImage bufferedImage, OutputStream outputStream, Map<String, Object> map) throws ImageWriteException, IOException {
        Object objRemove;
        HashMap map2 = map == null ? new HashMap() : new HashMap(map);
        HashMap map3 = new HashMap();
        if (map2.containsKey(ImagingConstants.PARAM_KEY_FORMAT)) {
            map2.remove(ImagingConstants.PARAM_KEY_FORMAT);
        }
        if (map2.containsKey(PcxConstants.PARAM_KEY_PCX_COMPRESSION)) {
            map3.put(PcxConstants.PARAM_KEY_PCX_COMPRESSION, map2.remove(PcxConstants.PARAM_KEY_PCX_COMPRESSION));
        }
        if (map2.containsKey(ImagingConstants.PARAM_KEY_PIXEL_DENSITY) && (objRemove = map2.remove(ImagingConstants.PARAM_KEY_PIXEL_DENSITY)) != null) {
            if (!(objRemove instanceof PixelDensity)) {
                throw new ImageWriteException("Invalid pixel density parameter");
            }
            map3.put(ImagingConstants.PARAM_KEY_PIXEL_DENSITY, objRemove);
        }
        if (!map2.isEmpty()) {
            throw new ImageWriteException("Unknown parameter: " + map2.keySet().iterator().next());
        }
        BinaryOutputStream binaryOutputStream = new BinaryOutputStream(outputStream, ByteOrder.LITTLE_ENDIAN);
        binaryOutputStream.write4Bytes(DcxHeader.DCX_ID);
        binaryOutputStream.write4Bytes(4100);
        for (int i = 0; i < 1023; i++) {
            binaryOutputStream.write4Bytes(0);
        }
        new PcxImageParser().writeImage(bufferedImage, binaryOutputStream, map3);
    }
}
