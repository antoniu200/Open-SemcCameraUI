// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.recorder.utility.encoder;

import android.media.MediaFormat;
import android.media.MediaCodec$BufferInfo;
import com.sonyericsson.android.camera.util.CamLog;
import android.media.MediaCodec;

class EncodedDataWriteTask implements Runnable
{
    private static final long OUTPUTBUFFER_TIMEOUT_NANOSECONDS = 100000000L;
    public static final String TAG = "EncodedDataWriteTask";
    private static boolean TRACE = false;
    private final MediaCodec mCodec;
    private final EncoderStateListener mListener;
    private final MediaMuxerWrapper mMuxer;
    private int mMuxerIndex;
    private final String mName;
    
    public EncodedDataWriteTask(final MediaMuxerWrapper mMuxer, final MediaCodec mCodec, final EncoderStateListener mListener, final String mName) {
        this.mName = mName;
        this.mMuxer = mMuxer;
        this.mCodec = mCodec;
        this.mListener = mListener;
    }
    
    private boolean awaitEncoderFormat() {
        if (EncodedDataWriteTask.TRACE) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.mName);
            sb.append(" awaitEncoderFormat E");
            CamLog.d(sb.toString());
        }
        final MediaCodec$BufferInfo mediaCodec$BufferInfo = new MediaCodec$BufferInfo();
        while (!canceled()) {
            final int dequeueOutputBuffer = this.mCodec.dequeueOutputBuffer(mediaCodec$BufferInfo, 100000000L);
            if (dequeueOutputBuffer == -2) {
                if (EncodedDataWriteTask.TRACE) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(this.mName);
                    sb2.append(" INFO_OUTPUT_FORMAT_CHANGED");
                    CamLog.d(sb2.toString());
                }
                return true;
            }
            if (!EncodedDataWriteTask.TRACE) {
                continue;
            }
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(this.mName);
            sb3.append(" INFO_OUTPUT:");
            sb3.append(dequeueOutputBuffer);
            CamLog.d(sb3.toString());
        }
        if (EncodedDataWriteTask.TRACE) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(this.mName);
            sb4.append(" awaitEncoderFormat X");
            CamLog.d(sb4.toString());
        }
        return false;
    }
    
    private static boolean canceled() {
        return Thread.currentThread().isInterrupted();
    }
    
    @Override
    public void run() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokespecial   com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask.awaitEncoderFormat:()Z
        //     4: ifne            8
        //     7: return         
        //     8: aload_0        
        //     9: getfield        com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask.mCodec:Landroid/media/MediaCodec;
        //    12: invokevirtual   android/media/MediaCodec.getOutputBuffers:()[Ljava/nio/ByteBuffer;
        //    15: astore_3       
        //    16: new             Landroid/media/MediaCodec$BufferInfo;
        //    19: dup            
        //    20: invokespecial   android/media/MediaCodec$BufferInfo.<init>:()V
        //    23: astore          4
        //    25: aload_0        
        //    26: getfield        com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask.mCodec:Landroid/media/MediaCodec;
        //    29: invokevirtual   android/media/MediaCodec.getOutputFormat:()Landroid/media/MediaFormat;
        //    32: astore          5
        //    34: aload_0        
        //    35: getfield        com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask.mMuxer:Lcom/sonyericsson/android/camera/recorder/utility/encoder/MediaMuxerWrapper;
        //    38: astore_2       
        //    39: aload_2        
        //    40: monitorenter   
        //    41: getstatic       com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask.TRACE:Z
        //    44: ifeq            106
        //    47: new             Ljava/lang/StringBuilder;
        //    50: astore          6
        //    52: aload           6
        //    54: invokespecial   java/lang/StringBuilder.<init>:()V
        //    57: aload           6
        //    59: aload_0        
        //    60: getfield        com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask.mName:Ljava/lang/String;
        //    63: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    66: pop            
        //    67: aload           6
        //    69: ldc             " ADD TRACK ("
        //    71: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    74: pop            
        //    75: aload           6
        //    77: aload           5
        //    79: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //    82: pop            
        //    83: aload           6
        //    85: ldc             "): E"
        //    87: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    90: pop            
        //    91: iconst_1       
        //    92: anewarray       Ljava/lang/String;
        //    95: dup            
        //    96: iconst_0       
        //    97: aload           6
        //    99: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   102: aastore        
        //   103: invokestatic    com/sonyericsson/android/camera/util/CamLog.d:([Ljava/lang/String;)V
        //   106: aload_0        
        //   107: aload_0        
        //   108: getfield        com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask.mMuxer:Lcom/sonyericsson/android/camera/recorder/utility/encoder/MediaMuxerWrapper;
        //   111: aload           5
        //   113: invokevirtual   com/sonyericsson/android/camera/recorder/utility/encoder/MediaMuxerWrapper.addTrack:(Landroid/media/MediaFormat;)I
        //   116: putfield        com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask.mMuxerIndex:I
        //   119: getstatic       com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask.TRACE:Z
        //   122: ifeq            184
        //   125: new             Ljava/lang/StringBuilder;
        //   128: astore          6
        //   130: aload           6
        //   132: invokespecial   java/lang/StringBuilder.<init>:()V
        //   135: aload           6
        //   137: aload_0        
        //   138: getfield        com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask.mName:Ljava/lang/String;
        //   141: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   144: pop            
        //   145: aload           6
        //   147: ldc             " ADD TRACK ("
        //   149: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   152: pop            
        //   153: aload           6
        //   155: aload           5
        //   157: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   160: pop            
        //   161: aload           6
        //   163: ldc             "): X"
        //   165: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   168: pop            
        //   169: iconst_1       
        //   170: anewarray       Ljava/lang/String;
        //   173: dup            
        //   174: iconst_0       
        //   175: aload           6
        //   177: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   180: aastore        
        //   181: invokestatic    com/sonyericsson/android/camera/util/CamLog.d:([Ljava/lang/String;)V
        //   184: aload_2        
        //   185: monitorexit    
        //   186: aload_3        
        //   187: astore_2       
        //   188: aload_0        
        //   189: getfield        com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask.mListener:Lcom/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask$EncoderStateListener;
        //   192: ifnull          208
        //   195: aload_0        
        //   196: getfield        com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask.mListener:Lcom/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask$EncoderStateListener;
        //   199: aload           5
        //   201: invokeinterface com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask$EncoderStateListener.onEncoderFormatChanged:(Landroid/media/MediaFormat;)V
        //   206: aload_3        
        //   207: astore_2       
        //   208: invokestatic    com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask.canceled:()Z
        //   211: ifne            733
        //   214: aload_0        
        //   215: getfield        com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask.mCodec:Landroid/media/MediaCodec;
        //   218: aload           4
        //   220: ldc2_w          100000000
        //   223: invokevirtual   android/media/MediaCodec.dequeueOutputBuffer:(Landroid/media/MediaCodec$BufferInfo;J)I
        //   226: istore_1       
        //   227: iload_1        
        //   228: iconst_m1      
        //   229: if_icmpne       329
        //   232: getstatic       com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask.TRACE:Z
        //   235: ifeq            276
        //   238: new             Ljava/lang/StringBuilder;
        //   241: dup            
        //   242: invokespecial   java/lang/StringBuilder.<init>:()V
        //   245: astore_3       
        //   246: aload_3        
        //   247: aload_0        
        //   248: getfield        com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask.mName:Ljava/lang/String;
        //   251: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   254: pop            
        //   255: aload_3        
        //   256: ldc             " INFO_TRY_AGAIN_LATER"
        //   258: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   261: pop            
        //   262: iconst_1       
        //   263: anewarray       Ljava/lang/String;
        //   266: dup            
        //   267: iconst_0       
        //   268: aload_3        
        //   269: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   272: aastore        
        //   273: invokestatic    com/sonyericsson/android/camera/util/CamLog.d:([Ljava/lang/String;)V
        //   276: invokestatic    com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask.canceled:()Z
        //   279: ifeq            208
        //   282: getstatic       com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask.TRACE:Z
        //   285: ifeq            733
        //   288: new             Ljava/lang/StringBuilder;
        //   291: dup            
        //   292: invokespecial   java/lang/StringBuilder.<init>:()V
        //   295: astore_2       
        //   296: aload_2        
        //   297: aload_0        
        //   298: getfield        com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask.mName:Ljava/lang/String;
        //   301: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   304: pop            
        //   305: aload_2        
        //   306: ldc             " CANCELED"
        //   308: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   311: pop            
        //   312: iconst_1       
        //   313: anewarray       Ljava/lang/String;
        //   316: dup            
        //   317: iconst_0       
        //   318: aload_2        
        //   319: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   322: aastore        
        //   323: invokestatic    com/sonyericsson/android/camera/util/CamLog.d:([Ljava/lang/String;)V
        //   326: goto            733
        //   329: iload_1        
        //   330: iflt            589
        //   333: invokestatic    com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask.canceled:()Z
        //   336: ifeq            351
        //   339: aload           4
        //   341: aload           4
        //   343: getfield        android/media/MediaCodec$BufferInfo.flags:I
        //   346: iconst_4       
        //   347: ior            
        //   348: putfield        android/media/MediaCodec$BufferInfo.flags:I
        //   351: getstatic       com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask.TRACE:Z
        //   354: ifeq            422
        //   357: new             Ljava/lang/StringBuilder;
        //   360: dup            
        //   361: invokespecial   java/lang/StringBuilder.<init>:()V
        //   364: astore_3       
        //   365: aload_3        
        //   366: aload_0        
        //   367: getfield        com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask.mName:Ljava/lang/String;
        //   370: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   373: pop            
        //   374: aload_3        
        //   375: ldc             " PULL SAMPLE DATA presentationTime:"
        //   377: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   380: pop            
        //   381: aload_3        
        //   382: aload           4
        //   384: getfield        android/media/MediaCodec$BufferInfo.presentationTimeUs:J
        //   387: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //   390: pop            
        //   391: aload_3        
        //   392: ldc             " flag:"
        //   394: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   397: pop            
        //   398: aload_3        
        //   399: aload           4
        //   401: getfield        android/media/MediaCodec$BufferInfo.flags:I
        //   404: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   407: pop            
        //   408: iconst_1       
        //   409: anewarray       Ljava/lang/String;
        //   412: dup            
        //   413: iconst_0       
        //   414: aload_3        
        //   415: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   418: aastore        
        //   419: invokestatic    com/sonyericsson/android/camera/util/CamLog.d:([Ljava/lang/String;)V
        //   422: aload_2        
        //   423: iload_1        
        //   424: aaload         
        //   425: astore          5
        //   427: aload           4
        //   429: getfield        android/media/MediaCodec$BufferInfo.flags:I
        //   432: iconst_2       
        //   433: iand           
        //   434: ifeq            440
        //   437: goto            464
        //   440: aload_0        
        //   441: getfield        com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask.mMuxer:Lcom/sonyericsson/android/camera/recorder/utility/encoder/MediaMuxerWrapper;
        //   444: astore_3       
        //   445: aload_3        
        //   446: monitorenter   
        //   447: aload_0        
        //   448: getfield        com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask.mMuxer:Lcom/sonyericsson/android/camera/recorder/utility/encoder/MediaMuxerWrapper;
        //   451: aload_0        
        //   452: getfield        com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask.mMuxerIndex:I
        //   455: aload           5
        //   457: aload           4
        //   459: invokevirtual   com/sonyericsson/android/camera/recorder/utility/encoder/MediaMuxerWrapper.writeSampleData:(ILjava/nio/ByteBuffer;Landroid/media/MediaCodec$BufferInfo;)V
        //   462: aload_3        
        //   463: monitorexit    
        //   464: aload_0        
        //   465: getfield        com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask.mCodec:Landroid/media/MediaCodec;
        //   468: iload_1        
        //   469: iconst_0       
        //   470: invokevirtual   android/media/MediaCodec.releaseOutputBuffer:(IZ)V
        //   473: invokestatic    com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask.canceled:()Z
        //   476: ifeq            526
        //   479: getstatic       com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask.TRACE:Z
        //   482: ifeq            733
        //   485: new             Ljava/lang/StringBuilder;
        //   488: dup            
        //   489: invokespecial   java/lang/StringBuilder.<init>:()V
        //   492: astore_2       
        //   493: aload_2        
        //   494: aload_0        
        //   495: getfield        com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask.mName:Ljava/lang/String;
        //   498: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   501: pop            
        //   502: aload_2        
        //   503: ldc             " CANCELED"
        //   505: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   508: pop            
        //   509: iconst_1       
        //   510: anewarray       Ljava/lang/String;
        //   513: dup            
        //   514: iconst_0       
        //   515: aload_2        
        //   516: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   519: aastore        
        //   520: invokestatic    com/sonyericsson/android/camera/util/CamLog.d:([Ljava/lang/String;)V
        //   523: goto            733
        //   526: aload           4
        //   528: getfield        android/media/MediaCodec$BufferInfo.flags:I
        //   531: iconst_4       
        //   532: iand           
        //   533: iconst_4       
        //   534: if_icmpne       208
        //   537: getstatic       com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask.TRACE:Z
        //   540: ifeq            733
        //   543: new             Ljava/lang/StringBuilder;
        //   546: dup            
        //   547: invokespecial   java/lang/StringBuilder.<init>:()V
        //   550: astore_2       
        //   551: aload_2        
        //   552: aload_0        
        //   553: getfield        com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask.mName:Ljava/lang/String;
        //   556: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   559: pop            
        //   560: aload_2        
        //   561: ldc             " BUFFER_FLAG_END_OF_STREAM"
        //   563: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   566: pop            
        //   567: iconst_1       
        //   568: anewarray       Ljava/lang/String;
        //   571: dup            
        //   572: iconst_0       
        //   573: aload_2        
        //   574: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   577: aastore        
        //   578: invokestatic    com/sonyericsson/android/camera/util/CamLog.d:([Ljava/lang/String;)V
        //   581: goto            733
        //   584: astore_2       
        //   585: aload_3        
        //   586: monitorexit    
        //   587: aload_2        
        //   588: athrow         
        //   589: iload_1        
        //   590: bipush          -3
        //   592: if_icmpne       650
        //   595: getstatic       com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask.TRACE:Z
        //   598: ifeq            639
        //   601: new             Ljava/lang/StringBuilder;
        //   604: dup            
        //   605: invokespecial   java/lang/StringBuilder.<init>:()V
        //   608: astore_2       
        //   609: aload_2        
        //   610: aload_0        
        //   611: getfield        com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask.mName:Ljava/lang/String;
        //   614: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   617: pop            
        //   618: aload_2        
        //   619: ldc             " INFO_OUTPUT_BUFFERS_CHANGED"
        //   621: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   624: pop            
        //   625: iconst_1       
        //   626: anewarray       Ljava/lang/String;
        //   629: dup            
        //   630: iconst_0       
        //   631: aload_2        
        //   632: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   635: aastore        
        //   636: invokestatic    com/sonyericsson/android/camera/util/CamLog.d:([Ljava/lang/String;)V
        //   639: aload_0        
        //   640: getfield        com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask.mCodec:Landroid/media/MediaCodec;
        //   643: invokevirtual   android/media/MediaCodec.getOutputBuffers:()[Ljava/nio/ByteBuffer;
        //   646: astore_2       
        //   647: goto            208
        //   650: iload_1        
        //   651: bipush          -2
        //   653: if_icmpne       208
        //   656: getstatic       com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask.TRACE:Z
        //   659: ifeq            208
        //   662: new             Ljava/lang/StringBuilder;
        //   665: dup            
        //   666: invokespecial   java/lang/StringBuilder.<init>:()V
        //   669: astore_3       
        //   670: aload_3        
        //   671: aload_0        
        //   672: getfield        com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask.mName:Ljava/lang/String;
        //   675: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   678: pop            
        //   679: aload_3        
        //   680: ldc             " INFO_OUTPUT_FORMAT_CHANGED"
        //   682: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   685: pop            
        //   686: iconst_1       
        //   687: anewarray       Ljava/lang/String;
        //   690: dup            
        //   691: iconst_0       
        //   692: aload_3        
        //   693: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   696: aastore        
        //   697: invokestatic    com/sonyericsson/android/camera/util/CamLog.d:([Ljava/lang/String;)V
        //   700: goto            208
        //   703: astore_2       
        //   704: new             Ljava/lang/StringBuilder;
        //   707: dup            
        //   708: invokespecial   java/lang/StringBuilder.<init>:()V
        //   711: astore_3       
        //   712: aload_3        
        //   713: aload_2        
        //   714: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   717: pop            
        //   718: aload_3        
        //   719: ldc             " occurred. Maybe camera server is dead."
        //   721: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   724: pop            
        //   725: aload_3        
        //   726: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   729: aload_2        
        //   730: invokestatic    com/sonyericsson/android/camera/util/CamLog.e:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   733: aload_0        
        //   734: getfield        com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask.mListener:Lcom/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask$EncoderStateListener;
        //   737: ifnull          749
        //   740: aload_0        
        //   741: getfield        com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask.mListener:Lcom/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask$EncoderStateListener;
        //   744: invokeinterface com/sonyericsson/android/camera/recorder/utility/encoder/EncodedDataWriteTask$EncoderStateListener.onEncoderFinished:()V
        //   749: return         
        //   750: astore_3       
        //   751: aload_2        
        //   752: monitorexit    
        //   753: aload_3        
        //   754: athrow         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                             
        //  -----  -----  -----  -----  ---------------------------------
        //  41     106    750    755    Any
        //  106    184    750    755    Any
        //  184    186    750    755    Any
        //  214    227    703    733    Ljava/lang/IllegalStateException;
        //  447    464    584    589    Any
        //  585    587    584    589    Any
        //  751    753    750    755    Any
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
    
    public interface EncoderStateListener
    {
        void onEncoderFinished();
        
        void onEncoderFormatChanged(final MediaFormat p0);
    }
}
