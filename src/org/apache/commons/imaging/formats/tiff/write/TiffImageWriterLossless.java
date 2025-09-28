package org.apache.commons.imaging.formats.tiff.write;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.imaging.FormatCompliance;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.common.BinaryOutputStream;
import org.apache.commons.imaging.common.bytesource.ByteSourceArray;
import org.apache.commons.imaging.formats.tiff.JpegImageData;
import org.apache.commons.imaging.formats.tiff.TiffContents;
import org.apache.commons.imaging.formats.tiff.TiffDirectory;
import org.apache.commons.imaging.formats.tiff.TiffElement;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.TiffImageData;
import org.apache.commons.imaging.formats.tiff.TiffReader;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class TiffImageWriterLossless extends TiffImageWriterBase {
    private static final Comparator<TiffElement> ELEMENT_SIZE_COMPARATOR = new Comparator<TiffElement>() { // from class: org.apache.commons.imaging.formats.tiff.write.TiffImageWriterLossless.1
        @Override // java.util.Comparator
        public int compare(TiffElement tiffElement, TiffElement tiffElement2) {
            return tiffElement.length - tiffElement2.length;
        }
    };
    private static final Comparator<TiffOutputItem> ITEM_SIZE_COMPARATOR = new Comparator<TiffOutputItem>() { // from class: org.apache.commons.imaging.formats.tiff.write.TiffImageWriterLossless.2
        @Override // java.util.Comparator
        public int compare(TiffOutputItem tiffOutputItem, TiffOutputItem tiffOutputItem2) {
            return tiffOutputItem.getItemLength() - tiffOutputItem2.getItemLength();
        }
    };
    private final byte[] exifBytes;

    public TiffImageWriterLossless(byte[] bArr) {
        this.exifBytes = bArr;
    }

    public TiffImageWriterLossless(ByteOrder byteOrder, byte[] bArr) {
        super(byteOrder);
        this.exifBytes = bArr;
    }

    private List<TiffElement> analyzeOldTiff(Map<Integer, TiffOutputField> map) throws ImageWriteException, IOException {
        try {
            TiffElement tiffElement = null;
            TiffContents contents = new TiffReader(false).readContents(new ByteSourceArray(this.exifBytes), null, FormatCompliance.getDefault());
            ArrayList<TiffElement> arrayList = new ArrayList();
            for (TiffDirectory tiffDirectory : contents.directories) {
                arrayList.add(tiffDirectory);
                for (TiffField tiffField : tiffDirectory.getDirectoryEntries()) {
                    TiffElement oversizeValueElement = tiffField.getOversizeValueElement();
                    if (oversizeValueElement != null) {
                        TiffOutputField tiffOutputField = map.get(Integer.valueOf(tiffField.getTag()));
                        if (tiffOutputField != null && tiffOutputField.getSeperateValue() != null && tiffOutputField.bytesEqual(tiffField.getByteArrayValue())) {
                            tiffOutputField.getSeperateValue().setOffset(tiffField.getOffset());
                        } else {
                            arrayList.add(oversizeValueElement);
                        }
                    }
                }
                JpegImageData jpegImageData = tiffDirectory.getJpegImageData();
                if (jpegImageData != null) {
                    arrayList.add(jpegImageData);
                }
                TiffImageData tiffImageData = tiffDirectory.getTiffImageData();
                if (tiffImageData != null) {
                    Collections.addAll(arrayList, tiffImageData.getImageData());
                }
            }
            Collections.sort(arrayList, TiffElement.COMPARATOR);
            ArrayList arrayList2 = new ArrayList();
            long j = -1;
            for (TiffElement tiffElement2 : arrayList) {
                long j2 = tiffElement2.offset + tiffElement2.length;
                if (tiffElement != null) {
                    if (tiffElement2.offset - j > 3) {
                        arrayList2.add(new TiffElement.Stub(tiffElement.offset, (int) (j - tiffElement.offset)));
                    } else {
                        j = j2;
                    }
                }
                tiffElement = tiffElement2;
                j = j2;
            }
            if (tiffElement != null) {
                arrayList2.add(new TiffElement.Stub(tiffElement.offset, (int) (j - tiffElement.offset)));
            }
            return arrayList2;
        } catch (ImageReadException e) {
            throw new ImageWriteException(e.getMessage(), (Throwable) e);
        }
    }

    @Override // org.apache.commons.imaging.formats.tiff.write.TiffImageWriterBase
    public void write(OutputStream outputStream, TiffOutputSet tiffOutputSet) throws ImageWriteException, IOException {
        HashMap map = new HashMap();
        TiffOutputField tiffOutputFieldFindField = tiffOutputSet.findField(ExifTagConstants.EXIF_TAG_MAKER_NOTE);
        if (tiffOutputFieldFindField != null && tiffOutputFieldFindField.getSeperateValue() != null) {
            map.put(Integer.valueOf(ExifTagConstants.EXIF_TAG_MAKER_NOTE.tag), tiffOutputFieldFindField);
        }
        List<TiffElement> listAnalyzeOldTiff = analyzeOldTiff(map);
        int length = this.exifBytes.length;
        if (listAnalyzeOldTiff.isEmpty()) {
            throw new ImageWriteException("Couldn't analyze old tiff data.");
        }
        if (listAnalyzeOldTiff.size() == 1) {
            TiffElement tiffElement = listAnalyzeOldTiff.get(0);
            if (tiffElement.offset == 8 && tiffElement.offset + tiffElement.length + 8 == length) {
                new TiffImageWriterLossy(this.byteOrder).write(outputStream, tiffOutputSet);
                return;
            }
        }
        HashMap map2 = new HashMap();
        Iterator<Map.Entry<Integer, TiffOutputField>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            TiffOutputField value = it.next().getValue();
            if (value.getSeperateValue().getOffset() != -1) {
                map2.put(Long.valueOf(value.getSeperateValue().getOffset()), value);
            }
        }
        TiffOutputSummary tiffOutputSummaryValidateDirectories = validateDirectories(tiffOutputSet);
        List<TiffOutputItem> outputItems = tiffOutputSet.getOutputItems(tiffOutputSummaryValidateDirectories);
        ArrayList arrayList = new ArrayList();
        for (TiffOutputItem tiffOutputItem : outputItems) {
            if (!map2.containsKey(Long.valueOf(tiffOutputItem.getOffset()))) {
                arrayList.add(tiffOutputItem);
            }
        }
        long jUpdateOffsetsStep = updateOffsetsStep(listAnalyzeOldTiff, arrayList);
        tiffOutputSummaryValidateDirectories.updateOffsets(this.byteOrder);
        writeStep(outputStream, tiffOutputSet, listAnalyzeOldTiff, arrayList, jUpdateOffsetsStep);
    }

    private long updateOffsetsStep(List<TiffElement> list, List<TiffOutputItem> list2) {
        long length = this.exifBytes.length;
        ArrayList<TiffElement> arrayList = new ArrayList(list);
        Collections.sort(arrayList, TiffElement.COMPARATOR);
        Collections.reverse(arrayList);
        while (!arrayList.isEmpty()) {
            if (((TiffElement) arrayList.get(0)).offset + r10.length != length) {
                break;
            }
            length -= r10.length;
            arrayList.remove(0);
        }
        Collections.sort(arrayList, ELEMENT_SIZE_COMPARATOR);
        Collections.reverse(arrayList);
        ArrayList arrayList2 = new ArrayList(list2);
        Collections.sort(arrayList2, ITEM_SIZE_COMPARATOR);
        Collections.reverse(arrayList2);
        while (!arrayList2.isEmpty()) {
            TiffOutputItem tiffOutputItem = (TiffOutputItem) arrayList2.remove(0);
            int itemLength = tiffOutputItem.getItemLength();
            TiffElement tiffElement = null;
            for (TiffElement tiffElement2 : arrayList) {
                if (tiffElement2.length < itemLength) {
                    break;
                }
                tiffElement = tiffElement2;
            }
            if (tiffElement == null) {
                tiffOutputItem.setOffset(length);
                length += itemLength;
            } else {
                tiffOutputItem.setOffset(tiffElement.offset);
                arrayList.remove(tiffElement);
                if (tiffElement.length > itemLength) {
                    arrayList.add(new TiffElement.Stub(tiffElement.offset + itemLength, tiffElement.length - itemLength));
                    Collections.sort(arrayList, ELEMENT_SIZE_COMPARATOR);
                    Collections.reverse(arrayList);
                }
            }
        }
        return length;
    }

    private static class BufferOutputStream extends OutputStream {
        private final byte[] buffer;
        private int index;

        public BufferOutputStream(byte[] bArr, int i) {
            this.buffer = bArr;
            this.index = i;
        }

        @Override // java.io.OutputStream
        public void write(int i) throws IOException {
            if (this.index >= this.buffer.length) {
                throw new IOException("Buffer overflow.");
            }
            byte[] bArr = this.buffer;
            int i2 = this.index;
            this.index = i2 + 1;
            bArr[i2] = (byte) i;
        }

        @Override // java.io.OutputStream
        public void write(byte[] bArr, int i, int i2) throws IOException {
            if (this.index + i2 > this.buffer.length) {
                throw new IOException("Buffer overflow.");
            }
            System.arraycopy(bArr, i, this.buffer, this.index, i2);
            this.index += i2;
        }
    }

    private void writeStep(OutputStream outputStream, TiffOutputSet tiffOutputSet, List<TiffElement> list, List<TiffOutputItem> list2, long j) throws ImageWriteException, IOException {
        TiffOutputDirectory rootDirectory = tiffOutputSet.getRootDirectory();
        byte[] bArr = new byte[(int) j];
        System.arraycopy(this.exifBytes, 0, bArr, 0, Math.min(this.exifBytes.length, bArr.length));
        writeImageFileHeader(new BinaryOutputStream(new BufferOutputStream(bArr, 0), this.byteOrder), rootDirectory.getOffset());
        for (TiffElement tiffElement : list) {
            for (int i = 0; i < tiffElement.length; i++) {
                int i2 = (int) (tiffElement.offset + i);
                if (i2 < bArr.length) {
                    bArr[i2] = 0;
                }
            }
        }
        for (TiffOutputItem tiffOutputItem : list2) {
            tiffOutputItem.writeItem(new BinaryOutputStream(new BufferOutputStream(bArr, (int) tiffOutputItem.getOffset()), this.byteOrder));
        }
        outputStream.write(bArr);
    }
}
