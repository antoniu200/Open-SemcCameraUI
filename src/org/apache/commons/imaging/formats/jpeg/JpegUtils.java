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

    /* JADX WARN: Code restructure failed: missing block: B:22:0x006f, code lost:
    
        if (r12.beginSOS() != false) goto L24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0071, code lost:
    
        r10 = new java.io.Closeable[]{r11};
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0076, code lost:
    
        r12.visitSOS(r5, r6, org.apache.commons.imaging.common.BinaryFunctions.getStreamBytes(r11));
        org.apache.commons.imaging.util.Debug.debug(java.lang.Integer.toString(r2) + " markers");
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0095, code lost:
    
        org.apache.commons.imaging.util.IoUtils.closeQuietly(true, r11);
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x009c, code lost:
    
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void traverseJFIF(org.apache.commons.imaging.common.bytesource.ByteSource r11, org.apache.commons.imaging.formats.jpeg.JpegUtils.Visitor r12) throws java.lang.Throwable {
        /*
            r10 = this;
            r0 = 0
            r1 = 1
            java.io.InputStream r11 = r11.getInputStream()     // Catch: java.lang.Throwable -> L9f
            org.apache.commons.imaging.common.BinaryConstant r2 = org.apache.commons.imaging.formats.jpeg.JpegConstants.SOI     // Catch: java.lang.Throwable -> L9d
            java.lang.String r3 = "Not a Valid JPEG File: doesn't begin with 0xffd8"
            org.apache.commons.imaging.common.BinaryFunctions.readAndVerifyBytes(r11, r2, r3)     // Catch: java.lang.Throwable -> L9d
            r2 = r0
        Le:
            r3 = 2
            byte[] r6 = new byte[r3]     // Catch: java.lang.Throwable -> L9d
        L11:
            r4 = r6[r1]     // Catch: java.lang.Throwable -> L9d
            r6[r0] = r4     // Catch: java.lang.Throwable -> L9d
            java.lang.String r4 = "marker"
            java.lang.String r5 = "Could not read marker"
            byte r4 = org.apache.commons.imaging.common.BinaryFunctions.readByte(r4, r11, r5)     // Catch: java.lang.Throwable -> L9d
            r6[r1] = r4     // Catch: java.lang.Throwable -> L9d
            r4 = r6[r0]     // Catch: java.lang.Throwable -> L9d
            r5 = 255(0xff, float:3.57E-43)
            r4 = r4 & r5
            if (r4 != r5) goto L11
            r4 = r6[r1]     // Catch: java.lang.Throwable -> L9d
            r4 = r4 & r5
            if (r4 == r5) goto L11
            r4 = r6[r0]     // Catch: java.lang.Throwable -> L9d
            r4 = r4 & r5
            int r4 = r4 << 8
            r7 = r6[r1]     // Catch: java.lang.Throwable -> L9d
            r5 = r5 & r7
            r5 = r5 | r4
            r4 = 65497(0xffd9, float:9.1781E-41)
            if (r5 == r4) goto L6b
            r4 = 65498(0xffda, float:9.1782E-41)
            if (r5 != r4) goto L3f
            goto L6b
        L3f:
            java.lang.String r4 = "segmentLengthBytes"
            java.lang.String r7 = "segmentLengthBytes"
            byte[] r8 = org.apache.commons.imaging.common.BinaryFunctions.readBytes(r4, r11, r3, r7)     // Catch: java.lang.Throwable -> L9d
            java.nio.ByteOrder r3 = r10.getByteOrder()     // Catch: java.lang.Throwable -> L9d
            int r7 = org.apache.commons.imaging.common.ByteConversions.toUInt16(r8, r3)     // Catch: java.lang.Throwable -> L9d
            java.lang.String r3 = "Segment Data"
            int r4 = r7 + (-2)
            java.lang.String r9 = "Invalid Segment: insufficient data"
            byte[] r9 = org.apache.commons.imaging.common.BinaryFunctions.readBytes(r3, r11, r4, r9)     // Catch: java.lang.Throwable -> L9d
            r4 = r12
            boolean r3 = r4.visitSegment(r5, r6, r7, r8, r9)     // Catch: java.lang.Throwable -> L9d
            if (r3 != 0) goto L68
            java.io.Closeable[] r10 = new java.io.Closeable[r1]
            r10[r0] = r11
        L64:
            org.apache.commons.imaging.util.IoUtils.closeQuietly(r1, r10)
            return
        L68:
            int r2 = r2 + 1
            goto Le
        L6b:
            boolean r10 = r12.beginSOS()     // Catch: java.lang.Throwable -> L9d
            if (r10 != 0) goto L76
            java.io.Closeable[] r10 = new java.io.Closeable[r1]
            r10[r0] = r11
            goto L64
        L76:
            byte[] r10 = org.apache.commons.imaging.common.BinaryFunctions.getStreamBytes(r11)     // Catch: java.lang.Throwable -> L9d
            r12.visitSOS(r5, r6, r10)     // Catch: java.lang.Throwable -> L9d
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L9d
            r10.<init>()     // Catch: java.lang.Throwable -> L9d
            java.lang.String r12 = java.lang.Integer.toString(r2)     // Catch: java.lang.Throwable -> L9d
            r10.append(r12)     // Catch: java.lang.Throwable -> L9d
            java.lang.String r12 = " markers"
            r10.append(r12)     // Catch: java.lang.Throwable -> L9d
            java.lang.String r10 = r10.toString()     // Catch: java.lang.Throwable -> L9d
            org.apache.commons.imaging.util.Debug.debug(r10)     // Catch: java.lang.Throwable -> L9d
            java.io.Closeable[] r10 = new java.io.Closeable[r1]
            r10[r0] = r11
            org.apache.commons.imaging.util.IoUtils.closeQuietly(r1, r10)
            return
        L9d:
            r10 = move-exception
            goto La1
        L9f:
            r10 = move-exception
            r11 = 0
        La1:
            java.io.Closeable[] r12 = new java.io.Closeable[r1]
            r12[r0] = r11
            org.apache.commons.imaging.util.IoUtils.closeQuietly(r0, r12)
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.imaging.formats.jpeg.JpegUtils.traverseJFIF(org.apache.commons.imaging.common.bytesource.ByteSource, org.apache.commons.imaging.formats.jpeg.JpegUtils$Visitor):void");
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
