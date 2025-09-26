// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.android.media;

import android.util.Log;
import android.os.Message;
import android.media.CamcorderProfile;
import android.support.annotation.NonNull;
import android.hardware.Camera;
import java.io.FileDescriptor;
import java.io.IOException;
import android.view.Surface;
import android.os.Handler;
import java.lang.ref.WeakReference;
import android.os.Looper;
import com.sonymobile.android.media.internal.SomcMediaRecorder;
import android.media.MediaRecorder$OnInfoListener;
import android.media.MediaRecorder$OnErrorListener;

public class MediaRecorder
{
    public static final int MEDIA_ERROR_SERVER_DIED = 100;
    public static final int MEDIA_RECORDER_ERROR_UNKNOWN = 1;
    public static final int MEDIA_RECORDER_INFO_MAX_DURATION_REACHED = 800;
    public static final int MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED = 801;
    public static final int MEDIA_RECORDER_INFO_UNKNOWN = 1;
    public static final int MEDIA_RECORDER_TRACK_INFO_COMPLETION_STATUS = 1000;
    public static final int MEDIA_RECORDER_TRACK_INFO_DATA_KBYTES = 1009;
    public static final int MEDIA_RECORDER_TRACK_INFO_DURATION_MS = 1003;
    public static final int MEDIA_RECORDER_TRACK_INFO_ENCODED_FRAMES = 1005;
    public static final int MEDIA_RECORDER_TRACK_INFO_INITIAL_DELAY_MS = 1007;
    public static final int MEDIA_RECORDER_TRACK_INFO_LIST_END = 2000;
    public static final int MEDIA_RECORDER_TRACK_INFO_LIST_START = 1000;
    public static final int MEDIA_RECORDER_TRACK_INFO_MAX_CHUNK_DUR_MS = 1004;
    public static final int MEDIA_RECORDER_TRACK_INFO_PROGRESS_IN_TIME = 1001;
    public static final int MEDIA_RECORDER_TRACK_INFO_START_OFFSET_MS = 1008;
    public static final int MEDIA_RECORDER_TRACK_INFO_TYPE = 1002;
    public static final int MEDIA_RECORDER_TRACK_INTER_CHUNK_TIME_MS = 1006;
    private static final String TAG = "MediaRecorder";
    private boolean mIsAvailable;
    private MediaRecorder$OnErrorListener mOnErrorListener;
    private MediaRecorder$OnInfoListener mOnInfoListener;
    private SomcMediaRecorder mSomcMediaRecorder;
    
    public MediaRecorder() {
        CallbackHandler callbackHandler;
        if (Looper.myLooper() != null) {
            callbackHandler = new CallbackHandler(new WeakReference<MediaRecorder>(this), Looper.myLooper());
        }
        else {
            if (Looper.getMainLooper() == null) {
                throw new IllegalArgumentException("MediaRecorder must be created on thread with Looper running");
            }
            callbackHandler = new CallbackHandler(new WeakReference<MediaRecorder>(this), Looper.getMainLooper());
        }
        this.mSomcMediaRecorder = new SomcMediaRecorder(callbackHandler);
    }
    
    public static final int getAudioSourceMax() {
        return SomcMediaRecorder.getAudioSourceMax();
    }
    
    public void adjustAudioStartVolume(final int n) {
        this.mSomcMediaRecorder.adjustAudioStartVolume(n);
    }
    
    public void adjustAudioTimestamp(final long n) {
        this.mSomcMediaRecorder.adjustAudioTimestamp(n);
    }
    
    public String dump(final String s) {
        return this.mSomcMediaRecorder.dump(s);
    }
    
