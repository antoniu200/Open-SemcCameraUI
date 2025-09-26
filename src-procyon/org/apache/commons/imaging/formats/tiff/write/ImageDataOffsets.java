// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.write;

import org.apache.commons.imaging.formats.tiff.TiffElement;

class ImageDataOffsets
{
    final int[] imageDataOffsets;
    final TiffOutputField imageDataOffsetsField;
    final TiffOutputItem[] outputItems;
    
    ImageDataOffsets(final TiffElement.DataElement[] array, final int[] imageDataOffsets, final TiffOutputField imageDataOffsetsField) {
        this.imageDataOffsets = imageDataOffsets;
        this.imageDataOffsetsField = imageDataOffsetsField;
        this.outputItems = new TiffOutputItem[array.length];
        for (int i = 0; i < array.length; ++i) {
            this.outputItems[i] = new TiffOutputItem.Value("TIFF image data", array[i].getData());
        }
    }
}
