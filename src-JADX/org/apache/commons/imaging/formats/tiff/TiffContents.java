package org.apache.commons.imaging.formats.tiff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.imaging.util.Debug;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class TiffContents {
    public final List<TiffDirectory> directories;
    public final TiffHeader header;

    public TiffContents(TiffHeader tiffHeader, List<TiffDirectory> list) {
        this.header = tiffHeader;
        this.directories = Collections.unmodifiableList(list);
    }

    public List<TiffElement> getElements() throws ImageReadException {
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.header);
        for (TiffDirectory tiffDirectory : this.directories) {
            arrayList.add(tiffDirectory);
            Iterator<TiffField> it = tiffDirectory.entries.iterator();
            while (it.hasNext()) {
                TiffElement oversizeValueElement = it.next().getOversizeValueElement();
                if (oversizeValueElement != null) {
                    arrayList.add(oversizeValueElement);
                }
            }
            if (tiffDirectory.hasTiffImageData()) {
                arrayList.addAll(tiffDirectory.getTiffRawImageDataElements());
            }
            if (tiffDirectory.hasJpegImageData()) {
                arrayList.add(tiffDirectory.getJpegRawImageDataElement());
            }
        }
        return arrayList;
    }

    public TiffField findField(TagInfo tagInfo) throws ImageReadException {
        Iterator<TiffDirectory> it = this.directories.iterator();
        while (it.hasNext()) {
            TiffField tiffFieldFindField = it.next().findField(tagInfo);
            if (tiffFieldFindField != null) {
                return tiffFieldFindField;
            }
        }
        return null;
    }

    public void dissect(boolean z) throws ImageReadException {
        String elementDescription;
        List<TiffElement> elements = getElements();
        Collections.sort(elements, TiffElement.COMPARATOR);
        long j = 0;
        for (TiffElement tiffElement : elements) {
            if (tiffElement.offset > j) {
                Debug.debug("\tgap: " + (tiffElement.offset - j));
            }
            if (tiffElement.offset < j) {
                Debug.debug("\toverlap");
            }
            Debug.debug("element, start: " + tiffElement.offset + ", length: " + tiffElement.length + ", end: " + (tiffElement.offset + tiffElement.length) + ": " + tiffElement.getElementDescription(false));
            if (z && (elementDescription = tiffElement.getElementDescription(true)) != null) {
                Debug.debug(elementDescription);
            }
            j = tiffElement.offset + tiffElement.length;
        }
        Debug.debug("end: " + j);
        Debug.debug();
    }
}
