// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.jpeg.iptc;

import org.apache.commons.imaging.util.IoUtils;
import java.io.Closeable;
import org.apache.commons.imaging.ImageWriteException;
import java.util.Collections;
import java.util.Comparator;
import java.io.OutputStream;
import org.apache.commons.imaging.common.BinaryOutputStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.Collection;
import java.util.Map;
import java.io.PrintStream;
import org.apache.commons.imaging.util.Debug;
import java.util.ArrayList;
import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import java.util.List;
import org.apache.commons.imaging.common.ByteConversions;
import org.apache.commons.imaging.common.BinaryFunctions;
import org.apache.commons.imaging.formats.jpeg.JpegConstants;
import java.nio.ByteOrder;
import org.apache.commons.imaging.common.BinaryFileParser;

public class IptcParser extends BinaryFileParser
{
    private static final ByteOrder APP13_BYTE_ORDER;
    
    static {
        APP13_BYTE_ORDER = ByteOrder.BIG_ENDIAN;
    }
    
    public IptcParser() {
        this.setByteOrder(ByteOrder.BIG_ENDIAN);
    }
    
    public boolean isPhotoshopJpegSegment(final byte[] array) {
        final boolean startsWith = BinaryFunctions.startsWith(array, JpegConstants.PHOTOSHOP_IDENTIFICATION_STRING);
        final boolean b = false;
        if (!startsWith) {
            return false;
        }
        final int size = JpegConstants.PHOTOSHOP_IDENTIFICATION_STRING.size();
        boolean b2 = b;
        if (size + 4 <= array.length) {
            b2 = b;
            if (ByteConversions.toInt(array, size, IptcParser.APP13_BYTE_ORDER) == JpegConstants.CONST_8BIM) {
                b2 = true;
            }
        }
        return b2;
    }
    
