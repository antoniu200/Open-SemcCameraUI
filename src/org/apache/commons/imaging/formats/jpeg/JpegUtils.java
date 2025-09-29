package org.apache.commons.imaging.formats.jpeg;

import java.io.IOException;
import java.nio.ByteOrder;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.BinaryFileParser;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.util.Debug;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class JpegUtils extends BinaryFileParser {

    public interface Visitor {
        boolean beginSOS();

        void visitSOS(int i, byte[] bArr, byte[] bArr2);

        boolean visitSegment(int i, byte[] bArr, int i2, byte[] bArr2, byte[] bArr3) throws IOException, ImageReadException;
    }

    public static String getMarkerName(int i) {
        switch (i) {
            case JpegConstants.SOF0_MARKER /* 65472 */:
                return "SOF0_MARKER";
            case JpegConstants.SOF1_MARKER /* 65473 */:
                return "SOF1_MARKER";
            case JpegConstants.SOF2_MARKER /* 65474 */:
                return "SOF2_MARKER";
            case JpegConstants.SOF3_MARKER /* 65475 */:
                return "SOF3_MARKER";
            case JpegConstants.DHT_MARKER /* 65476 */:
                return "SOF4_MARKER";
            case JpegConstants.SOF5_MARKER /* 65477 */:
                return "SOF5_MARKER";
            case JpegConstants.SOF6_MARKER /* 65478 */:
                return "SOF6_MARKER";
            case JpegConstants.SOF7_MARKER /* 65479 */:
                return "SOF7_MARKER";
            case JpegConstants.SOF8_MARKER /* 65480 */:
                return "SOF8_MARKER";
            case JpegConstants.SOF9_MARKER /* 65481 */:
                return "SOF9_MARKER";
            case JpegConstants.SOF10_MARKER /* 65482 */:
                return "SOF10_MARKER";
            case JpegConstants.SOF11_MARKER /* 65483 */:
                return "SOF11_MARKER";
            case JpegConstants.DAC_MARKER /* 65484 */:
                return "DAC_MARKER";
            case JpegConstants.SOF13_MARKER /* 65485 */:
                return "SOF13_MARKER";
            case JpegConstants.SOF14_MARKER /* 65486 */:
                return "SOF14_MARKER";
            case JpegConstants.SOF15_MARKER /* 65487 */:
                return "SOF15_MARKER";
            default:
                switch (i) {
                    case JpegConstants.SOS_MARKER /* 65498 */:
                        return "SOS_MARKER";
                    case JpegConstants.DQT_MARKER /* 65499 */:
                        return "DQT_MARKER";
                    default:
                        switch (i) {
                            case 65504:
                                return "JFIF_MARKER";
                            case JpegConstants.JPEG_APP1_MARKER /* 65505 */:
                                return "JPEG_APP1_MARKER";
                            case JpegConstants.JPEG_APP2_MARKER /* 65506 */:
                                return "JPEG_APP2_MARKER";
                            default:
                                switch (i) {
                                    case JpegConstants.JPEG_APP13_MARKER /* 65517 */:
                                        return "JPEG_APP13_MARKER";
                                    case JpegConstants.JPEG_APP14_MARKER /* 65518 */:
                                        return "JPEG_APP14_MARKER";
                                    case JpegConstants.JPEG_APP15_MARKER /* 65519 */:
                                        return "JPEG_APP15_MARKER";
                                    default:
                                        return "Unknown";
                                }
                        }
                }
        }
    }

    public JpegUtils() {
        setByteOrder(ByteOrder.BIG_ENDIAN);
    }

    public void traverseJFIF(final ByteSource byteSource, final Visitor visitor) throws ImageReadException, IOException {
        Closeable closeable;
        try {
            final InputStream inputStream = byteSource.getInputStream();
            try {
                BinaryFunctions.readAndVerifyBytes(inputStream, JpegConstants.SOI, "Not a Valid JPEG File: doesn't begin with 0xffd8");
                int i = 0;
                Closeable[] array2;
                while (true) {
                    final byte[] array = new byte[2];
                    do {
                        array[0] = array[1];
                        array[1] = BinaryFunctions.readByte("marker", inputStream, "Could not read marker");
                    } while ((array[0] & 0xFF) != 0xFF || (array[1] & 0xFF) == 0xFF);
                    final int n = (0xFF & array[1]) | (array[0] & 0xFF) << 8;
                    if (n != 65497 && n != 65498) {
                        final byte[] bytes = BinaryFunctions.readBytes("segmentLengthBytes", inputStream, 2, "segmentLengthBytes");
                        final int uInt16 = ByteConversions.toUInt16(bytes, this.getByteOrder());
                        if (!visitor.visitSegment(n, array, uInt16, bytes, BinaryFunctions.readBytes("Segment Data", inputStream, uInt16 - 2, "Invalid Segment: insufficient data"))) {
                            array2 = new Closeable[] { inputStream };
                            break;
                        }
                        ++i;
                    }
                    else {
                        if (!visitor.beginSOS()) {
                            array2 = new Closeable[] { inputStream };
                            break;
                        }
                        visitor.visitSOS(n, array, BinaryFunctions.getStreamBytes(inputStream));
                        final StringBuilder sb = new StringBuilder();
                        sb.append(Integer.toString(i));
                        sb.append(" markers");
                        Debug.debug(sb.toString());
                        IoUtils.closeQuietly(true, inputStream);
                        return;
                    }
                }
                IoUtils.closeQuietly(true, array2);
                return;
            }
            finally {}
        }
        finally {
            closeable = null;
        }
        IoUtils.closeQuietly(false, closeable);
    }

    public void dumpJFIF(ByteSource byteSource) throws Throwable {
        traverseJFIF(byteSource, new Visitor() { // from class: org.apache.commons.imaging.formats.jpeg.JpegUtils.1
            @Override // org.apache.commons.imaging.formats.jpeg.JpegUtils.Visitor
            public boolean beginSOS() {
                return true;
            }

            @Override // org.apache.commons.imaging.formats.jpeg.JpegUtils.Visitor
            public void visitSOS(int i, byte[] bArr, byte[] bArr2) {
                Debug.debug("SOS marker.  " + bArr2.length + " bytes of image data.");
                Debug.debug("");
            }

            @Override // org.apache.commons.imaging.formats.jpeg.JpegUtils.Visitor
            public boolean visitSegment(int i, byte[] bArr, int i2, byte[] bArr2, byte[] bArr3) {
                Debug.debug("Segment marker: " + Integer.toHexString(i) + " (" + JpegUtils.getMarkerName(i) + "), " + bArr3.length + " bytes of segment data.");
                return true;
            }
        });
    }
}
