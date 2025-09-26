// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.recorder.defaultrecorder;

import com.sonymobile.android.media.MediaRecorder;
import android.location.Location;
import java.io.IOException;
import android.media.MediaRecorder$OnInfoListener;
import android.media.MediaRecorder$OnErrorListener;
import android.view.Surface;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.cameracommon.storage.StorageUtil;
import java.io.FileNotFoundException;
import com.sonyericsson.android.camera.util.CamLog;
import android.net.Uri;
import android.content.Context;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.recorder.RecorderParameters;
import com.sonymobile.android.media.MediaRecorderWrapper;
import android.os.ParcelFileDescriptor;
import com.sonyericsson.android.camera.recorder.RecorderInterface;

public class DefaultRecorder implements RecorderInterface
{
    private static final int DELAY_AUDIO_DURATION_IN_MILLIS = 100;
    private static final int MUTE_START_RECORD_SOUND_DURATION_IN_MILLIS = 900;
    private static final int REQUEST_PROGRESS_INTERVAL_IN_MILLIS = 1000;
    private static final String TAG = "DefaultRecorder";
    private static final boolean TRACE = false;
    private RecordTrackListener mAudioTrackListener;
    private ParcelFileDescriptor mDescriptor;
    private boolean mIsMicrophoneEnabled;
    private long mLastProgressMillis;
    private RecorderInterface.OnErrorListener mOnErrorListener;
    private OnMaxReachedListener mOnMaxReachedListener;
    private final MediaRecorderWrapper mRecorder;
    private final int mVideoSource;
    private RecordTrackListener mVideoTrackListener;
    
    public DefaultRecorder(final int mVideoSource, final boolean b) {
        this.mDescriptor = null;
        this.mVideoSource = mVideoSource;
        (this.mRecorder = new MediaRecorderWrapper(b)).useIntelligentActive(b);
    }
    
    private void adjustAudioSettings() {
        this.mRecorder.adjustAudioStartVolume(900);
        this.mRecorder.adjustAudioTimestamp(100L);
    }
    
    private static String getNameForErrorCode(final int i) {
        if (i == 1) {
            return "MEDIA_RECORDER_ERROR_UNKNOWN";
        }
        if (i != 100) {
            final StringBuilder sb = new StringBuilder();
            sb.append("unknown:");
            sb.append(i);
            return sb.toString();
        }
        return "MEDIA_ERROR_SERVER_DIED";
    }
    
    private boolean prepareReceiveRecordingInfo() {
        boolean b;
        try {
            this.mRecorder.requestProgressInfo(1000);
            b = true;
        }
        catch (final UnsupportedOperationException ex) {
            b = false;
        }
        return b;
    }
    
    private void setVideoEncodingProfileLevel(final RecorderParameters recorderParameters) {
        if (recorderParameters != null && recorderParameters.profile() != null) {
            final int videoFrameWidth = recorderParameters.profile().videoFrameWidth;
            final int videoFrameHeight = recorderParameters.profile().videoFrameHeight;
            if (recorderParameters.isHdr()) {
                this.mRecorder.setVideoEncodingProfileLevel(PlatformCapability.getVideoHdrRecordingProfile(), 1);
            }
            else if (videoFrameWidth >= 3840 && videoFrameHeight >= 2160) {
                if (recorderParameters.profile().videoCodec == 2) {
                    this.mRecorder.setVideoEncodingProfileLevel(8, 1);
                }
                else {
                    this.mRecorder.setVideoEncodingProfileLevel(1, 1);
                }
            }
            else if (videoFrameWidth >= 640 && videoFrameHeight >= 480) {
                this.mRecorder.setVideoEncodingProfileLevel(8, 1);
            }
            else {
                this.mRecorder.setVideoEncodingProfileLevel(1, 1);
            }
        }
    }
    
