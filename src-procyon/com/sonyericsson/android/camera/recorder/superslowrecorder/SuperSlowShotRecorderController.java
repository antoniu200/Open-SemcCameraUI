// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.recorder.superslowrecorder;

import com.sonyericsson.android.camera.recorder.utility.encoder.source.VideoFrameSource;
import com.sonyericsson.android.camera.recorder.utility.encoder.InputDataSource;
import android.media.CamcorderProfile;
import android.media.MediaCodec;
import android.util.Pair;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import com.sonyericsson.android.camera.recorder.defaultrecorder.BaseRecorderController;
import java.util.concurrent.ExecutionException;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.recorder.RecorderParameters;
import com.sonyericsson.android.camera.util.ThreadUtil;
import com.sonyericsson.android.camera.recorder.RecorderInterface;
import android.os.Handler;
import com.sonyericsson.android.camera.recorder.RecorderController;
import com.sonymobile.imageprocessor.bypasscamera2.BypassCamera;
import com.sonyericsson.android.camera.device.CameraActionSound;
import com.sonyericsson.android.camera.recorder.utility.Accessor;
import android.content.Context;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import com.sonyericsson.android.camera.recorder.defaultrecorder.DefaultRecorderController;

public class SuperSlowShotRecorderController extends DefaultRecorderController
{
    private static final int MEDIA_FORMAT_OPERATING_RATE = 120;
    private static long MIN_VIDEO_DURATION_MILLIS = 0L;
    private static final long START_RECORDING_TIME_OUT_MILLIS = 10000L;
    private static final String THREAD_NAME = "SSS_RECORDER_PREPARE";
    private static final boolean TRACE = true;
    private final OnSuperSlowRecordingFinishedListener mOnSuperSlowRecordingFinishedListener;
    private final CallbackLock mPrepareSuperSlowRecordingCallbackLock;
    private Future<Boolean> mPrepareTask;
    private final ExecutorService mPrepareTaskExecutor;
    private final CallbackLock mStartSuperSlowRecordingCallbackLock;
    private final int mSuperSlowFrameNum;
    private final int mSuperSlowFrameRate;
    
    public SuperSlowShotRecorderController(final Context context, final Accessor<CameraActionSound> accessor, final Accessor<BypassCamera> accessor2, final RecorderListener recorderListener, final OnSuperSlowRecordingFinishedListener mOnSuperSlowRecordingFinishedListener, final Handler handler, final int n, final Handler handler2, final boolean b, final int mSuperSlowFrameRate, final int mSuperSlowFrameNum) {
        super(context, accessor, accessor2, new VariableSourceMediaRecorder(120), recorderListener, SuperSlowShotRecorderController.MIN_VIDEO_DURATION_MILLIS, handler, n, handler2, true, false, false, b, true);
        this.mPrepareTaskExecutor = ThreadUtil.buildExecutor("SSS_RECORDER_PREPARE");
        trace("SuperSlowShotRecorderController() E");
        this.mSuperSlowFrameRate = mSuperSlowFrameRate;
        this.mSuperSlowFrameNum = mSuperSlowFrameNum;
        this.mOnSuperSlowRecordingFinishedListener = mOnSuperSlowRecordingFinishedListener;
        ((VariableSourceMediaRecorder)this.getRecorder()).setInputDataSourceFactory((VariableSourceMediaRecorder.InputDataSourceFactory)new SuperSlowSourceFactory());
        this.mPrepareSuperSlowRecordingCallbackLock = new CallbackLock();
        this.mStartSuperSlowRecordingCallbackLock = new CallbackLock();
        trace("SuperSlowShotRecorderController() X");
    }
    
    private static void trace(final String s) {
        CamLog.d(s);
    }
    
    private boolean waitForPrepareCompleted() {
        if (this.mPrepareTask == null) {
            CamLog.e("PrepareTask is not submitted.");
            return false;
        }
        try {
            return this.mPrepareTask.get();
        }
        catch (final ExecutionException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Exception is thrown in PrepareTask.  cause:");
            sb.append(ex.getCause().getMessage());
            CamLog.e(sb.toString());
            return false;
        }
        catch (final InterruptedException ex2) {
            CamLog.e("PrepareTask is interrupted.");
            return false;
        }
    }
    
