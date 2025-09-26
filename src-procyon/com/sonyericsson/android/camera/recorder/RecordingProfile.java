// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.recorder;

import android.graphics.Rect;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import android.media.CamcorderProfile;

public class RecordingProfile
{
    public static final int MIN_RECORDING_DURATION = 1;
    public static final int QUALITY_4K_UHD_30FPS = 100;
    public static final int VIDEO_AUDIO_BIT_RATE = 128000;
    public static final int VIDEO_AUDIO_BIT_RATE_AAC = 156000;
    public static final int VIDEO_AUDIO_BIT_RATE_AMR_NB = 12200;
    public static final int VIDEO_AUDIO_BIT_RATE_MMS = 5000;
    public static final int VIDEO_AUDIO_CHANNEL_MONO = 1;
    public static final int VIDEO_AUDIO_CHANNEL_STEREO = 2;
    public static final int VIDEO_AUDIO_SAMPLE_RATE = 48000;
    public static final int VIDEO_AUDIO_SAMPLE_RATE_MMS = 8000;
    public static final int VIDEO_BIT_RATE_4K_UHD_AVC_30FPS = 55000000;
    public static final int VIDEO_BIT_RATE_4K_UHD_HEVC_30FPS = 35000000;
    public static final int VIDEO_BIT_RATE_FULL_HD = 17500000;
    public static final int VIDEO_BIT_RATE_FULL_HD_60FPS = 30000000;
    public static final int VIDEO_BIT_RATE_HD = 12000000;
    public static final int VIDEO_BIT_RATE_HD_120FPS = 50000000;
    public static final int VIDEO_BIT_RATE_LOW = 210000;
    public static final int VIDEO_BIT_RATE_VGA = 3555555;
    public static final int VIDEO_FRAME_RATE_24FPS = 24;
    public static final int VIDEO_FRAME_RATE_30FPS = 30;
    public static final int VIDEO_FRAME_RATE_60FPS = 60;
    public static final int VIDEO_FRAME_RATE_HD_120FPS = 120;
    public static final int VIDEO_FRAME_RATE_MMS = 15;
    public final long averageFileSize;
    public final CamcorderProfile camcorderProfile;
    public final RecorderParameters.DataSpace dataSpace;
    public final String extension;
    public final boolean isMms;
    public final String mimeType;
    public final long minFileSize;
    public final int progressInterval;
    
    private RecordingProfile(final CamcorderProfile camcorderProfile, final String extension, final String mimeType, final long averageFileSize, final long minFileSize, final int progressInterval, final RecorderParameters.DataSpace dataSpace, final boolean isMms) {
        this.camcorderProfile = camcorderProfile;
        this.extension = extension;
        this.mimeType = mimeType;
        this.averageFileSize = averageFileSize;
        this.minFileSize = minFileSize;
        this.progressInterval = progressInterval;
        this.dataSpace = dataSpace;
        this.isMms = isMms;
    }
    
    private static long computeSize(final long n, final long n2, final long n3) {
        return (n + n2) * n3 / 8L / 1024L;
    }
    
    private static int decideDefaultQuality(final VideoSize videoSize) {
        if (videoSize != VideoSize.MMS) {
            return 1;
        }
        return 0;
    }
    
    private static int decideFrameRate(final VideoSize videoSize, final VideoHdr videoHdr) {
        if (videoSize == null) {
            throw new IllegalStateException("Don't set parameters.");
        }
        final int n = RecordingProfile$1.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$VideoSize[videoSize.ordinal()];
        int n2 = 30;
        switch (n) {
            default: {
                throw new IllegalStateException("Don't set parameters.");
            }
            case 1:
            case 2:
            case 3: {
                n2 = n2;
                if (videoHdr == VideoHdr.HDR_ON) {
                    n2 = 24;
                    return n2;
                }
                return n2;
            }
            case 5:
            case 7: {
                return n2;
            }
            case 8: {
                n2 = 15;
                return n2;
            }
            case 6: {
                n2 = 120;
                return n2;
            }
            case 4: {
                n2 = 60;
                return n2;
            }
        }
    }
    
    private static int decideQuality(final VideoSize videoSize, final boolean b) {
        if (videoSize == null) {
            throw new IllegalStateException("Don't set parameters.");
        }
        int n = 0;
        switch (RecordingProfile$1.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$VideoSize[videoSize.ordinal()]) {
            default: {
                throw new IllegalStateException("Don't set parameters.");
            }
            case 8: {
                if (b) {
                    n = 2;
                    break;
                }
                n = 0;
                break;
            }
            case 7: {
                n = 4;
                break;
            }
            case 5:
            case 6: {
                n = 5;
                break;
            }
            case 3:
            case 4: {
                n = 6;
                break;
            }
            case 1:
            case 2: {
                n = 100;
                break;
            }
        }
        if (CamcorderProfile.hasProfile(n)) {
            return n;
        }
        return decideDefaultQuality(videoSize);
    }
    
    private static int getAudioBitRate(final CamcorderProfile camcorderProfile, final int n) {
        if (camcorderProfile != null) {
            return camcorderProfile.audioBitRate;
        }
        if (n == 0) {
            return 5000;
        }
        return 128000;
    }
    
    public static int getVideoFrameRate(final VideoSize videoSize, final VideoHdr videoHdr) {
        return decideFrameRate(videoSize, videoHdr);
    }
    
    public CamcorderProfile getCamcorderProfile() {
        return this.camcorderProfile;
    }
    
    public String getExtension() {
        return this.extension;
    }
    
    public String getMime() {
        return this.mimeType;
    }
    
    public int getProgressInterval() {
        return this.progressInterval;
    }
    
