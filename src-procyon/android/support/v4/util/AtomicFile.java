// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.util;

import java.io.FileNotFoundException;
import java.io.FileInputStream;
import android.util.Log;
import android.support.annotation.Nullable;
import java.io.IOException;
import java.io.FileOutputStream;
import android.support.annotation.NonNull;
import java.io.File;

public class AtomicFile
{
    private final File mBackupName;
    private final File mBaseName;
    
    public AtomicFile(@NonNull final File mBaseName) {
        this.mBaseName = mBaseName;
        final StringBuilder sb = new StringBuilder();
        sb.append(mBaseName.getPath());
        sb.append(".bak");
        this.mBackupName = new File(sb.toString());
    }
    
    private static boolean sync(@NonNull final FileOutputStream fileOutputStream) {
        try {
            fileOutputStream.getFD().sync();
            return true;
        }
        catch (final IOException ex) {
            return false;
        }
    }
    
    public void delete() {
        this.mBaseName.delete();
        this.mBackupName.delete();
    }
    
    public void failWrite(@Nullable final FileOutputStream fileOutputStream) {
        if (fileOutputStream != null) {
            sync(fileOutputStream);
            try {
                fileOutputStream.close();
                this.mBaseName.delete();
                this.mBackupName.renameTo(this.mBaseName);
            }
            catch (final IOException ex) {
                Log.w("AtomicFile", "failWrite: Got exception:", (Throwable)ex);
            }
        }
    }
    
    public void finishWrite(@Nullable final FileOutputStream fileOutputStream) {
        if (fileOutputStream != null) {
            sync(fileOutputStream);
            try {
                fileOutputStream.close();
                this.mBackupName.delete();
            }
            catch (final IOException ex) {
                Log.w("AtomicFile", "finishWrite: Got exception:", (Throwable)ex);
            }
        }
    }
    
    @NonNull
    public File getBaseFile() {
        return this.mBaseName;
    }
    
    @NonNull
    public FileInputStream openRead() throws FileNotFoundException {
        if (this.mBackupName.exists()) {
            this.mBaseName.delete();
            this.mBackupName.renameTo(this.mBaseName);
        }
        return new FileInputStream(this.mBaseName);
    }
    
    @NonNull
    public byte[] readFully() throws IOException {
        final FileInputStream openRead = this.openRead();
        try {
            byte[] b = new byte[openRead.available()];
            int off = 0;
            while (true) {
                final int read = openRead.read(b, off, b.length - off);
                if (read <= 0) {
                    break;
                }
                final int n = off + read;
                final int available = openRead.available();
                off = n;
                if (available <= b.length - n) {
                    continue;
                }
                final byte[] array = new byte[available + n];
                System.arraycopy(b, 0, array, 0, n);
                b = array;
                off = n;
            }
            return b;
        }
        finally {
            openRead.close();
        }
    }
    
    @NonNull
    public FileOutputStream startWrite() throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        android/support/v4/util/AtomicFile.mBaseName:Ljava/io/File;
        //     4: invokevirtual   java/io/File.exists:()Z
        //     7: ifeq            95
        //    10: aload_0        
        //    11: getfield        android/support/v4/util/AtomicFile.mBackupName:Ljava/io/File;
        //    14: invokevirtual   java/io/File.exists:()Z
        //    17: ifne            87
        //    20: aload_0        
        //    21: getfield        android/support/v4/util/AtomicFile.mBaseName:Ljava/io/File;
        //    24: aload_0        
        //    25: getfield        android/support/v4/util/AtomicFile.mBackupName:Ljava/io/File;
        //    28: invokevirtual   java/io/File.renameTo:(Ljava/io/File;)Z
        //    31: ifne            95
        //    34: new             Ljava/lang/StringBuilder;
        //    37: dup            
        //    38: invokespecial   java/lang/StringBuilder.<init>:()V
        //    41: astore_1       
        //    42: aload_1        
        //    43: ldc             "Couldn't rename file "
        //    45: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    48: pop            
        //    49: aload_1        
        //    50: aload_0        
        //    51: getfield        android/support/v4/util/AtomicFile.mBaseName:Ljava/io/File;
        //    54: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //    57: pop            
        //    58: aload_1        
        //    59: ldc             " to backup file "
        //    61: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    64: pop            
        //    65: aload_1        
        //    66: aload_0        
        //    67: getfield        android/support/v4/util/AtomicFile.mBackupName:Ljava/io/File;
        //    70: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //    73: pop            
        //    74: ldc             "AtomicFile"
        //    76: aload_1        
        //    77: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    80: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;)I
        //    83: pop            
        //    84: goto            95
        //    87: aload_0        
        //    88: getfield        android/support/v4/util/AtomicFile.mBaseName:Ljava/io/File;
        //    91: invokevirtual   java/io/File.delete:()Z
        //    94: pop            
        //    95: new             Ljava/io/FileOutputStream;
        //    98: astore_1       
        //    99: aload_1        
        //   100: aload_0        
        //   101: getfield        android/support/v4/util/AtomicFile.mBaseName:Ljava/io/File;
        //   104: invokespecial   java/io/FileOutputStream.<init>:(Ljava/io/File;)V
        //   107: goto            172
        //   110: astore_1       
        //   111: aload_0        
        //   112: getfield        android/support/v4/util/AtomicFile.mBaseName:Ljava/io/File;
        //   115: invokevirtual   java/io/File.getParentFile:()Ljava/io/File;
        //   118: invokevirtual   java/io/File.mkdirs:()Z
        //   121: ifne            160
        //   124: new             Ljava/lang/StringBuilder;
        //   127: dup            
        //   128: invokespecial   java/lang/StringBuilder.<init>:()V
        //   131: astore_1       
        //   132: aload_1        
        //   133: ldc             "Couldn't create directory "
        //   135: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   138: pop            
        //   139: aload_1        
        //   140: aload_0        
        //   141: getfield        android/support/v4/util/AtomicFile.mBaseName:Ljava/io/File;
        //   144: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   147: pop            
        //   148: new             Ljava/io/IOException;
        //   151: dup            
        //   152: aload_1        
        //   153: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   156: invokespecial   java/io/IOException.<init>:(Ljava/lang/String;)V
        //   159: athrow         
        //   160: new             Ljava/io/FileOutputStream;
        //   163: dup            
        //   164: aload_0        
        //   165: getfield        android/support/v4/util/AtomicFile.mBaseName:Ljava/io/File;
        //   168: invokespecial   java/io/FileOutputStream.<init>:(Ljava/io/File;)V
        //   171: astore_1       
        //   172: aload_1        
        //   173: areturn        
        //   174: astore_1       
        //   175: new             Ljava/lang/StringBuilder;
        //   178: dup            
        //   179: invokespecial   java/lang/StringBuilder.<init>:()V
        //   182: astore_1       
        //   183: aload_1        
        //   184: ldc             "Couldn't create "
        //   186: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   189: pop            
        //   190: aload_1        
        //   191: aload_0        
        //   192: getfield        android/support/v4/util/AtomicFile.mBaseName:Ljava/io/File;
        //   195: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   198: pop            
        //   199: new             Ljava/io/IOException;
        //   202: dup            
        //   203: aload_1        
        //   204: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   207: invokespecial   java/io/IOException.<init>:(Ljava/lang/String;)V
        //   210: athrow         
        //    Exceptions:
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                           
        //  -----  -----  -----  -----  -------------------------------
        //  95     107    110    172    Ljava/io/FileNotFoundException;
        //  160    172    174    211    Ljava/io/FileNotFoundException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0160:
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
}
