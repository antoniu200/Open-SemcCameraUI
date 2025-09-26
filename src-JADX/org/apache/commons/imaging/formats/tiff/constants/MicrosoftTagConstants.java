package org.apache.commons.imaging.formats.tiff.constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoShort;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoXpString;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class MicrosoftTagConstants {
    public static final TagInfoShort EXIF_TAG_RATING = new TagInfoShort("Rating", 18246, 1, TiffDirectoryType.EXIF_DIRECTORY_IFD0);
    public static final TagInfoShort EXIF_TAG_RATING_PERCENT = new TagInfoShort("RatingPercent", 18249, 1, TiffDirectoryType.EXIF_DIRECTORY_IFD0);
    public static final TagInfoXpString EXIF_TAG_XPTITLE = new TagInfoXpString("XPTitle", 40091, -1, TiffDirectoryType.EXIF_DIRECTORY_IFD0);
    public static final TagInfoXpString EXIF_TAG_XPCOMMENT = new TagInfoXpString("XPComment", 40092, -1, TiffDirectoryType.EXIF_DIRECTORY_IFD0);
    public static final TagInfoXpString EXIF_TAG_XPAUTHOR = new TagInfoXpString("XPAuthor", 40093, -1, TiffDirectoryType.EXIF_DIRECTORY_IFD0);
    public static final TagInfoXpString EXIF_TAG_XPKEYWORDS = new TagInfoXpString("XPKeywords", 40094, -1, TiffDirectoryType.EXIF_DIRECTORY_IFD0);
    public static final TagInfoXpString EXIF_TAG_XPSUBJECT = new TagInfoXpString("XPSubject", 40095, -1, TiffDirectoryType.EXIF_DIRECTORY_IFD0);
    public static final List<TagInfo> ALL_MICROSOFT_TAGS = Collections.unmodifiableList(Arrays.asList(EXIF_TAG_RATING, EXIF_TAG_RATING_PERCENT, EXIF_TAG_XPTITLE, EXIF_TAG_XPCOMMENT, EXIF_TAG_XPAUTHOR, EXIF_TAG_XPKEYWORDS, EXIF_TAG_XPSUBJECT));

    private MicrosoftTagConstants() {
    }
}
