package org.apache.commons.imaging.formats.tiff.taginfos;

import org.apache.commons.imaging.formats.tiff.constants.TiffDirectoryType;
import org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class TagInfoUnknown extends TagInfoByte {
    public TagInfoUnknown(String str, int i, int i2, TiffDirectoryType tiffDirectoryType) {
        super(str, i, FieldType.ANY, i2, tiffDirectoryType);
    }
}