    protected List<IptcBlock> parseAllBlocks(final byte[] p0, final boolean p1, final boolean p2) throws ImageReadException, IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: invokespecial   java/util/ArrayList.<init>:()V
        //     7: astore          8
        //     9: new             Ljava/io/ByteArrayInputStream;
        //    12: astore          7
        //    14: aload           7
        //    16: aload_1        
        //    17: invokespecial   java/io/ByteArrayInputStream.<init>:([B)V
        //    20: ldc             ""
        //    22: aload           7
        //    24: getstatic       org/apache/commons/imaging/formats/jpeg/JpegConstants.PHOTOSHOP_IDENTIFICATION_STRING:Lorg/apache/commons/imaging/common/BinaryConstant;
        //    27: invokevirtual   org/apache/commons/imaging/common/BinaryConstant.size:()I
        //    30: ldc             "App13 Segment missing identification string"
        //    32: invokestatic    org/apache/commons/imaging/common/BinaryFunctions.readBytes:(Ljava/lang/String;Ljava/io/InputStream;ILjava/lang/String;)[B
        //    35: astore          6
        //    37: getstatic       org/apache/commons/imaging/formats/jpeg/JpegConstants.PHOTOSHOP_IDENTIFICATION_STRING:Lorg/apache/commons/imaging/common/BinaryConstant;
        //    40: aload           6
        //    42: invokevirtual   org/apache/commons/imaging/common/BinaryConstant.equals:([B)Z
        //    45: ifne            60
        //    48: new             Lorg/apache/commons/imaging/ImageReadException;
        //    51: astore_1       
        //    52: aload_1        
        //    53: ldc             "Not a Photoshop App13 Segment"
        //    55: invokespecial   org/apache/commons/imaging/ImageReadException.<init>:(Ljava/lang/String;)V
        //    58: aload_1        
        //    59: athrow         
        //    60: ldc             ""
        //    62: aload           7
        //    64: ldc             "Image Resource Block missing identification string"
        //    66: getstatic       org/apache/commons/imaging/formats/jpeg/iptc/IptcParser.APP13_BYTE_ORDER:Ljava/nio/ByteOrder;
        //    69: invokestatic    org/apache/commons/imaging/common/BinaryFunctions.read4Bytes:(Ljava/lang/String;Ljava/io/InputStream;Ljava/lang/String;Ljava/nio/ByteOrder;)I
        //    72: istore          4
        //    74: iload           4
        //    76: getstatic       org/apache/commons/imaging/formats/jpeg/JpegConstants.CONST_8BIM:I
        //    79: if_icmpeq       94
        //    82: new             Lorg/apache/commons/imaging/ImageReadException;
        //    85: astore_1       
        //    86: aload_1        
        //    87: ldc             "Invalid Image Resource Block Signature"
        //    89: invokespecial   org/apache/commons/imaging/ImageReadException.<init>:(Ljava/lang/String;)V
        //    92: aload_1        
        //    93: athrow         
        //    94: ldc             ""
        //    96: aload           7
        //    98: ldc             "Image Resource Block missing type"
        //   100: getstatic       org/apache/commons/imaging/formats/jpeg/iptc/IptcParser.APP13_BYTE_ORDER:Ljava/nio/ByteOrder;
        //   103: invokestatic    org/apache/commons/imaging/common/BinaryFunctions.read2Bytes:(Ljava/lang/String;Ljava/io/InputStream;Ljava/lang/String;Ljava/nio/ByteOrder;)I
        //   106: istore          4
        //   108: iload_2        
        //   109: ifeq            173
        //   112: new             Ljava/lang/StringBuilder;
        //   115: astore          6
        //   117: aload           6
        //   119: invokespecial   java/lang/StringBuilder.<init>:()V
        //   122: aload           6
        //   124: ldc             "blockType: "
        //   126: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   129: pop            
        //   130: aload           6
        //   132: iload           4
        //   134: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   137: pop            
        //   138: aload           6
        //   140: ldc             " (0x"
        //   142: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   145: pop            
        //   146: aload           6
        //   148: iload           4
        //   150: invokestatic    java/lang/Integer.toHexString:(I)Ljava/lang/String;
        //   153: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   156: pop            
        //   157: aload           6
        //   159: ldc             ")"
        //   161: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   164: pop            
        //   165: aload           6
        //   167: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   170: invokestatic    org/apache/commons/imaging/util/Debug.debug:(Ljava/lang/String;)V
        //   173: ldc             "Name length"
        //   175: aload           7
        //   177: ldc             "Image Resource Block missing name length"
        //   179: invokestatic    org/apache/commons/imaging/common/BinaryFunctions.readByte:(Ljava/lang/String;Ljava/io/InputStream;Ljava/lang/String;)B
        //   182: istore          5
        //   184: iload_2        
        //   185: ifeq            254
        //   188: iload           5
        //   190: ifle            254
        //   193: new             Ljava/lang/StringBuilder;
        //   196: astore          6
        //   198: aload           6
        //   200: invokespecial   java/lang/StringBuilder.<init>:()V
        //   203: aload           6
        //   205: ldc             "blockNameLength: "
        //   207: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   210: pop            
        //   211: aload           6
        //   213: iload           5
        //   215: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   218: pop            
        //   219: aload           6
        //   221: ldc             " (0x"
        //   223: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   226: pop            
        //   227: aload           6
        //   229: iload           5
        //   231: invokestatic    java/lang/Integer.toHexString:(I)Ljava/lang/String;
        //   234: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   237: pop            
        //   238: aload           6
        //   240: ldc             ")"
        //   242: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   245: pop            
        //   246: aload           6
        //   248: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   251: invokestatic    org/apache/commons/imaging/util/Debug.debug:(Ljava/lang/String;)V
        //   254: iload           5
        //   256: ifne            277
        //   259: ldc             "Block name bytes"
        //   261: aload           7
        //   263: ldc             "Image Resource Block has invalid name"
        //   265: invokestatic    org/apache/commons/imaging/common/BinaryFunctions.readByte:(Ljava/lang/String;Ljava/io/InputStream;Ljava/lang/String;)B
        //   268: pop            
        //   269: iconst_0       
        //   270: newarray        B
        //   272: astore          6
        //   274: goto            307
        //   277: ldc             ""
        //   279: aload           7
        //   281: iload           5
        //   283: ldc             "Invalid Image Resource Block name"
        //   285: invokestatic    org/apache/commons/imaging/common/BinaryFunctions.readBytes:(Ljava/lang/String;Ljava/io/InputStream;ILjava/lang/String;)[B
        //   288: astore          6
        //   290: iload           5
        //   292: iconst_2       
        //   293: irem           
        //   294: ifne            307
        //   297: ldc             "Padding byte"
        //   299: aload           7
        //   301: ldc             "Image Resource Block missing padding byte"
        //   303: invokestatic    org/apache/commons/imaging/common/BinaryFunctions.readByte:(Ljava/lang/String;Ljava/io/InputStream;Ljava/lang/String;)B
        //   306: pop            
        //   307: ldc             ""
        //   309: aload           7
        //   311: ldc             "Image Resource Block missing size"
        //   313: getstatic       org/apache/commons/imaging/formats/jpeg/iptc/IptcParser.APP13_BYTE_ORDER:Ljava/nio/ByteOrder;
        //   316: invokestatic    org/apache/commons/imaging/common/BinaryFunctions.read4Bytes:(Ljava/lang/String;Ljava/io/InputStream;Ljava/lang/String;Ljava/nio/ByteOrder;)I
        //   319: istore          5
        //   321: iload_2        
        //   322: ifeq            386
        //   325: new             Ljava/lang/StringBuilder;
        //   328: astore          9
        //   330: aload           9
        //   332: invokespecial   java/lang/StringBuilder.<init>:()V
        //   335: aload           9
        //   337: ldc             "blockSize: "
        //   339: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   342: pop            
        //   343: aload           9
        //   345: iload           5
        //   347: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   350: pop            
        //   351: aload           9
        //   353: ldc             " (0x"
        //   355: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   358: pop            
        //   359: aload           9
        //   361: iload           5
        //   363: invokestatic    java/lang/Integer.toHexString:(I)Ljava/lang/String;
        //   366: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   369: pop            
        //   370: aload           9
        //   372: ldc             ")"
        //   374: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   377: pop            
        //   378: aload           9
        //   380: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   383: invokestatic    org/apache/commons/imaging/util/Debug.debug:(Ljava/lang/String;)V
        //   386: iload           5
        //   388: aload_1        
        //   389: arraylength    
        //   390: if_icmple       453
        //   393: new             Lorg/apache/commons/imaging/ImageReadException;
        //   396: astore          8
        //   398: new             Ljava/lang/StringBuilder;
        //   401: astore          6
        //   403: aload           6
        //   405: invokespecial   java/lang/StringBuilder.<init>:()V
        //   408: aload           6
        //   410: ldc             "Invalid Block Size : "
        //   412: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   415: pop            
        //   416: aload           6
        //   418: iload           5
        //   420: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   423: pop            
        //   424: aload           6
        //   426: ldc             " > "
        //   428: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   431: pop            
        //   432: aload           6
        //   434: aload_1        
        //   435: arraylength    
        //   436: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   439: pop            
        //   440: aload           8
        //   442: aload           6
        //   444: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   447: invokespecial   org/apache/commons/imaging/ImageReadException.<init>:(Ljava/lang/String;)V
        //   450: aload           8
        //   452: athrow         
        //   453: ldc             ""
        //   455: aload           7
        //   457: iload           5
        //   459: ldc             "Invalid Image Resource Block data"
        //   461: invokestatic    org/apache/commons/imaging/common/BinaryFunctions.readBytes:(Ljava/lang/String;Ljava/io/InputStream;ILjava/lang/String;)[B
        //   464: astore          9
        //   466: new             Lorg/apache/commons/imaging/formats/jpeg/iptc/IptcBlock;
        //   469: astore          10
        //   471: aload           10
        //   473: iload           4
        //   475: aload           6
        //   477: aload           9
        //   479: invokespecial   org/apache/commons/imaging/formats/jpeg/iptc/IptcBlock.<init>:(I[B[B)V
        //   482: aload           8
        //   484: aload           10
        //   486: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   491: pop            
        //   492: iload           5
        //   494: iconst_2       
        //   495: irem           
        //   496: ifeq            60
        //   499: ldc             "Padding byte"
        //   501: aload           7
        //   503: ldc             "Image Resource Block missing padding byte"
        //   505: invokestatic    org/apache/commons/imaging/common/BinaryFunctions.readByte:(Ljava/lang/String;Ljava/io/InputStream;Ljava/lang/String;)B
        //   508: pop            
        //   509: goto            60
        //   512: astore_1       
        //   513: iload_3        
        //   514: ifeq            526
        //   517: aload_1        
        //   518: athrow         
        //   519: astore_1       
        //   520: iload_3        
        //   521: ifeq            526
        //   524: aload_1        
        //   525: athrow         
        //   526: iconst_1       
        //   527: iconst_1       
        //   528: anewarray       Ljava/io/Closeable;
        //   531: dup            
        //   532: iconst_0       
        //   533: aload           7
        //   535: aastore        
        //   536: invokestatic    org/apache/commons/imaging/util/IoUtils.closeQuietly:(Z[Ljava/io/Closeable;)V
        //   539: aload           8
        //   541: areturn        
        //   542: astore          6
        //   544: aload           7
        //   546: astore_1       
        //   547: goto            554
        //   550: astore          6
        //   552: aconst_null    
        //   553: astore_1       
        //   554: iconst_0       
        //   555: iconst_1       
        //   556: anewarray       Ljava/io/Closeable;
        //   559: dup            
        //   560: iconst_0       
        //   561: aload_1        
        //   562: aastore        
        //   563: invokestatic    org/apache/commons/imaging/util/IoUtils.closeQuietly:(Z[Ljava/io/Closeable;)V
        //   566: aload           6
        //   568: athrow         
        //   569: astore_1       
        //   570: goto            526
        //    Exceptions:
        //  throws org.apache.commons.imaging.ImageReadException
        //  throws java.io.IOException
        //    Signature:
        //  ([BZZ)Ljava/util/List<Lorg/apache/commons/imaging/formats/jpeg/iptc/IptcBlock;>;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  9      20     550    554    Any
        //  20     60     542    550    Any
        //  60     74     569    573    Ljava/io/IOException;
        //  60     74     542    550    Any
        //  74     94     542    550    Any
        //  94     108    542    550    Any
        //  112    173    542    550    Any
        //  173    184    542    550    Any
        //  193    254    542    550    Any
        //  259    274    542    550    Any
        //  277    290    519    526    Ljava/io/IOException;
        //  277    290    542    550    Any
        //  297    307    542    550    Any
        //  307    321    542    550    Any
        //  325    386    542    550    Any
        //  386    453    542    550    Any
        //  453    466    512    519    Ljava/io/IOException;
        //  453    466    542    550    Any
        //  466    492    542    550    Any
        //  499    509    542    550    Any
        //  517    519    542    550    Any
        //  524    526    542    550    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0060:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2604)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:206)
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
    
