// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff;

import java.util.Map;
import org.apache.commons.imaging.util.IoUtils;
import java.io.Closeable;
import org.apache.commons.imaging.common.BinaryFunctions;
import java.io.InputStream;
import java.util.ArrayList;
import org.apache.commons.imaging.FormatCompliance;
import java.util.List;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.apache.commons.imaging.common.bytesource.ByteSourceFile;
import java.nio.ByteOrder;
import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.common.bytesource.ByteSource;
import org.apache.commons.imaging.common.BinaryFileParser;

public class TiffReader extends BinaryFileParser
{
    private final boolean strict;
    
    public TiffReader(final boolean strict) {
        this.strict = strict;
    }
    
    private JpegImageData getJpegRawImageData(final ByteSource byteSource, final TiffDirectory tiffDirectory) throws ImageReadException, IOException {
        final TiffDirectory.ImageDataElement jpegRawImageDataElement = tiffDirectory.getJpegRawImageDataElement();
        final long offset = jpegRawImageDataElement.offset;
        int length;
        if ((length = jpegRawImageDataElement.length) + offset > byteSource.getLength()) {
            length = (int)(byteSource.getLength() - offset);
        }
        final byte[] block = byteSource.getBlock(offset, length);
        if (this.strict && (length < 2 || ((block[block.length - 2] & 0xFF) << 8 | (block[block.length - 1] & 0xFF)) != 0xFFD9)) {
            throw new ImageReadException("JPEG EOI marker could not be found at expected location");
        }
        return new JpegImageData(offset, length, block);
    }
    
