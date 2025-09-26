// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.jpeg;

import org.apache.commons.imaging.formats.jpeg.segments.UnknownSegment;
import org.apache.commons.imaging.formats.jpeg.segments.DqtSegment;
import java.util.Arrays;
import org.apache.commons.imaging.formats.jpeg.iptc.IptcParser;
import org.apache.commons.imaging.formats.jpeg.xmp.JpegXmpParser;
import org.apache.commons.imaging.formats.jpeg.iptc.PhotoshopApp13Data;
import org.apache.commons.imaging.formats.jpeg.segments.App13Segment;
import org.apache.commons.imaging.common.ImageMetadata;
import java.awt.Dimension;
import org.apache.commons.imaging.formats.tiff.TiffField;
import java.io.UnsupportedEncodingException;
import org.apache.commons.imaging.formats.jpeg.segments.ComSegment;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.apache.commons.imaging.formats.jpeg.segments.App14Segment;
import org.apache.commons.imaging.formats.jpeg.segments.JfifSegment;
import org.apache.commons.imaging.formats.jpeg.segments.SofnSegment;
import java.io.PrintStream;
import org.apache.commons.imaging.formats.tiff.TiffImageParser;
import java.util.HashMap;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.jpeg.decoder.JpegDecoder;
import java.awt.image.BufferedImage;
import java.util.Map;
import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.ImageFormat;
import java.io.IOException;
import org.apache.commons.imaging.ImageInfo;
import java.text.NumberFormat;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import java.io.PrintWriter;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.formats.jpeg.segments.GenericSegment;
import java.util.ArrayList;
import org.apache.commons.imaging.util.Debug;
import java.util.Iterator;
import org.apache.commons.imaging.formats.jpeg.segments.Segment;
import java.util.Collections;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.formats.jpeg.segments.App2Segment;
import java.util.List;
import java.nio.ByteOrder;
import org.apache.commons.imaging.ImageParser;

public class JpegImageParser extends ImageParser
{
    private static final String[] ACCEPTED_EXTENSIONS;
    private static final String DEFAULT_EXTENSION = ".jpg";
    
    static {
        ACCEPTED_EXTENSIONS = new String[] { ".jpg", ".jpeg" };
    }
    
    public JpegImageParser() {
        this.setByteOrder(ByteOrder.BIG_ENDIAN);
    }
    
    private byte[] assembleSegments(final List<App2Segment> list) throws ImageReadException {
        try {
            return this.assembleSegments(list, false);
        }
        catch (final ImageReadException ex) {
            return this.assembleSegments(list, true);
        }
    }
    
    private byte[] assembleSegments(final List<App2Segment> list, final boolean b) throws ImageReadException {
        if (list.isEmpty()) {
            throw new ImageReadException("No App2 Segments Found.");
        }
        final int numMarkers = list.get(0).numMarkers;
        if (list.size() != numMarkers) {
            final StringBuilder sb = new StringBuilder();
            sb.append("App2 Segments Missing.  Found: ");
            sb.append(list.size());
            sb.append(", Expected: ");
            sb.append(numMarkers);
            sb.append(".");
            throw new ImageReadException(sb.toString());
        }
        Collections.sort((List<Comparable>)list);
        int i = 0;
        int n = 0;
        while (i < list.size()) {
            final App2Segment app2Segment = list.get(i);
            if (i + ((b ^ true) ? 1 : 0) != app2Segment.curMarker) {
                this.dumpSegments(list);
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Incoherent App2 Segment Ordering.  i: ");
                sb2.append(i);
                sb2.append(", segment[");
                sb2.append(i);
                sb2.append("].curMarker: ");
                sb2.append(app2Segment.curMarker);
                sb2.append(".");
                throw new ImageReadException(sb2.toString());
            }
            if (numMarkers != app2Segment.numMarkers) {
                this.dumpSegments(list);
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Inconsistent App2 Segment Count info.  markerCount: ");
                sb3.append(numMarkers);
                sb3.append(", segment[");
                sb3.append(i);
                sb3.append("].numMarkers: ");
                sb3.append(app2Segment.numMarkers);
                sb3.append(".");
                throw new ImageReadException(sb3.toString());
            }
            n += app2Segment.getIccBytes().length;
            ++i;
        }
        final byte[] array = new byte[n];
        final Iterator iterator = list.iterator();
        int n2 = 0;
        while (iterator.hasNext()) {
            final App2Segment app2Segment2 = (App2Segment)iterator.next();
            System.arraycopy(app2Segment2.getIccBytes(), 0, array, n2, app2Segment2.getIccBytes().length);
            n2 += app2Segment2.getIccBytes().length;
        }
        return array;
    }
    
    private void dumpSegments(final List<? extends Segment> list) {
        Debug.debug();
        final StringBuilder sb = new StringBuilder();
        sb.append("dumpSegments: ");
        sb.append(list.size());
        Debug.debug(sb.toString());
        for (int i = 0; i < list.size(); ++i) {
            final App2Segment app2Segment = list.get(i);
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(i);
            sb2.append(": ");
            sb2.append(app2Segment.curMarker);
            sb2.append(" / ");
            sb2.append(app2Segment.numMarkers);
            Debug.debug(sb2.toString());
        }
        Debug.debug();
    }
    
    private List<Segment> filterAPP1Segments(final List<Segment> list) {
        final ArrayList list2 = new ArrayList();
        for (final GenericSegment genericSegment : list) {
            if (isExifAPP1Segment(genericSegment)) {
                list2.add(genericSegment);
            }
        }
        return list2;
    }
    
    public static boolean isExifAPP1Segment(final GenericSegment genericSegment) {
        return BinaryFunctions.startsWith(genericSegment.getSegmentData(), JpegConstants.EXIF_IDENTIFIER_CODE);
    }
    
