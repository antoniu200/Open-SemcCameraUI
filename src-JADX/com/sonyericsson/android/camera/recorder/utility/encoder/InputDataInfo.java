package com.sonyericsson.android.camera.recorder.utility.encoder;

import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.view.Surface;
import java.io.IOException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class InputDataInfo {
    public final MediaCodec codec;
    public final MediaFormat codecFormat;
    public final InputDataSource source;

    private InputDataInfo(MediaFormat mediaFormat, MediaCodec mediaCodec, InputDataSource inputDataSource) {
        this.codecFormat = mediaFormat;
        this.codec = mediaCodec;
        this.source = inputDataSource;
    }

    public String mimeType() {
        return this.codecFormat.getString("mime");
    }

    public static InputDataInfo create(MediaFormat mediaFormat, MediaCodec mediaCodec, InputDataSource inputDataSource) {
        return new InputDataInfo(mediaFormat, mediaCodec, inputDataSource);
    }

    public static InputDataInfo create(MediaFormat mediaFormat, InputDataSource inputDataSource) throws IOException {
        MediaCodec mediaCodecCreateEncoderByType = MediaCodec.createEncoderByType(mediaFormat.getString("mime"));
        mediaCodecCreateEncoderByType.configure(mediaFormat, (Surface) null, (MediaCrypto) null, 1);
        return create(mediaFormat, mediaCodecCreateEncoderByType, inputDataSource);
    }
}