    private boolean setupOutput(final Context context, Uri file) {
        if (file.getScheme().equalsIgnoreCase("content")) {
            try {
                this.mDescriptor = context.getContentResolver().openFileDescriptor(file, "rw");
                if (this.mDescriptor == null) {
                    CamLog.e("openFileDescriptor fd is null.");
                    return false;
                }
                try {
                    this.mRecorder.setOutputFile(this.mDescriptor.getFileDescriptor());
                    return true;
                }
                catch (final UnsupportedOperationException ex) {
                    CamLog.e("setOutputFile() failed.", ex);
                    return false;
                }
            }
            catch (final FileNotFoundException ex2) {
                CamLog.e("openFileDescriptor failed.", ex2);
                return false;
            }
        }
        if (file.getScheme().equalsIgnoreCase("file")) {
            if (StorageUtil.getStorageTypeFromUri(file, context) != Storage.StorageType.EXTERNAL_CARD) {
                this.mRecorder.setOutputFile(file.getPath());
                return true;
            }
            final Uri sdCardGrantedUri = StorageUtil.getSdCardGrantedUri(context);
            file = StorageUtil.createFile(context, sdCardGrantedUri, StorageUtil.getPathAfterDcim(sdCardGrantedUri, file.getPath()));
            try {
                this.mDescriptor = context.getContentResolver().openFileDescriptor(file, "rw");
                if (this.mDescriptor == null) {
                    CamLog.e("openFileDescriptor fd is null.");
                    return false;
                }
                this.mRecorder.setOutputFile(this.mDescriptor.getFileDescriptor());
                return true;
            }
            catch (final FileNotFoundException ex3) {
                CamLog.e("openFileDescriptor failed.", ex3);
                return false;
            }
        }
        return false;
    }
    
