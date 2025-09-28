package org.apache.commons.imaging.formats.tiff.constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoAscii;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoByte;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoGpsText;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoRational;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoShort;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class GpsTagConstants {
    public static final int GPS_TAG_GPS_ALTITUDE_REF_VALUE_ABOVE_SEA_LEVEL = 0;
    public static final int GPS_TAG_GPS_ALTITUDE_REF_VALUE_BELOW_SEA_LEVEL = 1;
    public static final String GPS_TAG_GPS_DEST_BEARING_REF_VALUE_MAGNETIC_NORTH = "M";
    public static final String GPS_TAG_GPS_DEST_BEARING_REF_VALUE_TRUE_NORTH = "T";
    public static final String GPS_TAG_GPS_DEST_DISTANCE_REF_VALUE_KILOMETERS = "K";
    public static final String GPS_TAG_GPS_DEST_DISTANCE_REF_VALUE_MILES = "M";
    public static final String GPS_TAG_GPS_DEST_DISTANCE_REF_VALUE_NAUTICAL_MILES = "N";
    public static final String GPS_TAG_GPS_DEST_LATITUDE_REF_VALUE_NORTH = "N";
    public static final String GPS_TAG_GPS_DEST_LATITUDE_REF_VALUE_SOUTH = "S";
    public static final String GPS_TAG_GPS_DEST_LONGITUDE_REF_VALUE_EAST = "E";
    public static final String GPS_TAG_GPS_DEST_LONGITUDE_REF_VALUE_WEST = "W";
    public static final int GPS_TAG_GPS_DIFFERENTIAL_VALUE_DIFFERENTIAL_CORRECTED = 1;
    public static final int GPS_TAG_GPS_DIFFERENTIAL_VALUE_NO_CORRECTION = 0;
    public static final String GPS_TAG_GPS_IMG_DIRECTION_REF_VALUE_MAGNETIC_NORTH = "M";
    public static final String GPS_TAG_GPS_IMG_DIRECTION_REF_VALUE_TRUE_NORTH = "T";
    public static final String GPS_TAG_GPS_LATITUDE_REF_VALUE_NORTH = "N";
    public static final String GPS_TAG_GPS_LATITUDE_REF_VALUE_SOUTH = "S";
    public static final String GPS_TAG_GPS_LONGITUDE_REF_VALUE_EAST = "E";
    public static final String GPS_TAG_GPS_LONGITUDE_REF_VALUE_WEST = "W";
    public static final int GPS_TAG_GPS_MEASURE_MODE_VALUE_2_DIMENSIONAL_MEASUREMENT = 2;
    public static final int GPS_TAG_GPS_MEASURE_MODE_VALUE_3_DIMENSIONAL_MEASUREMENT = 3;
    public static final String GPS_TAG_GPS_SPEED_REF_VALUE_KMPH = "K";
    public static final String GPS_TAG_GPS_SPEED_REF_VALUE_KNOTS = "N";
    public static final String GPS_TAG_GPS_SPEED_REF_VALUE_MPH = "M";
    public static final String GPS_TAG_GPS_STATUS_VALUE_MEASUREMENT_INTEROPERABILITY = "V";
    public static final String GPS_TAG_GPS_STATUS_VALUE_MEASUREMENT_IN_PROGRESS = "A";
    public static final String GPS_TAG_GPS_TRACK_REF_VALUE_MAGNETIC_NORTH = "M";
    public static final String GPS_TAG_GPS_TRACK_REF_VALUE_TRUE_NORTH = "T";
    public static final TagInfoByte GPS_TAG_GPS_VERSION_ID = new TagInfoByte("GPSVersionID", 0, 4, TiffDirectoryType.EXIF_DIRECTORY_GPS);
    public static final byte[] GPS_VERSION = {2, 3, 0, 0};
    public static final TagInfoAscii GPS_TAG_GPS_LATITUDE_REF = new TagInfoAscii("GPSLatitudeRef", 1, 2, TiffDirectoryType.EXIF_DIRECTORY_GPS);
    public static final TagInfoRational GPS_TAG_GPS_LATITUDE = new TagInfoRational("GPSLatitude", 2, 3, TiffDirectoryType.EXIF_DIRECTORY_GPS);
    public static final TagInfoAscii GPS_TAG_GPS_LONGITUDE_REF = new TagInfoAscii("GPSLongitudeRef", 3, 2, TiffDirectoryType.EXIF_DIRECTORY_GPS);
    public static final TagInfoRational GPS_TAG_GPS_LONGITUDE = new TagInfoRational("GPSLongitude", 4, 3, TiffDirectoryType.EXIF_DIRECTORY_GPS);
    public static final TagInfoByte GPS_TAG_GPS_ALTITUDE_REF = new TagInfoByte("GPSAltitudeRef", 5, 1, TiffDirectoryType.EXIF_DIRECTORY_GPS);
    public static final TagInfoRational GPS_TAG_GPS_ALTITUDE = new TagInfoRational("GPSAltitude", 6, 1, TiffDirectoryType.EXIF_DIRECTORY_GPS);
    public static final TagInfoRational GPS_TAG_GPS_TIME_STAMP = new TagInfoRational("GPSTimeStamp", 7, 3, TiffDirectoryType.EXIF_DIRECTORY_GPS);
    public static final TagInfoAscii GPS_TAG_GPS_SATELLITES = new TagInfoAscii("GPSSatellites", 8, -1, TiffDirectoryType.EXIF_DIRECTORY_GPS);
    public static final TagInfoAscii GPS_TAG_GPS_STATUS = new TagInfoAscii("GPSStatus", 9, 2, TiffDirectoryType.EXIF_DIRECTORY_GPS);
    public static final TagInfoAscii GPS_TAG_GPS_MEASURE_MODE = new TagInfoAscii("GPSMeasureMode", 10, 2, TiffDirectoryType.EXIF_DIRECTORY_GPS);
    public static final TagInfoRational GPS_TAG_GPS_DOP = new TagInfoRational("GPSDOP", 11, 1, TiffDirectoryType.EXIF_DIRECTORY_GPS);
    public static final TagInfoAscii GPS_TAG_GPS_SPEED_REF = new TagInfoAscii("GPSSpeedRef", 12, 2, TiffDirectoryType.EXIF_DIRECTORY_GPS);
    public static final TagInfoRational GPS_TAG_GPS_SPEED = new TagInfoRational("GPSSpeed", 13, 1, TiffDirectoryType.EXIF_DIRECTORY_GPS);
    public static final TagInfoAscii GPS_TAG_GPS_TRACK_REF = new TagInfoAscii("GPSTrackRef", 14, 2, TiffDirectoryType.EXIF_DIRECTORY_GPS);
    public static final TagInfoRational GPS_TAG_GPS_TRACK = new TagInfoRational("GPSTrack", 15, 1, TiffDirectoryType.EXIF_DIRECTORY_GPS);
    public static final TagInfoAscii GPS_TAG_GPS_IMG_DIRECTION_REF = new TagInfoAscii("GPSImgDirectionRef", 16, 2, TiffDirectoryType.EXIF_DIRECTORY_GPS);
    public static final TagInfoRational GPS_TAG_GPS_IMG_DIRECTION = new TagInfoRational("GPSImgDirection", 17, 1, TiffDirectoryType.EXIF_DIRECTORY_GPS);
    public static final TagInfoAscii GPS_TAG_GPS_MAP_DATUM = new TagInfoAscii("GPSMapDatum", 18, -1, TiffDirectoryType.EXIF_DIRECTORY_GPS);
    public static final TagInfoAscii GPS_TAG_GPS_DEST_LATITUDE_REF = new TagInfoAscii("GPSDestLatitudeRef", 19, 2, TiffDirectoryType.EXIF_DIRECTORY_GPS);
    public static final TagInfoRational GPS_TAG_GPS_DEST_LATITUDE = new TagInfoRational("GPSDestLatitude", 20, 3, TiffDirectoryType.EXIF_DIRECTORY_GPS);
    public static final TagInfoAscii GPS_TAG_GPS_DEST_LONGITUDE_REF = new TagInfoAscii("GPSDestLongitudeRef", 21, 2, TiffDirectoryType.EXIF_DIRECTORY_GPS);
    public static final TagInfoRational GPS_TAG_GPS_DEST_LONGITUDE = new TagInfoRational("GPSDestLongitude", 22, 3, TiffDirectoryType.EXIF_DIRECTORY_GPS);
    public static final TagInfoAscii GPS_TAG_GPS_DEST_BEARING_REF = new TagInfoAscii("GPSDestBearingRef", 23, 2, TiffDirectoryType.EXIF_DIRECTORY_GPS);
    public static final TagInfoRational GPS_TAG_GPS_DEST_BEARING = new TagInfoRational("GPSDestBearing", 24, 1, TiffDirectoryType.EXIF_DIRECTORY_GPS);
    public static final TagInfoAscii GPS_TAG_GPS_DEST_DISTANCE_REF = new TagInfoAscii("GPSDestDistanceRef", 25, 2, TiffDirectoryType.EXIF_DIRECTORY_GPS);
    public static final TagInfoRational GPS_TAG_GPS_DEST_DISTANCE = new TagInfoRational("GPSDestDistance", 26, 1, TiffDirectoryType.EXIF_DIRECTORY_GPS);
    public static final TagInfoGpsText GPS_TAG_GPS_PROCESSING_METHOD = new TagInfoGpsText("GPSProcessingMethod", 27, -1, TiffDirectoryType.EXIF_DIRECTORY_GPS);
    public static final TagInfoGpsText GPS_TAG_GPS_AREA_INFORMATION = new TagInfoGpsText("GPSAreaInformation", 28, -1, TiffDirectoryType.EXIF_DIRECTORY_GPS);
    public static final TagInfoAscii GPS_TAG_GPS_DATE_STAMP = new TagInfoAscii("GPSDateStamp", 29, 11, TiffDirectoryType.EXIF_DIRECTORY_GPS);
    public static final TagInfoShort GPS_TAG_GPS_DIFFERENTIAL = new TagInfoShort("GPSDifferential", 30, 1, TiffDirectoryType.EXIF_DIRECTORY_GPS);
    public static final List<TagInfo> ALL_GPS_TAGS = Collections.unmodifiableList(Arrays.asList(GPS_TAG_GPS_VERSION_ID, GPS_TAG_GPS_LATITUDE_REF, GPS_TAG_GPS_LATITUDE, GPS_TAG_GPS_LONGITUDE_REF, GPS_TAG_GPS_LONGITUDE, GPS_TAG_GPS_ALTITUDE_REF, GPS_TAG_GPS_ALTITUDE, GPS_TAG_GPS_TIME_STAMP, GPS_TAG_GPS_SATELLITES, GPS_TAG_GPS_STATUS, GPS_TAG_GPS_MEASURE_MODE, GPS_TAG_GPS_DOP, GPS_TAG_GPS_SPEED_REF, GPS_TAG_GPS_SPEED, GPS_TAG_GPS_TRACK_REF, GPS_TAG_GPS_TRACK, GPS_TAG_GPS_IMG_DIRECTION_REF, GPS_TAG_GPS_IMG_DIRECTION, GPS_TAG_GPS_MAP_DATUM, GPS_TAG_GPS_DEST_LATITUDE_REF, GPS_TAG_GPS_DEST_LATITUDE, GPS_TAG_GPS_DEST_LONGITUDE_REF, GPS_TAG_GPS_DEST_LONGITUDE, GPS_TAG_GPS_DEST_BEARING_REF, GPS_TAG_GPS_DEST_BEARING, GPS_TAG_GPS_DEST_DISTANCE_REF, GPS_TAG_GPS_DEST_DISTANCE, GPS_TAG_GPS_PROCESSING_METHOD, GPS_TAG_GPS_AREA_INFORMATION, GPS_TAG_GPS_DATE_STAMP, GPS_TAG_GPS_DIFFERENTIAL));

    private GpsTagConstants() {
    }
}
