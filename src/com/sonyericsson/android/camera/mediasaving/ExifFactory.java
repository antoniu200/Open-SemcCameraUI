package com.sonyericsson.android.camera.mediasaving;

import android.location.Location;
import android.support.v4.media.session.PlaybackStateCompat;
import com.sonyericsson.android.camera.util.CamLog;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import kotlin.jvm.internal.ByteCompanionObject;
import org.apache.commons.imaging.formats.pnm.PnmConstants;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ExifFactory {
    private static final int MAKER_NAME_LIMITATION = 14;
    public static final String TAG = "ExifFactory";
    private static byte[] APP1_HEADER = {-1, -31, 3, 27, 69, 120, 105, 102, 0, 0};
    private static byte[] TIFF_HEADER = {77, 77, 0, 42, 0, 0, 0, 8};
    private static byte[] ZERO_IFD = {0, 10, 1, 15, 0, 2, 0, 0, 0, 0, 0, 0, 0, -122, 1, 16, 0, 2, 0, 0, 0, 30, 0, 0, 0, -108, 1, 18, 0, 3, 0, 0, 0, 1, 0, 6, 0, 0, 1, 26, 0, 5, 0, 0, 0, 1, 0, 0, 0, -78, 1, 27, 0, 5, 0, 0, 0, 1, 0, 0, 0, -70, 1, 40, 0, 3, 0, 0, 0, 1, 0, 2, 0, 0, 1, PnmConstants.PGM_TEXT_CODE, 0, 2, 0, 0, 0, 20, 0, 0, 0, -62, 2, 19, 0, 3, 0, 0, 0, 1, 0, 1, 0, 0, -121, 105, 0, 4, 0, 0, 0, 1, 0, 0, 0, -42, -120, 37, 0, 4, 0, 0, 0, 1, 0, 0, 1, -102, 0, 0, 2, -86, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 72, 0, 0, 0, 1, 0, 0, 0, 72, 0, 0, 0, 1, PnmConstants.PGM_TEXT_CODE, 48, PnmConstants.PBM_TEXT_CODE, PnmConstants.PBM_TEXT_CODE, 58, 48, PnmConstants.PBM_TEXT_CODE, 58, PnmConstants.PGM_TEXT_CODE, PnmConstants.PPM_TEXT_CODE, PnmConstants.PNM_SEPARATOR, PnmConstants.PBM_TEXT_CODE, PnmConstants.PGM_TEXT_CODE, 58, PnmConstants.PPM_TEXT_CODE, PnmConstants.PBM_RAW_CODE, 58, PnmConstants.PGM_RAW_CODE, PnmConstants.PPM_RAW_CODE, 0};
    private static byte[] EXIF_IFD = {0, 9, -112, 0, 0, 7, 0, 0, 0, 4, 48, PnmConstants.PGM_TEXT_CODE, PnmConstants.PGM_TEXT_CODE, 48, -112, 3, 0, 2, 0, 0, 0, 20, 0, 0, 1, 84, -112, 4, 0, 2, 0, 0, 0, 20, 0, 0, 1, 104, -111, 1, 0, 7, 0, 0, 0, 4, 1, 2, 3, 0, -96, 0, 0, 7, 0, 0, 0, 4, 48, PnmConstants.PBM_TEXT_CODE, 48, 48, -96, 1, 0, 3, 0, 0, 0, 1, 0, 1, 0, 0, -96, 2, 0, 4, 0, 0, 0, 1, 0, 0, 12, -64, -96, 3, 0, 4, 0, 0, 0, 1, 0, 0, 0, 0, -96, 5, 0, 4, 0, 0, 0, 1, 0, 0, 1, 124, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, PnmConstants.PGM_TEXT_CODE, 48, PnmConstants.PBM_TEXT_CODE, PnmConstants.PBM_TEXT_CODE, 58, 48, PnmConstants.PBM_TEXT_CODE, 58, PnmConstants.PGM_TEXT_CODE, PnmConstants.PPM_TEXT_CODE, PnmConstants.PNM_SEPARATOR, PnmConstants.PBM_TEXT_CODE, PnmConstants.PGM_TEXT_CODE, 58, PnmConstants.PPM_TEXT_CODE, PnmConstants.PBM_RAW_CODE, 58, PnmConstants.PGM_RAW_CODE, PnmConstants.PPM_RAW_CODE, 0, PnmConstants.PGM_TEXT_CODE, 48, PnmConstants.PBM_TEXT_CODE, PnmConstants.PBM_TEXT_CODE, 58, 48, PnmConstants.PBM_TEXT_CODE, 58, PnmConstants.PGM_TEXT_CODE, PnmConstants.PPM_TEXT_CODE, PnmConstants.PNM_SEPARATOR, PnmConstants.PBM_TEXT_CODE, PnmConstants.PGM_TEXT_CODE, 58, PnmConstants.PPM_TEXT_CODE, PnmConstants.PBM_RAW_CODE, 58, PnmConstants.PGM_RAW_CODE, PnmConstants.PPM_RAW_CODE, 0};
    private static byte[] ZERO_IFD_INT = {0, 2, 0, 1, 0, 2, 0, 0, 0, 4, 82, 57, 56, 0, 0, 2, 0, 7, 0, 0, 0, 4, 48, PnmConstants.PBM_TEXT_CODE, 48, 48, 0, 0, 0, 0};
    private static byte[] GPS_IFD = {0, 12, 0, 0, 0, 1, 0, 0, 0, 4, 2, 2, 0, 0, 0, 1, 0, 2, 0, 0, 0, 2, 78, 0, 0, 0, 0, 2, 0, 5, 0, 0, 0, 3, 0, 0, 2, 48, 0, 3, 0, 2, 0, 0, 0, 2, 69, 0, 0, 0, 0, 4, 0, 5, 0, 0, 0, 3, 0, 0, 2, 72, 0, 5, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 6, 0, 5, 0, 0, 0, 1, 0, 0, 2, 96, 0, 7, 0, 5, 0, 0, 0, 3, 0, 0, 2, 104, 0, 9, 0, 2, 0, 0, 0, 2, 65, 0, 0, 0, 0, 18, 0, 2, 0, 0, 0, 7, 0, 0, 2, ByteCompanionObject.MIN_VALUE, 0, 27, 0, 7, 0, 0, 0, 0, 0, 0, 2, -118, 0, 29, 0, 2, 0, 0, 0, 11, 0, 0, 2, -98, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 87, 71, 83, 45, 56, PnmConstants.PBM_RAW_CODE, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, PnmConstants.PGM_TEXT_CODE, 48, PnmConstants.PBM_TEXT_CODE, PnmConstants.PBM_TEXT_CODE, 58, 48, PnmConstants.PBM_TEXT_CODE, 58, PnmConstants.PGM_TEXT_CODE, PnmConstants.PPM_TEXT_CODE, 0, 0};
    private static byte[] FIRST_IFD = {0, 7, 1, 3, 0, 3, 0, 0, 0, 1, 0, 6, 0, 0, 1, 18, 0, 3, 0, 0, 0, 1, 0, 6, 0, 0, 1, 26, 0, 5, 0, 0, 0, 1, 0, 0, 3, 4, 1, 27, 0, 5, 0, 0, 0, 1, 0, 0, 3, 12, 1, 40, 0, 3, 0, 0, 0, 1, 0, 2, 0, 0, 2, 1, 0, 4, 0, 0, 0, 1, 0, 0, 3, 20, 2, 2, 0, 4, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 72, 0, 0, 0, 1, 0, 0, 0, 72, 0, 0, 0, 1};
    private static int APP1_LENGTH = (((((APP1_HEADER.length + TIFF_HEADER.length) + ZERO_IFD.length) + EXIF_IFD.length) + ZERO_IFD_INT.length) + GPS_IFD.length) + FIRST_IFD.length;

    public static int getLength() {
        return APP1_LENGTH;
    }

    public static int generate(byte[] bArr, ExifOption exifOption) {
        checkArguments(bArr, exifOption);
        int iWriteTemplate = writeTemplate(bArr);
        updateMake(bArr, exifOption.mMake);
        updateModel(bArr, exifOption.mModel);
        updateOrientation(bArr, exifOption.mOrientation);
        updateDateTime(bArr, exifOption.mDateTime);
        updatePixelXDimension(bArr, exifOption.mPixelXDimension);
        updatePixelYDimension(bArr, exifOption.mPixelYDimension);
        updateGpsFields(bArr, exifOption.mGPSOption);
        updateJpegInterchangeFormatLength(bArr, exifOption.mThumbnailDataLength);
        System.arraycopy(exifOption.mThumbnailData, 0, bArr, iWriteTemplate, (int) exifOption.mThumbnailDataLength);
        int i = (int) (iWriteTemplate + exifOption.mThumbnailDataLength);
        updateExifSize(bArr, i - 2);
        return i;
    }

    private static void checkArguments(byte[] bArr, ExifOption exifOption) {
        if (bArr == null || exifOption == null || exifOption.mModel == null || exifOption.mDateTime == null || exifOption.mThumbnailData == null) {
            throw new IllegalArgumentException("can not null");
        }
        if (exifOption.mGPSOption != null && !exifOption.mGPSOption.hasAltitude()) {
            exifOption.mGPSOption.setAltitude(0.0d);
        }
        if (exifOption.mThumbnailData.length < exifOption.mThumbnailDataLength) {
            throw new IllegalArgumentException("thumbnail data length too big");
        }
        if (bArr.length < APP1_LENGTH + exifOption.mThumbnailDataLength) {
            throw new IllegalArgumentException("buffer too short");
        }
        if (exifOption.mModel.length() == 0 || exifOption.mDateTime.length() < "YYYY:MM:DD hh:mm:ss".length()) {
            throw new IllegalArgumentException("model or datetime too short");
        }
        if (exifOption.mModel.length() >= 30 || exifOption.mDateTime.length() > "YYYY:MM:DD hh:mm:ss".length()) {
            throw new IllegalArgumentException("model or datetime too long");
        }
    }

    private static void updateMake(byte[] bArr, String str) {
        if (str.length() > 14) {
            str = str.substring(0, 14);
        }
        writeShortValue(bArr, APP1_HEADER.length + 16, writeASCIIValue(bArr, APP1_HEADER.length + 134, str) + 1);
    }

    private static void updateModel(byte[] bArr, String str) {
        writeLongValue(bArr, APP1_HEADER.length + 26, writeASCIIValue(bArr, APP1_HEADER.length + 148, str) + 1);
    }

    private static void updateOrientation(byte[] bArr, int i) {
        writeShortValue(bArr, APP1_HEADER.length + 42, i);
        writeShortValue(bArr, APP1_HEADER.length + 704, i);
    }

    private static void updateDateTime(byte[] bArr, String str) {
        writeASCIIValue(bArr, APP1_HEADER.length + 194, str);
        writeASCIIValue(bArr, APP1_HEADER.length + 340, str);
        writeASCIIValue(bArr, APP1_HEADER.length + 360, str);
    }

    private static void updatePixelXDimension(byte[] bArr, long j) {
        writeLongValue(bArr, (APP1_HEADER.length + 308) - 12, j);
    }

    private static void updatePixelYDimension(byte[] bArr, long j) {
        writeLongValue(bArr, (APP1_HEADER.length + 320) - 12, j);
    }

    private static void updateGpsFields(byte[] bArr, Location location) {
        if (location == null || !writeGpsInfoToHeader(bArr, location)) {
            removeGpsInfoFromHeader(bArr);
        }
    }

    private static boolean writeGpsInfoToHeader(byte[] bArr, Location location) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(location.getTime());
        double latitude = location.getLatitude();
        if (latitude < 0.0d) {
            writeASCIIValue(bArr, APP1_HEADER.length + 432, "S");
            latitude = -latitude;
        }
        try {
            String[] strArrSplit = Location.convert(latitude, 2).split(":");
            try {
                writeRationalValue(bArr, APP1_HEADER.length + 560, Long.parseLong(strArrSplit[0]), 1L);
                writeRationalValue(bArr, APP1_HEADER.length + 568, Long.parseLong(strArrSplit[1]), 1L);
                writeRationalValue(bArr, APP1_HEADER.length + 576, (long) (Float.parseFloat(strArrSplit[2]) * 1000.0f), 1000L);
                double longitude = location.getLongitude();
                if (longitude < 0.0d) {
                    writeASCIIValue(bArr, APP1_HEADER.length + 456, "W");
                    longitude = -longitude;
                }
                try {
                    String[] strArrSplit2 = Location.convert(longitude, 2).split(":");
                    try {
                        writeRationalValue(bArr, APP1_HEADER.length + 584, Long.parseLong(strArrSplit2[0]), 1L);
                        writeRationalValue(bArr, APP1_HEADER.length + 592, Long.parseLong(strArrSplit2[1]), 1L);
                        writeRationalValue(bArr, APP1_HEADER.length + 600, (long) (Float.parseFloat(strArrSplit2[2]) * 1000.0f), 1000L);
                        double altitude = location.getAltitude();
                        if (altitude < 0.0d) {
                            writeByteValue(bArr, APP1_HEADER.length + 480, 1);
                        }
                        writeRationalValue(bArr, APP1_HEADER.length + 608, (long) (altitude * 1000.0d), 1000L);
                        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
                        try {
                            writeRationalValue(bArr, APP1_HEADER.length + 616, calendar.get(11), 1L);
                            writeRationalValue(bArr, APP1_HEADER.length + 624, calendar.get(12) + 1, 1L);
                            writeRationalValue(bArr, APP1_HEADER.length + 632, calendar.get(13) * 1000, 1000L);
                            writeASCIIValue(bArr, APP1_HEADER.length + 670, String.format(Locale.US, "%04d:%02d:%02d", Integer.valueOf(calendar.get(1)), Integer.valueOf(calendar.get(2) + 1), Integer.valueOf(calendar.get(5))));
                            return true;
                        } catch (IllegalArgumentException unused) {
                            if (CamLog.VERBOSE) {
                                CamLog.d("failed to get gpsDateStamp");
                            }
                            return false;
                        }
                    } catch (NumberFormatException unused2) {
                        if (CamLog.VERBOSE) {
                            CamLog.d("failed to get longitude value");
                        }
                        return false;
                    }
                } catch (IllegalArgumentException unused3) {
                    if (CamLog.VERBOSE) {
                        CamLog.d("failed to change longitude format");
                    }
                    return false;
                }
            } catch (NumberFormatException unused4) {
                if (CamLog.VERBOSE) {
                    CamLog.d("failed to get latitude value");
                }
                return false;
            }
        } catch (IllegalArgumentException unused5) {
            if (CamLog.VERBOSE) {
                CamLog.d("failed to change latitude format");
            }
            return false;
        }
    }

    private static void removeGpsInfoFromHeader(byte[] bArr) {
        writeShortValue(bArr, APP1_HEADER.length + 8, 9);
        fillNullValue(bArr, APP1_HEADER.length + 118, 11);
        writeLongValue(bArr, APP1_HEADER.length + 118, 682L);
        fillNullValue(bArr, APP1_HEADER.length + 410, 272);
    }

    private static void updateJpegInterchangeFormatLength(byte[] bArr, long j) {
        writeLongValue(bArr, APP1_HEADER.length + 764, j);
    }

    private static void updateExifSize(byte[] bArr, int i) {
        writeShortValue(bArr, APP1_HEADER.length - 8, i);
    }

    private static int writeTemplate(byte[] bArr) {
        System.arraycopy(APP1_HEADER, 0, bArr, 0, APP1_HEADER.length);
        int length = APP1_HEADER.length + 0;
        System.arraycopy(TIFF_HEADER, 0, bArr, length, TIFF_HEADER.length);
        int length2 = length + TIFF_HEADER.length;
        System.arraycopy(ZERO_IFD, 0, bArr, length2, ZERO_IFD.length);
        int length3 = length2 + ZERO_IFD.length;
        System.arraycopy(EXIF_IFD, 0, bArr, length3, EXIF_IFD.length);
        int length4 = length3 + EXIF_IFD.length;
        System.arraycopy(ZERO_IFD_INT, 0, bArr, length4, ZERO_IFD_INT.length);
        int length5 = length4 + ZERO_IFD_INT.length;
        System.arraycopy(GPS_IFD, 0, bArr, length5, GPS_IFD.length);
        int length6 = length5 + GPS_IFD.length;
        System.arraycopy(FIRST_IFD, 0, bArr, length6, FIRST_IFD.length);
        return length6 + FIRST_IFD.length;
    }

    private static int writeASCIIValue(byte[] bArr, int i, String str) {
        byte[] bytes = str.getBytes(Charset.forName("US-ASCII"));
        System.arraycopy(bytes, 0, bArr, i, bytes.length);
        return bytes.length;
    }

    private static void writeByteValue(byte[] bArr, int i, int i2) {
        bArr[i + 0] = (byte) i2;
    }

    private static void writeShortValue(byte[] bArr, int i, int i2) {
        bArr[i + 0] = (byte) (i2 / 256);
        bArr[i + 1] = (byte) (i2 % 256);
    }

    private static void writeLongValue(byte[] bArr, int i, long j) {
        bArr[i + 0] = (byte) (j / 16777216);
        bArr[i + 1] = (byte) (j / PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH);
        bArr[i + 2] = (byte) (j / 256);
        bArr[i + 3] = (byte) (j % 256);
    }

    private static void writeRationalValue(byte[] bArr, int i, long j, long j2) {
        writeLongValue(bArr, i + 0, j);
        writeLongValue(bArr, i + 4, j2);
    }

    private static void fillNullValue(byte[] bArr, int i, int i2) {
        for (int i3 = 0; i3 < i2; i3++) {
            bArr[i + i3] = 0;
        }
    }
}
