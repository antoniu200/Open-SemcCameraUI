// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.write;

import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import java.util.HashMap;
import org.apache.commons.imaging.common.BinaryOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import org.apache.commons.imaging.formats.tiff.TiffImageData;
import org.apache.commons.imaging.formats.tiff.JpegImageData;
import java.util.Iterator;
import org.apache.commons.imaging.formats.tiff.TiffContents;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import java.util.Collection;
import java.util.Collections;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.TiffDirectory;
import java.util.ArrayList;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.formats.tiff.TiffReader;
import org.apache.commons.imaging.FormatCompliance;
import org.apache.commons.imaging.common.bytesource.ByteSourceArray;
import java.util.List;
import java.util.Map;
import java.nio.ByteOrder;
import org.apache.commons.imaging.formats.tiff.TiffElement;
import java.util.Comparator;

public class TiffImageWriterLossless extends TiffImageWriterBase
{
    private static final Comparator<TiffElement> ELEMENT_SIZE_COMPARATOR;
    private static final Comparator<TiffOutputItem> ITEM_SIZE_COMPARATOR;
    private final byte[] exifBytes;
    
    static {
        ELEMENT_SIZE_COMPARATOR = new Comparator<TiffElement>() {
            @Override
            public int compare(final TiffElement tiffElement, final TiffElement tiffElement2) {
                return tiffElement.length - tiffElement2.length;
            }
        };
        ITEM_SIZE_COMPARATOR = new Comparator<TiffOutputItem>() {
            @Override
            public int compare(final TiffOutputItem tiffOutputItem, final TiffOutputItem tiffOutputItem2) {
                return tiffOutputItem.getItemLength() - tiffOutputItem2.getItemLength();
            }
        };
    }
    
    public TiffImageWriterLossless(final ByteOrder byteOrder, final byte[] exifBytes) {
        super(byteOrder);
        this.exifBytes = exifBytes;
    }
    
    public TiffImageWriterLossless(final byte[] exifBytes) {
        this.exifBytes = exifBytes;
    }
    
    private List<TiffElement> analyzeOldTiff(final Map<Integer, TiffOutputField> map) throws ImageWriteException, IOException {
        try {
            final ByteSourceArray byteSourceArray = new ByteSourceArray(this.exifBytes);
            final FormatCompliance default1 = FormatCompliance.getDefault();
            final TiffReader tiffReader = new TiffReader(false);
            final TiffElement tiffElement = null;
            final TiffContents contents = tiffReader.readContents(byteSourceArray, null, default1);
            final ArrayList list = new ArrayList();
            for (final TiffDirectory tiffDirectory : contents.directories) {
                list.add(tiffDirectory);
                for (final TiffField tiffField : tiffDirectory.getDirectoryEntries()) {
                    final TiffElement oversizeValueElement = tiffField.getOversizeValueElement();
                    if (oversizeValueElement != null) {
                        final TiffOutputField tiffOutputField = map.get(tiffField.getTag());
                        if (tiffOutputField != null && tiffOutputField.getSeperateValue() != null && tiffOutputField.bytesEqual(tiffField.getByteArrayValue())) {
                            tiffOutputField.getSeperateValue().setOffset(tiffField.getOffset());
                        }
                        else {
                            list.add(oversizeValueElement);
                        }
                    }
                }
                final JpegImageData jpegImageData = tiffDirectory.getJpegImageData();
                if (jpegImageData != null) {
                    list.add(jpegImageData);
                }
                final TiffImageData tiffImageData = tiffDirectory.getTiffImageData();
                if (tiffImageData != null) {
                    Collections.addAll(list, tiffImageData.getImageData());
                }
            }
            Collections.sort((List<Object>)list, (Comparator<? super Object>)TiffElement.COMPARATOR);
            final ArrayList list2 = new ArrayList();
            long n = -1L;
            final Iterator iterator3 = list.iterator();
            TiffElement tiffElement2 = tiffElement;
            while (iterator3.hasNext()) {
                final TiffElement tiffElement3 = (TiffElement)iterator3.next();
                final long offset = tiffElement3.offset;
                final long n2 = tiffElement3.length;
                TiffElement tiffElement4 = null;
                Label_0398: {
                    if (tiffElement2 != null) {
                        tiffElement4 = tiffElement2;
                        if (tiffElement3.offset - n <= 3L) {
                            break Label_0398;
                        }
                        list2.add(new TiffElement.Stub(tiffElement2.offset, (int)(n - tiffElement2.offset)));
                    }
                    tiffElement4 = tiffElement3;
                }
                n = offset + n2;
                tiffElement2 = tiffElement4;
            }
            if (tiffElement2 != null) {
                list2.add(new TiffElement.Stub(tiffElement2.offset, (int)(n - tiffElement2.offset)));
            }
            return list2;
        }
        catch (final ImageReadException ex) {
            throw new ImageWriteException(ex.getMessage(), ex);
        }
    }
    