    protected List<IptcRecord> parseIPTCBlock(final byte[] array, final boolean b) throws IOException {
        final ArrayList list = new ArrayList();
        int n = 0;
        while (true) {
            final int n2 = n + 1;
            if (n2 >= array.length) {
                return list;
            }
            final int n3 = array[n] & 0xFF;
            if (b) {
                final StringBuilder sb = new StringBuilder();
                sb.append("tagMarker: ");
                sb.append(n3);
                sb.append(" (0x");
                sb.append(Integer.toHexString(n3));
                sb.append(")");
                Debug.debug(sb.toString());
            }
            if (n3 != 28) {
                if (b) {
                    System.out.println("Unexpected record tag marker in IPTC data.");
                }
                return list;
            }
            int n4 = n2 + 1;
            final int n5 = array[n2] & 0xFF;
            if (b) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("recordNumber: ");
                sb2.append(n5);
                sb2.append(" (0x");
                sb2.append(Integer.toHexString(n5));
                sb2.append(")");
                Debug.debug(sb2.toString());
            }
            final int n6 = 0xFF & array[n4];
            if (b) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("recordType: ");
                sb3.append(n6);
                sb3.append(" (0x");
                sb3.append(Integer.toHexString(n6));
                sb3.append(")");
                Debug.debug(sb3.toString());
            }
            ++n4;
            final int uInt16 = ByteConversions.toUInt16(array, n4, this.getByteOrder());
            final int n7 = n4 + 2;
            final boolean b2 = uInt16 > 32767;
            if (b2 && b) {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("extendedDataset. dataFieldCountLength: ");
                sb4.append(uInt16 & 0x7FFF);
                Debug.debug(sb4.toString());
            }
            if (b2) {
                return list;
            }
            final byte[] slice = BinaryFunctions.slice(array, n7, uInt16);
            final int n8 = n7 + uInt16;
            if (n5 != 2) {
                n = n8;
            }
            else if (n6 == 0) {
                n = n8;
                if (!b) {
                    continue;
                }
                final PrintStream out = System.out;
                final StringBuilder sb5 = new StringBuilder();
                sb5.append("ignore record version record! ");
                sb5.append(list.size());
                out.println(sb5.toString());
                n = n8;
            }
            else {
                list.add(new IptcRecord(IptcTypeLookup.getIptcType(n6), slice, new String(slice, "ISO-8859-1")));
                n = n8;
            }
        }
    }
    
    public PhotoshopApp13Data parsePhotoshopSegment(final byte[] array, final Map<String, Object> map) throws ImageReadException, IOException {
        final boolean b = false;
        final boolean b2 = map != null && Boolean.TRUE.equals(map.get("STRICT"));
        boolean b3 = b;
        if (map != null) {
            b3 = b;
            if (Boolean.TRUE.equals(map.get("VERBOSE"))) {
                b3 = true;
            }
        }
        return this.parsePhotoshopSegment(array, b3, b2);
    }
    
    public PhotoshopApp13Data parsePhotoshopSegment(final byte[] array, final boolean b, final boolean b2) throws ImageReadException, IOException {
        final ArrayList list = new ArrayList();
        final List<IptcBlock> allBlocks = this.parseAllBlocks(array, b, b2);
        for (final IptcBlock iptcBlock : allBlocks) {
            if (!iptcBlock.isIPTCBlock()) {
                continue;
            }
            list.addAll(this.parseIPTCBlock(iptcBlock.blockData, b));
        }
        return new PhotoshopApp13Data(list, allBlocks);
    }
    
    public byte[] writeIPTCBlock(final List<IptcRecord> c) throws ImageWriteException, IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Closeable closeable;
        try {
            final BinaryOutputStream binaryOutputStream = new BinaryOutputStream(byteArrayOutputStream, this.getByteOrder());
            try {
                binaryOutputStream.write(28);
                binaryOutputStream.write(2);
                binaryOutputStream.write(IptcTypes.RECORD_VERSION.type);
                binaryOutputStream.write2Bytes(2);
                binaryOutputStream.write2Bytes(2);
                final ArrayList<Object> list = (ArrayList<Object>)new ArrayList<IptcRecord>(c);
                Collections.sort(list, (Comparator<? super Object>)new Comparator<IptcRecord>(this) {
                    final IptcParser this$0;
                    
                    @Override
                    public int compare(final IptcRecord iptcRecord, final IptcRecord iptcRecord2) {
                        return iptcRecord2.iptcType.getType() - iptcRecord.iptcType.getType();
                    }
                });
                for (final IptcRecord iptcRecord : list) {
                    if (iptcRecord.iptcType == IptcTypes.RECORD_VERSION) {
                        continue;
                    }
                    binaryOutputStream.write(28);
                    binaryOutputStream.write(2);
                    if (iptcRecord.iptcType.getType() < 0 || iptcRecord.iptcType.getType() > 255) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Invalid record type: ");
                        sb.append(iptcRecord.iptcType.getType());
                        throw new ImageWriteException(sb.toString());
                    }
                    binaryOutputStream.write(iptcRecord.iptcType.getType());
                    final byte[] bytes = iptcRecord.value.getBytes("ISO-8859-1");
                    if (!new String(bytes, "ISO-8859-1").equals(iptcRecord.value)) {
                        throw new ImageWriteException("Invalid record value, not ISO-8859-1");
                    }
                    binaryOutputStream.write2Bytes(bytes.length);
                    binaryOutputStream.write(bytes);
                }
                IoUtils.closeQuietly(true, binaryOutputStream);
                return byteArrayOutputStream.toByteArray();
            }
            finally {}
        }
        finally {
            closeable = null;
        }
        IoUtils.closeQuietly(false, closeable);
    }
    
    public byte[] writePhotoshopApp13Segment(final PhotoshopApp13Data photoshopApp13Data) throws IOException, ImageWriteException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final BinaryOutputStream binaryOutputStream = new BinaryOutputStream(byteArrayOutputStream);
        JpegConstants.PHOTOSHOP_IDENTIFICATION_STRING.writeTo(binaryOutputStream);
        for (final IptcBlock iptcBlock : photoshopApp13Data.getRawBlocks()) {
            binaryOutputStream.write4Bytes(JpegConstants.CONST_8BIM);
            if (iptcBlock.blockType < 0 || iptcBlock.blockType > 65535) {
                throw new ImageWriteException("Invalid IPTC block type.");
            }
            binaryOutputStream.write2Bytes(iptcBlock.blockType);
            if (iptcBlock.blockNameBytes.length > 255) {
                final StringBuilder sb = new StringBuilder();
                sb.append("IPTC block name is too long: ");
                sb.append(iptcBlock.blockNameBytes.length);
                throw new ImageWriteException(sb.toString());
            }
            binaryOutputStream.write(iptcBlock.blockNameBytes.length);
            binaryOutputStream.write(iptcBlock.blockNameBytes);
            if (iptcBlock.blockNameBytes.length % 2 == 0) {
                binaryOutputStream.write(0);
            }
            if (iptcBlock.blockData.length > 32767) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("IPTC block data is too long: ");
                sb2.append(iptcBlock.blockData.length);
                throw new ImageWriteException(sb2.toString());
            }
            binaryOutputStream.write4Bytes(iptcBlock.blockData.length);
            binaryOutputStream.write(iptcBlock.blockData);
            if (iptcBlock.blockData.length % 2 != 1) {
                continue;
            }
            binaryOutputStream.write(0);
        }
        binaryOutputStream.flush();
        return byteArrayOutputStream.toByteArray();
    }
}
