package com.sonyericsson.android.camera.recorder.utility.encoder;

import android.media.MediaCodec;
import android.media.MediaFormat;
import com.sonyericsson.android.camera.util.CamLog;
import java.nio.ByteBuffer;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class EncodedDataWriteTask implements Runnable {
    private static final long OUTPUTBUFFER_TIMEOUT_NANOSECONDS = 100000000;
    public static final String TAG = "EncodedDataWriteTask";
    private static boolean TRACE = false;
    private final MediaCodec mCodec;
    private final EncoderStateListener mListener;
    private final MediaMuxerWrapper mMuxer;
    private int mMuxerIndex;
    private final String mName;

    public interface EncoderStateListener {
        void onEncoderFinished();

        void onEncoderFormatChanged(MediaFormat mediaFormat);
    }

    public EncodedDataWriteTask(MediaMuxerWrapper mediaMuxerWrapper, MediaCodec mediaCodec, EncoderStateListener encoderStateListener, String str) {
        this.mName = str;
        this.mMuxer = mediaMuxerWrapper;
        this.mCodec = mediaCodec;
        this.mListener = encoderStateListener;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (awaitEncoderFormat()) {
            ByteBuffer[] outputBuffers = this.mCodec.getOutputBuffers();
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            MediaFormat outputFormat = this.mCodec.getOutputFormat();
            synchronized (this.mMuxer) {
                if (TRACE) {
                    CamLog.d(this.mName + " ADD TRACK (" + outputFormat + "): E");
                }
                this.mMuxerIndex = this.mMuxer.addTrack(outputFormat);
                if (TRACE) {
                    CamLog.d(this.mName + " ADD TRACK (" + outputFormat + "): X");
                }
            }
            if (this.mListener != null) {
                this.mListener.onEncoderFormatChanged(outputFormat);
            }
            while (true) {
                if (canceled()) {
                    break;
                }
                try {
                    int iDequeueOutputBuffer = this.mCodec.dequeueOutputBuffer(bufferInfo, OUTPUTBUFFER_TIMEOUT_NANOSECONDS);
                    if (iDequeueOutputBuffer == -1) {
                        if (TRACE) {
                            CamLog.d(this.mName + " INFO_TRY_AGAIN_LATER");
                        }
                        if (canceled()) {
                            if (TRACE) {
                                CamLog.d(this.mName + " CANCELED");
                            }
                        }
                    } else if (iDequeueOutputBuffer >= 0) {
                        if (canceled()) {
                            bufferInfo.flags |= 4;
                        }
                        if (TRACE) {
                            CamLog.d(this.mName + " PULL SAMPLE DATA presentationTime:" + bufferInfo.presentationTimeUs + " flag:" + bufferInfo.flags);
                        }
                        ByteBuffer byteBuffer = outputBuffers[iDequeueOutputBuffer];
                        if ((bufferInfo.flags & 2) == 0) {
                            synchronized (this.mMuxer) {
                                this.mMuxer.writeSampleData(this.mMuxerIndex, byteBuffer, bufferInfo);
                            }
                        }
                        this.mCodec.releaseOutputBuffer(iDequeueOutputBuffer, false);
                        if (canceled()) {
                            if (TRACE) {
                                CamLog.d(this.mName + " CANCELED");
                            }
                        } else if ((bufferInfo.flags & 4) == 4) {
                            if (TRACE) {
                                CamLog.d(this.mName + " BUFFER_FLAG_END_OF_STREAM");
                            }
                        }
                    } else if (iDequeueOutputBuffer == -3) {
                        if (TRACE) {
                            CamLog.d(this.mName + " INFO_OUTPUT_BUFFERS_CHANGED");
                        }
                        outputBuffers = this.mCodec.getOutputBuffers();
                    } else if (iDequeueOutputBuffer == -2 && TRACE) {
                        CamLog.d(this.mName + " INFO_OUTPUT_FORMAT_CHANGED");
                    }
                } catch (IllegalStateException e) {
                    CamLog.e(e + " occurred. Maybe camera server is dead.", e);
                }
            }
            if (this.mListener != null) {
                this.mListener.onEncoderFinished();
            }
        }
    }

    private boolean awaitEncoderFormat() {
        if (TRACE) {
            CamLog.d(this.mName + " awaitEncoderFormat E");
        }
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        while (!canceled()) {
            int iDequeueOutputBuffer = this.mCodec.dequeueOutputBuffer(bufferInfo, OUTPUTBUFFER_TIMEOUT_NANOSECONDS);
            if (iDequeueOutputBuffer == -2) {
                if (TRACE) {
                    CamLog.d(this.mName + " INFO_OUTPUT_FORMAT_CHANGED");
                }
                return true;
            }
            if (TRACE) {
                CamLog.d(this.mName + " INFO_OUTPUT:" + iDequeueOutputBuffer);
            }
        }
        if (TRACE) {
            CamLog.d(this.mName + " awaitEncoderFormat X");
        }
        return false;
    }

    private static boolean canceled() {
        return Thread.currentThread().isInterrupted();
    }
}
