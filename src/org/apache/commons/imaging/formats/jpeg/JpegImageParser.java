package org.apache.commons.imaging.formats.jpeg;

import com.sonyericsson.cameracommon.mediasaving.MediaSavingConstants;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.imaging.ImageFormat;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageParser;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImagingConstants;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.formats.jpeg.JpegUtils;
import org.apache.commons.imaging.formats.jpeg.decoder.JpegDecoder;
import org.apache.commons.imaging.formats.jpeg.iptc.IptcParser;
import org.apache.commons.imaging.formats.jpeg.iptc.PhotoshopApp13Data;
import org.apache.commons.imaging.formats.jpeg.segments.App13Segment;
import org.apache.commons.imaging.formats.jpeg.segments.App14Segment;
import org.apache.commons.imaging.formats.jpeg.segments.App2Segment;
import org.apache.commons.imaging.formats.jpeg.segments.ComSegment;
import org.apache.commons.imaging.formats.jpeg.segments.DqtSegment;
import org.apache.commons.imaging.formats.jpeg.segments.GenericSegment;
import org.apache.commons.imaging.formats.jpeg.segments.JfifSegment;
import org.apache.commons.imaging.formats.jpeg.segments.Segment;
import org.apache.commons.imaging.formats.jpeg.segments.SofnSegment;
import org.apache.commons.imaging.formats.jpeg.segments.UnknownSegment;
import org.apache.commons.imaging.formats.jpeg.xmp.JpegXmpParser;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffImageParser;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.apache.commons.imaging.util.Debug;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class JpegImageParser extends ImageParser {
    private static final String DEFAULT_EXTENSION = ".jpg";
    private static final String[] ACCEPTED_EXTENSIONS = {DEFAULT_EXTENSION, ".jpeg"};

    @Override // org.apache.commons.imaging.ImageParser
    public String getDefaultExtension() {
        return DEFAULT_EXTENSION;
    }

    @Override // org.apache.commons.imaging.ImageParser
    public String getName() {
        return "Jpeg-Custom";
    }

    public JpegImageParser() {
        setByteOrder(ByteOrder.BIG_ENDIAN);
    }

    @Override // org.apache.commons.imaging.ImageParser
    protected ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[]{ImageFormats.JPEG};
    }

    @Override // org.apache.commons.imaging.ImageParser
    protected String[] getAcceptedExtensions() {
        return ACCEPTED_EXTENSIONS;
    }

    @Override // org.apache.commons.imaging.ImageParser
    public final BufferedImage getBufferedImage(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        return new JpegDecoder().decode(byteSource);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean keepMarker(int i, int[] iArr) {
        if (iArr == null) {
            return true;
        }
        for (int i2 : iArr) {
            if (i2 == i) {
                return true;
            }
        }
        return false;
    }

    public List<Segment> readSegments(ByteSource byteSource, final int[] iArr, final boolean z, boolean z2) throws Throwable {
        final ArrayList arrayList = new ArrayList();
        final int[] iArr2 = {JpegConstants.SOF0_MARKER, JpegConstants.SOF1_MARKER, JpegConstants.SOF2_MARKER, JpegConstants.SOF3_MARKER, JpegConstants.SOF5_MARKER, JpegConstants.SOF6_MARKER, JpegConstants.SOF7_MARKER, JpegConstants.SOF9_MARKER, JpegConstants.SOF10_MARKER, JpegConstants.SOF11_MARKER, JpegConstants.SOF13_MARKER, JpegConstants.SOF14_MARKER, JpegConstants.SOF15_MARKER};
        new JpegUtils().traverseJFIF(byteSource, new JpegUtils.Visitor() { // from class: org.apache.commons.imaging.formats.jpeg.JpegImageParser.1
            @Override // org.apache.commons.imaging.formats.jpeg.JpegUtils.Visitor
            public boolean beginSOS() {
                return false;
            }

            @Override // org.apache.commons.imaging.formats.jpeg.JpegUtils.Visitor
            public void visitSOS(int i, byte[] bArr, byte[] bArr2) {
            }

            @Override // org.apache.commons.imaging.formats.jpeg.JpegUtils.Visitor
            public boolean visitSegment(int i, byte[] bArr, int i2, byte[] bArr2, byte[] bArr3) throws IOException, ImageReadException {
                if (i == 65497) {
                    return false;
                }
                if (!JpegImageParser.this.keepMarker(i, iArr)) {
                    return true;
                }
                if (i == 65517) {
                    arrayList.add(new App13Segment(this, i, bArr3));
                } else if (i == 65518) {
                    arrayList.add(new App14Segment(i, bArr3));
                } else if (i == 65506) {
                    arrayList.add(new App2Segment(i, bArr3));
                } else if (i == 65504) {
                    arrayList.add(new JfifSegment(i, bArr3));
                } else if (Arrays.binarySearch(iArr2, i) >= 0) {
                    arrayList.add(new SofnSegment(i, bArr3));
                } else if (i == 65499) {
                    arrayList.add(new DqtSegment(i, bArr3));
                } else if (i >= 65505 && i <= 65519) {
                    arrayList.add(new UnknownSegment(i, bArr3));
                } else if (i == 65534) {
                    arrayList.add(new ComSegment(i, bArr3));
                }
                return !z;
            }
        });
        return arrayList;
    }

    private byte[] assembleSegments(List<App2Segment> list) throws ImageReadException {
        try {
            return assembleSegments(list, false);
        } catch (ImageReadException unused) {
            return assembleSegments(list, true);
        }
    }

    private byte[] assembleSegments(List<App2Segment> list, boolean z) throws ImageReadException {
        if (list.isEmpty()) {
            throw new ImageReadException("No App2 Segments Found.");
        }
        int i = list.get(0).numMarkers;
        if (list.size() != i) {
            throw new ImageReadException("App2 Segments Missing.  Found: " + list.size() + ", Expected: " + i + ".");
        }
        Collections.sort(list);
        int i2 = !z ? 1 : 0;
        int length = 0;
        for (int i3 = 0; i3 < list.size(); i3++) {
            App2Segment app2Segment = list.get(i3);
            if (i3 + i2 != app2Segment.curMarker) {
                dumpSegments(list);
                throw new ImageReadException("Incoherent App2 Segment Ordering.  i: " + i3 + ", segment[" + i3 + "].curMarker: " + app2Segment.curMarker + ".");
            }
            if (i != app2Segment.numMarkers) {
                dumpSegments(list);
                throw new ImageReadException("Inconsistent App2 Segment Count info.  markerCount: " + i + ", segment[" + i3 + "].numMarkers: " + app2Segment.numMarkers + ".");
            }
            length += app2Segment.getIccBytes().length;
        }
        byte[] bArr = new byte[length];
        int length2 = 0;
        for (App2Segment app2Segment2 : list) {
            System.arraycopy(app2Segment2.getIccBytes(), 0, bArr, length2, app2Segment2.getIccBytes().length);
            length2 += app2Segment2.getIccBytes().length;
        }
        return bArr;
    }

    private void dumpSegments(List<? extends Segment> list) {
        Debug.debug();
        Debug.debug("dumpSegments: " + list.size());
        for (int i = 0; i < list.size(); i++) {
            App2Segment app2Segment = (App2Segment) list.get(i);
            Debug.debug(i + ": " + app2Segment.curMarker + " / " + app2Segment.numMarkers);
        }
        Debug.debug();
    }

    public List<Segment> readSegments(ByteSource byteSource, int[] iArr, boolean z) throws IOException, ImageReadException {
        return readSegments(byteSource, iArr, z, false);
    }

    @Override // org.apache.commons.imaging.ImageParser
    public byte[] getICCProfileBytes(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        List<Segment> segments = readSegments(byteSource, new int[]{JpegConstants.JPEG_APP2_MARKER}, false);
        ArrayList arrayList = new ArrayList();
        if (segments != null) {
            Iterator<Segment> it = segments.iterator();
            while (it.hasNext()) {
                App2Segment app2Segment = (App2Segment) it.next();
                if (app2Segment.getIccBytes() != null) {
                    arrayList.add(app2Segment);
                }
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        byte[] bArrAssembleSegments = assembleSegments(arrayList);
        if (getDebug()) {
            System.out.println("bytes: " + bArrAssembleSegments.length);
        }
        if (getDebug()) {
            System.out.println("");
        }
        return bArrAssembleSegments;
    }

    @Override // org.apache.commons.imaging.ImageParser
    public ImageMetadata getMetadata(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        TiffImageMetadata exifMetadata = getExifMetadata(byteSource, map);
        JpegPhotoshopMetadata photoshopMetadata = getPhotoshopMetadata(byteSource, map);
        if (exifMetadata == null && photoshopMetadata == null) {
            return null;
        }
        return new JpegImageMetadata(photoshopMetadata, exifMetadata);
    }

    public static boolean isExifAPP1Segment(GenericSegment genericSegment) {
        return BinaryFunctions.startsWith(genericSegment.getSegmentData(), JpegConstants.EXIF_IDENTIFIER_CODE);
    }

    private List<Segment> filterAPP1Segments(List<Segment> list) {
        ArrayList arrayList = new ArrayList();
        Iterator<Segment> it = list.iterator();
        while (it.hasNext()) {
            GenericSegment genericSegment = (GenericSegment) it.next();
            if (isExifAPP1Segment(genericSegment)) {
                arrayList.add(genericSegment);
            }
        }
        return arrayList;
    }

    public TiffImageMetadata getExifMetadata(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        byte[] exifRawData = getExifRawData(byteSource);
        if (exifRawData == null) {
            return null;
        }
        if (map == null) {
            map = new HashMap<>();
        }
        if (!map.containsKey(ImagingConstants.PARAM_KEY_READ_THUMBNAILS)) {
            map.put(ImagingConstants.PARAM_KEY_READ_THUMBNAILS, Boolean.TRUE);
        }
        return (TiffImageMetadata) new TiffImageParser().getMetadata(exifRawData, map);
    }

    public byte[] getExifRawData(ByteSource byteSource) throws IOException, ImageReadException {
        List<Segment> segments = readSegments(byteSource, new int[]{JpegConstants.JPEG_APP1_MARKER}, false);
        if (segments == null || segments.isEmpty()) {
            return null;
        }
        List<Segment> listFilterAPP1Segments = filterAPP1Segments(segments);
        if (getDebug()) {
            System.out.println("exif_segments.size: " + listFilterAPP1Segments.size());
        }
        if (listFilterAPP1Segments.isEmpty()) {
            return null;
        }
        if (listFilterAPP1Segments.size() > 1) {
            throw new ImageReadException("Imaging currently can't parse EXIF metadata split across multiple APP1 segments.  Please send this image to the Imaging project.");
        }
        return BinaryFunctions.remainingBytes("trimmed exif bytes", ((GenericSegment) listFilterAPP1Segments.get(0)).getSegmentData(), 6);
    }

    public boolean hasExifSegment(ByteSource byteSource) throws Throwable {
        final boolean[] zArr = {false};
        new JpegUtils().traverseJFIF(byteSource, new JpegUtils.Visitor() { // from class: org.apache.commons.imaging.formats.jpeg.JpegImageParser.2
            @Override // org.apache.commons.imaging.formats.jpeg.JpegUtils.Visitor
            public boolean beginSOS() {
                return false;
            }

            @Override // org.apache.commons.imaging.formats.jpeg.JpegUtils.Visitor
            public void visitSOS(int i, byte[] bArr, byte[] bArr2) {
            }

            @Override // org.apache.commons.imaging.formats.jpeg.JpegUtils.Visitor
            public boolean visitSegment(int i, byte[] bArr, int i2, byte[] bArr2, byte[] bArr3) throws IOException, ImageReadException {
                if (i == 65497) {
                    return false;
                }
                if (i != 65505 || !BinaryFunctions.startsWith(bArr3, JpegConstants.EXIF_IDENTIFIER_CODE)) {
                    return true;
                }
                zArr[0] = true;
                return false;
            }
        });
        return zArr[0];
    }

    public boolean hasIptcSegment(ByteSource byteSource) throws Throwable {
        final boolean[] zArr = {false};
        new JpegUtils().traverseJFIF(byteSource, new JpegUtils.Visitor() { // from class: org.apache.commons.imaging.formats.jpeg.JpegImageParser.3
            @Override // org.apache.commons.imaging.formats.jpeg.JpegUtils.Visitor
            public boolean beginSOS() {
                return false;
            }

            @Override // org.apache.commons.imaging.formats.jpeg.JpegUtils.Visitor
            public void visitSOS(int i, byte[] bArr, byte[] bArr2) {
            }

            @Override // org.apache.commons.imaging.formats.jpeg.JpegUtils.Visitor
            public boolean visitSegment(int i, byte[] bArr, int i2, byte[] bArr2, byte[] bArr3) throws IOException, ImageReadException {
                if (i == 65497) {
                    return false;
                }
                if (i != 65517 || !new IptcParser().isPhotoshopJpegSegment(bArr3)) {
                    return true;
                }
                zArr[0] = true;
                return false;
            }
        });
        return zArr[0];
    }

    public boolean hasXmpSegment(ByteSource byteSource) throws Throwable {
        final boolean[] zArr = {false};
        new JpegUtils().traverseJFIF(byteSource, new JpegUtils.Visitor() { // from class: org.apache.commons.imaging.formats.jpeg.JpegImageParser.4
            @Override // org.apache.commons.imaging.formats.jpeg.JpegUtils.Visitor
            public boolean beginSOS() {
                return false;
            }

            @Override // org.apache.commons.imaging.formats.jpeg.JpegUtils.Visitor
            public void visitSOS(int i, byte[] bArr, byte[] bArr2) {
            }

            @Override // org.apache.commons.imaging.formats.jpeg.JpegUtils.Visitor
            public boolean visitSegment(int i, byte[] bArr, int i2, byte[] bArr2, byte[] bArr3) throws IOException, ImageReadException {
                if (i == 65497) {
                    return false;
                }
                if (i != 65505 || !new JpegXmpParser().isXmpJpegSegment(bArr3)) {
                    return true;
                }
                zArr[0] = true;
                return false;
            }
        });
        return zArr[0];
    }

    @Override // org.apache.commons.imaging.ImageParser
    public String getXmpXml(ByteSource byteSource, Map<String, Object> map) throws Throwable {
        final ArrayList arrayList = new ArrayList();
        new JpegUtils().traverseJFIF(byteSource, new JpegUtils.Visitor() { // from class: org.apache.commons.imaging.formats.jpeg.JpegImageParser.5
            @Override // org.apache.commons.imaging.formats.jpeg.JpegUtils.Visitor
            public boolean beginSOS() {
                return false;
            }

            @Override // org.apache.commons.imaging.formats.jpeg.JpegUtils.Visitor
            public void visitSOS(int i, byte[] bArr, byte[] bArr2) {
            }

            @Override // org.apache.commons.imaging.formats.jpeg.JpegUtils.Visitor
            public boolean visitSegment(int i, byte[] bArr, int i2, byte[] bArr2, byte[] bArr3) throws IOException, ImageReadException {
                if (i == 65497) {
                    return false;
                }
                if (i != 65505 || !new JpegXmpParser().isXmpJpegSegment(bArr3)) {
                    return true;
                }
                arrayList.add(new JpegXmpParser().parseXmpJpegSegment(bArr3));
                return false;
            }
        });
        if (arrayList.isEmpty()) {
            return null;
        }
        if (arrayList.size() > 1) {
            throw new ImageReadException("Jpeg file contains more than one XMP segment.");
        }
        return (String) arrayList.get(0);
    }

    public JpegPhotoshopMetadata getPhotoshopMetadata(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        List<Segment> segments = readSegments(byteSource, new int[]{JpegConstants.JPEG_APP13_MARKER}, false);
        if (segments == null || segments.isEmpty()) {
            return null;
        }
        Iterator<Segment> it = segments.iterator();
        PhotoshopApp13Data photoshopApp13Data = null;
        while (it.hasNext()) {
            PhotoshopApp13Data photoshopSegment = ((App13Segment) it.next()).parsePhotoshopSegment(map);
            if (photoshopSegment != null && photoshopApp13Data != null) {
                throw new ImageReadException("Jpeg contains more than one Photoshop App13 segment.");
            }
            photoshopApp13Data = photoshopSegment;
        }
        if (photoshopApp13Data == null) {
            return null;
        }
        return new JpegPhotoshopMetadata(photoshopApp13Data);
    }

    @Override // org.apache.commons.imaging.ImageParser
    public Dimension getImageSize(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        List<Segment> segments = readSegments(byteSource, new int[]{JpegConstants.SOF0_MARKER, JpegConstants.SOF1_MARKER, JpegConstants.SOF2_MARKER, JpegConstants.SOF3_MARKER, JpegConstants.SOF5_MARKER, JpegConstants.SOF6_MARKER, JpegConstants.SOF7_MARKER, JpegConstants.SOF9_MARKER, JpegConstants.SOF10_MARKER, JpegConstants.SOF11_MARKER, JpegConstants.SOF13_MARKER, JpegConstants.SOF14_MARKER, JpegConstants.SOF15_MARKER}, true);
        if (segments == null || segments.isEmpty()) {
            throw new ImageReadException("No JFIF Data Found.");
        }
        if (segments.size() > 1) {
            throw new ImageReadException("Redundant JFIF Data Found.");
        }
        SofnSegment sofnSegment = (SofnSegment) segments.get(0);
        return new Dimension(sofnSegment.width, sofnSegment.height);
    }

    @Override // org.apache.commons.imaging.ImageParser
    public ImageInfo getImageInfo(ByteSource byteSource, Map<String, Object> map) throws IOException, ImageReadException {
        double d;
        double d2;
        double d3;
        String str;
        float f;
        int iRound;
        int iRound2;
        ImageInfo.ColorType colorType;
        int i;
        int i2;
        boolean z;
        ImageInfo.ColorType colorType2;
        ImageInfo.ColorType colorType3;
        ImageInfo.ColorType colorType4;
        ImageInfo.ColorType colorType5;
        boolean z2;
        List<Segment> segments = readSegments(byteSource, new int[]{JpegConstants.SOF0_MARKER, JpegConstants.SOF1_MARKER, JpegConstants.SOF2_MARKER, JpegConstants.SOF3_MARKER, JpegConstants.SOF5_MARKER, JpegConstants.SOF6_MARKER, JpegConstants.SOF7_MARKER, JpegConstants.SOF9_MARKER, JpegConstants.SOF10_MARKER, JpegConstants.SOF11_MARKER, JpegConstants.SOF13_MARKER, JpegConstants.SOF14_MARKER, JpegConstants.SOF15_MARKER}, false);
        if (segments == null) {
            throw new ImageReadException("No SOFN Data Found.");
        }
        List<Segment> segments2 = readSegments(byteSource, new int[]{65504}, true);
        SofnSegment sofnSegment = (SofnSegment) segments.get(0);
        if (sofnSegment == null) {
            throw new ImageReadException("No SOFN Data Found.");
        }
        int i3 = sofnSegment.width;
        int i4 = sofnSegment.height;
        App14Segment app14Segment = null;
        JfifSegment jfifSegment = (segments2 == null || segments2.isEmpty()) ? null : (JfifSegment) segments2.get(0);
        List<Segment> segments3 = readSegments(byteSource, new int[]{JpegConstants.JPEG_APP14_MARKER}, true);
        if (segments3 != null && !segments3.isEmpty()) {
            app14Segment = (App14Segment) segments3.get(0);
        }
        if (jfifSegment != null) {
            d = jfifSegment.xDensity;
            d2 = jfifSegment.yDensity;
            int i5 = jfifSegment.densityUnits;
            str = "Jpeg/JFIF v." + jfifSegment.jfifMajorVersion + "." + jfifSegment.jfifMinorVersion;
            switch (i5) {
                case 0:
                default:
                    d3 = -1.0d;
                    break;
                case 1:
                    d3 = 1.0d;
                    break;
                case 2:
                    d3 = 2.54d;
                    break;
            }
        } else {
            JpegImageMetadata jpegImageMetadata = (JpegImageMetadata) getMetadata(byteSource, map);
            if (jpegImageMetadata != null) {
                TiffField tiffFieldFindEXIFValue = jpegImageMetadata.findEXIFValue(TiffTagConstants.TIFF_TAG_XRESOLUTION);
                double dDoubleValue = tiffFieldFindEXIFValue != null ? ((Number) tiffFieldFindEXIFValue.getValue()).doubleValue() : -1.0d;
                TiffField tiffFieldFindEXIFValue2 = jpegImageMetadata.findEXIFValue(TiffTagConstants.TIFF_TAG_YRESOLUTION);
                double dDoubleValue2 = tiffFieldFindEXIFValue2 != null ? ((Number) tiffFieldFindEXIFValue2.getValue()).doubleValue() : -1.0d;
                TiffField tiffFieldFindEXIFValue3 = jpegImageMetadata.findEXIFValue(TiffTagConstants.TIFF_TAG_RESOLUTION_UNIT);
                if (tiffFieldFindEXIFValue3 != null) {
                    switch (((Number) tiffFieldFindEXIFValue3.getValue()).intValue()) {
                        case 2:
                            d2 = dDoubleValue2;
                            d = dDoubleValue;
                            d3 = 1.0d;
                            break;
                        case 3:
                            d2 = dDoubleValue2;
                            d = dDoubleValue;
                            d3 = 2.54d;
                            break;
                    }
                    str = "Jpeg/DCM";
                }
                d2 = dDoubleValue2;
                d = dDoubleValue;
            } else {
                d = -1.0d;
                d2 = -1.0d;
            }
            d3 = -1.0d;
            str = "Jpeg/DCM";
        }
        float f2 = -1.0f;
        if (d3 > 0.0d) {
            double d4 = d * d3;
            iRound2 = (int) Math.round(d4);
            double d5 = d2 * d3;
            float f3 = (float) (i4 / d5);
            f2 = (float) (i3 / d4);
            iRound = (int) Math.round(d5);
            f = f3;
        } else {
            f = -1.0f;
            iRound = -1;
            iRound2 = -1;
        }
        ArrayList arrayList = new ArrayList();
        Iterator<Segment> it = readSegments(byteSource, new int[]{JpegConstants.COM_MARKER}, false).iterator();
        while (it.hasNext()) {
            String str2 = "";
            try {
                str2 = new String(((ComSegment) it.next()).getComment(), "UTF-8");
            } catch (UnsupportedEncodingException unused) {
            }
            arrayList.add(str2);
        }
        int i6 = sofnSegment.numberOfComponents;
        int i7 = i6 * sofnSegment.precision;
        ImageFormats imageFormats = ImageFormats.JPEG;
        boolean z3 = sofnSegment.marker == 65474;
        ImageInfo.ColorType colorType6 = ImageInfo.ColorType.UNKNOWN;
        int i8 = 3;
        if (app14Segment != null && app14Segment.isAdobeJpegSegment()) {
            int adobeColorTransform = app14Segment.getAdobeColorTransform();
            if (adobeColorTransform == 0) {
                if (i6 == 3) {
                    colorType6 = ImageInfo.ColorType.RGB;
                } else if (i6 == 4) {
                    colorType6 = ImageInfo.ColorType.CMYK;
                }
            } else if (adobeColorTransform == 1) {
                colorType6 = ImageInfo.ColorType.YCbCr;
            } else if (adobeColorTransform == 2) {
                colorType6 = ImageInfo.ColorType.YCCK;
            }
            colorType3 = colorType6;
        } else {
            if (jfifSegment != null) {
                if (i6 == 1) {
                    colorType5 = ImageInfo.ColorType.GRAYSCALE;
                } else {
                    if (i6 == 3) {
                        colorType5 = ImageInfo.ColorType.YCbCr;
                    }
                    colorType = colorType6;
                    i = i3;
                    colorType3 = colorType;
                }
                colorType3 = colorType5;
            } else {
                if (i6 == 1) {
                    colorType5 = ImageInfo.ColorType.GRAYSCALE;
                } else {
                    if (i6 == 2) {
                        colorType4 = ImageInfo.ColorType.GRAYSCALE;
                    } else {
                        if (i6 == 3 || i6 == 4) {
                            SofnSegment.Component[] components = sofnSegment.getComponents();
                            int length = components.length;
                            int i9 = 0;
                            boolean z4 = false;
                            boolean z5 = false;
                            boolean z6 = false;
                            boolean z7 = false;
                            boolean z8 = false;
                            while (i9 < length) {
                                int i10 = components[i9].componentIdentifier;
                                ImageInfo.ColorType colorType7 = colorType6;
                                if (i10 == 1) {
                                    z4 = true;
                                } else if (i10 == 2) {
                                    z5 = true;
                                } else if (i10 == 3) {
                                    z6 = true;
                                } else if (i10 == 4) {
                                    z7 = true;
                                } else {
                                    z8 = true;
                                }
                                i9++;
                                colorType6 = colorType7;
                                i8 = 3;
                            }
                            colorType = colorType6;
                            if (i6 == i8 && z4 && z5 && z6 && !z7 && !z8) {
                                colorType5 = ImageInfo.ColorType.YCbCr;
                            } else if (i6 == 4 && z4 && z5 && z6 && z7 && !z8) {
                                colorType4 = ImageInfo.ColorType.YCbCr;
                            } else {
                                SofnSegment.Component[] components2 = sofnSegment.getComponents();
                                int length2 = components2.length;
                                int i11 = 0;
                                boolean z9 = false;
                                boolean z10 = false;
                                boolean z11 = false;
                                boolean z12 = false;
                                boolean z13 = false;
                                boolean z14 = false;
                                boolean z15 = false;
                                while (i11 < length2) {
                                    int i12 = length2;
                                    int i13 = components2[i11].componentIdentifier;
                                    SofnSegment.Component[] componentArr = components2;
                                    if (i13 == 82) {
                                        z9 = true;
                                    } else if (i13 == 71) {
                                        z10 = true;
                                    } else if (i13 == 66) {
                                        z11 = true;
                                    } else if (i13 == 65) {
                                        z12 = true;
                                    } else if (i13 == 67) {
                                        z13 = true;
                                    } else if (i13 == 99) {
                                        z14 = true;
                                    } else if (i13 == 89) {
                                        z15 = true;
                                    }
                                    i11++;
                                    length2 = i12;
                                    components2 = componentArr;
                                }
                                if (z9 && z10 && z11 && !z12 && !z13 && !z14 && !z15) {
                                    colorType5 = ImageInfo.ColorType.RGB;
                                } else if (z9 && z10 && z11 && z12 && !z13 && !z14 && !z15) {
                                    colorType4 = ImageInfo.ColorType.RGB;
                                } else if (z15 && z13 && z14 && !z9 && !z10 && !z11 && !z12) {
                                    colorType5 = ImageInfo.ColorType.YCC;
                                } else if (z15 && z13 && z14 && z12 && !z9 && !z10 && !z11) {
                                    colorType4 = ImageInfo.ColorType.YCC;
                                } else {
                                    SofnSegment.Component[] components3 = sofnSegment.getComponents();
                                    int length3 = components3.length;
                                    int i14 = Integer.MAX_VALUE;
                                    int i15 = Integer.MIN_VALUE;
                                    int i16 = Integer.MAX_VALUE;
                                    i = i3;
                                    int i17 = Integer.MIN_VALUE;
                                    int i18 = 0;
                                    while (i18 < length3) {
                                        int i19 = length3;
                                        SofnSegment.Component component = components3[i18];
                                        SofnSegment.Component[] componentArr2 = components3;
                                        if (i14 > component.horizontalSamplingFactor) {
                                            i14 = component.horizontalSamplingFactor;
                                        }
                                        if (i15 < component.horizontalSamplingFactor) {
                                            i15 = component.horizontalSamplingFactor;
                                        }
                                        if (i16 > component.verticalSamplingFactor) {
                                            i16 = component.verticalSamplingFactor;
                                        }
                                        if (i17 < component.verticalSamplingFactor) {
                                            i17 = component.verticalSamplingFactor;
                                        }
                                        i18++;
                                        length3 = i19;
                                        components3 = componentArr2;
                                    }
                                    if (i14 == i15 && i16 == i17) {
                                        i2 = 3;
                                        z = false;
                                    } else {
                                        i2 = 3;
                                        z = true;
                                    }
                                    if (i6 == i2) {
                                        if (z) {
                                            colorType2 = ImageInfo.ColorType.YCbCr;
                                        } else {
                                            colorType2 = ImageInfo.ColorType.RGB;
                                        }
                                    } else if (i6 == 4) {
                                        if (z) {
                                            colorType2 = ImageInfo.ColorType.YCCK;
                                        } else {
                                            colorType2 = ImageInfo.ColorType.CMYK;
                                        }
                                    }
                                    colorType3 = colorType2;
                                }
                            }
                        } else {
                            colorType = colorType6;
                            i = i3;
                        }
                        colorType3 = colorType;
                    }
                    colorType3 = colorType4;
                    i = i3;
                    z2 = true;
                    return new ImageInfo(str, i7, arrayList, imageFormats, "JPEG (Joint Photographic Experts Group) Format", i4, MediaSavingConstants.MEDIA_TYPE_JPEG_MIME, 1, iRound, f, iRound2, f2, i, z3, z2, false, colorType3, ImageInfo.CompressionAlgorithm.JPEG);
                }
                colorType3 = colorType5;
            }
            z2 = false;
            return new ImageInfo(str, i7, arrayList, imageFormats, "JPEG (Joint Photographic Experts Group) Format", i4, MediaSavingConstants.MEDIA_TYPE_JPEG_MIME, 1, iRound, f, iRound2, f2, i, z3, z2, false, colorType3, ImageInfo.CompressionAlgorithm.JPEG);
        }
        i = i3;
        z2 = false;
        return new ImageInfo(str, i7, arrayList, imageFormats, "JPEG (Joint Photographic Experts Group) Format", i4, MediaSavingConstants.MEDIA_TYPE_JPEG_MIME, 1, iRound, f, iRound2, f2, i, z3, z2, false, colorType3, ImageInfo.CompressionAlgorithm.JPEG);
    }

    @Override // org.apache.commons.imaging.ImageParser
    public boolean dumpImageFile(PrintWriter printWriter, ByteSource byteSource) throws IOException, ImageReadException {
        printWriter.println("jpeg.dumpImageFile");
        ImageInfo imageInfo = getImageInfo(byteSource);
        if (imageInfo == null) {
            return false;
        }
        imageInfo.toString(printWriter, "");
        printWriter.println("");
        List<Segment> segments = readSegments(byteSource, null, false);
        if (segments == null) {
            throw new ImageReadException("No Segments Found.");
        }
        for (int i = 0; i < segments.size(); i++) {
            Segment segment = segments.get(i);
            printWriter.println(i + ": marker: " + Integer.toHexString(segment.marker) + ", " + segment.getDescription() + " (length: " + NumberFormat.getIntegerInstance().format(segment.length) + ")");
            segment.dump(printWriter);
        }
        printWriter.println("");
        return true;
    }
}
