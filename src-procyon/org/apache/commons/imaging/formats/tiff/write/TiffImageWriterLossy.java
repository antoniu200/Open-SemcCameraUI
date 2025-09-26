// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.write;

import java.io.OutputStream;
import org.apache.commons.imaging.ImageWriteException;
import java.io.IOException;
import org.apache.commons.imaging.common.BinaryOutputStream;
import java.util.Iterator;
import java.util.List;
import java.nio.ByteOrder;

public class TiffImageWriterLossy extends TiffImageWriterBase
{
    public TiffImageWriterLossy() {
    }
    
    public TiffImageWriterLossy(final ByteOrder byteOrder) {
        super(byteOrder);
    }
    
    private void updateOffsetsStep(final List<TiffOutputItem> list) {
        final Iterator<TiffOutputItem> iterator = list.iterator();
        int n = 8;
        while (iterator.hasNext()) {
            final TiffOutputItem tiffOutputItem = iterator.next();
            tiffOutputItem.setOffset(n);
            final int itemLength = tiffOutputItem.getItemLength();
            n = n + itemLength + TiffImageWriterBase.imageDataPaddingLength(itemLength);
        }
    }
    
    private void writeStep(final BinaryOutputStream binaryOutputStream, final List<TiffOutputItem> list) throws IOException, ImageWriteException {
        this.writeImageFileHeader(binaryOutputStream);
        for (final TiffOutputItem tiffOutputItem : list) {
            tiffOutputItem.writeItem(binaryOutputStream);
            for (int imageDataPaddingLength = TiffImageWriterBase.imageDataPaddingLength(tiffOutputItem.getItemLength()), i = 0; i < imageDataPaddingLength; ++i) {
                binaryOutputStream.write(0);
            }
        }
    }
    
    @Override
    public void write(final OutputStream outputStream, final TiffOutputSet set) throws IOException, ImageWriteException {
        final TiffOutputSummary validateDirectories = this.validateDirectories(set);
        final List<TiffOutputItem> outputItems = set.getOutputItems(validateDirectories);
        this.updateOffsetsStep(outputItems);
        validateDirectories.updateOffsets(this.byteOrder);
        this.writeStep(new BinaryOutputStream(outputStream, this.byteOrder), outputItems);
    }
}
