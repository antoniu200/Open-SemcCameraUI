// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.recorder.utility.encoder;

import java.io.IOException;
import android.media.MediaCrypto;
import android.view.Surface;
import android.media.MediaFormat;
import android.media.MediaCodec;

public class InputDataInfo
{
    public final MediaCodec codec;
    public final MediaFormat codecFormat;
    public final InputDataSource source;
    
    private InputDataInfo(final MediaFormat codecFormat, final MediaCodec codec, final InputDataSource source) {
        this.codecFormat = codecFormat;
        this.codec = codec;
        this.source = source;
    }
    
    public static InputDataInfo create(final MediaFormat mediaFormat, final MediaCodec mediaCodec, final InputDataSource inputDataSource) {
        return new InputDataInfo(mediaFormat, mediaCodec, inputDataSource);
    }
    
    public static InputDataInfo create(final MediaFormat mediaFormat, final InputDataSource inputDataSource) throws IOException {
        final MediaCodec encoderByType = MediaCodec.createEncoderByType(mediaFormat.getString("mime"));
        encoderByType.configure(mediaFormat, (Surface)null, (MediaCrypto)null, 1);
        return create(mediaFormat, encoderByType, inputDataSource);
    }
    
    public String mimeType() {
        return this.codecFormat.getString("mime");
    }
}
