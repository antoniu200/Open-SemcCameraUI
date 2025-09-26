// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.android.media.internal;

import android.os.Looper;
import android.os.Handler;
import android.os.Message;
import java.util.Iterator;
import java.util.ArrayList;

public class HandlerHelper
{
    private final Object mListLock;
    private final ArrayList<WaitHandler> mMessageList;
    
    public HandlerHelper() {
        this.mMessageList = new ArrayList<WaitHandler>();
        this.mListLock = new Object();
    }
    
    public void releaseAllLocks() {
        synchronized (this.mListLock) {
            for (final WaitHandler waitHandler : this.mMessageList) {
                synchronized (waitHandler.lock) {
                    waitHandler.releaseLock = true;
                    waitHandler.lock.notifyAll();
                    continue;
                }
                break;
            }
        }
    }
    
    public Object sendMessageAndAwaitResponse(final Message p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: ldc             "sendMessageAndAwaitResponse"
        //     6: invokespecial   android/os/HandlerThread.<init>:(Ljava/lang/String;)V
        //     9: astore          6
        //    11: new             Ljava/lang/Object;
        //    14: dup            
        //    15: invokespecial   java/lang/Object.<init>:()V
        //    18: astore          4
        //    20: aload           6
        //    22: invokevirtual   android/os/HandlerThread.start:()V
        //    25: aload           6
        //    27: invokevirtual   android/os/HandlerThread.getLooper:()Landroid/os/Looper;
        //    30: astore          5
        //    32: aconst_null    
        //    33: astore_3       
        //    34: new             Lcom/sonymobile/android/media/internal/HandlerHelper$WaitHandler;
        //    37: dup            
        //    38: aload           5
        //    40: aload           4
        //    42: aconst_null    
        //    43: invokespecial   com/sonymobile/android/media/internal/HandlerHelper$WaitHandler.<init>:(Landroid/os/Looper;Ljava/lang/Object;Lcom/sonymobile/android/media/internal/HandlerHelper$1;)V
        //    46: astore          5
        //    48: aload_0        
        //    49: getfield        com/sonymobile/android/media/internal/HandlerHelper.mListLock:Ljava/lang/Object;
        //    52: astore          7
        //    54: aload           7
        //    56: monitorenter   
        //    57: aload_0        
        //    58: getfield        com/sonymobile/android/media/internal/HandlerHelper.mMessageList:Ljava/util/ArrayList;
        //    61: aload           5
        //    63: invokevirtual   java/util/ArrayList.add:(Ljava/lang/Object;)Z
        //    66: pop            
        //    67: aload           7
        //    69: monitorexit    
        //    70: aload_1        
        //    71: getfield        android/os/Message.obj:Ljava/lang/Object;
        //    74: ifnull          97
        //    77: aload_1        
        //    78: new             Lcom/sonymobile/android/media/internal/HandlerHelper$ExtendedObject;
        //    81: dup            
        //    82: aload_1        
        //    83: getfield        android/os/Message.obj:Ljava/lang/Object;
        //    86: aload           5
        //    88: invokespecial   com/sonymobile/android/media/internal/HandlerHelper$ExtendedObject.<init>:(Ljava/lang/Object;Landroid/os/Handler;)V
        //    91: putfield        android/os/Message.obj:Ljava/lang/Object;
        //    94: goto            103
        //    97: aload_1        
        //    98: aload           5
        //   100: putfield        android/os/Message.obj:Ljava/lang/Object;
        //   103: aload_1        
        //   104: invokevirtual   android/os/Message.sendToTarget:()V
        //   107: aload           4
        //   109: monitorenter   
        //   110: aload_3        
        //   111: astore_1       
        //   112: aload_1        
        //   113: ifnonnull       149
        //   116: aload           5
        //   118: getfield        com/sonymobile/android/media/internal/HandlerHelper$WaitHandler.releaseLock:Z
        //   121: istore_2       
        //   122: iload_2        
        //   123: ifne            149
        //   126: aload           4
        //   128: ldc2_w          10
        //   131: invokevirtual   java/lang/Object.wait:(J)V
        //   134: aload           5
        //   136: getfield        com/sonymobile/android/media/internal/HandlerHelper$WaitHandler.reply:Ljava/lang/Object;
        //   139: astore_3       
        //   140: aload_3        
        //   141: astore_1       
        //   142: goto            112
        //   145: astore_1       
        //   146: goto            184
        //   149: aload           4
        //   151: monitorexit    
        //   152: aload           6
        //   154: invokevirtual   android/os/HandlerThread.quit:()Z
        //   157: pop            
        //   158: aload_0        
        //   159: getfield        com/sonymobile/android/media/internal/HandlerHelper.mListLock:Ljava/lang/Object;
        //   162: astore_3       
        //   163: aload_3        
        //   164: monitorenter   
        //   165: aload_0        
        //   166: getfield        com/sonymobile/android/media/internal/HandlerHelper.mMessageList:Ljava/util/ArrayList;
        //   169: aload           5
        //   171: invokevirtual   java/util/ArrayList.remove:(Ljava/lang/Object;)Z
        //   174: pop            
        //   175: aload_3        
        //   176: monitorexit    
        //   177: aload_1        
        //   178: areturn        
        //   179: astore_1       
        //   180: aload_3        
        //   181: monitorexit    
        //   182: aload_1        
        //   183: athrow         
        //   184: aload           4
        //   186: monitorexit    
        //   187: aload_1        
        //   188: athrow         
        //   189: astore_1       
        //   190: aload           7
        //   192: monitorexit    
        //   193: aload_1        
        //   194: athrow         
        //   195: astore_3       
        //   196: goto            112
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                            
        //  -----  -----  -----  -----  --------------------------------
        //  57     70     189    195    Any
        //  116    122    145    189    Any
        //  126    140    195    199    Ljava/lang/InterruptedException;
        //  126    140    145    189    Any
        //  149    152    145    189    Any
        //  165    177    179    184    Any
        //  180    182    179    184    Any
        //  184    187    145    189    Any
        //  190    193    189    195    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0149:
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
    
    public static class ExtendedObject
    {
        public final Handler handler;
        public final Object obj;
        
        public ExtendedObject(final Object obj, final Handler handler) {
            this.obj = obj;
            this.handler = handler;
        }
    }
    
    private static class WaitHandler extends Handler
    {
        private final Object lock;
        public boolean releaseLock;
        public Object reply;
        
        private WaitHandler(final Looper looper, final Object lock) {
            super(looper);
            this.lock = lock;
        }
        
        public void handleMessage(final Message message) {
            this.reply = message.obj;
            synchronized (this.lock) {
                this.lock.notifyAll();
            }
        }
    }
}
