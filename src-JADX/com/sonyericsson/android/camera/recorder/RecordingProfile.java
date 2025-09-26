package com.sonyericsson.android.camera.recorder;

import android.graphics.Rect;
import android.media.CamcorderProfile;
import android.support.v4.media.session.PlaybackStateCompat;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.recorder.RecorderParameters;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingConstants;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class RecordingProfile {
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

    private RecordingProfile(CamcorderProfile camcorderProfile, String str, String str2, long j, long j2, int i, RecorderParameters.DataSpace dataSpace, boolean z) {
        this.camcorderProfile = camcorderProfile;
        this.extension = str;
        this.mimeType = str2;
        this.averageFileSize = j;
        this.minFileSize = j2;
        this.progressInterval = i;
        this.dataSpace = dataSpace;
        this.isMms = z;
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

    public static int getVideoFrameRate(VideoSize videoSize, VideoHdr videoHdr) {
        return decideFrameRate(videoSize, videoHdr);
    }

    public static class Builder {
        private boolean mIsOneShot;
        private String mResultExt;
        private String mResultMimeType;
        private VideoHdr mVideoHdr;
        private VideoSize mVideoSize;

        public Builder videoSize(VideoSize videoSize) {
            this.mVideoSize = videoSize;
            return this;
        }

        public Builder videoHdr(VideoHdr videoHdr) {
            this.mVideoHdr = videoHdr;
            return this;
        }

        public Builder setOneShot(boolean z) {
            this.mIsOneShot = z;
            return this;
        }

        public RecordingProfile build() {
            int i;
            int i2;
            int i3;
            if (this.mVideoSize == VideoSize.FOUR_K_UHD_H264 && this.mVideoHdr == VideoHdr.HDR_ON) {
                this.mVideoSize = VideoSize.FOUR_K_UHD_H265;
            }
            int iDecideQuality = RecordingProfile.decideQuality(this.mVideoSize, this.mIsOneShot);
            CamcorderProfile camcorderProfile = CamcorderProfile.get(iDecideQuality);
            if (camcorderProfile != null) {
                updateCamcorderProfile(camcorderProfile);
                if (!this.mIsOneShot) {
                    setupOutputFormatWithQuality(iDecideQuality);
                } else {
                    this.mResultExt = MediaSavingConstants.MEDIA_TYPE_MPEG4_EXT;
                    this.mResultMimeType = MediaSavingConstants.MEDIA_TYPE_MPEG4_MIME;
                }
                int i4 = this.mVideoSize == VideoSize.MMS ? 100 : 1000;
                long audioBitRate = RecordingProfile.getAudioBitRate(camcorderProfile, RecordingProfile.decideDefaultQuality(this.mVideoSize));
                long jComputeSize = RecordingProfile.computeSize(audioBitRate, camcorderProfile.videoBitRate, 60L);
                long jComputeSize2 = RecordingProfile.computeSize(audioBitRate, camcorderProfile.videoBitRate, 1L);
                if (this.mVideoHdr == VideoHdr.HDR_ON) {
                    i = 6;
                    i2 = 7;
                    i3 = 2;
                } else {
                    i = 0;
                    i2 = 0;
                    i3 = 0;
                }
                return new RecordingProfile(camcorderProfile, this.mResultExt, this.mResultMimeType, jComputeSize, jComputeSize2, i4, new RecorderParameters.DataSpace(i, i2, i3), this.mVideoSize == VideoSize.MMS);
            }
            throw new RuntimeException("Could not get profile. Because Camera.getNumberOfCameras() returns 0 or No CamcorderProfile that matches quality : " + iDecideQuality);
        }

        private void setupOutputFormatWithQuality(int i) {
            if (i == 0 || i == 2) {
                this.mResultExt = MediaSavingConstants.MEDIA_TYPE_3GP_EXT;
                this.mResultMimeType = MediaSavingConstants.MEDIA_TYPE_3GP_MIME;
            } else {
                this.mResultExt = MediaSavingConstants.MEDIA_TYPE_MPEG4_EXT;
                this.mResultMimeType = MediaSavingConstants.MEDIA_TYPE_MPEG4_MIME;
            }
        }

        private void updateCamcorderProfile(CamcorderProfile camcorderProfile) {
            if (camcorderProfile == null) {
                return;
            }
            camcorderProfile.videoFrameRate = RecordingProfile.decideFrameRate(this.mVideoSize, this.mVideoHdr);
            camcorderProfile.fileFormat = 2;
            camcorderProfile.videoCodec = 2;
            switch (this.mVideoSize) {
                case FOUR_K_UHD_H264:
                case FOUR_K_UHD_H265:
                    if (this.mVideoSize == VideoSize.FOUR_K_UHD_H264) {
                        camcorderProfile.videoBitRate = RecordingProfile.VIDEO_BIT_RATE_4K_UHD_AVC_30FPS;
                    } else {
                        camcorderProfile.videoBitRate = RecordingProfile.VIDEO_BIT_RATE_4K_UHD_HEVC_30FPS;
                        camcorderProfile.videoCodec = 5;
                    }
                    camcorderProfile.audioCodec = 3;
                    camcorderProfile.audioBitRate = RecordingProfile.VIDEO_AUDIO_BIT_RATE_AAC;
                    camcorderProfile.audioSampleRate = RecordingProfile.VIDEO_AUDIO_SAMPLE_RATE;
                    camcorderProfile.audioChannels = 2;
                    break;
                case FULL_HD:
                case FULL_HD_60FPS:
                    if (this.mVideoSize == VideoSize.FULL_HD_60FPS) {
                        camcorderProfile.videoBitRate = RecordingProfile.VIDEO_BIT_RATE_FULL_HD_60FPS;
                    } else {
                        camcorderProfile.videoBitRate = RecordingProfile.VIDEO_BIT_RATE_FULL_HD;
                        if (this.mVideoHdr == VideoHdr.HDR_ON) {
                            camcorderProfile.videoCodec = 5;
                        }
                    }
                    camcorderProfile.audioCodec = 3;
                    camcorderProfile.audioBitRate = RecordingProfile.VIDEO_AUDIO_BIT_RATE_AAC;
                    camcorderProfile.audioSampleRate = RecordingProfile.VIDEO_AUDIO_SAMPLE_RATE;
                    camcorderProfile.audioChannels = 2;
                    break;
                case HD:
                case HD_120FPS:
                    if (this.mVideoSize == VideoSize.HD_120FPS) {
                        camcorderProfile.videoBitRate = RecordingProfile.VIDEO_BIT_RATE_HD_120FPS;
                    } else {
                        camcorderProfile.videoBitRate = RecordingProfile.VIDEO_BIT_RATE_HD;
                    }
                    camcorderProfile.audioCodec = 3;
                    camcorderProfile.audioBitRate = RecordingProfile.VIDEO_AUDIO_BIT_RATE_AAC;
                    camcorderProfile.audioSampleRate = RecordingProfile.VIDEO_AUDIO_SAMPLE_RATE;
                    camcorderProfile.audioChannels = 2;
                    break;
                case VGA:
                    camcorderProfile.videoBitRate = RecordingProfile.VIDEO_BIT_RATE_VGA;
                    camcorderProfile.audioCodec = 3;
                    camcorderProfile.audioBitRate = RecordingProfile.VIDEO_AUDIO_BIT_RATE_AAC;
                    camcorderProfile.audioSampleRate = RecordingProfile.VIDEO_AUDIO_SAMPLE_RATE;
                    camcorderProfile.audioChannels = 2;
                    break;
                case MMS:
                    camcorderProfile.videoBitRate = RecordingProfile.VIDEO_BIT_RATE_LOW;
                    camcorderProfile.audioCodec = 1;
                    camcorderProfile.audioBitRate = RecordingProfile.VIDEO_AUDIO_BIT_RATE_AMR_NB;
                    camcorderProfile.audioSampleRate = RecordingProfile.VIDEO_AUDIO_SAMPLE_RATE_MMS;
                    camcorderProfile.audioChannels = 1;
                    break;
            }
            Rect videoRect = this.mVideoSize.getVideoRect();
            if (videoRect.width() == camcorderProfile.videoFrameWidth && videoRect.height() == camcorderProfile.videoFrameHeight) {
                return;
            }
            camcorderProfile.videoFrameWidth = videoRect.width();
            camcorderProfile.videoFrameHeight = videoRect.height();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long computeSize(long j, long j2, long j3) {
        return (((j + j2) * j3) / 8) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getAudioBitRate(CamcorderProfile camcorderProfile, int i) {
        if (camcorderProfile != null) {
            return camcorderProfile.audioBitRate;
        }
        return i == 0 ? VIDEO_AUDIO_BIT_RATE_MMS : VIDEO_AUDIO_BIT_RATE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int decideFrameRate(VideoSize videoSize, VideoHdr videoHdr) {
        if (videoSize == null) {
            throw new IllegalStateException("Don't set parameters.");
        }
        switch (videoSize) {
            case FOUR_K_UHD_H264:
            case FOUR_K_UHD_H265:
            case FULL_HD:
                return videoHdr == VideoHdr.HDR_ON ? 24 : 30;
            case FULL_HD_60FPS:
                return 60;
            case HD:
            case VGA:
                return 30;
            case HD_120FPS:
                return 120;
            case MMS:
                return 15;
            default:
                throw new IllegalStateException("Don't set parameters.");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int decideQuality(VideoSize videoSize, boolean z) {
        int i;
        if (videoSize == null) {
            throw new IllegalStateException("Don't set parameters.");
        }
        switch (videoSize) {
            case FOUR_K_UHD_H264:
            case FOUR_K_UHD_H265:
                i = 100;
                break;
            case FULL_HD:
            case FULL_HD_60FPS:
                i = 6;
                break;
            case HD:
            case HD_120FPS:
                i = 5;
                break;
            case VGA:
                i = 4;
                break;
            case MMS:
                if (!z) {
                    i = 0;
                    break;
                } else {
                    i = 2;
                    break;
                }
            default:
                throw new IllegalStateException("Don't set parameters.");
        }
        return CamcorderProfile.hasProfile(i) ? i : decideDefaultQuality(videoSize);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int decideDefaultQuality(VideoSize videoSize) {
        return videoSize != VideoSize.MMS ? 1 : 0;
    }
}