    public static class Builder
    {
        private boolean mIsOneShot;
        private String mResultExt;
        private String mResultMimeType;
        private VideoHdr mVideoHdr;
        private VideoSize mVideoSize;
        
        private void setupOutputFormatWithQuality(final int n) {
            if (n != 0 && n != 2) {
                this.mResultExt = ".mp4";
                this.mResultMimeType = "video/mp4";
            }
            else {
                this.mResultExt = ".3gp";
                this.mResultMimeType = "video/3gpp";
            }
        }
        
        private void updateCamcorderProfile(final CamcorderProfile camcorderProfile) {
            if (camcorderProfile == null) {
                return;
            }
            camcorderProfile.videoFrameRate = decideFrameRate(this.mVideoSize, this.mVideoHdr);
            camcorderProfile.fileFormat = 2;
            camcorderProfile.videoCodec = 2;
            switch (RecordingProfile$1.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$VideoSize[this.mVideoSize.ordinal()]) {
                case 8: {
                    camcorderProfile.videoBitRate = 210000;
                    camcorderProfile.audioCodec = 1;
                    camcorderProfile.audioBitRate = 12200;
                    camcorderProfile.audioSampleRate = 8000;
                    camcorderProfile.audioChannels = 1;
                    break;
                }
                case 7: {
                    camcorderProfile.videoBitRate = 3555555;
                    camcorderProfile.audioCodec = 3;
                    camcorderProfile.audioBitRate = 156000;
                    camcorderProfile.audioSampleRate = 48000;
                    camcorderProfile.audioChannels = 2;
                    break;
                }
                case 5:
                case 6: {
                    if (this.mVideoSize == VideoSize.HD_120FPS) {
                        camcorderProfile.videoBitRate = 50000000;
                    }
                    else {
                        camcorderProfile.videoBitRate = 12000000;
                    }
                    camcorderProfile.audioCodec = 3;
                    camcorderProfile.audioBitRate = 156000;
                    camcorderProfile.audioSampleRate = 48000;
                    camcorderProfile.audioChannels = 2;
                    break;
                }
                case 3:
                case 4: {
                    if (this.mVideoSize == VideoSize.FULL_HD_60FPS) {
                        camcorderProfile.videoBitRate = 30000000;
                    }
                    else {
                        camcorderProfile.videoBitRate = 17500000;
                        if (this.mVideoHdr == VideoHdr.HDR_ON) {
                            camcorderProfile.videoCodec = 5;
                        }
                    }
                    camcorderProfile.audioCodec = 3;
                    camcorderProfile.audioBitRate = 156000;
                    camcorderProfile.audioSampleRate = 48000;
                    camcorderProfile.audioChannels = 2;
                    break;
                }
                case 1:
                case 2: {
                    if (this.mVideoSize == VideoSize.FOUR_K_UHD_H264) {
                        camcorderProfile.videoBitRate = 55000000;
                    }
                    else {
                        camcorderProfile.videoBitRate = 35000000;
                        camcorderProfile.videoCodec = 5;
                    }
                    camcorderProfile.audioCodec = 3;
                    camcorderProfile.audioBitRate = 156000;
                    camcorderProfile.audioSampleRate = 48000;
                    camcorderProfile.audioChannels = 2;
                    break;
                }
            }
            final Rect videoRect = this.mVideoSize.getVideoRect();
            if (videoRect.width() != camcorderProfile.videoFrameWidth || videoRect.height() != camcorderProfile.videoFrameHeight) {
                camcorderProfile.videoFrameWidth = videoRect.width();
                camcorderProfile.videoFrameHeight = videoRect.height();
            }
        }
        
        public RecordingProfile build() {
            if (this.mVideoSize == VideoSize.FOUR_K_UHD_H264 && this.mVideoHdr == VideoHdr.HDR_ON) {
                this.mVideoSize = VideoSize.FOUR_K_UHD_H265;
            }
            final int access$000 = decideQuality(this.mVideoSize, this.mIsOneShot);
            final CamcorderProfile value = CamcorderProfile.get(access$000);
            if (value != null) {
                this.updateCamcorderProfile(value);
                if (!this.mIsOneShot) {
                    this.setupOutputFormatWithQuality(access$000);
                }
                else {
                    this.mResultExt = ".mp4";
                    this.mResultMimeType = "video/mp4";
                }
                int n = 1000;
                if (this.mVideoSize == VideoSize.MMS) {
                    n = 100;
                }
                final long n2 = getAudioBitRate(value, decideDefaultQuality(this.mVideoSize));
                final long access$2 = computeSize(n2, value.videoBitRate, 60L);
                final long access$3 = computeSize(n2, value.videoBitRate, 1L);
                int n3;
                int n4;
                int n5;
                if (this.mVideoHdr == VideoHdr.HDR_ON) {
                    n3 = 6;
                    n4 = 7;
                    n5 = 2;
                }
                else {
                    n3 = 0;
                    n4 = (n5 = 0);
                }
                return new RecordingProfile(value, this.mResultExt, this.mResultMimeType, access$2, access$3, n, new RecorderParameters.DataSpace(n3, n4, n5), this.mVideoSize == VideoSize.MMS, null);
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Could not get profile. Because Camera.getNumberOfCameras() returns 0 or No CamcorderProfile that matches quality : ");
            sb.append(access$000);
            throw new RuntimeException(sb.toString());
        }
        
        public Builder setOneShot(final boolean mIsOneShot) {
            this.mIsOneShot = mIsOneShot;
            return this;
        }
        
        public Builder videoHdr(final VideoHdr mVideoHdr) {
            this.mVideoHdr = mVideoHdr;
            return this;
        }
        
        public Builder videoSize(final VideoSize mVideoSize) {
            this.mVideoSize = mVideoSize;
            return this;
        }
    }
}
