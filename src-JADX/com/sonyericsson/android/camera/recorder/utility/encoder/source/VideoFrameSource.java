package com.sonyericsson.android.camera.recorder.utility.encoder.source;

import android.media.MediaCodec;
import android.view.Surface;
import com.sonyericsson.android.camera.recorder.utility.encoder.InputDataSource;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class VideoFrameSource implements InputDataSource {
    private final MediaCodec mEncoder;

    @Override // com.sonyericsson.android.camera.recorder.utility.encoder.InputDataSource
    public void release() {
    }

    @Override // com.sonyericsson.android.camera.recorder.utility.encoder.InputDataSource
    public void start() {
    }

    public VideoFrameSource(MediaCodec mediaCodec) {
        this.mEncoder = mediaCodec;
    }

    public Surface createInputSurface() {
        return this.mEncoder.createInputSurface();
    }

    @Override // com.sonyericsson.android.camera.recorder.utility.encoder.InputDataSource
    public void stop() {
        this.mEncoder.signalEndOfInputStream();
    }
}
