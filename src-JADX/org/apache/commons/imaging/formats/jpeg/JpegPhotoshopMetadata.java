package org.apache.commons.imaging.formats.jpeg;

import java.util.Collections;
import java.util.List;
import org.apache.commons.imaging.common.GenericImageMetadata;
import org.apache.commons.imaging.formats.jpeg.iptc.IptcRecord;
import org.apache.commons.imaging.formats.jpeg.iptc.IptcTypes;
import org.apache.commons.imaging.formats.jpeg.iptc.PhotoshopApp13Data;
import org.apache.commons.imaging.util.Debug;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class JpegPhotoshopMetadata extends GenericImageMetadata {
    public final PhotoshopApp13Data photoshopApp13Data;

    public JpegPhotoshopMetadata(PhotoshopApp13Data photoshopApp13Data) {
        this.photoshopApp13Data = photoshopApp13Data;
        List<IptcRecord> records = photoshopApp13Data.getRecords();
        Collections.sort(records, IptcRecord.COMPARATOR);
        for (IptcRecord iptcRecord : records) {
            if (iptcRecord.iptcType != IptcTypes.RECORD_VERSION) {
                add(iptcRecord.getIptcTypeName(), iptcRecord.getValue());
            }
        }
    }

    public void dump() {
        Debug.debug(toString());
    }
}
