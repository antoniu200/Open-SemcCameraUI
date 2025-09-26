// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff;

import java.util.Collection;
import java.util.ArrayList;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.imaging.ImageReadException;
import java.util.Iterator;
import org.apache.commons.imaging.util.Debug;
import java.util.Comparator;
import java.util.Collections;
import java.util.List;

public class TiffContents
{
    public final List<TiffDirectory> directories;
    public final TiffHeader header;
    
    public TiffContents(final TiffHeader header, final List<TiffDirectory> list) {
        this.header = header;
        this.directories = Collections.unmodifiableList((List<? extends TiffDirectory>)list);
    }
    
    public void dissect(final boolean b) throws ImageReadException {
        final List<TiffElement> elements = this.getElements();
        Collections.sort((List<Object>)elements, (Comparator<? super Object>)TiffElement.COMPARATOR);
        final Iterator<TiffElement> iterator = elements.iterator();
        long lng = 0L;
        while (iterator.hasNext()) {
            final TiffElement tiffElement = iterator.next();
            if (tiffElement.offset > lng) {
                final StringBuilder sb = new StringBuilder();
                sb.append("\tgap: ");
                sb.append(tiffElement.offset - lng);
                Debug.debug(sb.toString());
            }
            if (tiffElement.offset < lng) {
                Debug.debug("\toverlap");
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("element, start: ");
            sb2.append(tiffElement.offset);
            sb2.append(", length: ");
            sb2.append(tiffElement.length);
            sb2.append(", end: ");
            sb2.append(tiffElement.offset + tiffElement.length);
            sb2.append(": ");
            sb2.append(tiffElement.getElementDescription(false));
            Debug.debug(sb2.toString());
            if (b) {
                final String elementDescription = tiffElement.getElementDescription(true);
                if (elementDescription != null) {
                    Debug.debug(elementDescription);
                }
            }
            lng = tiffElement.offset + tiffElement.length;
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("end: ");
        sb3.append(lng);
        Debug.debug(sb3.toString());
        Debug.debug();
    }
    
    public TiffField findField(final TagInfo tagInfo) throws ImageReadException {
        final Iterator<TiffDirectory> iterator = this.directories.iterator();
        while (iterator.hasNext()) {
            final TiffField field = iterator.next().findField(tagInfo);
            if (field != null) {
                return field;
            }
        }
        return null;
    }
    
    public List<TiffElement> getElements() throws ImageReadException {
        final ArrayList list = new ArrayList();
        list.add(this.header);
        for (final TiffDirectory tiffDirectory : this.directories) {
            list.add(tiffDirectory);
            final Iterator<TiffField> iterator2 = tiffDirectory.entries.iterator();
            while (iterator2.hasNext()) {
                final TiffElement oversizeValueElement = iterator2.next().getOversizeValueElement();
                if (oversizeValueElement != null) {
                    list.add(oversizeValueElement);
                }
            }
            if (tiffDirectory.hasTiffImageData()) {
                list.addAll(tiffDirectory.getTiffRawImageDataElements());
            }
            if (tiffDirectory.hasJpegImageData()) {
                list.add(tiffDirectory.getJpegRawImageDataElement());
            }
        }
        return list;
    }
}
