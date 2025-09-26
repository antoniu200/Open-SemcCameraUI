// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.mediasaving;

import android.support.annotation.NonNull;
import java.lang.reflect.InvocationTargetException;
import android.os.storage.VolumeInfo;
import java.util.List;
import android.os.storage.StorageManager;
import java.util.HashMap;
import java.lang.reflect.Method;

public class StorageManagerProxy
{
    private static Method mMethodGetVolumes;
    private static Class<?> mStorageTypeClass;
    private static HashMap<Object, StorageType> mStorageTypeInverseMap;
    private static HashMap<StorageType, Object> mStorageTypeMap;
    private StorageManager mStorageManager;
    
    static {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     2: ldc             "getVolumes"
        //     4: iconst_0       
        //     5: anewarray       Ljava/lang/Class;
        //     8: invokevirtual   java/lang/Class.getMethod:(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
        //    11: putstatic       com/sonyericsson/cameracommon/mediasaving/StorageManagerProxy.mMethodGetVolumes:Ljava/lang/reflect/Method;
        //    14: ldc             "android.os.storage.StorageManager$StorageType"
        //    16: invokestatic    java/lang/Class.forName:(Ljava/lang/String;)Ljava/lang/Class;
        //    19: putstatic       com/sonyericsson/cameracommon/mediasaving/StorageManagerProxy.mStorageTypeClass:Ljava/lang/Class;
        //    22: getstatic       com/sonyericsson/cameracommon/mediasaving/StorageManagerProxy.mStorageTypeClass:Ljava/lang/Class;
        //    25: invokevirtual   java/lang/Class.getEnumConstants:()[Ljava/lang/Object;
        //    28: ifnull          167
        //    31: getstatic       com/sonyericsson/cameracommon/mediasaving/StorageManagerProxy.mStorageTypeClass:Ljava/lang/Class;
        //    34: invokevirtual   java/lang/Class.getEnumConstants:()[Ljava/lang/Object;
        //    37: astore          5
        //    39: aload           5
        //    41: arraylength    
        //    42: istore_2       
        //    43: iconst_0       
        //    44: istore_0       
        //    45: iload_0        
        //    46: iload_2        
        //    47: if_icmpge       167
        //    50: aload           5
        //    52: iload_0        
        //    53: aaload         
        //    54: astore          6
        //    56: invokestatic    com/sonyericsson/cameracommon/mediasaving/StorageManagerProxy$StorageType.values:()[Lcom/sonyericsson/cameracommon/mediasaving/StorageManagerProxy$StorageType;
        //    59: astore          7
        //    61: aload           7
        //    63: arraylength    
        //    64: istore_3       
        //    65: iconst_0       
        //    66: istore_1       
        //    67: iload_1        
        //    68: iload_3        
        //    69: if_icmpge       161
        //    72: aload           7
        //    74: iload_1        
        //    75: aaload         
        //    76: astore          4
        //    78: aload           4
        //    80: invokevirtual   com/sonyericsson/cameracommon/mediasaving/StorageManagerProxy$StorageType.name:()Ljava/lang/String;
        //    83: aload           6
        //    85: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //    88: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    91: ifeq            155
        //    94: getstatic       com/sonyericsson/cameracommon/mediasaving/StorageManagerProxy.mStorageTypeMap:Ljava/util/HashMap;
        //    97: ifnonnull       130
        //   100: new             Ljava/util/HashMap;
        //   103: astore          7
        //   105: aload           7
        //   107: invokespecial   java/util/HashMap.<init>:()V
        //   110: aload           7
        //   112: putstatic       com/sonyericsson/cameracommon/mediasaving/StorageManagerProxy.mStorageTypeMap:Ljava/util/HashMap;
        //   115: new             Ljava/util/HashMap;
        //   118: astore          7
        //   120: aload           7
        //   122: invokespecial   java/util/HashMap.<init>:()V
        //   125: aload           7
        //   127: putstatic       com/sonyericsson/cameracommon/mediasaving/StorageManagerProxy.mStorageTypeInverseMap:Ljava/util/HashMap;
        //   130: getstatic       com/sonyericsson/cameracommon/mediasaving/StorageManagerProxy.mStorageTypeMap:Ljava/util/HashMap;
        //   133: aload           4
        //   135: aload           6
        //   137: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   140: pop            
        //   141: getstatic       com/sonyericsson/cameracommon/mediasaving/StorageManagerProxy.mStorageTypeInverseMap:Ljava/util/HashMap;
        //   144: aload           6
        //   146: aload           4
        //   148: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   151: pop            
        //   152: goto            161
        //   155: iinc            1, 1
        //   158: goto            67
        //   161: iinc            0, 1
        //   164: goto            45
        //   167: getstatic       com/sonyericsson/cameracommon/mediasaving/StorageManagerProxy.mStorageTypeMap:Ljava/util/HashMap;
        //   170: ifnull          195
        //   173: getstatic       com/sonyericsson/cameracommon/mediasaving/StorageManagerProxy.mStorageTypeMap:Ljava/util/HashMap;
        //   176: invokevirtual   java/util/HashMap.isEmpty:()Z
        //   179: ifne            210
        //   182: getstatic       com/sonyericsson/cameracommon/mediasaving/StorageManagerProxy.mStorageTypeMap:Ljava/util/HashMap;
        //   185: invokevirtual   java/util/HashMap.size:()I
        //   188: invokestatic    com/sonyericsson/cameracommon/mediasaving/StorageManagerProxy$StorageType.values:()[Lcom/sonyericsson/cameracommon/mediasaving/StorageManagerProxy$StorageType;
        //   191: arraylength    
        //   192: if_icmpeq       210
        //   195: new             Ljava/lang/RuntimeException;
        //   198: astore          4
        //   200: aload           4
        //   202: ldc             "Support StorageType is not expected"
        //   204: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/String;)V
        //   207: aload           4
        //   209: athrow         
        //   210: return         
        //   211: astore          4
        //   213: new             Ljava/lang/RuntimeException;
        //   216: dup            
        //   217: aload           4
        //   219: invokespecial   java/lang/RuntimeException.<init>:(Ljava/lang/Throwable;)V
        //   222: athrow         
        //   223: astore          4
        //   225: goto            210
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                              
        //  -----  -----  -----  -----  ----------------------------------
        //  0      14     211    223    Ljava/lang/NoSuchMethodException;
        //  14     43     223    228    Ljava/lang/ClassNotFoundException;
        //  56     65     223    228    Ljava/lang/ClassNotFoundException;
        //  78     130    223    228    Ljava/lang/ClassNotFoundException;
        //  130    152    223    228    Ljava/lang/ClassNotFoundException;
        //  167    195    223    228    Ljava/lang/ClassNotFoundException;
        //  195    210    223    228    Ljava/lang/ClassNotFoundException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0045:
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
    
    private StorageManagerProxy(final StorageManager mStorageManager) {
        this.mStorageManager = mStorageManager;
    }
    
    public static StorageManagerProxy createProxy(final StorageManager storageManager) {
        return new StorageManagerProxy(storageManager);
    }
    
    @NonNull
    public List<VolumeInfo> getVolumes() {
        try {
            return (List)StorageManagerProxy.mMethodGetVolumes.invoke(this.mStorageManager, new Object[0]);
        }
        catch (final IllegalAccessException | InvocationTargetException cause) {
            throw new RuntimeException((Throwable)cause);
        }
    }
    
    public enum StorageType
    {
        private static final StorageType[] $VALUES;
        
        EXTERNAL_CARD, 
        EXTERNAL_USB, 
        INTERNAL, 
        UNKNOWN;
        
        static {
            $VALUES = new StorageType[] { StorageType.INTERNAL, StorageType.EXTERNAL_CARD, StorageType.EXTERNAL_USB, StorageType.UNKNOWN };
        }
    }
}