    @Override
    protected void finalize() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        com/sonymobile/android/media/MediaRecorder.mSomcMediaRecorder:Lcom/sonymobile/android/media/internal/SomcMediaRecorder;
        //     4: ifnull          21
        //     7: aload_0        
        //     8: getfield        com/sonymobile/android/media/MediaRecorder.mSomcMediaRecorder:Lcom/sonymobile/android/media/internal/SomcMediaRecorder;
        //    11: invokevirtual   com/sonymobile/android/media/internal/SomcMediaRecorder.reset:()V
        //    14: aload_0        
        //    15: getfield        com/sonymobile/android/media/MediaRecorder.mSomcMediaRecorder:Lcom/sonymobile/android/media/internal/SomcMediaRecorder;
        //    18: invokevirtual   com/sonymobile/android/media/internal/SomcMediaRecorder.release:()V
        //    21: return         
        //    22: astore_1       
        //    23: goto            14
        //    26: astore_1       
        //    27: goto            21
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                             
        //  -----  -----  -----  -----  ---------------------------------
        //  7      14     22     26     Ljava/lang/IllegalStateException;
        //  14     21     26     30     Ljava/lang/IllegalStateException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0014:
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
    
    public int getMaxAmplitude() throws IllegalStateException {
        throw new UnsupportedOperationException(" getMaxAmplitude unsupported");
    }
    
    public Surface getSurface() {
        return this.mSomcMediaRecorder.getSurface();
    }
    
    public void pause() throws IllegalStateException {
        this.mSomcMediaRecorder.pause();
    }
    
    public void prepare() throws IllegalStateException, IOException {
        this.mSomcMediaRecorder.prepare();
        this.mIsAvailable = true;
    }
    
    public void release() {
        this.mSomcMediaRecorder.release();
    }
    
    public void requestProgressInfo(final int n) {
        this.mSomcMediaRecorder.requestProgressInfo(n);
    }
    
    public void reset() {
        this.mIsAvailable = false;
        this.mSomcMediaRecorder.reset();
    }
    
    public void resume() throws IllegalStateException {
        this.mSomcMediaRecorder.resume();
    }
    
    public void setAudioChannels(final int audioChannels) {
        this.mSomcMediaRecorder.setAudioChannels(audioChannels);
    }
    
    public void setAudioEncoder(final int audioEncoder) throws IllegalStateException {
        this.mSomcMediaRecorder.setAudioEncoder(audioEncoder);
    }
    
    public void setAudioEncodingBitRate(final int audioEncodingBitRate) {
        this.mSomcMediaRecorder.setAudioEncodingBitRate(audioEncodingBitRate);
    }
    
    public void setAudioSamplingRate(final int audioSamplingRate) {
        this.mSomcMediaRecorder.setAudioSamplingRate(audioSamplingRate);
    }
    
    public void setAudioSource(final int audioSource) throws IllegalStateException {
        this.mSomcMediaRecorder.setAudioSource(audioSource);
    }
    
    public void setAuxiliaryOutputFile(final FileDescriptor fileDescriptor) {
        throw new UnsupportedOperationException("setAuxiliaryOutputFile(FileDescriptor) unsupported");
    }
    
    public void setAuxiliaryOutputFile(final String s) {
        throw new UnsupportedOperationException("setAuxiliaryOutputFile unsupported");
    }
    
