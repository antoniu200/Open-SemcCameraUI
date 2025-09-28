package org.apache.commons.imaging.formats.jpeg;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.imaging.common.BinaryConstant;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.formats.pnm.PnmConstants;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class JpegConstants {
    public static final int JFIF_MARKER = 65504;
    public static final int JPEG_APP0 = 224;
    public static final int JPEG_APP0_MARKER = 65504;
    public static final int MAX_SEGMENT_SIZE = 65535;
    public static final BinaryConstant JFIF0_SIGNATURE = new BinaryConstant(new byte[]{74, 70, 73, 70, 0});
    public static final BinaryConstant JFIF0_SIGNATURE_ALTERNATIVE = new BinaryConstant(new byte[]{74, 70, 73, 70, PnmConstants.PNM_SEPARATOR});
    public static final BinaryConstant EXIF_IDENTIFIER_CODE = new BinaryConstant(new byte[]{69, 120, 105, 102});
    public static final BinaryConstant XMP_IDENTIFIER = new BinaryConstant(new byte[]{104, 116, 116, 112, 58, 47, 47, 110, 115, 46, 97, 100, 111, 98, 101, 46, 99, 111, 109, 47, 120, 97, 112, 47, PnmConstants.PBM_TEXT_CODE, 46, 48, 47, 0});
    public static final BinaryConstant SOI = new BinaryConstant(new byte[]{-1, -40});
    public static final BinaryConstant EOI = new BinaryConstant(new byte[]{-1, -39});
    public static final int JPEG_APP1_MARKER = 65505;
    public static final int JPEG_APP2_MARKER = 65506;
    public static final int JPEG_APP13_MARKER = 65517;
    public static final int JPEG_APP14_MARKER = 65518;
    public static final int JPEG_APP15_MARKER = 65519;
    public static final int SOF0_MARKER = 65472;
    public static final int SOF1_MARKER = 65473;
    public static final int SOF2_MARKER = 65474;
    public static final int SOF3_MARKER = 65475;
    public static final int DHT_MARKER = 65476;
    public static final int SOF5_MARKER = 65477;
    public static final int SOF6_MARKER = 65478;
    public static final int SOF7_MARKER = 65479;
    public static final int SOF8_MARKER = 65480;
    public static final int SOF9_MARKER = 65481;
    public static final int SOF10_MARKER = 65482;
    public static final int SOF11_MARKER = 65483;
    public static final int DAC_MARKER = 65484;
    public static final int SOF13_MARKER = 65485;
    public static final int SOF14_MARKER = 65486;
    public static final int SOF15_MARKER = 65487;
    public static final int EOI_MARKER = 65497;
    public static final int SOS_MARKER = 65498;
    public static final int DQT_MARKER = 65499;
    public static final int DNL_MARKER = 65500;
    public static final int COM_MARKER = 65534;
    public static final List<Integer> MARKERS = Collections.unmodifiableList(Arrays.asList(224, 65504, Integer.valueOf(JPEG_APP1_MARKER), Integer.valueOf(JPEG_APP2_MARKER), Integer.valueOf(JPEG_APP13_MARKER), Integer.valueOf(JPEG_APP14_MARKER), Integer.valueOf(JPEG_APP15_MARKER), 65504, Integer.valueOf(SOF0_MARKER), Integer.valueOf(SOF1_MARKER), Integer.valueOf(SOF2_MARKER), Integer.valueOf(SOF3_MARKER), Integer.valueOf(DHT_MARKER), Integer.valueOf(SOF5_MARKER), Integer.valueOf(SOF6_MARKER), Integer.valueOf(SOF7_MARKER), Integer.valueOf(SOF8_MARKER), Integer.valueOf(SOF9_MARKER), Integer.valueOf(SOF10_MARKER), Integer.valueOf(SOF11_MARKER), Integer.valueOf(DAC_MARKER), Integer.valueOf(SOF13_MARKER), Integer.valueOf(SOF14_MARKER), Integer.valueOf(SOF15_MARKER), Integer.valueOf(EOI_MARKER), Integer.valueOf(SOS_MARKER), Integer.valueOf(DQT_MARKER), Integer.valueOf(DNL_MARKER), Integer.valueOf(COM_MARKER)));
    public static final BinaryConstant ICC_PROFILE_LABEL = new BinaryConstant(new byte[]{73, 67, 67, 95, PnmConstants.PNM_PREFIX_BYTE, 82, 79, 70, 73, 76, 69, 0});
    public static final BinaryConstant PHOTOSHOP_IDENTIFICATION_STRING = new BinaryConstant(new byte[]{PnmConstants.PNM_PREFIX_BYTE, 104, 111, 116, 111, 115, 104, 111, 112, PnmConstants.PNM_SEPARATOR, PnmConstants.PPM_TEXT_CODE, 46, 48, 0});
    public static final int CONST_8BIM = BinaryFunctions.charsToQuad('8', 'B', 'I', 'M');

    private JpegConstants() {
    }
}