    private boolean setupParameters(final Context p0, final RecorderParameters p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   com/sonyericsson/android/camera/recorder/RecorderParameters.isMicrophoneEnabled:()Z
        //     4: ifeq            45
        //     7: aload_0        
        //     8: getfield        com/sonyericsson/android/camera/recorder/defaultrecorder/DefaultRecorder.mRecorder:Lcom/sonymobile/android/media/MediaRecorderWrapper;
        //    11: iconst_5       
        //    12: invokevirtual   com/sonymobile/android/media/MediaRecorderWrapper.setAudioSource:(I)V
        //    15: aload_0        
        //    16: getfield        com/sonyericsson/android/camera/recorder/defaultrecorder/DefaultRecorder.mRecorder:Lcom/sonymobile/android/media/MediaRecorderWrapper;
        //    19: aload_0        
        //    20: getfield        com/sonyericsson/android/camera/recorder/defaultrecorder/DefaultRecorder.mVideoSource:I
        //    23: invokevirtual   com/sonymobile/android/media/MediaRecorderWrapper.setVideoSource:(I)V
        //    26: aload_0        
        //    27: getfield        com/sonyericsson/android/camera/recorder/defaultrecorder/DefaultRecorder.mRecorder:Lcom/sonymobile/android/media/MediaRecorderWrapper;
        //    30: aload_2        
        //    31: invokevirtual   com/sonyericsson/android/camera/recorder/RecorderParameters.profile:()Landroid/media/CamcorderProfile;
        //    34: invokevirtual   com/sonymobile/android/media/MediaRecorderWrapper.setProfile:(Landroid/media/CamcorderProfile;)V
        //    37: aload_0        
        //    38: iconst_1       
        //    39: putfield        com/sonyericsson/android/camera/recorder/defaultrecorder/DefaultRecorder.mIsMicrophoneEnabled:Z
        //    42: goto            138
        //    45: aload_0        
        //    46: getfield        com/sonyericsson/android/camera/recorder/defaultrecorder/DefaultRecorder.mRecorder:Lcom/sonymobile/android/media/MediaRecorderWrapper;
        //    49: aload_0        
        //    50: getfield        com/sonyericsson/android/camera/recorder/defaultrecorder/DefaultRecorder.mVideoSource:I
        //    53: invokevirtual   com/sonymobile/android/media/MediaRecorderWrapper.setVideoSource:(I)V
        //    56: aload_0        
        //    57: getfield        com/sonyericsson/android/camera/recorder/defaultrecorder/DefaultRecorder.mRecorder:Lcom/sonymobile/android/media/MediaRecorderWrapper;
        //    60: aload_2        
        //    61: invokevirtual   com/sonyericsson/android/camera/recorder/RecorderParameters.profile:()Landroid/media/CamcorderProfile;
        //    64: getfield        android/media/CamcorderProfile.fileFormat:I
        //    67: invokevirtual   com/sonymobile/android/media/MediaRecorderWrapper.setOutputFormat:(I)V
        //    70: aload_0        
        //    71: getfield        com/sonyericsson/android/camera/recorder/defaultrecorder/DefaultRecorder.mRecorder:Lcom/sonymobile/android/media/MediaRecorderWrapper;
        //    74: aload_2        
        //    75: invokevirtual   com/sonyericsson/android/camera/recorder/RecorderParameters.profile:()Landroid/media/CamcorderProfile;
        //    78: getfield        android/media/CamcorderProfile.videoFrameRate:I
        //    81: invokevirtual   com/sonymobile/android/media/MediaRecorderWrapper.setVideoFrameRate:(I)V
        //    84: aload_0        
        //    85: getfield        com/sonyericsson/android/camera/recorder/defaultrecorder/DefaultRecorder.mRecorder:Lcom/sonymobile/android/media/MediaRecorderWrapper;
        //    88: aload_2        
        //    89: invokevirtual   com/sonyericsson/android/camera/recorder/RecorderParameters.profile:()Landroid/media/CamcorderProfile;
        //    92: getfield        android/media/CamcorderProfile.videoFrameWidth:I
        //    95: aload_2        
        //    96: invokevirtual   com/sonyericsson/android/camera/recorder/RecorderParameters.profile:()Landroid/media/CamcorderProfile;
        //    99: getfield        android/media/CamcorderProfile.videoFrameHeight:I
        //   102: invokevirtual   com/sonymobile/android/media/MediaRecorderWrapper.setVideoSize:(II)V
        //   105: aload_0        
        //   106: getfield        com/sonyericsson/android/camera/recorder/defaultrecorder/DefaultRecorder.mRecorder:Lcom/sonymobile/android/media/MediaRecorderWrapper;
        //   109: aload_2        
        //   110: invokevirtual   com/sonyericsson/android/camera/recorder/RecorderParameters.profile:()Landroid/media/CamcorderProfile;
        //   113: getfield        android/media/CamcorderProfile.videoBitRate:I
        //   116: invokevirtual   com/sonymobile/android/media/MediaRecorderWrapper.setVideoEncodingBitRate:(I)V
        //   119: aload_0        
        //   120: getfield        com/sonyericsson/android/camera/recorder/defaultrecorder/DefaultRecorder.mRecorder:Lcom/sonymobile/android/media/MediaRecorderWrapper;
        //   123: aload_2        
        //   124: invokevirtual   com/sonyericsson/android/camera/recorder/RecorderParameters.profile:()Landroid/media/CamcorderProfile;
        //   127: getfield        android/media/CamcorderProfile.videoCodec:I
        //   130: invokevirtual   com/sonymobile/android/media/MediaRecorderWrapper.setVideoEncoder:(I)V
        //   133: aload_0        
        //   134: iconst_0       
        //   135: putfield        com/sonyericsson/android/camera/recorder/defaultrecorder/DefaultRecorder.mIsMicrophoneEnabled:Z
        //   138: aload_0        
        //   139: aload_2        
        //   140: invokespecial   com/sonyericsson/android/camera/recorder/defaultrecorder/DefaultRecorder.setVideoEncodingProfileLevel:(Lcom/sonyericsson/android/camera/recorder/RecorderParameters;)V
        //   143: aload_2        
        //   144: invokevirtual   com/sonyericsson/android/camera/recorder/RecorderParameters.hasMaxDuration:()Z
        //   147: ifeq            161
        //   150: aload_0        
        //   151: getfield        com/sonyericsson/android/camera/recorder/defaultrecorder/DefaultRecorder.mRecorder:Lcom/sonymobile/android/media/MediaRecorderWrapper;
        //   154: aload_2        
        //   155: invokevirtual   com/sonyericsson/android/camera/recorder/RecorderParameters.maxDuration:()I
        //   158: invokevirtual   com/sonymobile/android/media/MediaRecorderWrapper.setMaxDuration:(I)V
        //   161: aload_2        
        //   162: invokevirtual   com/sonyericsson/android/camera/recorder/RecorderParameters.hasMaxFileSize:()Z
        //   165: ifeq            179
        //   168: aload_0        
        //   169: getfield        com/sonyericsson/android/camera/recorder/defaultrecorder/DefaultRecorder.mRecorder:Lcom/sonymobile/android/media/MediaRecorderWrapper;
        //   172: aload_2        
        //   173: invokevirtual   com/sonyericsson/android/camera/recorder/RecorderParameters.maxFileSize:()J
        //   176: invokevirtual   com/sonymobile/android/media/MediaRecorderWrapper.setMaxFileSize:(J)V
        //   179: aload_2        
        //   180: invokevirtual   com/sonyericsson/android/camera/recorder/RecorderParameters.hasLocation:()Z
        //   183: ifeq            209
        //   186: aload_0        
        //   187: getfield        com/sonyericsson/android/camera/recorder/defaultrecorder/DefaultRecorder.mRecorder:Lcom/sonymobile/android/media/MediaRecorderWrapper;
        //   190: aload_2        
        //   191: invokevirtual   com/sonyericsson/android/camera/recorder/RecorderParameters.location:()Landroid/location/Location;
        //   194: invokevirtual   android/location/Location.getLatitude:()D
        //   197: d2f            
        //   198: aload_2        
        //   199: invokevirtual   com/sonyericsson/android/camera/recorder/RecorderParameters.location:()Landroid/location/Location;
        //   202: invokevirtual   android/location/Location.getLongitude:()D
        //   205: d2f            
        //   206: invokevirtual   com/sonymobile/android/media/MediaRecorderWrapper.setLocation:(FF)V
        //   209: aload_2        
        //   210: invokevirtual   com/sonyericsson/android/camera/recorder/RecorderParameters.hasOrientationHint:()Z
        //   213: ifeq            227
        //   216: aload_0        
        //   217: getfield        com/sonyericsson/android/camera/recorder/defaultrecorder/DefaultRecorder.mRecorder:Lcom/sonymobile/android/media/MediaRecorderWrapper;
        //   220: aload_2        
        //   221: invokevirtual   com/sonyericsson/android/camera/recorder/RecorderParameters.orientationHint:()I
        //   224: invokevirtual   com/sonymobile/android/media/MediaRecorderWrapper.setOrientationHint:(I)V
        //   227: aload_0        
        //   228: aload_1        
        //   229: aload_2        
        //   230: invokevirtual   com/sonyericsson/android/camera/recorder/RecorderParameters.outputUri:()Landroid/net/Uri;
        //   233: invokespecial   com/sonyericsson/android/camera/recorder/defaultrecorder/DefaultRecorder.setupOutput:(Landroid/content/Context;Landroid/net/Uri;)Z
        //   236: pop            
        //   237: aload_0        
        //   238: invokespecial   com/sonyericsson/android/camera/recorder/defaultrecorder/DefaultRecorder.adjustAudioSettings:()V
        //   241: iconst_1       
        //   242: ireturn        
        //   243: astore_3       
        //   244: goto            161
        //   247: astore_3       
        //   248: goto            179
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                        
        //  -----  -----  -----  -----  ----------------------------
        //  150    161    243    247    Ljava/lang/RuntimeException;
        //  168    179    247    251    Ljava/lang/RuntimeException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0179:
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
    
    private static void trace(final String s) {
        CamLog.d(s);
    }
    
    @Override
    public Surface getSurface() {
        if (this.mVideoSource != 2) {
            throw new UnsupportedOperationException("This method is not supported with Surface source");
        }
        return this.mRecorder.getSurface();
    }
    
    @Override
    public boolean isAsyncStopSupported() {
        return this.mRecorder.isAsyncStopSupported();
    }
    
    @Override
    public void pause() {
        this.mRecorder.pause();
    }
    
    @Override
    public boolean prepare(final Context context, final RecorderParameters recorderParameters) {
        this.mRecorder.setOnErrorListener((MediaRecorder$OnErrorListener)new OnErrorListener(this.mOnErrorListener));
        this.mRecorder.setOnInfoListener((MediaRecorder$OnInfoListener)new OnInfoListener(this.mAudioTrackListener, this.mVideoTrackListener, this.mOnMaxReachedListener));
        if (!this.setupParameters(context, recorderParameters)) {
            this.release();
            return false;
        }
        if (!this.prepareReceiveRecordingInfo()) {
            return false;
        }
        try {
            this.mRecorder.prepare();
            this.mLastProgressMillis = 0L;
            return true;
        }
        catch (final IllegalStateException | IOException ex) {
            this.release();
            return false;
        }
    }
    
    @Override
    public void release() {
        this.mRecorder.reset();
        this.mRecorder.release();
    }
    
    @Override
    public void reset() {
        this.mRecorder.reset();
    }
    
    @Override
    public void resume() {
        this.mRecorder.resume();
    }
    
    @Override
    public void setListener(final RecordTrackListener mAudioTrackListener, final RecordTrackListener mVideoTrackListener, final RecorderInterface.OnErrorListener mOnErrorListener, final OnMaxReachedListener mOnMaxReachedListener) {
        this.mAudioTrackListener = mAudioTrackListener;
        this.mVideoTrackListener = mVideoTrackListener;
        this.mOnErrorListener = mOnErrorListener;
        this.mOnMaxReachedListener = mOnMaxReachedListener;
    }
    
    @Override
    public void setLocation(final Location location) {
        throw new UnsupportedOperationException("setLocation() is not supported.");
    }
    
    @Override
    public void setMaxDurationMillis(final long n) {
        throw new UnsupportedOperationException("setMaxDurationMillis() is not supported.");
    }
    
    @Override
    public void setMaxFileSizeBytes(final long n) {
        throw new UnsupportedOperationException("setMaxFileSizeBytes() is not supported.");
    }
    
    @Override
    public void setOrientationHint(final int n) {
        throw new UnsupportedOperationException("setOrientationHint() is not supported.");
    }
    
    @Override
    public void setOutputFilePath(final String s) {
        throw new UnsupportedOperationException("setOutputFilePath() is not supported.");
    }
    
    @Override
    public void start() {
        this.mRecorder.start();
    }
    
    @Override
    public void stop() {
        this.mRecorder.stop();
        if (this.mDescriptor != null) {
            try {
                this.mDescriptor.close();
            }
            catch (final IOException ex) {
                CamLog.w(ex.getMessage());
            }
        }
    }
    
    @Override
    public void stopAsync() {
        this.mRecorder.stopAsync();
    }
    
    @Override
    public void stopAudioRecording() {
        this.mRecorder.stopAudioRecording();
    }
    
    @Override
    public void stopOnCameraError() {
        trace("stopOnCameraError() E");
        this.mRecorder.stopOnError();
        trace("stopOnCameraError() X");
    }
    
    @Override
    public void waitUntilStopCompleted() {
        this.mRecorder.waitUntilStopCompleted();
    }
    
    private static class OnErrorListener implements MediaRecorder$OnErrorListener
    {
        private final RecorderInterface.OnErrorListener mListener;
        
        private OnErrorListener(final RecorderInterface.OnErrorListener mListener) {
            this.mListener = mListener;
        }
        
        private void onError(final MediaRecorder mediaRecorder, final int n, final int i) {
            final StringBuilder sb = new StringBuilder();
            sb.append("onError() E what:");
            sb.append(getNameForErrorCode(n));
            sb.append(" extra:");
            sb.append(i);
            CamLog.e(sb.toString());
            this.mListener.onError();
            CamLog.e("onError() X");
        }
        
        public void onError(final android.media.MediaRecorder mediaRecorder, final int n, final int n2) {
            this.onError((MediaRecorder)null, n, n2);
        }
    }
    
    private class OnInfoListener implements MediaRecorder$OnInfoListener
    {
        private static final int MEDIA_RECORDER_INFO_KIND_MASK = 15;
        private static final int MEDIA_RECORDER_INFO_KIND_SHIFT = 28;
        private static final int MEDIA_RECORDER_INFO_KIND_VIDEO = 1;
        private static final int MEDIA_RECORDER_INFO_KIND_VIDEO_AUDIO = 2;
        private static final int MEDIA_RECORDER_INFO_MASK = 268435455;
        private final RecordTrackListener mAudioTrackListener;
        private boolean mIsAudioTrackStarted;
        private boolean mIsVideoTrackStarted;
        private final OnMaxReachedListener mOnMaxReachedListener;
        private final RecordTrackListener mVideoTrackListener;
        final DefaultRecorder this$0;
        
        private OnInfoListener(final DefaultRecorder this$0, final RecordTrackListener mAudioTrackListener, final RecordTrackListener mVideoTrackListener, final OnMaxReachedListener mOnMaxReachedListener) {
            this.this$0 = this$0;
            this.mAudioTrackListener = mAudioTrackListener;
            this.mVideoTrackListener = mVideoTrackListener;
            this.mOnMaxReachedListener = mOnMaxReachedListener;
            this.mIsAudioTrackStarted = false;
            this.mIsVideoTrackStarted = false;
        }
        
        private void onCompleted(final int n) {
            if (this.this$0.mIsMicrophoneEnabled && n == 2) {
                this.mAudioTrackListener.onCompleted();
            }
            else if (!this.this$0.mIsMicrophoneEnabled && n == 1) {
                this.mVideoTrackListener.onCompleted();
            }
        }
        
        private void onInfo(final MediaRecorder mediaRecorder, final int n, final int n2) {
            final int n3 = n >> 28 & 0xF;
            switch (0xFFFFFFF & n) {
                case 1001: {
                    this.onProgress(n2, n3);
                    break;
                }
                case 1000: {
                    this.onCompleted(n3);
                    break;
                }
                case 801: {
                    this.mOnMaxReachedListener.onMaxFileSizeReached();
                    break;
                }
                case 800: {
                    this.mOnMaxReachedListener.onMaxDurationReached();
                    break;
                }
            }
        }
        
        private void onProgress(final int n, final int n2) {
            if (this.this$0.mIsMicrophoneEnabled) {
                if (n2 == 2) {
                    if (!this.mIsAudioTrackStarted) {
                        this.mAudioTrackListener.onStarted();
                        this.mIsAudioTrackStarted = true;
                    }
                    this.this$0.mLastProgressMillis += n;
                    this.mAudioTrackListener.onProgress(this.this$0.mLastProgressMillis);
                }
                else if (n2 == 1 && !this.mIsVideoTrackStarted) {
                    this.mVideoTrackListener.onStarted();
                    this.mIsVideoTrackStarted = true;
                }
            }
            else if (n2 == 1) {
                if (!this.mIsVideoTrackStarted) {
                    this.mVideoTrackListener.onStarted();
                    this.mIsVideoTrackStarted = true;
                }
                this.this$0.mLastProgressMillis += n;
                this.mVideoTrackListener.onProgress(this.this$0.mLastProgressMillis);
            }
        }
        
        public void onInfo(final android.media.MediaRecorder mediaRecorder, final int n, final int n2) {
            this.onInfo((MediaRecorder)null, n, n2);
        }
    }
}
