// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.recorder.utility.encoder.source;

import android.view.Surface;
import android.media.MediaCodec;
import com.sonyericsson.android.camera.recorder.utility.encoder.InputDataSource;

public class VideoFrameSource implements InputDataSource
{
    private final MediaCodec mEncoder;
    
    public VideoFrameSource(final MediaCodec mEncoder) {
        this.mEncoder = mEncoder;
    }
    
    public Surface createInputSurface() {
        return this.mEncoder.createInputSurface();
    }
    
    @Override
    public void release() {
    }
    
    @Override
    public void start() {
    }
    
    @Override
    public void stop() {
        this.mEncoder.signalEndOfInputStream();
    }
}