    private boolean keepMarker(final int n, final int[] array) {
        if (array == null) {
            return true;
        }
        for (int length = array.length, i = 0; i < length; ++i) {
            if (array[i] == n) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean dumpImageFile(final PrintWriter printWriter, final ByteSource byteSource) throws ImageReadException, IOException {
        printWriter.println("jpeg.dumpImageFile");
        final ImageInfo imageInfo = this.getImageInfo(byteSource);
        int i = 0;
        if (imageInfo == null) {
            return false;
        }
        imageInfo.toString(printWriter, "");
        printWriter.println("");
        final List<Segment> segments = this.readSegments(byteSource, null, false);
        if (segments == null) {
            throw new ImageReadException("No Segments Found.");
        }
        while (i < segments.size()) {
            final Segment segment = segments.get(i);
            final NumberFormat integerInstance = NumberFormat.getIntegerInstance();
            final StringBuilder sb = new StringBuilder();
            sb.append(i);
            sb.append(": marker: ");
            sb.append(Integer.toHexString(segment.marker));
            sb.append(", ");
            sb.append(segment.getDescription());
            sb.append(" (length: ");
            sb.append(integerInstance.format(segment.length));
            sb.append(")");
            printWriter.println(sb.toString());
            segment.dump(printWriter);
            ++i;
        }
        printWriter.println("");
        return true;
    }
    
    @Override
    protected String[] getAcceptedExtensions() {
        return JpegImageParser.ACCEPTED_EXTENSIONS;
    }
    
    @Override
    protected ImageFormat[] getAcceptedTypes() {
        return new ImageFormat[] { ImageFormats.JPEG };
    }
    
    @Override
    public final BufferedImage getBufferedImage(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        return new JpegDecoder().decode(byteSource);
    }
    
    @Override
    public String getDefaultExtension() {
        return ".jpg";
    }
    
    public TiffImageMetadata getExifMetadata(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final byte[] exifRawData = this.getExifRawData(byteSource);
        if (exifRawData == null) {
            return null;
        }
        Map<String, Object> map2;
        if ((map2 = map) == null) {
            map2 = new HashMap<String, Object>();
        }
        if (!map2.containsKey("READ_THUMBNAILS")) {
            map2.put("READ_THUMBNAILS", Boolean.TRUE);
        }
        return (TiffImageMetadata)new TiffImageParser().getMetadata(exifRawData, map2);
    }
    
    public byte[] getExifRawData(final ByteSource byteSource) throws ImageReadException, IOException {
        final List<Segment> segments = this.readSegments(byteSource, new int[] { 65505 }, false);
        if (segments == null || segments.isEmpty()) {
            return null;
        }
        final List<Segment> filterAPP1Segments = this.filterAPP1Segments(segments);
        if (this.getDebug()) {
            final PrintStream out = System.out;
            final StringBuilder sb = new StringBuilder();
            sb.append("exif_segments.size: ");
            sb.append(filterAPP1Segments.size());
            out.println(sb.toString());
        }
        if (filterAPP1Segments.isEmpty()) {
            return null;
        }
        if (filterAPP1Segments.size() > 1) {
            throw new ImageReadException("Imaging currently can't parse EXIF metadata split across multiple APP1 segments.  Please send this image to the Imaging project.");
        }
        return BinaryFunctions.remainingBytes("trimmed exif bytes", ((GenericSegment)filterAPP1Segments.get(0)).getSegmentData(), 6);
    }
    
    @Override
    public byte[] getICCProfileBytes(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final List<Segment> segments = this.readSegments(byteSource, new int[] { 65506 }, false);
        final ArrayList list = new ArrayList();
        if (segments != null) {
            for (final App2Segment app2Segment : segments) {
                if (app2Segment.getIccBytes() != null) {
                    list.add(app2Segment);
                }
            }
        }
        if (list.isEmpty()) {
            return null;
        }
        final byte[] assembleSegments = this.assembleSegments(list);
        if (this.getDebug()) {
            final PrintStream out = System.out;
            final StringBuilder sb = new StringBuilder();
            sb.append("bytes: ");
            sb.append(assembleSegments.length);
            out.println(sb.toString());
        }
        if (this.getDebug()) {
            System.out.println("");
        }
        return assembleSegments;
    }
    
    @Override
    public ImageInfo getImageInfo(ByteSource byteSource, Map<String, Object> string) throws ImageReadException, IOException {
        final List<Segment> segments = this.readSegments(byteSource, new int[] { 65472, 65473, 65474, 65475, 65477, 65478, 65479, 65481, 65482, 65483, 65485, 65486, 65487 }, false);
        if (segments == null) {
            throw new ImageReadException("No SOFN Data Found.");
        }
        final List<Segment> segments2 = this.readSegments(byteSource, new int[] { 65504 }, true);
        final SofnSegment sofnSegment = segments.get(0);
        if (sofnSegment == null) {
            throw new ImageReadException("No SOFN Data Found.");
        }
        final int width = sofnSegment.width;
        final int height = sofnSegment.height;
        final App14Segment app14Segment = null;
        Object o;
        if (segments2 != null && !segments2.isEmpty()) {
            o = segments2.get(0);
        }
        else {
            o = null;
        }
        final List<Segment> segments3 = this.readSegments(byteSource, new int[] { 65518 }, true);
        Object o2 = app14Segment;
        if (segments3 != null) {
            o2 = app14Segment;
            if (!segments3.isEmpty()) {
                o2 = segments3.get(0);
            }
        }
        double doubleValue = 0.0;
        double doubleValue2 = 0.0;
        double n = 0.0;
        if (o != null) {
            doubleValue = ((JfifSegment)o).xDensity;
            doubleValue2 = ((JfifSegment)o).yDensity;
            final int densityUnits = ((JfifSegment)o).densityUnits;
            final StringBuilder sb = new StringBuilder();
            sb.append("Jpeg/JFIF v.");
            sb.append(((JfifSegment)o).jfifMajorVersion);
            sb.append(".");
            sb.append(((JfifSegment)o).jfifMinorVersion);
            string = sb.toString();
            switch (densityUnits) {
                case 2: {
                    n = 2.54;
                    break;
                }
                case 1: {
                    n = 1.0;
                    break;
                }
                default:
                case 0: {
                    n = -1.0;
                    break;
                }
            }
        }
        else {
            final JpegImageMetadata jpegImageMetadata = (JpegImageMetadata)this.getMetadata(byteSource, (Map<String, Object>)string);
            Label_0558: {
                if (jpegImageMetadata != null) {
                    final TiffField exifValue = jpegImageMetadata.findEXIFValue(TiffTagConstants.TIFF_TAG_XRESOLUTION);
                    if (exifValue != null) {
                        doubleValue = ((Number)exifValue.getValue()).doubleValue();
                    }
                    else {
                        doubleValue = -1.0;
                    }
                    final TiffField exifValue2 = jpegImageMetadata.findEXIFValue(TiffTagConstants.TIFF_TAG_YRESOLUTION);
                    if (exifValue2 != null) {
                        doubleValue2 = ((Number)exifValue2.getValue()).doubleValue();
                    }
                    else {
                        doubleValue2 = -1.0;
                    }
                    final TiffField exifValue3 = jpegImageMetadata.findEXIFValue(TiffTagConstants.TIFF_TAG_RESOLUTION_UNIT);
                    if (exifValue3 != null) {
                        switch (((Number)exifValue3.getValue()).intValue()) {
                            case 3: {
                                n = 2.54;
                                break Label_0558;
                            }
                            case 2: {
                                n = 1.0;
                                break Label_0558;
                            }
                        }
                    }
                }
                else {
                    doubleValue = -1.0;
                    doubleValue2 = -1.0;
                }
                n = -1.0;
            }
            string = "Jpeg/DCM";
        }
        float n2 = -1.0f;
        if (n > 0.0) {
            final double a = doubleValue * n;
            final int n3 = (int)Math.round(a);
            n2 = (float)(width / a);
            final double a2 = doubleValue2 * n;
            final int n4 = (int)Math.round(a2);
            final float n5 = (float)(height / a2);
        }
        else {
            final float n5 = -1.0f;
            final int n4 = -1;
            final int n3 = -1;
        }
        final ArrayList list = new ArrayList();
        final Iterator<Segment> iterator = this.readSegments(byteSource, new int[] { 65534 }, false).iterator();
    Label_0709_Outer:
        while (true) {
            Label_0721: {
                if (!iterator.hasNext()) {
                    break Label_0721;
                }
                final ComSegment comSegment = (ComSegment)iterator.next();
                byteSource = (ByteSource)"";
                final int n3;
                final int n4;
                final float n5;
                int componentIdentifier;
                int numberOfComponents;
                boolean b;
                boolean b2;
                boolean b3;
                boolean b4;
                boolean b5;
                int n6;
                int verticalSamplingFactor;
                int n7;
                int n9;
                int n8;
                int n11;
                int n10;
                int n13;
                int n12;
                int n15;
                int n14;
                int n17;
                int n16;
                int n18;
                int n19;
                boolean b6;
                int precision;
                ImageFormats jpeg;
                boolean b7;
                int n20;
                int componentIdentifier2;
                int adobeColorTransform;
                boolean b8;
                int horizontalSamplingFactor;
                int length;
                int n21;
                int n22;
                int n23;
                int n24;
                int verticalSamplingFactor2;
                int length2;
                boolean b9;
                int horizontalSamplingFactor2;
                int length3;
                Label_1408_Outer:Block_84_Outer:
                while (true) {
                    try {
                        byteSource = (ByteSource)new String(comSegment.getComment(), "UTF-8");
                        list.add(byteSource);
                        continue Label_0709_Outer;
                        Label_1185: {
                            iftrue(Label_1218:)(componentIdentifier != 71);
                        }
                        while (true) {
                            Block_83: {
                                Label_0856:Block_48_Outer:
                                while (true) {
                                    Block_61: {
                                    Block_75_Outer:
                                        while (true) {
                                        Block_47:
                                            while (true) {
                                                Label_1408:Label_1802_Outer:Label_0901_Outer:Block_34_Outer:
                                                while (true) {
                                                    Block_49: {
                                                        break Block_49;
                                                        Label_1023:
                                                        iftrue(Label_1061:)(numberOfComponents != 3 || !b || !b2 || !b3 || b4 || b5);
                                                        while (true) {
                                                            Label_0901:Block_33_Outer:
                                                            while (true) {
                                                            Block_68:
                                                                while (true) {
                                                                Label_0949:
                                                                    while (true) {
                                                                        Label_1017: {
                                                                        Label_1802:
                                                                            while (true) {
                                                                            Label_1135:
                                                                                while (true) {
                                                                                    while (true) {
                                                                                        Block_28_Outer:Block_22_Outer:Block_27_Outer:
                                                                                        while (true) {
                                                                                            Block_90: {
                                                                                                Block_93: {
                                                                                                    while (true) {
                                                                                                        Label_1676: {
                                                                                                        Block_26_Outer:
                                                                                                            while (true) {
                                                                                                                while (true) {
                                                                                                                    Label_0838: {
                                                                                                                        while (true) {
                                                                                                                            while (true) {
                                                                                                                                while (true) {
                                                                                                                                    while (true) {
                                                                                                                                        Block_52: {
                                                                                                                                            Block_31: {
                                                                                                                                                while (true) {
                                                                                                                                                    Block_46: {
                                                                                                                                                        Block_51_Outer:Block_19_Outer:
                                                                                                                                                        while (true) {
                                                                                                                                                            while (true) {
                                                                                                                                                                Block_18: {
                                                                                                                                                                    while (true) {
                                                                                                                                                                        while (true) {
                                                                                                                                                                        Block_25:
                                                                                                                                                                            while (true) {
                                                                                                                                                                            Label_1641:
                                                                                                                                                                                while (true) {
                                                                                                                                                                                Label_1739:
                                                                                                                                                                                    while (true) {
                                                                                                                                                                                        Block_86: {
                                                                                                                                                                                        Block_50_Outer:
                                                                                                                                                                                            while (true) {
                                                                                                                                                                                                while (true) {
                                                                                                                                                                                                    while (true) {
                                                                                                                                                                                                        Block_23: {
                                                                                                                                                                                                            Label_0919: {
                                                                                                                                                                                                                Block_40: {
                                                                                                                                                                                                                    break Block_40;
                                                                                                                                                                                                                    iftrue(Label_1718:)((verticalSamplingFactor = n6) <= ((SofnSegment.Component)o2).verticalSamplingFactor);
                                                                                                                                                                                                                    break Block_86;
                                                                                                                                                                                                                    n7 = 1;
                                                                                                                                                                                                                    n8 = n9;
                                                                                                                                                                                                                    n10 = n11;
                                                                                                                                                                                                                    n12 = n13;
                                                                                                                                                                                                                    n14 = n15;
                                                                                                                                                                                                                    n16 = n17;
                                                                                                                                                                                                                    break Label_1408;
                                                                                                                                                                                                                    byteSource = (ByteSource)ImageInfo.ColorType.YCbCr;
                                                                                                                                                                                                                    break Label_1802;
                                                                                                                                                                                                                    ++n18;
                                                                                                                                                                                                                    n9 = n8;
                                                                                                                                                                                                                    n11 = n10;
                                                                                                                                                                                                                    n13 = n12;
                                                                                                                                                                                                                    n19 = n7;
                                                                                                                                                                                                                    n15 = n14;
                                                                                                                                                                                                                    n17 = n16;
                                                                                                                                                                                                                    break Label_1135;
                                                                                                                                                                                                                    byteSource = (ByteSource)ImageInfo.ColorType.CMYK;
                                                                                                                                                                                                                    break Label_0838;
                                                                                                                                                                                                                    byteSource = (ByteSource)ImageInfo.ColorType.GRAYSCALE;
                                                                                                                                                                                                                    break Label_0901;
                                                                                                                                                                                                                    b6 = false;
                                                                                                                                                                                                                    return new ImageInfo(string, numberOfComponents * precision, list, jpeg, "JPEG (Joint Photographic Experts Group) Format", height, "image/jpeg", 1, n4, n5, n3, n2, width, b7, b6, false, (ImageInfo.ColorType)byteSource, ImageInfo.CompressionAlgorithm.JPEG);
                                                                                                                                                                                                                    continue Label_0856;
                                                                                                                                                                                                                    componentIdentifier2 = o[n20].componentIdentifier;
                                                                                                                                                                                                                    iftrue(Label_0978:)(componentIdentifier2 != 1);
                                                                                                                                                                                                                    break Block_31;
                                                                                                                                                                                                                    Label_0828:
                                                                                                                                                                                                                    iftrue(Label_0838:)(adobeColorTransform != 2);
                                                                                                                                                                                                                    break Block_23;
                                                                                                                                                                                                                    while (true) {
                                                                                                                                                                                                                        b8 = true;
                                                                                                                                                                                                                        n16 = n17;
                                                                                                                                                                                                                        n14 = n15;
                                                                                                                                                                                                                        n7 = n19;
                                                                                                                                                                                                                        n12 = n13;
                                                                                                                                                                                                                        n10 = n11;
                                                                                                                                                                                                                        n8 = n9;
                                                                                                                                                                                                                        continue Label_1408;
                                                                                                                                                                                                                        horizontalSamplingFactor = ((SofnSegment.Component)o2).horizontalSamplingFactor;
                                                                                                                                                                                                                        break Label_1676;
                                                                                                                                                                                                                        iftrue(Label_0862:)(numberOfComponents != 1);
                                                                                                                                                                                                                        break Block_25;
                                                                                                                                                                                                                        Label_1606:
                                                                                                                                                                                                                        o = sofnSegment.getComponents();
                                                                                                                                                                                                                        length = ((JfifSegment)o).length;
                                                                                                                                                                                                                        n21 = Integer.MAX_VALUE;
                                                                                                                                                                                                                        n22 = Integer.MIN_VALUE;
                                                                                                                                                                                                                        n6 = Integer.MAX_VALUE;
                                                                                                                                                                                                                        n23 = Integer.MIN_VALUE;
                                                                                                                                                                                                                        n24 = 0;
                                                                                                                                                                                                                        break Label_1641;
                                                                                                                                                                                                                        continue Label_0856;
                                                                                                                                                                                                                        n12 = 1;
                                                                                                                                                                                                                        n8 = n9;
                                                                                                                                                                                                                        n10 = n11;
                                                                                                                                                                                                                        n7 = n19;
                                                                                                                                                                                                                        n14 = n15;
                                                                                                                                                                                                                        n16 = n17;
                                                                                                                                                                                                                        continue Label_1408;
                                                                                                                                                                                                                        numberOfComponents = sofnSegment.numberOfComponents;
                                                                                                                                                                                                                        precision = sofnSegment.precision;
                                                                                                                                                                                                                        jpeg = ImageFormats.JPEG;
                                                                                                                                                                                                                        b7 = (sofnSegment.marker == 65474);
                                                                                                                                                                                                                        byteSource = (ByteSource)ImageInfo.ColorType.UNKNOWN;
                                                                                                                                                                                                                        iftrue(Label_0841:)(o2 == null || !((App14Segment)o2).isAdobeJpegSegment());
                                                                                                                                                                                                                        break Block_18;
                                                                                                                                                                                                                        byteSource = (ByteSource)ImageInfo.ColorType.YCbCr;
                                                                                                                                                                                                                        break Label_0838;
                                                                                                                                                                                                                        Label_0907:
                                                                                                                                                                                                                        iftrue(Label_0875:)(numberOfComponents != 3 && numberOfComponents != 4);
                                                                                                                                                                                                                        break Label_0919;
                                                                                                                                                                                                                        n8 = 1;
                                                                                                                                                                                                                        n10 = n11;
                                                                                                                                                                                                                        n12 = n13;
                                                                                                                                                                                                                        n7 = n19;
                                                                                                                                                                                                                        n14 = n15;
                                                                                                                                                                                                                        n16 = n17;
                                                                                                                                                                                                                        continue Label_1408;
                                                                                                                                                                                                                        Label_1284:
                                                                                                                                                                                                                        iftrue(Label_1317:)(componentIdentifier != 67);
                                                                                                                                                                                                                        break Block_52;
                                                                                                                                                                                                                        iftrue(Label_1739:)((verticalSamplingFactor2 = n23) >= ((SofnSegment.Component)o2).verticalSamplingFactor);
                                                                                                                                                                                                                        verticalSamplingFactor2 = ((SofnSegment.Component)o2).verticalSamplingFactor;
                                                                                                                                                                                                                        break Label_1739;
                                                                                                                                                                                                                        Label_1350:
                                                                                                                                                                                                                        n8 = n9;
                                                                                                                                                                                                                        n10 = n11;
                                                                                                                                                                                                                        n12 = n13;
                                                                                                                                                                                                                        n7 = n19;
                                                                                                                                                                                                                        n14 = n15;
                                                                                                                                                                                                                        n16 = n17;
                                                                                                                                                                                                                        iftrue(Label_1408:)(componentIdentifier != 89);
                                                                                                                                                                                                                        continue Block_84_Outer;
                                                                                                                                                                                                                    }
                                                                                                                                                                                                                }
                                                                                                                                                                                                                byteSource = (ByteSource)ImageInfo.ColorType.YCbCr;
                                                                                                                                                                                                                continue Label_0856;
                                                                                                                                                                                                                iftrue(Label_1761:)(n24 >= length);
                                                                                                                                                                                                                break Block_83;
                                                                                                                                                                                                                byteSource = (ByteSource)ImageInfo.ColorType.GRAYSCALE;
                                                                                                                                                                                                                continue Label_0856;
                                                                                                                                                                                                                b2 = true;
                                                                                                                                                                                                                break Label_1017;
                                                                                                                                                                                                                byteSource = (ByteSource)ImageInfo.ColorType.YCC;
                                                                                                                                                                                                                continue Label_0856;
                                                                                                                                                                                                            }
                                                                                                                                                                                                            o = sofnSegment.getComponents();
                                                                                                                                                                                                            length2 = ((JfifSegment)o).length;
                                                                                                                                                                                                            n20 = 0;
                                                                                                                                                                                                            b = false;
                                                                                                                                                                                                            b2 = false;
                                                                                                                                                                                                            b3 = false;
                                                                                                                                                                                                            b4 = false;
                                                                                                                                                                                                            b5 = false;
                                                                                                                                                                                                            break Label_0949;
                                                                                                                                                                                                        }
                                                                                                                                                                                                        byteSource = (ByteSource)ImageInfo.ColorType.YCCK;
                                                                                                                                                                                                        Label_0875:
                                                                                                                                                                                                        continue Block_50_Outer;
                                                                                                                                                                                                    }
                                                                                                                                                                                                    Label_1218:
                                                                                                                                                                                                    iftrue(Label_1251:)(componentIdentifier != 66);
                                                                                                                                                                                                    continue Block_22_Outer;
                                                                                                                                                                                                }
                                                                                                                                                                                                byteSource = (ByteSource)ImageInfo.ColorType.YCC;
                                                                                                                                                                                                break Label_0901;
                                                                                                                                                                                                Label_1812:
                                                                                                                                                                                                iftrue(Label_1837:)(numberOfComponents != 4);
                                                                                                                                                                                                iftrue(Label_1830:)(!b9);
                                                                                                                                                                                                break Block_93;
                                                                                                                                                                                                byteSource = (ByteSource)ImageInfo.ColorType.RGB;
                                                                                                                                                                                                break Label_0838;
                                                                                                                                                                                                iftrue(Label_1023:)(n20 >= length2);
                                                                                                                                                                                                continue Block_84_Outer;
                                                                                                                                                                                            }
                                                                                                                                                                                        }
                                                                                                                                                                                        verticalSamplingFactor = ((SofnSegment.Component)o2).verticalSamplingFactor;
                                                                                                                                                                                        continue Block_27_Outer;
                                                                                                                                                                                    }
                                                                                                                                                                                    ++n24;
                                                                                                                                                                                    n21 = horizontalSamplingFactor;
                                                                                                                                                                                    n22 = horizontalSamplingFactor2;
                                                                                                                                                                                    n6 = verticalSamplingFactor;
                                                                                                                                                                                    n23 = verticalSamplingFactor2;
                                                                                                                                                                                    continue Label_1641;
                                                                                                                                                                                }
                                                                                                                                                                                Label_0802:
                                                                                                                                                                                iftrue(Label_0838:)(numberOfComponents != 4);
                                                                                                                                                                                continue Block_28_Outer;
                                                                                                                                                                            }
                                                                                                                                                                            byteSource = (ByteSource)ImageInfo.ColorType.GRAYSCALE;
                                                                                                                                                                            continue Label_0856;
                                                                                                                                                                            Label_1251:
                                                                                                                                                                            iftrue(Label_1284:)(componentIdentifier != 65);
                                                                                                                                                                            continue Label_1408_Outer;
                                                                                                                                                                        }
                                                                                                                                                                        iftrue(Label_0802:)(numberOfComponents != 3);
                                                                                                                                                                        continue Block_19_Outer;
                                                                                                                                                                    }
                                                                                                                                                                }
                                                                                                                                                                adobeColorTransform = ((App14Segment)o2).getAdobeColorTransform();
                                                                                                                                                                iftrue(Label_0815:)(adobeColorTransform != 0);
                                                                                                                                                                continue Block_33_Outer;
                                                                                                                                                            }
                                                                                                                                                            Label_1061:
                                                                                                                                                            iftrue(Label_1099:)(numberOfComponents != 4 || !b || !b2 || !b3 || !b4 || b5);
                                                                                                                                                            break Block_46;
                                                                                                                                                            iftrue(Label_1438:)(n18 >= length3);
                                                                                                                                                            break Block_47;
                                                                                                                                                            Label_1014:
                                                                                                                                                            b5 = true;
                                                                                                                                                            break Label_1017;
                                                                                                                                                            Label_1438:
                                                                                                                                                            iftrue(Label_1480:)(n9 == 0 || n11 == 0 || n13 == 0 || n19 != 0 || n15 != 0 || n17 != 0 || b8);
                                                                                                                                                            break Block_61;
                                                                                                                                                            b3 = true;
                                                                                                                                                            break Label_1017;
                                                                                                                                                            horizontalSamplingFactor2 = ((SofnSegment.Component)o2).horizontalSamplingFactor;
                                                                                                                                                            continue Block_51_Outer;
                                                                                                                                                        }
                                                                                                                                                    }
                                                                                                                                                    byteSource = (ByteSource)ImageInfo.ColorType.YCbCr;
                                                                                                                                                    break Label_0901;
                                                                                                                                                    Label_1564:
                                                                                                                                                    iftrue(Label_1606:)(!b8 || n15 == 0 || n17 == 0 || n19 == 0 || n9 != 0 || n11 != 0 || n13 != 0);
                                                                                                                                                    continue Block_33_Outer;
                                                                                                                                                }
                                                                                                                                            }
                                                                                                                                            b = true;
                                                                                                                                            break Label_1017;
                                                                                                                                            n16 = 1;
                                                                                                                                            n8 = n9;
                                                                                                                                            n10 = n11;
                                                                                                                                            n12 = n13;
                                                                                                                                            n7 = n19;
                                                                                                                                            n14 = n15;
                                                                                                                                            continue Label_1408;
                                                                                                                                            Label_1761:
                                                                                                                                            b9 = (n21 != n22 || n6 != n23);
                                                                                                                                            iftrue(Label_1812:)(numberOfComponents != 3);
                                                                                                                                            break Block_90;
                                                                                                                                        }
                                                                                                                                        n14 = 1;
                                                                                                                                        n8 = n9;
                                                                                                                                        n10 = n11;
                                                                                                                                        n12 = n13;
                                                                                                                                        n7 = n19;
                                                                                                                                        n16 = n17;
                                                                                                                                        continue Label_1408;
                                                                                                                                        Label_1317:
                                                                                                                                        iftrue(Label_1350:)(componentIdentifier != 99);
                                                                                                                                        continue Block_26_Outer;
                                                                                                                                    }
                                                                                                                                    Label_0978:
                                                                                                                                    iftrue(Label_0990:)(componentIdentifier2 != 2);
                                                                                                                                    continue Block_75_Outer;
                                                                                                                                }
                                                                                                                                Label_0891:
                                                                                                                                iftrue(Label_0907:)(numberOfComponents != 2);
                                                                                                                                continue Label_0901_Outer;
                                                                                                                            }
                                                                                                                            byteSource = (ByteSource)ImageInfo.ColorType.YCbCr;
                                                                                                                            continue Label_0856;
                                                                                                                            b6 = true;
                                                                                                                            return new ImageInfo(string, numberOfComponents * precision, list, jpeg, "JPEG (Joint Photographic Experts Group) Format", height, "image/jpeg", 1, n4, n5, n3, n2, width, b7, b6, false, (ImageInfo.ColorType)byteSource, ImageInfo.CompressionAlgorithm.JPEG);
                                                                                                                            Label_0815:
                                                                                                                            iftrue(Label_0828:)(adobeColorTransform != 1);
                                                                                                                            continue Block_48_Outer;
                                                                                                                        }
                                                                                                                    }
                                                                                                                    continue Label_0856;
                                                                                                                    Label_0862:
                                                                                                                    iftrue(Label_0875:)(numberOfComponents != 3);
                                                                                                                    continue Block_34_Outer;
                                                                                                                }
                                                                                                                Label_1805:
                                                                                                                byteSource = (ByteSource)ImageInfo.ColorType.RGB;
                                                                                                                continue Label_1802;
                                                                                                                Label_0878:
                                                                                                                iftrue(Label_0891:)(numberOfComponents != 1);
                                                                                                                continue Block_75_Outer;
                                                                                                            }
                                                                                                        }
                                                                                                        iftrue(Label_1697:)((horizontalSamplingFactor2 = n22) >= ((SofnSegment.Component)o2).horizontalSamplingFactor);
                                                                                                        continue Block_34_Outer;
                                                                                                    }
                                                                                                }
                                                                                                byteSource = (ByteSource)ImageInfo.ColorType.YCCK;
                                                                                                continue Label_1802;
                                                                                            }
                                                                                            iftrue(Label_1805:)(!b9);
                                                                                            continue Label_1802_Outer;
                                                                                        }
                                                                                        b4 = true;
                                                                                        break Label_1017;
                                                                                        Label_0841:
                                                                                        iftrue(Label_0878:)(o == null);
                                                                                        continue Block_48_Outer;
                                                                                    }
                                                                                    Label_1099:
                                                                                    o = sofnSegment.getComponents();
                                                                                    length3 = ((JfifSegment)o).length;
                                                                                    n18 = 0;
                                                                                    n9 = 0;
                                                                                    n11 = 0;
                                                                                    n13 = 0;
                                                                                    n19 = 0;
                                                                                    n15 = 0;
                                                                                    n17 = 0;
                                                                                    b8 = false;
                                                                                    continue Label_1135;
                                                                                }
                                                                                Label_1480:
                                                                                iftrue(Label_1522:)(n9 == 0 || n11 == 0 || n13 == 0 || n19 == 0 || n15 != 0 || n17 != 0 || b8);
                                                                                break Block_68;
                                                                                Label_1830:
                                                                                byteSource = (ByteSource)ImageInfo.ColorType.CMYK;
                                                                                continue Label_1802;
                                                                            }
                                                                        }
                                                                        ++n20;
                                                                        continue Label_0949;
                                                                    }
                                                                    Label_0990:
                                                                    iftrue(Label_1002:)(componentIdentifier2 != 3);
                                                                    continue Block_34_Outer;
                                                                }
                                                                byteSource = (ByteSource)ImageInfo.ColorType.RGB;
                                                                continue Label_0901;
                                                            }
                                                            Label_1002:
                                                            iftrue(Label_1014:)(componentIdentifier2 != 4);
                                                            continue;
                                                        }
                                                    }
                                                    n10 = 1;
                                                    n8 = n9;
                                                    n12 = n13;
                                                    n7 = n19;
                                                    n14 = n15;
                                                    n16 = n17;
                                                    continue Label_1408;
                                                }
                                                Label_1522:
                                                iftrue(Label_1564:)(!b8 || n15 == 0 || n17 == 0 || n9 != 0 || n11 != 0 || n13 != 0 || n19 != 0);
                                                continue;
                                            }
                                            componentIdentifier = o[n18].componentIdentifier;
                                            iftrue(Label_1185:)(componentIdentifier != 82);
                                            continue Block_75_Outer;
                                        }
                                    }
                                    byteSource = (ByteSource)ImageInfo.ColorType.RGB;
                                    continue Label_0856;
                                }
                            }
                            o2 = o[n24];
                            iftrue(Label_1676:)((horizontalSamplingFactor = n21) <= ((SofnSegment.Component)o2).horizontalSamplingFactor);
                            continue;
                        }
                    }
                    catch (final UnsupportedEncodingException ex) {
                        continue;
                    }
                    break;
                }
            }
        }
    }
    
    @Override
    public Dimension getImageSize(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final List<Segment> segments = this.readSegments(byteSource, new int[] { 65472, 65473, 65474, 65475, 65477, 65478, 65479, 65481, 65482, 65483, 65485, 65486, 65487 }, true);
        if (segments == null || segments.isEmpty()) {
            throw new ImageReadException("No JFIF Data Found.");
        }
        if (segments.size() > 1) {
            throw new ImageReadException("Redundant JFIF Data Found.");
        }
        final SofnSegment sofnSegment = segments.get(0);
        return new Dimension(sofnSegment.width, sofnSegment.height);
    }
    
    @Override
    public ImageMetadata getMetadata(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final TiffImageMetadata exifMetadata = this.getExifMetadata(byteSource, map);
        final JpegPhotoshopMetadata photoshopMetadata = this.getPhotoshopMetadata(byteSource, map);
        if (exifMetadata == null && photoshopMetadata == null) {
            return null;
        }
        return new JpegImageMetadata(photoshopMetadata, exifMetadata);
    }
    
    @Override
    public String getName() {
        return "Jpeg-Custom";
    }
    
    public JpegPhotoshopMetadata getPhotoshopMetadata(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final List<Segment> segments = this.readSegments(byteSource, new int[] { 65517 }, false);
        if (segments == null || segments.isEmpty()) {
            return null;
        }
        final Iterator iterator = segments.iterator();
        PhotoshopApp13Data photoshopApp13Data = null;
        while (iterator.hasNext()) {
            final PhotoshopApp13Data photoshopSegment = ((App13Segment)iterator.next()).parsePhotoshopSegment(map);
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
    
    @Override
    public String getXmpXml(final ByteSource byteSource, final Map<String, Object> map) throws ImageReadException, IOException {
        final ArrayList list = new ArrayList();
        new JpegUtils().traverseJFIF(byteSource, (JpegUtils.Visitor)new JpegUtils.Visitor(this, list) {
            final JpegImageParser this$0;
            final List val$result;
            
            @Override
            public boolean beginSOS() {
                return false;
            }
            
            @Override
            public void visitSOS(final int n, final byte[] array, final byte[] array2) {
            }
            
            @Override
            public boolean visitSegment(final int n, final byte[] array, final int n2, final byte[] array2, final byte[] array3) throws ImageReadException, IOException {
                if (n == 65497) {
                    return false;
                }
                if (n == 65505 && new JpegXmpParser().isXmpJpegSegment(array3)) {
                    this.val$result.add(new JpegXmpParser().parseXmpJpegSegment(array3));
                    return false;
                }
                return true;
            }
        });
        if (list.isEmpty()) {
            return null;
        }
        if (list.size() > 1) {
            throw new ImageReadException("Jpeg file contains more than one XMP segment.");
        }
        return (String)list.get(0);
    }
    
    public boolean hasExifSegment(final ByteSource byteSource) throws ImageReadException, IOException {
        final boolean[] array = { false };
        new JpegUtils().traverseJFIF(byteSource, (JpegUtils.Visitor)new JpegUtils.Visitor(this, array) {
            final JpegImageParser this$0;
            final boolean[] val$result;
            
            @Override
            public boolean beginSOS() {
                return false;
            }
            
            @Override
            public void visitSOS(final int n, final byte[] array, final byte[] array2) {
            }
            
            @Override
            public boolean visitSegment(final int n, final byte[] array, final int n2, final byte[] array2, final byte[] array3) throws ImageReadException, IOException {
                if (n == 65497) {
                    return false;
                }
                if (n == 65505 && BinaryFunctions.startsWith(array3, JpegConstants.EXIF_IDENTIFIER_CODE)) {
                    this.val$result[0] = true;
                    return false;
                }
                return true;
            }
        });
        return array[0];
    }
    
    public boolean hasIptcSegment(final ByteSource byteSource) throws ImageReadException, IOException {
        final boolean[] array = { false };
        new JpegUtils().traverseJFIF(byteSource, (JpegUtils.Visitor)new JpegUtils.Visitor(this, array) {
            final JpegImageParser this$0;
            final boolean[] val$result;
            
            @Override
            public boolean beginSOS() {
                return false;
            }
            
            @Override
            public void visitSOS(final int n, final byte[] array, final byte[] array2) {
            }
            
            @Override
            public boolean visitSegment(final int n, final byte[] array, final int n2, final byte[] array2, final byte[] array3) throws ImageReadException, IOException {
                if (n == 65497) {
                    return false;
                }
                if (n == 65517 && new IptcParser().isPhotoshopJpegSegment(array3)) {
                    this.val$result[0] = true;
                    return false;
                }
                return true;
            }
        });
        return array[0];
    }
    
    public boolean hasXmpSegment(final ByteSource byteSource) throws ImageReadException, IOException {
        final boolean[] array = { false };
        new JpegUtils().traverseJFIF(byteSource, (JpegUtils.Visitor)new JpegUtils.Visitor(this, array) {
            final JpegImageParser this$0;
            final boolean[] val$result;
            
            @Override
            public boolean beginSOS() {
                return false;
            }
            
            @Override
            public void visitSOS(final int n, final byte[] array, final byte[] array2) {
            }
            
            @Override
            public boolean visitSegment(final int n, final byte[] array, final int n2, final byte[] array2, final byte[] array3) throws ImageReadException, IOException {
                if (n == 65497) {
                    return false;
                }
                if (n == 65505 && new JpegXmpParser().isXmpJpegSegment(array3)) {
                    this.val$result[0] = true;
                    return false;
                }
                return true;
            }
        });
        return array[0];
    }
    
    public List<Segment> readSegments(final ByteSource byteSource, final int[] array, final boolean b) throws ImageReadException, IOException {
        return this.readSegments(byteSource, array, b, false);
    }
    
    public List<Segment> readSegments(final ByteSource byteSource, final int[] array, final boolean b, final boolean b2) throws ImageReadException, IOException {
        final ArrayList list = new ArrayList();
        new JpegUtils().traverseJFIF(byteSource, (JpegUtils.Visitor)new JpegUtils.Visitor(this, array, list, this, new int[] { 65472, 65473, 65474, 65475, 65477, 65478, 65479, 65481, 65482, 65483, 65485, 65486, 65487 }, b) {
            final JpegImageParser this$0;
            final int[] val$markers;
            final JpegImageParser val$parser;
            final List val$result;
            final boolean val$returnAfterFirst;
            final int[] val$sofnSegments;
            
            @Override
            public boolean beginSOS() {
                return false;
            }
            
            @Override
            public void visitSOS(final int n, final byte[] array, final byte[] array2) {
            }
            
            @Override
            public boolean visitSegment(final int key, final byte[] array, final int n, final byte[] array2, final byte[] array3) throws ImageReadException, IOException {
                if (key == 65497) {
                    return false;
                }
                if (!this.this$0.keepMarker(key, this.val$markers)) {
                    return true;
                }
                if (key == 65517) {
                    this.val$result.add(new App13Segment(this.val$parser, key, array3));
                }
                else if (key == 65518) {
                    this.val$result.add(new App14Segment(key, array3));
                }
                else if (key == 65506) {
                    this.val$result.add(new App2Segment(key, array3));
                }
                else if (key == 65504) {
                    this.val$result.add(new JfifSegment(key, array3));
                }
                else if (Arrays.binarySearch(this.val$sofnSegments, key) >= 0) {
                    this.val$result.add(new SofnSegment(key, array3));
                }
                else if (key == 65499) {
                    this.val$result.add(new DqtSegment(key, array3));
                }
                else if (key >= 65505 && key <= 65519) {
                    this.val$result.add(new UnknownSegment(key, array3));
                }
                else if (key == 65534) {
                    this.val$result.add(new ComSegment(key, array3));
                }
                return !this.val$returnAfterFirst;
            }
        });
        return list;
    }
}