    @Override
    public long getRecordingTimeMillis() {
        if (this.verifyState(State.STOPPING, State.RELEASING)) {
            return 1000 * this.mSuperSlowFrameNum / 30;
        }
        return super.getRecordingTimeMillis();
    }
    
    @Override
    protected boolean prepareBypassCamera(final RecorderParameters recorderParameters) {
        final StringBuilder sb = new StringBuilder();
        sb.append("prepareBypassCamera() E frame-rate:");
        sb.append(this.mSuperSlowFrameRate);
        sb.append(" frame-num");
        sb.append(this.mSuperSlowFrameNum);
        trace(sb.toString());
        final CountDownLatch requestLatch = this.mPrepareSuperSlowRecordingCallbackLock.requestLatch();
        try {
            try {
                this.getBypassCamera().requestPrepareSuperSlowRecording(this.getRecorder().getSurface(), new BypassCamera.RecordingParameters(new BypassCamera.DataSpace(0, 0, 0)));
                requestLatch.await();
                this.mPrepareSuperSlowRecordingCallbackLock.release();
                trace("prepareBypassCamera() X");
                return true;
            }
            finally {}
        }
        catch (final RuntimeException | InterruptedException ex) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("prepareBypassCamera() X failed : ");
            sb2.append(((Throwable)ex).getMessage());
            trace(sb2.toString());
            this.mPrepareSuperSlowRecordingCallbackLock.release();
            return false;
        }
        this.mPrepareSuperSlowRecordingCallbackLock.release();
    }
    
    @Override
    protected boolean prepareCallBack() {
        trace("prepareCallBack() E");
        if (!super.prepareCallBack()) {
            trace("prepareCallBack() X failed.");
            return false;
        }
        this.getBypassCamera().setSuperSlowCallbacks((BypassCamera.PrepareSuperSlowRecordingCallback)new PrepareSuperSlowRecordingCallbackImpl(this.mPrepareSuperSlowRecordingCallbackLock), (BypassCamera.StartSuperSlowRecordingCallback)new StartSuperSlowRecordingCallbackImpl(this.mStartSuperSlowRecordingCallbackLock));
        trace("prepareCallBack() X");
        return true;
    }
    
    @Override
    protected boolean prepareInternal(final RecorderParameters recorderParameters) {
        this.mPrepareTask = this.mPrepareTaskExecutor.submit((Callable<Boolean>)new PrepareTask(recorderParameters));
        return true;
    }
    
    @Override
    protected void releaseInternal() {
        this.waitForPrepareCompleted();
        this.mPrepareTaskExecutor.shutdown();
        super.releaseInternal();
    }
    
    @Override
    protected boolean startInternal() throws TimeoutException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: invokestatic    com/sonyericsson/android/camera/recorder/superslowrecorder/SuperSlowShotRecorderController.trace:(Ljava/lang/String;)V
        //     6: aload_0        
        //     7: invokespecial   com/sonyericsson/android/camera/recorder/superslowrecorder/SuperSlowShotRecorderController.waitForPrepareCompleted:()Z
        //    10: ifne            15
        //    13: iconst_0       
        //    14: ireturn        
        //    15: aload_0        
        //    16: getfield        com/sonyericsson/android/camera/recorder/superslowrecorder/SuperSlowShotRecorderController.mStartSuperSlowRecordingCallbackLock:Lcom/sonyericsson/android/camera/recorder/defaultrecorder/DefaultRecorderController$CallbackLock;
        //    19: invokevirtual   com/sonyericsson/android/camera/recorder/defaultrecorder/DefaultRecorderController$CallbackLock.requestLatch:()Ljava/util/concurrent/CountDownLatch;
        //    22: astore_2       
        //    23: aload_0        
        //    24: invokevirtual   com/sonyericsson/android/camera/recorder/superslowrecorder/SuperSlowShotRecorderController.getBypassCamera:()Lcom/sonymobile/imageprocessor/bypasscamera2/BypassCamera;
        //    27: invokevirtual   com/sonymobile/imageprocessor/bypasscamera2/BypassCamera.requestStartSuperSlowRecording:()V
        //    30: ldc_w           "startInternal() recorder.start E"
        //    33: invokestatic    com/sonyericsson/android/camera/recorder/superslowrecorder/SuperSlowShotRecorderController.trace:(Ljava/lang/String;)V
        //    36: aload_0        
        //    37: invokevirtual   com/sonyericsson/android/camera/recorder/superslowrecorder/SuperSlowShotRecorderController.getRecorder:()Lcom/sonyericsson/android/camera/recorder/RecorderInterface;
        //    40: invokeinterface com/sonyericsson/android/camera/recorder/RecorderInterface.start:()V
        //    45: ldc_w           "startInternal() recorder.start X"
        //    48: invokestatic    com/sonyericsson/android/camera/recorder/superslowrecorder/SuperSlowShotRecorderController.trace:(Ljava/lang/String;)V
        //    51: ldc_w           "startInternal() reference-clock.start E"
        //    54: invokestatic    com/sonyericsson/android/camera/recorder/superslowrecorder/SuperSlowShotRecorderController.trace:(Ljava/lang/String;)V
        //    57: aload_0        
        //    58: invokevirtual   com/sonyericsson/android/camera/recorder/superslowrecorder/SuperSlowShotRecorderController.getReferenceClock:()Lcom/sonyericsson/android/camera/recorder/utility/ReferenceClock;
        //    61: invokevirtual   com/sonyericsson/android/camera/recorder/utility/ReferenceClock.start:()V
        //    64: ldc_w           "startInternal() reference-clock.start X"
        //    67: invokestatic    com/sonyericsson/android/camera/recorder/superslowrecorder/SuperSlowShotRecorderController.trace:(Ljava/lang/String;)V
        //    70: ldc_w           "startInternal() latch.await E"
        //    73: invokestatic    com/sonyericsson/android/camera/recorder/superslowrecorder/SuperSlowShotRecorderController.trace:(Ljava/lang/String;)V
        //    76: aload_2        
        //    77: ldc2_w          10000
        //    80: getstatic       java/util/concurrent/TimeUnit.MILLISECONDS:Ljava/util/concurrent/TimeUnit;
        //    83: invokevirtual   java/util/concurrent/CountDownLatch.await:(JLjava/util/concurrent/TimeUnit;)Z
        //    86: ifne            117
        //    89: aload_0        
        //    90: getstatic       com/sonyericsson/android/camera/recorder/defaultrecorder/BaseRecorderController$State.RELEASED:Lcom/sonyericsson/android/camera/recorder/defaultrecorder/BaseRecorderController$State;
        //    93: invokevirtual   com/sonyericsson/android/camera/recorder/superslowrecorder/SuperSlowShotRecorderController.changeTo:(Lcom/sonyericsson/android/camera/recorder/defaultrecorder/BaseRecorderController$State;)V
        //    96: getstatic       com/sonyericsson/android/camera/util/CamLog.DEBUG:Z
        //    99: ifeq            115
        //   102: new             Ljava/util/concurrent/TimeoutException;
        //   105: astore_1       
        //   106: aload_1        
        //   107: ldc_w           "Callback of slow motion frame is not sent over 5s from Bypasscamera"
        //   110: invokespecial   java/util/concurrent/TimeoutException.<init>:(Ljava/lang/String;)V
        //   113: aload_1        
        //   114: athrow         
        //   115: iconst_0       
        //   116: ireturn        
        //   117: ldc_w           "startInternal() latch.await X"
        //   120: invokestatic    com/sonyericsson/android/camera/recorder/superslowrecorder/SuperSlowShotRecorderController.trace:(Ljava/lang/String;)V
        //   123: goto            159
        //   126: astore_1       
        //   127: new             Ljava/lang/StringBuilder;
        //   130: dup            
        //   131: invokespecial   java/lang/StringBuilder.<init>:()V
        //   134: astore_2       
        //   135: aload_2        
        //   136: ldc_w           "startInternal() X failed : "
        //   139: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   142: pop            
        //   143: aload_2        
        //   144: aload_1        
        //   145: invokevirtual   java/lang/InterruptedException.getMessage:()Ljava/lang/String;
        //   148: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   151: pop            
        //   152: aload_2        
        //   153: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   156: invokestatic    com/sonyericsson/android/camera/recorder/superslowrecorder/SuperSlowShotRecorderController.trace:(Ljava/lang/String;)V
        //   159: ldc_w           "startInternal() post callback E"
        //   162: invokestatic    com/sonyericsson/android/camera/recorder/superslowrecorder/SuperSlowShotRecorderController.trace:(Ljava/lang/String;)V
        //   165: aload_0        
        //   166: invokevirtual   com/sonyericsson/android/camera/recorder/superslowrecorder/SuperSlowShotRecorderController.getCallbackHandler:()Landroid/os/Handler;
        //   169: new             Lcom/sonyericsson/android/camera/recorder/superslowrecorder/SuperSlowShotRecorderController$1;
        //   172: dup            
        //   173: aload_0        
        //   174: invokespecial   com/sonyericsson/android/camera/recorder/superslowrecorder/SuperSlowShotRecorderController$1.<init>:(Lcom/sonyericsson/android/camera/recorder/superslowrecorder/SuperSlowShotRecorderController;)V
        //   177: invokevirtual   android/os/Handler.post:(Ljava/lang/Runnable;)Z
        //   180: pop            
        //   181: ldc_w           "startInternal() post callback X"
        //   184: invokestatic    com/sonyericsson/android/camera/recorder/superslowrecorder/SuperSlowShotRecorderController.trace:(Ljava/lang/String;)V
        //   187: ldc_w           "startInternal() X"
        //   190: invokestatic    com/sonyericsson/android/camera/recorder/superslowrecorder/SuperSlowShotRecorderController.trace:(Ljava/lang/String;)V
        //   193: iconst_1       
        //   194: ireturn        
        //   195: astore_1       
        //   196: goto            332
        //   199: astore_3       
        //   200: new             Ljava/lang/StringBuilder;
        //   203: astore_1       
        //   204: aload_1        
        //   205: invokespecial   java/lang/StringBuilder.<init>:()V
        //   208: aload_1        
        //   209: ldc_w           "startInternal() X failed : "
        //   212: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   215: pop            
        //   216: aload_1        
        //   217: aload_3        
        //   218: invokevirtual   java/lang/Exception.getMessage:()Ljava/lang/String;
        //   221: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   224: pop            
        //   225: aload_1        
        //   226: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   229: invokestatic    com/sonyericsson/android/camera/recorder/superslowrecorder/SuperSlowShotRecorderController.trace:(Ljava/lang/String;)V
        //   232: aload_0        
        //   233: invokevirtual   com/sonyericsson/android/camera/recorder/superslowrecorder/SuperSlowShotRecorderController.getRecorder:()Lcom/sonyericsson/android/camera/recorder/RecorderInterface;
        //   236: invokeinterface com/sonyericsson/android/camera/recorder/RecorderInterface.reset:()V
        //   241: ldc_w           "startInternal() latch.await E"
        //   244: invokestatic    com/sonyericsson/android/camera/recorder/superslowrecorder/SuperSlowShotRecorderController.trace:(Ljava/lang/String;)V
        //   247: aload_2        
        //   248: ldc2_w          10000
        //   251: getstatic       java/util/concurrent/TimeUnit.MILLISECONDS:Ljava/util/concurrent/TimeUnit;
        //   254: invokevirtual   java/util/concurrent/CountDownLatch.await:(JLjava/util/concurrent/TimeUnit;)Z
        //   257: ifne            288
        //   260: aload_0        
        //   261: getstatic       com/sonyericsson/android/camera/recorder/defaultrecorder/BaseRecorderController$State.RELEASED:Lcom/sonyericsson/android/camera/recorder/defaultrecorder/BaseRecorderController$State;
        //   264: invokevirtual   com/sonyericsson/android/camera/recorder/superslowrecorder/SuperSlowShotRecorderController.changeTo:(Lcom/sonyericsson/android/camera/recorder/defaultrecorder/BaseRecorderController$State;)V
        //   267: getstatic       com/sonyericsson/android/camera/util/CamLog.DEBUG:Z
        //   270: ifeq            286
        //   273: new             Ljava/util/concurrent/TimeoutException;
        //   276: astore_1       
        //   277: aload_1        
        //   278: ldc_w           "Callback of slow motion frame is not sent over 5s from Bypasscamera"
        //   281: invokespecial   java/util/concurrent/TimeoutException.<init>:(Ljava/lang/String;)V
        //   284: aload_1        
        //   285: athrow         
        //   286: iconst_0       
        //   287: ireturn        
        //   288: ldc_w           "startInternal() latch.await X"
        //   291: invokestatic    com/sonyericsson/android/camera/recorder/superslowrecorder/SuperSlowShotRecorderController.trace:(Ljava/lang/String;)V
        //   294: goto            330
        //   297: astore_1       
        //   298: new             Ljava/lang/StringBuilder;
        //   301: dup            
        //   302: invokespecial   java/lang/StringBuilder.<init>:()V
        //   305: astore_2       
        //   306: aload_2        
        //   307: ldc_w           "startInternal() X failed : "
        //   310: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   313: pop            
        //   314: aload_2        
        //   315: aload_1        
        //   316: invokevirtual   java/lang/InterruptedException.getMessage:()Ljava/lang/String;
        //   319: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   322: pop            
        //   323: aload_2        
        //   324: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   327: invokestatic    com/sonyericsson/android/camera/recorder/superslowrecorder/SuperSlowShotRecorderController.trace:(Ljava/lang/String;)V
        //   330: iconst_0       
        //   331: ireturn        
        //   332: ldc_w           "startInternal() latch.await E"
        //   335: invokestatic    com/sonyericsson/android/camera/recorder/superslowrecorder/SuperSlowShotRecorderController.trace:(Ljava/lang/String;)V
        //   338: aload_2        
        //   339: ldc2_w          10000
        //   342: getstatic       java/util/concurrent/TimeUnit.MILLISECONDS:Ljava/util/concurrent/TimeUnit;
        //   345: invokevirtual   java/util/concurrent/CountDownLatch.await:(JLjava/util/concurrent/TimeUnit;)Z
        //   348: ifne            379
        //   351: aload_0        
        //   352: getstatic       com/sonyericsson/android/camera/recorder/defaultrecorder/BaseRecorderController$State.RELEASED:Lcom/sonyericsson/android/camera/recorder/defaultrecorder/BaseRecorderController$State;
        //   355: invokevirtual   com/sonyericsson/android/camera/recorder/superslowrecorder/SuperSlowShotRecorderController.changeTo:(Lcom/sonyericsson/android/camera/recorder/defaultrecorder/BaseRecorderController$State;)V
        //   358: getstatic       com/sonyericsson/android/camera/util/CamLog.DEBUG:Z
        //   361: ifeq            377
        //   364: new             Ljava/util/concurrent/TimeoutException;
        //   367: astore_2       
        //   368: aload_2        
        //   369: ldc_w           "Callback of slow motion frame is not sent over 5s from Bypasscamera"
        //   372: invokespecial   java/util/concurrent/TimeoutException.<init>:(Ljava/lang/String;)V
        //   375: aload_2        
        //   376: athrow         
        //   377: iconst_0       
        //   378: ireturn        
        //   379: ldc_w           "startInternal() latch.await X"
        //   382: invokestatic    com/sonyericsson/android/camera/recorder/superslowrecorder/SuperSlowShotRecorderController.trace:(Ljava/lang/String;)V
        //   385: goto            421
        //   388: astore_3       
        //   389: new             Ljava/lang/StringBuilder;
        //   392: dup            
        //   393: invokespecial   java/lang/StringBuilder.<init>:()V
        //   396: astore_2       
        //   397: aload_2        
        //   398: ldc_w           "startInternal() X failed : "
        //   401: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   404: pop            
        //   405: aload_2        
        //   406: aload_3        
        //   407: invokevirtual   java/lang/InterruptedException.getMessage:()Ljava/lang/String;
        //   410: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   413: pop            
        //   414: aload_2        
        //   415: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   418: invokestatic    com/sonyericsson/android/camera/recorder/superslowrecorder/SuperSlowShotRecorderController.trace:(Ljava/lang/String;)V
        //   421: aload_1        
        //   422: athrow         
        //   423: astore_1       
        //   424: new             Ljava/lang/StringBuilder;
        //   427: dup            
        //   428: invokespecial   java/lang/StringBuilder.<init>:()V
        //   431: astore_2       
        //   432: aload_2        
        //   433: ldc_w           "startInternal() X failed : "
        //   436: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   439: pop            
        //   440: aload_2        
        //   441: aload_1        
        //   442: invokevirtual   java/lang/RuntimeException.getMessage:()Ljava/lang/String;
        //   445: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   448: pop            
        //   449: aload_2        
        //   450: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   453: invokestatic    com/sonyericsson/android/camera/recorder/superslowrecorder/SuperSlowShotRecorderController.trace:(Ljava/lang/String;)V
        //   456: iconst_0       
        //   457: ireturn        
        //    Exceptions:
        //  throws java.util.concurrent.TimeoutException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                             
        //  -----  -----  -----  -----  ---------------------------------
        //  23     30     423    458    Ljava/lang/RuntimeException;
        //  30     70     199    332    Ljava/lang/IllegalStateException;
        //  30     70     199    332    Ljava/io/IOException;
        //  30     70     195    423    Any
        //  70     115    126    159    Ljava/lang/InterruptedException;
        //  117    123    126    159    Ljava/lang/InterruptedException;
        //  200    241    195    423    Any
        //  241    286    297    330    Ljava/lang/InterruptedException;
        //  288    294    297    330    Ljava/lang/InterruptedException;
        //  332    377    388    421    Ljava/lang/InterruptedException;
        //  379    385    388    421    Ljava/lang/InterruptedException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index 212 out of bounds for length 212
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:100)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:106)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:302)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:385)
        //     at java.base/java.util.ArrayList.get(ArrayList.java:427)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3362)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3611)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3476)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:112)
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
    protected Pair<Boolean, CountDownLatch> stopBypassCamera() {
        return (Pair<Boolean, CountDownLatch>)Pair.create((Object)true, (Object)null);
    }
    
    private static class PrepareSuperSlowRecordingCallbackImpl implements PrepareSuperSlowRecordingCallback
    {
        private final CallbackLock mLock;
        
        public PrepareSuperSlowRecordingCallbackImpl(final CallbackLock mLock) {
            this.mLock = mLock;
        }
        
        @Override
        public void onPrepareSuperSlowRecordingDone() {
            trace("onPrepareSuperSlowRecordingDone() E");
            this.mLock.unlock();
            trace("onPrepareSuperSlowRecordingDone() X");
        }
    }
    
    private class PrepareTask implements Callable<Boolean>
    {
        private final RecorderParameters mParams;
        final SuperSlowShotRecorderController this$0;
        
        public PrepareTask(final SuperSlowShotRecorderController this$0, final RecorderParameters mParams) {
            this.this$0 = this$0;
            this.mParams = mParams;
        }
        
        @Override
        public Boolean call() throws Exception {
            trace("prepareInternal() E");
            if (!this.this$0.prepareCallBack()) {
                CamLog.e("prepareCallBack() is failed in PrepareTask.");
                this.this$0.notifyError();
                return false;
            }
            if (!this.this$0.prepareInternal(this.mParams)) {
                CamLog.e("prepareInternal() is failed in PrepareTask.");
                this.this$0.notifyError();
                return false;
            }
            if (!this.this$0.startBypassCamera()) {
                CamLog.e("startBypassCamera() is failed in PrepareTask.");
                this.this$0.notifyError();
                return false;
            }
            trace("prepareInternal() X");
            return true;
        }
    }
    
    private static class StartSuperSlowRecordingCallbackImpl implements StartSuperSlowRecordingCallback
    {
        private final CallbackLock mLock;
        
        public StartSuperSlowRecordingCallbackImpl(final CallbackLock mLock) {
            this.mLock = mLock;
        }
        
        @Override
        public void onStartSuperSlowRecordingDone() {
            trace("onStartSuperSlowRecordingDone() E");
            this.mLock.unlock();
            trace("onStartSuperSlowRecordingDone() X");
        }
    }
    
    private static class SuperSlowSourceFactory implements InputDataSourceFactory
    {
        @Override
        public InputDataSource createAudioSource(final MediaCodec mediaCodec, final CamcorderProfile camcorderProfile) {
            throw new UnsupportedOperationException("This recorder doesn't support audio track.");
        }
        
        @Override
        public VideoFrameSource createVideoSource(final MediaCodec mediaCodec, final CamcorderProfile camcorderProfile) {
            return new VideoFrameSource(mediaCodec);
        }
    }
}
