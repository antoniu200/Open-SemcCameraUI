package org.apache.commons.imaging.formats.jpeg.iptc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.ImagingConstants;
import org.apache.commons.imaging.common.BinaryFileParser;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.common.BinaryOutputStream;
import org.apache.commons.imaging.common.ByteConversions;
import org.apache.commons.imaging.formats.jpeg.JpegConstants;
import org.apache.commons.imaging.util.Debug;
import org.apache.commons.imaging.util.IoUtils;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class IptcParser extends BinaryFileParser {
    private static final ByteOrder APP13_BYTE_ORDER = ByteOrder.BIG_ENDIAN;

    public IptcParser() {
        setByteOrder(ByteOrder.BIG_ENDIAN);
    }

    public boolean isPhotoshopJpegSegment(byte[] bArr) {
        if (!BinaryFunctions.startsWith(bArr, JpegConstants.PHOTOSHOP_IDENTIFICATION_STRING)) {
            return false;
        }
        int size = JpegConstants.PHOTOSHOP_IDENTIFICATION_STRING.size();
        return size + 4 <= bArr.length && ByteConversions.toInt(bArr, size, APP13_BYTE_ORDER) == JpegConstants.CONST_8BIM;
    }

    public PhotoshopApp13Data parsePhotoshopSegment(byte[] bArr, Map<String, Object> map) throws IOException, ImageReadException {
        boolean z = false;
        boolean z2 = map != null && Boolean.TRUE.equals(map.get(ImagingConstants.PARAM_KEY_STRICT));
        if (map != null && Boolean.TRUE.equals(map.get(ImagingConstants.PARAM_KEY_VERBOSE))) {
            z = true;
        }
        return parsePhotoshopSegment(bArr, z, z2);
    }

    public PhotoshopApp13Data parsePhotoshopSegment(byte[] bArr, boolean z, boolean z2) throws Throwable {
        ArrayList arrayList = new ArrayList();
        List<IptcBlock> allBlocks = parseAllBlocks(bArr, z, z2);
        for (IptcBlock iptcBlock : allBlocks) {
            if (iptcBlock.isIPTCBlock()) {
                arrayList.addAll(parseIPTCBlock(iptcBlock.blockData, z));
            }
        }
        return new PhotoshopApp13Data(arrayList, allBlocks);
    }

    protected List<IptcRecord> parseIPTCBlock(byte[] bArr, boolean z) throws IOException {
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (true) {
            int i2 = i + 1;
            if (i2 >= bArr.length) {
                return arrayList;
            }
            int i3 = bArr[i] & 255;
            if (z) {
                Debug.debug("tagMarker: " + i3 + " (0x" + Integer.toHexString(i3) + ")");
            }
            if (i3 != 28) {
                if (z) {
                    System.out.println("Unexpected record tag marker in IPTC data.");
                }
                return arrayList;
            }
            int i4 = i2 + 1;
            int i5 = bArr[i2] & 255;
            if (z) {
                Debug.debug("recordNumber: " + i5 + " (0x" + Integer.toHexString(i5) + ")");
            }
            int i6 = 255 & bArr[i4];
            if (z) {
                Debug.debug("recordType: " + i6 + " (0x" + Integer.toHexString(i6) + ")");
            }
            int i7 = i4 + 1;
            int uInt16 = ByteConversions.toUInt16(bArr, i7, getByteOrder());
            int i8 = i7 + 2;
            boolean z2 = uInt16 > 32767;
            int i9 = uInt16 & IptcConstants.IPTC_NON_EXTENDED_RECORD_MAXIMUM_SIZE;
            if (z2 && z) {
                Debug.debug("extendedDataset. dataFieldCountLength: " + i9);
            }
            if (z2) {
                return arrayList;
            }
            byte[] bArrSlice = BinaryFunctions.slice(bArr, i8, uInt16);
            i = i8 + uInt16;
            if (i5 == 2) {
                if (i6 != 0) {
                    arrayList.add(new IptcRecord(IptcTypeLookup.getIptcType(i6), bArrSlice, new String(bArrSlice, "ISO-8859-1")));
                } else if (z) {
                    System.out.println("ignore record version record! " + arrayList.size());
                }
            }
        }
    }

    protected List<IptcBlock> parseAllBlocks(byte[] bArr, boolean z, boolean z2) throws Throwable {
        ByteArrayInputStream byteArrayInputStream;
        byte[] bArr2;
        ArrayList arrayList = new ArrayList();
        try {
            byteArrayInputStream = new ByteArrayInputStream(bArr);
        } catch (Throwable th) {
            th = th;
            byteArrayInputStream = null;
        }
        try {
            if (!JpegConstants.PHOTOSHOP_IDENTIFICATION_STRING.equals(BinaryFunctions.readBytes("", byteArrayInputStream, JpegConstants.PHOTOSHOP_IDENTIFICATION_STRING.size(), "App13 Segment missing identification string"))) {
                throw new ImageReadException("Not a Photoshop App13 Segment");
            }
            while (BinaryFunctions.read4Bytes("", byteArrayInputStream, "Image Resource Block missing identification string", APP13_BYTE_ORDER) == JpegConstants.CONST_8BIM) {
                try {
                    int i = BinaryFunctions.read2Bytes("", byteArrayInputStream, "Image Resource Block missing type", APP13_BYTE_ORDER);
                    if (z) {
                        Debug.debug("blockType: " + i + " (0x" + Integer.toHexString(i) + ")");
                    }
                    byte b = BinaryFunctions.readByte("Name length", byteArrayInputStream, "Image Resource Block missing name length");
                    if (z && b > 0) {
                        Debug.debug("blockNameLength: " + ((int) b) + " (0x" + Integer.toHexString(b) + ")");
                    }
                    if (b == 0) {
                        BinaryFunctions.readByte("Block name bytes", byteArrayInputStream, "Image Resource Block has invalid name");
                        bArr2 = new byte[0];
                    } else {
                        try {
                            byte[] bytes = BinaryFunctions.readBytes("", byteArrayInputStream, b, "Invalid Image Resource Block name");
                            if (b % 2 == 0) {
                                BinaryFunctions.readByte("Padding byte", byteArrayInputStream, "Image Resource Block missing padding byte");
                            }
                            bArr2 = bytes;
                        } catch (IOException e) {
                            if (z2) {
                                throw e;
                            }
                            IoUtils.closeQuietly(true, byteArrayInputStream);
                            return arrayList;
                        }
                    }
                    int i2 = BinaryFunctions.read4Bytes("", byteArrayInputStream, "Image Resource Block missing size", APP13_BYTE_ORDER);
                    if (z) {
                        Debug.debug("blockSize: " + i2 + " (0x" + Integer.toHexString(i2) + ")");
                    }
                    if (i2 > bArr.length) {
                        throw new ImageReadException("Invalid Block Size : " + i2 + " > " + bArr.length);
                    }
                    try {
                        arrayList.add(new IptcBlock(i, bArr2, BinaryFunctions.readBytes("", byteArrayInputStream, i2, "Invalid Image Resource Block data")));
                        if (i2 % 2 != 0) {
                            BinaryFunctions.readByte("Padding byte", byteArrayInputStream, "Image Resource Block missing padding byte");
                        }
                    } catch (IOException e2) {
                        if (z2) {
                            throw e2;
                        }
                        IoUtils.closeQuietly(true, byteArrayInputStream);
                        return arrayList;
                    }
                } catch (IOException unused) {
                }
            }
            throw new ImageReadException("Invalid Image Resource Block Signature");
        } catch (Throwable th2) {
            th = th2;
            IoUtils.closeQuietly(false, byteArrayInputStream);
            throw th;
        }
    }

    public byte[] writePhotoshopApp13Segment(PhotoshopApp13Data photoshopApp13Data) throws ImageWriteException, IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BinaryOutputStream binaryOutputStream = new BinaryOutputStream(byteArrayOutputStream);
        JpegConstants.PHOTOSHOP_IDENTIFICATION_STRING.writeTo(binaryOutputStream);
        for (IptcBlock iptcBlock : photoshopApp13Data.getRawBlocks()) {
            binaryOutputStream.write4Bytes(JpegConstants.CONST_8BIM);
            if (iptcBlock.blockType < 0 || iptcBlock.blockType > 65535) {
                throw new ImageWriteException("Invalid IPTC block type.");
            }
            binaryOutputStream.write2Bytes(iptcBlock.blockType);
            if (iptcBlock.blockNameBytes.length > 255) {
                throw new ImageWriteException("IPTC block name is too long: " + iptcBlock.blockNameBytes.length);
            }
            binaryOutputStream.write(iptcBlock.blockNameBytes.length);
            binaryOutputStream.write(iptcBlock.blockNameBytes);
            if (iptcBlock.blockNameBytes.length % 2 == 0) {
                binaryOutputStream.write(0);
            }
            if (iptcBlock.blockData.length > 32767) {
                throw new ImageWriteException("IPTC block data is too long: " + iptcBlock.blockData.length);
            }
            binaryOutputStream.write4Bytes(iptcBlock.blockData.length);
            binaryOutputStream.write(iptcBlock.blockData);
            if (iptcBlock.blockData.length % 2 == 1) {
                binaryOutputStream.write(0);
            }
        }
        binaryOutputStream.flush();
        return byteArrayOutputStream.toByteArray();
    }

    public byte[] writeIPTCBlock(List<IptcRecord> list) throws Throwable {
        BinaryOutputStream binaryOutputStream;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            binaryOutputStream = new BinaryOutputStream(byteArrayOutputStream, getByteOrder());
        } catch (Throwable th) {
            th = th;
            binaryOutputStream = null;
        }
        try {
            binaryOutputStream.write(28);
            binaryOutputStream.write(2);
            binaryOutputStream.write(IptcTypes.RECORD_VERSION.type);
            binaryOutputStream.write2Bytes(2);
            binaryOutputStream.write2Bytes(2);
            ArrayList<IptcRecord> arrayList = new ArrayList(list);
            Collections.sort(arrayList, new Comparator<IptcRecord>() { // from class: org.apache.commons.imaging.formats.jpeg.iptc.IptcParser.1
                @Override // java.util.Comparator
                public int compare(IptcRecord iptcRecord, IptcRecord iptcRecord2) {
                    return iptcRecord2.iptcType.getType() - iptcRecord.iptcType.getType();
                }
            });
            for (IptcRecord iptcRecord : arrayList) {
                if (iptcRecord.iptcType != IptcTypes.RECORD_VERSION) {
                    binaryOutputStream.write(28);
                    binaryOutputStream.write(2);
                    if (iptcRecord.iptcType.getType() >= 0 && iptcRecord.iptcType.getType() <= 255) {
                        binaryOutputStream.write(iptcRecord.iptcType.getType());
                        byte[] bytes = iptcRecord.value.getBytes("ISO-8859-1");
                        if (!new String(bytes, "ISO-8859-1").equals(iptcRecord.value)) {
                            throw new ImageWriteException("Invalid record value, not ISO-8859-1");
                        }
                        binaryOutputStream.write2Bytes(bytes.length);
                        binaryOutputStream.write(bytes);
                    }
                    throw new ImageWriteException("Invalid record type: " + iptcRecord.iptcType.getType());
                }
            }
            IoUtils.closeQuietly(true, binaryOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Throwable th2) {
            th = th2;
            IoUtils.closeQuietly(false, binaryOutputStream);
            throw th;
        }
    }
}
