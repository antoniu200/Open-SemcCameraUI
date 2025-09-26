// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff;

import java.util.Collections;
import org.apache.commons.imaging.formats.tiff.constants.WangTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.TiffEpTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.Tiff4TagConstants;
import org.apache.commons.imaging.formats.tiff.constants.Rfc2301TagConstants;
import org.apache.commons.imaging.formats.tiff.constants.OceScanjobTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.MolecularDynamicsGelTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.MicrosoftHdPhotoTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.MicrosoftTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.HylaFaxTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.GpsTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.GdalLibraryTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.GeoTiffTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.DngTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.DcfTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.AliasSketchbookProTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.AdobePhotoshopTagConstants;
import java.util.Collection;
import org.apache.commons.imaging.formats.tiff.constants.AdobePageMaker6TagConstants;
import java.util.ArrayList;
import org.apache.commons.imaging.formats.tiff.constants.TiffDirectoryType;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import java.util.List;

final class TiffTags
{
    private static final List<TagInfo> ALL_TAGS;
    private static final Map<Integer, List<TagInfo>> ALL_TAG_MAP;
    private static final Map<Integer, Integer> TAG_COUNTS;
    
    static {
        ALL_TAGS = makeMergedTagList();
        ALL_TAG_MAP = makeTagMap(TiffTags.ALL_TAGS);
        TAG_COUNTS = countTags(TiffTags.ALL_TAGS);
    }
    
    private TiffTags() {
    }
    
    private static Map<Integer, Integer> countTags(final List<TagInfo> list) {
        final HashMap hashMap = new HashMap();
        for (final TagInfo tagInfo : list) {
            final Integer n = (Integer)hashMap.get(tagInfo.tag);
            if (n == null) {
                hashMap.put(tagInfo.tag, 1);
            }
            else {
                hashMap.put(tagInfo.tag, n + 1);
            }
        }
        return hashMap;
    }
    
    static TagInfo getTag(final int n, final int i) {
        final List list = TiffTags.ALL_TAG_MAP.get(i);
        if (list == null) {
            return TiffTagConstants.TIFF_TAG_UNKNOWN;
        }
        return getTag(n, list);
    }
    
    private static TagInfo getTag(final int n, final List<TagInfo> list) {
        if (list.size() < 1) {
            return null;
        }
        for (final TagInfo tagInfo : list) {
            if (tagInfo.directoryType == TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN) {
                continue;
            }
            if (n == tagInfo.directoryType.directoryType) {
                return tagInfo;
            }
        }
        for (final TagInfo tagInfo2 : list) {
            if (tagInfo2.directoryType == TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN) {
                continue;
            }
            if (n >= 0 && tagInfo2.directoryType.isImageDirectory()) {
                return tagInfo2;
            }
            if (n < 0 && !tagInfo2.directoryType.isImageDirectory()) {
                return tagInfo2;
            }
        }
        for (final TagInfo tagInfo3 : list) {
            if (tagInfo3.directoryType == TiffDirectoryType.EXIF_DIRECTORY_UNKNOWN) {
                return tagInfo3;
            }
        }
        return TiffTagConstants.TIFF_TAG_UNKNOWN;
    }
    
    static Integer getTagCount(final int i) {
        return TiffTags.TAG_COUNTS.get(i);
    }
    
    private static List<TagInfo> makeMergedTagList() {
        final ArrayList list = new ArrayList();
        list.addAll(AdobePageMaker6TagConstants.ALL_ADOBE_PAGEMAKER_6_TAGS);
        list.addAll(AdobePhotoshopTagConstants.ALL_ADOBE_PHOTOSHOP_TAGS);
        list.addAll(AliasSketchbookProTagConstants.ALL_ALIAS_SKETCHBOOK_PRO_TAGS);
        list.addAll(DcfTagConstants.ALL_DCF_TAGS);
        list.addAll(DngTagConstants.ALL_DNG_TAGS);
        list.addAll(ExifTagConstants.ALL_EXIF_TAGS);
        list.addAll(GeoTiffTagConstants.ALL_GEO_TIFF_TAGS);
        list.addAll(GdalLibraryTagConstants.ALL_GDAL_LIBRARY_TAGS);
        list.addAll(GpsTagConstants.ALL_GPS_TAGS);
        list.addAll(HylaFaxTagConstants.ALL_HYLAFAX_TAGS);
        list.addAll(MicrosoftTagConstants.ALL_MICROSOFT_TAGS);
        list.addAll(MicrosoftHdPhotoTagConstants.ALL_MICROSOFT_HD_PHOTO_TAGS);
        list.addAll(MolecularDynamicsGelTagConstants.ALL_MOLECULAR_DYNAMICS_GEL_TAGS);
        list.addAll(OceScanjobTagConstants.ALL_OCE_SCANJOB_TAGS);
        list.addAll(Rfc2301TagConstants.ALL_RFC_2301_TAGS);
        list.addAll(Tiff4TagConstants.ALL_TIFF_4_TAGS);
        list.addAll(TiffEpTagConstants.ALL_TIFF_EP_TAGS);
        list.addAll(TiffTagConstants.ALL_TIFF_TAGS);
        list.addAll(WangTagConstants.ALL_WANG_TAGS);
        return (List<TagInfo>)Collections.unmodifiableList((List<?>)list);
    }
    
    private static Map<Integer, List<TagInfo>> makeTagMap(final List<TagInfo> list) {
        final HashMap hashMap = new HashMap();
        for (final TagInfo tagInfo : list) {
            List list2;
            if ((list2 = (List)hashMap.get(tagInfo.tag)) == null) {
                list2 = new ArrayList();
                hashMap.put(tagInfo.tag, list2);
            }
            list2.add(tagInfo);
        }
        return hashMap;
    }
}
