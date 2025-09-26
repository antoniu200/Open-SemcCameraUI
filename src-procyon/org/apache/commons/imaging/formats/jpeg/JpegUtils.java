// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.jpeg;

import java.io.InputStream;
import org.apache.commons.imaging.util.IoUtils;
import java.io.Closeable;
import org.apache.commons.imaging.common.ByteConversions;
import org.apache.commons.imaging.common.BinaryFunctions;
import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.util.Debug;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import java.nio.ByteOrder;
import org.apache.commons.imaging.common.BinaryFileParser;

public class JpegUtils extends BinaryFileParser
{
    public JpegUtils() {
        this.setByteOrder(ByteOrder.BIG_ENDIAN);
    }
    
    public static String getMarkerName(final int n) {
        switch (n) {
            default: {
                switch (n) {
                    default: {
                        switch (n) {
                            default: {
                                switch (n) {
                                    default: {
                                        return "Unknown";
                                    }
                                    case 65519: {
                                        return "JPEG_APP15_MARKER";
                                    }
                                    case 65518: {
                                        return "JPEG_APP14_MARKER";
                                    }
                                    case 65517: {
                                        return "JPEG_APP13_MARKER";
                                    }
                                }
                                break;
                            }
                            case 65506: {
                                return "JPEG_APP2_MARKER";
                            }
                            case 65505: {
                                return "JPEG_APP1_MARKER";
                            }
                            case 65504: {
                                return "JFIF_MARKER";
                            }
                        }
                        break;
                    }
                    case 65499: {
                        return "DQT_MARKER";
                    }
                    case 65498: {
                        return "SOS_MARKER";
                    }
                }
                break;
            }
            case 65487: {
                return "SOF15_MARKER";
            }
            case 65486: {
                return "SOF14_MARKER";
            }
            case 65485: {
                return "SOF13_MARKER";
            }
            case 65484: {
                return "DAC_MARKER";
            }
            case 65483: {
                return "SOF11_MARKER";
            }
            case 65482: {
                return "SOF10_MARKER";
            }
            case 65481: {
                return "SOF9_MARKER";
            }
            case 65480: {
                return "SOF8_MARKER";
            }
            case 65479: {
                return "SOF7_MARKER";
            }
            case 65478: {
                return "SOF6_MARKER";
            }
            case 65477: {
                return "SOF5_MARKER";
            }
            case 65476: {
                return "SOF4_MARKER";
            }
            case 65475: {
                return "SOF3_MARKER";
            }
            case 65474: {
                return "SOF2_MARKER";
            }
            case 65473: {
                return "SOF1_MARKER";
            }
            case 65472: {
                return "SOF0_MARKER";
            }
        }
    }
    
    public void dumpJFIF(final ByteSource byteSource) throws ImageReadException, IOException {
        this.traverseJFIF(byteSource, (Visitor)new Visitor(this) {
            final JpegUtils this$0;
            
            @Override
            public boolean beginSOS() {
                return true;
            }
            
            @Override
            public void visitSOS(final int n, final byte[] array, final byte[] array2) {
                final StringBuilder sb = new StringBuilder();
                sb.append("SOS marker.  ");
                sb.append(array2.length);
                sb.append(" bytes of image data.");
                Debug.debug(sb.toString());
                Debug.debug("");
            }
            
            @Override
            public boolean visitSegment(final int i, final byte[] array, final int n, final byte[] array2, final byte[] array3) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Segment marker: ");
                sb.append(Integer.toHexString(i));
                sb.append(" (");
                sb.append(JpegUtils.getMarkerName(i));
                sb.append("), ");
                sb.append(array3.length);
                sb.append(" bytes of segment data.");
                Debug.debug(sb.toString());
                return true;
            }
        });
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
    
    public interface Visitor
    {
        boolean beginSOS();
        
        void visitSOS(final int p0, final byte[] p1, final byte[] p2);
        
        boolean visitSegment(final int p0, final byte[] p1, final int p2, final byte[] p3, final byte[] p4) throws ImageReadException, IOException;
    }
}