    public void setCamera(final Camera camera) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Camera source not supported");
    }
    
    public void setCaptureRate(final double captureRate) {
        this.mSomcMediaRecorder.setCaptureRate(captureRate);
    }
    
    public void setInputSurface(@NonNull final Surface inputSurface) {
        this.mSomcMediaRecorder.setInputSurface(inputSurface);
    }
    
    public void setLocation(final float n, final float n2) {
        this.mSomcMediaRecorder.setLocation(n, n2);
    }
    
    public void setMaxDuration(final int maxDuration) throws IllegalArgumentException {
        this.mSomcMediaRecorder.setMaxDuration(maxDuration);
    }
    
    public void setMaxFileSize(final long maxFileSize) throws IllegalArgumentException {
        this.mSomcMediaRecorder.setMaxFileSize(maxFileSize);
    }
    
    public void setOnErrorListener(final MediaRecorder$OnErrorListener mOnErrorListener) {
        this.mOnErrorListener = mOnErrorListener;
    }
    
    public void setOnInfoListener(final MediaRecorder$OnInfoListener mOnInfoListener) {
        this.mOnInfoListener = mOnInfoListener;
    }
    
    public void setOrientationHint(final int orientationHint) {
        this.mSomcMediaRecorder.setOrientationHint(orientationHint);
    }
    
    public void setOutputFile(final FileDescriptor outputFile) throws IllegalStateException {
        this.mSomcMediaRecorder.setOutputFile(outputFile);
    }
    
    public void setOutputFile(final String outputFile) throws IllegalStateException {
        this.mSomcMediaRecorder.setOutputFile(outputFile);
    }
    
    public void setOutputFormat(final int outputFormat) throws IllegalStateException {
        this.mSomcMediaRecorder.setOutputFormat(outputFormat);
    }
    
    public void setPreviewDisplay(final Surface surface) {
        throw new UnsupportedOperationException("setPreviewDisplay not supported");
    }
    
    public void setProfile(final CamcorderProfile profile) {
        this.mSomcMediaRecorder.setProfile(profile);
    }
    
    public void setVideoBitRateMode(final int videoBitRateMode) {
        this.mSomcMediaRecorder.setVideoBitRateMode(videoBitRateMode);
    }
    
    public void setVideoColorAspects(final int n, final int n2, final int n3) {
        this.mSomcMediaRecorder.setVideoColorAspects(n, n2, n3);
    }
    
    public void setVideoEncoder(final int videoEncoder) throws IllegalStateException {
        this.mSomcMediaRecorder.setVideoEncoder(videoEncoder);
    }
    
    public void setVideoEncodingBitRate(final int videoEncodingBitRate) {
        this.mSomcMediaRecorder.setVideoEncodingBitRate(videoEncodingBitRate);
    }
    
    public void setVideoEncodingProfileLevel(final int n, final int n2) {
        this.mSomcMediaRecorder.setVideoEncodingProfileLevel(n, n2);
    }
    
    public void setVideoFrameRate(final int videoFrameRate) throws IllegalStateException {
        this.mSomcMediaRecorder.setVideoFrameRate(videoFrameRate);
    }
    
    public void setVideoSize(final int n, final int n2) throws IllegalStateException {
        this.mSomcMediaRecorder.setVideoSize(n, n2);
    }
    
    public void setVideoSource(final int videoSource) throws IllegalStateException {
        this.mSomcMediaRecorder.setVideoSource(videoSource);
    }
    
    public void start() throws IllegalStateException {
        this.mSomcMediaRecorder.start();
    }
    
    public void stop() throws IllegalStateException {
        try {
            this.mSomcMediaRecorder.stop();
        }
        finally {
            this.mIsAvailable = false;
        }
    }
    
    public void stopAsync() throws IllegalStateException {
        this.mSomcMediaRecorder.stopAsync();
    }
    
    public void stopAudioRecording() {
        this.mSomcMediaRecorder.stopAudioRecording();
    }
    
    public void stopOnError() {
        try {
            this.mSomcMediaRecorder.stopOnCameraError();
        }
        finally {
            this.mIsAvailable = false;
        }
    }
    
    public void useIntelligentActive(final boolean b) {
        this.mSomcMediaRecorder.useIntelligentActive(b);
    }
    
    public void waitUntilStopCompleted() throws IllegalStateException {
        try {
            this.mSomcMediaRecorder.waitUntilStopCompleted();
        }
        finally {
            this.mIsAvailable = false;
        }
    }
    
    public static final class AudioEncoder
    {
        public static final int AAC = 3;
        public static final int AAC_ELD = 5;
        public static final int AMR_NB = 1;
        public static final int AMR_WB = 2;
        public static final int DEFAULT = 0;
        public static final int HE_AAC = 4;
        public static final int VORBIS = 6;
        
        private AudioEncoder() {
        }
    }
    
    public static final class AudioSource
    {
        public static final int AUDIO_SOURCE_INVALID = -1;
        public static final int CAMCORDER = 5;
        public static final int DEFAULT = 0;
        public static final int HOTWORD = 1999;
        public static final int MIC = 1;
        public static final int RADIO_TUNER = 1998;
        public static final int REMOTE_SUBMIX = 8;
        public static final int VOICE_CALL = 4;
        public static final int VOICE_COMMUNICATION = 7;
        public static final int VOICE_DOWNLINK = 3;
        public static final int VOICE_RECOGNITION = 6;
        public static final int VOICE_UPLINK = 2;
        
        private AudioSource() {
        }
    }
    
    private class CallbackHandler extends Handler
    {
        private final WeakReference<MediaRecorder> mRecorder;
        final MediaRecorder this$0;
        
        public CallbackHandler(final MediaRecorder this$0, final WeakReference<MediaRecorder> mRecorder, final Looper looper) {
            this.this$0 = this$0;
            super(looper);
            this.mRecorder = mRecorder;
        }
        
        public void handleMessage(final Message message) {
            final int n = message.arg1 & 0xFFFFFFF;
            Label_0380: {
                switch (message.what) {
                    case 4: {
                        if (this.this$0.mOnErrorListener == null) {
                            break;
                        }
                        if (this.this$0.mIsAvailable) {
                            this.this$0.mOnErrorListener.onError((android.media.MediaRecorder)null, 1, 0);
                            break;
                        }
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Message is rejected because recorder is already stopped. what:");
                        sb.append(message.what);
                        sb.append(" message:");
                        sb.append(n);
                        Log.w("MediaRecorder", sb.toString());
                        break;
                    }
                    case 3: {
                        if (this.this$0.mOnInfoListener != null) {
                            this.this$0.mOnInfoListener.onInfo((android.media.MediaRecorder)null, 801, 0);
                            break;
                        }
                        break;
                    }
                    case 2: {
                        if (this.this$0.mOnInfoListener != null) {
                            this.this$0.mOnInfoListener.onInfo((android.media.MediaRecorder)null, 800, 0);
                            break;
                        }
                        break;
                    }
                    case 1: {
                        if (n != 4) {
                            switch (n) {
                                default: {
                                    break Label_0380;
                                }
                                case 1001: {
                                    if (this.this$0.mOnInfoListener != null) {
                                        this.this$0.mOnInfoListener.onInfo((android.media.MediaRecorder)null, message.arg1, message.arg2);
                                        break Label_0380;
                                    }
                                    break Label_0380;
                                }
                                case 1000: {
                                    if (this.this$0.mOnInfoListener != null) {
                                        this.this$0.mOnInfoListener.onInfo((android.media.MediaRecorder)null, message.arg1, message.arg2);
                                        break Label_0380;
                                    }
                                    break Label_0380;
                                }
                            }
                        }
                        else {
                            if (this.this$0.mOnErrorListener == null) {
                                break;
                            }
                            if (this.this$0.mIsAvailable) {
                                this.this$0.mOnErrorListener.onError((android.media.MediaRecorder)null, 1, 0);
                                break;
                            }
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("Message is rejected because recorder is already stopped. what:");
                            sb2.append(message.what);
                            sb2.append(" message:");
                            sb2.append(n);
                            Log.w("MediaRecorder", sb2.toString());
                            break;
                        }
                        break;
                    }
                }
            }
        }
    }
    
    public static final class OutputFormat
    {
        public static final int AAC_ADIF = 5;
        public static final int AAC_ADTS = 6;
        public static final int AMR_NB = 3;
        public static final int AMR_WB = 3;
        public static final int DEFAULT = 0;
        public static final int MPEG_4 = 2;
        public static final int OUTPUT_FORMAT_MPEG2TS = 8;
        public static final int OUTPUT_FORMAT_RTP_AVP = 7;
        public static final int RAW_AMR = 3;
        public static final int THREE_GPP = 1;
        public static final int WEBM = 9;
        
        private OutputFormat() {
        }
    }
    
    public static final class VideoEncoder
    {
        public static final int DEFAULT = 0;
        public static final int H263 = 1;
        public static final int H264 = 2;
        public static final int HEVC = 5;
        public static final int MPEG_4_SP = 3;
        public static final int VP8 = 4;
        
        private VideoEncoder() {
        }
    }
    
    public static final class VideoSource
    {
        public static final int CAMERA = 1;
        public static final int DEFAULT = 0;
        public static final int SURFACE = 2;
        
        private VideoSource() {
        }
    }
}
