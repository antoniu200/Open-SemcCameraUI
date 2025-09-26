// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.write;

import org.apache.commons.imaging.ImageWriteException;
import java.util.Iterator;
import org.apache.commons.imaging.formats.tiff.fieldtypes.FieldType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.nio.ByteOrder;

class TiffOutputSummary
{
    public final ByteOrder byteOrder;
    public final Map<Integer, TiffOutputDirectory> directoryTypeMap;
    private final List<ImageDataOffsets> imageDataItems;
    private final List<OffsetItem> offsetItems;
    public final TiffOutputDirectory rootDirectory;
    
    public TiffOutputSummary(final ByteOrder byteOrder, final TiffOutputDirectory rootDirectory, final Map<Integer, TiffOutputDirectory> directoryTypeMap) {
        this.offsetItems = new ArrayList<OffsetItem>();
        this.imageDataItems = new ArrayList<ImageDataOffsets>();
        this.byteOrder = byteOrder;
        this.rootDirectory = rootDirectory;
        this.directoryTypeMap = directoryTypeMap;
    }
    
    public void add(final TiffOutputItem tiffOutputItem, final TiffOutputField tiffOutputField) {
        this.offsetItems.add(new OffsetItem(tiffOutputItem, tiffOutputField));
    }
    
    public void addTiffImageData(final ImageDataOffsets imageDataOffsets) {
        this.imageDataItems.add(imageDataOffsets);
    }
    
    public void updateOffsets(final ByteOrder byteOrder) throws ImageWriteException {
        for (final OffsetItem offsetItem : this.offsetItems) {
            offsetItem.itemOffsetField.setData(FieldType.LONG.writeData((int)offsetItem.item.getOffset(), byteOrder));
        }
        for (final ImageDataOffsets imageDataOffsets : this.imageDataItems) {
            for (int i = 0; i < imageDataOffsets.outputItems.length; ++i) {
                imageDataOffsets.imageDataOffsets[i] = (int)imageDataOffsets.outputItems[i].getOffset();
            }
            imageDataOffsets.imageDataOffsetsField.setData(FieldType.LONG.writeData(imageDataOffsets.imageDataOffsets, byteOrder));
        }
    }
    
    private static class OffsetItem
    {
        public final TiffOutputItem item;
        public final TiffOutputField itemOffsetField;
        
        public OffsetItem(final TiffOutputItem item, final TiffOutputField itemOffsetField) {
            this.itemOffsetField = itemOffsetField;
            this.item = item;
        }
    }
}