    private ByteOrder getTiffByteOrder(final int n) throws ImageReadException {
        if (n == 73) {
            return ByteOrder.LITTLE_ENDIAN;
        }
        if (n == 77) {
            return ByteOrder.BIG_ENDIAN;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Invalid TIFF byte order ");
        sb.append(n & 0xFF);
        throw new ImageReadException(sb.toString());
    }
    
    private TiffImageData getTiffRawImageData(final ByteSource byteSource, final TiffDirectory tiffDirectory) throws ImageReadException, IOException {
        final List<TiffDirectory.ImageDataElement> tiffRawImageDataElements = tiffDirectory.getTiffRawImageDataElements();
        final TiffImageData.Data[] array = new TiffImageData.Data[tiffRawImageDataElements.size()];
        final boolean b = byteSource instanceof ByteSourceFile;
        int i = 0;
        final int n = 0;
        if (b) {
            final ByteSourceFile byteSourceFile = (ByteSourceFile)byteSource;
            for (int j = n; j < tiffRawImageDataElements.size(); ++j) {
                final TiffDirectory.ImageDataElement imageDataElement = tiffRawImageDataElements.get(j);
                array[j] = new TiffImageData.ByteSourceData(imageDataElement.offset, imageDataElement.length, byteSourceFile);
            }
        }
        else {
            while (i < tiffRawImageDataElements.size()) {
                final TiffDirectory.ImageDataElement imageDataElement2 = tiffRawImageDataElements.get(i);
                array[i] = new TiffImageData.Data(imageDataElement2.offset, imageDataElement2.length, byteSource.getBlock(imageDataElement2.offset, imageDataElement2.length));
                ++i;
            }
        }
        if (tiffDirectory.imageDataInStrips()) {
            final TiffField field = tiffDirectory.findField(TiffTagConstants.TIFF_TAG_ROWS_PER_STRIP);
            int n2 = Integer.MAX_VALUE;
            if (field != null) {
                n2 = field.getIntValue();
            }
            else {
                final TiffField field2 = tiffDirectory.findField(TiffTagConstants.TIFF_TAG_IMAGE_LENGTH);
                if (field2 != null) {
                    n2 = field2.getIntValue();
                }
            }
            return new TiffImageData.Strips(array, n2);
        }
        final TiffField field3 = tiffDirectory.findField(TiffTagConstants.TIFF_TAG_TILE_WIDTH);
        if (field3 == null) {
            throw new ImageReadException("Can't find tile width field.");
        }
        final int intValue = field3.getIntValue();
        final TiffField field4 = tiffDirectory.findField(TiffTagConstants.TIFF_TAG_TILE_LENGTH);
        if (field4 == null) {
            throw new ImageReadException("Can't find tile length field.");
        }
        return new TiffImageData.Tiles(array, intValue, field4.getIntValue());
    }
    
    private void readDirectories(final ByteSource byteSource, final FormatCompliance formatCompliance, final Listener listener) throws ImageReadException, IOException {
        final TiffHeader tiffHeader = this.readTiffHeader(byteSource);
        if (!listener.setTiffHeader(tiffHeader)) {
            return;
        }
        this.readDirectory(byteSource, tiffHeader.offsetToFirstIFD, 0, formatCompliance, listener, new ArrayList<Number>());
    }
    
    private boolean readDirectory(final ByteSource byteSource, final long n, final int n2, final FormatCompliance formatCompliance, final Listener listener, final List<Number> list) throws ImageReadException, IOException {
        return this.readDirectory(byteSource, n, n2, formatCompliance, listener, false, list);
    }
    
    private boolean readDirectory(final ByteSource p0, final long p1, final int p2, final FormatCompliance p3, final Listener p4, final boolean p5, final List<Number> p6) throws ImageReadException, IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     2: lload_2        
        //     3: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //     6: invokeinterface java/util/List.contains:(Ljava/lang/Object;)Z
        //    11: ifeq            16
        //    14: iconst_0       
        //    15: ireturn        
        //    16: aload           8
        //    18: lload_2        
        //    19: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //    22: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //    27: pop            
        //    28: aload_1        
        //    29: invokevirtual   org/apache/commons/imaging/common/bytesource/ByteSource.getLength:()J
        //    32: lstore          15
        //    34: lload_2        
        //    35: lload           15
        //    37: lcmp           
        //    38: iflt            55
        //    41: iconst_1       
        //    42: iconst_1       
        //    43: anewarray       Ljava/io/Closeable;
        //    46: dup            
        //    47: iconst_0       
        //    48: aconst_null    
        //    49: aastore        
        //    50: invokestatic    org/apache/commons/imaging/util/IoUtils.closeQuietly:(Z[Ljava/io/Closeable;)V
        //    53: iconst_1       
        //    54: ireturn        
        //    55: aload_1        
        //    56: invokevirtual   org/apache/commons/imaging/common/bytesource/ByteSource.getInputStream:()Ljava/io/InputStream;
        //    59: astore          21
        //    61: aload           21
        //    63: lload_2        
        //    64: invokestatic    org/apache/commons/imaging/common/BinaryFunctions.skipBytes:(Ljava/io/InputStream;J)V
        //    67: new             Ljava/util/ArrayList;
        //    70: astore          26
        //    72: aload           26
        //    74: invokespecial   java/util/ArrayList.<init>:()V
        //    77: ldc             "DirectoryEntryCount"
        //    79: aload           21
        //    81: ldc             "Not a Valid TIFF File"
        //    83: aload_0        
        //    84: invokevirtual   org/apache/commons/imaging/formats/tiff/TiffReader.getByteOrder:()Ljava/nio/ByteOrder;
        //    87: invokestatic    org/apache/commons/imaging/common/BinaryFunctions.read2Bytes:(Ljava/lang/String;Ljava/io/InputStream;Ljava/lang/String;Ljava/nio/ByteOrder;)I
        //    90: istore          9
        //    92: iconst_0       
        //    93: istore          10
        //    95: iload           10
        //    97: iload           9
        //    99: if_icmpge       443
        //   102: ldc             "Tag"
        //   104: aload           21
        //   106: ldc             "Not a Valid TIFF File"
        //   108: aload_0        
        //   109: invokevirtual   org/apache/commons/imaging/formats/tiff/TiffReader.getByteOrder:()Ljava/nio/ByteOrder;
        //   112: invokestatic    org/apache/commons/imaging/common/BinaryFunctions.read2Bytes:(Ljava/lang/String;Ljava/io/InputStream;Ljava/lang/String;Ljava/nio/ByteOrder;)I
        //   115: istore          12
        //   117: ldc             "Type"
        //   119: aload           21
        //   121: ldc             "Not a Valid TIFF File"
        //   123: aload_0        
        //   124: invokevirtual   org/apache/commons/imaging/formats/tiff/TiffReader.getByteOrder:()Ljava/nio/ByteOrder;
        //   127: invokestatic    org/apache/commons/imaging/common/BinaryFunctions.read2Bytes:(Ljava/lang/String;Ljava/io/InputStream;Ljava/lang/String;Ljava/nio/ByteOrder;)I
        //   130: istore          11
        //   132: ldc2_w          4294967295
        //   135: ldc             "Count"
        //   137: aload           21
        //   139: ldc             "Not a Valid TIFF File"
        //   141: aload_0        
        //   142: invokevirtual   org/apache/commons/imaging/formats/tiff/TiffReader.getByteOrder:()Ljava/nio/ByteOrder;
        //   145: invokestatic    org/apache/commons/imaging/common/BinaryFunctions.read4Bytes:(Ljava/lang/String;Ljava/io/InputStream;Ljava/lang/String;Ljava/nio/ByteOrder;)I
        //   148: i2l            
        //   149: land           
        //   150: lstore          17
        //   152: ldc             "Offset"
        //   154: aload           21
        //   156: iconst_4       
        //   157: ldc             "Not a Valid TIFF File"
        //   159: invokestatic    org/apache/commons/imaging/common/BinaryFunctions.readBytes:(Ljava/lang/String;Ljava/io/InputStream;ILjava/lang/String;)[B
        //   162: astore          22
        //   164: aload           22
        //   166: aload_0        
        //   167: invokevirtual   org/apache/commons/imaging/formats/tiff/TiffReader.getByteOrder:()Ljava/nio/ByteOrder;
        //   170: invokestatic    org/apache/commons/imaging/common/ByteConversions.toInt:([BLjava/nio/ByteOrder;)I
        //   173: istore          13
        //   175: ldc2_w          4294967295
        //   178: iload           13
        //   180: i2l            
        //   181: land           
        //   182: lstore          15
        //   184: iload           12
        //   186: ifne            192
        //   189: goto            433
        //   192: iload           11
        //   194: invokestatic    org/apache/commons/imaging/formats/tiff/fieldtypes/FieldType.getFieldType:(I)Lorg/apache/commons/imaging/formats/tiff/fieldtypes/FieldType;
        //   197: astore          23
        //   199: aload           23
        //   201: invokevirtual   org/apache/commons/imaging/formats/tiff/fieldtypes/FieldType.getSize:()I
        //   204: i2l            
        //   205: lload           17
        //   207: lmul           
        //   208: lstore          19
        //   210: lload           19
        //   212: ldc2_w          4
        //   215: lcmp           
        //   216: ifle            362
        //   219: lload           15
        //   221: lconst_0       
        //   222: lcmp           
        //   223: iflt            256
        //   226: lload           15
        //   228: lload           19
        //   230: ladd           
        //   231: aload_1        
        //   232: invokevirtual   org/apache/commons/imaging/common/bytesource/ByteSource.getLength:()J
        //   235: lcmp           
        //   236: ifle            242
        //   239: goto            256
        //   242: aload_1        
        //   243: lload           15
        //   245: lload           19
        //   247: l2i            
        //   248: invokevirtual   org/apache/commons/imaging/common/bytesource/ByteSource.getBlock:(JI)[B
        //   251: astore          22
        //   253: goto            362
        //   256: aload_0        
        //   257: getfield        org/apache/commons/imaging/formats/tiff/TiffReader.strict:Z
        //   260: ifeq            433
        //   263: new             Ljava/io/IOException;
        //   266: astore          5
        //   268: new             Ljava/lang/StringBuilder;
        //   271: astore          6
        //   273: aload           6
        //   275: invokespecial   java/lang/StringBuilder.<init>:()V
        //   278: aload           6
        //   280: ldc_w           "Attempt to read byte range starting from "
        //   283: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   286: pop            
        //   287: aload           6
        //   289: lload           15
        //   291: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //   294: pop            
        //   295: aload           6
        //   297: ldc_w           " "
        //   300: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   303: pop            
        //   304: aload           6
        //   306: ldc_w           "of length "
        //   309: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   312: pop            
        //   313: aload           6
        //   315: lload           19
        //   317: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //   320: pop            
        //   321: aload           6
        //   323: ldc_w           " "
        //   326: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   329: pop            
        //   330: aload           6
        //   332: ldc_w           "which is outside the file's size of "
        //   335: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   338: pop            
        //   339: aload           6
        //   341: aload_1        
        //   342: invokevirtual   org/apache/commons/imaging/common/bytesource/ByteSource.getLength:()J
        //   345: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //   348: pop            
        //   349: aload           5
        //   351: aload           6
        //   353: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   356: invokespecial   java/io/IOException.<init>:(Ljava/lang/String;)V
        //   359: aload           5
        //   361: athrow         
        //   362: new             Lorg/apache/commons/imaging/formats/tiff/TiffField;
        //   365: astore          24
        //   367: aload           24
        //   369: iload           12
        //   371: iload           4
        //   373: aload           23
        //   375: lload           17
        //   377: lload           15
        //   379: aload           22
        //   381: aload_0        
        //   382: invokevirtual   org/apache/commons/imaging/formats/tiff/TiffReader.getByteOrder:()Ljava/nio/ByteOrder;
        //   385: iload           10
        //   387: invokespecial   org/apache/commons/imaging/formats/tiff/TiffField.<init>:(IILorg/apache/commons/imaging/formats/tiff/fieldtypes/FieldType;JJ[BLjava/nio/ByteOrder;I)V
        //   390: aload           26
        //   392: aload           24
        //   394: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   399: pop            
        //   400: aload           6
        //   402: aload           24
        //   404: invokeinterface org/apache/commons/imaging/formats/tiff/TiffReader$Listener.addField:(Lorg/apache/commons/imaging/formats/tiff/TiffField;)Z
        //   409: istore          14
        //   411: iload           14
        //   413: ifne            433
        //   416: iconst_1       
        //   417: anewarray       Ljava/io/Closeable;
        //   420: astore_1       
        //   421: aload_1        
        //   422: iconst_0       
        //   423: aload           21
        //   425: aastore        
        //   426: iconst_1       
        //   427: aload_1        
        //   428: invokestatic    org/apache/commons/imaging/util/IoUtils.closeQuietly:(Z[Ljava/io/Closeable;)V
        //   431: iconst_1       
        //   432: ireturn        
        //   433: iinc            10, 1
        //   436: goto            95
        //   439: astore_1       
        //   440: goto            890
        //   443: aload           21
        //   445: astore          22
        //   447: ldc_w           "nextDirectoryOffset"
        //   450: aload           21
        //   452: ldc             "Not a Valid TIFF File"
        //   454: aload_0        
        //   455: invokevirtual   org/apache/commons/imaging/formats/tiff/TiffReader.getByteOrder:()Ljava/nio/ByteOrder;
        //   458: invokestatic    org/apache/commons/imaging/common/BinaryFunctions.read4Bytes:(Ljava/lang/String;Ljava/io/InputStream;Ljava/lang/String;Ljava/nio/ByteOrder;)I
        //   461: i2l            
        //   462: lstore          15
        //   464: aload           21
        //   466: astore          22
        //   468: new             Lorg/apache/commons/imaging/formats/tiff/TiffDirectory;
        //   471: astore          28
        //   473: aload           21
        //   475: astore          22
        //   477: aload           28
        //   479: iload           4
        //   481: aload           26
        //   483: lload_2        
        //   484: ldc2_w          4294967295
        //   487: lload           15
        //   489: land           
        //   490: invokespecial   org/apache/commons/imaging/formats/tiff/TiffDirectory.<init>:(ILjava/util/List;JJ)V
        //   493: aload           21
        //   495: astore          22
        //   497: aload           6
        //   499: invokeinterface org/apache/commons/imaging/formats/tiff/TiffReader$Listener.readImageData:()Z
        //   504: istore          14
        //   506: iload           14
        //   508: ifeq            551
        //   511: aload           28
        //   513: invokevirtual   org/apache/commons/imaging/formats/tiff/TiffDirectory.hasTiffImageData:()Z
        //   516: ifeq            531
        //   519: aload           28
        //   521: aload_0        
        //   522: aload_1        
        //   523: aload           28
        //   525: invokespecial   org/apache/commons/imaging/formats/tiff/TiffReader.getTiffRawImageData:(Lorg/apache/commons/imaging/common/bytesource/ByteSource;Lorg/apache/commons/imaging/formats/tiff/TiffDirectory;)Lorg/apache/commons/imaging/formats/tiff/TiffImageData;
        //   528: invokevirtual   org/apache/commons/imaging/formats/tiff/TiffDirectory.setTiffImageData:(Lorg/apache/commons/imaging/formats/tiff/TiffImageData;)V
        //   531: aload           28
        //   533: invokevirtual   org/apache/commons/imaging/formats/tiff/TiffDirectory.hasJpegImageData:()Z
        //   536: ifeq            551
        //   539: aload           28
        //   541: aload_0        
        //   542: aload_1        
        //   543: aload           28
        //   545: invokespecial   org/apache/commons/imaging/formats/tiff/TiffReader.getJpegRawImageData:(Lorg/apache/commons/imaging/common/bytesource/ByteSource;Lorg/apache/commons/imaging/formats/tiff/TiffDirectory;)Lorg/apache/commons/imaging/formats/tiff/JpegImageData;
        //   548: invokevirtual   org/apache/commons/imaging/formats/tiff/TiffDirectory.setJpegImageData:(Lorg/apache/commons/imaging/formats/tiff/JpegImageData;)V
        //   551: aload           21
        //   553: astore          22
        //   555: aload           6
        //   557: aload           28
        //   559: invokeinterface org/apache/commons/imaging/formats/tiff/TiffReader$Listener.addDirectory:(Lorg/apache/commons/imaging/formats/tiff/TiffDirectory;)Z
        //   564: istore          14
        //   566: iload           14
        //   568: ifne            584
        //   571: iconst_1       
        //   572: anewarray       Ljava/io/Closeable;
        //   575: astore_1       
        //   576: aload_1        
        //   577: iconst_0       
        //   578: aload           21
        //   580: aastore        
        //   581: goto            426
        //   584: aload           21
        //   586: astore          23
        //   588: aload           21
        //   590: astore          22
        //   592: aload           6
        //   594: invokeinterface org/apache/commons/imaging/formats/tiff/TiffReader$Listener.readOffsetDirectories:()Z
        //   599: ifeq            828
        //   602: aload           21
        //   604: astore          22
        //   606: iconst_3       
        //   607: anewarray       Lorg/apache/commons/imaging/formats/tiff/taginfos/TagInfoLong;
        //   610: astore          24
        //   612: aload           21
        //   614: astore          22
        //   616: aload           24
        //   618: iconst_0       
        //   619: getstatic       org/apache/commons/imaging/formats/tiff/constants/ExifTagConstants.EXIF_TAG_EXIF_OFFSET:Lorg/apache/commons/imaging/formats/tiff/taginfos/TagInfoDirectory;
        //   622: aastore        
        //   623: aload           21
        //   625: astore          22
        //   627: getstatic       org/apache/commons/imaging/formats/tiff/constants/ExifTagConstants.EXIF_TAG_GPSINFO:Lorg/apache/commons/imaging/formats/tiff/taginfos/TagInfoDirectory;
        //   630: astore          23
        //   632: aload           24
        //   634: iconst_1       
        //   635: aload           23
        //   637: aastore        
        //   638: aload           21
        //   640: astore          22
        //   642: aload           24
        //   644: iconst_2       
        //   645: getstatic       org/apache/commons/imaging/formats/tiff/constants/ExifTagConstants.EXIF_TAG_INTEROP_OFFSET:Lorg/apache/commons/imaging/formats/tiff/taginfos/TagInfoDirectory;
        //   648: aastore        
        //   649: aload           21
        //   651: astore          22
        //   653: iconst_3       
        //   654: newarray        I
        //   656: astore          25
        //   658: aload           21
        //   660: astore          22
        //   662: aload           25
        //   664: dup            
        //   665: iconst_0       
        //   666: bipush          -2
        //   668: iastore        
        //   669: dup            
        //   670: iconst_1       
        //   671: bipush          -3
        //   673: iastore        
        //   674: dup            
        //   675: iconst_2       
        //   676: bipush          -4
        //   678: iastore        
        //   679: pop            
        //   680: iconst_0       
        //   681: istore          9
        //   683: aload           21
        //   685: astore          23
        //   687: aload           21
        //   689: astore          22
        //   691: iload           9
        //   693: aload           24
        //   695: arraylength    
        //   696: if_icmpge       828
        //   699: aload           24
        //   701: iload           9
        //   703: aaload         
        //   704: astore          23
        //   706: aload           21
        //   708: astore          22
        //   710: aload           28
        //   712: aload           23
        //   714: invokevirtual   org/apache/commons/imaging/formats/tiff/TiffDirectory.findField:(Lorg/apache/commons/imaging/formats/tiff/taginfos/TagInfo;)Lorg/apache/commons/imaging/formats/tiff/TiffField;
        //   717: astore          29
        //   719: aload           29
        //   721: ifnull          822
        //   724: aload           21
        //   726: astore          22
        //   728: aload           28
        //   730: aload           23
        //   732: invokevirtual   org/apache/commons/imaging/formats/tiff/TiffDirectory.getSingleFieldValue:(Lorg/apache/commons/imaging/formats/tiff/taginfos/TagInfoLong;)I
        //   735: i2l            
        //   736: lstore_2       
        //   737: aload           25
        //   739: iload           9
        //   741: iaload         
        //   742: istore          10
        //   744: aload           21
        //   746: astore          22
        //   748: aload_0        
        //   749: aload_1        
        //   750: lload_2        
        //   751: iload           10
        //   753: aload           5
        //   755: aload           6
        //   757: iconst_1       
        //   758: aload           8
        //   760: invokespecial   org/apache/commons/imaging/formats/tiff/TiffReader.readDirectory:(Lorg/apache/commons/imaging/common/bytesource/ByteSource;JILorg/apache/commons/imaging/FormatCompliance;Lorg/apache/commons/imaging/formats/tiff/TiffReader$Listener;ZLjava/util/List;)Z
        //   763: istore          14
        //   765: goto            800
        //   768: astore          23
        //   770: goto            775
        //   773: astore          23
        //   775: aload           21
        //   777: astore          27
        //   779: aload           27
        //   781: astore          22
        //   783: aload_0        
        //   784: getfield        org/apache/commons/imaging/formats/tiff/TiffReader.strict:Z
        //   787: ifeq            797
        //   790: aload           27
        //   792: astore          22
        //   794: aload           23
        //   796: athrow         
        //   797: iconst_0       
        //   798: istore          14
        //   800: aload           21
        //   802: astore          22
        //   804: iload           14
        //   806: ifne            822
        //   809: aload           26
        //   811: aload           29
        //   813: invokeinterface java/util/List.remove:(Ljava/lang/Object;)Z
        //   818: pop            
        //   819: goto            822
        //   822: iinc            9, 1
        //   825: goto            683
        //   828: iload           7
        //   830: ifne            872
        //   833: aload           23
        //   835: astore          22
        //   837: aload           28
        //   839: getfield        org/apache/commons/imaging/formats/tiff/TiffDirectory.nextDirectoryOffset:J
        //   842: lconst_0       
        //   843: lcmp           
        //   844: ifle            872
        //   847: aload           23
        //   849: astore          22
        //   851: aload_0        
        //   852: aload_1        
        //   853: aload           28
        //   855: getfield        org/apache/commons/imaging/formats/tiff/TiffDirectory.nextDirectoryOffset:J
        //   858: iload           4
        //   860: iconst_1       
        //   861: iadd           
        //   862: aload           5
        //   864: aload           6
        //   866: aload           8
        //   868: invokespecial   org/apache/commons/imaging/formats/tiff/TiffReader.readDirectory:(Lorg/apache/commons/imaging/common/bytesource/ByteSource;JILorg/apache/commons/imaging/FormatCompliance;Lorg/apache/commons/imaging/formats/tiff/TiffReader$Listener;Ljava/util/List;)Z
        //   871: pop            
        //   872: iconst_1       
        //   873: anewarray       Ljava/io/Closeable;
        //   876: astore_1       
        //   877: aload_1        
        //   878: iconst_0       
        //   879: aload           23
        //   881: aastore        
        //   882: goto            426
        //   885: astore_1       
        //   886: aload           22
        //   888: astore          21
        //   890: goto            929
        //   893: astore_1       
        //   894: aload           21
        //   896: astore          22
        //   898: aload_0        
        //   899: getfield        org/apache/commons/imaging/formats/tiff/TiffReader.strict:Z
        //   902: ifeq            911
        //   905: aload           21
        //   907: astore          22
        //   909: aload_1        
        //   910: athrow         
        //   911: iconst_1       
        //   912: anewarray       Ljava/io/Closeable;
        //   915: astore_1       
        //   916: aload_1        
        //   917: iconst_0       
        //   918: aload           21
        //   920: aastore        
        //   921: goto            426
        //   924: astore_1       
        //   925: aload           22
        //   927: astore          21
        //   929: goto            940
        //   932: astore_1       
        //   933: goto            940
        //   936: astore_1       
        //   937: aconst_null    
        //   938: astore          21
        //   940: iconst_0       
        //   941: iconst_1       
        //   942: anewarray       Ljava/io/Closeable;
        //   945: dup            
        //   946: iconst_0       
        //   947: aload           21
        //   949: aastore        
        //   950: invokestatic    org/apache/commons/imaging/util/IoUtils.closeQuietly:(Z[Ljava/io/Closeable;)V
        //   953: aload_1        
        //   954: athrow         
        //   955: astore          22
        //   957: goto            189
        //    Exceptions:
        //  throws org.apache.commons.imaging.ImageReadException
        //  throws java.io.IOException
        //    Signature:
        //  (Lorg/apache/commons/imaging/common/bytesource/ByteSource;JILorg/apache/commons/imaging/FormatCompliance;Lorg/apache/commons/imaging/formats/tiff/TiffReader$Listener;ZLjava/util/List<Ljava/lang/Number;>;)Z
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                           
        //  -----  -----  -----  -----  -----------------------------------------------
        //  28     34     936    940    Any
        //  55     61     936    940    Any
        //  61     77     932    936    Any
        //  77     92     893    924    Ljava/io/IOException;
        //  77     92     932    936    Any
        //  102    175    439    443    Any
        //  192    199    955    960    Lorg/apache/commons/imaging/ImageReadException;
        //  192    199    439    443    Any
        //  199    210    439    443    Any
        //  226    239    439    443    Any
        //  242    253    439    443    Any
        //  256    362    439    443    Any
        //  362    411    439    443    Any
        //  447    464    885    890    Any
        //  468    473    885    890    Any
        //  477    493    885    890    Any
        //  497    506    885    890    Any
        //  511    531    439    443    Any
        //  531    551    439    443    Any
        //  555    566    885    890    Any
        //  592    602    885    890    Any
        //  606    612    885    890    Any
        //  616    623    885    890    Any
        //  627    632    885    890    Any
        //  642    649    885    890    Any
        //  653    658    885    890    Any
        //  662    680    885    890    Any
        //  691    699    885    890    Any
        //  710    719    885    890    Any
        //  728    737    773    775    Lorg/apache/commons/imaging/ImageReadException;
        //  728    737    885    890    Any
        //  748    765    768    773    Lorg/apache/commons/imaging/ImageReadException;
        //  748    765    924    929    Any
        //  783    790    924    929    Any
        //  794    797    924    929    Any
        //  809    819    924    929    Any
        //  837    847    924    929    Any
        //  851    872    924    929    Any
        //  898    905    924    929    Any
        //  909    911    924    929    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException: Cannot invoke "com.strobel.assembler.metadata.TypeReference.getSimpleType()" because "type" is null
        //     at com.strobel.assembler.ir.StackMappingVisitor.push(StackMappingVisitor.java:290)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:837)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2086)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:203)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:93)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:868)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:761)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:638)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:605)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:195)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:162)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:137)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:333)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:254)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:129)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private TiffHeader readTiffHeader(final InputStream inputStream) throws ImageReadException, IOException {
        final byte byte1 = BinaryFunctions.readByte("BYTE_ORDER_1", inputStream, "Not a Valid TIFF File");
        final byte byte2 = BinaryFunctions.readByte("BYTE_ORDER_2", inputStream, "Not a Valid TIFF File");
        if (byte1 != byte2) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Byte Order bytes don't match (");
            sb.append(byte1);
            sb.append(", ");
            sb.append(byte2);
            sb.append(").");
            throw new ImageReadException(sb.toString());
        }
        final ByteOrder tiffByteOrder = this.getTiffByteOrder(byte1);
        this.setByteOrder(tiffByteOrder);
        final int read2Bytes = BinaryFunctions.read2Bytes("tiffVersion", inputStream, "Not a Valid TIFF File", this.getByteOrder());
        if (read2Bytes != 42) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Unknown Tiff Version: ");
            sb2.append(read2Bytes);
            throw new ImageReadException(sb2.toString());
        }
        final long n = 0xFFFFFFFFL & (long)BinaryFunctions.read4Bytes("offsetToFirstIFD", inputStream, "Not a Valid TIFF File", this.getByteOrder());
        BinaryFunctions.skipBytes(inputStream, n - 8L, "Not a Valid TIFF File: couldn't find IFDs");
        if (this.getDebug()) {
            System.out.println("");
        }
        return new TiffHeader(tiffByteOrder, read2Bytes, n);
    }
    
    private TiffHeader readTiffHeader(final ByteSource byteSource) throws ImageReadException, IOException {
        Closeable closeable;
        try {
            final InputStream inputStream = byteSource.getInputStream();
            try {
                final TiffHeader tiffHeader = this.readTiffHeader(inputStream);
                IoUtils.closeQuietly(true, inputStream);
                return tiffHeader;
            }
            finally {}
        }
        finally {
            closeable = null;
        }
        IoUtils.closeQuietly(false, closeable);
    }
    
    public void read(final ByteSource byteSource, final Map<String, Object> map, final FormatCompliance formatCompliance, final Listener listener) throws ImageReadException, IOException {
        this.readDirectories(byteSource, formatCompliance, listener);
    }
    
    public TiffContents readContents(final ByteSource byteSource, final Map<String, Object> map, final FormatCompliance formatCompliance) throws ImageReadException, IOException {
        final Collector collector = new Collector(map);
        this.read(byteSource, map, formatCompliance, (Listener)collector);
        return collector.getContents();
    }
    
    public TiffContents readDirectories(final ByteSource byteSource, final boolean b, final FormatCompliance formatCompliance) throws ImageReadException, IOException {
        final Collector collector = new Collector(null);
        this.readDirectories(byteSource, formatCompliance, (Listener)collector);
        final TiffContents contents = collector.getContents();
        if (contents.directories.size() < 1) {
            throw new ImageReadException("Image did not contain any directories.");
        }
        return contents;
    }
    
    public TiffContents readFirstDirectory(final ByteSource byteSource, final Map<String, Object> map, final boolean b, final FormatCompliance formatCompliance) throws ImageReadException, IOException {
        final FirstDirectoryCollector firstDirectoryCollector = new FirstDirectoryCollector(b);
        this.read(byteSource, map, formatCompliance, (Listener)firstDirectoryCollector);
        final TiffContents contents = ((Collector)firstDirectoryCollector).getContents();
        if (contents.directories.size() < 1) {
            throw new ImageReadException("Image did not contain any directories.");
        }
        return contents;
    }
    
    private static class Collector implements Listener
    {
        private final List<TiffDirectory> directories;
        private final List<TiffField> fields;
        private final boolean readThumbnails;
        private TiffHeader tiffHeader;
        
        public Collector() {
            this(null);
        }
        
        public Collector(final Map<String, Object> map) {
            this.directories = new ArrayList<TiffDirectory>();
            this.fields = new ArrayList<TiffField>();
            this.readThumbnails = (map == null || !map.containsKey("READ_THUMBNAILS") || Boolean.TRUE.equals(map.get("READ_THUMBNAILS")));
        }
        
        @Override
        public boolean addDirectory(final TiffDirectory tiffDirectory) {
            this.directories.add(tiffDirectory);
            return true;
        }
        
        @Override
        public boolean addField(final TiffField tiffField) {
            this.fields.add(tiffField);
            return true;
        }
        
        public TiffContents getContents() {
            return new TiffContents(this.tiffHeader, this.directories);
        }
        
        @Override
        public boolean readImageData() {
            return this.readThumbnails;
        }
        
        @Override
        public boolean readOffsetDirectories() {
            return true;
        }
        
        @Override
        public boolean setTiffHeader(final TiffHeader tiffHeader) {
            this.tiffHeader = tiffHeader;
            return true;
        }
    }
    
    private static class FirstDirectoryCollector extends Collector
    {
        private final boolean readImageData;
        
        public FirstDirectoryCollector(final boolean readImageData) {
            this.readImageData = readImageData;
        }
        
        @Override
        public boolean addDirectory(final TiffDirectory tiffDirectory) {
            super.addDirectory(tiffDirectory);
            return false;
        }
        
        @Override
        public boolean readImageData() {
            return this.readImageData;
        }
    }
    
    public interface Listener
    {
        boolean addDirectory(final TiffDirectory p0);
        
        boolean addField(final TiffField p0);
        
        boolean readImageData();
        
        boolean readOffsetDirectories();
        
        boolean setTiffHeader(final TiffHeader p0);
    }
}
