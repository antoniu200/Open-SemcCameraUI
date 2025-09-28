package org.apache.commons.imaging.formats.tiff.write;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteOrder;
import java.util.List;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.common.BinaryOutputStream;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class TiffImageWriterLossy extends TiffImageWriterBase {
    public TiffImageWriterLossy() {
    }

    public TiffImageWriterLossy(ByteOrder byteOrder) {
        super(byteOrder);
    }

    @Override // org.apache.commons.imaging.formats.tiff.write.TiffImageWriterBase
    public void write(OutputStream outputStream, TiffOutputSet tiffOutputSet) throws ImageWriteException, IOException {
        TiffOutputSummary tiffOutputSummaryValidateDirectories = validateDirectories(tiffOutputSet);
        List<TiffOutputItem> outputItems = tiffOutputSet.getOutputItems(tiffOutputSummaryValidateDirectories);
        updateOffsetsStep(outputItems);
        tiffOutputSummaryValidateDirectories.updateOffsets(this.byteOrder);
        writeStep(new BinaryOutputStream(outputStream, this.byteOrder), outputItems);
    }

    private void updateOffsetsStep(List<TiffOutputItem> list) {
        int iImageDataPaddingLength = 8;
        for (TiffOutputItem tiffOutputItem : list) {
            tiffOutputItem.setOffset(iImageDataPaddingLength);
            int itemLength = tiffOutputItem.getItemLength();
            iImageDataPaddingLength = iImageDataPaddingLength + itemLength + imageDataPaddingLength(itemLength);
        }
    }

    private void writeStep(BinaryOutputStream binaryOutputStream, List<TiffOutputItem> list) throws ImageWriteException, IOException {
        writeImageFileHeader(binaryOutputStream);
        for (TiffOutputItem tiffOutputItem : list) {
            tiffOutputItem.writeItem(binaryOutputStream);
            int iImageDataPaddingLength = imageDataPaddingLength(tiffOutputItem.getItemLength());
            for (int i = 0; i < iImageDataPaddingLength; i++) {
                binaryOutputStream.write(0);
            }
        }
    }
}
