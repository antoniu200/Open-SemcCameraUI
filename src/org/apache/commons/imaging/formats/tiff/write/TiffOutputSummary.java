package org.apache.commons.imaging.formats.tiff.write;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class TiffOutputSummary {
    public final ByteOrder byteOrder;
    public final Map<Integer, TiffOutputDirectory> directoryTypeMap;
    public final TiffOutputDirectory rootDirectory;
    private final List<OffsetItem> offsetItems = new ArrayList();
    private final List<ImageDataOffsets> imageDataItems = new ArrayList();

    public TiffOutputSummary(ByteOrder byteOrder, TiffOutputDirectory tiffOutputDirectory, Map<Integer, TiffOutputDirectory> map) {
        this.byteOrder = byteOrder;
        this.rootDirectory = tiffOutputDirectory;
        this.directoryTypeMap = map;
    }

    private static class OffsetItem {
        public final TiffOutputItem item;
        public final TiffOutputField itemOffsetField;

        public OffsetItem(TiffOutputItem tiffOutputItem, TiffOutputField tiffOutputField) {
            this.itemOffsetField = tiffOutputField;
            this.item = tiffOutputItem;
        }
    }

    public void add(TiffOutputItem tiffOutputItem, TiffOutputField tiffOutputField) {
        this.offsetItems.add(new OffsetItem(tiffOutputItem, tiffOutputField));
    }

    public void updateOffsets(ByteOrder byteOrder) throws ImageWriteException {
        for (OffsetItem offsetItem : this.offsetItems) {
            offsetItem.itemOffsetField.setData(FieldType.LONG.writeData(Integer.valueOf((int) offsetItem.item.getOffset()), byteOrder));
        }
        for (ImageDataOffsets imageDataOffsets : this.imageDataItems) {
            for (int i = 0; i < imageDataOffsets.outputItems.length; i++) {
                imageDataOffsets.imageDataOffsets[i] = (int) imageDataOffsets.outputItems[i].getOffset();
            }
            imageDataOffsets.imageDataOffsetsField.setData(FieldType.LONG.writeData(imageDataOffsets.imageDataOffsets, byteOrder));
        }
    }

    public void addTiffImageData(ImageDataOffsets imageDataOffsets) {
        this.imageDataItems.add(imageDataOffsets);
    }
}