    private long updateOffsetsStep(final List<TiffElement> c, final List<TiffOutputItem> c2) {
        long offset = this.exifBytes.length;
        final ArrayList list = new ArrayList((Collection<? extends E>)c);
        Collections.sort((List<Object>)list, (Comparator<? super Object>)TiffElement.COMPARATOR);
        Collections.reverse(list);
        while (!list.isEmpty()) {
            final TiffElement tiffElement = (TiffElement)list.get(0);
            if (tiffElement.offset + tiffElement.length != offset) {
                break;
            }
            offset -= tiffElement.length;
            list.remove(0);
        }
        Collections.sort((List<Object>)list, (Comparator<? super Object>)TiffImageWriterLossless.ELEMENT_SIZE_COMPARATOR);
        Collections.reverse(list);
        final ArrayList list2 = new ArrayList<Object>(c2);
        Collections.sort((List<Object>)list2, (Comparator<? super Object>)TiffImageWriterLossless.ITEM_SIZE_COMPARATOR);
        Collections.reverse(list2);
        while (!list2.isEmpty()) {
            final TiffOutputItem tiffOutputItem = (TiffOutputItem)list2.remove(0);
            final int itemLength = tiffOutputItem.getItemLength();
            TiffElement tiffElement2 = null;
            for (final TiffElement tiffElement3 : list) {
                if (tiffElement3.length < itemLength) {
                    break;
                }
                tiffElement2 = tiffElement3;
            }
            if (tiffElement2 == null) {
                tiffOutputItem.setOffset(offset);
                offset += itemLength;
            }
            else {
                tiffOutputItem.setOffset(tiffElement2.offset);
                list.remove(tiffElement2);
                if (tiffElement2.length <= itemLength) {
                    continue;
                }
                list.add(new TiffElement.Stub(tiffElement2.offset + itemLength, tiffElement2.length - itemLength));
                Collections.sort((List<Object>)list, (Comparator<? super Object>)TiffImageWriterLossless.ELEMENT_SIZE_COMPARATOR);
                Collections.reverse(list);
            }
        }
        return offset;
    }
    
    private void writeStep(final OutputStream outputStream, final TiffOutputSet set, final List<TiffElement> list, final List<TiffOutputItem> list2, final long n) throws IOException, ImageWriteException {
        final TiffOutputDirectory rootDirectory = set.getRootDirectory();
        final byte[] b = new byte[(int)n];
        System.arraycopy(this.exifBytes, 0, b, 0, Math.min(this.exifBytes.length, b.length));
        this.writeImageFileHeader(new BinaryOutputStream(new BufferOutputStream(b, 0), this.byteOrder), rootDirectory.getOffset());
        for (final TiffElement tiffElement : list) {
            for (int i = 0; i < tiffElement.length; ++i) {
                final int n2 = (int)(tiffElement.offset + i);
                if (n2 < b.length) {
                    b[n2] = 0;
                }
            }
        }
        final Iterator<TiffOutputItem> iterator2 = list2.iterator();
        while (iterator2.hasNext()) {
            final TiffOutputItem tiffOutputItem;
            tiffOutputItem.writeItem(new BinaryOutputStream(new BufferOutputStream(b, (int)(tiffOutputItem = iterator2.next()).getOffset()), this.byteOrder));
        }
        outputStream.write(b);
    }
    
    @Override
    public void write(final OutputStream outputStream, final TiffOutputSet set) throws IOException, ImageWriteException {
        final HashMap hashMap = new HashMap();
        final TiffOutputField field = set.findField(ExifTagConstants.EXIF_TAG_MAKER_NOTE);
        if (field != null && field.getSeperateValue() != null) {
            hashMap.put(ExifTagConstants.EXIF_TAG_MAKER_NOTE.tag, field);
        }
        final List<TiffElement> analyzeOldTiff = this.analyzeOldTiff(hashMap);
        final int length = this.exifBytes.length;
        if (analyzeOldTiff.isEmpty()) {
            throw new ImageWriteException("Couldn't analyze old tiff data.");
        }
        if (analyzeOldTiff.size() == 1) {
            final TiffElement tiffElement = analyzeOldTiff.get(0);
            if (tiffElement.offset == 8L && tiffElement.offset + tiffElement.length + 8L == length) {
                new TiffImageWriterLossy(this.byteOrder).write(outputStream, set);
                return;
            }
        }
        final HashMap hashMap2 = new HashMap();
        final Iterator iterator = hashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            final TiffOutputField tiffOutputField = ((Map.Entry<K, TiffOutputField>)iterator.next()).getValue();
            if (tiffOutputField.getSeperateValue().getOffset() != -1L) {
                hashMap2.put(tiffOutputField.getSeperateValue().getOffset(), tiffOutputField);
            }
        }
        final TiffOutputSummary validateDirectories = this.validateDirectories(set);
        final List<TiffOutputItem> outputItems = set.getOutputItems(validateDirectories);
        final ArrayList list = new ArrayList();
        for (final TiffOutputItem tiffOutputItem : outputItems) {
            if (!hashMap2.containsKey(tiffOutputItem.getOffset())) {
                list.add(tiffOutputItem);
            }
        }
        final long updateOffsetsStep = this.updateOffsetsStep(analyzeOldTiff, list);
        validateDirectories.updateOffsets(this.byteOrder);
        this.writeStep(outputStream, set, analyzeOldTiff, list, updateOffsetsStep);
    }
    
    private static class BufferOutputStream extends OutputStream
    {
        private final byte[] buffer;
        private int index;
        
        public BufferOutputStream(final byte[] buffer, final int index) {
            this.buffer = buffer;
            this.index = index;
        }
        
        @Override
        public void write(final int n) throws IOException {
            if (this.index >= this.buffer.length) {
                throw new IOException("Buffer overflow.");
            }
            this.buffer[this.index++] = (byte)n;
        }
        
        @Override
        public void write(final byte[] array, final int n, final int n2) throws IOException {
            if (this.index + n2 > this.buffer.length) {
                throw new IOException("Buffer overflow.");
            }
            System.arraycopy(array, n, this.buffer, this.index, n2);
            this.index += n2;
        }
    }
}
