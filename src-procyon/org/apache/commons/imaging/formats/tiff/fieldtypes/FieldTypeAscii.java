// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.tiff.fieldtypes;

import org.apache.commons.imaging.ImageWriteException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;
import org.apache.commons.imaging.formats.tiff.TiffField;

public class FieldTypeAscii extends FieldType
{
    public FieldTypeAscii(final int n, final String s) {
        super(n, s, 1);
    }
    
    @Override
    public Object getValue(final TiffField p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   org/apache/commons/imaging/formats/tiff/TiffField.getByteArrayValue:()[B
        //     4: astore          7
        //     6: iconst_0       
        //     7: istore_3       
        //     8: iconst_1       
        //     9: istore          4
        //    11: iload_3        
        //    12: aload           7
        //    14: arraylength    
        //    15: iconst_1       
        //    16: isub           
        //    17: if_icmpge       44
        //    20: iload           4
        //    22: istore_2       
        //    23: aload           7
        //    25: iload_3        
        //    26: baload         
        //    27: ifne            35
        //    30: iload           4
        //    32: iconst_1       
        //    33: iadd           
        //    34: istore_2       
        //    35: iinc            3, 1
        //    38: iload_2        
        //    39: istore          4
        //    41: goto            11
        //    44: iload           4
        //    46: anewarray       Ljava/lang/String;
        //    49: astore_1       
        //    50: aload_1        
        //    51: iconst_0       
        //    52: ldc             ""
        //    54: aastore        
        //    55: iconst_0       
        //    56: istore_3       
        //    57: iconst_0       
        //    58: istore          6
        //    60: iload           6
        //    62: istore_2       
        //    63: iload_3        
        //    64: aload           7
        //    66: arraylength    
        //    67: if_icmpge       143
        //    70: iload           6
        //    72: istore          5
        //    74: iload_2        
        //    75: istore          4
        //    77: aload           7
        //    79: iload_3        
        //    80: baload         
        //    81: ifne            130
        //    84: new             Ljava/lang/String;
        //    87: astore          8
        //    89: aload           8
        //    91: aload           7
        //    93: iload           6
        //    95: iload_3        
        //    96: iload           6
        //    98: isub           
        //    99: ldc             "UTF-8"
        //   101: invokespecial   java/lang/String.<init>:([BIILjava/lang/String;)V
        //   104: iload_2        
        //   105: iconst_1       
        //   106: iadd           
        //   107: istore          4
        //   109: aload_1        
        //   110: iload_2        
        //   111: aload           8
        //   113: aastore        
        //   114: iload           4
        //   116: istore_2       
        //   117: goto            122
        //   120: astore          8
        //   122: iload_3        
        //   123: iconst_1       
        //   124: iadd           
        //   125: istore          5
        //   127: iload_2        
        //   128: istore          4
        //   130: iinc            3, 1
        //   133: iload           5
        //   135: istore          6
        //   137: iload           4
        //   139: istore_2       
        //   140: goto            63
        //   143: iload           6
        //   145: aload           7
        //   147: arraylength    
        //   148: if_icmpge       178
        //   151: new             Ljava/lang/String;
        //   154: astore          8
        //   156: aload           8
        //   158: aload           7
        //   160: iload           6
        //   162: aload           7
        //   164: arraylength    
        //   165: iload           6
        //   167: isub           
        //   168: ldc             "UTF-8"
        //   170: invokespecial   java/lang/String.<init>:([BIILjava/lang/String;)V
        //   173: aload_1        
        //   174: iload_2        
        //   175: aload           8
        //   177: aastore        
        //   178: aload_1        
        //   179: arraylength    
        //   180: iconst_1       
        //   181: if_icmpne       188
        //   184: aload_1        
        //   185: iconst_0       
        //   186: aaload         
        //   187: areturn        
        //   188: aload_1        
        //   189: areturn        
        //   190: astore          7
        //   192: goto            178
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                  
        //  -----  -----  -----  -----  --------------------------------------
        //  84     104    120    122    Ljava/io/UnsupportedEncodingException;
        //  151    173    190    195    Ljava/io/UnsupportedEncodingException;
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
    
    @Override
    public byte[] writeData(final Object obj, final ByteOrder byteOrder) throws ImageWriteException {
        if (obj instanceof byte[]) {
            final byte[] array = (byte[])obj;
            final byte[] array2 = new byte[array.length + 1];
            System.arraycopy(array, 0, array2, 0, array.length);
            array2[array2.length - 1] = 0;
            return array2;
        }
        if (obj instanceof String) {
            try {
                final byte[] bytes = ((String)obj).getBytes("UTF-8");
                final byte[] array3 = new byte[bytes.length + 1];
                System.arraycopy(bytes, 0, array3, 0, bytes.length);
                array3[array3.length - 1] = 0;
                return array3;
            }
            catch (final UnsupportedEncodingException cause) {
                throw new IllegalArgumentException("Your Java doesn't support UTF-8", cause);
            }
        }
        if (obj instanceof String[]) {
            final String[] array4 = (String[])obj;
            final int length = array4.length;
            int i = 0;
            int n = 0;
            while (i < length) {
                final String s = array4[i];
                try {
                    n += s.getBytes("UTF-8").length + 1;
                    ++i;
                    continue;
                }
                catch (final UnsupportedEncodingException cause2) {
                    throw new IllegalArgumentException("Your Java doesn't support UTF-8", cause2);
                }
                break;
            }
            final byte[] array5 = new byte[n];
            final int length2 = array4.length;
            int j = 0;
            int n2 = 0;
            while (j < length2) {
                final String s2 = array4[j];
                try {
                    final byte[] bytes2 = s2.getBytes("UTF-8");
                    System.arraycopy(bytes2, 0, array5, n2, bytes2.length);
                    n2 += bytes2.length + 1;
                    ++j;
                    continue;
                }
                catch (final UnsupportedEncodingException cause3) {
                    throw new IllegalArgumentException("Your Java doesn't support UTF-8", cause3);
                }
                break;
            }
            return array5;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Unknown data type: ");
        sb.append(obj);
        throw new ImageWriteException(sb.toString());
    }
}
